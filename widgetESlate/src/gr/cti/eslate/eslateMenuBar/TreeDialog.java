package gr.cti.eslate.eslateMenuBar;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


public class TreeDialog extends JDialog {
    ResourceBundle MenuBarDialogBundle;
    private int newNodeSuffix = 1;
    Dimension d = new Dimension(350, 350);
    ImageIcon downIcon = new ImageIcon(getClass().getResource("Images/downArrow.gif"));
    ImageIcon upIcon = new ImageIcon(getClass().getResource("Images/upArrow.gif"));
    ImageIcon deleteIcon = new ImageIcon(getClass().getResource("Images/delete.gif"));
    ImageIcon addIcon = new ImageIcon(getClass().getResource("Images/add.gif"));
    ImageIcon separatorIcon = new ImageIcon(getClass().getResource("Images/JSeparatorColor16n.gif"));
    ImageIcon checkIcon = new ImageIcon(getClass().getResource("Images/JCheckBoxColor16p.gif"));
    JButton downButton, upButton, deleteButton, addButton, separatorButton, checkButton;
    JScrollPane scrollPane;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    boolean changedValues;
    MenuTreePanel treePanel;
    JTree tree;

    public TreeDialog(final JTree oldtree, JFrame frame, boolean bool) {

        super(frame, true);
        setSize(d);
        setResizable(true);
        tree = createIdenticalTree(oldtree);
        treePanel = new MenuTreePanel(tree);
        MenuBarDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.eslateMenuBar.MenuBarDialogBundle", Locale.getDefault());
        String title = MenuBarDialogBundle.getString("DialogTitle");
        setTitle(title);
        changedValues = false;
        //final JTree tree = treePanel.getTree();


        upButton = new JButton(upIcon);
        downButton = new JButton(downIcon);
        addButton = new JButton(addIcon);
        separatorButton = new JButton(separatorIcon);
        deleteButton = new JButton(deleteIcon);
        checkButton = new JButton(checkIcon);

        Dimension buttonSize = new Dimension(27, 22);

        upButton.setMaximumSize(buttonSize);
        upButton.setPreferredSize(buttonSize);
        upButton.setMinimumSize(buttonSize);
        downButton.setMaximumSize(buttonSize);
        downButton.setPreferredSize(buttonSize);
        downButton.setMinimumSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        addButton.setMinimumSize(buttonSize);
        separatorButton.setMaximumSize(buttonSize);
        separatorButton.setPreferredSize(buttonSize);
        separatorButton.setMinimumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMinimumSize(buttonSize);
        checkButton.setMaximumSize(buttonSize);
        checkButton.setPreferredSize(buttonSize);
        checkButton.setMinimumSize(buttonSize);
        upButton.setToolTipText(MenuBarDialogBundle.getString("UpButtonTip"));
        downButton.setToolTipText(MenuBarDialogBundle.getString("DownButtonTip"));
        addButton.setToolTipText(MenuBarDialogBundle.getString("AddButtonTip"));
        deleteButton.setToolTipText(MenuBarDialogBundle.getString("DeleteButtonTip"));
        separatorButton.setToolTipText(MenuBarDialogBundle.getString("SeparatorButtonTip"));
        checkButton.setToolTipText(MenuBarDialogBundle.getString("CheckButtonTip"));
        JPanel listButtonPanel = new JPanel();

        listButtonPanel.setLayout(new BoxLayout(listButtonPanel, BoxLayout.Y_AXIS));

        listButtonPanel.add(Box.createGlue());
        listButtonPanel.add(upButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(downButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(addButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(separatorButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(checkButton);
        if (treePanel.getTree().getSelectionPath() == null || treePanel.getTree().getSelectionPath().getLastPathComponent() == treePanel.getTree().getModel().getRoot() || treePanel.getTree().getSelectionPath().getLastPathComponent() instanceof CustomSeparator)
            separatorButton.setEnabled(false);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(deleteButton);
        listButtonPanel.add(Box.createGlue());

        JPanel topPanel = new JPanel();
        BorderLayout borderLayout = new BorderLayout();

        borderLayout.setHgap(10);
        topPanel.setLayout(borderLayout);//Layout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(treePanel, BorderLayout.CENTER);
        topPanel.add(listButtonPanel, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));

        // The button panel (APPLY, OK, CANCEL)
        JButton okButton = new JButton(MenuBarDialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);

        buttonSize = new Dimension(90, 25);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);

        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(MenuBarDialogBundle.getString("Cancel"));

        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // The main panel
        JPanel mainPanel = new JPanel(true);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);
        getContentPane().add(mainPanel);

        treePanel.getTree().addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {

                    if (treePanel.getTree().getSelectionPath() == null || treePanel.getTree().getSelectionPath().getLastPathComponent() == treePanel.getTree().getModel().getRoot() || treePanel.getTree().getSelectionPath().getLastPathComponent() instanceof CustomSeparator) {
                        separatorButton.setEnabled(false);
                    } else {
                        separatorButton.setEnabled(true);
                    }
                    if (treePanel.getTree().getSelectionPath() != null && treePanel.getTree().getSelectionPath().getLastPathComponent() instanceof CustomSeparator) {
                        addButton.setEnabled(false);
                        checkButton.setEnabled(false);
                    } else {
                        addButton.setEnabled(true);
                        checkButton.setEnabled(true);
                    }

                    if (treePanel.getTree().getSelectionPath() != null &&
                        ((MenuItemNode) treePanel.getTree().getSelectionPath().getLastPathComponent()).getPreviousSibling() != null)
                        upButton.setEnabled(true);
                    else upButton.setEnabled(false);

                    if (treePanel.getTree().getSelectionPath() != null &&
                        ((MenuItemNode) treePanel.getTree().getSelectionPath().getLastPathComponent()).getNextSibling() != null)
                        downButton.setEnabled(true);
                    else downButton.setEnabled(false);

                }
            }
        );

        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changedValues = true;
                    dispose();
                }
            }
        );
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changedValues = false;
                    tree = createIdenticalTree(oldtree);
                    dispose();
                }
            }
        );

        upButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.moveNodeUp();
                }
            }
        );

        downButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.moveNodeDown();
                }
            }
        );

        addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.addObject(MenuBarDialogBundle.getString("New Node ") + newNodeSuffix++);
                }
            }
        );

        separatorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.addSpecialObject("<<separator>>");
                }
            }
        );

        checkButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.addCheckObject(MenuBarDialogBundle.getString("New Node ") + newNodeSuffix++);
                }
            }
        );

        deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.removeCurrentNode();
                }
            }
        );

        JButton clearButton = new JButton("Clear");

        clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    treePanel.clear();
                }
            }
        );

        getRootPane().registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    javax.swing.ButtonModel bm = cancelButton.getModel();

                    bm.setArmed(true);
                    bm.setPressed(true);
                    if (oldtree != null)
                        tree = createIdenticalTree(oldtree);
                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    javax.swing.ButtonModel bm = cancelButton.getModel();

                    bm.setPressed(false);
                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // }

    public static void showDialog(Window dialog, Component centerAroundComp, boolean pack) {

        if (pack)

            dialog.pack();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;

        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width / 2) - (dialog.getSize().width / 2);
            y = (screenSize.height / 2) - (dialog.getSize().height / 2);
        } else {
            Rectangle compBounds = centerAroundComp.getBounds();
            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();

            x = compLocation.x + compBounds.width / 2 - dialog.getSize().width / 2;
            y = compLocation.y + compBounds.height / 2 - dialog.getSize().height / 2;
            if (x + dialog.getSize().width > screenSize.width)
                x = screenSize.width - dialog.getSize().width;
            if (y + dialog.getSize().height > screenSize.height)
                y = screenSize.height - dialog.getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        dialog.setLocation(x, y);
        dialog.setVisible(true);
    }

    public JTree getMenuTree() {
        JTree tree = treePanel.getTree();

        return tree;
    }

    public boolean getChangedValues() {
        return changedValues;
    }

    public JTree createIdenticalTree(JTree tree) {
        MenuItemNode rootNode = (MenuItemNode) tree.getModel().getRoot();
        MenuItemNode newRoot = (MenuItemNode) rootNode.clone();
        JTree newTree = new JTree(newRoot);

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            MenuItemNode node = (MenuItemNode) rootNode.getChildAt(i);

            if (node.isLeaf() == false) {
                newRoot.add(createSubNodes((MenuItemNode) node));
            } else
                newRoot.add((MenuItemNode) node.clone());
        }
        return newTree;
    }

    public MenuItemNode createSubNodes(MenuItemNode node) {
        MenuItemNode nodeClone = (MenuItemNode) node.clone();

        for (int i = 0; i < node.getChildCount(); i++) {
            MenuItemNode childNode = (MenuItemNode) node.getChildAt(i);

            if (node.isLeaf() == false) {
                nodeClone.add(createSubNodes((MenuItemNode) childNode));
            } else {
                nodeClone.add((MenuItemNode) childNode.clone());
            }
        }
        return nodeClone;
    }
}

