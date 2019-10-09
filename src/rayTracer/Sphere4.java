package rayTracer;

public class Sphere4 extends MatObject {

	double radius;

	Sphere4(Vector p, Material m, double radius) {
		super(p, m);
		this.radius = radius;
	}

	// https://www.shadertoy.com/view/3tj3DW
	boolean Intersect(Ray ray, IntersectInfo info) {

		Matrix4 objToWorld = this.transform;
		Matrix4 worldToObj = Invert.invert(objToWorld);

		// convert from world to object space
		Vector rayDirectionOS = worldToObj.timesVDirection(ray.direction);
		Vector rayOriginOS = worldToObj.timesVPoint(ray.origin);

		// solve quartic equation

		double r2 = this.radius*this.radius;
		Vector d2 = rayDirectionOS.multComponents(rayDirectionOS); Vector d3 = d2.multComponents(rayDirectionOS);
		Vector o2 = rayOriginOS.multComponents(rayOriginOS); Vector o3 = o2.multComponents(rayOriginOS);

		double ka = 1.0/(d2.dotSelf());

		double k3 = ka* rayOriginOS.dotProduct(d3);
		double k2 = ka* o2.dotProduct(d2);
		double k1 = ka* o3.dotProduct(rayDirectionOS);
		double k0 = ka*(o2.dotSelf() - r2*r2);

		// solve cubic

		double c2 = k2 - k3*k3;
		double c1 = k1 + 2.0*k3*k3*k3 - 3.0*k3*k2;
		double c0 = k0 - 3.0*k3*k3*k3*k3 + 6.0*k3*k3*k2 - 4.0*k3*k1;

		double p = c2*c2 + c0/3.0;
		double q = c2*c2*c2 - c2*c0 + c1*c1;

		double h = q*q - p*p*p;

		// skip the case of three real solutions for the cubic, which involves four
		// complex solutions for the quartic, since we know this objcet is convex

		if (h<0.0) return false;

		// one real solution, two complex (conjugated)
		double sh = Math.sqrt(h);

		double s = Math.signum(q+sh)*Math.pow(Math.abs(q+sh), 1.0/3.0); // cuberoot
		double t = Math.signum(q-sh)*Math.pow(Math.abs(q-sh), 1.0/3.0); // cuberoot
		Vector w = new Vector(s+t,s-t);

		// the quartic will have two real solutions and two complex solutions.
		// we only want the real ones

		Vector v = new Vector(w.x()+c2*4.0, w.y()*Math.sqrt(3.0)).timesConst(0.5);
		double r = v.length();
		double time = -Math.abs(v.y())/Math.sqrt(r+v.x()) - c1/r -k3;

		if (time < 0.0) return false;

		if (time < info.getTime()) {

			Vector hitPointOS = rayOriginOS.plus(rayDirectionOS.timesConst(time));
			Vector normalOS = this.calcNormal(hitPointOS);

			Vector hitPoint = ray.atTime(time);
			Vector normal = objToWorld.timesVDirection(normalOS);

			info.updateInfo(time, hitPoint, normal, this);

		}

		return true;
	}

	private Vector calcNormal(Vector hitPointOS) {
		return hitPointOS.multComponents(hitPointOS).multComponents(hitPointOS);
	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatObject clone() {
		Sphere4 clone = new Sphere4(this.p0.clone(),this.material.clone(),this.radius);
		return clone;
	}



}
