package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when the cursor's time changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 30-Apr-2002
 */
public class CursorEvent extends EventObject
{
  /**
   * The time of the cursor.
   */
  private int time;

  /**
   * Constructs a cursor event.
   * @param	source	      The source of the event.
   * @param	time          The time of the cursor that changed.
   */
  public CursorEvent (Object source, int time)
  {
    super(source);
    this.time = time;
  }

  public int getTime() {
    return time;
  }
}
