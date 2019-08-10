package rayTracer;

public class Matrix4{

	double d[][] = new double[4][4];

	public Matrix4(double[][] d){
		this.d = d;
	}

	public static Matrix4 translationMatrix(double x, double y, double z) {

		double[][] d = new double[][]{
			{1,0,0,x},
			{0,1,0,y},
			{0,0,1,z},
			{0,0,0,1}
		};

		return new Matrix4(d);

	}

	public static Matrix4 translationMatrix(Vector p) {
		return translationMatrix(p.d[0],p.d[1],p.d[2]);
	}

	Matrix4 add(Matrix4 m) {

		double[][] sum = new double[4][4];

		for (int i = 0; i<4; i++) {

			for (int j = 0; j<4; j++) {

				sum[i][j] = this.d[i][j] + m.d[i][j];

			}

		}

		return new Matrix4(sum);

	}

	Matrix4 minus(Matrix4 m) {

		double[][] dif = new double[4][4];

		for (int i = 0; i<4; i++) {

			for (int j = 0; j<4; j++) {

				dif[i][j] = this.d[i][j] - m.d[i][j];

			}

		}

		return new Matrix4(dif);

	}

	Vector times(Vector v) {

		double[] prod = new double[4];

		for (int i = 0; i<4; i++) {

			double sum = 0;

			for (int j = 0; j<4; j++) {

				sum+= d[i][j] * v.d[j];

			}

			prod[i] = sum;

		}

		return new Vector(prod);

	}

	// Source: https://www.scratchapixel.com/lessons/mathematics-physics-for-computer-graphics/lookat-function
	// TODO: test and fix
	static Matrix4 lookAt(Vector from, Vector to, Vector tmp) {

		{ 
			Vector forward = from.minus(to).normalize(); 
			Vector right = tmp.normalize().crossProduct(forward); 
			Vector up = forward.crossProduct(right); 

			double[][] camToWorld = new double[4][4]; 

			camToWorld[0][0] = right.d[0]; 
			camToWorld[1][0] = right.d[1]; 
			camToWorld[2][0] = right.d[2]; 
			camToWorld[0][1] = up.d[0]; 
			camToWorld[1][1] = up.d[1]; 
			camToWorld[2][1] = up.d[2]; 
			camToWorld[0][2] = forward.d[0]; 
			camToWorld[1][2] = forward.d[1]; 
			camToWorld[2][2] = forward.d[2]; 

			camToWorld[0][3] = from.d[0]; 
			camToWorld[1][3] = from.d[1]; 
			camToWorld[2][3] = from.d[2]; 

			return new Matrix4(camToWorld); 
		} 
	}

	public Matrix4 inverse() {
		return Invert.invert(this);
	}
	
	public Matrix4 transpose() {
		
		double[][] m = new double[4][4];
		
		for (int i = 0; i<4; i++) {
			for (int j = 0; j<4; j++) {
				m[i][j] = this.d[j][i];
			}
		}
		
		return new Matrix4(m);
	}

	public String toString() {

		String s = "";

		for (int i=0; i<4; i++) {

			s = s + "(";

			for (int j = 0; j<4; j++) {
				s = s + this.d[i][j] + ",";
			}

			s = s.substring(0, s.length()-1);

			s = s + ")\n";

		}

		return s;

	}

}