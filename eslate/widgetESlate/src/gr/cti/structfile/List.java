package gr.cti.structfile;

import java.util.*;

/**
 * This class implements an application that lists the contents of a
 * structured file to the console.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.StructFile
 */
public class List
{
  /**
   * The structured file to be listed.
   */
  private static StructFile f = null;
  /**
   * Maximum length to pad printed fields.
   */
  private final static int MAXLENGTH = 16;
  /**
   * Localized resources.
   */
  private static ResourceBundle resources;

  /**
   * The constructor is private, as there is no need to instantiate this
   * class.
   */
  private List()
  {
  }

  /**
   * Lists a structured file to the console.
   * <BR>
   * Usage: <CODE>java gr.cti.structfile.List file</CODE>
   * @param     args    args[0] should contain the name of the file to be
   *                    listed.
   */
  public static void main(String args[])
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.structfile.UtilResources", Locale.getDefault()
    );
    if (args.length != 1) {
      System.out.println(resources.getString("listUsage"));
    }else{
      try {
        f = new StructFile(args[0], StructFile.OLD);
        list(f.list(), "/");
        f.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Lists recursively a directory in the structured file.
   * @param     v       The directory to list.
   * @param     prevDir The name of the parent directory of the directory
   *                    in unix absolute format (e.g., /usr/tmp/...).
   */
  private static void list(Vector v, String prevDir)
  {
    try {
      // Display the contents of the directory
      for (int i=0; i<v.size(); i++) {
        Entry e = (Entry)(v.elementAt(i));
        System.out.println(
          ((e.getType() == Entry.DIRECTORY) ? "(D)" : "   ") + " " +
          pad(e.getName()) + "\t" +
          pad(e.getSize()) + " \t" +
          pad(e.getLocation())
        );
        if (e.getType() == Entry.DIRECTORY) {
        }
      }

      // Recurse into any subdirectories
      for (int i=0; i<v.size(); i++) {
        Entry e = (Entry)(v.elementAt(i));
        if (e.getType() == Entry.DIRECTORY) {
          f.changeDir(e);
          String newName;
          if (prevDir.equals("/")) {
            newName = prevDir + e.getName();
          }else{
            newName = prevDir + "/" + e.getName();
          }
          System.out.println("");
          System.out.println(newName + ":");
          list(f.list(), newName);
          f.changeToParentDir();
        }
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }
  }

  /**
   * Pads a string to MAXLENGTH bytes.
   * @param     s       The string to pad.
   */
  private static String pad(String s)
  {
    if (s.length() >= MAXLENGTH) {
      return s;
    }else{
      StringBuffer sb = new StringBuffer(s);
      for (int i=s.length(); i<MAXLENGTH; i++) {
        sb.append(' ');
      }
      return sb.toString();
    }
  }

  /**
   * Returns a string representation of an integer, padded to MAXLENGTH bytes.
   * @param     x       The integer to pad.
   */
  private static String pad(int x)
  {
    return pad(new Integer(x).toString());
  }

  /**
   * Returns a string representation of a long integer, padded to MAXLENGTH
   * bytes.
   * @param     x       The long integer to pad.
   */
  private static String pad(long x)
  {
    return pad(new Long(x).toString());
  }
}
