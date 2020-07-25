package gr.cti.eslate.mapViewer;

import gr.cti.eslate.eslateToggleButton.ESlateToggleButtonBeanInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ToggleButton bean info.
 */
public class MapViewerToggleButtonBeanInfo extends ESlateToggleButtonBeanInfo {
    private ResourceBundle bundle;

    public MapViewerToggleButtonBeanInfo() {
        super();
        bundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.JToggleToolBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            //Help
            PropertyDescriptor pd1=new PropertyDescriptor("helpText",MapViewerToggleButton.class,"getHelpText","setHelpText");
            pd1.setDisplayName(bundle.getString("helpText"));
            pd1.setShortDescription(bundle.getString("helpTextTip"));
            //Visible
            PropertyDescriptor pd2=new PropertyDescriptor("visible",MapViewerButton.class,"isVisible","setVisible");
            pd2.setDisplayName(bundle.getString("visible"));
            pd2.setShortDescription(bundle.getString("visibleTip"));

            //Combine my property descriptors with my parent's descriptors.
            PropertyDescriptor[] par=super.getPropertyDescriptors();
            PropertyDescriptor[] my=new PropertyDescriptor[par.length+2];

            System.arraycopy(par,0,my,0,par.length);
            my[my.length-2]=pd1;
            my[my.length-1]=pd2;

            return my;
        } catch (IntrospectionException exc) {
            System.out.println("MAPVIEWER#2002040419: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

}