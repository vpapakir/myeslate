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
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ESlatePopupMenuBeanInfo extends ESlateBeanInfo {
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("Images/JMenuBarColor16n.gif"));
    private ResourceBundle bundleMessages;

    /**
     * Construct the BeanInfo.
     */
    public ESlatePopupMenuBeanInfo() {
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
            Method popupMenuWillBecomeInvisibleMethod = PopupMenuListener.class.getMethod(
                    "popupMenuWillBecomeInvisible", new Class[] {PopupMenuEvent.class}
                );
            Method addListenerMethod = ESlatePopupMenu.class.getMethod(
                    "addPopupMenuListener", new Class[] {PopupMenuListener.class}
                );
            Method removelistenerMethod = ESlatePopupMenu.class.getMethod(
                    "removePopupMenuListener",
                    new Class[] {PopupMenuListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(popupMenuWillBecomeInvisibleMethod);

            md.setDisplayName("PopupMenuWillBecomeInvisible");

            esd = new EventSetDescriptor(
                        "popupMenuWillBecomeInvisible", PopupMenuListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );

            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method popupMenuWillBecomeVisibleMethod = PopupMenuListener.class.getMethod(
                    "popupMenuWillBecomeVisible", new Class[] {PopupMenuEvent.class}
                );
            Method addListenerMethod = ESlatePopupMenu.class.getMethod(
                    "addPopupMenuListener", new Class[] {PopupMenuListener.class}
                );
            Method removelistenerMethod = ESlatePopupMenu.class.getMethod(
                    "removePopupMenuListener",
                    new Class[] {PopupMenuListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(popupMenuWillBecomeVisibleMethod);

            md.setDisplayName("PopupMenuWillBecomeVisible");

            esd = new EventSetDescriptor(
                        "popupMenuWillBecomeVisible", PopupMenuListener.class, new MethodDescriptor[] {md},
                        addListenerMethod, removelistenerMethod
                    );

            descriptors.addElement(esd);

        } catch (IntrospectionException exc) {} catch (NoSuchMethodException exc) {} catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method popupMenuCanceledMethod = PopupMenuListener.class.getMethod(
                    "popupMenuCanceled", new Class[] {PopupMenuEvent.class}
                );
            Method addListenerMethod = ESlatePopupMenu.class.getMethod(
                    "addPopupMenuListener", new Class[] {PopupMenuListener.class}
                );
            Method removelistenerMethod = ESlatePopupMenu.class.getMethod(
                    "removePopupMenuListener",
                    new Class[] {PopupMenuListener.class}
                );
            MethodDescriptor md = new MethodDescriptor(popupMenuCanceledMethod);

            md.setDisplayName("PopupMenuCanceled");

            esd = new EventSetDescriptor(
                        "popupMenuCanceled", PopupMenuListener.class, new MethodDescriptor[] {md},
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
                    "Menus", ESlatePopupMenu.class,
                    "getMenuStructure", "setMenuStructure"
                );

            pd0.setDisplayName("Popup Menu Editor");
            pd0.setPropertyEditorClass(PopupMenuPropertyEditor.class);
            pd0.setShortDescription(bundleMessages.getString("setMenusTip"));

          return new PropertyDescriptor[] {pd0};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
