package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
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


public class ESlateMenuBarBeanInfo extends ESlateBeanInfo {
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JMenuBarColor16n.gif"));
    private ResourceBundle bundleMessages;

    /**
     * Construct the BeanInfo.
     */
    public ESlateMenuBarBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateMenuBar.BundleMessages", Locale.getDefault());

        /*      String BeanInfo = "BeanInfo";

         Class myClass = getClass();
         System.out.println("HERE3");
         String className = myClass.getName();
         System.out.println("Classname : "+className);
         if (className.endsWith(BeanInfo)) {
         className = className.substring(0, className.length()-BeanInfo.length());
         try {
         this.componentClass = Class.forName(className);
         } catch (ClassNotFoundException e) {
         // This won't work, but then again, this shouldn't happen.
         this.componentClass = myClass;
         }
         }else{
         // This won't work, but then again, this shouldn't happen.
         this.componentClass = myClass;
         }
         */
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
            Method pathChangedMethod = PathChangedListener.class.getMethod(
                    "pathChanged", new Class[] {PathChangedEvent.class}
                );
            Method addListenerMethod = ESlateMenuBar.class.getMethod(
                    "addPathChangedListener", new Class[] {PathChangedListener.class}
                );
            Method removelistenerMethod = ESlateMenuBar.class.getMethod(
                    "removePathChangedListener",
                    new Class[] {PathChangedListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(pathChangedMethod);

            md.setDisplayName(bundleMessages.getString("PathChanged"));

            esd = new EventSetDescriptor(
                        "pathChanged", PathChangedListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );

            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
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
            PropertyDescriptor pd0 = new PropertyDescriptor(
                    "Menus", ESlateMenuBar.class,
                    "getMenuStructure", "setMenuStructure"
                );

            pd0.setDisplayName(bundleMessages.getString("Menus"));
            pd0.setPropertyEditorClass(MenuBarPropertyEditor.class);
            pd0.setShortDescription(bundleMessages.getString("setMenusTip"));

            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Background", ESlateMenuBar.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateMenuBar.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateMenuBar.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "BorderPainted", ESlateMenuBar.class,
                    "isBorderPainted", "setBorderPainted"
                );

            pd4.setDisplayName(bundleMessages.getString("BorderPainted"));
            pd4.setShortDescription(bundleMessages.getString("setBorderPaintedTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "Foreground", ESlateMenuBar.class,
                    "getForeground", "setForeground"
                );

            pd5.setDisplayName(bundleMessages.getString("Foreground"));
            pd5.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "Name", ESlateMenuBar.class,
                    "getName", "setName"
                );

            pd6.setDisplayName(bundleMessages.getString("Name"));
            pd6.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "ToolTipText", ESlateMenuBar.class,
                    "getToolTipText", "setToolTipText"
                );

            pd7.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd7.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "AlignmentX", ESlateMenuBar.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd8.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd8.setShortDescription(bundleMessages.getString("setAlignmentXTip"));
            //pd8.setExpert(true);

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "AlignmentY", ESlateMenuBar.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd9.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd9.setShortDescription(bundleMessages.getString("setAlignmentYTip"));
            //pd9.setExpert(true);

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "Layout", ESlateMenuBar.class,
                    "getLayout", "setLayout"
                );

            pd10.setDisplayName(bundleMessages.getString("Layout"));
            pd10.setShortDescription(bundleMessages.getString("setLayoutTip"));
            //pd10.setExpert(true);

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "Margin", ESlateMenuBar.class,
                    "getMargin", "setMargin"
                );

            pd11.setDisplayName(bundleMessages.getString("Margin"));
            pd11.setShortDescription(bundleMessages.getString("setMarginTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "Opaque", ESlateMenuBar.class,
                    "isOpaque", "setOpaque"
                );

            pd12.setDisplayName(bundleMessages.getString("Opaque"));
            pd12.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "Enabled", ESlateMenuBar.class,
                    "isEnabled", "setEnabled"
                );

            pd13.setDisplayName(bundleMessages.getString("Enabled"));
            pd13.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "MaximumSize", ESlateMenuBar.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd14.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd14.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "MinimumSize", ESlateMenuBar.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd15.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd15.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "PreferredSize", ESlateMenuBar.class,
                    "getPreferredSize", "setPreferredSize"
                );

            pd16.setDisplayName(bundleMessages.getString("PreferredSize"));
            pd16.setShortDescription(bundleMessages.getString("setPreferredSizeTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "Location", ESlateMenuBar.class,
                    "getLocation", "setLocation"
                );

            pd17.setDisplayName(bundleMessages.getString("Location"));
            pd17.setShortDescription(bundleMessages.getString("setLocationTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "Autoscrolls", ESlateMenuBar.class,
                    "getAutoscrolls", "setAutoscrolls"
                );

            pd18.setDisplayName(bundleMessages.getString("Autoscrolls"));
            pd18.setShortDescription(bundleMessages.getString("setAutoscrollsTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "Visible", ESlateMenuBar.class,
                    "isVisible", "setVisible"
                );

            pd19.setDisplayName(bundleMessages.getString("Visible"));
            pd19.setShortDescription(bundleMessages.getString("setVisibleTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "RequestFocusEnabled", ESlateMenuBar.class,
                    "isRequestFocusEnabled", "setRequestFocusEnabled"
                );

            pd20.setDisplayName(bundleMessages.getString("RequestFocusEnabled"));
            pd20.setShortDescription(bundleMessages.getString("setRequestFocusEnabledTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "MenuScrollSpeed", ESlateMenuBar.class,
                    "getScrollSpeed", "setScrollSpeed"
                );

            pd21.setDisplayName(bundleMessages.getString("MenuScrollSpeed"));
            pd21.setShortDescription(bundleMessages.getString("setMenuScrollSpeedTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "itemMultipleEventProductionCapability", ESlateMenuBar.class,
                    "getItemCapableOfMultiplePathChangedEvents", "setItemCapableOfMultiplePathChangedEvents"
                );

            pd22.setDisplayName(bundleMessages.getString("menuItemCanSendEventAgain"));
            pd22.setShortDescription(bundleMessages.getString("menuItemCanSendEventAgainTip"));
            pd22.setExpert(true);
            return new PropertyDescriptor[] {pd0, pd1, pd2,/* pd3, */pd4, pd5, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15, pd16, pd17, pd18, pd19, pd20, pd21, pd22};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
