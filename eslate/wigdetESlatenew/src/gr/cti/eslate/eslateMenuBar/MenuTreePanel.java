package gr.cti.eslate.eslateMenuBar;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class MenuTreePanel extends JPanel {
    public static final ImageIcon ICON_SEPERATOR = new ImageIcon("Images/JSeparatorColor16n.gif");

    ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.eslateMenuBar.MenuBarDialogBundle", Locale.getDefault());
    protected MenuItemNode rootNode;
    protected DefaultTreeModel treeModel;
    protected DefaultTreeSelectionModel treeSelectionModel;
    MenuItemNode draggedNode, draggedOverNode;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    JScrollPane scrollPane;

    boolean treeEditable = true;

    public MenuTreePanel(JTree initTree) {
        if (initTree == null) {
            rootNode = new MenuItemNode("MenuBar");
            treeModel = new DefaultTreeModel(rootNode);
            treeSelectionModel = new DefaultTreeSelectionModel();
            treeModel.addTreeModelListener(new MyTreeModelListener());
        } else {
            rootNode = (MenuItemNode) initTree.getModel().getRoot();
            treeSelectionModel = (DefaultTreeSelectionModel) initTree.getSelectionModel();
            treeModel = (DefaultTreeModel) initTree.getModel();
            treeModel.addTreeModelListener(new MyTreeModelListener());
        }
        tree = new JTree(treeModel) {
                    public boolean isPathEditable(TreePath path) {
                        MenuItemNode node = (MenuItemNode) path.getLastPathComponent();

                        if (node instanceof CustomSeparator || node.getLevel() == 0) return false;
                        else return isEditable();
                    }
                };
        tree.setSelectionModel(treeSelectionModel);
        if (treeEditable)
            tree.setEditable(true);
        //tree.setRootVisible(false);
        tree.setCellRenderer(new MenuTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setExpandsSelectedPaths(true);
        MouseInputListener mil = new MouseInputAdapter() {
                MenuItemNode nodeToBeDragged = null;

                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) return;
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());

                    if (path == null) return;
                    nodeToBeDragged = (MenuItemNode) tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
                }

                public void mouseReleased(MouseEvent e) {
                    tree.setCursor(Cursor.getDefaultCursor());
                    if (treeEditable)
                        tree.setEditable(true);
                    boolean reparent = true;

                    if (draggedNode == null || draggedOverNode == null) reparent = false;
                    if (reparent && draggedOverNode == draggedNode.getParent()) reparent = false;
                    if (reparent && draggedNode == draggedOverNode) reparent = false;
                    if (reparent && draggedNode.isNodeDescendant(draggedOverNode)) reparent = false;
                    if (!reparent) {
                        draggedNode = null;
                        draggedOverNode = null;
                        nodeToBeDragged = null;
                        tree.repaint();
                        return;
                    }

                    treeModel.removeNodeFromParent(draggedNode);
                    treeModel.insertNodeInto(draggedNode, draggedOverNode, 0);
                    TreePath path = new TreePath(draggedNode.getPath());

                    tree.setSelectionPath(path);
                    tree.expandPath(path);
                    TreeModel model = tree.getModel();

                    tree.setModel(model);
                    draggedNode = null;
                    draggedOverNode = null;
                    nodeToBeDragged = null;
                    tree.repaint();
                }

                public void mouseDragged(MouseEvent e) {
                    if (nodeToBeDragged == null) return;
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());

                    if (path == null) return;
                    MenuItemNode currNode = (MenuItemNode) path.getLastPathComponent();

                    /* Dragging starts as soon as the mouse exist the area of the 'nodeToBeDragged'. It
                     * does not start on the first mouseDragged event. Also if the mouse re-enters the
                     * area of the 'nodeToBeDragged', dragging stops until it exits again. Also dragging
                     * temporarily stops if a node which is a descentant of the 'nodeToBeDragged' is
                     * entered.
                     */
                    if (currNode == nodeToBeDragged || currNode.isNodeAncestor(nodeToBeDragged)) {
                        draggedOverNode = null;
                        tree.setCursor(Cursor.getDefaultCursor());
                        tree.repaint();
                        return;
                    } else {
                        if (treeEditable)
                            tree.setEditable(false);
                        draggedNode = nodeToBeDragged;
                        if (draggedOverNode != currNode) {
                            draggedOverNode = currNode;
                            tree.scrollPathToVisible(path);
                            tree.repaint();
                        }
                        tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                }
            };

        tree.addMouseListener(mil);
        tree.addMouseMotionListener(mil);

        /*MouseInputListener mil = new MouseInputAdapter() {
         public void mousePressed(MouseEvent e) {
         //      ((LayerTreeCellRenderer) tree.getCellRenderer()).setOpaque(true);
         TreePath path = tree.getPathForLocation(e.getX(), e.getY());
         if (path == null) return;
         draggedNode = (MenuItemNode) tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
         if (draggedNode instanceof CustomSeparator)
         draggedNode=null;
         }
         public void mouseReleased(MouseEvent e) {
         if (tree.getCursor() != Cursor.getDefaultCursor())
         tree.setCursor(Cursor.getDefaultCursor());
         if (draggedNode == null || draggedOverNode == null || draggedOverNode instanceof CustomSeparator) return;
         if (draggedOverNode == draggedNode.getParent()) return;
         if (draggedNode == draggedOverNode) return;
         if (draggedNode.isNodeDescendant(draggedOverNode)) return;

         treeModel.removeNodeFromParent(draggedNode);
         treeModel.insertNodeInto(draggedNode, draggedOverNode, 0);
         TreePath path = new TreePath(draggedNode.getPath());
         tree.setSelectionPath(path);
         tree.expandPath(path);
         TreeModel model = tree.getModel();
         tree.setModel(model);
         tree.repaint();
         draggedNode = null;
         draggedOverNode = null;
         }
         public void mouseDragged(MouseEvent e) {
         if (tree.getCursor() != Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
         tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         if (draggedNode == null) {
         draggedOverNode = null;
         tree.repaint();
         return;
         }
         TreePath path = tree.getPathForLocation(e.getX(), e.getY());
         if (path != null) tree.scrollPathToVisible(path);
         if (draggedNode == null || path == null) {
         draggedOverNode = null;
         tree.repaint();
         return;
         }
         draggedOverNode = (MenuItemNode) path.getLastPathComponent();
         //                if (draggedOverNode.getLevel()<draggedNode.getLevel()) return;
         //                Enable this, and everyone can be everyone's father or child! (ESlateMenuBar)
         tree.repaint();
         }
         };
         tree.addMouseListener(mil);
         tree.addMouseMotionListener(mil);
         */
        scrollPane = new JScrollPane(tree);

        tree.setBackground(scrollPane.getBackground());
        Dimension scrollPaneSize = new Dimension(300, 300);
        scrollPane.setBackground(tree.getBackground());

        scrollPane.setMaximumSize(scrollPaneSize);
        scrollPane.setPreferredSize(scrollPaneSize);
        scrollPane.setMinimumSize(scrollPaneSize);
        setLayout(new GridLayout(1, 1));
        add(scrollPane);
    }

    public JTree getTree() {
        return tree;
    }

    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();

        if (currentSelection != null) {
            MenuItemNode currentNode = (MenuItemNode)
                (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());

            if (parent != null) {
                MenuItemNode previousExpandedNode = getPreviousExpandedNode(currentNode);

                treeModel.removeNodeFromParent(currentNode);
                treeSelectionModel.setSelectionPath(new TreePath(previousExpandedNode.getPath()));
                return;
            }
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    public MenuItemNode getPreviousExpandedNode(MenuItemNode node) {
        MenuItemNode previousExpandedNode = (MenuItemNode) node.getPreviousNode();

        if (tree.isVisible(new TreePath(previousExpandedNode.getPath())) == true)
            return previousExpandedNode;
        else
            return getPreviousExpandedNode(previousExpandedNode);

    }

    /** Add child to the currently selected node. */
    public MenuItemNode addObject(Object child) {
        MenuItemNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (MenuItemNode)
                    (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true, false);
    }

    public MenuItemNode addCheckObject(Object child) {
        MenuItemNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (MenuItemNode)
                    (parentPath.getLastPathComponent());
        }

        return addCheckObject(parentNode, child, true);
    }

    public MenuItemNode addSpecialObject(Object child) {
        MenuItemNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        parentNode = (MenuItemNode)
                (parentPath.getLastPathComponent());

        /* The below code snippet was used to avoid adding separators to root node
         * (because the root node used to be only the menubar). Now that the root
         * node can be a popup menu, a separator can be added to it. Blocking of
         * separator adding functionality must be applied to addSeparator button
         * at the PopupEditorDialog
         */
/*
        if (parentNode != rootNode)
            return addObject(parentNode, child, true, true);
        else
            return null;
*/
        return addObject(parentNode, child, true, true);
    }

    public MenuItemNode addObject(MenuItemNode parent,
        Object child) {
        return addObject(parent, child, false, false);
    }

    public MenuItemNode addObject(MenuItemNode parent,
        Object child,
        boolean shouldBeVisible, boolean special) {
        MenuItemNode childNode = null;

        if (special == true) {
            childNode = new CustomSeparator(bundle.getString("<<Separator>>"));
        } else {
            childNode = new MenuItemNode(child);
        }

        if (parent == null) {
            parent = rootNode;
        }

        treeModel.insertNodeInto(childNode, parent,
            parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public MenuItemNode addCheckObject(MenuItemNode parent,
        Object child,
        boolean shouldBeVisible) {
        MenuItemNode childNode = null;

        childNode = new CheckMenuItemNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        treeModel.insertNodeInto(childNode, parent,
            parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public void moveNodeUp() {
        TreePath currentSelection = tree.getSelectionPath();
        MenuItemNode movingNode = (MenuItemNode) (currentSelection.getLastPathComponent());
        int movingNodeIndex = movingNode.getParent().getIndex(movingNode);
        MenuItemNode parentNode = (MenuItemNode) movingNode.getParent();
        MenuItemNode previousNode = (MenuItemNode) movingNode.getPreviousSibling();

        if (previousNode != null) {
            int previousNodeIndex = previousNode.getParent().getIndex(previousNode);

            parentNode.insert(movingNode, previousNodeIndex);
            parentNode.insert(previousNode, movingNodeIndex);
            tree.setSelectionPath(new TreePath(movingNode.getPath()));
            treeModel.reload();
            treeSelectionModel.setSelectionPath(new TreePath(movingNode.getPath()));
        }
    }

    public void moveNodeDown() {
        TreePath currentSelection = tree.getSelectionPath();
        MenuItemNode movingNode = (MenuItemNode) (currentSelection.getLastPathComponent());
        int movingNodeIndex = movingNode.getParent().getIndex(movingNode);
        MenuItemNode parentNode = (MenuItemNode) movingNode.getParent();
        MenuItemNode nextNode = (MenuItemNode) movingNode.getNextSibling();

        if (nextNode != null) {
            int nextNodeIndex = nextNode.getParent().getIndex(nextNode);

            parentNode.insert(movingNode, nextNodeIndex);
            parentNode.insert(nextNode, movingNodeIndex);
            tree.setSelectionPath(new TreePath(movingNode.getPath()));
            treeModel.reload();
            treeSelectionModel.setSelectionPath(new TreePath(movingNode.getPath()));
        }
    }

    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {

            MenuItemNode node;

            node = (MenuItemNode)
                    (e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            try {
                int index = e.getChildIndices()[0];

                node = (MenuItemNode)
                        (node.getChildAt(index));
                //System.out.println("Editing begins");
            } catch (NullPointerException exc) {}

            //System.out.println("The user has finished editing the node.");
            //System.out.println("New value: " + node.getUserObject());
        }

        public void treeNodesInserted(TreeModelEvent e) {}

        public void treeNodesRemoved(TreeModelEvent e) {}

        public void treeStructureChanged(TreeModelEvent e) {}
    }


    class MenuTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
        Color selectedForeground = Color.white;
        Color selectedBackground = new Color(0, 0, 128);
        Color foreground = Color.black;
        Color background = getBackground();
        ImageIcon separatorIcon = new ImageIcon(MenuTreeCellRenderer.class.getResource("Images/JSeparatorColor16n.gif"));
        ImageIcon menuIcon = new ImageIcon(MenuTreeCellRenderer.class.getResource("Images/JMenuColor16n.gif"));
        ImageIcon menuItemIcon = new ImageIcon(MenuTreeCellRenderer.class.getResource("Images/JMenuItemColor16n.gif"));
        ImageIcon menuBarIcon = new ImageIcon(MenuTreeCellRenderer.class.getResource("Images/JMenuBarColor16n.gif"));
        ImageIcon checkItemIcon = new ImageIcon(MenuTreeCellRenderer.class.getResource("Images/JCheckBoxColor16p.gif"));

        EmptyBorder normalBorder = new EmptyBorder(1, 1, 1, 1);
        LineBorder dragOverBorder = new LineBorder(new Color(0, 0, 128));

        public MenuTreeCellRenderer() {
            super();
            setOpaque(false);
            setBorder(normalBorder);
        }

        public Component getTreeCellRendererComponent(JTree tree,
            Object value,            // value to display
            boolean isSelected,      // is the cell selected
            boolean expanded,    // the table and the cell have the focus
            boolean leaf,
            int row,
            boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
            MenuItemNode node = (MenuItemNode) value;

            if (node == draggedOverNode)
                setBackgroundNonSelectionColor(UIManager.getColor("Tree.selectionBackground"));
            else
                setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));

            // Take care of the foreground of the 'draggedOvelNode'.
            if (isSelected || node == draggedOverNode)
                setForeground(getTextSelectionColor());
            else
                setForeground(getTextNonSelectionColor());

            setLeafIcon(menuItemIcon);
            if (node instanceof CustomSeparator) {
                setIcon(separatorIcon);
            } else if (node.getLevel() == 0) {
                setIcon(menuBarIcon);
                setOpenIcon(menuBarIcon);
                setClosedIcon(menuBarIcon);
            }
            if (node.getUserObject() instanceof ESlateMenu || node.getChildCount() != 0){// || node.getLevel() == 1) {
                setIcon(menuIcon);
                setOpenIcon(menuIcon);
                setClosedIcon(menuIcon);
            }
            if (node.getUserObject()  instanceof ESlateCheckMenuItem)
                setIcon(checkItemIcon);

            if (node.getUserObject() instanceof String)
                setText((String) node.getUserObject());
            else if (node.getUserObject() instanceof ESlateMenu){
                setText(((ESlateMenu) node.getUserObject()).getText());
            }else if (node.getUserObject() instanceof RollMenuButton) {}
            else if (node.getUserObject() instanceof ESlatePopupMenu) {
                setText("PopupMenu");
            }else{
                if ((JMenuItem) node.getUserObject() != null)
                    setText(((JMenuItem) node.getUserObject()).getText());
            }
            if (isSelected) {
                setTextSelectionColor(selectedForeground);
                setBackgroundSelectionColor(selectedBackground);
            } else {
                setTextNonSelectionColor(foreground);
                setBackgroundNonSelectionColor(background);
            }

            return this;
        }
    }

    void setPanelTreeEditable(boolean editable){
        treeEditable = editable;
    }

    boolean isPanelTreeEditable(){
        return treeEditable;
    }
}
