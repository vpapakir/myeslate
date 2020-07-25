/*
 * Created on 19 Ιουν 2006
 *
 */
package gr.cti.eslate.scene3d.utils;

import gr.cti.eslate.eslateTextField.BundleMessages;
import gr.cti.eslate.spinButton.SpinButton;
import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;
import gr.cti.eslate.math.linalg.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScalePropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    private Vec3d value = new Vec3d();
    PropertyEditorUI editorUI;
    private static ResourceBundle bundleMessages;

    public ScalePropertyEditor() {
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
        JLabel label1 = new JLabel(bundleMessages.getString("XScale")),
               label2 = new JLabel(bundleMessages.getString("YScale")),
               label3 = new JLabel(bundleMessages.getString("ZScale"));
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
            pcs.firePropertyChange("Scale", oldObj, newObj);
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
//            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
//            JPanel inPanel1 = new JPanel();
//            inPanel1.setLayout(new GridLayout(1,2));
//            inPanel1.add(label1);
//            inPanel1.add(n1);
//            add(inPanel1);
//            JPanel inPanel2 = new JPanel();
//            inPanel2.setLayout(new GridLayout(1,2));
//            inPanel2.add(label2);
//            inPanel2.add(n2);
//            add(inPanel2);
//            JPanel inPanel3 = new JPanel();
//            inPanel3.setLayout(new GridLayout(1,2));
//            inPanel3.add(label3);
//            inPanel3.add(n3);
//            add(inPanel3);
        }
        
    }
    
    public static void main(String[] args){
        JFrame f = new JFrame("test");
        ScalePropertyEditor editor = new ScalePropertyEditor();
        PropertyEditorUI ui = editor.new PropertyEditorUI();
        f.setContentPane(ui);
        f.setSize(100,300);
        f.setVisible(true);
    }

}

