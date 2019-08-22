package rayTracer;

public class Cone extends MatObject {

	Vector endB;
	double radiusA;
	double radiusB;

	Cone(Vector endA, Material m, Vector endB, double radiusA, double radiusB) {
		super(endA, m);
		this.endB = endB;
		this.radiusA = radiusA;
		this.radiusB = radiusB;
	}

	// http://iquilezles.org/www/articles/intersectors/intersectors.htm
	boolean Intersect(Ray ray, IntersectInfo info) {

		Vector endA = this.p0;

		Vector span = endB.minus(endA);
		Vector endAToR = ray.origin.minus(endA);
		Vector endBToR = ray.origin.minus(endB);

		double m0 = span.dotProduct(span);
		double m1 = endAToR.dotProduct(span);
		double m2 = ray.direction.dotProduct(span);
		double m3 = ray.direction.dotProduct(endAToR);
		double m5 = endAToR.squared();
		double m9 = endBToR.dotProduct(span);

		// caps
		if (m1 < 0) {
			if ((endAToR.timesConst(m2).minus(ray.direction.timesConst(m1))).squared() < (this.radiusA*this.radiusA*m2*m2)) {

				double time = -m1/m2;

				if (time > 0) {

					Vector hitPoint = ray.atTime(time);

					Vector normal = span.timesConst(-Util.inverseSqrt(m0));

					info.updateInfo(time, hitPoint, normal, this);

					return true;

				}

			}
		}

		else if(m9 > 0) {

			double time = -m9/m2;

			if (time > 0) {

				if (endBToR.plus(ray.direction.timesConst(time)).squared() < (radiusB*radiusB)) {

					Vector hitPoint = ray.atTime(time);

					Vector normal = span.timesConst(Util.inverseSqrt(m0));

					info.updateInfo(time, hitPoint, normal, this);

					return true;

				}

			}

		}

		// body
		double rr = radiusA - radiusB;
		double hy = m0 + rr*rr;
		double k2 = m0*m0		-m2*m2*hy;
		double k1 = m0*m0*m3	-m1*m2*hy	+m0*radiusA*(rr*m2);
		double k0 = m0*m0*m5	-m1*m1*hy	+m0*radiusA*(rr*m1*2 -m0*radiusA);
		double h = k1*k1 - k2*k0;

		if (h < 0) { // no intersection
			return false;
		}

		double time = (-k1-Math.sqrt(h))/k2;
		double y = m1 + time*m2;

		if (y < 0 || y > m0) { // no intersection
			return false;
		}

		if (time > 0) {

			Vector hitPoint = ray.atTime(time);

			Vector normal = endAToR.plus(ray.direction.timesConst(time)).timesConst(m0);
			normal = normal.plus(span.timesConst(rr*radiusA)).timesConst(m0);
			normal = normal.minus(span.timesConst(hy*y));

			info.updateInfo(time, hitPoint, normal, this);

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
	public MatObject clone() {
		Cone clone = new Cone(this.p0.clone(), this.material.clone(), this.endB.clone(), this.radiusA, this.radiusB);
		return clone;
	}

}
