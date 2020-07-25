package gr.cti.eslate.database;

import java.beans.*;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Vector;
import java.lang.reflect.Method;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import gr.cti.eslate.event.ColumnListener;
import gr.cti.eslate.event.ColumnMovedEvent;
import javax.swing.ImageIcon;


public class DatabaseBeanInfo extends SimpleBeanInfo {
    ResourceBundle bundle;
    ImageIcon mono16Icon = new ImageIcon(getClass().getResource("images/DatabaseBeanIcon.gif"));

    public DatabaseBeanInfo() {
//        System.out.println("DatabaseBeanInfo constructor");
        bundle=ResourceBundle.getBundle("gr.cti.eslate.database.DatabaseBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "standardToolBarVisible",
                                            Database.class,
                                            "isStandardToolbarVisible",
                                            "setStandardToolbarVisible");
            pd1.setDisplayName(bundle.getString("StandardToolBarVisible"));
            pd1.setShortDescription(bundle.getString("StandardToolBarVisibleTip"));
            pd1.setValue("propertyCategory", bundle.getString("ToolBar"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                                            "formattingToolBarVisible",
                                            Database.class,
                                            "isFormattingToolBarVisible",
                                            "setFormattingToolBarVisible");
            pd2.setDisplayName(bundle.getString("FormattingToolBarVisible"));
            pd2.setShortDescription(bundle.getString("FormattingToolBarVisibleTip"));
            pd2.setValue("propertyCategory", bundle.getString("ToolBar"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                                            "userMode",
                                            Database.class);
            pd3.setDisplayName(bundle.getString("UserMode"));
            pd3.setShortDescription(bundle.getString("UserModeTip"));
//            pd3.setPreferred(true);
            pd3.setBound(true);
//            pd3.setCategory("My category");
            pd3.setPropertyEditorClass(TaggedUserModePropEditor.class);

            PropertyDescriptor pd4 = null;
            try{
                Method getter = Database.class.getMethod("getDBase", new Class[] {});
                pd4 = new PropertyDescriptor(
                                            "db",
                                            getter, null);
                pd4.setDisplayName(bundle.getString("LoadedDatabase"));
            }catch (Throwable thr) {
               System.out.println("Error while creating the property descriptor for the DBase nested object");
            }

            PropertyDescriptor pd5 = new PropertyDescriptor(
                                            "menuBarVisible",
                                            Database.class,
                                            "isMenuBarVisible",
                                            "setMenuBarVisible");
            pd5.setDisplayName(bundle.getString("MenuBarVisible"));
            pd5.setShortDescription(bundle.getString("MenuBarVisibleTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                                            "border",
                                            Database.class,
                                            "getBorder",
                                            "setBorder");
            pd6.setDisplayName(bundle.getString("Border"));
            pd6.setShortDescription(bundle.getString("BorderTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                                            "statusBarVisible",
                                            Database.class,
                                            "isStatusBarVisible",
                                            "setStatusBarVisible");
            pd7.setDisplayName(bundle.getString("StatusBarVisible"));
            pd7.setShortDescription(bundle.getString("StatusBarVisibleTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                                            "tabTransparent",
                                            Database.class,
                                            "isTabTransparent",
                                            "setTabTransparent");
            pd8.setDisplayName(bundle.getString("TransparentTab"));
            pd8.setShortDescription(bundle.getString("TransparentTabTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                                            "tableHeaderFont",
                                            Database.class

                                            );
            pd9.setDisplayName(bundle.getString("TableFont"));
            pd9.setShortDescription(bundle.getString("TableFontTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                                            "FieldsHeaderFont",
                                            Database.class
                                            );
            pd10.setDisplayName(bundle.getString("FieldsHeaderFont"));
            pd10.setShortDescription(bundle.getString("FieldsHeaderFontTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                                            "TableHeaderExpansionChangeAllowed",
                                            Database.class
                                            );
            pd11.setDisplayName(bundle.getString("TableHeaderExpansionChangeAllowed"));
            pd11.setShortDescription(bundle.getString("TableHeaderExpansionChangeAllowedTip"));


            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11};
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        Vector descriptors = new Vector();
/*            ExtendedEventSetDescriptor esd1 = new ExtendedEventSetDescriptor(
                  Database.class,
                  "propertyChange",
                  PropertyChangeListener.class,
                  "propertyChange",
                  bundle.getString("propertyChange"));
*/
            EventSetDescriptor esd = null;
            try{
                Method listenerMethod = PropertyChangeListener.class.getMethod("propertyChange", new Class[] {PropertyChangeEvent.class});
                Method addListenerMethod = Database.class.getMethod("addPropertyChangeListener", new Class[] {PropertyChangeListener.class});
                Method removelistenerMethod = Database.class.getMethod("removePropertyChangeListener", new Class[] {PropertyChangeListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("propertyChange"));
                esd = new EventSetDescriptor("propertyChange",
                                       PropertyChangeListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod = VetoableChangeListener.class.getMethod("vetoableChange", new Class[] {PropertyChangeEvent.class});
                Method addListenerMethod = Database.class.getMethod("addVetoableChangeListener", new Class[] {VetoableChangeListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeVetoableChangeListener", new Class[] {VetoableChangeListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("vetoableChange"));
                esd = new EventSetDescriptor("vetoableChange",
                                       VetoableChangeListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod1 = MouseListener.class.getMethod("mouseEntered", new Class[] {MouseEvent.class});
                MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
                md1.setDisplayName(bundle.getString("mouseEntered"));
                Method listenerMethod2 = MouseListener.class.getMethod("mouseExited", new Class[] {MouseEvent.class});
                MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
                md2.setDisplayName(bundle.getString("mouseExited"));
                Method addListenerMethod = Database.class.getMethod("addMouseListener", new Class[] {MouseListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeMouseListener", new Class[] {MouseListener.class});
                esd = new EventSetDescriptor("mouse",
                                       MouseListener.class,
                                       new MethodDescriptor[] {md1, md2},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod = MouseMotionListener.class.getMethod("mouseMoved", new Class[] {MouseEvent.class});
                Method addListenerMethod = Database.class.getMethod("addMouseMotionListener", new Class[] {MouseMotionListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeMouseMotionListener", new Class[] {MouseMotionListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("mouseMoved"));
                esd = new EventSetDescriptor("mouseMotion",
                                       MouseMotionListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod1 = ComponentListener.class.getMethod("componentHidden", new Class[] {ComponentEvent.class});
                MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
                md1.setDisplayName(bundle.getString("componentHidden"));
                Method listenerMethod2 = ComponentListener.class.getMethod("componentShown", new Class[] {ComponentEvent.class});
                MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
                md2.setDisplayName(bundle.getString("componentShown"));
                Method addListenerMethod = Database.class.getMethod("addComponentListener", new Class[] {ComponentListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeComponentListener", new Class[] {ComponentListener.class});
                esd = new EventSetDescriptor("component",
                                       ComponentListener.class,
                                       new MethodDescriptor[] {md1, md2},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod = ColumnListener.class.getMethod("columnMoved", new Class[] {ColumnMovedEvent.class});
                Method addListenerMethod = Database.class.getMethod("addColumnListener", new Class[] {ColumnListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeColumnListener", new Class[] {ColumnListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("columnMoved"));
                esd = new EventSetDescriptor("columnMoved",
                                       ColumnListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
            for (int i=0; i<d.length; i++)
                d[i] = (EventSetDescriptor) descriptors.elementAt(i);
            return d;
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }

/*    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(Database.class, DatabaseCustomizer.class);
    }
*/

/*    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = Button.class.getSuperclass();
        try{
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] {superBeanInfo};
        }catch (IntrospectionException exc) {
            return null;
        }
    }
*/
}


