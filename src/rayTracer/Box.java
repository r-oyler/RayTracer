package rayTracer;

public class Box extends MatObject {

	// Used for naming object
	static int count = 0;

	Vector boxSize;

	Box(Vector p, Material m, Vector boxSize) {
		super(p, m);

		this.boxSize = boxSize;

		count++;
		this.name = "Box " + count;

	}

	@Override
	// Source: https://www.shadertoy.com/view/tl23Rm http://iquilezles.org/www/articles/boxfunctions/boxfunctions.htm
	boolean Intersect(Ray ray, IntersectInfo info) {

		Matrix4 boxToWorld = this.transform;
		Matrix4 worldToBox = Invert.invert(boxToWorld);

		// convert from world to box space
		Vector rayDirectionBS = worldToBox.timesV(ray.direction.addDim(0)).dropDim();
		Vector rayOriginBS = worldToBox.timesV(ray.origin.addDim(1)).dropDim(); 

		Vector m = rayDirectionBS.sign().divComponents(rayDirectionBS.absolute());
		Vector n = m.multComponents(rayOriginBS);
		Vector k = m.absolute().multComponents(this.boxSize);
		Vector t1 = n.timesConst(-1).minus(k);
		Vector t2 = n.timesConst(-1).plus(k);

		double tN = Math.max( Math.max( t1.x(), t1.y() ), t1.z() );
		double tF = Math.min( Math.min( t2.x(), t2.y() ), t2.z() );
		if( tN>tF || tF<=0.0) {
			return false; // no intersection
		}

		double time = tN;

		if (time < info.getTime()) {
			
			Vector hitPoint = ray.atTime(time);
			
			Vector t1yzx = new Vector(t1.y(),t1.z(),t1.x());
			Vector t1xyz = new Vector(t1.x(),t1.y(),t1.z());
			Vector t1zxy = new Vector(t1.z(),t1.x(),t1.y());

			Vector normal = rayDirectionBS.sign().timesConst(-1);
			normal = normal.multComponents(t1xyz.step(t1yzx));
			normal = normal.multComponents(t1xyz.step(t1zxy));

			normal = boxToWorld.timesV(normal.addDim(0)).dropDim();
			
			info.updateInfo(time, hitPoint, normal, this);

		}

		return true;
	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Box clone() {
		Box clone = new Box(this.p0.clone(),this.material.clone(),this.boxSize.clone());
		clone.transform = this.transform;
		return clone;
	}

}
