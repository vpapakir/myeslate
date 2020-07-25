package gr.cti.eslate.eslateRadioButton;


import java.beans.*;
import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.ESlateBeanInfo;

import java.util.*;
import java.lang.reflect.*;
import java.awt.event.*;
import javax.swing.event.*;


public class ESlateRadioButtonBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JRadioButtonColor16p.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateRadioButtonBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateRadioButton.BundleMessages", Locale.getDefault());
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
            Method stateChangedMethod = ChangeListener.class.getMethod(
                    "stateChanged", new Class[] {ChangeEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addChangeListener", new Class[] {ChangeListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeChangeListener",
                    new Class[] {ChangeListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(stateChangedMethod);

            md.setDisplayName(bundleMessages.getString("stateChanged"));

            esd = new EventSetDescriptor(
                        "stateChanged", ChangeListener.class, new MethodDescriptor[] {md},
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

            md.setDisplayName(bundleMessages.getString("itemStateChanged"));

            esd = new EventSetDescriptor(
                        "itemStateChanged", ItemListener.class, new MethodDescriptor[] {md},
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
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Background", ESlateRadioButton.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateRadioButton.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateRadioButton.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "BorderPainted", ESlateRadioButton.class,
                    "isBorderPainted", "setBorderPainted"
                );

            pd4.setDisplayName(bundleMessages.getString("BorderPainted"));
            pd4.setShortDescription(bundleMessages.getString("setBorderPaintedTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "Icon", ESlateRadioButton.class,
                    "getIcon", "setIcon"
                );

            pd5.setDisplayName(bundleMessages.getString("Icon"));
            pd5.setShortDescription(bundleMessages.getString("setIconTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "SelectedIcon", ESlateRadioButton.class,
                    "getSelectedIcon", "setSelectedIcon"
                );

            pd6.setDisplayName(bundleMessages.getString("SelectedIcon"));
            pd6.setShortDescription(bundleMessages.getString("setSelectedIconTip"));
            pd6.setExpert(true);

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "RolloverIcon", ESlateRadioButton.class,
                    "getRolloverIcon", "setRolloverIcon"
                );

            pd7.setDisplayName(bundleMessages.getString("RolloverIcon"));
            pd7.setShortDescription(bundleMessages.getString("setRolloverIconTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "RolloverSelectedIcon", ESlateRadioButton.class,
                    "getRolloverSelectedIcon", "setRolloverSelectedIcon"
                );

            pd8.setDisplayName(bundleMessages.getString("RolloverSelectedIcon"));
            pd8.setShortDescription(bundleMessages.getString("setRolloverSelectedIconTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "RolloverEnabled", ESlateRadioButton.class,
                    "isRolloverEnabled", "setRolloverEnabled"
                );

            pd9.setDisplayName(bundleMessages.getString("RolloverEnabled"));
            pd9.setShortDescription(bundleMessages.getString("setRolloverEnabledTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "Foreground", ESlateRadioButton.class,
                    "getForeground", "setForeground"
                );

            pd10.setDisplayName(bundleMessages.getString("Foreground"));
            pd10.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "Text", ESlateRadioButton.class,
                    "getText", "setText"
                );

            pd11.setDisplayName(bundleMessages.getString("Text"));
            pd11.setShortDescription(bundleMessages.getString("setTextTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "ToolTipText", ESlateRadioButton.class,
                    "getToolTipText", "setToolTipText"
                );

            pd12.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd12.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "AlignmentX", ESlateRadioButton.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd13.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd13.setShortDescription(bundleMessages.getString("setAlignmentXTip"));
            pd13.setExpert(true);

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "AlignmentY", ESlateRadioButton.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd14.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd14.setShortDescription(bundleMessages.getString("setAlignmentYTip"));
            pd14.setExpert(true);

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "DisabledIcon", ESlateRadioButton.class,
                    "getDisabledIcon", "setDisabledIcon"
                );

            pd15.setDisplayName(bundleMessages.getString("DisabledIcon"));
            pd15.setShortDescription(bundleMessages.getString("setDisabledIconTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "DisabledSelectedIcon", ESlateRadioButton.class,
                    "getDisabledSelectedIcon", "setDisabledSelectedIcon"
                );

            pd16.setDisplayName(bundleMessages.getString("DisabledSelectedIcon"));
            pd16.setShortDescription(bundleMessages.getString("setDisabledSelectedIconTip"));
            pd16.setExpert(true);

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "FocusPainted", ESlateRadioButton.class,
                    "isFocusPainted", "setFocusPainted"
                );

            pd17.setDisplayName(bundleMessages.getString("FocusPainted"));
            pd17.setShortDescription(bundleMessages.getString("setFocusPaintedTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "HorizontalAlignment", ESlateRadioButton.class,
                    "getHorizontalAlignment", "setHorizontalAlignment"
                );

            pd18.setPropertyEditorClass(EditorHorizontalAlignment.class);
            pd18.setDisplayName(bundleMessages.getString("HorizontalAlignment"));
            pd18.setShortDescription(bundleMessages.getString("setHorizontalAlignmentTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "VerticalAlignment", ESlateRadioButton.class,
                    "getVerticalAlignment", "setVerticalAlignment"
                );

            pd19.setPropertyEditorClass(EditorVerticalAlignment.class);
            pd19.setDisplayName(bundleMessages.getString("VerticalAlignment"));
            pd19.setShortDescription(bundleMessages.getString("setVerticalAlignmentTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "VerticalTextPosition", ESlateRadioButton.class,
                    "getVerticalTextPosition", "setVerticalTextPosition"
                );

            pd20.setPropertyEditorClass(EditorVerticalAlignment.class);
            pd20.setDisplayName(bundleMessages.getString("VerticalTextPosition"));
            pd20.setShortDescription(bundleMessages.getString("setVerticalTextPositionTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "HorizontalTextPosition", ESlateRadioButton.class,
                    "getHorizontalTextPosition", "setHorizontalTextPosition"
                );

            pd21.setPropertyEditorClass(EditorHorizontalAlignment.class);
            pd21.setDisplayName(bundleMessages.getString("HorizontalTextPosition"));
            pd21.setShortDescription(bundleMessages.getString("setHorizontalTextPositionTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "Layout", ESlateRadioButton.class,
                    "getLayout", "setLayout"
                );

            pd22.setDisplayName(bundleMessages.getString("Layout"));
            pd22.setShortDescription(bundleMessages.getString("setLayoutTip"));
            pd22.setExpert(true);

            PropertyDescriptor pd23 = new PropertyDescriptor(
                    "Margin", ESlateRadioButton.class,
                    "getMargin", "setMargin"
                );

            pd23.setDisplayName(bundleMessages.getString("Margin"));
            pd23.setShortDescription(bundleMessages.getString("setMarginTip"));

            PropertyDescriptor pd24 = new PropertyDescriptor(
                    "Opaque", ESlateRadioButton.class,
                    "isOpaque", "setOpaque"
                );

            pd24.setDisplayName(bundleMessages.getString("Opaque"));
            pd24.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd25 = new PropertyDescriptor(
                    "PressedIcon", ESlateRadioButton.class,
                    "getPressedIcon", "setPressedIcon"
                );

            pd25.setDisplayName(bundleMessages.getString("PressedIcon"));
            pd25.setShortDescription(bundleMessages.getString("setPressedIconTip"));
            pd25.setExpert(true);

            PropertyDescriptor pd26 = new PropertyDescriptor(
                    "Selected", ESlateRadioButton.class,
                    "isSelected", "setSelected"
                );

            pd26.setDisplayName(bundleMessages.getString("Selected"));
            pd26.setShortDescription(bundleMessages.getString("setSelectedTip"));

            PropertyDescriptor pd27 = new PropertyDescriptor(
                    "Name", ESlateRadioButton.class,
                    "getName", "setName"
                );

            pd27.setDisplayName(bundleMessages.getString("Name"));
            pd27.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd28 = new PropertyDescriptor(
                    "Enabled", ESlateRadioButton.class,
                    "isEnabled", "setEnabled"
                );

            pd28.setDisplayName(bundleMessages.getString("Enabled"));
            pd28.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd29 = new PropertyDescriptor(
                    "MaximumSize", ESlateRadioButton.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd29.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd29.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd30 = new PropertyDescriptor(
                    "MinimumSize", ESlateRadioButton.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd30.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd30.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd31 = new PropertyDescriptor(
                    "PreferredSize", ESlateRadioButton.class,
                    "getPreferredSize", "setPreferredSize"
                );

            pd31.setDisplayName(bundleMessages.getString("PreferredSize"));
            pd31.setShortDescription(bundleMessages.getString("setPreferredSizeTip"));

            PropertyDescriptor pd32 = new PropertyDescriptor(
                    "PlugsUsed", ESlateRadioButton.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd32.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd32.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15,
                    pd16, pd17, pd18, pd19, pd20, pd21, pd23, pd24, pd25, pd26, pd28, pd29, pd30, pd31, pd32};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
