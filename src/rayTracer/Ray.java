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