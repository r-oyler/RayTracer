package rayTracer;

public class InfCylinder extends MatObject {

	Vector direction;
	double radius;
	
	InfCylinder(Vector p, Material m, Vector direction, double radius) {
		super(p, m);
		this.direction = direction.normalize();
		this.radius = radius;
	}

	@Override
	// http://iquilezles.org/www/articles/intersectors/intersectors.htm	
	boolean Intersect(Ray ray, IntersectInfo info) {

		Vector cToR = ray.origin.minus(this.p0);
		double cDir_RDir = this.direction.dotProduct(ray.direction);
		double cDir_CToR = this.direction.dotProduct(cToR);
		
		double a = 1 - (cDir_RDir*cDir_RDir);
		double b = cToR.dotProduct(ray.direction) - (cDir_CToR*cDir_RDir);
		double c = cToR.dotProduct(cToR) - (cDir_CToR * cDir_CToR) - (this.radius * this.radius);
		
		double h = b*b - a*c;
		
		if (h<0) {
			return false;
		}
		
		h = Math.sqrt(h);
		
		double time = (-b-h)/a;
		
		if (time < 0) {
			time = (-b+h)/a;
		}
		
		if (time < 0) {	// intersections behind ray origin
			return false;
		}
		
		if (time < info.getTime()) { // if this intersection is the closest so far (or first)        	
        	Vector hitPoint = ray.atTime(time);
        	
        	// a = this.p0
        	// b = hitPoint
        	// d = this.direction
        	
        	Vector hitToC = hitPoint.minus(this.p0);
        	
        	double hitToC_cDir = hitToC.dotProduct(this.direction);
        	double cDir_cDir = this.direction.dotSelf();
        	
        	Vector normal = this.p0.minus(this.direction.timesConst(hitToC_cDir/cDir_cDir)).plus(hitPoint);        	
        	
        	info.setTime(time);
            info.setHitPoint(hitPoint);
            info.setNormal(normal);
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

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfCylinder clone() {
		InfCylinder clone = new InfCylinder(this.p0.clone(), this.material.clone(), this.direction.clone(), this.radius);
		return clone;
	}

	
	
}
