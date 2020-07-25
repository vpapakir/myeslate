package gr.cti.eslate.utils.browser;

import java.awt.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.browser.swing.*;

/**
 * This class encapsulates the behavior of a web browser, providing a standard
 * interface independent of the actual implementation.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public abstract class ESlateBrowser extends JPanel
{
  /**
   * Sets the browser location to a new URL.
   * @param     s       The URL to visit.
   * @exception Exception       Thrown if the URL cannot be visited. This
   *                    exception is only thrown if the underlying browser
   *                    implementation supports this functionality.
   */
  public abstract void setCurrentLocation(String s) throws Exception;

  /**
   * Returns the URL currently being displayed by the browser.
   * @return    The requested URL.
   */
  public abstract String getCurrentLocation();

  /**
   * Sets the browser location to the "home" URL.
   * @return    The home URL.
   */
  public abstract String home();

  /**
   * Visits the next URL in the browser's history.
   * @return    The next URL in the browser's history. This method does
   *            nothing if the browser is displaying the last URL in the
   *            browser's history.
   */
  public abstract String forward();

  /**
   * Visits the previous URL in the browser's history.
   * @return    The previous URL in the browser's history.  This method does
   *            nothing if the browser is displaying the first URL in the
   *            browser's history.
   */
  public abstract String back();

  /**
   * Returns the URLs that were visited before the URL that is currently
   * being displayed.
   * @return    A vector containing the requested URLs.
   */
  public abstract Vector getBackHistory();

  /**
   * Returns the URLs that were visited after the URL that is currently
   * being displayed.
   * @return    A vector containing the requested URLs.
   */
  public abstract Vector getForwardHistory();

  /**
   * Stop loading the current URL. This method will work only if the
   * underlying browser implementation supports this functionality.
   */
  public abstract void stop();

  /**
   * Reload the currently displayed URL.
   */
  public abstract void refresh();

  /**
   * Search for a given text in the displayed document. (<EM>Note:</EM> This
   * API is subject to change.)
   * @param     prev    Result from the previous search, so that the new
   *                    search will continue from the last one.
   * @param     text    Text for which to search. It can be one or more words
   *                    separated by spaces.
   * @param     useCurrentFrame If this parameter is true the search will be
   *                    conducted in the currently selected html frame.
   *                    Otherwise the search will happen in the html frame
   *                    that has input focus (has been clicked upon)
   */
  public abstract int search(int prev, String text, boolean useCurrentFrame);

  /**
   * Get the font used to display text rendered in a proportional font.
   * @return    The requested font. This result is only meaningful if the
   *            underlying implementation supports this functionality.
   */
  public abstract Font getProportionalFont();

  /**
   * Sets the font used to display text rendered in a proportional font.
   * This method will only work if the underlying implementation supports this
   * functionality.
   * @param     f       The font to set.
   */
  public abstract void setProportionalFont(Font f);

  /**
   * Print the displayed document.
   * @param     f       The frame in which the browser is contained.
   * @param     title   Title to use for the print job.
   */
  public abstract void printDoc(Frame f, String title);

  /**
   * Adds a listener for events generated when a link is followed by clicking
   * on it with the mouse.
   * @param     l       The listener to add.
   */
  public abstract void addLinkFollowedListener(LinkFollowedListener l);

  /**
   * Removes a listener for events generated when a link is followed by
   * clicking on it with the mouse.
   * @param     l       The listener to add.
   */
  public abstract void removeLinkFollowedListener(LinkFollowedListener l);

  /**
   * Checks whether the browser supports printing the current document.
   * @return    True if yes, false if no.
   */
  public abstract boolean supportsPrint();

  /**
   * Checks whether the browser supports searching the current document for a
   * given text string.
   * @return    True if yes, false if no.
   */
  public abstract boolean supportsSearch();

  /**
   * Returns the underlying browser implementation.
   * @return    An object implementing the browser.
   */
  public abstract Object getBrowser();

  /**
   * Adds a listener for history changed events.
   * @param     l       The listener to add.
   */
  public abstract void addHistoryChangeListener(HistoryChangeListener l);

  /**
   * Removes a listener for history changed events.
   * @param     l       The listener to remove.
   */
  public abstract void removeHistoryChangeListener(HistoryChangeListener l);

  /**
   * Creates an ESlateBrowser instance.
   * @return    An ESlateBrowser instance that uses a browser implementation
   *            that depends on the platform and the installed browser
   *            components. In order of preference, the returned browser will
   *            be one of: Internet Explorer, ICE browser, CalHTMLPane, or a
   *            simple browser built using Swing's JTextPane class.
   */
  public final static ESlateBrowser createBrowser()
  {
    String os = System.getProperty("os.name").toLowerCase();
    Class<?>[] parms = new Class<?>[0];
    Class<?> cl;
    Constructor<?> c;
    if (os.startsWith("windows")) {
      try {
        cl = Class.forName("gr.cti.eslate.utils.browser.SWT.IEBrowser");
        c = cl.getConstructor(parms);
        return (ESlateBrowser)(c.newInstance(new Object[0]));
      } catch (Throwable th) {
      }
    }
    try {
      cl = Class.forName("gr.cti.eslate.utils.browser.webWindow.WWBrowser");
      c = cl.getConstructor(parms);
      return (ESlateBrowser)(c.newInstance(new Object[0]));
    } catch (Throwable th) {
    }
    return new SwingBrowser();
  }
}
