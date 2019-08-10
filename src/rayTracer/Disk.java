package rayTracer;

// A disk, defined by a centre point, normal vector and radius
public class Disk extends MatObject {

	Vector normal;
	double radius;
	
	Disk(Vector p, Material m, Vector normal, double r) {
		super(p, m);
		this.normal = normal;
		this.radius = r;
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {
		// TODO Auto-generated method stub
		return false;
	}

}
