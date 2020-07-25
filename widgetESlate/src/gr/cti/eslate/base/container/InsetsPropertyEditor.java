package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Dimension;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class InsetsPropertyEditor extends PropertyEditorSupport {
    Insets insets;
    PropertyChangeSupport pcs;
    Locale locale;
    ResourceBundle insetsPropertyEditorBundle;
    boolean localeIsGreek = false;
    SpinField topPanel, bottomPanel, leftPanel, rightPanel;
    java.awt.Component component;

    public InsetsPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        locale = Locale.getDefault();
        insetsPropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.InsetsPropertyEditorBundle", locale);
        if (insetsPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.InsetsPropertyEditorBundle_el_GR"))
            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Insets.class.isInstance(value))
            return;
        insets = (Insets) value;
        if (rightPanel != null)
            rightPanel.spin.setValue(new Integer(insets.right));
        if (leftPanel != null)
            leftPanel.spin.setValue(new Integer(insets.left));
        if (topPanel != null)
            topPanel.spin.setValue(new Integer(insets.top));
        if (bottomPanel != null)
            bottomPanel.spin.setValue(new Integer(insets.bottom));
    }

    public Object getValue() {
        return insets;
    }

    public java.awt.Component getCustomEditor() {
        topPanel = new SpinField(true, insetsPropertyEditorBundle.getString("Top"), insets.top);
        bottomPanel = new SpinField(true, insetsPropertyEditorBundle.getString("Bottom"), insets.bottom);
        rightPanel = new SpinField(true, insetsPropertyEditorBundle.getString("Right"), insets.right);
        leftPanel = new SpinField(true, insetsPropertyEditorBundle.getString("Left"), insets.left);
        topPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        bottomPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        rightPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        leftPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        SpinField[] spins = new SpinField[] {topPanel, bottomPanel, rightPanel, leftPanel};
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 50, topPanel.getPreferredSize().height);

        topPanel.setMinValue(0);
        bottomPanel.setMinValue(0);
        rightPanel.setMinValue(0);
        leftPanel.setMinValue(0);

        topPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newTop = ((Number) evt.getValue()).intValue();
                Insets oldInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
                insets.top = newTop;
                pcs.firePropertyChange("Insets", oldInsets, insets);
                if (component != null)
                    component.repaint();
            }
        });
        bottomPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newBottom = ((Number) evt.getValue()).intValue();
                Insets oldInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
                insets.bottom = newBottom;
                pcs.firePropertyChange("Insets", oldInsets, insets);
                if (component != null)
                    component.repaint();
            }
        });
        rightPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newRight = ((Number) evt.getValue()).intValue();
                Insets oldInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
                insets.right = newRight;
                pcs.firePropertyChange("Insets", oldInsets, insets);
                if (component != null)
                    component.repaint();
            }
        });
        leftPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newLeft = ((Number) evt.getValue()).intValue();
                Insets oldInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
                insets.left = newLeft;
                pcs.firePropertyChange("Insets", oldInsets, insets);
                if (component != null)
                    component.repaint();
            }
        });

        JPanel insetsEditorPanel = new JPanel(true) {
            public Dimension getPreferredSize() {
//                System.out.println("Insets SpinField preferred height: " + rightPanel.getPreferredSize());
//                System.out.println("Returning dimension with height: " + (4*rightPanel.getPreferredSize().height));
                return new Dimension(0, 4*rightPanel.getPreferredSize().height+10);
            }
            public void setEnabled(boolean enabled) {
                topPanel.setEnabled(enabled);
                bottomPanel.setEnabled(enabled);
                rightPanel.setEnabled(enabled);
                leftPanel.setEnabled(enabled);
            }
        };
        insetsEditorPanel.setLayout(new BoxLayout(insetsEditorPanel, BoxLayout.Y_AXIS));
        insetsEditorPanel.add(topPanel);
        insetsEditorPanel.add(Box.createVerticalStrut(3));
        insetsEditorPanel.add(bottomPanel);
        insetsEditorPanel.add(Box.createVerticalStrut(3));
        insetsEditorPanel.add(rightPanel);
        insetsEditorPanel.add(Box.createVerticalStrut(3));
        insetsEditorPanel.add(leftPanel);

        //Initialization
        topPanel.spin.setValue(new Integer(insets.top));
        bottomPanel.spin.setValue(new Integer(insets.bottom));
        rightPanel.spin.setValue(new Integer(insets.right));
        leftPanel.spin.setValue(new Integer(insets.left));

        return insetsEditorPanel;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public void setComponent(java.awt.Component comp) {
        this.component = comp;
    }
}

