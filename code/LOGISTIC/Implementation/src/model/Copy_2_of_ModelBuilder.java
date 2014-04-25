package model;
import java.io.ObjectInputStream.GetField;

import java.util.HashMap;
import util.Vector;
import util.VectorOperations;
import preprocess.*;
import org.ejml.simple.SimpleMatrix;

public class Copy_2_of_ModelBuilder {
	/*
	 * Class methods to generate a model from data. This class implements a regularized logistic regression
	 * model from training data. The training data is in a matrix format where the last column is the class label.	 *  
	 */
	
	/*
	 * The model that would be trained.
	 */
	public Model model;
	/*
	 * The dataMatrix is training data. 
	 * Each row of the matrix consists of a data instance and the last column of each instance is the corresponding class value.
	 * Currently only a binary classification problem is supported.
	 */
	public SimpleMatrix dataMatrix; 
	public double eps; // The learning Rate
	public double C; // The regularization constant parameter.
	public int N; // The number of training Instances.
	public int K; // The number of features.
	
	
	public Vector W; // Temporary vector to keep track of current weights.
	
	public Copy_2_of_ModelBuilder(double eps, double C, SimpleMatrix dataMatrix)
	{
		/*
		 * Constructor.
		 * Initialize parameters prior to training the model.
		 */	
		this.eps = eps;
		this.C = C;
		this.dataMatrix = dataMatrix;
		
		/*
		 * Set N and K
		 */
		this.N = dataMatrix.numRows();
		this.K = dataMatrix.numCols()-1; // The last column will be the class value.		
	}
	
	public Boolean hasConverged()
	{
		/*
		 * Check for convergence. Weights must have converged. |W-|
		 */
		return true;
	}
		
	public double getZ_I(int i)
	{
		/*
		 * Computes Z_i as sigma(k) wk*xik
		 */
		double z_i=0;
		for(int k=0;k<K;k++)
		{
			double weight = W.elements[K];
			double element = dataMatrix.get(i,k);
			z_i = z_i + (weight * element);
		}
		return z_i;
	}
	
	public double getUpdateSum(int k)
	{
		/*
		 * This method computes the update (delta) to be added to the weight in the previous iteration,
		 * @param - k - the kth weight.
		 * @return - computed sum.
		 */
		
		double totalSum = 0;
		for(int i=0;i<N;i++)
		{
			 /*
			  * Class value for a data instance d_i
			  */
			 double y_i = dataMatrix.get(i,K);		
			 
			 /*
			  * 
			  */
			 double x_i_k = dataMatrix.get(i,k);
			 
			 double z_i = getZ_I(i);
			 
			 //double g_zwi = SigmoidFunction.sigmoid(-y_i*z_i);
			 
			 //double prod = (y_i * x_i_k * g_zwi);
			 
			 double prod = x_i_k * (y_i - p_yi_1(z_i));
			 
			 //double  
			 
			 totalSum = totalSum + prod;
					 
		}
		
		return totalSum;		
	}
	
	public double p_yi_1(double z_i)
	{
		/*
		 * Returns the probability that a data point was classified into class 1.
		 * @param z_i = WT * x_i
		 * @return P_yi=1 , sigmoid function value.
		 */
		return SigmoidFunction.sigmoid(-z_i);
	}
	
