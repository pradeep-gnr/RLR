package util;

import model.Logistic;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.ejml.simple.SimpleMatrix;

import preprocess.CorpusLoader;

public class LogisticPairwisePredictor implements AbstractPredictor {
	/*
	 * Main class to simulate pairwise prediction experiment for ranking.
	 */
	public LogisticPreprocessor preProcessor;
	/*
	 * Pairwise training data.
	 */
	public PairData trainingData;
	/*
	 * Pairwise test data.
	 */
	public PairData testData;
	public SimpleMatrix testMatrix;
	public Logistic logModel;
	
	public LogisticPairwisePredictor()
	{
		this.preProcessor = new LogisticPreprocessor();
	}
	
	public void loadTrainingData(String trainFile)
	{
		/*
		 * Load training data.
		 */
		this.trainingData = preProcessor.processTrainingData(trainFile);
		 
	}
	public void loadTestingData(String testFile)
	{
		//this.testData = preProcessor.processTestData(testFile);
		//SimpleMatrix testMatrix = CorpusLoader.loadCorporaFromPath(path, type)
		this.testMatrix = CorpusLoader.loadCorporaFromPath(testFile, "msr");
		}
	
	public void trainModel(double C)
	{
		/*
		 * The main training module.
		 */
		this.logModel = new Logistic(0.0001,C, this.trainingData.dataMatrix);
		logModel.train();
		
	}
	
	public void rankTestData()
	{
		/*
		 * Rank Test data,
		 */
	}
	
	public void generateRankList(ArrayList<Double> predictionList)
	{
		/*
		 * Produce ranking List for each query.
		 */
		for(int i=0;i<predictionList.size();i++)
		{
			/*
			 * For each possible prediction list pair.
			 */
			DocPair curQryInfo = this.testData.indexDocPairMap.get(i);
			int qryId = curQryInfo.qryId;
			int d1 =  curQryInfo.d1;
			int d2 =  curQryInfo.d2;
			
			if(i==10)
				break;
			//System.out.println(qryId+"  " + d1+"  "+d2);		
			
		}		
	}
	
	public void predictOnTestData() throws IOException
	{
		/*
		 * Apply prediction on test data.
		 * Walk through test data and predict each label.
		 */		
		SimpleMatrix testMatrix = this.testMatrix; // the test data matrix
		/*
		 * 
		 */
		int S = testMatrix.numRows(); // Size of the test instance
		//System.out.println(S);
 		int M = testMatrix.numCols(); // Number of dimensions.
 		ArrayList<Double> predictionList = new ArrayList<Double>();
 		
 		int correct=0; 		
 		
 		/*
 		 * Write output to file
 		 */
 		//File file = new File(opFile);
 		//FileWriter fw = new FileWriter(file.getAbsoluteFile());
		//BufferedWriter bw = new BufferedWriter(fw);
		
 		
 		for(int i=0;i<S;i++) 		
 		{
 			/*
 			 * Predict each instance.
 			 */
 			 
 			//ystem.out.println(i);
 			double[] x = MatrixUtil.getRow(testMatrix, i);        	
            double predicted = this.logModel.classify(x);
            //double label = instances.get(i).getLabel();
            double label = testMatrix.get(i,M-1);
            System.out.println(predicted);
          //  bw.write(predicted+"\n");
            /*
             * 
             */
            double predLabel=0;  
           // System.out.println(predicted);
            predictionList.add(predicted);
            
            /*
             * 
             */
           //       
            
            /*
            if(predicted>0.5)
            	predLabel=1;
                        
            //System.out.println(label+"  "+predLabel);            
            if(label==predLabel)            	
            	correct+=1;
            */
 		}		
 		//System.out.println("Accuracy  "+ correct + "  "+ S);
 		//generateRankList(predictionList);
		//bw.close();
	}
	
	public static void main(String args[]) throws Exception
	{
		/*
		 * Make predictions for Logistic regression.
		 */
		if(args.length==0)
		{
			/*
			 * return
			 */
			System.out.println("Pass DATA.TXT as argument");
			return; 
		}
		else{
			
			/*
			 * Process DATA.txt
			 */
			
			FileInputStream fis = new FileInputStream(new File(args[0]));
			 
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		 
			String line = null;
			int i=0;
			String trainPath=null;
			String testPath=null;
			double c=0;
			while ((line = br.readLine()) != null) {
				String[] comp = line.split("=");
				comp[0]=comp[0].trim();
				comp[1]=comp[1].trim();
				
				/*
				 * 
				 */
				//System.out.println(comp[0]);
				if(comp[0].equals("train"))
					trainPath=comp[1];
				
				if(comp[0].equals("test"))
					testPath = comp[1];
				
				if(comp[0].equals("c"))
					c=Double.parseDouble(comp[1]);				
				//System.out.println(line);
			}
		 
			br.close();
			
		if(trainPath==null||testPath==null||c==0)
			throw new Exception("Parameters wrongly initialized");
		
		LogisticPairwisePredictor pairPredictor = new LogisticPairwisePredictor();
		pairPredictor.loadTrainingData(trainPath);
		pairPredictor.loadTestingData(testPath);
		pairPredictor.trainModel(c);
		try {
			pairPredictor.predictOnTestData();
			//System.out.println("DONE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}


