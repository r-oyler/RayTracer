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
		
		case 1:{

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
		
		case 2:{

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

		case 3:{

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

		case 4:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("textures\\gridred.png");
			p1.addTextureMap("textures\\gridgreen.png");
			p2.addTextureMap("textures\\gridblue.png");

			Sphere s = new Sphere(new Vector(1,1,1), Material.TEXTURE, 0.5);

			s.addTextureMap("textures\\checkerboard.png");

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

		case 5:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("textures\\gridred.png");
			p1.addTextureMap("textures\\gridgreen.png");
			p2.addTextureMap("textures\\gridblue.png");

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

		case 6:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("textures\\gridred.png");
			p1.addTextureMap("textures\\coloredgrid.png");
			p2.addTextureMap("textures\\gridblue.png");

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

		case 7:{

			Plane p0 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			Plane p1 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,0,1));
			Plane p2 = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(1,0,0));

			p0.addTextureMap("textures\\gridred.png");
			p1.addTextureMap("textures\\coloredgrid.png");
			p2.addTextureMap("textures\\gridblue.png");

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

		case 8:{
			
			Plane p = new Plane(new Vector(0,0,0), Material.TEXTURE, new Vector(0,1,0));
			p.addTextureMap("textures\\checkerboard.png");
			this.addObject(p);
			
			Sphere s = new Sphere(new Vector(0,1,0), Material.BLUE, 1);
			this.addObject(s);
			
			Triangle t = new Triangle(new Vector(2,0,0), new Vector(2,2,-1), new Vector(4,0,0), Material.BLUE);
			this.addObject(t);
			
			Disk d = new Disk(new Vector(-3,1,0), Material.BLUE, new Vector(0,1,1), 1);
			this.addObject(d);
			
			Cone c = new Cone(new Vector(-3,1,-3), Material.RED, new Vector(-1,1,-3), 0.5, 1);
			this.addObject(c);
			
			Cylinder cyl = new Cylinder(new Vector(3,1,-3), Material.RED, new Vector(1,1,-3), 1);
			this.addObject(cyl);
			
			Capsule cap = new Capsule(new Vector(5,1,-3), Material.RED, new Vector(7,1,-3), 1);
			this.addObject(cap);
			
			AABB aabb = new AABB(new Vector(-5,1,-3), Material.RED, new Vector(1,1,1));
			this.addObject(aabb);
			
			RoundedBox rb = new RoundedBox(new Vector(-2,1,3), Material.GREEN, new Vector(0.75,0.75,0.75), 0.25);
			this.addObject(rb);
			
			Ellipsoid e = new Ellipsoid(new Vector(1,1,3), Material.GREEN, new Vector(0.75,1,0.25));
			this.addObject(e);
			
			Torus tor = new Torus(new Vector(-5,1.5,3), Material.GREEN, 1, 0.5);
			this.addObject(tor);
			
			Sphere4 s4 = new Sphere4(new Vector(3,1,3), Material.GREEN, 1);
			this.addObject(s4);
			
			Vector camPos = new Vector(0,6,10);
			camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();
			
			Vector lightPos = camPos;
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, new Vector(0,1,0), new Vector(0,1,0)));

			break;
			
		}

		case 9:{
			
			Cylinder c = new Cylinder(new Vector(0,0,0), Material.WATER, new Vector(0,5,0), 2);
			this.addObject(c);
			
			Vector top = new Vector(1.5,6,0.2);
			Vector direction = new Vector(-3.0, -5.5, -0.2);
			Vector pencil_end = top.plus(direction.timesConst(0.8));
			
			Vector wood_end = top.plus(direction.timesConst(0.9));
			Vector lead_end = top.plus(direction);
			
			Material pencil = new Material("pencil");
			pencil.setAmbient(new Vector(245,171,11).divide(255));
			pencil.setDiffuse(new Vector(245,171,11).divide(255));
			pencil.setSpecular(new Vector(0.7,0.7,0.7),200);
			
			Cylinder c1 = new Cylinder(top, pencil, pencil_end, 0.25);
			this.addObject(c1);
			
			Material wood = new Material("wood");
			wood.setAmbient(new Vector(229,212,202).divide(255));
			wood.setDiffuse(new Vector(229,212,202).divide(255));
			
			Cone c2 = new Cone(pencil_end, wood, wood_end, 0.25, 0.1);
			this.addObject(c2);
			
			Material lead = new Material("lead");
			lead.setAmbient(new Vector(142,145,162).divide(255));
			lead.setDiffuse(new Vector(142,145,162).divide(255));
			lead.setSpecular(new Vector(0.7,0.7,0.7),200);
			
			Cone c3 = new Cone(wood_end, lead, lead_end, 0.1, 0);
			this.addObject(c3);
			
			Vector metal_end = top.minus(direction.timesConst(0.08));
			Vector rubber_end = top.minus(direction.timesConst(0.12));
			
			Material metal = new Material("metal");
			metal.setAmbient(new Vector(113,117,129).divide(255));
			metal.setDiffuse(new Vector(113,117,129).divide(255));
			metal.setSpecular(new Vector(0.7,0.7,0.7),200);
			
			Cylinder c4 = new Cylinder(top, metal, metal_end, 0.25);
			this.addObject(c4);
			
			Material rubber = new Material("rubber");
			rubber.setAmbient(new Vector(214,101,97).divide(255));
			rubber.setDiffuse(new Vector(214,101,97).divide(255));
			
			Cylinder c5 = new Cylinder(metal_end, rubber, rubber_end, 0.25);
			this.addObject(c5);
			
			Plane p = new Plane(new Vector(0,0,0), Material.GREY, new Vector(0,1,0));
			this.addObject(p);
			
			Vector camPos = new Vector(0,8,6);
			camPos = Matrix4.yRotationMatrix(Util.degreeToRadian(time*360)).timesV(camPos.addDim(1)).dropDim();
			
			Vector lightPos = new Vector(-50,100,40);
			this.addLight(new Light(lightPos,PlanetPixel.DIRECT_SUNLIGHT));

			this.setViewMatrix(Matrix4.lookAt(camPos, c.centre(), new Vector(0,1,0)));

			
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
