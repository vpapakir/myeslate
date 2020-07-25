package gr.cti.eslate.utils.browser.SWT;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

import gr.cti.eslate.utils.browser.*;

import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;

/**
 * This class encapsulates the behavior of the Internet Explorer, providing a
 * standard interface independent of the actual implementation.
 *
 * @author	Kriton Kyrimis
 * @version	2.0.20, 12-Sep-2007
 */
public class IEBrowser extends ESlateBrowser implements LinkFollowedListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The encapsulated browser.
   */
  private SWTWebBrowser browser;
  /**
   * The back history.
   */
  private Vector<String> backHistory = new Vector<String>();
  /**
   * The forward history.
   */
  private Vector<String> forwardHistory = new Vector<String>();
  /**
   * Indicates whether listeners have been added to the encapsulated browser.
   */
  private boolean listenersAdded = false;
  /**
   * A list of listeners added to the encapsulated browser.
   */
  private Vector<Object> listeners = new Vector<Object>();
  /**
   * A list of listeners listening for history changed events.
   */
  private Vector<HistoryChangeListener> historyListeners =
    new Vector<HistoryChangeListener>();
  /**
   * Indicates whether the URL currently being visited was visited by clicking
   * on a link with the mouse.
   */
  boolean followingLink = true;
  /**
   * The URL of the location currently being displayed.
   */
  private String location = null;
  /**
   * SWT display for browser component.
   */
  static Display display = null;

  /**
   * Acquiring this semaphore means that <code>display</code> has been
   * initialized by calling <code>Display.getDefault()</code>.
   */
  static Semaphore createDisplay = new Semaphore(1);

  static {
    // Prevent threads other than the SWT dispatch loop from invoking
    // Display.getDefault().
    createDisplay.acquireUninterruptibly();
    Thread th = new Thread("SWT dispatch loop")
    {
      public void run()
      {
        display = Display.getDefault();
        new Shell(display);
        // Threads other than the SWT dispatch loop can now invoke
        // Display.getDefault();
        createDisplay.release();
        for(;;) {
          if (!display.readAndDispatch()) {
            display.sleep();
          }
        }
      }
    };
    th.start();
  }

  /**
   * Create an IEBrowser instance.
   */
  public IEBrowser()
  {
    super();
    setLayout(new BorderLayout());
    addHierarchyListener(new HierarchyListener()
    {
      public void hierarchyChanged(HierarchyEvent e)
      {
        Object f =
          SwingUtilities.getAncestorOfClass(Frame.class, IEBrowser.this);
        if (f != null) {
          IEBrowser.this.removeHierarchyListener(this);
          // The SWTWebBrowser constructor will invoke Display.getDefault(),
          // so make sure that this is allowed.
          createDisplay.acquireUninterruptibly();
          // Release the semaphore, so that subsequent invocations of the
          // IEBrowser constructor do not block.
          createDisplay.release();
          browser = new SWTWebBrowser();
          add(browser, BorderLayout.CENTER);
          revalidate();
          IEBrowser.this.addLinkFollowedListener(IEBrowser.this);
        }
      }
    });
  }

  /**
   * Sets the browser location to a new URL.
   * @param	s	The URL to visit.
   * @exception	Exception	Thrown if the URL cannot be visited.
   */
  public void setCurrentLocation(String s) throws Exception
  {
    // Listeners must be added here. If we add them during initialization, or
    // even in the addNotify method, they don't work!
    if (!listenersAdded) {
      display.syncExec(new Runnable()
      {
        public void run()
        {
          int n = listeners.size();
          for (int i=0; i<n; i++) {
            Object l = listeners.elementAt(i);
            if (l instanceof LocationListener) {
              //browser.getBrowser().addLocationListener((LocationListener)l);
              // browser.getBrowser() returns null, as the encapsulated
              // SWT browser has nott been created yet. Instead of adding the
              // listener to the encapsulated browser, add it to the wrapper,
              // who will make sure that the listener will be added when the
              // browser is created.
              browser.addLocationListener((LocationListener)l);
            }else{
              if (l instanceof StatusTextListener) {
                browser.getBrowser().addStatusTextListener((StatusTextListener)l);
              }else{
                if (l instanceof TitleListener) {
                  browser.getBrowser().addTitleListener((TitleListener)l);
                }
              }
            }
          }
        }
      });
    }
    listenersAdded = true;
    String newLocation = IEURL(s);
    if (newLocation.equals(getCurrentLocation())) {
      return;
    }
    notFollowingLink();
    Exception ex = null;
    try {
      browser.setURL(s);
    } catch (Exception e) {
      ex = e;
    }
    if (location != null) {
      backHistory.addElement(location);
    }
    location = newLocation;
    forwardHistory.removeAllElements();
    historyChanged();
    if (ex != null) {
      throw ex;
    }
  }

  /**
   * Returns the URL currently being displayed by the browser.
   * @return	The requested URL.
   */
  public String getCurrentLocation()
  {
    return location;
  }

  /**
   * Marks any IEListeners added to the encapsulated browser so that they know
   * that the link being shown was not visited by clicking on a link with the
   * mouse.
   */
  private void notFollowingLink()
  {
    int n = listeners.size();
    for (int i=0; i<n; i++) {
      Object listener = listeners.elementAt(i);
      if (listener instanceof IEListener) {
        ((IEListener)listener).followingLink.setValue(false);
      }
    }
  }

  /**
   * Invokes the history changed listeners attached to the browser.
   */
  @SuppressWarnings("unchecked")
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
   * Adds a listener for history changed events.
   * @param	l	The listener to add.
   */
  public void addHistoryChangeListener(HistoryChangeListener l)
  {
    if (!(historyListeners.contains(l))) {
      historyListeners.addElement(l);
    }
  }

  /**
   * Removes a listener for history changed events.
   * @param	l	The listener to remove.
   */
  public void removeHistoryChangeListener(HistoryChangeListener l)
  {
    if (historyListeners.contains(l)) {
      historyListeners.removeElement(l);
    }
  }

  /**
   * Sets the browser location to the "home" URL.
   * @return	The home URL, as specified in Internet Explorer's Internet
   * options.
   */
  public String home()
  {
    String location = null;
    notFollowingLink();
    try {
      //browser.GoHome();
      //browser.setUrl("http://E-Slate.cti.gr/");
      browser.setURL("about:home");
    } catch (Exception e) {
    }
    String newLocation;
    try {
      newLocation = browser.getURL().toString();
    } catch (Exception e) {
      newLocation = "";
    }
    if (location != null) {
      backHistory.addElement(location);
    }
    location = newLocation;
    forwardHistory.removeAllElements();
    historyChanged();
    return getCurrentLocation();
  }

  /**
   * Visits the next URL in the browser's history.
   * @return	The next URL in the browser's history. This method does
   *		nothing if the browser is displaying the last URL in the
   *		browser's history.
   */
  public String forward()
  {
    if (forwardHistory.size() > 0) {
      int last = forwardHistory.size() - 1;
      String s = forwardHistory.elementAt(last);
      notFollowingLink();
      try {
	browser.forward();
      } catch (Exception e) {
      }
      if (location != null) {
	backHistory.addElement(location);
      }
      forwardHistory.removeElementAt(last);
      location = s;
    }
    historyChanged();
    return getCurrentLocation();
  }

  /**
   * Visits the previous URL in the browser's history.
   * @return	The previous URL in the browser's history.  This method does
   *		nothing if the browser is displaying the first URL in the
   *		browser's history.
   */
  public String back()
  {
    if (backHistory.size() > 0) {
      int last = backHistory.size() - 1;
      String s = backHistory.elementAt(last);
      notFollowingLink();
      try {
	browser.back();
      } catch (Exception e) {
      }
      forwardHistory.addElement(location);
      backHistory.removeElementAt(last);
      location = s;
    }
    historyChanged();
    return getCurrentLocation();
  }

  /**
   * Returns the URLs that were visited before the URL that is currently
   * being displayed.
   * @return	A vector containing the requested URLs.
   */
  @SuppressWarnings("unchecked")
  public Vector getBackHistory()
  {
    return backHistory;
  }

  /**
   * Returns the URLs that were visited after the URL that is currently
   * being displayed.
   * @return	A vector containing the requested URLs.
   */
  @SuppressWarnings("unchecked")
  public Vector getForwardHistory()
  {
    return forwardHistory;
  }

  /**
   * Stop loading the current URL. This method will work only if the
   * underlying browser implementation supports this functionality.
   */
  public void stop()
  {
    display.syncExec(new Runnable()
    {
      public void run()
      {
        try {
          browser.getBrowser().stop();
        } catch (Exception e) {
        }
      }
    });
  }

  /**
   * Reload the currently displayed URL.
   */
  public void refresh()
  {
    display.syncExec(new Runnable()
    {
      public void run()
      {
        try {
          browser.getBrowser().refresh();
        } catch (Exception e) {
        }
      }
    });
  }

  /**
   * Print the displayed document.
   * @param	f	The frame in which the browser is contained (ignored).
   * @param	title	Title to use for the print job (ignored).
   */
  public void printDoc(Frame f, String title)
  {
    try {
      browser.print();
    } catch (Exception e) {
    }
  }

  /**
   * Adds a listener for events generated when a link is followed by clicking
   * on it with the mouse.
   * @param	l	The listener to add.
   */
  public void addLinkFollowedListener(LinkFollowedListener l)
  {
    LocationListener d = findLocationListener(l);
    if (d == null) {
      final IEListener listener = new IEListener(l);
      listeners.addElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().addLocationListener(listener);
          }
        });
      }
    }
  }

  /**
   * Removes a listener for events generated when a link is followed by
   * clicking on it with the mouse.
   * @param	l	The listener to remove.
   */
  public void removeLinkFollowedListener(LinkFollowedListener l)
  {
    final LocationListener d = findLocationListener(l);
    if (d != null) {
      listeners.removeElement(d);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().removeLocationListener(d);
          }
        });
      }
    }
  }

  /**
   * Finds the LocationListener corresponding to a given LinkFollowedListener.
   * @param	l	The LinkFollowedListener.
   * @return	The corresponding LocationListener or null if there
   *		is no corresponding LocationListener.
   */
  private LocationListener findLocationListener(LinkFollowedListener l)
  {
    int n = listeners.size();
    for (int i=0; i<n; i++) {
      Object d = listeners.elementAt(i);
      if (d instanceof IEListener) {
        IEListener iel = (IEListener)d;
	if (iel.getLinkFollowedListener().equals(l)) {
	  return iel;
	}
      }
    }
    return null;
  }

  /**
   * Adds a LocationListener to the encapsulated browser, making sure
   * that the addition is not ignored.
   * @param	listener	The listener to add.
   */
  public void addLocationListener(final LocationListener listener)
  {
    if (!(listeners.contains(listener))) {
      listeners.addElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().addLocationListener(listener);
          }
        });
      }
    }
  }

  /**
   * Removes a LocationListener to the encapsulated browser, making
   * sure that the addition is not ignored.
   * @param	listener	The listener to remove.
   */
  public void removeLocationListener(final LocationListener listener)
  {
    if (listeners.contains(listener)) {
      listeners.removeElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().removeLocationListener(listener);
          }
        });
      }
    }
  }

  /**
   * Adds a StatusTextListener to the encapsulated browser, making sure
   * that the addition is not ignored.
   * @param	listener	The listener to add.
   */
  public void addStatusTextListener(final StatusTextListener listener)
  {
    if (!(listeners.contains(listener))) {
      listeners.addElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().addStatusTextListener(listener);
          }
        });
      }
    }
  }

  /**
   * Removes a StatusTextListener to the encapsulated browser, making
   * sure that the addition is not ignored.
   * @param	listener	The listener to remove.
   */
  public void removeStatusTextListener(final StatusTextListener listener)
  {
    if (listeners.contains(listener)) {
      listeners.removeElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().removeStatusTextListener(listener);
          }
        });
      }
    }
  }

  /**
   * Adds a TitleListener to the encapsulated browser, making sure
   * that the addition is not ignored.
   * @param	listener	The listener to add.
   */
  public void addTitleListener(final TitleListener listener)
  {
    if (!(listeners.contains(listener))) {
      listeners.addElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            browser.getBrowser().addTitleListener(listener);
          }
        });
      }
    }
  }

  /**
   * Removes a TitleListener to the encapsulated browser, making
   * sure that the addition is not ignored.
   * @param	listener	The listener to remove.
   */
  public void removeTitleListener(final TitleListener listener)
  {
    if (listeners.contains(listener)) {
      listeners.removeElement(listener);
      if (listenersAdded) {
        display.syncExec(new Runnable()
        {
          public void run()
          {
              browser.getBrowser().removeTitleListener(listener);
          }
        });
      }
    }
  }

  /**
   * Invoked when a link is followed by clicking on it with the mouse, to
   * ensure that the history is updated.
   * @param	e	The event to process.
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

  /**
   * Converts a URL to the form used by Internet Explorer.
   * @param	url	The URL to convert.
   * @return	The converted URL.
   */
  private static String IEURL(String url)
  {
    if (url.startsWith("file:///")) {
      return url.replace('\\', '/');
    }
    if (url.startsWith("file://")) {
      return "file:///" + url.substring(7).replace('\\', '/');
    }
    if (url.startsWith("file:/")) {
      return "file:///" + url.substring(6).replace('\\', '/');
    }
    return url;
  }

  /**
   * Checks whether the browser supports printing the current document.
   * @return	True.
   */
  public boolean supportsPrint()
  {
    return true;
  }

  /**
   * Checks whether the browser supports searching the current document for a
   * given text string.
   * @return	False.
   */
  public boolean supportsSearch()
  {
    return false;
  }

