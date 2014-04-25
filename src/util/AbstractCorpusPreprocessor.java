package util;
import java.util.List;

import java.util.LinkedHashMap;

public interface AbstractCorpusPreprocessor {
	
	/*
	 * Abstract preprocessor Interface for processing training data; 
	 */	 
	public PairData processTrainingData(String trainFile); 
	public PairData processTestData(String testFile);
	public LinkedHashMap<List<Integer>,Vector> convertToQueryDocMap(String file);
	
}
