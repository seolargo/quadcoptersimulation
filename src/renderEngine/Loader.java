package renderEngine;
 
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
 
import models.RawModel;
 
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
 
public class Loader {
	
	/*
	 * This class is going to deal with loading 3D models into memory by storing positional data about the model in a VAO.
	 */
     
	/*
	 * Once we close the game, we delete all the VAO's and the VBO's that we've created in memory.
	 */
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    //Those will keep track of all the VAO's and VBO's that we create.
    
    private List<Integer> textures = new ArrayList<Integer>(); //when we close the game, we're going to need to delete all of the textures. So we need to keep track of them
    //when they're created.
     
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions); //store the positional data into one of the attribute lists of the VAO; 0 --> first, why not?  
        //3 for the positions bcs they are 3D vector.
        storeDataInAttributeList(1,2,textureCoords);
        //texture for 1, 2D for texture --> 2
        storeDataInAttributeList(2,3,normals);
        //each normal is a 3D vector.
        unbindVAO(); // we finished using VAO and we will unbind it and then return the data that we've created.
        return new RawModel(vaoID, indices.length); //returns information about the VAO as a RawModel.
        //and it also needs to know the number of vertices of the model. 
    }
    
    public int loadToVAO(float[] positions, float[] textureCoords){
        int vaoID = createVAO();
        //The positions are going to be stored in attribute lists 0 and they are 2D coordinates, bcs they are 2D quads.
        storeDataInAttributeList(0, 3, positions); //store the positional data into one of the attribute lists of the VAO; 0 --> first, why not?  
        //3 for the positions bcs they are 3D vector.
        storeDataInAttributeList(1, 2, textureCoords);
        //texture for 1, 2D for texture --> 2
        //each normal is a 3D vector.
        unbindVAO(); // we finished using VAO and we will unbind it and then return the data that we've created.
        return vaoID; 
    }
     
    public int loadTexture(String fileName) {
    	/*
    	 * It is going to load up a texture into OpenGL.
    	 * It will load up into memory, and return the ID of the texture.
    	 */
       	Texture texture = null;
        try {
        	texture = TextureLoader.getTexture("PNG",
                    new FileInputStream("res/" + fileName + ".png"));
        	GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D); //we generate all the lower resolution versions of the texture by calling GLgeneratemipmap and then we put in the type of the texture.
        	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR); //Then we have to use these lower resolution images, so we call OpenGL.
        	//linear parts basically tell it to transition smoothly btw different texture resolutions.
        	GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f); //- makes the textures rendered and slightly high resolution.
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ".png , didn't work");
            System.exit(-1);
        }
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    	
        textures.add(texture.getTextureID()); //we're going to store the ID of the texture into that list.
        return texture.getTextureID();
    }
     
    public void cleanUp(){ //delete VAO's and VBO's
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture:textures){
            GL11.glDeleteTextures(texture);
        }
    }
     
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays(); //creates an empty VAO. glGenerateVertexArrays()
        vaos.add(vaoID); //whenever we create a VAO, we're going to add it to the VAO list so that we can delete them later.
        GL30.glBindVertexArray(vaoID); //activate this VAO. Do it via binding it.
        return vaoID;
    }
     
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){ //we need to store data into one of the attribute lists of the VAO.
        int vboID = GL15.glGenBuffers(); //create an empty VBO by calling glGenBuffers. And it will return the ID of the VBO that we've created.
        vbos.add(vboID); //see: vaos.add()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //If you have to do something with VBO, you have to bind it first.
        FloatBuffer buffer = storeDataInFloatBuffer(data); 
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //buffer for data, and then specify what the data is going to be used for. Whether it is static or editable once we store it?
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0); //We are going to go ahead and put that VBO into the VAO into one of the attribute lists.
        //Attribute list for which you want to store the data and that's just gonna be attribute number, length of each vertex which is three bcs they're 3D vectors
        //and it needs to know the type of data --> float, is your data normalized?, distance between each of your vertices (have you got any other data btw them?),
        //the offset should it start the beginng of the data --> yes
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //since we've finished using the VBO, we can unbind it. 
    }
     
    private void unbindVAO(){ //when you finished using VAO, you have to unbind it.
    	/*
    	 * Vertex array object will stay bound until we unbind it.
    	 */
        GL30.glBindVertexArray(0); //it will unbind the currently binded VAO.
    }
     
    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers(); 
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
     
    private IntBuffer storeDataInIntBuffer(int[] data){
    	/*
    	 * Now we need to store our indices into this VBO.
    	 */
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip(); //flip the buffer, so that's ready to be read from.
        return buffer;
    }
     
    private FloatBuffer storeDataInFloatBuffer(float[] data){ //Data has to be stored into a VBO as a float buffer. 
    	/*
    	 * This method is going to convert our float array of data into a float buffer.
    	 */
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); //create an empty float buffer. size of the data --> data.length
        buffer.put(data);
        buffer.flip(); //we need to prepare the buffer to be read from because at the moment, it's expecting to be written to.
        return buffer; //then we return the buffer so that we can use it to store into the VBO.
    }
     
}