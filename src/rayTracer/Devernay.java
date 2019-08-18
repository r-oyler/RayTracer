package rayTracer;

// http://devernay.free.fr/cours/opengl/materials.html
public class Devernay {

	static Material EMERALD = new Material("EMERALD");
	static Material JADE = new Material("JADE");
	static Material OBSIDIAN = new Material("OBSIDIAN");
	static Material PEARL = new Material("PEARL");
	static Material RUBY = new Material("RUBY");
	static Material TURQUOISE = new Material("TURQUOISE");
	static Material BRASS = new Material("BRASS");
	static Material BRONZE = new Material("BRONZE");
	static Material CHROME = new Material("CHROME");
	static Material COPPER = new Material("COPPER");
	static Material GOLD = new Material("GOLD");
	static Material SILVER = new Material("SILVER");
	static Material BLACK_PLASTIC = new Material("BLACK_PLASTIC");
	static Material CYAN_PLASTIC = new Material("CYAN_PLASTIC");
	static Material GREEN_PLASTIC = new Material("GREEN_PLASTIC");
	static Material RED_PLASTIC = new Material("RED_PLASTIC");
	static Material WHITE_PLASTIC = new Material("WHITE_PLASTIC");
	static Material YELLOW_PLASTIC = new Material("YELLOW_PLASTIC");
	static Material BLACK_RUBBER = new Material("BLACK_RUBBER");
	static Material CYAN_RUBBER = new Material("CYAN_RUBBER");
	static Material GREEN_RUBBER = new Material("GREEN_RUBBER");
	static Material RED_RUBBER = new Material("RED_RUBBER");
	static Material WHITE_RUBBER = new Material("WHITE_RUBBER");
	static Material YELLOW_RUBBER = new Material("YELLOW_RUBBER");

