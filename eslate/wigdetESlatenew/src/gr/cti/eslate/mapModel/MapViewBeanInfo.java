package gr.cti.eslate.mapModel;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class MapViewBeanInfo extends SimpleBeanInfo {
    private ResourceBundle bundle;
    private ImageIcon mono16Icon = new ImageIcon(Map.class.getResource("images/mapModelBeanIcon.gif"));

    public MapViewBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleMapBeanInfo", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor("name",MapView.class);
            pd1.setDisplayName(bundle.getString("name"));
            pd1.setShortDescription(bundle.getString("nametip"));

            PropertyDescriptor pd2 = null;
            try{
                Method getter = MapView.class.getMethod("getMapRoot", new Class[] {});
                pd2 = new PropertyDescriptor("mapRoot",getter, null);
                pd2.setDisplayName(bundle.getString("root"));
            } catch (Throwable thr) {}

            return new PropertyDescriptor[] {pd1,pd2};
        } catch (IntrospectionException exc) {
            System.out.println("MAPVIEW#200010241511: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }
}
