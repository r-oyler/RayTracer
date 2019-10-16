package rayTracer;

public class Settings {
	
	boolean multithreading = false;
	
	// Dimensions of image
	int windowX = 640;
	int windowY = 480;
	// The field of view of the camera.  This is 90 degrees because our imaginary image plane is 2 units high (-1->1) and 1 unit from the camera position
	double fov = 90f;
	// Settings for supersampling anti-aliasing
	int ssColumnMax = 1;
	int ssRowMax = 1;
	// Maximum depth that reflection/refraction rays are cast
	int maxRecursionDepth = 7;
	// Distance that shadow ray, reflection ray and refraction ray origins are moved along the surface normal to prevent shadow acne
	double bias = 0.0001;
	
	// Calculated values
	double aspectRatio;
	double fovAdjust;

	int totalSubPixels;
	double columnMargin;
	double rowMargin;

	public Settings() {

	}
	
	// The window aspect ratio
	public void calcAspectRatio() {
		this.aspectRatio = (double)this.windowX/(double)this.windowY;
	}
	
	// Value for adjusting the pixel position to account for the field of view
	public void calcFovAdjust() {
		this.fovAdjust = (double) Math.tan(this.fov*0.5f *(Math.PI/180.0f));
	}
	
	// Total number of subpixels, used for averaging contribution to final pixel value
	public void calcTotalSubPixels() {
		this.totalSubPixels = this.ssColumnMax * this.ssRowMax;
	}
	
	// columnMargin is the horizontal distance from the border of a pixel to first sample point
	// This is also half the distance between sample points
	public void calcColumnMargin() {
		this.columnMargin = 1f/(2*ssColumnMax);
	}
	
	// rowMargin is the vertical distance from the border of a pixel to first sample point
	// This is also half the distance between sample points
	public void calcRowMargin() {
		this.rowMargin = 1f/(2*ssRowMax);
	}
	
	public boolean isMultithreading() {
		return multithreading;
	}

	public void setMultithreading(boolean multithreading) {
		this.multithreading = multithreading;
	}

	public int getWindowX() {
		return windowX;
	}

	public void setWindowXY(int windowX, int windowY) {
		if (windowX <= 0) throw new IllegalArgumentException("Image width must not be less than or equal to 0.\nValue entered: "+ windowX);
		if (windowY <= 0) throw new IllegalArgumentException("Image height must not be less than or equal to 0.\nValue entered: "+ windowY);
		
		this.windowX = windowX;
		this.windowY = windowY;
		this.calcAspectRatio();
		this.calcFovAdjust();
	}

	public int getWindowY() {
		return windowY;
	}

	public double getFov() {
		return fov;
	}

	public void setFov(double fov) {
		if (fov <= 0) throw new IllegalArgumentException("Field of view must not be less than or equal to 0.\nValue entered: "+ fov);
		
		this.fov = fov;
		this.calcFovAdjust();
	}

	public int getSSColumnMax() {
		return ssColumnMax;
	}

	public int getSSRowMax() {
		return ssRowMax;
	}

	public void setSSColRowMax(int ssColumnMax, int ssRowMax) {
		if (ssColumnMax <= 0) throw new IllegalArgumentException("Supersampling grid width must not be less than or equal to 0.\nValue entered: "+ ssColumnMax);
		if (ssRowMax <= 0) throw new IllegalArgumentException("Supersampling grid height must not be less than or equal to 0.\nValue entered: "+ ssRowMax);
		
		this.ssColumnMax = ssColumnMax;
		this.ssRowMax = ssRowMax;
		this.calcTotalSubPixels();
		this.calcColumnMargin();
		this.calcRowMargin();
	}
	
	public int getMaxRecursionDepth() {
		return maxRecursionDepth;
	}

	public void setMaxRecursionDepth(int maxRecursionDepth) {
		if (maxRecursionDepth < 0) throw new IllegalArgumentException("Max recursion depth must not be less than 0.\nValue entered: "+ maxRecursionDepth);
		
		this.maxRecursionDepth = maxRecursionDepth;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		if (bias < 0) throw new IllegalArgumentException("Bias must not be less than 0.\nValue entered: "+ bias);
		
		this.bias = bias;
	}

	public double getAspectRatio() {
		return aspectRatio;
	}

	public double getFovAdjust() {
		return fovAdjust;
	}

	public int getTotalSubPixels() {
		return totalSubPixels;
	}

	public double getColumnMargin() {
		return columnMargin;
	}

	public double getRowMargin() {
		return rowMargin;
	}

	public Settings clone() {
		
		Settings clone = new Settings();
		clone.setMultithreading(this.multithreading);
		clone.setWindowXY(this.getWindowX(),this.getWindowY());
		clone.setFov(this.getFov());
		clone.setSSColRowMax(this.getSSColumnMax(), this.getSSRowMax());
		clone.setMaxRecursionDepth(this.getMaxRecursionDepth());
		clone.setBias(this.getBias());
		
		return clone;
		
	}
	
}
