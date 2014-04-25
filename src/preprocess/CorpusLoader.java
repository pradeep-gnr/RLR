package preprocess;
import Jama.Matrix;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import util.MatrixUtil;
import util.MiscUtilities;
import util.Vector;
import util.VectorOperations;

import org.ejml.simple.SimpleMatrix;;
public class CorpusLoader {
	/*
	 * This class loads data and converts into a matrix format.
	 */
	
	public static SimpleMatrix loadCorporaFromPath(String path,String type)
	{
		/*
		 * Load a corpus and convert into a Matrix form.
		 * Must write wrapper code to load from different corpora formats.
		 * Currently only a custom "msr" format is supported.  
		 */
		if(type=="msr")
		{
			SimpleMatrix dataMatrix = loadMSRCorpora(path);
			return dataMatrix;
		}
			
		return null;
	}
	
	public static SimpleMatrix loadMSRCorpora(String path)
	{
		/*
		 * This method loads the data from a file in MSR format and converts it into 
		 * a matrix.
		 * Each row in the matrix consists of feature vectors and the last column would be the class value.
		 * Each line is in the form described below.
		 * 
		 * <classVal> fId_1:fvalue fId_2:fvalue .... #docid = docIdNumber  
		 * @param path: The path of the corpus.
		 * 
		 */
		
		File file = new File(path);
		/*
		 *  Declare a temporary list of feature vectors to be used before building the matrix.
		 *  Matrix size needs to be computed before a a new matrix object is computed. 
		 *  So we need to do one pass to estimate the size of training data.
		 */		
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
					
					featureArray[k] = featureArray[17]*featureArray[25]; k+=1;
					featureArray[k] = featureArray[33]+featureArray[34]+ featureArray[35]+featureArray[36]; k+=1;
					featureArray[k] = featureArray[7]*featureArray[8]; k+=1;
					
				
					colLen=k;
					featureArray[k]=classVal;
					//System.out.println(k);
					
					/*
					 * Now convert to a vector and add it to the Vector array List.
					 */					
					featureVectors.add(new Vector(featureArray));			
					//System.out.println(new Vector(featureArray).dim);
					//new Vector(featureArray).print();
					//System.out.println("\n\n");				
					
				}
				int rowLen = featureVectors.size(); // the number of data instances
				SimpleMatrix dataMatrix = getMatrixFromVectorList(featureVectors, rowLen, colLen);
				
				//dataMatrix.print();
				//MiscUtilities.writeMatrixToFile(dataMatrix, "/home/pradeep/mat.mat");
				return dataMatrix;
				
				
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
		
		SimpleMatrix dataMatrix = new SimpleMatrix(rowLen,colLen+1);
		//System.out.println(rowLen + "  "+ colLen);
		for(int i=0;i<featureVectorList.size();i++)
		{
						
			Vector curFeatVector = featureVectorList.get(i);
			//System.out.println(curFeatVector.dim);
			
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
	public static void main(String args[])
	{
		/*
		 * Test the Corpus loader.
		 */
		
		//System.out.println(loadCorporaFromPath("/home/pradeep/courses/IR/HW5/data/data/train.txt", "msr"));
		
		
	}

}
