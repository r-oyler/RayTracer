package rayTracer;

// Material lighting values from http://www.barradeau.com/nicoptere/dump/materials.html

public class Barradeau {

	static int count = 19;
	
	static Material BRASS= new Material(
			new Vector(0.329412,0.223529,0.027451),
			new Vector(0.780392,0.568627,0.113725),
			new Vector(0.992157,0.941176,0.807843),
			27.8974,
			"BRASS"
			);

	static Material BRONZE= new Material(
			new Vector(0.2125,0.1275,0.054),
			new Vector(0.714,0.4284,0.18144),
			new Vector(0.393548,0.271906,0.166721),
			25.6,
			"BRONZE"
			);

	static Material POLISHED_BRONZE= new Material(
			new Vector(0.25,0.148,0.06475),
			new Vector(0.4,0.2368,0.1036),
			new Vector(0.774597,0.458561,0.200621),
			76.8,
			"POLISHED_BRONZE"
			);

	static Material CHROME = new Material(
			new Vector(0.25,0.25,0.25),
			new Vector(0.4,0.4,0.4),
			new Vector(0.774597,0.774597,0.774597),
			76.8,
			"CHROME"
			);

	static Material COPPER = new Material(
			new Vector(0.19125,0.0735,0.0225),
			new Vector(0.7038,0.27048,0.0828),
			new Vector(0.256777,0.137622,0.086014),
			12.8,
			"COPPER"
			);

	static Material POLISHED_COPPER = new Material(
			new Vector(0.2295,0.08825,0.0275),
			new Vector(0.5508,0.2118,0.066),
			new Vector(0.580594,0.223257,0.0695701),
			51.2,
			"POLISHED_COPPER"
			);

	static Material GOLD = new Material(
			new Vector(0.24725,0.1995,0.0745),
			new Vector(0.75164,0.60648,0.22648),
			new Vector(0.628281,0.555802,0.366065),
			51.2,
			"GOLD"
			);

	static Material POLISHED_GOLD = new Material(
			new Vector(0.24725,0.2245,0.0645),
			new Vector(0.34615,0.3143,0.0903),
			new Vector(0.797357,0.723991,0.208006),
			83.2,
			"POLISHED_GOLD"
			);

	static Material PEWTER = new Material(
			new Vector(0.105882,0.058824,0.113725),
			new Vector(0.427451,0.470588,0.541176),
			new Vector(0.333333,0.333333,0.521569),
			9.84615,
			"PEWTER"
			);

	static Material SILVER = new Material(
			new Vector(0.19225,0.19225,0.19225),
			new Vector(0.50754,0.50754,0.50754),
			new Vector(0.508273,0.508273,0.508273),
			51.2,
			"SILVER"
			);

	static Material POLISHED_SILVER = new Material(
			new Vector(0.23125,0.23125,0.23125),
			new Vector(0.2775,0.2775,0.2775),
			new Vector(0.773911,0.773911,0.773911),
			89.6,
			"POLISHED_SILVER"
			);

	static Material EMERALD = new Material(
			new Vector(0.0215,0.1745,0.0215),
			new Vector(0.07568,0.61424,0.07568),
			new Vector(0.633,0.727811,0.633),
			76.8,
			"EMERALD"
			);

	static Material JADE = new Material(
			new Vector(0.135,0.2225,0.1575),
			new Vector(0.54,0.89,0.63),
			new Vector(0.316228,0.316228,0.316228),
			12.8,
			"JADE"
			);

	static Material OBSIDIAN = new Material(
			new Vector(0.05375,0.05,0.06625),
			new Vector(0.18275,0.17,0.22525),
			new Vector(0.332741,0.328634,0.346435),
			38.4,
			"OBSIDIAN"
			);

	static Material PEARL = new Material(
			new Vector(0.25,0.20725,0.20725),
			new Vector(1,0.829,0.829),
			new Vector(0.296648,0.296648,0.296648),
			11.264,
			"PEARL"
			);

	static Material RUBY = new Material(
			new Vector(0.1745,0.01175,0.01175),
			new Vector(0.61424,0.04136,0.04136),
			new Vector(0.727811,0.626959,0.626959),
			76.8,
			"RUBY"
			);

	static Material TURQUOISE = new Material(
			new Vector(0.1,0.18725,0.1745),
			new Vector(0.396,0.74151,0.69102),
			new Vector(0.297254,0.30829,0.306678),
			12.8,
			"TURQUOISE"
			);

	static Material BLACK_PLASTIC = new Material(
			new Vector(0,0,0),
			new Vector(0.01,0.01,0.01),
			new Vector(0.5,0.5,0.5),
			32,
			"BLACK_PLASTIC"
			);

	static Material BLACK_RUBBER = new Material(
			new Vector(0.02,0.02,0.02),
			new Vector(0.01,0.01,0.01),
			new Vector(0.4,0.4,0.4),
			10,
			"BLACK_RUBBER"
			);
	
	static Material RANDOM_MATERIAL(){

		int i = (int) (Math.random()* count);
		switch(i){
			case 0: return BRASS;
			case 1: return BRONZE; 
			case 2: return POLISHED_BRONZE; 
			case 3: return CHROME; 
			case 4: return COPPER; 
			case 5: return POLISHED_COPPER; 
			case 6: return GOLD; 
			case 7: return POLISHED_GOLD; 
			case 8: return PEWTER; 
			case 9: return SILVER; 
			case 10: return POLISHED_SILVER; 
			case 11: return EMERALD; 
			case 12: return JADE; 
			case 13: return OBSIDIAN; 
			case 14: return PEARL; 
			case 15: return RUBY; 
			case 16: return TURQUOISE;
			case 17: return BLACK_PLASTIC;
			case 18: return BLACK_RUBBER;
			default: return BRASS;
		}
	}
	
}
