package model;
import util.Vector;
import util.DimensionMismatchException;
import util.*;


public class Model {
	/*
	 * The main Regularized regression model. This class defines the parameters to be used in the regression model. 
	 */
	
	/* vector of weights where w = w_0, w_1, w_2, w_3...w_k where k  is the dimension of each data point.
	 * Also w0 is a bias term.
	 */
	
	
	Vector w; 
	
	public Model(Vector weights) 
	{
		/*
		 * Initialize the parameters of the model
		 * @param weights - A vector of weights.
		 */		
		w=weights;
	}
	
	public double getPrediction(Vector x) throws DimensionMismatchException
	{
		/*
		 * Computes the predicted class value for a data point x.
		 * sigmoid(wx). 
		 */
				
		double dotScore =  SimilarityComputer.getDotProduct(x, w);
		return SigmoidFunction.sigmoid(dotScore);	
	}
	
	
}
