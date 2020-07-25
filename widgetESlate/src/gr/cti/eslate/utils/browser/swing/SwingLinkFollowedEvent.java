package gr.cti.eslate.utils.browser.swing;

import gr.cti.eslate.utils.browser.*; 

/**
 * Event generated when a link is followed by clicking on it with the mouse.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class SwingLinkFollowedEvent extends LinkFollowedEvent
{
  /**
   * The URL of the link that was followed.
   */
  String url;

  /**
   * Create a SwingLinkFollwedEvent.
   * @param     url     The URL that was followed.
   */
  public SwingLinkFollowedEvent(String url)
  {
    this.url = url;
  }

  /**
   * Returns the URL of the link that was followed.
   * @return    The requested URL.
   */
  public String getLink()
  {
    return url;
  }
}
