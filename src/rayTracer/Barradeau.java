package rayTracer;

// http://www.barradeau.com/nicoptere/dump/materials.html
public class Barradeau {

	static Material BRASS = new Material("BRASS");
	static Material BRONZE = new Material("BRONZE");
	static Material POLISHED_BRONZE = new Material("POLISHED_BRONZE");
	static Material CHROME = new Material("CHROME");
	static Material COPPER = new Material("COPPER");
	static Material POLISHED_COPPER = new Material("POLISHED_COPPER");
	static Material GOLD = new Material("GOLD");
	static Material POLISHED_GOLD = new Material("POLISHED_GOLD");
	static Material PEWTER = new Material("PEWTER");
	static Material SILVER = new Material("SILVER");
	static Material POLISHED_SILVER = new Material("POLISHED_SILVER");
	static Material EMERALD = new Material("EMERALD");
	static Material JADE = new Material("JADE");
	static Material OBSIDIAN = new Material("OBSIDIAN");
	static Material PEARL = new Material("PEARL");
	static Material RUBY = new Material("RUBY");
	static Material TURQUOISE = new Material("TURQUOISE");
	static Material BLACK_PLASTIC = new Material("BLACK_PLASTIC");
	static Material BLACK_RUBBER = new Material("BLACK_RUBBER");

	static {
		
		BRASS.setAmbient(new Vector(0.329412,0.223529,0.027451));
		BRASS.setDiffuse(new Vector(0.780392,0.568627,0.113725));
		BRASS.setSpecular(new Vector(0.992157,0.941176,0.807843),27.8974);

		BRONZE.setAmbient(new Vector(0.2125,0.1275,0.054));
		BRONZE.setDiffuse(new Vector(0.714,0.4284,0.18144));
		BRONZE.setSpecular(new Vector(0.393548,0.271906,0.166721),25.6);

		POLISHED_BRONZE.setAmbient(new Vector(0.25,0.148,0.06475));
		POLISHED_BRONZE.setDiffuse(new Vector(0.4,0.2368,0.1036));
		POLISHED_BRONZE.setSpecular(new Vector(0.774597,0.458561,0.200621),76.8);

		CHROME.setAmbient(new Vector(0.25,0.25,0.25));
		CHROME.setDiffuse(new Vector(0.4,0.4,0.4));
		CHROME.setSpecular(new Vector(0.774597,0.774597,0.774597),76.8);

		COPPER.setAmbient(new Vector(0.19125,0.0735,0.0225));
		COPPER.setDiffuse(new Vector(0.7038,0.27048,0.0828));
		COPPER.setSpecular(new Vector(0.256777,0.137622,0.086014),12.8);

		POLISHED_COPPER.setAmbient(new Vector(0.2295,0.08825,0.0275));
		POLISHED_COPPER.setDiffuse(new Vector(0.5508,0.2118,0.066));
		POLISHED_COPPER.setSpecular(new Vector(0.580594,0.223257,0.0695701),51.2);

		GOLD.setAmbient(new Vector(0.24725,0.1995,0.0745));
		GOLD.setDiffuse(new Vector(0.75164,0.60648,0.22648));
		GOLD.setSpecular(new Vector(0.628281,0.555802,0.366065),51.2);

		POLISHED_GOLD.setAmbient(new Vector(0.24725,0.2245,0.0645));
		POLISHED_GOLD.setDiffuse(new Vector(0.34615,0.3143,0.0903));
		POLISHED_GOLD.setSpecular(new Vector(0.797357,0.723991,0.208006),83.2);

		PEWTER.setAmbient(new Vector(0.105882,0.058824,0.113725));
		PEWTER.setDiffuse(new Vector(0.427451,0.470588,0.541176));
		PEWTER.setSpecular(new Vector(0.333333,0.333333,0.521569),9.84615);

		SILVER.setAmbient(new Vector(0.19225,0.19225,0.19225));
		SILVER.setDiffuse(new Vector(0.50754,0.50754,0.50754));
		SILVER.setSpecular(new Vector(0.508273,0.508273,0.508273),51.2);

		POLISHED_SILVER.setAmbient(new Vector(0.23125,0.23125,0.23125));
		POLISHED_SILVER.setDiffuse(new Vector(0.2775,0.2775,0.2775));
		POLISHED_SILVER.setSpecular(new Vector(0.773911,0.773911,0.773911),89.6);

		EMERALD.setAmbient(new Vector(0.0215,0.1745,0.0215));
		EMERALD.setDiffuse(new Vector(0.07568,0.61424,0.07568));
		EMERALD.setSpecular(new Vector(0.633,0.727811,0.633),76.8);

		JADE.setAmbient(new Vector(0.135,0.2225,0.1575));
		JADE.setDiffuse(new Vector(0.54,0.89,0.63));
		JADE.setSpecular(new Vector(0.316228,0.316228,0.316228),12.8);

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

		BLACK_PLASTIC.setAmbient(new Vector(0,0,0));
		BLACK_PLASTIC.setDiffuse(new Vector(0.01,0.01,0.01));
		BLACK_PLASTIC.setSpecular(new Vector(0.5,0.5,0.5),32);

		BLACK_RUBBER.setAmbient(new Vector(0.02,0.02,0.02));
		BLACK_RUBBER.setDiffuse(new Vector(0.01,0.01,0.01));
		BLACK_RUBBER.setSpecular(new Vector(0.4,0.4,0.4),10);

		
	}
	
}
