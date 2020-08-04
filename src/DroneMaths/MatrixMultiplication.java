package DroneMaths;

public class MatrixMultiplication {
	//member fields
	private double matrix1[][] = new double[3][3];
	private double matrix2[][] = new double[3][3];
	private double vector[] = new double[3];
	private double val;
	
	/****************************************CONSTRUCTORS***********************************************/
	public MatrixMultiplication(double matrix1[][], double val) {
		this.matrix1 = matrix1;
		this.val = val;
		multiplicateMatrixValue(matrix1, val);
	}
	
	public MatrixMultiplication(double vector[], double val) {
		this.vector = vector;
		this.val = val;
		multiplicateVectorValue(vector, val);
	}
	
	public MatrixMultiplication(double matrix1[][], double vector[], boolean isDot) {
		this.matrix1 = matrix1;
		this.vector = vector;
		if(isDot == true) {
			multiplicateMatrixDotVector(matrix1, vector);
		} else {
			multiplicateMatrixVector(matrix1, vector);
		}
	}
	
	public MatrixMultiplication(double matrix1[][], double matrix2[][]) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		multiplicateMatrixMatrix(matrix1, matrix2);
	}
	
	/*********************************************CALCULATIONS********************************************/
	private void multiplicateMatrixDotVector(double[][] matrix1, double[] vector) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				matrix1[i][j] = matrix1[i][j] * vector[i];
			}
		}
	}
	private void multiplicateMatrixValue(double[][] matrix1, double val) {
		for(int i=0; i<3; i++) {
			matrix1[i][0] = matrix1[i][0] * val;
		}
	}
	private void multiplicateMatrixMatrix(double[][] matrix1, double[][] matrix2) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				matrix1[i][j] = matrix1[i][j] * matrix2[i][j];
			}	
		}
	}
	private void multiplicateVectorValue(double[] vector, double val) {
		for(int i=0; i<3; i++) {
			vector[i] = vector[i]*val;	
		}
	}
	private void multiplicateMatrixVector(double[][] matrix1, double[] vector) {
		for(int i=0; i<3; i++) {
			vector[i] = matrix1[i][2] * vector[2];
		}
	}
	
	//Get methods
	public double getVector0() {
		return vector[0];
	}
	public double getVector1() {
		return vector[1];
	}
	public double getVector2() {
		return vector[2];
	}
	
	public double[][] getResultMatrix() {
		return matrix1;
	}
	public double get00() {
		return matrix1[0][0];
	}
	public double get01() {
		return matrix1[0][1];
	}
	public double get02() {
		return matrix1[0][2];
	}
	public double get10() {
		return matrix1[1][0];
	}
	public double get11() {
		return matrix1[1][1];
	}
	public double get12() {
		return matrix1[1][2];
	}
	public double get20() {
		return matrix1[2][0];
	}
	public double get21() {
		return matrix1[2][1];
	}
	public double get22() {
		return matrix1[2][2];
	}
}