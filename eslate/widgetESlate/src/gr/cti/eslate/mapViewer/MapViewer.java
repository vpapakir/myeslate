package gr.cti.eslate.mapViewer;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.IProtocolPlug;
import gr.cti.eslate.base.LeftMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugNotConnectedException;
import gr.cti.eslate.base.ProtocolPlug;
import gr.cti.eslate.base.RightSingleConnectionProtocolPlug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.eslateToolBar.ToolLocation;
import gr.cti.eslate.eslateToolBar.VisualGroup;
import gr.cti.eslate.mapModel.LayerEvent;
import gr.cti.eslate.mapModel.LayerListener;
import gr.cti.eslate.mapModel.MapEvent;
import gr.cti.eslate.mapModel.MapListener;
import gr.cti.eslate.mapModel.RegionEvent;
import gr.cti.eslate.mapModel.RegionListener;
import gr.cti.eslate.mapModel.geom.VectorGeographicObject;
import gr.cti.eslate.protocol.EnchancedMapListener;
import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.IInteractiveMapViewer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapBackground;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IPoint;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IPolyLine;
import gr.cti.eslate.protocol.IPolygon;
import gr.cti.eslate.protocol.IRasterLayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.LayerVisibilityEvent;
import gr.cti.eslate.utils.BooleanWrapper;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.Print;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * This is the Map Browser component. It can show maps and host agents.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999i
 */
public class MapViewer extends ImageJPanel implements EnchancedMapListener,IInteractiveMapViewer,ESlatePart,Externalizable {
	public MapViewer() {
		super();
		//Localize
		java.util.Locale locale=Locale.getDefault();
		messagesBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.MessagesBundle",locale);
		menuBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.MenuBundle",locale);
		toolbarTipBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.ToolbarTipBundle",locale);
		plugBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.PlugBundle",locale);
// GT -start
		attachTimers();
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.constructionStarted(this);
		pm.init(constructorTimer);
// GT - end

		initializeCommon();
		initialize();

		pm.constructionEnded(this);
		pm.stop(constructorTimer);
		pm.displayTime(constructorTimer, messagesBundle.getString("ConstructorTimer"), "ms");
	}

// GT -start
	public MapViewer(ObjectInput in) throws Exception {
		super();
		//Localize
		java.util.Locale locale=Locale.getDefault();
		messagesBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.MessagesBundle",locale);
		menuBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.MenuBundle",locale);
		toolbarTipBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.ToolbarTipBundle",locale);
		plugBundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.PlugBundle",locale);
// GT -start
		attachTimers();
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.constructionStarted(this);
		pm.init(constructorTimer);
// GT - end

		initializeCommon();

		pm.stop(constructorTimer);
		pm.constructionEnded(this);
		getESlateHandle();
		initialize(in);
		pm.displayTime(constructorTimer, messagesBundle.getString("ConstructorTimer"), "ms");
	}
// GT -end

	/**
	 * Create toolbar.
	 */
	private void createToolBar() {
		toolbar=new MapViewerToolBar(this,ESlateToolBar.HORIZONTAL);
		toolbar.setDynamicBorders(true);
		getESlateHandle().add(toolbar.getESlateHandle());
		//toolbar.setAlignmentX(LEFT_ALIGNMENT);
		//toolbar.setAlignmentY(TOP_ALIGNMENT);
		setToolBarPosition(ToolBar.NORTH);
		setToolBarVisible(true);

		toolbar.setOpaque(false);
		toolbar.setBorder(new EmptyBorder(1,1,1,1));

		ButtonGroup btngrp=toolbar.addButtonGroup();
		VisualGroup vg1=toolbar.addVisualGroup();


		//Navigate tool
		navigate=new MapViewerToggleButton();
		toolbar.add(vg1,navigate,toolbarTipBundle.getString("navigate"));
		btngrp.add(navigate);
		setUpTool(navigate,loadImageIcon("images/navigate.gif"),toolbarTipBundle.getString("navigate"),toolbarTipBundle.getString("navigatehelp"));
		//JToggleTool(loadImageIcon("images/navigate.gif"),toolbarTipBundle.getString("navigate"),toolbarTipBundle.getString("navigatehelp"),this,mapPane.getNavigateMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Activate tool
		activate=new MapViewerToggleButton();
		toolbar.add(vg1,activate,toolbarTipBundle.getString("activate"));
		btngrp.add(activate);
		setUpTool(activate,loadImageIcon("images/activate.gif"),toolbarTipBundle.getString("activate"),toolbarTipBundle.getString("activatehelp"));
		//JToggleTool(loadImageIcon("images/activate.gif"),toolbarTipBundle.getString("activate"),toolbarTipBundle.getString("activatehelp"),this,mapPane.getActivateMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Browse (pan and activate) tool
		browse=new MapViewerToggleButton();
		toolbar.add(vg1,browse,toolbarTipBundle.getString("browse"));
		btngrp.add(browse);
		setUpTool(browse,loadImageIcon("images/browse.gif"),toolbarTipBundle.getString("browse"),toolbarTipBundle.getString("browsehelp"));
		//JToggleTool(loadImageIcon("images/browse.gif"),toolbarTipBundle.getString("browse"),toolbarTipBundle.getString("browsehelp"),this,mapPane.getBrowseMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Rectangular selection tool
		select=new MapViewerToggleButton();
		toolbar.add(vg1,select,toolbarTipBundle.getString("select"));
		btngrp.add(select);
		setUpTool(select,loadImageIcon("images/select.gif"),toolbarTipBundle.getString("select"),toolbarTipBundle.getString("selecthelp"));
		//JToggleTool(loadImageIcon("images/select.gif"),toolbarTipBundle.getString("select"),toolbarTipBundle.getString("selecthelp"),this,mapPane.getSelectMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Circlular selection tool
		selectCircle=new MapViewerToggleButton();
		toolbar.add(vg1,selectCircle,toolbarTipBundle.getString("selectCircle"));
		btngrp.add(selectCircle);
		setUpTool(selectCircle,loadImageIcon("images/selectCircle.gif"),toolbarTipBundle.getString("selectCircle"),toolbarTipBundle.getString("selecthelp"));
		//JToggleTool(loadImageIcon("images/selectCircle.gif"),toolbarTipBundle.getString("select"),toolbarTipBundle.getString("selecthelp"),this,mapPane.getSelectCircleMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Pan tool
		pan=new MapViewerToggleButton();
		toolbar.add(vg1,pan,toolbarTipBundle.getString("pan"));
		btngrp.add(pan);
		setUpTool(pan,loadImageIcon("images/pan.gif"),toolbarTipBundle.getString("pan"),toolbarTipBundle.getString("panhelp"));
		//JToggleTool(loadImageIcon("images/pan.gif"),toolbarTipBundle.getString("pan"),toolbarTipBundle.getString("panhelp"),this,mapPane.getPanMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Object tool
		insObject=new MapViewerToggleButton();
		toolbar.add(vg1,insObject,toolbarTipBundle.getString("edit"));
		btngrp.add(insObject);
		setUpTool(insObject,loadImageIcon("images/pin.gif"),toolbarTipBundle.getString("edit"),toolbarTipBundle.getString("edithelp"));
		//JToggleTool(loadImageIcon("images/pin.gif"),toolbarTipBundle.getString("insObject"),toolbarTipBundle.getString("insObjecthelp"),this,mapPane.getInsertObjectMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Go-inside tool
		goin=new MapViewerToggleButton();
		toolbar.add(vg1,goin,toolbarTipBundle.getString("goIn"));
		btngrp.add(goin);
		setUpTool(goin,loadImageIcon("images/zoomin.gif"),toolbarTipBundle.getString("goIn"),toolbarTipBundle.getString("goInhelp"));
		//JToggleTool(loadImageIcon("images/zoomin.gif"),toolbarTipBundle.getString("goin"),toolbarTipBundle.getString("goinhelp"),this,mapPane.getGoInMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Go-outside tool
		goout=new MapViewerToggleButton();
		toolbar.add(vg1,goout,toolbarTipBundle.getString("goOut"));
		btngrp.add(goout);
		setUpTool(goout,loadImageIcon("images/zoomout.gif"),toolbarTipBundle.getString("goOut"),toolbarTipBundle.getString("goOuthelp"));
		//JToggleTool(loadImageIcon("images/zoomout.gif"),toolbarTipBundle.getString("goout"),null,this,mapPane.getGoOutMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Rotate tool
		rotate=new MapViewerToggleButton();
		toolbar.add(vg1,rotate,toolbarTipBundle.getString("rotate"));
		btngrp.add(rotate);
		setUpTool(rotate,loadImageIcon("images/rotate.gif"),toolbarTipBundle.getString("rotate"),toolbarTipBundle.getString("rotatehelp"));
		//JToggleTool(loadImageIcon("images/rotate.gif"),toolbarTipBundle.getString("rotate"),toolbarTipBundle.getString("rotatehelp"),this,mapPane.getRotateMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Meter tool
		meter=new MapViewerToggleButton();
		toolbar.add(vg1,meter,toolbarTipBundle.getString("meter"));
		btngrp.add(meter);
		setUpTool(meter,loadImageIcon("images/meter.gif"),toolbarTipBundle.getString("meter"),toolbarTipBundle.getString("meterhelp"));
		//JToggleTool(loadImageIcon("images/meter.gif"),toolbarTipBundle.getString("meter"),toolbarTipBundle.getString("meterhelp"),this,mapPane.getMeterMouseListener(),toolbar.getDefaultButtonGroup(),true);


		VisualGroup vg2=toolbar.addVisualGroup();


		//Identify tool
		identify=new MapViewerToggleButton();
		toolbar.add(vg2,identify,toolbarTipBundle.getString("identify"));
		btngrp.add(identify);
		ImageIcon icn;
		if (Locale.getDefault().getISO3Country().equals("GRC"))
			icn=loadImageIcon("images/identifygr.gif");
		else
			icn=loadImageIcon("images/identify.gif");
		setUpTool(identify,icn,toolbarTipBundle.getString("identify"),toolbarTipBundle.getString("identifyhelp"));
		//JToggleTool(loadImageIcon("images/identifygr.gif"),toolbarTipBundle.getString("identify"),toolbarTipBundle.getString("identifyhelp"),this,mapPane.getIdentifyMouseListener(),toolbar.getDefaultButtonGroup(),true);


		VisualGroup vg3=toolbar.addVisualGroup();


		//Layer visibility tool
		layerVisib=new MapViewerButton();
		toolbar.add(vg3,layerVisib,toolbarTipBundle.getString("layerVisib"));
		setUpTool(layerVisib,loadImageIcon("images/visible.gif"),toolbarTipBundle.getString("layerVisib"),null);
		//JButtonTool(loadImageIcon("images/visible.gif"),toolbarTipBundle.getString("layervisibility"),this,true);


		//Legend tool
		legend=new MapViewerToggleButton();
		toolbar.add(vg3,legend,toolbarTipBundle.getString("legend"));
		if (Locale.getDefault().getISO3Country().equals("GRC"))
			icn=loadImageIcon("images/legendgr.gif");
		else
			icn=loadImageIcon("images/legend.gif");
		setUpTool(legend,icn,toolbarTipBundle.getString("legend"),null);


		//Show miniature tool
		miniature=new MapViewerToggleButton();
		toolbar.add(vg3,miniature,toolbarTipBundle.getString("miniature"));
		setUpTool(miniature,loadImageIcon("images/miniature.gif"),toolbarTipBundle.getString("miniature"),toolbarTipBundle.getString("miniaturehelp"));
		//JToggleTool(loadImageIcon("images/miniature.gif"),toolbarTipBundle.getString("miniature"),toolbarTipBundle.getString("miniaturehelp"),this,null,null,true);


		//Grid tool
		grid=new MapViewerToggleButton();
		toolbar.add(vg3,grid,toolbarTipBundle.getString("grid"));
		setUpTool(grid,loadImageIcon("images/grid.gif"),toolbarTipBundle.getString("grid"),null);
		//JToggleTool(loadImageIcon("images/grid.gif"),toolbarTipBundle.getString("grid"),"",this,null,null,true);


		//Spot tool
		spot=new MapViewerButton();
		toolbar.add(vg3,spot,toolbarTipBundle.getString("spot"));
		setUpTool(spot,loadImageIcon("images/target.gif"),toolbarTipBundle.getString("spot"),null);
		//JButtonTool(loadImageIcon("images/target.gif"),toolbarTipBundle.getString("spot"),this,true);


		//Zoom to rectangle tool
		zoomrect=new MapViewerToggleButton();
		toolbar.add(vg3,zoomrect,toolbarTipBundle.getString("zoomRect"));
		btngrp.add(zoomrect);
		setUpTool(zoomrect,loadImageIcon("images/zoomintorect.gif"),toolbarTipBundle.getString("zoomRect"),toolbarTipBundle.getString("zoomRecthelp"));
		//JToggleTool(loadImageIcon("images/zoomintorect.gif"),toolbarTipBundle.getString("zoomrect"),toolbarTipBundle.getString("zoomrecthelp"),this,mapPane.getZoomInToRectMouseListener(),toolbar.getDefaultButtonGroup(),true);


		//Zoom slider tool
		zoom=new ZoomSlider(toolbarTipBundle.getString("zoom"),this);
		zoom.setEnabled(false);
		toolbar.add(vg3,zoom,toolbarTipBundle.getString("zoom"));
	}
	/**
	 * Add listeners to the toolbar tools, either in a new toolbar or in a restored toolbar.
	 * When setUpToolBar() is called from initialize(ObjectInput) is shouldn't restore the original
	 * button state, cause the state of the buttons has already been restored by the toolbar itself.
	 * Therefore a boolean argument was introduced to this method.
	 */
	private void setUpToolBar(boolean initializeButtonState) {
		if (toolbar==null) return;
		if (initializeButtonState) {
			//Navigate
			if (navigate!=null)
				setUpTool(navigate,loadImageIcon("images/navigate.gif"),toolbarTipBundle.getString("navigate"),toolbarTipBundle.getString("navigatehelp"));
			//Activate tool
			if (activate!=null)
				setUpTool(activate,loadImageIcon("images/activate.gif"),toolbarTipBundle.getString("activate"),toolbarTipBundle.getString("activatehelp"));
			//Browse (pan and activate) tool
			if (browse!=null)
				setUpTool(browse,loadImageIcon("images/browse.gif"),toolbarTipBundle.getString("browse"),toolbarTipBundle.getString("browsehelp"));
			//Rectangular selection tool
			if (select!=null)
				setUpTool(select,loadImageIcon("images/select.gif"),toolbarTipBundle.getString("select"),toolbarTipBundle.getString("selecthelp"));
			//Circlular selection tool
			if (selectCircle!=null)
				setUpTool(selectCircle,loadImageIcon("images/selectCircle.gif"),toolbarTipBundle.getString("selectCircle"),toolbarTipBundle.getString("selecthelp"));
			//Pan tool
			if (pan!=null)
				setUpTool(pan,loadImageIcon("images/pan.gif"),toolbarTipBundle.getString("pan"),toolbarTipBundle.getString("panhelp"));
			//Object tool
			if (insObject!=null)
				setUpTool(insObject,loadImageIcon("images/pin.gif"),toolbarTipBundle.getString("edit"),toolbarTipBundle.getString("edithelp"));
			//Go-inside tool
			if (goin!=null)
				setUpTool(goin,loadImageIcon("images/zoomin.gif"),toolbarTipBundle.getString("goIn"),toolbarTipBundle.getString("goInhelp"));
			//Go-outside tool
			if (goout!=null)
				setUpTool(goout,loadImageIcon("images/zoomout.gif"),toolbarTipBundle.getString("goOut"),toolbarTipBundle.getString("goOuthelp"));
			//Rotate tool
			if (rotate!=null)
				setUpTool(rotate,loadImageIcon("images/rotate.gif"),toolbarTipBundle.getString("rotate"),toolbarTipBundle.getString("rotatehelp"));
			//Meter tool
			if (meter!=null)
				setUpTool(meter,loadImageIcon("images/meter.gif"),toolbarTipBundle.getString("meter"),toolbarTipBundle.getString("meterhelp"));
			//Identify tool
			ImageIcon icn;
			if (identify!=null) {
				if (Locale.getDefault().getISO3Country().equals("GRC"))
					icn=loadImageIcon("images/identifygr.gif");
				else
					icn=loadImageIcon("images/identify.gif");
				setUpTool(identify,icn,toolbarTipBundle.getString("identify"),toolbarTipBundle.getString("identifyhelp"));
			}
			//Layer visibility tool
			if (layerVisib!=null)
				setUpTool(layerVisib,loadImageIcon("images/visible.gif"),toolbarTipBundle.getString("layerVisib"),null);
			//Legend tool
			if (legend!=null) {
				if (Locale.getDefault().getISO3Country().equals("GRC"))
					icn=loadImageIcon("images/legendgr.gif");
				else
					icn=loadImageIcon("images/legend.gif");
				setUpTool(legend,icn,toolbarTipBundle.getString("legend"),null);
			}
			//Show miniature tool
			if (miniature!=null)
				setUpTool(miniature,loadImageIcon("images/miniature.gif"),toolbarTipBundle.getString("miniature"),toolbarTipBundle.getString("miniaturehelp"));
			//Grid tool
			if (grid!=null)
				setUpTool(grid,loadImageIcon("images/grid.gif"),toolbarTipBundle.getString("grid"),null);
			//Spot tool
			if (spot!=null)
				setUpTool(spot,loadImageIcon("images/target.gif"),toolbarTipBundle.getString("spot"),null);
			//Zoom to rectangle tool
			if (zoomrect!=null)
				setUpTool(zoomrect,loadImageIcon("images/zoomintorect.gif"),toolbarTipBundle.getString("zoomRect"),toolbarTipBundle.getString("zoomRecthelp"));
		}

		if (navigate!=null)
			navigate.addActionListener(getAction(NAVIGATE_TOOL_NAME));
		if (activate!=null)
			activate.addActionListener(getAction(ACTIVATE_TOOL_NAME));
		if (browse!=null) {
			browse.addActionListener(getAction(BROWSE_TOOL_NAME));
			//Hides the neighbor when deselected
			browse.addItemListener(new ItemListener() {
				boolean lastTime=!browse.isSelected();
				public void itemStateChanged(ItemEvent e) {
					if (lastTime!=browse.isSelected()) {
						lastTime=!lastTime;
						if (!browse.isSelected())
							checkScrollPan(null,null,null,null);
					}
				}
			});
		}
		if (select!=null) {
			select.addActionListener(getAction(SELECTRECT_TOOL_NAME));
			//Displays a message when the control key is pressed
			select.addItemListener(new ItemListener() {
				KeyAdapter ka;
				boolean alreadyAdded=false;
				boolean lastTime=!select.isSelected();
				public void itemStateChanged(ItemEvent e) {
					if (lastTime==select.isSelected())
						return;
					lastTime=select.isSelected();
					if (select.isSelected()) {
						if (ka==null) {
							ka=new KeyAdapter() {
								public void keyPressed(KeyEvent ke) {
									if (ke.getKeyCode()==KeyEvent.VK_CONTROL)
										setMessage(toolbarTipBundle.getString("selectcontrolhelp"),0);
								}
								public void keyReleased(KeyEvent ke) {
									if (ke.getKeyCode()==KeyEvent.VK_CONTROL)
										setMessage(toolbarTipBundle.getString("selecthelp"),0);
								}
							};
						}
						if (!alreadyAdded) {
							alreadyAdded=true;
							MapViewer.this.addKeyListener(ka);
						}
					} else {
						alreadyAdded=false;
						MapViewer.this.removeKeyListener(ka);
					}
				}
			});
		}
		if (selectCircle!=null) {
			selectCircle.addActionListener(getAction(SELECTCIRCLE_TOOL_NAME));
			//Displays a message when the control key is pressed
			selectCircle.addItemListener(new ItemListener() {
				KeyAdapter ka;
				boolean alreadyAdded=false;
				boolean lastTime=!selectCircle.isSelected();
				public void itemStateChanged(ItemEvent e) {
					if (lastTime==selectCircle.isSelected())
						return;
					lastTime=selectCircle.isSelected();
					if (selectCircle.isSelected()) {
						if (ka==null) {
							ka=new KeyAdapter() {
								public void keyPressed(KeyEvent ke) {
									if (ke.getKeyCode()==KeyEvent.VK_CONTROL)
										setMessage(toolbarTipBundle.getString("selectcontrolhelp"),0);
								}
								public void keyReleased(KeyEvent ke) {
									if (ke.getKeyCode()==KeyEvent.VK_CONTROL)
										setMessage(toolbarTipBundle.getString("selecthelp"),0);
								}
							};
						}
						if (!alreadyAdded) {
							alreadyAdded=true;
							MapViewer.this.addKeyListener(ka);
						}
					} else {
						alreadyAdded=false;
						MapViewer.this.removeKeyListener(ka);
					}
				}
			});
		}
		if (pan!=null) {
			pan.addActionListener(getAction(PAN_TOOL_NAME));
			//Hides the neighbor when deselected
			pan.addItemListener(new ItemListener() {
				boolean lastTime=!pan.isSelected();
				public void itemStateChanged(ItemEvent e) {
					if (lastTime!=pan.isSelected()) {
						lastTime=!lastTime;
						if (map!=null)
							checkPanTool();
						if (!pan.isSelected())
							checkScrollPan(null,null,null,null);
					}
				}
			});
		}
		if (insObject!=null)
			insObject.addActionListener(getAction(INSERT_OBJECT_TOOL_NAME));
		if (goin!=null) {
			goin.addActionListener(getAction(GO_IN_TOOL_NAME));
			goin.addItemListener(new ItemListener() {
				boolean lastTime=!goin.isSelected();
				public void itemStateChanged(ItemEvent e) {
					if (lastTime==goin.isSelected())
						return;
					lastTime=goin.isSelected();
					if (!goin.isSelected())
						mapPane.clearLargerScaleMapsRectangles();
				}
			});
		}
		if (goout!=null)
			goout.addActionListener(getAction(GO_OUT_TOOL_NAME));
		if (rotate!=null)
			rotate.addActionListener(getAction(ROTATE_TOOL_NAME));
		if (meter!=null)
			meter.addActionListener(getAction(METER_TOOL_NAME));
		if (identify!=null)
			identify.addActionListener(getAction(IDENTIFY_TOOL_NAME));
		if (layerVisib!=null) {
			layerVisib.addActionListener(new ActionListener() {
				JPopupMenu pop;
				public void actionPerformed(ActionEvent e) {
					pop=new JPopupMenu();
					pop.setLightWeightPopupEnabled(false);
					pop.addPopupMenuListener(new PopupMenuListener() {
						public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
						}
						public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
							mapPane.paintImmediately(mapPane.getVisibleRect());
							clearImages();
						}
						public void popupMenuCanceled(PopupMenuEvent e) {
							mapPane.paintImmediately(mapPane.getVisibleRect());
							clearImages();
						}
						private void clearImages() {
							Component[] c=pop.getComponents();
							for (int i=0;i<c.length;i++)
								if (c[i] instanceof MyJCheckBoxMenuItem)
									((MyJCheckBoxMenuItem) c[i]).clear();
						}
					});
					//Load background names
					String[] sn=map.getActiveRegionView().getBackgroundNames();
					ILayerView[] l=map.getActiveRegionView().getLayerViews();
					if (sn.length>1) {
						String ab=map.getActiveRegionView().getBackgroundName();
						for (int i=0;i<sn.length;i++) {
							MyJCheckBoxMenuItem ci=new MyJCheckBoxMenuItem(sn[i]);
							pop.add(ci);
							if (sn[i].equals(ab))
								ci.setSelected(true);
							else
								ci.setSelected(false);
						}
						if (l.length>0)
							pop.add(new JSeparator());
					}
					//Load layers
					for (int i=l.length-1;i>-1;i--) {
						MyJCheckBoxMenuItem mi=new MyJCheckBoxMenuItem(l[i]);
						pop.add(mi);
					}
					//Load agent paths
					try {
						if (agentPlug.getProtocolPlugs().length>0) {
							Iterator agents=mapPane.getMotionPane().getConnectedAgents();
							if (agents.hasNext())
								pop.add(new JSeparator());
							while (agents.hasNext())
								pop.add(new MyJCheckBoxMenuItem((IAgent) agents.next()));
						}
					} catch(Throwable ex) {
						//Agent classes don't exist
					}
					pop.show(layerVisib.getParent(),layerVisib.getX()+layerVisib.getWidth()-1,layerVisib.getY());
				}

				class MyJCheckBoxMenuItem extends JCheckBoxMenuItem {
					MyJCheckBoxMenuItem(ILayerView l) {
						super(l.getName(),l.isVisible());
						this.layer=l;
						started=false;
						block=false;
						//Deactivated layer highlight. Threading is not supported
						//by Point, PolyLine and Polygon objects to improve performance.
						/*addMouseListener(new MouseAdapter() {
							private LoomThread lt;
							public void mouseEntered(MouseEvent e) {
								if (im==null) {
									if (!started) {
										started=true;
										if (working==null)
											working=MapViewer.createCustomCursor("images/workingcursor.gif",new Point(6,6),new Point(6,6),"workingcursor");
										setCursor(working);
										lt=new LoomThread();
										lt.setPriority(Thread.NORM_PRIORITY-1);
										lt.start();
										lt.run();
									} else
										lt.paint=true;
								} else {
									mapPane.getAuxiliaryPane().getGraphics().drawImage(im,0,0,mapPane.getAuxiliaryPane());
									if (mapPane.isLegendVisible())
										mapPane.repaintLegend();
								}
							}
							public void mouseExited(MouseEvent e) {
								lt.paint=false;
								mapPane.paintImmediately(mapPane.getVisibleRect());
							}
							public void mousePressed(MouseEvent e) {
								lt.paint=false;
								mapPane.paintImmediately(mapPane.getVisibleRect());
							}
							//This thread begins loom. If someone makes paint=false while the thread is working
							//nothing will be painted.
							class LoomThread extends Thread {
								boolean paint=true;
								public void run() {
								 //Paints an Image which shows a set of objects on top of everything. The objects are highlighted.
									Array objectsToLoom=layer.getGeographicObjects();
									Component aux=mapPane.getAuxiliaryPane();
									BufferedImage bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(aux.getWidth(),aux.getHeight(),Transparency.TRANSLUCENT);
									Graphics2D g2=(Graphics2D) bi.getGraphics();
									g2.transform(mapPane.getTransform());
									//Set rendering hints
									g2.setRenderingHints(mapPane.getRenderingHints());
									mapPane.loom(g2,layer,objectsToLoom);
									if (paint) {
										mapPane.getAuxiliaryPane().getGraphics().drawImage(bi,0,0,mapPane.getAuxiliaryPane());
										if (mapPane.isLegendVisible())
											mapPane.repaintLegend();
									}
									if (im!=null) {
										im.flush();
										im=null;
									}
									if (block) {
										bi.flush();
										bi=null;
									} else
										im=bi;
									g2.dispose();
									setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
								}
							};

						});*/
						addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								layer.setVisible(!layer.isVisible());
								mapPane.layers.redrawImage();
							}
						});
					}
					MyJCheckBoxMenuItem(Object oag) {
						super();
						try {
							this.agent=(IAgent) oag;
							setText(((IAgent) agent).getName()+": "+messagesBundle.getString("agentpath"));
							setSelected(mapPane.getMotionPane().hasPathVisible((IAgent) agent));
							addMouseListener(new MouseAdapter() {
								public void mouseEntered(MouseEvent e) {
									mapPane.getMotionPane().locateAgent((IAgent) agent);
								}
							});
							addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									mapPane.getMotionPane().setPathVisible((IAgent) agent,!mapPane.getMotionPane().hasPathVisible((IAgent) agent));
								}
							});
						} catch(Throwable t) {}
					}
					MyJCheckBoxMenuItem(String s) {
						super(s);
						addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								map.getActiveRegionView().setBackground(getText());
							}
						});
					}
					void clear() {
						block=true;
						if (im!=null) {
							im.getGraphics().dispose();
							im.flush();
							im=null;
						}
					}
					Image im;
					ILayerView layer;
					//It is an object to avoid loading IAgent class, which can lead to a NoClassDefFoundError
					private Object agent;
					private boolean started;
					private boolean block;
				};
			});
		}
		if (legend!=null) {
			legend.addItemListener(new ItemListener() {
				boolean lastTime=legend.isSelected();
				public void itemStateChanged(ItemEvent e) {
					if (lastTime==legend.isSelected())
						return;
					lastTime=legend.isSelected();
					Cursor c=getCursor();
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					mapPane.showLegend(legend.isSelected());
					setCursor(c);
				}
			});
		}
		//JToggleTool(loadImageIcon("images/legendgr.gif"),toolbarTipBundle.getString("legend"),null,this,null,null,true);
		if (miniature!=null) {
			miniature.addActionListener(getAction(MINIATURE_TOOL_NAME));
		}
		if (grid!=null) {
			grid.addActionListener(getAction(GRID_TOOL_NAME));
		}
		if (spot!=null) {
			spot.addActionListener(getAction(SPOT_TOOL_NAME));
		}
		if (zoomrect!=null)
			zoomrect.addActionListener(getAction(ZOOM_TO_RECT_TOOL_NAME));
