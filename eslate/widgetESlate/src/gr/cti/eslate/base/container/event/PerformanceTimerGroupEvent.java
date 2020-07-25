package gr.cti.eslate.base.container.event;

import java.util.EventObject;
import gr.cti.eslate.base.container.PerformanceTimerGroup;

/**
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class PerformanceTimerGroupEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Marks PerformanceTimerGroupEvents fired when a PerformanceTimerGroup is
   * added to another.
   */
  public static final int PERFORMANCE_GROUP_ADDED = 0;
  /**
   * Marks PerformanceTimerGroupEvents fired when a PerformanceTimerGroup is
   * removed from another.
   */
  public static final int PERFORMANCE_GROUP_REMOVED = 1;
  /**
   * The parent between two groups involved in the event.
   */
  private PerformanceTimerGroup parent;
  /**
   * The child between two groups invloved in the event.
   */
  private PerformanceTimerGroup child;
  /**
   * The id of the event. One of PERFORMANCE_GROUP_ADDED,
   * PERFORMANCE_GROUP_REMOVED.
   */
  private int id;

  /**
   * Constucts a PerformanceTimerGroupEvent.
   * @param     performanceManager      The shared instance of the
   *                                    PerformanceManager.
   * @param     id                      One of PERFORMANCE_GROUP_ADDED,
   *                                    PERFORMANCE_GROUP_REMOVED.
   * @param     parent                  The parent.
   * @param     child                   The child.
   */
  public PerformanceTimerGroupEvent(Object performanceManager,
                                    int id, PerformanceTimerGroup parent,
                                    PerformanceTimerGroup child)
  {
    super(performanceManager);
    this.id = id;
    this.parent = parent;
    this.child = child;
  }

  /**
   * Returns the id of the event. One of PERFORMANCE_GROUP_ADDED,
   * PERFORMANCE_GROUP_REMOVED.
   */
  public int getID()
  {
    return id;
  }

  /**
   * Returns the parent PerformanceTimerGroup.
   */
  public PerformanceTimerGroup getParent()
  {
    return parent;
  }

  /**
   * Returns the child PerformanceTimerGroup.
   */
  public PerformanceTimerGroup getChild()
  {
    return child;
  }

}
