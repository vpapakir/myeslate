package gr.cti.eslate.base.container.internalFrame;

import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The BeanInfo class of SkinPane.
 * <p>
 * @author  Giorgos Vasiliou
 * @version 2.0, 23-Apr-2001
 */
public class SkinPaneBeanInfo extends gr.cti.eslate.panel.PanelComponentBeanInfo {

    public SkinPaneBeanInfo() {
        super();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor pd1=null;
        PropertyDescriptor pd2=null;

        try {
            pd1 = new PropertyDescriptor("customSize",SkinPane.class);
            pd1.setDisplayName(bundle.getString("customSize"));
            pd1.setShortDescription(bundle.getString("customSizeTip"));
            pd1.setPropertyEditorClass(gr.cti.eslate.base.container.EditorPanelSize.class);
            pd2 = new PropertyDescriptor("useCustomSize",SkinPane.class);
            pd2.setDisplayName(bundle.getString("useCustomSize"));
            pd2.setShortDescription(bundle.getString("useCustomSizeTip"));
        } catch(Exception exc) {
            exc.printStackTrace();
            return super.getPropertyDescriptors();
        }

        //Combine my event descriptors with my parent's descriptors.
        PropertyDescriptor[] par=super.getPropertyDescriptors();
        PropertyDescriptor[] my=new PropertyDescriptor[par.length+2];

        System.arraycopy(par,0,my,0,par.length);
        my[my.length-2]=pd1;
        my[my.length-1]=pd2;

        return my;
    }

    protected static ResourceBundle bundle;

    {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameResourceBundle", Locale.getDefault());
    }
}