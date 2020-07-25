package gr.cti.eslate.base;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import gr.cti.eslate.utils.*;

/**
 * Cell editor for plug view trees.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugTreeCellEditor extends DefaultTreeCellEditor
                               implements CellEditorListener
{
  /**
   * The tree on which the editor operates.
   */
  private PlugTree myTree;

  /**
   * Construct a PlugTreeCellEditor instance.
   * @param     tree    The tree on which the editor will operate.
   */
  public PlugTreeCellEditor(PlugTree tree)
  {
   super(tree, (DefaultTreeCellRenderer)(tree.getCellRenderer()));
   myTree = tree;
   addCellEditorListener(this);
  }

  /**
   * Invoked when the user has finished editing the name of a node.
   * (Required by the CellEditorListener interface).
   * @param     e       The event indicating that the tree has been changed.
   */
  public void editingStopped(ChangeEvent e)
  {
    PlugTreeNode node = (PlugTreeNode)(myTree.getLastSelectedPathComponent());
    ESlateHandle handle = node.getHandle();
    if (handle != null) {
      try {
        handle.setComponentName(node.toString());
      } catch (Exception ex) {
        node.setUserObject(handle);  // Swing puts the newly entered name there!
        // Update plug views
        handle.reloadModels();
        handle.makeVisible(null); 
        ResourceBundle resources = ResourceBundle.getBundle(
          "gr.cti.eslate.base.ESlateResource",
          ESlateMicroworld.getCurrentLocale()
        );
        ESlateOptionPane.showMessageDialog(
          myTree, ex.getMessage(),
          resources.getString("error"),
          JOptionPane.ERROR_MESSAGE);
      }
    }else{
      Plug plug = node.getPlug();
      try {
        plug.setName(node.toString());
      } catch (PlugExistsException ex) {
        node.setUserObject(plug);  // Swing puts the newly entered name there!  
        // Update plug views
        plug.getHandle().reloadModels();
        plug.getHandle().makeVisible(node); 
        ResourceBundle resources = ResourceBundle.getBundle(
          "gr.cti.eslate.base.ESlateResource",
          ESlateMicroworld.getCurrentLocale()
        );
        ESlateOptionPane.showMessageDialog(
          myTree, ex.getMessage(),
          resources.getString("error"),
          JOptionPane.ERROR_MESSAGE);
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
      PlugTreeNode node = ((PlugTreeNode)(myTree.getLastSelectedPathComponent()));
      if (node.getHandle() != null) {
        super.editingIcon =
          new ESlateRootIcon(((PlugTreeCellRenderer)super.renderer).image);
      }else{
        super.editingIcon = node.getPlug().getIcon();
      }
      super.offset =
        super.renderer.getIconTextGap() + super.editingIcon.getIconWidth();
    }
  }
}
