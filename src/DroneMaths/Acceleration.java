package DroneMaths;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.TexturedModel;

public class Acceleration extends Entity{

	private double acc[] = new double[3];
	private double g = 9.81;
	private double gravity[] = new double[3];
	private double velocities[] = new double[3];
	private double m = 0.44;
	private double kd = 0.25;
	private double totalThrust[] = new double[3];
	private double totalThrustArray[] = new double[3];
	private double R[][] = new double[3][3];	
	private double Fd[] = new double[3];
	private double w1, w2, w3, w4; 
	
	private double phi, theta, psi;
	
	Weight weight = new Weight();
	
	public Acceleration(double phi, double theta, double psi, double w1, double w2, 
			double w3, double w4, double[] xdot, TexturedModel model, Vector3f position, float rotX, 
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.phi = phi;
		this.theta = theta;
		this.psi = psi;
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.w4 = w4;
		
		gravity[0] = 0;
		gravity[1] = 0;
		gravity[2] = -g;
		
		/*	n
		 * R
		 *  b
		 */
		
		R[0][0] = Math.cos(phi) * Math.cos(theta);
		R[0][1] = Math.cos(psi) * Math.sin(theta) * Math.sin(phi) - Math.cos(psi) * Math.sin(psi);
		R[0][2] = Math.sin(phi) * Math.sin(psi) + Math.cos(phi) * Math.cos(psi) * Math.sin(theta);
		
		R[1][0] = Math.cos(theta) * Math.sin(psi);
		R[1][1] = Math.cos(phi) * Math.cos(psi) + Math.sin(phi) * Math.sin(theta) * Math.sin(psi);
		R[1][2] = Math.cos(psi) * Math.sin(phi) * Math.sin(theta) - Math.cos(psi) * Math.sin(phi);
		
		R[2][0] = -Math.sin(theta);
		R[2][1] = Math.cos(theta) * Math.sin(phi);
		R[2][2] = Math.cos(theta) * Math.cos(phi);
		
		Thrust thrust = new Thrust(w1, w2, w3, w4, getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
		totalThrust[0] = 0;
		totalThrust[1] = 0;
		totalThrust[2] = thrust.getTotalThrust();
		
		MatrixMultiplication MM = new MatrixMultiplication(R, totalThrust, false);
		
		Fd[0] = -kd * xdot[0];
		Fd[1] = -kd * xdot[1];
		Fd[2] = -kd * xdot[2];
		
		totalThrustArray[0] = MM.getVector0();
		totalThrustArray[1] = MM.getVector1();
		totalThrustArray[2] = MM.getVector2();
				
		acc[0] = 1 / weight.getWeight() * totalThrustArray[0] + 1 / weight.getWeight() * Fd[0];
		acc[1] = 1 / weight.getWeight() * totalThrustArray[1] + 1 / weight.getWeight() * Fd[1];
		acc[2] = gravity[2] + 1 / weight.getWeight() * totalThrustArray[2] +  1 / weight.getWeight() * Fd[2];
	}
	
	public double getAcc0() {
		return acc[0];
	}
	public double getAcc1() {
		return acc[1];
	}
	public double getAcc2() {
		return acc[2];
	}
}