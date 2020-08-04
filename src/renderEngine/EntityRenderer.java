package renderEngine;
 
import java.util.List;
import java.util.Map;
 
import models.RawModel;
import models.TexturedModel;
 
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
 
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import entities.Entity;
 
public class EntityRenderer {
 
    private StaticShader shader;
 
    public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
 
    public void render(Map<TexturedModel, List<Entity>> entities) {
    	/*
    	 * Loop all the HashMap
    	 */
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model); //prepare that textured model
            List<Entity> batch = entities.get(model); //get all of the entities that use that textured model and putting it in the textured model.
            for (Entity entity : batch) { //for each of the entity in this batch, we need to prepare the entity. 
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }
 
    private void prepareTexturedModel(TexturedModel model) {
    	
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0); //quad
        GL20.glEnableVertexAttribArray(1); //texture
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();
        //We want to disable culling whenever we're rendering a texture with transparency.
        if(texture.isHasTransparency()) {
        	MasterRenderer.disableCulling();
        }
        //we need to load up that boolean.
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //which texture we want to render on to our quads.
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }
 
    private void unbindTexturedModel() {
    	/*
    	 * A method that can unbind the textured model once we finish rendering all the entities that use that textured model.
    	 * That will do unbinding.
    	 */
    	MasterRenderer.enableCulling(); //definetely enabled for the next model.
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
 
    private void prepareInstance(Entity entity) {
    	/*
    	 * prepare "entities" of each of these textured models.
    	 * After preparing the textured model will then prepare all of the entities and render all of the entities
    	 * that use that textured model.
    	 */
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
 
}