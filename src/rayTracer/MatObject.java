package rayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// Object that does have a material
abstract public class MatObject extends Object {

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