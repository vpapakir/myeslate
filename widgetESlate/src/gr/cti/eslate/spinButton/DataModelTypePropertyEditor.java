package gr.cti.eslate.spinButton;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;


public class DataModelTypePropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    int model = SpinButton.DATE_DATA_MODEL;
    JComboBox comboBox;
    Vector v;
    ResourceBundle bundleMessages;

    public DataModelTypePropertyEditor() {
        super();
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());
        pcs = new PropertyChangeSupport(this);
        v = new Vector();
        v.addElement(bundleMessages.getString("DateDataModel"));
        v.addElement(bundleMessages.getString("TimeDataModel"));
        v.addElement(bundleMessages.getString("NumberDataModel"));
        v.addElement(bundleMessages.getString("EnumeratedDataModel"));
        comboBox = new JComboBox(v);
        comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int oldModel = model;
                    int newModel = (int) comboBox.getSelectedIndex();

                    pcs.firePropertyChange("modelType", oldModel, newModel);
                    //              System.out.println("actionPerformed and model is : "+newModel);
                    model = newModel;
                }
            }
        );
    }

    public void setValue(Object value) {
        model = ((Integer) value).intValue();
        comboBox.setSelectedIndex(model);
    }

    public Object getValue() {
        return new Integer(model);
    }

    public Component getCustomEditor() {
        return comboBox;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);

    }

}
