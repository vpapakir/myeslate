package gr.cti.eslate.navigator.suppliers; //14Jun2000: moved to package "suppliers"

import java.awt.Component;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.BorderLayout;

import gr.cti.eslate.navigator.models.INavigator;
import gr.cti.eslate.navigator.models.INavigatorEventSource;
import gr.cti.eslate.navigator.models.INavigatorEventSink;
import gr.cti.eslate.utils.browser.*;
import gr.cti.eslate.utils.browser.swing.*;

/**
 * Navigator (web browser etc.) component for E-Slate
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
public class SwingNavigator
 extends JComponent
 implements INavigator,
            INavigatorEventSource, // 15Mar2000
            HyperlinkListener,
            LinkFollowedListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private ESlateBrowser browser;
  private INavigatorEventSink listener;

  public SwingNavigator()
  {
    setLayout(new BorderLayout());

    browser=new SwingBrowser();
    JTextPane actualBrowser = (JTextPane)(browser.getBrowser());
    actualBrowser.addHyperlinkListener(this);
    browser.addLinkFollowedListener(this);

    add( (Component)browser, BorderLayout.CENTER );
  }

  // INavigator implementation

  public void setCurrentLocation(String location) throws Exception
  {
    if (listener != null) {
      listener.beforeNavigation(location, new boolean[]{false}); 
    }
    browser.setCurrentLocation(location);
    if (listener != null) {
      listener.navigationComplete(browser.getCurrentLocation());
    }
  }

  public String getCurrentLocation()
  {
    return browser.getCurrentLocation();
  }

  public String home()
  {
    String loc = browser.home();
    if (listener != null) {
      listener.navigationComplete(loc);
    }
    return loc;
  }

  public String forward()
  {
    String loc = browser.forward();
    if (listener != null) {
      listener.navigationComplete(loc);
    }
    return loc;
  }

  public String back()
  {
    String loc = browser.back();
    if (listener != null) {
      listener.navigationComplete(loc);
    }
    return loc;
  }

  public void stop()
  {
    browser.stop();
  }

  public void refresh()
  {
    browser.refresh();
    if (listener != null) {
      listener.navigationComplete(browser.getCurrentLocation());
    }
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


  // HyperlinkListener implementation

  public void hyperlinkUpdate(HyperlinkEvent e)
  {
    if (listener != null) {
      HyperlinkEvent.EventType type = e.getEventType();
      if (type.equals(HyperlinkEvent.EventType.ACTIVATED)) {
        listener.beforeNavigation(e.getURL().toString(), new boolean[]{false}); 
      }else{
        if (type.equals(HyperlinkEvent.EventType.ENTERED)) {
          listener.statusTextChange(e.getURL().toString());
        }else{
          if (type.equals(HyperlinkEvent.EventType.EXITED)) {
            listener.statusTextChange("");
          }
        }
      }
    }
  }

  // LinkFollowedListener implementation.

  public void linkFollowed(LinkFollowedEvent e)
  {
    if (listener != null) {
      listener.navigationComplete(e.getLink());
    }
  }

}
