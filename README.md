# RayTracer

## Outline

A ray tracer written in Java.

## Compiling

Clone or download the project.
In the project directory (contains README.md, src), run:

```
javac src/rayTracer/*.java -d classes
```

## Running

In the same directory, run:
```
java -cp classes rayTracer/RayTracer
```

## Features  
  
Ambient, diffuse, specular, reflective and refractive materials  
Grid supersampling anti-aliasing  
Animation generation  
Multithreading  
  
## Possible future features  
More UV mapping  
Input file parsing  
  
## Primitives  
  
Sphere  
Plane  
Triangle  
Disk  
Cone  
Cylinder  
Capsule  
AABB  
RoundedBox  
Ellipsoid  
Sphere4  
Infinite Cylinder - Normal calculation is incorrect  
Torus - Torus erronously casts shadows on itself   
    
## Example Output 
![Pencil](saved_outputs/pencil.gif "Pencil")