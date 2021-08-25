package gov.sandia.gmp.util.numerical.simplex;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SimplexException extends Exception
{
  public SimplexException()
  {
    super();
  }

  public SimplexException(String string)
  {
    super(string);
  }

  public SimplexException(String string, Throwable throwable)
  {
    super(string, throwable);
  }

  public SimplexException(Throwable throwable)
  {
    super(throwable);
  }
}
