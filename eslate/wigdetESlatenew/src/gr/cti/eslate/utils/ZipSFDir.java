package gr.cti.eslate.utils;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import gr.cti.structfile.*;

/**
 * This class provides a static method for creating a zip file from a given
 * directory in a structured file.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
//@SuppressWarnings(value={"unchecked"})
public class ZipSFDir
{
  /**
   * The constructor is private, as this class only provides static methods.
   */
  private ZipSFDir()
  {
  }

  /**
   * Packs a folder.
   * @param     acc     The structured file on which the folder to pack resides.
   * @param     out     The stream on which the packed output will be written.
   *                    The stream is left open after the packed directory is
   *                    written.
   * @exception IOException     Thrown if something goes wrong.
   */
  public static void zip(Access acc, OutputStream out)
    throws IOException
  {
    ArrayList a = new ArrayList();
    acc.changeToRootDir();
    accumulate(acc, new Vector(), a);
    ZipOutputStream zos = new ZipOutputStream(out);
    //zos.setMethod(ZipOutputStream.STORED);
    int n = a.size();
    byte[] buf = new byte[512];
    for (int i=0; i<n; i++) {
      Vector v = (Vector)(a.get(i));
      String name = mangle(v);
      ZipEntry ze = new ZipEntry(name);
      zos.putNextEntry(ze);
      Vector v2 = (Vector)(v.clone());
      int last = v2.size() - 1;
      String f = (String)(v.elementAt(last));
      v2.removeElementAt(last);
      acc.changeDir(v2, StructFile.ABSOLUTE_PATH);
      InputStream is = acc.openInputFile(f);
      int nb;
      do {
        nb = is.read(buf);
        if (nb >= 0) {
        zos.write(buf, 0, nb);
        }
      } while (nb >= 0);
      is.close();
      zos.closeEntry();
    }
    zos.finish();
  }

  /**
   * Recursively accumulates all the plain files in a directory.
   * @param     acc     The structured file from which folders will be
   *                    accumulated.
   * @param     cd      The current directory. Invoke this method with an
   *                    empty vector.
   * @param     a       A list where the files will be accumulated.
   * @exception IOException     Thrown if something goes wrong.
   */
  private static void accumulate(Access acc, Vector cd, ArrayList a)
    throws IOException
  {
    String[] files = acc.listFiles();
    int n = files.length;
    for (int i=0; i<n; i++) {
      String f = files[i];
      if (acc.isPlainFile(f)) {
        Vector v = (Vector)(cd.clone());
        v.add(f);
        a.add(v);
      }else{
        acc.changeDir(f);
        cd.addElement(f);
        accumulate(acc, cd, a);
        cd.removeElementAt(cd.size() - 1);
        acc.changeToParentDir();
      }
    }
  }

  /**
   * Converts file paths into something appropriate for a zip file entry.
   * @param     path    The path to convert.
   * @return    The name of the file, with "/" used as a file separator.
   */
  private static String mangle(Vector v) throws IOException
  {
    int n = v.size();
    String name = (String)(v.elementAt(0));
    for (int i=1; i<n; i++) {
      name = name + "/" + (String)(v.elementAt(i));
    }
    return name;
  }
}
