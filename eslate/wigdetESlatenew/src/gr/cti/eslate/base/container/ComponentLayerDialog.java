package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


class ComponentLayerDialog extends JDialog {
    ResourceBundle componentLayerDialogBundle;
    ESlateContainer container;
    JTree tree;
    JScrollPane scrollpane;
    DefaultTreeModel treeModel;
    DefaultMutableTreeNode draggedNode, draggedOverNode;
//    static JFrame contentFrame = new JFrame();

    public ComponentLayerDialog(Frame parentFrame, ESlateContainer cont) {
        super(parentFrame, true);
        this.container = cont;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not. If a script wants to use this action, is should first enable the
         * realtive setting. The script is responsible for disabling the action afterwards.
         */
        if (cont.microworld != null)
            cont.microworld.checkActionPriviledge(cont.microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");

//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());


        componentLayerDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ComponentLayerDialogBundle", Locale.getDefault());
        String title = componentLayerDialogBundle.getString("DialogTitle");
        setTitle(title);

        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(componentLayerDialogBundle.getString("MicroworldLayers")));
        tree = new JTree(treeModel);
        tree.setCellRenderer(new LayerTreeCellRenderer(this));
        tree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
        javax.swing.ToolTipManager.sharedInstance().registerComponent(tree);
        scrollpane = new JScrollPane(tree);
        scrollpane.setBackground(Color.white);
        scrollpane.setBorder(new CompoundBorder(scrollpane.getBorder(), new EmptyBorder(2,0,0,0))); //new SoftBevelBorder(SoftBevelBorder.LOWERED));

        LayerInfo layerInfo = container.mwdLayers;
//System.out.println("layerInfo: " + layerInfo);
        String[] layerNames = layerInfo.getLayerNames();
        for (int i=0; i<layerNames.length; i++) {
            DefaultMutableTreeNode layerNode = new DefaultMutableTreeNode(layerNames[i]);
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(layerNode);
            int level = layerInfo.getLayerLevel(layerNames[i]);
//System.out.println("Adding layer: " + layerNames[i] + ", with level: " + level);
            for (int k=0; k<container.mwdComponents.size(); k++) {
                if (!container.mwdComponents.components.get(k).visualBean)
                    continue;
                ESlateComponent ecomponent = container.mwdComponents.components.get(k);
                ESlateInternalFrame fr = ecomponent.frame;
//System.out.println("Layer: " + fr.getLayer() + ", component: " + ecomponent);
                if (fr != null && fr.getLayer() == level)
                    layerNode.add(new DefaultMutableTreeNode(ecomponent.handle.getComponentName()));
            }
        }
        tree.expandRow(0);

        final OK_Cancel_ApplyPanel buttonPanel = container.containerUtils.createOKCancelApplyPanel(null);

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 8));

        mainPanel.add(scrollpane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        buttonPanel.getOKButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                assignLayers();
                dispose();
            }
        });
        buttonPanel.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.getApplyButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                assignLayers();
            }
        });

        MouseInputListener mil = new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {
//                ((LayerTreeCellRenderer) tree.getCellRenderer()).setOpaque(true);
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) return;
                draggedNode = (DefaultMutableTreeNode) tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
                if (draggedNode != null && draggedNode.getLevel() != 2)
                    draggedNode = null;
//                System.out.println("Dragged node: " + draggedNode.getUserObject());
            }
            public void mouseReleased(MouseEvent e) {
                if (tree.getCursor() != Cursor.getDefaultCursor())
                    tree.setCursor(Cursor.getDefaultCursor());
                if (draggedNode == null || draggedOverNode == null) return;
                if (draggedOverNode == draggedNode.getParent()) return;
                treeModel.removeNodeFromParent(draggedNode);
                treeModel.insertNodeInto(draggedNode, draggedOverNode, 0);
                TreePath path = new TreePath(draggedNode.getPath());
                tree.setSelectionPath(path);
                tree.expandPath(path);
                draggedNode = null;
                draggedOverNode = null;
                container.setMicroworldChanged(true);
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
                draggedOverNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (draggedOverNode.getLevel() != 1) {
                    if (draggedOverNode.getLevel() == 2)
                        draggedOverNode = (DefaultMutableTreeNode) draggedOverNode.getParent();
                    else{
                        draggedOverNode = null;
                    }
                }
                if (draggedOverNode == draggedNode.getParent())
                    draggedOverNode = null;

                tree.repaint();
            }
        };
        tree.addMouseListener(mil);
        tree.addMouseMotionListener(mil);

        scrollpane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                scrollpane.invalidate();
                scrollpane.doLayout();
                scrollpane.revalidate();
            }
        });

        //Intitialization
        tree.requestFocus();

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = buttonPanel.getCancelButton().getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = buttonPanel.getCancelButton().getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(buttonPanel.getOKButton());

        setSize(350, 400);
        validate();
        setResizable(true);
        ESlateContainerUtils.showDialog(this, container, false);
    }

    private void assignLayers() {
        int layerCount = ((DefaultMutableTreeNode) treeModel.getRoot()).getChildCount();
        for (int i=0; i<layerCount; i++) {
            DefaultMutableTreeNode layerNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) treeModel.getRoot()).getChildAt(i);
            String layerName = (String) layerNode.getUserObject();
            Integer level = new Integer(container.mwdLayers.getLayerLevel(layerName));
            int componentCount = layerNode.getChildCount();
            for(int k=0; k<componentCount; k++) {
//                System.out.println("Setting level to: " + level + " for component: " + container.getComponentFrame((String) ((DefaultMutableTreeNode) layerNode.getChildAt(k)).getUserObject()));
                container.getComponent((String) ((DefaultMutableTreeNode) layerNode.getChildAt(k)).getUserObject()).desktopItem.setLayer(level.intValue());
            }
        }
    }
}

class LayerTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {// implements javax.swing.tree.DefaultTreeCellRenderer {
    static Color selectedForeground = Color.white;
    static Color selectedBackground = new Color(0, 0, 128);
    static Color foreground = Color.black;
    static Color background = Color.white;
    static ImageIcon layerIcon = new ImageIcon(LayerTreeCellRenderer.class.getResource("images/layer.gif"));
    ComponentLayerDialog compLayerDialog = null;
    static EmptyBorder normalBorder = new EmptyBorder(1,1,1,1);
    static LineBorder dragOverBorder = new LineBorder(new Color(0, 0, 128));

    public LayerTreeCellRenderer(ComponentLayerDialog dialog) {
        super();
        setOpaque(false);
        compLayerDialog = dialog;
        setBorder(normalBorder);
    }

    public Component getTreeCellRendererComponent (JTree tree,
                                                Object value,            // value to display
                                                boolean isSelected,      // is the cell selected
                                                boolean expanded,    // the table and the cell have the focus
                                                boolean leaf,
                                                int row,
                                                boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.getLevel() == 1)
            setIcon(layerIcon);
        if (node.getLevel() == 2)
            setToolTipText(compLayerDialog.componentLayerDialogBundle.getString("DragToChangeLayer"));
        else
            setToolTipText("");

        setText((String) node.getUserObject());
        if (isSelected) {
            setTextSelectionColor(selectedForeground);
            setBackgroundSelectionColor(selectedBackground);
        }else{
            setTextNonSelectionColor(foreground);
            setBackgroundNonSelectionColor(background);
        }

        if (node == compLayerDialog.draggedOverNode) {
            setBorder(dragOverBorder);
        }else{
            setBorder(normalBorder);
        }
        return this;
    }
}

