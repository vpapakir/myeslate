package gr.cti.eslate.eslateTextField;


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


public class ESlateTextFieldBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JTextFieldColor16p.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateTextFieldBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateTextField.BundleMessages", Locale.getDefault());
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
            Method keyPressedMethod = KeyListener.class.getMethod(
                    "keyPressed", new Class[] {KeyEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addKeyListener", new Class[] {KeyListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeKeyListener",
                    new Class[] {KeyListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(keyPressedMethod);

            md.setDisplayName(bundleMessages.getString("KeyPressed"));

            esd = new EventSetDescriptor(
                        "keyPressed", KeyListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method keyReleasedMethod = KeyListener.class.getMethod(
                    "keyReleased", new Class[] {KeyEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addKeyListener", new Class[] {KeyListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeKeyListener",
                    new Class[] {KeyListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(keyReleasedMethod);

            md.setDisplayName(bundleMessages.getString("KeyReleased"));

            esd = new EventSetDescriptor(
                        "keyReleased", KeyListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method keyTypedMethod = KeyListener.class.getMethod(
                    "keyTyped", new Class[] {KeyEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addKeyListener", new Class[] {KeyListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeKeyListener",
                    new Class[] {KeyListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(keyTypedMethod);

            md.setDisplayName(bundleMessages.getString("KeyTyped"));

            esd = new EventSetDescriptor(
                        "keyTyped", KeyListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );
            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method caretUpdateMethod = CaretListener.class.getMethod(
                    "caretUpdate", new Class[] {CaretEvent.class}
                );
            Method addListenerMethod = componentClass.getMethod(
                    "addCaretListener", new Class[] {CaretListener.class}
                );
            Method removelistenerMethod = componentClass.getMethod(
                    "removeCaretListener",
                    new Class[] {CaretListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(caretUpdateMethod);

            md.setDisplayName(bundleMessages.getString("CaretUpdate"));

            esd = new EventSetDescriptor(
                        "caretUpdate", CaretListener.class, new MethodDescriptor[] {md},
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

        EventSetDescriptor[] d = new EventSetDescriptor[array1.length + descriptors.size()];

        System.arraycopy(array1, 0, d, 0, array1.length);

        for (int i = 0; i < descriptors.size(); i++)
            d[array1.length + i] = (EventSetDescriptor) descriptors.get(i);

        return d;

    }

    public PropertyDescriptor[] getPropertyDescriptors() {

        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Background", ESlateTextField.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateTextField.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateTextField.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "CaretColor", ESlateTextField.class,
                    "getCaretColor", "setCaretColor"
                );

            pd4.setDisplayName(bundleMessages.getString("CaretColor"));
            pd4.setShortDescription(bundleMessages.getString("setCaretColorTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "CaretPosition", ESlateTextField.class,
                    "getCaretPosition", "setCaretPosition"
                );

            pd5.setDisplayName(bundleMessages.getString("CaretPosition"));
            pd5.setShortDescription(bundleMessages.getString("setCaretPositionTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "Columns", ESlateTextField.class,
                    "getColumns", "setColumns"
                );

            pd6.setDisplayName(bundleMessages.getString("Columns"));
            pd6.setShortDescription(bundleMessages.getString("setColumnsTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "DisabledTextColor", ESlateTextField.class,
                    "getDisabledTextColor", "setDisabledTextColor"
                );

            pd7.setDisplayName(bundleMessages.getString("DisabledTextColor"));
            pd7.setShortDescription(bundleMessages.getString("setDisabledTextColorTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "Editable", ESlateTextField.class,
                    "isEditable", "setEditable"
                );

            pd8.setDisplayName(bundleMessages.getString("Editable"));
            pd8.setShortDescription(bundleMessages.getString("setEditableTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "Foreground", ESlateTextField.class,
                    "getForeground", "setForeground"
                );

            pd10.setDisplayName(bundleMessages.getString("Foreground"));
            pd10.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "Text", ESlateTextField.class,
                    "getText", "setText"
                );

            pd11.setDisplayName(bundleMessages.getString("Text"));
            pd11.setShortDescription(bundleMessages.getString("setTextTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "ToolTipText", ESlateTextField.class,
                    "getToolTipText", "setToolTipText"
                );

            pd12.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd12.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "AlignmentX", ESlateTextField.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd13.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd13.setShortDescription(bundleMessages.getString("setAlignmentXTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "AlignmentY", ESlateTextField.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd14.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd14.setShortDescription(bundleMessages.getString("setAlignmentYTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "MaximumSize", ESlateTextField.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd15.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd15.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "MinimumSize", ESlateTextField.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd16.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd16.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "SelectedTextColor", ESlateTextField.class,
                    "getSelectedTextColor", "setSelectedTextColor"
                );

            pd18.setDisplayName(bundleMessages.getString("SelectedTextColor"));
            pd18.setShortDescription(bundleMessages.getString("setSelectedTextColorTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "SelectionColor", ESlateTextField.class,
                    "getSelectionColor", "setSelectionColor"
                );

            pd19.setDisplayName(bundleMessages.getString("SelectionColor"));
            pd19.setShortDescription(bundleMessages.getString("setSelectionColorTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "SelectionStart", ESlateTextField.class,
                    "getSelectionStart", "setSelectionStart"
                );

            pd20.setDisplayName(bundleMessages.getString("SelectionStart"));
            pd20.setShortDescription(bundleMessages.getString("setSelectionStartTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "SelectionEnd", ESlateTextField.class,
                    "getSelectionEnd", "setSelectionEnd"
                );

            pd21.setDisplayName(bundleMessages.getString("SelectionEnd"));
            pd21.setShortDescription(bundleMessages.getString("setSelectionEndTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "HorizontalAlignment", ESlateTextField.class,
                    "getHorizontalAlignment", "setHorizontalAlignment"
                );

            pd22.setPropertyEditorClass(EditorHorizontalAlignment.class);
            pd22.setDisplayName(bundleMessages.getString("HorizontalAlignment"));
            pd22.setShortDescription(bundleMessages.getString("setHorizontalAlignmentTip"));

            PropertyDescriptor pd24 = new PropertyDescriptor(
                    "Opaque", ESlateTextField.class,
                    "isOpaque", "setOpaque"
                );

            pd24.setDisplayName(bundleMessages.getString("Opaque"));
            pd24.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd25 = new PropertyDescriptor(
                    "ScrollOffset", ESlateTextField.class,
                    "getScrollOffset", "setScrollOffset"
                );

            pd25.setDisplayName(bundleMessages.getString("ScrollOffset"));
            pd25.setShortDescription(bundleMessages.getString("setScrollOffsetTip"));

            PropertyDescriptor pd26 = new PropertyDescriptor(
                    "DoubleBuffered", ESlateTextField.class,
                    "isDoubleBuffered", "setDoubleBuffered"
                );

            pd26.setDisplayName(bundleMessages.getString("DoubleBuffered"));
            pd26.setShortDescription(bundleMessages.getString("setDoubleBufferedTip"));

            PropertyDescriptor pd27 = new PropertyDescriptor(
                    "Name", ESlateTextField.class,
                    "getName", "setName"
                );

            pd27.setDisplayName(bundleMessages.getString("Name"));
            pd27.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd28 = new PropertyDescriptor(
                    "NumberMode", ESlateTextField.class,
                    "getNumberMode", "setNumberMode"
                );

            pd28.setDisplayName(bundleMessages.getString("NumberMode"));
            pd28.setShortDescription(bundleMessages.getString("setNumberModeTip"));

            PropertyDescriptor pd29 = new PropertyDescriptor(
                    "FireOnEnterPress", ESlateTextField.class,
                    "getFireOnEnterPress", "setFireOnEnterPress"
                );

            pd29.setDisplayName(bundleMessages.getString("FireOnEnterPress"));
            pd29.setShortDescription(bundleMessages.getString("setFireOnEnterPressTip"));

            PropertyDescriptor pd30 = new PropertyDescriptor(
                    "Enabled", ESlateTextField.class,
                    "isEnabled", "setEnabled"
                );

            pd30.setDisplayName(bundleMessages.getString("Enabled"));
            pd30.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd31 = new PropertyDescriptor(
                    "PlugsUsed", ESlateTextField.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd31.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd31.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd10, pd11, pd12, pd13, pd14, pd15,
                    pd16, pd18, pd19, pd20, pd21, pd22, pd24, pd25, pd26, pd27, pd28, pd29, pd30, pd31};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
