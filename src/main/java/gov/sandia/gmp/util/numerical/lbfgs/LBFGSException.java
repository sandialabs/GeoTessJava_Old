//Module:        $RCSfile: LBFGSException.java,v $
//Revision:      $Revision: 1.2 $
//Last Modified: $Date: 2010/06/09 12:37:08 $
//Last Check-in: $Author: jrhipp $

package gov.sandia.gmp.util.numerical.lbfgs;

/**
* <p>Title: </p>
*
* <p>Description: </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: Sandia National Laboratories</p>
*
* @author Jim Hipp
* @version 1.0
*/
@SuppressWarnings("serial")
public class LBFGSException extends Exception
{
  public int iflag = 3;
  
  public LBFGSException()
  {
   super();
  }
  
  public LBFGSException(String string)
  {
     super(string);
  }
  
  public LBFGSException(int iflg, String string)
  {
     super(string);
     iflag = iflg;
  }
  
  public LBFGSException(int iflg, String string, Throwable throwable)
  {
     super(string, throwable);
     iflag = iflg;
  }
  
  public LBFGSException(Throwable throwable)
  {
     super(throwable);
  }
}
