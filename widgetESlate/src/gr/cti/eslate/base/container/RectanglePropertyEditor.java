package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class RectanglePropertyEditor extends PropertyEditorSupport {
    Rectangle rect;
    PropertyChangeSupport pcs;
    Locale locale;
    ResourceBundle rectPropertyEditorBundle;
    boolean localeIsGreek = false;
    SpinField widthPanel, heightPanel, xPanel, yPanel;

    public RectanglePropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        locale = Locale.getDefault();
        rectPropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.RectanglePropertyEditorBundle", locale);
        if (rectPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.RectanglePropertyEditorBundle_el_GR"))
            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Rectangle.class.isInstance(value))
            return;
        rect = (Rectangle) value;
        if (xPanel != null)
            xPanel.spin.setValue(new Integer(rect.x));
        if (yPanel != null)
            yPanel.spin.setValue(new Integer(rect.y));
        if (widthPanel != null)
            widthPanel.spin.setValue(new Integer(rect.width));
        if (heightPanel != null)
            heightPanel.spin.setValue(new Integer(rect.height));
    }

    public Object getValue() {
        return rect;
    }

    public java.awt.Component getCustomEditor() {
//        System.out.println("getCustomEditor() rect: " + rect);
        xPanel = new SpinField(true, rectPropertyEditorBundle.getString("X"), (rect==null)?0:rect.x);
        yPanel = new SpinField(true, rectPropertyEditorBundle.getString("Y"), (rect==null)?0:rect.y);
        widthPanel = new SpinField(true, rectPropertyEditorBundle.getString("Width"), (rect==null)?0:rect.width);
        heightPanel = new SpinField(true, rectPropertyEditorBundle.getString("Height"), (rect==null)?0:rect.height);
        xPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        yPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        widthPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        heightPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
//        heightPanel.setBorder(new LineBorder(Color.red));

        SpinField[] spins = new SpinField[] {xPanel, yPanel, widthPanel, heightPanel};
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 50, 20);

        xPanel.setMinValue(0);
        yPanel.setMinValue(0);
        widthPanel.setMinValue(0);
        heightPanel.setMinValue(0);

        xPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newX = ((Number) evt.getValue()).intValue();
                Rectangle oldRect;
                if (rect == null)
                    rect = new Rectangle();
                oldRect = new Rectangle(rect);
                rect.x = newX;
                pcs.firePropertyChange("Rectangle", oldRect, rect);
            }
        });
        yPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newY = ((Number) evt.getValue()).intValue();
                if (rect == null)
                    rect = new Rectangle();
                Rectangle oldRect = new Rectangle(rect);
                rect.y = newY;
                pcs.firePropertyChange("Rectangle", oldRect, rect);
            }
        });
        widthPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newWidth = ((Number) evt.getValue()).intValue();
                if (rect == null)
                    rect = new Rectangle();
                Rectangle oldRect = new Rectangle(rect);
                rect.width = newWidth;
                pcs.firePropertyChange("Rectangle", oldRect, rect);
            }
        });
        heightPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newHeight = ((Number) evt.getValue()).intValue();
                if (rect == null)
                    rect = new Rectangle();
                Rectangle oldRect = new Rectangle(rect);
                rect.height = newHeight;
                pcs.firePropertyChange("Rectangle", oldRect, rect);
            }
        });

        JPanel rectangleEditorPanel = new JPanel(true) {
            public Dimension getPreferredSize() {
//                System.out.println("Returning dimension with height: " + (2*widthPanel.getPreferredSize().height));
                return new Dimension(0, 4*widthPanel.getPreferredSize().height);
            }
            public void setEnabled(boolean enabled) {
                xPanel.setEnabled(enabled);
                yPanel.setEnabled(enabled);
                widthPanel.setEnabled(enabled);
                heightPanel.setEnabled(enabled);
            }
        };
        rectangleEditorPanel.setLayout(new BoxLayout(rectangleEditorPanel, BoxLayout.Y_AXIS));
        rectangleEditorPanel.add(xPanel);
        rectangleEditorPanel.add(yPanel);
        rectangleEditorPanel.add(widthPanel);
        rectangleEditorPanel.add(heightPanel);

        //Initialization
        xPanel.spin.setValue(new Integer((rect==null)?0:rect.x));
        yPanel.spin.setValue(new Integer((rect==null)?0:rect.y));
        widthPanel.spin.setValue(new Integer((rect==null)?0:rect.width));
        heightPanel.spin.setValue(new Integer((rect==null)?0:rect.height));

        return rectangleEditorPanel;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
