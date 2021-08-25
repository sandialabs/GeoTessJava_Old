package gov.sandia.gmp.util.statistics;

import java.util.Arrays;

import gov.sandia.gmp.util.containers.arraylist.ArrayListDouble;

/**
 * An extension of Statistic that stores input values for Statistic in a
 * sorted manner form which the median can be extracted. The object currently
 * overrides add(double value) and reset(). It should also override read() and
 * write() but as yet does not.
 * 
 * @author jrhipp
 *
 */
@SuppressWarnings("serial")
public class StatisticMedian extends Statistic
{ 
	/**
	 * True if additions to values0 have been made since the last call to
	 * getMedian.
	 */
	private boolean changed = true;

	/**
	 * If true values are set into storage as abs(value).
	 */
	private boolean setAbsolute = false;
	
	/**
	 * The set of values saved in an ArrayListDouble
	 */
  ArrayListDouble values = new ArrayListDouble();

  /**
   * Standard constructor.
   * 
   * @param absolute Sets setAbsolute with this value.
   */
  public StatisticMedian(boolean absolute)
  {
  	super();
  	setAbsolute = absolute;
  }

  /**
   * Adds the input value into the Statistic and into the sorted values map.
   * If setAbsolute is true the values are added into the map as the absolute
   * value of val.
   */
  @Override
  public void add(double val)
  {
  	super.add(val);
  	changed = true;
  	if (setAbsolute)
  		values.add(Math.abs(val));
  	else
  		values.add(val);
  }

  /**
   * Returns the median of the input values.
   * 
   * @return The median of the input values.
   */
  public double getMedian()
  {
  	if (getCount() == 0.0)
  		return 0.0;
  	else
  	{
  		// sort if changed is true

  		if (changed)
  		  Arrays.sort(values.getArray(), 0, values.size());

	  	changed = false;
  		int midIndex = (int) (values.size() / 2);
	  	boolean even = (values.size() % 2 == 0);

	  	if (even)
	  		return (values.get(midIndex-1) + values.get(midIndex)) / 2.0;
	  	else
	  		return values.get(midIndex);
  	}
  }

  /**
   * Resets the Statistic to empty.
   */
  @Override
  public void reset()
  {
  	super.reset();
  	values.clear();
  	changed = true;
  }
}
