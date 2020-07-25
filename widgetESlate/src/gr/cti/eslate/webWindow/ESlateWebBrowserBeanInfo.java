package gr.cti.eslate.webWindow;


import gr.cti.eslate.utils.ESlateBeanInfo;
import horst.webwindow.event.LinkEvent;
import horst.webwindow.event.LinkListener;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;


public class ESlateWebBrowserBeanInfo extends ESlateBeanInfo {

    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/bIcon.gif"));
    static ResourceBundle bundleMessages;

    public ESlateWebBrowserBeanInfo() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.webWindow.BrowserBundle", Locale.getDefault());
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

    public EventSetDescriptor[] getEventSetDescriptors() {

        EventSetDescriptor ed1 = null, ed2 = null, ed3 = null;

        try {
            Method listenerMethod = LinkListener.class.getMethod("linkClicked", new Class[] {LinkEvent.class}
                );
            Method addListenerMethod = ESlateWebBrowser.class.getMethod("addLinkListener", new Class[] {LinkListener.class}
                );
            Method removelistenerMethod = ESlateWebBrowser.class.getMethod("removeLinkListener", new Class[] {LinkListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(listenerMethod);

            md.setDisplayName(bundleMessages.getString("LinkClicked"));

            ed1 = new EventSetDescriptor("linkClicked",
                        LinkListener.class,
                        new MethodDescriptor[] {md},
                        addListenerMethod,
                        removelistenerMethod);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method listenerMethod = LinkListener.class.getMethod("mousePressedOnLink", new Class[] {LinkEvent.class}
                );
            Method addListenerMethod = ESlateWebBrowser.class.getMethod("addLinkListener", new Class[] {LinkListener.class}
                );
            Method removelistenerMethod = ESlateWebBrowser.class.getMethod("removeLinkListener", new Class[] {LinkListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(listenerMethod);

            md.setDisplayName(bundleMessages.getString("MousePressedOnLink"));

            ed2 = new EventSetDescriptor("mousePressedOnLink",
                        LinkListener.class,
                        new MethodDescriptor[] {md},
                        addListenerMethod,
                        removelistenerMethod);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method listenerMethod = LinkListener.class.getMethod("mouseReleasedOnLink", new Class[] {LinkEvent.class}
                );
            Method addListenerMethod = ESlateWebBrowser.class.getMethod("addLinkListener", new Class[] {LinkListener.class}
                );
            Method removelistenerMethod = ESlateWebBrowser.class.getMethod("removeLinkListener", new Class[] {LinkListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(listenerMethod);

            md.setDisplayName(bundleMessages.getString("MouseReleasedOnLink"));

            ed3 = new EventSetDescriptor("mouseReleasedOnLink",
                        LinkListener.class,
                        new MethodDescriptor[] {md},
                        addListenerMethod,
                        removelistenerMethod);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        //Combine my event descriptors with my parent's descriptors.

        EventSetDescriptor[] par = super.getEventSetDescriptors();
        EventSetDescriptor[] my = new EventSetDescriptor[par.length + 3];

        System.arraycopy(par, 0, my, 0, par.length);
        my[my.length - 3] = ed1;
        my[my.length - 2] = ed2;
        my[my.length - 1] = ed3;
        return my;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                    "BrowserMenuBarVisible", ESlateWebBrowser.class,
                    "isMenuBarVisible", "setMenuBarVisible"
                );

            pd1.setDisplayName(bundleMessages.getString("BrowserMenuBarVisible"));
            pd1.setShortDescription(bundleMessages.getString("setMenuBarVisibleTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                    "ToolbarVisible", ESlateWebBrowser.class,
                    "isToolBarVisible", "setToolBarVisible"
                );

            pd2.setDisplayName(bundleMessages.getString("BrowserButtonPanelVisible"));
            pd2.setShortDescription(bundleMessages.getString("setButtonPanelVisibleTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                    "BrowserLocationPanelVisible", ESlateWebBrowser.class,
                    "isLocationPanelVisible", "setLocationPanelVisible"
                );

            pd3.setDisplayName(bundleMessages.getString("BrowserLocationPanelVisible"));
            pd3.setShortDescription(bundleMessages.getString("setLocationPanelVisibleTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                    "BrowserStatusPanelVisible", ESlateWebBrowser.class,
                    "isStatusPanelVisible", "setStatusPanelVisible"
                );

            pd4.setDisplayName(bundleMessages.getString("BrowserStatusPanelVisible"));
            pd4.setShortDescription(bundleMessages.getString("setStatusPanelVisibleTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                    "Border", ESlateWebBrowser.class,
                    "getBorder", "setBorder"
                );

            pd5.setDisplayName(bundleMessages.getString("Border"));
            pd5.setShortDescription(bundleMessages.getString("setBorderTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                    "HomeURL", ESlateWebBrowser.class,
                    "getHomeURL", "setHomeURL"
                );

            pd6.setDisplayName(bundleMessages.getString("HomeURL"));
            pd6.setPropertyEditorClass(gr.cti.eslate.propertyEditors.StringPropertyEditor2.class);
            pd6.setShortDescription(bundleMessages.getString("setHomeURLTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                    "Location", ESlateWebBrowser.class,
                    "getURLLocation", "setLocation"
                );

            pd7.setDisplayName(bundleMessages.getString("Location"));
            pd7.setPropertyEditorClass(gr.cti.eslate.propertyEditors.StringPropertyEditor2.class);
            pd7.setShortDescription(bundleMessages.getString("setLocationTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                    "SearchURL", ESlateWebBrowser.class,
                    "getSearchURL", "setSearchURL"
                );

            pd8.setDisplayName(bundleMessages.getString("SearchURL"));
            pd8.setPropertyEditorClass(gr.cti.eslate.propertyEditors.StringPropertyEditor2.class);
            pd8.setShortDescription(bundleMessages.getString("setSearchURLTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                    "PlugsUsed", ESlateWebBrowser.class,
                    "isPlugsUsed", "setPlugsUsed"
                );

            pd9.setDisplayName(bundleMessages.getString("PlugsUsed"));
            pd9.setShortDescription(bundleMessages.getString("setPlugsUsedTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                    "Encoding", ESlateWebBrowser.class,
                    "getCharacterEncoding", "setCharacterEncoding"
                );

            pd10.setDisplayName(bundleMessages.getString("CharacterEncoding"));
            pd10.setShortDescription(bundleMessages.getString("setCharacterEncodingTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                    "MenuBarUsed", ESlateWebBrowser.class,
                    "isMenuBarUsed", "setMenuBarUsed"
                );

            pd11.setDisplayName(bundleMessages.getString("MenuBarUsed"));
            pd11.setShortDescription(bundleMessages.getString("setMenuBarUsedTip"));
            pd11.setExpert(true);

/*            PropertyDescriptor pd12 = new PropertyDescriptor(
                    "ToolBarUsed", ESlateWebBrowser.class,
                    "isToolBarUsed", "setToolBarUsed"
                );

            pd12.setDisplayName(bundleMessages.getString("ToolBarUsed"));
            pd12.setShortDescription(bundleMessages.getString("setToolBarUsedTip"));
            pd12.setExpert(true);
*/
            PropertyDescriptor pd13 = new PropertyDescriptor(
                    "LocationPanelUsed", ESlateWebBrowser.class,
                    "isLocationPanelUsed", "setLocationPanelUsed"
                );

            pd13.setDisplayName(bundleMessages.getString("LocationPanelUsed"));
            pd13.setShortDescription(bundleMessages.getString("setLocationPanelUsedTip"));
            pd13.setExpert(true);

            PropertyDescriptor pd14 = new PropertyDescriptor(
                    "HtmlPaneBorder", ESlateWebBrowser.class,
                    "getHtmlPaneBorder", "setHtmlPaneBorder"
                );

            pd14.setDisplayName(bundleMessages.getString("HtmlPaneBorder"));
            pd14.setShortDescription(bundleMessages.getString("setHtmlPaneBorderTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor(
                    "Opaque", ESlateWebBrowser.class,
                    "isOpaque", "setOpaque"
                );

            pd15.setDisplayName(bundleMessages.getString("Opaque"));
            pd14.setShortDescription(bundleMessages.getString("setOpaqueTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd5, pd6, pd9, pd10, pd11, /*pd12,*/ pd13, pd14, pd15/*,pd7,pd9,pd10,pd11,pd12,pd13,pd14,pd15,
                 pd16,pd17,pd18,pd19,pd20,pd21,pd23,pd24,pd25,pd26,pd28*/};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
