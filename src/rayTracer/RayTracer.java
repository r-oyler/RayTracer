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

	public static void main(String args[]) {

		Settings settings = new Settings();
		
		// Dimensions of image
		settings.setWindowXY(640,480);
		//The field of view of the camera.  This is 90 degrees because our imaginary image plane is 2 units high (-1->1) and 1 unit from the camera position
		settings.setFov(90f);

		// Settings for supersampling anti-aliasing
		settings.setSSColRowMax(1,1);

		// Maximum depth that reflection/refraction rays are cast
		settings.setMaxRecursionDepth(7);
		// Distance that shadow ray orgins are moved along the surface normal to prevent shadow acne
		settings.setShadowRayBias(0.0001);		
		
		Scene scene = new Scene();
		
		// The ambient light that is cast on every object
		scene.setAmbientLight(new Vector(40f,40f,40f));
		// The color that is seen when a ray doesn't hit an object;
		scene.setBackgroundColor(new Vector(20f,0f,20f));;

		// Switch statement to have multiple scene setups
		int sceneNum = 6;
		switch(sceneNum) {

		case 0:
		{
			AABB aabb = new AABB(Material.RED, new Vector(0,0,0), new Vector(1,1,1));

			Vector camPos = new Vector(0.5,0.5,3);

			scene.addObject(aabb);
			scene.addLight(new Light(camPos,PlanetPixel.DIRECT_SUNLIGHT));

			scene.setViewMatrix(Matrix4.lookAt(camPos, aabb.centre(), new Vector(0,1,0)));

			break;
		}

		case 1:
		{
			Sphere s = new Sphere(new Vector(0,0,0), Material.RED, 1);

			Vector camPos = new Vector(0,0,3);
			Vector ligPos = new Vector(2,0,3);

			scene.addObject(s);
			scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			scene.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));

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

			scene.addObject(t0);
			scene.addObject(t1);
			scene.addObject(t2);
			scene.addObject(t3);

			scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			scene.setViewMatrix(Matrix4.lookAt(camPos, centre, new Vector(0,1,0)));

			break;
		}

		case 3:{

			Vector p0 = new Vector(0,0,0);
			Vector p1 = new Vector(0,1,0);
			Vector p2 = new Vector(1,0,0);

			Triangle t = new Triangle(p0,p1,p2, Material.RED);

			Vector camPos = new Vector(0,0,1);
			Vector ligPos = camPos;

			scene.addObject(t);
			scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			scene.setViewMatrix(Matrix4.lookAt(camPos, t.centre(), new Vector(0,1,0)));

			break;
		}

		case 4:{

			Vector p0 = new Vector(0,0,0);
			Vector n = new Vector(1,1,1);

			Disk d = new Disk(p0, Material.RED, n, 1);

			Vector camPos = new Vector(0,0,5);
			Vector ligPos = camPos;

			scene.addObject(d);
			scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			scene.setViewMatrix(Matrix4.lookAt(camPos, p0, new Vector(0,1,0)));
		}

		case 5:{

			Plane p0 = new Plane(new Vector(0,0,-1.1), Material.RED, new Vector(0,0,1));
			Plane p1 = new Plane(new Vector(0,-1.1,0), Material.GREEN, new Vector(0,1,0));
			Plane p2 = new Plane(new Vector(-1.1,0,0), Material.BLUE, new Vector(1,0,0));

			Sphere s = new Sphere(new Vector(0,0,0), Material.MIRROR, 1);

			Vector camPos = new Vector(1,1,4);
			Vector ligPos = new Vector(0,5,0);

			scene.addObject(p0);
			scene.addObject(p1);
			scene.addObject(p2);
			scene.addObject(s);
			scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			scene.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));

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

			scene.addObject(s);
			scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));
			scene.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));

		}

		}

		long startTime = System.currentTimeMillis();
		
		rayTrace(settings, scene);

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;

		System.out.println("Elapsed time: " + elapsedTime/1000f + " seconds");
		
	}

	public static void rayTrace(Settings settings, Scene scene) {

		try {
			BufferedImage img = new BufferedImage(settings.getWindowX(), settings.getWindowY(), BufferedImage.TYPE_INT_ARGB);

			for (int column = 0; column < settings.getWindowX(); column++) {
				for (int row = 0; row < settings.getWindowY(); row++) {

					// Color of pixel, super samples are added to this
					Vector pixelColor = new Vector(0,0,0);

					for (int ssColumn = 0; ssColumn < settings.getSSColumnMax(); ssColumn++) {
						for (int ssRow = 0; ssRow < settings.getSSRowMax(); ssRow++) {

							//Convert the pixel (Raster space coordinates: (0->ScreenWidth,0->ScreenHeight)) to NDC (Normalised Device Coordinates: (0->1,0->1))
							double pixelNormX = (column+settings.getColumnMargin()+(ssColumn*2*settings.getColumnMargin()))/settings.getWindowX(); //Add 0.5f to get centre of pixel
							double pixelNormY = (row+settings.getRowMargin()+(ssRow*2*settings.getRowMargin()))/settings.getWindowY();
							//Convert from NDC, (0->1,0->1), to Screen space (-1->1,-1->1).  These coordinates correspond to those used by OpenGL
							//Note coordinate (-1,1) in screen space corresponds to coordinate (0,0) in raster space i.e. column = 0, row = 0
							double pixelScreenX = 2.0f*pixelNormX - 1.0f;
							double pixelScreenY = 1.0f-2.0f*pixelNormY;

							//Account for Field of View			
							double pixelCameraX = pixelScreenX * settings.getFovAdjust();
							double pixelCameraY = pixelScreenY * settings.getFovAdjust();

							//Account for image aspect ratio
							pixelCameraX *= settings.getAspectRatio();

							//Put pixel into camera space (offset by 1 unit along camera facing direction i.e. negative z axis)
							Vector pixelCameraSpace = new Vector(pixelCameraX,pixelCameraY,-1.0,1.0);

							//ray comes from camera origin
							Vector rayOrigin = new Vector(0.0f,0.0f,0.0f,1.0f);

							//Transform from camera space to world space
							pixelCameraSpace = scene.getViewMatrix().times(pixelCameraSpace);
							//The origin of the ray we are casting
							rayOrigin = scene.getViewMatrix().times(rayOrigin);

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

							Vector color = scene.getBackgroundColor();

							//Cast our ray into the scene
							if(CastRay(ray, payload, settings, scene)>0.0){// > 0.0f indicates an intersection
								color = payload.getColor();
							}	

							// Divide ray payload by number of subpixels and add to total
							pixelColor = pixelColor.plus(color.divide(settings.getTotalSubPixels()));

						}
					}

					for (int i = 0; i<3; i++) {
						pixelColor.d[i] = Util.clamp(pixelColor.d[i], 0, 255);
					}

					Color tempColor = new Color((int)pixelColor.x(),(int)pixelColor.y(),(int)pixelColor.z());

					img.setRGB(column, row, tempColor.getRGB());

				}

				double percent = ((double)column*100)/((double)settings.getWindowX());
				System.out.println(percent + "%");

			}

			File outputfile = new File("saved.png");
			ImageIO.write(img, "png", outputfile);

			Desktop dt = Desktop.getDesktop();
			dt.open(outputfile);

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	//Recursive ray-casting function
	//Called for each pixel and each time a ray is reflected/used for shadow testing
	//@ray The ray we are casting
	//@payload Information on the current ray i.e. the cumulative color and the number of bounces it has performed
	//returns either the time of intersection with an object (the coefficient t in the equation: RayPosition = RayOrigin + t*RayDirection) or zero to indicate no intersection
	static double CastRay(Ray ray, Payload payload, Settings settings, Scene scene){

		if (payload.getNumBounces() > settings.getMaxRecursionDepth()) { //Return if max depth reached
			return 0f;
		}

		IntersectInfo info = new IntersectInfo();

		info.setTime(Double.POSITIVE_INFINITY); 

		boolean hit = false;

		// Sets info to the intersection info of nearest intersection
		for (MatObject o : scene.getObjects()) {
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
					Vector ambient = scene.getAmbientLight().multComponents(info.getMaterial().ambient);
					illumination = illumination.plus(ambient);
				}


				// Diffuse and specular illumination
				if (info.getMaterial().isDiffuse || info.getMaterial().isSpecular) {

					for (Light l: scene.getLights()) {

						Vector objectToLight = l.Position().minus(info.getHitPoint());
						double timeToLight = objectToLight.length();
						objectToLight = objectToLight.normalize();
						Ray shadowRay = new Ray(info.getHitPoint().plus(info.getNormal().times(settings.getShadowRayBias())), objectToLight);
						IntersectInfo shadowRayInfo = new IntersectInfo();
						shadowRayInfo.setTime(Double.POSITIVE_INFINITY);

						for (MatObject obj : scene.getObjects()) {
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
					CastRay(reflectionRay, reflectionPayload, settings, scene);

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




