package rayTracer;

public class AABB {

	Vector[] bounds = new Vector[2];
	
	AABB(Vector minBounds, Vector maxBounds) {
		this.bounds[0] = minBounds;
		this.bounds[1] = maxBounds;
	}

	public boolean intersect(Ray r) {
		
		double tmin, tmax, tymin, tymax, tzmin, tzmax;
		
		tmin = (bounds[r.sign[0]].x() - r.origin.x()) * r.invDirection.x();
		tmax = (bounds[1-r.sign[0]].x() - r.origin.x()) * r.invDirection.x();
		tymin = (bounds[r.sign[1]].y() - r.origin.y()) * r.invDirection.y();
		tymax = (bounds[1-r.sign[1]].y() - r.origin.y()) * r.invDirection.y();
		
		if ((tmin > tymax) || (tymin > tmax)) return false;
		
		if (tymin > tmin) tmin = tymin;
		if (tymax < tmax) tmax = tymax;
		
		tzmin = (bounds[r.sign[2]].z() - r.origin.z()) * r.invDirection.z();
		tzmax = (1-bounds[r.sign[2]].z() - r.origin.z()) * r.invDirection.z();
		
		if ((tmin > tzmax) || (tzmin > tmax)) return false;
		
		if (tzmin > tmin) tmin = tzmin;
		if (tzmax < tmax) tmax = tzmax;
		
		return true;
		
	}
	
	
	
}
