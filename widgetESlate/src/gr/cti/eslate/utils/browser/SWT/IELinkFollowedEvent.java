package gr.cti.eslate.utils.browser.SWT;

import org.eclipse.swt.browser.*;

import gr.cti.eslate.utils.browser.*;

/**
 * Event generated when a link is followed by clicking on it with the mouse.
 *
 * @author	Kriton Kyrimis
 * @version	2.0.12, 30-Oct-2006
 */
public class IELinkFollowedEvent extends LinkFollowedEvent
{
  /**
   * The encapsulated event.
   */
  LocationEvent locationEvent;

  /**
   * Create an IELinkFollowedEvent by converting a LocationEvent
   * generated by Internet Explorer.
   * @param	e	The event to convert.
   */
  public IELinkFollowedEvent(LocationEvent e)
  {
    locationEvent = e;
  }

  /**
   * Returns the URL of the link that was followed.
   * @return	The requested URL.
   */
  public String getLink()
  {
    String url = locationEvent.location;
    // If URL is a file reference, change it into a file URL using the
    // conventions followed by IE.
    if ((url.length() >= 2) && (url.charAt(1) == ':')) {
      url = "file:///" + url.replace('\\', '/');
    }
    return url;
  }
}
