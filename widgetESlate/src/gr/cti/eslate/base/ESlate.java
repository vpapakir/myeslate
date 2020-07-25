package gr.cti.eslate.base;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.registry.*;

/**
 * This class provides global E-Slate platform functionality.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ESlateHandle
 */
public class ESlate
{
  /**
   * The folders where components can look for native programs.
   */
  private static File[] NativeProgramFolders = new File[0];

  /**
   * The image that E-Slate frames should display in their minimized icons.
   */
  private static Image iconImage = null;
  /**
   * The 32x32 version of the E-Slate logo.
   */
  private static Image iconImage32 = null;

  /**
   * Indicates whether we have already checked the version of the Java VM
   * under which E-Slate is currently running.
   */
  private static boolean javaVersionKnown = false;

  /**
   * Indicates whether E-Slate is currently running under Java 2 or later.
   */
  private static boolean isJava2 = false;

  /*
   * Indicates whether E-Slate is currently running under Java 1.4 or later.
   */
  private static boolean isJava14 = false;

  /**
   * The global variable registry.
   */
  private static Registry globalRegistry = null;

  /**
   * The constructor is private, as there is no need to instantiate this
   * class.
   */
  private ESlate()
  {
  }

  /**
   * Registers an ESlate part with E-Slate.
   * @param     component       The ESlate part to register with E-Slate.
   * @return    A handle to to E-Slate, allowing providing the component with
   *            access to the functionality of E-Slate.
   * @exception NullPointerException    Thrown if component is null.
   */
  public static ESlateHandle registerPart(ESlatePart component)
  {
    /* For the time being, we do not differentiate between ESlate parts and
     * arbitrary components. However, we do provide the two different methods,
     * to force components to implement the ESlatePart interface.
     */
    return registerPart((Object)component);
  }

  /**
   * Registers an arbitrary component with E-Slate.
   * @param     component       The component to register with E-Slate.
   * @return    A handle to to E-Slate, allowing providing the component with
   *            access to the functionality of E-Slate.
   * @exception NullPointerException    Thrown if component is null.
   */
  public static ESlateHandle registerPart(Object component)
  {
    if (component == null) {
      throw new NullPointerException("Component must not be null");
    }
    return new ESlateHandle(component);
  }

  /**
   * Registers a null component with E-Slate. The actual component will be
   * filled in later by the platform.
   */
  static ESlateHandle registerPart()
  {
    return new ESlateHandle(null);
  }

  /**
   * Sets the folders where components can look for native programs.
   * @param     folders An array containing references to the folders where
   *                    components can look for native programs.
   * @exception IllegalArgumentException        Thrown if any of the specified
   *                    files is null, does not exist, or is not a directory.
   */
  public static void setNativeProgramFolders(File[] folders)
    throws IllegalArgumentException
  {
    int length = folders.length;
    ResourceBundle resources = ResourceBundle.getBundle(
      "gr.cti.eslate.base.ESlateResource", ESlateMicroworld.getCurrentLocale());
    for (int i=0; i<length; i++) {
      if (folders[i] == null) {
        throw new IllegalArgumentException(resources.getString("nullFolder"));
      }
      if (!(folders[i].exists())) {
        throw new IllegalArgumentException(
          resources.getString("folderNotExist1") +
          folders[i].getAbsolutePath() +
          resources.getString("folderNotExist2")
        );
      }
      if (!(folders[i].isDirectory())) {
        throw new IllegalArgumentException(
          resources.getString("folderNotDir1") +
          folders[i].getAbsolutePath() +
          resources.getString("folderNotDir2")
        );
      }
    }
    NativeProgramFolders = new File[length];
    System.arraycopy(folders, 0, NativeProgramFolders, 0, length);
  }

  /**
   * Returns the folders where components can look for native programs.
   * @return    An array containing references to the folders where
   *            components can look for native programs.
   */
  public static File[] getNativeProgramFolders()
  {
    int length = NativeProgramFolders.length;
    File[] folders = new File[length];
    System.arraycopy(NativeProgramFolders, 0, folders, 0, length);
    return folders;
  }

  /**
   * Searches the folders where components can look for native programs for a
   * specified program.
   * @param     programName     The name of the program for which to search.
   * @return    A reference to the first occurrence of the specified program
   *            in the list of folders where components can look for native
   *            programs.
   */
  public static File getNativeProgram(String programName)
  {
    int length = NativeProgramFolders.length;
    for (int i=0; i<length; i++) {
      File f = new File(NativeProgramFolders[i], programName);
      if (f.exists()) {
        return f;
      }
    }
    return null;
  }

  /**
   * Returns the image that E-Slate frames should display in their minimized
   * icons.
   * @return    The requested image.
   */
  public synchronized static Image getIconImage()
  {
    if (iconImage == null) {
      iconImage =
        new ImageIcon((ESlate.class).getResource("eslateLogo.gif")).getImage();
    }
    return iconImage;
  }

  /**
   * Returns the 32x32 version of the E-Slate logo.
   * @return    The requested image.
   */
  public synchronized static Image getIconImage32()
  {
    if (iconImage32 == null) {
      iconImage32 = new
        ImageIcon((ESlate.class).getResource("eslateLogo32.gif")).getImage();
    }
    return iconImage32;
  }
  
  /**
   * Checks whether E-Slate is running on a Java 2 JVM.
   * @return    True if running under a Java 2 (or later...) JVM, false if
   *            running under a Java 1.0 or 1.1 JVM.
   */
  public static boolean runningOnJava2()
  {
    if (!javaVersionKnown) {
      identifyJavaVersion();
    }
    return isJava2;
  }

  /**
   * Checks whether E-Slate is running on a Java 1.4 JVM.
   * @return    True if running under a Java 1.4 (or later...) JVM, false if
   *            running under a Java 1.0 or 1.1, 1.2, or 1.3 JVM.
   */
  public static boolean runningOnJava14()
  {
    if (!javaVersionKnown) {
      identifyJavaVersion();
    }
    return isJava14;
  }

  private static void identifyJavaVersion()
  {
/*
    // This is faster, but not 100% reliable. It works with Sun's and
    // Microsoft's JVM, as well as with JOVE, but, apparently, implementors
    // can put their own version strings in here: gcj 2.95.2 puts "2.95.0",
    // and kaffe 1.0.5 puts "1.0.5". Assuming that gcj is compatible with
    // the yet non-existent jdk 2.95, while kaffe is only 1.0 compatible is
    // wrong: gcj doesn't even implement the entire 1.0 jdk, while kaffe is
    // 1.1 compatible.
    String ver =  System.getProperty("java.version");
    if (ver.startsWith("1.0") || ver.startsWith("1.1")) {
      isJava2 = false;
    }else{
      isJava2 = true;
    }
*/
    // This is slower, but more reliable: check for a method that only
    // exists in Java 1.2 and above.
    try {
      Method m = Class.class.getMethod("getProtectionDomain");
      isJava2 = (m != null);
    } catch (NoSuchMethodException e) {
      isJava2 = false;
    }
    // Ditto for Java 1.4.
    try {
      Method m = JComponent.class.getMethod("getTransferHandler");
      isJava14 = (m != null);
    } catch (NoSuchMethodException e) {
      isJava14 = false;
    }
    javaVersionKnown = true;
  }

  /**
   * Returns a microworld-independent variable registry.
   * @return    The requested registry.
   */
  public static Registry getGlobalRegistry()
  {
    if (globalRegistry == null) {
      globalRegistry = new Registry();
    }
    return globalRegistry;
  }
}
