package gr.cti.eslate.base.container.event;

import java.beans.*;

/**
 * The adapter of the PerformanceListener.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class PerformanceAdapter implements PerformanceListener
{
  /**
   * This method is called every time a PerformanceTimerGroup is added to
   * some other PerformanceTimerGroup.
   */
  public void performanceTimerGroupAdded(PerformanceTimerGroupEvent e)
  {
  }

  /**
   * This method is called every time a PerformanceTimerGroup is removed
   * from some other PerformanceTimerGroup.
   */
  public void performanceTimerGroupRemoved(PerformanceTimerGroupEvent e)
  {
  }

  /**
   * This method is called when the enabled state of the PerformanceManager
   * changes.
   */
  public void performanceManagerStateChanged(PropertyChangeEvent e)
  {
  }

}
