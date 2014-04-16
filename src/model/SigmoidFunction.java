package model;
import java.math.*;

public class SigmoidFunction {
	/*
	 * This class has methods to compute the sigmoid function. 
	 * sig(z) = 1/(1+e^(-z))
	 * sig(-1) = e^(-z)/(1+e^(-z)) 
	 */
	
	static double sigmoid(double z)
	{
		/*
		 * Computes sigmoid function value for a given z.
		 * @param z - the value 
		 */
		
		double value  = 1/(1+Math.exp(-z));
		return value;
		
	}
	
	public static void main(String args[])
	{
		/*
		 * Test the sigmoid function.
		 */
		System.out.println(sigmoid(-1000));
	}
	

}
