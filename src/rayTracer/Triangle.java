package rayTracer;

public class Triangle extends MatObject {

	// Used for naming object
	static int count = 0;

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
		this.normal = v1.crossProduct(v2);

		count++;
		this.name = "Triangle " + count;

	}

	// Specify points anti-clockwise
	static public Triangle revTriangle(Vector p0, Vector p1, Vector p2, Material m) {
		return new Triangle(p2,p1,p0, m);
	}

	Vector centre() {
		return (this.p0.plus(this.p1).plus(this.p2)).divide(3);
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {

		Vector p0p1 = p1.minus(p0);
		Vector p0p2 = p2.minus(p0);

		Vector pvec = ray.direction.crossProduct(p0p2);

		double det = p0p1.dotProduct(pvec);

		if (det < 0.00001) return false;

		double invDet = 1/det;

		Vector tvec = ray.origin.minus(p0);
		double u = tvec.dotProduct(pvec) * invDet;

		if (!Util.isBetweenInc(u, 0, 1)) return false;

		Vector qvec = tvec.crossProduct(p0p1);
		double v = ray.direction.dotProduct(qvec) * invDet;

		if (v < 0 || u + v > 1) return false;

		double time = p0p2.dotProduct(qvec) * invDet;

		if (time < 0) return false;

		if (time < info.getTime()) {

			Vector hitPoint = ray.atTime(time);

			info.updateInfo(time, hitPoint, normal, this);

		}

		return true;

	}

	static double baryFunc(Vector pA, Vector pB, Vector p) {

		return (pA.y() - pB.y())*p.x() + (pB.x() - pA.x())*p.y() + pA.x()*pB.y() - pB.x()*pA.y() ;

	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Triangle clone() {
		return new Triangle(this.p0.clone(), this.p1.clone(), this.p2.clone(), this.material.clone());
	}

}
