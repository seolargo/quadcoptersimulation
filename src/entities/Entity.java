package entities;
 
import models.TexturedModel;
 
import org.lwjgl.util.vector.Vector3f;

import DroneMaths.AirDensity;
import DroneMaths.Thrust;
 
public class Entity {
 
    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
            float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    
    }
    
    /*
	 * phi, theta and psi are for roll, pitch and yaw angles. (BAK--> rotX, rotY, rotZ?)
	 * We have initialized them to 0.
	 * phi, theta and yaw represents r, p and y angles in the BODY frame.     .		    .     .     .  T
	 * angular velocities of roll, pitch and yaw angles are represented as thetadot = (phi, theta, yaw) 
	 * To find the derivatives of these angles, see omega2thetadot.java 
	 */
 
    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }
 
    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }
 
    public TexturedModel getModel() {
        return model;
    }
 
    public void setModel(TexturedModel model) {
        this.model = model;
    }
 
    public Vector3f getPosition() {
        return position;
    }
 
    //Returns positions in X, Y and Z axes.
    public float getPositionX() {
        return position.x;
    }
    
    public float getPositionY() {
        return position.y;
    }
    
    public float getPositionZ() {
        return position.z;
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
    }
 
    public float getRotX() {
        return rotX;
    }
 
    public void setRotX(float rotX) {
        this.rotX = rotX;
    }
 
    public float getRotY() {
        return rotY;
    }
 
    public void setRotY(float rotY) {
        this.rotY = rotY;
    }
 
    public float getRotZ() {
        return rotZ;
    }
 
    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }
 
    public float getScale() {
        return scale;
    }
 
    public void setScale(float scale) {
        this.scale = scale;
    }
    
}