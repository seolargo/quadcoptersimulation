package DroneMaths;

import org.lwjgl.util.vector.Vector3f;

import DroneMaths.MatrixMultiplication;
import entities.Entity;
import entities.Player;
import models.TexturedModel;

public class AngularAcceleration extends Entity{
	/*	    __
	 *     |. |
	 *     |Wx|
	 * .   |. |   
	 * W = |Wy| = 
	 * 	   |. |
	 * 	   |Wz|
	 * 	   |__|
	 * 
	 * W is the angular velocity vector
	 */
	
	private double tauRoll=0, tauPitch=0, tauYaw=0;
	private double omegaDotArray[] = new double[3];
	
	//constructor
	public AngularAcceleration(TexturedModel model, Vector3f position, float rotX, float rotY, 
			float rotZ, float scale, double w1, double w2, double w3, double w4, double[] thetadot) {
		super(model, position, rotX, rotY, rotZ, scale);
		
		Player player = new Player(model, position, rotX, rotY, rotZ, scale);
		
		Torque torque = new Torque(w1, w2, w3, w4);
		tauRoll = torque.getRollTorque();
		tauPitch = torque.getPitchTorque();
		tauYaw = torque.getYawTorque();
		
		/*	-1			-1
		 * I_xx = (5e-3)   = 200
		 *
		 * 	-1			-1
		 * I_yy = (5e-3)   = 200
		 *
		 *	-1 			-1
		 * I_zz = (10e-3)  = 100
		 */
	
		omegaDotArray[0] = tauRoll*200  - (((0.005)-(0.01))/(0.005)*player.getWy()*player.getWz());
		omegaDotArray[1] = tauPitch*200 - (((0.01)-(0.005))/(0.005)*player.getWx()*player.getWz());
		omegaDotArray[2] = tauYaw*200   - (((0.005)-(0.005))/(0.01)*player.getWy()*player.getWx());
	}
	
	//Get methods
	public double getWDotX() {
		return omegaDotArray[0];
	}
	public double getWDotY() {
		return omegaDotArray[1];
	}
	public double getWDotZ() {
		return omegaDotArray[2];
	}
}