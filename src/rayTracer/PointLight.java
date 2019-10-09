package rayTracer;

public class PointLight extends Light {

	//Position in world
	Vector position;

	PointLight(Vector c, Vector p) {
		color = c;
		position = p;
	}

	@Override
	public Vector DiffuseSpecular(Ray ray, IntersectInfo info, Scene scene, Settings settings) {

		Vector illumination = new Vector(0,0,0);

		Vector objectToLight = this.position.minus(info.getHitPoint());
		double timeToLight = objectToLight.length();
		objectToLight = objectToLight.normalize();
		Ray shadowRay = new Ray(info.getHitPoint().plus(info.getNormal().timesConst(settings.getBias())), objectToLight);
		IntersectInfo shadowRayInfo = new IntersectInfo();
		shadowRayInfo.setTime(Double.POSITIVE_INFINITY);

		boolean visible = true;
		
		for (int i=0; visible && i<scene.getObjects().size(); i++) {
			MatObject obj = scene.getObjects().get(i);
			if (!obj.material.isRefractive){	// Refractive materials don't cast shadows				
				obj.Intersect(shadowRay, shadowRayInfo); // shadowRayInfo becomes info of nearest intersection, if any
				visible &= (shadowRayInfo.getTime()>=timeToLight);
			}
		}

		if (visible) { // no intersections with objects occur between object and light

			if (info.getMaterial().isDiffuse) {
				Vector diffuse = diffuse(info, objectToLight);
				illumination = illumination.plus(diffuse);
			}

			if (info.getMaterial().isSpecular) {
				Vector specular = specular(info, objectToLight, ray);
				illumination = illumination.plus(specular);
			}
		}

		return illumination;

	}

	public Vector diffuse(IntersectInfo info, Vector objectToLight) {
		// Diffuse illumination

		Vector normal = info.getNormal();
		double cosTheta = objectToLight.cosAngleBetween(normal);

		cosTheta = Math.max(0, cosTheta);

		Vector diffuse;

		if (info.hasUV) {
			diffuse = this.getColor().multComponents(info.getMaterial().diffuse.multComponents(info.getUVcolor())).timesConst(cosTheta);
		}
		else {
			diffuse = this.getColor().multComponents(info.getMaterial().diffuse).timesConst(cosTheta);
		}

		return diffuse;

	}

	public Vector specular(IntersectInfo info, Vector objectToLight, Ray ray) {
		// Specular illumination

		Vector objectToCamera = ray.direction.timesConst(-1).normalize(); // objectToCamera is the reverse of the original camera ray

		Vector normal = info.getNormal();
		Vector reflection = info.getNormal().timesConst(2).timesConst(normal.dotProduct(objectToLight)).minus(objectToLight);

		double cosAngle = reflection.cosAngleBetween(objectToCamera);
		cosAngle = Math.max(0, cosAngle);
		cosAngle = Math.pow(cosAngle, info.getMaterial().specularExponent);

		Vector specular = this.getColor().multComponents(info.getMaterial().specular).timesConst(cosAngle);

		return specular;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public PointLight clone() {
		return new PointLight(this.color.clone(),this.position.clone());
	}

}
