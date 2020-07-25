package gr.cti.eslate.mapModel;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class MapBeanInfo extends ESlateBeanInfo {
	private ResourceBundle bundle;
	private ImageIcon mono16Icon = new ImageIcon(Map.class.getResource("images/mapModelBeanIcon.gif"));

	public MapBeanInfo() {
		bundle=ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleMapBeanInfo", Locale.getDefault());
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try{
			PropertyDescriptor pd1 = new PropertyDescriptor("name",Map.class);
			pd1.setDisplayName(bundle.getString("name"));
			pd1.setShortDescription(bundle.getString("nametip"));

			PropertyDescriptor pd2 = new PropertyDescriptor("menubarVisible",Map.class);
			pd2.setDisplayName(bundle.getString("menubarVisible"));
			pd2.setShortDescription(bundle.getString("menubarVisibletip"));

			PropertyDescriptor pd3 = new PropertyDescriptor("border",Map.class);
			pd3.setDisplayName(bundle.getString("border"));
			pd3.setShortDescription(bundle.getString("bordertip"));

			IndexedPropertyDescriptor pd5=null;
			try {
				Method getter = Map.class.getMethod("getMapViews", new Class[]{});
				Method indexedGetter = Map.class.getMethod("getMapView", new Class[]{int.class});
				/* Indexed property descriptors have to at least specify the 'indexedGetter'
				 * method in order for the Instrospector to be able to use them.
				 */
				pd5=new IndexedPropertyDescriptor("MapViews",getter,null,indexedGetter,null);
			} catch (Throwable thr) {
			}
			return new PropertyDescriptor[] {pd1,pd2,pd3,pd5};
		} catch (IntrospectionException exc) {
			System.out.println("MAP#200004051949: IntrospectionException: " + exc.getMessage());
			return null;
		}/* catch (NoSuchMethodException exc1) {
			System.out.println("MAP#200010101749: IntrospectionException: " + exc1.getMessage());
			return null;
		}*/
	}
/*            pd1.setDisplayName(bundle.getString("StandardToolBarVisible"));
			pd1.setShortDescription(bundle.getString("StandardToolBarVisibleTip"));
			pd1.setValue("propertyCategory", bundle.getString("ToolBar"));

			PropertyDescriptor pd2 = new PropertyDescriptor(
											"formattingToolBarVisible",
											Database.class,
											"isFormattingToolBarVisible",
											"setFormattingToolBarVisible");
			pd2.setDisplayName(bundle.getString("FormattingToolBarVisible"));
			pd2.setShortDescription(bundle.getString("FormattingToolBarVisibleTip"));
			pd2.setValue("propertyCategory", bundle.getString("ToolBar"));

			PropertyDescriptor pd3 = new PropertyDescriptor(
											"userMode",
											Database.class);
			pd3.setDisplayName(bundle.getString("UserMode"));
			pd3.setShortDescription(bundle.getString("UserModeTip"));
//            pd3.setPreferred(true);
			pd3.setBound(true);
//            pd3.setCategory("My category");
			pd3.setPropertyEditorClass(TaggedUserModePropEditor.class);

			return new PropertyDescriptor[] {pd1, pd2, pd3};
		}catch (IntrospectionException exc) {
			System.out.println("IntrospectionException: " + exc.getMessage());
			return null;
		}
	}*/

	public Image getIcon(int iconKind) {
		if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
			return mono16Icon.getImage();
		return null;
	}
}