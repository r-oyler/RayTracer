package rayTracer;

public class Triangle extends MatObject {

	// Points 1 and 2, point 0 is in Object
	Vector p1;
	Vector p2;

	Vector normal;

	// Viewing from front side, specify points clockwise
	Triangle(Vector p0, Vector p1, Vector p2, Material m) {
		super(p0, m);
		this.p1 = p1;
		this.p2 = p2;

		// Two sides of triangle = two vectors on plane of triangle
		Vector v1 = p1.minus(p0);
		Vector v2 = p2.minus(p0);
		// Normal is perpendicular to two vectors on plane
		this.normal = v2.crossProduct(v1);
	}
	
	// Specify points anti-clockwise
	static public Triangle revTriangle(Vector p0, Vector p1, Vector p2, Material m) {
		return new Triangle(p2,p1,p0, m);
	}

	// Return triangle where p1 and p2 and given relative to p1
	static public Triangle relTriangle(Vector p0, Vector p1, Vector p2, Material m) {
		return new Triangle(p0, p0.plus(p1), p0.plus(p2), m);
	}
	
	Vector centre() {
		return (this.p0.plus(this.p1).plus(this.p2)).divide(3);
	}
	
	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {

		// Starts by doing intersection with plane that triangle is on

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

		if (time < info.getTime()) {

			Vector hitPoint = ray.atTime(time);

			// Project triangle onto plane xy, yz or xz
			// Choose by dropping dimension that has largest value of normal

			Vector p;
			Vector p0;
			Vector p1;
			Vector p2;


			if (this.normal.x() >= this.normal.y()) { // x or z is largest

				if (this.normal.x() >= this.normal.z()) { // x is largest
					p = new Vector(hitPoint.d[1],hitPoint.d[2]);
					p0 = new Vector(this.p0.d[1],this.p0.d[2]);
					p1 = new Vector(this.p1.d[1],this.p1.d[2]);
					p2 = new Vector(this.p2.d[1],this.p2.d[2]);
				}
				else { // z is largest
					p = new Vector(hitPoint.d[0],hitPoint.d[1]);
					p0 = new Vector(this.p0.d[0],this.p0.d[1]);
					p1 = new Vector(this.p1.d[0],this.p1.d[1]);
					p2 = new Vector(this.p2.d[0],this.p2.d[1]);
				}

			}

			else { // y or z is largest

				if (this.normal.d[1] >= this.normal.d[2]) { // y is largest
					p = new Vector(hitPoint.d[0],hitPoint.d[2]);
					p0 = new Vector(this.p0.d[0],this.p0.d[2]);
					p1 = new Vector(this.p1.d[0],this.p1.d[2]);
					p2 = new Vector(this.p2.d[0],this.p2.d[2]);
				}
				else { // z is largest
					p = new Vector(hitPoint.d[0],hitPoint.d[1]);
					p0 = new Vector(this.p0.d[0],this.p0.d[1]);
					p1 = new Vector(this.p1.d[0],this.p1.d[1]);
					p2 = new Vector(this.p2.d[0],this.p2.d[1]);
				}

			}

			double alpha = baryFunc(p1,p2,p)/baryFunc(p1,p2,p0);
			double beta = baryFunc(p2,p0,p)/baryFunc(p2,p0,p1);
			double gamma = baryFunc(p0,p1,p)/baryFunc(p0,p1,p2);

			if (!Util.isBetweenInc(alpha,0,1) || !Util.isBetweenInc(beta,0,1) || !Util.isBetweenInc(gamma,0,1)) {
				// Point is not within triangle
				return false;
			}

			// Point is within triangle
			info.setTime(time);
			info.setHitPoint(ray.atTime(time));
			info.setNormal(this.normal);
			info.setMaterial(this.material);
			info.setObjectName(this.name);
			
		}
		
		return true;
	}

	static double baryFunc(Vector pA, Vector pB, Vector p) {

		return (pA.d[1] - pB.d[1])*p.d[0] + (pB.d[0] - pA.d[0])*p.d[1] + pA.d[0]*pB.d[1] - pB.d[0]*pA.d[1] ;

	}

}
