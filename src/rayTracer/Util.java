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

	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
