package rayTracer;

public class Shading {

	public static Vector ambient(Scene scene, IntersectInfo info) {
		
		Vector ambient;

		if (info.hasUV) {
			ambient = scene.getAmbientLight().multComponents(info.getMaterial().ambient.multComponents(info.getUVcolor()));
		}
		else {
			ambient = scene.getAmbientLight().multComponents(info.getMaterial().ambient);
		}
			
		return ambient;
		
	}
	
	public static Vector diffuseSpecular(Settings settings, Scene scene, IntersectInfo info, Ray ray) {
		
		Vector illumination = new Vector(0,0,0);
		
		for (Light l: scene.getLights()) {

			Vector diffuseSpecular = l.DiffuseSpecular(ray, info, scene, settings);
			illumination = illumination.plus(diffuseSpecular);

		}
		
		return illumination;
		
	}
	
	public static Vector reflection(IntersectInfo info, Ray ray, Payload payload, Settings settings, Scene scene, Vector illumination) {
		
		Vector normal = info.getNormal();
		Vector reflectionDir = Util.reflect(ray.direction, normal);

		Ray reflectionRay = new Ray(info.getHitPoint().plus(info.getNormal().timesConst(settings.getBias())),reflectionDir.normalize());

		Payload reflectionPayload = new Payload();
		reflectionPayload.setNumBounces(payload.numBounces+1);

		// Reflection ray is cast 
		RayTracer.castRay(reflectionRay, reflectionPayload, settings, scene);

		Vector reflection = reflectionPayload.Color.timesConst(info.getMaterial().reflectionCoefficient);

		reflection = reflection.multComponents(info.getMaterial().getReflectRefractTint());
		
		return reflection.plus(illumination.timesConst(1-info.getMaterial().reflectionCoefficient));

	}
	
	public static Vector refraction(IntersectInfo info, Ray ray, Payload payload, Settings settings, Scene scene, Vector illumination) {


		double kReflect = Util.fresnel(ray.direction, info.getNormal(), info.getMaterial().refractiveIndex);

		boolean outside = ray.direction.dotProduct(info.getNormal()) < 0;

		Vector bias = info.getNormal().timesConst(settings.getBias());

		// compute refraction if it is not a case of total internal reflection
		if (kReflect < 1) {

			Vector refractionDirection = Util.refract(ray.direction, info.getNormal(), info.getMaterial().refractiveIndex);

			Vector refractionRayOrigin = outside ? info.getHitPoint().minus(bias) : info.getHitPoint().plus(bias);

			Ray refractionRay = new Ray(refractionRayOrigin, refractionDirection.normalize());

			Payload refractionPayload = new Payload();
			refractionPayload.setNumBounces(payload.numBounces+1);

			RayTracer.castRay(refractionRay, refractionPayload, settings, scene);

			Vector refraction = refractionPayload.Color.timesConst(1-kReflect);

			refraction = refraction.multComponents(info.getMaterial().getReflectRefractTint());

			illumination = illumination.plus(refraction);

		}

		// Reflection
		Vector reflectionDirection = Util.reflect(ray.direction, info.getNormal());

		Vector reflectionRayOrigin = outside ? info.getHitPoint().plus(bias) : info.getHitPoint().minus(bias);

		Ray reflectionRay = new Ray(reflectionRayOrigin, reflectionDirection);

		Payload reflectionPayload = new Payload();
		reflectionPayload.setNumBounces(payload.numBounces+1);

		RayTracer.castRay(reflectionRay, reflectionPayload, settings, scene);

		Vector reflection = reflectionPayload.Color.timesConst(kReflect);

		reflection = reflection.multComponents(info.getMaterial().getReflectRefractTint());

		return illumination.plus(reflection);
		
	}
}
