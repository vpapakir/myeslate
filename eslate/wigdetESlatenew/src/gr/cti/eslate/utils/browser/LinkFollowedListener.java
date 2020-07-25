package gr.cti.eslate.utils.browser;

import java.util.*;

/**
 * Listener for events generated when a link is followed by clicking on it
 * with the mouse.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface LinkFollowedListener extends EventListener
{
  /**
   * Invoked when a link is followed by clicking on it with the mouse.
   * @param     e       The event sent when a link is followed.
   */
  public void linkFollowed(LinkFollowedEvent e);
}
