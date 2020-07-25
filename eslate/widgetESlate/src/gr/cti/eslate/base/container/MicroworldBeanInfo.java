package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.event.MicroworldComponentEvent;
import gr.cti.eslate.base.container.event.MicroworldEvent;
import gr.cti.eslate.base.container.event.MicroworldListener;
import gr.cti.eslate.base.container.event.MicroworldViewEvent;

import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;


public class MicroworldBeanInfo extends SimpleBeanInfo {
    ResourceBundle bundle;

    public MicroworldBeanInfo() {
//        System.out.println("MicroworldBeanInfo constructor");
        bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.MicroworldBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "microworldLAFClassName",
                                            Microworld.class,
                                            "getMicroworldLAFClassName",
                                            "setMicroworldLAFClassName");
            pd1.setDisplayName(bundle.getString("MicroworldLAFClassName"));
            pd1.setShortDescription(bundle.getString("MicroworldLAFClassNameTip"));
            pd1.setPropertyEditorClass(gr.cti.eslate.propertyEditors.StringPropertyEditor2.class);

/*            PropertyDescriptor pd2 = new PropertyDescriptor(
                                            "storeSkinOnAPerViewBasis",
                                            Microworld.class,
                                            "isStoreSkinOnAPerViewBasis",
                                            "setStoreSkinOnAPerViewBasis");
            pd2.setDisplayName(bundle.getString("StoreSkinOnAPerViewBasis"));
            pd2.setShortDescription(bundle.getString("StoreSkinOnAPerViewBasisTip"));
*/
            PropertyDescriptor pd3 = new PropertyDescriptor(
                                            "mwdLoadProgressMsg",
                                            Microworld.class,
                                            "getMwdLoadProgressMsg",
                                            "setMwdLoadProgressMsg");
            pd3.setDisplayName(bundle.getString("MwdLoadProgressMsg"));
            pd3.setShortDescription(bundle.getString("MwdLoadProgressMsgTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                                            "mwdLoadProgressInfoDisplayed",
                                            Microworld.class,
                                            "isMwdLoadProgressInfoDisplayed",
                                            "setMwdLoadProgressInfoDisplayed");
            pd4.setDisplayName(bundle.getString("MwdLoadProgressInfoDisplayed"));
            pd4.setShortDescription(bundle.getString("MwdLoadProgressInfoDisplayedTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                                            "mwdSaveProgressMsg",
                                            Microworld.class,
                                            "getMwdSaveProgressMsg",
                                            "setMwdSaveProgressMsg");
            pd5.setDisplayName(bundle.getString("MwdSaveProgressMsg"));
            pd5.setShortDescription(bundle.getString("MwdSaveProgressMsgTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                                            "mwdSaveProgressInfoDisplayed",
                                            Microworld.class,
                                            "isMwdSaveProgressInfoDisplayed",
                                            "setMwdSaveProgressInfoDisplayed");
            pd6.setDisplayName(bundle.getString("MwdSaveProgressInfoDisplayed"));
            pd6.setShortDescription(bundle.getString("MwdSaveProgressInfoDisplayedTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                                            "progressDialogTitleFont",
                                            Microworld.class,
                                            "getProgressDialogTitleFont",
                                            "setProgressDialogTitleFont");
            pd7.setDisplayName(bundle.getString("ProgressDialogTitleFont"));
            pd7.setShortDescription(bundle.getString("ProgressDialogTitleFontTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                                            "progressDialogTitleColor",
                                            Microworld.class,
                                            "getProgressDialogTitleColor",
                                            "setProgressDialogTitleColor");
            pd8.setDisplayName(bundle.getString("ProgressDialogTitleColor"));
            pd8.setShortDescription(bundle.getString("ProgressDialogTitleColorTip"));

            return new PropertyDescriptor[] {pd1, pd3, pd4, pd5, pd6, pd7, pd8};
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        Vector descriptors = new Vector();
        Method addListenerMethod = null;
        Method removelistenerMethod = null;
        try{
            addListenerMethod = Microworld.class.getMethod("addMicroworldListener", new Class[] {MicroworldListener.class});
            removelistenerMethod = Microworld.class.getMethod("removeMicroworldListener", new Class[] {MicroworldListener.class});
        }catch (Throwable thr) {
            return new EventSetDescriptor[0];
        }

        EventSetDescriptor esd = null;
        try{
            Method listenerMethod = MicroworldListener.class.getMethod("microworldLoaded", new Class[] {MicroworldEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("microworldLoaded"));
            esd = new EventSetDescriptor("microworldLoaded",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("microworldClosing", new Class[] {MicroworldEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("microworldClosing"));
            esd = new EventSetDescriptor("microworldClosing",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentActivated", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentActivated"));
            esd = new EventSetDescriptor("componentActivated",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentDeactivated", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentDeactivated"));
            esd = new EventSetDescriptor("componentDeactivated",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentIconified", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentIconified"));
            esd = new EventSetDescriptor("componentIconified",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentRestored", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentRestored"));
            esd = new EventSetDescriptor("componentRestored",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentMaximized", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentMaximized"));
            esd = new EventSetDescriptor("componentMaximized",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentClosed", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentClosed"));
            esd = new EventSetDescriptor("componentClosed",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("componentClosing", new Class[] {MicroworldComponentEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("componentClosing"));
            esd = new EventSetDescriptor("componentClosing",
                                   MicroworldListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = MicroworldListener.class.getMethod("activeViewChanged", new Class[] {MicroworldViewEvent.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("activeViewChanged"));
            esd = new EventSetDescriptor("activeViewChanged",
                                   MicroworldListener.class,
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
}