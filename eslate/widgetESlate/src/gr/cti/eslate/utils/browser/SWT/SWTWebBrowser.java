package gr.cti.eslate.utils.browser.SWT;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleEvent;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.OleListener;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Encapsulates a SWT browser into a <code>JPanel</code>.
 * Overwrites the encapsulated SWT browser's <code>setUrl</code> method
 * because of the need to disable temporarily the location listener.
 *
 * @version     2.0.18, 5-Jul-2007
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class SWTWebBrowser extends JPanel
{
  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Canvas hosting SWT browser.
   */
  private Canvas canvas;
  /**
   *  SWT display device.
   */
  private Display display;
  /**
   * SWT browser.
   */
  private Browser browser = null;
  /**
   * true, if the SWT side of the browser has concluded initialization.
   */
  private boolean swtSideInitialized = false;
  /**
   * True while the SWT part of the browser is being initialized.
   */
  private boolean swtSideBeingInitialized = false;
  /**
   * The list of actions which could not be executed because the browser was
   * being initialized when they were invoked. These actions are saved in
   * this list and get executed as soon as the browser finishes initialization.
   */
  private ArrayList<AbstractAction> pendingActionList =
    new ArrayList<AbstractAction>();
  /**
   * The list of location listeners that were added before the encapsulated
   * SWT browser was created. These listeners are saved in this list and are
   * added to the encapsulated browser when it is created.
   */
  private ArrayList<LocationListener> pendingLocationListeners = null;

  /**
   * Create a new SWT web browser.
   */
  public SWTWebBrowser()
  {
    super();
    display = Display.getDefault();

    initialize();
  }

  /**
   * Initialize.
   */
  private void initialize()
  {
    initializeComponents();
    layoutComponents();
    attachListeners();
  }

  /**
   * Initialize components.
   */
  private void initializeComponents()
  {
    canvas = new Canvas();
  }

  /**
   * Layout components.
   */
  private void layoutComponents()
  {
    setLayout(new BorderLayout());
    add(canvas, BorderLayout.CENTER);
  }

  /**
   * Attach listeners.
   */
  private void attachListeners()
  {
    addAncestorListener(new AncestorListener()
    {
      public void ancestorMoved(AncestorEvent event)
      {
      }
      public void ancestorRemoved(AncestorEvent event)
      {
      }
      public void ancestorAdded(AncestorEvent event)
      {
        //System.out.println("ancestorAdded....");
        if (swtSideInitialized || swtSideBeingInitialized) {
          return;
        }
        swtSideBeingInitialized = true;
        display.syncExec(new Runnable()
        {
          public void run()
          {
            Shell shell = SWT_AWT.new_Shell(display, canvas);
            shell.setLayout(new FillLayout());
            try {
              browser = new Browser(shell, SWT.NONE);
              SWTWebBrowser.this.revalidate();
              Point size = shell.computeSize (SWT.DEFAULT, SWT.DEFAULT);
              canvas.setSize(size.x + 2, size.y + 2);
              swtSideInitialized = true;
              swtSideBeingInitialized = false;
              SwingUtilities.invokeLater(new Runnable()
              {
                public void run()
                {
                  for (int i=0; i<pendingActionList.size(); i++) {
                    //System.out.println(
                    //  "Executing action: " + pendingActionList.get(i)
                    //);
                    pendingActionList.get(i).actionPerformed(
                      new ActionEvent(SWTWebBrowser.this, i, "nothing")
                    );
                  }
                  pendingActionList.clear();
                }
              });
              if (pendingLocationListeners != null) {
                int n = pendingLocationListeners.size();
                for (int i=0; i<n; i++) {
                  browser.addLocationListener(pendingLocationListeners.get(i));
                }
                pendingLocationListeners = null;
              }
            } catch (SWTError e) {
              e.printStackTrace();
            }
          }
        });
      }
    });
  }

  /**
   * Loads a URL.
   * Overwrites the encapsulated SWT browser's <code>setUrl</code> method
   * because of the need to disable temporarily the location listener.
   * @param     url     The URL to be loaded.
   */
  public void setURL(final URL url)
  {
    if (!swtSideInitialized) {
      SetURLAction action = new SetURLAction(url);
      pendingActionList.add(action);
      return ;
    };
    if (url == null) {
      return ;
    }
    display.syncExec(new Runnable()
    {
      public void run()
      {
        browser.setUrl(url.toString());
      }
    });
  }

  class SetURLAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    URL url = null;

    SetURLAction(URL url)
    {
      this.url = url;
    }

    public void actionPerformed(ActionEvent e)
    {
      setURL(url);
    }
  }

  /**
   * @see IBrowser#setURL(String)
   * @param url
   */
  public void setURL(String url)
  {
    try {
      URL u = new URL(url);
      setURL(u);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Renders HTML.
   * Overwrites the encapsulated SWT browser's <code>setText</code> method
   * because of the need to disable temporarily the location listener.
   * @param htmlContent the HTML content to be rendered
   */
  public void setContent(final String htmlContent)
  {
    if (!swtSideInitialized) {
      SetContentAction action = new SetContentAction(htmlContent);
      pendingActionList.add(action);
      return ;
    };
    display.syncExec(new Runnable()
    {
      public void run()
      {
        if (htmlContent == null) {
          browser.setText("");
        }else{
          browser.setText(htmlContent);
        }
      }
    });
  }

  class SetContentAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    String htmlContent = null;

    SetContentAction(String htmlContent)
    {
      this.htmlContent = htmlContent;
    }

    public void actionPerformed(ActionEvent e)
    {
      setContent(htmlContent);
    }
  }

  /**
   * Execute the specified script.
   *
   * Execute a script containing javascript commands in the context of the
   * current document. 
   * @param javaScript the script with javascript commands
   * @exception IllegalArgumentException
   *            <ul>
   *            <li>ERROR_NULL_ARGUMENT - if the script is null</li>
   *            </ul>
   * @exception SWTException
   *    <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
   *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
   *    </ul>
   */
  public boolean executeScript(final String javaScript)
  {
    if (!swtSideInitialized) {
      ExecuteScriptAction action = new ExecuteScriptAction(javaScript);
      pendingActionList.add(action);
      return false;
    }
    ExecuteScriptRunnable runnable = new ExecuteScriptRunnable(javaScript);
    display.syncExec(runnable);
    return runnable.executed;
  }

  class ExecuteScriptRunnable implements Runnable
  {
    boolean executed = false;
    String JSscript = null;

    ExecuteScriptRunnable(String JSscript)
    {
      this.JSscript = JSscript;
    }

    public void run()
    {
      if (JSscript == null) {
        executed = true;
        return ;
      }
      executed = browser.execute(JSscript);
      //System.out.println("executed? " + executed);
    }
  }

  class ExecuteScriptAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    private static final int MAX_RETRIES = 8;
    String script = null;
    int numOfRetries = 0;

    ExecuteScriptAction(String script)
    {
      this.script = script;
    }

    public void actionPerformed(ActionEvent e)
    {
      execute();
    }

    public void execute()
    {
      numOfRetries++;
      if (numOfRetries > MAX_RETRIES) {
        return;
      }
      if (!executeScript(script)) {
        try {
          Thread.sleep(500);
          execute();
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      }
    }
  }

  /**
   * Navigate to the previous session history item.
   * @exception SWTError
   *    <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
   *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
   *    </ul>
   * @see #forward
   */
  public void back()
  {
    if (!swtSideInitialized) {
      BackAction action = new BackAction();
      pendingActionList.add(action);
      return;
    }
    display.syncExec(new Runnable()
    {
      public void run()
      {
        browser.back();
      }
    });
  }

  class BackAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e)
    {
      back();
    }
  }

  /**
   * Navigate to the next session history item.
   * @exception SWTError
   *    <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
   *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
   *    </ul>
   * @see #back
   */
  public void forward()
  {
    if (!swtSideInitialized) {
      ForwardAction action = new ForwardAction();
      pendingActionList.add(action);
      return ;
    }
    display.syncExec(new Runnable()
    {
      public void run()
      {
        browser.forward();
      }
    });
  }

  class ForwardAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    public void actionPerformed(ActionEvent e)
    {
      forward();
    }
  }

  /**
   * Print browser contents.
   */
  public void print()
  {
    if (!swtSideInitialized) {
      PrintAction action = new PrintAction();
      pendingActionList.add(action);
      return ;
    }
    display.syncExec(new Runnable()
    {
      public void run()
      {
        browser.execute("window.print();");
      }
    });
  }

  class PrintAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    public void actionPerformed(ActionEvent e)
    {
      print();
    }
  }

  /**
   * Print browser contents.
   * @param showStdSetupDialog True for showing the setup dialog.
   */
  public void print(final boolean showStdSetupDialog)
  {
    if (!swtSideInitialized) {
      PrintSetupDialogAction action =
        new PrintSetupDialogAction(showStdSetupDialog);
      pendingActionList.add(action);
      return ;
    }
    display.syncExec(new Runnable()
    {
      public void run()
      {
        final Shell shell = new Shell();
        OleFrame oleFrame = new OleFrame(shell, SWT.NONE);
        OleControlSite oleControlSite =
          new OleControlSite(oleFrame, SWT.NONE, "Shell.Explorer");
        oleControlSite.doVerb(OLE.OLEIVERB_UIACTIVATE);
        final OleAutomation oleAutomation =
          new OleAutomation(oleControlSite);

        // Set WebBrowser control offline
        int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Offline"});
        oleAutomation.setProperty(rgdispid[0], new Variant(true));

        // Navigate
        rgdispid = oleAutomation.getIDsOfNames(new String[]{"Navigate"});
        int dispIdMember = rgdispid[0];
        Variant[] rgvarg = new Variant[1]; // this is the URL parameter
        rgvarg[0] = new Variant(browser.getUrl());
        oleAutomation.invoke(dispIdMember, rgvarg);

        // Listen for changes to the ready state
        rgdispid = oleAutomation.getIDsOfNames(new String[]{"ReadyState"});
        final int READYSTATE = rgdispid[0];
        oleControlSite.addPropertyListener(READYSTATE, new OleListener()
        {
          public void handleEvent(OleEvent event)
          {
            if (event.detail == OLE.PROPERTY_CHANGED) {
              Variant state = oleAutomation.getProperty(READYSTATE);
              //System.out.println("Web State changed to " + state.getInt());

              //READYSTATE_COMPLETE = 4
              if (state.getInt() == 4) {
                // Print
                int[] rgdispid = oleAutomation.getIDsOfNames(
                  new String[]{"ExecWB", "cmdID", "cmdexecopt"}
                );
                Variant[] rgvarg = new Variant[2];

                //OLECMDID_PRINT is const from MS headers
                rgvarg[0] = new Variant(OLE.OLECMDID_PRINT);

                //OLECMDEXECOPT_PROMPTUSER = 1
                //OLECMDEXECOPT_DONTPROMPTUSER = 2
                rgvarg[1] = new Variant(
                  showStdSetupDialog ?
                    OLE.OLECMDEXECOPT_PROMPTUSER :
                    OLE.OLECMDEXECOPT_DONTPROMPTUSER
                  );

                int[] rgdispidNamedArgs = new int[2];
                rgdispidNamedArgs[0] = rgdispid[1];
                rgdispidNamedArgs[1] = rgdispid[2];
                oleAutomation.invoke(
                  rgdispid[0], rgvarg, rgdispidNamedArgs
                );

                shell.dispose();
              }
            }
          }
        });
      }
    });
  }

  class PrintSetupDialogAction extends AbstractAction
  {
    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    boolean showDialog = false;

    PrintSetupDialogAction(boolean showDialog)
    {
      this.showDialog = showDialog;
    }

    public void actionPerformed(ActionEvent e)
    {
      print(showDialog);
    }
  }

  /**
    * Get displayed URL.
    * @return   Displayed URL.
  */
  public URL getURL()
  {
    if (!swtSideInitialized) {
      return null;
    }
    GetURLRunnable runnable = new GetURLRunnable(null);
    display.syncExec(runnable);
    URLWrapper urlWrapper = runnable.getURLWrapper();
    if (urlWrapper == null) {
      return null;
    //}else{
    //  System.out.println(urlWrapper.url);
    }
    return urlWrapper.url;
  }

  /**
   * A simple URL-wrapper class.
   */
  class URLWrapper
  {
    URL url = null;
  }

  /**
   * The runnable which gets executed to acquire the SWT browser's URL.
   */
  class GetURLRunnable implements Runnable
  {
    URLWrapper urlWrapper = null;

    GetURLRunnable(URLWrapper urlWrapper)
    {
      if (urlWrapper == null)
        urlWrapper = new URLWrapper();
      this.urlWrapper = urlWrapper;
    }

    public void run()
    {
      try {
        //System.out.println("browser.getUrl(): " + browser.getUrl());
        String url = browser.getUrl();
        if (url != null && url.trim().length() != 0) {
          urlWrapper.url = new URL(url);
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }

    URLWrapper getURLWrapper()
    {
      return urlWrapper;
    }
  }

  /**
   * Get the SWT <code>Browser</code>.
   * @return The SWT <code>Browser</code>.
   */
  public Browser getBrowser()
  {
    return browser;
  }

  /**
   * Adds a location listener to the encapsulated SWT browser.
   * If the encapsulated SWT browser has not been created yet, then the
   * addition is deferred until the browser is created.
   * @param     l       The listener to add.
   */
  public void addLocationListener(LocationListener l)
  {
    if (browser != null) {
      browser.addLocationListener(l);
    }else{
      if (pendingLocationListeners == null) {
        pendingLocationListeners = new ArrayList<LocationListener>();
      }
      pendingLocationListeners.add(l);
    }
  }

}
