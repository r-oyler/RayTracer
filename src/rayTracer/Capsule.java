package rayTracer;

public class Capsule extends MatObject {

	Vector p1;
	double radius;

	Capsule(Vector p0, Material m, Vector p1, double radius) {
		super(p0, m);
		this.p1 = p1;
		this.radius = radius;
	}

	@Override
	// http://www.iquilezles.org/www/articles/intersectors/intersectors.htm
	boolean Intersect(Ray ray, IntersectInfo info) {

		Vector span = this.p1.minus(p0);
		Vector cToR = ray.origin.minus(this.p0);

		double span_span = span.squared();
		double span_rD = span.dotProduct(ray.direction);
		double span_cToR = span.dotProduct(cToR);
		double rD_cToR = ray.direction.dotProduct(cToR);
		double cToR_cToR = cToR.squared();

		double a = span_span - span_rD*span_rD;
		double b = span_span*rD_cToR - span_cToR*span_rD;
		double c = span_span*cToR_cToR - span_cToR*span_cToR - this.radius*this.radius*span_span;

		double h = b*b - a*c;

		if (h > 0) {

			double time = (-b-Math.sqrt(h))/a;

			double y = span_cToR + time*span_rD;

			if (time > 0) {

				// body
				if (y > 0 && y < span_span) {

					Vector hitPoint = ray.atTime(time);

					Vector cToHit = hitPoint.minus(this.p0);

					double h1 = Util.clamp((cToHit.dotProduct(span)/span.squared()), 0, 1);

					Vector normal = cToHit.minus(span.timesConst(h1)).divide(this.radius);

					info.updateInfo(time, hitPoint, normal, this);

					return true;

				}

			}

			// caps

			Vector oc = (y <= 0) ? cToR : ray.origin.minus(this.p1);

			b = ray.direction.dotProduct(oc);
			c = oc.squared() - this.radius*this.radius;

			h = b*b - c;

			if (h > 0) {

				time = -b -Math.sqrt(h);

				if (time > 0) {

					Vector hitPoint = ray.atTime(time);

					Vector cToHit = hitPoint.minus(this.p0);

					double h1 = Util.clamp((cToHit.dotProduct(span)/span.squared()), 0, 1);

					Vector normal = cToHit.minus(span.timesConst(h1)).divide(this.radius);

					info.updateInfo(time, hitPoint, normal, this);

					return true;

				}

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
	public MatObject clone() {
		Capsule clone = new Capsule(this.p0.clone(),this.material.clone(),this.p1.clone(),this.radius);
		return clone;
	}


}
