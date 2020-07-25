package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

public class ZoomSliderBeanInfo extends ESlateBeanInfo {
	private ResourceBundle bundle;

	public ZoomSliderBeanInfo() {
		bundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.JToggleToolBeanInfoBundle", Locale.getDefault());
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try{
			//Value
			PropertyDescriptor pd1 = new PropertyDescriptor("value",ZoomSlider.class);
			pd1.setDisplayName(bundle.getString("value"));
			pd1.setShortDescription(bundle.getString("valueTip"));

			//Tooltip
			PropertyDescriptor pd5 = new PropertyDescriptor("toolTipText",ZoomSlider.class);
			pd5.setDisplayName(bundle.getString("toolTipText"));
			pd5.setShortDescription(bundle.getString("toolTipTextTip"));

			//Opaque
			PropertyDescriptor pd7 = new PropertyDescriptor("opaque",ZoomSlider.class);
			pd7.setDisplayName(bundle.getString("opaque"));
			pd7.setShortDescription(bundle.getString("opaqueTip"));

			//Visible
			PropertyDescriptor pd8 = new PropertyDescriptor("visible",ZoomSlider.class);
			pd8.setDisplayName(bundle.getString("visible"));
			pd8.setShortDescription(bundle.getString("visibleTip"));

			//Drop down visible
			PropertyDescriptor pd9 = new PropertyDescriptor("dropDownButtonVisible",ZoomSlider.class);
			pd9.setDisplayName(bundle.getString("dropDownButtonVisible"));
			pd9.setShortDescription(bundle.getString("dropDownButtonVisibleTip"));

			return new PropertyDescriptor[] {pd1,pd5,pd7,pd8,pd9};
		} catch (IntrospectionException exc) {
			System.out.println("MAPVIEWER#200008031658: IntrospectionException: " + exc.getMessage());
			return null;
		}
	}
}
