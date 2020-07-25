package gr.cti.eslate.database.engine;

import java.beans.SimpleBeanInfo;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.ResourceBundle;
import java.util.Locale;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import gr.cti.eslate.database.engine.event.*;


public class DBaseBeanInfo extends SimpleBeanInfo {
    ResourceBundle bundle;
    ImageIcon mono16Icon = new ImageIcon(getClass().getResource("images/dbase16x16.gif"));
    ImageIcon mono32Icon = new ImageIcon(getClass().getResource("images/dbase32x32.gif"));

    public DBaseBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.database.engine.DBaseBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "title",
                                            DBase.class,
                                            "getTitle",
                                            "setTitle");
            pd1.setDisplayName(bundle.getString("Title"));
            pd1.setShortDescription(bundle.getString("TitleTip"));
            return new PropertyDescriptor[] {pd1};
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        ArrayList descriptors = new ArrayList();

        EventSetDescriptor esd = null;
        try{
            Method listenerMethod = PropertyChangeListener.class.getMethod("propertyChange", new Class[] {PropertyChangeEvent.class});
            Method addListenerMethod = DBase.class.getMethod("addPropertyChangeListener", new Class[] {PropertyChangeListener.class});
            Method removelistenerMethod = DBase.class.getMethod("removePropertyChangeListener", new Class[] {PropertyChangeListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(gr.cti.eslate.utils.ESlateBeanResource.getString("propertyChange"));
            esd = new EventSetDescriptor("propertyChange",
                                   PropertyChangeListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseListener.class.getMethod("activeTableChanged", new Class[] {ActiveTableChangedEvent.class});
            Method addListenerMethod = DBase.class.getMethod("addDatabaseListener", new Class[] {DatabaseListener.class});
            Method removelistenerMethod = DBase.class.getMethod("removeDatabaseListener", new Class[] {DatabaseListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ActiveTableChanged"));
            esd = new EventSetDescriptor("activeTableChanged",
                                   DatabaseListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseListener.class.getMethod("databaseRenamed", new Class[] {DatabaseRenamedEvent.class});
            Method addListenerMethod = DBase.class.getMethod("addDatabaseListener", new Class[] {DatabaseListener.class});
            Method removelistenerMethod = DBase.class.getMethod("removeDatabaseListener", new Class[] {DatabaseListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("DatabaseRenamed"));
            esd = new EventSetDescriptor("databaseRenamed",
                                   DatabaseListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseListener.class.getMethod("tableAdded", new Class[] {TableAddedEvent.class});
            Method addListenerMethod = DBase.class.getMethod("addDatabaseListener", new Class[] {DatabaseListener.class});
            Method removelistenerMethod = DBase.class.getMethod("removeDatabaseListener", new Class[] {DatabaseListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("TableAdded"));
            esd = new EventSetDescriptor("tableAdded",
                                   DatabaseListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseListener.class.getMethod("tableRemoved", new Class[] {TableRemovedEvent.class});
            Method addListenerMethod = DBase.class.getMethod("addDatabaseListener", new Class[] {DatabaseListener.class});
            Method removelistenerMethod = DBase.class.getMethod("removeDatabaseListener", new Class[] {DatabaseListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("TableRemoved"));
            esd = new EventSetDescriptor("tableRemoved",
                                   DatabaseListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseListener.class.getMethod("tableReplaced", new Class[] {TableReplacedEvent.class});
            Method addListenerMethod = DBase.class.getMethod("addDatabaseListener", new Class[] {DatabaseListener.class});
            Method removelistenerMethod = DBase.class.getMethod("removeDatabaseListener", new Class[] {DatabaseListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("TableReplaced"));
            esd = new EventSetDescriptor("tableReplaced",
                                   DatabaseListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
        for (int i=0; i<d.length; i++)
            d[i] = (EventSetDescriptor) descriptors.get(i);
        return d;
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        else if (iconKind == BeanInfo.ICON_MONO_32x32 || iconKind == BeanInfo.ICON_COLOR_32x32)
            return mono32Icon.getImage();
        return null;
    }

}
