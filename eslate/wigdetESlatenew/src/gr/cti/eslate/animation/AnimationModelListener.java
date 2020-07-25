package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about changes at the animation model.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-Jun-2002
 */
public interface AnimationModelListener extends EventListener
{
  /**
   * Invoked when an actor is added.
   * @param   e   The animation event.
   */
  public void actorAdded(AnimationEvent e);

  /**
   * Invoked when an actor is removed.
   * @param   e   The animation event.
   */
  public void actorRemoved(AnimationEvent e);

  /**
   * Invoked when cursor time is changed.
   * @param   e   The cursor event.
   */
  public void timeChanged(CursorEvent e);
}
