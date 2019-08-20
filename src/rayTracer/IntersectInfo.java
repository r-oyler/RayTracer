package rayTracer;

public class IntersectInfo{

	//The time along the ray that the intersection occurs
	double time = Double.POSITIVE_INFINITY;
	//The position of the intersection in 3D coordinates
	Vector hitPoint = new Vector(0.0f,0.0f,0.0f);
	//The normal vector of the surface at the point of the intersection
	Vector normal = new Vector(0.0f,0.0f,0.0f);
	//The material of the object that was intersected
	Material material;

	boolean hasUV = false;

	Vector uvColor;

	MatObject object;

	String objectName;

	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public Vector getHitPoint() {
		return hitPoint;
	}
	public void setHitPoint(Vector hitPoint) {
		this.hitPoint = hitPoint;
	}
	public Vector getNormal() {
		return normal;
	}
	public void setNormal(Vector normal) {
		this.normal = normal;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public void setHasNoUV() {
		this.hasUV = false;
	}
	public Vector getUVcolor() {
		return uvColor;
	}
	public void setUVcolor(Vector uvColor) {
		this.uvColor = uvColor;
		this.hasUV = true;
	}
	public MatObject getObject() {
		return this.object;
	}
	public void setObject(MatObject object) {
		this.object = object;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public void updateInfo(double time, Vector hitPoint, Vector normal, MatObject obj) {

		// if this is the first intersection or nearer than previous intersection
		if (time < this.time) {

			this.setTime(time);
			this.setHitPoint(hitPoint);
			this.setNormal(normal.normalize());
			this.setMaterial(obj.material);

			if (obj.hasTextureMap) {
				Vector color = obj.getUVcolor(obj.calcUV(hitPoint));
				this.setUVcolor(color);
			}
			else {
				this.setHasNoUV();
			}

			this.setObject(obj);
			this.setObjectName(obj.name);

		}

	}
}