/*------------------------------------------------------------------------*/

  /**
   * Search for a given text in the displayed document. This method does
   * nothing.
   * @param	prev	Result from the previous search, so that the new
   *			search will continue from the last one.
   * @param	text	Text for which to search. It can be one or more words
   *			separated by spaces.
   * @param	useCurrentFrame	If this parameter is true the search will be
   *			conducted in the currently selected html frame.
   *			Otherwise the search will happen in the html frame
   *			that has input focus (has been clicked upon).
   * @return	0
   */
  public int search(int prev, String text, boolean useCurrentFrame)
  {
    return 0;
  }

  // We can't implement these two under IE: all we can do is change the font
  // size in a scale of 0 to 4, but since this will affect the text size of IE
  // in general, we can't do even that.

  /**
   * Get the font used to display text rendered in a proportional font.
   * @return	This method does not return a meaningful result.
   */
  public Font getProportionalFont()
  {
    return getFont();
  }

  /**
   * Sets the font used to display text rendered in a proportional font.
   * This method does not work.
   * @param	f	The font to set.
   */
  public void setProportionalFont(Font f)
  {
    setFont(f);
  }

  /**
   * Returns the underlying browser implementation.
   * @return	An instance of class gr.cti.eslate.utils.browser.SWTWebBrowser.
   */
  public Object getBrowser()
  {
    return browser;
  }

}
