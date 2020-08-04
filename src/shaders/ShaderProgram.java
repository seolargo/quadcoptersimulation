package shaders;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
 
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
 
public abstract class ShaderProgram {
	
	/*
	 * We need to be able to use and access that program from our Java code (fragment.txt + vertex.txt)
	 * abstract class --> This class is going to represent a generic shader program containing all the attributes and methods that every shader program would have.
	 */
     
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
   
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
     
    public ShaderProgram(String vertexFile,String fragmentFile){
        vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        //shaders are now attached to this program.
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }
     
    protected abstract void getAllUniformLocations(); //to make sure that all shader program classes will have a method.
     
    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID,uniformName);
    }
     
    public void start(){
        GL20.glUseProgram(programID);
    }
     
    public void stop(){
        GL20.glUseProgram(0);
    }
     
    public void cleanUp(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }
     
    protected abstract void bindAttributes();
     
    protected void bindAttribute(int attribute, String variableName){
    	/*
    	 * This will take in the number of the attribute list in the VAO that we want to bind and it will take in the variable name in the shader code
    	 * that we want to bind that attribute to. 
    	 */
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }
     
    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }
    
    protected void loadInt(int location, int value) {
    	GL20.glUniform1i(location, value);
    }
     
    protected void loadVector(int location, Vector3f vector){
        GL20.glUniform3f(location,vector.x,vector.y,vector.z);
    }
     
    protected void loadBoolean(int location, boolean value){
        float toLoad = 0;
        if(value){
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }
     
    protected void loadMatrix(int location, Matrix4f matrix){
        matrix.store(matrixBuffer);
        matrixBuffer.flip(); //to get it ready to be read from.
        GL20.glUniformMatrix4(location, false, matrixBuffer); //we load it up into the location of that uniform variable.
        //false for transformation
    }
    
	protected void load2DVector(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);
	}
	
     
    /*
     * Let's create a method that can load up shader source code files.
     * This method will take in the file name of the source code file and also
     * an int that indicates whether it's a vertex or a fragment shader.
     * 
     */
    private static int loadShader(String file, int type){
        
    	/*
    	 * What all this method does is that it opens up the source code files, read in all the lines and connect them altogether in one long string.
    	 * Before creating a new vertex or fragment shader depending on the type we gave it, attaching the source code to it, compiling it and then
    	 * printing any errors that were found in the code.
    	 * 
    	 */
    	
    	StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
 
}