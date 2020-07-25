package gr.cti.eslate.utils;

import java.io.*;
import java.util.zip.*;

/**
 * This class provides a static method for unpacking a zip file.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class UnzipDir
{
  /**
   * The constructor is private, as this class only provides static methods.
   */
  private UnzipDir()
  {
  }

  /**
   * Unpack a zip file.
   * @param     dir     The folder where the zip file will be unpacked.
   * @param     in      The input stream from where the zipped data will be
   *                    read. The stream is left open after unpacking the data.
   * @exception IOException     Thrown if something goes wrong.
   */
  public static void unzip(File dir, InputStream in) throws IOException
  {
    ZipInputStream zis = new ZipInputStream(in);
    byte[] buf = new byte[512];
    for(;;) {
      ZipEntry ze = zis.getNextEntry();
      if (ze == null) {
        break;
      }
      String name = mangle(ze.getName());
      if (ze.isDirectory()) {
        mkdir(dir, name);
      }else{
        File newFile = new File(dir, name);
        mkParentDir(newFile);
        FileOutputStream fos = new FileOutputStream(newFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int nb;
        do {
          nb = zis.read(buf);
          if (nb >= 0) {
            bos.write(buf, 0, nb);
          }
        } while (nb >= 0);
        bos.close();
        zis.closeEntry();
      }
    }
  }

  /**
   * Creates a directory, ensuring that all directories in its path are
   * created as well,if necessary.
   * @param     parent  The directory relative to which the directory to
   *                    create will be specified.
   * @param     name    The path name of the directory, relative to
   *                    <code>parent</code>.
   * @exception IOException     Thrown if something goes wrong.
   */
  private static void mkdir(File parent, String name) throws IOException
  {
    File f = new File(new File(parent, name).getAbsolutePath());
    f.mkdirs();
  }

  /**
   * Creates the parent directory of a file, ensuring that all directories
   * in its path are created as well,if necessary.
   * @param     f       The file whose parent directory will be created.
   */
  private static void mkParentDir(File f)
  {
    f.getParentFile().mkdirs();
  }

  /**
   * Converts a file name from the format stored in a zip file entry to the
   * format supported by the native file system.
   * @param     name    The name to convert.
   * @return    The file name, with the appropriate file separator used,
   *            instead of "/".
   */
  private static String mangle(String name)
  {
    char sep = File.separatorChar;
    if (sep != '/') {
      name = name.replace('/', sep);
    }
    return name;
  }

}
