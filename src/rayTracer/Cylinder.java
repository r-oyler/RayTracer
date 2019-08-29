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
	// https://www.shadertoy.com/view/4lcSRn
	boolean Intersect(Ray ray, IntersectInfo info) {

		Vector pa = this.p0;
		Vector pb = this.endB;
		Vector ro = ray.origin;
		Vector rd = ray.direction;
		double ra = this.radius;

		Vector ba = pb.minus(pa);

		Vector oc = ro.minus(pa);

		double baba = ba.dotSelf();
		double bard = ba.dotProduct(rd);
		double baoc = ba.dotProduct(oc);

		double k2 = baba            - bard*bard;
		double k1 = baba*oc.dotProduct(rd) - baoc*bard;
		double k0 = baba*oc.dotSelf() - baoc*baoc - ra*ra*baba;

		double h = k1*k1 - k2*k0;
		if( h<0.0 ) return false;
		h = Math.sqrt(h);
		double t = (-k1-h)/k2;

		// body
		double y = baoc + t*bard;
		if( y>0.0 && y<baba ) {

			if (t > 0) {
				Vector hitPoint = ray.atTime(t);
				Vector normal = (rd.timesConst(t).plus(oc).minus(ba.timesConst(y/baba))).divide(ra);

				info.updateInfo(t, hitPoint, normal, this);

				return true;
			}
		}

		// caps
		t = ( ((y<0.0) ? 0.0 : baba) - baoc)/bard;
		if( Math.abs(k1+k2*t)<h )
		{
			if (t > 0) {
				Vector hitPoint = ray.atTime(t);
				Vector normal =  ba.timesConst(Util.sign(y/baba));
				
				info.updateInfo(t, hitPoint, normal, this);
				
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

	public Vector centre() {
		return this.p0.plus(this.endB.minus(p0).timesConst(0.5));
	}

	@Override
	public Cylinder clone() {
		Cylinder clone = new Cylinder(this.p0.clone(), this.material.clone(), this.endB.clone(), radius);
		return clone;
	}



}
