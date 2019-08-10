package rayTracer;

// Material lighting values from http://devernay.free.fr/cours/opengl/materials.html

public class Devernay {

	static int count = 17;

	static Material EMERALD = new Material(
			new Vector(0.0215,0.1745,0.0215),
			new Vector(0.07568,0.61424,0.07568),
			new Vector(0.633,0.727811,0.633),
			0.6,
			"EMERALD"
			);

	static Material JADE = new Material(
			new Vector(0.135,0.2225,0.1575),
			new Vector(0.54,0.89,0.63),
			new Vector(0.316228,0.316228,0.316228),
			0.1,
			"JADE"
			);

	static Material OBSIDIAN = new Material(
			new Vector(0.05375,0.05,0.06625),
			new Vector(0.18275,0.17,0.22525),
			new Vector(0.332741,0.328634,0.346435),
			0.3,
			"OBSIDIAN"
			);

	static Material PEARL = new Material(
			new Vector(0.25,0.20725,0.20725),
			new Vector(1,0.829,0.829),
			new Vector(0.296648,0.296648,0.296648),
			0.088,
			"PEARL"
			);

	static Material RUBY = new Material(
			new Vector(0.1745,0.01175,0.01175),
			new Vector(0.61424,0.04136,0.04136),
			new Vector(0.727811,0.626959,0.626959),
			0.6,
			"RUBY"
			);

	static Material BLACK_PLASTIC = new Material(
			new Vector(0.0,0,0),
			new Vector(0.01,0.01,0.01),
			new Vector(0.5,0.5,0.5),
			0.25,
			"BLACK_PLASTIC"
			);

	static Material CYAN_PLASTIC= new Material(
			new Vector(0,0.1,0.06),
			new Vector(0,0.50980392,0.50980392),
			new Vector(0.50196078,0.50196078,0.50196078),
			0.25,
			"CYAN_PLASTIC"
			);


	static Material GREEN_PLASTIC= new Material(
			new Vector(0,0,0),
			new Vector(0.1,0.35,0.1),
			new Vector(0.45,0.55,0.45),
			0.25,
			"GREEN_PLASTIC"
			);

	static Material RED_PLASTIC = new Material(
			new Vector(0,0,0),
			new Vector(0.5,0,0),
			new Vector(0.7,0.6,0.6),
			0.25,
			"RED_PLASTIC"
			);

	static Material WHITE_PLASTIC= new Material(
			new Vector(0,0,0),
			new Vector(0.55,0.55,0.55),
			new Vector(0.7,0.7,0.7),
			0.25,
			"WHITE_PLASTIC"
			);

	static Material YELLOW_PLASTIC= new Material(
			new Vector(0,0,0),
			new Vector(0.5,0.5,0),
			new Vector(0.6,0.6,0.5),
			0.25,
			"YELLOW_PLASTIC"
			);

	static Material BLACK_RUBBER= new Material(
			new Vector(0.02,0.02,0.02),
			new Vector(0.01,0.01,0.01),
			new Vector(0.4,0.4,0.4),
			0.078125,
			"BLACK_RUBBER"
			);

	static Material CYAN_RUBBER= new Material(
			new Vector(0,0.05,0.05),
			new Vector(0.4,0.5,0.5),
			new Vector(0.04,0.7,0.7),
			0.078125,
			"CYAN_RUBBER"
			);

	static Material GREEN_RUBBER= new Material(
			new Vector(0,0.05,0),
			new Vector(0.4,0.5,0.4),
			new Vector(0.04,0.7,0.04),
			0.078125,
			"GREEN_RUBBER"
			);

	static Material RED_RUBBER= new Material(
			new Vector(0.05,0,0),
			new Vector(0.5,0.4,0.4),
			new Vector(0.7,0.04,0.04),
			0.078125,
			"RED_RUBBER"
			);

	static Material WHITE_RUBBER= new Material(
			new Vector(0.05,0.05,0.05),
			new Vector(0.5,0.5,0.5),
			new Vector(0.7,0.7,0.7),
			0.078125,
			"WHITE_RUBBER"
			);

	static Material YELLOW_RUBBER= new Material(
			new Vector(0.05,0.05,0),
			new Vector(0.5,0.5,0.4),
			new Vector(0.7,0.7,0.04),
			0.078125,
			"YELLOW_RUBBER"
			);

	static Material RANDOM_MATERIAL(){

		int i = (int) (Math.random()* count);
		switch(i){
			case 0: return EMERALD;
			case 1: return JADE; 
			case 2: return OBSIDIAN; 
			case 3: return PEARL; 
			case 4: return RUBY; 
			case 5: return BLACK_PLASTIC; 
			case 6: return CYAN_PLASTIC; 
			case 7: return GREEN_PLASTIC; 
			case 8: return RED_PLASTIC; 
			case 9: return WHITE_PLASTIC; 
			case 10: return YELLOW_PLASTIC; 
			case 11: return BLACK_RUBBER; 
			case 12: return CYAN_RUBBER; 
			case 13: return GREEN_RUBBER; 
			case 14: return RED_RUBBER; 
			case 15: return WHITE_RUBBER; 
			case 16: return YELLOW_RUBBER;
			default: return EMERALD;
		}
	}
}
