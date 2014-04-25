package util;
import java.math.*;
import java.util.Random;
public class VectorOperations {
	/*
	 * Class methods for various vector operations.
	 */
	
	static double getVectorMean(Vector vector)
	{
		/*
		 * Compute vector mean
		 */
		double totalSum = 0;
		
		for(int i=0;i<vector.elements.length;i++)
		{
			totalSum+=vector.elements[i];			
		}		
		return totalSum/vector.dim;
	}
	
	public static Vector generateRandomVector(int dim, double lower, double upper)
	{
		/*
		 * This method generates a random vector of a dimension d. Each v_i in V is sampled between lower and upper
		 * @param dim - dimension of the vector
		 * @param lower - lower bound of the range from which we want to generate a random number.
		 * @param upper - upper bound of the range from which we want to generate a random number.
		 */
		double[] array = new double[dim];
		for(int i=0;i<dim;i++)
		{
			double R = (Math.random() * (upper - lower)) + lower;
			array[i] = R;
		}
		return new Vector(array);
	}
	
	public static double getLogVectorNorm(Vector a)
	{
		/*
		 * Computes the L2 norm of a vector a
		 * @return - norm
		 */
		
		double sqSum=0;
		
		for(int i=1;i<a.elements.length-1;i++)
		{
			sqSum=sqSum+(a.elements[i]*a.elements[i]);			
		}
		return Math.sqrt(sqSum);		
	}
	
	public static Vector invertVector(Vector vector)
	{
		/*
		 * Inverts a vector and reverses the sign.
		 */
		for(int i=1;i<vector.dim-1;i++)
		{
			vector.elements[i] = vector.elements[i]*-1; 
		}
		return vector;
	}
	
	public static Vector normalizeVector(Vector vector)
	{
		/*
		 * Normalize each vector.
		 */
		double norm = getLogVectorNorm(vector);
		if(norm==0)
			norm=1;
		//norm = norm-1;
		/*
		 * Divide by norm.
		 */
		for(int i=1;i<vector.dim-1;i++)
			vector.elements[i] = vector.elements[i]/norm;
		return vector;
	}
	static double getVectorVariance(Vector vector)
	{
		/*
		 * Compute vector variance
		 */
		double totalSum = 0;
		int l=0;
		double mean =  getVectorMean(vector); 
		for(int i=0;i<vector.elements.length;i++)
		{
			double element = vector.elements[i];
			
			double diff  = element-mean;			
			totalSum+=(diff*diff);
			//l+=1;
		}		
		return Math.pow( totalSum,0.5);		
	}	
	
	static Vector standardizeVector(Vector vector)
	{
		/*
		 * Standardizes a vector
		 */
		double mean = getVectorMean(vector);
		//System.out.println(mean);
		
		double variance = getVectorVariance(vector);
		if(variance==0)
			return vector;
		
		//System.out.println(variance);
		
		/*
		 * Change the vector
		 */
		
		for(int i=0;i<vector.elements.length;i++)
		{
			double element = vector.elements[i];
			element = (element - mean) / variance;
			vector.elements[i] = element;			
		}
		return vector;
	}
	
	
	static Vector reStandardizeVector(double mean, double variance, Vector vector)
	{
		/*
		 * Restandardize a vector into original format.
		 */
		
		for(int i=0;i<vector.elements.length; i++)
		{
			/*
			 * Re standardize 
			 */
			double element  = vector.elements[i];
			vector.elements[i] = (element*variance)+mean;			 
		}
		return vector;
	}
	
	public static void main(String args[])
	{
		/*
		 * 
		 */
		double[] array = new double[]{1,2,3};		
		
		Vector v = new Vector(array);
		double mean = getVectorMean(v);		
		double variance = getVectorVariance(v);
		System.out.println("STUFF  " +mean+"  "+variance);
		
		double[] a = standardizeVector(v).elements;
		
		for(int i=0;i<a.length;i++)
		{
			System.out.println(a[i]);
		}
		System.out.println(a);
		
		Vector newVect = new Vector(a);
		
		Vector reStandard = reStandardizeVector(mean, variance , newVect );
		
		a = reStandard.elements;
		
		for(int i=0;i<a.length;i++)
		{
			System.out.println(a[i]);
		}
		System.out.println(a);
		
		/*
		 * 
		 */
		double[] b = generateRandomVector(6, 0, 1).elements;
		for(int i=0;i<b.length;i++)
		{
			System.out.println(b[i]);
		}
		
		
		
		
	}

}
