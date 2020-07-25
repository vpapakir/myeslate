package gr.cti.eslate.eslateLabel;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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


public class ESlateLabelBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JLabelColor16p.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateLabelBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateLabel.BundleMessages", Locale.getDefault());
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
                    "Background", ESlateLabel.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateLabel.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateLabel.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "Icon", ESlateLabel.class,
                    "getIcon", "setIcon"
                );

            pd4.setDisplayName(bundleMessages.getString("Icon"));
            pd4.setShortDescription(bundleMessages.getString("setIconTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "Foreground", ESlateLabel.class,
                    "getForeground", "setForeground"
                );

            pd5.setDisplayName(bundleMessages.getString("Foreground"));
            pd5.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "Text", ESlateLabel.class,
                    "getText", "setText"
                );

            pd6.setDisplayName(bundleMessages.getString("Text"));
            pd6.setShortDescription(bundleMessages.getString("setTextTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "ToolTipText", ESlateLabel.class,
                    "getToolTipText", "setToolTipText"
                );

            pd7.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd7.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "AlignmentX", ESlateLabel.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd8.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd8.setShortDescription(bundleMessages.getString("setAlignmentXTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "AlignmentY", ESlateLabel.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd9.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd9.setShortDescription(bundleMessages.getString("setAlignmentYTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "DisabledIcon", ESlateLabel.class,
                    "getDisabledIcon", "setDisabledIcon"
                );

            pd10.setDisplayName(bundleMessages.getString("DisabledIcon"));
            pd10.setShortDescription(bundleMessages.getString("setDisabledIconTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "HorizontalAlignment", ESlateLabel.class,
                    "getHorizontalAlignment", "setHorizontalAlignment"
                );

            pd11.setPropertyEditorClass(EditorHorizontalAlignment.class);
            pd11.setDisplayName(bundleMessages.getString("HorizontalAlignment"));
            pd11.setShortDescription(bundleMessages.getString("setHorizontalAlignmentTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "VerticalAlignment", ESlateLabel.class,
                    "getVerticalAlignment", "setVerticalAlignment"
                );

            pd12.setPropertyEditorClass(EditorVerticalAlignment.class);
            pd12.setDisplayName(bundleMessages.getString("VerticalAlignment"));
            pd12.setShortDescription(bundleMessages.getString("setVerticalAlignmentTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "Opaque", ESlateLabel.class,
                    "isOpaque", "setOpaque"
                );

            pd13.setDisplayName(bundleMessages.getString("Opaque"));
            pd13.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "Name", ESlateLabel.class,
                    "getName", "setName"
                );

            pd14.setDisplayName(bundleMessages.getString("Name"));
            pd14.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "MaximumSize", ESlateLabel.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd15.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd15.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "MinimumSize", ESlateLabel.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd16.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd16.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "PreferredSize", ESlateLabel.class,
                    "getPreferredSize", "setPreferredSize"
                );

            pd17.setDisplayName(bundleMessages.getString("PreferredSize"));
            pd17.setShortDescription(bundleMessages.getString("setPreferredSizeTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "Layout", ESlateLabel.class,
                    "getLayout", "setLayout"
                );

            pd18.setDisplayName(bundleMessages.getString("Layout"));
            pd18.setShortDescription(bundleMessages.getString("setLayoutTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "IconTextGap", ESlateLabel.class,
                    "getIconTextGap", "setIconTextGap"
                );

            pd19.setDisplayName(bundleMessages.getString("IconTextGap"));
            pd19.setShortDescription(bundleMessages.getString("setIconTextGapTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "DoubleBuffered", ESlateLabel.class,
                    "isDoubleBuffered", "setDoubleBuffered"
                );

            pd20.setDisplayName(bundleMessages.getString("DoubleBuffered"));
            pd20.setShortDescription(bundleMessages.getString("setDoubleBufferedTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "Enabled", ESlateLabel.class,
                    "isEnabled", "setEnabled"
                );

            pd21.setDisplayName(bundleMessages.getString("Enabled"));
            pd21.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "HorizontalTextPosition", ESlateLabel.class,
                    "getHorizontalTextPosition", "setHorizontalTextPosition"
                );

            pd22.setPropertyEditorClass(EditorHorizontalAlignment.class);
            pd22.setDisplayName(bundleMessages.getString("HorizontalTextPosition"));
            pd22.setShortDescription(bundleMessages.getString("setHorizontalTextPositionTip"));

            PropertyDescriptor pd23 = new PropertyDescriptor(
                    "VerticalTextPosition", ESlateLabel.class,
                    "getVerticalTextPosition", "setVerticalTextPosition"
                );

            pd23.setPropertyEditorClass(EditorVerticalAlignment.class);
            pd23.setDisplayName(bundleMessages.getString("VerticalTextPosition"));
            pd23.setShortDescription(bundleMessages.getString("setVerticalTextPositionTip"));

            PropertyDescriptor pd24 = new PropertyDescriptor(
                    "PlugsUsed", ESlateLabel.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd24.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd24.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));
//            pd24.setExpert(true);

            PropertyDescriptor pd25 = new PropertyDescriptor(
                    "MultilineMode", ESlateLabel.class,
                    "isMultilineMode", "setMultilineMode"
                );

            pd25.setDisplayName(bundleMessages.getString("MultilineMode"));
            pd25.setShortDescription(bundleMessages.getString("MultilineModeTip"));
            pd25.setExpert(true);

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd15, pd16, pd17, pd19, pd20, pd21, pd22, pd23, pd24, pd25};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
