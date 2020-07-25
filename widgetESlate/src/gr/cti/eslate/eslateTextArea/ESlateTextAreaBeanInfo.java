package gr.cti.eslate.eslateTextArea;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


public class ESlateTextAreaBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bund0eMessages.
     */
    static ResourceBundle bundleMessages;
    private Class<?> componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JTextAreaColor16p.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateTextAreaBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateTextArea.BundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class<?> myClass = getClass();
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
        ArrayList<EventSetDescriptor> descriptors = new ArrayList<EventSetDescriptor>();
        EventSetDescriptor esd = null;

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
            descriptors.add(esd);

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
                    "Background", ESlateTextArea.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateTextArea.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateTextArea.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "CaretColor", ESlateTextArea.class,
                    "getCaretColor", "setCaretColor"
                );

            pd4.setDisplayName(bundleMessages.getString("CaretColor"));
            pd4.setShortDescription(bundleMessages.getString("setCaretColorTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "CaretPosition", ESlateTextArea.class,
                    "getCaretPosition", "setCaretPosition"
                );

            pd5.setDisplayName(bundleMessages.getString("CaretPosition"));
            pd5.setShortDescription(bundleMessages.getString("setCaretPositionTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "Columns", ESlateTextArea.class,
                    "getColumns", "setColumns"
                );

            pd6.setDisplayName(bundleMessages.getString("Columns"));
            pd6.setShortDescription(bundleMessages.getString("setColumnsTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "DisabledTextColor", ESlateTextArea.class,
                    "getDisabledTextColor", "setDisabledTextColor"
                );

            pd7.setDisplayName(bundleMessages.getString("DisabledTextColor"));
            pd7.setShortDescription(bundleMessages.getString("setDisabledTextColorTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "Editable", ESlateTextArea.class,
                    "isEditable", "setEditable"
                );

            pd8.setDisplayName(bundleMessages.getString("Editable"));
            pd8.setShortDescription(bundleMessages.getString("setEditableTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "LineWrap", ESlateTextArea.class,
                    "getLineWrap", "setLineWrap"
                );

            pd9.setDisplayName(bundleMessages.getString("LineWrap"));
            pd9.setShortDescription(bundleMessages.getString("setLineWrapTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "Foreground", ESlateTextArea.class,
                    "getForeground", "setForeground"
                );

            pd10.setDisplayName(bundleMessages.getString("Foreground"));
            pd10.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "Text", ESlateTextArea.class,
                    "getText", "setText"
                );

            pd11.setDisplayName(bundleMessages.getString("Text"));
            pd11.setShortDescription(bundleMessages.getString("setTextTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "ToolTipText", ESlateTextArea.class,
                    "getToolTipText", "setToolTipText"
                );

            pd12.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd12.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "AlignmentX", ESlateTextArea.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd13.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd13.setShortDescription(bundleMessages.getString("setAlignmentXTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "AlignmentY", ESlateTextArea.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd14.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd14.setShortDescription(bundleMessages.getString("setAlignmentYTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "MaximumSize", ESlateTextArea.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd15.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd15.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "MinimumSize", ESlateTextArea.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd16.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd16.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "Rows", ESlateTextArea.class,
                    "getRows", "setRows"
                );

            pd17.setDisplayName(bundleMessages.getString("Rows"));
            pd17.setShortDescription(bundleMessages.getString("setRowsTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "SelectedTextColor", ESlateTextArea.class,
                    "getSelectedTextColor", "setSelectedTextColor"
                );

            pd18.setDisplayName(bundleMessages.getString("SelectedTextColor"));
            pd18.setShortDescription(bundleMessages.getString("setSelectedTextColorTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "SelectionColor", ESlateTextArea.class,
                    "getSelectionColor", "setSelectionColor"
                );

            pd19.setDisplayName(bundleMessages.getString("SelectionColor"));
            pd19.setShortDescription(bundleMessages.getString("setSelectionColorTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "SelectionStart", ESlateTextArea.class,
                    "getSelectionStart", "setSelectionStart"
                );

            pd20.setDisplayName(bundleMessages.getString("SelectionStart"));
            pd20.setShortDescription(bundleMessages.getString("setSelectionStartTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "SelectionEnd", ESlateTextArea.class,
                    "getSelectionEnd", "setSelectionEnd"
                );

            pd21.setDisplayName(bundleMessages.getString("SelectionEnd"));
            pd21.setShortDescription(bundleMessages.getString("setSelectionEndTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "TabSize", ESlateTextArea.class,
                    "getTabSize", "setTabSize"
                );

            pd22.setDisplayName(bundleMessages.getString("TabSize"));
            pd22.setShortDescription(bundleMessages.getString("setTabSizeTip"));

            PropertyDescriptor pd24 = new PropertyDescriptor(
                    "Opaque", ESlateTextArea.class,
                    "isOpaque", "setOpaque"
                );

            pd24.setDisplayName(bundleMessages.getString("Opaque"));
            pd24.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd25 = new PropertyDescriptor(
                    "WrapStyleWord", ESlateTextArea.class,
                    "getWrapStyleWord", "setWrapStyleWord"
                );

            pd25.setDisplayName(bundleMessages.getString("WrapStyleWord"));
            pd25.setShortDescription(bundleMessages.getString("setWrapStyleWordTip"));

            PropertyDescriptor pd26 = new PropertyDescriptor(
                    "DoubleBuffered", ESlateTextArea.class,
                    "isDoubleBuffered", "setDoubleBuffered"
                );

            pd26.setDisplayName(bundleMessages.getString("DoubleBuffered"));
            pd26.setShortDescription(bundleMessages.getString("setDoubleBufferedTip"));

            PropertyDescriptor pd27 = new PropertyDescriptor(
                    "Name", ESlateTextArea.class,
                    "getName", "setName"
                );

            pd27.setDisplayName(bundleMessages.getString("Name"));
            pd27.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd28 = new PropertyDescriptor(
                    "BackgroundImageFile", ESlateTextArea.class,
                    "getBackgroundImageFile", "setBackgroundImageFile"
                );

            pd28.setDisplayName(bundleMessages.getString("BackgroundImageFile"));
            pd28.setShortDescription(bundleMessages.getString("setBackgroundImageFileTip"));

            PropertyDescriptor pd29 = new PropertyDescriptor(
                    "TextFile", ESlateTextArea.class,
                    "getTextFile", "setTextFile"
                );

            pd29.setDisplayName(bundleMessages.getString("TextFile"));
            pd29.setShortDescription(bundleMessages.getString("setTextFileTip"));
            pd29.setPropertyEditorClass(TextFilePropertyEditor.class);
            
            PropertyDescriptor pd30 = new PropertyDescriptor(
                    "FireOnEnterPress", ESlateTextArea.class,
                    "getFireOnEnterPress", "setFireOnEnterPress"
                );

            pd30.setDisplayName(bundleMessages.getString("FireOnEnterPress"));
            pd30.setShortDescription(bundleMessages.getString("setFireOnEnterPressTip"));

            PropertyDescriptor pd31 = new PropertyDescriptor(
                    "HorizontalScrollBarPolicy", ESlateTextArea.class,
                    "getHorizontalScrollBarPolicy", "setHorizontalScrollBarPolicy"
                );

            pd31.setPropertyEditorClass(HorizontalScrollBarPolicyEditor.class);
            pd31.setDisplayName(bundleMessages.getString("HorizontalScrollBarPolicy"));
            pd31.setShortDescription(bundleMessages.getString("setHorizontalScrollBarPolicyTip"));

            PropertyDescriptor pd32 = new PropertyDescriptor(
                    "VerticalScrollBarPolicy", ESlateTextArea.class,
                    "getVerticalScrollBarPolicy", "setVerticalScrollBarPolicy"
                );

            pd32.setPropertyEditorClass(VerticalScrollBarPolicyEditor.class);
            pd32.setDisplayName(bundleMessages.getString("VerticalScrollBarPolicy"));
            pd32.setShortDescription(bundleMessages.getString("setVerticalScrollBarPolicyTip"));

            PropertyDescriptor pd33 = new PropertyDescriptor(
                    "PlugsUsed", ESlateTextArea.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd33.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd33.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15,
                    pd16, pd17, pd18, pd19, pd20, pd21, pd22, pd24, pd25, pd26, pd27, pd28, pd29, pd30,
                    pd31, pd32, pd33};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
