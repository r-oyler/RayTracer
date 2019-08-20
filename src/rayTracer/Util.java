package rayTracer;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Util {

	// Check if input is between min and max, inclusive
	public static boolean isBetweenInc(double x, double min, double max) {

		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}

		return (x >= min) && (x <= max);

	}

	// Check if input is between min and max, exclusive
	public static boolean isBetweenExc(double x, double min, double max) {

		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}

		return (x > min) && (x < max);

	}

	int smallest(double... d) {

		int smallest = 0;

		for (int i = 0; i<d.length-1; i++) {
			smallest = d[i] <= d[i+1] ? i : i+1;
		}

		return smallest;

	}

	int largest(double... d) {

		int largest = 0;

		for (int i = 0; i<d.length-1; i++) {
			largest = d[i] >= d[i+1] ? i : i+1;
		}

		return largest;

	}

	public static double randomBetween(double min, double max) {

		if (min >= max) throw new IllegalArgumentException("Max must be strictly greater than min");

		double range = max-min;

		double rand = Math.random();

		while (rand == 0) rand = Math.random();

		rand*= range;

		return min+rand;

	}

	public static double clamp(double in, double min, double max) {

		if (min >= max) throw new IllegalArgumentException("Max must be strictly greater than min");

		if (in < min) in = min;

		if (in > max) in = max;

		return in;

	}

	public static double sign(double d) {

		if (d < 0) {
			return -1;
		}
		else if (d > 0) {
			return 1;
		}
		else {
			return 0;
		}

	}

	public static double step(double edge, double d) {

		if (d < edge) {
			return 0.0;
		}
		else {
			return 1.0;
		}

	}

	public static double degreeToRadian(double degrees) {

		return degrees * Math.PI/180.0;

	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	// https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
	public static double fresnel(Vector incident, Vector normal, double ior) {
		
		double kReflect;
		
		double cosIncident = clamp(incident.dotProduct(normal),-1,1);
		
		// Can only be used for air to refractive object or vice versa transmission
		double etaIncident = 1;
		double etaTransmission = ior;
		
		if (cosIncident > 0) {
			double temp = etaIncident;
			etaIncident = etaTransmission;
			etaTransmission = temp;
		}
		
		double sinTransmission = (etaIncident/etaTransmission) * Math.sqrt(Math.max(0, 1 - cosIncident * cosIncident));
		
		// Total internal reflection
		if (sinTransmission >= 1) {
			kReflect = 1;
		}
		
		else {
			double cosTransmission = Math.sqrt(Math.max(0, 1 - sinTransmission * sinTransmission));
			cosIncident = Math.abs(cosIncident);
			double fresnelParralel = ((etaTransmission * cosIncident)-(etaIncident * cosTransmission)) / ((etaTransmission * cosIncident) + (etaIncident * cosTransmission));
			double fresnelPerpendicular = ((etaIncident * cosIncident)-(etaTransmission * cosTransmission)) / ((etaIncident * cosIncident) + (etaTransmission * cosTransmission));
			kReflect = (fresnelParralel * fresnelParralel + fresnelPerpendicular * fresnelPerpendicular) / 2;
		}
		
		return kReflect;
		// As a consequence of the conservation of energy, transmittance is given by:
		// kTransmit = 1 - kReflect;
	}
	
	public static Vector reflect(Vector incident, Vector normal) {
		
		// R = I - (N*(I dot N)) * 2
		return incident.minus(normal.timesConst(incident.dotProduct(normal)).timesConst(2));
		
	}
	
	public static Vector refract(Vector incident, Vector normal, double ior) {
		
		double cosIncident = clamp(incident.dotProduct(normal),-1,1);
		
		double etaIncident = 1;
		double etaTransmission = ior;
		
		if (cosIncident < 0) {
			cosIncident = -cosIncident;
		}
		else {
			double temp = etaIncident;
			etaIncident = etaTransmission;
			etaTransmission = temp;
			normal = normal.timesConst(-1);
		}
		
		double eta = etaIncident/etaTransmission;
		
		double k = 1 - eta * eta * (1 - cosIncident * cosIncident);
		
		// Total internal reflection
		if (k < 0) {
			return new Vector(0,0,0);
		}
		else {
			
			// eta * I + (eta * cosi - sqrtf(k)) * n
			return incident.timesConst(eta).plus(normal.timesConst(eta * cosIncident - Math.sqrt(k)));
			
		}
		
	}
	
	public static double Schlick(Vector incident, Vector normal, double ior) {
		
		double rNaught = (1-ior)/(1+ior);
		rNaught *= rNaught;
		
		double cosTheta = incident.dotProduct(normal);
		
		double rTheta = rNaught + (1 - rNaught)*Math.pow((1 - cosTheta),5);
		
		return rTheta;
		
	}
	
	public static Vector quadraticFormula(double a, double b, double c) {
		
		double discriminant = b*b - 4*a*c;
		
		double x1 = (-b -Math.sqrt(discriminant))/(2*a);
		double x2 = (-b +Math.sqrt(discriminant))/(2*a);
		
		return new Vector(x1,x2);
	}
	
}
