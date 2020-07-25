package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
//import pv.jfcx.JPVSpin;


public class DimensionPropertyEditor extends PropertyEditorSupport {
    Dimension dim;
    PropertyChangeSupport pcs;
    Locale locale;
    ResourceBundle dimPropertyEditorBundle;
    boolean localeIsGreek = false;
    SpinField widthPanel, heightPanel;

    public DimensionPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        locale = Locale.getDefault();
        dimPropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.DimensionPropertyEditorBundle", locale);
        if (dimPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.DimensionPropertyEditorBundle_el_GR"))
            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Dimension.class.isInstance(value))
            return;
        dim = (Dimension) value;
        if (widthPanel != null)
            widthPanel.spin.setValue(new Integer(dim.width));
        if (heightPanel != null)
            heightPanel.spin.setValue(new Integer(dim.height));
    }

    public Object getValue() {
        return dim;
    }

    public java.awt.Component getCustomEditor() {
        widthPanel = new SpinField(true, dimPropertyEditorBundle.getString("Width"), dim.width);
        heightPanel = new SpinField(true, dimPropertyEditorBundle.getString("Height"), dim.height);
        widthPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        heightPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        SpinField[] spins = new SpinField[] {widthPanel, heightPanel};
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 50, 20);

        widthPanel.setMinValue(0);
        heightPanel.setMinValue(0);

        widthPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newWidth = ((Number) evt.getValue()).intValue();
                Dimension oldDim = new Dimension(dim);
                dim.width = newWidth;
                pcs.firePropertyChange("Dimension", oldDim, dim);
            }
        });

        heightPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newHeight = ((Number) evt.getValue()).intValue();
                Dimension oldDim = new Dimension(dim);
                dim.height = newHeight;
                pcs.firePropertyChange("Dimension", oldDim, dim);
            }
        });

        JPanel dimensionEditorPanel = new JPanel(true) {
            public Dimension getPreferredSize() {
//                System.out.println("Returning dimension with height: " + (2*widthPanel.getPreferredSize().height));
                return new Dimension(0, 2*widthPanel.getPreferredSize().height);
            }
            public void setEnabled(boolean enabled) {
                widthPanel.setEnabled(enabled);
                heightPanel.setEnabled(enabled);
            }
        };
        dimensionEditorPanel.setLayout(new BoxLayout(dimensionEditorPanel, BoxLayout.Y_AXIS));
        dimensionEditorPanel.add(widthPanel);
        dimensionEditorPanel.add(heightPanel);

        //Initialization
        widthPanel.spin.setValue(new Integer(dim.width));
        heightPanel.spin.setValue(new Integer(dim.height));

        return dimensionEditorPanel;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
