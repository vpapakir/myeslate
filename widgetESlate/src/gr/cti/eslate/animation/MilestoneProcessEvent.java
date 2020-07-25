package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when find a milestone.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-May-2002
 */
public class MilestoneProcessEvent extends EventObject
{
  /**
   * The milestone that found.
   */
  private Milestone milestone;

  /**
   * Constructs a milestone event.
   * @param	source	      The source of the event.
   * @param	milestone     The milestone that found.
   */
  public MilestoneProcessEvent (Object source, Milestone milestone)
  {
    super(source);
    this.milestone = milestone;
  }

  public Milestone getMilestone() {
    return milestone;
  }
}
