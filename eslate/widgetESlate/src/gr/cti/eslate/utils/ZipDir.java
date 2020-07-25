package gr.cti.eslate.utils;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * This class provides a static method for creating a zip file from a given
 * directory.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
@SuppressWarnings(value={"unchecked"})
public class ZipDir
{
  /**
   * The constructor is private, as this class only provides static methods.
   */
  private ZipDir()
  {
  }

  /**
   * Packs a folder.
   * @param     dir     The folder to pack.
   * @param     out     The stream on which the packed output will be written.
   *                    The stream is left open after the packed directory is
   *                    written.
   * @exception IOException     Thrown if something goes wrong.
   */
  public static void zip(File dir, OutputStream out) throws IOException
  {
    ArrayList a = new ArrayList();
    accumulate(dir, a);
    ZipOutputStream zos = new ZipOutputStream(out);
    //zos.setMethod(ZipOutputStream.STORED);
    int n = a.size();
    byte[] buf = new byte[512];
    for (int i=0; i<n; i++) {
      File ff = (File)(a.get(i));
      String name = mangle(dir, ff);
      ZipEntry ze = new ZipEntry(name);
      zos.putNextEntry(ze);
      FileInputStream fis = new FileInputStream(ff);
      BufferedInputStream bis = new BufferedInputStream(fis);
      int nb;
      do {
        nb = bis.read(buf);
        if (nb >= 0) {
        zos.write(buf, 0, nb);
        }
      } while (nb >= 0);
      bis.close();
      zos.closeEntry();
    }
    zos.finish();
  }

  /**
   * Recursively accumulates all the plain files in a directory.
   * @param     f       The file from which to start.
   * @param     a       A list where the files will be accumulated.
   */
  private static void accumulate(File f, ArrayList a)
  {
    if (f.isDirectory()) {
      String[] files = f.list();
      int n = files.length;
      for (int i=0; i<n; i++) {
        accumulate(new File(f, files[i]), a);
      }
    }else{
      a.add(f);
    }
  }

  /**
   * Converts file names into something appropriate for a zip file entry.
   * @param     parent  The folder relative to which the entries will be
   *                    converted.
   * @param     f       The file whose name will be converted.
   * @return    The name of the file, relative to <code>parent</code>, with
   *            "/" used as a file separator.
   */
  private static String mangle(File parent, File f) throws IOException
  {
    if (!parent.isAbsolute()) {
      parent = new File(parent.getAbsolutePath());
    }
    int trim = parent.getAbsolutePath().length() + 1;
    String name = f.getAbsolutePath().substring(trim);

    char sep = File.separatorChar;
    if (sep != '/') {
      name = name.replace(sep, '/');
    }
    return name;
  }
}
