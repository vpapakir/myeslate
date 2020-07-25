package gr.cti.eslate.mapModel;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class LayerViewBeanInfo extends SimpleBeanInfo {
    private ResourceBundle bundle;
    private ImageIcon mono16Icon = new ImageIcon(Map.class.getResource("images/mapModelBeanIcon.gif"));

    public LayerViewBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleMapBeanInfo", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor("name",LayerView.class);
            pd1.setDisplayName(bundle.getString("name"));
            pd1.setShortDescription(bundle.getString("nametip"));
            return new PropertyDescriptor[] {pd1};
        } catch (Throwable exc) {
            System.out.println("LAYERVIEW#200010241636: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }
}

