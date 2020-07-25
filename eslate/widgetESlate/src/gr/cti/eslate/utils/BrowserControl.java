package gr.cti.eslate.utils;

import java.io.*;

/**
  * A simple, static class to display a URL in the system browser.
  * <P>
  * Under Unix, the system browser is hard-coded to be one of 'mozilla' or
  * 'netscape', which must be in your PATH for this to work.  This has been
  * tested with the following platforms: AIX, HP-UX and Solaris.
  * According to one tip, this will also work under MacOS.
  * <P>
  * Under Windows, this will bring up the default browser under windows,
  * usually either Netscape or Microsoft IE.  The default browser is
  * determined by the OS.  This has been tested under Windows 95/98/NT.
  * <P>
  * Examples:
  * <P>
  * <code>BrowserControl.displayURL("http://www.javaworld.com");</code>
  * <P>
  * <code>BrowserControl.displayURL("file://c:\\docs\\index.html");</code>
  * <P>
  * <code>BrowserContorl.displayURL("file:///user/joe/index.html");</code>
  * <P>
  * Note - you must include the url type -- either <code>"http://"</code> or
  * <code>"file://"</code>.
  *
  * @version    2.0.0, 18-May-2006
  * @author     Steven Spencer (JavaWorld tip #66).
  * @author     Kriton Kyrimis
  */
public class BrowserControl
{
  /**
   * Used to identify the windows platform.
   */
  private static final String WIN_ID = "Windows";
  /**
   * The default system browser under windows.
   */
  private static final String WIN_PATH = "rundll32";
  /**
   * The flag to display a URL.
   */
  private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
  /**
   * The first browser to try under unix.
   */
  static final String MOZILLA = "mozilla";
  /**
   * The second browser to try under unix.
   */
  static final String NETSCAPE = "netscape";
  /**
   * The first argument required to display a remote url under
   * netscape/mozilla.
   */
  private static final String UNIX_FLAG1 = "-remote";
  /**
   * The second argument required to display a remote url under
   * netscape/mozilla.
   */
  private static final String UNIX_FLAG2 = "openURL";

  /**
   * Display a file in the system browser.  If you want to display a
   * file, you must include the absolute path name.
   * @param     url     The file's URL (the URL must start with either
   *                    <code>"http://"</code> or <code>"file://"</code>).
   */
  public static void displayURL(String url)
  {
    boolean windows = isWindowsPlatform();
    if (windows) {
      try {
        // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
        //cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
        String[] cmd = new String[]{WIN_PATH, WIN_FLAG, url};
        Runtime.getRuntime().exec(cmd);
      } catch (IOException e2) {
        System.err.println("Cannot execute default browser.");
      }
    }else{
      // Display the URL in a thread, so that we do not block.
      NetscapeThread th = new NetscapeThread(url);
      th.start();
    }
  }

  /**
   * Show a URL in a netscape compatible browser (i.e., netscape or mozilla).
   * @param     prog    The name of the browser.
   * @param     url     The URL to display.
   * @exception IOException     Thrown if the specified browser cannot be
   *                            executed.
   */
  static void netscape(String prog, String url) throws Exception
  {
    // Under Unix, Mozilla/Netscape have to be running for the "-remote"
    // command to work.  So, we try sending the command and
    // check for an exit value.  If the exit command is 0,
    // it worked, otherwise we need to start the browser.
    // cmd = 'prog -remote openURL(http://www.javaworld.com)'
    String[] cmd = new String[]{prog, UNIX_FLAG1, UNIX_FLAG2 + "(" + url + ")"};
    Process p = Runtime.getRuntime().exec(cmd);
    // wait for exit code -- if it's 0, command worked,
    // otherwise we need to start the browser up.
    int exitCode = p.waitFor();
    if (exitCode != 0) {
      // Command failed, start up the browser
      // cmd = 'prog http://www.javaworld.com'
      //cmd = prog + " " + url;
      cmd = new String[]{prog, url};
      p = Runtime.getRuntime().exec(cmd);
    }
  }

  /**
   * Try to determine whether this application is running under Windows
   * or some other platform by examing the "os.name" property.
   * @return    true if this application is running under a Windows OS.
   */
  public static boolean isWindowsPlatform()
  {
    String os = System.getProperty("os.name");
    if ( os != null && os.startsWith(WIN_ID)) {
      return true;
    }else{
      return false;
    }
  }

//  /**
//   * Simple example.
//   */
//  public static void main(String[] args)
//  {
//    displayURL("http://www.javaworld.com");
//  }

}

/**
 * This class implements a thread that displays a URL using either mozilla or
 * netscape.
 */
class NetscapeThread extends Thread
{
  /**
   * The URL displayed by the thread.
   */
  private String url;

  /**
   * Construct a <code>NetscapeThread</code> instance.
   * @param     url     The URL to display.
   */
  NetscapeThread(String url)
  {
    super();
    this.url = url;
  }

  /**
   * Thread body: display the URL.
   */
  public void run()
  {
    try {
      BrowserControl.netscape(BrowserControl.MOZILLA, url);
    } catch (Exception e) {
      try {
        BrowserControl.netscape(BrowserControl.NETSCAPE, url);
      } catch (Exception e2) {
        System.err.println("Cannot execute mozilla or netscape.");
      }
    }
  }
}

/*
from Ryan Stevens:

For Mac users, here's an easy way to open a Web page in the default browser,
using MRJ 2.2:

import com.apple.mrj.MRJFileUtils;
import java.io.*;

class Open {
    String url = "http://www.yourpage.com/";

  public static void main(String[] args)
  {
    new Open();
  }

  Open()
  {
    try {
      MRJFileUtils.openURL(url);
    } catch (IOException ex) {}
  }
}


from Mark Weakly:


You can launch the command line stuff on Mac similar to Unix (MacOS 8 and 9),
except you must place the command-line tokens into a java.lang.String array.
The array gets passed to the process exec() method. For example:

  String[] commandLine = { "netscape", "http://www.javaworld.com/" };
  Process process = Runtime.getRuntime().exec(commandLine);
*/
