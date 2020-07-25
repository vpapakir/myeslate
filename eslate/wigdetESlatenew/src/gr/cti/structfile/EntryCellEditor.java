package gr.cti.structfile;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Cell editor for structured file tree views.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class EntryCellEditor extends DefaultTreeCellEditor
                             implements CellEditorListener
{
  /**
   * The tree on which the editor operates.
   */
  private JTree tree;
  /**
   * The structured file corresponding to the tree.
   */
  private StructFile sf;

  /**
   * Construct an EntryCellEditor instance.
   * @param     tree    The tree on which the editor will operate.
   * @param     sf      The structured file corresponding to this tree.
   */
  public EntryCellEditor(JTree tree, StructFile sf)
  {
   super(tree, (DefaultTreeCellRenderer)(tree.getCellRenderer()));
   this.tree = tree;
   this.sf = sf;
   addCellEditorListener(this);
  }

  /**
   * Invoked when the user has finished editing the name of a node.
   * (Required by the CellEditorListener interface).
   * @param     e       The event indicating that the tree has been changed.
   */
  @SuppressWarnings(value={"unchecked"})
  public void editingStopped(ChangeEvent e)
  {
    EntryNode node = (EntryNode)(tree.getLastSelectedPathComponent());
    String newName = (String)(node.getUserObject());
    if (newName == null || newName.equals("")) {
      node.setUserObject(node.getName());
      TreeTools.reloadModel(tree);
      TreeTools.select(tree, node);
    }else{
      if (!node.isRoot()) {
        TreeNode[] nodePath = node.getPath();
        int length = nodePath.length - 2;
        if (length > 0) {
          Vector path = new Vector(nodePath.length);
          for (int i=1; i<=length; i++) {
            path.addElement(((EntryNode)(nodePath[i])).getUserObject());
          }
          try {
            sf.changeDir(path, StructFile.ABSOLUTE_PATH);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }else{
          try {
            sf.changeToRootDir();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        try {
          sf.renameEntry(
            node.getName(),
            (String)(node.getUserObject())
          );
          node.setName(newName);
          TreeTools.sort((EntryNode)(node.getParent()));
          TreeTools.reloadModel(tree);
          TreeTools.select(tree, node);
        }catch (Exception ex) {
          ex.printStackTrace();
        }
      }else{
        node.setUserObject(node.getName());
        TreeTools.reloadModel(tree);
        TreeTools.select(tree, node);
      }
    }
  }

  /**
   * Invoked when the user has canceled editing the name of a node.
   * (Required by the CellEditorListener interface).
   * @param     e       The event indicating that the tree has been changed.
   */
  public void editingCanceled(ChangeEvent e)
  {
  }

  /**
   * Prepare for editing by selecting the appropriate icon to show next to the
   * text box that will appear.
   */
  protected void prepareForEditing()
  {
    super.prepareForEditing();
    if (super.renderer != null) {
      EntryNode node = ((EntryNode)(tree.getLastSelectedPathComponent()));
      if (node.isRoot()) {
        if (EntryCellRenderer.rootIcon != null) {
          super.editingIcon = EntryCellRenderer.rootIcon;
        }
      }else{
        if (node.getEntry().getType() == Entry.FILE) {
          if (EntryCellRenderer.fileIcon != null) {
            super.editingIcon = EntryCellRenderer.fileIcon;
          }
        }else{
          if (node.isLeaf()) {
            if (EntryCellRenderer.folderIcon != null) {
              super.editingIcon = EntryCellRenderer.folderIcon;
            }
          }
        }
      }
    }
  }
}
