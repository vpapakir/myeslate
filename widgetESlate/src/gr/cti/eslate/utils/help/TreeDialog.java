package gr.cti.eslate.utils.help;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.event.*;


/**
 * This class implements the dialog that lets the user adjust the table of
 * contents before it is created.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 */
class TreeDialog extends JDialog
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The button that moves entries one level up.
   */
  private JButton upButton;
  /**
   * The button that moves entries one level down.
   */
  private JButton downButton;
  /**
   * The button that removes entries.
   */
  private JButton deleteButton;
  /**
   * The tree containing the descriptions in the table of contents.
   */
  private JTree tree;
  /**
   * The cell editor of the tree containing the descriptions in the table of
   * contents.
   */
  private CellEditor cellEditor;
  /**
   * The result returned by the dialog.
   */
  private int result = CANCEL;
  /**
   * The "OK" button of the dialog.
   */
  private JButton okButton;

  /**
   * Indicates that the user has pressed the "OK" button.
   */
  final static int OK = 0;
  /**
   * Indicates that the user has canceled the dialog.
   */
  final static int CANCEL = 1;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.eslate.utils.help.HelpResource", Locale.getDefault()
    );

  /**
   * Build the dialog.
   * @param     owner   The owner of the dialog. It can be <code>null</code>.
   * @param     title   The title of the dialog.
   * @param     ft      The tree structure of the help.
   */
  TreeDialog(Frame owner, String title, FileTree ft)
  {
    super(owner, title, true);

    JPanel topPanel = new JPanel(new BorderLayout(10, 0));
    topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));
    tree = new JTree(ft)
    {
      /**
       * Serialization version.
       */
      final static long serialVersionUID = 1L;
      
      public void updateUI()
      {
        super.updateUI();
        setBackground(UIManager.getColor("Tree.background"));
      }
    };
    tree.setRootVisible(false);
    tree.setBackground(UIManager.getColor("Tree.background"));
    tree.setEditable(true);
    tree.setRowHeight(
      4 + 
      Math.max(
        tree.getRowHeight(),
            FileTreeCellRenderer.getFileIconHeight()
      )
    );
    tree.setCellRenderer(new FileTreeCellRenderer());
    cellEditor = tree.getCellEditor();
    cellEditor.addCellEditorListener(new CellEditorListener(){
      public void editingStopped(ChangeEvent e)
      {
        FileTree ft = (FileTree)(tree.getLastSelectedPathComponent());
        ft.description = (String)(cellEditor.getCellEditorValue());
      }
      public void editingCanceled(ChangeEvent e)
      {
      }
    });
    tree.addTreeSelectionListener(new TreeSelectionListener(){
      public void valueChanged(TreeSelectionEvent e)
      {
        FileTree ft = (FileTree)(tree.getLastSelectedPathComponent());
        enableButtons(ft);
      }
    });
    JScrollPane scrollPane = new JScrollPane(tree);
    //Dimension scrollPaneSize = new Dimension(300, 300);
    //scrollPane.setPreferredSize(scrollPaneSize);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    Dimension buttonSize = new Dimension(27, 22);
    upButton = new JButton(
      new ImageIcon(TreeDialog.class.getResource("upArrow.gif"))
    );
    upButton.setToolTipText(resources.getString("up"));
    upButton.setEnabled(false);
    upButton.setMargin(new Insets(0, 0, 0, 0));
    upButton.setMaximumSize(buttonSize);
    upButton.setPreferredSize(buttonSize);
    upButton.setMinimumSize(buttonSize);
    upButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        FileTree ft = (FileTree)(tree.getLastSelectedPathComponent());
        FileTree parent = (FileTree)(ft.getParent());
        int i = parent.getIndex(ft);
        parent.remove(ft);
        parent.insert(ft, i-1);
        reloadModel();
        tree.setSelectionPath(new TreePath(ft.getPath()));
      }
    });
    downButton = new JButton(
      new ImageIcon(TreeDialog.class.getResource("downArrow.gif"))
    );
    downButton.setToolTipText(resources.getString("down"));
    downButton.setEnabled(false);
    downButton.setMargin(new Insets(0, 0, 0, 0));
    downButton.setMaximumSize(buttonSize);
    downButton.setPreferredSize(buttonSize);
    downButton.setMinimumSize(buttonSize);
    downButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        FileTree ft = (FileTree)(tree.getLastSelectedPathComponent());
        FileTree parent = (FileTree)(ft.getParent());
        int i = parent.getIndex(ft);
        parent.remove(ft);
        parent.insert(ft, i+1);
        reloadModel();
        tree.setSelectionPath(new TreePath(ft.getPath()));
      }
    });
    deleteButton = new JButton(
      new ImageIcon(TreeDialog.class.getResource("delete.gif"))
    );
    deleteButton.setToolTipText(resources.getString("delete"));
    deleteButton.setEnabled(false);
    deleteButton.setMargin(new Insets(0, 0, 0, 0));
    deleteButton.setMaximumSize(buttonSize);
    deleteButton.setPreferredSize(buttonSize);
    deleteButton.setMinimumSize(buttonSize);
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        FileTree ft = (FileTree)(tree.getLastSelectedPathComponent());
        FileTree parent = (FileTree)(ft.getParent());
        parent.remove(ft);
        reloadModel();
      }
    });

    JPanel toolPanel = new JPanel();
    toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
    toolPanel.add(Box.createGlue());
    toolPanel.add(upButton);
    toolPanel.add(Box.createVerticalStrut(3));
    toolPanel.add(downButton);
    toolPanel.add(Box.createVerticalStrut(3));
    toolPanel.add(deleteButton);
    toolPanel.add(Box.createGlue());

    topPanel.add(scrollPane, BorderLayout.CENTER);
    topPanel.add(toolPanel, BorderLayout.EAST);

    buttonSize = new Dimension(90, 25);
    //Color buttonTextColor = new Color(0, 0, 128);
    Insets buttonInsets = new Insets(0, 0, 0, 0);
    okButton = new JButton(resources.getString("ok"));
    okButton.setMargin(buttonInsets);
    okButton.setPreferredSize(buttonSize);
    okButton.setMaximumSize(buttonSize);
    okButton.setMinimumSize(buttonSize);
    //okButton.setForeground(buttonTextColor);
    okButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        result = OK;
        setVisible(false);
      }
    });
    JButton cancelButton = new JButton(resources.getString("cancel"));
    cancelButton.setMargin(buttonInsets);
    cancelButton.setPreferredSize(buttonSize);
    cancelButton.setMaximumSize(buttonSize);
    cancelButton.setMinimumSize(buttonSize);
    //cancelButton.setForeground(buttonTextColor);
    cancelButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        result = CANCEL;
        setVisible(false);
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createGlue());
    buttonPanel.add(okButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(cancelButton);
    buttonPanel.add(Box.createGlue());

    JPanel mainPanel = new JPanel(true);
    mainPanel.setLayout(new BoxLayout(mainPanel, 1));
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(topPanel);
    mainPanel.add(buttonPanel);

    getContentPane().add(mainPanel);
    pack();
    setSize(new Dimension(320, 240));
    center(owner);
    addWindowListener(new WindowAdapter() {
      boolean gotFocus = false;

      public void windowActivated(WindowEvent we)
      {
        if (!gotFocus) {
          // Set the focus on the "OK" button...
          okButton.requestFocus();
          // ... and ensure that the button will be activated when
          // hitting <ENTER>.
          JRootPane root = SwingUtilities.getRootPane(okButton);
          if (root != null) {
            root.setDefaultButton(okButton);
          }
          gotFocus = true;
        }else{
        }
      }
    });
  }

  /**
   * Centers the dialog relative to its owner.
   * @param     owner   The owner of the dialog. If it is <null>code</code>,
   *                    the dialog will be placed in the center of the screen.
   */
  private void center(Frame owner)
  {
    Dimension s = getSize();
    int width = s.width;
    int height = s.height;
    int x, y;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    if (owner == null || !owner.isVisible()) {
      x = (screenWidth - width) / 2;
      y = (screenHeight - height) / 2;
    }else{
      int ownerWidth = owner.getWidth();
      int ownerHeight = owner.getHeight();
      Point ownerLocation = owner.getLocationOnScreen();
      x = ownerLocation.x + (ownerWidth - width) / 2;
      y = ownerLocation.y + (ownerHeight - height) / 2;
      if ((x + width) > screenWidth) {
        x = screenWidth - width;
      }
      if ((y + height) > screenHeight) {
        y = screenHeight - height;
      }
      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }
    }
    setLocation(x, y);
  }

  /**
   * Displays the dialog.
   * @return    If the user presses the "OK" button, <code>OK</code> is
   *            returned, otherwise <code>CANCEL</code> is returned.
   */
  int showDialog()
  {
    setVisible(true);
    dispose();
    return result;
  }

  /**
   * Enables or disables the "up", "down", and "delete" buttonis, according
   * to the currently selected node.
   * @param     ft      The currently selected node.
   */
  private void enableButtons(FileTree ft)
  {
    if (ft == null) {
      upButton.setEnabled(false);
      downButton.setEnabled(false);
      deleteButton.setEnabled(false);
    }else{
      TreeNode parent = ft.getParent();
      int n = parent.getChildCount();
      if (n == 1) {
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        deleteButton.setEnabled(true);
      }else{
        int i = parent.getIndex(ft);
        if (i == 0) {
          upButton.setEnabled(false);
          downButton.setEnabled(true);
          deleteButton.setEnabled(true);
        }else{
          if (i == (n-1)) {
            upButton.setEnabled(true);
            downButton.setEnabled(false);
            deleteButton.setEnabled(true);
          }else{
            upButton.setEnabled(true);
            downButton.setEnabled(true);
            deleteButton.setEnabled(true);
          }
        }
      }
    }
  }

  /**
   * Reloads the model of the displayed JTree.
   */
  private void reloadModel()
  {
    // Reloading the model does not preserve the expanded
    // state of the tree nodes, so we have to keep track of it ourselves.
    FileTree root = (FileTree)(tree.getModel().getRoot());
    TreePath rootPath = new TreePath(root.getPath());
    Enumeration<TreePath> e = tree.getExpandedDescendants(rootPath);
    ArrayList<TreePath> a = new ArrayList<TreePath>();
    if (e != null) {
      while (e.hasMoreElements()) {
        a.add(e.nextElement());
      }
    }

    // Reload the tree model.
    ((DefaultTreeModel)(tree.getModel())).reload();

    // Restore the expanded state of the tree nodes.
    int n = a.size();
    for (int j=0; j<n; j++) {
      tree.expandPath(a.get(j));
    }
  }

}
