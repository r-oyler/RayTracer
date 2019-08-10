package rayTracer;


public class Light extends Object{

	// The color of the light
	Vector color;

	Light(Vector p, Vector c){
		super(p);
		color = c;
	}
	
	Vector Position() {
		return new Vector(transform.d[0][3],transform.d[1][3],transform.d[2][3]);
	}
	
	Vector getColor() {
		return color;
	}
	
	public static Vector randomColor() {
		return new Vector(Math.random()*255,Math.random()*255,Math.random()*255);
	}
	
}
