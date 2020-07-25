package gr.cti.eslate.base.container;

import gr.cti.eslate.imageEditor.ImageEditorDialog;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class IconPropertyEditor extends PropertyEditorSupport {
    Icon _icon;
    PropertyChangeSupport pcs;
    Locale locale;
    ResourceBundle iconPropertyEditorBundle;
    boolean localeIsGreek = false;
    Component editorComponent = null;
    static File homeFolder=null;


    public IconPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        locale = Locale.getDefault();
        iconPropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.IconPropertyEditorBundle", locale);
        if (iconPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.IconPropertyEditorBundle_el_GR"))
            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Icon.class.isInstance(value))
            return;
        if (_icon != null && ImageIcon.class.isAssignableFrom(_icon.getClass()))
            ((ImageIcon) _icon).getImage().flush();
        _icon = (Icon) value;
    }

    public Object getValue() {
        return _icon;
    }

    public java.awt.Component getCustomEditor() {
        NoBorderButton editButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/imageEdit.gif")));
        editButton.setToolTipText(iconPropertyEditorBundle.getString("EditIcon"));
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        editButton.setMargin(zeroInsets);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try{
                    ImageEditorDialog imageEditorDialog;
                    Icon oldIcon = _icon;
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, (JButton) e.getSource());
//System.out.println("IconPropertyEditor topLevelFrame: " + topLevelFrame);
                    imageEditorDialog = new ImageEditorDialog(topLevelFrame, 32, 32); //topLevelFrame, 32, 32);
                    imageEditorDialog.getImageEditor().getFileChooser().setCurrentDirectory(homeFolder);
                    if (_icon != null) {
                        /* we have to supply an ImageIcon to the ImageEditor, but we have an Icon.
                         * If the icon can be casted to an ImageIcon, then everything is fine.
                         * Otherwise create the required ImageIcon by drawing the Icon on an Image
                         * and putting this image in the new ImageIcon.
                         */
/*                        if (!ImageIcon.class.isAssignableFrom(_icon.getClass())) {
                            Icon ic = _icon;
                            _icon = new ImageIcon();
                            Image img1 = editorComponent.createImage(ic.getIconWidth(), ic.getIconHeight());
                            ((Icon) ic).paintIcon(null, img1.getGraphics(), 0, 0);
                            ((ImageIcon) _icon).setImage(img1);
                          }
*/

                        imageEditorDialog.setIcon(_icon);//(ImageIcon) _icon);
                    }
//                    if (icon.getForegroundColor() != null) {
//                        iconEditor.setForegroundColor(icon.getForegroundColor(), icon.getForegroundTransparency());
//                    }
//                    if (icon.getBackgroundColor() != null) {
//                        iconEditor.setBackgroundColor(icon.getBackgroundColor(), icon.getBackgroundTransparency());
//                    }
//                    iconEditor.setFileName(icon.getFileName());
                    imageEditorDialog.showDialog(null);
                    homeFolder=imageEditorDialog.getImageEditor().getFileChooser().getCurrentDirectory();
                    if (imageEditorDialog.getReturnCode() == ImageEditorDialog.IMAGE_EDITOR_OK) {
                        _icon = imageEditorDialog.getIcon(); //imageEditorDialog.getImage();
                        pcs.firePropertyChange("Icon", oldIcon, _icon);
                    }
                    if (oldIcon != null && ImageIcon.class.isAssignableFrom(oldIcon.getClass()))
                        ((ImageIcon) oldIcon).getImage().flush();
                    oldIcon = null;
/*                }catch (IconTooBigException exc) {
                    ESlateOptionPane.showMessageDialog(new JFrame(), exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
*/            }
        });
        NoBorderButton clearButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/clear.gif")));
        clearButton.setToolTipText(iconPropertyEditorBundle.getString("ClearIcon"));
        clearButton.setMargin(zeroInsets);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Icon oldIcon = _icon;
                _icon = null;
                pcs.firePropertyChange("Icon", oldIcon, _icon);
                if (oldIcon != null && ImageIcon.class.isAssignableFrom(oldIcon.getClass()))
                    ((ImageIcon) oldIcon).getImage().flush();
                oldIcon = null;
            }
        });

        JPanel iconEditorPanel = new JPanel(true);
        iconEditorPanel.setLayout(new BoxLayout(iconEditorPanel, BoxLayout.X_AXIS));
//        iconEditorPanel.add(Box.createGlue());
        iconEditorPanel.add(editButton);
        iconEditorPanel.add(Box.createHorizontalStrut(5));
        iconEditorPanel.add(clearButton);
        iconEditorPanel.add(Box.createGlue());

        editorComponent = iconEditorPanel;
        return iconEditorPanel;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
