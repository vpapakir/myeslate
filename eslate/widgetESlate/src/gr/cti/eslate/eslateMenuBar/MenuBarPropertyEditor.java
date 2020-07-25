package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.utils.NoBorderButton;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;


public class MenuBarPropertyEditor extends PropertyEditorSupport {
    PropertyChangeSupport pcs;
    ResourceBundle bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateMenuBar.BundleMessages", Locale.getDefault());
    NoBorderButton button;
    MenuItemNode oldnode = null;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));

    MenuItemNode node = new MenuItemNode();
    TreeDialog dialog;
    static boolean changedValues;
    int size;

    public MenuBarPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void setValue(Object value) {
        node = (MenuItemNode) value;
    }

    public Object getValue() {
        return node;
    }

    public java.awt.Component getCustomEditor() {

        button = new NoBorderButton(bundleMessages.getString("Menus..."));
        button.setMargin(new Insets(0, 0, 0, 0));

        Dimension buttonSize = new Dimension(80, 22);

        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFrame frame = new JFrame();
                    Image image = logo.getImage();
                    frame.setIconImage(image);
                    if (oldnode == null)
                        oldnode = (MenuItemNode) node;
                    JTree oldtree = new JTree(oldnode);

                    dialog = new TreeDialog(oldtree, frame, true);
                    TreeDialog.showDialog(dialog, button, true);
                    if (dialog.getChangedValues() == true) {
                        node = (MenuItemNode) dialog.getMenuTree().getModel().getRoot();
                        pcs.firePropertyChange("Menus", oldnode, node);

                        //JTree tree = new JTree(node);
                        oldnode = node;//(MenuItemNode) dialog.createIdenticalTree(tree).getModel().getRoot();
                    }
                }
            }
        );
        return button;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
