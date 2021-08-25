//Module:        $RCSfile: LBFGSFunction.java,v $
//Revision:      $Revision: 1.1 $
//Last Modified: $Date: 2008/10/29 17:33:08 $
//Last Check-in: $Author: mchang $

package gov.sandia.gmp.util.numerical.lbfgs;

/**
 * @author jrhipp
 *
 * An interface used by the LBGS.lbgs functions that contain the function
 * definitions setFunctionAndGradient(double[], double[]) and
 * setDiagonal(double[], double[]) to be overridden by the implementor
 * of this interface to provide appropriate gradient and inverse hessian
 * diagonal estimate return values at the input vector x.
 */
public interface LBFGSFunction
{
  /**
   * Sets the gradient at each value of x and returns the functional value
   * 
   * @param x The independent parameter vector.
   * @param g The gradient defined at each parameter entry.
   * @return The functional value to be minimized evaluted at x.
   */
  abstract double setFunctionAndGradient(double[] x, double[] g)
           throws LBFGSException;
  
  /**
   * Sets the diagonal of the inverse Hessian approximation at the current
   * value of the vector x.
   * 
   * @param x The vector of independent parameters.
   * @param diag The diagonal approximation of the inverse hessian evaluated
   *        at x.
   */
  abstract void   setDiagonal(double[] x, double[] diag);
}
