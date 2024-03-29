package rayTracer;

public class Vector {

	double[] d;
	int dim;

	public Vector(double ... args) {

		dim = args.length;

		if(dim == 0) throw new IllegalArgumentException("Cannot create a vector with 0 dimensions");

		d = new double[dim];

		for(int i = 0; i<dim; i++) {
			d[i] = args[i];
		}

	}

	Vector plus(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");

		double[] sum = new double[dim];

		for(int i = 0; i<dim; i++) {
			sum[i] = d[i]+v.d[i];
		}

		return new Vector(sum);

	}
	
	Vector plus(double d) {
		
		double[] sum = new double[dim];

		for(int i = 0; i<dim; i++) {
			sum[i] = this.d[i]+d;
		}

		return new Vector(sum);
		
	}

	// B - A = Vector from A to B
	Vector minus(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");

		double[] dif = new double[dim];

		for(int i = 0; i<dim; i++) {
			dif[i] = d[i]-v.d[i];
		}

		return new Vector(dif);

	}

	Vector negative() {
		return this.timesConst(-1);
	}
	
	Vector timesConst(double s) {

		double[] prod = new double[dim];

		for(int i = 0; i<dim; i++) {
			prod[i] = d[i]*s;
		}

		return new Vector(prod);
	}

	Vector divide(double s) {

		double[] quot = new double[dim];

		for(int i = 0; i<dim; i++) {
			quot[i] = d[i]/s;
		}

		return new Vector(quot);
	}
	
	Vector inverseDivide(double s) {

		double[] quot = new double[dim];

		for(int i = 0; i<dim; i++) {
			quot[i] = s/d[i];
		}

		return new Vector(quot);
	}


	double dotProduct(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");

		double prod = 0;

		for (int i = 0; i<dim; i++) {
			prod += d[i]*v.d[i];
		}

		return prod;
	}
	
	double dotSelf() {
		return this.dotProduct(this);
	}

	Vector multComponents(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");

		double[] prod = new double[this.dim];

		for (int i = 0; i<dim; i++) {
			prod[i] = d[i]*v.d[i];
		}

		return new Vector(prod);
	}

	Vector divComponents(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");

		double[] quot = new double[this.dim];

		for (int i = 0; i<dim; i++) {
			quot[i] = d[i]/v.d[i];
		}

		return new Vector(quot);
	}
	
	public boolean equals(Vector v) {
		
		if(this.dim != v.dim) return false;
		
		boolean equal = true;
		
		for (int i = 0; i<this.dim; i++) {
			equal &= (this.d[i] == v.d[i]);
		}
		
		return equal;
		
	}

	double cosAngleBetween(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");

		double angle = this.dotProduct(v);
		angle = angle/(this.length()*v.length());

		return angle;

	}

	double length() {

		double len = 0;

		for (int i = 0; i<dim; i++) {
			len += d[i]*d[i];
		}

		return Math.sqrt(len);

	}

	double squaredLength() {

		double len = 0;

		for (int i = 0; i<dim; i++) {
			len += d[i]*d[i];
		}

		return len;

	}

	Vector normalize() {

		return this.timesConst(1.0/this.length());

	}

	Vector absolute() {
		
		double[] abs = new double[this.dim];

		for (int i = 0; i<dim; i++) {
			abs[i] = Math.abs(this.d[i]);
		}

		return new Vector(abs);
		
	}
	
	// Source: https://rosettacode.org/wiki/Vector_products#Java
	Vector crossProduct(Vector v) {

		if(this.dim != 3 || v.dim !=3) throw new IllegalArgumentException("Invalid vector dimensions. Both vectors must have dimension of 3");

		double a = this.d[1]*v.d[2] - this.d[2]*v.d[1];
		double b = this.d[2]*v.d[0] - this.d[0]*v.d[2];
		double c = this.d[0]*v.d[1] - this.d[1]*v.d[0];

		return new Vector(a,b,c);

	}

