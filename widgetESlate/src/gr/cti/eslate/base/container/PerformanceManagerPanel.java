package gr.cti.eslate.base.container;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import gr.cti.eslate.utils.*;

/**
 * This class implements the Graphical User Interface of the
 * PerformanceManager, allowing the user to selectively enable or disable the
 * PerformanceTimerGroups that have been registered with the
 * PerformanceManager.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.2, 1-Jun-2006
 */
public class PerformanceManagerPanel extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The tree displaying the registered PerformanceTimerGroups.
   */
  private JTree tree;
  /**
   * The currently selected activation policy.
   */
  private int policy = PerformanceTimerGroup.DONT_ENABLE;
  /**
   * A timer used to schedule the processing of events generated by clicking
   * on the tree.
   */
  private java.util.Timer timer = new java.util.Timer();
  /**
   * The task that will process the current mouse click on the tree.
   */
  private UpdateTask updateTask;

  /**
   * Localized resources.
   */
  static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.base.container.PerformancePanelResource",
    Locale.getDefault()
  );

  /**
   * The menu item correposnding to DONT_ENABLE.
   */
  private JCheckBoxMenuItem dontItem;
  /**
   * The menu item correposnding to ENABLE_IMMEDIATE.
   */
  private JCheckBoxMenuItem immItem;
  /**
   * The menu item correposnding to ENABLE_ALL.
   */
  private JCheckBoxMenuItem allItem;
  /**
   * The button correposnding to DONT_ENABLE.
   */
  private JToggleButton dontButton;
  /**
   * The button correposnding to ENABLE_IMMEDIATE.
   */
  private JToggleButton immButton;
  /**
   * The button correposnding to ENABLE_ALL.
   */
  private JToggleButton allButton;
  /**
   * The popup menu.
   */
  private ESlateJPopupMenu popup;
  /**
   * The PerformanceTimerGroup correposnding to the node on which the popup
   * menu is shown.
   */
  private PerformanceTimerGroup popupPTG;

  /**
   * Create the PerformanceManagerPanel.
   */
  PerformanceManagerPanel()
  {
    super();
    setLayout(new BorderLayout());
    initTools();
    initTree();
    initPopup();
  }

  /**
   * Initialize the tree.
   */
  private void initTree()
  {
    TimerTreeNode node = createTree();
    tree = new JTree(node)
    {
      /**
       * Serialization version.
       */
      final static long serialVersionUID = 1L;
      
      public void updateUI()
      {
        super.updateUI();
        setRowHeight(
          Math.max(getRowHeight(), (4+TimerCellRenderer.icon1.getIconHeight()))
        );
        setBackground(UIManager.getColor("Panel.background"));
      }
    };
    tree.setRootVisible(true);
    tree.setCellRenderer(new TimerCellRenderer());
    tree.setRowHeight(
      Math.max(
        tree.getRowHeight(), (4+TimerCellRenderer.icon1.getIconHeight())
      )
    );
    tree.setBackground(UIManager.getColor("Panel.background"));
    tree.addMouseListener(new MouseAdapter(){
      /**
       * Handle a click with the the control key pressed down.
       * @param e       The event sent by the click.
       */
      private void controlClick(MouseEvent e)
      {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        if (selRow != -1) {
          TreePath selPath = tree.getPathForRow(selRow);
          PerformanceTimerGroup ptg =
            ((TimerTreeNode)(selPath.getLastPathComponent())).timer;
          if (ptg != null) {
            ptg.toggleTimeMode();
          }
        }
      }
      /**
       * Handle a click of the menu popup key.
       * @param e       The event sent by the click.
       */
      private void popupClick(MouseEvent e)
      {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        if (selRow != -1) {
          TreePath selPath = tree.getPathForRow(selRow);
          PerformanceTimerGroup ptg =
            ((TimerTreeNode)(selPath.getLastPathComponent())).timer;
          if (ptg != null) {
            popupPTG = ptg;
            popup.show(tree, e.getX(), e.getY());
          }
        }
      }
      /**
       * Handle a plain click, either single or double.
       * @param e       The event sent by the click.
       */
      private void plainClick(MouseEvent e)
      {
        // Delay processing the click for 250 msecs, to see if it was
        // the first click of a double click.
        if (updateTask == null) {
          updateTask = new UpdateTask(e);
          timer.schedule(updateTask, 250L);
        }
      }
      public void mousePressed(MouseEvent e)
      {
        if (e.getClickCount() == 1) {
          // Check if this is a click with the control button pressed.
          if (e.isControlDown()) {
            controlClick(e);
          }else{
          // Check for popup menu mouse button. Since isPopupTrigger does not
          // appear to be working under Windows, we also check explicitly for
          // the right button.
            if (e.isPopupTrigger() ||
                (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
              popupClick(e);
            }else{
              // This is plain click, either single or double.
              plainClick(e);
            }
          }
        }else{
          // Cancel the task that would have processed the first click of this
          // double click as a single click.
          if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
          }
          // Treat double control-clicks as two single control-clicks.
          if (e.isControlDown()) {
            controlClick(e);
          }
        }
      }
    });
    JScrollPane scrollPane = new JScrollPane(tree);
    scrollPane.setBorder(
      new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.LOWERED)
    );
    Dimension scrollPaneSize = new Dimension(300, 300);
    scrollPane.setPreferredSize(scrollPaneSize);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Initialize the menu bar and the tool bar.
   */
  private void initTools()
  {
    JPanel top = new JPanel();
    top.setLayout(new BorderLayout());
    dontButton = new JToggleButton(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("dont1.gif")
      )
    );
    dontButton.setSelectedIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("dont2.gif")
      )
    );
    dontButton.setToolTipText(resources.getString("dontActivateTip"));
    dontButton.setSelected(true);
    dontButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        setPolicy(PerformanceTimerGroup.DONT_ENABLE);
      }
    });
    immButton = new JToggleButton(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("immediate1.gif")
      )
    );
    immButton.setSelectedIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("immediate2.gif")
      )
    );
    immButton.setToolTipText(resources.getString("immediateTip"));
    immButton.setSelected(false);
    immButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        setPolicy(PerformanceTimerGroup.ENABLE_IMMEDIATE);
      }
    });
    allButton = new JToggleButton(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("all1.gif")
      )
    );
    allButton.setToolTipText(resources.getString("activateAllTip"));
    allButton.setSelectedIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("all2.gif")
      )
    );
    allButton.setSelected(false);
    allButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        setPolicy(PerformanceTimerGroup.ENABLE_ALL);
      }
    });
    ButtonGroup group = new ButtonGroup();
    group.add(dontButton);
    group.add(immButton);
    group.add(allButton);

    JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(false);
    toolBar.add(dontButton);
    toolBar.add(immButton);
    toolBar.add(allButton);


    JMenu menu = new JMenu(resources.getString("activationPolicy"));
    dontItem = new JCheckBoxMenuItem(resources.getString("dontActivate"));
    dontItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        setPolicy(PerformanceTimerGroup.DONT_ENABLE);
      }
    });
    dontItem.setSelected(true);
    menu.add(dontItem);
    immItem = new JCheckBoxMenuItem(resources.getString("immediate"));
    immItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        setPolicy(PerformanceTimerGroup.ENABLE_IMMEDIATE);
      }
    });
    immItem.setSelected(false);
    menu.add(immItem);
    allItem = new JCheckBoxMenuItem(resources.getString("activateAll"));
    allItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        setPolicy(PerformanceTimerGroup.ENABLE_ALL);
      }
    });
    allItem.setSelected(false);
    menu.add(allItem);

    JMenuBar menuBar = new JMenuBar();
    menuBar.setBorder(null);
    menuBar.add(menu);

    top.add(menuBar, BorderLayout.NORTH);
    top.add(toolBar, BorderLayout.CENTER);
    add(top, BorderLayout.NORTH);
  }

  /**
   * Initialize the popup menu.
   */
  private void initPopup()
  {
    popup = new ESlateJPopupMenu();
    popup.setLightWeightPopupEnabled(false);

    JMenuItem mi = new JMenuItem(resources.getString("dontActivate1"));
    mi.setIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("dont3.gif")
      )
    );
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        MenuSelectionManager.defaultManager().clearSelectedPath();
        popupPTG.setDisplayEnabled(true, PerformanceTimerGroup.DONT_ENABLE);
        redoGUI();
        popupPTG = null;
      }
    });
    popup.add(mi);
    mi = new JMenuItem(resources.getString("immediate1"));
    mi.setIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("immediate3.gif")
      )
    );
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        MenuSelectionManager.defaultManager().clearSelectedPath();
        popupPTG.setDisplayEnabled(
          true, PerformanceTimerGroup.ENABLE_IMMEDIATE
        );
        redoGUI();
        popupPTG = null;
      }
    });
    popup.add(mi);
    mi = new JMenuItem(resources.getString("activateAll1"));
    mi.setIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("all3.gif")
      )
    );
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        MenuSelectionManager.defaultManager().clearSelectedPath();
        popupPTG.setDisplayEnabled(true, PerformanceTimerGroup.ENABLE_ALL);
        redoGUI();
        popupPTG = null;
      }
    });
    popup.add(mi);

    mi = new JMenuItem(resources.getString("dontActivate0"));
    mi.setIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("dont4.gif")
      )
    );
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        MenuSelectionManager.defaultManager().clearSelectedPath();
        popupPTG.setDisplayEnabled(false, PerformanceTimerGroup.DONT_ENABLE);
        redoGUI();
        popupPTG = null;
      }
    });
    popup.add(mi);
    mi = new JMenuItem(resources.getString("immediate0"));
    mi.setIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("immediate4.gif")
      )
    );
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        MenuSelectionManager.defaultManager().clearSelectedPath();
        popupPTG.setDisplayEnabled(
          false, PerformanceTimerGroup.ENABLE_IMMEDIATE
        );
        redoGUI();
        popupPTG = null;
      }
    });
    popup.add(mi);
    mi = new JMenuItem(resources.getString("activateAll0"));
    mi.setIcon(
      new ImageIcon(
        PerformanceManagerPanel.class.getResource("all4.gif")
      )
    );
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        MenuSelectionManager.defaultManager().clearSelectedPath();
        popupPTG.setDisplayEnabled(false, PerformanceTimerGroup.ENABLE_ALL);
        redoGUI();
        popupPTG = null;
      }
    });
    popup.add(mi);
  }

  /**
   * Populate the tree.
   * @return    The root node of the tree.
   */
  private TimerTreeNode createTree()
  {
    TimerTreeNode node = new TimerTreeNode((PerformanceTimerGroup)null);
    addNodes(node, PerformanceManager.getPerformanceManager().globalGroups);
    return node;
  }

  /**
   * Populate the tree below a given node.
   * @param     node    The node below which nodes will be added.
   * @param     groups  The PerformanceManagerGroups for which child nodes
   *                    will be created.
   */
  private void addNodes(TimerTreeNode node, PTGBaseArray groups)
  {
    int n = groups.size();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup childGroup = groups.get(i);
      TimerTreeNode childNode = new TimerTreeNode(childGroup);
      node.add(childNode);
      addNodes(childNode, childGroup.children);
      if (node.isRoot()) {
        if (i == PerformanceManager.CONSTRUCTOR) {
          addNodes(childNode, pm.constructorTimers);
        }else{
          if (i == PerformanceManager.INIT_ESLATE_ASPECT) {
            addNodes(childNode, pm.eSlateTimers);
          }
        }
      }
    }
  }

  /**
   * Adds the nodes corresponding to the constructor timers.
   */
  @SuppressWarnings("unchecked")
  private void addNodes(TimerTreeNode node, HashMap classTimers)
  {
    Iterator it = classTimers.values().iterator();
    while (it.hasNext()) {
      ClassTimer t = (ClassTimer)(it.next());
      node.add(new TimerTreeNode(t));
    }
  }

  /**
   * Add a global PerformanceTimerGroup to the GUI.
   * @param     ptg     The global PerformanceTimerGroup to add.
   */
  void addGlobalGroup(PerformanceTimerGroup ptg)
  {
    TimerTreeNode node = new TimerTreeNode(ptg);
    addNodes(node, ptg.children);
    TimerTreeNode root =
      (TimerTreeNode)((tree.getModel()).getRoot());
    root.add(node);
    updateStructure();
  }

  /**
   * Remove a global PerformanceTimerGroup from the GUI.
   * @param     ptg     The global PerformanceTimerGroup to remove.
   */
  void removeGlobalGroup(PerformanceTimerGroup ptg)
  {
    TimerTreeNode root = (TimerTreeNode)((tree.getModel()).getRoot());
    int n = root.getChildCount();
    for (int i=0; i<n; i++) {
      TimerTreeNode node = (TimerTreeNode)(root.getChildAt(i));
      if (ptg.equals(node.timer)) {
        root.remove(node);
        break;
      }
    }
    updateStructure();
  }

  /**
   * Adds a new PerformanceTimerGroup to the GUI.
   * @param     parent  The PerformanceTimerGroup to which the new
   *                    PerformanceTimerGroup was added.
   * @param     child   The PerformanceTimerGroup that was added to
   *                    <code>parent</code>.
   */
  void addGroup(PerformanceTimerGroup parent, PerformanceTimerGroup child)
  {
    TimerTreeNode root = (TimerTreeNode)(tree.getModel().getRoot());
    addGroup(root, parent, child);
    updateStructure();
  }

  /**
   * Adds a new PerformanceTimerGroup to the GUI, starting from a given node
   * of the tree.
   * @param     node    The node from which to start.
   * @param     parent  The PerformanceTimerGroup to which the new
   *                    PerformanceTimerGroup was added.
   * @param     child   The PerformanceTimerGroup that was added to
   *                    <code>parent</code>.
   */
  @SuppressWarnings("unchecked")
  private void addGroup(
    TimerTreeNode node, PerformanceTimerGroup parent,
    PerformanceTimerGroup child)
  {
    Enumeration e = node.children();
    while (e.hasMoreElements()) {
      addGroup((TimerTreeNode)(e.nextElement()), parent, child);
    }
    if ((node.timer != null) && node.timer.equals(parent)) {
      node.add(groupTree(child));
    }
  }

  /**
   * Adds a ClassTimer to the GUI.
   * @param     group   The group under which the ClassTimer should be added.
   * @param     ct      The ClassTimer to add.
   */
  void addClassTimer(int group, ClassTimer ct)
  {
    TimerTreeNode root = (TimerTreeNode)(tree.getModel().getRoot());
    TimerTreeNode groupNode = (TimerTreeNode)(root.getChildAt(group));
    groupNode.add(new TimerTreeNode(ct));
    updateStructure();
  }

  /**
   * Removes a ClassTimer from the GUI.
   * @param     group   The group from which the ClassTimer should be removed
   * @param     ct      The ClassTimer to remove.
   */
  void removeClassTimer(int group, ClassTimer ct)
  {
    TimerTreeNode root = (TimerTreeNode)(tree.getModel().getRoot());
    TimerTreeNode groupNode = (TimerTreeNode)(root.getChildAt(group));
    int n = groupNode.getChildCount();
    for (int i=0; i<n; i++) {
      TimerTreeNode node = (TimerTreeNode)(groupNode.getChildAt(i));
      if (ct.equals(node.classTimer)) {
        groupNode.remove(node);
        break;
      }
    }
    updateStructure();
  }

  /**
   * Returns a node structure correposinding to the structure of a given
   * PerformanceTimerGroup.
   * @param     ptg     The PerformanceTimerGroup.
   */
  private TimerTreeNode groupTree(PerformanceTimerGroup ptg)
  {
    TimerTreeNode node = new TimerTreeNode(ptg);
    buildTree(node, ptg.children);
    return node;
  }

  /**
   * Recursively adds nodes to a node corresponding to the children of the
   * PerformanceTimerGroup which corresponds to the node.
   * @param     node    The node.
   * @param     groups  The PerformanceTimerGroups which are children of the
   *                    PerformanceTimerGroup corresponding to
   *                    <code>node</code>.
   */
  private void buildTree(TimerTreeNode node, PTGBaseArray groups)
  {
    int n = groups.size();
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup ptg = groups.get(i);
      TimerTreeNode newNode = new TimerTreeNode(groups.get(i));
      node.add(newNode);
      buildTree(newNode, ptg.children);
    }
  }

  /**
   * Removes a PerformanceTimerGroup from the GUI.
   * @param     parent  The parent PerformanceTimerGroup of the
   *                    PerformanceTimerGroup to remove.
   * @param     child   The PerformanceTimerGroup to remove.
   */
  void removeGroup(PerformanceTimerGroup parent, PerformanceTimerGroup child)
  {
    TimerTreeNode root = (TimerTreeNode)(tree.getModel().getRoot());
    removeGroup(root, parent, child);
    updateStructure();
  }

  /**
   * Removes the nodes corresponding to a given PerformanceTimerGroup having a
   * given parent PerformanceTimerGroup.
   * @param     node    The node from which to start.
   * @param     parent  The parent PerformanceTimerGroup of the
   *                    PerformanceTimerGroup corresponding to the nodes
   *                    that are to be removed. If <code>parent</code> is
   *                    <code>null<code>, all nodes corresponding to
   *                    <code>child</code> will be removed.
   * @param     child   The PerformanceTimerGroup corresponding to the nodes
   *                    that are to be removed.
   */
  private void removeGroup(
    TimerTreeNode node, PerformanceTimerGroup parent,
    PerformanceTimerGroup child)
  {
    boolean isParent;
    if (parent == null) {
      isParent = true;
    }else{
      if (node.isRoot()) {
        isParent = false;
      }else{
        isParent = parent.equals(node.timer);
      }
    }
    int n = node.getChildCount();
    for (int i=0; i<n; i++) {
      TimerTreeNode childNode = (TimerTreeNode)(node.getChildAt(i));
      if (isParent && child.equals(childNode.timer)) {
        node.remove(childNode);
        return;
      }else{
        removeGroup(childNode, parent, child);
      }
    }
  }

  /**
   * Updates the structure of the tree, making them visible in the GUI.
   * If the state of the tree has changed, but its structure has remained teh
   * same, you can use <code>redoGUI()</code>instead.
   */
  void updateStructure()
  {
    // Reloading the model does not preserve the expanded
    // state of the tree nodes, so we have to keep track of it ourselves.
    TimerTreeNode root = (TimerTreeNode)(tree.getModel().getRoot());
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
   * Revalidates and repaints the tree, making any changes in its state
   * visible in the GUI. If the structure of the tree has changed as well, use
   * <code>updateStructure()</code> instead.
   */
  void redoGUI()
  {
    tree.revalidate();
    tree.repaint();
  }

  /**
   * Sets the currently selected activation policy, adjusting the UI
   * accordingly.
   * @param     policy  One of PerformanceTimerGroup.DONT_ENABLE,
   *                    PerformanceTimerGroup.ENABLE_IMMEDIATE,
   *                    PerformanceTimerGroup.ENABLE_ALL.
   */
  private void setPolicy(int policy)
  {
    this.policy = policy;
    switch (policy) {
      case PerformanceTimerGroup.DONT_ENABLE:
        dontItem.setSelected(true);
        immItem.setSelected(false);
        allItem.setSelected(false);
        dontButton.setSelected(true);
        immButton.setSelected(false);
        allButton.setSelected(false);
        break;
      case PerformanceTimerGroup.ENABLE_IMMEDIATE:
        dontItem.setSelected(false);
        immItem.setSelected(true);
        allItem.setSelected(false);
        dontButton.setSelected(false);
        immButton.setSelected(true);
        allButton.setSelected(false);
        break;
      case PerformanceTimerGroup.ENABLE_ALL:
        dontItem.setSelected(false);
        immItem.setSelected(false);
        allItem.setSelected(true);
        dontButton.setSelected(false);
        immButton.setSelected(false);
        allButton.setSelected(true);
        break;
    }
  }

  /**
   * Prints the entire node structure of the GUI. Useful for debugging.
   */
  void dumpGUIStructure()
  {
    TimerTreeNode root = (TimerTreeNode)(tree.getModel().getRoot());
    dumpGUIStructure(root, 0);
  }

  /**
   * Prints the entire structure of a GUI node.
   * @param     node    The node.
   * @param     indent  The indentation level.
   */
  private void dumpGUIStructure(TimerTreeNode node, int indent)
  {
    for (int i=0; i<indent; i++) {
      System.out.print("    ");
    }
    if (node.isRoot()) {
      System.out.println("ROOT");
    }else{
      PerformanceTimerGroup ptg = node.timer;
      if (ptg != null) {
        System.out.println(ptg.toString());
      }else{
        System.out.println(node.classTimer.className);
      }
    }
    int n = node.getChildCount();
    for (int i=0; i<n; i++) {
      TimerTreeNode childNode = (TimerTreeNode)(node.getChildAt(i));
      dumpGUIStructure(childNode, indent+1);
    }
  }

  /**
   * This is a TimerTask that processes single mouse clicks after a suitable
   * delay has passed. The tree's MouseListener will cancel such tasks when
   * detecting a double click, thus preventing double clicks from being
   * processed as both single and double clicks.
   */
  class UpdateTask extends TimerTask
  {
    /**
     * The event to process.
     */
    private MouseEvent e;

    /**
     * Body of the task.
     */
    public void run()
    {
      int selRow = tree.getRowForLocation(e.getX(), e.getY());
      if (selRow != -1) {
        TreePath selPath = tree.getPathForRow(selRow);
        TimerTreeNode node = (TimerTreeNode)(selPath.getLastPathComponent());
        PerformanceTimerGroup ptg = node.timer;
        if (ptg != null) {
          ptg.setDisplayEnabled(!ptg.isDisplayEnabled(), policy);
        }else{
          ClassTimer ct = node.classTimer;
          ct.enabled = !(ct.enabled);
        }
        redoGUI();
      }
      updateTask = null;
    }

    /**
     * Create a task.
     * @param   e       The event to process.
     */
    UpdateTask(MouseEvent e)
    {
      this.e = e;
    }
  }
}
