package rayTracer;

public abstract class Light{

	// The color of the light
	Vector color;

	public abstract Vector DiffuseSpecular(Ray ray, IntersectInfo info, Scene scene, Settings settings);
	
	public Vector getColor() {
		return color;
	}

	public void setColor(Vector color) {
		this.color = color;
	}

	public abstract Light clone();

}
