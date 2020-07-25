package gr.cti.structfile;

import java.io.*;
import java.util.*;

import gr.cti.structfile.version2.*;

/**
 * This class defragments a structured file by copying its contents to a
 * new structured file and replacing the old file with the copy.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.1, 6-Jul-2006
 * @see         gr.cti.structfile.StructFile
 */
public class Defragment
{
  /**
   * The structured file to defragment.
   */
  private static StructFile f;
  /**
   * The temporary structured file to use for the defragmentation.
   */
  private static StructFile tmp;
  /**
   * I/O buffer.
   */
  private static byte[] buf;
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = null;

  /**
   * The constructor is private, as there is no need to instantiate this
   * class.
   */
  private Defragment()
  {
  }

  /**
   * Defragments a structured file.
   * <BR>
   * Usage: <CODE>java gr.cti.structfile.Defragment structfile tempfile</CODE>
   * @param     args    args[0] should contain the name of the file to be
   *                    defragmented, and args[1] should contain the name of
   *                    a temporary file to use for the defragmentation.
   */
  public static void main(String args[])
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.structfile.UtilResources", Locale.getDefault()
    );
    if (args.length != 2) {
      System.out.println(resources.getString("defragUsage"));
    }else{
      try {
        defragment(args[0], args[1]);
      } catch (DefragmentException e) {
        System.out.println(e.getMessage());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

 /**
  * Defragments a structured file by copying its contents to a
  * new structured file and replacing the old file with the copy.
  * @param      origName        The name of the structured file to defragment.
  * @param      tmpName         The name of the file to use for the
  *                             intermediate copy.
  * @exception  IOException     Thrown if something goes wrong while accessing
  *                             one of the two files.
  * @exception  DefragmentException     Thrown if something goes wrong.
  */
  public static void defragment(String origName, String tmpName)
    throws DefragmentException, IOException
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.structfile.UtilResources", Locale.getDefault()
      );
    }
    try {
      f = new StructFile(origName, StructFile.OLD);
      tmp = new StructFile(tmpName, StructFile.NEW);
    } catch (IOException e) {
      throw new DefragmentException(e.getMessage());
    }
    if (f.getStructFile() instanceof StructFile2) {
      buf = new byte[StructFile2.BLOCKSIZE];
    }else{
      buf = null;
    }
    // Copy comments in comment block.
    tmp.setComment(f.getCommentBytes());
    // Copy contents.
    copy(f.list());
    tmp.close();
    f.close();
    File origFile = new File(origName);
    File tmpFile = new File(tmpName);
    if (origFile.delete()) {
      if (!tmpFile.renameTo(origFile)) {
        throw new DefragmentException(
          resources.getString("renameFailed1") + tmpName +
          resources.getString("renameFailed2") + origName +
          resources.getString("renameFailed3"));
      }
    }else{
      throw new DefragmentException(
        resources.getString("deleteFailed1") + origName +
        resources.getString("deleteFailed2"));
    }
  }

  /**
   * Copies recursively a directory from the strctured file to the temporary
   * file.
   * @param     v       The directory to copy.
   * @exception IOException
   */
  private static void copy(Vector v)
    throws IOException
  {
    for (int i=0; i<v.size(); i++) {
      Entry e = (Entry)(v.elementAt(i));
      if (e.getType() == Entry.DIRECTORY) {
        // Recursively copy directories
        f.changeDir(e);
        tmp.changeDir(tmp.createDir(e.getName()));
        copy(f.list());
        f.changeToParentDir();
        tmp.changeToParentDir();
      }else{
        // Copy the contents of subfiles.
        StructInputStream is = new StructInputStream(f, e);
        StructOutputStream os =
          new StructOutputStream(tmp, tmp.createFile(e.getName()));
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
