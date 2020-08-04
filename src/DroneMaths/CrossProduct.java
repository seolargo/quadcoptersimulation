package DroneMaths; 
  
public class CrossProduct { 
	
	//Initialization.   
    private double[] crossArray = new double[3];
    private double vectorA[], vectorB[];
    
    //Constructor
    public CrossProduct(double vectorA[], double vectorB[]) {
    	this.vectorA = vectorA;
    	this.vectorB = vectorB;
    	calculate();
    }
    
    // Function to find cross product of two vector array.
    private void calculate(){ 
    	/*			|i  j   k|
    	 *  a x b = |ax ay az| = (ay*bz-az*by)*i + (ax*bz-az*bx)*j + (ax*by-ay*bx)*k  
    	 *          |bx by bz|
    	 */
        crossArray[0] = vectorA[1] * vectorB[2]  
                    - vectorA[2] * vectorB[1]; 
        crossArray[1] = vectorA[0] * vectorB[2]  
                    - vectorA[2] * vectorB[0]; 
        crossArray[2] = vectorA[0] * vectorB[1]  
                    - vectorA[1] * vectorB[0]; 
    }
    
    public double getCrossArrayX() {
    	return crossArray[0];
    }
    public double getCrossArrayY() {
    	return crossArray[1];
    }
    public double getCrossArrayZ() {
    	return crossArray[2];
    }
} 