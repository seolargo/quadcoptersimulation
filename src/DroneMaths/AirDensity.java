package DroneMaths;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.TexturedModel;

public class AirDensity extends Entity{
		
		public AirDensity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
			super(model, position, rotX, rotY, rotZ, scale);
		}
		
		/*
		 * p = PMo / R*Tm (Air density function)
		 */
		double height = getPositionY(); //Height above the ground
		double Mzero = 28.9644; //kg/kmol mean molecular weight of air (Mo)
		double R = 8314.32; //N m/(kmol K) universal gas constant (R*)
		double Lmolb = -0.0065; //TAKEN FROM TABLE (K/m')
		double Tmolb = 288.15; //TAKEN FROM TABLE (K)
		double Heightb = 1100; //TAKEN FROM TABLE (m')
		double Tmol = Tmolb + Lmolb*(height - Heightb); //Tm = Tm,b + Lm,b(H-Hb) molecular - scale temperature
		double P = 4.11225000000000E+02; //pressure TAKEN FROM TABLE
		
		double airDensity = P * Mzero / R * Tmol;

		public double getAirDensity() {
			return airDensity;
		}
}