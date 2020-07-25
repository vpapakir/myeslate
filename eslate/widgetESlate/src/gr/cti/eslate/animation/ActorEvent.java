package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when the actor's state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public class ActorEvent extends EventObject
{
  /**
   * The segment that added/removed.
   */
  private BaseSegment segment;

  /**
   * Constructs an actor event.
   * @param	source	      The source of the event.
   * @param	segment     The segment of the actor.
   */
  public ActorEvent (Object source, BaseSegment segment)
  {
    super(source);
    this.segment = segment;
  }

  public BaseSegment getSegment() {
    return segment;
  }
}

