package gr.cti.eslate.imageEditor;

import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;

public class ImageEditorBeanInfo extends SimpleBeanInfo {
	ResourceBundle bundle;

	ImageIcon mono16Icon;

	public ImageEditorBeanInfo() {
		mono16Icon=new ImageIcon(getClass().getResource("Images/iconEditorBar.gif"));
		bundle=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.ImageEditorBeanInfoBundle",Locale.getDefault());
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor pd1=new PropertyDescriptor("NewOpenSaveVisible",ImageEditor.class,"isNewOpenSaveVisible","setNewOpenSaveVisible");
			pd1.setDisplayName(bundle.getString("NewOpenSaveVisible"));
			pd1.setShortDescription(bundle.getString("NewOpenSaveVisibleTip"));
			pd1.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd2=new PropertyDescriptor("ClearDimensionCropVisible",ImageEditor.class,"isClearDimensionCropVisible","setClearDimensionCropVisible");
			pd2.setDisplayName(bundle.getString("ClearDimensionCropVisible"));
			pd2.setShortDescription(bundle.getString("ClearDimensionCropVisibleTip"));
			pd2.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd3=new PropertyDescriptor("CutCopyPasteVisible",ImageEditor.class,"isCutCopyPasteVisible","setCutCopyPasteVisible");
			pd3.setDisplayName(bundle.getString("CutCopyPasteVisible"));
			pd3.setShortDescription(bundle.getString("CutCopyPasteVisibleTip"));
			pd3.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd4=new PropertyDescriptor("ZoomUnzoomPanVisible",ImageEditor.class,"isZoomUnzoomPanVisible","setZoomUnzoomPanVisible");
			pd4.setDisplayName(bundle.getString("ZoomUnzoomPanVisible"));
			pd4.setShortDescription(bundle.getString("ZoomUnzoomPanVisibleTip"));
			pd4.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd5=new PropertyDescriptor("GridButtonVisible",ImageEditor.class,"isGridButtonVisible","setGridButtonVisible");
			pd5.setDisplayName(bundle.getString("GridButtonVisible"));
			pd5.setShortDescription(bundle.getString("GridButtonVisibleTip"));
			pd5.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd6=new PropertyDescriptor("SelectButtonVisible",ImageEditor.class,"isSelectButtonVisible","setSelectButtonVisible");
			pd6.setDisplayName(bundle.getString("SelectButtonVisible"));
			pd6.setShortDescription(bundle.getString("SelectButtonVisibleTip"));
			pd6.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd7=new PropertyDescriptor("WandInvertClearSVisible",ImageEditor.class,"isWandInvertClearSVisible","setWandInvertClearSVisible");
			pd7.setDisplayName(bundle.getString("WandInvertClearSVisible"));
			pd7.setShortDescription(bundle.getString("WandInvertClearSVisibleTip"));
			pd7.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd8=new PropertyDescriptor("UndoButtonVisible",ImageEditor.class,"isUndoButtonVisible","setUndoButtonVisible");
			pd8.setDisplayName(bundle.getString("UndoButtonVisible"));
			pd8.setShortDescription(bundle.getString("UndoButtonVisibleTip"));
			pd8.setValue("propertyCategory",bundle.getString("Buttons"));

			PropertyDescriptor pd9=new PropertyDescriptor("PreviewVisible",ImageEditor.class,"isPreviewVisible","setPreviewVisible");
			pd9.setDisplayName(bundle.getString("PreviewVisible"));
			pd9.setShortDescription(bundle.getString("PreviewVisibleTip"));
			pd9.setValue("propertyCategory",bundle.getString("Preview"));

			PropertyDescriptor pd10=new PropertyDescriptor("DrawingSettings",ImageEditor.class,"isDrawingSettingsVisible","setDrawingSettingsVisible");
			pd10.setDisplayName(bundle.getString("DrawingSettings"));
			pd10.setShortDescription(bundle.getString("DrawingSettingsTip"));
			pd10.setValue("propertyCategory",bundle.getString("RightPanel"));

			PropertyDescriptor pd11=new PropertyDescriptor("PalettesVisible",ImageEditor.class,"isPalettesVisible","setPalettesVisible");
			pd11.setDisplayName(bundle.getString("PalettesVisible"));
			pd11.setShortDescription(bundle.getString("PalettesVisibleTip"));
			pd11.setValue("propertyCategory",bundle.getString("RightPanel"));

			PropertyDescriptor pd12=new PropertyDescriptor("MenuVisible",ImageEditor.class,"isMenuVisible","setMenuVisible");
			pd12.setDisplayName(bundle.getString("MenuVisible"));
			pd12.setShortDescription(bundle.getString("MenuVisibleTip"));
			pd12.setValue("propertyCategory",bundle.getString("Bars"));

			PropertyDescriptor pd13=new PropertyDescriptor("RealTimeUpdate",ImageEditor.class,"isRealTimeUpdate","setRealTimeUpdate");
			pd13.setDisplayName(bundle.getString("RealTimeUpdate"));
			pd13.setShortDescription(bundle.getString("RealTimeUpdateTip"));

			PropertyDescriptor pd14=new PropertyDescriptor("StatusBarVisible",ImageEditor.class,"isStatusBarVisible","setStatusBarVisible");
			pd14.setDisplayName(bundle.getString("StatusBarVisible"));
			pd14.setShortDescription(bundle.getString("StatusBarVisibleTip"));
			pd14.setValue("propertyCategory",bundle.getString("Bars"));

			PropertyDescriptor pd15=new PropertyDescriptor("border",ImageEditor.class,"getBorder","setBorder");
			pd15.setDisplayName(bundle.getString("BorderVisible"));
			pd15.setShortDescription(bundle.getString("BorderVisibleTip"));

			// return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13,
			// pd14, pd15, pd16, pd17};
			return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11,pd12,pd13,pd14,pd15};
		} catch (IntrospectionException exc) {
			System.out.println("IntrospectionException: " + exc.getMessage());
			return null;
		}
	}

	public EventSetDescriptor[] getEventSetDescriptors() {
		Vector<EventSetDescriptor> descriptors=new Vector<EventSetDescriptor>();
		EventSetDescriptor esd=null;

		try {
			Method listenerMethod=PropertyChangeListener.class.getMethod("propertyChange",new Class[] {PropertyChangeEvent.class});
			Method addListenerMethod=ImageEditor.class.getMethod("addPropertyChangeListener",new Class[] {PropertyChangeListener.class});
			Method removelistenerMethod=ImageEditor.class.getMethod("removePropertyChangeListener",new Class[] {PropertyChangeListener.class});
			MethodDescriptor md=new MethodDescriptor(listenerMethod);
			md.setDisplayName(bundle.getString("propertyChange"));
			esd=new EventSetDescriptor("propertyChange",PropertyChangeListener.class,new MethodDescriptor[] {md},addListenerMethod,removelistenerMethod);
			descriptors.addElement(esd);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			Method listenerMethod=VetoableChangeListener.class.getMethod("vetoableChange",new Class[] {PropertyChangeEvent.class});
			Method addListenerMethod=ImageEditor.class.getMethod("addVetoableChangeListener",new Class[] {VetoableChangeListener.class});
			Method removelistenerMethod=ImageEditor.class.getMethod("removeVetoableChangeListener",new Class[] {VetoableChangeListener.class});
			MethodDescriptor md=new MethodDescriptor(listenerMethod);
			md.setDisplayName(bundle.getString("vetoableChange"));
			esd=new EventSetDescriptor("vetoableChange",VetoableChangeListener.class,new MethodDescriptor[] {md},addListenerMethod,removelistenerMethod);
			descriptors.addElement(esd);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			Method listenerMethod1=MouseListener.class.getMethod("mouseEntered",new Class[] {MouseEvent.class});
			MethodDescriptor md1=new MethodDescriptor(listenerMethod1);
			md1.setDisplayName(bundle.getString("mouseEntered"));
			Method listenerMethod2=MouseListener.class.getMethod("mouseExited",new Class[] {MouseEvent.class});
			MethodDescriptor md2=new MethodDescriptor(listenerMethod2);
			md2.setDisplayName(bundle.getString("mouseExited"));
			Method addListenerMethod=ImageEditor.class.getMethod("addMouseListener",new Class[] {MouseListener.class});
			Method removelistenerMethod=ImageEditor.class.getMethod("removeMouseListener",new Class[] {MouseListener.class});
			esd=new EventSetDescriptor("mouse",MouseListener.class,new MethodDescriptor[] {md1,md2},addListenerMethod,removelistenerMethod);
			descriptors.addElement(esd);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			Method listenerMethod=MouseMotionListener.class.getMethod("mouseMoved",new Class[] {MouseEvent.class});
			Method addListenerMethod=ImageEditor.class.getMethod("addMouseMotionListener",new Class[] {MouseMotionListener.class});
			Method removelistenerMethod=ImageEditor.class.getMethod("removeMouseMotionListener",new Class[] {MouseMotionListener.class});
			MethodDescriptor md=new MethodDescriptor(listenerMethod);
			md.setDisplayName(bundle.getString("mouseMoved"));
			esd=new EventSetDescriptor("mouseMotion",MouseMotionListener.class,new MethodDescriptor[] {md},addListenerMethod,removelistenerMethod);
			descriptors.addElement(esd);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {
			Method listenerMethod1=ComponentListener.class.getMethod("componentHidden",new Class[] {ComponentEvent.class});
			MethodDescriptor md1=new MethodDescriptor(listenerMethod1);
			md1.setDisplayName(bundle.getString("componentHidden"));
			Method listenerMethod2=ComponentListener.class.getMethod("componentShown",new Class[] {ComponentEvent.class});
			MethodDescriptor md2=new MethodDescriptor(listenerMethod2);
			md2.setDisplayName(bundle.getString("componentShown"));
			Method listenerMethod3=ComponentListener.class.getMethod("componentResized",new Class[] {ComponentEvent.class});
			MethodDescriptor md3=new MethodDescriptor(listenerMethod3);
			md3.setDisplayName(bundle.getString("componentResized"));
			Method listenerMethod4=ComponentListener.class.getMethod("componentMoved",new Class[] {ComponentEvent.class});
			MethodDescriptor md4=new MethodDescriptor(listenerMethod4);
			md4.setDisplayName(bundle.getString("componentMoved"));

			Method addListenerMethod=ImageEditor.class.getMethod("addComponentListener",new Class[] {ComponentListener.class});
			Method removelistenerMethod=ImageEditor.class.getMethod("removeComponentListener",new Class[] {ComponentListener.class});
			esd=new EventSetDescriptor("component",ComponentListener.class,new MethodDescriptor[] {md1,md2,md3,md4},addListenerMethod,removelistenerMethod);
			descriptors.addElement(esd);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		EventSetDescriptor[] d=new EventSetDescriptor[descriptors.size()];
		for (int i=0;i < d.length;i++)
			d[i]=(EventSetDescriptor) descriptors.elementAt(i);
		return d;

	}

	public Image getIcon(int iconKind) {
		if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
			return mono16Icon.getImage();
		return null;
	}
}
