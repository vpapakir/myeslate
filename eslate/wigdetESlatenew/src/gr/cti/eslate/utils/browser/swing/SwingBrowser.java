package gr.cti.eslate.utils.browser.swing;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import gr.cti.eslate.utils.browser.*;

/**
 * This class implements a web browser based on Swing's JTextPane component,
 * using the standard ESlateBrowser interface.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
@SuppressWarnings(value={"unchecked"})
public class SwingBrowser extends ESlateBrowser implements HyperlinkListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The encapsulated JtextPane.
   */
  private JTextPane pane;
  /**
   * The scroll pane containing the JTextPane.
   */
  private JScrollPane paneSP;
  /**
   * A JLabel used to display image URLs, which JTextPane can't handle.
   */
  private JLabel label;
  /**
   * The scroll pane containing the JLabel.
   */
  private JScrollPane labelSP;
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
   * Indicates if the JTextPane is being shown, or if the JLabel is being
   * shown instead.
   */
  private boolean showingPane;
  /**
   * The default cursor.
   */
  private Cursor defaultCursor =
    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
  /**
   * The "busy" cursor.
   */
  private Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
  /**
   * Indicates whether the URL currently being visited was visited by clicking
   * on a link with the mouse.
   */
  private boolean followingLink = false;
  /**
   * A list of listeners added to the encapsulated browser.
   */
  private Vector listeners = new Vector();
  /**
   * A list of HistoryChangeListeners added to the browser.
   */
  private Vector historyListeners = new Vector();

  /**
   * Create a SwingBrowser instance.
   */
  public SwingBrowser()
  {
    super();
    pane = new JTextPane();
    pane.setEditable(false);
    setLayout(new BorderLayout());
    paneSP = new JScrollPane(pane);
    add(paneSP, BorderLayout.CENTER);
    showingPane = true;
    label = new JLabel();
    labelSP = new JScrollPane(label);
    pane.addHyperlinkListener(this);
  }

  /**
   * Sets the browser location to a new URL.
   * @param     s       The URL to visit.
   * @exception Exception       Thrown if the URL cannot be visited.
   */
  public void setCurrentLocation(String s) throws Exception
  {
    String newLocation = javaURL(s);
    if (newLocation.equals(getCurrentLocation())) {
      return;
    }
    Exception ex = null;
    try {
      setPage(newLocation);
    } catch (Exception e) {
      ex = e;
/*
      try {
        pane.read(badURLStream(newLocation), new HTMLDocument());
      } catch (Exception e2) {
        e2.printStackTrace();
      }
*/
      try {
        setPage(getClass().getResource("bad.html").toString());
      } catch (IOException ioe) {
      }
      
    }
    if (location != null) {
      backHistory.addElement(location);
    }
    location = newLocation;
    forwardHistory.removeAllElements();
    if (followingLink) {
      int n = listeners.size();
      for (int i=0; i<n; i++) {
        LinkFollowedListener l = (LinkFollowedListener)(listeners.elementAt(i));
        SwingLinkFollowedEvent ev = new SwingLinkFollowedEvent(newLocation);
        l.linkFollowed(ev);
      }
    }
    if (ex != null) {
      throw ex;
    }
  }

