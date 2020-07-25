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
 * This class implements the editing dialog of the visual group editor of the
 * E-Slate toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 */
class VisualGroupDialog extends JDialog
  implements MouseListener, MouseMotionListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The button that moves entries up.
   */
  private JButton upButton;
  /**
   * The button that moves entries down.
   */
  private JButton downButton;
  /**
   * The button that adds visual groups.
   */
  private JButton addGroupButton;
  /**
   * The button that adds tools.
   */
  private JButton addToolButton;
  /**
   * The button that removes entries.
   */
  private JButton deleteButton;
  /**
   * The reset to defaul state button.
   */
  private JButton defaultButton = null;
  /**
   * The tree containing the visual groups.
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
   * The description of the layout of the visual groups of the toolbar.
   */
  private VisualGroupLayout vgLayout;
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
   * The node on which the user has pressed the right mouse button.
   */
  private VisualGroupNode rightClickNode;
  /**
   * The node on which the user has pressed the left mouse button.
   */
  private VisualGroupNode leftClickNode;
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
   * The label used to provide visual feedback for the dragging.
   */
  private JLabel dragLabel;
  /**
   * X Offset for drawing drag label.
   */
  private final static int X_OFFSET =
    VisualGroupCellRenderer.MAX_ICON_HEIGHT / 2;

  /**
   * Indicates that the user has pressed the "OK" button.
   */
  final static int OK = 0;
  /**
   * Indicates that the user has canceled the dialog.
   */
  final static int CANCEL = 1;
  /**
   * Indicates that the user pressed the "reset to default" button.
   */
  final static int RESET_TO_DEFAULT = 2;

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
  private static ResourceBundle resources = VisualGroupEditor.resources;

  /**
   * Build the dialog.
   * @param     owner           The owner of the dialog.
   * @param     vgLayout        A description of the layout of the visual
   *                            groups of the toolbar.
   */
  VisualGroupDialog(Component owner, VisualGroupLayout vgLayout)
  {
    super(
      (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, owner)),
      resources.getString("vgDialogTitle"),
      true
    );

    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    buildCursors();
    currentCursorType = DEFAULT_CURSOR;

    this.vgLayout = vgLayout;

    JPanel topPanel = new JPanel(new BorderLayout(0, 0));
    topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));

    VisualGroupNode root = new VisualGroupNode();
    VisualGroupEditorGroupInfoBaseArray vGroups = vgLayout.groups;
    int nGroups = vGroups.size();
    for (int i=0; i<nGroups; i++) {
      VisualGroupEditorGroupInfo group = vGroups.get(i);
      VisualGroupNode vNode = new VisualGroupNode(group);
      root.add(vNode);
      VisualGroupEditorToolInfoBaseArray tools = group.tools;
      int nTools = tools.size();
      for (int j=0; j<nTools; j++) {
        vNode.add(new VisualGroupNode(tools.get(j)));
      }
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
      4 + Math.max(tree.getRowHeight(), VisualGroupCellRenderer.MAX_ICON_HEIGHT)
    );
    VisualGroupCellRenderer renderer = new VisualGroupCellRenderer();
    tree.setCellRenderer(renderer);
    tree.setCellEditor(new VisualGroupCellEditor(tree, renderer));
    tree.addMouseListener(this);
    tree.addMouseMotionListener(this);
    cellEditor = tree.getCellEditor();
    cellEditor.addCellEditorListener(new CellEditorListener(){
      public void editingStopped(ChangeEvent e)
      {
        VisualGroupNode node =
          (VisualGroupNode)(tree.getLastSelectedPathComponent());
        String newName = (String)(cellEditor.getCellEditorValue());
        if (node.group != null) {
          try {
            NameServiceContext nsc =
              VisualGroupDialog.this.vgLayout.groupNames;
            Object o = nsc.lookup(newName);
            if ((o == null) || node.group.equals(o)) {
              nsc.renameNoException(node.group.name, newName);
              node.group.name = newName;
            }else{
              ESlateOptionPane.showMessageDialog(
                VisualGroupDialog.this, resources.getString("groupExists"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE
              );
            }
          } catch (NameServiceException nse) {
          }
        }else{
          if (node.tool != null) {
            //VisualGroupNode parent = (VisualGroupNode)(node.getParent());
            try {
              NameServiceContext nsc =
                VisualGroupDialog.this.vgLayout.toolNames;
              Object o = nsc.lookup(newName);
              if ((o == null) || node.tool.equals(o)) {
                nsc.renameNoException(node.tool.name, newName);
                node.tool.name = newName;
              }else{
                ESlateOptionPane.showMessageDialog(
                  VisualGroupDialog.this, resources.getString("toolExists"),
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
        VisualGroupNode node =
          (VisualGroupNode)(tree.getLastSelectedPathComponent());
        enableButtons(node);
      }
    });
    JScrollPane scrollPane = new JScrollPane(tree);
    //Dimension scrollPaneSize = new Dimension(300, 300);
    //scrollPane.setPreferredSize(scrollPaneSize);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    Dimension buttonSize = new Dimension(27, 22);
    upButton = new JButton(
      new ImageIcon(VisualGroupDialog.class.getResource("images/upArrow.gif"))
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
        processUp(e);
      }
    });
    downButton = new JButton(
      new ImageIcon(VisualGroupDialog.class.getResource("images/downArrow.gif"))
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
        processDown(e);
      }
    });
    addGroupButton = new JButton(
      new ImageIcon(VisualGroupDialog.class.getResource("images/addgroup.gif"))
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
    addToolButton = new JButton(
      new ImageIcon(VisualGroupDialog.class.getResource("images/addtool.gif"))
    );
    addToolButton.setToolTipText(resources.getString("addTool"));
    addToolButton.setEnabled(false);
    addToolButton.setMargin(new Insets(0, 0, 0, 0));
    addToolButton.setMaximumSize(buttonSize);
    addToolButton.setPreferredSize(buttonSize);
    addToolButton.setMinimumSize(buttonSize);
    addToolButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processAddTool(e);
      }
    });
    deleteButton = new JButton(
      new ImageIcon(VisualGroupDialog.class.getResource("images/delete.gif"))
    );
    deleteButton.setToolTipText(resources.getString("remove"));
    deleteButton.setEnabled(false);
    deleteButton.setMargin(new Insets(0, 0, 0, 0));
    deleteButton.setMaximumSize(buttonSize);
    deleteButton.setPreferredSize(buttonSize);
    deleteButton.setMinimumSize(buttonSize);
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processDelete(e);
      }
    });

    ESlateToolBar toolPanel = new ESlateToolBar(ESlateToolBar.VERTICAL);
    toolPanel.setFloatable(false);
    toolPanel.setCentered(true);
    toolPanel.setBorderPainted(false);
    VisualGroup vg = toolPanel.addVisualGroup();
    vg.setSeparation(null); // Use default separation.
    toolPanel.add(upButton);
    toolPanel.add(downButton);
    toolPanel.add(addGroupButton);
    toolPanel.add(addToolButton);
    toolPanel.add(deleteButton);

    // If a default state setter has been defined for the toolbar,
    // add a "set default state" button. If not, omit it, to avoid confusion.
    if (vgLayout.toolBar.getDefaultStateSetter() != null) {
      defaultButton = new JButton(
        new ImageIcon(VisualGroupDialog.class.getResource("images/default.gif"))
      );
      defaultButton.setToolTipText(resources.getString("default"));
      defaultButton.setEnabled(true);
      defaultButton.setMargin(new Insets(0, 0, 0, 0));
      defaultButton.setMaximumSize(buttonSize);
      defaultButton.setPreferredSize(buttonSize);
      defaultButton.setMinimumSize(buttonSize);
      defaultButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          processDefault(e);
        }
      });
      toolPanel.add(defaultButton);
    }

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
        VisualGroupDialog.class.getResource("images/hand.gif")
      );
    }else{
      hotspot = new Point(5, 0);
      im = tk.createImage(
        VisualGroupDialog.class.getResource("images/hand16.gif")
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
        VisualGroupDialog.class.getResource("images/closedhand.gif")
      );
    }else{
      hotspot = new Point(6, 2);
      im = tk.createImage(
        VisualGroupDialog.class.getResource("images/closedhand16.gif")
      );
    }
    try {
      closedHandCursor = tk.createCustomCursor(im, hotspot, "closedHand");
    } catch (IndexOutOfBoundsException iobe) {
    }

    if (use32x32) {
      im = tk.createImage(
        VisualGroupDialog.class.getResource("images/forbidden.gif")
      );
    }else{
      im = tk.createImage(
        VisualGroupDialog.class.getResource("images/forbidden16.gif")
      );
    }
    try {
      forbiddenCursor = tk.createCustomCursor(im, hotspot, "forbiddenHand");
    } catch (IndexOutOfBoundsException iobe) {
    }
  }

  /**
   * Processes clicks on the "up" buton.
   * @param     e       The event sent when the "up" button is clicked.
   */
  private void processUp(ActionEvent e)
  {
    VisualGroupNode node =
      (VisualGroupNode)(tree.getLastSelectedPathComponent());
    VisualGroupNode parent = (VisualGroupNode)(node.getParent());
    int i = parent.getIndex(node);
    parent.remove(node);
    parent.insert(node, i-1);
    if (node.group != null) {
      VisualGroupEditorGroupInfoBaseArray a = vgLayout.groups;
      VisualGroupEditorGroupInfo info = a.get(i);
      a.remove(i);
      a.add(i-1, info);
    }else{
      if (node.tool != null) {
        VisualGroupEditorToolInfoBaseArray a = parent.group.tools;
        VisualGroupEditorToolInfo info = a.get(i);
        a.remove(i);
        a.add(i-1, info);
      }
    }
    reloadModel();
    tree.setSelectionPath(new TreePath(node.getPath()));
  }

  /**
   * Processes clicks on the "down" button.
   * @param     e       The event sent when the "down" button is clicked.
   */
  private void processDown(ActionEvent e)
  {
    VisualGroupNode node =
      (VisualGroupNode)(tree.getLastSelectedPathComponent());
    VisualGroupNode parent = (VisualGroupNode)(node.getParent());
    int i = parent.getIndex(node);
    parent.remove(node);
    parent.insert(node, i+1);
    if (node.group != null) {
      VisualGroupEditorGroupInfoBaseArray a = vgLayout.groups;
      VisualGroupEditorGroupInfo info = a.get(i);
      a.remove(i);
      a.add(i+1, info);
    }else{
      if (node.tool != null) {
        VisualGroupEditorToolInfoBaseArray a = parent.group.tools;
        VisualGroupEditorToolInfo info = a.get(i);
        a.remove(i);
        a.add(i+1, info);
      }
    }
    reloadModel();
    tree.setSelectionPath(new TreePath(node.getPath()));
  }

  /**
   * Processes clicks on the "add group" button.
   * @param     e       The event sent when the "add group" button is clicked.
   */
  private void processAddGroup(ActionEvent e)
  {
    String name =
      vgLayout.groupNames.constructUniqueName(resources.getString("group"));
    VisualGroupEditorGroupInfo vInfo = new VisualGroupEditorGroupInfo(
      name, true, new VisualGroupEditorToolInfoBaseArray()
    );
    vgLayout.groups.add(vInfo);
    try {
      vgLayout.groupNames.bind(name, vInfo);
    } catch (NameServiceException nse) {
    }
    VisualGroupNode root = (VisualGroupNode)(tree.getModel().getRoot());
    VisualGroupNode vNode = new VisualGroupNode(vInfo);
    root.add(vNode);
    reloadModel();
    tree.setSelectionPath(new TreePath(vNode.getPath()));
  }

  /**
   * Processes clicks on the "add tool" button.
   * @param     e       The event sent when the "add tool" button is clicked.
   */
  private void processAddTool(ActionEvent e)
  {
    VisualGroupNode node =
      (VisualGroupNode)(tree.getLastSelectedPathComponent());
    if (node.group == null) {
      node = (VisualGroupNode)(node.getParent());
    }
    ArrayList <Object> a = getClassOfComponentToAdd();
    if (a != null) {
      Class<?> c = (Class<?>)(a.get(0));
      String name = (String)(a.get(1));
      if (c != null) {
        VisualGroupEditorToolInfo tInfo = new VisualGroupEditorToolInfo(
          name, c, null, true
        );
        node.group.tools.add(tInfo);
        try {
          vgLayout.toolNames.bind(name, tInfo);
        } catch (NameServiceException nse) {
        }
        VisualGroupNode tNode = new VisualGroupNode(tInfo);
        node.add(tNode);
        reloadModel();
        tree.setSelectionPath(new TreePath(tNode.getPath()));
      }else{
        ESlateOptionPane.showMessageDialog(
          this,
          resources.getString("classNotFound1") + name +
            resources.getString("classNotFound2"),
          resources.getString("error"),
          JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }

  /**
   * Presents the user with a dialog from which they can select the class of a
   * component to add to the toolbar.
   * @return    An ArrayList containing two elements. The first element is the
   *            class of the component, and the second is its name. The name
   *            is either the localized name of the class, if the class is one
   *            of the classes presented to the user via the dialog, or null,
   *            if the user has specified an arbitrary class. If the name is
   *            not unique, then a "_", followed by a number, will be added to
   *            the name to make it unique. If the user
   *            cancels the dialog, <code>null</code> is returned.
   */
  private ArrayList<Object> getClassOfComponentToAdd()
  {
    ClassDialog cd = new ClassDialog(this);
    if (cd.showDialog() == ClassDialog.OK) {
      Class<?> cls = cd.selectedClass;
      String cName =
        vgLayout.toolNames.constructUniqueName(cd.selectedClassName);
      ArrayList<Object> a = new ArrayList<Object>();
      a.add(cls);
      a.add(cName);
      return a;
    }else{
      return null;
    }
  }

  /**
   * Processes clicks on the "delete" button.
   * @param     e       The event sent when the "delete" button is clicked.
   */
  private void processDelete(ActionEvent e)
  {
    VisualGroupNode node =
      (VisualGroupNode)(tree.getLastSelectedPathComponent());
    VisualGroupNode parent = (VisualGroupNode)(node.getParent());
    if (node.group != null) {
      //VisualGroupEditorGroupInfo vInfo = node.group;
      vgLayout.groups.removeElements(node.group);
      try {
        vgLayout.groupNames.unbind(node.group.name);
      } catch (NameServiceException nse) {
      }
    }else{
      try {
        parent.group.tools.removeElements(node.tool);
        vgLayout.toolNames.unbind(node.tool.name);
      } catch (NameServiceException nse) {
      }
    }
    parent.remove(node);
    reloadModel();
  }

  /**
   * Processes clicks on the "reset to default state" button.
   * @param     e       The event sent when the "reset to default state"
   *                    button is pressed.
   */
  private void processDefault(ActionEvent e)
  {
    int confirm = ESlateOptionPane.showConfirmDialog(
      this,
      resources.getString("confirmDefault"),
      resources.getString("confirm"),
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE
    );
    if (confirm == JOptionPane.YES_OPTION) {
      ESlateToolBar toolbar = vgLayout.toolBar;
      toolbar.setDefaultState();
      result = RESET_TO_DEFAULT;
      setVisible(false);
    }
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
   *            returned, <code>CANCEL</code> if the dialog is cancelled, and
   *            <code>RESET_TO_DEFAULT</code> if the "reset to default" button
   *            is pressed.
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
   * @param     node    The currently selected node.
   */
  private void enableButtons(VisualGroupNode node)
  {
    if (node == null) {
      upButton.setEnabled(false);
      downButton.setEnabled(false);
      deleteButton.setEnabled(false);
      addToolButton.setEnabled(false);
    }else{
      deleteButton.setEnabled(true);
      addToolButton.setEnabled(true);
      TreeNode parent = node.getParent();
      int n = parent.getChildCount();
      if (n == 1) {
        upButton.setEnabled(false);
        downButton.setEnabled(false);
      }else{
        int i = parent.getIndex(node);
        if (i == 0) {
          upButton.setEnabled(false);
          downButton.setEnabled(true);
        }else{
          if (i == (n-1)) {
            upButton.setEnabled(true);
            downButton.setEnabled(false);
          }else{
            upButton.setEnabled(true);
            downButton.setEnabled(true);
          }
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
    addToolButton = null;
    deleteButton = null;
    defaultButton = null;
    tree = null;
    cellEditor = null;
    okButton = null;
    vgLayout = null;
    rightClickNode = null;
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
    VisualGroupNode root =
      (VisualGroupNode)(((DefaultTreeModel)(tree.getModel())).getRoot());
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
  private VisualGroupNode getNodeAt(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();
    int selRow = tree.getRowForLocation(x, y);
    if (selRow != -1) {
      VisualGroupNode node = (VisualGroupNode)
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
  private void setCursor(VisualGroupNode node)
  {
    if ((node != null) &&((node.group != null) || (node.tool != null))) {
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
   * Pops up a property menu for a given node.
   * @param     node    The node.
   * @param     x       The x coordinate of the point where the menu should
   *                    appear.
   * @param     y       The y coordinate of the point where the menu should
   *                    appear.
   */
  private void propertyPopup(VisualGroupNode node, int x, int y)
  {
    ESlateJPopupMenu popup = new ESlateJPopupMenu();
    popup.setLightWeightPopupEnabled(false);
    JMenuItem propMenuItem = new JMenuItem(resources.getString("properties"));
    rightClickNode = node;
    if (node.group != null) {
      propMenuItem.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
          VisualGroupPropertiesDialog dialog =
            new VisualGroupPropertiesDialog(
              VisualGroupDialog.this, rightClickNode.group
            );
          if (dialog.showDialog() == VisualGroupPropertiesDialog.OK) {
            tree.repaint();
          }
        }
      });
    }else{
      propMenuItem.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
          ToolPropertiesDialog dialog =
            new ToolPropertiesDialog(
              VisualGroupDialog.this, rightClickNode.tool
            );
          if (dialog.showDialog() == ToolPropertiesDialog.OK) {
            tree.repaint();
          }
        }
      });
    }
    popup.add(propMenuItem);
    popup.show(getContentPane(), x, y);
  }

  /**
   * Moves a node to the position of another node.
   * @param     from    The node to move.
   * @param     to      The node to whose position the <code>from</code> node
   *                    will be moved.
   */
  private void moveNode(VisualGroupNode from, VisualGroupNode to)
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
      VisualGroupNode oldParent = (VisualGroupNode)(from.getParent());
      VisualGroupNode newParent = to;
      if (!oldParent.equals(newParent)) {
        int fromIndex = oldParent.getIndex(from);
        VisualGroupEditorToolInfoBaseArray oldTools = oldParent.group.tools;
        VisualGroupEditorToolInfo info = oldTools.get(fromIndex);
        oldTools.remove(fromIndex);
        VisualGroupEditorToolInfoBaseArray newTools = newParent.group.tools;
        newTools.add(info);
        //
        oldParent.remove(from);
        newParent.add(from);
        reloadModel();
        tree.setSelectionPath(new TreePath(from.getPath()));
        return;
      }
    }

    // Move a tool to the position of a different tool.
    if ((from.tool != null) && (to.tool != null)) {
      VisualGroupNode oldParent = (VisualGroupNode)(from.getParent());
      VisualGroupNode newParent = (VisualGroupNode)(to.getParent());
      int fromIndex = oldParent.getIndex(from);
      int toIndex = newParent.getIndex(to);
      VisualGroupEditorToolInfoBaseArray oldTools = oldParent.group.tools;
      VisualGroupEditorToolInfo info = oldTools.get(fromIndex);
      oldTools.remove(fromIndex);
      VisualGroupEditorToolInfoBaseArray newTools = newParent.group.tools;
      newTools.add(toIndex, info);
      //
      oldParent.remove(from);
      newParent.insert(from, toIndex);
      reloadModel();
      tree.setSelectionPath(new TreePath(from.getPath()));
      return;
    }

    // Move a group to the position of a different group.
    if ((from.group != null) && (to.group != null)) {
      VisualGroupNode root = (VisualGroupNode)(from.getParent());
      int fromIndex = root.getIndex(from);
      int toIndex = root.getIndex(to);
      VisualGroupEditorGroupInfoBaseArray groups = vgLayout.groups;
      VisualGroupEditorGroupInfo info = groups.get(fromIndex);
      groups.remove(fromIndex);
      groups.add(toIndex, info);
      //
      root.remove(from);
      root.insert(from, toIndex);
      reloadModel();
      tree.setSelectionPath(new TreePath(from.getPath()));
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
    VisualGroupNode node = getNodeAt(e);

    // Check for popup menu mouse button. Since isPopupTrigger does not
    // appear to be working under Windows, we also check explicitly for
    // the right button.
    boolean rightButtonPressed =
      e.isPopupTrigger() || (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0;
    if (rightButtonPressed) {
      if ((node != null) && !node.isRoot()) {
        propertyPopup(node, e.getX(), e.getY());
      }
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
      VisualGroupNode node = getNodeAt(e);
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
    VisualGroupNode node = getNodeAt(e);
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
      VisualGroupNode node = getNodeAt(e);
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
      VisualGroupNode node = getNodeAt(e);
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
