package gr.cti.eslate.eslateComboBox;


import java.beans.*;
import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.ESlateBeanInfo;
import java.util.*;
import java.lang.reflect.*;
import java.awt.event.*;


public class ESlateComboBoxBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JComboBoxColor16p.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateComboBoxBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateComboBox.ComboBoxBundle", Locale.getDefault());
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
            Method actionPerformedMethod = ActionListener.class.getMethod(
                    "actionPerformed", new Class[] {ActionEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addActionListener", new Class[] {ActionListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeActionListener",
                    new Class[] {ActionListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(actionPerformedMethod);

            md.setDisplayName(bundleMessages.getString("ActionPerformed"));

            esd = new EventSetDescriptor(
                        "actionPerformed", ActionListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );

            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

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
            Method focusGainedMethod = FocusListener.class.getMethod(
                    "focusGained", new Class[] {FocusEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addFocusListener", new Class[] {FocusListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeFocusListener",
                    new Class[] {FocusListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(focusGainedMethod);

            md.setDisplayName(bundleMessages.getString("FocusGained"));

            esd = new EventSetDescriptor(
                        "focusGained", FocusListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method focusLostMethod = FocusListener.class.getMethod(
                    "focusLost", new Class[] {FocusEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addFocusListener", new Class[] {FocusListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeFocusListener",
                    new Class[] {FocusListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(focusLostMethod);

            md.setDisplayName(bundleMessages.getString("FocusLost"));

            esd = new EventSetDescriptor(
                        "focusLost", FocusListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method itemStateChangedMethod = ItemListener.class.getMethod(
                    "itemStateChanged", new Class[] {ItemEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addItemListener", new Class[] {ItemListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeItemListener",
                    new Class[] {ItemListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(itemStateChangedMethod);

            md.setDisplayName(bundleMessages.getString("ItemStateChanged"));

            esd = new EventSetDescriptor(
                        "itemStateChanged", ItemListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        /*    try {
         Method selectedItemChangedMethod = ItemListener.class.getMethod(
         "selectedItemChanged", new Class[] {ItemEvent.class}
         );
         Method addListenerMethod = componentClass.getMethod(
         "addItemListener", new Class[] {ItemListener.class}
         );
         Method removelistenerMethod = componentClass.getMethod(
         "removeItemListener",
         new Class[] {ItemListener.class}
         );
         MethodDescriptor md = new MethodDescriptor(selectedItemChangedMethod);
         md.setDisplayName(bundleMessages.getString("SelectedItemChanged"));

         esd = new EventSetDescriptor(
         "selectedItemChanged", ItemListener.class, new MethodDescriptor[] {md},
         addListenerMethod, removelistenerMethod
         );
         descriptors.addElement(esd);

         } catch (IntrospectionException exc) {
         } catch (NoSuchMethodException exc) {
         } catch (Exception exc) {
         exc.printStackTrace();
         }
         */

        EventSetDescriptor[] d = new EventSetDescriptor[array1.length + descriptors.size()];

        System.arraycopy(array1, 0, d, 0, array1.length);

        for (int i = 0; i < descriptors.size(); i++)
            d[array1.length + i] = (EventSetDescriptor) descriptors.get(i);
        return d;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pd0 = new PropertyDescriptor(
                    "Model", ESlateComboBox.class,
                    "getModel", "setModel"
                );

            pd0.setDisplayName(bundleMessages.getString("Model"));
            pd0.setPropertyEditorClass(ComboBoxPropertyEditor.class);
            pd0.setShortDescription(bundleMessages.getString("setModelTip"));

            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Background", ESlateComboBox.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateComboBox.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateComboBox.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "PopupVisible", ESlateComboBox.class,
                    "isPopupVisible", "setPopupVisible"
                );

            pd4.setDisplayName(bundleMessages.getString("PopupVisible"));
            pd4.setShortDescription(bundleMessages.getString("setPopupVisibleTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "Editable", ESlateComboBox.class,
                    "isEditable", "setEditable"
                );

            pd5.setDisplayName(bundleMessages.getString("Editable"));
            pd5.setShortDescription(bundleMessages.getString("setEditableTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "Foreground", ESlateComboBox.class,
                    "getForeground", "setForeground"
                );

            pd6.setDisplayName(bundleMessages.getString("Foreground"));
            pd6.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "ToolTipText", ESlateComboBox.class,
                    "getToolTipText", "setToolTipText"
                );

            pd7.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd7.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "MaximumSize", ESlateComboBox.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd8.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd8.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "MinimumSize", ESlateComboBox.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd9.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd9.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "SelectedIndex", ESlateComboBox.class,
                    "getSelectedIndex", "setSelectedIndex"
                );

            pd10.setDisplayName(bundleMessages.getString("SelectedIndex"));
            pd10.setShortDescription(bundleMessages.getString("setSelectedIndexTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "AlignmentX", ESlateComboBox.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd11.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd11.setShortDescription(bundleMessages.getString("setAlignmentXTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "AlignmentY", ESlateComboBox.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd12.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd12.setShortDescription(bundleMessages.getString("setAlignmentYTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "Opaque", ESlateComboBox.class,
                    "isOpaque", "setOpaque"
                );

            pd13.setDisplayName(bundleMessages.getString("Opaque"));
            pd13.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "Name", ESlateComboBox.class,
                    "getName", "setName"
                );

            pd14.setDisplayName(bundleMessages.getString("Name"));
            pd14.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "MaximumRowCount", ESlateComboBox.class,
                    "getMaximumRowCount", "setMaximumRowCount"
                );

            pd15.setDisplayName(bundleMessages.getString("MaximumRowCount"));
            pd15.setShortDescription(bundleMessages.getString("setMaximumRowCountTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "DoubleBuffered", ESlateComboBox.class,
                    "isDoubleBuffered", "setDoubleBuffered"
                );

            pd16.setDisplayName(bundleMessages.getString("DoubleBuffered"));
            pd16.setExpert(true);
            pd16.setShortDescription(bundleMessages.getString("setDoubleBufferedTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "Enabled", ESlateComboBox.class,
                    "isEnabled", "setEnabled"
                );

            pd17.setDisplayName(bundleMessages.getString("Enabled"));
            pd17.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "PlugsUsed", ESlateComboBox.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd18.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd18.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            return new PropertyDescriptor[] {pd0, pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15, pd16, pd17, pd18};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
