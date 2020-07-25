package gr.cti.eslate.clock;

import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.*;


public class ClockBeanInfo extends ESlateBeanInfo {

    static ResourceBundle resources;
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/clock16c.gif"));

    /**
     * Construct the BeanInfo.
     */
    public ClockBeanInfo() {
        resources = ResourceBundle.getBundle("gr.cti.eslate.clock.ClockBundleMessages", Locale.getDefault());
        String BeanInfo = "BeanInfo";
        Class myClass = getClass();
        String className = myClass.getName();

        if (className.endsWith(BeanInfo)) {
            className = className.substring(0, className.length() - BeanInfo.length());
        } 

    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return color16Icon.getImage();
        return null;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "Appearance", Clock.class,
                    "getAppearance", "setAppearance"
                );

            pd1.setDisplayName(resources.getString("Appearance"));
            pd1.setShortDescription(resources.getString("setAppearanceTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "SystemTimeCounting", Clock.class,
                    "getSystemTimeCounting", "setSystemTimeCounting"
                );

            pd2.setDisplayName(resources.getString("SystemTimeCounting"));
            pd2.setShortDescription(resources.getString("setSystemTimeCountingTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "BackgroundImageIcon", Clock.class,
                    "getBackgroundImageIcon", "setBackgroundImageIcon"
                );

            pd3.setDisplayName(resources.getString("BackgroundImageIcon"));
            pd3.setShortDescription(resources.getString("setBackgroundImageIconTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "Background", Clock.class,
                    "getBackground", "setBackground"
                );

            pd4.setDisplayName(resources.getString("Background"));
            pd4.setShortDescription(resources.getString("setBackgroundTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "HourHandColor", Clock.class,
                    "getHourHandColor", "setHourHandColor"
                );

            pd6.setDisplayName(resources.getString("HourHandColor"));
            pd6.setShortDescription(resources.getString("setHourHandColorTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "MinuteHandColor", Clock.class,
                    "getMinuteHandColor", "setMinuteHandColor"
                );

            pd7.setDisplayName(resources.getString("MinuteHandColor"));
            pd7.setShortDescription(resources.getString("setMinuteHandColorTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "SweepHandColor", Clock.class,
                    "getSweepHandColor", "setSweepHandColor"
                );

            pd8.setDisplayName(resources.getString("SweepHandColor"));
            pd8.setShortDescription(resources.getString("setSweepHandColorTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "FaceColor", Clock.class,
                    "getFaceColor", "setFaceColor"
                );

            pd9.setDisplayName(resources.getString("FaceColor"));
            pd9.setShortDescription(resources.getString("setFaceColorTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "CaseColor", Clock.class,
                    "getCaseColor", "setCaseColor"
                );

            pd10.setDisplayName(resources.getString("CaseColor"));
            pd10.setShortDescription(resources.getString("setCaseColorTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "TextColor", Clock.class,
                    "getTextColor", "setTextColor"
                );

            pd11.setDisplayName(resources.getString("TextColor"));
            pd11.setShortDescription(resources.getString("setTextColorTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "Text", Clock.class,
                    "getLogoText", "setLogoText"
                );

            pd12.setDisplayName(resources.getString("Text"));
            pd12.setShortDescription(resources.getString("setTextTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "DigitalReeedingsFont", Clock.class,
                    "getDigitalReedingsFont", "setDigitalReedingsFont"
                );

            pd13.setDisplayName(resources.getString("DigitalReedingsFont"));
            pd13.setShortDescription(resources.getString("setDigitalReedingsFontTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "LogoTextFont", Clock.class,
                    "getLogoTextFont", "setLogoTextFont"
                );

            pd14.setDisplayName(resources.getString("LogoTextFont"));
            pd14.setShortDescription(resources.getString("setLogoTextFontTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "Opaque", Clock.class,
                    "isOpaque", "setOpaque"
                );

            pd5.setDisplayName(resources.getString("Opaque"));
            pd5.setShortDescription(resources.getString("setOpaqueTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "Border", Clock.class,
                    "getBorder", "setBorder"
                );

            pd15.setDisplayName(resources.getString("Border"));
            pd15.setShortDescription(resources.getString("setBorderTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor(
                    "ShowHourNumbers", Clock.class,
                    "getHourNumberShowing", "setHourNumberShowing"
                );

            pd16.setDisplayName(resources.getString("ShowHourNumbers"));
            pd16.setShortDescription(resources.getString("setShowHourNumbersTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor(
                    "ShowMinNumbers", Clock.class,
                    "getMinNumberShowing", "setMinNumberShowing"
                );

            pd17.setDisplayName(resources.getString("ShowMinNumbers"));
            pd17.setShortDescription(resources.getString("setShowMinNumbersTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor(
                    "Time", Clock.class,
                    "getTime", "setTime"
                );

            pd18.setDisplayName(resources.getString("Time"));
            pd18.setPropertyEditorClass(gr.cti.eslate.propertyEditors.TimePropertyEditor.class);
            pd18.setShortDescription(resources.getString("setTimeTip"));

            PropertyDescriptor pd19 = new PropertyDescriptor(
                    "Date", Clock.class,
                    "getDate", "setDate"
                );

            pd19.setDisplayName(resources.getString("Date"));
            pd19.setPropertyEditorClass(gr.cti.eslate.propertyEditors.DatePropertyEditor.class);
            pd19.setShortDescription(resources.getString("setDateTip"));

            PropertyDescriptor pd20 = new PropertyDescriptor(
                    "Format", Clock.class,
                    "getDateFormat", "setDateFormat"
                );

            pd20.setDisplayName(resources.getString("DateFormat"));
            pd20.setPropertyEditorClass(DateFormatPropertyEditor.class);
            pd20.setShortDescription(resources.getString("setDateFormatTip"));

            PropertyDescriptor pd21 = new PropertyDescriptor(
                    "ShowDate", Clock.class,
                    "getDateVisible", "setDateVisible"
                );

            pd21.setDisplayName(resources.getString("DateVisible"));
            pd21.setShortDescription(resources.getString("setDateVisibleTip"));

            PropertyDescriptor pd22 = new PropertyDescriptor(
                    "PlugsUsed", Clock.class,
                    "getPlugsUsed", "setPlugsUsed"
                );

            pd22.setDisplayName(resources.getString("PlugsUsed"));
            pd22.setShortDescription(resources.getString("setPlugsUsedTip"));


            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15, pd16, pd17, pd18, pd19, pd20, pd21, pd22};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
