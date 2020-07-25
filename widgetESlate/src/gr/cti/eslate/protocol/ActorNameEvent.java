package gr.cti.eslate.protocol;

import java.util.*;

/**
 * Event triggered when the actor's name changes.
 * The event's getSource() method will return the E-Slate handle of the
 * sequence.
 *
 * @author      Augustine Grillakis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.sequence.ActorNameEvent
 */
public class ActorNameEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The name that changed.
   */
  private String actorName;

  /**
   * Constructs a milestone event.
   * @param     source        The source of the event.
   * @param     actorName     The value of the milestone that changed.
   */
  public ActorNameEvent (Object source, String actorName)
  {
    super(source);
    this.actorName = actorName;
  }

  public String getActorName() {
    return actorName;
  }
}
