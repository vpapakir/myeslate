package gr.cti.eslate.protocol;

import java.util.*;

/**
 * The listener interface for receiving events about changes at the actor's name.
 *
 * @author      Augustine Grillakis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.sequence.ActorNameListener
 */
public interface ActorNameListener extends EventListener
{
  /**
   * Invoked when actor's name parameter is changed.
   * @param   e   The actorName event.
   */
  public void actorNameChanged(ActorNameEvent e);
}

