package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when looped playback state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 12-Jun-2002
 */
public class LoopEvent extends EventObject
{
  /**
   * The state of looped playback.
   */
  private boolean looped;

  /**
   * Constructs a loop event.
   * @param   source  The source of the event.
   * @param   looped  The state of looped playback
   */
  public LoopEvent (Object source, boolean looped)
  {
    super(source);
    this.looped = looped;
  }

  public boolean getLooped() {
    return looped;
  }
}
