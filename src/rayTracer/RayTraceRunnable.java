package rayTracer;

import java.awt.image.BufferedImage;

public class RayTraceRunnable implements Runnable {

	int threadNum;
	BufferedImage img;
	int startCol;
	int endCol;
	int stepSize;
	Settings settings;
	Scene scene;
	
	public RayTraceRunnable(int threadNum, BufferedImage img, int startCol, int endCol, int stepSize, Settings settings, Scene scene) {
		this.threadNum = threadNum;
		this.img = img;
		this.startCol = startCol;
		this.endCol = endCol;
		this.stepSize = stepSize;
		this.settings = settings;
		this.scene = scene;
	}
	
	@Override
	public void run() {
		//System.out.println(startCol + " - " + (endCol-1) + " starting.");
		RayTracer.rayTrace(this.threadNum, this.img, this.startCol, this.endCol, this.stepSize, this.settings, this.scene);
		//System.out.println(startCol + " - " + (endCol-1) + " exiting.");
	}

}