//		addListenersOfSelectedTools();
	}
	/**
	 * Listens to control key.
	 */
	protected void processKeyEvent(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
			if (e.getID()==KeyEvent.KEY_PRESSED) {
				if (zoomrect!=null) {
					if (zoomrect.isSelected()) {
						setMessage(toolbarTipBundle.getString("zoomRectcontrolhelp"),0);
						mapPane.setCursor(mapPane.getCustomCursor("zoomoutrectcursor"));
					}
				}
			} else if (e.getID()==KeyEvent.KEY_RELEASED) {
				if (zoomrect!=null) {
					if (zoomrect.isSelected()) {
						setMessage(toolbarTipBundle.getString("zoomRecthelp"),0);
						mapPane.setCursor(mapPane.getCustomCursor("zoomrectcursor"));
					}
				}
			}
		}
		super.processKeyEvent(e);
	}
	/**
	 * Adds the listeners of the legended tools, probably after loading from
	 * a microworld with a tool activated.
	 */
	private void addListenersOfSelectedTools() {
		if (toolbar!=null) {
			try {
				((ActionListener)((DefaultButtonModel)toolbar.getButtonGroup(0).getSelection()).getListeners(ActionListener.class)[0]).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,""));
			} catch(Exception ex) {/*Something can be null*/}
		}
	}
	/**
	 * Apply the common adjustments to the toolbar tools.
	 */
	private void setUpTool(AbstractButton b,ImageIcon icon,String tooltip,String help) {
		b.setIcon(icon);
		if (b instanceof MapViewerButton)
			((MapViewerButton)b).clearIconFlag();
		else
			((MapViewerToggleButton)b).clearIconFlag();
		if (!((ToolComponent) b).isToolTipTextChanged()) {
			b.setToolTipText(tooltip);
			((ToolComponent) b).clearToolTipTextFlag();
		}
		b.setAlignmentX(CENTER_ALIGNMENT);
		b.setAlignmentY(CENTER_ALIGNMENT);
		b.setMargin(new Insets(1,1,1,1));
		b.setPreferredSize(ToolComponent.SIZE);
		b.setFocusPainted(false);
		b.setRequestFocusEnabled(false);
		b.setOpaque(false);
		b.setEnabled(false);
		if (((ToolComponent) b).isHelpTextChanged())
			toolbar.setAssociatedText(b,((ToolComponent) b).getHelpText());
		else if (help!=null)
			toolbar.setAssociatedText(b,help);
	}
	/**
	 * Returns the tool (the widget itself) described by the name in the parameter.
	 * @param toolName  One of the predefined tool names defined in the class, e.g. NAVIGATE_TOOL_NAME.
	 * @return  The tool widget.
	 */
	public Component getTool(String toolName) {
		if (toolName==null) {
			return null;
		} else if (SELECTRECT_TOOL_NAME.equals(toolName)) {
			return select;
		} else if (SELECTCIRCLE_TOOL_NAME.equals(toolName)) {
			return selectCircle;
		} else if (NAVIGATE_TOOL_NAME.equals(toolName)) {
			return navigate;
		} else if (ACTIVATE_TOOL_NAME.equals(toolName)) {
			return activate;
		} else if (BROWSE_TOOL_NAME.equals(toolName)) {
			return browse;
		} else if (PAN_TOOL_NAME.equals(toolName)) {
			return pan;
		} else if (INSERT_OBJECT_TOOL_NAME.equals(toolName)) {
			return insObject;
		} else if (GO_IN_TOOL_NAME.equals(toolName)) {
			return goin;
		} else if (GO_OUT_TOOL_NAME.equals(toolName)) {
			return goout;
		} else if (ROTATE_TOOL_NAME.equals(toolName)) {
			return rotate;
		} else if (METER_TOOL_NAME.equals(toolName)) {
			return meter;
		} else if (IDENTIFY_TOOL_NAME.equals(toolName)) {
			return identify;
		} else if (MINIATURE_TOOL_NAME.equals(toolName)) {
			return miniature;
		} else if (GRID_TOOL_NAME.equals(toolName)) {
			return grid;
		} else if (SPOT_TOOL_NAME.equals(toolName)) {
			return spot;
		} else if (ZOOM_TO_RECT_TOOL_NAME.equals(toolName)) {
			return zoomrect;
		} else {
			throw new IllegalArgumentException("MapViewer.getTool(): Tool "+toolName+" does not exist in Map Viewer.");
		}
	}
	/**
	 * Returns the action associated with a tool. This action shall be used anywhere
	 * inside or outside the component, which will behave exactly as if the tool
	 * has been activated.
	 */
	public Action getAction(String toolName) {
		if (NAVIGATE_TOOL_NAME.equals(toolName)) {
			if (navigateAction==null) {
				navigateAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getNavigateMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(navigate),0);
					}
				};
			}
			return navigateAction;
		} else if (ACTIVATE_TOOL_NAME.equals(toolName)) {
			if (activateAction==null) {
				activateAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getActivateMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(activate),0);
					}
				};
			}
			return activateAction;
		} else if (BROWSE_TOOL_NAME.equals(toolName)) {
			if (browseAction==null) {
				browseAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getBrowseMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(browse),0);
						checkScrollPan();
					}
				};
			}
			return browseAction;
		} else if (SELECTRECT_TOOL_NAME.equals(toolName)) {
			if (selectRectAction==null) {
				selectRectAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getSelectRectangleMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(select),0);
						setSelectionShapeType(SelectionShape.RECTANGULAR_SELECTION_SHAPE);
					}
				};
			}
			return selectRectAction;
		} else if (SELECTCIRCLE_TOOL_NAME.equals(toolName)) {
			if (selectCircleAction==null) {
				selectCircleAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getSelectCircleMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(selectCircle),0);
						setSelectionShapeType(SelectionShape.CIRCULAR_SELECTION_SHAPE);
					}
				};
			}
			return selectCircleAction;
		} else if (PAN_TOOL_NAME.equals(toolName)) {
			if (panAction==null) {
				panAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getPanMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(pan),0);
						checkPanTool();
					}
				};
			}
			return panAction;
		} else if (INSERT_OBJECT_TOOL_NAME.equals(toolName)) {
			if (insObjectAction==null) {
				insObjectAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getInsertObjectMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(insObject),0);
					}
				};
			}
			return insObjectAction;
		} else if (GO_IN_TOOL_NAME.equals(toolName)) {
			if (goinAction==null) {
				goinAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getGoInMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						//Usable when the action is called from outside the component
						//to draw the inner region zoom rectangles as well
						goinActionPerformed=true;
						mapPane.removeAllListeners();
						//Reversed the order with removeAllListeners because the latter
						//removes the rectangles. Necessary when the go in action is called
						//outside the component. If removeAllListeners does not clear the
						//rectangles, they remain even when the tool is deselected.
						mapPane.showInnerRegionsRectangles();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						if (statusbar!=null && toolbar!=null && goin!=null)
							setMessage(toolbar.getAssociatedText(goin),0);
					}
				};
			}
			return goinAction;
		} else if (GO_OUT_TOOL_NAME.equals(toolName)) {
			if (gooutAction==null) {
				gooutAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getGoOutMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbarTipBundle.getString("goOuthelp")+" \""+map.getOuterRegionName(map.getActiveRegionView())+"\".",0);
					}
				};
			}
			return gooutAction;
		} else if (ROTATE_TOOL_NAME.equals(toolName)) {
			if (rotateAction==null) {
				rotateAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getRotateMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(rotate),0);
					}
				};
			}
			return rotateAction;
		} else if (METER_TOOL_NAME.equals(toolName)) {
			if (meterAction==null) {
				meterAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getMeterMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(meter),0);
					}
				};
			}
			return meterAction;
		} else if (IDENTIFY_TOOL_NAME.equals(toolName)) {
			if (identifyAction==null) {
				identifyAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getIdentifyMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(identify),0);
					}
				};
			}
			return identifyAction;
		} else if (MINIATURE_TOOL_NAME.equals(toolName)) {
			if (miniatureAction==null) {
				miniatureAction=new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						if (miniature.isSelected()) {
							setMessage(toolbar.getAssociatedText(miniature),0);
							if (miniaturePane==null) {
								miniaturePane=new MiniaturePane(mapPane);
								mapPane.add(miniaturePane,new Integer(Integer.MAX_VALUE));
								miniaturePane.imageChanged(map);
								miniaturePane.setBounds(mapPane.getInsets().left,mapPane.getHeight()-mapPane.getInsets().bottom-miniaturePane.getHeight(),miniaturePane.getWidth(),miniaturePane.getHeight());
								map.addMapListener(miniaturePane.mapListener);
							}
							miniaturePane.setVisible(true);
							miniaturePane.repaint();
							//Change the location of the legend if it is covered by the miniature
							if (mapPane.isLegendVisible()) {
								Point p=SwingUtilities.convertPoint(mapPane.getLegend(),10,10,miniaturePane);
								if (miniaturePane.contains(p))
									mapPane.getLegend().setLocation(0,0);
							}
						} else {
							setMessage(" ",0);
							miniaturePane.setVisible(false);
							mapPane.repaint();
						}
					}
				};
			}
			return miniatureAction;
		} else if (GRID_TOOL_NAME.equals(toolName)) {
			if (gridAction==null) {
				gridAction=new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						setGridVisible(grid.isSelected());
					}
				};
			}
			return gridAction;
		} else if (SPOT_TOOL_NAME.equals(toolName)) {
			if (spotAction==null) {
				spotAction=new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						JFrame f=new JFrame();
						f.setTitle(((map.getActiveRegionView().getName()==null)?"":map.getActiveRegionView().getName()+": ")+MapViewer.messagesBundle.getString("mapspot"));
						f.setResizable(false);
						f.getContentPane().setLayout(new BorderLayout());
						f.getContentPane().add(new JLabel(map.getMapRoot().getBackground()) {
							private int x,y,width,height;
							{
								//Calculate the pixel positions of the rectangle coordinates
								Rectangle2D.Double r=(Rectangle2D.Double) map.getMapRoot().getBoundingRect();
								Rectangle2D.Double c=(Rectangle2D.Double) map.getActiveRegionView().getBoundingRect();
								//Find the Pixel over Degree fraction
								double px=map.getMapRoot().getBackground().getIconWidth()/r.getWidth();
								double py=map.getMapRoot().getBackground().getIconHeight()/r.getHeight();
								//With the "max" a rectangle, no matter how small the region is, is ensured.
								width=Math.max((int)Math.round(px*c.width),3);
								height=Math.max((int)Math.round(py*c.height),3);
								x=(int)Math.round(px*(c.x-r.x));
								y=map.getMapRoot().getBackground().getIconHeight()-height-(int)Math.round(py*(c.y-r.y));
							}
							public void paintComponent(Graphics g) {
								super.paintComponent(g);
								//Paint a yellow antialiased rectangle with a semitransparent black outline.
								Graphics2D g2=(Graphics2D) g;
								g2.setStroke(new BasicStroke(5));
								g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
								g2.setPaint(new Color(0,0,0,150));
								g2.drawRect(x,y,width,height);
								g2.setStroke(new BasicStroke(3));
								g2.setPaint(Color.yellow);
								g2.drawRect(x,y,width,height);
							}
						},BorderLayout.CENTER);
						f.pack();
						Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
						f.setLocation((s.width-f.getWidth())/2,(s.height-f.getHeight())/2);
						f.setVisible(true);
					}
				};
			}
			return spotAction;
		} else if (ZOOM_TO_RECT_TOOL_NAME.equals(toolName)) {
			if (zoomrectAction==null) {
				zoomrectAction=new AbstractAction() {
					MouseInputListener mouseListener=mapPane.getZoomInToRectMouseListener();
					public void actionPerformed(ActionEvent e) {
						if (map==null)
							return;
						mapPane.setToolTipText(null);
						mapPane.removeAllListeners();
						mapPane.getForegroundComponent().addMouseInputListener(mouseListener);
						setMessage(toolbar.getAssociatedText(zoomrect),0);
					}
				};
			}
			return zoomrectAction;
		} else {
			throw new IllegalArgumentException("MapViewer.getAction(): Tool "+toolName+" does not exist in Map Viewer.");
		}
	}
	/**
	 * Returns an array containing the current locations of the tools in the
	 * toolbar.
	 * @return	An array containing the current locations of the tools in the
	 *		toolbar.
	 */
	private ToolLocation[] getToolLocations() {
		ToolLocation loc[] = new ToolLocation[19];
// GT - start
		if (toolbar==null)
			return loc;
// GT -end
		loc[0]=toolbar.locateTool(navigate);
		loc[1]=toolbar.locateTool(activate);
		loc[2]=toolbar.locateTool(browse);
		loc[3]=toolbar.locateTool(select);
		loc[4]=toolbar.locateTool(selectCircle);
		loc[5]=toolbar.locateTool(pan);
		loc[6]=toolbar.locateTool(insObject);
		loc[7]=toolbar.locateTool(goin);
		loc[8]=toolbar.locateTool(goout);
		loc[9]=toolbar.locateTool(rotate);
		loc[10]=toolbar.locateTool(meter);
		loc[11]=toolbar.locateTool(identify);
		loc[12]=toolbar.locateTool(layerVisib);
		loc[13]=toolbar.locateTool(legend);
		loc[14]=toolbar.locateTool(miniature);
		loc[15]=toolbar.locateTool(grid);
		loc[16]=toolbar.locateTool(spot);
		loc[17]=toolbar.locateTool(zoomrect);
		loc[18]=toolbar.locateTool(zoom);

		return loc;
	}
	/**
	 * Identifies the tools in a toolbar that has just been loaded, updating
	 * the references to these tools.
	 * @param	loc The locations of the tools in the toolbar.
	 */
	private void identifyTools(ToolLocation[] loc) {
		if (toolbar==null)
            return;
		VisualGroup[] groups=toolbar.getVisualGroups();

		if (loc[0]!=null)
			navigate=(MapViewerToggleButton)(groups[loc[0].visualGroup].getComponent(loc[0].toolIndex));
		else
			navigate=null;
		if (loc[1]!=null)
			activate=(MapViewerToggleButton)(groups[loc[1].visualGroup].getComponent(loc[1].toolIndex));
		else
			activate=null;
		if (loc[2]!=null)
			browse=(MapViewerToggleButton)(groups[loc[2].visualGroup].getComponent(loc[2].toolIndex));
		else
			browse=null;
		if (loc[3]!=null)
			select=(MapViewerToggleButton)(groups[loc[3].visualGroup].getComponent(loc[3].toolIndex));
		else
			select=null;
		if (loc[4]!=null)
			selectCircle=(MapViewerToggleButton)(groups[loc[4].visualGroup].getComponent(loc[4].toolIndex));
		else
			selectCircle=null;
		if (loc[5]!=null)
			pan=(MapViewerToggleButton)(groups[loc[5].visualGroup].getComponent(loc[5].toolIndex));
		else
			pan=null;
		if (loc[6]!=null)
			insObject=(MapViewerToggleButton)(groups[loc[6].visualGroup].getComponent(loc[6].toolIndex));
		else
			insObject=null;
		if (loc[7]!=null)
			goin=(MapViewerToggleButton)(groups[loc[7].visualGroup].getComponent(loc[7].toolIndex));
		else
			goin=null;
		if (loc[8]!=null)
			goout=(MapViewerToggleButton)(groups[loc[8].visualGroup].getComponent(loc[8].toolIndex));
		else
			goout=null;
		if (loc[9]!=null)
			rotate=(MapViewerToggleButton)(groups[loc[9].visualGroup].getComponent(loc[9].toolIndex));
		else
			rotate=null;
		if (loc[10]!=null)
			meter=(MapViewerToggleButton)(groups[loc[10].visualGroup].getComponent(loc[10].toolIndex));
		else
			meter=null;
		if (loc[11]!=null)
			identify=(MapViewerToggleButton)(groups[loc[11].visualGroup].getComponent(loc[11].toolIndex));
		else
			identify=null;
		if (loc[12]!=null)
			layerVisib=(MapViewerButton)(groups[loc[12].visualGroup].getComponent(loc[12].toolIndex));
		else
			layerVisib=null;
		if (loc[13]!=null)
			legend=(MapViewerToggleButton)(groups[loc[13].visualGroup].getComponent(loc[13].toolIndex));
		else
			legend=null;
		if (loc[14]!=null)
			miniature=(MapViewerToggleButton)(groups[loc[14].visualGroup].getComponent(loc[14].toolIndex));
		else
			miniature=null;
		if (loc[15]!=null)
			grid=(MapViewerToggleButton)(groups[loc[15].visualGroup].getComponent(loc[15].toolIndex));
		else
			grid=null;
		if (loc[16]!=null)
			spot=(MapViewerButton)(groups[loc[16].visualGroup].getComponent(loc[16].toolIndex));
		else
			spot=null;
		if (loc[17]!=null)
			zoomrect=(MapViewerToggleButton)(groups[loc[17].visualGroup].getComponent(loc[17].toolIndex));
		else
			zoomrect=null;
		if (loc[18]!=null) {
			zoom=(ZoomSlider)(groups[loc[18].visualGroup].getComponent(loc[18].toolIndex));
			zoom.viewer=this;
		} else
			zoom=null;
	}

	public void addMapViewerListener(MapViewerListener l) {
		if (listeners==null)
			listeners=new MapViewerEventMulticaster();
		listeners.add(l);
	}

	public void removeMapViewerListener(MapViewerListener l) {
		listeners.remove(l);
		if (listeners.size()==0)
			listeners=null;
	}

	public void addSelectionShapeTypeListener(SelectionShapeTypeListener l) {
		if (selectionShapeTypeListener!=null)
			selectionShapeTypeListener.add(l);
	}

	public void removeSelectionShapeTypeListener(SelectionShapeTypeListener l) {
		if (selectionShapeTypeListener!=null)
			selectionShapeTypeListener.remove(l);
	}

	public void addSelectionShapeListener(SelectionShapeListener l) {
		if (selectionShapeListener!=null)
			selectionShapeListener.add(l);
	}

	public void removeSelectionShapeListener(SelectionShapeListener l) {
		if (selectionShapeListener!=null)
			selectionShapeListener.remove(l);
	}


	/**
	 * Adds an agent in the pending list. If the component is not is the agent-first-time-positioning
	 * mode for another agent, this agent is added immediately. If it is, the agent is added
	 * as soon as the previous positioning ends.
	 */
	void addPendingAgent(Plug ap) {
		IAgent ag=(IAgent) ((ProtocolPlug)ap).getProtocolImplementor();
		try {
			//If a map doesn't have a valid coordinate space, the agent cannot be plugged.
			if (mapPane.map.getActiveRegionView().getBoundingRect()==null)
//            if (!mapPane.hasValidCoordinates())
				throw new Exception();
			if (mapPane.getMotionPane().hasPendingAgent())
				pendingAgents.add(ap);
			else
				mapPane.getMotionPane().addAgent(ag);
		} catch(MotionPane.AgentNotAddedException e1) {
			if (e1.showDialog)
				JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,this),MapViewer.messagesBundle.getString("requestedlayernotpresent"),ag.getName(),JOptionPane.ERROR_MESSAGE);
			try {
				agentPlug.disconnectPlug(ap);
			} catch(PlugNotConnectedException pnc) {}
		} catch(Throwable e2) {
			e2.printStackTrace();
			JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,this),messagesBundle.getString("agentcantconnect"),ag.getName(),JOptionPane.ERROR_MESSAGE);
			try {
				agentPlug.disconnectPlug(ap);
			} catch(PlugNotConnectedException pnc) {}
		}
	}
	/**
	 * Called by the motion pane to inform that a pending agent has been added. If
	 * more pending agents exist, they are added in a fifo order.
	 */
	void pendingAgentAdded() {
		if (pendingAgents.size()>0) {
			Plug ag=(Plug) pendingAgents.get(0);
			pendingAgents.remove(0);
			addPendingAgent(ag);
		}
		if (navigate!=null)
			navigate.setEnabled(true);
		if (layerVisib!=null)
			layerVisib.setEnabled(true);
	}

	/**
	 * @return  The IMapView object this viewer shows.
	 */
	public IMapView getMap() {
		return map;
	}
	/**
	 * Printing utility.
	 * @param title The page title.
	 */
	public void print(String title) {
		print(0,0,0,0,true,false,1,title,true,true);
	}
	/**
	 * Printing utility
	 * @param   top The margin, in cm, at the top of the image. Ignored if centerOnPage == true or fitToPage == true.
	 * @param   left The margin, in cm, at the left of the image. Ignored if centerOnPage == true or fitToPage == true.
	 * @param   bottom The margin, in cm, at the bottom of the image. Ignored if centerOnPage == true or fitToPage == true.
	 * @param   right The margin, in cm, at the right of the image. Ignored if centerOnPage == true or fitToPage == true.
	 * @param   centerOnPage Specifies whether the image will be centered on the page.
	 * @param   fitToPage Specifies whether the image will be stretched/shrunk to cover the entire page.
	 * @param   scale The scale of the printing. Set to 1.0 to print the image at its original size.
	 * @param   title Title to be printed above the image. It can be null
	 * @param   showTitle Specifies whether the title will be printed.
	 * @param   showPageNumbers Specifies whether page numbers will be printed.
	 */
	public void print(double top,double left,double bottom,double right,boolean centerOnPage,boolean fitToPage,double scale,String title,boolean showTitle,boolean showPageNumbers) {
		BufferedImage img=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(mapPane.getWidth(),mapPane.getHeight(),Transparency.TRANSLUCENT);
		Graphics2D g=img.createGraphics();
		mapPane.getLayersPane().paintComponent(g);
		if (mapPane.aux!=null)
			mapPane.getAuxiliaryPane().paintComponent(g);
		if (mapPane.labels!=null)
			mapPane.getLabelPane().paintComponent(g);
		if (mapPane.motion!=null)
			mapPane.getMotionPane().paintComponent(g);
		String file=System.getProperty("java.io.tmpdir")+"tempMapFile1.jpg";
		System.out.println("Image File used for print located at : "+ file);
		try {
			FileOutputStream fout=new FileOutputStream(file);
			NewRestorableImageIcon restorableIcon=new NewRestorableImageIcon(img);
			restorableIcon.saveImage(NewRestorableImageIcon.JPG,fout);
			fout.close();
			Print.printImage(file,top,left,bottom,right,centerOnPage,fitToPage,scale,title,showTitle,showPageNumbers);
		} catch(Exception e) {System.out.println(e);}
		File f=new File(file);
		f.deleteOnExit();
		//Reclaim used memory
		g.dispose();
		img.flush();
	}

	public void printAll(String title){ //////not in use yet! (N)
		Icon ico = getMap().getActiveRegionView().getBackground();
		int zoom = (new Double(mapPane.getZoom())).intValue();
		int x = (int) Math.round(mapPane.getTransform().getTranslateX());
		int y = (int) Math.round(mapPane.getTransform().getTranslateY());
		//System.out.println("X:"+x+",Y:"+y);
		int width = getWidth();
		int height = getHeight();
		BufferedImage img=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ico.getIconWidth()*zoom,ico.getIconHeight()*zoom,Transparency.TRANSLUCENT);
		BufferedImage img2=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(getWidth(),getHeight(),Transparency.TRANSLUCENT);

		Graphics2D g2 = img2.createGraphics();
		Graphics2D g3 = img.createGraphics();

		g2.setBackground(Color.white);
		g2.clearRect(0,0,width, height);
		g2.setBackground(Color.white);
		g2.clearRect(0,0,ico.getIconWidth()*zoom,ico.getIconWidth()*zoom);
		//mapPane.getLayersPane().paintEntireMapComponent(g2);
		//Add again to add printing: mapPane.getLayersPane().paintEntireMap(g3);
		g2.drawImage(img,x,y,Color.white,null);
		String file = System.getProperty("java.io.tmpdir")+"tempMapFile1.jpg";
		System.out.println("Image File used for print located at : "+ file);
		try{
			FileOutputStream fout = new FileOutputStream(file);
			NewRestorableImageIcon restorableIcon = new NewRestorableImageIcon(img);
			restorableIcon.saveImage(NewRestorableImageIcon.JPG, fout);
			fout.close();
//            Print.printImage(file, 0, 0, 0, 0, false, false, 1,title, true, true);
			//Print.printImage(file, new Insets(0,0,0,0),1, 0,100);
		}catch(Exception e) {System.out.println(e);}
		File f = new File(file);
		f.deleteOnExit();
	}

	public void setBounds(int x,int y,int w,int h) {
		super.setBounds(x,y,w,h);
		if (miniaturePane!=null)
			miniaturePane.setBounds(mapPane.getInsets().left,mapPane.getHeight()-mapPane.getInsets().bottom-miniaturePane.getHeight(),miniaturePane.getWidth(),miniaturePane.getHeight());
	}
	/**
	 * Checks when the pan tool and the scroll-pan buttons will be visible.
	 */
	void checkPanTool() {
		//On initialization
		if (toolbar==null || mapPane==null || mapPane.map==null)
			return;
		IRegionView nu,nd,nl,nr;
		if (map==null || map.getActiveRegionView()==null) {
			checkScrollPan(null,null,null,null);
			if (pan!=null)
				pan.setEnabled(false);
			return;
		}
		nu=map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_UP);
		nd=map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_DOWN);
		nl=map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_LEFT);
		nr=map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_RIGHT);
		if (!mapPane.fitsOK() || nu!=null || nd!=null || nl!=null || nr!=null) {
			if (pan != null)
				pan.setEnabled(true);
			if (nu!=null)
				mapPane.getAuxiliaryPane().getUp().setToolTipText(messagesBundle.getString("scrollpan")+" : "+nu.getName());
			if (nd!=null)
				mapPane.getAuxiliaryPane().getDown().setToolTipText(messagesBundle.getString("scrollpan")+" : "+nd.getName());
			if (nl!=null)
				mapPane.getAuxiliaryPane().getLeft().setToolTipText(messagesBundle.getString("scrollpan")+" : "+nl.getName());
			if (nr!=null)
				mapPane.getAuxiliaryPane().getRight().setToolTipText(messagesBundle.getString("scrollpan")+" : "+nr.getName());
		} else{
			if (pan!=null)
				pan.setEnabled(false);
		}
		checkScrollPan(nu,nd,nl,nr);
	}
	/**
	 * Shows or hides the scroll-pan buttons when pan tool is selected.
	 */
	void checkScrollPan() {
		checkScrollPan(map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_UP),map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_DOWN),
					   map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_LEFT),map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_RIGHT));
	}
	/**
	 * Shows or hides the scroll-pan buttons when pan tool is selected.
	 */
	private void checkScrollPan(IRegionView nu,IRegionView nd,IRegionView nl,IRegionView nr) {
		if (mapPane.aux==null)
			return;
		if ((pan!=null && pan.isSelected()) || (browse!=null && browse.isSelected())) {
//System.out.println("Up neighbor "+nu);
//System.out.println("Down neighbor "+nd);
//System.out.println("Left neighbor "+nl);
//System.out.println("Right neighbor "+nr);
			if (nu!=null)
				mapPane.getAuxiliaryPane().getUp().setVisible(true);
			else if (mapPane.getAuxiliaryPane().hasUp())
				mapPane.getAuxiliaryPane().getUp().setVisible(false);
			if (nd!=null)
				mapPane.getAuxiliaryPane().getDown().setVisible(true);
			else if (mapPane.getAuxiliaryPane().hasDown())
				mapPane.getAuxiliaryPane().getDown().setVisible(false);
			if (nl!=null)
				mapPane.getAuxiliaryPane().getLeft().setVisible(true);
			else if (mapPane.getAuxiliaryPane().hasLeft())
				mapPane.getAuxiliaryPane().getLeft().setVisible(false);
			if (nr!=null)
				mapPane.getAuxiliaryPane().getRight().setVisible(true);
			else if (mapPane.getAuxiliaryPane().hasRight())
				mapPane.getAuxiliaryPane().getRight().setVisible(false);
		} else {
			if (mapPane.getAuxiliaryPane().hasUp())
				mapPane.getAuxiliaryPane().getUp().setVisible(false);
			if (mapPane.getAuxiliaryPane().hasDown())
				mapPane.getAuxiliaryPane().getDown().setVisible(false);
			if (mapPane.getAuxiliaryPane().hasLeft())
				mapPane.getAuxiliaryPane().getLeft().setVisible(false);
			if (mapPane.getAuxiliaryPane().hasRight())
				mapPane.getAuxiliaryPane().getRight().setVisible(false);
		}
	}
	/**
	 * Checks when the go-in tool will be visible.
	 */
	private void checkGoInTool() {
		//Go in tool
		if (goin==null) {
			//True when the goin action is called outside the component
			if (goinActionPerformed) {
				goinActionPerformed=false;
				if (map.hasInnerRegions(map.getActiveRegionView()))
					mapPane.showInnerRegionsRectangles();
			}
			return;
		}
		if (map.hasInnerRegions(map.getActiveRegionView())) {
			goin.setEnabled(true);
			if (goin.isSelected() || goinActionPerformed)
				mapPane.showInnerRegionsRectangles();
		} else {
			if (goin.isSelected() || goinActionPerformed)
				toolbar.getButtonGroup(0).setSelected(null,true);
			goin.setEnabled(false);
		}
		goinActionPerformed=false;
	}
	/**
	 * Sets the map asynchronously and not in the connection.
	 */
	public void setMap(IMapView map) {
		if (this.map!=null)
			freeUpResources();
		this.map=map;
		map.addEnchancedMapListener(MapViewer.this);
		if (miniaturePane!=null)
			map.addMapListener(miniaturePane.mapListener);
		initializeMap(map,true);
		mapPane.repaint();
	}
	/**
	 * Sets the foreground to all the contained components.
	 */
	public void setForeground(java.awt.Color color) {
		super.setForeground(color);
		//Null only in the initialization
		if (menu!=null) {
			MenuElement[] elem=menu.getSubElements();
			for (int i=0;i<elem.length;i++)
				try {
					((JComponent) elem[i].getComponent()).setForeground(color);
				} catch(ClassCastException e) {
					/*OK, probably not a lightweight component.*/
				}
			statusbar.setForeground(color);
		}
	}

	public MapPane getMapPane() {
		return mapPane;
	}

	/**
	 * This method returns the component that represents an agent in this viewer.
	 * @param   name    The agent name.
	 */
	public Component getAgentComponent(String name) {
		if (mapPane.motion!=null)
			return mapPane.motion.getAgentComponent(name);
		return null;
	}

	/**
	 * This method returns an agent in this viewer.
	 * @param   name    The agent name.
	 */
	public Object getAgent(String name) {
		//Returns an object to avoid loading the class when it does not exist.
		if (mapPane.motion!=null)
			return mapPane.motion.getAgent(name);
		return null;
	}

	/**
	 * This method returns the inverse transformation used for positioning.
	 */
	public AffineTransform getInversePositionTransform() {
		return mapPane.getInversePositionTransform();
	}

	/**
	 * This method returns the transformation used for positioning.
	 */
	public AffineTransform getPositionTransform() {
		return mapPane.getPositionTransform();
	}

	/**
	 * Defines where the toolbar should be placed.
	 * 0 for NORTH, 1 for SOUTH, 2 for EAST, 3 for WEST.
	 */
	public void setToolBarPosition(int pos) {
		if (toolbar == null) return; // GT
		switch (pos) {
			case ToolBar.NORTH:
				toolbarPosition=pos;
				toolbar.setOrientation(ToolBar.HORIZONTAL);
				setStatusBarPosition(StatusBar.SOUTH);
				if (isStatusBarVisible())
					add(statusbar,BorderLayout.SOUTH);
				if (isToolBarVisible())
					add(toolbar,BorderLayout.NORTH);
				break;
			case ToolBar.SOUTH:
				toolbarPosition=pos;
				toolbar.setOrientation(ToolBar.HORIZONTAL);
				setStatusBarPosition(StatusBar.NORTH);
				if (isStatusBarVisible())
					add(statusbar,BorderLayout.NORTH);
				if (isToolBarVisible())
					add(toolbar,BorderLayout.SOUTH);
				break;
			case ToolBar.EAST:
				toolbarPosition=pos;
				toolbar.setOrientation(ToolBar.VERTICAL);
				if (isToolBarVisible())
					add(toolbar,BorderLayout.EAST);
				break;
			case ToolBar.WEST:
				toolbarPosition=pos;
				toolbar.setOrientation(ToolBar.VERTICAL);
				if (isToolBarVisible())
					add(toolbar,BorderLayout.WEST);
				break;
		}
		revalidate();
	}

	///////////GETTER-SETTER METHODS FOR NEW TOOLS VISIBILITY (N)////////////////////

	public void setBrowseToolVisible(boolean b) {
		if (browse != null) // GT
			browse.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isBrowseToolVisible() {
		if (browse == null) return false; // GT
		return browse.isVisible();
	}

	public void setCircleSelectToolVisible(boolean b) {
		if (selectCircle != null) // GT
			selectCircle.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isCircleSelectToolVisible() {
		if (selectCircle == null) return false; // GT
		return selectCircle.isVisible();
	}

	/////////////////////////////////////////////////////////////////////////////

	public void setBusyIconVisible(boolean b){
	  if (statusbar != null)
		 statusbar.setBusyIconPanelVisible(b);
	}

	public boolean isBusyIconVisible(){
	   return statusbar.isBusyIconPanelVisible();
	}

	/**
	 * Returns where toolbar is placed.
	 * 0 for NORTH, 1 for SOUTH, 2 for EAST, 3 for WEST.
	 */
	public int getToolBarPosition() {
		return toolbarPosition;
	}
	/**
	 * Shows or hides the statusbar.
	 */
	public void setToolBarVisible(boolean value) {
		if (value)
			switch (getToolBarPosition()) {
				case ToolBar.NORTH:
					add(toolbar,BorderLayout.NORTH);
					break;
				case ToolBar.SOUTH:
					add(toolbar,BorderLayout.SOUTH);
					break;
				case ToolBar.EAST:
					add(toolbar,BorderLayout.EAST);
					break;
				case ToolBar.WEST:
					add(toolbar,BorderLayout.WEST);
					break;
			}
		else
			remove(toolbar);
		toolbar.setVisible(value);
		revalidate();
	}
	/**
	 * @return The visibility tool of the statusbar.
	 */
	public boolean isToolBarVisible() {
		if (toolbar==null)
			return false;
		else
			return toolbar.isVisible();
	}
	/**
	 * Defines where the statusbar should be placed.
	 * 0 for NORTH, 1 for SOUTH.
	 */
	public void setStatusBarPosition(int pos) {
		switch (pos) {
			case StatusBar.NORTH:
				statusbarPosition=pos;
				if (toolbarPosition==ToolBar.NORTH) {
					setToolBarPosition(ToolBar.SOUTH);
					if (isToolBarVisible())
						add(toolbar,BorderLayout.SOUTH);
				}
				if (isStatusBarVisible())
					add(statusbar,BorderLayout.NORTH);
				break;
			case StatusBar.SOUTH:
				statusbarPosition=pos;
				if (toolbarPosition==ToolBar.SOUTH) {
					setToolBarPosition(ToolBar.NORTH);
					if (isToolBarVisible())
						add(toolbar,BorderLayout.NORTH);
				}
				if (isStatusBarVisible())
					add(statusbar,BorderLayout.SOUTH);
				break;
		}
		revalidate();
	}
	/**
	 * Returns where statusbar is placed.
	 * 0 for NORTH, 1 for SOUTH.
	 */
	public int getStatusBarPosition() {
		return statusbarPosition;
	}
	/**
	 * Shows or hides the statusbar.
	 */
	public void setStatusBarVisible(boolean value) {
		statusbarVisible=value;
		if (value)
			switch (getStatusBarPosition()) {
				case StatusBar.NORTH:
					add(statusbar,BorderLayout.NORTH);
					break;
				case StatusBar.SOUTH:
					add(statusbar,BorderLayout.SOUTH);
					break;
			}
		else
			remove(statusbar);
		statusbar.setVisible(value);
		revalidate();
	}
	/**
	 * @return The visibility status of the statusbar.
	 */
	public boolean isStatusBarVisible() {
		return statusbarVisible;
	}
	/**
	 * Opaqueness the statusbar.
	 */
	public void setStatusBarOpaque(boolean value) {
		statusbar.setOpaque(value);
		statusbar.repaint();
	}
	/**
	 * @return The opaqueness of the statusbar.
	 */
	public boolean isStatusBarOpaque() {
		return statusbar.isOpaque();
	}
	/**
	 * Shows or hides the menubar.
	 */
	public void setMenuBarVisible(boolean value) {
		/*
		menubarVisible=value;
		if (value) {
			if (menu==null) {
				//Menu
				menu=new JMenuBar();
				JMenu manage=new JMenu(menuBundle.getString("manage"));
				JMenuItem clearHistory=new JMenuItem(menuBundle.getString("clearhistory")/*,loadImageIcon("null.gif")/);
				clearHistory.setEnabled(false);
				clearHistory.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				manage.add(clearHistory);
				//menu.add(manage);
				setMenuBarOpaque(false);
			}
			add(menu,BorderLayout.NORTH);
		} else
			remove(menu);*/
	}
	/**
	 * @return The visibility status of the menubar.
	 */
	public boolean isMenuBarVisible() {
		return menubarVisible;
	}
	/**
	 * Makes the menubar opaque or transparent.
	 */
	public void setMenuBarOpaque(boolean value) {
		menu.setOpaque(value);
		MenuElement[] elem=menu.getSubElements();
		for (int i=0;i<elem.length;i++)
			try {
				((JComponent) elem[i].getComponent()).setOpaque(value);
			} catch(ClassCastException e) {
				/*OK, probably not a lightweight component.*/
			}
	}
	/**
	 * @return The opaque-status of the menubar.
	 */
	public boolean isMenuBarOpaque() {
		return menu.isOpaque();
	}
	/**
	 * Gets the E-Slate handle of the component.
	 */
	public ESlateHandle getESlateHandle() {
		if (handle==null) {
			PerformanceManager pm=PerformanceManager.getPerformanceManager();
			pm.eSlateAspectInitStarted(this);
			pm.init(initESlateAspectTimer);

			handle=ESlate.registerPart(this);
			try {
				handle.setUniqueComponentName(messagesBundle.getString("componentname"));
			} catch(Throwable e) {}
			handle.addESlateListener(new ESlateAdapter() {
				/**
				 * This stores the Agent handles of the MapViewer. Some of
				 * these handles will be disposed in the handleDisposed() of the
				 * MapViewer handle. Since there is no way to acquire the
				 * list of child handles in 'handleDisposed()', we get them in
				 * 'handleDisposing()' and store them in this list.
				 */
				private ESlateHandle[] agentHandles = null;

				public void disposingHandle(HandleDisposalEvent e) {
					/*
					 First notify each of the child Agents (Agents whose
					 handles belong to the MapViewer's handle, that they are about
					 to be disposed. The agents won't be given the chance to
					 cancel the disposal.
					 */
					if (agentPlug!=null && agentPlug.getProtocolPlugs().length>0) {
						IAgent[] agents = new IAgent[mapPane.getMotionPane().getConnectedAgentsCount()];

						int ai=0;
						Iterator agentsit=mapPane.getMotionPane().getConnectedAgents();
						while (agentsit.hasNext())
							agents[ai++]=(IAgent) agentsit.next();

						agentHandles = new ESlateHandle[agents.length];
						for (int i=0; i<agents.length; i++)
							agentHandles[i] = ((ESlatePart) agents[i]).getESlateHandle();
						boolean agentModified = false;
						for (int i=0; i<agentHandles.length && !agentModified; i++) {
							// Notify only those Agents whose parent is the MapViewer.
							if (agentHandles[i].getParentHandle() == handle) {
								BooleanWrapper agentStateChangedWrapper = new BooleanWrapper(false);
								agentHandles[i].toBeDisposed(false, agentStateChangedWrapper);
								if (agentStateChangedWrapper.getValue())
									agentModified = true;
							}
						}

						if (agentModified)
							e.stateChanged = true;
					}
				}

				public void handleDisposed(HandleDisposalEvent e) {
					//For old toolbar: toolbar.removeAll();
					statusbar.removeAll();
					if (zoom != null) // GT
						zoom.removeAll();
					removeAll();
					handle.removeESlateListener(this);
					freeUpResources();
					PerformanceManager pm = PerformanceManager.getPerformanceManager();
					pm.removePerformanceListener(perfListener);
					perfListener = null;
				}
			});

			handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.MapViewerPrimitives");
			bundleInfo=java.util.ResourceBundle.getBundle("gr.cti.eslate.mapViewer.BundleInfo",handle.getLocale());
			String[] info={bundleInfo.getString("part"),bundleInfo.getString("development"),bundleInfo.getString("contribution"),bundleInfo.getString("copyright")};
			handle.setInfo(new ESlateInfo(bundleInfo.getString("compo")+" "+version,info));

			//Plug creation is put at the end because the mapPane is needed.
			//Map plug
			try {
				Class protocol=Class.forName("gr.cti.eslate.protocol.IMapView");
				mapPlug=new RightSingleConnectionProtocolPlug(handle,plugBundle,"mapviewer",new Color(112,0,64),protocol);
				mapPlug.addConnectionListener(new ConnectionListener() {
					public void handleConnectionEvent(ConnectionEvent e) {
						try {
//							boolean addListen=map==null;
							map=(IMapView) ((ProtocolPlug)e.getPlug()).getProtocolImplementor();
							connect();
//							if (addListen)
//								addListenersOfSelectedTools();
						} catch(Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,MapViewer.this),messagesBundle.getString("cantconnect"),messagesBundle.getString("wrongtitle"),JOptionPane.ERROR_MESSAGE);
							e.getOwnPlug().disconnect();
						}
					}
				});
				mapPlug.addDisconnectionListener(new DisconnectionListener() {
					public void handleDisconnectionEvent(DisconnectionEvent e) {
						mapPane.getForegroundComponent().removeAllListeners();
						freeUpResources();
						showAgentPlug(false);
						//Remove the time machine plug
						if (timeMachPlug!=null) {
							try {
								handle.removePlug(timeMachPlug);
							} catch(Throwable t) {}
							timeMachPlug=null;
						}
					}
				});
				handle.addPlug(mapPlug);
			} catch(Throwable e1) {
				//No need to define the multiple exceptions. Nothing is done when a plug cannot be created.
				e1.printStackTrace();
			}
			//Agent plug
			try {
				Class protocol=Class.forName("gr.cti.eslate.protocol.IAgent");
				agentPlug=new LeftMultipleConnectionProtocolPlug(handle,plugBundle,"agent",Color.cyan,protocol,new AgentHostImplementor(mapPane));
				agentPlug.setHostingPlug(true);
				//Add connection listener to the plug, to add an agent component to the panel.
				agentPlug.addConnectionListener(new ConnectionListener() {
					public void handleConnectionEvent(ConnectionEvent e) {
						mapPane.getMotionPane().initialize();
						addPendingAgent(e.getPlug());
					}
				});
				//Add disconnection listener to the plug, to remove an agent when disconnected.
				agentPlug.addDisconnectionListener(new DisconnectionListener() {
					public void handleDisconnectionEvent(DisconnectionEvent e) {
						if (mapPane==null || mapPane.motion==null)
							return;
						IAgent agnt=(IAgent) ((ProtocolPlug)e.getPlug()).getProtocolImplementor();
						mapPane.motion.removeAgent(agnt);
						if (navigate != null && agentPlug.getProtocolPlugs().length==0) // GT
							navigate.setEnabled(false);
						//Layer visibility tool
						if (layerVisib!=null) {
							if (shouldShowLamp())
								layerVisib.setEnabled(true);
							else
								layerVisib.setEnabled(false);
						}
					}
				});
				handle.addPlug(agentPlug);
				showAgentPlug(mapPane.hasValidCoordinates());
			} catch(Throwable e1) {
				//No need to define the multiple exceptions. Nothing is done when a plug cannot be created.
			}

			pm.eSlateAspectInitEnded(this);
			pm.stop(initESlateAspectTimer);
			pm.displayTime(initESlateAspectTimer, handle, "", "ms");
			System.out.println("(PerformanceMgr note : E-Slate part created in component constructor");
		}
// GT -end
		return handle;
	}
	/**
	 * This is used by both the plug connection listener and the ancestor added listener.
	 * Put in a method to avoid code duplication and possible errors.
	 */
	private void connect() {
		// Don't paint the plugged Map, if the MapViewer is not showing
		// This check should also be done in other methods which accept a
		// Map. Also freeUpResources() should be checked in case the
		// MapViewer is assigned a Map, which hasn't yet been painted.
		Dimension size=MapViewer.this.getSize();
		if (size.width!=0 && size.height!=0) {
			map.addEnchancedMapListener(MapViewer.this);
			if (miniaturePane!=null)
				map.addMapListener(miniaturePane.mapListener);
			mapPane.invalidate();
			initializeMap(map,true);
			//Remove the ancestor listener which will repaint the map
			//if there is a change in the ancestor hierarchy
			removeAncestorListener(delayedRepaint);
			delayedRepaint=null;
		}
	}
	/**
	 * Gets the statusbar so that it can be seen in the object view of the properties.
	 */
	public StatusBar getStatusBar() {
		return statusbar;
	}
	/**
	 * Gets the toolbar so that it can be seen in the object view of the properties.
	 */
	public ESlateToolBar getToolBar() {
		return toolbar;
	}
	/*
	  Gets the toolbar so that it can be seen in the object view of the properties.
	 /
	public JToggleTool getToolActivate() {
		return activate;
	}*/
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolActivateVisible(boolean b) {
		if (activate != null) // GT
			activate.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolActivateVisible() {
		if (activate != null) return false; // GT
		return activate.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolSelectVisible(boolean b) {
		if (select != null) // GT
			select.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolSelectVisible() {
		if (select == null) return false; // GT
		return select.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolPanVisible(boolean b) {
		if (pan != null) // GT
			pan.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolPanVisible() {
		if (pan == null) return false; // GT
		return pan.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolEditVisible(boolean b) {
		if (insObject != null) // GT
			insObject.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolEditVisible() {
		if (insObject == null) return false; // GT
		return insObject.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolZoomVisible(boolean b) {
		if (zoom != null) // GT
			zoom.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolZoomVisible() {
		if (zoom == null) return false; // GT
		return zoom.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolGoInVisible(boolean b) {
		if (goin != null) // GT
			goin.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolGoInVisible() {
		if (goin == null) return false; // GT
		return goin.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolGoOutVisible(boolean b) {
		if (goout != null) // GT
			goout.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolGoOutVisible() {
		if (goout != null) return false; // GT
		return goout.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolRotateVisible(boolean b) {
		if (rotate != null) // GT
			rotate.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolRotateVisible() {
		if (rotate == null) return false; // GT
		return rotate.isVisible();
	}
	/**
	 * Sets the visibility status of the .
	 */
	public void setToolMiniatureVisible(boolean b) {
		if (miniature != null) // GT
			miniature.setVisible(b);
	}
	/**
	 * Gets the visibility status of the .
	 */
	public boolean isToolMiniatureVisible() {
		if (miniature == null) return false; // GT
		return miniature.isVisible();
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolMeterVisible(boolean b) {
		if (meter != null) // GT
			meter.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolMeterVisible() {
		if (meter == null) return false; // GT
		return meter.isVisible();
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolIdentifyVisible(boolean b) {
		if (identify != null) // GT
			identify.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolIdentifyVisible() {
		if (identify == null) return false; // GT
			return identify.isVisible();
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolLayerVisibilityVisible(boolean b) {
		if (layerVisib != null) // GT
			layerVisib.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolLayerVisibilityVisible() {
		if (layerVisib == null) return false; // GT
		return layerVisib.isVisible();
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolSpotVisible(boolean b) {
		if (spot != null) // GT
			spot.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolSpotVisible() {
		if (spot == null) return false; // GT
		return spot.isVisible();
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolNavigateVisible(boolean b) {
		if (navigate != null) // GT
			navigate.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolLegendVisible() {
		if (legend == null) return false; // GT
		return legend.isVisible();
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolLegendVisible(boolean b) {
		if (legend == null) // GT
			legend.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolNavigateVisible() {
		if (navigate == null) // GT
			return false; // GT
		return navigate.isVisible();
	}
	/**
	 * Sets the antialiasing property.
	 */
	public void setAntialiasing(boolean aa) {
		mapPane.setAntialiasing(aa);
	}
	/**
	 * Gets the antialiasing property.
	 */
	public boolean getAntialiasing() {
		return mapPane.getAntialiasing();
	}
	/**
	 * Sets the quality property. True=quality optimized, False=speed optimized.
	 */
	public void setQualityOverSpeed(boolean q) {
		mapPane.setQualityOverSpeed(q);
	}
	/**
	 * Gets the quality property. True=quality optimized, False=speed optimized.
	 */
	public boolean getQualityOverSpeed() {
		return mapPane.getQualityOverSpeed();
	}
	/**
	 * Setter.
	 */
	public void setShowCoordinates(boolean b) {
		mapPane.showCoordinates(b);
	}
	/**
	 * Getter.
	 */
	public boolean getShowCoordinates() {
		return mapPane.isCoordinatesShown();
	}
	/**
	 * Sets the grid visible or invisible.
	 */
	public void setGridVisible(boolean b) {
		gridVisible=b;
		if (grid != null) // GT
			grid.setSelected(b);
		mapPane.repaint();
	}
	/**
	 * Checks if the grid is visible.
	 */
	public boolean isGridVisible() {
		return gridVisible;
	}
	/**
	 * Sets the grid color.
	 */
	public void setGridColor(Color c) {
		gridColor=c;
		mapPane.repaint();
	}
	/**
	 * Gets the grid color.
	 */
	public Color getGridColor() {
		return gridColor;
	}
	/**
	 * Sets the grid step.
	 */
	public void setGridStep(double d) {
		gridStep=d;
		mapPane.repaint();
	}
	/**
	 * Gets the grid step.
	 */
	public double getGridStep() {
		return gridStep;
	}
	/**
	 * Sets the visibility status of the tool.
	 */
	public void setToolGridVisible(boolean b) {
		if (grid != null) // GT
			grid.setVisible(b);
	}
	/**
	 * Gets the visibility status of the tool.
	 */
	public boolean isToolGridVisible() {
		if (grid == null) return false; // GT
		return grid.isVisible();
	}
	/**
	 * If set to true, an information dialog pops-up when connecting an unpositioned agent.
	 */
	public void setShowAgentPositioningInfoDialog(boolean b) {
		apdi=b;
	}
	/**
	 * If set to true, an information dialog pops-up when connecting an unpositioned agent.
	 */
	public boolean getShowAgentPositioningInfoDialog() {
		return apdi;
	}
	/**
	 * This method gets the MapListener object.
	 */
	public MapListener getMapListener() {
		return mapListener;
	}
	/**
	 * This method gets the RegionListener object.
	 */
	public RegionListener getRegionListener() {
		return regionListener;
	}
	/**
	 * This method gets the RegionListener object.
	 */
	public LayerListener getLayerListener() {
		return layerListener;
	}
	/**
	 * Known method.
	 */
	public void setOpaque(boolean value) {
		super.setOpaque(value);
		repaint();
	}
	/**
	 * Known method.
	 */
	public void setBackground(Color color) {
		super.setBackground(color);
		if (statusbar!=null)
			statusbar.setBackground(color);
		if (toolbar!=null)
			toolbar.setBackground(color);
	}
	/**
	 * Sets the background color of the map when a background image does not exist.
	 * @param color the color.
	 */
	public void setMapBackground(Color color) {
		mapBackground=color;
		repaint();
	}
	/**
	 * Gets the background color of the map when a background image does not exist.
	 * @return the background color.
	 */
	public Color getMapBackground() {
		return mapBackground;
	}
	/**
	 * Shows or hides the agent plug.
	 */
	void showAgentPlug(boolean b) {
		//Is null when the Agent classes don't exist
		if (agentPlug!=null)
			agentPlug.setVisible(b);
	}
	/**
	 * Known method.
	 */
	public void setBorder(Border b) {
		borderChanged=true;
		super.setBorder(b);
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		if (mapPane!=null) {
			mapPane.setFont(f);
			if (mapPane.layers!=null)
				mapPane.layers.redrawImage();
		}
		/*Temporarily removed. Don't want to change all the tooltips also.
		  Also removed from ImageJPanel.
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
		*/
	}
	/**
	 * If true, the viewer paints a cross to indicate the position of the agents.
	 */
	public void setPaintCrossOnAgent(boolean b) {
		paintCrossOnAgent=b;
		if (mapPane.getMotionPane()!=null)
			mapPane.getMotionPane().repaint();
	}
	/**
	 * If true, the viewer paints a cross to indicate the position of the agents.
	 */
	public boolean getPaintCrossOnAgent() {
		return paintCrossOnAgent;
	}
	/**
	 * If true, all zoom rectangles are shown when the go-in tool is pressed.
	 * If false, only the zoom rectangle which has the mouse over it is shown.
	 */
	public void setShowInactiveZoomRects(boolean b) {
		if (b==showInactiveZoomRects)
			return;
		showInactiveZoomRects=b;
		if (b) {
			mapPane.zoomOutline=mapPane.COLOROUTT;
			mapPane.zoomFill=mapPane.COLORFILT;
		} else {
			mapPane.zoomOutline=mapPane.COLOROUTF;
			mapPane.zoomFill=mapPane.COLORFILF;
		}
		if (goin!=null && goin.isSelected())
			mapPane.showInnerRegionsRectangles();
	}
	/**
	 * If true, all zoom rectangles are shown when the go-in tool is pressed.
	 * If false, only the zoom rectangle which has the mouse over it is shown.
	 */
	public boolean getShowInactiveZoomRects() {
		return showInactiveZoomRects;
	}
	/**
	 * Translates a point from pixel coordinates to real map coordinates. The pixel coordinates
	 * should be in the map viewing pane coordinate system.
	 * @param   x   The x dimension of the pixel coordinates.
	 * @param   y   The y dimension of the pixel coordinates.
	 * @return  A point with the coordinates transformed to the map real coordinate system.
	 */
	public Point2D.Double toRealCoordinates(double x,double y) {
		return toRealCoordinates(new Point2D.Double(x,y));
	}
	/**
	 * Translates a point from pixel coordinates to real map coordinates. The pixel coordinates
	 * should be in the map viewing pane coordinate system.
	 * <p>
	 * This method should be used if many tranformations will be needed, passing the same
	 * Point Object as a parameter, to avoid instatiating a new object in each method call.
	 * @param   p   A point in pixel coordinates. This point is changed and contains the transformed coordinates.
	 * @return  A point with the coordinates transformed to the map real coordinate system.
	 */
	public Point2D.Double toRealCoordinates(Point2D.Double p) {
		double[] corcache1=new double[2];
		corcache1[0]=p.x;
		corcache1[1]=p.y;
		mapPane.getInverseTransform().transform(corcache1,0,corcache1,0,1);
		mapPane.getInversePositionTransform().transform(corcache1,0,corcache1,0,1);
		p.x=corcache1[0]; p.y=corcache1[1];
		return p;
	}
	/**
	 * Translates a point from real map coordinates to pixel coordinates. The pixel coordinates
	 * are in the map viewing pane coordinate system.
	 * @param   x   The x dimension of the real coordinates.
	 * @param   y   The y dimension of the real coordinates.
	 * @return  A point with the coordinates transformed to the map real coordinate system.
	 */
	public Point2D.Double toPixelCoordinates(double x,double y) {

		return toPixelCoordinates(new Point2D.Double(x,y));
	}
	/**
	 * Translates a point from real map coordinates to pixel coordinates. The pixel coordinates
	 * are in the map viewing pane coordinate system.
	 * <p>
	 * This method should be used if many tranformations will be needed, passing the same
	 * Point Object as a parameter, to avoid instatiating a new object in each method call.
	 * @param   p   A point in real coordinates. This point is changed and contains the transformed coordinates.
	 * @return  A point with the coordinates transformed to the map real coordinate system.
	 */
	public Point2D.Double toPixelCoordinates(Point2D.Double p) {
		double[] corcache3=new double[2];
		corcache3[0]=p.x;
		corcache3[1]=p.y;
		mapPane.getPositionTransform().transform(corcache3,0,corcache3,0,1);
		mapPane.getTransform().transform(corcache3,0,corcache3,0,1);
		p.x=corcache3[0]; p.y=corcache3[1];
		return p;
	}
	/**
	 * Translates a point from map viewing area pixel coordinates to map background
	 * image pixel coordinates.
	 * @param   x   The x dimension of the map viewing area coordinates.
	 * @param   y   The y dimension of the map viewing area coordinates.
	 * @return  A point with the coordinates transformed to the backgroung image coordinate system.
	 */
	public Point2D.Double toBackgroundPixelCoordinates(int x,int y) {
		return toBackgroundPixelCoordinates(new Point2D.Double(x,y));
	}
	/**
	 * Translates a point from map viewing area pixel coordinates to map background
	 * image pixel coordinates.
	 * <p>
	 * This method should be used if many tranformations will be needed, passing the same
	 * Point Object as a parameter, to avoid instatiating a new object in each method call.
	 * @param   p   A point in map viewing area coordinates. This point is changed and contains the transformed coordinates.
	 * @return  A point with the coordinates transformed to the background image coordinate system.
	 */
	public Point2D.Double toBackgroundPixelCoordinates(Point2D.Double p) {
		double[] corcache5=new double[2];
		corcache5[0]=p.x;
		corcache5[1]=p.y;
		mapPane.getInverseTransform().transform(corcache5,0,corcache5,0,1);
		AffineTransform at=new AffineTransform();
		at.scale(1/mapPane.getZoom(),1/mapPane.getZoom());
		at.transform(corcache5,0,corcache5,0,1);
		p.x=(int) corcache5[0]; p.y=(int) corcache5[1];
		return p;
	}
	/**
	 * Gets an int array with the color components (r,g,b,a) of a given pixel of the
	 * map background image. The x,y pixel coordinates are in the background image coordinate space.
	 * @param   pixelx  The x pixel coordinate in the background image coordinate space.
	 * @param   pixely  The y pixel coordinate in the background image coordinate space.
	 * @return  An array containing the color components of a pixel (r,g,b,a). If an error occurs in the process, the method returns <code>null</code>.
	 */
	public int[] getColorInBackgroundImage(int pixelx,int pixely) {
		try {
			int[] pxls=new int[4];
			PixelGrabber pg=new PixelGrabber(((ImageIcon) mapPane.getLayersPane().getCurrentBackground()).getImage(),pixelx,pixely,1,1,true);
			pg.grabPixels();
			int pxl=((int[]) pg.getPixels())[0];
			if ((pg.getStatus() & ImageObserver.ABORT) != 0)
				return null;
			pxls[0]=(pxl >> 16) & 0xff;
			pxls[1]=(pxl >>  8) & 0xff;
			pxls[2]=(pxl      ) & 0xff;
			pxls[3]=(pxl >> 24) & 0xff;
			return pxls;
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		initialize(in);
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.init(saveTimer);
		super.writeExternal(out);
		ESlateFieldMap2 ht = new ESlateFieldMap2(2);
		if (!(getBackground() instanceof javax.swing.plaf.ColorUIResource))
			ht.put("backcolor",getBackground());
		ht.put("toolbarpos",getToolBarPosition());
		ht.put("toollocations",getToolLocations());
		ht.put("statusbarpos",getStatusBarPosition());
		ht.put("rotation",mapPane.rotation);
		ht.put("antialiasing",getAntialiasing());
		ht.put("quality",getQualityOverSpeed());
		ht.put("coordinates",getShowCoordinates());
		ht.put("gridvisible",isGridVisible());
		ht.put("gridstep",getGridStep());
		ht.put("gridcolor",getGridColor());
		if (borderChanged) {
			try {
				BorderDescriptor bd=ESlateUtils.getBorderDescriptor(getBorder(),this);
				ht.put("border",bd);
			} catch (Throwable thr) {}
		}
		if (!(getFont() instanceof javax.swing.plaf.FontUIResource))
			ht.put("font",getFont());
		ht.put("offsetX",mapPane.offsetX);
		ht.put("offsetY",mapPane.offsetY);
		ht.put("showagentdialog",apdi);
		ht.put("paintcrossonagent",paintCrossOnAgent);
		ht.put("MaximumSize",getMaximumSize());
		ht.put("MinimumSize",getMinimumSize());
		ht.put("PreferredSize",getPreferredSize());
		ht.put("SSType",getSelectionShapeType());
		if (mapPane.getAuxiliaryPane().getSelectionShape()!=null) {
			SelectionShape selectionShape=mapPane.getAuxiliaryPane().getSelectionShape();
			ht.put("SShapeX",selectionShape.getX());
			ht.put("SShapeY",selectionShape.getY());
			ht.put("SShapeEX",selectionShape.getEndX());
			ht.put("SShapeEY",selectionShape.getEndY());
		}
		ht.put("BusyIconVisible",isBusyIconVisible());
		ht.put("showInactiveZoomRects",getShowInactiveZoomRects());
		ht.put("zoomvalue",mapPane.getZoom());
		ht.put("meterpopup",isMeterPopupEnabled());
		ht.put("selectionpopup",isSelectionPopupEnabled());
		ht.put("errorTolerance",errorTolerance);
		ht.put("mapBackground",mapBackground);

		//Save the children agents of the viewer
		try {
			IProtocolPlug[] connAgents=agentPlug.getProtocolPlugs();
			ArrayList storeAgents=new ArrayList();

			for (int i=0; i<connAgents.length; i++) {
				if (((ProtocolPlug) connAgents[i]).getHandle().getParentHandle()==handle)
					storeAgents.add(((ProtocolPlug) connAgents[i]).getHandle());
			}
			ESlateHandle[] agHandles=new ESlateHandle[storeAgents.size()];
			for (int i=0;i<agHandles.length;i++)
				agHandles[i]=(ESlateHandle) storeAgents.get(i);
			handle.saveChildren(ht,"childrenAgents",agHandles);
		} catch(Throwable ex) {}

		//Save the tools
		if (toolbar!=null) {
			try {
				ESlateHandle[] tools=new ESlateHandle[] {toolbar.getESlateHandle()};
				handle.saveChildren(ht,"tools",tools);
			} catch(Throwable ex) {}
		}

		out.writeObject(ht);
		statusbar.writeExternal(out);
		out.flush();
		pm.stop(saveTimer);
		pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
	}
	/**
	 * Loads an image from the jar file.
	 */
	protected ImageIcon loadImageIcon(String filename) {
		try {
			return new ImageIcon(MapViewer.class.getResource(filename));
		} catch(Exception e) {
			System.out.println("Error loading Image Icon '"+filename+"'");
			try {
				return new ImageIcon(MapViewer.class.getResource("images/notfound.gif"));
			} catch(Exception e1) {
			}
		}
		return null;
	}
	/**
	 * Makes all the actions that are needed before showing a map. Handles tool availability.
	 */
	private void initializeMap(final IMapView map,final boolean centerView) {
		if (map==null) return;
		//The toolbar has been restored in readExternal
		if (toolbar==null) {
			createToolBar();
			setUpToolBar(true);
		}
		setMessage("",0);
		mapPane.setBusy(true);
		mapPane.setRedrawEnabled(false);
		mapPane.showMap(map,centerView);
		//Apply pending selection, if any.
		if (pendingSelectionShape!=null) {
			setSelectionShape(pendingSelectionShape);
			pendingSelectionShape=null;
		}
		//Pan tool
		checkPanTool();
		//Go in tool
		checkGoInTool();
		//Go out tool
		if (goout!=null) {
			if (map.getOuterRegion(map.getActiveRegionView())!=null)
				goout.setEnabled(true);
			else {
				goout.setEnabled(false);
				if (goout.isSelected()) {
					toolbar.getButtonGroup(0).setSelected(null,true);
					mapPane.removeAllListeners();
					setMessage("",0);
				}
			}
		}
		//Layer visibility tool
		if (layerVisib!=null) {
			if (shouldShowLamp())
				layerVisib.setEnabled(true);
			else
				layerVisib.setEnabled(false);
		}
		if (activate!=null)
			activate.setEnabled(true);
		if (browse!=null)
			browse.setEnabled(true);
		if (select!=null)
			select.setEnabled(true);
		if (selectCircle!=null)
			selectCircle.setEnabled(true);
		if (insObject!=null)
			insObject.setEnabled(true);
		if (rotate!=null)
			rotate.setEnabled(false); //Disabled the rotate tool temporarily
		if (legend!=null)
			legend.setEnabled(true);
		if (miniature!=null)
			miniature.setEnabled(true);
		if (meter!=null)
			meter.setEnabled(true);
		if (grid!=null)
			grid.setEnabled(true);
		if (zoom!=null)
			zoom.setEnabled(true);
		if (zoomrect!=null)
			zoomrect.setEnabled(true);
		if (spot!=null) {
			if (map.getActiveRegionView().equals(map.getMapRoot()))
				spot.setEnabled(false);
			else
				spot.setEnabled(true);
		}
		if (identify!=null)
			identify.setEnabled(true);
		//Statusbar
		setMessage(" ",0);
		if (map.getActiveRegionView().getBoundingRect()!=null) {
			updateScale();
		}
		//Already done in showMap earlier: miniaturePane.imageChanged(map);
		/*SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Recalculates the clip rect
				mapPane.invalidate();
				mapPane.validate();
				mapPane.repaint();
			}
		});*/
		if (!blockRedraw) {
			if (mapPane.getWidth()>0 && mapPane.getHeight()>0) {
				mapPane.setRedrawEnabled(true);
			} else {
				cl=new ComponentAdapter() {
					public void componentResized(ComponentEvent e) {
						if (mapPane.getWidth()>0 && mapPane.getHeight()>0) {
							mapPane.removeComponentListener(cl);
							cl=null;
							mapPane.setRedrawEnabled(true);
						}
					}
				};
				mapPane.addComponentListener(cl);
				//Trick to load the layers and repaint quicker when the
				//repaint will eventually happen
				for (int i=mapPane.map.getActiveRegionView().getLayerViews().length-1;i>-1;i--)
					mapPane.map.getActiveRegionView().getLayerViews()[i].getGeographicObjects();
			}
		}
		addListenersOfSelectedTools();
		mapPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		mapPane.setBusy(false);
	}
	protected void updateScale() {   /////////////WAS PRIVATE, CHANGED DUE TO NO CHANGING SCALE WHILE ZOOM BUG (N)/////////
		try {
			Rectangle2D.Double boundRect=(Rectangle2D.Double) mapPane.map.getActiveRegionView().getBoundingRect();
			//How many meters is a pixel in the screen
			//Calculate it in the quarter of the line that divides the map in two horizontaly.
			//This is to avoid measuring a distance greater than half the earth which makes the algorithm fail.

			//(N) zoom wasn't calculated and also if it was multiplied after computation of
			// scr, scr was already too close to zero so it produced zero results and infinite
			//scale. Zoom should be multiplied to the original width before anything else
			double scr=0.0254d*((mapPane.getLayersPane().getOriginalWidth()*mapPane.getZoom()/4)/Toolkit.getDefaultToolkit().getScreenResolution());

			double mp=mapPane.map.getActiveRegionView().measureDistance(
					 boundRect.getX(),
					 boundRect.getY()+boundRect.getHeight()/2,
					 boundRect.getX()+boundRect.getWidth()/4,
					 boundRect.getY()+boundRect.getHeight()/2);
			setScale(mp/scr);
		} catch(Throwable t) {
			//Something is missing
			setScale(" ");
		}
	}
	/**
	 * Makes all the actions that are needed before closing a map.
	 */
	private void finalizeMap() {
		if (map!=null) {
			mapPane.clearMap();

			//Disable tools
			if (toolbar!=null) {
				//Deselect the selected tool
				if (navigate!=null)
					navigate.setEnabled(false);
				if (pan != null) // GT
					pan.setEnabled(false);
				if (goin != null) // GT
					goin.setEnabled(false);
				if (goout != null) // GT
					goout.setEnabled(false);
				if (layerVisib != null) // GT
					layerVisib.setEnabled(false);
				if (activate != null) // GT
					activate.setEnabled(false);
				if (browse != null) // GT
					browse.setEnabled(false);
				if (select != null) // GT
					select.setEnabled(false);
				if (selectCircle != null) // GT
					selectCircle.setEnabled(false);
				if (insObject != null) // GT
					insObject.setEnabled(false);
				if (rotate != null) // GT
					rotate.setEnabled(false);
				if (miniature!=null && miniature.isSelected())
					miniature.doClick();
				if (legend!=null) {
					legend.setEnabled(false);
					if (mapPane.getLegend()!=null && mapPane.getLegend().isVisible())
						mapPane.showLegend(false);
				}
				if (miniature != null) // GT
					miniature.setEnabled(false);
				if (meter != null) // GT
					meter.setEnabled(false);
				if (grid != null) { // GT
					if (grid.isSelected())
						grid.doClick();
					grid.setEnabled(false);
				}
				if (zoom != null) // GT
					zoom.setEnabled(false);
				if (zoomrect != null) // GT
					zoomrect.setEnabled(false);
				if (spot != null) // GT
					spot.setEnabled(false);
				if (identify != null) // GT
					identify.setEnabled(false);
				//if (toolbar.getESlateHandle()!=null)
				//    handle.remove(toolbar.getESlateHandle());
			}
			//Statusbar
			setMessage(" ",0);
			setScale(" ");
			if (miniaturePane!=null) {
				miniaturePane.clearImage();
				miniaturePane=null;
			}
		}
	}
	/**
	 * Frees up the resources held by the browser.
	 */
	private void freeUpResources() {
		mapPane.removeAll();
		mapPane.aux=null;
		mapPane.motion=null;
		mapPane.labels=null;
		mapPane.layers=null;
		if (map!=null) {
			map.removeEnchancedMapListener(MapViewer.this);
			if (miniaturePane!=null)
				map.removeMapListener(miniaturePane.mapListener);
		}
		finalizeMap();
		map=null;
	}
	/**
	 * Checks if a time-machine is connected.
	 */
	boolean isTimeMachineConnected() {
		return (timeMachPlug==null);
	}
	/**
	 * The index of the LayerView in the region.
	 */
	private int indexOf(IRegionView rv,ILayerView lv) {
		for (int i=0;i<rv.getLayerViews().length;i++)
			if (rv.getLayerViews()[i].equals(lv))
				return i;
		return -1;
	}
	/**
	 * Should the lamp be enabled?
	 */
	private boolean shouldShowLamp() {
		return ((map.getActiveRegionView().getLayerViews()!=null) && (map.getActiveRegionView().getLayerViews().length>0)) || (mapPane.getMotionPane()!=null && mapPane.getMotionPane().getConnectedAgentsCount()>0)
			   || map.getActiveRegionView().getBackgroundNames().length>1;
	}
	/**
	 * Sets the message in the statusbar for the given amount of time. If time is zero,
	 * the message will appear until it is changed.
	 */
	public void setMessage(String s,int time) {
		if (statusbar!=null)
			statusbar.setMessage(s,time);
	}
	/**
	 * Enables or disables a popup showing the distance when using the meter tool.
	 */
	public void setMeterPopupEnabled(boolean b) {
		meterPopupEnabled=b;
	}
	/**
	 * Checks if a popup showing the distance when using the meter tool is disabled or not.
	 */
	public boolean isMeterPopupEnabled() {
		return meterPopupEnabled;
	}
	/**
	 * Enables or disables a popup showing area or radius when using the selection tool.
	 */
	public void setSelectionPopupEnabled(boolean b) {
		selectionPopupEnabled=b;
	}
	/**
	 * Checks if a popup showing area or radius when using the selection tool is disabled or not.
	 */
	public boolean isSelectionPopupEnabled() {
		return selectionPopupEnabled;
	}
	/**
	 * Gets the scale string.
	 */
	public String getScale() {
		if (statusbar!=null)
			return statusbar.getScale();
		else
			return null;
	}
	/**
	 * Sets the zoom level.
	 * @param   zoom    The zoom level. 1 is 100%, 0.5 is 50% etc.
	 */
	public void setZoom(double zoom) {
		if (mapPane!=null) {
			double old=mapPane.getZoom();
			mapPane.setZoom(zoom);
			if (old!=mapPane.getZoom())
				mapPane.getLayersPane().viewZoomChanged(true);
		}
	}
	/**
	 * Sets the scale string.
	 */
	public void setScale(String s) {
		if ((lastScale!=null && lastScale.equals(s)) || (lastScale==null && s==null))
			return;
		if (statusbar!=null)
			statusbar.setScale(s);
		lastScale=s;
		//Inform the listeners
		if (listeners!=null) {
			MapViewerEvent e=new MapViewerEvent(this,MapViewerEvent.MAP_VIEWER_SCALE_CHANGED,new String(s));
			for (int i=0;i<listeners.size();i++) {
				try {
					((MapViewerListener) listeners.get(i)).mapViewerScaleChanged(e);
				} catch (AbstractMethodError err) {}
			}
		}
	}
	/**
	 * Sets the scale string.
	 */
	public void setScale(double d) {
		String s;
		java.text.NumberFormat nf=java.text.NumberFormat.getInstance();
		if (d<10)
			nf.setMaximumFractionDigits(2);
		else
			nf.setMaximumFractionDigits(0);
		int i=0;
		if (d<10){
			d=Math.round(d*100)/100.0;
			setScale("1 : "+nf.format(d));
		} else {
			i=(int) Math.round(d);
			setScale("1 : "+nf.format(i));
		}
	}

	public void setErrorTolerance(float f) {
		if (errorTolerance<0)
			throw new IllegalArgumentException("Negative error tolerance set on MapViewer.");
		if (errorTolerance==f)
			return;
		errorTolerance=f;
		mapPane.layers.redrawImage();
		mapPane.layers.repaint();
	}

	public float getErrorTolerance() {
		return errorTolerance;
	}

// GT -start
	/** This method performs common initializations in the MapViewer, which are needed
	 *  by both the default and the ESR2 constructor. Typically this method constructs
	 *  the basic UI of the component (which is not part of the component's state) and
	 *  initializes all the variable's of the component which are not part of it's
	 *  state.
	 */
	private void initializeCommon() {
		setLayout(new BorderLayout());
		mapBackground=Color.white;
		borderChanged=false;

		//Listener stuff
		mapListener=new MapListenerClass();
		regionListener=new RegionListenerClass();
		layerListener=new LayerListenerClass();

		pendingAgents=new ArrayList();
		//MapPane shows the �iniaturePane as well.
		mapPane=new MapPane(this);
		mapPane.setBorder(BorderFactory.createLoweredBevelBorder());
		add(mapPane,BorderLayout.CENTER);

		//Create statusbar
		statusbar=new StatusBar(this);
		statusbarPosition=StatusBar.SOUTH;
		add(statusbar,BorderLayout.SOUTH);

		menubarVisible=false;

		//Handle resizing of the component
		addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			public void ancestorResized(HierarchyEvent e) {
				//This trick brings the legend inside
				if (mapPane.isLegendVisible())
					mapPane.getLegend().setVisible(true);
				checkPanTool();
			}
		});

		/* The following listener takes care that the Map of the MapViewer is
		 * painted only the first time the MapViewer becomes visible and not
		 * right after a Map is attached to it. This speeds-up microworld start-up,
		 * cause the MapViewer does not paint its Map, until the first time it
		 * becomes de-iconified.
		 * This listener is removed if the map is drawn to avoid redrawing.
		 */
		delayedRepaint=new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				Dimension size=MapViewer.this.getSize();
				if (map!=null) {
					connect();
					removeAncestorListener(delayedRepaint);
				}
			}
			public void ancestorRemoved(AncestorEvent event) {}
			public void ancestorMoved(AncestorEvent event) {}
		};
		addAncestorListener(delayedRepaint);
	}

	/** This method constructs the MapViewer in its default state. Called in the
	 *  default MapViewer's constructor (zero-arg).
	 */
	private void initialize() {
		setBorder(new gr.cti.eslate.utils.NoTopOneLineBevelBorder(BevelBorder.RAISED));
		paintCrossOnAgent=false;
		apdi=true;
		setPreferredSize(new java.awt.Dimension(550,400));


		gridVisible=false;
		gridStep=36;
		gridColor=new Color(215,210,210,190);

		//Create toolbar
		//Don't create toolbar! Make it a lazy toolbar.

		menubarVisible=statusbarVisible=true;
		meterPopupEnabled=false;
		selectionPopupEnabled=true;
		//add(menu,BorderLayout.NORTH);
		showInactiveZoomRects=true;
	}


	/** The previous body of the readExternal(). Moved here, so as to be used by the
	 *  ESR2 constructor too.
	 */
	private void initialize(ObjectInput in) throws ClassNotFoundException,IOException {

		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.init(loadTimer);

		super.readExternal(in);

		StorageStructure ht=(StorageStructure) in.readObject();
		String dataVersionStr=ht.getDataVersion();
		int dataVersion=1;
		// Before version 2, the data version was a string of the type "1.0". These
		// strings cannot be converted to ints, so a value 1 for data version means
		// pre 2 data version.
		try {
			dataVersion=Integer.valueOf(dataVersionStr).intValue();
		} catch (Throwable thr) {}

		if (ht.containsKey("backcolor"))
			setBackground((Color) ht.get("backcolor"));
		setStatusBarPosition(ht.get("statusbarpos",getStatusBarPosition()));
		mapPane.rotation=ht.get("rotation",0d);
		mapPane.setAntialiasing(ht.get("antialiasing",true));
		mapPane.setQualityOverSpeed(ht.get("quality",true));
		mapPane.offsetX=ht.get("offsetX",0);
		mapPane.offsetY=ht.get("offsetY",0);
		mapPane.rebuildTransformation();
		mapPane.rebuildPositionTransformation();
		setShowCoordinates(ht.get("coordinates",true));
		gridVisible=ht.get("gridvisible",false);
		gridStep=ht.get("gridstep",36d);
		gridColor=ht.get("gridcolor",gridColor);
		mapBackground=ht.get("mapBackground",Color.white);

		if (ht.containsKey("border")) {
			try {
				BorderDescriptor bd=(BorderDescriptor) ht.get("border");
				setBorder(bd.getBorder());
			} catch (Throwable thr) {/*No Border*/}
		}
		if (ht.containsKey("font"))
			setFont((Font) ht.get("font"));
		statusbar.readExternal(in);
		if (!statusbar.isVisible())
			setStatusBarVisible(false);

		apdi=ht.get("showagentdialog",apdi);
		paintCrossOnAgent=ht.get("paintcrossonagent",paintCrossOnAgent);
		setMaximumSize((Dimension) ht.get("MaximumSize",getMaximumSize()));
		setMinimumSize((Dimension) ht.get("MinimumSize",getMinimumSize()));
		setPreferredSize((Dimension) ht.get("PreferredSize",getPreferredSize()));
		setSelectionShapeType(ht.get("SSType",getSelectionShapeType()));
		if (ht.containsKey("SShapeX")){
			if (getSelectionShapeType()==SelectionShape.RECTANGULAR_SELECTION_SHAPE)
				pendingSelectionShape=new RectangularSelectionShape(ht.get("SShapeX",0d),ht.get("SShapeY",0d),ht.get("SShapeEX",0d),ht.get("SShapeEY",0d),mapPane);
			else
				pendingSelectionShape=new CircularSelectionShape(ht.get("SShapeX",0d),ht.get("SShapeY",0d),ht.get("SShapeEX",0d),ht.get("SShapeEY",0d),mapPane);
		}
		setBusyIconVisible(ht.get("BusyIconVisible",isBusyIconVisible()));
		setShowInactiveZoomRects(ht.get("showInactiveZoomRects",true));
		mapPane.setZoom(ht.get("zoomvalue",1d));
		setMeterPopupEnabled(ht.get("meterpopup",false));
		setSelectionPopupEnabled(ht.get("selectionpopup",true));
		errorTolerance=ht.get("errorTolerance",errorTolerance);

		//Restore children agents
		handle.restoreChildren(ht,"childrenAgents");

		//Restore toolbar (NEW TOOLBAR)
		if (dataVersion == 2 || ht.containsKey("tools")) { // GT
			if (toolbar!=null) {
				remove(toolbar);
				handle.remove(toolbar.getESlateHandle());
			}
			handle.restoreChildren(ht,"tools");
			//Find which of our children is a toolbar.
			ESlateHandle[] tbs=handle.getChildrenOfType(new Class[]{MapViewerToolBar.class});
			if (tbs.length>0) {
				ESlateHandle h=tbs[0];    //There should be exactly one.
				toolbar=(MapViewerToolBar) (h.getComponent());
				toolbar.viewer=this;
				toolbar.setOpaque(toolbar.isOpaque() && isOpaque());
			}
			identifyTools((ToolLocation[]) ht.get("toollocations"));	//Update references to tools in the toolbar.
			setUpToolBar(false);	              //Add listeners, button groups, etc.
		} else {
		//Restore toolbar (OLD TOOLBAR)
			if (toolbar!=null) {
				remove(toolbar);
				handle.remove(toolbar.getESlateHandle());
			}
			createToolBar();
			setUpToolBar(true);	              //Add listeners, button groups, etc.
			ToolBar tb=new ToolBar();
			tb.compatibilityReadExternal(in);
			setToolBarVisible(tb.isVisible());
			toolbar.setOpaque(tb.isOpaque() && isOpaque());
			if (tb.getOrientation()==ToolBar.HORIZONTAL)
				toolbar.setOrientation(ESlateToolBar.HORIZONTAL);
			else
				toolbar.setOrientation(ESlateToolBar.VERTICAL);
			//Restore tools
			String[] tn=new String[] {"navigate","activate","browse","select","selectCircle","pan","edit","goIn","goOut","rotate","meter","identify","layerVisib","legend","miniature","grid","spot","zoomRect"};
			for (int i=0;i<tn.length;i++) {
				AbstractButton newTool=(AbstractButton) toolbar.getTool(toolbarTipBundle.getString(tn[i]));
				AbstractButton oldTool=(AbstractButton) tb.getTool(tn[i]);
				if (oldTool==null) {
					newTool.setVisible(false);
					continue;
				}
				newTool.setSelected(oldTool.isSelected());
				newTool.setVisible(oldTool.isVisible());
				newTool.setOpaque(oldTool.isOpaque());
				newTool.setToolTipText(oldTool.getToolTipText());
				if (oldTool.getIcon()!=null)
					newTool.setIcon(oldTool.getIcon());
				if (oldTool.getSelectedIcon()!=null)
					newTool.setSelectedIcon(oldTool.getSelectedIcon());
				if (oldTool.getRolloverIcon()!=null)
					newTool.setRolloverIcon(oldTool.getRolloverIcon());
				if (oldTool.getPressedIcon()!=null)
					newTool.setPressedIcon(oldTool.getPressedIcon());
				if (((Tool) oldTool).isHelpTextChanged()) {
					((ToolComponent) newTool).setHelpText(((Tool) oldTool).getHelpText());
					toolbar.setAssociatedText(newTool,((Tool) oldTool).getHelpText());
				}
			}
			if (!tb.isVisible())
				setToolBarVisible(false);
		}
		//After creating toolbar action
		if (grid != null) // GT
			grid.setSelected(gridVisible);
		setToolBarPosition(ht.get("toolbarpos",getToolBarPosition()));

		pm.stop(loadTimer);
		pm.displayTime(loadTimer, getESlateHandle(), "", "ms");

	}

// GT -end

	public void setSelectionShapeType(int type) throws InvalidParameterException {
		if (type!=SelectionShape.CIRCULAR_SELECTION_SHAPE && type!=SelectionShape.RECTANGULAR_SELECTION_SHAPE)
			throw new InvalidParameterException("Invalid selection shape type");
		if (selectionShapeType!=type) {
			selectionShapeType=type;
			//Deselect the button tool of the opposite type
			if ((type==SelectionShape.CIRCULAR_SELECTION_SHAPE && select!=null && select.isSelected()) ||
				(type==SelectionShape.RECTANGULAR_SELECTION_SHAPE && selectCircle!=null && selectCircle.isSelected())) {
				toolbar.getButtonGroup(0).setSelected(null,true);
				mapPane.removeAllListeners();
				mapPane.setToolTipText(null);
			}

			fireSelectionShapeTypeChanged(type);
		}
	}

	/**
	 * Gets the view center in map coordinates.
	 */
	public java.awt.geom.Point2D getViewCenter() {
		if (map==null)
			return null;
		Point2D.Double p2d=new Point2D.Double(mapPane.layers.offscreenView.x+mapPane.layers.offscreenView.width/2,mapPane.layers.offscreenView.y+mapPane.layers.offscreenView.height/2);
		mapPane.getInversePositionTransform().transform(p2d,p2d);
		return p2d;
	}

	public void setSelectionShape(SelectionShape shape) {
		mapPane.getAuxiliaryPane().setSelectionShape(shape);
		mapPane.updateSelectionShape();
	}

	public int getSelectionShapeType() {
		return selectionShapeType;
	}

	public SelectionShape getSelectionShape() {
		return mapPane.getAuxiliaryPane().getSelectionShape();
	}

	protected void fireSelectionShapeTypeChanged(int type) {
		if (selectionShapeTypeListener!=null) {
			SelectionShapeTypeEvent en=new SelectionShapeTypeEvent(this,SelectionShapeTypeEvent.SHAPE_TYPE_CHANGED,type);
			selectionShapeTypeListener.shapeTypeChanged(en);
		}
	}

	protected void fireSelectionShapeChanged(){
		if (selectionShapeListener!=null ){
			SelectionShapeEvent en=new SelectionShapeEvent(this,SelectionShapeEvent.SHAPE_GEOMETRY_CHANGED);
			selectionShapeListener.shapeGeometryChanged(en);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////

	//////////////////CODE ADDED FOR CUSTOMIZING CURSORS WHEN IN DIFFERENT PLATFORMS (N)//////////

	public static Cursor createCustomCursor(String imageName, Point hotspot16, Point hotspot32, String cursorName){
		Cursor cursor = null;
		Dimension bestCursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(32,32);
		String nameWithoutExtension = imageName.substring(0,imageName.lastIndexOf("."));
		String extension = imageName.substring(imageName.lastIndexOf("."),imageName.length());
		if (bestCursorSize.width == 16 && bestCursorSize.height == 16)
			if (MapViewer.class.getResource(nameWithoutExtension+"_16x16"+extension) == null)
				cursor = Toolkit.getDefaultToolkit().createCustomCursor((new ImageIcon(MapViewer.class.getResource(nameWithoutExtension+extension))).getImage(), hotspot16,cursorName);
			else
				cursor = Toolkit.getDefaultToolkit().createCustomCursor((new ImageIcon(MapViewer.class.getResource(nameWithoutExtension+"_16x16"+extension))).getImage(), hotspot16,cursorName);
		else
			if (MapViewer.class.getResource(nameWithoutExtension+"_32x32"+extension) == null)
				cursor = Toolkit.getDefaultToolkit().createCustomCursor((new ImageIcon(MapViewer.class.getResource(nameWithoutExtension+extension))).getImage(), hotspot32,cursorName);
			else
				cursor = Toolkit.getDefaultToolkit().createCustomCursor((new ImageIcon(MapViewer.class.getResource(nameWithoutExtension+"_32x32"+extension))).getImage(), hotspot32,cursorName);
		return cursor;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////




	//E-Slate variables
	private ESlateHandle handle;
	private RightSingleConnectionProtocolPlug mapPlug;
	LeftMultipleConnectionProtocolPlug agentPlug;
	private SingleInputPlug timeMachPlug;

	public static final int STR_FORMAT_VERSION = 2;

   /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
   PerformanceTimer loadTimer;
   /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
   PerformanceTimer saveTimer;
   /**
   * Timer which measures the MapViewer construction time.
   */
   PerformanceTimer constructorTimer;
   /**
	* Timer which measures the creation of the E-Slate side of the MapViewer.
	*/
   PerformanceTimer initESlateAspectTimer;
   /**
	* Timer which measures the changing of the active region.
	*/
   PerformanceTimer activeRegionChangedTimer;
   /**
	* Timer which measures the full repainting of the map area.
	*/
   PerformanceTimer fullMapRedraw;
   /**
	* Timer which measures the partial repainting of the map area.
	*/
   PerformanceTimer partialMapRedraw;
   /**
	* Timer which measures the full repainting of the labels.
	*/
   PerformanceTimer fullLabelRedraw;
   /**
	* Timer which measures the partial repainting of the labels.
	*/
   PerformanceTimer partialLabelRedraw;
   /**
	* Timer which measures the time the model takes to return its layers.
	* This time may include loading from disk.
	*/
   PerformanceTimer getLayersFromModel;
   /**
	* Timer which measures the time the model takes to return a background.
	* This time may include loading from disk.
	*/
   PerformanceTimer getBackgroundFromModel;
   /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
   PerformanceListener perfListener = null;

	//Communication variables
	private IMapView map;
	private MapListener mapListener;
	private RegionListener regionListener;
	private LayerListener layerListener;
	private Cursor working;
	//Map Viewer listeners, listening for component events
	MapViewerEventMulticaster listeners;
	SelectionShapeTypeEventMulticaster selectionShapeTypeListener = new SelectionShapeTypeEventMulticaster() ;
	SelectionShapeEventMulticaster selectionShapeListener = new SelectionShapeEventMulticaster() ;
	//Toolbar variables
	private MapViewerToolBar toolbar;
	private int toolbarPosition;
	//Statusbar variables
	private StatusBar statusbar;
	private int statusbarPosition;
	//Bundles
	protected static ResourceBundle messagesBundle,menuBundle,toolbarTipBundle,plugBundle,bundleInfo;
	//UI variables
	private MapPane mapPane;
	MiniaturePane miniaturePane;
	private JMenuBar menu;
	MapViewerToggleButton activate,goin,goout,pan, browse;
	private MapViewerToggleButton navigate,select,insObject,rotate,miniature,meter,identify,grid,zoomrect,selectCircle;
	protected MapViewerToggleButton legend; //Accessed by MapPane.legend
	private MapViewerButton layerVisib,spot;
	ZoomSlider zoom;
	//UI element visibility variables
	private boolean menubarVisible,statusbarVisible,meterPopupEnabled,selectionPopupEnabled;
	/**
	 * If true, all zoom rectangles are shown when the go-in tool is pressed.
	 * If false, only the zoom rectangle which has the mouse over it is shown.
	 */
	private boolean showInactiveZoomRects;
	private boolean borderChanged;
	boolean blockRedraw=false;
	/**
	 * Grid variables.
	 */
	private boolean gridVisible;
	private Color gridColor;
	private double gridStep;
	private boolean apdi;
	//Agent variables
	private ArrayList pendingAgents;
	/**
	 * Pending Selection Shape on loading. This shape should be added to the
	 * Auxiliary Pane when the component is connected to the map.
	 */
	private SelectionShape pendingSelectionShape;
	/**
	 * Indicates if a cross is painted beneath the agents.
	 */
	private boolean paintCrossOnAgent;
	private AncestorListener delayedRepaint;
	private ComponentListener cl;

	private int selectionShapeType;

	boolean goinActionPerformed=false;
	//Selection shape preservation
	boolean iamselecting=false;
	private String lastScale;
	private float errorTolerance=0.01f;
	private Point2D.Double p2d=new Point2D.Double();

	private Color mapBackground;


	//Actions exposed to the public through getAction() method
	private Action navigateAction,activateAction,browseAction,selectRectAction,selectCircleAction,panAction,insObjectAction,goinAction,gooutAction;
	private Action rotateAction,meterAction,identifyAction,miniatureAction,gridAction,spotAction,zoomrectAction;

	//Action names exposed to the public through getAction() method
	public static final String NAVIGATE_TOOL_NAME="navigate";
	public static final String ACTIVATE_TOOL_NAME="activate";
	public static final String BROWSE_TOOL_NAME="browse";
	public static final String SELECTRECT_TOOL_NAME="select";
	public static final String SELECTCIRCLE_TOOL_NAME="selectCircle";
	public static final String PAN_TOOL_NAME="pan";
	public static final String INSERT_OBJECT_TOOL_NAME="insObject";
	public static final String GO_IN_TOOL_NAME="goin";
	public static final String GO_OUT_TOOL_NAME="goout";
	public static final String ROTATE_TOOL_NAME="rotate";
	public static final String METER_TOOL_NAME="meter";
	public static final String IDENTIFY_TOOL_NAME="identify";
	public static final String MINIATURE_TOOL_NAME="miniature";
	public static final String GRID_TOOL_NAME="grid";
	public static final String SPOT_TOOL_NAME="spot";
	public static final String ZOOM_TO_RECT_TOOL_NAME="zoomrect";

	//Externalization
	static final long serialVersionUID=3000L;
	/**
	 * The component version.
	 */
	public static final String version="3";

	/*************************/
	/** Map Listener class. **/

 /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm)
  {
	if (perfListener == null) {
	  perfListener = new PerformanceAdapter() {
		public void performanceManagerStateChanged(PropertyChangeEvent e)
		{
		  boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
		  // When the Performance Manager is enabled, try to attach the
		  // timers.
		  if (enabled) {
			attachTimers();
		  }
		}
	  };
	  pm.addPerformanceListener(perfListener);
	}
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  private void attachTimers(){
	PerformanceManager pm = PerformanceManager.getPerformanceManager();
	boolean pmEnabled = pm.isEnabled();

	// If the performance manager is disabled, install a listener which will
	// re-invoke this method when the performance manager is enabled.
	if (!pmEnabled && (perfListener == null)) {
	  createPerformanceManagerListener(pm);
	}

	// Do nothing if the PerformanceManager is disabled.
	if (!pmEnabled) {
	  return;
	}

	boolean timersCreated = (loadTimer != null);
	// If the timers have already been constructed and attached, there is
	// nothing to do.
	if (!timersCreated) {
// GT -start
		// Get the performance timer group for this component.
	  PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
	  // Construct and attach the component's timers.
	  constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, messagesBundle.getString("ConstructorTimer"), true
	  );
	  loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, messagesBundle.getString("LoadTimer"), true
	  );
	  saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, messagesBundle.getString("SaveTimer"), true
	  );
	  initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, messagesBundle.getString("InitESlateAspectTimer"), true
	  );

		activeRegionChangedTimer=(PerformanceTimer) pm.createPerformanceTimerGroup(
			compoTimerGroup,messagesBundle.getString("ActiveRegionChanged"),true
		);

		fullMapRedraw=(PerformanceTimer) pm.createPerformanceTimerGroup(
			activeRegionChangedTimer,messagesBundle.getString("FullMapRedraw"),true
		);

		partialMapRedraw=(PerformanceTimer) pm.createPerformanceTimerGroup(
			compoTimerGroup,messagesBundle.getString("PartialMapRedraw"),true
		);

		fullLabelRedraw=(PerformanceTimer) pm.createPerformanceTimerGroup(
			activeRegionChangedTimer,messagesBundle.getString("FullLabelRedraw"),true
		);

		partialLabelRedraw=(PerformanceTimer) pm.createPerformanceTimerGroup(
			compoTimerGroup,messagesBundle.getString("PartialLabelRedraw"),true
		);

		pm.addPerformanceTimerGroup(compoTimerGroup,fullMapRedraw);
		pm.addPerformanceTimerGroup(compoTimerGroup,partialMapRedraw);
		pm.addPerformanceTimerGroup(compoTimerGroup,fullLabelRedraw);
		pm.addPerformanceTimerGroup(compoTimerGroup,partialLabelRedraw);

		getLayersFromModel=(PerformanceTimer) pm.createPerformanceTimerGroup(
			fullMapRedraw,messagesBundle.getString("GetLayersFromModel"),true
		);

		getBackgroundFromModel=(PerformanceTimer) pm.createPerformanceTimerGroup(
			activeRegionChangedTimer,messagesBundle.getString("GetBackgroundFromModel"),true
		);

		pm.addPerformanceTimerGroup(compoTimerGroup,getBackgroundFromModel);

	  pm.registerPerformanceTimerGroup(
		PerformanceManager.CONSTRUCTOR, constructorTimer, this
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.LOAD_STATE, loadTimer, this
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.SAVE_STATE, saveTimer, this
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
	  );

// GT -end
	  // If the component's timers have not been constructed yet, then
	  // construct them. During constuction, the timers are also attached.
/*      PerformanceTimer loadTimer =
	(PerformanceTimer)pm.createGlobalPerformanceTimerGroup(
	  messagesBundle.getString("LoadTimer"), true
	);
	  PerformanceTimer saveTimer =
	(PerformanceTimer)pm.createGlobalPerformanceTimerGroup(
	  messagesBundle.getString("SaveTimer"), true
	);
	  ESlateHandle h = getESlateHandle();
	  pm.registerPerformanceTimerGroup(
	PerformanceManager.LOAD_STATE, loadTimer, h
	  );
	  pm.registerPerformanceTimerGroup(
	PerformanceManager.SAVE_STATE, saveTimer, h
	  );
*/
	}
  }



	private class MapListenerClass extends gr.cti.eslate.mapModel.MapAdapter {
		/**
		 * Invoked when a Map component changes its contents.
		 */
		public void mapChanged(MapEvent e) {
			freeUpResources();
			if (mapPlug.getProviders().length>0) {
				map=(IMapView) ((ProtocolPlug)mapPlug.getProviders()[0]).getProtocolImplementor();
				map.addEnchancedMapListener(MapViewer.this);
				if (miniaturePane!=null)
					map.addMapListener(miniaturePane.mapListener);
				initializeMap(map,true);
			}
		}
		/**
		 * Invoked when a Map (a set of regions) is renamed.
		 */
		public void mapRenamed(MapEvent e) {
		}
		/**
		 * Invoked when the active region is changed.
		 */
		public void mapActiveRegionChanged(MapEvent e) {
			PerformanceManager pm = PerformanceManager.getPerformanceManager();
			pm.init(activeRegionChangedTimer);
			initializeMap(map,true);
			pm.stop(activeRegionChangedTimer);
			pm.displayTime(activeRegionChangedTimer,getESlateHandle(),"","ms");
		}
		/**
		 * Invoked when the database is changed.
		 */
		public void mapDatabaseChanged(MapEvent e) {
			if (mapPane.layers.mayHaveLabels()) {
				mapPane.getLabelPane().redrawImage();
				mapPane.repaint();
			} else if (mapPane.labels!=null)
				mapPane.labels.clear();
		}
		/**
		 * Invoked when the bounding rectangle of the map is modified.
		 */
		public void mapBoundingRectChanged(MapEvent e) {
		}
		/**
		 * Invoked when the entry node of the map is changed.
		 */
		public void mapEntryNodeChanged(MapEvent e) {
		}
		/**
		 * Invoked when a new region is added in the structure.
		 */
		public void mapRegionAdded(MapEvent e) {
			checkGoInTool();
		}
		/**
		 * Invoked when a region is being removed from the structure.
		 */
		public void mapRegionRemoved(MapEvent e) {
			checkGoInTool();
		}
		/**
		 * Invoked when the date interval shown by the view is changed. A browser should care for the consequences.
		 */
		public void mapDateIntervalChanged(MapEvent e) {
			IMapBackground mb=map.getActiveRegionView().getBackground();
			mapPane.setRedrawEnabled(false);
			if (mapPane.getLayersPane().background!=mb) {
				IMapBackground old=mapPane.getLayersPane().background;
				mapPane.getLayersPane().setBackground(mb);
				//If there is a change in size, send a viewSizeChanged "event".
				if (mapPane.getLayersPane().background!=null && mb!=null && mapPane.getLayersPane().background.getIconWidth()!=mb.getIconWidth()
					&& mapPane.getLayersPane().background.getIconHeight()!=mb.getIconHeight())
					mapPane.layers.viewSizeChanged(true);
			}
			//Redraw layers only if there is a time aware layer.
			boolean redrawLayers=false;
			ILayerView[] layers=map.getActiveRegionView().getLayerViews();
			for (int i=0;i<layers.length && !redrawLayers && !mapPane.labelsNeedRedrawing;i++) {
				redrawLayers=redrawLayers || layers[i].isTimeEraAware();
				if (layers[i].isTimeEraAware())
					mapPane.labelsNeedRedrawing=true;
			}
			if (redrawLayers)
				mapPane.layers.redrawImage();
			//This will draw labels as well
			mapPane.setRedrawEnabled(true);
		}
	}


	/****************************/
	/** Region Listener class. **/
	class RegionListenerClass extends gr.cti.eslate.mapModel.RegionAdapter {
		public void regionBoundingRectChanged(RegionEvent e) {
			mapPane.clearTransformations();
			if (mapPane.layers.getCurrentBackground()==null)
				mapPane.layers.findOriginalSizeOnNullBackground();
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage();
			if (mapPane.layers.mayHaveLabels())
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			mapPane.layers.isZooming=false;
			mapPane.repaint();
			updateScale();
		}
		/**/
		public void regionLayerAdded(RegionEvent e) {
			//The bounding rect is null when no coordinates have been defined in the current view
			if (map.getActiveRegionView().getBoundingRect()==null)
				return;
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage(r);
			if (((ILayerView)e.getNewValue()).mayHaveLabels())
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			else if (mapPane.labels!=null)
				mapPane.labels.clear();
			if (layerVisib!=null)
				layerVisib.setEnabled(true);
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().addLayer((ILayerView) e.getNewValue(),indexOf(mapPane.map.getActiveRegionView(),(ILayerView) e.getNewValue()));
			mapPane.layers.repaint();
		}
		/**/
		public void regionLayerRemoved(RegionEvent e) {
			//The bounding rect is null when no coordinates have been defined in the current view
			if (map.getActiveRegionView().getBoundingRect()==null)
				return;
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage(r);
			if (((ILayerView)e.getOldValue()).mayHaveLabels())
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			if (!shouldShowLamp() && layerVisib!=null)
				layerVisib.setEnabled(false);
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().removeLayer((ILayerView) e.getOldValue());
			mapPane.layers.repaint();
		}
		/**/
		public void regionLayersSwapped(RegionEvent e) {
			//The bounding rect is null when no coordinates have been defined in the current view
			if (map.getBoundingRect()==null)
				return;
			mapPane.layers.redrawImage();
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().swapLayers(indexOf(mapPane.map.getActiveRegionView(),(ILayerView) e.getOldValue()),indexOf(mapPane.map.getActiveRegionView(),(ILayerView) e.getNewValue()));
			mapPane.layers.repaint();
		}
		/**/
		public void regionLayersReordered(RegionEvent e) {
			//The bounding rect is null when no coordinates have been defined in the current view
			if (map.getBoundingRect()==null)
				return;
			mapPane.layers.redrawImage();
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().layersReordered();
			mapPane.layers.repaint();
		}
		/**
		 * Invoked when the scale is changed.
		 */
		public void regionScaleChanged(RegionEvent e) {
			if (map.getActiveRegionView().getScale()!=null)
				setScale("1 : "+map.getActiveRegionView().getScale());
			else
				setScale(" ");
		}
		/**
		 * Invoked when the orientation is changed.
		 */
		public void regionOrientationChanged(RegionEvent e) {
		}
		/**
		 * Invoked when a background image is added.
		 */
		public void regionBackgroundImageAdded(RegionEvent e) {
			mapPane.setBusy(true);
			if (layerVisib!=null) {
				if (shouldShowLamp())
					layerVisib.setEnabled(true);
				else
					layerVisib.setEnabled(false);
			}
			/*
			mapPane.getLayersPane().setBackground(map.getActiveRegionView().getBackground());
			mapPane.layers.redrawImage();
			mapPane.validate();
			updateScale();
			checkPanTool();
			*/
			//If the new background is a candidate (in all eras, or this era) add it to the legend
			String[] sn=map.getActiveRegionView().getBackgroundNames();
			String s=((IMapBackground) e.getNewValue()).getFilename();
			for (int i=0;i<sn.length;i++)
				if ((sn[i].equals(s) && sn.length!=1) || sn.length==2)
					if (mapPane.getLegend()!=null)
						mapPane.getLegend().addBackground(map.getActiveRegionView().getBackground(sn[i]),sn[i]);
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().repaint();
			mapPane.setBusy(false);
		}
		/**
		 * Invoked when the default background image is changed.
		 */
		public void regionDefaultBackgroundImageChanged(RegionEvent e) {
			mapPane.setBusy(true);
			if (layerVisib!=null) {
				if (shouldShowLamp())
					layerVisib.setEnabled(true);
				else
					layerVisib.setEnabled(false);
			}
			mapPane.setRedrawEnabled(false);
			IMapBackground mb=map.getActiveRegionView().getBackground();
			if (mapPane.getLayersPane().background!=mb) {
				IMapBackground old=mapPane.getLayersPane().background;
				mapPane.getLayersPane().setBackground(mb);
				//If there is a change in size, send a viewSizeChanged "event".
				if ((old!=null && mb!=null && (old.getIconWidth()!=mb.getIconWidth()
					|| old.getIconHeight()!=mb.getIconHeight())) || ((old==null)!=(mb==null)))
					mapPane.layers.viewSizeChanged(true);
			}
			mapPane.setRedrawEnabled(true);
			updateScale();
			checkPanTool();
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().activateBackground(map.getActiveRegionView().getBackgroundName());
			mapPane.layers.repaint();
		}

		public void regionLinkChanged(RegionEvent e) {
			mapPane.setBusy(true);
			mapPane.layers.redrawImage();
			if (mapPane.getLegend()!=null)
				mapPane.populateLegend();
			mapPane.layers.repaint();
			mapPane.setBusy(false);
		}
	}


	/***************************/
	/** Layer Listener class. **/
	private class LayerListenerClass extends gr.cti.eslate.mapModel.LayerAdapter {
		public void layerVisibilityChanged(LayerVisibilityEvent e) {
			if (e.isChanging()) {
				mapPane.needsRedrawing=true;
				if (((ILayerView)e.getSource()).mayHaveLabels())
					mapPane.labelsNeedRedrawing=true;
				return;
			}
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage(r);
			if (((ILayerView)e.getSource()).mayHaveLabels() || mapPane.labelsNeedRedrawing) {
				mapPane.labelsNeedRedrawing=false;
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			}
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().rebuildLayerPanel((ILayerView) e.getSource());
			mapPane.repaint();
		}
		/**/
		public void layerSelectionChanged(LayerEvent e) {
			//Enlarge the rectangle of point layers according to the view.
			//This is a piece of information the model cannot know. That's
			//why the work is done by the viewer, which passes a transformation.
			if (e.getSource() instanceof IPointLayerView) {
				((IPointLayerView) e.getSource()).enlargeSelectionRectangle(mapPane.getPositionTransform().getScaleX(),-mapPane.getPositionTransform().getScaleY());
			}
			if (e.getOldValue()!=null) {
				Rectangle2D r=(Rectangle2D) e.getOldValue();
				oldR=addRectangle(oldR,r.getX(),r.getY(),r.getWidth(),r.getHeight());
			}
			if (e.getNewValue()!=null) {
				Rectangle2D r=(Rectangle2D) e.getNewValue();
				newR=addRectangle(newR,r.getX(),r.getY(),r.getWidth(),r.getHeight());
			}
			if (!e.isChanging()) {
				mapPane.layers.redrawImage(oldR,newR);
				//Reinitialiaze selection rects for the next selection
				oldR=null;
				newR=null;
				if (!iamselecting) {
					//Removed because table record selection causes the shape to disappear.
					//Noticed in ASE.
					//mapPane.getAuxiliaryPane().setSelectionShape(null);
					//mapPane.getAuxiliaryPane().repaint();
				}
			}
			iamselecting=false;
		}
		//Keep the selection rectangles from all layers until finishing selection
		private Rectangle2D.Double oldR=null,newR=null;
		/**
		 * Adds a rectangle to another rectangle.
		 */
		private Rectangle2D.Double addRectangle(Rectangle2D.Double rect,double x,double y,double w,double h) {
			if (rect==null)
				rect=new Rectangle.Double(x,y,w,h);
			else {
				double xn=Math.min(x,rect.getX()); double yn=Math.min(y,rect.getY());
				rect.setRect(xn,yn,Math.max(x+w,rect.getX()+rect.getWidth())-xn,Math.max(y+h,rect.getY()+rect.getHeight())-yn);
			}
			return rect;
		}
		/**/
		public void layerActiveGeographicObjectChanged(LayerEvent e) {
			if (e.getSource() instanceof IRasterLayerView)
				mapPane.layers.redrawImage();
			else {
				Rectangle visr=getVisibleRect();
				//Repaint the old area
				Object old=e.getOldValue();
				if (old!=null) {
					if (old instanceof IPoint) {
						p2d.setLocation(((IPoint)old).getX(),((IPoint)old).getY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						mapPane.getTransform().transform(p2d,p2d);
						if (visr.contains(p2d))
							mapPane.getLayersPane().repaint(((int)p2d.x)-10,((int)p2d.y)-10,21,21);
					} else if (old instanceof IPolygon || old instanceof IPolyLine) {
						Rectangle2D r=((IVectorGeographicObject)old).getBounds2D();
						mapPane.transformRect(mapPane.getPositionTransform(),r);
						mapPane.transformRect(mapPane.getTransform(),r);
						if (visr.intersects(r))
							mapPane.getLayersPane().repaint(((int)r.getX())-5,((int)r.getY())-5,((int)r.getWidth())+11,((int)r.getHeight())+11);
					}
				}
				//Repaint all the new area
				Object fresh=e.getNewValue();
				if (fresh!=null) {
					if (fresh instanceof IPoint) {
						p2d.setLocation(((IPoint)fresh).getX(),((IPoint)fresh).getY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						mapPane.getTransform().transform(p2d,p2d);
						if (visr.contains(p2d))
							mapPane.getLayersPane().repaint(((int)p2d.x)-10,((int)p2d.y)-10,21,21);
					} else if (fresh instanceof IPolygon || fresh instanceof IPolyLine) {
						Rectangle2D r=((IVectorGeographicObject)fresh).getBounds2D();
						mapPane.transformRect(mapPane.getPositionTransform(),r);
						mapPane.transformRect(mapPane.getTransform(),r);
						if (visr.intersects(r))
							mapPane.getLayersPane().repaint(((int)r.getX())-5,((int)r.getY())-5,((int)r.getWidth())+11,((int)r.getHeight())+11);
					}
				}
			}
		}
		/**/
		public void layerGeographicObjectAdded(LayerEvent e) {
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage(r);
			if (((ILayerView)e.getSource()).mayHaveLabels())
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			mapPane.repaint();
		}
		/**/
		public void layerGeographicObjectRemoved(LayerEvent e) {
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage(r);
			if (((ILayerView)e.getSource()).mayHaveLabels())
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			mapPane.repaint();
		}
		/**/
		public void layerGeographicObjectRepositioned(LayerEvent e) {
			Rectangle2D.Double r=mapPane.layers.offscreenViewToReal();
			mapPane.layers.redrawImage(r);
			if (((ILayerView)e.getSource()).mayHaveLabels())
				mapPane.getLabelPane().redrawImage(mapPane.layers.offscreenView,r);
			mapPane.repaint();
		}
		/**/
		public void layerColoringChanged(LayerEvent e) {
			mapPane.layers.redrawImage();
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().rebuildLayerPanel((ILayerView) e.getSource());
			mapPane.repaint();
		}
		/**/
		public void layerLabelBaseChanged(LayerEvent e) {
			if (mapPane.layers.mayHaveLabels()) {
				mapPane.getLabelPane().redrawImage();
				mapPane.repaint();
			} else if (mapPane.labels!=null)
				mapPane.labels.clear();
		}
		/**/
		public void layerPaintPropertiesChanged(LayerEvent e) {
			mapPane.layers.redrawImage();
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().rebuildLayerPanel((ILayerView) e.getSource());
			mapPane.repaint();
		}
		/**/
		public void layerUnknownPropertyChanged(LayerEvent e) {
			mapPane.layers.redrawImage();
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().rebuildLayerPanel((ILayerView) e.getSource());
			mapPane.repaint();
		}
		/**/
		public void layerGeographicObjectPropertiesChanged(LayerEvent e) {
            Rectangle2D.Double repRect=new Rectangle2D.Double();
			VectorGeographicObject go=(VectorGeographicObject) e.getOldValue();
			if (go instanceof IPoint) {
				double tolerance=25*mapPane.PIXEL_TOLERANCE*mapPane.getInversePositionTransform().getScaleX();
				repRect.setRect(go.getBoundingMinX()-tolerance,go.getBoundingMinY()-tolerance,go.getBoundingMaxX()-go.getBoundingMinX()+2*tolerance,go.getBoundingMaxY()-go.getBoundingMinY()+2*tolerance);
			} else
				repRect.setRect(go.getBoundingMinX(),go.getBoundingMinY(),go.getBoundingMaxX()-go.getBoundingMinX(),go.getBoundingMaxY()-go.getBoundingMinY());
			mapPane.layers.redrawImage(repRect,null);
			if (mapPane.layers.mayHaveLabels())
				mapPane.getLabelPane().redrawImage();
			mapPane.repaint();
		}
		/**/
		public void layerDatabaseTableChanged(LayerEvent e) {
			if (map==null)
				return;
			if (mapPane.layers.mayHaveLabels())
				mapPane.getLabelPane().redrawImage();
			else if (mapPane.labels!=null)
				mapPane.labels.clear();
			mapPane.repaint();
		}
		/**/
		public void layerObjectsWithNoDataShownChanged(LayerEvent e) {
			mapPane.layers.redrawImage();
		}
		/**/
		public void layerObjectVisibilityChanged(LayerEvent e) {
			mapPane.updateSelectionShape();
		}
		/**/
		public void layerObjectsCanBeSelectedChanged(LayerEvent e) {
			if (mapPane.getLegend()!=null)
				mapPane.getLegend().objectSelectabilityChanged((ILayerView) e.getSource());
		}
		/**/
		public void layerRasterTransparencyLevelChanged(LayerEvent e) {
			mapPane.layers.redrawImage();
			mapPane.repaint();
		}
	}

}
