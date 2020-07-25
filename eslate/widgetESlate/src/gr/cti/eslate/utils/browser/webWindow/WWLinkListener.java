package gr.cti.eslate.utils.browser.webWindow;

import gr.cti.eslate.utils.browser.*;

import horst.webwindow.event.*;

/**
 * LinkListener for the WebWindow browser component-based implementation of
 * ESlateBrowser.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class WWLinkListener implements LinkListener
{
  /**
   * The encapsulated link followed listener.
   */
  private LinkFollowedListener listener;

  /**
   * Create a LinkListener instance by converting a LinkFollowedListener.
   * @param     l       The LinkFollowedListener to convert.
   */
  public WWLinkListener(LinkFollowedListener l)
  {
    listener = l;
  }

  public void mouseEnteredLink(LinkEvent e)
  {
  }

  public void mouseExitedLink(LinkEvent e)
  {
  }

  public void mousePressedOnLink(LinkEvent linkevent)
  {
  }

  public void mouseReleasedOnLink(LinkEvent linkevent)
  {
  }

  public boolean linkClicked(LinkEvent e)
  {
    listener.linkFollowed(new WWLinkFollowedEvent(e));
    return true;
  }
}
