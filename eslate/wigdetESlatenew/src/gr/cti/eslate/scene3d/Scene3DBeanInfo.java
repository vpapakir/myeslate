/*
 * Created on 8 Ιουν 2006
 *
 */
package gr.cti.eslate.scene3d;

import java.beans.*;
import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

import gr.cti.eslate.scene3d.utils.BoundingBoxPropertyEditor;
import gr.cti.eslate.scene3d.utils.OrientationPropertyEditor;
import gr.cti.eslate.scene3d.utils.Position3DPropertyEditor;
import gr.cti.eslate.scene3d.viewer.event.CameraEvent;
import gr.cti.eslate.scene3d.viewer.event.CameraListener;
import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.*;
import java.util.*;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Scene3DBeanInfo extends ESlateBeanInfo {
    /**
     * Localized bundleMessages.
     */
    private static ResourceBundle bundleMessages;

    private Class componentClass;

    private ImageIcon color16Icon = new ImageIcon(getClass().getResource(
            "images/icon1-16.gif"));

    /**
     * Construct the BeanInfo.
     */
    public Scene3DBeanInfo() {
        bundleMessages = ResourceBundle.getBundle(
                "gr.cti.eslate.scene3d.BundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class myClass = getClass();
        String className = myClass.getName();

        if (className.endsWith(BeanInfo)) {
            className = className.substring(0, className.length()
                    -BeanInfo.length());
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
        if (iconKind==BeanInfo.ICON_MONO_16x16
                ||iconKind==BeanInfo.ICON_COLOR_16x16){
            return color16Icon.getImage();
        }
        return null;
    }

    public EventSetDescriptor[] getEventSetDescriptors() {

        Object array1[] = super.getEventSetDescriptors();
        Vector<EventSetDescriptor> descriptors = new Vector<EventSetDescriptor>();
        EventSetDescriptor esd = null;

        try {
            Method positionChangedMethod = CameraListener.class.getMethod(
                    "positionChanged", new Class[] {CameraEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addCameraListener", new Class[] {CameraListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeCameraListener", new Class[] {CameraListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(positionChangedMethod);

            md.setDisplayName(bundleMessages.getString("PositionChanged"));

            esd = new EventSetDescriptor(
                        "positionChanged", CameraListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod);

            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method orientationChangedMethod = CameraListener.class.getMethod(
                    "orientationChanged", new Class[] {CameraEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addCameraListener", new Class[] {CameraListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeCameraListener", new Class[] {CameraListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(orientationChangedMethod);

            md.setDisplayName(bundleMessages.getString("OrientationChanged"));

            esd = new EventSetDescriptor(
                        "orientationChanged", CameraListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod);

            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        EventSetDescriptor[] d = new EventSetDescriptor[array1.length + descriptors.size()];

        System.arraycopy(array1, 0, d, 0, array1.length);

        for (int i = 0; i < descriptors.size(); i++)
            d[array1.length + i] = descriptors.get(i);

        return d;

    }

    public PropertyDescriptor[] getPropertyDescriptors() {

        PropertyDescriptor[] defaultProperties = super.getPropertyDescriptors();

        try {
            
             PropertyDescriptor pd1 = new PropertyDescriptor(
             "CartesianHelperVisible", Scene3D.class,
             "isCartesianHelperVisible", "setCartesianHelperVisible"
             );

             pd1.setDisplayName(bundleMessages.getString("CartesianHelperVisible"));
             pd1.setShortDescription(bundleMessages.getString("CartesianHelperVisibleTip"));
             
             PropertyDescriptor pd2 = new PropertyDescriptor(
                     "CameraPosition", Scene3D.class,
                     "getCameraPosition", "setCameraPosition"
                     );

                     pd2.setDisplayName(bundleMessages.getString("CameraPosition"));
                     pd2.setShortDescription(bundleMessages.getString("CameraPositionTip"));
                     pd2.setPropertyEditorClass(Position3DPropertyEditor.class);
             
             PropertyDescriptor pd3 = new PropertyDescriptor(
                     "CameraOrientation", Scene3D.class,
                     "getCameraOrientation", "setCameraOrientation"
                     );

                     pd3.setDisplayName(bundleMessages.getString("CameraOrientation"));
                     pd3.setShortDescription(bundleMessages.getString("CameraOrientationTip"));
                     pd3.setPropertyEditorClass(OrientationPropertyEditor.class);    
                     
             PropertyDescriptor pd4 = new PropertyDescriptor(
                     "BoundingBoxDimensions", Scene3D.class,
                     "getBoundingBoxDimensions", "setBoundingBoxDimensions"
                     );

                     pd4.setDisplayName(bundleMessages.getString("BoundingBoxDimensions"));
                     pd4.setShortDescription(bundleMessages.getString("BoundingBoxDimensionsTip"));
                     pd4.setPropertyEditorClass(BoundingBoxPropertyEditor.class);  
                     
            return new PropertyDescriptor[] {pd1,pd2,pd3,pd4};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

