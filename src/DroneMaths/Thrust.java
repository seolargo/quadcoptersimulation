package DroneMaths;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Player;
import models.TexturedModel;

public class Thrust extends Entity{
	
	/*
	 * RPS (revolutions per second)
	 * Angular Speed (radian/s) --> w = rps*2*PI
	 * Assume RPS is 134.7053
	 *
	 * 		rad
	 * w =  ---
	 * 		sec
	 */
	
	private double k = 3e-6; //Thrust coefficient
	private double w1, w2, w3, w4;
	private double totalThrust;
	
	//Constructor
	public Thrust(double w1, double w2, double w3, double w4, TexturedModel model, Vector3f position, float rotX, 
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.w4 = w4;
	}
	
	Player player = new Player(getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
	
	/*
	 * All thrust's units are rad^2 / sec^2. 
	 */
	
	//Getter methods
	public double Thrust1(){
		double thrust1 = k*w1*w1;
		return thrust1;
	}
	public double Thrust2(){
		double thrust2 = k*w2*w2;
		return thrust2;
	}
	public double Thrust3(){
		double thrust3 = k*w3*w3;
		return thrust3;
	}
	public double Thrust4(){
		double thrust4 = k*w4*w4;
		return thrust4;
	}
	public double totalThrust() {
		double thrust1 = Thrust1();
		double thrust2 = Thrust2();
		double thrust3 = Thrust3();
		double thrust4 = Thrust4();
		totalThrust = thrust1 + thrust2 + thrust3 + thrust4;
		return totalThrust;
	}
	public double getW1(){
		return w1;
	}
	public double getW2(){
		return w2;
	}
	public double getW3(){
		return w3;
	}
	public double getW4(){
		return w4;
	}
	public void setOmega(double w1, double w2, double w3, double w4) {
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.w4 = w4;
	}
	public double getTotalThrust(){
		return totalThrust;
	}	
}