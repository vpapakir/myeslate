package gr.cti.eslate.utils;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import gr.cti.structfile.*;

/**
 * This class provides a static method for unpacking a zip file into a
 * structured file.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
@SuppressWarnings(value={"unchecked"})
public class UnzipSFDir
{
  /**
   * The constructor is private, as this class only provides static methods.
   */
  private UnzipSFDir()
  {
  }

  /**
   * Unpack a zip file.
   * @param     acc     The structured file where the zip file will be unpacked.
   * @param     in      The input stream from where the zipped data will be
   *                    read. The stream is left open after unpacking the data.
   * @exception IOException     Thrown if something goes wrong.
   */
  public static void unzip(Access acc, InputStream in) throws IOException
  {
    ZipInputStream zis = new ZipInputStream(in);
    byte[] buf = new byte[512];
    for(;;) {
      ZipEntry ze = zis.getNextEntry();
      if (ze == null) {
        break;
      }
      Vector v = mangle(ze.getName());
      if (ze.isDirectory()) {
        mkdir(acc, v);
      }else{
        mkParentDir(acc, v);
        OutputStream os =
          acc.openOutputFile((String)(v.elementAt(v.size() - 1)));
        int nb;
        do {
          nb = zis.read(buf);
          if (nb >= 0) {
            os.write(buf, 0, nb);
          }
        } while (nb >= 0);
        os.close();
        zis.closeEntry();
      }
    }
  }

  /**
   * Creates a directory, ensuring that all directories in its path are
   * created as well,if necessary. The directory becomes the current
   * directory.
   * @param     acc     The structured file where the directorywill be made.
   * @param     path    The path of the directory to create.
   * @exception IOException     Thrown if something goes wrong.
   */
  private static void mkdir(Access acc, Vector path) throws IOException
  {
    acc.changeToRootDir();
    int n = path.size();
    for (int i=0; i<n; i++) {
      String s = (String)(path.elementAt(i));
      if ((!acc.fileExists(s)) || acc.isPlainFile(s)) {
        acc.createDirectory(s);
      }
      acc.changeDir(s);
    }
  }

  /**
   * Creates the parent directory of a file, ensuring that all directories
   * in its path are created as well,if necessary. The parent directory
   * becomes the current directory.
   * @param     acc     The structured file where the directorywill be made.
   * @param     path    The path of the file whose parent directory will be
   *                    created.
   * @exception IOException     Thrown if something goes wrong.
   */
  private static void mkParentDir(Access acc, Vector path)
    throws IOException
  {
    int n = path.size() - 1;
    String s = (String)(path.elementAt(n));
    path.removeElementAt(n);
    mkdir(acc, path);
    path.addElement(s);
  }

  /**
   * Converts a file name from the format stored in a zip file entry to a
   * vector of path components.
   * @param     name    The name to convert.
   * @return    A vector containing the path components of the given name.
   */
  private static Vector mangle(String name)
  {
    Vector v = new Vector();
    StringTokenizer st = new StringTokenizer(name, "/");
    while (st.hasMoreTokens()) {
      v.addElement(st.nextToken());
    }
    return v;
  }

}
