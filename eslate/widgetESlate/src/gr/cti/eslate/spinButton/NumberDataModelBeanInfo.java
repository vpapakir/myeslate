package gr.cti.eslate.spinButton;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;


public class NumberDataModelBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    //   ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JButtonColor16p.gif"));
    /**
     * Construct the BeanInfo.
     */
    public NumberDataModelBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class myClass = getClass();
        String className = myClass.getName();

        if (className.endsWith(BeanInfo)) {
            className = className.substring(0, className.length() - BeanInfo.length());
        }

    }

    /*   public Image getIcon(int iconKind) {
     if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
     return color16Icon.getImage();
     return null;
     } */

    public EventSetDescriptor[] getEventSetDescriptors() {
        return null;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "MaximumValue", NumberDataModel.class,
                    "getMaximumValue", "setMaximumValue"
                );

            pd3.setDisplayName(bundleMessages.getString("MaximumNumber"));
            pd3.setPropertyEditorClass(MaximumNumberPropertyEditor.class);
            pd3.setShortDescription(bundleMessages.getString("setMaximumNumberTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "MinimumValue", NumberDataModel.class,
                    "getMinimumValue", "setMinimumValue"
                );

            pd4.setDisplayName(bundleMessages.getString("MinimumNumber"));
            pd4.setPropertyEditorClass(MinimumNumberPropertyEditor.class);
            pd4.setShortDescription(bundleMessages.getString("setMinimumNumberTip"));

            return new PropertyDescriptor[] {pd3, pd4};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
