package rayTracer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RayTracer extends JPanel {

	static Vector CAMERA_POSITION;
	//static final Vector CAMERA_POSITION = new Vector(3f,0f,10f);
	static final int MAX_RECURSION_DEPTH = 7;	
	static final double SHADOW_RAY_BIAS = 0.0001;
	static final Vector AMBIENT_LIGHT = new Vector(40f,40f,40f);
	static final Vector BACKGROUND_COLOR = new Vector(20f,0f,20f);
	static Matrix4 viewMatrix;

	static int hits = 0;

	static int helps = 0;

	//Recursive ray-casting function
	//Called for each pixel and each time a ray is reflected/used for shadow testing
	//@ray The ray we are casting
	//@payload Information on the current ray i.e. the cumulative color and the number of bounces it has performed
	//returns either the time of intersection with an object (the coefficient t in the equation: RayPosition = RayOrigin + t*RayDirection) or zero to indicate no intersection

	static double CastRay(Ray ray, Payload payload){

		if (payload.getNumBounces() > MAX_RECURSION_DEPTH) { //Return if max depth reached
			return 0f;
		}

		IntersectInfo info = new IntersectInfo();

		info.setTime(Double.POSITIVE_INFINITY); 

		boolean hit = false;

		// Sets info to the intersection info of nearest intersection
		for (MatObject o : objects) {
			boolean intersect = o.Intersect(ray, info);
			//System.out.println(o.name + " " + intersect);
			hit = hit | intersect;
		}

		if (hit){
			hits++;

			// Get colour of surface

			Vector ambient = AMBIENT_LIGHT.multComponents(info.getMaterial().ambient);

			Vector totalDiffuse = new Vector(0,0,0);
			Vector totalSpecular = new Vector(0,0,0);

			for (Light l: lights) {

				Vector objectToLight = l.Position().minus(info.getHitPoint());
				double timeToLight = objectToLight.length();
				objectToLight = objectToLight.normalize();
				Ray shadowRay = new Ray(info.getHitPoint().plus(info.getNormal().times(0.001)), objectToLight);
				IntersectInfo shadowRayInfo = new IntersectInfo();
				shadowRayInfo.setTime(Double.POSITIVE_INFINITY);

				for (MatObject obj : objects) {
					obj.Intersect(shadowRay, shadowRayInfo); // shadowRayInfo becomes info of nearest intersection, if any
				}

				if (shadowRayInfo.getTime()>=timeToLight) { // no intersections with objects occur between object and light

					// Diffuse illumination

					Vector normal = info.getNormal();
					double cosTheta = objectToLight.cosAngleBetween(normal);

					if (cosTheta == 0) {
						System.out.println("Help");
					}

					cosTheta = Math.max(0, cosTheta);
					Vector diffuse = l.getColor().multComponents(info.getMaterial().diffuse).times(cosTheta);

					totalDiffuse = totalDiffuse.plus(diffuse);

					// Specular illumination

					Vector objectToCamera = ray.direction.times(-1).normalize(); // objectToCamera is the reverse of the original camera ray

					Vector reflection = info.getNormal().times(2).times(normal.dotProduct(objectToLight)).minus(objectToLight);

					boolean b = true;

					b &= (reflection.d[0] == 0.0);
					b &= (reflection.d[1] == 0.0);
					b &= (reflection.d[2] == 0.0);

					if (b) {
						System.out.println("Error");
					}

					Ray reflectionRay = new Ray(info.getHitPoint(),reflection.normalize());

					Payload reflectionPayload = new Payload();
					reflectionPayload.setNumBounces(payload.numBounces+1);

					// Reflection ray is cast 
					CastRay(reflectionRay, reflectionPayload);

					double cosAngle = reflection.cosAngleBetween(objectToCamera);
					//System.out.println(cosAngle);
					cosAngle = Math.max(0, cosAngle);
					//System.out.println(cosAngle);
					cosAngle = Math.pow(cosAngle, info.getMaterial().specularExponent);
					//System.out.println(cosAngle);

					//Vector specular = l.getColor().multComponents(info.getMaterial().specular).times(cosAngle);
					Vector specular = (l.getColor().plus(reflectionPayload.Color)).multComponents(info.getMaterial().specular.times(cosAngle));

					totalSpecular = totalSpecular.plus(specular);

				}

			}


			Vector illumination = ambient.plus(totalDiffuse.plus(totalSpecular));

			//illumination = ambient;
			//illumination = totalDiffuse;
			//illumination = totalSpecular;

			payload.setColor(illumination);
			return info.getTime();
		}

		return 0.0f;
	}

	//Structure containing all scene objects
	static ArrayList<MatObject> objects = new ArrayList<MatObject>();
	static ArrayList<Light> lights = new ArrayList<Light>();


	public static void main(String args[]) {

		int scene = 2;

		// Switch statement to have multiple scene setups
		switch(scene) {

		case 0:
		{
			AABB aabb = new AABB(Material.TEST_RED, new Vector(0,0,0), new Vector(1,1,1));

			Vector camPos = new Vector(0.5,0.5,3);

			objects.add(aabb);
			lights.add(new Light(camPos,PlanetPixel.DIRECT_SUNLIGHT));

			CAMERA_POSITION = camPos;
			viewMatrix = Matrix4.lookAt(CAMERA_POSITION, aabb.centre(), new Vector(0,1,0));

			break;
		}

		case 1:
		{
			Sphere s = new Sphere(new Vector(0,0,0), Material.TEST_RED, 1);

			Vector camPos1 = new Vector(0,0,3);
			Vector ligPos1 = new Vector(2,0,3);

			objects.add(s);
			lights.add(new Light(ligPos1,PlanetPixel.DIRECT_SUNLIGHT));

			CAMERA_POSITION = camPos1;
			viewMatrix = Matrix4.lookAt(CAMERA_POSITION, s.Position(), new Vector(0,1,0));

			break;
		}
		case 2:
		{
			Vector p0 = new Vector(0,0,0);
			Vector p1 = new Vector(1,0,0);
			Vector p2 = p0.plus(new Vector(1,0,1).normalize());
			Vector p3 = new Vector(p2.x(),Math.sqrt(6)/3f,(p0.z()+p1.z()+p2.z())/3);	

			Vector centre = (p0.plus(p1).plus(p2).plus(p3)).divide(4);

			Triangle t0 = Triangle.revTriangle(p0,p1,p2, Material.TEST_RED);
			Triangle t1 = Triangle.revTriangle(p1,p0,p3, Material.TEST_GREEN);
			Triangle t2 = Triangle.revTriangle(p2,p1,p3, Material.TEST_BLUE);
			Triangle t3 = Triangle.revTriangle(p0,p2,p3, Material.TEST_YELLOW);

			Vector camPos2 = new Vector(0,0,3);
			Vector ligPos2 = camPos2;

			objects.add(t0);
			objects.add(t1);
			objects.add(t2);
			objects.add(t3);

			lights.add(new Light(ligPos2,PlanetPixel.DIRECT_SUNLIGHT));

			CAMERA_POSITION = camPos2;
			viewMatrix = Matrix4.lookAt(CAMERA_POSITION, centre, new Vector(0,1,0));

			break;
		}
		}



		int windowX = 640;
		int windowY = 480;
		//The window aspect ratio
		double aspectRatio = (double)windowX/(double)windowY;
		//The field of view of the camera.  This is 90 degrees because our imaginary image plane is 2 units high (-1->1) and 1 unit from the camera position
		double fov = 90.0f;
		//Value for adjusting the pixel position to account for the field of view
		double fovAdjust = (double) Math.tan(fov*0.5f *(Math.PI/180.0f));

		try {
			BufferedImage img = new BufferedImage(windowX, windowY, BufferedImage.TYPE_INT_ARGB);

			for (int column = 0; column < windowX; column++) {
				for (int row = 0; row < windowY; row++) {

					//Convert the pixel (Raster space coordinates: (0->ScreenWidth,0->ScreenHeight)) to NDC (Normalised Device Coordinates: (0->1,0->1))
					double pixelNormX = (column+0.5f)/windowX; //Add 0.5f to get centre of pixel
					double pixelNormY = (row+0.5f)/windowY;
					//Convert from NDC, (0->1,0->1), to Screen space (-1->1,-1->1).  These coordinates correspond to those used by OpenGL
					//Note coordinate (-1,1) in screen space corresponds to coordinate (0,0) in raster space i.e. column = 0, row = 0
					double pixelScreenX = 2.0f*pixelNormX - 1.0f;
					double pixelScreenY = 1.0f-2.0f*pixelNormY;

					//Account for Field of View			
					double pixelCameraX = pixelScreenX * fovAdjust;
					double pixelCameraY = pixelScreenY * fovAdjust;

					//Account for image aspect ratio
					pixelCameraX *= aspectRatio;

					//Put pixel into camera space (offset by 1 unit along camera facing direction i.e. negative z axis)
					Vector pixelCameraSpace = new Vector(pixelCameraX,pixelCameraY,-1.0,1.0);

					//ray comes from camera origin
					Vector rayOrigin = new Vector(0.0f,0.0f,0.0f,1.0f);

					//Transform from camera space to world space
					pixelCameraSpace = viewMatrix.times(pixelCameraSpace);
					//The origin of the ray we are casting
					rayOrigin = viewMatrix.times(rayOrigin);

					// Drop 4th dimension for rayOrigin and pixelCameraSpace
					rayOrigin = rayOrigin.dropDim();
					pixelCameraSpace = pixelCameraSpace.dropDim();

					//The direction the ray is travelling in
					Vector rayDirection = pixelCameraSpace.minus(rayOrigin).normalize();

					//Set up ray in world space
					Ray ray = new Ray(rayOrigin,rayDirection);

					//Structure for storing the information we get from casting the ray
					Payload payload = new Payload();

					payload.setNumBounces(0);

					Vector color = BACKGROUND_COLOR;
					
					//Cast our ray into the scene
					if(CastRay(ray,payload)>0.0){// > 0.0f indicates an intersection
						color = payload.getColor();
					}	

					for (int i = 0; i<3; i++) {
						color.d[i] = Util.clamp(color.d[i], 0, 255);
					}

					Color tempColor = new Color((int)color.d[0],(int)color.d[1],(int)color.d[2]);

					img.setRGB(column, row, tempColor.getRGB());

				}
			}

			File outputfile = new File("saved.png");
			ImageIO.write(img, "png", outputfile);

			Desktop dt = Desktop.getDesktop();
			dt.open(outputfile);

			System.out.println(hits);
			System.out.println(AABB.normals);

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}


