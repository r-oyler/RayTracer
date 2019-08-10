package rayTracer;

// Holds information about the current state of the ray
public class Payload{
	
	// Accumulated color of this ray.
	Vector Color = new Vector(0.0f,0.0f,0.0f);
	// Number of bounces this ray has made so far.
	int numBounces = 0;
	
	public Vector getColor() {
		return Color;
	}
	public void setColor(Vector color) {
		Color = color;
	}
	public int getNumBounces() {
		return numBounces;
	}
	public void setNumBounces(int numBounces) {
		this.numBounces = numBounces;
	}
	
}
