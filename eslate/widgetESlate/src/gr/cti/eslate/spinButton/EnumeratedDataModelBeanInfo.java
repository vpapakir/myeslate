package gr.cti.eslate.spinButton;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;


public class EnumeratedDataModelBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    //   ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JButtonColor16p.gif"));
    /**
     * Construct the BeanInfo.
     */
    public EnumeratedDataModelBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        //      System.out.println("constructing so far bean info");
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
                    "Elements", EnumeratedDataModel.class,
                    "getElements", "setElements"
                );

            pd1.setDisplayName(bundleMessages.getString("DefineElements"));
            pd1.setPropertyEditorClass(DiscreteValuesPropertyEditor.class);
            pd1.setShortDescription(bundleMessages.getString("setDefineElementsTip"));

            return new PropertyDescriptor[] {pd1};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
