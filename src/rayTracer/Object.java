package rayTracer;

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
	
}

// Object that does have a material
abstract class MatObject extends Object {

	//The material properties of the object
	Material material;

	MatObject(Vector p, Material m){
		super(p);
		this.p0 = p;
		this.transform = Matrix4.translationMatrix(p);
		this.material = m;
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

}

class Material{

	//Material values used for lighting equations
	Vector ambient;
	Vector diffuse;
	Vector specular;
	double specularExponent;
	//Name, useful for debugging
	String name;



	static Material TEST_RED = new Material(
			new Vector(1,0,0),
			new Vector(1,0,0),
			new Vector(0.7,0.7,0.7),
			200,
			"TEST_RED"
			);

	static Material TEST_GREEN = new Material(
			new Vector(0,1,0),
			new Vector(0,1,0),
			new Vector(0,1,0),
			200,
			"TEST_GREEN"
			);

	static Material TEST_BLUE = new Material(
			new Vector(0,0,1),
			new Vector(0,0,1),
			new Vector(0,0,1),
			200,
			"TEST_BLUE"
			);

	static Material TEST_YELLOW = new Material(
			new Vector(1,1,0),
			new Vector(1,1,0),
			new Vector(1,1,0),
			200,
			"TEST_YELLOW"
			);

	static Material TEST_MAGENTA = new Material(
			new Vector(1,0,1),
			new Vector(1,0,1),
			new Vector(1,0,1),
			200,
			"TEST_MAGENTA"
			);

	static Material TEST_CYAN = new Material(
			new Vector(0,1,1),
			new Vector(0,1,1),
			new Vector(0,1,1),
			200,
			"TEST_CYAN"
			);

	static Material MIRROR = new Material(
			new Vector(0,0,0),
			new Vector(0,0,0),
			new Vector(1,1,1),
			1,
			"MIRROR"
			);
	
	Material(Vector a, Vector d, Vector s, double se, String n){
		ambient = a;
		diffuse = d;
		specular = s;
		specularExponent = se;
		name = n;
	}

	@Override
	public String toString() {
		return "Material [name=" + name + "]";
	}

	public static Material randomMat() {
		int total = Devernay.count + Barradeau.count;
		int r = (int) (Math.random()*total);
		if (r<Devernay.count) {
			return Devernay.RANDOM_MATERIAL();
		}
		else {
			return Barradeau.RANDOM_MATERIAL();
		}
	}

	//TODO add further material values here such as reflection/refraction index

}