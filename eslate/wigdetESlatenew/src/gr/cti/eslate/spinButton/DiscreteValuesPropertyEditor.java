package gr.cti.eslate.spinButton;


import gr.cti.eslate.utils.NoBorderButton;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class DiscreteValuesPropertyEditor extends PropertyEditorSupport {
    PropertyChangeSupport pcs;
    NoBorderButton button;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    Vector oldvector = null;
    Vector vector = new Vector();
    DiscreteValuesDialog dialog;
    static boolean dialogWasCancelled = false;
    int size;
    ResourceBundle bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());

    Object item = new Object();
    //ResourceBundle colorEditorBundle;

    public DiscreteValuesPropertyEditor() {
        super();
        //        System.out.println("constructing so far property editor");
        pcs = new PropertyChangeSupport(this);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void setValue(Object value) {
        vector = (Vector) value;
    }

    public Object getValue() {
        return vector;
    }

    public java.awt.Component getCustomEditor() {

        button = new NoBorderButton(bundleMessages.getString("Elements"));

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
                    dialog = new DiscreteValuesDialog(oldvector, frame, true);
                    DiscreteValuesDialog.showDialog(dialog, button, true);
                    if (dialog.getchangedValues() == true) {
                        vector = dialog.getVector();
                        pcs.firePropertyChange("Elements", oldvector, vector);
                        oldvector = (Vector) vector.clone();
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
