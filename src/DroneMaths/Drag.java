package DroneMaths;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Player;
import models.TexturedModel;

public class Drag extends Entity{
	
	private double vector[] = new double[3];
	
	//Constructor
	public Drag(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, double array[]) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.vector = array;
	}
	
	/*
	 * The drag equation from fluid dynamics.
	 * Assume dt = 0.005;
	 */
	//velocities. Assume the quadcopter goes with the equal velocities.
	private double V = 5;
	private double Cd = 1.3;
	private static double pi = 3.14;
	private double bladeRadius = 0.15;
	private double A = pi * (bladeRadius * bladeRadius);
	private double drag;
	
	//Initialization
	AirDensity AirDensity = new AirDensity(getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
	
	//Getter methods.
	public double getDragX() {
		return 0.25*vector[0];
	}
	public double getDragY() {
		return 0.25*vector[1];
	}
	public double getDragZ() {
		return 0.25*vector[2];
	}
	
	public void printDragEquationFromFrictionalForce() {
		System.out.println("Air density: " + AirDensity.getAirDensity());
		drag = AirDensity.getAirDensity() * (V * V) * Cd * A;
		System.out.println("Drag force from fluid dynamics: " + drag/2*-1);
	}
}