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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;



public class PopupEditorDialog extends JDialog {
    ResourceBundle bundle;
    private int newNodeSuffix = 1;
    Dimension d = new Dimension(600, 500);
    ImageIcon downIcon = new ImageIcon(getClass().getResource("Images/downArrow.gif"));
    ImageIcon upIcon = new ImageIcon(getClass().getResource("Images/upArrow.gif"));
    ImageIcon deleteIcon = new ImageIcon(getClass().getResource("Images/delete.gif"));
    ImageIcon addIcon = new ImageIcon(getClass().getResource("Images/add.gif"));
    ImageIcon separatorIcon = new ImageIcon(getClass().getResource("Images/JSeparatorColor16n.gif"));
    ImageIcon checkIcon = new ImageIcon(getClass().getResource("Images/JCheckBoxColor16p.gif"));
    JButton downButton, upButton, deleteButton, addButton, separatorButton, checkButton, okButton, cancelButton;
    JCheckBox itemEnabled;
    JTextField itemName;
    JComboBox availableActions;
    JScrollPane scrollPane;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    boolean changedValues;
    MenuTreePanel treePanel;
    JTree tree,oldtree;
    Dimension buttonSize = new Dimension(27, 22);

    //This is used to stop actionPerformed changes when we just want to "show"
    //the action chosen for a specific item when selecting nodes on a tree
    boolean lockActionAssingments = false;

