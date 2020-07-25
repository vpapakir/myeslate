package gr.cti.eslate.animation;

import java.util.EventObject;

/**
 * Event triggered when the animation's state changes.
 * The event's getSource() method will return the E-Slate handle of the
 * animation.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public class AnimationEvent extends EventObject
{
  /**
   * The actor that added/removed.
   */
  private BaseActor actor;

  /**
   * Constructs a animation event.
   * @param	source	      The source of the event.
   * @param	actor         The actor of the animation.
   */
  public AnimationEvent (Object source, BaseActor actor)
  {
    super(source);
    this.actor = actor;
  }

  public BaseActor getActor() {
    return actor;
  }
}

