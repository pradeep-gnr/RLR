package util;
import org.ejml.simple.SimpleMatrix;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
public class MiscUtilities {
	/*
	 * Miscellanous utilities for various functions. 
	 */
	
	public static void writeMatrixToFile(SimpleMatrix matrix, String filePath) throws IOException
	{
		File file = new File(filePath);	
		
		BufferedWriter bufferedWriter = null;

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		/*
		 * 
		 */
		for(int i=0;i<matrix.numRows();i++)
			bw.write(Arrays.toString(MatrixUtil.getRow(matrix, i)));
			bw.write("\n\n");
	}

}
