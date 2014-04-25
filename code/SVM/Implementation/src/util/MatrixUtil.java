package util;

import org.ejml.simple.SimpleMatrix;

public class MatrixUtil {
	/*
	 * A set of custom matrix utilities.
	 */
	
	public static double[] getRow(SimpleMatrix matrix, int ind)
	{
		int size=matrix.numCols()-1;
		double[] vector = new double[size];
		
		for(int j=0;j<size;j++)
			vector[j] = matrix.get(ind,j);
		
		return vector;
	}

}
