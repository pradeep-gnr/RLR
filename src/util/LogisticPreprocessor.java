package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Arrays;

import preprocess.*;

import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class LogisticPreprocessor implements AbstractCorpusPreprocessor{
	
	/*
	 * Sets up training and testing data for Logistic regression.  
	 */
	
	public LogisticPreprocessor()
	{
		/*
		 * Constructor.
		 * 
		 */
		
	}	
	
	public PairData getPairMatrix(LinkedHashMap<List<Integer>,Vector> pairMap,Boolean trainFlag) throws IOException	
	
	{
		/*
		 * Return a data Matrix where each instance is computed for each d1-d2  
		 */
				
		HashMap<Integer,DocPair> indexDocPairMap = new HashMap<Integer, DocPair>(); // Keeps track of meta information for each index in the data matrix.		
		ArrayList<Vector> newFeatureVectorList = new ArrayList<Vector>(); // The final pairwise feature vector list
		
		/*
		 * Iterate through each query and gather all relevant and irrelevant documents for it.
		 */
	//	HashMap<K, V>
		
		/*
		 * Walk through once and get a list of query and its corresponding document pairs.
		 */
		
		LinkedHashMap<Integer,ArrayList<Integer>> qryAllDocMap = new LinkedHashMap<Integer,ArrayList<Integer>>() ; // Keep track of all documents for a given query.
		
		Iterator it = pairMap.entrySet().iterator();
		 while (it.hasNext()) {
			 
		     Map.Entry curKeyVal = (Map.Entry)it.next();
		     List<Integer> curPair = (List<Integer>) curKeyVal.getKey();		     
		     
		     int qryId = curPair.get(0);
		     int docId = curPair.get(1);
		     
		     if(!qryAllDocMap.containsKey(qryId))
		     {
		    	/*
		    	 * 
		    	 */
		    	 ArrayList<Integer> tmpList =  new ArrayList<Integer>();
		    	 tmpList.add(docId);
		    	 qryAllDocMap.put(qryId,tmpList);
		     }
		     else
		     {
		    	 ArrayList curDocList = qryAllDocMap.get(qryId);
		    	 curDocList.add(docId);
		    	 qryAllDocMap.put(qryId, curDocList); 
		    	 
		     }
		 }
		 
		 //System.out.println(qryAllDocMap);
		 
		     /*
		      * Second pass.
		      * For each query for each pair of documents <di,dj>- compute f(di) - f(dj) and add to vector list.
		      * where f is the feature vector.
		      */
		     
		     Iterator iter  = qryAllDocMap.entrySet().iterator();		     
		     while(iter.hasNext())
		     {
		    	 /*
		    	  * First loop through each query and generate pairs.		    	  * 
		    	  */
		    	 Map.Entry curQryDocEntry = (Map.Entry)iter.next();		    	 
		    	 int curQry = (Integer) curQryDocEntry.getKey();
		    	 //System.out.println(curQry);
		    	 ArrayList<Integer> docList = (ArrayList<Integer>) curQryDocEntry.getValue();
		    	 //System.out.println(docList.size());
		    	 
		    	 //System.out.println(curQry);
		    	 //System.out.println(docList);
		    	 
		    	 /*
		    	  * Get each pair's feature vector.
		    	  */
		    	 for(int i=0;i<docList.size();i++)
		    	 {
		    		 for(int j=0;j<docList.size();j++)
		    			 /*
		    			  * Get all pairs of di and dj such that di=relevant and dj=irrelevant.
		    			  */
		    		 {
		    			 if(i==j)
		    				 continue;
		    			 
		    			 int docId1 = docList.get(i);
		    			 int docId2 = docList.get(j);
		    			 
		    			//System.out.println(docId1 +"  "+ docId2);
		    			 
		    			 Vector d1 =  pairMap.get(Arrays.asList(curQry,docId1));
		    			 Vector d2 =  pairMap.get(Arrays.asList(curQry,docId2));		 
		    			 
		    			 /*
		    			  * 
		    			  */		    	
		    			// System.out.println(d1);
		    			 double class1 = d1.elements[d1.dim-1];
		    			 double class2 = d2.elements[d2.dim-1];		    			 
		    					    			 
		    			 if(class1>class2 || trainFlag==false)
		    			 {
		    				 /*
		    				  * d1 is relevant while d2 is irrelevant.
		    				  */
		    				 Vector diffVector = subtractVectors(d1,d2);
		    				 //System.out.println(class1+"   "+class2);
		    				 
		    				 /*
		    				  * Make label 1
		    				  */
		    				 diffVector.elements[diffVector.dim-1] = 1;
		    				 newFeatureVectorList.add(diffVector); 				 
		    				 /*
		    				  * Get the index of the pair. 
		    				  */
		    				 int index = newFeatureVectorList.size()-1;	
		    				 indexDocPairMap.put(index, new DocPair(curQry,docList.get(i),docList.get(j)));
		    				// System.out.println(curQry+"  "+docList.get(i) + "  "+ docList.get(j));
		    				 
		    				 /*
		    				  * Add the inverted case.
		    				  */
		    				 
		    				 
		    				 //diffVector.elements[diffVector.dim-1] = 0;	
		    				
		    				 double[] tmpArray = diffVector.elements.clone();
		    				 
		    				 Vector tmpVector = new Vector(tmpArray);
		    				 Vector invertVector = VectorOperations.invertVector(tmpVector);
					    	 invertVector.elements[invertVector.dim-1] = 0;
					    	 //invertVector.elements[0]=-1;
					    	 newFeatureVectorList.add(invertVector);		    				 
					    	 index = newFeatureVectorList.size()-1;
					    	 indexDocPairMap.put(index, new DocPair(curQry,docList.get(i),docList.get(j))); 				
		    				 
		    			 }
		    		 }
		    	 }
		    				 
		    	 //Vector d1Vec = pairMap.get(new Pair())
		     }
		     	 
		/*
		 * Make the data Matrix.
		 */
		 int rowLen = newFeatureVectorList.size();
		 int colLen = newFeatureVectorList.get(0).dim;
		// System.out.println("CoLen " +colLen);
		 SimpleMatrix dataMatrix= getMatrixFromVectorList(newFeatureVectorList,rowLen,colLen);	
		 //System.out.println(indexDocPairMap);
		// MiscUtilities.writeMatrixToFile(dataMatrix, "/home/pradeep/mat.txt");
		 return new PairData(dataMatrix, indexDocPairMap);
		
	}
	 
	public Vector subtractVectors(Vector d1, Vector d2)	
	{
		/*
		 * Subtract vectors. 0th term is a bias term. Just make the final bias term to be 1.
		 */
		int l = d1.dim;
		double[] elements = new double[l];
		
		for(int i=0;i<l;i++)
			elements[i] = d1.elements[i] - d2.elements[i];
		
		elements[0]=1; // The bias term.
		
		return new Vector(elements);
				
		
	}
		 
	public PairData processTrainingData(String trainFile) 
	{
		/*
		 * Main Training data processing file.
		 */
		LinkedHashMap<List<Integer>,Vector> pairMap = convertToQueryDocMap(trainFile);
		
		/*
		 * Generate Pair data structure.
		 */
		
		try {
			PairData pairData = getPairMatrix(pairMap,true);
			//System.out.println(pairData.dataMatrix.numCols());
			//pairData.dataMatrix.print();
		//	pairData.dataMatrix.print();
			//System.out.println(pairData.dataMatrix.numRows() + pairData.dataMatrix.numCols());
			return pairData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	public PairData processTestData(String testFile)
	{
		/*
		 * Main test data processing file.
		 */
		LinkedHashMap<List<Integer>,Vector> pairMap = convertToQueryDocMap(testFile);
		/*
		 * Generate Pair data structure.
		 */
		try {
			PairData pairData = getPairMatrix(pairMap,false);
			//System.out.println(pairData.dataMatrix.numCols());
			//pairData.dataMatrix.print();
			//pairData.dataMatrix.print();
			return pairData;
			//System.out.println(pairData.dataMatrix.numRows() + pairData.dataMatrix.numCols());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}	
	}
	
	public LinkedHashMap<List<Integer>,Vector> convertToQueryDocMap(String filePath)
	{
		/*
		 * Read from a file and convert it into a queryID:<docId:FeatureVector> representation.
		 */
		File file = new File(filePath);
		/*
		 *  Declare a temporary list of feature vectors to be used before building the matrix.
		 *  Matrix size needs to be computed before a a new matrix object is computed. 
		 *  So we need to do one pass to estimate the size of training data.
		 */
		
		/*
		 * Maps each qId:DocId pair to its corresponding feature vector. Last column would be the class value.
		 */
		LinkedHashMap<List<Integer>,Vector> pairMap = new LinkedHashMap<List<Integer>,Vector>();
		
		
		ArrayList<Vector> featureVectors = new ArrayList<Vector>();   
		BufferedReader br;
		int colLen=0 ;// the number of feature dimensions + 1  for the class instance.;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			try {
				while ((line = br.readLine()) != null) {
				   // process the line.
					String[] components = line.split(" ");
					//System.out.println(components.length);
					int classVal = Integer.parseInt(components[0]);
					
					/*
					 * Use class value of -1 and 1 
					 */
					//if(classVal==0)
						//classVal=-1;
					
					double[] featureArray = new double[components.length-3+3];
					int k=0;
					featureArray[k] = 1;
					
					/*
					 * 
					 */
					k+=1;
					
					for(int i=1;i<components.length;i++)
					{
						/*
						 * Preprocess each of the value and store it in the array.
						 */
						String curFeatStr = components[i];						
						/*
						 * Check if the feature string is of the form number:number
						 */
						
						curFeatStr = curFeatStr.trim();
						if(curFeatStr.matches("[0-9]+:.*"))
						{
							//System.out.println(curFeatStr);
							String[] curFeatParts = curFeatStr.split(":");
							featureArray[k]=Double.parseDouble(curFeatParts[1]);
							k+=1;
						}																	
					}
				
					/*
					 * Add 3 additional features.
					 */
					featureArray[k] = featureArray[16]*featureArray[24]; k+=1;
					featureArray[k] = featureArray[42]*featureArray[43]; k+=1;
					featureArray[k] = featureArray[15]*featureArray[19]; k+=1;
					
					//System.out.println(featureArray[k-3]+"  "+ featureArray[k-2] +"  "+ featureArray[k-1]);
					
					colLen=k;
					featureArray[k]=classVal;
					//System.out.println(k);
					
					/*
					 * Now convert to a vector and add it to the Vector array List.
					 */
					int qryId = Integer.parseInt(components[1].split(":")[1].trim());
					int docId =  Integer.parseInt(components[components.length-1].trim());			
					
					//System.out.println(qryId+"  "+docId);
					/*
					 * Add to the paired Qurey-Doc Map.
					 */					
					pairMap.put(Arrays.asList(qryId,docId),new Vector(featureArray));
					
					Vector tmpVector = new Vector(featureArray);
					//System.out.println(tmpVector.dim);
					/*
					 * Sanity check to make sure vector was created properly,
					 */
					//System.out.println(tmpVector.elements[tmpVector.dim-1]);
					//System.out.println(tmpVector.elements[2]);
					
					
					//featureVectors.add(new Vector(featureArray));						
					//new Vector(featureArray).print();
					//System.out.println("\n\n");				
					
				}
				//int rowLen = featureVectors.size(); // the number of data instances
				//SimpleMatrix dataMatrix = getMatrixFromVectorList(featureVectors, rowLen, colLen);
				
				//dataMatrix.print();
				//return dataMatrix;
				return pairMap;
				
				//System.out.println(featureVectors.size());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	
	public static SimpleMatrix getMatrixFromVectorList(ArrayList<Vector> featureVectorList, int rowLen, int colLen)
	{
		/*
		 * Converts a list of vectors into a Matrix.
		 * Takes a list of feature vectors and converts it into a Matrix format,
		 * Note: last index at the feature vector list is the class value.
		 */
		
		SimpleMatrix dataMatrix = new SimpleMatrix(rowLen,colLen);
		//System.out.println(rowLen + "  "+ colLen);
		for(int i=0;i<featureVectorList.size();i++)
		{
						
			Vector curFeatVector = featureVectorList.get(i);
			
			// Normalize vector.
			curFeatVector = VectorOperations.normalizeVector(curFeatVector);
			//curFeatVector.elements[0]=1;
			for(int j=0;j<curFeatVector.dim; j++)
			{
				/*
				 * Fill in the matrix.
				 */
				dataMatrix.set(i,j,curFeatVector.elements[j]);
			}
		}
		
		return dataMatrix;		
	}
			 
	
	public static void main(String args[]) throws IOException
	{
		/*
		 * Test the processor method
		 */
		LogisticPreprocessor processor = new LogisticPreprocessor();
		//processor.processTrainingData("/home/pradeep/courses/IR/HW5/data/data/train.txt").dataMatrix.print();;
		processor.processTrainingData("/home/pradeep/courses/IR/HW5/data/data/train.txt");
		
		
		/*
		 * Test something.
		 */
		/*
		HashMap<List<String>,Integer> testMap = new HashMap<List<String>,Integer>();
		
		List<String> a= Arrays.asList("1","c");
		
		testMap.put(a, 100);
		System.out.println(testMap.get(Arrays.asList("1","c")));
		*/
		
	}
	

}

