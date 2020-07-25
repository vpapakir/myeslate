package gr.cti.eslate.eslateList;


import java.beans.*;
import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.ESlateBeanInfo;
import java.util.*;
import java.lang.reflect.*;
import java.awt.event.*;


public class ESlateListBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JListColor16.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateListBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateList.ListBundle", Locale.getDefault());
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

        /*    try {
         Method valueChangedMethod = ListSelectionListener.class.getMethod(
         "valueChanged", new Class[] {ListSelectionEvent.class}
         );
         Method addListenerMethod = componentClass.getMethod(
         "addListSelectionListener", new Class[] {ListSelectionListener.class}
         );
         Method removelistenerMethod = componentClass.getMethod(
         "removeListSelectionListener",
         new Class[] {ListSelectionListener.class}
         );
         MethodDescriptor md = new MethodDescriptor(valueChangedMethod);
         md.setDisplayName(bundleMessages.getString("ValueChanged"));

         esd = new EventSetDescriptor(
         "valueChanged", ListSelectionListener.class, new MethodDescriptor[] {md},
         addListenerMethod, removelistenerMethod
         );
         descriptors.addElement(esd);

         } catch (IntrospectionException exc) {
         } catch (NoSuchMethodException exc) {
         } catch (Exception exc) {
         exc.printStackTrace();
         }
         */
        try {
            Method selectionChangedMethod = SelectionChangedListener.class.getMethod(
                    "selectionChanged", new Class[] {SelectionChangedEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addSelectionChangedListener", new Class[] {SelectionChangedListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeSelectionChangedListener",
                    new Class[] {SelectionChangedListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(selectionChangedMethod);

            md.setDisplayName(bundleMessages.getString("SelectionChanged"));

            esd = new EventSetDescriptor(
                        "selectionChanged", SelectionChangedListener.class, new MethodDescriptor[] {md},
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
                    "Model", ESlateList.class,
                    "getModel", "setModel"
                );

            pd0.setDisplayName(bundleMessages.getString("Model"));
            pd0.setPropertyEditorClass(ListPropertyEditor.class);
            pd0.setShortDescription(bundleMessages.getString("setModelTip"));

            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Background", ESlateList.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateList.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateList.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "FixedCellWidth", ESlateList.class,
                    "getFixedCellWidth", "setFixedCellWidth"
                );

            pd4.setDisplayName(bundleMessages.getString("FixedCellWidth"));
            pd4.setShortDescription(bundleMessages.getString("setFixedCellWidthTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "FixedCellHeight", ESlateList.class,
                    "getFixedCellHeight", "setFixedCellHeight"
                );

            pd5.setDisplayName(bundleMessages.getString("FixedCellHeight"));
            pd5.setShortDescription(bundleMessages.getString("setFixedCellHeightTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "Foreground", ESlateList.class,
                    "getForeground", "setForeground"
                );

            pd6.setDisplayName(bundleMessages.getString("Foreground"));
            pd6.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "ToolTipText", ESlateList.class,
                    "getToolTipText", "setToolTipText"
                );

            pd7.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd7.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "MaximumSize", ESlateList.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd8.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd8.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "MinimumSize", ESlateList.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd9.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd9.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "SelectedIndex", ESlateList.class,
                    "getSelectedIndex", "setSelectedIndex"
                );

            pd10.setDisplayName(bundleMessages.getString("SelectedIndex"));
            pd10.setShortDescription(bundleMessages.getString("setSelectedIndexTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "AlignmentX", ESlateList.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd11.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd11.setShortDescription(bundleMessages.getString("setAlignmentXTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "AlignmentY", ESlateList.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd12.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd12.setShortDescription(bundleMessages.getString("setAlignmentYTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "Opaque", ESlateList.class,
                    "isOpaque", "setOpaque"
                );

            pd13.setDisplayName(bundleMessages.getString("Opaque"));
            pd13.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "Name", ESlateList.class,
                    "getName", "setName"
                );

            pd14.setDisplayName(bundleMessages.getString("Name"));
            pd14.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "SelectionBackground", ESlateList.class,
                    "getSelectionBackground", "setSelectionBackground"
                );

            pd15.setDisplayName(bundleMessages.getString("SelectionBackground"));
            pd15.setShortDescription(bundleMessages.getString("setSelectionBackgroundTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "SelectionForeground", ESlateList.class,
                    "getSelectionForeground", "setSelectionForeground"
                );

            pd16.setDisplayName(bundleMessages.getString("SelectionForeground"));
            pd16.setShortDescription(bundleMessages.getString("setSelectionForegroundTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "DoubleBuffered", ESlateList.class,
                    "isDoubleBuffered", "setDoubleBuffered"
                );

            pd17.setDisplayName(bundleMessages.getString("DoubleBuffered"));
            pd17.setExpert(true);
            pd17.setShortDescription(bundleMessages.getString("setDoubleBufferedTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "SelectionMode", ESlateList.class,
                    "getSelectionMode", "setSelectionMode"
                );

            pd18.setPropertyEditorClass(SelectionModePropertyEditor.class);
            pd18.setDisplayName(bundleMessages.getString("SelectionMode"));
            pd18.setShortDescription(bundleMessages.getString("setSelectionModeTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "ValueIsAdjusting", ESlateList.class,
                    "getValueIsAdjusting", "setValueIsAdjusting"
                );

            pd19.setDisplayName(bundleMessages.getString("ValueIsAdjusting"));
            pd19.setShortDescription(bundleMessages.getString("setValueIsAdjustingTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "VisibleRowCount", ESlateList.class,
                    "getVisibleRowCount", "setVisibleRowCount"
                );

            pd20.setDisplayName(bundleMessages.getString("VisibleRowCount"));
            pd20.setShortDescription(bundleMessages.getString("setVisibleRowCountTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "PreferredSize", ESlateList.class,
                    "getPreferredSize", "setPreferredSize"
                );

            pd21.setDisplayName(bundleMessages.getString("PreferredSize"));
            pd21.setShortDescription(bundleMessages.getString("setPreferredSizeTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "Enabled", ESlateList.class,
                    "isEnabled", "setEnabled"
                );

            pd22.setDisplayName(bundleMessages.getString("Enabled"));
            pd22.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd23 = new PropertyDescriptor(
                    "PlugsUsed", ESlateList.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd23.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd23.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            PropertyDescriptor pd24 = new PropertyDescriptor(
                    "RequestFocusEnabled", ESlateList.class,
                    "isRequestFocusEnabled", "setRequestFocusEnabled"
                );

            pd24.setDisplayName(bundleMessages.getString("RequestFocusEnabled"));
            pd24.setShortDescription(bundleMessages.getString("setRequestFocusEnabledTip"));
            pd24.setExpert(true);

            return new PropertyDescriptor[] {pd0, pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15, pd16, pd17, pd18, pd19, pd20, pd21, pd22, pd23, pd24};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
