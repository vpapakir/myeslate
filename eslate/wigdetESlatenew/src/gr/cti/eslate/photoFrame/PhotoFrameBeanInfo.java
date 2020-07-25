package gr.cti.eslate.photoFrame;


import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;


public class PhotoFrameBeanInfo extends ESlateBeanInfo {
    public PhotoFrameBeanInfo() {
        super();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pd1 = new PropertyDescriptor("background", PhotoFrame.class);

            pd1.setDisplayName(bundle.getString("background"));
            pd1.setShortDescription(bundle.getString("backgroundtip"));

            PropertyDescriptor pd2 = new PropertyDescriptor("border", PhotoFrame.class);

            pd2.setDisplayName(bundle.getString("border"));
            pd2.setShortDescription(bundle.getString("bordertip"));

            PropertyDescriptor pd3 = new PropertyDescriptor("opaque", PhotoFrame.class);

            pd3.setDisplayName(bundle.getString("opaque"));
            pd3.setShortDescription(bundle.getString("opaquetip"));

            PropertyDescriptor pd4 = new PropertyDescriptor("automaticallyFitImage", PhotoFrame.class);

            pd4.setDisplayName(bundle.getString("automaticallyFitImage"));
            pd4.setShortDescription(bundle.getString("automaticallyFitImagetip"));

            PropertyDescriptor pd5 = new PropertyDescriptor("stretchImage", PhotoFrame.class);

            pd5.setDisplayName(bundle.getString("stretchImage"));
            pd5.setShortDescription(bundle.getString("stretchImagetip"));
            
            PropertyDescriptor pd6 = new PropertyDescriptor("filename", PhotoFrame.class,
                    "getFilename", "setFilename"
            );

            pd6.setDisplayName(bundle.getString("filename"));
            pd6.setShortDescription(bundle.getString("filenametip"));
            
            PropertyDescriptor pd7 = new PropertyDescriptor("nullImageIcon", PhotoFrame.class,
                    "getNullImageIcon", "setNullImageIcon"
            );

            pd7.setDisplayName(bundle.getString("nullImage"));
            pd7.setShortDescription(bundle.getString("nullImageTip"));



            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7};
        } catch (IntrospectionException exc) {
            System.out.println("PHOTOFRAME#200010101418: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }

    static ResourceBundle bundle;
    private ImageIcon mono16Icon = new ImageIcon(getClass().getResource("images/photoFrameBeanIcon.gif")); {
        bundle = ResourceBundle.getBundle("gr.cti.eslate.photoFrame.BundleBeanInfo", Locale.getDefault());
    }

}
