/*
 * Created on 8 Ιουν 2006
 *
 */
package gr.cti.eslate.object3D;

import gr.cti.eslate.scene3d.utils.OrientationPropertyEditor;
import gr.cti.eslate.scene3d.utils.Position3DPropertyEditor;
import gr.cti.eslate.scene3d.utils.ScalePropertyEditor;
import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class Object3DBeanInfo extends ESlateBeanInfo {
    /**
     * Localized bundleMessages.
     */
    private static ResourceBundle bundleMessages;

    private Class componentClass;

    private ImageIcon color16Icon = new ImageIcon(getClass().getResource(
            "images/obj3d-16.gif"));
    private ImageIcon color32Icon = new ImageIcon(getClass().getResource(
            "images/obj3d-32.gif"));
    
    /**
     * Construct the BeanInfo.
     */
    public Object3DBeanInfo() {
        bundleMessages = ResourceBundle.getBundle(
                "gr.cti.eslate.object3D.BundleMessages", Locale.getDefault());
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
        switch (iconKind) {
        case ICON_MONO_32x32:
            return color32Icon.getImage();
        case ICON_COLOR_32x32:
            return color32Icon.getImage();
        case ICON_MONO_16x16:
            return color16Icon.getImage();
        case ICON_COLOR_16x16:
            return color16Icon.getImage();
        default:
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return null;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {

        PropertyDescriptor[] defaultProperties = super.getPropertyDescriptors();

        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Visible", Object3D.class,
                    "isVisible", "setVisible"
                );

            pd1.setDisplayName(bundleMessages.getString("Visible"));
            pd1.setShortDescription(bundleMessages.getString("setVisibleTip"));
            
            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Position", Object3D.class,
                    "getPosition", "setPosition"
                );

            pd2.setDisplayName(bundleMessages.getString("Position"));
            pd2.setShortDescription(bundleMessages.getString("PositionTip"));
            pd2.setPropertyEditorClass(Position3DPropertyEditor.class);
            
            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Orientation", Object3D.class,
                    "getOrientation", "setOrientation"
                );

            pd3.setDisplayName(bundleMessages.getString("Orientation"));
            pd3.setShortDescription(bundleMessages.getString("OrientationTip"));
            pd3.setPropertyEditorClass(OrientationPropertyEditor.class);
            
            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "Scale", Object3D.class,
                    "getScale", "setScale"
                );

            pd4.setDisplayName(bundleMessages.getString("Scale"));
            pd4.setShortDescription(bundleMessages.getString("ScaleTip"));
            pd4.setPropertyEditorClass(ScalePropertyEditor.class);
            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
