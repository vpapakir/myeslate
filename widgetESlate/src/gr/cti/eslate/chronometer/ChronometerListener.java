package gr.cti.eslate.chronometer;

import java.util.*;

/**
 * The listener interface for receiving events about changes in the state of
 * the chronometer.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public interface ChronometerListener extends EventListener
{
  /**
   * Invoked when the chronometer is started.
   */
  public void chronometerStarted(ChronometerEvent e);

  /**
   * Invoked when the chronometer is stopped.
   */
  public void chronometerStopped(ChronometerEvent e);

  /**
   * Invoked when the chronometer's value is changed.
   */
  public void valueChanged(ChronometerEvent e);
}
