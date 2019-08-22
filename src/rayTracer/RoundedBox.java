package rayTracer;

public class RoundedBox extends MatObject{

	// box extends from +boxSize to -boxSize in each dimension
	Vector boxSize;
	double radius;

	RoundedBox(Vector p, Material m, Vector boxSize, double radius) {
		super(p, m);
		
		if (radius >= boxSize.min()) throw new IllegalArgumentException("Radius cannot be larger than the smallest box size dimension");
		
		this.boxSize = boxSize;
		this.radius = radius;
	}

	// http://iquilezles.org/www/articles/intersectors/intersectors.htm
	boolean Intersect(Ray ray, IntersectInfo info) {

		Matrix4 boxToWorld = this.transform;
		Matrix4 worldToBox = Invert.invert(boxToWorld);

		// convert from world to box space
		Vector rayDirectionBS = worldToBox.timesV(ray.direction.addDim(0)).dropDim();
		Vector rayOriginBS = worldToBox.timesV(ray.origin.addDim(1)).dropDim();

		// bounding box
		Vector m = rayDirectionBS.inverseDivide(1);
		Vector n = m.multComponents(rayOriginBS);
		Vector k = m.absolute().multComponents(this.boxSize.plus(this.radius));
		Vector t1 = n.negative().minus(k);
		Vector t2 = n.negative().plus(k);

		double tN = Math.max(Math.max(t1.x(), t1.y()), t1.z());
		double tF = Math.min(Math.min(t2.x(), t2.y()), t2.z());

		if (tN > tF || tF < 0) {
			return false;
		}

		double time = tN;

		// convert to first octant
		Vector pos = rayOriginBS.plus(rayDirectionBS.timesConst(time));
		Vector s = pos.sign();

		Vector rayOriginBSOct = rayOriginBS.multComponents(s);
		Vector rayDirectionBSOct = rayDirectionBS.multComponents(s);
		pos = pos.multComponents(s);

		// faces
		pos = pos.minus(this.boxSize);
		Vector posYZX = new Vector(pos.y(),pos.z(),pos.x());
		pos = pos.max(posYZX);

		if (Math.min(Math.min(pos.x(), pos.y()), pos.z()) < 0) {

			if (time > 0) {
			
				Vector hitPointBS = rayOriginBS.plus(rayDirectionBS.timesConst(time));
				Vector normalBS = this.calcNormal(hitPointBS);
				
				Vector hitPoint = ray.atTime(time);
				Vector normal = boxToWorld.timesV(normalBS.addDim(0)).dropDim();
				
				info.updateInfo(time, hitPoint, normal, this);
				
				return true;
				
			}
			
		}

		// some precomputation
		Vector oc = rayOriginBSOct.minus(this.boxSize);
		Vector dd = rayDirectionBSOct.multComponents(rayDirectionBSOct);
		Vector oo = oc.multComponents(oc);
		Vector od = oc.multComponents(rayDirectionBSOct);

		double ra2 = this.radius*this.radius;

		time = 1e20;

		// corner
		{
			double b = od.x() + od.y() + od.z();
			double c = oo.x() + oo.y() + oo.z() - ra2;
			double h = b*b - c;

			if (h > 0) {
				time = -b -Math.sqrt(h);
			}
		}

		// edge X
		{
			double a = dd.y() + dd.z();
			double b = od.y() + od.z();
			double c = oo.y() + oo.z() - ra2;
			double h = b*b - a*c;

			if (h > 0) {
				h = (-b-Math.sqrt(h))/a;
				if (h > 0 && h < time && Math.abs(rayOriginBSOct.x() + rayDirectionBSOct.x() * h)< this.boxSize.x()) {
					time = h;
				}	
			}
		}

		// edge Y
		{
			double a = dd.z() + dd.x();
			double b = od.z() + od.x();
			double c = oo.z() + oo.x() - ra2;
			double h = b*b - a*c;

			if (h > 0) {
				h = (-b-Math.sqrt(h))/a;
				if (h > 0 && h < time && Math.abs(rayOriginBSOct.y() + rayDirectionBSOct.y() * h)< this.boxSize.y()) {
					time = h;
				}	
			}
		}

		// edge Z
		{
			double a = dd.x() + dd.y();
			double b = od.x() + od.y();
			double c = oo.x() + oo.y() - ra2;
			double h = b*b - a*c;

			if (h > 0) {
				h = (-b-Math.sqrt(h))/a;
				if (h > 0 && h < time && Math.abs(rayOriginBSOct.z() + rayDirectionBSOct.z() * h)< this.boxSize.z()) {
					time = h;
				}	
			}
		}
		
		if (time > 1e19) {
			return false;
		}

		if (time > 0) {
		
			Vector hitPointBS = rayOriginBS.plus(rayDirectionBS.timesConst(time));
			Vector normalBS = this.calcNormal(hitPointBS);
			
			Vector hitPoint = ray.atTime(time);
			Vector normal = boxToWorld.timesV(normalBS.addDim(0)).dropDim();
			
			info.updateInfo(time, hitPoint, normal, this);
			
			return true;
		
		}
		
		return false;
	}

	Vector calcNormal(Vector hitPoint) {
		
		Vector t = hitPoint.absolute().minus(this.boxSize).max(0).normalize();
		Vector normal = hitPoint.sign().multComponents(t);
		
		return normal;
		
	}
	
	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoundedBox clone() {
		
		RoundedBox clone = new RoundedBox(this.p0.clone(), this.material.clone(), this.boxSize.clone(), radius);
		clone.transform = this.transform.clone();
		
		return clone;
	}



}
