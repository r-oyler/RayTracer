package rayTracer;

public class Cylinder extends MatObject {

	Vector endB;
	double radius;

	Cylinder(Vector endA, Material m, Vector endB, double radius) {
		super(endA, m);
		this.endB = endB;
		this.radius = radius;
	}

	@Override
	// http://iquilezles.org/www/articles/intersectors/intersectors.htm
	boolean Intersect(Ray ray, IntersectInfo info) {

		Vector endA = this.p0;

		Vector span = endB.minus(endA);
		Vector endAToR = ray.origin.minus(endA);

		double span_span = span.dotProduct(span);
		double span_rD = span.dotProduct(ray.direction);
		double span_endAToR = span.dotProduct(endAToR);

		double a = span_span - span_rD * span_rD;
		double b = span_span * endAToR.dotProduct(ray.direction) - (span_endAToR * span_rD);
		double c = span_span * endAToR.dotProduct(endAToR) - (span_endAToR * span_endAToR) - (this.radius*this.radius*span_span);

		double h = b*b - a*c;

		if (h < 0) {
			return false;
		}

		h = Math.sqrt(h);

		double time = (-b-h)/a;

		double y = span_endAToR + time*span_rD;

		if (time > 0) {

			// body
			if (y > 0 && y < span_span) {

				Vector hitPoint = ray.atTime(time);

				Vector normal = endAToR.plus((hitPoint).minus(span.timesConst(y/span_span)).divide(this.radius));

				info.updateInfo(time, hitPoint, normal, this);

				return true;

			}

		}

		// caps
		time = (((y<0.0)?0.0:span_span) - span_endAToR)/span_rD;

		if (time > 0) {

			if (Math.abs(b+a*time) < h) {

				Vector hitPoint = ray.atTime(time);

				Vector normal = span.timesConst(Util.sign(y)/span_span).normalize();

				info.updateInfo(time, hitPoint, normal, this);

				return true;

			}

		}

		return false;
	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cylinder clone() {
		Cylinder clone = new Cylinder(this.p0.clone(), this.material.clone(), this.endB.clone(), radius);
		return clone;
	}



}
