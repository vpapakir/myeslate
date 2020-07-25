package gr.cti.eslate.base.sharedObject;

import java.util.*;

import gr.cti.eslate.base.*;

/**
 * Implements shared objects.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public abstract class SharedObject
{
  /**
   * Constructs the shared object.
   * @param     handle  The E-Slate handle owned by the component to which
   *                    the shared object belongs.
   */
  @SuppressWarnings("unchecked")
  public SharedObject(ESlateHandle handle)
  {
    myHandle = handle;
    myListeners = new Vector();
    threads = new ArrayList<Thread>();
  }

  /**
   * Returns the E-Slate handle owned by the component to which the shared
   * object belongs.
   * @return    The requested handle.
   */
  public ESlateHandle getHandle()
  {
    return myHandle;
  }

  /**
   * Adds a listener to the shared object's list of listeners.
   * @param     listener        The listener to add to the list.
   */
  public void addSharedObjectChangedListener(SharedObjectListener listener)
  {
    if (!containsListener(listener)) {
      myListeners.addElement(listener);
    }
  }

  /**
   * Removes a listener from the shared object's list of listeners.
   * @param     listener        The listener to remove from the list.
   */
  public void removeSharedObjectChangedListener(SharedObjectListener listener)
  {
    myListeners.removeElement(listener);
  }

  /**
   * Checks whether a specified listener is in the shared object's list of
   * listeners.
   * @param     listener        The listener.
   */
  public boolean containsListener(SharedObjectListener listener)
  {
    return myListeners.contains(listener);
  }

  /**
   * Returns a copy of the shared object's list of listeners.
   * @return    The returned list.
   */
  public synchronized Vector getListeners()
  {
    return (Vector) myListeners.clone();
  }

  /**
   * Returns the number of listeners in the shared object's list of listeners.
   * @return    The number of listeners.
   */
  public synchronized int getNumberOfListeners()
  {
    return myListeners.size();
  }

  /**
   * Sends an event signifying that the shared object has changed to
   * all the listeners in the shared object's list of listeners.
   * This method should be called from within the shared object's
   * <EM>set</EM> methods.
   * @param     soe     The event to send to all the listeners.
   */
  public final void fireSharedObjectChanged(SharedObjectEvent soe)
  {
    SharedObjectListener listener;
    Thread t = Thread.currentThread();
    synchronized(threads) {
      // If the shared object is currently being processed in the current
      // thread, do not send it again, to avoid loops.
      if (threads.contains(t)) {
        return;
      }else{
        threads.add(t);
      }
    }
    try {
      int nListeners = myListeners.size();
      for (int i=0; i < nListeners; i++) {
        listener = (SharedObjectListener)myListeners.elementAt(i);
        listener.handleSharedObjectEvent(soe);
      }
    } finally {
      synchronized (threads) {
        threads.remove(t);
      }
    }
  }

  /**
   * Set a reference to the plug exporting the shared object.
   */
  public void setPlug(Plug plug)
  {
    this.plug = plug;
  }

  /**
   * Return the plug exporting the shared object.
   * @return    The requested plug.
   */
  public Plug getPlug()
  {
    return plug;
  }

  /**
   * Marks the shared object as currently being processed in the current
   * thread. This is part of the platform's loop prevention mechanism.
   * <EM>NOTE:</EM> This method is public only because it needs to be invoked
   * from a different package in the platform. Unless you know what you are
   * doing, avoid using it!
   */
  public void markAsUsedInCurrentThread()
  {
    Thread t = Thread.currentThread();
    synchronized(threads) {
      if (!threads.contains(t)) {
        threads.add(t);
      }
    }
  }

  /**
   * Marks the shared object as not being processed in the current
   * thread. This is part of the platform's loop prevention mechanism.
   * <EM>NOTE:</EM> This method is public only because it needs to be invoked
   * from a different package in the platform. Unless you know what you are
   * doing, avoid using it!
   */
  public void unmarkAsUsedInCurrentThread()
  {
    Thread t = Thread.currentThread();
    synchronized(threads) {
      int i = threads.indexOf(t);
      if (i >= 0) {
        threads.remove(i);
      }
    }
  }

  /**
   * Checks whether two objects are different, taking into account the
   * possibility that either object might be null. Use this method to check
   * whether the value set for your shared object is different from its current
   * value, so that you can avoid sending unnecessary
   * <code>SharedObjectEvent</code>s.
   * @param     ob1     The first object.
   * @param     ob2     The second object.
   * @return    True if the two objects are equal (or both <code>null</code>),
   *            false otherwise.
   */
  protected static boolean areDifferent(Object ob1, Object ob2)
  {
    if (ob1 == null) {
      return (ob2 != null);
    }else{
      return !(ob1.equals(ob2));
    }
  }

  /**
   * A reference to the E-Slate handle owned by the component to which the
   * shared object belongs.
   */
  protected ESlateHandle myHandle;

  /**
   * The object's listeners.
   */
  protected Vector myListeners;

  /**
   * The plug exporting the shared object.
   */
  private Plug plug = null;

  /**
   * The list of threads currently processing shared object events for this
   * shared object.
   */
  protected final ArrayList<Thread> threads;
}
