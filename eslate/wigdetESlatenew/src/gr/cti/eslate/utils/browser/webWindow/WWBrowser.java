package gr.cti.eslate.utils.browser.webWindow;

import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.browser.*;

import horst.webwindow.*;

/**
 * This class encapsulates the behavior of the WebWindow browser component,
 * providing a standard interface independent of the actual implementation.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
@SuppressWarnings(value={"unchecked"})
public class WWBrowser extends ESlateBrowser implements LinkFollowedListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The encapsulated browser.
   */
  private WebWindow browser;
  /**
   * The encapsulated browser's HTMLRenderer.
   */
  private HTMLRenderer htmlRenderer;
  /**
   * A list of HistoryChangeListeners added to the browser.
   */
  private Vector historyListeners = new Vector();
  /**
   * The back history.
   */
  private Vector backHistory = new Vector();
  /**
   * The forward history.
   */
  private Vector forwardHistory = new Vector();
  /**
   * The URL of the location currently being displayed.
   */
  private String location = null;

  /**
   * Create a WWBrowser instance.
   */
  public WWBrowser()
  {
    super();
//    browser = new WebWindow(false);
    browser = new WebWindow(
      false,
      new JScrollBar(JScrollBar.HORIZONTAL),
      new JScrollBar(JScrollBar.VERTICAL)
    );
    htmlRenderer = (HTMLRenderer)(browser.getHTMLPane());
    String language = Locale.getDefault().getLanguage();
    String os = System.getProperty("os.name").toLowerCase();
    // Stupid browser ignores page encoding.
    if (language.equals("el") || language.equals("el_GR")) {
      if (os.startsWith("windows")) {
        htmlRenderer.setCharacterEncoding("WINDOWS-1253");
      }else{
        htmlRenderer.setCharacterEncoding("ISO-8859-7");
      }
    }
    setLayout(new BorderLayout());
    add(browser, BorderLayout.CENTER);
    addLinkFollowedListener(this);
  }

  /**
   * Sets the browser location to a new URL.
   * @param     s       The URL to visit.
   * @exception Exception       Not thrown by this implementation.
   */
  public void setCurrentLocation(String s) throws Exception
  {
    if ((s != null) && !s.equals(getCurrentLocation())) {
      htmlRenderer.loadPage(new URL(s), true);
      if (location != null) {
        backHistory.addElement(location);
      }
      location = s;
      forwardHistory.removeAllElements();
      historyChanged();
    }
  }

  /**
   * Returns the URL currently being displayed by the browser.
   * @return    The requested URL.
   */
  public String getCurrentLocation()
  {
    return location;
  }

  /**
   * Sets the browser location to the "home" URL.
   * @return    "http://e-slate.cti.gr/"
   */
  public String home()
  {
    try {
      setCurrentLocation("http://e-slate.cti.gr/");
    } catch (Exception e) {
    }
    return location;
  }

  /**
   * Visits the previous URL in the browser's history.
   * @return    The previous URL in the browser's history.  This method does
   *            nothing if the browser is displaying the first URL in the
   *            browser's history.
   */
  public String back()
  {
    if (backHistory.size() > 0) {
      int last = backHistory.size() - 1;
      String s = (String)(backHistory.elementAt(last));
      try {
        htmlRenderer.loadPage(new URL(s), false);
        forwardHistory.addElement(location);
        backHistory.removeElementAt(last);
        location = s;
        historyChanged();
      } catch (Exception e) {
      }
    }
    return location;
  }

  /**
   * Visits the next URL in the browser's history.
   * @return    The next URL in the browser's history.  This method does
   *            nothing if the browser is displaying the last URL in the
   *            browser's history.
   */
  public String forward()
  {
    if (forwardHistory.size() > 0) {
      int last = forwardHistory.size() - 1;
      String s = (String)(forwardHistory.elementAt(last));
      try {
        htmlRenderer.loadPage(new URL(s), false);
        if (location != null) {
          backHistory.addElement(location);
        }
        forwardHistory.removeElementAt(last);
        location = s;
        historyChanged();
      } catch (Exception e) {
      }
    }
    return location;
  }

  /**
   * Returns the URLs that were visited before the URL that is currently
   * being displayed.
   * @return    A vector containing the requested URLs.
   */
  public Vector getBackHistory()
  {
    return backHistory;
  }

  /**
   * Returns the URLs that were visited after the URL that is currently
   * being displayed.
   * @return    A vector containing the requested URLs.
   */
  public Vector getForwardHistory()
  {
    return forwardHistory;
  }

  /**
   * Stop loading the current URL.
   */
  public void stop()
  {
    htmlRenderer.stop();
  }

  /**
   * Reload the currently displayed URL.
   */
  public void refresh()
  {
    htmlRenderer.reload();
  }

  /**
   * Search for a given text in the displayed document.
   * @param     prev    Result from the previous search, so that the new
   *                    search will continue from the last one.
   * @param     text    Text for which to search. It can be one or more words
   *                    separated by spaces.
   * @param     useCurrentFrame If this parameter is true the search will be
   *                    conducted in the currently selected html frame.
   *                    Otherwise the search will happen in the html frame
   *                    that has input focus (has been clicked upon)
   */
  public int search(int prev, String text, boolean useCurrentFrame)
  {
    if (prev > 0) {
      return browser.findNext();
    }else{
      return browser.find(text);
    }
  }

  /**
   * Get the font used to display text rendered in a proportional font.
   * @return    The requested font.
   */
  public Font getProportionalFont()
  {
    return htmlRenderer.getDefaultFont();
  }

  /**
   * Sets the font used to display text rendered in a proportional font.
   */
  public void setProportionalFont(Font f)
  {
    htmlRenderer.setDefaultFont(f);
  }

  /**
   * Print the displayed document.
   * @param     f       The frame in which the browser is contained.
   * @param     title   Title to use for the print job.
   */
  public void printDoc(Frame f, String title)
  {
    htmlRenderer.printPage(true, true, true, false, true);
  }

  /**
   * Adds a listener for events generated when a link is followed by clicking
   * on it with the mouse.
   * @param     l       The listener to add.
   */
  public void addLinkFollowedListener(LinkFollowedListener l)
  {
    htmlRenderer.addLinkListener(new WWLinkListener(l));
  }

  /**
   * Removes a listener for events generated when a link is followed by
   * clicking on it with the mouse.
   * @param     l       The listener to add.
   */
  public void removeLinkFollowedListener(LinkFollowedListener l)
  {
    htmlRenderer.removeLinkListener(new WWLinkListener(l));
  }

  /**
   * Checks whether the browser supports printing the current document.
   * @return    True.
   */
  public boolean supportsPrint()
  {
    return true;
  }

  /**
   * Checks whether the browser supports searching the current document for a
   * given text string.
   * @return    True.
   */
  public boolean supportsSearch()
  {
    return true;
  }

  /**
   * Returns the underlying browser implementation.
   * @return    An instance of class ice.iblite.Browser.
   */
  public Object getBrowser()
  {
    return browser;
  }

  /**
   * Adds a listener for history changed events.
   * @param     l       The listener to add.
   */
  public void addHistoryChangeListener(HistoryChangeListener l)
  {
    if (!(historyListeners.contains(l))) {
      historyListeners.addElement(l);
    }
  }

  /**
   * Removes a listener for history changed events.
   * @param     l       The listener to remove.
   */
  public void removeHistoryChangeListener(HistoryChangeListener l)
  {
    if (historyListeners.contains(l)) {
      historyListeners.removeElement(l);
    }
  }

  /**
   * Invokes the history changed listeners attached to the browser.
   */
  private void historyChanged()
  {
    Vector listeners = (Vector)(historyListeners.clone());
    int n = listeners.size();
    for (int i=0; i<n; i++) {
      HistoryChangeListener listener =
        (HistoryChangeListener)(listeners.elementAt(i));
      HistoryChangedEvent e = new HistoryChangedEvent(this);
      listener.historyChanged(e);
    }
  }

  /**
   * Invoked when a link is followed.
   * @param     e       The event signalling the following of the link.
   */
  public void linkFollowed(LinkFollowedEvent e)
  {
    if (location != null) {
      backHistory.add(location);
    }
    location = e.getLink();
    forwardHistory.removeAllElements();
    historyChanged();
  }
}
