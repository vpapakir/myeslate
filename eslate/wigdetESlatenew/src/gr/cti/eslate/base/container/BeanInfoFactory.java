package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.BooleanWrapper;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A wrapper for the Introspector to generate and return instances
 * of BeanInfos. Creating a BeanInfo with the Introspector is a
 * really expensive operation. Since a BeanInfo is immutable for
 * each class, it makes sense to share instances of BeanInfo's
 * throughout the application.
 *
 * @version 1.1 11/09/99
 * @author  Mark Davidson
 */
public class BeanInfoFactory  {

    private static Hashtable infos = new Hashtable();
    private static Hashtable icons_16x16 = new Hashtable();
    private static Hashtable icons_32x32 = new Hashtable();
    /* The names fof the classes which have been checked for a BeanInfo icon */
    private static Vector checkedClasses = new Vector();

    /**
     * Retrieves the BeanInfo for a Class
     */
    public static BeanInfo getBeanInfo(Class cls)  {
        BeanInfo beanInfo = (BeanInfo)infos.get(cls);
//        System.out.println("getBeanInfo(): " + cls.getName());
        if (beanInfo == null)  {
            try {
//                System.out.println("Trying to get BeanInfo for class " + cls);
                beanInfo = Introspector.getBeanInfo(cls);
//                System.out.println("Got beaninfo " + beanInfo);
                infos.put(cls, beanInfo);
            } catch (Throwable thr) {
                // XXX - should handle this better.
                System.out.println("Error while getting the BeanInfo of class: " + cls);
                thr.printStackTrace();
            }
        }
        return beanInfo;
    }

    private static BeanInfo createTmpBeanInfo(Class cls) {
        String beanInfoClassName = cls.getName() + "BeanInfo";
//        System.out.println("createTmpBeanInfo: " + cls.getName());
        try{
            Class beanInfoClass = Class.forName(beanInfoClassName);
            if (beanInfoClass == null) return null;
            return (BeanInfo) beanInfoClass.newInstance();
        }catch (Throwable thr) {
            System.out.println("Can't getIcon for class" + beanInfoClassName);
            thr.printStackTrace();
            return null;
        }
    }

    private static BeanInfo createTmpBeanInfo(String beanClassName) {
        try{
            Class beanClass = Class.forName(beanClassName);
            return createTmpBeanInfo(beanClass);
        }catch (Throwable exc) {
            return null;
        }
    }

    public static Image get16x16BeanIcon(Class cls) {
        return getBeanIcon(cls, null, true);
/*        if (checkedClasses.contains(cls))
            return (Image) icons_16x16.get(cls);

        BeanInfo info = BeanInfoFactory.createTmpBeanInfo(cls);
        if (info == null) return null;

//        System.out.println("Looking for the icon of " + cls.getName());
        try{
            Image beanIcon = info.getIcon(BeanInfo.ICON_COLOR_16x16);
            if (beanIcon == null)
                beanIcon = info.getIcon(BeanInfo.ICON_MONO_16x16);
            icons_16x16.put(cls, beanIcon);
            checkedClasses.addElement(cls);
            return beanIcon;
        }catch (Throwable thr) {
//            System.out.println("Can't getIcon for class" + beanInfoClassName);
            return null;
        }
*/
    }

    public static Image get16x16BeanIcon(Class cls, BooleanWrapper iconExisted) {
        return getBeanIcon(cls, iconExisted, true);
    }

    public static Image get32x32BeanIcon(Class cls) {
        return getBeanIcon(cls, null, false);
    }

    public static Image get32x32BeanIcon(Class cls, BooleanWrapper iconExisted) {
        return getBeanIcon(cls, iconExisted, false);
    }

    public static Image getBeanIcon(Class cls, BooleanWrapper iconExisted, boolean smallIconDimension) {
        if (checkedClasses.contains(cls)) {
            Image img = null;
            if (smallIconDimension)
                img = (Image) icons_16x16.get(cls);
            else
                img = (Image) icons_32x32.get(cls);
            if (img != null) {
                if (iconExisted != null)
                    iconExisted.setValue(true);
                return img;
            }
        }

        if (iconExisted != null)
            iconExisted.setValue(false);
        BeanInfo info = BeanInfoFactory.createTmpBeanInfo(cls);
        if (info == null) return null;

//        System.out.println("Looking for the icon of " + cls.getName());
        try{
            Image beanIcon = null;
            if (smallIconDimension) {
                beanIcon = info.getIcon(BeanInfo.ICON_COLOR_16x16);
                if (beanIcon == null)
                    beanIcon = info.getIcon(BeanInfo.ICON_MONO_16x16);
                icons_16x16.put(cls, beanIcon);
            }else{
                beanIcon = info.getIcon(BeanInfo.ICON_COLOR_32x32);
                if (beanIcon == null)
                    beanIcon = info.getIcon(BeanInfo.ICON_MONO_32x32);
                icons_32x32.put(cls, beanIcon);
            }
            checkedClasses.addElement(cls);
            return beanIcon;
        }catch (Throwable thr) {
//            System.out.println("Can't getIcon for class" + beanInfoClassName);
            return null;
        }
    }
}
