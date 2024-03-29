/*
 * Created on 4 ���� 2006
 *
 */
package gr.cti.eslate.scene3d.utils;

import gr.cti.eslate.math.linalg.Vec3d;
import gr.cti.eslate.spinButton.NumberDataModel;
import gr.cti.eslate.spinButton.SpinButton;
import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoundingBoxPropertyEditor extends PropertyEditorSupport{
    private PropertyChangeSupport pcs;
    private Vec3d value = new Vec3d();
    private static ResourceBundle bundleMessages;
    PropertyEditorUI editorUI;
//    Vector v;

    public BoundingBoxPropertyEditor() {
        super();
        bundleMessages = ResourceBundle.getBundle(
                "gr.cti.eslate.scene3d.BundleMessages", Locale.getDefault());
        pcs = new PropertyChangeSupport(this);
        editorUI = new PropertyEditorUI();
    }

    public void setValue(Object value) {
        if (!Vec3d.class.isInstance(value))
            return;
        Vec3d v = (Vec3d) value;
        if (v!=null){
            this.value.set(v);
            editorUI.n1.setValue(new Double(v.x()));
            editorUI.n2.setValue(new Double(v.y()));   
            editorUI.n3.setValue(new Double(v.z())); 
        }
    }

    public Object getValue() {
        return new Vec3d(((Number) editorUI.n1.getValue()).doubleValue(),
                ((Number) editorUI.n2.getValue()).doubleValue(),
                ((Number) editorUI.n3.getValue()).doubleValue());
    }

    public Component getCustomEditor() {
        return editorUI;
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
    
    class PropertyEditorUI extends JPanel{
        
        SpinButton n1 = new SpinButton(),
                   n2 = new SpinButton(),
                   n3 = new SpinButton();
        
        JLabel label1 = new JLabel(bundleMessages.getString("XDim")),
               label2 = new JLabel(bundleMessages.getString("YDim")),
               label3 = new JLabel(bundleMessages.getString("ZDim"));
        Dimension size = new Dimension(80,60);
        
        PropertyEditorUI(){
            super();
            createUI();
            n1.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    fireChange();
                }
            });
            n2.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    fireChange();
                }
            });
            n3.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    fireChange();
                }
            });

        }
        
        private void fireChange(){
            Vec3d oldObj = value;
            Vec3d newObj = new Vec3d(((Number) editorUI.n1.getValue()).doubleValue(),
                    ((Number) editorUI.n2.getValue()).doubleValue(),
                    ((Number) editorUI.n3.getValue()).doubleValue());
            pcs.firePropertyChange("BoundingBoxDimensions", oldObj, newObj);
        }
        
        public Dimension getMaximumSize(){
            return size;
        }
        
        public Dimension getMinimumSize(){
            return size;
        }
        
        public Dimension getPreferredSize(){
            return size;
        }
        
        void createUI(){
            setLayout(new GridLayout(3,3));
            add(label1);
            add(n1);
            add(label2);
            add(n2);
            add(label3);
            add(n3);

            n1.model.setMinimumValue(0);
            n2.model.setMinimumValue(0);
            n3.model.setMinimumValue(0);
        }
        
    }
}
