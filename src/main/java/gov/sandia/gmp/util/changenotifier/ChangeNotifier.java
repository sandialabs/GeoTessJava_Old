package gov.sandia.gmp.util.changenotifier;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This notifier keeps a list of ChangeListeners, and notifies them when its
 * fireStateChanged method is called.
 *
 * This implements the Observer design pattern. ChangeNotifier is the
 * ConcreteObservable class, ChangeListener is the Observer interface, and the
 * Objects saved in list are the ConcreteObservers.
 *
 * @author Randall W. Simons
 * @version $Revision: 1.2 $, $Date: 2009/07/21 17:03:56 $
 */
public class ChangeNotifier
{
  // ~ Instance fields
  // ********************************************************

  /** Event that will tell listeners where a notification came from. */
  protected ChangeEvent event = null;

  /** List of listeners desiring notification of changes. */
  protected List<ChangeListener> list = new ArrayList<ChangeListener> ();

  // ~ Constructors
  // ***********************************************************

  /**
   * Create a new ChangeNotifier object.
   *
   * @param source the object that will be included as the source in the
   *            ChangeEvent sent to all listeners.
   */
  public ChangeNotifier(Object source)
  {
    event = new ChangeEvent(source);
  }

  // ~ Methods
  // ****************************************************************

  public void setSource(Object source)
  {
    event = new ChangeEvent(source);
  }

  /**
   * Register a listener to be notified of changes.
   *
   * @param ls the listener to add
   */
  public void addListener(ChangeListener ls)
  {
    list.add(ls);
  }

  /**
   * Cease notifying a listener of changes.
   *
   * @param ls the listener to stop notifying
   */
  public void removeListener(ChangeListener ls)
  {
    list.remove(ls);
  }

  public void removeAll()
  {
    list.clear();
  }

  /**
   * Notify all listeners that are registered for notification.
   */
  public void fireStateChanged()
  {
    for (ChangeListener listnr : list)
    {
      listnr.stateChanged(event);
    }
  }
}
