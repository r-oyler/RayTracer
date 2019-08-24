package rayTracer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class RayTracer {

	private static ArrayList<Thread> arrThreads = new ArrayList<Thread>();

	public static void main(String args[]) throws InterruptedException, IOException {
		
		Settings settings = new Settings();

		boolean getUserInput = Input.getBoolean("Choose custom inputs?");

		if (getUserInput) {
			settings = Input.getSettings();
		}

		else {

			settings.setMultithreading(true);
			// Dimensions of image
			settings.setWindowXY(1366, 768);
			//The field of view of the camera.  This is 90 degrees because our imaginary image plane is 2 units high (-1->1) and 1 unit from the camera position
			settings.setFov(90);

			// Settings for supersampling anti-aliasing
			settings.setSSColRowMax(1,1);

			// Maximum depth that reflection/refraction rays are cast
			settings.setMaxRecursionDepth(7);
			// Distance that shadow ray origins are moved along the surface normal to prevent shadow acne
			settings.setBias(0.0001);		

		}

		Scene scene;

		String outputFileName = Input.getString("Output file name", "saved");
		String directoryName = "gif";

		// Switch statement to have multiple scene setups
		int sceneNum = Input.getInt("Scene number", 18, 0, 18);

		int totalSteps = 10;
		double deltaTperStep = 1.0/10.0; 

		for(int step = 0; step <= totalSteps; step++ ){

			double time = step*deltaTperStep;

			scene = new Scene();
			
			// The ambient light that is cast on every object
			scene.setAmbientLight(new Vector(40f,40f,40f));
			// The color that is seen when a ray doesn't hit an object;
			scene.setBackgroundColor(new Vector(20f,0f,20f));;
			
			switch(sceneNum) {

			case 0:
			{
				AABB aabb = new AABB(new Vector(0,0,0), Material.RED, new Vector(1,1,1));

				Vector camPos = new Vector(0.5,0.5,3);

				scene.addObject(aabb);
				scene.addLight(new Light(camPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, aabb.p0, new Vector(0,1,0)));

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
				break;
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
				break;
			}

			case 6:{

				Sphere s = new Sphere(new Vector(0,0,0), Material.BLACK, 2);

				s.addTextureMap("2k_moon.jpg");

				Vector camPos = new Vector(4,4,4);
				Vector ligPos = new Vector(0,0,4);

				scene.addObject(s);
				scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));
				scene.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));
				break;
			}

			case 7:{

				AABB aabb = new AABB(new Vector(1,1,1), Material.RED, new Vector(1,1,1));
				AABB aabb2 = new AABB(new Vector(5,1,1), Material.BLUE, new Vector(1,1,1));
				AABB aabb3 = new AABB(new Vector(1,5,1), Material.YELLOW, new Vector(1,1,1));
				AABB aabb4 = new AABB(new Vector(1,1,5), Material.GREEN, new Vector(1,1,1));

				Vector camPos = new Vector(8,8,8);
				Vector lightPos = camPos;

				scene.addObject(aabb);
				scene.addObject(aabb2);
				scene.addObject(aabb3);
				scene.addObject(aabb4);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, aabb.p0, new Vector(0,1,0)));

				break;
			}

			case 8:{

				AABB aabb = new AABB(new Vector(0,0,0), Material.RED, new Vector(1,1,1));
				AABB aabb2 = new AABB(new Vector(2,0,0), Material.BLUE, new Vector(0.5,0.5,0.5));
				AABB aabb3 = new AABB(new Vector(0,2,0), Material.YELLOW, new Vector(0.5,0.5,0.5));
				AABB aabb4 = new AABB(new Vector(0,0,2), Material.GREEN, new Vector(0.5,0.5,0.5));


				Vector camPos = new Vector(8,8,8);
				Vector lightPos = new Vector(0,8,0);

				scene.addObject(aabb);
				scene.addObject(aabb2);
				scene.addObject(aabb3);
				scene.addObject(aabb4);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, aabb.p0, new Vector(0,1,0)));

				aabb2.rotateY(Util.degreeToRadian(45));
				aabb3.rotateX(Util.degreeToRadian(45));
				aabb4.rotateZ(Util.degreeToRadian(45));

				break;
			}

			case 9:{

				Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
				Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
				Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

				p0.addTextureMap("gridred.png");
				p1.addTextureMap("gridgreen.png");
				p2.addTextureMap("gridblue.png");

				Sphere s = new Sphere(new Vector(1,1,1), Material.TEXTURE, 0.5);

				s.addTextureMap("checkerboard.png");

				Vector camPos = new Vector(3,3,3);
				Vector lightPos = new Vector(3,5,2);

				scene.addObject(p0);
				scene.addObject(p1);
				scene.addObject(p2);
				scene.addObject(s);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, s.p0, new Vector(0,1,0)));

				break;
			}

			case 10:{

				Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
				Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
				Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

				p0.addTextureMap("gridred.png");
				p1.addTextureMap("gridgreen.png");
				p2.addTextureMap("gridblue.png");

				Sphere s = new Sphere(new Vector(1,1,1), Material.GLASS, 0.5);

				Vector camPos = new Vector(3,3,3);
				Vector lightPos = new Vector(3,3,3);

				scene.addObject(p0);
				scene.addObject(p1);
				scene.addObject(p2);
				scene.addObject(s);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(1,1,1), new Vector(0,1,0)));

				break;
			}

			case 11:{

				Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
				Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
				Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

				p0.addTextureMap("gridred.png");
				p1.addTextureMap("coloredgrid.png");
				p2.addTextureMap("gridblue.png");

				Sphere s0 = new Sphere(new Vector(1,1,1), Material.DIAMOND, 0.5);
				Sphere s1 = new Sphere(new Vector(3,1,1), Material.SAPPHIRE, 0.5);
				Sphere s2 = new Sphere(new Vector(5,1,1), Material.EMERALD, 0.5);
				Sphere s3 = new Sphere(new Vector(7,1,1), Material.RUBY, 0.5);

				Vector camPos = new Vector(4,1,5);
				Vector lightPos = new Vector(4,4,3);

				scene.addObject(p0);
				scene.addObject(p1);
				scene.addObject(p2);
				scene.addObject(s0);
				scene.addObject(s1);
				scene.addObject(s2);
				scene.addObject(s3);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(4,1,1), new Vector(0,1,0)));

				break;
			}

			case 12:{

				Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
				Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
				Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

				p0.addTextureMap("gridred.png");
				p1.addTextureMap("coloredgrid.png");
				p2.addTextureMap("gridblue.png");

				Sphere s0 = new Sphere(new Vector(5,1,1), Material.BRASS, 0.5);

				Vector camPos = new Vector(4,1,5);
				Vector lightPos = new Vector(4,4,3);

				scene.addObject(p0);
				scene.addObject(p1);
				scene.addObject(p2);
				scene.addObject(s0);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(4,1,1), new Vector(0,1,0)));

				break;
			}

			case 13:{

				InfCylinder c = new InfCylinder(new Vector(0,0,0), Material.BLUE, new Vector(1,0,0), 1);

				Vector camPos = new Vector(2,2,5);
				Vector lightPos = new Vector(0,0,5);

				scene.addObject(c);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

				break;
			}

			case 14:{

				Cylinder c = new Cylinder(new Vector(-2,0,0), Material.BLUE, new Vector(2,0,0), 1);

				Vector camPos = new Vector(-3,0,4);
				Vector lightPos = new Vector(-3,0,4);

				scene.addObject(c);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

				break;

			}

			case 15:{

				Capsule c = new Capsule(new Vector(-2,0,0), Material.BLUE, new Vector(2,0,0), 1);

				Vector camPos = new Vector(2,2,5);
				Vector lightPos = new Vector(0,0,5);

				scene.addObject(c);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

				break;

			}

			case 16:{

				Cone c = new Cone(new Vector(-2,0,0), Material.BLUE, new Vector(2,0,0), 0.5, 1);

				Vector camPos = new Vector(-3,0,5);
				Vector lightPos = new Vector(0,0,5);

				scene.addObject(c);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

				break;

			}

			case 17:{

				RoundedBox r = new RoundedBox(new Vector(0,0,0), Material.BLUE, new Vector(2,4,1), 0.5);

				Vector camPos = new Vector(0,0,15);

				camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();

				Vector lightPos = new Vector(0,0,15);

				scene.addObject(r);
				scene.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

				r.rotateY(Util.degreeToRadian(0));

				break;

			}
			
			case 18:{
				
				double sin = (Math.sin(time*2*Math.PI)/2)+0.5;
				
				Vector v = new Vector(sin,0,1-sin);
				
				Material m = new Material("m");
				m.setAmbient(v.timesConst(0.2));
				m.setDiffuse(v);
				m.setSpecular(new Vector(1,1,1),20);
				Sphere s = new Sphere(new Vector(sin,0,1-sin), m, 1);

				Vector camPos = new Vector(0,0,3);
				Vector ligPos = new Vector(2,0,3);

				scene.addObject(s);
				scene.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

				scene.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

				break;
				
			}

			}

			long startTime = System.currentTimeMillis();

			BufferedImage img = null;

			try {
				img = new BufferedImage(settings.getWindowX(), settings.getWindowY(), BufferedImage.TYPE_INT_ARGB);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			int processors = 1;

			if (settings.isMultithreading()) {

				processors = Runtime.getRuntime().availableProcessors();

				int colsPerThread = settings.getWindowX()/processors;

				for (int i = 0; i < processors; i++) {

					int startCol = i * colsPerThread;
					int endCol = (i+1) * colsPerThread;

					RayTraceRunnable runnable = new RayTraceRunnable(i, img, startCol, endCol, settings.clone(), scene.clone());
					Thread t = new Thread(runnable);
					t.start();
					arrThreads.add(t);

				}

				for (int i = 0; i < arrThreads.size(); i++) 
				{
					arrThreads.get(i).join(); 
				}

			}

			else {

				rayTrace(0, img, 0, settings.getWindowX(), settings, scene);

			}

			File directory = new File(directoryName);
			if (!directory.exists()) directory.mkdir();

			File outputfile = new File(directoryName + "\\" + outputFileName + step + ".png");
			ImageIO.write(img, "png", outputfile);

			//		Desktop dt = Desktop.getDesktop();
			//		dt.open(outputfile);

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;

			System.out.println();
			System.out.println("Elapsed time: " + elapsedTime/1000f + " seconds");

			int totalPixels = settings.getWindowX() * settings.getWindowY();
			int totalSubpixels = totalPixels * settings.getTotalSubPixels();

			System.out.println("Total pixels: " + totalPixels);
			System.out.println("Pixels per second (per thread): " + totalPixels / elapsedTime + " (" + ((totalPixels / elapsedTime)/processors) + ")");
			System.out.println("Subpixels per second (per thread): " + totalSubpixels / elapsedTime + " (" + ((totalSubpixels / elapsedTime)/processors) + ")");

		}

		BufferedImage firstImage = ImageIO.read(new File (directoryName + "\\" + outputFileName + "0.png"));
		ImageOutputStream output = new FileImageOutputStream(new File(outputFileName + ".gif"));

		int deltaTperStepMillis = Math.round((long)deltaTperStep*1000);

		GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), deltaTperStepMillis, false);

		writer.writeToSequence(firstImage);
		for(int i=1; i<=totalSteps; i++) {
			BufferedImage nextImage = ImageIO.read(new File(directoryName + "\\" + outputFileName + i + ".png"));
			writer.writeToSequence(nextImage);
		}

		writer.close();
		output.close();

		Desktop dt = Desktop.getDesktop();
		dt.open(new File(outputFileName + ".gif"));
	}

	public static void rayTrace(int threadNum, BufferedImage img, int startCol, int endCol, Settings settings, Scene scene) {

		int lastPercent = 0;

		for (int column = startCol; column < endCol; column++) {
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
						pixelCameraSpace = scene.getViewMatrix().timesV(pixelCameraSpace);
						//The origin of the ray we are casting
						rayOrigin = scene.getViewMatrix().timesV(rayOrigin);

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

						//						if (column == 1366/2 && row == 768/2) {
						//							System.out.print("");
						//						}

						if(CastRay(ray,payload, settings, scene)>0.0){// > 0.0f indicates an intersection
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

			int percent = (int) (((double)(column-startCol)*100)/((double)(endCol-startCol)));

			if (percent > lastPercent) {
				System.out.println("Thread " + threadNum + ": " + percent + "%");
				lastPercent = percent;
			}

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

			if (info.getMaterial().isAmbient) {

				Vector ambient;

				if (info.hasUV) {
					ambient = scene.getAmbientLight().multComponents(info.getMaterial().ambient.multComponents(info.getUVcolor()));
				}
				else {
					ambient = scene.getAmbientLight().multComponents(info.getMaterial().ambient);
				}
				illumination = illumination.plus(ambient);
			}


			// Diffuse and specular illumination
			if (info.getMaterial().isDiffuse || info.getMaterial().isSpecular) {

				for (Light l: scene.getLights()) {

					Vector objectToLight = l.Position().minus(info.getHitPoint());
					double timeToLight = objectToLight.length();
					objectToLight = objectToLight.normalize();
					Ray shadowRay = new Ray(info.getHitPoint().plus(info.getNormal().timesConst(settings.getBias())), objectToLight);
					IntersectInfo shadowRayInfo = new IntersectInfo();
					shadowRayInfo.setTime(Double.POSITIVE_INFINITY);

					for (MatObject obj : scene.getObjects()) {
						if (!obj.material.isRefractive){	// Refractive materials don't cast shadows				
							obj.Intersect(shadowRay, shadowRayInfo); // shadowRayInfo becomes info of nearest intersection, if any
						}
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

							Vector diffuse;

							if (info.hasUV) {
								diffuse = l.getColor().multComponents(info.getMaterial().diffuse.multComponents(info.getUVcolor())).timesConst(cosTheta);
							}
							else {
								diffuse = l.getColor().multComponents(info.getMaterial().diffuse).timesConst(cosTheta);
							}

							illumination = illumination.plus(diffuse);
						}

						if (info.getMaterial().isSpecular) {
							// Specular illumination

							Vector objectToCamera = ray.direction.timesConst(-1).normalize(); // objectToCamera is the reverse of the original camera ray

							Vector normal = info.getNormal();
							Vector reflection = info.getNormal().timesConst(2).timesConst(normal.dotProduct(objectToLight)).minus(objectToLight);

							double cosAngle = reflection.cosAngleBetween(objectToCamera);
							cosAngle = Math.max(0, cosAngle);
							cosAngle = Math.pow(cosAngle, info.getMaterial().specularExponent);

							Vector specular = l.getColor().multComponents(info.getMaterial().specular).timesConst(cosAngle);

							illumination = illumination.plus(specular);

						}
					}

				}

			}

			if (info.getMaterial().isReflective) {

				Vector normal = info.getNormal();
				Vector reflection = Util.reflect(ray.direction, normal);

				Ray reflectionRay = new Ray(info.getHitPoint().plus(info.getNormal().timesConst(settings.getBias())),reflection.normalize());

				Payload reflectionPayload = new Payload();
				reflectionPayload.setNumBounces(payload.numBounces+1);

				// Reflection ray is cast 
				CastRay(reflectionRay, reflectionPayload, settings, scene);

				reflection = reflectionPayload.Color.timesConst(info.getMaterial().reflectionCoefficient);

				reflection = reflection.multComponents(info.getMaterial().getReflectRefractTint());

				illumination = reflection.plus(illumination.timesConst(1-info.getMaterial().reflectionCoefficient));

			}

			if (info.getMaterial().isRefractive) {

				double kReflect = Util.fresnel(ray.direction, info.getNormal(), info.getMaterial().refractiveIndex);

				boolean outside = ray.direction.dotProduct(info.getNormal()) < 0;

				Vector bias = info.getNormal().timesConst(settings.getBias());

				// compute refraction if it is not a case of total internal reflection
				if (kReflect < 1) {

					Vector refractionDirection = Util.refract(ray.direction, info.getNormal(), info.getMaterial().refractiveIndex);

					Vector refractionRayOrigin = outside ? info.getHitPoint().minus(bias) : info.getHitPoint().plus(bias);

					Ray refractionRay = new Ray(refractionRayOrigin, refractionDirection.normalize());

					Payload refractionPayload = new Payload();
					refractionPayload.setNumBounces(payload.numBounces+1);

					CastRay(refractionRay, refractionPayload, settings, scene);

					Vector refraction = refractionPayload.Color.timesConst(1-kReflect);

					refraction = refraction.multComponents(info.getMaterial().getReflectRefractTint());

					illumination = illumination.plus(refraction);

				}

				Vector reflectionDirection = Util.reflect(ray.direction, info.getNormal());

				Vector reflectionRayOrigin = outside ? info.getHitPoint().plus(bias) : info.getHitPoint().minus(bias);

				Ray reflectionRay = new Ray(reflectionRayOrigin, reflectionDirection);

				Payload reflectionPayload = new Payload();
				reflectionPayload.setNumBounces(payload.numBounces+1);

				CastRay(reflectionRay, reflectionPayload, settings, scene);

				Vector reflection = reflectionPayload.Color.timesConst(kReflect);

				reflection = reflection.multComponents(info.getMaterial().getReflectRefractTint());

				illumination = illumination.plus(reflection);


			}

			payload.setColor(illumination);
			return info.getTime();

		}

		return 0.0f;
	}

}




