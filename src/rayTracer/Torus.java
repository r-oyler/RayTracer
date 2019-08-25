package rayTracer;

public class Torus extends MatObject {

	double radiusMajor;
	double radiusMinor;

	Torus(Vector p, Material m, double radiusOutter, double radiusInner) {
		super(p, m);
		this.radiusMajor = radiusOutter;
		this.radiusMinor = radiusInner;
	}

	// http://iquilezles.org/www/articles/intersectors/intersectors.htm
	// https://www.shadertoy.com/view/4sBGDy
	boolean Intersect(Ray ray, IntersectInfo info) {
		
		Matrix4 objToWorld = this.transform;
		Matrix4 worldToObj = Invert.invert(objToWorld);

		// convert from world to object space
		Vector rayDirectionOS = worldToObj.timesV(ray.direction.addDim(0)).dropDim();
		Vector rayOriginOS = worldToObj.timesV(ray.origin.addDim(1)).dropDim();

		double po = 1.0;
		
		double Ra2 = this.radiusMajor * this.radiusMajor;
		double ra2 = this.radiusMinor * this.radiusMinor;
		
		double m = rayOriginOS.dotSelf();
		double n = rayOriginOS.dotProduct(rayDirectionOS);
		
		double k = (m - ra2 - Ra2)/2.0;
		double k3 = n;
		double k2 = n*n + Ra2*rayDirectionOS.z()*rayDirectionOS.z() + k;
		double k1 = k*n + Ra2*rayOriginOS.z()*rayDirectionOS.z();
		double k0 = k*k + Ra2*rayOriginOS.z()*rayOriginOS.z() - Ra2*ra2;

		// prevent |c1| from being too close to zero
		if (Math.abs(k3*(k3*k3-k2)+k1) < 0.01) {

			po = -1.0;
			double tmp = k1; k1=k3; k3=tmp;
			k0 = 1.0/k0;
			k1 = k1*k0;
			k2 = k2*k0;
			k3 = k3*k0;

		}

		double c2 = 2.0*k2 - 3.0*k3*k3;
	    double c1 = k3*(k3*k3 - k2) + k1;
	    double c0 = k3*(k3*(-3.0*k3*k3 + 4.0*k2) - 8.0*k1) + 4.0*k0;

		c2 /= 3.0;
		c1 *= 2.0;
		c0 /= 3.0;

		double Q = c2*c2 + c0;
		double R = 3.0*c0*c2 - c2*c2*c2 - c1*c1;
		
		double h = R*R - Q*Q*Q;
		double z = 0.0;
		
		if (h < 0.0) {
			// 4 intersections
			double sQ = Math.sqrt(Q);
			z = 2.0*sQ*Math.cos(Math.acos(R/(sQ*Q))/3.0);
		}
		else {
			// 2 intersections
			double sQ = Math.pow(Math.sqrt(h)+Math.abs(R), 1.0/3.0);
			z = Util.sign(R)*Math.abs(sQ + Q/sQ);
		}
		z = c2 -z;
		
		double d1 = z	- 3.0*c2;
		double d2 = z*z	- 3.0*c0;
		if (Math.abs(d1) < 1e-4) {
			if (d2 < 0.0) return false;
			d2 = Math.sqrt(d2);
		}
		else {
			if (d1 < 0.0) return false;
			d1 = Math.sqrt(d1/2.0);
			d2 = c1/d1;
		}
		
		double time = 1e20;
		
		h = d1*d1 - z + d2;
		if (h > 0.0) {
			h = Math.sqrt(h);
			double t1 = -d1 - h - k3; t1 = (po<0.0)?2.0/t1:t1;
			double t2 = -d1 + h - k3; t2 = (po<0.0)?2.0/t2:t2;
			if (t1 > 0.0) time = t1;
			if (t2 > 0.0) time = Math.min(time, t2);
		}
		
		h = d1*d1 - z - d2;
		if (h > 0.0) {
			h = Math.sqrt(h);
			double t1 = d1 - h - k3; t1 = (po<0.0)?2.0/t1:t1;
			double t2 = d1 + h - k3; t2 = (po<0.0)?2.0/t2:t2;
			if (t1 > 0.0) time = Math.min(time, t1);
			if (t2 > 0.0) time = Math.min(time, t2);
		}
		
		if (time > 1e19) return false;
		
		Vector hitPointOS = rayOriginOS.plus(rayDirectionOS.timesConst(time));
		Vector normalOS = this.calcNormal(hitPointOS);
		
		Vector hitPoint = ray.atTime(time);
		Vector normal = objToWorld.timesV(normalOS.addDim(0)).dropDim();
		
		info.updateInfo(time, hitPoint, normal, this);
		
		return true;
		
	}

	private Vector calcNormal(Vector hitPointOS) {

		double d = hitPointOS.dotSelf();
		double r2 = this.radiusMinor*this.radiusMinor;
		double R2 = this.radiusMajor*this.radiusMajor;
		Vector v = new Vector(1.0,1.0,-1.0);
		Vector t1 = new Vector(d-r2,d-r2,d-r2);
		Vector t2 = v.timesConst(R2);

		return hitPointOS.multComponents(t1.minus(t2));

	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatObject clone() {
		Torus clone = new Torus(this.p0.clone(), this.material.clone(), this.radiusMajor, this.radiusMinor);
		return clone;
	}

}
