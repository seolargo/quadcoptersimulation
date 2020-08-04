package DroneMaths;

public class Momentum {
	
	private double moment[] = new double[4];
	private double thrust1 , thrust2, thrust3, thrust4;
	private double k = 3e-6; //Thrust coefficient
	
	//constructor
	public Momentum(double w1, double w2, double w3, double w4) {
		this.thrust1 = k*(w1*w1);
		this.thrust2 = k*(w2*w2);
		this.thrust3 = k*(w3*w3);
		this.thrust4 = k*(w4*w4);
		
		//L = 0.225
		moment[0] = 0.225 * thrust1;
		moment[1] = 0.225 * thrust2;
		moment[2] = 0.225 * thrust3;
		moment[3] = 0.225 * thrust4;
	}
	
	//@return If all momentums are equal then this function returns true, else false.
	public boolean isEqual() {		
		/*
		 *   1\    /2
		 *     \  /
		 *      \/
		 *      /\
		 *     /  \
		 *   4/    \3
		 */
		if((moment[1] == moment[3]) && (moment[2] == moment[4]) && (moment[1] == moment[2])){
			return true;
		} else {
			return false;
		}	
	}
	
	public void momentumbyMotor1() {
		System.out.println("Momentum by Motor 1 (rad^2 / sec^2): " + moment[0]);
	}
	public void momentumbyMotor2() {
		System.out.println("Momentum by Motor 2 (rad^2 / sec^2): " + moment[1]);
	}
	public void momentumbyMotor3() {
		System.out.println("Momentum by Motor 3 (rad^2 / sec^2): " + moment[2]);
	}
	public void momentumbyMotor4() {
		System.out.println("Momentum by Motor 4 (rad^2 / sec^2): " + moment[3]);
	}
}