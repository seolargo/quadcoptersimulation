package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {
	/*
	 * It is going to handle all of the rendering code in our game.
	 */
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	/*
	 * Hashmap for containing all of the textured models (imagine 200 cubes) and their respective entities that need 
	 * to be rendered for a particular frame.
	 * Hashmap that will contain a load of textured model keys and each of them will be mapped to a list of the
	 * entities that use that specific textured model.
	 * So, we'll have basically have a list of all the entities for each textured model that's going to be rendered 
	 */
	private List<Terrain> terrains = new ArrayList<Terrain>(); //!!!!!
	
	public MasterRenderer(){
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		/*
		 * It has to wait until the projection matrix has been set up.
		 */
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
	}
	
	/*
	 * We need to create a method to enable back face culling and also a method that can disable back face culling.
	 */
	
	public static void enableCulling() {
		/*
		 * GL11.... --> we add 2 functions that will stop triangles facing away from the camera from being rendered.
		 * CullFace --> not render (GL_BACK --> NOT render the back faces of the model)
		 */
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
		
	}
	
	public void render(Light sun,Camera camera){
		/*
		 * will be called once every frame and it will render all the entities in the scene.
		 * 
		 */
		prepare();
		shader.start();
		/*
		 * It will take "sun" as a light. 
		 */
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity){
	
		/*
		 * Create a method that can actually put entities into this hashMap.
		 * They need to be sorted in every frame.
		 */
		
		TexturedModel entityModel = entity.getModel(); //we need to find out which textured model that entity is using by doing entity.getModel()
		List<Entity> batch = entities.get(entityModel); //we will get the list that corresponds to that entity from the HashMap.
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);		
		}
	}
	
	public void cleanUp(){
		/*
		 * Shader always need to be cleaned up at the end when we close the game.
		 */
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void prepare() { 
		/*
		 * We are going to call this once every frame and it will prepare openGL to render the game.
		 * 
		 */
		GL11.glEnable(GL11.GL_DEPTH_TEST); //all we need to do at the moment in this method is to clear the color from the last frame.
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.49f, 89f, 0.98f, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	

}
