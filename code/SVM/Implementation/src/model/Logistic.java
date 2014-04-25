package model;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import preprocess.CorpusLoader;
import util.Vector;
import util.MatrixUtil;

public class Logistic {

    /** the learning rate */
    private double rate;
    
    //public 

    // weights
    public double[] weights;

    /** the number of iterations */
    
    public double C;
    public int N; // The number of training Instances.
	public int K; // The number of features.
	public double eps; // The convergence criterion.
	public SimpleMatrix dataMatrix;

    public Logistic (double rate, double C, SimpleMatrix dataMatrix)
    {
        this.rate = rate;        
        this.C=C;
        this.eps = 0.0001;
        
        this.N = dataMatrix.numRows();
		this.K = dataMatrix.numCols()-1; // The last column will be the class value.
		weights = new double[K];
		this.dataMatrix = dataMatrix;
		
		//System.out.println(N+"  "+K);
		
    }

    private double sigmoid(double z) {
        return 1 / (1 + Math.exp(-z));
    }
    
    public void getLikelihood()
    {
    	/*
    	 * Get data Likelihood
    	 */
    }

    public void train() {    	
    	/*
    	 * Train the logistic regression model.
    	 */
    	//double newLik = 1000;
    	double oldLik = 0;
    	double likDiff = 1000;
    	
    	while(likDiff>eps) {
    		
            double lik = 0.0;
            double regSum=0;           
            
            for (int i=0; i<N; i++) {
                //int[] x = instances.get(i).getX();
            	//SimpleMatrix a = dataMatrix.extractVector(true, i);
            	double[] x = MatrixUtil.getRow(dataMatrix, i);
            	
                double predicted = classify(x);
                //double label = instances.get(i).getLabel();
                double label = dataMatrix.get(i,K);
                //System.out.println(label);                
                
                for (int j=0; j<weights.length; j++) {
                	
                	if(j==0)
                	{
                		/*
                		 * No regularization
                		 */
                		
                		double update = rate * (label - predicted) * x[j];
                		weights[j] = weights[j] + (rate * (label - predicted) * x[j]);
                	//	System.out.println(update);
                	}
                	else
                	{
                		double update = (rate * (label - predicted) * x[j]) - (rate*C*weights[j]) ; 
                		//double update = (rate * (label - predicted) * x[j]);
                		weights[j] = weights[j] + update;
                		//System.out.println(update);
                		//weights[j] = weights[j] + rate * (label - predicted) * x[j];
                	}
                    
                }
                // not necessary for learning
                double pred = classify(x);                
                double sum=label * Math.log(classify(x)) + (1-label) * Math.log(1- classify(x));              
                lik+=sum;
                
                if(sum>0)
                {
                	/*
                	 * 
                	 */
                	System.out.println("crap");
                }             
                
            }
        //   System.out.println(Arrays.toString(weights) + " mle: " + lik);
            regSum=0;
            for(int k=1;k<weights.length;k++)
            {
            	
            	/*
            	 * Add regu
            	 */
            	regSum+=(weights[k]*weights[k]);
            }
            regSum = (0.5*C)*regSum;
            lik+=regSum;
            
            likDiff = Math.abs(lik - oldLik);
           // System.out.println(lik+"  "+ likDiff);
            oldLik=lik;            
        }
    }
       
    public void applyTestSet(String testSetPath)
    {
    	/*
    	 * Apply the learnt model to compute accuracy on the test set.
    	 */
    	SimpleMatrix testMatrix = CorpusLoader.loadCorporaFromPath(testSetPath, "msr");
    	double S = testMatrix.numRows(); // Size of the test instance
 		double M = testMatrix.numCols()-1; // Number of dimensions.
 		
 		int correct=0;
 		for(int i=0;i<S;i++)
 		{
 			/*
 			 * Predict each instance.
 			 */
 			 
 			double[] x = MatrixUtil.getRow(testMatrix, i);        	
            double predicted = classify(x);
            //double label = instances.get(i).getLabel();
            double label = testMatrix.get(i,K);
            double predLabel=0;
            
            /*
             * 
             */
           // 
           // System.out.println(predicted);
            if(predicted>=0.5)
            {
            	//System.out.println(predicted);
            	predLabel=1;
            }
            
            /*
             * 
             */
            
            if(predLabel==label)
            {
            	
            	correct+=1;
            	
            }
 		}
 		System.out.println("Accuracy :  "+ correct/S);    	
    }
    
   

    public double classify(double[] x) {
        double logit = .0;
        for (int i=0; i<weights.length;i++)  {
        	/*
        	if(i==0)
        	{
        		logit=1;
        	}
        	*/
            logit += weights[i] * x[i];
        }
        if(logit<-1000)
        {
        	//System.out.println("Crap");
        }
      //  System.out.println(logit);
        return sigmoid(logit);
    }
    
    


    public static void main(String... args) throws FileNotFoundException {
    	
    	/*
    	 * Logisic regression test.
    	 */
    	
    	SimpleMatrix dataMatrix = CorpusLoader.loadCorporaFromPath("/home/pradeep/courses/IR/HW5/data/data/train.txt", "msr");
    	//SimpleMatrix dataMatrix = CorpusLoader.loadCorporaFromPath("/home/pradeep/courses/IR/HW5/data/data/opData.txt", "msr");
    	
    	
    	//SimpleMatrix dataMatrix = CorpusLoader.loadCorporaFromPath("/home/pradeep/courses/IR/HW5/data/data/brIp.txt", "msr");
    	//dataMatrix.print();
		Logistic trainer = new Logistic(0.01,0.01, dataMatrix);
		trainer.train();
		//trainer.applyTestSet(testSetPath);
		
		/*
		 * 
		 */
		trainer.applyTestSet("/home/pradeep/courses/IR/HW5/data/data/train.txt");
		//trainer.applyTestSet("/home/pradeep/courses/IR/HW5/data/data/brIp.txt");
		
		/*
        List<Instance> instances = DataSet.readDataSet("dataset.txt");
        Logistic logistic = new Logistic(5);
        logistic.train(instances);
        int[] x = {2, 1, 1, 0, 1};
        System.out.println("prob(1|x) = " + logistic.classify(x));

        int[] x2 = {1, 0, 1, 0, 0};
        System.out.println("prob(1|x2) = " + logistic.classify(x2));
		*/
    }
}


