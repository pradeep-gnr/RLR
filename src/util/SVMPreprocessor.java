package util;
import java.util.Formatter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.math.*;
import java.io.IOException;
import java.text.NumberFormat;

import org.ejml.simple.SimpleMatrix;

import preprocess.CorpusLoader;

public class SVMPreprocessor {
	/*
	 * Class for processing SVM data.
	 */
	
	public static void writeDataforSvmFormat(String opFile, SimpleMatrix dataMatrix) throws IOException
	{
		/*
		 * Write the data for SVM specific format.
		 */
		Formatter format = new Formatter();


		NumberFormat nf = NumberFormat.getInstance();
		//output.println(nf.format(myNumber));
		
		File file = new File(opFile);
 		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
	//	System.out.println(dataMatrix.numRows());
		int N= dataMatrix.numRows();
		
		for(int i=0;i<N;i++)
		{
			/*
			 * For each data point.
			 */
			int colSize = dataMatrix.numCols();
			String opString="";
			double label = dataMatrix.get(i,colSize-1);
			if(label==0)
				label=-1;
			
			opString+=label;
			
			for(int j=1;j<colSize-1;j++)
			{
				double val = dataMatrix.get(i,j);
				val = Math.round(val*10000.0)/10000.0;
				String valStr = nf.format(val);


			//	format.format(val);
				//String mystring = format.formatNumber(DOUBLENUMBERVARIABLE,val);
				opString = opString+" "+j+":"+ valStr;
			}
			bw.write(opString+"\n");			
		}
		//fw.close();
		bw.close();
	}
	
	public static void main(String args[]) throws Exception
	{
		/*
		 * Get the required parameters from training file.
		 */
		
		/*
		 * Load training data and generate pairs 
		 */
		if(args.length==0)
		{
			/*
			 * return
			 */
			System.out.println("Pass DATA.TXT as argument");
			throw new Exception("Pass DATA.TXT as argument");
			
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
		
		DataPreprocessor processor = new DataPreprocessor();
		SimpleMatrix trainMatrix = processor.processTrainingData(trainPath).dataMatrix;
		
		/*
		 * Process test data
		 * 
		 */
		SimpleMatrix testMatrix =  CorpusLoader.loadCorporaFromPath(testPath,"msr");
		
		/*
		 * Write data to file for SVM Light format.
		 */
		try {
			writeDataforSvmFormat("./Implementation/svmTrain.txt", trainMatrix);
			writeDataforSvmFormat("./Implementation/svmTest.txt", testMatrix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("DONE");
		
		
	}
	}

}
