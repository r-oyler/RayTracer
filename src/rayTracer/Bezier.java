package rayTracer;

public class Bezier {

	static Vector evalBezierCurve(Vector[] p, double t) {
		
		if (p.length != 4) throw new IllegalArgumentException("Bezier curve requires 4 control points");
		
		double k1 = (1-t)*(1-t)*(1-t);
		double k2 = 3*(1-t)*(1-t)*t;
		double k3 = 3*(1-t)*t*t;
		double k4 = t*t*t;
		
		Vector p1 = p[0].timesConst(k1);
		Vector p2 = p[1].timesConst(k2);
		Vector p3 = p[2].timesConst(k3);
		Vector p4 = p[3].timesConst(k4);
		
		Vector point = p1.plus(p2).plus(p3).plus(p4);
		
		return point;
		
	}
	
	static Vector evalBezierSurface(Vector[] p, double u, double v) {
		
		if (p.length != 16) throw new IllegalArgumentException("Bezier surface requires 16 control points");

		Vector[] pu = new Vector[4];
		
		for (int i = 0; i<4; i++) {
			
			Vector[] curveP = new Vector[4];
			curveP[0] = p[i*4];
			curveP[1] = p[i*4 + 1];
			curveP[2] = p[i*4 + 2];
			curveP[3] = p[i*4 + 3];
			
			pu[i] = evalBezierCurve(curveP, u);
			
		}
		
		return evalBezierCurve(pu, v);
		
	}
	
}
