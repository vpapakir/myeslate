package gr.cti.structfile;

import java.io.*;
import java.util.*;

/**
 * This class implements static methods for copying a folder in a structured
 * file to another structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.StructFile
 * @see         gr.cti.structfile.Access
 */
public class Copy
{
  /**
   * Copies the data from the current folder in a structured file to the
   * current folder of another structured file.
   * @param     from    The source file.
   * @param     to      The destination file.
   * @exception IllegalArgumentException        Thrown if the two files are
   *                            identical or if any of the two files is null.
   * @exception IOException     Thrown if the copy fails for some reason.
   */
  public static void copy(Access from, Access to)
    throws IOException, IllegalArgumentException
  {
    if ((from == null) || (to == null)) {
      ResourceBundle resources = ResourceBundle.getBundle(
        "gr.cti.structfile.StructFileResources", Locale.getDefault()
      );
      throw new IllegalArgumentException(resources.getString("nullFiles"));
    }
    if (from.equals(to)) {
      ResourceBundle resources = ResourceBundle.getBundle(
        "gr.cti.structfile.StructFileResources", Locale.getDefault()
      );
      throw new IllegalArgumentException(resources.getString("equalFiles"));
    }
    recursiveCopy(from, to, from.listFiles());
  }

  /**
   * Recursively copies the current directory entry from the current directory
   * of a structured file file to the current directory of another.
   * @param     in      The source file.
   * @param     out     The destination file.
   * @param     v       The list of files to copy.
   * @exception IOException     Thrown if the copying fails.
   */
  private static void recursiveCopy(Access in, Access out, String[] v)
    throws IOException
  {
    int nEntries = v.length;
    byte buf[] = new byte[508];
    for (int i=0; i<nEntries; i++) {
      String name = v[i];
      if (!in.isPlainFile(name)) {
        in.changeDir(name);
        out.createDirectory(name);
        out.changeDir(name);
        recursiveCopy(in, out, in.listFiles());
        in.changeToParentDir();
        out.changeToParentDir();
      }else{
        InputStream is = in.openInputFile(name);
        OutputStream os = out.openOutputFile(name);
        int n;
        do {
          n = is.read(buf);
          if (n >= 0) {
            os.write(buf, 0, n);
          }
        } while (n >= 0);
        os.close();
        is.close();
      }
    }
  }

}
