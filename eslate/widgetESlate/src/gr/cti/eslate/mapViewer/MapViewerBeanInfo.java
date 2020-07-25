package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class MapViewerBeanInfo extends ESlateBeanInfo {
	protected static ResourceBundle bundle;
	private ImageIcon mono16Icon = new ImageIcon(MapViewerBeanInfo.class.getResource("images/mapViewerBeanIcon.gif"));

	public MapViewerBeanInfo() {
		bundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.MapViewerBeanInfoBundle", Locale.getDefault());
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try{
			//StatusBar position
			PropertyDescriptor pd1 = new PropertyDescriptor("statusBarPosition",MapViewer.class);
			pd1.setPropertyEditorClass(EditorStatusBarPosition.class);
			pd1.setDisplayName(bundle.getString("statusBarPosition"));
			pd1.setShortDescription(bundle.getString("statusBarPositionTip"));

			//StatusBar visibility
			PropertyDescriptor pd2 = new PropertyDescriptor("statusBarVisible",MapViewer.class);
			pd2.setDisplayName(bundle.getString("statusBarVisible"));
			pd2.setShortDescription(bundle.getString("statusBarVisibleTip"));

			//ToolBar position
			PropertyDescriptor pd3 = new PropertyDescriptor("toolBarPosition",MapViewer.class);
			pd3.setPropertyEditorClass(EditorToolBarPosition.class);
			pd3.setDisplayName(bundle.getString("toolBarPosition"));
			pd3.setShortDescription(bundle.getString("toolBarPositionTip"));

			//ToolBar visibility
			PropertyDescriptor pd4 = new PropertyDescriptor("toolBarVisible",MapViewer.class);
			pd4.setDisplayName(bundle.getString("toolBarVisible"));
			pd4.setShortDescription(bundle.getString("toolBarVisibleTip"));

			//Opaque
			PropertyDescriptor pd5 = new PropertyDescriptor("opaque",MapViewer.class);
			pd5.setDisplayName(bundle.getString("opaque"));
			pd5.setShortDescription(bundle.getString("opaqueTip"));

			//Background
			PropertyDescriptor pd6 = new PropertyDescriptor("background",MapViewer.class);
			pd6.setDisplayName(bundle.getString("background"));
			pd6.setShortDescription(bundle.getString("backgroundTip"));

			//Statusbar Opaque
			PropertyDescriptor pd7 = new PropertyDescriptor("statusBarOpaque",MapViewer.class);
			pd7.setDisplayName(bundle.getString("statusBarOpaque"));
			pd7.setShortDescription(bundle.getString("statusBarOpaqueTip"));

			//Background image
			PropertyDescriptor pd8 = new PropertyDescriptor("image",MapViewer.class);
			pd8.setDisplayName(bundle.getString("image"));
			pd8.setShortDescription(bundle.getString("imageTip"));

			//Background image view properties
			PropertyDescriptor pd9 = new PropertyDescriptor("imageViewPolicy",MapViewer.class);
			pd9.setPropertyEditorClass(EditorImageBackground.class);
			pd9.setDisplayName(bundle.getString("image")+" ");
			pd9.setShortDescription(bundle.getString("imageViewPolicyTip"));

			//Tool
			PropertyDescriptor pd10 = new PropertyDescriptor("toolActivateVisible",MapViewer.class);
			pd10.setDisplayName(bundle.getString("toolActivateVisible"));
			pd10.setShortDescription(bundle.getString("toolVisibleTip"));
			pd10.setValue("propertyCategory", bundle.getString("tools"));
			pd10.setHidden(true);

			//Tool
			PropertyDescriptor pd11 = new PropertyDescriptor("toolSelectVisible",MapViewer.class);
			pd11.setDisplayName(bundle.getString("toolSelectVisible"));
			pd11.setShortDescription(bundle.getString("toolVisibleTip"));
			pd11.setValue("propertyCategory", bundle.getString("tools"));
			pd11.setHidden(true);

			//Tool
			PropertyDescriptor pd12 = new PropertyDescriptor("toolPanVisible",MapViewer.class);
			pd12.setDisplayName(bundle.getString("toolPanVisible"));
			pd12.setShortDescription(bundle.getString("toolVisibleTip"));
			pd12.setValue("propertyCategory", bundle.getString("tools"));
			pd12.setHidden(true);

			//Tool
			PropertyDescriptor pd13 = new PropertyDescriptor("toolEditVisible",MapViewer.class);
			pd13.setDisplayName(bundle.getString("toolEditVisible"));
			pd13.setShortDescription(bundle.getString("toolVisibleTip"));
			pd13.setValue("propertyCategory", bundle.getString("tools"));
			pd13.setHidden(true);

			//Tool
			PropertyDescriptor pd14 = new PropertyDescriptor("toolZoomVisible",MapViewer.class);
			pd14.setDisplayName(bundle.getString("toolZoomVisible"));
			pd14.setShortDescription(bundle.getString("toolVisibleTip"));
			pd14.setValue("propertyCategory", bundle.getString("tools"));
			pd14.setHidden(true);

			//Tool
			PropertyDescriptor pd15 = new PropertyDescriptor("toolGridVisible",MapViewer.class);
			pd15.setDisplayName(bundle.getString("toolGridVisible"));
			pd15.setShortDescription(bundle.getString("toolVisibleTip"));
			pd15.setValue("propertyCategory", bundle.getString("tools"));
			pd15.setHidden(true);

			//Tool
			PropertyDescriptor pd16 = new PropertyDescriptor("toolGoInVisible",MapViewer.class);
			pd16.setDisplayName(bundle.getString("toolGoInVisible"));
			pd16.setShortDescription(bundle.getString("toolVisibleTip"));
			pd16.setValue("propertyCategory", bundle.getString("tools"));
			pd16.setHidden(true);

			//Tool
			PropertyDescriptor pd17 = new PropertyDescriptor("toolGoOutVisible",MapViewer.class);
			pd17.setDisplayName(bundle.getString("toolGoOutVisible"));
			pd17.setShortDescription(bundle.getString("toolVisibleTip"));
			pd17.setValue("propertyCategory", bundle.getString("tools"));
			pd17.setHidden(true);

			//Tool
			PropertyDescriptor pd18 = new PropertyDescriptor("toolRotateVisible",MapViewer.class);
			pd18.setDisplayName(bundle.getString("toolRotateVisible"));
			pd18.setShortDescription(bundle.getString("toolVisibleTip"));
			pd18.setValue("propertyCategory", bundle.getString("tools"));
			pd18.setHidden(true);

			//Tool
			PropertyDescriptor pd19 = new PropertyDescriptor("toolMiniatureVisible",MapViewer.class);
			pd19.setDisplayName(bundle.getString("toolMiniatureVisible"));
			pd19.setShortDescription(bundle.getString("toolVisibleTip"));
			pd19.setValue("propertyCategory", bundle.getString("tools"));
			pd19.setHidden(true);

			//Tool
			PropertyDescriptor pd20 = new PropertyDescriptor("toolMeterVisible",MapViewer.class);
			pd20.setDisplayName(bundle.getString("toolMeterVisible"));
			pd20.setShortDescription(bundle.getString("toolVisibleTip"));
			pd20.setValue("propertyCategory", bundle.getString("tools"));
			pd20.setHidden(true);

			//Tool
			PropertyDescriptor pd21 = new PropertyDescriptor("toolIdentifyVisible",MapViewer.class);
			pd21.setDisplayName(bundle.getString("toolIdentifyVisible"));
			pd21.setShortDescription(bundle.getString("toolVisibleTip"));
			pd21.setValue("propertyCategory", bundle.getString("tools"));
			pd21.setHidden(true);

			//Tool
			PropertyDescriptor pd22 = new PropertyDescriptor("toolLayerVisibilityVisible",MapViewer.class);
			pd22.setDisplayName(bundle.getString("toolLayerVisibilityVisible"));
			pd22.setShortDescription(bundle.getString("toolVisibleTip"));
			pd22.setValue("propertyCategory", bundle.getString("tools"));
			pd22.setHidden(true);

			//Tool
			PropertyDescriptor pd23 = new PropertyDescriptor("toolSpotVisible",MapViewer.class);
			pd23.setDisplayName(bundle.getString("toolSpotVisible"));
			pd23.setShortDescription(bundle.getString("toolVisibleTip"));
			pd23.setValue("propertyCategory", bundle.getString("tools"));
			pd23.setHidden(true);

			//Antialiasing
			PropertyDescriptor pd24 = new PropertyDescriptor("antialiasing",MapViewer.class);
			pd24.setDisplayName(bundle.getString("antialiasing"));
			pd24.setShortDescription(bundle.getString("antialiasingTip"));

			//Quality
			PropertyDescriptor pd25 = new PropertyDescriptor("qualityOverSpeed",MapViewer.class);
			pd25.setDisplayName(bundle.getString("qualityOverSpeed"));
			pd25.setShortDescription(bundle.getString("qualityOverSpeedTip"));

			//Tool
			PropertyDescriptor pd26 = new PropertyDescriptor("toolNavigateVisible",MapViewer.class);
			pd26.setDisplayName(bundle.getString("toolNavigateVisible"));
			pd26.setShortDescription(bundle.getString("toolVisibleTip"));
			pd26.setValue("propertyCategory", bundle.getString("tools"));
			pd26.setHidden(true);

			//Coordinates
			PropertyDescriptor pd27 = new PropertyDescriptor("showCoordinates",MapViewer.class);
			pd27.setDisplayName(bundle.getString("showCoordinates"));
			pd27.setShortDescription(bundle.getString("showCoordinatesTip"));

			//Grid visible
			PropertyDescriptor pd28 = new PropertyDescriptor("gridVisible",MapViewer.class);
			pd28.setDisplayName(bundle.getString("gridVisible"));
			pd28.setShortDescription(bundle.getString("gridVisibleTip"));

			//Grid color
			PropertyDescriptor pd29 = new PropertyDescriptor("gridColor",MapViewer.class);
			pd29.setDisplayName(bundle.getString("gridColor"));
			pd29.setShortDescription(bundle.getString("gridColorTip"));

			//Grid step
			PropertyDescriptor pd30 = new PropertyDescriptor("gridStep",MapViewer.class);
			pd30.setDisplayName(bundle.getString("gridStep"));
			pd30.setShortDescription(bundle.getString("gridStepTip"));

			//Border
			PropertyDescriptor pd31 = new PropertyDescriptor("border",MapViewer.class);
			pd31.setDisplayName(bundle.getString("border"));
			pd31.setShortDescription(bundle.getString("borderTip"));

			//Font
			PropertyDescriptor pd32 = new PropertyDescriptor("font",MapViewer.class);
			pd32.setDisplayName(bundle.getString("font"));
			pd32.setShortDescription(bundle.getString("fontTip"));

			//Tool
			PropertyDescriptor pd33 = new PropertyDescriptor("toolLegendVisible",MapViewer.class);
			pd33.setDisplayName(bundle.getString("toolLegendVisible"));
			pd33.setShortDescription(bundle.getString("toolVisibleTip"));
			pd33.setValue("propertyCategory", bundle.getString("tools"));
			pd33.setHidden(true);

			//Agent positioning dialog
			PropertyDescriptor pd34 = new PropertyDescriptor("showAgentPositioningInfoDialog",MapViewer.class);
			pd34.setDisplayName(bundle.getString("showAgentPositioningInfoDialog"));
			pd34.setShortDescription(bundle.getString("showAgentPositioningInfoDialogTip"));
			pd34.setExpert(true);

			//Agent cross
			PropertyDescriptor pd35 = new PropertyDescriptor("paintCrossOnAgent",MapViewer.class);
			pd35.setDisplayName(bundle.getString("paintCrossOnAgent"));
			pd35.setShortDescription(bundle.getString("paintCrossOnAgentTip"));


			//CircleSelectionTool visible
			PropertyDescriptor pd36 = new PropertyDescriptor("circleSelectToolVisible",MapViewer.class);
			pd36.setDisplayName(bundle.getString("circleSelectToolVisible"));
			pd36.setShortDescription(bundle.getString("toolVisibleTip"));
			pd36.setHidden(true);
			pd36.setValue("propertyCategory", bundle.getString("tools"));

			//BrowseTool visible
			PropertyDescriptor pd37 = new PropertyDescriptor("browseToolVisible",MapViewer.class);
			pd37.setDisplayName(bundle.getString("browseToolVisible"));
			pd37.setShortDescription(bundle.getString("toolVisibleTip"));
			pd37.setHidden(true);
			pd37.setValue("propertyCategory", bundle.getString("tools"));

			//BrowseTool visible
			PropertyDescriptor pd38 = new PropertyDescriptor("busyIconVisible",MapViewer.class);
			pd38.setDisplayName(bundle.getString("busyIconVisible"));
			pd38.setShortDescription(bundle.getString("busyIconVisibleTip"));
			pd38.setExpert(true);

			//Show Inactive ZoomRects
			PropertyDescriptor pd39 = new PropertyDescriptor("showInactiveZoomRects",MapViewer.class);
			pd39.setDisplayName(bundle.getString("showInactiveZoomRects"));
			pd39.setShortDescription(bundle.getString("showInactiveZoomRectsTip"));

			//Enable/disable meter popup
			PropertyDescriptor pd40 = new PropertyDescriptor("meterPopupEnabled",MapViewer.class);
			pd40.setDisplayName(bundle.getString("meterPopupEnabled"));
			pd40.setShortDescription(bundle.getString("meterPopupEnabledTip"));

			//Enable/disable selection popup
			PropertyDescriptor pd41 = new PropertyDescriptor("selectionPopupEnabled",MapViewer.class);
			pd41.setDisplayName(bundle.getString("selectionPopupEnabled"));
			pd41.setShortDescription(bundle.getString("selectionPopupEnabledTip"));

			//ErrorTolerance
			PropertyDescriptor pd42 = new PropertyDescriptor("errorTolerance",MapViewer.class);
			pd42.setDisplayName(bundle.getString("errorTolerance"));
			pd42.setShortDescription(bundle.getString("errorToleranceTip"));

			//ErrorTolerance
			PropertyDescriptor pd43 = new PropertyDescriptor("mapBackground",MapViewer.class);
			pd43.setDisplayName(bundle.getString("mapBackground"));
			pd43.setShortDescription(bundle.getString("mapBackgroundTip"));

			return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11,pd12,pd13,pd14,pd15,pd16,pd17,pd18,pd19,pd20,pd21,pd22,pd23,pd24,pd25,pd26,pd27,pd28,pd29,pd30,pd31,pd32,pd33,pd34,pd35,pd36,pd37,pd38,pd39,pd40,pd41,pd42,pd43};
		} catch (IntrospectionException exc) {
			System.out.println("MAPVIEWER#200004051949: IntrospectionException: " + exc.getMessage());
			return null;
		}
	}

	public EventSetDescriptor[] getEventSetDescriptors() {
		EventSetDescriptor ed1=null, ed2=null, ed3=null, ed4=null, ed5=null, ed6=null, ed7=null,ed8 = null,ed9 = null;

		try {
			Method addListenerMethod = MapViewer.class.getMethod("addMapViewerListener", new Class[] {MapViewerListener.class});
			Method removelistenerMethod = MapViewer.class.getMethod("removeMapViewerListener", new Class[] {MapViewerListener.class});

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mousePressedOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mousePressedOnMapArea"));
				ed1 = new EventSetDescriptor("mousePressedOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mouseReleasedOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mouseReleasedOnMapArea"));
				ed2 = new EventSetDescriptor("mouseReleasedOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mouseClickedOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mouseClickedOnMapArea"));
				ed3 = new EventSetDescriptor("mouseClickedOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mouseMovedOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mouseMovedOnMapArea"));
				ed4 = new EventSetDescriptor("mouseMovedOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mouseDraggedOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mouseDraggedOnMapArea"));
				ed5 = new EventSetDescriptor("mouseDraggedOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mouseEnteredOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mouseEnteredOnMapArea"));
				ed6 = new EventSetDescriptor("mouseEnteredOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mouseExitedOnMapArea", new Class[] {MapViewerMouseEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mouseExitedOnMapArea"));
				ed7 = new EventSetDescriptor("mouseExitedOnMapArea",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mapViewerScaleChanged", new Class[] {MapViewerEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mapViewerScaleChanged"));
				ed7 = new EventSetDescriptor("mapViewerScaleChanged",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			try {
				Method listenerMethod = MapViewerListener.class.getMethod("mapViewerBusyStatusChanged", new Class[] {MapViewerEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("mapViewerBusyStatusChanged"));
				ed7 = new EventSetDescriptor("mapViewerBusyStatusChanged",
									   MapViewerListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			addListenerMethod = MapViewer.class.getMethod("addSelectionShapeTypeListener", new Class[] {SelectionShapeTypeListener.class});
			removelistenerMethod = MapViewer.class.getMethod("removeSelectionShapeTypeListener", new Class[] {SelectionShapeTypeListener.class});


			try {
				Method listenerMethod = SelectionShapeTypeListener.class.getMethod("shapeTypeChanged", new Class[] {SelectionShapeTypeEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("selectionShapeTypeChanged"));
				ed8 = new EventSetDescriptor("shapeTypeChanged",
									   SelectionShapeTypeListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			addListenerMethod = MapViewer.class.getMethod("addSelectionShapeListener", new Class[] {SelectionShapeListener.class});
			removelistenerMethod = MapViewer.class.getMethod("removeSelectionShapeListener", new Class[] {SelectionShapeListener.class});

			try {
				Method listenerMethod = SelectionShapeListener.class.getMethod("shapeGeometryChanged", new Class[] {SelectionShapeEvent.class});
				MethodDescriptor md = new MethodDescriptor(listenerMethod);
				md.setDisplayName(bundle.getString("selectionShapeGeometryChanged"));
				ed9 = new EventSetDescriptor("shapeGeometryChanged",
									   SelectionShapeListener.class,
									   new MethodDescriptor[] {md},
									   addListenerMethod,
									   removelistenerMethod);
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		} catch(Exception exc) {
			exc.printStackTrace();
		}

		//Combine my event descriptors with my parent's descriptors.
		EventSetDescriptor[] par=super.getEventSetDescriptors();
		EventSetDescriptor[] my=new EventSetDescriptor[par.length+9];

		System.arraycopy(par,0,my,0,par.length);
		my[par.length+0]=ed1;
		my[par.length+1]=ed2;
		my[par.length+2]=ed3;
		my[par.length+3]=ed4;
		my[par.length+4]=ed5;
		my[par.length+5]=ed6;
		my[par.length+6]=ed7;
		my[par.length+7]=ed8;
		my[par.length+8]=ed9;

		return my;
	}

	public Image getIcon(int iconKind) {
		if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
			return mono16Icon.getImage();
		return null;
	}
}