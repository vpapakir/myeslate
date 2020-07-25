package gr.cti.eslate.chronometer;

import gr.cti.eslate.base.*;

/**
 * Event triggered when the chronometer's state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * chronometer.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ChronometerEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Chronometer's "hours" value.
   */
  public int hour;
  /**
   * Chronometer's "minutes" value.
   */
  public int min;
  /**
   * Chronometer's "seconds" value.
   */
  public int sec;
  /**
   * Chronometer's "microseconds" value.
   */
  public int usec;

  /**
   * Constructs a chronometer state changed event.
   * @param     handle  The E-Slate handle of the chronometer whose state has
   *                    changed.
   */
  public ChronometerEvent(ESlateHandle handle)
  {
    super(handle);
  }
}
