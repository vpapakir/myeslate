package gr.cti.eslate.utils.browser.SWT;

import org.eclipse.swt.browser.*;

import gr.cti.eslate.utils.*;
import gr.cti.eslate.utils.browser.*;

/**
 * LocationListener for the Internet explorer-based implementation of
 * ESlateBrowser.
 *
 * @author	Kriton Kyrimis
 * @version	2.0.12, 30-Oct-2006
 */
public class IEListener implements LocationListener
{
  /**
   * The encapsulated link followed listener.
   */
  private LinkFollowedListener listener;

  /**
   * A flag indicating whether the link currently being processed was visited
   * by clicking on a link with the mouse.
   */
  BooleanWrapper followingLink = new BooleanWrapper(true);

  /**
   * Create an IEListener instance by converting a LinkFollowedListener.
   */
  public IEListener(LinkFollowedListener l)
  {
    listener = l;
  }

  /**
   * Returns the encapsulated LinkFollowedListener.
   * @return	The requested LinkFollowedListener.
   */
  public LinkFollowedListener getLinkFollowedListener()
  {
    return listener;
  }

  public void changed(LocationEvent e)
  {
    if (followingLink.getValue()) {
      listener.linkFollowed(new IELinkFollowedEvent(e));
    }else{
      followingLink.setValue(true);
    }
  }

  public void changing (LocationEvent e)
  {
  }

}
