package rayTracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsParser {

	public static int divider = '=';

	public static Settings parseFile(String filePath) {

		Settings settings = new Settings();
		boolean[] values = new boolean[6];

		for (int i=0;i<values.length;i++) values[i] = false;

		File f = new File(filePath);

		if (!f.exists()) generateDefaultFile(filePath);

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			System.out.println("Error");
			e.printStackTrace();
		}

		String st; 
		try {
			while ((st = br.readLine()) != null) { 

				st = st.toLowerCase();

				if (st.startsWith("multithreading")) {

					int dividerIndex = st.indexOf(divider);
					st = st.substring(dividerIndex+1);
					boolean multithreading = Boolean.parseBoolean(st.trim());
					settings.setMultithreading(multithreading);
					values[0] = true;

				}
				else if(st.startsWith("image size")) {

					int dividerIndex = st.indexOf(divider);
					st = st.substring(dividerIndex+1);
					String[] strs = st.split("x");
					try {
						strs[0] = strs[0].trim();
						strs[1] = strs[1].trim();
					}
					catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("Error: could not parse " + st + " as Image size (WIDTHxHEIGHT) (integers)");
						System.exit(1);
					}

					int width = 0;
					int height = 0;
					try {
						width = Integer.parseInt(strs[0]);
					}
					catch (NumberFormatException nfe){
						System.out.println("Error: could not parse " + strs[0] + " as integer for Image width.");
						System.exit(1);
					}
					try {
						height = Integer.parseInt(strs[1]);
					}
					catch (NumberFormatException nfe){
						System.out.println("Error: could not parse " + strs[1] + " as integer for Image height.");
						System.exit(1);
					}
					settings.setWindowXY(width, height);
					values[1] = true;

				}
				else if(st.startsWith("field of view")) {

					int dividerIndex = st.indexOf(divider);
					st = st.substring(dividerIndex+1).trim();
					double fov = 0;
					try {
						fov = Double.parseDouble(st);
					}
					catch (NumberFormatException nfe) {
						System.out.println("Error: could not parse " + st + " as double for Field of view.");
						System.exit(1);
					}
					settings.setFov(fov);
					values[2] = true;

				}
				else if(st.startsWith("supersampling grid")) {

					int dividerIndex = st.indexOf(divider);
					st = st.substring(dividerIndex+1);
					String[] strs = st.split("x");
					try {
						strs[0] = strs[0].trim();
						strs[1] = strs[1].trim();
					}
					catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("Error: could not parse " + st + " as Supersampling grid (WIDTHxHEIGHT) (integers)");
						System.out.println("Error: settings file does not contain all settings.\nTo generate the default file, delete " + filePath + " and rerun the program.");
						System.exit(1);
					}

					int width = 0;
					int height = 0;
					try {
						width = Integer.parseInt(strs[0]);
					}
					catch (NumberFormatException nfe){
						System.out.println("Error: could not parse " + strs[0] + " as integer for Supersampling grid width.");
						System.out.println("Error: settings file does not contain all settings.\nTo generate the default file, delete " + filePath + " and rerun the program.");
						System.exit(1);
					}
					try {
						height = Integer.parseInt(strs[1]);
					}
					catch (NumberFormatException nfe){
						System.out.println("Error: could not parse " + strs[1] + " as integer for Supersampling grid height.");
						System.out.println("Error: settings file does not contain all settings.\nTo generate the default file, delete " + filePath + " and rerun the program.");
						System.exit(1);
					}
					settings.setSSColRowMax(width, height);
					values[3] = true;

				}
				else if(st.startsWith("max recursion depth")) {

					int dividerIndex = st.indexOf(divider);
					st = st.substring(dividerIndex+1);
					st = st.trim();
					int maxRecursionDepth = 0;
					try {
						maxRecursionDepth = Integer.parseInt(st);
					}
					catch (NumberFormatException nfe){
						System.out.println("Error: could not parse " + st + " as integer for Max recursion depth.");
						System.out.println("Error: settings file does not contain all settings.\nTo generate the default file, delete " + filePath + " and rerun the program.");
						System.exit(1);
					}
					settings.setMaxRecursionDepth(maxRecursionDepth);
					values[4] = true;

				}
				else if(st.startsWith("bias")) {

					int dividerIndex = st.indexOf(divider);
					st = st.substring(dividerIndex+1);
					st = st.trim();
					double bias = 0;
					try {
						bias = Double.parseDouble(st);
					}
					catch (NumberFormatException nfe){
						System.out.println("Error: could not parse " + st + " as double for Bias.");
						System.out.println("Error: settings file does not contain all settings.\nTo generate the default file, delete " + filePath + " and rerun the program.");
						System.exit(1);
					}
					settings.setBias(bias);
					values[5] = true;

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


		boolean allValues = true;

		for (int i=0;i<values.length;i++) allValues &= values[i];

		if (!allValues) {
			System.out.println("Error: settings file does not contain all settings.\nTo generate the default file, delete " + filePath + " and rerun the program.");
		}

		return settings;

	}

	public static void generateDefaultFile(String filePath) {

		System.out.println("Settings file missing.\nGenerating new file.");

		String text = "# Enable multithreading (true/false)\r\n" + 
				"Multithreading = true\r\n" + 
				"# Image size in pixels WIDTHxHEIGHT (integers)\r\n" + 
				"Image size = 1366x768\r\n" + 
				"# Field of view in degrees (double)\r\n" + 
				"Field of view = 90\r\n" + 
				"# Grid size used in supersampling anti-aliasing WIDTHxHEIGHT (integer)\r\n" + 
				"Supersampling = 1x1\r\n" + 
				"# Maximum depth that reflection/refraction rays are cast (integer)\r\n" + 
				"Max Recursion Depth = 7\r\n" + 
				"# Distance that shadow ray, reflection ray and refraction ray origins are moved along the surface normal to prevent shadow acne (double)\r\n" + 
				"Bias = 0.0001";

		File f = new File(filePath);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		try {
			FileWriter fw = new FileWriter(f);
			fw.write(text);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);

	}

}
