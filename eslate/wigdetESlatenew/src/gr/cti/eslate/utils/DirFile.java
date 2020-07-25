package gr.cti.eslate.utils;

import java.io.*;

/**
 * Some useful extensions of the File class.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class DirFile extends File
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a new DirFile instance.
   * @param     parent  The parent directory of the file.
   * @param     child   The name of the file.
   */
  public DirFile(File parent, String child)
  {
    super(parent, child);
  }

  /**
   * Create a new DirFile instance.
   * @param     parent  The parent directory of the file.
   * @param     child   The name of the file.
   */
  public DirFile(String parent, String child)
  {
    super(parent, child);
  }

  /**
   * Create a new DirFile instance.
   * @param     file    The name of the file.
   */
  public DirFile(String file)
  {
    super(file);
  }

  /**
   * Delete the file. This method overrides <code>File.delete()</code>,
   * to delete the contents of the file, if it points to a directory.
   * @return    True if the file and all its contents (if the file points to a
   *            directory) are deleted, false otherwise.
   */
  public boolean delete()
  {
    return deleteRecursively(this);
  }

  /**
   * Delete the file. This method is equivalent to <code>File.delete()</code>.
   * @return    True if the file is deleted, false otherwise.
   */
  private boolean plainDelete()
  {
    return super.delete();
  }

  /**
   * Recursively delete a file and any of the files it may contain, if it
   * points to a directory.
   * @param     f       The file to delete.
   * @return    True if the file and all its contents (if the file points to a
   *            directory) are deleted, false otherwise.
   */
  private static boolean deleteRecursively(File f)
  {
    boolean result = true;
    if (f.exists()) {
      if (f.isDirectory()) {
        String[] files = f.list();
        int n = files.length;
        for (int i=0; i<n; i++) {
          result |= deleteRecursively(new File(f, files[i]));
        }
      }
      if (f instanceof DirFile) {
        DirFile df = (DirFile)f;
        result |= df.plainDelete();
      }else{
        result |= f.delete();
      }
    }else{
      result = false;
    }
    return result;
  }

  /**
   * Recursively deletes the contents of a directory, leaving the directory
   * empty. If the file is a plain file, it is left intact.
   */
  public void clear()
  {
    clear(this);
  }

  /**
   * Recursively deletes the contents of a directory, leaving the directory
   * empty. If the supplied <code>File</code> is a plain file, it is left
   * intact.
   */
  private static void clear(File f)
  {
    if (f.isDirectory()) {
      String[] files = f.list();
      int n = files.length;
      for (int i=0; i<n; i++) {
        deleteRecursively(new File(f, files[i]));
      }
    }
  }

  /**
   * Creates an empty directory in the default temporary file directory.
   * @param     name    The name of the directory.
   */
  public final static DirFile createTempDir(String name)
  {
    File tmp = new File(
      System.getProperty("java.io.tmpdir") + File.separator + ".temp"
    );
    DirFile f = new DirFile(tmp, name);
    f.mkdir();
    return f;
  }

}
