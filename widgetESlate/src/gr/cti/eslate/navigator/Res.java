package gr.cti.eslate.navigator;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import java.net.URL;
import java.net.MalformedURLException;
import gr.cti.eslate.base.ESlateHandle;
import java.io.File;

/**
 * Resource handling routines here (based on Res class of Canvas project.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
  public class Res
  {
    // 26Jun2000: since we'll always check it at startup made it a static
    // field instead of a function, so that we can use it often at the
    // "NavigatorClassPropertyEditor"
    public final static boolean isMSJavaVM =
      (((String)System.getProperty("java.vendor")).indexOf("Microsoft") != -1);

    //GREEK// //7-1-1999: made static
    transient static protected ResourceBundle m;

    public static String[] localizeArray(String s)
    {
      try {
        return m.getStringArray(s);
      } catch(Exception e) {
      return null;
    }
  }

  public static String localize(String s) //7-1-1999: made static
  {
    try {
      return m.getString(s);
    } catch(Exception e) {
      //System.out.println("Couldn't localize " + s);
      return s;
    }
  }

  // 7-1-1999: made this static: localize(...) is used at variables'
  // initialization that are set before the constructor gets called
  static {
    try {
      m = ResourceBundle.getBundle(
        "gr.cti.eslate.navigator.MessagesBundle", Locale.getDefault()
      );
    } catch(/*MissingResource*/Exception e) {
      // Catching all Exceptions because in MS-JVM some "bad path" is thrown
      // when a ResourceBundle is missing.
      System.err.println("Couldn't find messages resource");
    } // If localized bundle is not found it shall load MessagesBundle.class
  }

  // Routines...

  public static ImageIcon loadImageIcon(String filename, String description)
  {
    try {
      URL u = Res.class.getResource(filename);
      //System.out.println(u);
      if (u != null) {
        return new ImageIcon(u, description);
      } else {
        return null;
      }
    } catch(Exception e) {
      // Catching Exception thrown by the MS-JVM when filename not found
      // in .jar.
      return null;
    }
  }

  /////////

  // 15Mar2000: got from TV
  private static boolean isExistingAbsoluteFilepath(String s)
  {
    File f = new java.io.File(s);
    // Check this first, it should be faster than the f.exists check because
    // it doesn't really access the native filesystem.
    return (f.isAbsolute() && f.exists());
  }

  // 15Mar2000: got from TV. Try to prepend a "file:///" if the string passed
  // in isn't a URL.
  public static URL makeURL(ESlateHandle handle,String s)
  {
    URL url;
    try {
      url = new URL(s);
    } catch(MalformedURLException e) {
      try {
        if (isExistingAbsoluteFilepath(s)) {
          url = new URL("file:///" + s);
        }else{
          // 7Mar2000: If not passed in an absolute path to an existing local
          // file, assume this is a path relative to the document base URL.
          url = new URL(handle.getESlateMicroworld().getDocumentBase(), s);
        }
      } catch(Exception ex) {
        url = null;
      }
    }
    //System.out.println(url);
    return url;
  }

  /**
   * Return the resource bundle used uinternally by this class.
   * @return    The requested resource bundle.
   */
  public static ResourceBundle getResourceBundle()
  {
    return m;
  }
}
