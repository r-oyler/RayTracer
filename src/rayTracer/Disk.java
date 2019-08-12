package rayTracer;

// A disk, defined by a centre point, normal vector and radius
public class Disk extends MatObject {

	Vector normal;
	double radius;
	
	Disk(Vector p, Material m, Vector normal, double r) {
		super(p, m);
		this.normal = normal;
		this.radius = r;
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {
		
		// Plane of disk
		Plane p = new Plane(this.p0, this.material, this.normal);
		// Intersect info for plane
		IntersectInfo planeInfo = new IntersectInfo();
		
		// Check intersection with plane first
		if (p.Intersect(ray, planeInfo)) {
			
			Vector centreToHitPoint = planeInfo.getHitPoint().minus(this.p0);
			double squaredLength = centreToHitPoint.squaredLength();
			
			// Hit point outside radius
			if (squaredLength > this.radius*this.radius){
				return false;
			}
			
			if (planeInfo.time < info.getTime()) { // if this intersection is the closest so far (or first)        	
				info.setTime(planeInfo.time);
				info.setHitPoint(ray.atTime(planeInfo.time));
				info.setNormal(this.normal);
				info.setMaterial(this.material);
				info.setObjectName(this.name);
			}

			return true;
			
		}
		
		return false;
	}

	@Override
	double u(Vector hitPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	double v(Vector hitPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

}
