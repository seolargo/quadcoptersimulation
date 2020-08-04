package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import DroneMaths.Drag;
import DroneMaths.AirDensity;
import DroneMaths.AngularAcceleration;
import DroneMaths.Momentum;
import DroneMaths.Thrust;
import DroneMaths.Torque;
import DroneMaths.Weight;
import DroneMaths.omega2thetadot;
import DroneMaths.thetadot2omega;
import DroneMaths.Acceleration;
import models.TexturedModel;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 10; //Assume the quadcopter runs with 10 cm/s. 
	@SuppressWarnings("unused")
	private static final float TURN_SPEED = 100;
	@SuppressWarnings("unused")
	private static final double GRAVITY = -9.81;
	@SuppressWarnings("unused")
	private static final float TERRAIN_HEIGHT = 0;

	@SuppressWarnings("unused")
	private float currentSpeed = 0;
	
	private double getRotX = 0;
	private double getRotY = 45;
	private double getRotZ = -40;
	
	private boolean lastPushedW = false, lastPushedS = false, lastPushedD = false, lastPushedA = false;
	private boolean lastPushedP = false, lastPushedR = false, lastPushedV = false, lastPushedH = false;
	private boolean lastPushedY = false;
	
	private double getRotYy = 0;
	
	private double[] omega = new double[3];
	private double[] angularVelocity = new double[3];
	private double[] linearVelocity = new double[3];
	
	/*					   .
	 * Linear velocity --> x
	 * 
	 * linearVelocity[0] --> quadcopter's x-axis linear movement
	 * linearVelocity[1] --> quadcopter's y-axis linear movement
	 * linearVelocity[2] --> quadcopter's z-axis linear movement
	 */
	
	/*			  1		  2	
	 * 			   \	  /
	 * 				\	 /
	 * 				 \	/
	 * 				  \/
	 * 				  /\
	 * 				 /	\
	 * 				/	 \
	 *             /	  \
	 *			  4        3
	 */
	
	private double[] a = new double[3];
	
	private double w1 = 600;
	private double w2 = 600;
	private double w3 = 600;
	private double w4 = 600;

	//Initialization for the quadcopter's current array and the target array.
	private double[][] actualArray;
	private static double[][] targetArray;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		actualArray = new double[3][3];
		targetArray = new double[3][3];
	}
	
	Entity entity = new Entity(getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
	Weight Weight = new Weight();
	AirDensity AirDensity = new AirDensity(getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
	
	public void move() {	
		checkInputs();
		
		/* Initial values of the rotation angles of the quadcopter:
		 * GetRotX: 0 
		 * GetRotY: 45
		 * GetRotZ: -40
		 * 
		 * Warning: RotY doesn't represent getRotY :)
		 * 
		 * If pressed W then only GetRotY is changed to 90, thus pitch angle changes.
		 */
		
		/* @see Your right hand :)
		 * Z direction goes towards to your screen. Do not confuse!
		 * 
		 * 				Y	   Z
		 * 				|     /
		 * 				|    /
		 * 				|   /
		 * 				|  /	*
		 * 				| /		
		 * 				|/__________ X						
		 *
		 */
		
		//Printing the rotations
		System.out.println("getRotX() Normal değer = 0: " + getRotX());
		System.out.println("getRotY() Normal değer = 45: " + getRotY());
		System.out.println("getRotZ() Normal değer = -40: " + getRotZ());
		
		//actualArray stands for the current position of the quadcopter.
		actualArray[0][0] = getPositionX();
		actualArray[1][0] = getPositionY();
		actualArray[2][0] = getPositionZ();
		
		actualArray[0][1] = Math.sqrt(getPositionX() * getPositionX() + getPositionY() * getPositionY());
		actualArray[0][2] = Math.sqrt(getPositionX() * getPositionX() + getPositionZ() * getPositionZ());
		actualArray[1][1] = Math.sqrt(getPositionY() * getPositionY() + getPositionZ() * getPositionZ());
		
		//Prints to the console of the quadcopter's current position.
		System.out.println("\nQuadcopter's current position");
		System.out.println("X: " + getPositionX());
		System.out.println("Y: " + getPositionY());
		System.out.println("Z: " + getPositionZ());
		System.out.println("X-Y: " + actualArray[0][1]);
		System.out.println("X-Z: " + actualArray[0][2]);
		System.out.println("Y-Z: " + actualArray[1][1]);
		
		/*
		 * ********************************* THRUST ***************************************
		 */
		//w1, w2, w3, w4 stands for the angular velocities. They will be used for the thrust calculation.
		Thrust Thrust = new Thrust(w1, w2, w3, w4, getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
		double totalThrust = Thrust.totalThrust();
		System.out.println("Total Thrust (rad^2 / sec^2): " + totalThrust);
		System.out.println("T_z (rad^2 / sec^2): " + 0.44*9.81);
		System.out.println("Thrust by Motor 1 (rad^2 / sec^2): " + Thrust.Thrust1());
		System.out.println("Thrust by Motor 2 (rad^2 / sec^2): " + Thrust.Thrust2());
		System.out.println("Thrust by Motor 3 (rad^2 / sec^2): " + Thrust.Thrust3());
		System.out.println("Thrust by Motor 4 (rad^2 / sec^2): " + Thrust.Thrust4());
		
		/*
		 * ********************************* TORQUE ***************************************
		 */
		//w1, w2, w3, w4 stands for the angular velocities. They will be used for the torque calculation.
		Torque torque = new Torque(getW1(), getW2(), getW3(), getW4()); 
		System.out.println("Torque in X axis (m * rad^2 / sec^2): " + torque.getRollTorque());
		System.out.println("Torque in Y axis (m * rad^2 / sec^2): " + torque.getPitchTorque());
		System.out.println("Torque in Z axis (rad^2 / sec^2): " + torque.getYawTorque());
		
		/*
		 * ********************************* MOMENTUM ***************************************
		 */
		//w1, w2, w3, w4 stands for the angular velocities. They will be used for the momentum calculation.
		Momentum momentum = new Momentum(w1, w2, w3, w4);
		momentum.momentumbyMotor1();
		momentum.momentumbyMotor2();
		momentum.momentumbyMotor3();
		momentum.momentumbyMotor4();
		
		/*
		 * ********************************* THETADOT2OMEGA ***************************************
		 */
		thetadot2omega t2o = new thetadot2omega(getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale(), angularVelocity);
		
		omega[0] = t2o.getOmega0();
		omega[1] = t2o.getOmega1();
		omega[2] = t2o.getOmega2();
		
		System.out.println("   " + t2o.getOmega0());
		System.out.println("w = " + t2o.getOmega1());
		System.out.println("   " + t2o.getOmega2());
		
		/*
		 * ********************************* LINEAR ACCELERATION ***************************************
		 */
		//a = acceleration(i, theta, xdot, m, g, k, kd);
		/*
		 function a = acceleration(inputs, angles, vels, m, g, k, kd)
			gravity = [0; 0; -g];
			R = rotation(angles);
			T = R * thrust(inputs, k);
			Fd = -kd * vels;
			a = gravity + 1 / m * T + Fd;
		 end
		 */
		Acceleration acc = new Acceleration(getRotX(), getRotY(), getRotZ(), w1, w2, w3, w4, linearVelocity, getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
		
		a[0] = acc.getAcc0();
		a[1] = acc.getAcc1();
		a[2] = acc.getAcc2();
		
		System.out.println(". .  " + a[0]);
		System.out.println(" x = " + a[1]);
		System.out.println(" 	"  + a[2]);
		
		/*
		 * ********************************* ANGULAR ACCELERATION ***************************************
		 */
		AngularAcceleration angular_acceleration = new AngularAcceleration(getModel(), getPosition(), 
				getRotX(), getRotY(), getRotZ(), getScale(), w1, w2, w3, w4, angularVelocity);
		
		System.out.println("     .   ");
		System.out.println("    |w_x| " + angular_acceleration.getWDotX());
		System.out.println(".    .   ");
		System.out.println("w = |w_y| " + angular_acceleration.getWDotY());
		System.out.println("     .   ");
		System.out.println("    |w_z| " + angular_acceleration.getWDotZ());
   		
		/*
		 * ********************************* DRAG FORCES ************************************************
		 */
		//Drag forces in three directions.
		Drag drag = new Drag(getModel(), getPosition(), getRotX(), getRotY(), getRotZ(), getScale(), linearVelocity);
		System.out.println("Drag X: " + drag.getDragX());
		System.out.println("Drag Y: " + drag.getDragY());
		System.out.println("Drag Z: " + drag.getDragZ());
		drag.printDragEquationFromFrictionalForce();
		
		/*
		 * ********************************* WEIGHT *****************************************************
		 */
		//Get the weight of the quadcopter.
		Weight Weight = new Weight();
		double weight = Weight.getWeight();
		System.out.println("Weight (Kg): " + weight);
		
		/*
		 * ********************************* ROTATIONS ***************************************************
		 */
		System.out.println("Rotation in X axis (roll): " + getRotX);
		System.out.println("Rotation in Y axis (pitch): " + (getRotY - 45));
		System.out.println("Rotation in Z axis (yaw): " + ((getRotZ + 40)%360));
	
		//super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		//float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		//float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		//float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		//super.increasePosition(0, 0, (float)0.1);	
	}
	
	//Method for checking the user options
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			/*
			 * Pitch angle changes.
			 * RotY changes. 
			 */
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 0;
			linearVelocity[1] = 5;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = 0;
			angularVelocity[1] = 5;
			angularVelocity[2] = 0;
			
			//Setting the angular velocities
			//Min w'lar 600 olabilir.
			setW1(600);
			setW2(600);
			setW3(1152);
			setW4(1152);
			
			/*errX = Kd * (getRotX()) + Kp * integral[0];
			errY = Kd * (getRotY()-45) + Kp * integral[1];
			errZ = Kd * (getRotZ() + 40) + Kp * integral[2];
			integral[0] += 0.005*getRotX();
			integral[1] += 0.005*(getRotY()-45);
			integral[2] += 0.005*(getRotX()+40);*/
			
			/*
			 * g = 9.81;
				m = 0.5;
				L = 0.25;
				k = 3e-6;
				b = 1e-7;
				I = diag([5e-3, 5e-3, 10e-3]);
				kd = 0.25;
			 */
			
			/*Weight weight = new Weight();
			setW1(weight.getWeight()/4-(2*(1e-7)*errX*(5e-3) + errZ * (10e-3) * (3e-5) * 0.25) / (4*(1e-7)*(3e-5)*0.25));
			setW2(weight.getWeight()/4 + errZ * (5e-3)/(4*(1e-7))- (errY * (5e-3))/(2*(3e-5)*(0.25)));
			setW3(weight.getWeight()/4-(-2*(1e-7)*errX*(5e-3) + errZ * (10e-3) * (3e-5) * 0.25) / (4*(1e-7)*(3e-5)*0.25));
			setW4(weight.getWeight()/4 + errZ * (5e-3)/(4*(1e-7))+ (errY * (5e-3))/(2*(3e-5)*(0.25)));*/
					
			this.currentSpeed = RUN_SPEED;
			increasePosition((float) 0, (float)0, (float)0.1);
			/*
			 * getRotY üzerinden işlem yapacaksın!
			 * W tuşu pitch yönünü değiştirir.
			 */
			if(getRotY<90) {
				increaseRotation((float)+0.1, 0, 0);
				getRotY = getRotY + 0.1;
			}
			
			lastPushedY = false;
			lastPushedW = true;
			lastPushedS = false;
			lastPushedD = false;
			lastPushedA = false;
			lastPushedP = false;
			lastPushedH = false;
			lastPushedR = false;
			lastPushedV = false;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 0;
			linearVelocity[1] = -5;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = 0;
			angularVelocity[1] = -5;
			angularVelocity[2] = 0;
			
			//Setting the angular velocities
			setW1(1152);
			setW2(1152);
			setW3(600);
			setW4(600);
			
			this.currentSpeed = -RUN_SPEED;
			if(getRotY>0) {
				getRotY = getRotY - 0.1;
				increaseRotation((float)-0.1, 0, 0);
			}
			increasePosition((float) 0, (float)0, (float)-0.1);
			
			lastPushedY=false;
			lastPushedW=false;
			lastPushedS=true;
			lastPushedD=false;
			lastPushedA=false;
			lastPushedP=false;
			lastPushedH=false;
			lastPushedR=false;
			lastPushedV=false;
		} 
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if(getRotX >= -23.2) {
				increaseRotation((float)-0.1, (float)0, (float)0.1);
				getRotX = getRotX - 0.1;
				getRotZ = getRotZ + 0.1;
			}
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 5;
			linearVelocity[1] = 0;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = 5;
			angularVelocity[1] = 0;
			angularVelocity[2] = 0;
			
			//Setting the angular velocities
			setW1(1152);
			setW2(600);
			setW3(600);
			setW4(1152);
			
			lastPushedY=false;
			lastPushedD=true;
			lastPushedA=false;
			lastPushedS=false;
			lastPushedW=false;
			lastPushedP=false;
			lastPushedH=false;
			lastPushedR=false;
			lastPushedV=false;
			
			this.currentSpeed = RUN_SPEED;
			increasePosition((float) -0.1, 0, 0);
		} 
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if(getRotX <= 25) {
				increaseRotation((float)0.1, (float)0, (float)-0.05);
				getRotX = getRotX + 0.1;
				getRotZ = getRotZ - 0.05;
			}
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = -5;
			linearVelocity[1] = 0;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = -5;
			angularVelocity[1] = 0;
			angularVelocity[2] = 0;
			
			//Setting the angular velocities
			setW1(1152);
			setW2(600);
			setW3(600);
			setW4(1152);
			
			lastPushedY=false;
			lastPushedA=true;
			lastPushedS=false;
			lastPushedW=false;
			lastPushedD=false;
			lastPushedP=false;
			lastPushedH=false;
			lastPushedR=false;
			lastPushedV=false;
			
			this.currentSpeed = -RUN_SPEED;
			increasePosition((float)0.1, 0, (float)0);
		} 
		//Angular velocities are increased.
		else if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			increasePosition((float)0, (float) 0.1, (float)0);
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 0;
			linearVelocity[1] = 0;
			linearVelocity[2] = 5;
			
			angularVelocity[0] = 0;
			angularVelocity[1] = 0;
			angularVelocity[2] = 5;
			
			//Setting the angular velocities
			setW1(800);
			setW2(800);
			setW3(800);
			setW4(800);
			
			lastPushedY=false;
			lastPushedD=false;
			lastPushedA=false;
			lastPushedS=false;
			lastPushedW=false;
			lastPushedP=false;
			lastPushedH=false;
			lastPushedR=false;
			lastPushedV=false;
		}
		//Angular velocities are decreased.
		else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			increasePosition((float)0,(float) -0.1, (float)0);
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 0;
			linearVelocity[1] = 0;
			linearVelocity[2] = -5;
			
			angularVelocity[0] = 0;
			angularVelocity[1] = 0;
			angularVelocity[2] = -5;
			
			//Setting the angular velocities
			setW1(600);
			setW2(600);
			setW3(600);
			setW4(600);
			
			lastPushedY=false;
			lastPushedD=false;
			lastPushedA=false;
			lastPushedS=false;
			lastPushedW=false;
			lastPushedP=false;
			lastPushedH=false;
			lastPushedR=false;
			lastPushedV=false;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			//If the user pressed the button "Y" then the quadcopter turns around the Z axis.
			increaseRotation(0, (float) 0.1, 0);
			getRotYy = getRotYy + 0.1;
			System.err.println("Rotation in Z axis(yaw): " + getRotYy);
			
			//Setting the angular velocities
			setW1(800);
			setW2(600);
			setW3(800);
			setW4(600);	
			
			lastPushedD=false;
			lastPushedA=false;
			lastPushedS=false;
			lastPushedW=false;
			lastPushedP=false;
			lastPushedH=false;
			lastPushedR=false;
			lastPushedV=false;
			lastPushedY=true;
		}
		
		/* Rotates a little bit :)
		 * else if(Keyboard.isKeyDown(Keyboard.KEY_T)) {
			if(getRotY>0) {
				increaseRotation(0, (float) -0.1, 0);
				getRotY = getRotY - 0.1;
			}
			lastPushedT = true;
		}*/
		
		//Pitch angle changes 
		else if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
			if(getRotZ<-18) {
				increaseRotation((float)0,0,(float)0.05);
				getRotZ = getRotZ + 0.05;
			}
			increasePosition((float)-0.1, (float)0, (float)0.1);
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 5;
			linearVelocity[1] = 5;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = 5;
			angularVelocity[1] = 5;
			angularVelocity[2] = 0;
			
			lastPushedP = true;
			lastPushedH = false;
			lastPushedV = false;
			lastPushedR = false;
			lastPushedW = false;
			lastPushedS = false;
			lastPushedA = false;
			lastPushedD = false;
			lastPushedY=false;
			
			setW1(600);
			setW2(1152);
			setW3(600);
			setW4(700);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_H)) {
			if(getRotZ > -58) {
				increaseRotation((float)0,0,(float)-0.05);
				getRotZ = getRotZ - 0.05;
			}
			increasePosition((float)0.1,(float)0,(float)-0.1);
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = -5;
			linearVelocity[1] = -5;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = -5;
			angularVelocity[1] = -5;
			angularVelocity[2] = 0;
			
			lastPushedH = true;
			lastPushedV = false;
			lastPushedR = false;
			lastPushedP = false;
			lastPushedA = false;
			lastPushedS = false;
			lastPushedD = false;
			lastPushedW = false;
			lastPushedY=false;
			
			setW1(600);
			setW2(700);
			setW3(600);
			setW4(1152);
		}
		
		//Roll angle changes
		else if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			if(getRotX < 40) {
				increaseRotation((float)0.1,(float)0,(float)0.0);
				getRotX = getRotX + 0.1;
			}
			increasePosition((float)0.1,(float)0,(float)0.1);
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = -5;
			linearVelocity[1] = 5;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = -5;
			angularVelocity[1] = 5;
			angularVelocity[2] = 0;
			
			lastPushedR = true;
			lastPushedH = false;
			lastPushedV = false;
			lastPushedP = false;
			lastPushedW = false;
			lastPushedA = false;
			lastPushedS = false;
			lastPushedD = false;
			lastPushedY=false;
			
			setW1(1152);
			setW2(600);
			setW3(700);
			setW4(600);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_V)) {
			if(getRotX > -40) {
				increaseRotation((float)-0.1,(float)0,(float)0.05);
				getRotX = getRotX - 0.1;
				getRotZ = getRotZ + 0.05;
			}
			increasePosition((float)-0.1,(float)0,(float)-0.1);
			
			//deltaX = 5cm ve deltaTime = 1sn olsun
			linearVelocity[0] = 5;
			linearVelocity[1] = -5;
			linearVelocity[2] = 0;
			
			angularVelocity[0] = 5;
			angularVelocity[1] = -5;
			angularVelocity[2] = 0;
			
			lastPushedV = true;
			lastPushedH = false;
			lastPushedR = false;
			lastPushedP = false;
			lastPushedW = false;
			lastPushedA = false;
			lastPushedS = false;
			lastPushedD = false;
			lastPushedY=false;
			
			setW1(700);
			setW2(600);
			setW3(1152);
			setW4(600);
		}
		else {
			setW1(600);
			setW2(600);
			setW3(600);
			setW4(600);
		
			//setW1(thrust.Thrust1()-(2*(1e-7)*errX*(5e-3) + errZ * (10e-3) * (3e-5) * 0.25) / (4*(1e-7)*(3e-5)*0.25));
			//setW2(thrust.Thrust2() + errZ * (5e-3)/(4*(1e-7))- (errY * (5e-3))/(2*(3e-5)*(0.25)));
			//setW3(thrust.Thrust3()-(-2*(1e-7)*errX*(5e-3) + errZ * (10e-3) * (3e-5) * 0.25) / (4*(1e-7)*(3e-5)*0.25));
			//setW4(thrust.Thrust4() + errZ * (5e-3)/(4*(1e-7))+ (errY * (5e-3))/(2*(3e-5)*(0.25)));
		
			if(lastPushedW == true) {
				if(getRotY > 45) {
					increaseRotation((float)-0.1, 0, 0);
					getRotY = getRotY - 0.1;
					increasePosition((float) 0, (float)0, (float)0.1);
				}
			}
			else if(lastPushedS == true) {
				if(getRotY < 45) {
					getRotY = getRotY + 0.1;
					increaseRotation((float) 0.1, 0, 0);
					increasePosition((float) 0, (float)0, (float)-0.1);
				}
			}
			else if(lastPushedD == true) {
				if(getRotX < 0) {
					increaseRotation((float)0.1, (float)0, (float)-0.1);
					getRotX = getRotX + 0.1;
					getRotZ = getRotZ - 0.1;
					increasePosition((float) -0.1, 0, 0);
				}
			}
			else if(lastPushedA == true) {
				if(getRotX > 0) {
					increaseRotation((float)-0.1, (float)0, (float)+0.05);
					getRotX = getRotX - 0.1;
					getRotZ = getRotZ + 0.05;
					increasePosition((float)0.1, 0, (float)0);
				}
			}
			else if(lastPushedP == true) {
				if(getRotZ > -40) {
					getRotZ = getRotZ - 0.05;
					increaseRotation((float)0, 0, (float)-0.05);
					increasePosition((float)-0.1, (float)0, (float)0.1);
				}
				if(getRotZ == -40 && getRotX > 0) {
					increaseRotation((float)-0.05, 0, (float)0.0);
					getRotX = getRotX - 0.05;
				}
			}
			else if(lastPushedR == true) {
				if(getRotX > 0) {
					increaseRotation((float)-0.1,(float)0,(float)0.0);
					getRotX = getRotX - 0.1;
					increasePosition((float)0.1,(float)0,(float)0.1);
				}
			}
			else if(lastPushedH == true) {
				if(getRotZ < -45 ) {
					increaseRotation((float)0, 0, (float)0.05);
					getRotZ = getRotZ + 0.05;
					increasePosition((float)0.1, (float)0, (float)-0.1);
				}
			}
			else if(lastPushedV == true) {
				if(getRotX < 0) {
					increaseRotation((float)0.1,(float)0,(float)-0.05);
					getRotX = getRotX + 0.1;
					getRotZ = getRotZ - 0.05;
					increasePosition((float)-0.1,(float)0,(float)-0.1);
				}
			}
		}
	}	
	
	//Returning actual positions of the quadcopter.
	public double getActualPositionX() {
		return actualArray[0][0];
	}
	public double getActualPositionY() {
		return actualArray[1][0];
	}
	public double getActualPositionZ() {
		return actualArray[2][0];
	}
	
	//Returning target positions of the quacopter.
	public double getTargetPositionX() {
		return targetArray[0][0];
	}
	public double getTargetPositionY() {
		return targetArray[1][0];
	}
	public double getTargetPositionZ() {
		return targetArray[2][0];
	}
	
	//Setting target positions of the quadcopter.
	public void setTargetPositionX(double x) {
		Player.targetArray[0][0] = x;
	}
	public void setTargetPositionY(double y) {
		Player.targetArray[1][0] = y;
	}
	public void setTargetPositionZ(double z) {
		Player.targetArray[2][0] = z;
	}
	
	private void hovering() {
		//Turkish: Havada asili kalmak.
		increasePosition(0, 0, 0);
	}
	private void altitude(double x) {
		//Turkish: Yükselmek.
		increasePosition(0, (float) x, 0);
	}
	private void land(double x) {
		//Turkish: Alçalmak.
		if(actualArray[1][0] > 0) {
			increasePosition(0, (float) -x, 0);
		}
	}
	
	/* 
	 * See: System Identification of a Quad-rotor in X Configuration from Experimental Data
	 * 				Z (yaw)
	 * 				|
	 * 				|
	 * 				|
	 * 				|
	 * 				|____________ Y (pitch)
	 * 			   /	
	 *			  /
	 *			 /
	 *			/
	 *		   X (roll)
	 */
	
	//Getting linear velocities
	public double getLinearVelocityX() {
		return linearVelocity[0];
	}
	public double getLinearVelocityY() {
		return linearVelocity[1];
	}
	public double getLinearVelocityZ() {
		return linearVelocity[2];
	}
	
	//Get methods
	public double getW1() {
		return w1;
	}
	public double getW2() {
		return w2;
	}
	public double getW3() {
		return w3;
	}
	public double getW4() {
		return w4;
	}
	
	//Set methods
	public void setW1(double w1) {
		this.w1 = w1;
	}
	public void setW2(double w2) {
		this.w2 = w2;
	}
	public void setW3(double w3) {
		this.w3 = w3;
	}
	public void setW4(double w4) {
		this.w4 = w4;
	}
	
	//Get methods
	public double getWx() {
		return angularVelocity[0];
	}
	public double getWy() {
		return angularVelocity[1];
	}
	public double getWz() {
		return angularVelocity[2];
	}
	
	public double getRotXNumerical() {
		return getRotX;
	}
	public double getRotYNumerical() {
		return getRotY;
	}
	public double getRotZNumerical() {
		return getRotZ;
	}
}