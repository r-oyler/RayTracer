package rayTracer;

public class Settings {

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
	// Distance that shadow ray origins are moved along the surface normal to prevent shadow acne
	double shadowRayBias = 0.0001;
	
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
	
	public int getWindowX() {
		return windowX;
	}

	public void setWindowXY(int windowX, int windowY) {
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
		this.maxRecursionDepth = maxRecursionDepth;
	}

	public double getShadowRayBias() {
		return shadowRayBias;
	}

	public void setShadowRayBias(double shadowRayBias) {
		this.shadowRayBias = shadowRayBias;
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
		clone.setWindowXY(this.getWindowX(),this.getWindowY());
		clone.setFov(this.getFov());
		clone.setSSColRowMax(this.getSSColumnMax(), this.getSSRowMax());
		clone.setMaxRecursionDepth(this.getMaxRecursionDepth());
		clone.setShadowRayBias(this.getShadowRayBias());
		
		return clone;
		
	}
	
}
