package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram{
	
	 private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
	    private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";
	    
	    /*
	     * We need to load up each of them to indicate which texture units they should be using.
	     */
	    
	    private int location_transformationMatrix;
	    private int location_projectionMatrix;
	    private int location_viewMatrix;
	    private int location_lightPosition;
	    private int location_lightColour;
	    private int location_shineDamper;
	    private int location_reflectivity;
	    private int location_backgroundTexture;
	    private int location_rTexture;
	    private int location_gTexture;
	    private int location_bTexture;
	    private int location_blendMap;
	    
	    public TerrainShader() {
	        super(VERTEX_FILE, FRAGMENT_FILE);
	    }
	 
	    @Override
	    protected void bindAttributes() {
	        super.bindAttribute(0, "position");
	        super.bindAttribute(1, "textureCoordinates ");
	        super.bindAttribute(2, "normal");
	    }
	 
	    @Override
	    protected void getAllUniformLocations() {
	        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	        location_viewMatrix = super.getUniformLocation("viewMatrix");
	        location_lightPosition = super.getUniformLocation("lightPosition");
	        location_lightColour = super.getUniformLocation("lightColour");
	        location_shineDamper = super.getUniformLocation("shineDamper");
	        location_reflectivity = super.getUniformLocation("reflectivity");
	        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
	        location_rTexture = super.getUniformLocation("rTexture");
	        location_gTexture = super.getUniformLocation("gTexture");
	        location_bTexture = super.getUniformLocation("bTexture");
	        location_blendMap = super.getUniformLocation("blendMap");
	    }
	    
	    /*
	     * We need to create up a method that loads up an int to each of the sampler 2D's to indicate which texture unit
	     * they should be referencing.
	     */
	    public void connectTextureUnits() {
	    	super.loadInt(location_backgroundTexture, 0);
	    	super.loadInt(location_rTexture, 1);
	    	super.loadInt(location_rTexture, 2);
	    	super.loadInt(location_rTexture, 3);
	    	super.loadInt(location_blendMap, 4);
	    }
	    
	    
	    public void loadShineVariables(float damper, float reflectivity) {
	    	
	    	super.loadFloat(location_shineDamper, damper);
	    	super.loadFloat(location_reflectivity, reflectivity);
	    	
	    }
	     
	    public void loadTransformationMatrix(Matrix4f matrix){
	        super.loadMatrix(location_transformationMatrix, matrix);
	    }
	     
	    public void loadViewMatrix(Camera camera){
	        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
	        super.loadMatrix(location_viewMatrix, viewMatrix);
	    }
	    
	    public void loadLight(Light light) {
	    	super.loadVector(location_lightPosition, light.getPosition());
	    	super.loadVector(location_lightColour, light.getColour());
	    }
	     
	    public void loadProjectionMatrix(Matrix4f projection){
	        super.loadMatrix(location_projectionMatrix, projection);
	    }	
}