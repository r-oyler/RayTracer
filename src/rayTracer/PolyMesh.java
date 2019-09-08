package rayTracer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PolyMesh {

	int numFaces;
	int[] faceIndex;
	int[] vertexIndex;
	Vector[] vertices;
	Vector[] normals;
	Material material;

	public PolyMesh(int numFaces, int[] faceIndex, int[] vertexIndex, Vector[] vertexes, Vector[] normals, Material m) {
		this.numFaces = numFaces;
		this.faceIndex = faceIndex;
		this.vertexIndex = vertexIndex;
		this.vertices = vertexes;
		this.normals = normals;
		this.material = m;
	}

	static public PolyMesh ReadFile(String fileName, Material m){

		PolyMesh pm = null;
		
		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(fileName));

			int numFaces;
			// line 1: number of faces on polygon mesh
			numFaces = Integer.parseInt(br.readLine().trim());

			int[] faceIndex = new int[numFaces];

			// line 2: face index array
			String line = br.readLine();
			String[] sLine = line.split(" ");

			int totalFaceVertices = 0;

			for (int i=0; i<numFaces; i++) {
				faceIndex[i] = Integer.parseInt(sLine[i].trim());
				totalFaceVertices+= faceIndex[i];
			}

			int[] vertexIndex = new int[totalFaceVertices];

			// line 3: vertex index array
			line = br.readLine();
			sLine = line.split(" ");

			for (int i=0; i<totalFaceVertices; i++) {
				vertexIndex[i] = Integer.parseInt(sLine[i].trim());
			}

			// line 4: vertices
			line = br.readLine();
			sLine = line.split(" ");

			if (sLine.length % 3 !=0) throw new RuntimeException("File vertices line contains a number of doubles not divisible by 3");

			Vector[] vertices = new Vector[sLine.length/3];

			for (int i=0; i<sLine.length/3; i++) {
				vertices[i] = new Vector(
						Double.parseDouble(sLine[3*i].trim()),
						Double.parseDouble(sLine[3*i+1].trim()),
						Double.parseDouble(sLine[3*i+2].trim())
						);
			}

			// line 5: vertex normals
			line = br.readLine();
			sLine = line.split(" ");

			if (sLine.length % 3 !=0) throw new RuntimeException("File vertices line contains a number of doubles not divisible by 3");
			if (sLine.length/3 != totalFaceVertices) throw new RuntimeException("File normals line contains a number of integers not divisible by 3");

			Vector[] normals = new Vector[sLine.length/3];

			for (int i=0; i<sLine.length/3; i++) {
				normals[i] = new Vector(
						Double.parseDouble(sLine[3*i].trim()),
						Double.parseDouble(sLine[3*i+1].trim()),
						Double.parseDouble(sLine[3*i+2].trim())
						);
			}

			pm = new PolyMesh(numFaces, faceIndex, vertexIndex, vertices, normals, m);			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return pm;

	}

	public TriangleMesh GenerateTriangleMesh() {
		
		int numTris = 0;
		
		for (int i=0; i<numFaces; i++) {
			numTris+= faceIndex[i] - 2;
		}
		
		int[] triVertexIndex = new int[numTris*3];
		Vector[] triNormals = new Vector[numTris*3];
		
		int triNum = 0;
		
		// for each polygon
		for (int i=0, vertNum=0; i<numFaces; i++) {
			
			// for each triangle in face
			for (int j=0; j<faceIndex[i] - 2; j++) {
				
				triVertexIndex[triNum] = vertexIndex[vertNum];
				triNormals[triNum] = normals[vertNum];
				
				triVertexIndex[triNum + 1] = vertexIndex[vertNum + j + 1];
				triNormals[triNum + 1] = normals[vertNum + j + 1];
				
				triVertexIndex[triNum + 2] = vertexIndex[vertNum + j + 2];
				triNormals[triNum + 2] = normals[vertNum + j + 2];
				
				triNum+=3;
			}
			
			vertNum+=faceIndex[i];
			
		}
		
		return new TriangleMesh(numTris, triVertexIndex, vertices, triNormals, material);
		
	}
	
}

