package gov.sandia.gmp.util.numerical.simplex;

/**
 * <p>Title: </p>
 *
 * <p>Description:
 * </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Sandia National Laboratories</p>
 *
 * @author Sandy Ballard
 * @version 1.0
 */
public interface SimplexFunction
{
	/**
	 * Classes that use the simplex minimization algorithm must implement the
	 * SimplexFunction interface and provide a method called
	 * simplexFunction(double[] x) that returns the fitness value at the position
	 * of the independent variable x.
	 * 
	 * <p>This method is only called by SimplexSequential, not by SimplexParallel.
	 *
	 * @param x double[]
	 * @return double
	 * @throws Exception 
	 */
  abstract double simplexFunction(double[] x) throws Exception;
  
}
