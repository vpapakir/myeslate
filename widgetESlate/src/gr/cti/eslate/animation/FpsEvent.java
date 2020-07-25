package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when the fps change.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 14-Jun-2002
 */
public class FpsEvent extends EventObject
{
  /**
   * The maximum fps.
   */
  private int fps;

  /**
   * Constructs a fps event.
   * @param	source	      The source of the event.
   * @param	fps           The maximum fps.
   */
  public FpsEvent (Object source, int fps)
  {
    super(source);
    this.fps = fps;
  }

  public int getFps() {
    return fps;
  }
}
