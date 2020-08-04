package DroneMaths;

public class Weight {

	//the weights of the pieces of the quadcopter.
	static double ESC = 9; //gram
	static double battery = 100; //gram
	static double bodyFrame = 32; //gram
	static double BrushlessMotor = 52; //gram
	static double Propeller = 1.6; //gram
	static double Microcontroller = 36; //gram
	static double MPU6050 = 5; //gram
	static double HMC5883L = 5; //gram
	static double wirelessCommunication = 10; //gram
	static double HCSR04 = 10;
	static double weight = bodyFrame + (BrushlessMotor*4) + Propeller + Microcontroller + 
					4 * ESC + battery + MPU6050 + HMC5883L + wirelessCommunication + HCSR04;
	
	private double[][] weightArray;
	
	public Weight() {
		weightArray = new double[3][1];
		
		weightArray[0][0] = 0;
		weightArray[1][0] = 0;
		weightArray[2][0] = weight/1000;
		
		/*
		 * In order to choose a motor you need to determine how much weight you are planning to take, and then work out the thrust required to lift the quadcopter.
		 * Also, when purchasing motors you need to consider Watt’s and Efficiency as well apart from Motor KV and Thrust.
		 * The higher the Efficiency, the better it is.
		 * The manufacturer/seller of Motors will provide the specifications about the Motor. 
		 * This will help you to find out the thrust, power, rpm etc of the motor.
		 */
	}
	
	public double getWeight() {
		return weightArray[2][0];
	}
}