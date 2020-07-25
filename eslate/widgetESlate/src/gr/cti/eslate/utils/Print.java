package gr.cti.eslate.utils;

import java.io.*;
import java.text.*;
import java.util.*;

import gr.cti.eslate.base.*;

/**
 * Utilities for printing.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class Print
{
  /**
   * The full path of the executable for printing images.
   */
  private static String imagePrinter = null;

  /**
   * The name of the executable for printing images.
   */
  private final static String IMAGE_PRINTER_WINDOWS = "vbPrintImage.exe";

  /**
   * Localized resources.
   */
  private static ResourceBundle resources = null;

  /**
   * The constructor is private, as only static methods are provided.
   */
  private Print()
  {
  }

  /**
   * Prints an image file. Printing is done using a native executable.
   * @param     fileName        The name of the file to print.
   * @param     top             The margin, in cm, at the top of the
   *                            image. Ignored if <code>centerOnPage ==
   *                            true</code> or <code>fitToPage == true</code>.
   * @param     left            The margin, in cm, at the left of the
   *                            image. Ignored if <code>centerOnPage ==
   *                            true</code> or <code>fitToPage == true</code>.
   * @param     bottom          The margin, in cm, at the bottom of the
   *                            image. Ignored if <code>centerOnPage ==
   *                            true</code> or <code>fitToPage == true</code>.
   * @param     right           The margin, in cm, at the right of the
   *                            image. Ignored if <code>centerOnPage ==
   *                            true</code> or <code>fitToPage == true</code>.
   * @param     centerOnPage    Specifies whether the image will be centered
   *                            on the page.
   * @param     fitToPage       Specifies whether the image will be
   *                            stretched/shrunk to cover the entire page.
   * @param     scale           The scale of the printing. Set to 1.0 to
   *                            print the image at its original size.
   * @param     title           Title to be printed above the image.
   *                            It can be <code>null</code>
   * @param     showTitle       Specifies whether the title will be printed.
   * @param     showPageNumbers Specifies whether page numbers will be
   *                            printed.
   */
  public static void printImage(String fileName, double top, double left,
                                double bottom, double right,
                                boolean centerOnPage, boolean fitToPage,
                                double scale, String title, boolean showTitle,
                                boolean showPageNumbers)
    throws Exception
  {
    // Get the name of the native executable for this platform.
    String prog = imagePrinterName();
    if (prog == null) {
      initResources();
      throw new Exception(resources.getString("PrintErrorMsg1"));
    }

    // Locate the executable which does the printing in the native program
    // folders.
    if (imagePrinter == null) {
      imagePrinter = findImagePrinter(prog);
    }
    if (imagePrinter == null) {
      initResources();
      throw new Exception(
        resources.getString("PrintErrorMsg2") +
        prog +
        resources.getString("PrintErrorMsg3")
      );
    }

    if (title == null) {
      title = "";
    }

    NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

    String command =
      imagePrinter +
      " Filename:" + fileName +
      " Top:" + nf.format(top) +
      " Bottom:" + nf.format(bottom) +
      " Left:" + nf.format(left) +
      " Right:" + nf.format(right) +
      " Center:" + booleanToInt(centerOnPage) +
      " Stretch:" + booleanToInt(fitToPage) +
      " Scale:" + Math.round(100.0 * scale) +
      " Title:\"" + title + "\"" +
      " TitleExists:" + booleanToInt(showTitle) +
      " PageNumberExists:" + booleanToInt(showPageNumbers);
      //System.out.println("command: " + command);
      /*Process p = */Runtime.getRuntime().exec(command);
  }

  /**
   * Converts a boolean into a C-style boolean.
   * @param     flag    The boolean to convert.
   * @result    1 if <code>flag</code> is true, otherwise 0.
   */
  private static int booleanToInt(boolean flag)
  {
    if (flag) {
      return 1;
    }else{
      return 0;
    }
  }

  /**
   * Returns the name of the executable that prints images, depending on the
   * current OS and architecture.
   * @return    The name of the executable. If there is not executable for
   *            this platform, this method returns <code>null</code>.
   */
  private static String imagePrinterName()
  {
    String prog;
    String os = System.getProperty("os.name");
    String arch = System.getProperty("os.arch");
    if (os.startsWith("Windows") && arch.equals("x86")) {
      prog = IMAGE_PRINTER_WINDOWS;
    }else{
      prog = null;
    }
    return prog;
  }

  /**
   * Locates the executable that prints images.
   * @paran     prog    The name of the executable without its path.
   * @return    The full path of the executable. If the executable cannot be
   *            found, this method returns null.
   */
  private static String findImagePrinter(String prog)
  {
    File[] folders = ESlate.getNativeProgramFolders();
    for (int i=0; i<folders.length; i++) {
      File printExe = new File(folders[i], prog);
      if (printExe.exists()) {
        return printExe.getAbsolutePath();
      }
    }
    return null;
  }

  /**
   * Initialize localized resources.
   */
  private static void initResources()
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.PrintResource", Locale.getDefault()
      );
    }
  }
}
