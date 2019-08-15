package rayTracer;

// A plane, defined by a point on the plane and a normal vector
public class Plane extends MatObject {

	// Used for naming object
	static int count;
	
	Vector normal;

	Plane(Vector p, Material m, Vector normal) {
		super(p, m);
		this.normal = normal.normalize();
		
		count++;
		this.name = "Plane " + count;
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {

		double cosAngle = this.normal.cosAngleBetween(ray.direction);
		
		if (cosAngle == 0) return false;
		
		double denom = ray.direction.dotProduct(this.normal);

		if (Math.abs(denom) < 0.0001) {	// If dot product close to zero, ray and plane are parallel, so don't intersect
			return false;
		}

		double numer = (this.Position().minus(ray.origin)).dotProduct(this.normal);

		double time = numer/denom;
		
		if (denom == Double.NaN) {
        	System.out.println("Error");
        }
		
		if (time < 0) {	// If time is negative, plane is behind ray origin
			return false;
		}
		
		if (time <= 0.01) {
			return false;
		}

		if (time < info.getTime()) { // if this intersection is the closest so far (or first)        	
			info.setTime(time);
			info.setHitPoint(ray.atTime(time));
			info.setNormal(this.normal);
			info.setMaterial(this.material);
			info.setObject(this);
			info.setObjectName(this.name);
		}

		return true;
	}
	
	double distanceToPoint(Vector p) {
		
		if (p.dim !=3) throw new IllegalArgumentException("Point must be vector of dimension 3");
		
		Vector planeOriginToPoint = p.minus(this.Position());
		
		Vector planeToPoint = this.normal.times(planeOriginToPoint.dotProduct(this.normal));
		
		return planeToPoint.length();
		
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

	@Override
	public MatObject clone() {
		return new Plane(this.p0.clone(),this.material.clone(),this.normal.clone());
	}
	
}
