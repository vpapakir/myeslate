package gr.cti.eslate.spinButton;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;


public class SpinButtonBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/spin.gif"));

    /**
     * Construct the BeanInfo.
     */
    public SpinButtonBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class myClass = getClass();
        String className = myClass.getName();

        if (className.endsWith(BeanInfo)) {
            className = className.substring(0, className.length() - BeanInfo.length());
            try {
                this.componentClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                // This won't work, but then again, this shouldn't happen.
                this.componentClass = myClass;
            }
        } else {
            // This won't work, but then again, this shouldn't happen.
            this.componentClass = myClass;
        }
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return color16Icon.getImage();
        return null;
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        Object array1[] = super.getEventSetDescriptors();
        Vector descriptors = new Vector();
        EventSetDescriptor esd = null;
        try {
            Method mousePressedMethod = MouseListener.class.getMethod(
                    "mousePressed", new Class[] {MouseEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addMouseListener", new Class[] {MouseListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeMouseListener",
                    new Class[] {MouseListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(mousePressedMethod);

            md.setDisplayName(bundleMessages.getString("MousePressed"));

            esd = new EventSetDescriptor(
                        "mousePressed", MouseListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method mouseReleasedMethod = MouseListener.class.getMethod(
                    "mouseReleased", new Class[] {MouseEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addMouseListener", new Class[] {MouseListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeMouseListener",
                    new Class[] {MouseListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(mouseReleasedMethod);

            md.setDisplayName(bundleMessages.getString("MouseReleased"));

            esd = new EventSetDescriptor(
                        "mouseReleased", MouseListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }
        

        try {
            Method valueChangedMethod = ValueChangedListener.class.getMethod(
                    "valueChanged", new Class[] { ValueChangedEvent.class });
            Method addListenerMethod = componentClass.getMethod(
                    "addValueChangedListener",
                    new Class[] { ValueChangedListener.class });
            Method removelistenerMethod = componentClass.getMethod(
                    "removeValueChangedListener",
                    new Class[] { ValueChangedListener.class });
            MethodDescriptor md = new MethodDescriptor(valueChangedMethod);
            md.setDisplayName(bundleMessages.getString("ValueChanged"));

            esd = new EventSetDescriptor("valueChanged",
                    ValueChangedListener.class, new MethodDescriptor[] { md },
                    addListenerMethod, removelistenerMethod);
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {
        } catch (NoSuchMethodException exc) {
        } catch (Exception exc) {
            exc.printStackTrace();
        }
         

        EventSetDescriptor[] d = new EventSetDescriptor[array1.length + descriptors.size()];

        System.arraycopy(array1, 0, d, 0, array1.length);

        for (int i = 0; i < descriptors.size(); i++)
            d[array1.length + i] = (EventSetDescriptor) descriptors.get(i);

        return d;

    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {

            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "modelType", SpinButton.class,
                    "getModelType", "setModelType"
                );

            pd1.setDisplayName(bundleMessages.getString("ModelType"));
            pd1.setPropertyEditorClass(DataModelTypePropertyEditor.class);
            pd1.setShortDescription(bundleMessages.getString("setModelTypeTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Step", SpinButton.class,
                    "getStep", "setStep"
                );

            pd2.setDisplayName(bundleMessages.getString("Step"));
            pd2.setShortDescription(bundleMessages.getString("setStepTip"));

            Method getter = SpinButton.class.getMethod("getModel", new Class[] {}
                );
            PropertyDescriptor pd4 = new PropertyDescriptor("model", getter, null);

            pd4.setDisplayName(bundleMessages.getString("model"));

            return new PropertyDescriptor[] {pd1, pd2, pd4};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
