package gr.cti.eslate.spinButton;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;


public class DateDataModelBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;

    //   ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JButtonColor16p.gif"));
    /**
     * Construct the BeanInfo.
     */
    public DateDataModelBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class myClass = getClass();
        String className = myClass.getName();

        if (className.endsWith(BeanInfo)) {
            className = className.substring(0, className.length() - BeanInfo.length());
        }

    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return null;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Format", DateDataModel.class,
                    "getFormat", "setFormat"
                );

            pd1.setDisplayName(bundleMessages.getString("DateFormat"));
            pd1.setPropertyEditorClass(DateFormatPropertyEditor.class);
            pd1.setShortDescription(bundleMessages.getString("setDateFormatTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "DateRate", DateDataModel.class,
                    "getDateRate", "setDateRate"
                );

            pd2.setDisplayName(bundleMessages.getString("DateRate"));
            pd2.setPropertyEditorClass(DateRatePropertyEditor.class);
            pd2.setShortDescription(bundleMessages.getString("setDateRateTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "MaximumValue", DateDataModel.class,
                    "getMaximumValue", "setMaximumValue"
                );

            pd3.setDisplayName(bundleMessages.getString("MaximumDate"));
            pd3.setPropertyEditorClass(MaximumDatePropertyEditor.class);
            pd3.setShortDescription(bundleMessages.getString("setMaximumDateTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "MinimumValue", DateDataModel.class,
                    "getMinimumValue", "setMinimumValue"
                );

            pd4.setDisplayName(bundleMessages.getString("MinimumDate"));
            pd4.setPropertyEditorClass(MinimumDatePropertyEditor.class);
            pd4.setShortDescription(bundleMessages.getString("setMinimumDateTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