    public PopupEditorDialog(final JTree oldtree, JFrame frame, boolean b) {

        super(frame,true);
        setSize(d);
        setResizable(true);
        this.oldtree = oldtree;
        tree = createIdenticalTree(oldtree);
        setTitle("Popup Editor");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createTreePanel(), BorderLayout.CENTER);
//        panel.add(Box.createVerticalStrut(3));
        panel.add(createNodePropertiesPanel(),BorderLayout.EAST);
//        panel.add(Box.createGlue());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(createOKCancelPanel(), BorderLayout.SOUTH);
        registerButtonActions();
        //getContentPane().add(createOKCancelPanel());

   }

   JPanel createTreePanel(){
        treePanel = new MenuTreePanel(tree);
        treePanel.scrollPane.setMaximumSize(new Dimension(200,300));
        treePanel.scrollPane.setMinimumSize(new Dimension(200,300));
        treePanel.scrollPane.setPreferredSize(new Dimension(200,300));
        treePanel.scrollPane.setRequestFocusEnabled(true);
        treePanel.getTree().setRequestFocusEnabled(true);
        treePanel.getTree().setEditable(false);
        treePanel.setPanelTreeEditable(false);
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
        upButton.setRequestFocusEnabled(false);
        downButton.setMaximumSize(buttonSize);
        downButton.setPreferredSize(buttonSize);
        downButton.setMinimumSize(buttonSize);
        downButton.setRequestFocusEnabled(false);
        addButton.setMaximumSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        addButton.setMinimumSize(buttonSize);
        addButton.setRequestFocusEnabled(false);
        separatorButton.setMaximumSize(buttonSize);
        separatorButton.setPreferredSize(buttonSize);
        separatorButton.setMinimumSize(buttonSize);
        separatorButton.setRequestFocusEnabled(false);
        deleteButton.setMaximumSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMinimumSize(buttonSize);
        deleteButton.setRequestFocusEnabled(false);
        checkButton.setMaximumSize(buttonSize);
        checkButton.setPreferredSize(buttonSize);
        checkButton.setMinimumSize(buttonSize);
        checkButton.setRequestFocusEnabled(false);
//        upButton.setToolTipText(MenuBarDialogBundle.getString("UpButtonTip"));
//        downButton.setToolTipText(MenuBarDialogBundle.getString("DownButtonTip"));
//        addButton.setToolTipText(MenuBarDialogBundle.getString("AddButtonTip"));
//        deleteButton.setToolTipText(MenuBarDialogBundle.getString("DeleteButtonTip"));
//        separatorButton.setToolTipText(MenuBarDialogBundle.getString("SeparatorButtonTip"));
//        checkButton.setToolTipText(MenuBarDialogBundle.getString("CheckButtonTip"));
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

        JPanel panel = new JPanel();
        BorderLayout borderLayout = new BorderLayout();

        borderLayout.setHgap(10);
        panel.setLayout(borderLayout);//Layout(topPanel, BoxLayout.X_AXIS));
        panel.add(treePanel, BorderLayout.CENTER);
        panel.add(listButtonPanel, BorderLayout.WEST);
        panel.setBorder(new EmptyBorder(3, 4, 3, 4));

        return panel;
    }

    JPanel createNodePropertiesPanel(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel panel1 = new JPanel(){
            public Dimension getPreferredSize(){
                return new Dimension(200,18);
            }
            public Dimension getMinimumSize(){
                return new Dimension(100,18);
            }
            public Dimension getMaximumSize(){
                return new Dimension(Integer.MAX_VALUE,18);
            }
        };
        JPanel panel2 = new JPanel(){
            public Dimension getPreferredSize(){
                return new Dimension(200,18);
            }
            public Dimension getMinimumSize(){
                return new Dimension(100,18);
            }
            public Dimension getMaximumSize(){
                return new Dimension(Integer.MAX_VALUE,18);
            }
        };
        JPanel panel3 = new JPanel(){
            public Dimension getPreferredSize(){
                return new Dimension(200,18);
            }
            public Dimension getMinimumSize(){
                return new Dimension(100,18);
            }
            public Dimension getMaximumSize(){
                return new Dimension(Integer.MAX_VALUE,18);
            }
        };
        Dimension labelDim = new Dimension(75,18);
        JLabel label1 = new JLabel("Item Name : ");
        JLabel label2 = new JLabel("Action : ");
        JLabel label3 = new JLabel("Enabled : ");
        label1.setMaximumSize(labelDim);
        label1.setMinimumSize(labelDim);
        label1.setPreferredSize(labelDim);
        label2.setMaximumSize(labelDim);
        label2.setMinimumSize(labelDim);
        label2.setPreferredSize(labelDim);
        label3.setMaximumSize(labelDim);
        label3.setMinimumSize(labelDim);
        label3.setPreferredSize(labelDim);
        itemName = new JTextField();
        itemName.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    updateSelectedItemText();
            }

            public void keyReleased(KeyEvent e){}
            public void keyTyped(KeyEvent e){}
        });

        itemName.addFocusListener(new FocusListener(){
            public void focusLost(FocusEvent e){
                    updateSelectedItemText();
            }
            public void focusGained(FocusEvent e){}
        });

        availableActions = new JComboBox(){
            public Dimension getPreferredSize(){
                return new Dimension(itemName.getWidth(),18);
            }
        };

        /* Code to "fill" combobox with the popup owner component available
         * actions.
         */

        ESlatePopupMenu popup = (ESlatePopupMenu) ((MenuItemNode) treePanel.getTree().getModel().getRoot()).getUserObject();
        JComponent owner = null;
        if (popup.getESlateHandle() != null && popup.getESlateHandle().getParentHandle() !=null
            && popup.getESlateHandle().getParentHandle().getComponent() != null){

            owner = (JComponent) popup.getESlateHandle().getParentHandle().getComponent();
        }

        if (owner == null){
            System.out.println("No owner for this popupMenu. Cannot discover actions.");
        }
        availableActions.addItem("--No Action--");
        if (owner != null){
          final ActionMap ownerActions = owner.getActionMap();
          for (int i=0;i<ownerActions.size();i++){
              availableActions.addItem(ownerActions.allKeys()[i].toString());
          }


          availableActions.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e){
                  if (lockActionAssingments)
                      return;
                  TreePath path = treePanel.getTree().getSelectionPath();
                  if (path == null)
                    return;
                  JMenuItem selectedItem = ((JMenuItem) ((MenuItemNode) path.getLastPathComponent()).getUserObject());
                  int index = availableActions.getSelectedIndex();
                  if (selectedItem != null){
                      String text = selectedItem.getText();
                      if (index == 0){ // null action case
                          if (selectedItem instanceof ESlateMenuItem)
                              ((ESlateMenuItem) selectedItem).setActionName(null);
                          else if (selectedItem instanceof ESlateCheckMenuItem)
                              ((ESlateCheckMenuItem) selectedItem).setActionName(null);
                          selectedItem.setAction(null);
                      }else{
                          if (selectedItem instanceof ESlateMenuItem)
                              ((ESlateMenuItem) selectedItem).setActionName((String) ownerActions.allKeys()[index-1]);
                          else if (selectedItem instanceof ESlateCheckMenuItem)
                              ((ESlateCheckMenuItem) selectedItem).setActionName((String) ownerActions.allKeys()[index-1]);
                          selectedItem.setAction(ownerActions.get(ownerActions.allKeys()[index-1]));
                      }
                      selectedItem.setText(text);
                  }
              }
          });
        }

        itemEnabled = new JCheckBox();
        itemEnabled.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JMenuItem selectedItem = ((JMenuItem) ((MenuItemNode) treePanel.getTree().getSelectionPath().getLastPathComponent()).getUserObject());
                if (selectedItem != null){
                    selectedItem.setEnabled(itemEnabled.isSelected());
                }
            }
        });




        panel1.setLayout(new BoxLayout(panel1,BoxLayout.X_AXIS));
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
        panel3.setLayout(new BoxLayout(panel3,BoxLayout.X_AXIS));
        panel1.add(label1);
        panel1.add(itemName);
        panel1.add(Box.createGlue());
        panel2.add(label2);
        panel2.add(availableActions);
        panel2.add(Box.createGlue());
        panel3.add(label3);
        panel3.add(itemEnabled);
        panel3.add(Box.createGlue());
        mainPanel.add(Box.createVerticalStrut(3));
        mainPanel.add(panel1);
        mainPanel.add(Box.createVerticalStrut(3));
        mainPanel.add(panel2);
        mainPanel.add(Box.createVerticalStrut(3));
        mainPanel.add(panel3);
        mainPanel.add(Box.createGlue());
        mainPanel.setBorder(new TitledBorder("Selected Item Properties"));
        return mainPanel;
    }

    JPanel createOKCancelPanel(){

        // The button panel (APPLY, OK, CANCEL)
        okButton = new JButton("OK");
        okButton.setRequestFocusEnabled(false);
        Color color128 = new Color(0, 0, 128);

        buttonSize = new Dimension(90, 25);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);

        okButton.setMargin(zeroInsets);

        cancelButton = new JButton("Cancel");
        cancelButton.setRequestFocusEnabled(false);

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
        return buttonPanel;
    }

    public void registerButtonActions(){

        treePanel.getTree().addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    TreePath previousPath = e.getOldLeadSelectionPath();
                    if (previousPath != null){
                        Object previousLast = previousPath.getLastPathComponent();
                        if (((DefaultMutableTreeNode) previousLast).getUserObject() instanceof ESlateMenuItem ||
                            ((DefaultMutableTreeNode) previousLast).getUserObject() instanceof ESlateCheckMenuItem){
                            if (!((JMenuItem) ((DefaultMutableTreeNode) previousLast).getUserObject()).getText().equals(itemName.getText()))
                                ((JMenuItem) ((DefaultMutableTreeNode) previousLast).getUserObject()).setText(itemName.getText());
                        }
                    }
                    Object root = treePanel.getTree().getModel().getRoot();
                    TreePath selectionPath = treePanel.getTree().getSelectionPath();
                    if (selectionPath == null
                        || (selectionPath.getLastPathComponent() == root && !(((MenuItemNode) selectionPath.getLastPathComponent()).getUserObject() instanceof ESlatePopupMenu))
                        || selectionPath.getLastPathComponent() instanceof CustomSeparator) {
                        separatorButton.setEnabled(false);
                    } else {
                        separatorButton.setEnabled(true);
                    }
                    if (selectionPath != null && selectionPath.getLastPathComponent() instanceof CustomSeparator) {
                        addButton.setEnabled(false);
                        checkButton.setEnabled(false);
                    } else {
                        addButton.setEnabled(true);
                        checkButton.setEnabled(true);
                    }
                    if (selectionPath != null &&
                        ((MenuItemNode) selectionPath.getLastPathComponent()).getPreviousSibling() != null)
                        upButton.setEnabled(true);
                    else upButton.setEnabled(false);

                    if (selectionPath != null &&
                        ((MenuItemNode) selectionPath.getLastPathComponent()).getNextSibling() != null)
                        downButton.setEnabled(true);
                    else
                        downButton.setEnabled(false);


                    if (selectionPath == null ||selectionPath.getLastPathComponent() instanceof CustomSeparator
                        || selectionPath.getLastPathComponent() == root){
                        itemName.setText("");
                        itemName.setBackground(Color.lightGray);
                        itemName.setEnabled(false);
                        availableActions.setEnabled(false);
                        itemEnabled.setEnabled(false);
                    }else if (!((MenuItemNode) selectionPath.getLastPathComponent()).isLeaf() ||
                        ((MenuItemNode) selectionPath.getLastPathComponent()).getUserObject() instanceof ESlateMenu){
                        itemName.setEnabled(true);
                        itemEnabled.setEnabled(true);
                        itemName.setBackground(Color.white);
                        availableActions.setEnabled(false);
                        itemName.setText(((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).toString());
                        itemEnabled.setSelected(((JMenuItem) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject()).isEnabled());
                    }else{
                        itemName.setText(((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).toString());
                        itemName.setEnabled(true);
                        itemEnabled.setEnabled(true);
                        itemName.setBackground(Color.white);
                        availableActions.setEnabled(true);
                        Action action = ((JMenuItem) ((MenuItemNode) selectionPath.getLastPathComponent()).getUserObject()).getAction();
                        ESlatePopupMenu popup = (ESlatePopupMenu) ((MenuItemNode) treePanel.getTree().getModel().getRoot()).getUserObject();
                        JComponent owner = null;
                        if (popup.getESlateHandle() != null && popup.getESlateHandle().getParentHandle() !=null
                            && popup.getESlateHandle().getParentHandle().getComponent() != null){

                            owner = (JComponent) popup.getESlateHandle().getParentHandle().getComponent();
                        }

                        if (owner == null){
                            System.out.println("No owner for this popupMenu. Binding cannot occur.");
                            return;
                        }
                        ActionMap actions = owner.getActionMap();
                        int selectedIndex = -1;
                        for (int i =0; i<actions.size();i++){
                            if (actions.get(actions.allKeys()[i]) == action){
                                selectedIndex = i;
                                break;
                            }
                        }
                        lockActionAssingments = true;
                        if (selectedIndex != -1)
                            availableActions.setSelectedIndex(selectedIndex+1);
                        else
                            availableActions.setSelectedIndex(0);
                        itemEnabled.setSelected(((JMenuItem) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject()).isEnabled());
                        lockActionAssingments = false;

                    }
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
                    treePanel.addObject(new ESlateMenuItem("New Node " + newNodeSuffix++));
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
                    ESlateCheckMenuItem item =new ESlateCheckMenuItem("New Node " + newNodeSuffix++);
                    treePanel.addCheckObject(item);
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

    private void updateSelectedItemText(){
        DefaultMutableTreeNode o = (DefaultMutableTreeNode) treePanel.getTree().getSelectionPath().getLastPathComponent();
        if (o.getUserObject() instanceof String)
            o.setUserObject(itemName.getText());
        else{
            ((JMenuItem)o.getUserObject()).setText(itemName.getText());
        }
         //   ((JMenuItem)o.getUserObject()).setText(itemName.getText());

        treePanel.getTree().repaint();
        treePanel.getTree().revalidate();
    }
}

