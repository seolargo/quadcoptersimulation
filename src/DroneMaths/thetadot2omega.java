package DroneMaths;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.TexturedModel;

public class thetadot2omega extends Entity{
	
	/*
	 * Convert derivatives of roll, pitch and yaw angles to omega.
	 * 
	 * Thetadot is a random number
	 * 																							   T     .    .  .  . T
	 * We define the position and velocity of the quadcopter in the inertial frame as x = (x, y, z)  and x = (x, y, z)
	 * 																						  T                         .       .     .     .  T
	 * We define the roll, pitch and yaw angles in the BODY frame as theta = (phi, theta, psi) and angular velocities theta = (phi, theta, psi)	 * 
	 * 
	 * NOTE: w != theta
	 * 
	 * Angular velocity vector x is a vector pointing along the axis of rotation, while thetadot is just time derivative of YAW, PITCH and ROLL angle.
	 * In order to convert these angular velocities into the angular velocity vector, we can use the relation:
	 *         __                                __
	 * 		  |1 		0 			-sin(theta)    | 
	 *omega = |0 	cos(phi) 	cos(theta)sin(phi) |thetadot;
	 *        |0 	-sin(phi) 	cos(theta)cos(phi) |
	 *        |__                                __|
	 *        
	 * where w is the angular velocity vector in the body frame.
	 */
	
	private double W[][];
	private double[] omega;
	//Note: 1 degree = 0.0174533 rad.
	
	//Constructor
	public thetadot2omega(TexturedModel model, Vector3f position, float rotX, 
			float rotY, float rotZ, float scale, double thetadot[]){
		super(model, position, rotX, rotY, rotZ, scale);
		
		omega = new double[3];
		W =  new double[3][3];
		double phi = getRotX();
		double theta = getRotY();
		
		//W
		W[0][0] = 1;
		W[0][1] = 0;
		W[0][2] = -Math.sin(theta);
		
		W[1][0] = 0;
		W[1][1] = Math.cos(phi);
		W[1][2] = Math.cos(theta) * Math.sin(phi);
		
		W[2][0] = 0;
		W[2][1] = -Math.sin(phi);
		W[2][2] = Math.cos(theta) * Math.cos(phi);
		
		//omega = W * thetadot;
		MatrixMultiplication MM = new MatrixMultiplication(W, thetadot, false);
		omega[0] = MM.getVector0();
		omega[1] = MM.getVector1();
		omega[2] = MM.getVector2();
	}
	
	//Getters.
	public double getOmega0() {
		return omega[0];
	}
	public double getOmega1() {
		return omega[1];
	}
	public double getOmega2() {
		return omega[2];
	}
}