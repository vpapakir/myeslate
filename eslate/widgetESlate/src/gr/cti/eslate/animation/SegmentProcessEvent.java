package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when enter/exit a segment.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-May-2002
 */
public class SegmentProcessEvent extends EventObject
{
  /**
   * The segment that entered/exited.
   */
  private BaseSegment segment;

  /**
   * Constructs a segment event.
   * @param	source	      The source of the event.
   * @param	segment     The segment that entered/exited.
   */
  public SegmentProcessEvent (Object source, BaseSegment segment)
  {
    super(source);
    this.segment = segment;
  }

  public BaseSegment getSegment() {
    return segment;
  }
}
