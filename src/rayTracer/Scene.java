package rayTracer;

import java.util.ArrayList;

public class Scene {

	// Transforms from world-space to view-space
	Matrix4 viewMatrix;
	// Structure containing all scene objects
	ArrayList<MatObject> objects;
	// Structure containing all scene lights
	ArrayList<Light> lights;
	// The ambient light that is cast on every object
	Vector ambientLight;
	// The color that is seen when a ray doesn't hit an object;
	Vector backgroundColor;
	
	public Scene() {
		objects = new ArrayList<MatObject>();
		lights = new ArrayList<Light>();
	}

	public Matrix4 getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(Matrix4 viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public ArrayList<MatObject> getObjects() {
		return objects;
	}

	public void addObject(MatObject object) {
		this.objects.add(object);
	}

	public ArrayList<Light> getLights() {
		return lights;
	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public Vector getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(Vector ambientLight) {
		this.ambientLight = ambientLight;
	}

	public Vector getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Vector backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public Scene clone() {

		Scene clone = new Scene();
		clone.setViewMatrix(viewMatrix.clone());
		for (MatObject o: this.objects) {
			clone.addObject(o.clone());
		}
		for (Light l: this.lights) {
			clone.addLight(l.clone());
		}
		clone.setAmbientLight(this.ambientLight);
		clone.setBackgroundColor(this.backgroundColor);
		return clone;

	}
	
}
