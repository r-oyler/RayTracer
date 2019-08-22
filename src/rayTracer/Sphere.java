package rayTracer;

// A sphere, defined by a centre point and radius
public class Sphere extends MatObject {
	
	// Used for naming object
	static int count = 0;
	
	double radius = 1f;

	Sphere(Vector p, Material m, double radius) {
		super(p, m);
		this.radius = radius;
		count++;
		this.name = "Sphere " + count;
	}
	
	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {
		
		Vector L = this.Position().minus(ray.origin); 
        double tca = L.dotProduct(ray.direction); 
        if (tca < 0) {
        	return false; 
        }
        double d2 = L.dotProduct(L) - tca * tca; 
        if (d2 > this.radius*this.radius) {
        	return false; 
        }
        double thc = Math.sqrt(this.radius*this.radius - d2); 
        double t0 = tca - thc; 
        double t1 = tca + thc; 

        // Ensure t0 and t1 are in order of value
        if (t0 > t1) {
        	double temp = t0;
        	t0 = t1;
        	t1 = temp;
        }

        if (t0 < 0.001) { 
            t0 = t1; // if t0 is negative, let's use t1 instead 
            if (t0 < 0.001) {
            	return false; // both t0 and t1 are negative 
            }
        } 
        
        double time = t0;
        
        if (time == Double.NaN) {
        	System.out.println("Error");
        }
        
        if (time < info.getTime()) { // if this intersection is the closest so far (or first)        	
        	Vector hitPoint = ray.atTime(time);
        	
        	info.setTime(time);
            info.setHitPoint(hitPoint);
            info.setNormal(info.getHitPoint().minus(this.Position()).normalize());
            info.setMaterial(this.material);
            
            if (this.hasTextureMap) {
            	Vector color = this.getUVcolor(this.calcUV(hitPoint));
    			info.setUVcolor(color);
            }
            else {
            	info.setHasNoUV();
            }
            
            info.setObject(this);
            info.setObjectName(this.name);
        }
        
        return true;        
        
	}

	public double distanceToPoint(Vector p) {
		
		Vector centreToPoint = this.Position().minus(p);
		
		return centreToPoint.length() - this.radius;
		
	}
	
	public double distanceToSphere(Sphere s) {
		
		double radiusSum = this.radius + s.radius;
		double centreDistance = this.Position().minus(s.Position()).length();
		
		return (centreDistance-radiusSum);
		
	}

	public Vector calcUV(Vector hitPoint) {
		
		Vector hitPointToCentre = (this.Position().minus(hitPoint)).normalize();
		
		double[] uv = new double[2];
		
		uv[0] = 0.5 - Math.atan2(hitPointToCentre.z(), hitPointToCentre.x())/(2*Math.PI);
		
		uv[1]  = 0.5 + Math.asin(hitPointToCentre.y())/Math.PI;
		
		return new Vector(uv);
		
	}
	
	@Override
	public Sphere clone() {
				
		Sphere clone = new Sphere(this.p0.clone(),this.material.clone(),this.radius);
	
		if (this.hasTextureMap) {
			clone.addTextureMap(Util.deepCopy(this.texture));
		}
		
		return clone;
		
	}
	
}
