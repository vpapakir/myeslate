package gr.cti.eslate.utils.browser.SWT;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Augustine Grillakis
 */
public class BrowserPanel extends JPanel
{
  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;
  /**
   * The encapsulated browser.
   */
  private Browser browser;
  /**
   * SWT shell for browser component.
   */
  private Shell shell = null;
  /**
   * Canvas hosting browser component.
   */
  private Canvas canvas = null;

  public BrowserPanel()
  {
    super();
    initialize();
  }

  public void initialize()
  {
    initializeComponents();
    addComponentListener(new ComponentAdapter()
    {
      @Override
      public void componentResized(ComponentEvent e)
      {
        initializeBrowser();
        BrowserPanel.this.removeComponentListener(this);
      }
    });
  }
    
  private void initializeComponents()
  {
    setBorder(null);
    setOpaque(false);
  }
    
  public Browser getBrowser()
  {
    return browser;
  }
    
  public void initializeBrowser()
  {
    if (!SWTThread.started) {
      SWTThread thread = new SWTThread(this);
      thread.setPriority(Thread.NORM_PRIORITY);
      try {
        synchronized (this) {
          thread.start();
          wait();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    canvas = new Canvas();
    canvas.setMinimumSize(new Dimension(0, 0));
    setLayout(new BorderLayout());
    add(canvas, BorderLayout.CENTER);

    Display.getDefault().syncExec(new Runnable()
    {
      public void run()
      {
        shell = SWT_AWT.new_Shell(Display.getDefault(), canvas);
        shell.setLayout(new FillLayout());
        browser = new Browser(shell, SWT.NONE);
      }
    });
  }
    
  public void removeBrowser()
  {
    if (browser != null) {
      remove(canvas);
      canvas = null;
      shell = null;
      browser = null;
    }
  }
    
  public boolean isBrowserInitialized()
  {
    return (browser != null);
  }
    
}

class SWTThread extends Thread
{
  static boolean started;
  private Object caller;
	
  SWTThread(Object caller)
  {
    super("SWT thread");
    this.caller = caller;
  }

  public void run()
  {
    started = true;
    Display display = Display.getDefault();
    Shell shell = new Shell(display);

    //System.out.println("SWT started");
    synchronized (caller)
    {
      caller.notify();
    }

    do {
      try {
        if (!display.readAndDispatch()) {
          display.sleep ();
        }
      } catch (OutOfMemoryError err) {
        err.printStackTrace();
        // If an OutOfMemoryError occurs, then try to handle it and then try
        // to make the SWT work again...
        continue;
      }catch (Throwable thr) {
        thr.printStackTrace();
        continue;
      }
    } while (!shell.isDisposed());
  }
}
