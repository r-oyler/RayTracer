package rayTracer;

public class Matrix4 {

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
		return translationMatrix(p.x(),p.y(),p.z());
	}

	public static Matrix4 xRotationMatrix(double theta) {

		double d[][] = {
				{1,0,0,0},
				{0,Math.cos(-theta),Math.sin(-theta),0},
				{0,-Math.sin(-theta),Math.cos(-theta),0},
				{0,0,0,1}
				};

		return new Matrix4(d);

	}

	public static Matrix4 yRotationMatrix(double theta) {

		double d[][] = {
				{Math.cos(-theta),0,-Math.sin(-theta),0},
				{0,1,0,0},
				{Math.sin(-theta),0,Math.cos(-theta),0},
				{0,0,0,1}
				};

		return new Matrix4(d);

	}

	public static Matrix4 zRotationMatrix(double theta) {

		double d[][] = {
				{Math.cos(-theta),Math.sin(-theta),0,0},
				{-Math.sin(-theta),Math.cos(-theta),0,0},
				{0,0,1,0},
				{0,0,0,1}
				};

		return new Matrix4(d);

	}
	
	public static Matrix4 scaleMatrix(double xScale, double yScale, double zScale) {
		
		double[][] d = new double[][]{
			{1/xScale,0,0,0},
			{0,1/yScale,0,0},
			{0,0,1/zScale,0},
			{0,0,0,1}
		};
		
		return new Matrix4(d	);
		
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

	Vector timesV(Vector v) {

		if (v.dim != 4) throw new IllegalArgumentException("Cannot multiply matrix by vector with dimension != 4");

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

	
	// Return C = AB
	// where A = this and B = m
	Matrix4 times(Matrix4 m) {
		
		double[][] d = new double [4][4];
		
		for (int i = 0; i < 4; i++) {
			
			for (int j = 0; j < 4; j++) {
				
				double sum = 0;
				
				for (int k = 0; k < 4; k++) {
					sum += this.d[i][k] * m.d[k][j];
				}
				
				d[i][j] = sum;
				
			}
			
		}
		
		return new Matrix4(d);
		
	}
	
	// Source: https://www.scratchapixel.com/lessons/mathematics-physics-for-computer-graphics/lookat-function
	// TODO: test and fix
	static Matrix4 lookAt(Vector from, Vector to, Vector tmp) {

		{ 
			Vector forward = from.minus(to).normalize(); 
			Vector right = tmp.normalize().crossProduct(forward); 
			Vector up = forward.crossProduct(right); 

			double[][] camToWorld = new double[4][4]; 

			camToWorld[0][0] = right.x(); 
			camToWorld[1][0] = right.y(); 
			camToWorld[2][0] = right.z(); 
			camToWorld[0][1] = up.x(); 
			camToWorld[1][1] = up.y(); 
			camToWorld[2][1] = up.z(); 
			camToWorld[0][2] = forward.x(); 
			camToWorld[1][2] = forward.y(); 
			camToWorld[2][2] = forward.z(); 

			camToWorld[0][3] = from.x(); 
			camToWorld[1][3] = from.y(); 
			camToWorld[2][3] = from.z(); 

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

	public Matrix4 clone() {
		return new Matrix4(this.d);
	}

}