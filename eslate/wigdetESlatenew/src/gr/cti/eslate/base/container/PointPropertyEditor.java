package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
//import pv.jfcx.JPVSpin;


public class PointPropertyEditor extends PropertyEditorSupport {
    Point point = new Point();
    PropertyChangeSupport pcs;
    Locale locale;
    ResourceBundle pointPropertyEditorBundle;
    boolean localeIsGreek = false;
    SpinField xPanel, yPanel;

    public PointPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        locale = Locale.getDefault();
        pointPropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.PointPropertyEditorBundle", locale);
        if (pointPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.PointPropertyEditorBundle_el_GR"))
            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Point.class.isInstance(value))
            return;
        point = (Point) value;
        if (xPanel != null)
            xPanel.spin.setValue(new Integer(point.x));
        if (yPanel != null)
            yPanel.spin.setValue(new Integer(point.y));
    }

    public Object getValue() {
        return point;
    }

    public java.awt.Component getCustomEditor() {
        xPanel = new SpinField(true, pointPropertyEditorBundle.getString("X"), point.x);
        yPanel = new SpinField(true, pointPropertyEditorBundle.getString("Y"), point.y);
        xPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        yPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        SpinField[] spins = new SpinField[] {xPanel, yPanel};
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 50, 20);

        xPanel.setMinValue(0);
        yPanel.setMinValue(0);

        xPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int x = ((Number) evt.getValue()).intValue();
                Point oldPoint = new Point(point);
                point.x = x;
                pcs.firePropertyChange("Point", oldPoint, point);
            }
        });

        yPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int y = ((Number) evt.getValue()).intValue();
                Point oldPoint = new Point(point);
                point.y = y;
                pcs.firePropertyChange("Point", oldPoint, point);
            }
        });

        JPanel pointEditorPanel = new JPanel(true) {
            public Dimension getPreferredSize() {
//                System.out.println("Returning dimension with height: " + (2*xPanel.getPreferredSize().height));
                return new Dimension(0, 2*xPanel.getPreferredSize().height);
            }
            public void setEnabled(boolean enabled) {
                xPanel.setEnabled(enabled);
                yPanel.setEnabled(enabled);
            }
        };
        pointEditorPanel.setLayout(new BoxLayout(pointEditorPanel, BoxLayout.Y_AXIS));
        pointEditorPanel.add(xPanel);
        pointEditorPanel.add(yPanel);

        //Initialization
        xPanel.spin.setValue(new Integer(point.x));
        yPanel.spin.setValue(new Integer(point.y));

        return pointEditorPanel;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
