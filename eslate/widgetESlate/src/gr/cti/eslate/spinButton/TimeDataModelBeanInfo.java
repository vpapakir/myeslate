package gr.cti.eslate.spinButton;


import java.beans.*;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.utils.ESlateBeanInfo;


public class TimeDataModelBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    //   ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JButtonColor16p.gif"));
    /**
     * Construct the BeanInfo.
     */
    public TimeDataModelBeanInfo() {
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
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Format", TimeDataModel.class,
                    "getFormat", "setFormat"
                );

            pd1.setDisplayName(bundleMessages.getString("Format"));
            pd1.setPropertyEditorClass(TimeFormatPropertyEditor.class);
            pd1.setShortDescription(bundleMessages.getString("setFormatTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "TimeRate", TimeDataModel.class,
                    "getTimeRate", "setTimeRate"
                );

            pd2.setDisplayName(bundleMessages.getString("TimeRate"));
            pd2.setPropertyEditorClass(TimeRatePropertyEditor.class);
            pd2.setShortDescription(bundleMessages.getString("setTimeRateTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "MaximumValue", TimeDataModel.class,
                    "getMaximumValue", "setMaximumValue"
                );

            pd3.setDisplayName(bundleMessages.getString("MaximumTime"));
            pd3.setPropertyEditorClass(MaximumTimePropertyEditor.class);
            pd3.setShortDescription(bundleMessages.getString("setMaximumTimeTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "MinimumValue", TimeDataModel.class,
                    "getMinimumValue", "setMinimumValue"
                );

            pd4.setDisplayName(bundleMessages.getString("MinimumTime"));
            pd4.setPropertyEditorClass(MinimumTimePropertyEditor.class);
            pd4.setShortDescription(bundleMessages.getString("setMinimumTimeTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
