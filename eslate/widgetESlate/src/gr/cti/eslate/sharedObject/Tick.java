package gr.cti.eslate.sharedObject;

import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;

/**
 * This class implements a "tick" shared object. The shared object can convey
 * information about the time elapsed since the previous tick.
 *
 * @author      Kriton Kyrimis
 * @author      Thanasis Mantes
 * @version     5.0.0, 19-May-2006
 */
public class Tick extends SharedObject
{
  private long tick;

  /**
   * Construct a Tick shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     t       The value of the time elapsed since the previous tick
   *                    in microseconds. (Usually 0.)
   */
  public Tick(ESlateHandle handle, int t)
  {
    super(handle);
    tick = t;
  }

  /**
   * Construct a Tick shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     t       The value of the time elapsed since the previous tick
   *                    in microseconds. (Usually 0.)
   */
  public Tick(ESlateHandle handle, long t)
  {
    super(handle);
    tick = t;
  }

  /**
   * Set the value of the shared object's time elapsed since the previous tick.
   * @param     n       Time elapsed since the previous tick, in microseconds.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a plug to its output
   *                    part.
   */
  @SuppressWarnings(value={"deprecation"})
  public void setTick(int n, Vector path)
  {
    tick = n;

    // Create an event
    SharedObjectEvent soe = new SharedObjectEvent(this, path);

    fireSharedObjectChanged(soe);       // Notify the listeners
  }

  /**
   * Set the value of the shared object's time elapsed since the previous tick.
   * @param     n       Time elapsed since the previous tick, in microseconds.
   */
  public void setTick(int n)
  {
    tick = n;

    // Create an event
    SharedObjectEvent soe = new SharedObjectEvent(this);

    fireSharedObjectChanged(soe);       // Notify the listeners
  }

  /**
   * Set the value of the shared object's time elapsed since the previous tick.
   * @param     n       Time elapsed since the previous tick, in microseconds.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a plug to its output
   *                    part.
   */
  @SuppressWarnings(value={"deprecation"})
  public void setTick(long n, Vector path)
  {
    tick = n;

    // Create an event
    SharedObjectEvent soe = new SharedObjectEvent(this, path);

    fireSharedObjectChanged(soe);       // Notify the listeners
  }

  /**
   * Set the value of the shared object's time elapsed since the previous tick.
   * @param     n       Time elapsed since the previous tick, in microseconds.
   */
  public void setTick(long n)
  {
    tick = n;

    // Create an event
    SharedObjectEvent soe = new SharedObjectEvent(this);

    fireSharedObjectChanged(soe);       // Notify the listeners
  }

  /**
   * Return the value of the the shared object's time in microseconds elapsed
   * since the previous tick.
   * @return    Microseconds elapsed since the previous tick.
   */
  public int getTick()
  {
    return (int)tick;
  }

  /**
   * Return the value of the the shared object's time in microseconds elapsed
   * since the previous tick.
   * @return    Microseconds elapsed since the previous tick.
   */
  public long getTickLong()
  {
    return tick;
  }

}
