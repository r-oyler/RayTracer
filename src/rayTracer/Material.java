package rayTracer;

public class Material {

	//Material values used for lighting equations
	boolean isAmbient = false;
	Vector ambient;

	boolean isDiffuse = false;
	Vector diffuse;

	boolean isSpecular = false;
	Vector specular;
	double specularExponent = 0;

	boolean isReflective = false;
	double reflectionCoefficient = 0;

	boolean isRefractive = false;
	double refractiveIndex = 0;
	
	Vector reflectRefractTint;
	
	//Name, useful for debugging
	String name;

	Material(String n){
		name = n;
	}

	public void setAmbient(Vector a) {
		if(a.dim != 3) throw new IllegalArgumentException("Ambient reflection must be a vector with 3 elements: {red,green,blue}");
		if (!a.elementsBetweenInc(0, 1)) throw new IllegalArgumentException("Ambient reflection must be a vector with all elements between 0 and 1 inclusive");

		isAmbient = true;
		ambient = a;
	}

	public void setDiffuse(Vector d) {
		if(d.dim != 3) throw new IllegalArgumentException("Diffuse reflection must be a vector with 3 elements: {red,green,blue}");
		if (!d.elementsBetweenInc(0, 1)) throw new IllegalArgumentException("Diffuse reflection must be a vector with all elements between 0 and 1 inclusive");

		isDiffuse = true;
		diffuse = d;
	}

	public void setSpecular(Vector s, double se) {
		if(s.dim != 3) throw new IllegalArgumentException("Ambient reflection must be a vector with 3 elements: {red,green,blue}");
		if (!s.elementsBetweenInc(0, 1)) throw new IllegalArgumentException("Ambient reflection must be a vector with all elements between 0 and 1 inclusive");

		isSpecular = true;
		specular = s;
		specularExponent = se;
	}

	public void setReflective(double r, Vector tint) {
		if (!Util.isBetweenInc(r, 0, 1)) throw new IllegalArgumentException("Reflection coefficient must be a value between 0 and 1 inclusive"); 

		isReflective = true;
		reflectionCoefficient = r;
		reflectRefractTint = tint;
	}
	
	public void setRefractive(double r, Vector tint) {
		if (!Util.isBetweenInc(r, 1, 38.6)) throw new IllegalArgumentException("Refractive index must be a value between 1 and 38.6 inclusive"); 

		isRefractive = true;
		refractiveIndex = r;
		reflectRefractTint = tint;
	}

	public Vector getReflectRefractTint() {
		return reflectRefractTint;
	}



	static Material RED = new Material("RED");
	static Material GREEN = new Material("GREEN");
	static Material BLUE = new Material("BLUE");
	static Material YELLOW = new Material("YELLOW");
	static Material BLACK = new Material("BLACK");
	static Material GREY = new Material("GREY");
	static Material MIRROR = new Material("MIRROR");
	static Material TEXTURE = new Material("TEXTURE");
	
	static Material WATER = new Material("WATER");
	static Material GLASS = new Material("GLASS");
	static Material DIAMOND = new Material("DIAMOND");
	static Material SAPPHIRE = new Material("SAPPHIRE");
	static Material EMERALD = new Material("EMERALD");
	static Material RUBY = new Material("RUBY");
	
	static Material BRASS = new Material("BRASS");
	static Material BRONZE = new Material("BRONZE");
	static Material CHROME = new Material("CHROME");
	static Material COPPER = new Material("COPPER");
	static Material GOLD = new Material("GOLD");
	static Material SILVER = new Material("SILVER");
	
