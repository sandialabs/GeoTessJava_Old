package gov.sandia.gmp.util.numerical.polygon;

/**
 * Support for the PolygonCSG operation type. Eithe "UNION"  or "INTERSECT".
 * 
 * @author jrhipp
 *
 */
public enum CSGOperation
{
  UNION()
  {
  	String opSymbol() {return "|";}
  },

  INTERSECT()
  {
  	String opSymbol() {return "&";}
  };
  
  abstract String opSymbol();
}
