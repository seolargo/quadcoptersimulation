package engineTester;
 
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import entities.Player;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop{
	
	public static void main(String args[]) {
    	
	//That will open up the display.
     	DisplayManager.createDisplay();
    	//That will load all the necessary stuff.
        Loader loader = new Loader();
        
        /*
         * *************TERRAIN TEXTURE STUFF**************
         */
        //Make the ground green (grassy).
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        //Where the ground is mixed with mud, pinkFlowers, path etc.
        //TODO
        //Should be fixed later. But not emergency.
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud")); 
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));        
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        //***************************************************
        
        //RawModel object --> "tree 23.51". It will be used by many times in the simulation.
        RawModel model = OBJLoader.loadObjModel("tree 23.51", loader);
        //Terrain models where they will be uploaded to the simulation.
        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
        //Necessary codes for the models we have initialized above.
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        flower.getTexture().setHasTransparency(true);
        flower.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);
        //An ArrayList of entities object.
        List<Entity> entities = new ArrayList<Entity>();
        //Randomized till 676452nd number.
        Random random = new Random(676452);
        for(int i = 0; i < 400; i++) {
        	if(i % 7 == 0){
        		//Code where you add something randomly.
        		entities.add(new Entity(grass, new Vector3f(random.nextFloat()*400 - 200,0,random.nextFloat() * -400),0,0,0,1.8f));
        		entities.add(new Entity(flower, new Vector3f(random.nextFloat()*400 - 200,0, random.nextFloat() * -400), 0, 0, 0, 2.3f));
        	}
        	if(i % 3 == 0) {
        		entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,random.nextFloat() * 1 + 4));
        		entities.add(new Entity(fern, new Vector3f(random.nextFloat()*400 - 200,0,random.nextFloat() * -400),0, random.nextFloat()* 360, 0, 0.9f));
        	}
       }
       //Code where you initialized light object. 
       Light light = new Light(new Vector3f(20000,40000,2000),new Vector3f(1,1,1));
       
       /*
        * Code where all the terrain is green. Not supported to be used.
        */
       //Terrain terrain = new Terrain(0,0,loader, new ModelTexture(loader.loadTexture("grass")));
       //Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")));
      
       /*
        * Code where the terrain is mixtured. Supported to be used.
        */
       Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
       Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);
       
       MasterRenderer renderer = new MasterRenderer();
       
       //Where the quadcopter model has been initalized. "drone23.53" is the .obj file which you design by using Blender. Could be replaced by any other .obj file.
       RawModel droneModel = OBJLoader.loadObjModel("drone23.53", loader);
       TexturedModel drone = new TexturedModel(droneModel, new ModelTexture(loader.loadTexture("white")));
       
       //Code for rotation of the quadcopter with the given angular velocites from user. 
       /*float rotX = 0, rotY = 0, rotZ = 0; 
       if(w1 + w2 > w3 + w4) {
    	   rotX = -90;
    	   rotY = 45;
    	   rotZ = -20;
       } else if(w2 + w3 > w1 + w4) {
    	   rotX = -45;
    	   rotY = 0;
    	   rotZ = 70;
       } else if(w1 + w4 > w3 + w2) {
    	   rotX = -45;
    	   rotY = 180;
    	   rotZ = 70;
       } else if(w3 + w4 > w1 + w2) {
    	   rotX = -90;
    	   rotY = 45;
    	   rotZ = -30;
       } else if(w1 == w2 && w3 == w4 && w2 == w3) {
    	   rotX = 0;
    	   rotY = 0;
    	   rotZ = -40;
       }*/
       
       //Player is the object where (new Vector3f(X, Z, Y) (X, Y, Z are the starting point for quadcopter)) initialization stands for the position of the quadcopter.
       //0, -60, -40 is perfect for starting.
       Player player = new Player(drone, new Vector3f(0, 0, -50), 0, 45, -40, 1); //new Vector3f(X, Z, Y), rotX, rotY, rotZ, scale
       //Initialized for the camera object.
       Camera camera = new Camera(player);
       
       //While the user does not close the window, it keeps running.
       while(!Display.isCloseRequested()) 
       	  {
    		   /*
    		    ******************************** Game Logic ***************************************
    		    * 
    		    * This is where all of the objects are updated every frame or the rendering happens.
    		    */
    	   		//Camera moves.
            	camera.move();
            	//Player moves with the parameters it takes.
            	//NOTE: Do not try to add if/else in this method.
            	player.move();
            	//Put it the entity that you want to render and do that for every single entity that you want to render.
                renderer.processEntity(player); 
                //Renders the terrain.
                renderer.processTerrain(terrain);
                //Renders the terrain2.
                renderer.processTerrain(terrain2);
                //For each entity, render it.
                for(Entity entity : entities){
                	renderer.processEntity(entity);
                }
                //Render the light object and the camera object.
                renderer.render(light, camera);
                //In every frame, we update the display.
                DisplayManager.updateDisplay();
                
       		}
       			//Delete all the Vertex Array Objects and Vertex Buffer Objects that we've created.
    	   		renderer.cleanUp();
                loader.cleanUp();
                
                //When everything finishes, we close it up.
                DisplayManager.closeDisplay();
	}
	
}