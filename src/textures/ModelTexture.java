package textures;
 
public class ModelTexture {
     
    private int textureID;
     
    private float shineDamper = 1;
    private float reflectivity = 0;
    
    private boolean hasTransparency = false;
    	/*
    	 * To determine whether the model texture has transparency.
    	 */
    
    private boolean useFakeLighting = false;
    
    public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public ModelTexture(int texture){
        this.textureID = texture;
    }
     
    public int getID(){
        return textureID;
    }
 
}