	static {
		
		EMERALD.setAmbient(new Vector(0.0215,0.1745,0.0215));
		EMERALD.setDiffuse(new Vector(0.07568,0.61424,0.07568));
		EMERALD.setSpecular(new Vector(0.633,0.727811,0.633),76.8);

		JADE.setAmbient(new Vector(0.135,0.2225,0.1575));
		JADE.setDiffuse(new Vector(0.54,0.89,0.63,0.316228));
		JADE.setSpecular(new Vector(0.316228,0.316228),12.8);

		OBSIDIAN.setAmbient(new Vector(0.05375,0.05,0.06625));
		OBSIDIAN.setDiffuse(new Vector(0.18275,0.17,0.22525));
		OBSIDIAN.setSpecular(new Vector(0.332741,0.328634,0.346435),38.4);

		PEARL.setAmbient(new Vector(0.25,0.20725,0.20725));
		PEARL.setDiffuse(new Vector(1,0.829,0.829));
		PEARL.setSpecular(new Vector(0.296648,0.296648,0.296648),11.264);

		RUBY.setAmbient(new Vector(0.1745,0.01175,0.01175));
		RUBY.setDiffuse(new Vector(0.61424,0.04136,0.04136));
		RUBY.setSpecular(new Vector(0.727811,0.626959,0.626959),76.8);

		TURQUOISE.setAmbient(new Vector(0.1,0.18725,0.1745));
		TURQUOISE.setDiffuse(new Vector(0.396,0.74151,0.69102));
		TURQUOISE.setSpecular(new Vector(0.297254,0.30829,0.306678),12.8);

		BRASS.setAmbient(new Vector(0.329412,0.223529,0.027451));
		BRASS.setDiffuse(new Vector(0.780392,0.568627,0.113725));
		BRASS.setSpecular(new Vector(0.992157,0.941176,0.807843),27.8974);

		BRONZE.setAmbient(new Vector(0.2125,0.1275,0.054));
		BRONZE.setDiffuse(new Vector(0.714,0.4284,0.18144));
		BRONZE.setSpecular(new Vector(0.393548,0.271906,0.166721),25.6);

		CHROME.setAmbient(new Vector(0.25,0.25,0.25));
		CHROME.setDiffuse(new Vector(0.4,0.4,0.4));
		CHROME.setSpecular(new Vector(0.774597,0.774597,0.774597),76.8);

		COPPER.setAmbient(new Vector(0.19125,0.0735,0.0225));
		COPPER.setDiffuse(new Vector(0.7038,0.27048,0.0828));
		COPPER.setSpecular(new Vector(0.256777,0.137622,0.086014),12.8);

		GOLD.setAmbient(new Vector(0.24725,0.1995,0.0745));
		GOLD.setDiffuse(new Vector(0.75164,0.60648,0.22648));
		GOLD.setSpecular(new Vector(0.628281,0.555802,0.366065),51.2);

		SILVER.setAmbient(new Vector(0.19225,0.19225,0.19225));
		SILVER.setDiffuse(new Vector(0.50754,0.50754,0.50754));
		SILVER.setSpecular(new Vector(0.508273,0.508273,0.508273),51.2);

		BLACK_PLASTIC.setAmbient(new Vector(0.0,0.0,0.0));
		BLACK_PLASTIC.setDiffuse(new Vector(0.01,0.01,0.01));
		BLACK_PLASTIC.setSpecular(new Vector(0.50,0.50,0.50),32);

		CYAN_PLASTIC.setAmbient(new Vector(0.0,0.1,0.06));
		CYAN_PLASTIC.setDiffuse(new Vector(0.0,0.50980392,0.50980392));
		CYAN_PLASTIC.setSpecular(new Vector(0.50196078,0.50196078,0.50196078),32);

		GREEN_PLASTIC.setAmbient(new Vector(0.0,0.0,0.0));
		GREEN_PLASTIC.setDiffuse(new Vector(0.1,0.35,0.1));
		GREEN_PLASTIC.setSpecular(new Vector(0.45,0.55,0.45),32);

		RED_PLASTIC.setAmbient(new Vector(0.0,0.0,0.0));
		RED_PLASTIC.setDiffuse(new Vector(0.5,0.0,0.0));
		RED_PLASTIC.setSpecular(new Vector(0.7,0.6,0.6),32);

		WHITE_PLASTIC.setAmbient(new Vector(0.0,0.0,0.0));
		WHITE_PLASTIC.setDiffuse(new Vector(0.55,0.55,0.55));
		WHITE_PLASTIC.setSpecular(new Vector(0.70,0.70,0.70),32);

		YELLOW_PLASTIC.setAmbient(new Vector(0.0,0.0,0.0));
		YELLOW_PLASTIC.setDiffuse(new Vector(0.5,0.5,0.0));
		YELLOW_PLASTIC.setSpecular(new Vector(0.60,0.60,0.50),32);

		BLACK_RUBBER.setAmbient(new Vector(0.02,0.02,0.02));
		BLACK_RUBBER.setDiffuse(new Vector(0.01,0.01,0.01));
		BLACK_RUBBER.setSpecular(new Vector(0.4,0.4,0.4),10);

		CYAN_RUBBER.setAmbient(new Vector(0.0,0.05,0.05));
		CYAN_RUBBER.setDiffuse(new Vector(0.4,0.5,0.5));
		CYAN_RUBBER.setSpecular(new Vector(0.04,0.7,0.7),10);

		GREEN_RUBBER.setAmbient(new Vector(0.0,0.05,0.0));
		GREEN_RUBBER.setDiffuse(new Vector(0.4,0.5,0.4));
		GREEN_RUBBER.setSpecular(new Vector(0.04,0.7,0.04),10);

		RED_RUBBER.setAmbient(new Vector(0.05,0.0,0.0));
		RED_RUBBER.setDiffuse(new Vector(0.5,0.4,0.4));
		RED_RUBBER.setSpecular(new Vector(0.7,0.04,0.04),10);

		WHITE_RUBBER.setAmbient(new Vector(0.05,0.05,0.05));
		WHITE_RUBBER.setDiffuse(new Vector(0.5,0.5,0.5));
		WHITE_RUBBER.setSpecular(new Vector(0.7,0.7,0.7),10);

		YELLOW_RUBBER.setAmbient(new Vector(0.05,0.05,0.0));
		YELLOW_RUBBER.setDiffuse(new Vector(0.5,0.5,0.4));
		YELLOW_RUBBER.setSpecular(new Vector(0.7,0.7,0.04),10	);
		
	}
	
}

