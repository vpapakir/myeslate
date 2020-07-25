package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

public class JButtonToolBeanInfo extends ESlateBeanInfo {
    private ResourceBundle bundle;

    public JButtonToolBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.JToggleToolBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            //Icon
            PropertyDescriptor pd1 = new PropertyDescriptor("icon",JButtonTool.class);
            pd1.setDisplayName(bundle.getString("icon"));
            pd1.setShortDescription(bundle.getString("iconTip"));

            //Selected Icon
            PropertyDescriptor pd2 = new PropertyDescriptor("selectedIcon",JButtonTool.class);
            pd2.setDisplayName(bundle.getString("selectedIcon"));
            pd2.setShortDescription(bundle.getString("selectedIconTip"));

            //Rollover Icon
            PropertyDescriptor pd3 = new PropertyDescriptor("rolloverIcon",JButtonTool.class);
            pd3.setDisplayName(bundle.getString("rolloverIcon"));
            pd3.setShortDescription(bundle.getString("rolloverIconTip"));

            //Pressed Icon
            PropertyDescriptor pd4 = new PropertyDescriptor("pressedIcon",JButtonTool.class);
            pd4.setDisplayName(bundle.getString("pressedIcon"));
            pd4.setShortDescription(bundle.getString("pressedIconTip"));

            //Tooltip
            PropertyDescriptor pd5 = new PropertyDescriptor("toolTipText",JButtonTool.class);
            pd5.setDisplayName(bundle.getString("toolTipText"));
            pd5.setShortDescription(bundle.getString("toolTipTextTip"));

            //Help
            PropertyDescriptor pd6 = new PropertyDescriptor("helpText",JButtonTool.class,"getHelpText","setHelpText");
            pd6.setDisplayName(bundle.getString("helpText"));
            pd6.setShortDescription(bundle.getString("helpTextTip"));

            //Opaque
            PropertyDescriptor pd7 = new PropertyDescriptor("opaque",JButtonTool.class);
            pd7.setDisplayName(bundle.getString("opaque"));
            pd7.setShortDescription(bundle.getString("opaqueTip"));

            //Visible
            PropertyDescriptor pd8 = new PropertyDescriptor("visible",JButtonTool.class);
            pd8.setDisplayName(bundle.getString("visible"));
            pd8.setShortDescription(bundle.getString("visibleTip"));

            //Border policy
            PropertyDescriptor pd9 = new PropertyDescriptor("borderPolicy",JButtonTool.class);
            pd9.setPropertyEditorClass(EditorToolBorder.class);
            pd9.setDisplayName(bundle.getString("borderPolicy"));
            pd9.setShortDescription(bundle.getString("borderPolicyTip"));

            return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9};
        } catch (IntrospectionException exc) {
            System.out.println("MAPVIEWER#200004051949: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }
}
