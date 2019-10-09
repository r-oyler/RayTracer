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
		String directoryName = "frames";

		// Switch statement to have multiple scene setups
		int sceneNum = Input.getInt("Scene number", 12, 1, 12);
		
		double totalTime = 1;
		int totalFrames = 1;
		double deltaTperFrame = totalTime/totalFrames; 

		for(int frame = 0; frame < totalFrames; frame++ ){

			double time = frame*deltaTperFrame;

			scene = new Scene();
			
			// The ambient light that is cast on every object
			scene.setAmbientLight(new Vector(40f,40f,40f));
			// The color that is seen when a ray doesn't hit an object;
			scene.setBackgroundColor(new Vector(10,10,10));
			
			scene.setScene(sceneNum, time);
			
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

				for (int i = 0; i < processors; i++) {

					RayTraceRunnable runnable = new RayTraceRunnable(i, img, i, settings.getWindowX(), processors, settings.clone(), scene.clone());
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

				rayTrace(0, img, 0, settings.getWindowX(), 1, settings, scene);

			}

			File directory = new File(directoryName);
			if (!directory.exists()) directory.mkdir();

			File outputfile = new File(directoryName + "\\" + outputFileName + frame + ".png");
			ImageIO.write(img, "png", outputfile);

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;

			System.out.println();
			System.out.println("Elapsed time: " + elapsedTime/1000f + " seconds");

			int totalPixels = settings.getWindowX() * settings.getWindowY();
			int totalSubpixels = totalPixels * settings.getTotalSubPixels();

			System.out.println("Total pixels: " + totalPixels);
			System.out.println("Pixels per second (per thread): " + totalPixels / (float)elapsedTime + " (" + ((totalPixels / (float)elapsedTime)/processors) + ")");
			System.out.println("Subpixels per second (per thread): " + totalSubpixels / (float)elapsedTime + " (" + ((totalSubpixels / (float)elapsedTime)/processors) + ")");

		}

		BufferedImage firstImage = ImageIO.read(new File (directoryName + "\\" + outputFileName + "0.png"));
		ImageOutputStream output = new FileImageOutputStream(new File(outputFileName + ".gif"));

		int deltaTperStepMillis = Math.round((long)deltaTperFrame*1000);

		GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), deltaTperStepMillis, false);

		writer.writeToSequence(firstImage);
		for(int i=1; i<totalFrames; i++) {
			BufferedImage nextImage = ImageIO.read(new File(directoryName + "\\" + outputFileName + i + ".png"));
			writer.writeToSequence(nextImage);
		}

		writer.close();
		output.close();

		Desktop dt = Desktop.getDesktop();
		dt.open(new File(outputFileName + ".gif"));
	}

	public static void rayTrace(int threadNum, BufferedImage img, int startCol, int endCol, int stepSize, Settings settings, Scene scene) {

		int lastPercent = 0;

		for (int column = startCol; column < endCol; column+= stepSize) {
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
						Vector pixelCameraSpace = new Vector(pixelCameraX,pixelCameraY,-1);

						//ray comes from camera origin
						Vector rayOrigin = new Vector(0,0,0);

						//Transform from camera space to world space
						pixelCameraSpace = scene.getViewMatrix().timesVPoint(pixelCameraSpace);
						//The origin of the ray we are casting
						rayOrigin = scene.getViewMatrix().timesVPoint(rayOrigin);

						//The direction the ray is travelling in
						Vector rayDirection = pixelCameraSpace.minus(rayOrigin).normalize();

						//Set up ray in world space
						Ray ray = new Ray(rayOrigin,rayDirection);

						//Structure for storing the information we get from casting the ray
						Payload payload = new Payload();

						payload.setNumBounces(0);

						Vector color = scene.getBackgroundColor();
						
//						if (column == 777 && row == 387) {
//							System.out.println();
//						}
						
						if(castRay(ray,payload, settings, scene)>0.0){// > 0.0f indicates an intersection
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
	static double castRay(Ray ray, Payload payload, Settings settings, Scene scene){

		if (payload.getNumBounces() > settings.getMaxRecursionDepth()) { //Return if max depth reached
			payload.Color = scene.backgroundColor;
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
				Vector ambient = Shading.ambient(scene, info);
				illumination = illumination.plus(ambient);
			}

			// Diffuse and specular illumination
			if (info.getMaterial().isDiffuse || info.getMaterial().isSpecular) {
				Vector diffuseSpecular = Shading.diffuseSpecular(settings, scene, info, ray);
				illumination = illumination.plus(diffuseSpecular);
			}

			if (info.getMaterial().isReflective) {
				illumination = Shading.reflection(info, ray, payload, settings, scene, illumination);
			}

			if (info.getMaterial().isRefractive) {
				illumination = Shading.refraction(info, ray, payload, settings, scene, illumination);
			}

			payload.setColor(illumination);
			return info.getTime();

		}
		payload.Color = scene.backgroundColor;
		return 0.0f;
	}

}




