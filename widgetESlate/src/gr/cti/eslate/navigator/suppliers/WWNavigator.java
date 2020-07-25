package gr.cti.eslate.navigator.suppliers; //14Jun2000: moved to package "suppliers"

import java.awt.*;
import javax.swing.*;

import gr.cti.eslate.navigator.models.*;
import gr.cti.eslate.utils.browser.*;
import gr.cti.eslate.utils.browser.webWindow.*;

import horst.webwindow.*;
import horst.webwindow.event.*;

/**
 * Navigator (web browser etc.) component for E-Slate
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
public class WWNavigator
 extends JComponent
 implements INavigator,
            INavigatorEventSource, // 15Mar2000
            HTMLPaneStatusListener,
            LinkListener,
            LinkFollowedListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private WWBrowser browser;
  private INavigatorEventSink listener;

  public WWNavigator()
  {
    setLayout(new BorderLayout());

    browser = new WWBrowser();
    HTMLPane p = ((WebWindow)browser.getBrowser()).getHTMLPane();
    p.addHTMLPaneStatusListener(this);
    p.addLinkListener(this);
    browser.addLinkFollowedListener(this);

    add( (Component)browser, BorderLayout.CENTER );
  }

  // INavigator implementation

  public void setCurrentLocation(String location) throws Exception
  {
    browser.setCurrentLocation(location);
  }

  public String getCurrentLocation()
  {
    return browser.getCurrentLocation();
  }

  public String home()
  {
    return browser.home();
  }

  public String forward()
  {
    return browser.forward();
  }

  public String back()
  {
    return browser.back();
  }

  public void stop()
  {
    browser.stop();
  }

  public void refresh()
  {
    browser.refresh();
  }

  public ESlateBrowser getESlateBrowser()
  {
    return browser;
  }

  // INavigatorEventSource implementation

  public void setNavigatorEventSink(INavigatorEventSink eventSink)
  {
    listener = eventSink;
  }

  // LinkListener implementation

  // HTMLPaneStatusListener implementation

  public boolean statusChanged(HTMLPaneStatusEvent evt)
  {
    switch (evt.getID()) {
      case HTMLPaneStatus.APPLET_STATUS_MSG:
      case HTMLPaneStatus.PAGE_LOADED:
      case HTMLPaneStatus.APPLET_LOADED:
        String msg = evt.getAppletStatusMessage();
        if (msg == null) {
          msg = "";
        }
        listener.statusTextChange(msg);
        break;
      default:
        break;
    }
    return true;
  }

  // LinkFollowedListener implementation

  public void linkFollowed(LinkFollowedEvent e)
  {
    if (listener != null) {
      listener.navigationComplete(e.getLink());
    }
  }

  // LinkListener implementation

  public boolean linkClicked(LinkEvent e)
  {
    return true;
  }

  public void mouseEnteredLink(LinkEvent e)
  {
    if (listener != null) {
      listener.statusTextChange(e.getLink());
    }
  }

  public void mouseExitedLink(LinkEvent e)
  {
    if (listener != null) {
      listener.statusTextChange("");
    }
  }

  public void mouseReleasedOnLink(LinkEvent e)
  {
  }

  public void mousePressedOnLink(LinkEvent e)
  {
  }

}
