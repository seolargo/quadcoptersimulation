package DroneMaths;
//This is sample program to find the inverse of a matrix

public class MatrixInverse {
	
	//Member fields
	private double A[][];
	private double d[][];
	
	//Constructor
	public MatrixInverse() {
		A = new double[3][3];
		
		A[0][0] = 5e-3;
		A[0][1] = 0;
		A[0][2] = 0;
		
		A[1][0] = 0;
		A[1][1] = 5e-3;
		A[1][2] = 0;
		
		A[2][0] = 0;
		A[2][1] = 0;
		A[2][2] = 10e-3;
	    
		d = invert(A);   
	}

	public static double[][] invert(double a[][]) {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) 
            b[i][i] = 1;
 
        // Transform the matrix into an upper triangle
        gaussian(a, index);
 
        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                    	    -= a[index[j]][i]*b[index[i]][k];
 
        // Perform backward substitutions
        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

	
	// Method to carry out the partial-pivoting Gaussian
		// elimination.  Here index[] stores pivoting order.
		 
		    public static void gaussian(double a[][], int index[]) 
		    {
		        int n = index.length;
		        double c[] = new double[n];
		 
		 // Initialize the index
		        for (int i=0; i<n; ++i) 
		            index[i] = i;
		 
		 // Find the rescaling factors, one from each row
		        for (int i=0; i<n; ++i) 
		        {
		            double c1 = 0;
		            for (int j=0; j<n; ++j) 
		            {
		                double c0 = Math.abs(a[i][j]);
		                if (c0 > c1) c1 = c0;
		            }
		            c[i] = c1;
		        }
		 
		 // Search the pivoting element from each column
		        int k = 0;
		        for (int j=0; j<n-1; ++j) 
		        {
		            double pi1 = 0;
		            for (int i=j; i<n; ++i) 
		            {
		                double pi0 = Math.abs(a[index[i]][j]);
		                pi0 /= c[index[i]];
		                if (pi0 > pi1) 
		                {
		                    pi1 = pi0;
		                    k = i;
		                }
		            }
		 
		   // Interchange rows according to the pivoting order
		            int itmp = index[j];
		            index[j] = index[k];
		            index[k] = itmp;
		            for (int i=j+1; i<n; ++i) 	
		            {
		                double pj = a[index[i]][j]/a[index[j]][j];
		 
		 // Record pivoting ratios below the diagonal
		                a[index[i]][j] = pj;
		 
		 // Modify other elements accordingly
		                for (int l=j+1; l<n; ++l)
		                    a[index[i]][l] -= pj*a[index[j]][l];
		            }
		        }
		    }
	
	public double getValue00() {
		return d[0][0];
	}
	public double getValue01() {
		return d[0][1];
	}
	public double getValue02() {
		return d[0][2];
	}
	public double getValue10() {
		return d[1][0];
	}
	public double getValue11() {
		return d[1][1];
	}
	public double getValue12() {
		return d[1][2];
	}
	public double getValue20() {
		return d[2][0];
	}
	public double getValue21() {
		return d[2][1];
	}
	public double getValue22() {
		return d[2][2];
	}
}