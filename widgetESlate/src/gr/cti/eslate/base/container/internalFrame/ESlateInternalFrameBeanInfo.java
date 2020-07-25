package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The BeanInfo for the ESlateInternalFrame class.
 * @author  Giorgos Vasiliou
 * @version 1.0
 */

public class ESlateInternalFrameBeanInfo extends ESlateBeanInfo {

    public ESlateInternalFrameBeanInfo() {
        super();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor("fooLayoutCustomizer",ESlateInternalFrame.class);
            pd1.setDisplayName(bundle.getString("fooLayoutCustomizer"));
            pd1.setShortDescription(bundle.getString("fooLayoutCustomizertip"));
            pd1.setPropertyEditorClass(ESlateInternalFrameLayoutCustomizerEditor.class);

            PropertyDescriptor pd2 = new PropertyDescriptor("northVisible",ESlateInternalFrame.class);
            pd2.setDisplayName(bundle.getString("northVisible"));

            PropertyDescriptor pd3 = new PropertyDescriptor("southVisible",ESlateInternalFrame.class);
            pd3.setDisplayName(bundle.getString("southVisible"));

            PropertyDescriptor pd4 = new PropertyDescriptor("eastVisible",ESlateInternalFrame.class);
            pd4.setDisplayName(bundle.getString("eastVisible"));

            PropertyDescriptor pd5 = new PropertyDescriptor("westVisible",ESlateInternalFrame.class);
            pd5.setDisplayName(bundle.getString("westVisible"));

            PropertyDescriptor pd6 = new PropertyDescriptor("draggableFromNorth",ESlateInternalFrame.class);
            pd6.setDisplayName(bundle.getString("draggableFromNorth"));

            PropertyDescriptor pd7 = new PropertyDescriptor("draggableFromSouth",ESlateInternalFrame.class);
            pd7.setDisplayName(bundle.getString("draggableFromSouth"));

            PropertyDescriptor pd8 = new PropertyDescriptor("draggableFromEast",ESlateInternalFrame.class);
            pd8.setDisplayName(bundle.getString("draggableFromEast"));

            PropertyDescriptor pd9 = new PropertyDescriptor("draggableFromWest",ESlateInternalFrame.class);
            pd9.setDisplayName(bundle.getString("draggableFromWest"));

            PropertyDescriptor pd10 = new PropertyDescriptor("helpButtonVisible",ESlateInternalFrame.class);
            pd10.setDisplayName(bundle.getString("helpButtonVisible"));

            PropertyDescriptor pd11 = new PropertyDescriptor("infoButtonVisible",ESlateInternalFrame.class);
            pd11.setDisplayName(bundle.getString("infoButtonVisible"));

            PropertyDescriptor pd12 = new PropertyDescriptor("plugButtonVisible",ESlateInternalFrame.class);
            pd12.setDisplayName(bundle.getString("plugButtonVisible"));

            PropertyDescriptor pd13 = new PropertyDescriptor("maxSizeRespected",ESlateInternalFrame.class);
            pd13.setDisplayName(bundle.getString("maxSizeRespected"));
            pd13.setShortDescription(bundle.getString("maxSizeRespectedTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor("minSizeRespected",ESlateInternalFrame.class);
            pd14.setDisplayName(bundle.getString("minSizeRespected"));
            pd14.setShortDescription(bundle.getString("minSizeRespectedTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor("modal",ESlateInternalFrame.class);
            pd15.setDisplayName(bundle.getString("modal"));
            pd15.setShortDescription(bundle.getString("modalTip"));

/*            PropertyDescriptor pd16 = new PropertyDescriptor("borderActivationInsets",ESlateInternalFrame.class);
            pd16.setDisplayName(bundle.getString("borderActivationInsets"));
            pd16.setShortDescription(bundle.getString("borderActivationInsetsTip"));
*/
            /*PropertyDescriptor pd = new PropertyDescriptor("",ESlateInternalFrame.class);
            pd.setDisplayName(bundle.getString(""));
            pd.setShortDescription(bundle.getString(""));*/

            PropertyDescriptor pd101,pd102,pd103,pd104;
            try {
                Method getter1 = ESlateInternalFrame.class.getMethod("getNorth", new Class[] {});
                pd101 = new PropertyDescriptor("nh", getter1, null);
                pd101.setDisplayName(bundle.getString("north"));

                Method getter2 = ESlateInternalFrame.class.getMethod("getSouth", new Class[] {});
                pd102 = new PropertyDescriptor("sh", getter2, null);
                pd102.setDisplayName(bundle.getString("south"));

                Method getter3 = ESlateInternalFrame.class.getMethod("getEast", new Class[] {});
                pd103 = new PropertyDescriptor("et", getter3, null);
                pd103.setDisplayName(bundle.getString("east"));

                Method getter4 = ESlateInternalFrame.class.getMethod("getWest", new Class[] {});
                pd104 = new PropertyDescriptor("wt", getter4, null);
                pd104.setDisplayName(bundle.getString("west"));
            } catch(NoSuchMethodException e) {
                System.err.println("ESLATEINTERNALFRAME#200101031757: Cannot show all skin panels as objects");
                return null;
            }

            PropertyDescriptor pd16 = new PropertyDescriptor("effect", ESlateInternalFrame.class,
                                                            "getEffect", "setEffect");
            pd16.setDisplayName(bundle.getString("setEffect"));
            pd16.setShortDescription(bundle.getString("setEffectTip"));
            pd16.setPropertyEditorClass(gr.cti.eslate.base.container.internalFrame.EffectsPropertyEditor.class);

            PropertyDescriptor pd17 = new PropertyDescriptor("clipShapeType", ESlateInternalFrame.class,
                                                            "getClipShapeType", "setClipShapeType");
            pd17.setDisplayName(bundle.getString("clipShapeType"));
            pd17.setShortDescription(bundle.getString("clipShapeTypeTip"));
            pd17.setPropertyEditorClass(gr.cti.eslate.shapedComponent.ShapeTypeEditor.class);

            return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11,pd12,pd13,pd14, pd15, pd101,pd102,pd103,pd104, pd16, pd17};
        } catch (IntrospectionException exc) {
            System.err.println("ESLATEINTERNALFRAME#200101031242: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public Image getIcon(int iconKind) {
        //if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
        //    return icon.getImage();
        return null;
    }

    //private ImageIcon icon = new ImageIcon(getClass().getResource("images/ESlateInternalFrameBeanIcon.gif"));
    static ResourceBundle bundle;

    {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameResourceBundle", Locale.getDefault());
    }
}