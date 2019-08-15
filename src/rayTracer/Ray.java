package rayTracer;

// A ray, defined by an origin and direction
// Cast by camera and from intersection points
public class Ray {

	Vector origin;
	Vector direction;
	
	// Used for AABB intersection
	Vector invDirection;
	int[] sign = new int[3];
	

	public Ray(Vector o, Vector d) {
		origin = o;
		direction = d;
		
		// Precomputation used for AABB
		invDirection = direction.inverseDivide(1);
		sign[0] =  invDirection.x() < 0 ? 1 : 0;
		sign[1] =  invDirection.y() < 0 ? 1 : 0;
		sign[2] =  invDirection.z() < 0 ? 1 : 0;
		
	}

	//Returns the position of the ray at time t i.e. the solution to: RayPosition = RayOrigin + time*RayDirection;
	//Usage: position = atTime(t);
	Vector atTime(double t)
	{
		return origin.plus(direction.timesConst(t));
	}

	@Override
	public String toString() {
		return "Ray [origin=" + origin + ",\ndirection=" + direction + "]";
	}
	
}

class IntersectInfo{
	
	//The time along the ray that the intersection occurs
	double time = Double.POSITIVE_INFINITY;
	//The position of the intersection in 3D coordinates
	Vector hitPoint = new Vector(0.0f,0.0f,0.0f);
	//The normal vector of the surface at the point of the intersection
	Vector normal = new Vector(0.0f,0.0f,0.0f);
	//The material of the object that was intersected
	Material material;
	
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
}