	static {
		RED.setAmbient(new Vector(1,0,0));
		RED.setDiffuse(new Vector(1,0,0));
		RED.setSpecular(new Vector(0.7,0.7,0.7), 200);

		GREEN.setAmbient(new Vector(0,1,0));
		GREEN.setDiffuse(new Vector(0,1,0));
		GREEN.setSpecular(new Vector(0.7,0.7,0.7), 200);

		BLUE.setAmbient(new Vector(0,0,1));
		BLUE.setDiffuse(new Vector(0,0,1));
		BLUE.setSpecular(new Vector(0.7,0.7,0.7), 200);

		YELLOW.setAmbient(new Vector(1,1,0));
		YELLOW.setDiffuse(new Vector(1,1,0));
		YELLOW.setSpecular(new Vector(0.7,0.7,0.7), 200);

		BLACK.setAmbient(new Vector(0.2,0.2,0.2));
		BLACK.setDiffuse(new Vector(0.2,0.2,0.2));
		BLACK.setSpecular(new Vector(0.7,0.7,0.7), 200);

		GREY.setAmbient(new Vector(0.3,0.3,0.3));
		GREY.setDiffuse(new Vector(0.3,0.3,0.3));
		GREY.setSpecular(new Vector(0.7,0.7,0.7), 200);
		
		MIRROR.setReflective(0.8, new Vector(1,1,1));
		
		TEXTURE.setAmbient(new Vector(1,1,1));
		TEXTURE.setDiffuse(new Vector(1,1,1));
		
		WATER.setRefractive(1.333, new Vector(0.9,0.9,1));
		GLASS.setRefractive(1.52, new Vector(1,1,1));
		DIAMOND.setRefractive(2.42, new Vector(1,1,1));
		SAPPHIRE.setRefractive(1.765, new Vector(0.059,0.322,0.729));
		EMERALD.setRefractive(1.575, new Vector(0.314,0.784,0.471));
		RUBY.setRefractive(1.765, new Vector(0.878,0.067,0.373));
		
		BRASS.setDiffuse(new Vector(0.329412,0.223529,0.027451));
		BRASS.setAmbient(new Vector(0.780392,0.568627,0.113725));
		BRASS.setSpecular(new Vector(0.992157,0.941176,0.807843), 27.89743616);
		BRASS.setReflective(0.15, new Vector(0.992157,0.941176,0.807843));
		
		BRONZE.setDiffuse(new Vector(0.2125,0.1275,0.054));
		BRONZE.setAmbient(new Vector(0.780392,0.568627,0.113725));
		BRONZE.setSpecular(new Vector(0.992157,0.941176,0.807843), 27.89743616);
		BRONZE.setReflective(0.15, new Vector(0.992157,0.941176,0.807843));
		
		CHROME.setDiffuse(new Vector(0.329412,0.223529,0.027451));
		CHROME.setAmbient(new Vector(0.780392,0.568627,0.113725));
		CHROME.setSpecular(new Vector(0.992157,0.941176,0.807843), 27.89743616);
		CHROME.setReflective(0.15, new Vector(0.992157,0.941176,0.807843));
		
		COPPER.setDiffuse(new Vector(0.329412,0.223529,0.027451));
		COPPER.setAmbient(new Vector(0.780392,0.568627,0.113725));
		COPPER.setSpecular(new Vector(0.992157,0.941176,0.807843), 27.89743616);
		COPPER.setReflective(0.15, new Vector(0.992157,0.941176,0.807843));
		
		GOLD.setDiffuse(new Vector(0.329412,0.223529,0.027451));
		GOLD.setAmbient(new Vector(0.780392,0.568627,0.113725));
		GOLD.setSpecular(new Vector(0.992157,0.941176,0.807843), 27.89743616);
		GOLD.setReflective(0.15, new Vector(0.992157,0.941176,0.807843));
		
		SILVER.setDiffuse(new Vector(0.329412,0.223529,0.027451));
		SILVER.setAmbient(new Vector(0.780392,0.568627,0.113725));
		SILVER.setSpecular(new Vector(0.992157,0.941176,0.807843), 27.89743616);
		SILVER.setReflective(0.15, new Vector(0.992157,0.941176,0.807843));
		
	}

	@Override
	public String toString() {
		return "Material [name=" + name + "]";
	}

	public Material clone() {

		Material clone = new Material(this.name);
		if (isAmbient) clone.setAmbient(this.ambient.clone());
		if (isDiffuse) clone.setDiffuse(this.diffuse.clone());
		if (isSpecular) clone.setSpecular(this.specular.clone(), this.specularExponent);
		if (isReflective) clone.setReflective(this.reflectionCoefficient, this.reflectRefractTint.clone());
		if (isRefractive) clone.setRefractive(this.refractiveIndex, this.reflectRefractTint.clone());

		return clone;

	}

}