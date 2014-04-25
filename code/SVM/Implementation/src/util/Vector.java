package util;

public class Vector {
	/*
	 * A class to define a Vector 
	 */
	public int dim; // Dimensions of the Vector
	public double[] elements; //Elements of the vector.
	
	public void print()
	{
		/*
		 * Print the vector
		 */
		for(int i=0;i<elements.length;i++)
			System.out.print(" "+elements[i]);
	}
	
	public Vector(double[] elements)
	{
		/*
		 * Initialize the Vector.	  
		 */
		dim = elements.length;
		this.elements = elements;		
	}
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
	}

}