//  /**
//   * Returns an input stream providing an HTML page saying that a specified
//   * URL is bad. This method is not being used currently.
//   * @param   url     The bad url.
//   * @return  The requested input stream.
//   */
//  private InputStream badURLStream(String url)
//  {
//    try {
//      ByteArrayOutputStream bos = new ByteArrayOutputStream();
//      DataOutputStream dos = new DataOutputStream(bos);
//      dos.writeUTF("<HTML>\n");
//      dos.writeUTF("<HEAD>\n");
//      dos.writeUTF("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">\n");
//     dos.writeUTF("<TITLE>Cannot connect to " + url + "<TITLE>\n");
//      dos.writeUTF("</HEAD>\n");
//      dos.writeUTF("<BODY>\n");
//      dos.writeUTF("<STRONG>Cannot connect to " + url + "<STRONG>\n");
//
//      dos.writeUTF(url + "\n");
//      dos.writeUTF("</BODY>\n");
//      dos.writeUTF("</HTML>\n");
//     dos.flush();
//      byte[] b = bos.toByteArray();
//      dos.close();
//      ByteArrayInputStream bis = new ByteArrayInputStream(b);
//      return bis;
//    }catch (IOException ioe) {
//      ioe.printStackTrace();
//      return null;
//    }
//  }

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
    return getCurrentLocation();
  }

  /**
   * Visits the next URL in the browser's history.
   * @return    The next URL in the browser's history. This method does
   *            nothing if the browser is displaying the last URL in the
   *            browser's history.
   */
  public String forward()
  {
    if (forwardHistory.size() > 0) {
      int last = forwardHistory.size() - 1;
      String s = (String)(forwardHistory.elementAt(last));
      try {
        setPage(s);
      } catch (IOException e) {
      }
      if (location != null) {
        backHistory.addElement(location);
      }
      forwardHistory.removeElementAt(last);
      location = s;
    }
    return getCurrentLocation();
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
        setPage(s);
      } catch (IOException e) {
      }
      forwardHistory.addElement(location);
      backHistory.removeElementAt(last);
      location = s;
    }
    return getCurrentLocation();
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
   * Reload the currently displayed URL.
   */
  public void refresh()
  {
    try {
      setPage(getCurrentLocation());
    } catch (IOException e) {
    }
  }

  /**
   * Display a given URL. Image URLs (heuristically defined as those ending in
   * ".gif", "jpg", ".png", or ".bmp". regardless of case) are displayed in a
   * JLabel, all other URLs are displayed in a JTextpane.
   * @param     url     The URL to display.
   * @exception IOException     Thrown if the URL cannot be displayed.
   */
  private void setPage(String url) throws IOException
  {
    pane.setCursor(waitCursor);
    label.setCursor(waitCursor);
    String lCaseURL = url.toLowerCase();
    if (lCaseURL.endsWith(".gif") || lCaseURL.endsWith(".jpg") ||
        lCaseURL.endsWith(".png") || lCaseURL.endsWith(".bmp")) {
      if (showingPane) {
        remove(paneSP);
        add(labelSP, BorderLayout.CENTER);
        revalidate();
        showingPane = false;
      }
      try {
        label.setIcon(new ImageIcon(new URL(url)));
      } catch (Exception e) {
        pane.setCursor(defaultCursor);
        label.setCursor(defaultCursor);
        throw new IOException(e.getMessage());
      }
    }else{
      if (!showingPane) {
        remove(labelSP);
        add(paneSP, BorderLayout.CENTER);
        revalidate();
        showingPane = true;
      }
      pane.setPage(url);
    }
    historyChanged();
    paintImmediately(getVisibleRect());
    pane.setCursor(defaultCursor);
    label.setCursor(defaultCursor);
  }

  /**
   * Converts a URL to the form used by Java.
   * @param     url     The URL to convert.
   * @return    The converted URL.
   */
  private static String javaURL(String url)
  {
    if (url.startsWith("file:///")) {
      return "file:/" + url.substring(8).replace('\\', '/');
    }
    if (url.startsWith("file://")) {
      return "file:/" + url.substring(7).replace('\\', '/');
    }
    if (url.startsWith("file:/")) {
      return "file:/" + url.substring(6).replace('\\', '/');
    }
    return url;
  }

  /**
   * Process hyperlink events generated by the encapsulated JTextPane.
   * @param     e       The event to process.
   */
  public void hyperlinkUpdate(HyperlinkEvent e)
  {
    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
      try {
        followingLink = true;
        setCurrentLocation(e.getURL().toString());
      } catch (Exception ex) {
      }
      followingLink = false;
    }
  }

  /**
   * Adds a listener for events generated when a link is followed by clicking
   * on it with the mouse.
   * @param     l       The listener to add.
   */
  public void addLinkFollowedListener(LinkFollowedListener l)
  {
    if (!(listeners.contains(l))) {
      listeners.addElement(l);
    }
  }

  /**
   * Removes a listener for events generated when a link is followed by
   * clicking on it with the mouse.
   * @param     l       The listener to add.
   */
  public void removeLinkFollowedListener(LinkFollowedListener l)
  {
    if (listeners.contains(l)) {
      listeners.removeElement(l);
    }
  }

  /**
   * Checks whether the browser supports printing the current document.
   * @return    False.
   */
  public boolean supportsPrint()
  {
    return false;
  }

  /**
   * Checks whether the browser supports searching the current document for a
   * given text string.
   * @return    False.
   */
  public boolean supportsSearch()
  {
    return false;
  }

  /**
   * Returns the underlying browser implementation.
   * @return    An instance of class javax.swing.JTextPane.
   */
  public Object getBrowser()
  {
    return pane;
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

/**************************************************************************/

  /**
   * Stop loading the current URL. This method does not work.
   */
  public void stop()
  {
  }

  /**
   * Search for a given text in the displayed document. This method does not
   * work.
   * @param     prev    Result from the previous search, so that the new
   *                    search will continue from the last one.
   * @param     text    Text for which to search. It can be one or more words
   *                    separated by spaces.
   * @param     useCurrentFrame If this parameter is true the search will be
   *                    conducted in the currently selected html frame.
   *                    Otherwise the search will happen in the html frame
   *                    that has input focus (has been clicked upon).
   * @return    0
   */
  public int search(int prev, String text, boolean useCurrentFrame)
  {
    return 0;
  }

  /**
   * Get the font used to display text rendered in a proportional font.
   * @return    This method does not return a meaningful result.
   */
  public Font getProportionalFont()
  {
    return pane.getFont();
  }

  /**
   * Sets the font used to display text rendered in a proportional font.
   * This method does not work.
   * @param     f       The font to set.
   */
  public void setProportionalFont(Font f)
  {
    pane.setFont(f);
  }

  /**
   * Print the displayed document. This method does not work.
   * @param     f       The frame in which the browser is contained.
   * @param     title   Title to use for the print job.
   */
  public void printDoc(Frame f, String title)
  {
  }

}
