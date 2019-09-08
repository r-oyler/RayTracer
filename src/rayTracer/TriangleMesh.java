package rayTracer;

public class TriangleMesh extends MatObject {

	int numTris;
	int[] vertexIndex;
	Vector[] vertices;
	Vector[] normals;
	Vector minBounds;
	Vector maxBounds;
	AABB aabb;

	public TriangleMesh(int numTris, int[] vertexIndex, Vector[] vertices, Vector[] normal, Material m) {
		super(new Vector(0,0,0), m);
		this.numTris = numTris;
		this.vertexIndex = vertexIndex;
		this.vertices = vertices;
		this.normals = normal;
		this.findBounds();
		this.aabb = new AABB(minBounds,maxBounds);
	}

	@Override
	boolean Intersect(Ray ray, IntersectInfo info) {

		boolean hit = false;

		if (aabb.intersect(ray)) {

			for (int i=0; i<numTris; i++) {

				Triangle t = new Triangle(
						vertices[vertexIndex[3*i]],
						vertices[vertexIndex[3*i+1]], 
						vertices[vertexIndex[3*i+2]], 
						material);

				boolean intersect = t.Intersect(ray, info);
				hit = hit | intersect;

			}

		}

		return hit;
	}

	@Override
	Vector calcUV(Vector hitPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {

		String s = new String("");
		s += "numTris: " + numTris + "\nvertexIndex: ";

		for (int i: vertexIndex) s+= i + " ";

		s+= "\nvertices:\n";

		for (Vector v: vertices) s+= v.toString() + "\n";

		return s;

	}

	public void findBounds() {

		minBounds = vertices[0].clone();
		maxBounds = vertices[0].clone();

		for (int i=1; i<vertices.length; i++) {
			if (vertices[i].x() < minBounds.x()) minBounds.setX(vertices[i].x());
			if (vertices[i].x() > maxBounds.x()) maxBounds.setX(vertices[i].x());
			if (vertices[i].y() < minBounds.y()) minBounds.setY(vertices[i].y());
			if (vertices[i].y() > maxBounds.y()) maxBounds.setY(vertices[i].y());
			if (vertices[i].z() < minBounds.z()) minBounds.setZ(vertices[i].z());
			if (vertices[i].z() > maxBounds.z()) maxBounds.setZ(vertices[i].z());
		}

	}

	public Vector centre() {

		Vector centre = minBounds.plus(maxBounds).divide(2);

		return centre;

	}

	@Override
	public TriangleMesh clone() {

		Vector[] cloneVertices = new Vector[vertices.length];
		for (int i=0; i<vertices.length; i++) cloneVertices[i] = vertices[i].clone();

		Vector[] cloneNormals = new Vector[vertices.length];
		for (int i=0; i<vertices.length; i++) cloneNormals[i] = normals[i].clone();

		return new TriangleMesh(numTris, vertexIndex.clone(), vertices, normals, material.clone());
	}



}
