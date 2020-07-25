package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when the segment's state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public class SegmentEvent extends EventObject
{
  /**
   * The milestone that added/removed.
   */
  private BaseMilestone milestone;

  /**
   * Constructs a segment event.
   * @param	source	      The source of the event.
   * @param	milestone     The milestone of the segment.
   */
  public SegmentEvent (Object source, BaseMilestone milestone)
  {
    super(source);
    this.milestone = milestone;
  }

  public BaseMilestone getMilestone() {
    return milestone;
  }
}
