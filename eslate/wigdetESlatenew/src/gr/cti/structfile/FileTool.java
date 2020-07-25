package gr.cti.structfile;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This class implements an application that manipulates the contents of a
 * structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.StructFile
 * @see         gr.cti.structfile.FileToolPanel
 */
public class FileTool
{
  private static ResourceBundle resources;
  private static FileToolPanel ftp = null;

  /**
   * The constructor is private, as there is no need to instantiate this
   * class.
   */
  private FileTool()
  {
  }

  /**
   * Structured file maintenance tool.
   * <BR>
   * Usage: <CODE>java gr.cti.structfile.FileTool</CODE>
   */
  public static void main(String args[])
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.structfile.UtilResources", Locale.getDefault()
    );
    JFrame f = null;
    try {
      ftp = new FileToolPanel();
      f = new JFrame();
      f.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          e.getWindow().setVisible(false);
          if (ftp != null) {
            ftp.exit();
          }else{
            System.exit(0);
          }
        }
      });
      f.setTitle(resources.getString("fileToolName"));
      f.getContentPane().add(ftp);
      f.pack();
      f.setVisible(true);
      if (args.length > 0) {
        ftp.loadFile(new File(args[0]).getCanonicalPath());
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      if (f != null) {
        f.setVisible(false);
        f.dispose();
      }
      if (ftp != null) {
        ftp.exit();
      }else{
        System.exit(0);
      }
    }
  }
}
