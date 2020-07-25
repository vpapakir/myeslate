package gr.cti.eslate.eslateSlider;


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

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ESlateSliderBeanInfo extends ESlateBeanInfo {

    /**
     * Localized bundleMessages.
     */
    static ResourceBundle bundleMessages;
    private Class componentClass;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JSliderColor16p.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ESlateSliderBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateSlider.BundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class myClass = getClass();
        String className = myClass.getName();

        if (className.endsWith(BeanInfo)) {
            className = className.substring(0, className.length() - BeanInfo.length());
            try {
                this.componentClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                this.componentClass = myClass;
            }
        } else {
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
        EventSetDescriptor esd = null;

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

            md.setDisplayName(bundleMessages.getString("StateChanged"));

            esd = new EventSetDescriptor(
                        "stateChanged", ChangeListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        EventSetDescriptor[] d = new EventSetDescriptor[array1.length + 1];

        System.arraycopy(array1, 0, d, 0, array1.length);
        d[array1.length] = esd;

        return d;

    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * @return	An array of PropertyDescriptors describing the editable
     *		properties supported by this bean.
     */

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Background", ESlateSlider.class,
                    "getBackground", "setBackground"
                );

            pd1.setDisplayName(bundleMessages.getString("Background"));
            pd1.setShortDescription(bundleMessages.getString("setBackgroundTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "Border", ESlateSlider.class,
                    "getBorder", "setBorder"
                );

            pd2.setDisplayName(bundleMessages.getString("Border"));
            pd2.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "Font", ESlateSlider.class,
                    "getFont", "setFont"
                );

            pd3.setDisplayName(bundleMessages.getString("Font"));
            pd3.setShortDescription(bundleMessages.getString("setFontTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "Foreground", ESlateSlider.class,
                    "getForeground", "setForeground"
                );

            pd4.setDisplayName(bundleMessages.getString("Foreground"));
            pd4.setShortDescription(bundleMessages.getString("setForegroundTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "ToolTipText", ESlateSlider.class,
                    "getToolTipText", "setToolTipText"
                );

            pd5.setDisplayName(bundleMessages.getString("ToolTipText"));
            pd5.setShortDescription(bundleMessages.getString("setToolTipTextTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "AlignmentX", ESlateSlider.class,
                    "getAlignmentX", "setAlignmentX"
                );

            pd6.setDisplayName(bundleMessages.getString("AlignmentX"));
            pd6.setShortDescription(bundleMessages.getString("setAlignmentXTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "AlignmentY", ESlateSlider.class,
                    "getAlignmentY", "setAlignmentY"
                );

            pd7.setDisplayName(bundleMessages.getString("AlignmentY"));
            pd7.setShortDescription(bundleMessages.getString("setAlignmentYTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "DoubleBuffered", ESlateSlider.class,
                    "isDoubleBuffered", "setDoubleBuffered"
                );

            pd8.setDisplayName(bundleMessages.getString("DoubleBuffered"));
            pd8.setShortDescription(bundleMessages.getString("setDoubleBufferedTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "Layout", ESlateSlider.class,
                    "getLayout", "setLayout"
                );

            pd9.setDisplayName(bundleMessages.getString("Layout"));
            pd9.setShortDescription(bundleMessages.getString("setLayoutTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "PaintLabels", ESlateSlider.class,
                    "getPaintLabels", "setPaintLabels"
                );

            pd10.setDisplayName(bundleMessages.getString("PaintLabels"));
            pd10.setShortDescription(bundleMessages.getString("setPaintLabelsTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "Opaque", ESlateSlider.class,
                    "isOpaque", "setOpaque"
                );

            pd11.setDisplayName(bundleMessages.getString("Opaque"));
            pd11.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "Name", ESlateSlider.class,
                    "getName", "setName"
                );

            pd12.setDisplayName(bundleMessages.getString("Name"));
            pd12.setShortDescription(bundleMessages.getString("setNameTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "Inverted", ESlateSlider.class,
                    "getInverted", "setInverted"
                );

            pd13.setDisplayName(bundleMessages.getString("Inverted"));
            pd13.setShortDescription(bundleMessages.getString("setInvertedTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "PaintTicks", ESlateSlider.class,
                    "getPaintTicks", "setPaintTicks"
                );

            pd14.setDisplayName(bundleMessages.getString("PaintTicks"));
            pd14.setShortDescription(bundleMessages.getString("setPaintTicksTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "PaintTrack", ESlateSlider.class,
                    "getPaintTrack", "setPaintTrack"
                );

            pd15.setDisplayName(bundleMessages.getString("PaintTrack"));
            pd15.setShortDescription(bundleMessages.getString("setPaintTrackTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "SnapToTicks", ESlateSlider.class,
                    "getSnapToTicks", "setSnapToTicks"
                );

            pd16.setDisplayName(bundleMessages.getString("SnapToTicks"));
            pd16.setShortDescription(bundleMessages.getString("setSnapToTicksTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "Maximum", ESlateSlider.class,
                    "getMaximum", "setMaximum"
                );

            pd17.setDisplayName(bundleMessages.getString("Maximum"));
            pd17.setShortDescription(bundleMessages.getString("setMaximumTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "MaximumSize", ESlateSlider.class,
                    "getMaximumSize", "setMaximumSize"
                );

            pd18.setDisplayName(bundleMessages.getString("MaximumSize"));
            pd18.setShortDescription(bundleMessages.getString("setMaximumSizeTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "MinimumSize", ESlateSlider.class,
                    "getMinimumSize", "setMinimumSize"
                );

            pd19.setDisplayName(bundleMessages.getString("MinimumSize"));
            pd19.setShortDescription(bundleMessages.getString("setMinimumSizeTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "Minimum", ESlateSlider.class,
                    "getMinimum", "setMinimum"
                );

            pd20.setDisplayName(bundleMessages.getString("Minimum"));
            pd20.setShortDescription(bundleMessages.getString("setMinimumTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "PreferredSize", ESlateSlider.class,
                    "getPreferredSize", "setPreferredSize"
                );

            pd21.setDisplayName(bundleMessages.getString("PreferredSize"));
            pd21.setShortDescription(bundleMessages.getString("setPreferredSizeTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "MajorTickSpacing", ESlateSlider.class,
                    "getMajorTickSpacing", "setMajorTickSpacing"
                );

            pd22.setDisplayName(bundleMessages.getString("MajorTickSpacing"));
            pd22.setShortDescription(bundleMessages.getString("setMajorTickSpacingTip"));

            PropertyDescriptor pd23 = new PropertyDescriptor(
                    "MinorTickSpacing", ESlateSlider.class,
                    "getMinorTickSpacing", "setMinorTickSpacing"
                );

            pd23.setDisplayName(bundleMessages.getString("MinorTickSpacing"));
            pd23.setShortDescription(bundleMessages.getString("setMinorTickSpacingTip"));

            PropertyDescriptor pd24 = new PropertyDescriptor(
                    "Orientation", ESlateSlider.class,
                    "getOrientation", "setOrientation"
                );

            pd24.setPropertyEditorClass(EditorOrientation.class);
            pd24.setDisplayName(bundleMessages.getString("Orientation"));
            pd24.setShortDescription(bundleMessages.getString("setOrientationTip"));

            PropertyDescriptor pd25 = new PropertyDescriptor(
                    "Value", ESlateSlider.class,
                    "getValue", "setValue"
                );

            pd25.setDisplayName(bundleMessages.getString("Value"));
            pd25.setShortDescription(bundleMessages.getString("setValueTip"));

            PropertyDescriptor pd26 = new PropertyDescriptor(
                    "Enabled", ESlateSlider.class,
                    "isEnabled", "setEnabled"
                );

            pd26.setDisplayName(bundleMessages.getString("Enabled"));
            pd26.setShortDescription(bundleMessages.getString("setEnabledTip"));

            PropertyDescriptor pd30 = new PropertyDescriptor(
                    "PlugsUsed", ESlateSlider.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd30.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd30.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd10, pd11, pd12, pd13, pd14, pd15,
                    pd16, pd17, pd18, pd19, pd20, pd21, pd22, pd23, pd24, pd25, pd26, pd30};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
