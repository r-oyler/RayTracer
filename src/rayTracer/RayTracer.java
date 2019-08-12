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

	// Static variables used in CastRay
	static int maxRecursionDepth;	
	static double shadowRayBias;
	static Vector ambientLight;
	//Structure containing all scene objects
	static ArrayList<MatObject> objects = new ArrayList<MatObject>();
	static ArrayList<Light> lights = new ArrayList<Light>();

	//Recursive ray-casting function
	//Called for each pixel and each time a ray is reflected/used for shadow testing
	//@ray The ray we are casting
	//@payload Information on the current ray i.e. the cumulative color and the number of bounces it has performed
	//returns either the time of intersection with an object (the coefficient t in the equation: RayPosition = RayOrigin + t*RayDirection) or zero to indicate no intersection

	public static void main(String args[]) {

		// Dimensions of image
		int windowX = 640;
		int windowY = 480;
		//The field of view of the camera.  This is 90 degrees because our imaginary image plane is 2 units high (-1->1) and 1 unit from the camera position
		double fov = 90.0f;

		// Settings for supersampling anti-aliasing
		int ssColumnMax = 1;
		int ssRowMax = 1;

		// Maximum depth that reflection/refraction rays are cast
		int maxRecursionDepth = 7;
		// Distance that shadow ray orgins are moved along the surface normal to prevent shadow acne
		double shadowRayBias = 0.0001; 
		// The ambient light that is cast on every object
		Vector ambientLight = new Vector(40f,40f,40f);
		// The color that is seen when a ray doesn't hit an object;
		Vector backgroundColor  = new Vector(20f,0f,20f);

		rayTrace(windowX, windowY, fov, ssColumnMax, ssRowMax, maxRecursionDepth, shadowRayBias, ambientLight, backgroundColor);

	}

	public static void rayTrace(int windowX, int windowY, double fov, int ssColumnMax, int ssRowMax, int maxRecursionDepth, double shadowRayBias, Vector ambientLight, Vector backgroundColor) {

		// Assign parameters to static variables
		RayTracer.maxRecursionDepth = maxRecursionDepth;
		RayTracer.shadowRayBias = shadowRayBias;
		RayTracer.ambientLight = ambientLight;

		long startTime = System.currentTimeMillis();

		// Transforms from world-space to view-space, assigned in scene setup
		Matrix4 viewMatrix = null;

		int scene = 6;

		// Switch statement to have multiple scene setups
		switch(scene) {

		case 0:
		{
			AABB aabb = new AABB(Material.RED, new Vector(0,0,0), new Vector(1,1,1));

			Vector camPos = new Vector(0.5,0.5,3);

			objects.add(aabb);
			lights.add(new Light(camPos,PlanetPixel.DIRECT_SUNLIGHT));

			viewMatrix = Matrix4.lookAt(camPos, aabb.centre(), new Vector(0,1,0));

			break;
		}

		case 1:
		{
			Sphere s = new Sphere(new Vector(0,0,0), Material.RED, 1);

			Vector camPos = new Vector(0,0,3);
			Vector ligPos = new Vector(2,0,3);

			objects.add(s);
			lights.add(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			viewMatrix = Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0));

			break;
		}
		case 2:
		{
			Vector p0 = new Vector(0,0,0);
			Vector p1 = new Vector(1,0,0);
			Vector p2 = p0.plus(new Vector(1,0,1).normalize());
			Vector p3 = new Vector(p2.x(),Math.sqrt(6)/3f,(p0.z()+p1.z()+p2.z())/3);	

			Vector centre = (p0.plus(p1).plus(p2).plus(p3)).divide(4);

			Triangle t0 = new Triangle(p0,p1,p2, Material.RED);
			Triangle t1 = new Triangle(p1,p0,p3, Material.GREEN);
			Triangle t2 = new Triangle(p2,p1,p3, Material.BLUE);
			Triangle t3 = new Triangle(p0,p2,p3, Material.YELLOW);

			Vector camPos = new Vector(0,0,3);
			Vector ligPos = camPos;

			objects.add(t0);
			objects.add(t1);
			objects.add(t2);
			objects.add(t3);

			lights.add(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			viewMatrix = Matrix4.lookAt(camPos, centre, new Vector(0,1,0));

			break;
		}

		case 3:{

			Vector p0 = new Vector(0,0,0);
			Vector p1 = new Vector(0,1,0);
			Vector p2 = new Vector(1,0,0);

			Triangle t = new Triangle(p0,p1,p2, Material.RED);

			Vector camPos = new Vector(0,0,1);
			Vector ligPos = camPos;

			objects.add(t);
			lights.add(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			viewMatrix = Matrix4.lookAt(camPos, t.centre(), new Vector(0,1,0));

			break;
		}

		case 4:{

			Vector p0 = new Vector(0,0,0);
			Vector n = new Vector(1,1,1);

			Disk d = new Disk(p0, Material.RED, n, 1);

			Vector camPos = new Vector(0,0,5);
			Vector ligPos = camPos;

			objects.add(d);
			lights.add(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			viewMatrix = Matrix4.lookAt(camPos, p0, new Vector(0,1,0));
			
			break;
		}

		case 5:{

			Plane p0 = new Plane(new Vector(0,0,-1.1), Material.RED, new Vector(0,0,1));
			Plane p1 = new Plane(new Vector(0,-1.1,0), Material.GREEN, new Vector(0,1,0));
			Plane p2 = new Plane(new Vector(-1.1,0,0), Material.BLUE, new Vector(1,0,0));

			Sphere s = new Sphere(new Vector(0,0,0), Material.MIRROR, 1);

			Vector camPos = new Vector(1,1,4);
			Vector ligPos = new Vector(0,5,0);

			objects.add(p0);
			objects.add(p1);
			objects.add(p2);
			objects.add(s);
			lights.add(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			viewMatrix = Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0));
			
			break;
		}

		case 6:{

			Sphere s = new Sphere(new Vector(0,0,0), Material.BLACK, 2);
			BufferedImage bI = null;
			try {
				bI = ImageIO.read(new File("checkerboard.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s.addTextureMap(bI);

			Vector camPos = new Vector(4,4,4);
			Vector ligPos = new Vector(0,0,4);

			objects.add(s);
			lights.add(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));
			viewMatrix = Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0));
			
			break;
		}

		}

		//The window aspect ratio
		double aspectRatio = (double)windowX/(double)windowY;

		//Value for adjusting the pixel position to account for the field of view
		double fovAdjust = (double) Math.tan(fov*0.5f *(Math.PI/180.0f));

		// Values used for supersampling anti-aliasing
		int totalSubPixels = ssColumnMax * ssRowMax;

		double columnMargin = (1f/(2*ssColumnMax));
		double rowMargin = (1f/(2*ssRowMax));

		try {
			BufferedImage img = new BufferedImage(windowX, windowY, BufferedImage.TYPE_INT_ARGB);

			for (int column = 0; column < windowX; column++) {
				for (int row = 0; row < windowY; row++) {

					// Color of pixel, super samples are added to this
					Vector pixelColor = new Vector(0,0,0);

					for (int ssColumn = 0; ssColumn < ssColumnMax; ssColumn++) {
						for (int ssRow = 0; ssRow < ssRowMax; ssRow++) {

							//Convert the pixel (Raster space coordinates: (0->ScreenWidth,0->ScreenHeight)) to NDC (Normalised Device Coordinates: (0->1,0->1))
							double pixelNormX = (column+columnMargin+(ssColumn*2*columnMargin))/windowX; //Add 0.5f to get centre of pixel
							double pixelNormY = (row+rowMargin+(ssRow*2*rowMargin))/windowY;
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

							Vector color = backgroundColor;

							//Cast our ray into the scene
							if(CastRay(ray,payload)>0.0){// > 0.0f indicates an intersection
								color = payload.getColor();
							}	

							// Divide ray payload by number of subpixels and add to total
							pixelColor = pixelColor.plus(color.divide(totalSubPixels));

						}
					}

					for (int i = 0; i<3; i++) {
						pixelColor.d[i] = Util.clamp(pixelColor.d[i], 0, 255);
					}

					Color tempColor = new Color((int)pixelColor.x(),(int)pixelColor.y(),(int)pixelColor.z());

					img.setRGB(column, row, tempColor.getRGB());

				}

				double percent = ((double)column*100)/((double)windowX);
				System.out.println(percent + "%");

			}

			File outputfile = new File("saved.png");
			ImageIO.write(img, "png", outputfile);

			Desktop dt = Desktop.getDesktop();
			dt.open(outputfile);

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;

			System.out.println("Elapsed time: " + elapsedTime/1000f + " seconds");

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	static double CastRay(Ray ray, Payload payload){

		if (payload.getNumBounces() > maxRecursionDepth) { //Return if max depth reached
			return 0f;
		}

		IntersectInfo info = new IntersectInfo();

		info.setTime(Double.POSITIVE_INFINITY); 

		boolean hit = false;

		// Sets info to the intersection info of nearest intersection
		for (MatObject o : objects) {
			boolean intersect = o.Intersect(ray, info);
			hit = hit | intersect;
		}

		if (hit){

			// Get colour of surface

			Vector illumination = new Vector(0,0,0);

			if (info.getObject().hasTextureMap) {
				
				MatObject o = info.getObject();
				BufferedImage bI = ((Sphere)o).texture;
				
				double u = o.u(info.getHitPoint());
				double v = o.v(info.getHitPoint());
				
				int iU = (int) (u * bI.getWidth());
				int iV = (int) (v * bI.getHeight());
				
				int rgb = info.getObject().texture.getRGB(iU, iV);
				Color c = new Color(rgb);
				
				payload.setColor(new Vector(c.getRed(),c.getGreen(),c.getBlue()));
				return info.getTime();
			}

			else {

				if (info.getMaterial().isAmbient) {
					Vector ambient = ambientLight.multComponents(info.getMaterial().ambient);
					illumination = illumination.plus(ambient);
				}


				// Diffuse and specular illumination
				if (info.getMaterial().isDiffuse || info.getMaterial().isSpecular) {

					for (Light l: lights) {

						Vector objectToLight = l.Position().minus(info.getHitPoint());
						double timeToLight = objectToLight.length();
						objectToLight = objectToLight.normalize();
						Ray shadowRay = new Ray(info.getHitPoint().plus(info.getNormal().times(shadowRayBias)), objectToLight);
						IntersectInfo shadowRayInfo = new IntersectInfo();
						shadowRayInfo.setTime(Double.POSITIVE_INFINITY);

						for (MatObject obj : objects) {
							obj.Intersect(shadowRay, shadowRayInfo); // shadowRayInfo becomes info of nearest intersection, if any
						}

						if (shadowRayInfo.getTime()>=timeToLight) { // no intersections with objects occur between object and light

							if (info.getMaterial().isDiffuse) {
								// Diffuse illumination

								Vector normal = info.getNormal();
								double cosTheta = objectToLight.cosAngleBetween(normal);

								if (cosTheta == 0) {
									System.out.println("Help");
								}

								cosTheta = Math.max(0, cosTheta);
								Vector diffuse = l.getColor().multComponents(info.getMaterial().diffuse).times(cosTheta);

								illumination = illumination.plus(diffuse);
							}

							if (info.getMaterial().isSpecular) {
								// Specular illumination

								Vector objectToCamera = ray.direction.times(-1).normalize(); // objectToCamera is the reverse of the original camera ray

								Vector normal = info.getNormal();
								Vector reflection = info.getNormal().times(2).times(normal.dotProduct(objectToLight)).minus(objectToLight);

								double cosAngle = reflection.cosAngleBetween(objectToCamera);
								cosAngle = Math.max(0, cosAngle);
								cosAngle = Math.pow(cosAngle, info.getMaterial().specularExponent);

								Vector specular = l.getColor().multComponents(info.getMaterial().specular).times(cosAngle);

								illumination = illumination.plus(specular);

							}
						}

					}

				}

				if (info.getMaterial().isReflective) {

					Vector objectToCamera = ray.direction.times(-1).normalize(); // objectToCamera is the reverse of the original camera ray

					Vector normal = info.getNormal();
					Vector reflection = info.getNormal().times(2).times(normal.dotProduct(objectToCamera)).minus(objectToCamera);

					Ray reflectionRay = new Ray(info.getHitPoint(),reflection.normalize());

					Payload reflectionPayload = new Payload();
					reflectionPayload.setNumBounces(payload.numBounces+1);

					// Reflection ray is cast 
					CastRay(reflectionRay, reflectionPayload);

					reflection = reflectionPayload.Color.times(info.getMaterial().reflectionCoefficient);

					illumination = reflection.plus(illumination.times(1-info.getMaterial().reflectionCoefficient));

				}

				//illumination = ambient;
				//illumination = totalDiffuse;
				//illumination = totalSpecular;

				payload.setColor(illumination);
				return info.getTime();
			}
		}

		return 0.0f;
	}

}




