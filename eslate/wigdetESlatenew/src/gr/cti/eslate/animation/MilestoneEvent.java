package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when the milestone's state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public class MilestoneEvent extends EventObject
{
  /**
   * The value that changed.
   */
  private int when;

  /**
   * Constructs a milestone event.
   * @param	source	      The source of the event.
   * @param	when          The value of the milestone that changed.
   */
  public MilestoneEvent (Object source, int when)
  {
    super(source);
    this.when = when;
  }

  public int getWhen() {
    return when;
  }
}
