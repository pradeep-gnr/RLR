package model;

import org.ejml.simple.SimpleMatrix;
import java.util.Arrays;

import preprocess.CorpusLoader;
import util.Vector;

public class LogisticRegression {
	
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
	
	public LogisticRegression(double eps, double C, SimpleMatrix dataMatrix)
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
		this.W = new Vector(new double[K]);
		
		System.out.println(K);
	}
	
	public double getZ_I(int i)
	{
		/*
		 * Computes Z_i as sigma(k) wk*xik
		 */
		double z_i=0;
		for(int k=0;k<K;k++)
		{
			double weight = W.elements[k];
			double element = dataMatrix.get(i,k);
			z_i = z_i + (weight * element);
		}
		return z_i;
	}
	
	public double predict(int i)
	{
		/*
		 * Apply prediction
		 */
		return SigmoidFunction.sigmoid(getZ_I(i));
	}
	public void printWeights(int k)
	{
		for(int i=0;i<k;i++)
			System.out.print(" "+W.elements[i]);
	}
	public void train()
	{
		/*
		 * Train the Model.
		 */
		
		int p=0;
		while(true)
		{
			/*
			 * Until convergence.
			 */
			/*
			 * Print weights and 
			 */
			double objective = 0d;
			System.out.println(Arrays.toString(W.elements));
			//int a=1;
			
			//System.out.println("\n");
			double lik=0;
			for(int i=0;i<N;i++)
			{
				/*
				 * For each iteration.
				 */
				//System.out.println(getZ_I(i));
				double prediction = predict(i);
				
				/*
				if(prediction==0||prediction==1)
				 	  continue;
				*/
				
				double label = dataMatrix.get(i,K);
				
				for(int j=0;j<K;j++)
				{
					
					double elt = dataMatrix.get(i,j);
					
					if(j==0)
					{
						W.elements[j] += (eps * (label-prediction) * dataMatrix.get(i,j)) ;
					}
					else
					{
					W.elements[j] +=  ((eps * (label-prediction) * dataMatrix.get(i,j)) - (C*eps*W.elements[j])) ;
					}
					//W.elements[j] = W.elements[j] + (eps * (label-prediction) * dataMatrix.get(i,j)) ;
				}
								
				
				// Compute Likelihood
				double predProb = predict(i);						
				double sum =label*Math.log(predProb) + (1-label)* Math.log(1-predProb);		
				
								
				if(Double.isNaN(sum))
				{
					//System.out.println(getZ_I(i));
					int c;
					double z_i = getZ_I(i);
					double term1=Math.log(predict(i));
					double term2=Math.log(1- predict(i));
					c=1;
					
					/*
					 * Handle zero cases.
					*/
					/*
					if(label==0 && predProb==0)
						sum=0;
					if(label==1 && predProb==1)
						sum=0;
						*/		
				}
				lik+=sum;							
			}
			// Add regularization values.
			
			double regSum = 0; // Sum the W coeffecients.
			
			 
			for(int d=1;d<K;d++)
			{
				double w_d = W.elements[d];
				regSum += (w_d*w_d);
			}	
			
			//System.out.println(lik+regSum);		
			p++;
		}
	}

	
	public static void main(String args[])
	{
		/*
		 * Test the logistic regression.
		 */
		
		System.out.println("Testing Gradient descent algorithm");
		
		/*
		 * Load the data matrix.		 * 
		 */
		SimpleMatrix dataMatrix = CorpusLoader.loadCorporaFromPath("/home/pradeep/courses/IR/HW5/data/data/train.txt", "msr");
		LogisticRegression trainer = new LogisticRegression(0.01,0.3, dataMatrix);
		
		/*
		 * Test the model
		 * 
		 */
		trainer.train();
	}
}
