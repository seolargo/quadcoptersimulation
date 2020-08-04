package textures;

public class TerrainTexturePack {

	private TerrainTexture backgroundTexture; //4 textures you would render on a certain terrain.
	private TerrainTexture rTexture; //4 textures you would render on a certain terrain.
	private TerrainTexture gTexture; //4 textures you would render on a certain terrain.
	private TerrainTexture bTexture; //4 textures you would render on a certain terrain.
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	public void setBackgroundTexture(TerrainTexture backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}
	public TerrainTexture getrTexture() {
		return rTexture;
	}
	public void setrTexture(TerrainTexture rTexture) {
		this.rTexture = rTexture;
	}
	public TerrainTexture getgTexture() {
		return gTexture;
	}
	public void setgTexture(TerrainTexture gTexture) {
		this.gTexture = gTexture;
	}
	public TerrainTexture getbTexture() {
		return bTexture;
	}
	public void setbTexture(TerrainTexture bTexture) {
		this.bTexture = bTexture;
	}
	
	
	
	
	
}
