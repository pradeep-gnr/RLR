package util;
import org.ejml.simple.SimpleMatrix;
import java.util.HashMap;

public class PairData {
	/*
	 * Complete dataset of all pairs.
	 */
	public SimpleMatrix dataMatrix;
	
	/*
	 * // We can fetch the details of each index in the data matrix.
	 * For example, for each index -i, we can fetch the query, d1 and d2.
	 */
	public HashMap<Integer,DocPair> indexDocPairMap; 
	
	
	
	public PairData(SimpleMatrix dataMatrix, HashMap<Integer,DocPair> indexDocPairMap)
	{
		this.dataMatrix = dataMatrix;
		this.indexDocPairMap = indexDocPairMap;				
	}
	
	
}
