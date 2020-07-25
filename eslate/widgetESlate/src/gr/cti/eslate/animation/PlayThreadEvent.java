package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when play thread state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 12-Jun-2002
 */
public class PlayThreadEvent extends EventObject
{
  /**
   * Constructs a play thread event.
   * @param	source	      The source of the event.
   */
  public PlayThreadEvent (Object source)
  {
    super(source);
  }
}
