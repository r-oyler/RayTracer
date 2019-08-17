package rayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// Object that does not have a material
public abstract class Object {

	//Name, useful for debugging
	String name;
	//Point, redundant given transform, but triangles require this
	Vector p0;
	//The transformation matrix for the object
	Matrix4 transform;

	Object(Vector p){
		this.p0 = p;
		transform = Matrix4.translationMatrix(p);
	}

	public void rotateX(double theta) {
		this.transform = transform.times(Matrix4.xRotationMatrix(theta));
	}

	public void rotateY(double theta) {
		this.transform = transform.times(Matrix4.yRotationMatrix(theta));
	}

	public void rotateZ(double theta) {
		this.transform = transform.times(Matrix4.zRotationMatrix(theta));
	}

	public void scale(double xScale, double yScale, double zScale) {
		this.transform = transform.times(Matrix4.scaleMatrix(xScale, yScale, zScale));
	}

	public void translate(double xDist, double yDist, double zDist) {
		this.transform = transform.times(Matrix4.translationMatrix(xDist, yDist, zDist));
	}

}

// Object that does have a material
abstract class MatObject extends Object {

	//The material properties of the object
	Material material;

	boolean hasTextureMap = false;
	BufferedImage texture = null;

	MatObject(Vector p, Material m){
		super(p);
		this.material = m;
	}

	public void addTextureMap(String file) {
		hasTextureMap = true;
		
		BufferedImage bI = null;
		try {
			bI = ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		texture = bI;
	}
	
	public void addTextureMap(BufferedImage bI) {
		hasTextureMap = true;
		
		texture = bI;
	}

	abstract Vector calcUV(Vector hitPoint);

	public Vector getUVcolor(Vector uv) {

		BufferedImage bI = this.texture;

		int iU = (int) (uv.x() * bI.getWidth());
		int iV = (int) (uv.y() * bI.getHeight());

		int rgb = bI.getRGB(iU, iV);
		Color c = new Color(rgb);

		Vector color = new Vector(c.getRed(),c.getGreen(),c.getBlue());

		color = color.divide(255);
		
		return color;

	}

	//Test whether a ray intersects the object
	//@ray The ray that we are testing for intersection
	//@info Object containing information on the intersection between the ray and the object(if any)
	abstract boolean Intersect(Ray ray, IntersectInfo info);

	//Return the position of the object, according to its transformation matrix
	Vector Position() {
		return new Vector(transform.d[0][3],transform.d[1][3],transform.d[2][3]);
	}

	@Override
	public String toString() {
		return "Object [name=" + name + "]";
	}

	abstract public MatObject clone();

}

class Material {

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

	public void setReflective(double r) {
		if (!Util.isBetweenInc(r, 0, 1)) throw new IllegalArgumentException("Reflection coefficient must be a value between 0 and 1 inclusive"); 

		isReflective = true;
		reflectionCoefficient = r;
	}
	
	public void setRefractive(double r) {
		if (!Util.isBetweenInc(r, 1, 38.6)) throw new IllegalArgumentException("Refractive index must be a value between 1 and 38.6 inclusive"); 

		isRefractive = true;
		refractiveIndex = r;
	}

	static Material RED = new Material("RED");
	static Material GREEN = new Material("GREEN");
	static Material BLUE = new Material("BLUE");
	static Material YELLOW = new Material("YELLOW");
	static Material BLACK = new Material("BLACK");
	static Material MIRROR = new Material("MIRROR");
	static Material TEXTURE = new Material("TEXTURE");
	
	static Material WATER = new Material("WATER");
	static Material GLASS = new Material("GLASS");
	static Material DIAMOND = new Material("DIAMOND");
	
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

		MIRROR.setReflective(0.8);
		
		TEXTURE.setAmbient(new Vector(1,1,1));
		TEXTURE.setDiffuse(new Vector(1,1,1));
		
		WATER.setRefractive(1.333);
		
		GLASS.setRefractive(1.52);
		
		DIAMOND.setRefractive(2.42);
	}

	@Override
	public String toString() {
		return "Material [name=" + name + "]";
	}

	public Material clone() {

		Material clone = new Material(this.name);
		if (isAmbient) clone.setAmbient(this.ambient);
		if (isDiffuse) clone.setDiffuse(this.diffuse);
		if (isSpecular) clone.setSpecular(this.specular, this.specularExponent);
		if (isReflective) clone.setReflective(this.reflectionCoefficient);
		if (isRefractive) clone.setRefractive(this.refractiveIndex);

		return clone;

	}

	//TODO add further material values here such as refraction index

}