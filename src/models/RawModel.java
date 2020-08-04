package models;
 
public class RawModel {
	/*
	 * This class is going to represent a 3D models stored in memory.
	 */
     
    private int vaoID; //once we need to know the ID of the VAO.
    private int vertexCount; //and we need to know how many vertices are in that 3D model.
     
    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }
 
    public int getVaoID() {
        return vaoID;
    }
 
    public int getVertexCount() {
        return vertexCount;
    }
     
     
 
}