	public double p_yi_0(double z_i)
	{
		/*
		 * Returns the probability that a data point was classified into class 0.
		 * @param z_i = WT * x_i
		 * @return P_yi=1 , sigmoid function value.
		 */
		return SigmoidFunction.sigmoid(z_i);
	}
	public double getLikelihood()
	{
		/*
		 * Compute the likelihood of the model using the given parameters.
		 * Likelihood is computed by:		 * 
		 * 
		 * L = -sum_i (log(g(yi*zi)) ) + C/2 * sum_k((w_k)^2)
		 * 
		 */
		
		/*
		 * Get the data likelihood
		 */
		double dataLikSum = 0; // Sum likelihood over whole data instances.
		for(int i=0;i<N;i++)
		{
			/*
			 * For all data instances
			 */
			double y_i = dataMatrix.get(i,K);
			double z_i = getZ_I(i);
			//double g_yz = SigmoidFunction.sigmoid(y_i*z_i);
			
			//dataLikSum+=Math.log(y_i*z_i);
			//dataLikSum+=Math.log(g_yz);
			
			//double term1 = Math.log(p_yi_1(z_i));
			//double term2 = Math.log(p_yi_0(z_i));
			
			double lik;
			
			double p_yi_zero = p_yi_0(z_i);
			double p_yi_one = p_yi_1(z_i);
			
			if(y_i==0)
				lik = Math.log(p_yi_zero);
			else
				
				lik = Math.log(p_yi_one);
			
			//double lik =  (y_i*term1) +  ( (1-y_i)* term2);
			
			dataLikSum+=lik ;
		}
		
		//dataLikSum = dataLikSum*-1;
		
		/*
		 * Incorporate regularization bias.
		 */
		
		double regSum = 0; // Sum the W coeffecients.
		for(int k=1;k<K;k++)
		{
			double w_k = W.elements[k];
			regSum = regSum+(w_k*w_k);
		}
		
		double L  = dataLikSum - (C/2 * (regSum));
		return L;
	}
	public void trainModel()
	{
		/*
		 *  Main function to train the model. From the initialized parameters, a gradient ascent based approach
		 *  is used to compute model parameters.
		 *  
		 *  PseudoCode for training
		 *  1) Randomly initialize weights w0..where K = |D_i| and w0 = bias term.
		 *  2) For t=1 to t_convergence
		 *  	2.1 ) for each weight
		 *  
		 *    --- Must update later.
		 *  
		 *  
		 */		
		
		/*
		 * Randomly initialize weights
		 */		
		
		//W = VectorOperations.generateRandomVector(K, 0, 1); //W[0] is the bias term.
		W = new Vector(new double[K]);
		//System.out.println(W.dim);
		
		/*
		 * Until convergence.
		 */
		
		int t=0;
		//System.out.println("Likelihood "+t+"  "+getLikelihood());
		while(true)
		{
			/*
			 * Update weights at each time step. Compute Log Likelihood and check if it converges.
			 */
			
			/*
			 * Declare a set of new weights that will be updated after this iteration.
			 */
			Vector WNew  = new Vector(new double[K]); 	
			//System.out.println(WNew.dim);
		//	System.out.println(K);
			for(int k=0;k<K;k++)
			{
				/*
				 * For each weight w_k 
				 */
				if(k==0)
				{
					/*
					 * Do non-regularized update. We do not want to regularize the Bias term.
					 */
					double compSum =  getUpdateSum(k);	
					//System.out.println(compSum);
					double w_k_new = W.elements[k] + (eps * compSum) ;
					WNew.elements[k] =  w_k_new;
					//W.elements[k] =  w_k_new;
				}
				
				else
				{
					/*
					 * For all other w_k when k!=0
					 * Compute wk_t+1 using wk
					 */
					
					double compSum =  getUpdateSum(k);
					
					double w_k_new = W.elements[k] + (eps * compSum) - (eps * C * W.elements[k]) ;
					WNew.elements[k] =  w_k_new;
					//W.elements[k] =  w_k_new;
				}			
			
			}
			W = WNew;
			t+=1;
			double likelihood= getLikelihood();
			System.out.println(t+ "  "+ likelihood);
			
		}
		
	}
	
	public static void main(String args[])
	{
		/*
		 * Test the gradient descent with regularization..
		 */
		System.out.println("Testing Gradient descent algorithm");
		
		/*
		 * Load the data matrix.		 * 
		 */
		SimpleMatrix dataMatrix = CorpusLoader.loadCorporaFromPath("/home/pradeep/courses/IR/HW5/data/data/train.txt", "msr");
		Copy_2_of_ModelBuilder trainer = new Copy_2_of_ModelBuilder(0.01,0.5, dataMatrix);
		
		/*
		 * Test the model
		 * 
		 */
		trainer.trainModel();
		
	}

}
