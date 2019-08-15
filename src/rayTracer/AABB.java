package rayTracer;

// An axis aligned bounding box, defined by 2 vectors at the min and max points
// Source: https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-box-intersection
public class AABB extends MatObject {

	Vector[] bounds = new Vector[2];

	AABB(Material m, Vector vMin, Vector vMax) {
		super(vMin, m);

		boolean error = false;

		for (int i = 0; i < 3; i++) {
			error &= vMin.d[i] <= vMax.d[i];
		}

		if (error) throw new IllegalArgumentException("vMin must be less than or equal to vMax in all dimensions");

		this.bounds[0] = vMin;
		this.bounds[1] = vMax;
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {

		double tMin = (bounds[ray.sign[0]].x() - ray.origin.x()) * ray.invDirection.x();
		double tMax = (bounds[1-ray.sign[0]].x() - ray.origin.x()) * ray.invDirection.x(); 
		double tYMin = (bounds[ray.sign[1]].y() - ray.origin.y()) * ray.invDirection.y();
		double tYMax = (bounds[1-ray.sign[1]].y() - ray.origin.y()) * ray.invDirection.y(); 

		int[] trace = {0,0,0,0};

		if ((tMin > tYMax) || (tYMin > tMax)) {
			return false; 
		}

		if (tYMin > tMin) {
			tMin = tYMin; 
			trace[0] = 1;
		}

		if (tYMax < tMax) {
			tMax = tYMax; 
			trace[1] = 1;
		}

		double tZMin = (bounds[ray.sign[2]].z() - ray.origin.z()) * ray.invDirection.z(); 
		double tZMax = (bounds[1-ray.sign[2]].z() - ray.origin.z()) * ray.invDirection.z(); 

		if ((tMin > tZMax) || (tZMin > tMax)) {
			return false; 
		}

		if (tZMin > tMin) {
			tMin = tZMin; 
			trace[2] = 1;
		}

		if (tZMax < tMax) {
			tMax = tZMax;
			trace[3] = 1;
		}

		double time = tMax;

		if (time < info.getTime()) { // if this intersection is the closest so far (or first)        	
			info.setTime(time);
			Vector hitPoint = ray.atTime(time);
			info.setHitPoint(hitPoint);

			Vector normal = calcNormal(hitPoint);
			
			info.setNormal(normal);
			info.setMaterial(this.material);
			info.setObject(this);
			info.setObjectName(this.name);
		}

		return true;
	}

	public Vector centre() {
		return this.bounds[1].plus(this.bounds[0]).divide(2);
	}

	
	// Source: https://blog.johnnovak.net/2016/10/22/the-nim-raytracer-project-part-4-calculating-box-normals/
	Vector calcNormal(Vector hitPoint){
		
		Vector centre = this.centre();

		Vector centreToPoint = hitPoint.minus(centre);

		Vector divisor = this.bounds[0].minus(this.bounds[1]).divide(2);
		
		double bias = 1.000001;

		double x = (int)(centreToPoint.x() / Math.abs(divisor.x()) * bias);
		double y = (int)(centreToPoint.y() / Math.abs(divisor.y()) * bias);
		double z = (int)(centreToPoint.z() / Math.abs(divisor.z()) * bias);
		
		return new Vector(x,y,z).normalize();
	}

	// Source: https://stackoverflow.com/questions/34030278/calculating-normal-of-bbox-cube
	Vector computeNormal(Vector intersection) {

		Vector[] normals = {new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1)};

		Vector interRelative = intersection.minus(this.centre());

		double xyCoef = interRelative.y() / interRelative.x();
		double zyCoef = interRelative.y() / interRelative.z();

		int coef = (Util.isBetweenInc(xyCoef, 1, -1) ? 1 :
			(Util.isBetweenExc(zyCoef, 1, -1) ? 2 : 0));
		// Here it's exclusive to avoid coef to be 3
		return normals[coef].multComponents(interRelative.sign()); // The sign he is used to know direction of the normal

	}

	@Override
	double u(Vector hitPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	double v(Vector hitPoint) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public AABB clone() {
		return new AABB(material.clone(), this.bounds[0].clone(), this.bounds[1].clone());
	}

}
