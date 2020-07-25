package gr.cti.structfile;

import java.io.*;
import java.util.*;

/**
 * This class extracts a file from a structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class Extract
{
  /**
   * Constructor is private, as all provided methods are static.
   */
  private Extract()
  {
  }

  /**
   * Extract a file from a structured file to standard output.
   * @param     args    Program arguments. The first argument is teh name of
   *                    the structured file. The remaining arguments form the
   *                    path of the file to extract.
   */
  public static void main(String[] args)
  {
    try {
      int n = args.length;
      ArrayList<String> path = new ArrayList<String>(n-1);
      for (int i=1; i<n; i++) {
        path.add(args[i]);
      }
      StructFile sf = new StructFile(args[0], StructFile.OLD);
      extract(sf, path, StructFile.ABSOLUTE_PATH, System.out);
    }catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Extract a file from a structured file to an output stream.
   * @param     sf              The structured file from which to extract
   *                            the file.
   * @param     path            The path of the file to extract.
   * @param     relative        Specifies whether the path is relative to the
   *                            current directory (true of
   *                            <code>StructFile.RELATIVE_PATH</code>) or to
   *                            the root directory (false or
   *                            <code>StructFile.ABSOLUTE_PATH</code>).
   * @param     out     The output stream to which to extract the file.
   * @exception IOException     Thrown if something goes wrong.
   */
  @SuppressWarnings("rawtypes")
public static void extract(
    StructFile sf, AbstractList path, boolean relative, OutputStream out)
    throws IOException
  {
    InputStream is = sf.newOpenInputFile(path, relative);
    BufferedInputStream bis = new BufferedInputStream(is);
    byte[] b = new byte[512];
    int n;
    while ((n = bis.read(b)) >= 0) {
      out.write(b, 0, n);
    }
    bis.close();
  }
}
