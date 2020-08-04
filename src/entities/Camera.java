package entities;
 
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
 
public class Camera {
    
	//Camera's distance from player.
	private float distanceFromPlayer = 50; 
	//Camera's angle around player.
	private float angleAroundPlayer = 0; 
	//Camera's position initialization.
    private Vector3f position = new Vector3f(100, 50, 50); 
    //Defines how the camera is away from the man.
    private float pitch = 10;
    private float yaw = 0;
    private float roll;
     
    private Player player;
    
    public Camera(Player player){
    	this.player = player;
    }
     
    public void move(){
    	
        /*if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x+=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z+=0.2f;
        }*/
    	
    	float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
       	
    	calculateZoom();	
        calculatePitch();
        calculateAngleAroundPlayer();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
        
        }
 
    public Vector3f getPosition() {
        return position;
    }
 
    public float getPitch() {
        return pitch;
    }
 
    public float getYaw() {
        return yaw;
    }
 
    public float getRoll() {
        return roll;
    }
    
    private void calculateCameraPosition(float horizDistance, float verticDistance) {
    	float theta = player.getRotY() + angleAroundPlayer;
    	float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
    	float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
    	position.x = player.getPosition().x - offsetX;
    	position.z = player.getPosition().z - offsetZ;
    	position.y = player.getPosition().y + verticDistance; 
    }
    
    private float calculateHorizontalDistance() {
    	return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }
    

    private float calculateVerticalDistance() {
    	return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }
     
    private void calculateZoom() {
    	float zoomLevel = Mouse.getDWheel() * 0.1f;
    	distanceFromPlayer -= zoomLevel;
    }
    
    private void calculatePitch() {
    	if(Mouse.isButtonDown(1)) { //0 for left mouse button
    		float pitchChange = Mouse.getDY() * 0.1f;
    		pitch -= pitchChange;
    	}
    }
    
    private void calculateAngleAroundPlayer() {
    	if(Mouse.isButtonDown(0)) {
    		float angleChange = Mouse.getDX() * 0.3f;
    		angleAroundPlayer -= angleChange;
    	}
    }
     
 
}