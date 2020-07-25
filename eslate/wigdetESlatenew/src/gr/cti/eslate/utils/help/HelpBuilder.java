package gr.cti.eslate.utils.help;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * This class contains a method that builds the JavaHelp files for a directory
 * containing help in HTML format. A <code>main</code> method is provided, so
 * that this class can be used as a stand-alone application.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class HelpBuilder
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.eslate.utils.help.HelpResource", Locale.getDefault()
    );

  /**
   * Main entry point.
   * @param     args    Not used.
   */
  public static void main(String[] args)
  {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      String cName = JOptionPane.showInputDialog(
        null,
        resources.getString("enterName1"),
        resources.getString("enterName2"),
        JOptionPane.PLAIN_MESSAGE
      );
      if ((cName != null) && !cName.equals("")) {
        buildHelp(null, cName);
      }
    }catch (Throwable e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

  /**
   * Build the JavaHelp files for a directory containing help in HTML format.
   * The directory will be specified by the user via a file dialog.
   * @param     owner   A component to be used as the owner of the modal
   *                    dialogs that will be displayed. It can be
   *                    <code>null</code>.
   * @param     cName   The name of the class for whose help the JavaHelp files
   *                    files will be generated.
   * @exception IOException     Thrown if something goes wrong while building
   *                            the files.
   * @return    Returns the directory containing the help if the files were
   *            created (or if they were already present and the user chose
   *            not to modiify them), <code>null</code>otherwise.
   */
  public static File buildHelp(Component owner, String cName)
    throws IOException
  {
    Frame ownerFrame;
    if (owner instanceof Frame) {
      ownerFrame = (Frame)owner;
    }else{
      ownerFrame = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, owner);
    }
    JFileChooser fd = new JFileChooser(System.getProperty("user.dir"));
    fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fd.setDialogTitle(resources.getString("selectDir"));
    int ret = fd.showOpenDialog(ownerFrame);
    if (ret == JFileChooser.APPROVE_OPTION) {
      File f = fd.getSelectedFile();
      return buildHelp(f, ownerFrame, cName, true);
    }
    return null;
  }

  /**
   * Build the JavaHelp files for a directory containing help in HTML format.
   * @param     f               The directory containing the help files.
   * @param     ownerFrame      A frame to be used as the owner of the modal
   *                            dialogs that will be displayed. It can be
   *                            <code>null</code>.
   * @param     cName           The name of the class for whose help the
   *                            JavaHelp files files will be generated.
   * @param     confirmOverwrite        Specifies whether the user will be
   *                            asked to confirm existing JavaHelp files.
   * @exception IOException     Thrown if something goes wrong while building
   *                            the files.
   * @return    Returns the directory containing the help if the files were
   *            created (or if they were already present and the user chose
   *            not to modiify them), <code>null</code>otherwise.
   */
  public static File buildHelp(
    File f, Frame ownerFrame, String cName, boolean confirmOverwrite)
    throws IOException
  {
    Files.setNames(cName);

    boolean overwrite;

    if (Files.allFilesExist(f)) {
      if (confirmOverwrite) {
        int x = ESlateOptionPane.showConfirmDialog(
          ownerFrame,
          resources.getString("confirmOverwriteAll"),
          resources.getString("confirm"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE
        );
        if (x == JOptionPane.YES_OPTION) {
          overwrite = true;
        }else{
          return f;
        }
      }else{
        overwrite = true;
      }
    }else{
      if (Files.filesExist(f)) {
        int x = ESlateOptionPane.showConfirmDialog(
          ownerFrame,
          resources.getString("confirmOverwriteSome"),
          resources.getString("confirm"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE
        );
        if (x == JOptionPane.YES_OPTION) {
          overwrite = true;
        }else{
          overwrite = false;
        }
      }else{
        overwrite = true;
      }
    }
    FileTree ft = new FileTree(f);
    ft.trimEmptyDirs();
    if (ft.getFirst() != null) {
      OrderHint oh = null;
      try {
        oh = Files.readOrderHints(f);
      } catch (Exception ioe) {
      }
      if (oh != null) {
        ft.order(oh);
      }
      TreeDialog td = new TreeDialog(
        ownerFrame, resources.getString("adjustContents"), ft
      );
      if (td.showDialog() == TreeDialog.OK) {
        if (ft.getFirst() != null) {
          if (overwrite) {
            Files.deleteOldFiles(f);
          }
          String encoding = System.getProperty("file.encoding");
          Files.makeHelpSet(f, encoding, ft, overwrite);
          Files.makeTOC(f, encoding, ft, overwrite);
          Files.makeIndex(f, encoding, ft, overwrite);
          Files.makeMap(f, encoding, ft, overwrite);
          Files.makeJHIndex(f, overwrite);
          Files.makeOrderHints(f, ft, overwrite);
          return f;
        }else{
          ESlateOptionPane.showMessageDialog(
            ownerFrame,
            resources.getString("noHTMLleft"),
            resources.getString("warning"),
            JOptionPane.WARNING_MESSAGE
          );
        }
      }
    }else{
      ESlateOptionPane.showMessageDialog(
        ownerFrame,
        resources.getString("noHTML"),
        resources.getString("warning"),
        JOptionPane.WARNING_MESSAGE
      );
    }
    return null;
  }

  /**
   * Deletes the help description files that were created during HelpBuilder's
   * run.
   * @param     dir             The folder containing the help, where the
   *                            decsription files were created.
   */
  public static void deleteDescriptionFiles(File dir)
  {
    Files.deleteOldFiles(dir);
  }
}
