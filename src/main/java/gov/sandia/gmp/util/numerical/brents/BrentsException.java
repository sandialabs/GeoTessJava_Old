// Module:        $RCSfile: BrentsException.java,v $
// Revision:      $Revision: 1.2 $
// Last Modified: $Date: 2010/06/09 12:37:06 $
// Last Check-in: $Author: jrhipp $

package gov.sandia.gmp.util.numerical.brents;

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
public class BrentsException extends Exception
{
  public BrentsException()
  {
    super();
  }

  public BrentsException(String string)
  {
      super(string);
  }
  
  public BrentsException(String string, Throwable throwable)
  {
      super(string, throwable);
  }
  
  public BrentsException(Throwable throwable)
  {
      super(throwable);
  }
}
