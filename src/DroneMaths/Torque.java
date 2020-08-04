package DroneMaths;

public class Torque {
	//Constants
	//L is the distance from the center of the quadcopter to any of the propellers.
	private double L = 0.25; //changable
	private double b = 1e-7; //constant
	private double k = 3e-6; //constant
	//Defining angular velocities
	private double w1, w2, w3, w4;
	//Defining tau array.
	private double tau[] = new double[3];
	
	//Constructor
	public Torque(double w1, double w2, double w3, double w4) {
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.w4 = w4;
		calculateTorque();
	}
	
	private void calculateTorque(){
		/* Each rotor contributes some torque about the body z axis.
		 * Required to keep the propeller spinning and providing thrust.
		 * It creates the instantaneous angular acceleration and overcomes the frictional drag forces.
		 * The total torque about the z axis is given by the sum of all the torques from each propeller.
		 */
		//Roll torque
		tau[0] =  L * k * (w1*w1 - w3*w3);
		//Pitch torque
		tau[1] =  L * k * (w2*w2 - w4*w4);
		//The total torque about the z axis is given by the sum of all the torques from each propeller.
		tau[2] =  b * (w1*w1 - w2*w2 + w3*w3 - w4*w4);
	}
	
	//Getter methods.
	public double getRollTorque() {
		return L * k * (w1*w1 - w3*w3);
	}
	public double getPitchTorque() {
		return L * k * (w2*w2 - w4*w4);
	}
	public double getYawTorque() {
		return b * (w1*w1 - w2*w2 + w3*w3 - w4*w4);
	}	
}