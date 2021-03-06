package util;
import java.lang.Math;

public class SimilarityComputer {
	/*
	 * Class to compute cosine similarity between two 
	 */	
	
	public static double getCosineSimilarity(Vector a, Vector b) throws DimensionMismatchException
	{
		/*
		 * Computes Cosine similarity between two vectors.
		 * @return - Returns a value between -1 to 1
		 */
		
		double dotProd = getDotProduct(a, b);
		double aNorm = getVectorNorm(a);
		double bNorm = getVectorNorm(b);
		
		if(aNorm==0||bNorm==0)
			return 0;
		
		double cosSim = (dotProd) / (aNorm*bNorm);				
		return cosSim;
		
		
	}
	
	public static double getDotProduct(Vector a, Vector b) throws DimensionMismatchException
	{
		/*
		 * Computes dot product between two vectors a and b.
		 * @return - Returns a value between -1 to 1.
		 */
		
		if(a.elements.length!=b.elements.length)
			throw new DimensionMismatchException("Dimensions do not match");
		
		else
		{
			double sum=0;
			int i=0;
			while(i<a.elements.length)
			{
				
				sum= sum+(a.elements[i]*b.elements[i]);
				i++;
			}
			return sum;
			
		}		
		
	}
	public static double getVectorNorm(Vector a)
	{
		/*
		 * Computes the L2 norm of a vector a
		 * @return - norm
		 */
		
		double sqSum=0;
		
		for(int i=0;i<a.elements.length;i++)
		{
			sqSum=sqSum+(a.elements[i]*a.elements[i]);			
		}
		return Math.sqrt(sqSum);		
	}
	
	public static void main(String args[])
	{
		/*
		 * Test dot product computation !
		 */
		Vector a = new Vector(new double[]{-0.5345224838248488,0.8017837257372732,-0.2672612419124244,0.0,0.0});
		Vector b = new Vector(new double[]{0.0,0.0,0.0,0.0,0.0});
		try {
			System.out.println(getDotProduct(a, b));
		} catch (DimensionMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(getCosineSimilarity(a, b));
		} catch (DimensionMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
