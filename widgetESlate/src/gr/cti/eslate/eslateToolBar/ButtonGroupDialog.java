package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import gr.cti.eslate.services.name.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements the editing dialog of the button group editor of the
 * E-Slate toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class ButtonGroupDialog extends JDialog
  implements MouseListener, MouseMotionListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The button that adds button groups.
   */
  private JButton addGroupButton;
  /**
   * The button that removes groups.
   */
  private JButton deleteGroupButton;
  /**
   * The button that removes tools.
   */
  private JButton remToolButton;
  /**
   * The tree containing the button groups.
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
   * The description of the layout of the button groups of the toolbar.
   */
  private ButtonGroupLayout bgLayout;
  /**
   * "Hand" cursor.
   */
  private Cursor handCursor;
  /**
   * "Closed hand" cursor.
   */
  private Cursor closedHandCursor;
  /**
   * "Forbidden" cursor.
   */
  private Cursor forbiddenCursor;
  /**
   * Default cursor.
   */
  private Cursor defaultCursor;
  /**
   * The type of cursor currently in use.
   */
  private int currentCursorType;
  /**
   * The node on which the user has pressed the left mouse button.
   */
  private ButtonGroupNode leftClickNode;
  /**
   * Indicates whether we are dragging a node.
   */
  private boolean dragging = false;
  /**
   * The x coordinate of the point where the user has pressed the left
   * mouse button.
   */
  private int clickX;
  /**
   * The y coordinate of the point where the user has pressed the left
   * mouse button.
   */
  private int clickY;
  /**
   * The tolerance for recognizing node  drags as such. Smaller drags are
   * considered to have been caused by shaking hands, and are ignored.
   */
  private final static int CLICKTHRESHOLD = 4;
  /**
   * The label used to provide button feedback for the dragging.
   */
  private JLabel dragLabel;
  /**
   * X Offset for drawing drag label.
   */
  private final static int X_OFFSET =
    ButtonGroupCellRenderer.MAX_ICON_HEIGHT / 2;

  /**
   * Indicates that the user has pressed the "OK" button.
   */
  final static int OK = 0;
  /**
   * Indicates that the user has canceled the dialog.
   */
  final static int CANCEL = 1;

  /**
   * Default cursor type.
   */
  private final static int DEFAULT_CURSOR = 0;
  /**
   * "Hand" cursor type.
   */
  private final static int HAND_CURSOR = 1;
  /**
   * "Closed hand" cursor type.
   */
  private final static int CLOSED_HAND_CURSOR = 2;
  /**
   * "Forbidden" cursor.
   */
  private final static int FORBIDDEN_CURSOR = 3;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ButtonGroupEditor.resources;

  /**
   * Build the dialog.
   * @param     owner           The owner of the dialog.
   * @param     bgLayout        A description of the layout of the button
   *                            groups of the toolbar.
   */
  ButtonGroupDialog(Component owner, ButtonGroupLayout bgLayout)
  {
    super(
      (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, owner)),
      resources.getString("bgDialogTitle"),
      true
    );

    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    buildCursors();
    currentCursorType = DEFAULT_CURSOR;

    this.bgLayout = bgLayout;

    JPanel topPanel = new JPanel(new BorderLayout(0, 0));
    topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));

    // Build the tree.
    ButtonGroupNode root = new ButtonGroupNode();
    ButtonGroupEditorGroupInfoBaseArray bGroups = bgLayout.groups;
    int nGroups = bGroups.size();
    // Add button groups.
    for (int i=0; i<nGroups; i++) {
      ButtonGroupEditorGroupInfo group = bGroups.get(i);
      ButtonGroupNode bNode = new ButtonGroupNode(group);
      root.add(bNode);
      ButtonGroupEditorToolInfoBaseArray tools = group.tools;
      int nTools = tools.size();
      for (int j=0; j<nTools; j++) {
        bNode.add(new ButtonGroupNode(tools.get(j)));
      }
    }
    // Add buttons that do not belong to a button group.
    ButtonGroupEditorToolInfoBaseArray freeButtons = bgLayout.freeButtons;
    int nButtons = freeButtons.size();
    for (int i=0; i<nButtons; i++) {
      ButtonGroupEditorToolInfo button = freeButtons.get(i);
      ButtonGroupNode bNode = new ButtonGroupNode(button);
      root.add(bNode);
    }

    tree = new JTree(root)
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
    tree.setShowsRootHandles(true);
    tree.setBackground(UIManager.getColor("Tree.background"));
    tree.setEditable(true);
    tree.setRowHeight(
      4 + Math.max(tree.getRowHeight(), ButtonGroupCellRenderer.MAX_ICON_HEIGHT)
    );
    ButtonGroupCellRenderer renderer = new ButtonGroupCellRenderer();
    tree.setCellRenderer(renderer);
    tree.setCellEditor(new ButtonGroupCellEditor(tree, renderer));
    tree.addMouseListener(this);
    tree.addMouseMotionListener(this);
    cellEditor = tree.getCellEditor();
    cellEditor.addCellEditorListener(new CellEditorListener(){
      public void editingStopped(ChangeEvent e)
      {
        ButtonGroupNode node =
          (ButtonGroupNode)(tree.getLastSelectedPathComponent());
        String newName = (String)(cellEditor.getCellEditorValue());
        if (node.group != null) {
          try {
            NameServiceContext nsc =
              ButtonGroupDialog.this.bgLayout.groupNames;
            Object o = nsc.lookup(newName);
            if ((o == null) || node.group.equals(o)) {
              nsc.renameNoException(node.group.name, newName);
              node.group.name = newName;
            }else{
              ESlateOptionPane.showMessageDialog(
                ButtonGroupDialog.this, resources.getString("groupExists"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE
              );
            }
          } catch (NameServiceException nse) {
          }
        }else{
          if (node.tool != null) {
            //ButtonGroupNode parent = (ButtonGroupNode)(node.getParent());
            try {
              NameServiceContext nsc =
                ButtonGroupDialog.this.bgLayout.toolNames;
              Object o = nsc.lookup(newName);
              if ((o == null) || node.tool.equals(o)) {
                nsc.renameNoException(node.tool.name, newName);
                node.tool.name = newName;
              }else{
                ESlateOptionPane.showMessageDialog(
                  ButtonGroupDialog.this, resources.getString("toolExists"),
                  resources.getString("error"), JOptionPane.ERROR_MESSAGE
                );
              }
            } catch (NameServiceException nse) {
            }
          }
        }
        tree.repaint();
      }
      public void editingCanceled(ChangeEvent e)
      {
      }
    });
    tree.addTreeSelectionListener(new TreeSelectionListener(){
      public void valueChanged(TreeSelectionEvent e)
      {
        ButtonGroupNode node =
          (ButtonGroupNode)(tree.getLastSelectedPathComponent());
        enableButtons(node);
      }
    });
    JScrollPane scrollPane = new JScrollPane(tree);
    //Dimension scrollPaneSize = new Dimension(300, 300);
    //scrollPane.setPreferredSize(scrollPaneSize);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    Dimension buttonSize = new Dimension(27, 22);
    addGroupButton = new JButton(
      new ImageIcon(ButtonGroupDialog.class.getResource("images/addgroup.gif"))
    );
    addGroupButton.setToolTipText(resources.getString("addGroup"));
    addGroupButton.setEnabled(true);
    addGroupButton.setMargin(new Insets(0, 0, 0, 0));
    addGroupButton.setMaximumSize(buttonSize);
    addGroupButton.setPreferredSize(buttonSize);
    addGroupButton.setMinimumSize(buttonSize);
    addGroupButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processAddGroup(e);
      }
    });
    deleteGroupButton = new JButton(
      new ImageIcon(ButtonGroupDialog.class.getResource("images/delete.gif"))
    );
    deleteGroupButton.setToolTipText(resources.getString("remove"));
    deleteGroupButton.setEnabled(false);
    deleteGroupButton.setMargin(new Insets(0, 0, 0, 0));
    deleteGroupButton.setMaximumSize(buttonSize);
    deleteGroupButton.setPreferredSize(buttonSize);
    deleteGroupButton.setMinimumSize(buttonSize);
    deleteGroupButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processDelete(e);
      }
    });
    remToolButton = new JButton(
      new ImageIcon(ButtonGroupDialog.class.getResource("images/remtool.gif"))
    );
    remToolButton.setToolTipText(resources.getString("remTool"));
    remToolButton.setEnabled(false);
    remToolButton.setMargin(new Insets(0, 0, 0, 0));
    remToolButton.setMaximumSize(buttonSize);
    remToolButton.setPreferredSize(buttonSize);
    remToolButton.setMinimumSize(buttonSize);
    remToolButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processRemTool(e);
      }
    });

    //JPanel toolPanel = new JPanel();
    //toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
    //toolPanel.add(Box.createGlue());
    //toolPanel.add(addGroupButton);
    //toolPanel.add(Box.createVerticalStrut(3));
    //toolPanel.add(deleteGroupButton);
    //toolPanel.add(Box.createVerticalStrut(3));
    //toolPanel.add(remToolButton);
    //toolPanel.add(Box.createGlue());
    ESlateToolBar toolPanel = new ESlateToolBar(ESlateToolBar.VERTICAL);
    toolPanel.setFloatable(false);
    toolPanel.setCentered(true);
    toolPanel.setBorderPainted(false);
    VisualGroup vg = toolPanel.addVisualGroup();
    vg.setSeparation(null); // Use default separation.
    toolPanel.add(vg, addGroupButton);
    toolPanel.add(vg, deleteGroupButton);
    toolPanel.add(vg, remToolButton);

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
    /*
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
    */
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createGlue());
    buttonPanel.add(okButton);
    /*
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(cancelButton);
    */
    buttonPanel.add(Box.createGlue());

    JPanel mainPanel = new JPanel(true);
    mainPanel.setLayout(new BoxLayout(mainPanel, 1));
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(topPanel);
    mainPanel.add(buttonPanel);

    getContentPane().add(mainPanel);
    pack();
    setSize(new Dimension(320, 452));
    center(owner);
    addWindowListener(new WindowAdapter()
    {
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
   * Builds the various cursors used.
   */
  private void buildCursors()
  {
    Toolkit tk = Toolkit.getDefaultToolkit();

    Dimension d = tk.getBestCursorSize(32, 32);
    boolean use32x32;
    if ((d.width >= 32) || (d.height >= 32)) {
      use32x32 = true;
    }else{
      use32x32 = false;
    }
    Point hotspot;
    Image im;

    if (use32x32) {
      hotspot = new Point(5, 0);
      im = tk.createImage(
        ButtonGroupDialog.class.getResource("images/hand.gif")
      );
    }else{
      hotspot = new Point(5, 0);
      im = tk.createImage(
        ButtonGroupDialog.class.getResource("images/hand16.gif")
      );
    }
    try {
      handCursor = tk.createCustomCursor(im, hotspot, "hand");
    } catch (IndexOutOfBoundsException iobe) {
    }

    defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    if (use32x32) {
      hotspot = new Point(7, 5);
      im = tk.createImage(
        ButtonGroupDialog.class.getResource("images/closedhand.gif")
      );
    }else{
      hotspot = new Point(6, 2);
      im = tk.createImage(
        ButtonGroupDialog.class.getResource("images/closedhand16.gif")
      );
    }
    try {
      closedHandCursor = tk.createCustomCursor(im, hotspot, "closedHand");
    } catch (IndexOutOfBoundsException iobe) {
    }

    if (use32x32) {
      im = tk.createImage(
        ButtonGroupDialog.class.getResource("images/forbidden.gif")
      );
    }else{
      im = tk.createImage(
        ButtonGroupDialog.class.getResource("images/forbidden16.gif")
      );
    }
    try {
      forbiddenCursor = tk.createCustomCursor(im, hotspot, "forbiddenHand");
    } catch (IndexOutOfBoundsException iobe) {
    }
  }

  /**
   * Processes clicks on the "add group" button.
   * @param     e       The event sent when the "add group" button is clicked.
   */
  private void processAddGroup(ActionEvent e)
  {
    ButtonGroupLayout bgl = bgLayout;
    String name =
      bgl.groupNames.constructUniqueName(resources.getString("group"));
    ButtonGroupEditorGroupInfo bInfo = new ButtonGroupEditorGroupInfo(
      name, new ButtonGroupEditorToolInfoBaseArray()
    );
    bgl.groups.add(bInfo);
    try {
      bgl.groupNames.bind(name, bInfo);
    } catch (NameServiceException nse) {
    }
    ButtonGroupNode root = (ButtonGroupNode)(tree.getModel().getRoot());
    ButtonGroupNode bNode = new ButtonGroupNode(bInfo);
    boolean added = false;
    int n = root.getChildCount();
    for (int i=0; i<n; i++) {
      ButtonGroupNode node = (ButtonGroupNode)(root.getChildAt(i));
      if (node.tool != null) {
        root.insert(bNode, i);
        added = true;
        break;
      }
    }
    if (!added) {
      root.insert(bNode, n);
    }
    reloadModel();
    tree.setSelectionPath(new TreePath(bNode.getPath()));
  }

  /**
   * Processes clicks on the "remove tool" button.
   * @param     e       The event sent when the "remove tool" button
   *                    is clicked.
   */
  private void processRemTool(ActionEvent e)
  {
    ButtonGroupNode node =
      (ButtonGroupNode)(tree.getLastSelectedPathComponent());
    ButtonGroupNode parent = (ButtonGroupNode)(node.getParent());
    if ((node.tool == null) || parent.isRoot()) {
      // Ignore groups or tools that do not belong to a group.
      return;
    }
    ButtonGroupNode root = (ButtonGroupNode)(tree.getModel().getRoot());
    parent.group.tools.removeElements(node.tool);
    bgLayout.freeButtons.add(node.tool);
    parent.remove(node);
    root.add(node);
    reloadModel();
  }

  /**
   * Processes clicks on the "delete group" button.
   * @param     e       The event sent when the "delete group" button
   *                    is clicked.
   */
  private void processDelete(ActionEvent e)
  {
    ButtonGroupNode node =
      (ButtonGroupNode)(tree.getLastSelectedPathComponent());
    ButtonGroupNode parent = (ButtonGroupNode)(node.getParent());
    if (node.group != null) {
      //ButtonGroupEditorGroupInfo bInfo = node.group;
      bgLayout.groups.removeElements(node.group);
      try {
        bgLayout.groupNames.unbind(node.group.name);
      } catch (NameServiceException nse) {
      }
      ButtonGroupEditorToolInfoBaseArray tools = node.group.tools;
      ButtonGroupEditorToolInfoBaseArray freeButtons = bgLayout.freeButtons;
      int nTools = tools.size();
      for (int i=nTools-1; i>=0; i--) {
        ButtonGroupEditorToolInfo info = tools.get(i);
        tools.remove(i);
        freeButtons.add(info);
        ButtonGroupNode child = (ButtonGroupNode)(node.getChildAt(i));
        node.remove(i);
        parent.add(child);
      }
    }
    parent.remove(node);
    reloadModel();
  }

  /**
   * Centers the dialog relative to its owner.
   * @param     owner   The owner of the dialog. If it is <null>code</code>,
   *                    the dialog will be placed in the center of the screen.
   */
  private void center(Component owner)
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
      Dimension ownerSize = owner.getSize();
      int ownerWidth = ownerSize.width;
      int ownerHeight = ownerSize.height;
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
   * Enables or disables the "add group", "delete group", and "remove tool"
   * buttons, according to the currently selected node.
   * @param     node    The currently selected node.
   */
  private void enableButtons(ButtonGroupNode node)
  {
    if (node == null) {
      // No node selected.
      deleteGroupButton.setEnabled(false);
      remToolButton.setEnabled(false);
    }else{
      // A group is selected
      if (node.group != null) {
        deleteGroupButton.setEnabled(true);
        remToolButton.setEnabled(false);
      }else{
        // A tool is selected
        deleteGroupButton.setEnabled(false);
        if (((ButtonGroupNode)(node.getParent())).isRoot()) {
          // Tool does not belong to a group.
          remToolButton.setEnabled(false);
        }else{
          // Tool belongs to a group.
          remToolButton.setEnabled(true);
        }
      }
    }
  }

  /**
   * Disposes the Dialog and then causes show() to return if it is currently
   * blocked.
   */
  public void dispose()
  {
    super.dispose();
    addGroupButton = null;
    remToolButton = null;
    deleteGroupButton = null;
    tree = null;
    cellEditor = null;
    okButton = null;
    bgLayout = null;
    leftClickNode = null;
    dragLabel = null;
  }

  /**
   * Reloads the model of the displayed JTree.
   */
  private void reloadModel()
  {
    // Reloading the model does not preserve the expanded
    // state of the tree nodes, so we have to keep track of it ourselves.
    ButtonGroupNode root =
      (ButtonGroupNode)(((DefaultTreeModel)(tree.getModel())).getRoot());
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

  /**
   * Returns the node corresponding to the coordinates of a
   * <code>MouseEvent</code>.
   * @param     e       The <code>MouseEvent</code>.
   * @return    The node corresponding to the coordinates of the
   *            <code>MouseEvent</code>.
   */
  private ButtonGroupNode getNodeAt(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();
    int selRow = tree.getRowForLocation(x, y);
    if (selRow != -1) {
      ButtonGroupNode node = (ButtonGroupNode)
        (tree.getPathForLocation(x, y).getLastPathComponent());
      return node;
    }else{
      return null;
    }
  }

  /**
   * Sets the tree's cursor according to a specified node.
   * @param     node    The node.
   */
  private void setCursor(ButtonGroupNode node)
  {
    if ((node != null) && (node.tool != null)) {
      setCursor(HAND_CURSOR);
    }else{
      setCursor(DEFAULT_CURSOR);
    }
  }

  /**
   * Sets the tree's cursor.
   * @param     type    The type of cursor to use. One of
   *                    <code>HAND_CURSOR</code>, <code>DEFAULT_CURSOR</code>.
   */
  private void setCursor(int type)
  {
    if (type != currentCursorType) {
      Cursor c;
      switch (type) {
        case HAND_CURSOR:
          c = handCursor;
          break;
        case CLOSED_HAND_CURSOR:
          c = closedHandCursor;
          break;
        case FORBIDDEN_CURSOR:
          c = forbiddenCursor;
          break;
        default:
          c = defaultCursor;
          break;
      }
      currentCursorType = type;
      tree.setCursor(c);
      if (dragLabel != null) {
        dragLabel.setCursor(c);
      }
    }
  }

  /**
   * Moves a node to the position of another node.
   * @param     from    The node to move.
   * @param     to      The node to whose position the <code>from</code> node
   *                    will be moved.
   */
  private void moveNode(ButtonGroupNode from, ButtonGroupNode to)
  {
    // No destination.
    if (to == null) {
      return;
    }

    // From and to nodes are the same.
    if (from.equals(to)) {
      return;
    }

    // Can't move a group to the position of a tool.
    if ((from.group != null) && (to.tool != null)) {
      return;
    }

    // Move a tool to a different group.
    if ((from.tool != null) && (to.group != null)) {
      ButtonGroupNode oldParent = (ButtonGroupNode)(from.getParent());
      ButtonGroupNode newParent = to;
      if (!oldParent.equals(newParent)) {
        ButtonGroupEditorToolInfoBaseArray oldTools;
        if (oldParent.isRoot()) {
          oldTools = bgLayout.freeButtons;
        }else{
          oldTools = oldParent.group.tools;
        }
        int fromIndex = oldTools.indexOf(from.tool);
        ButtonGroupEditorToolInfo info = oldTools.get(fromIndex);
        oldTools.remove(fromIndex);
        ButtonGroupEditorToolInfoBaseArray newTools = newParent.group.tools;
        newTools.add(info);
        //
        oldParent.remove(from);
        newParent.add(from);
        reloadModel();
        tree.setSelectionPath(new TreePath(from.getPath()));
        return;
      }
    }

    // Move a tool to the group of a different tool.
    if ((from.tool != null) && (to.tool != null)) {
      ButtonGroupNode oldParent = (ButtonGroupNode)(from.getParent());
      ButtonGroupNode newParent = (ButtonGroupNode)(to.getParent());
      if (!oldParent.equals(newParent)) {
        ButtonGroupEditorToolInfoBaseArray oldTools;
        if (oldParent.isRoot()) {
          oldTools = bgLayout.freeButtons;
        }else{
          oldTools = oldParent.group.tools;
        }
        //int fromIndex = oldParent.getIndex(from);
        int fromIndex = oldTools.indexOf(from.tool);
        ButtonGroupEditorToolInfo info = oldTools.get(fromIndex);
        oldTools.remove(fromIndex);
        ButtonGroupEditorToolInfoBaseArray newTools;
        if (newParent.isRoot()) {
          newTools = bgLayout.freeButtons;
        }else{
          newTools = newParent.group.tools;
        }
        newTools.add(info);
        //
        oldParent.remove(from);
        newParent.add(from);
        reloadModel();
        tree.setSelectionPath(new TreePath(from.getPath()));
      }
      return;
    }

    // Not reached.
    return;
  }


  /**
   * Handle mouse pressed events on the tree.
   * @param     e       The event to handle.
   *
   */
  public void mousePressed(MouseEvent e)
  {
    ButtonGroupNode node = getNodeAt(e);

    // Check for popup menu mouse button. Since isPopupTrigger does not
    // appear to be working under Windows, we also check explicitly for
    // the right button.
    boolean rightButtonPressed =
      e.isPopupTrigger() || (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0;
    if (rightButtonPressed) {
      // Do nothing.
    }else{
      clickX = e.getX();
      clickY = e.getY();
      leftClickNode = node;
    }
  }

  /**
   * Handle mouse released events on the tree.
   * @param     e       The event to handle.
   */
  public void mouseReleased(MouseEvent e)
  {
    if (dragging) {
      dragLabel.setVisible(false);
      getLayeredPane().remove(dragLabel);
      dragLabel = null;
      ButtonGroupNode node = getNodeAt(e);
      moveNode(leftClickNode, node);
      dragging = false;
      tree.setEditable(true);
      setCursor(node);
    }
    leftClickNode = null;
  }

  /**
   * Handle mouse dragged events on the tree.
   * @param     e       The event to handle.
   */
  public void mouseDragged(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();

    // Ignore small drags on a node.
    if ((!dragging) && (leftClickNode != null) &&
        (Math.abs(clickX - x) < CLICKTHRESHOLD) &&
        (Math.abs(clickY - y) < CLICKTHRESHOLD)) {
      return;
    }
    ButtonGroupNode node = getNodeAt(e);
    JLayeredPane lp = getLayeredPane();
    if (!dragging) {
      if (node == null) {
        return;
      }
      dragging = true;
      tree.setEditable(false);
      setCursor(CLOSED_HAND_CURSOR);
      dragLabel = new JLabel(node.toString());
      dragLabel.setIcon(node.imageIcon);
      Dimension labelSize = dragLabel.getPreferredSize();
      lp.add(dragLabel, JLayeredPane.DRAG_LAYER);
      Point p = SwingUtilities.convertPoint(
        tree,
        x - X_OFFSET, y - labelSize.height / 2,
        lp
      );
      dragLabel.setBounds(p.x, p.y, labelSize.width, labelSize.height);
    }else{
      Dimension labelSize = dragLabel.getSize();
      Point p = SwingUtilities.convertPoint(
        tree,
        x - X_OFFSET, y - labelSize.height / 2,
        lp
      );
      dragLabel.setLocation(p);
      if ((node == null) ||
          ((leftClickNode.group != null) && (node.tool != null))) {
        setCursor(FORBIDDEN_CURSOR);
        tree.setSelectionPath(new TreePath(leftClickNode.getPath()));
      }else{
        setCursor(CLOSED_HAND_CURSOR);
        tree.setSelectionPath(new TreePath(node.getPath()));
      }
    }
  }

  /**
   * Handle mouse moved events on the tree.
   * @param     e       The event to handle.
   */
  public void mouseMoved(MouseEvent e)
  {
    if (!dragging) {
      ButtonGroupNode node = getNodeAt(e);
      setCursor(node);
    }
  }

  /**
   * Handle mouse exited events on the tree.
   * @param     e       The event to handle.
   */
  public void mouseExited(MouseEvent e)
  {
  }

  /**
   * Handle mouse entered events on the tree.
   * @param     e       The event to handle.
   */
  public void mouseEntered(MouseEvent e)
  {
    if (!dragging) {
      ButtonGroupNode node = getNodeAt(e);
      setCursor(node);
    }
  }

  /**
   * Handle mouse clicked events on the tree.
   * @param     e       The event to handle.
   */
  public void mouseClicked(MouseEvent e)
  {
  }

}
