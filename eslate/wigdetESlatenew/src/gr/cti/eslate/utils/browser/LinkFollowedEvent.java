package gr.cti.eslate.utils.browser;

/**
 * Event generated when a link is followed by clicking on it with the mouse.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public abstract class LinkFollowedEvent
{
  /**
   * Returns the URL of the link that was followed.
   * @return    The requested URL.
   */
  public abstract String getLink();
}
