package gr.cti.eslate.base.container;

import gr.cti.eslate.imageEditor.ImageEditorDialog;
import gr.cti.eslate.utils.NoBorderButton;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImagePropertyEditor extends PropertyEditorSupport {
    Image _image;
    PropertyChangeSupport pcs;
    Locale locale;
    ResourceBundle iconPropertyEditorBundle;
    boolean localeIsGreek = false;
    Component editorComponent = null;

    public static final String IMAGE_PROPERTY="image";

    public ImagePropertyEditor() {
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
     if (!Image.class.isInstance(value)) return;
     if (_image != null) _image.flush();
     _image = (Image)value;
    }

    public Object getValue() {
        return _image;
    }

    public java.awt.Component getCustomEditor() {
        NoBorderButton editButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/imageEdit.gif")));
        editButton.setToolTipText(iconPropertyEditorBundle.getString("EditIcon"));
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        editButton.setMargin(zeroInsets);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try{
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, (JButton) e.getSource());
                    ImageEditorDialog imageEditorDialog;
                    Image oldImage = _image;
                    imageEditorDialog = new ImageEditorDialog(topLevelFrame, 32, 32); //topLevelFrame, 32, 32);
                    if(_image!=null) imageEditorDialog.setImage(_image); //new ImageIcon(_image));

//                    if (icon.getForegroundColor() != null) {
//                        iconEditor.setForegroundColor(icon.getForegroundColor(), icon.getForegroundTransparency());
//                    }
//                    if (icon.getBackgroundColor() != null) {
//                        iconEditor.setBackgroundColor(icon.getBackgroundColor(), icon.getBackgroundTransparency());
//                    }
//                    iconEditor.setFileName(icon.getFileName());
                    imageEditorDialog.showDialog(null);
                    if (imageEditorDialog.getReturnCode() == ImageEditorDialog.IMAGE_EDITOR_OK) {
                        _image = imageEditorDialog.getImage(); //imageEditorDialog.getImage().getImage();
                        pcs.firePropertyChange(IMAGE_PROPERTY, oldImage, _image);
                    }
                    if (oldImage != null) oldImage.flush();
                    oldImage = null;
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
                Image oldImage = _image;
                _image = null;
                pcs.firePropertyChange(IMAGE_PROPERTY, oldImage, _image);
                if (oldImage != null) oldImage.flush();
                oldImage = null;
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

//////////////////////////

// public static void register(){
//  java.beans.PropertyEditorManager.registerEditor(Image.class, ImagePropertyEditor.class);
// }

}
