package rayTracer;

import java.util.ArrayList;

public class Scene {

	// Transforms from world-space to view-space
	Matrix4 viewMatrix;
	// Structure containing all scene objects
	ArrayList<MatObject> objects;
	// Structure containing all scene lights
	ArrayList<Light> lights;
	// The ambient light that is cast on every object
	Vector ambientLight;
	// The color that is seen when a ray doesn't hit an object;
	Vector backgroundColor;

	public Scene() {
		objects = new ArrayList<MatObject>();
		lights = new ArrayList<Light>();
	}

	public Matrix4 getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(Matrix4 viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public ArrayList<MatObject> getObjects() {
		return objects;
	}

	public void addObject(MatObject object) {
		this.objects.add(object);
	}

	public ArrayList<Light> getLights() {
		return lights;
	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public Vector getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(Vector ambientLight) {
		this.ambientLight = ambientLight;
	}

	public Vector getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Vector backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setScene(int i, double time) {

		switch(i) {

		case 0:
		{
			AABB aabb = new AABB(new Vector(0,0,0), Material.RED, new Vector(1,1,1));

			Vector camPos = new Vector(0.5,0.5,3);

			this.addObject(aabb);
			this.addLight(new Light(camPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, aabb.p0, new Vector(0,1,0)));

			break;
		}

		case 1:
		{
			Sphere s = new Sphere(new Vector(0,0,0), Material.RED, 1);

			Vector camPos = new Vector(0,0,3);
			Vector ligPos = new Vector(2,0,3);

			this.addObject(s);
			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));

			break;
		}
		case 2:
		{
			Vector p0 = new Vector(0,0,0);
			Vector p1 = new Vector(1,0,0);
			Vector p2 = p0.plus(new Vector(1,0,1).normalize());
			Vector p3 = new Vector(p2.x(),Math.sqrt(6)/3f,(p0.z()+p1.z()+p2.z())/3);	

			Vector centre = (p0.plus(p1).plus(p2).plus(p3)).divide(4);

			Triangle t0 = new Triangle(p0,p1,p2, Material.RED);
			Triangle t1 = new Triangle(p1,p0,p3, Material.GREEN);
			Triangle t2 = new Triangle(p2,p1,p3, Material.BLUE);
			Triangle t3 = new Triangle(p0,p2,p3, Material.YELLOW);

			Vector camPos = new Vector(0,0,3);
			Vector ligPos = camPos;

			this.addObject(t0);
			this.addObject(t1);
			this.addObject(t2);
			this.addObject(t3);

			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, centre, new Vector(0,1,0)));

			break;
		}

