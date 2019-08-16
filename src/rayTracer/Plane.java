package rayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;

// A plane, defined by a point on the plane and a normal vector
public class Plane extends MatObject {

	// Used for naming object
	static int count = 0;
	
	Vector normal;

	Plane(Vector p, Material m, Vector normal) {
		super(p, m);
		this.normal = normal.normalize();
		
		count++;
		this.name = "Plane " + count;
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {

		double cosAngle = this.normal.cosAngleBetween(ray.direction);
		
		if (cosAngle == 0) return false;
		
		double denom = ray.direction.dotProduct(this.normal);

		if (Math.abs(denom) < 0.0001) {	// If dot product close to zero, ray and plane are parallel, so don't intersect
			return false;
		}

		double numer = (this.Position().minus(ray.origin)).dotProduct(this.normal);

		double time = numer/denom;
		
		if (denom == Double.NaN) {
        	System.out.println("Error");
        }
		
		if (time < 0) {	// If time is negative, plane is behind ray origin
			return false;
		}
		
		if (time <= 0.01) {
			return false;
		}

		if (time < info.getTime()) { // if this intersection is the closest so far (or first)        	
			Vector hitPoint = ray.atTime(time);
			
			info.setTime(time);
			info.setHitPoint(hitPoint);
			info.setNormal(this.normal);
			info.setMaterial(this.material);
			
			Vector color = this.getUVcolor(this.calcUV(hitPoint));
			info.setUVcolor(color);
			info.setObject(this);
			info.setObjectName(this.name);
		}

		return true;
	}
	
	double distanceToPoint(Vector p) {
		
		if (p.dim !=3) throw new IllegalArgumentException("Point must be vector of dimension 3");
		
		Vector planeOriginToPoint = p.minus(this.Position());
		
		Vector planeToPoint = this.normal.timesConst(planeOriginToPoint.dotProduct(this.normal));
		
		return planeToPoint.length();
		
	}

	@Override
	public Vector calcUV(Vector hitPoint) {
		
		Vector u = computePrimaryTexDir(this.normal).normalize();
		Vector v = this.normal.crossProduct(u);
		
		double[] uv = new double[2];
		
		uv[0] = u.dotProduct(hitPoint);
		uv[1] = v.dotProduct(hitPoint);
		
		uv[0] = uv[0] % 1;
		
		if (uv[0] < 0) {
			uv[0] = uv[0] + 1;
		}
		
		uv[1] = uv[1] % 1;
		
		if (uv[1] < 0) {
			uv[1] = uv[1] + 1;
		}
		
		return new Vector(uv);
		
	}

	
	// https://computergraphics.stackexchange.com/a/8384
	Vector computePrimaryTexDir(Vector normal)
	{
	    Vector a = normal.crossProduct(new Vector(1, 0, 0));
	    Vector b = normal.crossProduct(new Vector(0, 1, 0));

	    Vector max_ab = a.length() < b.length() ? b : a;

	    Vector c = normal.crossProduct(new Vector(0, 0, 1));

	    return (max_ab.length() < c.length() ? c.normalize() : max_ab.normalize());
	}
	
	@Override
	public MatObject clone() {
		Plane clone = new Plane(this.p0.clone(),this.material.clone(),this.normal.clone());
	
		if (this.hasTextureMap) {
			clone.addTextureMap(Util.deepCopy(this.texture));
		}
		
		return clone;
		
	}
	
}
