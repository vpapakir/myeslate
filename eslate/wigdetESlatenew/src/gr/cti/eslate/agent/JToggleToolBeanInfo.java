package gr.cti.eslate.agent;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Locale;
import java.util.ResourceBundle;

public class JToggleToolBeanInfo extends SimpleBeanInfo {
    private ResourceBundle bundle;

    public JToggleToolBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.agent.JToggleToolBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            //Icon
            PropertyDescriptor pd1 = new PropertyDescriptor("icon",JToggleTool.class);
            pd1.setDisplayName(bundle.getString("icon"));
            pd1.setShortDescription(bundle.getString("iconTip"));

            //Selected Icon
            PropertyDescriptor pd2 = new PropertyDescriptor("selectedIcon",JToggleTool.class);
            pd2.setDisplayName(bundle.getString("selectedIcon"));
            pd2.setShortDescription(bundle.getString("selectedIconTip"));

            //Rollover Icon
            PropertyDescriptor pd3 = new PropertyDescriptor("rolloverIcon",JToggleTool.class);
            pd3.setDisplayName(bundle.getString("rolloverIcon"));
            pd3.setShortDescription(bundle.getString("rolloverIconTip"));

            //Pressed Icon
            PropertyDescriptor pd4 = new PropertyDescriptor("pressedIcon",JToggleTool.class);
            pd4.setDisplayName(bundle.getString("pressedIcon"));
            pd4.setShortDescription(bundle.getString("pressedIconTip"));

            //Tooltip
            PropertyDescriptor pd5 = new PropertyDescriptor("toolTipText",JToggleTool.class);
            pd5.setDisplayName(bundle.getString("toolTipText"));
            pd5.setShortDescription(bundle.getString("toolTipTextTip"));

            //Help
            PropertyDescriptor pd6 = new PropertyDescriptor("helpText",JToggleTool.class,"getHelpText","setHelpText");
            pd6.setDisplayName(bundle.getString("helpText"));
            pd6.setShortDescription(bundle.getString("helpTextTip"));

            //Opaque
            PropertyDescriptor pd7 = new PropertyDescriptor("opaque",JToggleTool.class);
            pd7.setDisplayName(bundle.getString("opaque"));
            pd7.setShortDescription(bundle.getString("opaqueTip"));

            //Visible
            PropertyDescriptor pd8 = new PropertyDescriptor("visible",JToggleTool.class);
            pd8.setDisplayName(bundle.getString("visible"));
            pd8.setShortDescription(bundle.getString("visibleTip"));

            //Border policy
            PropertyDescriptor pd9 = new PropertyDescriptor("borderPolicy",JToggleTool.class);
            pd9.setPropertyEditorClass(EditorToolBorder.class);
            pd9.setDisplayName(bundle.getString("borderPolicy"));
            pd9.setShortDescription(bundle.getString("borderPolicyTip"));

            return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9};
        } catch (IntrospectionException exc) {
            System.out.println("MAP#200004051949: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }
/*            pd1.setDisplayName(bundle.getString("StandardToolBarVisible"));
            pd1.setShortDescription(bundle.getString("StandardToolBarVisibleTip"));
            pd1.setValue("propertyCategory", bundle.getString("ToolBar"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                                            "formattingToolBarVisible",
                                            Database.class,
                                            "isFormattingToolBarVisible",
                                            "setFormattingToolBarVisible");
            pd2.setDisplayName(bundle.getString("FormattingToolBarVisible"));
            pd2.setShortDescription(bundle.getString("FormattingToolBarVisibleTip"));
            pd2.setValue("propertyCategory", bundle.getString("ToolBar"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                                            "userMode",
                                            Database.class);
            pd3.setDisplayName(bundle.getString("UserMode"));
            pd3.setShortDescription(bundle.getString("UserModeTip"));
//            pd3.setPreferred(true);
            pd3.setBound(true);
//            pd3.setCategory("My category");
            pd3.setPropertyEditorClass(TaggedUserModePropEditor.class);

            return new PropertyDescriptor[] {pd1, pd2, pd3};
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }*/
}
