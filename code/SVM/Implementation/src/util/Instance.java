package util;
 

public class Instance {
	/*
	 * An Instance class consists of 2 components
	 * 1) feature vector
	 * 2) Corresponding label for the training data vector
	 */
	public Vector features;
	public int label;	
	
	public Instance(Vector features, int label)
	{
		/*
		 * Initialize an instance.
		 */
		this.features = features;
		this.label = label;
	}
	

}