		case 3:{

			Vector p0 = new Vector(0,0,0);
			Vector p1 = new Vector(0,1,0);
			Vector p2 = new Vector(1,0,0);

			Triangle t = new Triangle(p0,p1,p2, Material.RED);

			Vector camPos = new Vector(0,0,1);
			Vector ligPos = camPos;

			this.addObject(t);
			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, t.centre(), new Vector(0,1,0)));

			break;
		}

		case 4:{

			Vector p0 = new Vector(0,0,0);
			Vector n = new Vector(1,1,1);

			Disk d = new Disk(p0, Material.RED, n, 1);

			Vector camPos = new Vector(0,0,5);
			Vector ligPos = camPos;

			this.addObject(d);
			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, p0, new Vector(0,1,0)));
			break;
		}

		case 5:{

			Plane p0 = new Plane(new Vector(0,0,-1.1), Material.RED, new Vector(0,0,1));
			Plane p1 = new Plane(new Vector(0,-1.1,0), Material.GREEN, new Vector(0,1,0));
			Plane p2 = new Plane(new Vector(-1.1,0,0), Material.BLUE, new Vector(1,0,0));

			Sphere s = new Sphere(new Vector(0,0,0), Material.MIRROR, 1);

			Vector camPos = new Vector(1,1,4);
			Vector ligPos = new Vector(0,5,0);

			this.addObject(p0);
			this.addObject(p1);
			this.addObject(p2);
			this.addObject(s);
			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));
			break;
		}

		case 6:{

			Sphere s = new Sphere(new Vector(0,0,0), Material.BLACK, 2);

			s.addTextureMap("2k_moon.jpg");

			Vector camPos = new Vector(4,4,4);
			Vector ligPos = new Vector(0,0,4);

			this.addObject(s);
			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));
			this.setViewMatrix(Matrix4.lookAt(camPos, s.Position(), new Vector(0,1,0)));
			break;
		}

		case 7:{

			AABB aabb = new AABB(new Vector(1,1,1), Material.RED, new Vector(1,1,1));
			AABB aabb2 = new AABB(new Vector(5,1,1), Material.BLUE, new Vector(1,1,1));
			AABB aabb3 = new AABB(new Vector(1,5,1), Material.YELLOW, new Vector(1,1,1));
			AABB aabb4 = new AABB(new Vector(1,1,5), Material.GREEN, new Vector(1,1,1));

			Vector camPos = new Vector(8,8,8);
			Vector lightPos = camPos;

			this.addObject(aabb);
			this.addObject(aabb2);
			this.addObject(aabb3);
			this.addObject(aabb4);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, aabb.p0, new Vector(0,1,0)));

			break;
		}

		case 8:{

			AABB aabb = new AABB(new Vector(0,0,0), Material.RED, new Vector(1,1,1));
			AABB aabb2 = new AABB(new Vector(2,0,0), Material.BLUE, new Vector(0.5,0.5,0.5));
			AABB aabb3 = new AABB(new Vector(0,2,0), Material.YELLOW, new Vector(0.5,0.5,0.5));
			AABB aabb4 = new AABB(new Vector(0,0,2), Material.GREEN, new Vector(0.5,0.5,0.5));


			Vector camPos = new Vector(8,8,8);
			Vector lightPos = new Vector(0,8,0);

			this.addObject(aabb);
			this.addObject(aabb2);
			this.addObject(aabb3);
			this.addObject(aabb4);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, aabb.p0, new Vector(0,1,0)));

			aabb2.rotateY(Util.degreeToRadian(45));
			aabb3.rotateX(Util.degreeToRadian(45));
			aabb4.rotateZ(Util.degreeToRadian(45));

			break;
		}

		case 9:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("gridred.png");
			p1.addTextureMap("gridgreen.png");
			p2.addTextureMap("gridblue.png");

			Sphere s = new Sphere(new Vector(1,1,1), Material.TEXTURE, 0.5);

			s.addTextureMap("checkerboard.png");

			Vector camPos = new Vector(3,3,3);
			Vector lightPos = new Vector(3,5,2);

			this.addObject(p0);
			this.addObject(p1);
			this.addObject(p2);
			this.addObject(s);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, s.p0, new Vector(0,1,0)));

			break;
		}

		case 10:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("gridred.png");
			p1.addTextureMap("gridgreen.png");
			p2.addTextureMap("gridblue.png");

			Sphere s = new Sphere(new Vector(1,1,1), Material.GLASS, 0.5);

			Vector camPos = new Vector(3,3,3);
			Vector lightPos = new Vector(3,3,3);

			this.addObject(p0);
			this.addObject(p1);
			this.addObject(p2);
			this.addObject(s);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(1,1,1), new Vector(0,1,0)));

			break;
		}

		case 11:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("gridred.png");
			p1.addTextureMap("coloredgrid.png");
			p2.addTextureMap("gridblue.png");

			Sphere s0 = new Sphere(new Vector(1,1,1), Material.DIAMOND, 0.5);
			Sphere s1 = new Sphere(new Vector(3,1,1), Material.SAPPHIRE, 0.5);
			Sphere s2 = new Sphere(new Vector(5,1,1), Material.EMERALD, 0.5);
			Sphere s3 = new Sphere(new Vector(7,1,1), Material.RUBY, 0.5);

			Vector camPos = new Vector(4,1,5);
			Vector lightPos = new Vector(4,4,3);

			this.addObject(p0);
			this.addObject(p1);
			this.addObject(p2);
			this.addObject(s0);
			this.addObject(s1);
			this.addObject(s2);
			this.addObject(s3);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(4,1,1), new Vector(0,1,0)));

			break;
		}

		case 12:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("gridred.png");
			p1.addTextureMap("coloredgrid.png");
			p2.addTextureMap("gridblue.png");

			Sphere s0 = new Sphere(new Vector(5,1,1), Material.BRASS, 0.5);

			Vector camPos = new Vector(4,1,5);
			Vector lightPos = new Vector(4,4,3);

			this.addObject(p0);
			this.addObject(p1);
			this.addObject(p2);
			this.addObject(s0);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(4,1,1), new Vector(0,1,0)));

			break;
		}

		case 13:{

			InfCylinder c = new InfCylinder(new Vector(0,0,0), Material.BLUE, new Vector(1,0,0), 1);

			Vector camPos = new Vector(2,2,5);
			Vector lightPos = new Vector(0,0,5);

			this.addObject(c);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;
		}

		case 14:{

			Cylinder c = new Cylinder(new Vector(-2,0,0), Material.BLUE, new Vector(2,0,0), 1);

			Vector camPos = new Vector(-3,0,4);
			Vector lightPos = new Vector(-3,0,4);

			this.addObject(c);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;

		}

		case 15:{

			Capsule c = new Capsule(new Vector(-2,0,0), Material.BLUE, new Vector(2,0,0), 1);

			Vector camPos = new Vector(2,2,5);
			Vector lightPos = new Vector(0,0,5);

			this.addObject(c);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;

		}

		case 16:{

			Cone c = new Cone(new Vector(-2,0,0), Material.BLUE, new Vector(2,0,0), 0.5, 1);

			Vector camPos = new Vector(-3,0,5);
			Vector lightPos = new Vector(0,0,5);

			this.addObject(c);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;

		}

		case 17:{

			RoundedBox r = new RoundedBox(new Vector(0,0,0), Material.BLUE, new Vector(2,4,1), 0.5);

			Vector camPos = new Vector(0,0,15);

			camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();

			Vector lightPos = new Vector(0,0,15);

			this.addObject(r);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			r.rotateY(Util.degreeToRadian(0));

			break;

		}

		case 18:{

			double sin = (Math.sin(time*2*Math.PI)/2)+0.5;

			Vector v = new Vector(sin,0,1-sin);

			Material m = new Material("m");
			m.setAmbient(v.timesConst(0.2));
			m.setDiffuse(v);
			m.setSpecular(new Vector(1,1,1),20);
			Sphere s = new Sphere(new Vector(sin,0,1-sin), m, 1);

			Vector camPos = new Vector(0,0,3);
			Vector ligPos = new Vector(2,0,3);

			this.addObject(s);
			this.addLight(new Light(ligPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;

		}
		
		case 19:{
			
			Ellipsoid e = new Ellipsoid(new Vector(0,0,0), Material.BLUE, new Vector(1,0.5,0.6));

			Vector camPos = new Vector(0,0,5);
			
			camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();
			
			Vector lightPos = new Vector(0,0,5);

			this.addObject(e);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;
			
		}
		
		case 20:{
			
			Torus t = new Torus(new Vector(0,0,0), Material.BLUE, 4,2);
			
			Vector camPos = new Vector(0,0,15);
			camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();
			
			Vector lightPos = new Vector(0,0,15);

			this.addObject(t);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;
			
		}
		
		case 21:{
			
			Sphere4 s = new Sphere4(new Vector(0,0,0), Material.BLUE, 1);
			
			Vector camPos = new Vector(0,0,5);
			camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();
			
			Vector lightPos = camPos;

			this.addObject(s);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,0,0), new Vector(0,1,0)));

			break;
			
		}

		}

	}

	public Scene clone() {

		Scene clone = new Scene();
		clone.setViewMatrix(viewMatrix.clone());
		for (MatObject o: this.objects) {
			clone.addObject(o.clone());
		}
		for (Light l: this.lights) {
			clone.addLight(l.clone());
		}
		clone.setAmbientLight(this.ambientLight);
		clone.setBackgroundColor(this.backgroundColor);
		return clone;

	}

}