	// Add dimension to vector, new dimension is given x
	Vector addDim(double x) {

		double[] d = new double[this.dim+1];

		for (int i = 0; i<this.dim; i++) {
			d[i] = this.d[i];
		}

		d[d.length-1] = x;

		return new Vector(d);
	}

	// Drop dimension from vector
	Vector dropDim(){

		if(this.dim == 1) throw new IllegalArgumentException("Cannot drop dimension from vector with 1 dimension");

		double[] d = new double[dim-1];

		for (int i = 0; i<dim-1; i++) {
			d[i] = this.d[i];
		}

		return new Vector(d);

	}

	public Vector sign() {

		double[] sign = new double[this.dim];

		for (int i = 0; i<dim; i++) {

			sign[i] = Math.signum(this.d[i]);
			
		}

		return new Vector(sign);

	}

	public Vector step(Vector edge) {
		
		double[] step = new double[this.dim];

		for (int i = 0; i<dim; i++) {

			step[i] = Util.step(edge.d[i], this.d[i]);
			
		}

		return new Vector(step);
		
	}
	
	public double x() {
		return this.d[0];
	}

	public double y() {
		if(this.dim < 2) throw new IllegalArgumentException("Cannot return y value from vector with " + this.dim + " dimensions");
		return this.d[1];
	}

	public double z() {
		if(this.dim < 3) throw new IllegalArgumentException("Cannot return z value from vector with " + this.dim + " dimensions");
		return this.d[2];
	}
	
	public void setX(double d) {
		this.d[0] = d;
	}
	
	public void setY(double d) {
		if(this.dim < 2) throw new IllegalArgumentException("Cannot set y value from vector with " + this.dim + " dimensions");
		this.d[1] = d;
	}
	
	public void setZ(double d) {
		if(this.dim < 3) throw new IllegalArgumentException("Cannot set z value from vector with " + this.dim + " dimensions");
		this.d[2] = d;
	}

	public boolean elementsBetweenInc(double min, double max) {

		boolean result = true;

		for (int i = 0; i<dim && result; i++) {
			result &= Util.isBetweenInc(this.d[i], min, max);
		}

		return result;

	}

	public boolean elementsBetweenExc(double min, double max) {

		boolean result = true;

		for (int i = 0; i<dim && result; i++) {
			result &= Util.isBetweenExc(this.d[i], min, max);
		}

		return result;

	}
	
	public Vector max(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");
		
		double[] d = new double[this.dim];
		
		for (int i = 0; i < this.dim; i++) {
			d[i] = Math.max(this.d[i], v.d[i]);
		}
		
		return new Vector(d);
		
	}

	public Vector max(double d) {
		
		double[] d1 = new double[this.dim];
		
		for (int i = 0; i < this.dim; i++) {
			d1[i] = Math.max(this.d[i], d);
		}
		
		return new Vector(d1);
		
	}
	
	public double max() {
		
		double max = d[0];
		
		for (int i = 1; i < this.dim; i++) {
			max = Math.max(this.d[i], max);
		}
		
		return max;
		
	}
	
	public Vector min(Vector v) {

		if(this.dim != v.dim) throw new IllegalArgumentException("Vector dimension mismatch.");
		
		double[] d = new double[this.dim];
		
		for (int i = 0; i < this.dim; i++) {
			d[i] = Math.min(this.d[i], v.d[i]);
		}
		
		return new Vector(d);
		
	}
	
	public Vector min(double d) {
		
		double[] d1 = new double[this.dim];
		
		for (int i = 0; i < this.dim; i++) {
			d1[i] = Math.min(this.d[i], d);
		}
		
		return new Vector(d1);
		
	}
	
	public double min() {
		
		double max = d[0];
		
		for (int i = 1; i < this.dim; i++) {
			max = Math.min(this.d[i], max);
		}
		
		return max;
		
	}
	
	public String toString() {

		String s = "(";

		for (int i = 0; i<dim; i++) {
			s = s + this.d[i] + ", ";
		}

		s = s.substring(0, s.length()-2);

		s = s + ")";

		return s;

	}

	public Vector clone() {
		return new Vector(this.d);
	}
	
}