package gr.cti.eslate.eslateComboBox;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.awt.Dimension;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import java.util.ResourceBundle;
import gr.cti.eslate.utils.*;


public class ComboBoxPropertyEditor extends PropertyEditorSupport {
    PropertyChangeSupport pcs;
    NoBorderButton button;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    ComboBoxVector oldvector = null;
    ComboBoxVector vector = new ComboBoxVector();
    ComboBoxDialog dialog;
    static boolean dialogWasCancelled = false;
    int size;
    ResourceBundle bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateComboBox.ComboBoxBundle", Locale.getDefault());

    Object item = new Object();
    ResourceBundle colorEditorBundle;

    /**
     * Constructs a new PropertyEditor that can manipulate the combobox elements
     */

    public ComboBoxPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Adds a new PropertyChangeListener
     * @param listener The PropertyChangeListener to be added
     */

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener
     * @param listener The PropertyChangeListener to be removed
     */

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Sets value to the property editor
     * @param value The value to be set
     */

    public void setValue(Object value) {
        if (!ComboBoxModel.class.isInstance(value))
            return;
        if (ComboBoxVector.class.isAssignableFrom(value.getClass()))
            vector = (ComboBoxVector) value;
        else
            vector = createVectorFromModel((ComboBoxModel) value);
    }

    private ComboBoxVector createVectorFromModel(ComboBoxModel model) {
        for (int i = 0; i < model.getSize(); i++) {
            vector.add(i, model.getElementAt(i));
        }
        return vector;
    }

    /**
     * Returns object value from the property editor
     */

    public Object getValue() {
        return vector;
    }

    /**
     * Returns the custom editor for this property
     */

    public java.awt.Component getCustomEditor() {

        button = new NoBorderButton(bundleMessages.getString("Define Elements"));

        Dimension buttonSize = new Dimension(80, 22);

        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFrame frame = new JFrame();
                    Image image = logo.getImage();

                    frame.setIconImage(image);
                    oldvector = vector;
                    dialog = new ComboBoxDialog(oldvector, frame, true);
                    ComboBoxDialog.showDialog(dialog, button, true);
                    if (dialog.getChangedValues() == true) {
                        vector = dialog.getVector();
                        pcs.firePropertyChange("Model", oldvector, vector);
                        oldvector = (ComboBoxVector) vector.clone();
                    }

                }
            }
        );

        return button;
    }

    //    public String getAsText() {
    //        return null;
    //    }

    /**
     * This method returns true to show that this editor can be a custom editor
     */

    public boolean supportsCustomEditor() {
        return true;
    }
}
