package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about changes at the actor.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public interface ActorListener extends EventListener
{
  /**
   * Invoked when a segment is added.
   * @param   e   The actor event.
   */
  public void segmentAdded(ActorEvent e);

  /**
   * Invoked when a segment is removed.
   * @param   e   The actor event.
   */
  public void segmentRemoved(ActorEvent e);
}

