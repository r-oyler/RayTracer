package rayTracer;

// Object that does not have a material
public abstract class Object {

	//Name, useful for debugging
	String name;
	//Point, redundant given transform, but triangles require this
	Vector p0;
	//The transformation matrix for the object
	Matrix4 transform;

	Object(Vector p){
		this.p0 = p;
		transform = Matrix4.translationMatrix(p);
	}

	public void rotateX(double theta) {
		this.transform = transform.times(Matrix4.xRotationMatrix(theta));
	}

	public void rotateY(double theta) {
		this.transform = transform.times(Matrix4.yRotationMatrix(theta));
	}

	public void rotateZ(double theta) {
		this.transform = transform.times(Matrix4.zRotationMatrix(theta));
	}

	public void scale(double xScale, double yScale, double zScale) {
		this.transform = transform.times(Matrix4.scaleMatrix(xScale, yScale, zScale));
	}

	public void translate(double xDist, double yDist, double zDist) {
		this.transform = transform.times(Matrix4.translationMatrix(xDist, yDist, zDist));
	}

}