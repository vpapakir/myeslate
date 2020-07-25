package gr.cti.eslate.base.container.event;

import java.util.EventListener;
import java.beans.PropertyChangeEvent;

/**
 * PerformanceListener provides event notifications about what is going on
 * in the performance framework.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public interface PerformanceListener extends EventListener
{
  /**
   * This method is called every time a PerformanceTimerGroup is added to
   * some other PerformanceTimerGroup.
   */
  public abstract void
    performanceTimerGroupAdded(PerformanceTimerGroupEvent e);

  /**
   * This method is called every time a PerformanceTimerGroup is removed from
   * some other PerformanceTimerGroup.
   */
  public abstract void
    performanceTimerGroupRemoved(PerformanceTimerGroupEvent e);

  /**
   * This method is called when the enabled state of the PerformanceManager
   * changes.
   */
  public abstract void performanceManagerStateChanged(PropertyChangeEvent e);

}
