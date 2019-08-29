package rayTracer;

// A disk, defined by a centre point, normal vector and radius
public class Disk extends MatObject {

	// Used for naming object
	static int count = 0;

	Vector normal;
	double radius;

	Disk(Vector p, Material m, Vector normal, double r) {
		super(p, m);
		this.normal = normal;
		this.radius = r;
		
		count++;
		this.name = "Disk " + count;
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
				Vector hitPoint = ray.atTime(planeInfo.time);
				
				info.setTime(planeInfo.time);
				info.setHitPoint(hitPoint);
				info.setNormal(this.normal);
				info.setMaterial(this.material);
				
				if (this.hasTextureMap) {
	            	Vector color = this.getUVcolor(this.calcUV(hitPoint));
	    			info.setUVcolor(color);
	            }
	            else {
	            	info.setHasNoUV();
	            }
				
				info.setObject(this);
				info.setObjectName(this.name);
			}

			return true;

		}

		return false;
	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Disk clone() {
		return new Disk(this.p0.clone(),this.material.clone(),this.normal.clone(),this.radius);
	}

}
