package rayTracer;

import javax.swing.JOptionPane;

public class Input {

	public static Settings getSettings() {
		
		Settings s = new Settings();
		
		boolean multithreading = getBoolean("Enable multithreading?");
		
		s.setMultithreading(multithreading);
		
		int windowX = getInt("Window width", 1366, 1, Integer.MAX_VALUE);
		int windowY = getInt("Window height", 768, 1, Integer.MAX_VALUE);
		
		s.setWindowXY(windowX, windowY);
		
		double fov = getDouble("Field of view", 90, 40, 150);
		
		s.setFov(fov);
		
		int ssCols = getInt("Supersampling columns", 1, 1, Integer.MAX_VALUE);
		int ssRows = getInt("Supersampling rows", 1, 1, Integer.MAX_VALUE);
		
		s.setSSColRowMax(ssCols, ssRows);
		
		int maxRecursionDepth = getInt("Maximum recursion depth", 7, 1, Integer.MAX_VALUE);
		
		s.setMaxRecursionDepth(maxRecursionDepth);
		
		double shadowRayBias = getDouble("Shadow ray bias", 0.0001, 0, 1);
		
		s.setBias(shadowRayBias);
		
		return s;
		
	}
	
	public static int getInt(String message, int defaultValue, int min, int max) {
			
		int i = 0;
		
		boolean valid;
		
		do {
				valid = true;
				String s = JOptionPane.showInputDialog(null, message + "\n(Integer " + min + "-" + max + ")", defaultValue);
				
				if (s==null) {
					System.out.println("Exiting");
					System.exit(0);
				}
				
				try {
					i = Integer.parseInt(s);
					
					if (i < min || i > max) {
						valid = false;
						JOptionPane.showMessageDialog(null, "Please enter a valid integer between " + min + " and " + max);
					}
					
				} catch (NumberFormatException e){
					valid = false;
					JOptionPane.showMessageDialog(null, "Please enter a valid integer");
				}
								
		}
		while(valid == false);
		
		return i;
		
	}
	
	public static double getDouble(String message, double defaultValue, double min, double max) {
		
		double d = 0;
		
		boolean valid;
		
		do {
				valid = true;
				String s = JOptionPane.showInputDialog(null, message + "\n(Double " + min + "-" + max + ")", defaultValue);
				
				if (s==null) {
					System.out.println("Exiting");
					System.exit(0);
				}
				
				try {
					d = Double.parseDouble(s);
					
					if (d < min || d > max) {
						valid = false;
						JOptionPane.showMessageDialog(null, "Please enter a valid double between " + min + " and " + max);
					}
					
				} catch (NumberFormatException e){
					valid = false;
					JOptionPane.showMessageDialog(null, "Please enter a valid double");
				}
				
		}
		while(valid == false);
		
		return d;
		
	}
	
	public static Boolean getBoolean(String message){
		
		int i = JOptionPane.showConfirmDialog(null, message, null, JOptionPane.YES_NO_OPTION);
		
		if (i==-1) {
			System.out.println("Exiting");
			System.exit(0);
		}
		
		return (i == 0);
		
	}
	
	public static String getString(String message, String defaultValue) {
		
		String s = JOptionPane.showInputDialog(null, message + "\n(String)", defaultValue);
		
		if (s == null) {
			System.out.println("Exiting");
			System.exit(0);
		}
		
		return s;
		
	}
	
}
