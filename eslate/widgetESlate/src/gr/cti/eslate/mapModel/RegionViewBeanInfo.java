package gr.cti.eslate.mapModel;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class RegionViewBeanInfo extends SimpleBeanInfo {
    private ResourceBundle bundle;
    private ImageIcon mono16Icon = new ImageIcon(Map.class.getResource("images/mapModelBeanIcon.gif"));

    public RegionViewBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleMapBeanInfo", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor("name",RegionView.class);
            pd1.setDisplayName(bundle.getString("name"));
            pd1.setShortDescription(bundle.getString("nametip"));

            IndexedPropertyDescriptor pd2=null;
            Method getter = RegionView.class.getMethod("getChildRegionViews", new Class[]{});
            Method indexedGetter = RegionView.class.getMethod("getChildRegionView", new Class[]{int.class});
            /* Indexed property descriptors have to at least specify the 'indexedGetter'
             * method in order for the Instrospector to be able to use them.
             */
            pd2=new IndexedPropertyDescriptor("childRegionViews",getter,null,indexedGetter,null);

            IndexedPropertyDescriptor pd3=null;
            getter = RegionView.class.getMethod("getLayerViews", new Class[]{});
            indexedGetter = RegionView.class.getMethod("getLayerView", new Class[]{int.class});
            /* Indexed property descriptors have to at least specify the 'indexedGetter'
             * method in order for the Instrospector to be able to use them.
             */
            pd3=new IndexedPropertyDescriptor("LayerViews",getter,null,indexedGetter,null);

            return new PropertyDescriptor[] {pd1,pd2,pd3};
        } catch (Throwable exc) {
            System.out.println("REGIONVIEW#200010241619: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }
}

