package rayTracer;

public class Ellipsoid extends MatObject {

	Vector radii;

	Ellipsoid(Vector p, Material m, Vector radii) {
		super(p, m);
		this.radii = radii;
	}

	// https://www.shadertoy.com/view/llcfRf
	boolean Intersect(Ray ray, IntersectInfo info) {

		Matrix4 objToWorld = this.transform;
		Matrix4 worldToObj = Invert.invert(objToWorld);

		// convert from world to object space
		Vector rayDirectionOS = worldToObj.timesV(ray.direction.addDim(0)).dropDim();
		Vector rayOriginOS = worldToObj.timesV(ray.origin.addDim(1)).dropDim();

		Vector ocn = rayOriginOS.divComponents(this.radii);
		Vector rdn = rayDirectionOS.divComponents(this.radii);

		double a = rdn.dotSelf();
		double b = ocn.dotProduct(rdn);
		double c = ocn.dotSelf();

		double h = b*b - a*(c-1);

		if (h < 0) {
			return false;
		}

		h = Math.sqrt(h);

		double time = (-b-h)/a;
		double time2 = (-b+h)/a;

		if (time < 0) {
			time = time2;
		}

		if (time < 0){
			return false;
		}

		if (time < info.getTime()) {

			Vector hitPointOS = rayOriginOS.plus(rayDirectionOS.timesConst(time));
			Vector normalOS = this.calcNormal(hitPointOS);

			Vector hitPoint = ray.atTime(time);
			Vector normal = objToWorld.timesV(normalOS.addDim(0)).dropDim();

			info.updateInfo(time, hitPoint, normal, this);

		}

		return true;
	}

	private Vector calcNormal(Vector hitPointBS) {
		return hitPointBS.divComponents(this.radii).normalize();
	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatObject clone() {
		Ellipsoid clone = new Ellipsoid(this.p0.clone(), this.material.clone(), this.radii.clone());

		return clone;
	}

}
