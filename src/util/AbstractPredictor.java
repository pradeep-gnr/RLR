package util;

import java.io.IOException;

public interface AbstractPredictor {
	/*
	 * Abstract interface to load training data, Load testing data, Apply predictions 
	 * and write to file.
	 */
	
	public void loadTrainingData(String trainFile);
	public void loadTestingData(String trainFile);
	public void predictOnTestData() throws IOException; // The output file to write all predictions to.	

}
