package gr.cti.eslate.eslateList;


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


public class ListPropertyEditor extends PropertyEditorSupport {
    PropertyChangeSupport pcs;
    NoBorderButton button;
    ResourceBundle bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateList.ListBundle", Locale.getDefault());
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    ListVector oldvector = null;
    ListVector vector = new ListVector();
    ListDialog dialog;
    static boolean dialogWasCancelled = false;
    int size;

    Object item = new Object();
    ResourceBundle colorEditorBundle;

    public ListPropertyEditor() {
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
        if (!ListModel.class.isInstance(value))
            return;
        if (ListVector.class.isAssignableFrom(value.getClass()))
            vector = (ListVector) value;
        else
            vector = createVectorFromModel((ListModel) value);
    }

    private ListVector createVectorFromModel(ListModel model) {
        for (int i = 0; i < model.getSize(); i++) {
            vector.add(i, model.getElementAt(i));
        }
        return vector;
    }

    public Object getValue() {
        return vector;
    }

    public java.awt.Component getCustomEditor() {

        button = new NoBorderButton(bundleMessages.getString("Define elements"));

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
                    dialog = new ListDialog(oldvector, frame, true);
                    ListDialog.showDialog(dialog, button, true);
                    if (dialog.getchangedValues() == true) {
                        vector = dialog.getVector();
                        pcs.firePropertyChange("Model", oldvector, vector);
                        oldvector = (ListVector) vector.clone();
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
