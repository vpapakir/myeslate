package gr.cti.eslate.mapViewer;

//import java.awt.*;
import gr.cti.eslate.database.engine.CImageIcon;
import gr.cti.eslate.mapModel.PointLayer;
import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IPoint;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IPolyLine;
import gr.cti.eslate.protocol.IRasterLayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IVectorLayerView;
import gr.cti.eslate.protocol.IZoomRect;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.typeArray.ObjectBaseArray;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


/**
 * The pane where the map is shown.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class MapPane extends JLayeredPane implements TransparentMouseInput {
	/**
	 * The default constructor.
	 */
	MapPane(MapViewer viewr) {
		super();
		this.viewer=viewr;
		//UI Construction

		offsetX=offsetY=0;
		coordsCache1=new double[2];
		coordsCache2=new double[2];
		setOpaque(true);
		setBackground((Color) UIManager.get("controlShadow"));
		setDoubleBuffered(false);

		showCoordinates(true);

		ToolTipManager.sharedInstance().registerComponent(this);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				resized();
			}
		});
	}

	/**
	 * Adds a MouseInputListener. This is different from adding a MouseListener and a MouseMotionListener seperately.
	 */
	public void addMouseInputListener(MouseInputListener l) {
		if (l==null) return;
		if (getForegroundComponent()!=this) {
			getForegroundComponent().addMouseInputListener(l);
		} else {
			if (ml==null)
				ml=new ArrayList();
			ml.add(l);
			addMouseListener(l);
			addMouseMotionListener(l);
		}
	}
	/**
	 * Removes a MouseInputListener.
	 */
	public void removeMouseInputListener(MouseInputListener l) {
		if (getForegroundComponent()!=this) {
			getForegroundComponent().removeMouseInputListener(l);
		} else {
			if (ml==null)
				return;
			ml.remove(l);
			removeMouseListener(l);
			removeMouseMotionListener(l);
		}
	}

	/**
	 * Removes all Mouse and MouseMotion Listeners.
	 * @return A list containing the listeners removed.
	 */
	public List removeAllListeners() {
		clearLargerScaleMapsRectangles();
		if (motion!=null)
			motion.removeAllListeners();
		if (aux!=null)
			aux.removeAllListeners();
		if (layers!=null)
			layers.removeAllListeners();
		ArrayList copy=new ArrayList();
		//## MAY EXIT HERE!
		if (ml==null)
			return copy;
		for (int i=0;i<ml.size();i++) {
			removeMouseListener((MouseListener) ml.get(i));
			removeMouseMotionListener((MouseMotionListener) ml.get(i));
			copy.add(ml.get(i));
		}
		ml.clear();
		return copy;
	}


	/**
	 * Should make public access to processMouseEvent.
	 * @param e The event.
	 */
	public void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
	}

	/**
	 * Should make public access to processMouseMotionEvent.
	 * @param e The event.
	 */
	public void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
	}

	public void setBorder(Border b) {
		super.setBorder(b);
		ins=getInsets();
		resized();
	}

	public boolean isRedrawEnabled() {
		return redrawEnabled;
	}
	/**
	 * Setting to false, pauses the redrawing which will be done once, once setting
	 * the method back to true. May be used by scripts that want to make continuous
	 * changes but only one redrawing.
	 */
	public void setRedrawEnabled(boolean enabled) {
		if (redrawEnabled==enabled)
			return;
		this.redrawEnabled=enabled;
		if (enabled && layers!=null) {
			if (motion!=null)
				motion.centerViewToAgents();
			Rectangle2D.Double r=layers.offscreenViewToReal();
			if (needsRedrawing) {
				if (layers.pendingLayers!=null)
					layers.redefineView();
				layers.redrawImage(r);
				layers.updateShownOffscreenView();
				needsRedrawing=false;
				layers.clearZooming();
			}
			if (labelsNeedRedrawing) {
				labelsNeedRedrawing=false;
				if (layers.mayHaveLabels())
					getLabelPane().redrawImage(layers.offscreenView,r);
				else if (labels!=null)
					//Clear the labels that may exist from a previous situation
					labels.clear();
			}
			repaint();
		}
	}
	/**
	 * Gets the cursor with the given name. If the cursor has not been initialized yet,
	 * it is initialized.
	 * @param key   The cursor name.
	 * @return  The requested Cursor object.
	 */
	protected Cursor getCustomCursor(String key) {
		if (cursors==null)
			cursors=new CursorMap();
		return (Cursor) cursors.get(key);
	}

	public TransparentMouseInput getForegroundComponent() {
		if (motion!=null)
			return motion;
		else if (aux!=null)
			return aux;
		else if (labels!=null)
			return labels;
		else if (layers!=null)
			return layers;
		else
			return this;
	}

	public void setToolTipText(String text) {
		//Work around: For some strange reason setting the tooltip text to null
		//causes the tooltip not to show up again!
		if (text==null)
			text="";
		if (((JComponent) getForegroundComponent())==this)
			super.setToolTipText(text);
		else
			((JComponent) getForegroundComponent()).setToolTipText(text);
	}

	/**
	 * Roll effect for neighbouring regions.
	 */
	void roll(IRegionView rv,int dir) {
		final int stepX=12;
		final int stepY=12;
		boolean fixedEnd=false;
		Icon ic=rv.getBackground();
		Icon cic=map.getActiveRegionView().getBackground();
		Insets in=getInsets();
		BufferedImage bi=null;
		Graphics2D g2=null;
		Graphics2D tg=(Graphics2D) getGraphics();
		//Clip to preserve the border
		tg.scale(zoom,zoom);
		tg.setClip(null);
		//tg.clipRect((int)Math.round(in.top/zoom),(int)Math.round(in.left/zoom),(int)Math.round((getWidth()-in.left/zoom-in.right/zoom)),(int)Math.round((getHeight()-in.top/zoom-in.bottom/zoom)));
		//tg.clipRect(in.top,in.left,getWidth()-in.left-in.right,getHeight()-in.top-in.bottom);
		Cursor old=getCursor();
		setCursor(getCustomCursor("busy"));
		//Hide the panes so that e.g. agents donot appear while scrolling
		if (aux!=null)
			aux.setVisible(false);
		if (motion!=null)
			motion.setVisible(false);
		switch (dir) {
			case IRegionView.NEIGHBOUR_UP:
				//Create an image with the two backgrounds combined
				bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(Math.max(layers.getOriginalWidth(),ic.getIconWidth()),layers.getOriginalHeight()+ic.getIconHeight(),Transparency.TRANSLUCENT);
				g2=(Graphics2D) bi.getGraphics();
				ic.paintIcon(MapPane.this,g2,0,0);
				g2.setTransform(AffineTransform.getTranslateInstance(0,ic.getIconHeight()));
				cic.paintIcon(MapPane.this,g2,0,0);
				/*if (layers.fitsVertically()) {
					int cx=(getWidth()-layers.getOriginalWidth())/2+offsetX;
					int cy=(getHeight()-layers.getOriginalHeight())/2+offsetY;
					tg.setClip(Math.max(0,cx),Math.max(0,cy),Math.min(getWidth(),layers.getOriginalWidth()),layers.getOriginalHeight());
				}*/
				//Scroll the image
				int sp=(int)-((ic.getIconHeight()*zoom-(getHeight()-cic.getIconHeight()*zoom)/2-offsetY+in.top)/zoom);
				int fp=(int)(((getHeight()-ic.getIconHeight()*zoom)/2+offsetY+in.top)/zoom);
				//Prevent scrolling too much
				if (fp>0)
					fp=0;
				int x=(int)(((getWidth()-cic.getIconWidth()*zoom)/2+offsetX+in.left)/zoom);
				for (int i=sp;i<=fp;i+=stepY) {
					//Make sure that the final step will be at the exact position
					if (i+stepY>fp  && !fixedEnd) {
						i=fp-stepY;
						fixedEnd=true;
					}
					tg.drawImage(bi,x,i,null);
				}
				break;
			case IRegionView.NEIGHBOUR_DOWN:
				//Create an image with the two backgrounds combined
				bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(Math.max(cic.getIconWidth(),ic.getIconWidth()),cic.getIconHeight()+ic.getIconHeight(),Transparency.TRANSLUCENT);
				g2=(Graphics2D) bi.getGraphics();
				cic.paintIcon(MapPane.this,g2,0,0);
				g2.setTransform(AffineTransform.getTranslateInstance(0,cic.getIconHeight()));
				ic.paintIcon(MapPane.this,g2,0,0);
				/*if (layers.fitsVertically()) {
					int cx=(getWidth()-layers.getOriginalWidth())/2+offsetX;
					int cy=(getHeight()-layers.getOriginalHeight())/2+offsetY;
					tg.setClip(Math.max(0,cx),Math.max(0,cy),Math.min(getWidth(),layers.getOriginalWidth()),layers.getOriginalHeight());
				}*/
				//Scroll the image
				sp=(int)((((getHeight()-cic.getIconHeight()*zoom)/2+offsetY)+in.top)/zoom);
				fp=(int)(-(cic.getIconHeight()*zoom-(getHeight()-ic.getIconHeight()*zoom)/2-offsetY-in.top)/zoom);
				//Prevent scrolling too much
				if (fp*zoom<getHeight()-bi.getHeight()*zoom)
					fp=(int)((getHeight()-bi.getHeight()*zoom)/zoom);
				x=(int)(((getWidth()-cic.getIconWidth()*zoom)/2+offsetX+in.left)/zoom);
				for (int i=sp;i>fp;i-=stepY) {
					//Make sure that the final step will be at the exact position
					if (i-stepY<fp && !fixedEnd) {
						i=fp+stepY;
						fixedEnd=true;
					}
					tg.drawImage(bi,x,i,null);
				}
				//Final step, exactly at the end location
				tg.drawImage(bi,x,fp,null);
				break;
			case IRegionView.NEIGHBOUR_RIGHT:
				//Create an image with the two backgrounds combined
				bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(layers.getOriginalWidth()+ic.getIconWidth(),Math.max(layers.getOriginalHeight(),ic.getIconHeight()),Transparency.TRANSLUCENT);
				g2=(Graphics2D) bi.getGraphics();
				cic.paintIcon(MapPane.this,g2,0,0);
				g2.setTransform(AffineTransform.getTranslateInstance(cic.getIconWidth(),0));
				ic.paintIcon(MapPane.this,g2,0,0);
				/*if (layers.fitsHorizontally()) {
					int cx=(getWidth()-layers.getOriginalWidth())/2+offsetX;
					int cy=(getHeight()-layers.getOriginalHeight())/2+offsetY;
					tg.setClip(Math.max(0,cx),Math.max(0,cy),Math.min(getWidth(),layers.getOriginalWidth()),layers.getOriginalHeight());
				}*/
				//Scroll the image
				sp=(int)((((getWidth()-cic.getIconWidth()*zoom)/2+offsetX)+in.left)/zoom);
				fp=(int)(-(cic.getIconWidth()*zoom-(getWidth()-ic.getIconWidth()*zoom)/2-offsetX-in.left)/zoom);
				//Prevent scrolling too much
				if (fp*zoom<getWidth()-bi.getWidth()*zoom)
					fp=(int)((getWidth()-bi.getWidth()*zoom)/zoom);
				int y=(int)(((getHeight()-cic.getIconHeight()*zoom)/2+offsetY+in.top)/zoom);
				for (int i=sp;i>=fp;i-=stepX) {
					//Make sure that the final step will be at the exact position
					if (i-stepX<fp && !fixedEnd) {
						i=fp+stepX;
						fixedEnd=true;
					}
					tg.drawImage(bi,i,y,null);
				}
				//Final step, exactly at the end location
				tg.drawImage(bi,fp,y,null);
				break;
			case IRegionView.NEIGHBOUR_LEFT:
				//Create an image with the two backgrounds combined
				bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(layers.getOriginalWidth()+ic.getIconWidth(),Math.max(layers.getOriginalHeight(),ic.getIconHeight()),Transparency.TRANSLUCENT);
				g2=(Graphics2D) bi.getGraphics();
				ic.paintIcon(MapPane.this,g2,0,0);
				g2.setTransform(AffineTransform.getTranslateInstance(ic.getIconWidth(),0));
				cic.paintIcon(MapPane.this,g2,0,0);
				/*if (layers.fitsHorizontally()) {
					int cx=(getWidth()-layers.getOriginalWidth())/2+offsetX;
					int cy=(getHeight()-layers.getOriginalHeight())/2+offsetY;
					tg.setClip(Math.max(0,cx),Math.max(0,cy),Math.min(getWidth(),layers.getOriginalWidth()),layers.getOriginalHeight());
				}*/
				//Scroll the image
				sp=(int)-((ic.getIconWidth()*zoom-(getWidth()-cic.getIconWidth()*zoom)/2-offsetX+in.left)/zoom);
				fp=(int)(((getWidth()-ic.getIconWidth()*zoom)/2+offsetX+in.left)/zoom);
				//Prevent scrolling too much
				if (fp>0)
					fp=0;
				y=(int)(((getHeight()-cic.getIconHeight()*zoom)/2+offsetY+in.top)/zoom);
				for (int i=sp;i<=fp;i+=stepX) {
					//Make sure that the final step will be at the exact position
					if (i+stepX>fp && !fixedEnd) {
						i=fp-stepX;
						fixedEnd=true;
					}
					tg.drawImage(bi,i,y,null);
				}
				break;
		}
		tg.scale(1/zoom,1/zoom);
		//Reclaim memory
		tg.dispose();
		g2.dispose();
		bi.flush();
		setCursor(old);
		clearTransformations();
		setRedrawEnabled(false);
		map.setActiveRegionView(rv);
		layers.viewSizeChanged(true);
		setRedrawEnabled(true);
		//Show the panes again
		if (aux!=null)
			aux.setVisible(true);
		if (motion!=null)
			motion.setVisible(true);
		bi.flush();
		bi=null;
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}

	void showLegend(boolean b) {
		if (b && legend==null) {
			//Legend
			legend=new Legend(this);
			legend.setSize(150,300);
			legend.setVisible(true);
			legend.setDoubleBuffered(false);
			//Legend tool changes state when the legend is closed
			legend.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosing(InternalFrameEvent e) {
					if (viewer.legend!=null)
						viewer.legend.setSelected(false);
				}
			});
			populateLegend();
			add(legend,LEGEND_LAYER);
		}
		if (legend!=null) {
			legend.setVisible(b);
			//Change the location of the legend if it is covered by the miniature
			if (viewer.miniaturePane!=null) {
				java.awt.Point p=SwingUtilities.convertPoint(legend,10,10,viewer.miniaturePane);
				if (viewer.miniaturePane.isVisible() && viewer.miniaturePane.contains(p))
					legend.setLocation(0,0);
			}
		}
	}
	/**
	 * The visible rectangle of the background image in pixels.
	 */
	protected java.awt.Rectangle getVisibleMapRect() {
		return transformRect(getInverseTransform(),getVisibleRect());
	}
	/**
	 * Loads an image from the jar file.
	 */
	protected ImageIcon loadImageIcon(String filename) {
		try {
			return new ImageIcon(MapPane.class.getResource(filename));
		} catch(Exception e) {
			System.out.println("Error loading Image Icon '"+filename+"'");
			try {
				return new ImageIcon(MapPane.class.getResource("images/notfound.gif"));
			} catch(Exception e1) {
			}
		}
		return null;
	}
	/**
	 * Controls the effect of "busy" status.
	 */
	protected void setBusy(boolean busy) {
		if (busy==viewer.getStatusBar().isBusy())
			return;
		if (busy) {
			if ((getCursor()!=null) && !(getCursor().equals(getCustomCursor("wait"))))
				cacheCursor=getCursor();
			setCursor(getCustomCursor("wait"));
			viewer.getStatusBar().setBusy(true);
		} else {
			if (cacheCursor!=null) {
				setCursor(cacheCursor);
				cacheCursor=null;
			}
			viewer.getStatusBar().setBusy(false);
		}
		this.busy=busy;
		if (legend!=null)
			legend.setBusy(busy);
	}

	/**
	 *  This method is used to clear the cached cursor in some circumstances. It's not
	 *  clear why the cursor is cached in a situation where the panMouseListener is used
	 *  together with a custom MouseInputListener (like in the case of the browse tool).
	 *  However in this case we want after panning finishes the cursor of the custom
	 *  tool to be applied to the MapPane and not the cached cursor. (GT 2)
	 */
	public void setCacheCursor(Cursor c) {
		cacheCursor=c;
	}

	private void resized() {
		if (motion!=null)
			motion.centerViewToAgents();
		if (getWidth()==oldWidth && getHeight()==oldHeight)
			return;
		oldWidth=getWidth(); oldHeight=getHeight();
		//Force offsets to draw map inside the component area, leaving no borders if possible
		setBounds(getX(),getY(),getWidth(),getHeight());
		if (layers!=null)
			layers.setBounds(0,0,getWidth(),getHeight());
		rebuildTransformation();
		rebuildPositionTransformation();
		addOffsetX(0);
		addOffsetY(0);
		layers.viewSizeChanged(true);
		if (labels!=null)
			labels.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		if (aux!=null)
			aux.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		if (motion!=null)
			motion.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		repaint();
	}
	/**
	 * Busy status.
	 */
	protected boolean isBusy() {
		return busy;
	}
	/**
	 * Creates the LabelPane when needed.
	 */
	protected LabelPane createLabelPane() {
		java.util.List lst=null;
		if (getForegroundComponent()==layers)
			lst=layers.removeAllListeners();
		else if (getForegroundComponent()==this)
			lst=removeAllListeners();
		labels=new LabelPane(this);
		add(labels,LABELS_LAYER);
		labels.setOpaque(false);
		labels.setDoubleBuffered(false);
		labels.setVisible(true);
		if (ins==null)
			ins=getInsets();
		labels.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		if (lst!=null)
			for (int i=0;i<lst.size();i++)
				labels.addMouseInputListener((MouseInputListener)lst.get(i));
		showCoordinates(isCoordinatesShown());
		ToolTipManager.sharedInstance().registerComponent(labels);
		return labels;
	}
	/**
	 * Creates the MotionPane when needed.
	 */
	protected MotionPane createMotionPane() {
		java.util.List lst=null;
		if (getForegroundComponent()==aux)
			lst=aux.removeAllListeners();
		else if (getForegroundComponent()==labels)
			lst=labels.removeAllListeners();
		else if (getForegroundComponent()==layers)
			lst=layers.removeAllListeners();
		else if (getForegroundComponent()==this)
			lst=removeAllListeners();
		motion=new MotionPane(this);
		add(motion,MOTION_LAYER);
		motion.setOpaque(false);
		motion.setDoubleBuffered(false);
		motion.setVisible(true);
		if (ins==null)
			ins=getInsets();
		motion.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		if (lst!=null)
			for (int i=0;i<lst.size();i++)
				motion.addMouseInputListener((MouseInputListener)lst.get(i));
		showCoordinates(isCoordinatesShown());
		ToolTipManager.sharedInstance().registerComponent(motion);
		return motion;
	}
	/**
	 * Shows the active region of the map.
	 */
	protected void showMap(IMapView map,boolean centerView) {
		if (layers==null) {
			java.util.List lst=null;
			//Layers
			if (getForegroundComponent()==layers)
				lst=removeAllListeners();
			layers=new LayerPane(this);
			layers.setOpaque(false);
			layers.setDoubleBuffered(false);
			add(layers,LAYERS_LAYER);
			layers.setVisible(true);
			layers.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
			if (lst!=null)
				for (int i=0;i<lst.size();i++)
					layers.addMouseInputListener((MouseInputListener)lst.get(i));
			ToolTipManager.sharedInstance().registerComponent(layers);
		}

		clearLargerScaleMapsRectangles();
		this.map=map;
		offsetX=offsetY=0;

		layers.setBackground(map.getActiveRegionView().getBackground());
		ILayerView[] lv=map.getActiveRegionView().getLayerViews();
		layers.viewSizeChanged(false);
		layers.setLayers(lv);
		if (layers.mayHaveLabels() || labels!=null) {
			if (isRedrawEnabled())
				getLabelPane().redrawImage();
			else
				labelsNeedRedrawing=true;
		}
		if (legend!=null) {
			populateLegend();
		}
	}
	/**
	 * Populates the legend with the layers.
	 */
	protected void populateLegend() {
		//Add layers to the legend
		legend.clearLayers();
		ILayerView[] lv=map.getActiveRegionView().getLayerViews();
		for (int i=lv.length-1;i>-1;i--)
			legend.addLayer(lv[i]);
		//Add backgrounds to the legend
		legend.clearBackgrounds();
		String[] sn=map.getActiveRegionView().getBackgroundNames();
		if (sn.length>1) {
			String ab=map.getActiveRegionView().getBackgroundName();
			for (int i=0;i<sn.length;i++) {
				BackgroundPanel bp=legend.addBackground(map.getActiveRegionView().getBackground(sn[i]),sn[i]);
				//Activate the shown background
				if (sn[i].equals(ab))
					bp.setSelected(true);
			}
		}
		//WHAT ABOUT THE AGENT PATHS??? %%%%%%%%%%%%%%%%%%%%%%
	}

	/**
	 * Clears the transorfmations.
	 */
	protected void clearTransformations() {
		transform=invTransform=posTransform=invPosTransform=null;
	}
	/**
	 * Clears the map.
	 */
	protected void clearMap() {
		clearLargerScaleMapsRectangles();
		if (layers!=null)
			layers.clearAll();

		//Center the view
		offsetX=offsetY=0;
		rotation=0;
		clearTransformations();

		//Null when agent classes don't exist
		if (viewer.agentPlug!=null && viewer.agentPlug.getProtocolPlugs().length!=0)
			viewer.agentPlug.disconnect();

		map=null;
		validate();
		repaint();
	}
	/**
	 * Shows the zoom rectangles.
	 */
	protected void showInnerRegionsRectangles() {
		clearLargerScaleMapsRectangles();
		IZoomRect[] rZoomRects=(IZoomRect[])map.getInnerRegions(map.getActiveRegionView());
		if ((rZoomRects==null) || (rZoomRects.length==0)) return;
		zoomRects=new AuxiliaryShape[rZoomRects.length];
		//Transform the zoom rects and add them to the view.
		for (int i=0;i<rZoomRects.length;i++) {
			IZoomRect adjusted=rZoomRects[i].createTransformedShape(getPositionTransform());
			//Make the rectangle at least 10 pixel wide and high
			if (adjusted.getWidth()<10) {
				adjusted.setX(adjusted.getX()-5);
				adjusted.setWidth(10);
			}
			if (adjusted.getHeight()<10) {
				adjusted.setY(adjusted.getY()-5);
				adjusted.setHeight(10);
			}
			zoomRects[i]=new AuxiliaryShape(adjusted,null,zoomOutline,zoomFill,true);
			getAuxiliaryPane().addShape(zoomRects[i]);
		}
		if (aux!=null)
			aux.repaint();
	}
	/**
	 * Clears the zoom rectangles.
	 */
	protected void clearLargerScaleMapsRectangles() {
		if ((zoomRects==null) || (zoomRects.length==0)) return;
		for (int i=0;i<zoomRects.length;i++) {
			getAuxiliaryPane().removeShape(zoomRects[i]);
		}
		zoomRects=null;
		if (aux!=null)
			aux.repaint();
	}

	/**
	 * Temporarily shows objects on top of everything. The objects are highlighted.
	 */
	protected void loom(Graphics2D g2,ILayerView lv,ArrayList objects) {
		layers.drawObjects(((IVectorLayerView) lv),objects,g2,true);
	}
	/**
	 * Gets the "layers" layer, where all the region layers are drawn.
	 */
	public LayerPane getLayersPane() {
		return layers;
	}
	/**
	 * Gets the glass pane layer, where things like the selection rect are drawn.
	 */
	protected AuxiliaryPane getAuxiliaryPane() {
		if (aux==null) {
			if (ins==null)
				ins=getInsets();
			java.util.List lst=null;
			if (getForegroundComponent()==labels)
				lst=labels.removeAllListeners();
			else if (getForegroundComponent()==layers)
				lst=layers.removeAllListeners();
			else if (getForegroundComponent()==this)
				lst=removeAllListeners();
			//Auxiliary
			aux=new AuxiliaryPane(this);
			aux.setOpaque(false);
			aux.setVisible(true);
			aux.setDoubleBuffered(false);
			add(aux,AUXILIARY_LAYER);
			aux.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
			if (lst!=null)
				for (int i=0;i<lst.size();i++)
					aux.addMouseInputListener((MouseInputListener)lst.get(i));
			showCoordinates(isCoordinatesShown());
			ToolTipManager.sharedInstance().registerComponent(aux);
		}
		return aux;
	}
	/**
	 * Gets the glass pane layer, where things like the selection rect are drawn.
	 */
	protected LabelPane getLabelPane() {
		if (labels==null)
			createLabelPane();
		return labels;
	}
	/**
	 * Gets the motion layer, where the agents are drawn.
	 */
	public MotionPane getMotionPane() {
		if (motion==null)
			createMotionPane();
		return motion;
	}
	/**
	 * These rendering hints are used by all the panes included in this MapPane.
	 */
	protected RenderingHints getRenderingHints() {
		if (renderingHints==null) {
			//Adjust rendering hints
			renderingHints=new RenderingHints(null);
			if (antialiasing)
				renderingHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			else
				renderingHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
			//Use only speed settings
			if (quality) {
				renderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
				renderingHints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				renderingHints.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			} else {
				renderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
				renderingHints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
				renderingHints.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
			}
			renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			renderingHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
		}
		return renderingHints;
	}
	/**
	 * Transforms a rectangle with the given transformation.
	 */
	Rectangle2D transformRect(AffineTransform trans,Rectangle2D r) {
		ptr.setLocation(r.getX(),r.getY());
		trans.transform(ptr,ptr);
		double x=ptr.getX();
		double y=ptr.getY();
		ptr.setLocation(r.getX()+r.getWidth(),r.getY()+r.getHeight());
		trans.transform(ptr,ptr);
		r.setRect(Math.min(x,ptr.getX()),Math.min(y,ptr.getY()),Math.abs(ptr.getX()-x),Math.abs(ptr.getY()-y));

		return r;
	}
	/**
	 * Transforms a rectangle with the given transformation.
	 */
	java.awt.Rectangle transformRect(AffineTransform trans,java.awt.Rectangle r) {
		ptr.setLocation(r.getX(),r.getY());
		trans.transform(ptr,ptr);
		double x=ptr.getX();
		double y=ptr.getY();
		ptr.setLocation(r.getX()+r.getWidth(),r.getY()+r.getHeight());
		trans.transform(ptr,ptr);
		r.setRect(Math.min(x,ptr.getX()),Math.min(y,ptr.getY()),Math.abs(ptr.getX()-x),Math.abs(ptr.getY()-y));

		return r;
	}
	/**
	 * Gets the transformation formula. The transformation formula applies rotation, zooming
	 * and handles spherical coordinates and projections.
	 */
	protected AffineTransform getTransform() {
		if (transform==null) {
			transform=getTransform(getWidth(),getHeight());
		}
		return transform;
	}
	/**
	 * Gets the transformation formula for the given window size.
	 */
	protected AffineTransform getTransform(int w,int h) {
		if (transform==null) {
			if (layers!=null) {
				addOffsetX(0);
				addOffsetY(0);
				transform=AffineTransform.getTranslateInstance(Math.round(((w-layers.getOriginalWidth()*zoom)/2)+offsetX),Math.round(((h-layers.getOriginalHeight()*zoom)/2)+offsetY));
				//transform=AffineTransform.getRotateInstance(rotation,w/2d+offsetX,h/2d+offsetY);
				//transform.translate(Math.round(((w-layers.getOriginalWidth()*zoom)/2)+offsetX),Math.round(((h-layers.getOriginalHeight()*zoom)/2)+offsetY));
			} else
				transform=new AffineTransform();
			try {
				invTransform=transform.createInverse();
			} catch(java.awt.geom.NoninvertibleTransformException e) {
				invTransform=null;
			};
		}
		return transform;
	}
	/**
	 * Sets the position translation formula. The transformation formula handles origin and coordinates.
	 */
	protected AffineTransform getPositionTransform() {
		//Component in initialization
		if (map==null) {
			posTransform=invPosTransform=null;
			return null;
		}
		if (posTransform==null) {
			Rectangle2D.Double r=(Rectangle2D.Double) map.getActiveRegionView().getBoundingRect();
			if (r!=null) {
				double zoomRatioX,zoomRatioY;
				Icon background=layers.getCurrentBackground();
				//Calculate which portion of the area is shown
				if (background==null) {
					//No background. The rectangle will be placed inside the area.
					//Find the smallest ratio to fit all the region in the
					//initial visible area. Calculate the zoom ratio.
					if (layers.getOriginalWidth()/r.getWidth()>=layers.getOriginalHeight()/r.getHeight())
						zoomRatioX=zoomRatioY=layers.getOriginalHeight()/r.getHeight()*zoom;
					else
						zoomRatioX=zoomRatioY=layers.getOriginalWidth()/r.getWidth()*zoom;
				} else {
					//The rectangle given for the region will exactly fit the background
					zoomRatioX=background.getIconWidth()/r.getWidth()*zoom;
					zoomRatioY=background.getIconHeight()/r.getHeight()*zoom;
				}
				//Aply the rotation based on the region orientation
				double radOrient=map.getActiveRegionView().getOrientation()*TO_RAD;
				AffineTransform trans=AffineTransform.getRotateInstance(radOrient,r.x+r.width/2,r.y+r.height/2);
				trans.scale(zoomRatioX,-zoomRatioY);
				trans.translate(-r.x,-r.y-r.height);
				posTransform=trans;
				setValidCoordinates(true);
			} else {
				posTransform=new AffineTransform();
				setValidCoordinates(false);
			}
			//Create the inverse transformation
			try {
				invPosTransform=posTransform.createInverse();
			} catch(java.awt.geom.NoninvertibleTransformException e) {
				invPosTransform=null;
			};

			updateScale();
		}

		return posTransform;
	}
	/**
	 * Changes the zoom.
	 */
	protected void setZoom(double d) {
		//Restrict to the zoom range
		if (viewer.zoom!=null) {
			if (d*100>viewer.zoom.sldZoom.getMaximum())
				d=viewer.zoom.sldZoom.getMaximum()/100d;
			else if (d*100<viewer.zoom.sldZoom.getMinimum())
				d=viewer.zoom.sldZoom.getMinimum()/100d;
		}

		//###Exit###
		if (d==zoom || d<=0)
			return;

		//Restrict to 2 decimal places
		d=((int) (d*100))/100d;

		//Rebuild the transform the next time needed.
		double dif=d-zoom;
		offsetX=(int) Math.round(offsetX*d/zoom);
		offsetY=(int) Math.round(offsetY*d/zoom);
		zoom=d;
		addOffsetX(0);
		addOffsetY(0);

		//Rebuild transformations
		rebuildTransformation();
		rebuildPositionTransformation();
		//Don't start a vector repaint. This is done by the tools. Although asynchronous
		//and actually a side-effect, it is faster!
		if (layers!=null)
			layers.viewZoomChanged(false);
		viewer.checkPanTool();
		//Reposition the zoom rectangles
		if (viewer.goin!=null && viewer.goin.isSelected())
			viewer.goin.doClick();
		repaint();
	}

	protected void updateScale() {
		if (hasValidCoordinates()) {
			//Initialize scale
			if (screenSizeMM==null) {
				Dimension tkSS=Toolkit.getDefaultToolkit().getScreenSize();
				screenSizeMM=new Dimension(
					(int) Math.round((1d*tkSS.width)/Toolkit.getDefaultToolkit().getScreenResolution()/0.254d),
					(int) Math.round((1d*tkSS.height)/Toolkit.getDefaultToolkit().getScreenResolution()/0.254d)
				);
				screenSizePXL=new Dimension(tkSS.width,tkSS.height);
			}
			//Find the size of the drawing area in millimeters
			double sizeXMM=(1d*getWidth())*screenSizeMM.width/screenSizePXL.width;
			double sizeYMM=(1d*getHeight())*screenSizeMM.height/screenSizePXL.height;
			//The length of the diagonal of the drawing area in meters is
			double daDiag=Math.sqrt(sizeXMM*sizeXMM+sizeYMM*sizeYMM)/1000d;
			//The length of the view in reality is
			Point2D.Double p=new Point2D.Double();
			synchronized (p) {
				p.setLocation(0,0);
				AffineTransform inv=getInversePositionTransform();
				if (inv==null)
					return;
				inv.transform(p,p);
				double x=p.x;
				double y=p.y;
				p.setLocation(getWidth(),getHeight());
				inv.transform(p,p);
				double reDiag=map.getActiveRegionView().measureDistance(x,y,p.x,p.y);
				getAuxiliaryPane().setScale(reDiag/daDiag);
			}
		}
	}

	protected double getZoom() {
		return zoom;
	}

	public void setBounds(int x,int y,int w,int h) {
		if (w==oldWidth && h==oldHeight)
			return;
		super.setBounds(x,y,w,h);
		oldWidth=w; oldHeight=h;
		//Force offsets to draw map inside the component area, leaving no borders if possible
		if (layers!=null) {
			clearTransformations();
			getTransform(w,h);
			addOffsetX(0);
			addOffsetY(0);
			layers.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
			layers.viewSizeChanged(true);
			//First send a resize event without redrawing and then start a timer to send
			//a redraw event after 0.5sec, if no other resize event has arrived.
			if (delayResizeEvent==null) {
				//Delay to avoid sending too many resize events
				delayResizeEvent=new Timer(500,new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (layers!=null)
							layers.viewSizeChanged(true);
					}
				});
				delayResizeEvent.setRepeats(false);
				delayResizeEvent.setCoalesce(true);
			}
			if (!delayResizeEvent.isRunning())
				delayResizeEvent.start();
			else
				delayResizeEvent.restart();
		}
		if (labels!=null)
			labels.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		if (aux!=null)
			aux.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
		if (motion!=null)
			motion.setBounds(ins.left,ins.top,getWidth()-ins.left-ins.right,getHeight()-ins.top-ins.bottom);
	}
	/**
	 * OffsetX is the the offset from the center in X-Axis.
	 */
	void addOffsetX(int i) {
		if (layers==null)
			return;
		double lw=layers.getOriginalWidth()*zoom;
		offsetX+=i;
		if (lw<=getWidth())
			offsetX=0;
		else {
			//Don't let the view go out of the map pane area. The map should scroll only
			//until the edges of the component.
			//Next offsetX would be...
			int next=(int) Math.round(((getWidth()-lw)/2)+offsetX);
			//If positive, the map will leave a border to the left.
			if (next>0)
				offsetX-=next;
			//The next offset should be no less than the sum of the zoomed size of the region
			//and the size of the component.
			if (lw+next<getWidth())
				offsetX=(int) Math.round((getWidth()-lw)/2);
		}
	}
	/**
	 * OffsetX is the the offset from the center in X-Axis.
	 */
	void addOffsetY(int i) {
		if (layers==null)
			return;
		double lh=layers.getOriginalHeight()*zoom;

		offsetY+=i;

		if (lh<=getHeight())
			offsetY=0;
		else {
			//Don't let the view go out of the map pane area. The map should scroll only
			//until the edges of the component.
			//Next offsetY would be...
			int next=(int) /*Math.round(*/((getHeight()-lh)/2)+offsetY;
			//If positive, the map will leave a border to the left.
			if (next>0)
				offsetY-=next;
			//The next offset should be no less than the sum of the zoomed size of the region
			//and the size of the component.
			if (lh+next<getHeight())
				offsetY=(int) Math.round((getHeight()-lh)/2);
		}
	}

	/**
	 * Gets the inverse transformation formula. The transformation formula removes rotation, zooming
	 * and handling of spherical coordinates and projections.
	 */
	protected AffineTransform getInverseTransform() {
		if (invTransform==null) {
			getTransform();
			try {
				invTransform=transform.createInverse();
			} catch(NoninvertibleTransformException ex) {
				invTransform=new AffineTransform();
			}
		}
		return invTransform;
	}
	/**
	 * Sets the position translation formula. The transformation formula handles origin and coordinates.
	 */
	protected AffineTransform getInversePositionTransform() {
		if (invPosTransform==null)
			getPositionTransform();
		return invPosTransform;
	}
	/**
	 * Rebuilds the screen transformation formula and its inverse.
	 */
	void rebuildTransformation() {
		transform=null;
		invTransform=null;
		getTransform();
	}
	/**
	 * Rebuilds the position transformation formula and its inverse.
	 */
	void rebuildPositionTransformation() {
		posTransform=null;
		invPosTransform=null;
		getPositionTransform();
	}

	boolean hasValidCoordinates() {
		return validCoordinates;
	}

	void setValidCoordinates(boolean b) {
		validCoordinates=b;
		viewer.showAgentPlug(b);
	}
	/**
	 * @return <em>True</em> when the map fits in the panel in both directions.
	 */
	protected boolean fitsOK() {
		if (layers==null)
			return true;
		return layers.fitsHorizontally() && layers.fitsVertically();
	}
	/**
	 * Sets the antialiasing property.
	 */
	protected void setAntialiasing(boolean aa) {
		antialiasing=aa;
		renderingHints=null;
		if (layers!=null) {
			layers.redrawImage();
			repaint();
		}
	}
	/**
	 * Gets the antialiasing property.
	 */
	protected boolean getAntialiasing() {
		return antialiasing;
	}
	/**
	 * Sets the quality property. True=quality optimized, False=speed optimized.
	 */
	protected void setQualityOverSpeed(boolean q) {
		quality=q;
		renderingHints=null;
		if (layers!=null) {
			layers.redrawImage();
			repaint();
		}
	}
	/**
	 * Gets the quality property. True=quality optimized, False=speed optimized.
	 */
	protected boolean getQualityOverSpeed() {
		return quality;
	}
	/**
	 * Checks if the legend frame is shown.
	 */
	protected boolean isLegendVisible() {
		return (legend!=null && legend.isVisible());
	}
	/**
	 * Refreshes the legend frame.
	 */
	protected void repaintLegend() {
		if (legend!=null)
			legend.repaint();
	}
	/**
	 * Gets the Legend frame. The caller should check if it is null first.
	 */
	protected Legend getLegend() {
		return legend;
	}

	void showCoordinates(boolean b) {
		 if (coordsListener!=null) {
			removeMouseListener(coordsListener);
			removeMouseMotionListener(coordsListener);
			if (layers!=null) {
				layers.removeMouseListener(coordsListener);
				layers.removeMouseMotionListener(coordsListener);
			}
			if (aux!=null) {
				aux.removeMouseListener(coordsListener);
				aux.removeMouseMotionListener(coordsListener);
			}
			if (motion!=null) {
				motion.removeMouseListener(coordsListener);
				motion.removeMouseMotionListener(coordsListener);
			}
		 }
		 if (b) {
			coordsListener=getCoordinatesMouseListener();
			((JComponent)getForegroundComponent()).addMouseListener(coordsListener);
			((JComponent)getForegroundComponent()).addMouseMotionListener(coordsListener);
		} else {
			if (aux!=null)
				aux.setCoords("");
			coordsListener=null;
		}
		if (aux!=null)
			aux.repaint(aux.getX()+aux.getWidth()-50,aux.getY(),150,15);
	}

	boolean isCoordinatesShown() {
		return (coordsListener!=null);
	}
	/**
	 * Called by the legend to inform for a change. Didn't use the standard method of listeners
	 * because it would be a waste. There is always only one listener.
	 */
	void legendLayerVisibilityChanged(ILayerView l,boolean b) {
		if (l.isVisible()!=b)
			l.setVisible(b);
	}
	/**
	 * Called by the legend to inform for a change. Didn't use the standard method of listeners
	 * because it would be a waste. There is always only one listener.
	 */
	void legendPathVisibilityChanged(Object a,boolean b) {
		if (motion!=null)
			if (motion.hasPathVisible((gr.cti.eslate.protocol.IAgent) a)!=b)
				motion.setPathVisible((gr.cti.eslate.protocol.IAgent) a,b);
	}
	/**
	 * Called by the legend to inform for a change. Didn't use the standard method of listeners
	 * because it would be a waste. There is always only one listener.
	 */
	void legendLayersReordered(int[] order) {
		map.getActiveRegionView().reorderLayers(order);
	}
	/**
	 * Called by the legend to inform for a change. Didn't use the standard method of listeners
	 * because it would be a waste. There is always only one listener.
	 */
	void legendBackgroundChanged(String id) {
		if (!id.equals(map.getActiveRegionView().getBackgroundName()))
			map.getActiveRegionView().setBackground(id);
	}
	/**
	 * Scans for the objects near the given point in screen coordinates, in all layers
	 * @return An array with the objects found and their layers.
	 */
	private ObjectBaseArray scanAround(int x,int y) {
		scanArray.clear();
		Rectangle2D.Double rect=new Rectangle2D.Double(x-PIXEL_TOLERANCE,y-PIXEL_TOLERANCE,2*PIXEL_TOLERANCE,2*PIXEL_TOLERANCE);
		transformRect(getInverseTransform(),rect);
		transformRect(getInversePositionTransform(),rect);
		//Clickpoint
		p2d.setLocation(x,y);
		getInverseTransform().transform(p2d,p2d);
		getInversePositionTransform().transform(p2d,p2d);
		getAuxiliaryPane().removeNotZoomShapes();
		ILayerView[] lv=map.getActiveRegionView().getLayerViews();
		double tol=PIXEL_TOLERANCE*map.getActiveRegionView().getUnitsPerMeter()*getMetersPerPixel();
		for (int i=lv.length-1;i>-1;i--)
			if (lv[i].isVisible()) {
				GeographicObject obj=lv[i].getGeographicObjectAt(p2d.x,p2d.y,Math.max(rect.width/2,rect.height/2));
				AuxiliaryShape as;
				//When only one candidate
				if (obj!=null) {
					if (lv[i] instanceof IRasterLayerView) {
						scanArray.add(obj);
						scanArray.add(lv[i]);
					} else {
						//Polylines tend to declare presence when the line looks almost closed,
						//like a "U". Inside the "U" Java methods tell that the line is there.
						//Using the following line, we eliminate this.
						if (obj instanceof IPolyLine && ((IPolyLine)obj).calculateDistance(p2d)>tol)
							continue;
						scanArray.add(obj);
						scanArray.add(lv[i]);
						getAuxiliaryPane().addShape(new AuxiliaryShape(lv[i],(IVectorGeographicObject) obj));
					}
				}
			}
		return scanArray;
	}

	private ObjectBaseArray scanAroundForAllInLayer(int x,int y) {
		scanArray.clear();
		Rectangle2D.Double rect4other=new Rectangle2D.Double(x-PIXEL_TOLERANCE,y-PIXEL_TOLERANCE,2*PIXEL_TOLERANCE,2*PIXEL_TOLERANCE);
		Rectangle2D.Double rect4point=new Rectangle2D.Double(x-20*PIXEL_TOLERANCE,y-20*PIXEL_TOLERANCE,40*PIXEL_TOLERANCE,40*PIXEL_TOLERANCE);
		Rectangle2D.Double rect;
		transformRect(getInverseTransform(),rect4other);
		transformRect(getInversePositionTransform(),rect4other);
		transformRect(getInverseTransform(),rect4point);
		transformRect(getInversePositionTransform(),rect4point);
		//Clickpoint
		p2d.setLocation(x,y);
		getInverseTransform().transform(p2d,p2d);
		getInversePositionTransform().transform(p2d,p2d);
		ILayerView[] lv=map.getActiveRegionView().getLayerViews();
		getAuxiliaryPane().removeNotZoomShapes();
		double tol=PIXEL_TOLERANCE*map.getActiveRegionView().getUnitsPerMeter()*getMetersPerPixel();
		for (int i=lv.length-1;i>-1;i--)
			if (lv[i].isVisible()) {
				if (lv[i] instanceof IPointLayerView)
					rect=rect4point;
				else
					rect=rect4other;
				GeographicObject obj=lv[i].getGeographicObjectAt(p2d.x,p2d.y,Math.max(rect.width/2,rect.height/2));
				AuxiliaryShape as;
				//When only one candidate
				if (obj!=null) {
					if (lv[i] instanceof IRasterLayerView) {
						scanArray.add(obj);
						scanArray.add(lv[i]);
					} else {
						//Polylines tend to declare presence when the line looks almost closed,
						//like a "U". Inside the "U" Java methods tell that the line is there.
						//Using the following line, we eliminate this.
						if (obj instanceof IPolyLine && ((IPolyLine)obj).calculateDistance(p2d)>tol)
							continue;
						//Points declared presence only a few pixels around the actual point,
						//no matter how big its representation is. The following code segment
						//searches a greater area and then calculates using viewer geometry.
						if (obj instanceof IPoint) {
							double toCoords=getInversePositionTransform().getScaleX();
							if (lv[i].getPaintMode()==IPointLayerView.PAINT_AS_CIRCLE) {
								double distance=p2d.distance(((IPoint)obj).getX(),((IPoint)obj).getY());
								if (((IPointLayerView)lv[i]).getCircleRadius()*toCoords<distance)
									continue;
							} else {
								int w,h;
								Icon ic=(obj.isSelected()?((IPointLayerView)lv[i]).getSelectedIcon((IPoint)obj):((IPointLayerView)lv[i]).getNormalIcon((IPoint)obj));
								if (ic==null)
									w=h=((IPointLayerView)lv[i]).getCircleRadius();
								else {
									w=ic.getIconWidth();
									h=ic.getIconHeight();
								}
								Rectangle2D.Double rectp=new Rectangle2D.Double(((IPoint)obj).getX()-toCoords*w/2d,((IPoint)obj).getY()-toCoords*h/2d,toCoords*w,toCoords*h);
								if (p2d.x<rectp.x || p2d.y<rectp.y || p2d.x>rectp.x+rectp.width || p2d.y>rectp.y+rectp.height)
									continue;
							}
						}
						scanArray.add(obj);
						scanArray.add(lv[i]);
						if (lv[i].isObjectSelectable()) {
							as=new AuxiliaryShape(lv[i],(IVectorGeographicObject) obj);
							getAuxiliaryPane().addShape(as);
							//Rectangle2D.Double r=new Rectangle2D.Double(((IVectorGeographicObject)obj).getBoundingMinX(),((IVectorGeographicObject)obj).getBoundingMaxY(),((IVectorGeographicObject)obj).getBoundingMaxX()-((IVectorGeographicObject)obj).getBoundingMinX(),-((IVectorGeographicObject)obj).getBoundingMaxY()+((IVectorGeographicObject)obj).getBoundingMinY());
							//transformRect(getTransform(),r);
							//transformRect(getPositionTransform(),r);
							//getAuxiliaryPane().repaint((int)r.x,(int)r.y,(int)r.width+1,(int)r.height+1);
							repaintAux(((IVectorGeographicObject)obj).getBounds2D());
						}
					}
				}
			}
		return scanArray;
	}
	/**
	 * Meters per pixel. This is valid only for cartesian coordinates and no projection.
	 */
	private double getMetersPerPixel() {
		Rectangle2D.Double boundRect=(Rectangle2D.Double) map.getActiveRegionView().getBoundingRect();
		if (boundRect!=null)
			//Calculate it in the quarter of the line that divides the pane in two horizontaly.
			//This is to avoid measuring a distance greater than half the earth which makes the algorithm fail.
			return map.getActiveRegionView().measureDistance(boundRect.getX(),boundRect.getY()+boundRect.getHeight()/2,boundRect.getX()+boundRect.getWidth()/4,boundRect.getY()+boundRect.getHeight()/2)/(getLayersPane().getOriginalWidth()/4);
		else
			return 0;
	}
	/**
	 * Mouse Listener for activate tool.
	 */
	protected MouseInputListener getActivateMouseListener() {
		if (lsnrActivate==null)
		lsnrActivate=new MouseInputAdapter() {
			public void mouseEntered(MouseEvent e) {
				setToolTipText(null);
			}
			public void mouseExited(MouseEvent e) {
				if (scanArray.size()!=0) {
					getAuxiliaryPane().removeAllShapes();
					getAuxiliaryPane().repaint();
				}
				setToolTipText(null);
			}
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==1) {
					if (scanArray.size()!=0)
						for (int i=0;i<scanArray.size();i=i+2)
							if (((ILayerView) scanArray.get(i+1)).isObjectSelectable() || ((ILayerView) scanArray.get(i+1)) instanceof IRasterLayerView)
								((ILayerView) scanArray.get(i+1)).setActiveGeographicObject((GeographicObject) scanArray.get(i));
				}
			}
			public void mouseMoved(MouseEvent e) {
				if (layers==null)
					return;
				//A mouse event must be inside the clip area.
			   /*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					//scanAroundForAll(e.getX(),e.getY());
					if (scanArray.size()!=0) {
						getAuxiliaryPane().removeNotZoomShapes();
						///instead, use updateSelectionShape
						//updateSelectionShape();

						getAuxiliaryPane().repaint();
					}
					setToolTipText(null);
					setCursor(getCustomCursor("default"));
					return;
				}*/

				int pr=getAuxiliaryPane().countShapes();
				if (pr>0) {
					ArrayList s=(ArrayList)getAuxiliaryPane().getShapes().clone();
					getAuxiliaryPane().removeAllShapes();
					for (int i=0;i<s.size();i++) {
						Object obj=s.get(i);
						if (obj instanceof AuxiliaryShape)
							repaintAux(((IVectorGeographicObject)((AuxiliaryShape)obj).shape).getBounds2D());
					}
				}

				//Look the agent paths first, if no agent path exists to activate, look for geographic objects
				boolean foundAgent=false;
				if (motion!=null && viewer.agentPlug!=null) {
					gr.cti.eslate.base.Plug[] dp=viewer.agentPlug.getProviders();
					p2d.setLocation(e.getX(),e.getY());
					getInverseTransform().transform(p2d,p2d);
					getInversePositionTransform().transform(p2d,p2d);
					//Meters per pixel
					double mpu=map.getActiveRegionView().getUnitsPerMeter()*getMetersPerPixel();
					double tol=PIXEL_TOLERANCE*mpu;
					for (int i=dp.length-1;i>-1;i--) {
						IAgent ag=(IAgent) dp[i].getHandle().getComponent();
						if (motion.hasPathVisible(ag)) {
							gr.cti.eslate.agent.Path path=ag.getPath();
							for (int j=path.size()-1;j>-1;j--) {
//								if (path.get(j).calculateDistance(p2d,mpu)<=tol) {
//									setCursor(getCustomCursor("activateagentpathcursor"));
//									foundAgent=true;
//								}
							}
						}
					}
				}
				if (!foundAgent) {
					ObjectBaseArray o=scanAroundForAllInLayer(e.getX(),e.getY());
					if (o.size()!=0) {
						String tt=produceMultiLineToolTip(o);
                        boolean isAtLeastOneObjectSelectable=false;
						for (int i=1;i<o.size() && ! isAtLeastOneObjectSelectable;i+=2)
							if (((ILayerView)o.get(i)).isObjectSelectable())
								isAtLeastOneObjectSelectable=true;
						if (isAtLeastOneObjectSelectable || tt!=null)
							setCursor(getCustomCursor("hand"));
						else
							setCursor(getCustomCursor("activatecursor"));
					} else {
						setCursor(getCustomCursor("activatecursor"));
						if (pr!=0)
							getAuxiliaryPane().repaint();
						//Reset the tooltip text
						setToolTipText(null);
					}
				}
			}
		};
		return lsnrActivate;
	}

	private String produceMultiLineToolTip(ObjectBaseArray o) {
		String tt=null;
		int i=0;
		//Look for the topmost tooltip
		//...or show tooltips from all layers.(N)
		String fontName=getFont().getName();
		StringBuffer multiLineToolTip=new StringBuffer("<html><font face="+fontName+" size=2>");
		boolean atLeastOneToolTipExists = false;
		while (i<o.size()) {
			tt=((ILayerView) scanArray.get(i+1)).getTip(((GeographicObject) scanArray.get(i)));
			i=i+2;
			if (tt!=null){
			   multiLineToolTip.append(tt);
			   multiLineToolTip.append("<P>");
			   atLeastOneToolTipExists = true;
			}
		}
		String multi=null;
		//Set the tooltip text to the label of the object
		if (atLeastOneToolTipExists) {
			multiLineToolTip.delete(multiLineToolTip.length()-3,multiLineToolTip.length());
			multiLineToolTip.append("</font></html>");
			multi=new String(multiLineToolTip);
			setToolTipText(multi);
		} else {
			setToolTipText(null);
		}
		return multi;
	}

	public String getMultilineTooltip(int x,int y) {
     	return produceMultiLineToolTip(scanAroundForAllInLayer(x,y));
	}


	/**
	 * Mouse Listener for rectangle select tool.
	 */
	protected MouseInputListener getSelectRectangleMouseListener() {
		if (lsnrSelectRect==null)
		lsnrSelectRect=new SelectionMouseListener() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount()>1)
					return;
				//Sometimes, the window of the other selection tool flashes, so hide it.
				if (lsnrSelectCircle!=null)
					((SelectionMouseListener)lsnrSelectCircle).frame.setVisible(false);
				super.mousePressed(e);
				if (!SwingUtilities.isRightMouseButton(e)) {
					java.awt.Point p;
					if (cacheShape==null || !resizing)
						p=new java.awt.Point(e.getX(),e.getY());
					else {
						//Show the Radius popup in the center of the existing shape.
						Rectangle2D r=cacheShape.getBounds2D();
						p2d.setLocation(r.getX()+r.getWidth()/2,r.getY()+r.getHeight()/2);
						getPositionTransform().transform(p2d,p2d);
						getTransform().transform(p2d,p2d);
						p=new java.awt.Point((int)p2d.x,(int)p2d.y);
					}
					SwingUtilities.convertPointToScreen(p,MapPane.this);
					frame.setLocation(p.x+5,p.y+5);
					if (resizing) {
						//Create a new shape to make cancelling possible
						RectangularSelectionShape eas=new RectangularSelectionShape(((Rectangle2D.Double)cacheShape).getX(),((Rectangle2D.Double)cacheShape).getY(),((Rectangle2D.Double)cacheShape).getX()+((Rectangle2D.Double)cacheShape).getWidth(),((Rectangle2D.Double)cacheShape).getY()+((Rectangle2D.Double)cacheShape).getHeight(),MapPane.this);
						aux.setSelectionShape(eas);
					}
				}
			}
			public void mouseMoved(MouseEvent e) {
				//Where the mouse is now
				p2d.setLocation(e.getX(),e.getY());
				getInverseTransform().transform(p2d,p2d);
				getInversePositionTransform().transform(p2d,p2d);
				Shape as=getAuxiliaryPane().getSelectionShape();
				if (as!=null && as instanceof Rectangle2D.Double) {
					Rectangle2D.Double ras=(Rectangle2D.Double)as;
					//Center of the current shape
					double cx=ras.getX()+ras.getWidth()/2;
					double cy=ras.getY()+ras.getHeight()/2;
					if((Math.abs(p2d.x-ras.getX())<5*getInversePositionTransform().getScaleX()*getInverseTransform().getScaleX() && p2d.y>=ras.getY() && p2d.y<=ras.getY()+ras.getHeight())
					 ||(Math.abs(p2d.x-ras.getX()-ras.getWidth())<5*getInversePositionTransform().getScaleX()*getInverseTransform().getScaleX() && p2d.y>=ras.getY() && p2d.y<=ras.getY()+ras.getHeight())
					 ||(Math.abs(p2d.y-ras.getY())<5*getInversePositionTransform().getScaleX()*getInverseTransform().getScaleX() && p2d.x>=ras.getX() && p2d.x<=ras.getX()+ras.getWidth())
					 ||(Math.abs(p2d.y-ras.getY()-ras.getHeight())<5*getInversePositionTransform().getScaleX()*getInverseTransform().getScaleX() && p2d.x>=ras.getX() && p2d.x<=ras.getX()+ras.getWidth()))
					{
						resizing=true;
						if (p2d.x>=cx && p2d.y>=cy) {
							setCursor(getCustomCursor("resizeNE"));
							clickPoint.setLocation(ras.getX(),ras.getY());
						} else if (p2d.x>=cx && p2d.y<cy) {
							setCursor(getCustomCursor("resizeSE"));
							clickPoint.setLocation(ras.getX(),ras.getY()+ras.getHeight());
						} else if (p2d.x<cx && p2d.y>=cy) {
							setCursor(getCustomCursor("resizeNW"));
							clickPoint.setLocation(ras.getX()+ras.getWidth(),ras.getY());
						} else if (p2d.x<cx && p2d.y<cy) {
							setCursor(getCustomCursor("resizeSW"));
							clickPoint.setLocation(ras.getX()+ras.getWidth(),ras.getY()+ras.getHeight());
						} else
							setCursor(getCustomCursor("crosshair"));
					} else {
						resizing=false;
						setCursor(getCustomCursor("crosshair"));
					}
				} else {
					resizing=false;
					setCursor(getCustomCursor("crosshair"));
				}
			}
			void createShapeAndUpdateFrame() {
				double minX=Math.min(clickPoint.x,p2d.x);
				double minY=Math.min(clickPoint.y,p2d.y);
				double maxX=Math.max(clickPoint.x,p2d.x);
				double maxY=Math.max(clickPoint.y,p2d.y);
				SelectionShape as=aux.getSelectionShape();
				if (as!=null && as instanceof Rectangle2D.Double)
					((Rectangle2D.Double)as).setRect(minX,minY,maxX-minX,maxY-minY);
				else
					aux.setSelectionShape(new RectangularSelectionShape(minX,minY,maxX,maxY,MapPane.this));
				//Show a small window with the area
				if (viewer.isSelectionPopupEnabled()) {
					as=getAuxiliaryPane().getSelectionShape();
					double area=map.getActiveRegionView().measureDistance(as.getX(),as.getY(),as.getX()+as.getWidth(),as.getY());
					area*=map.getActiveRegionView().measureDistance(as.getX(),as.getY(),as.getX(),as.getY()+as.getHeight());
					if (area>1000000d)
						frame.setText(km.format(area/1000000d)+"km²");
					else
						frame.setText(((int)area)+"m²");
					frame.show();
				}
			}
		};
		return lsnrSelectRect;
	}

	/**
	 * Mouse Listener for cycle select tool.
	 * Actually, neither this nor the getSelectRectangleMouseListener
	 * are precise in earth coordinates. They are not projected.
	 */
	protected MouseInputListener getSelectCircleMouseListener() {
		if (lsnrSelectCircle==null)
		lsnrSelectCircle=new SelectionMouseListener() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount()>1)
					return;
				super.mousePressed(e);
				//Sometimes, the window of the other selection tool flashes, so hide it.
				if (lsnrSelectCircle!=null)
					((SelectionMouseListener)lsnrSelectRect).frame.setVisible(false);
				if (!SwingUtilities.isRightMouseButton(e)) {
					java.awt.Point p;
					if (cacheShape==null || !resizing)
						p=new java.awt.Point(e.getX(),e.getY());
					else {
						//Show the Radius popup in the center of the existing shape.
						Rectangle2D r=cacheShape.getBounds2D();
						p2d.setLocation(r.getX()+r.getWidth()/2,r.getY()+r.getHeight()/2);
						getPositionTransform().transform(p2d,p2d);
						getTransform().transform(p2d,p2d);
						p=new java.awt.Point((int)p2d.x,(int)p2d.y);
					}
					SwingUtilities.convertPointToScreen(p,MapPane.this);
					frame.setLocation(p.x,p.y);
					if (resizing) {
						//When resizing, keep the same center
						if (cacheShape!=null && cacheShape instanceof CircularSelectionShape) {
							//Create a new shape to make cancelling possible
							CircularSelectionShape eas=new CircularSelectionShape(((Ellipse2D.Double)cacheShape).getX(),((Ellipse2D.Double)cacheShape).getY(),((Ellipse2D.Double)cacheShape).getX()+((Ellipse2D.Double)cacheShape).getWidth(),((Ellipse2D.Double)cacheShape).getY()+((Ellipse2D.Double)cacheShape).getHeight(),MapPane.this);
							//Center of the current shape
							double cx=eas.getX()+eas.getWidth()/2;
							double cy=eas.getY()+eas.getHeight()/2;
							clickPoint.setLocation(cx,cy);
							aux.setSelectionShape(eas);
						}
					}
				}
			}
			public void mouseMoved(MouseEvent e) {
				//Where the mouse is now
				p2d.setLocation(e.getX(),e.getY());
				getInverseTransform().transform(p2d,p2d);
				getInversePositionTransform().transform(p2d,p2d);
				Shape as=getAuxiliaryPane().getSelectionShape();
				if (as!=null && as instanceof Ellipse2D.Double) {
					Ellipse2D.Double eas=(Ellipse2D.Double)as;
					//Center of the current shape
					double cx=eas.getX()+eas.getWidth()/2;
					double cy=eas.getY()+eas.getHeight()/2;
					//Radius of the current mouse position
					double radius=Math.sqrt(Math.pow(cx-p2d.x,2)+Math.pow(cy-p2d.y,2));
					if (Math.abs(radius-eas.getWidth()/2)<5*getInversePositionTransform().getScaleX()*getInverseTransform().getScaleX()) {
						resizing=true;
						if (p2d.x>=cx && p2d.y>=cy)
							setCursor(getCustomCursor("resizeNE"));
						else if (p2d.x>=cx && p2d.y<cy)
							setCursor(getCustomCursor("resizeSE"));
						else if (p2d.x<cx && p2d.y>=cy)
							setCursor(getCustomCursor("resizeNW"));
						else if (p2d.x<cx && p2d.y<cy)
							setCursor(getCustomCursor("resizeSW"));
						else
							setCursor(getCustomCursor("crosshair"));
						clickPoint.setLocation(cx,cy);
					} else {
						resizing=false;
						setCursor(getCustomCursor("crosshair"));
					}
				} else {
					resizing=false;
					setCursor(getCustomCursor("crosshair"));
				}
			}

			void createShapeAndUpdateFrame() {
				double radius=Math.sqrt(Math.pow(p2d.x-clickPoint.x,2)+Math.pow(p2d.y-clickPoint.y,2));
				SelectionShape as=aux.getSelectionShape();
				if (as!=null && as instanceof Ellipse2D.Double)
					((Ellipse2D.Double)as).setFrame(clickPoint.x-radius,clickPoint.y-radius,2*radius,2*radius);
				else
					aux.setSelectionShape(new CircularSelectionShape(clickPoint.x-radius,clickPoint.y-radius,clickPoint.x+2*radius,clickPoint.y+2*radius,MapPane.this));
				//Show a small window with the radius
				if (viewer.isSelectionPopupEnabled()) {
					double dist=radius/map.getActiveRegionView().getUnitsPerMeter();
					//double dist=map.getActiveRegionView().measureDistance(clickPoint.x-radius,clickPoint.y-radius,clickPoint.x,clickPoint.y);
					if (dist>1000)
						frame.setText("R="+km.format(dist/1000)+"km");
					else
						frame.setText("R="+((int)dist)+"m");
					frame.show();
				}
			}
		};
		return lsnrSelectCircle;
	}
	/**
	 * Repaints the selection shape and redoes the spatial query.
	 */
	void updateSelectionShape() {
		SelectionShape ss=getAuxiliaryPane().getSelectionShape();
		if (ss!=null) {
			selectGeographicObjects(ss,false);
			//If selecting from outside in this viewer, the shape will
			//be deleted if we don't set iamselecting.
			viewer.iamselecting=true;
			repaintAux(ss.getBounds2D());
		}
	}
	/**
	 * Repaints an area of the auxiliary pane. Used in selection with shape methods.
	 */
	void repaintAux(Rectangle2D r) {
		if (r==null)
			getAuxiliaryPane().repaint();
		else
			repaintAux(r.getX(),r.getY(),r.getWidth(),r.getHeight());
	}
	private Rectangle2D.Double repaintRect=new Rectangle2D.Double();
	/**
	 * Repaints an area of the auxiliary pane. Used in selection with shape methods.
	 */
	void repaintAux(double x,double y,double w,double h) {
		repaintRect.setRect(x,y,w,h);
//		repaintRect.setRect(x,y+h,w,-h);
		transformRect(getPositionTransform(),repaintRect);
		transformRect(getTransform(),repaintRect);
		getAuxiliaryPane().repaint((int)repaintRect.x-5,(int)repaintRect.y-5,(int)repaintRect.width+10,(int)repaintRect.height+10);
	}
	/**
	 * Does the actual selection of objects whenever the selection shape changes.
	 */
	private void selectGeographicObjects(SelectionShape selShape,boolean controlDown) {
		if (map==null || map.getActiveRegionView()==null)
			return;
		ILayerView[] lv=(ILayerView[])map.getActiveRegionView().getLayerViews();
		//Preserve the shape
		viewer.iamselecting=true;
		//For each layer
		//Don't sent the events immediately but sent them alltogether. This speeds up drawing
		//because the processing of each request doesnot last long.
		int wait=0;
		for (int i=0;i<lv.length;i++)
			if (lv[i] instanceof IVectorLayerView && lv[i].isVisible() && lv[i].isObjectSelectable())
				wait++;
		for (int i=0;i<lv.length;i++) {
			if (lv[i] instanceof IVectorLayerView && lv[i].isVisible() && lv[i].isObjectSelectable()) {
				wait--;
				if (wait==0) {
					if (controlDown)
						lv[i].addToSelection(selShape,false);
					else
						lv[i].setSelection(selShape,false);
				} else {
					if (controlDown)
						lv[i].addToSelection(selShape,true);
					else
						lv[i].setSelection(selShape,true);
				}
			}
		}
		viewer.iamselecting=false;
	}
	/**
	 * Does the actual selection of objects whenever the selection shape changes.
	 */
	void clearSelection() {
		if (map==null || map.getActiveRegionView()==null)
			return;
		ILayerView[] lv=(ILayerView[])map.getActiveRegionView().getLayerViews();
		//Preserve the shape
		viewer.iamselecting=true;
		//For each layer
		//Don't sent the events immediately but sent them alltogether. This speeds up drawing
		//because the processing of each request doesnot last long.
		int wait=0;
		for (int i=0;i<lv.length;i++)
			if (lv[i] instanceof IVectorLayerView && lv[i].isVisible() && lv[i].isObjectSelectable())
				wait++;
		for (int i=0;i<lv.length;i++) {
			if (lv[i] instanceof IVectorLayerView && lv[i].isVisible() && lv[i].isObjectSelectable()) {
				wait--;
				if (wait==0)
					lv[i].setSelection((ArrayList)null,false);
				else
					lv[i].setSelection((ArrayList)null,true);
			}
		}
		viewer.iamselecting=false;
	}
	/**
	 * Class that implements basic selection shape support.
	 * @author Giorgos Vasiliou
	 * @version 1.0
	 */
	private abstract class SelectionMouseListener extends MouseInputAdapter {
		Point2D.Double clickPoint=new Point2D.Double(Double.MAX_VALUE,Double.MAX_VALUE);
		//Flags the resizing process in contrast to drawing a new shape
		boolean resizing=false;
		boolean cancel=false;
		boolean firstSelection=true;
		boolean leftIsPressed=false;
		//For cancelling selection
		SelectionShape cacheShape;
		TimedPopupWindow frame=new TimedPopupWindow(MapPane.this);
		NumberFormat km=NumberFormat.getNumberInstance();
		{
			km.setMaximumFractionDigits(2);
		}
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount()>1)
				return;
			if (SwingUtilities.isRightMouseButton(e)) {
				cancel=true;
				frame.setVisible(false);
			} else {
				cancel=false;
				leftIsPressed=true;
				cacheShape=getAuxiliaryPane().getSelectionShape();
				if (!resizing) {
					if (cacheShape!=null) {
						firstSelection=false;
						aux.setSelectionShape(null);
						repaintAux(cacheShape.getBounds2D());
					}
					//Convert clickpoint to real
					clickPoint.setLocation(e.getX(),e.getY());
					getInverseTransform().transform(clickPoint,clickPoint);
					getInversePositionTransform().transform(clickPoint,clickPoint);
				}
			}
		}
		/**
		 * Right-double-click clears the selection.
		 */
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e) && e.getClickCount()==2) {
				SelectionShape ss=getAuxiliaryPane().getSelectionShape();
				aux.setSelectionShape(null);
				if (ss!=null)
					repaintAux(ss.getBounds2D());
				viewer.fireSelectionShapeChanged();
				clearSelection();
				firstSelection=true;
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (e.getClickCount()>1)
				return;
			aux.paintSelectionInterior=false;
			if (!SwingUtilities.isRightMouseButton(e)) {
				leftIsPressed=false;
				if (!cancel) {
					SelectionShape selShape=getAuxiliaryPane().getSelectionShape();
					if (selShape==null)
						selShape=new RectangularSelectionShape(clickPoint.x-0.01,clickPoint.y-0.01,clickPoint.x+0.01,clickPoint.y+0.01,MapPane.this);
					selectGeographicObjects(selShape,e.isControlDown());
					selShape.fireShapeGeometryChanged();
					viewer.fireSelectionShapeChanged();
					repaintAux(selShape.getBounds2D());
					cacheShape=null;
					firstSelection=false;
				}
			} else if (SwingUtilities.isRightMouseButton(e) && leftIsPressed) {
				Shape selShape=getAuxiliaryPane().getSelectionShape();
				//The repaint occurs later on the repaint thread
				if (selShape!=null)
					repaintAux(selShape.getBounds2D());
				if (firstSelection || cacheShape==null) {
					aux.setSelectionShape(null);
				} else if (cacheShape!=null) {
					aux.setSelectionShape(cacheShape);
					repaintAux(cacheShape.getBounds2D());
					cacheShape=null;
				}
			} else
				cancel=false;
		}
		public void mouseDragged(MouseEvent e) {
			//Where the mouse is now
			p2d.setLocation(e.getX(),e.getY());
			getInverseTransform().transform(p2d,p2d);
			getInversePositionTransform().transform(p2d,p2d);
			Shape as=getAuxiliaryPane().getSelectionShape();
			//Cache the old selection bounds for repaint sake
			Rectangle2D oldr=null;
			if (as!=null)
				oldr=as.getBounds2D();
			createShapeAndUpdateFrame();
			//Repaint the area of the intersection of the previous and the current shape
			aux.paintSelectionInterior=true;
			Rectangle2D bounds=getAuxiliaryPane().getSelectionShape().getBounds2D();
			if (oldr==null)
				repaintAux(bounds.getX(),bounds.getY(),bounds.getWidth(),bounds.getHeight());
			else {
				double x=Math.min(oldr.getX(),bounds.getX());
				double y=Math.min(oldr.getY(),bounds.getY());
				double x2=Math.max(oldr.getX()+oldr.getWidth(),bounds.getX()+bounds.getWidth());
				double y2=Math.max(oldr.getY()+oldr.getHeight(),bounds.getY()+bounds.getHeight());
				repaintAux(x,y,x2-x,y2-y);
			}
		}

		abstract void createShapeAndUpdateFrame();
	}

	protected MouseInputListener getBrowseMouseListener() {
		if (lsnrBrowse==null)
		lsnrBrowse=new MouseInputListener() {
			private MouseInputListener activate;
			private MouseInputListener pan;
			java.awt.Point mousePressedPoint=null; // GT 2
			boolean drag=false;
			{
				activate=getActivateMouseListener();
				pan=getPanMouseListener(false);
			}
			public void mousePressed(MouseEvent e) {
				mousePressedPoint=e.getPoint();
				activate.mousePressed(e);
				pan.mousePressed(e);
			}
			public void mouseReleased(MouseEvent e) {
				mousePressedPoint=null;
				pan.mouseReleased(e);
				if (!drag)
					activate.mouseReleased(e);
				//This trick places back the correct cursor, which is decided
				//on the first mouse move!
				mouseMoved(e);
				drag=false;
			}
			public void mouseClicked(MouseEvent e) {
				pan.mouseClicked(e);
				activate.mouseClicked(e);
			}
			public void mouseEntered(MouseEvent e) {
				activate.mouseEntered(e);
				pan.mouseEntered(e);
			}
			public void mouseExited(MouseEvent e) {
				activate.mouseExited(e);
				pan.mouseExited(e);
			}
			public void mouseMoved(MouseEvent e) {
				pan.mouseMoved(e);
				activate.mouseMoved(e);
			}
			public void mouseDragged(MouseEvent e) {
				// Adds some tolerance to the mousePressed event, before it is
				// considered a mouseDragged.
				if (mousePressedPoint!=null) {
					if (mousePressedPoint.distance(e.getPoint())<5)
						return;
				}
				drag=true;
				activate.mouseDragged(e);
				pan.mouseDragged(e);
			}
		};
		return lsnrBrowse;
	}

	/**
	 * Mouse Listener for pan tool.
	 */
	protected MouseInputListener getPanMouseListener() {
		if (lsnrPan==null)
		lsnrPan=getPanMouseListener(true);
		return lsnrPan;
	}

	/**
	 * Mouse Listener for pan tool. The changeCursor flag tells whether the listener will change the cursor.
	 * Used to provide the unified listener for pan and browse.
	 */
	public MouseInputListener getPanMouseListener(final boolean changeCursor) {
		return new MouseInputAdapter() {
			private java.awt.Point press,cr;
			private boolean validStart,dragged;
			private StringBuffer cs=new StringBuffer();
			//Used by the unified BrowseMouseListener. False by it to prevent cursor flashing by double changes.
			public void mouseMoved(MouseEvent e) {
				if (layers==null)
					return;
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				if (changeCursor)
					setCursor(getCustomCursor("pancursorhand"));
			}
			public void mousePressed(MouseEvent e) {
				validStart=false;
				dragged=false;
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				//Left mouse button
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==1) {
					validStart=true;
					press=new java.awt.Point(e.getX(),e.getY());
					cr=new java.awt.Point();
					SwingUtilities.convertPointToScreen(press,(Component) e.getSource());
					//Take care of the cursor when pan is initialized
					if (changeCursor)
						setCursor(getCustomCursor(cursorName()));
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (!validStart || ! dragged)
					return;
				layers.viewSizeChanged(true);
				if (changeCursor)
					setCursor(getCustomCursor("pancursorhand"));
			}
			public void mouseDragged(MouseEvent e) {
				//A mouse event must be inside the clip area.
				if (!validStart) {// || !layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}
				//Left mouse button
				if (SwingUtilities.isLeftMouseButton(e)) {
					dragged=true;
					cr.x=e.getX();
					cr.y=e.getY();
					SwingUtilities.convertPointToScreen(cr,(Component) e.getSource());
					addOffsetX(cr.x-press.x);
					addOffsetY(cr.y-press.y);
					press.x=cr.x;
					press.y=cr.y;
					transform=null;
					layers.viewSizeChanged(false);
					layers.repaint();
					//Take care of the cursor
					setCursor(getCustomCursor(cursorName()));
				}
			}
			/**
			 * Returns the pan-cursor name suitable for current panning position.
			 */
			String cursorName() {
				cs.setLength(0);
				cs.append("pancursor");
				double lw=layers.getOriginalWidth()*zoom-getWidth();
				double lh=layers.getOriginalHeight()*zoom-getHeight();
				if (lw<=0 && lh<=0) {
					return "pancursorhand";
				} else {
					if (lh<=0)
						cs=cs.append("NS");
					else {
						if (Math.abs(offsetY)==(int) (lh/2)){
							if (offsetY>0)
								cs=cs.append("S");
							else if (offsetY<0)
								cs=cs.append("N");
						}
						/*if (offsetY==Math.round((layers.getOriginalHeight()*zoom-getHeight())/2))
							cs=cs.append("S");
						else if (offsetY==Math.round((getHeight()-layers.getOriginalHeight()*zoom)/2))
							cs=cs.append("N");*/
					}
					if (lw<=0)
						cs=cs.append("EW");
					else {
						if (offsetX==Math.round(lw/2))
							cs=cs.append("E");
						else if (offsetX==Math.round(-lw/2))
							cs=cs.append("W");
					}
				}
				return cs.toString();
			}
		};
	}
	/**
	 * Mouse Listener for insert-object tool.
	 */
	protected MouseInputListener getInsertObjectMouseListener() {
		if (lsnrInsObj==null)
		lsnrInsObj=new MouseInputAdapter() {
			//Previous: The one used for "always insert". Normal: The one used for the next insertion.
			private IPointLayerView previousInsertLayer,insLayer;
			private NewRestorableImageIcon previousIcon,normalIcon;

			private boolean usePrevious=false,drag=false;
			private IVectorGeographicObject object;
			private Point copy;
			private IPointLayerView layer;
			private int pressX,pressY,previousX,previousY;
			private int xTol,yTol,xExt,yExt;
			private ImageIcon trashClosed=viewer.loadImageIcon("images/trashcanclosed.gif");
			private ImageIcon trashOpen=viewer.loadImageIcon("images/trashcanopen.gif");
			private JLabel trashcan=new JLabel(trashClosed);
			public void mouseEntered(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				setCursor(getCustomCursor("pincursor"));
				//If drag, this means that the mouse exited while dragging, so we must readd the trashcan.
				if (drag) {
					layers.add(trashcan);
					//getAuxiliaryPane().add(trashcan);
					trashcan.repaint();
				}
			}
			public void mouseExited(MouseEvent e) {
				setCursor(getCustomCursor("default"));
				clearGlass();
			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					object=null;
					layer=null;
					setCursor(getCustomCursor("default"));
					return;
				}*/
				object=null;
				layer=null;
				int pr=getAuxiliaryPane().countShapes();
				getAuxiliaryPane().removeAllShapes();
				ObjectBaseArray o=scanAround(e.getX(),e.getY());
				clearGlass(); //Remove all shapes because more than one layers are highlighted. The object will be added later.
				//Find the first editable layer
				for (int i=1;i<o.size();i=i+2)
					if (((ILayerView) o.get(i)).isEditable()) {
						object=(IVectorGeographicObject) o.get(i-1);
						layer=(IPointLayerView) o.get(i);
						break;
					}
				//If it is not an editable object remove it from the glasspane where it was put by scanAround.
				if (object==null) {
					setCursor(getCustomCursor("pincursor"));
					object=null;
					layer=null;
				} else {
					getAuxiliaryPane().addShape(new AuxiliaryShape(layer,object));

					showTrashcan();
					//Record the position of the object
					copy=new gr.cti.eslate.mapModel.geom.Point.Double(((Point.Double) object).getX(),((Point.Double) object).getY(),0);
					setCursor(getCustomCursor("pincursormove"));
					getAuxiliaryPane().repaint();
				}
			}
			public void mousePressed(MouseEvent e) {
				//Avoid multiple clicks
				if (e.getClickCount()>1)
					return;
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				//Handle right button while dragging. Cancel dragging and return.
				if (SwingUtilities.isRightMouseButton(e)) {
					if (drag) {
						((Point) object).setXY(copy.getX(),copy.getY());
						object=null;
						layer=null;
						drag=false;
					} else if (object==null) {
						if (showDialog(null,null).getReturnValue()==DialogObject.OK_PRESSED)
							insertNew(e.getX(),e.getY());
					} else if (object!=null) {
						setBusy(true);
						String id=null;
						layer.setActiveGeographicObject(object);
						if (showDialog(object,layer).getReturnValue()==DialogObject.OK_PRESSED) {
							if (insLayer!=layer) {
								//First remove the object from the old layer.
								try {
									layer.removeObject(object);
									//Then add it to the new one.
									object.setID(insLayer.getObjectCount());
									//Handle its properties
									if (insLayer.getIconBase()!=null && insLayer.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS && insLayer.getIconBase().isHidden()) {
										id=""+System.currentTimeMillis();
										if (normalIcon!=null) {
											((PointLayer) insLayer.getLayer()).addNormalIcon(id,new NewRestorableImageIcon(normalIcon.getImage()));
											((PointLayer) insLayer.getLayer()).addSelectedIcon(id,createSelectedIcon(normalIcon,insLayer));
											((PointLayer) insLayer.getLayer()).addHighlightedIcon(id,createHighlightedIcon(normalIcon,insLayer));
										}
										//Add it to the layer
										insLayer.addObject(object.getBoundingMinX(),object.getBoundingMinY(),object.getBoundingMaxX(),object.getBoundingMaxY(),object,insLayer.getIconBase().getName(),id);
									} else
										insLayer.addObject(object.getBoundingMinX(),object.getBoundingMinY(),object.getBoundingMaxX(),object.getBoundingMaxY(),object);
									layer=insLayer;
								} catch(Throwable t) {
									JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,MapPane.this),MapViewer.messagesBundle.getString("cannoteditobject"),"",JOptionPane.ERROR_MESSAGE);
									t.printStackTrace();
								}
							} else {
								//Put the new icon
								if (insLayer.getIconBase()!=null) {
									try {
										if (insLayer.getIconBase().getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
											CImageIcon cic=new CImageIcon();
											cic.setImage(normalIcon.getImage());
											insLayer.setField(object,insLayer.getIconBase().getName(),cic);
										} else {
											id=(String) insLayer.getField(object,insLayer.getIconBase().getName());
											if (id==null || id.equals("")) {
												id=""+System.currentTimeMillis();
												insLayer.setField(object,insLayer.getIconBase().getName(),id);
											}
											if (normalIcon!=null) {
												((PointLayer) layer.getLayer()).addNormalIcon(id,new NewRestorableImageIcon(normalIcon.getImage()));
												((PointLayer) layer.getLayer()).addSelectedIcon(id,createSelectedIcon(normalIcon,insLayer));
												((PointLayer) layer.getLayer()).addHighlightedIcon(id,createHighlightedIcon(normalIcon,insLayer));
												layer.fireLayerGeographicObjectPropertiesChanged(object);
											}
										}
									} catch(Exception ex) {
										System.err.println("MAPVIEWER#200003031745: Cannot get the old icon key for the object!");
									}
								}
							}
							insLayer=previousInsertLayer;
							normalIcon=previousIcon;
						}
						setBusy(false);
					}
				} else { /* Left mouse button*/
					if (object==null) {
						setBusy(true);
						int retVal=DialogObject.OK_PRESSED;
						if ((previousInsertLayer==null) || (!usePrevious) || !(regionContainsLayer()))
							retVal=showDialog(null,null).getReturnValue();
						if (retVal==DialogObject.OK_PRESSED)
							insertNew(e.getX(),e.getY());
						setBusy(false);
					} else {
						layer.setActiveGeographicObject(object);
						showTrashcan();
						//Drag starts. Get the press point and find the paramenters optimum repaint rectangle.
						Point2D p=new Point2D.Double(e.getX(),e.getY());
						getInverseTransform().transform(p,p);
						pressX=(int)Math.round(p.getX());
						pressY=(int)Math.round(p.getY());
						previousX=e.getX();
						previousY=e.getY();
						getAuxiliaryPane().removeAllShapes();
						getAuxiliaryPane().repaint();
						//Find the tight rectangle for an optimum repaint
						if ((layer.getPaintMode()==IPointLayerView.PAINT_AS_CIRCLE) || (layer.getHighlightedIcon((Point) object)==null)) {
							xTol=Math.max(layer.getCircleRadius()/2+5,layers.activeCircle);
							yTol=Math.max(xTol,layers.getFontMetrics(layers.getFont()).getHeight()/2)+5;
						} else {
							xTol=Math.max(layer.getHighlightedIcon((Point) object).getIconWidth()/2+5,layers.activeCircle); //5 is the border automaticaly created for user objects
							yTol=Math.max(layer.getHighlightedIcon((Point) object).getIconHeight()/2+5,layers.activeCircle); //5 is the border automaticaly created for user objects
						}
						if (layer.getTip(object)!=null) {
							xExt=2*xTol+layers.getFontMetrics(layers.getFont()).stringWidth(layer.getTip(object))+5;
							yExt=2*yTol+layers.getFontMetrics(layers.getFont()).getHeight()/2+5;
						} else {
							xExt=2*xTol+5;
							yExt=2*yTol+5;
						}
					}
				}
			}
			private void insertNew(int clickX,int clickY) {
				setBusy(true);
				//Insert the object
				String id=null;
				try {
					p2d.setLocation(clickX,clickY);
					getInverseTransform().transform(p2d,p2d);
					getInversePositionTransform().transform(p2d,p2d);
					//Create the point
					Point p;
					if (map.getDataPrecision()==IMapView.SINGLE_PRECISION)
						p=new Point.Float(p2d.x,p2d.y,insLayer.getObjectCount());
					else
						p=new Point.Double(p2d.x,p2d.y,insLayer.getObjectCount());
					Point2D.Double bp=new Point2D.Double(p.getX(),p.getY());
					//Handle its properties
					if (insLayer.getIconBase()!=null && insLayer.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS && insLayer.getIconBase().isHidden()) {
						id=""+System.currentTimeMillis();
						if (normalIcon!=null) {
							((PointLayer) insLayer.getLayer()).addNormalIcon(id,new NewRestorableImageIcon(normalIcon.getImage()));
							((PointLayer) insLayer.getLayer()).addSelectedIcon(id,createSelectedIcon(normalIcon,insLayer));
							((PointLayer) insLayer.getLayer()).addHighlightedIcon(id,createHighlightedIcon(normalIcon,insLayer));
						}
						if (!insLayer.isVisible())
							insLayer.setVisible(true);
					}
					//Add it to the layer
					if (insLayer.getIconBase()!=null)
						insLayer.addObject(bp.x,bp.y,bp.x,bp.y,p,insLayer.getIconBase().getName(),id);
					else
						insLayer.addObject(bp.x,bp.y,bp.x,bp.y,p);
				} catch(Throwable thr) {
					thr.printStackTrace();
				}
				setBusy(false);
			}
			private DialogObject showDialog(GeographicObject go,IPointLayerView goLayer) {
				//This actually updates the previousInsertLayer as a side-effect.
				regionContainsLayer();
				//If the icon base field is visible, the layer is not a user layer. It has
				//Show a dialog when the right button is pressed or no default has been defined.
				DialogObject ld;
				if (go==null)
					ld=new DialogObject((Frame) SwingUtilities.getAncestorOfClass(Frame.class,MapPane.this),map,map.getActiveRegionView().getLayerViews(),previousInsertLayer,previousIcon,true);
				else
					ld=new DialogObject((Frame) SwingUtilities.getAncestorOfClass(Frame.class,MapPane.this),map,map.getActiveRegionView().getLayerViews(),goLayer,(NewRestorableImageIcon) goLayer.getNormalIcon((Point) go),false);
				//Position in the center of the viewer.
				java.awt.Point pp=new java.awt.Point(getX(),getY());
				SwingUtilities.convertPointToScreen(pp,MapPane.this);
				ld.setLocation(pp.x+(getWidth()-ld.getWidth())/2,pp.y+(getHeight()-ld.getHeight())/2);
				ld.setVisible(true);
				//If cancel was pressed return with no further actions
				if (ld.getReturnValue()==DialogObject.CANCEL_PRESSED)
					return ld;

				//If editing an object, don't update the "previous" caches.
				if (go==null) {
					previousInsertLayer=(IPointLayerView) ld.getInsertLayer();
					previousIcon=(NewRestorableImageIcon) ld.getIcon();
					if (ld.isDefaultDefined())
						usePrevious=true;
					else
						usePrevious=false;
				}
				insLayer=(IPointLayerView) ld.getInsertLayer();
				if (ld.getIcon()!=null)
					normalIcon=new NewRestorableImageIcon(((NewRestorableImageIcon) ld.getIcon()).getImage());
				else
					normalIcon=null;
				return ld;
			}

			private NewRestorableImageIcon createSelectedIcon(NewRestorableImageIcon normal,IPointLayerView insLayer) {
				//Create the selected icon.
				BufferedImage b=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(normal.getIconWidth()+4,normal.getIconHeight()+4,Transparency.TRANSLUCENT);
				Graphics2D g2=(Graphics2D) b.getGraphics();
				g2.setStroke(new BasicStroke(2));
				g2.setPaint(insLayer.getSelectedOutlineColor());
				g2.drawRect(0,0,normal.getIconWidth()+4,normal.getIconHeight()+4);
				g2.drawImage(((NewRestorableImageIcon) normal).getImage(),AffineTransform.getTranslateInstance(2,2),null);
				return new NewRestorableImageIcon(b);
			}

			private NewRestorableImageIcon createHighlightedIcon(NewRestorableImageIcon normal,IPointLayerView insLayer) {
				//Create the selected icon.
				BufferedImage b=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(normal.getIconWidth()+4,normal.getIconHeight()+4,Transparency.TRANSLUCENT);
				Graphics2D g2=(Graphics2D) b.getGraphics();
				g2.setStroke(new BasicStroke(2));
				g2.setPaint(insLayer.getHighlightedOutlineColor());
				g2.drawRect(0,0,normal.getIconWidth()+4,normal.getIconHeight()+4);
				g2.drawImage(((NewRestorableImageIcon) normal).getImage(),AffineTransform.getTranslateInstance(2,2),null);
				return new NewRestorableImageIcon(b);
			}
			/**
			 * Also changes the previousInsertLayer as a side-effect.
			 */
			private boolean regionContainsLayer() {
				//Check whether the region contains the previous layerview. Checks with == and not with
				//equals or contains because we want to specify again the layer to insert when we zoom in.
				boolean contains=false;
				for (int i=0;i<map.getActiveRegionView().getLayerViews().length;i++) {
					if (map.getActiveRegionView().getLayerViews()[i]==previousInsertLayer) {
						contains=true;
						break;
					} else if (map.getActiveRegionView().getLayerViews()[i].equals(previousInsertLayer))
						previousInsertLayer=(IPointLayerView) map.getActiveRegionView().getLayerViews()[i];
				}
				return contains;
			}

			private void showTrashcan() {
				//Show the trashcan. Find its location.
				trashcan.setIcon(trashClosed);
				int pox=(int) Math.max(0,getTransform().getTranslateX());
				int poy=(int) Math.max(0,getTransform().getTranslateY());
				trashcan.setBounds(pox,poy,trashcan.getPreferredSize().width,trashcan.getPreferredSize().height);
				layers.add(trashcan);
				trashcan.repaint();
			}

			private void clearGlass() {
				layers.remove(trashcan);
				getAuxiliaryPane().removeAllShapes();
				getAuxiliaryPane().repaint();
			}

			public void mouseDragged(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				//Left mouse button
				if ((SwingUtilities.isLeftMouseButton(e)) && (object!=null)) {
					setCursor(getCustomCursor("pincursormove"));
					//Show the proper trashcan icon
					java.awt.Point pos=SwingUtilities.convertPoint(MapPane.this,e.getX(),e.getY(),trashcan);
					ImageIcon prev=(ImageIcon) trashcan.getIcon();
					if (trashcan.contains(pos.x,pos.y))
						trashcan.setIcon(trashOpen);
					else
						trashcan.setIcon(trashClosed);
					if (!prev.equals(trashcan.getIcon())) {
						trashcan.repaint();
						getAuxiliaryPane().repaint(trashcan.getX(),trashcan.getY(),trashcan.getWidth(),trashcan.getHeight());
					}
					drag=true;
					Point p=(Point) ((Point) (new Point.Double(e.getX(),e.getY()).createTransformedShape(getInverseTransform()))).createTransformedShape(getInversePositionTransform());
					getAuxiliaryPane().removeAllShapes();
					((Point) object).setXY(p.getX(),p.getY());
					getAuxiliaryPane().addShape(new AuxiliaryShape(layer,object));
					getAuxiliaryPane().repaint();//Math.min(previousX,e.getX())-xTol,Math.min(previousY,e.getY())-yTol,Math.abs(previousX-e.getX())+xExt,Math.abs(previousY-e.getY())+yExt);
					previousX=e.getX();
					previousY=e.getY();
				}
			}
			public void mouseReleased(MouseEvent e) {
				//Left mouse button
				if ((SwingUtilities.isLeftMouseButton(e)) && (object!=null) && (drag)) {
					setBusy(true);
					//Give the object its correct coordinates again
					double tempX,tempY;
					tempX=copy.getX();
					tempY=copy.getY();
					copy.setXY(((Point) object).getX(),((Point) object).getY());
					((Point) object).setXY(tempX,tempY);
					//Check whether the mouse is over the trashcan
					java.awt.Point pos=SwingUtilities.convertPoint(MapPane.this,e.getX(),e.getY(),trashcan);
					ImageIcon prev=(ImageIcon) trashcan.getIcon();
					if (trashcan.contains(pos.x,pos.y))
						//Delete the object
						try {
							layer.removeObject(object);
							layer.setActiveGeographicObject(null);
						} catch(gr.cti.eslate.protocol.CannotRemoveObjectException ex) {
							JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,MapPane.this),MapViewer.messagesBundle.getString("cannotremoveobject"),"",JOptionPane.ERROR_MESSAGE);
						}
					else
						//Reposition the object
						layer.repositionObject(object,((Point) copy).getX()-((Point) object).getX(),((Point) copy).getY()-((Point) object).getY());
					drag=false;
					setBusy(false);
				}
				setCursor(getCustomCursor("pincursor"));
				getAuxiliaryPane().removeAllShapes();
				getAuxiliaryPane().repaint();
			}
		};
		return lsnrInsObj;
	}
	/**
	 * Mouse Listener for rotate tool.
	 */
	protected MouseInputListener getRotateMouseListener() {
		if (lsnrRotate==null)
		lsnrRotate=new MouseInputAdapter() {
			//In screen coordinates to avoid "jumping" on dragging
			private int pressX;
			private NumberFormat nf;
			{
				nf=NumberFormat.getInstance();
				nf.setMaximumFractionDigits(1);
			}
			public void mousePressed(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				java.awt.Point p=new java.awt.Point(e.getX(),e.getY());
				SwingUtilities.convertPointToScreen(p,(Component) e.getSource());
				pressX=p.x;
				viewer.getStatusBar().setMessage(MapViewer.messagesBundle.getString("rotation")+" "+((nf.format(rotation*180/Math.PI).equals("360"))?"0":nf.format(rotation*180/Math.PI))+" "+MapViewer.messagesBundle.getString("degrees"),0);
			}
			public void mouseReleased(MouseEvent e) {
				viewer.getStatusBar().clearMessage();
				repaint();
				setCursor(getCustomCursor("rotatecursor"));
			}
			public void mouseDragged(MouseEvent e) {
				//Left mouse button
				if (SwingUtilities.isLeftMouseButton(e)) {
					java.awt.Point p=new java.awt.Point(e.getX(),e.getY());
					SwingUtilities.convertPointToScreen(p,(Component) e.getSource());
					double oldrot=rotation;
					//The rotation speed depends on the mouse motion speed
					rotation+=(p.x-pressX)*Math.PI/620;
					//Bound the rotation angle in [0,2�)
					rotation=rotation-((int) (rotation/(2*Math.PI))*2*Math.PI);
					if (rotation<0)
						rotation+=2*Math.PI;
					if (rotation==2*Math.PI)
						rotation=0;

					if (p.x>pressX)
						setCursor(getCustomCursor("rotaterightcursor"));
					else if (p.x<pressX)
						setCursor(getCustomCursor("rotateleftcursor"));
					double angle=Math.abs((rotation%(2*Math.PI))*180/Math.PI);
					//Snap to the orthogonal positions
					if (angle<=0.3d || angle>=359.7d) {
						setCursor(getCustomCursor("rotatecenteredcursor"));
						rotation=0;
					} else if (angle>=89.7d && angle<=90.3d) {
						setCursor(getCustomCursor("rotatecenteredcursor"));
						rotation=Math.PI/2;
					} else if (angle>=179.7d && angle<=180.3d) {
						setCursor(getCustomCursor("rotatecenteredcursor"));
						rotation=Math.PI;
					} else if (angle>=269.7d && angle<=270.3d) {
						setCursor(getCustomCursor("rotatecenteredcursor"));
						rotation=3*Math.PI/2;
					}
					rebuildTransformation();
					rebuildPositionTransformation();
					//pressX in this listener represents the previous value
					pressX=p.x;
					viewer.getStatusBar().setMessage(MapViewer.messagesBundle.getString("rotation")+" "+((nf.format(rotation*180/Math.PI).equals("360"))?"0":nf.format(rotation*180/Math.PI))+" "+MapViewer.messagesBundle.getString("degrees"),0);

					layers.viewRotationChanged();

					invalidate();
					repaint();
				}
			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				setCursor(getCustomCursor("rotatecursor"));
			}
		};
		return lsnrRotate;
	}
	/**
	 * Mouse Listener for go-inside tool.
	 */
	protected MouseInputListener getGoInMouseListener() {
		if (lsnrGoIn==null)
		lsnrGoIn=new MouseInputAdapter() {
			AuxiliaryShape found;
			public void mousePressed(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				if (zoomRects==null || e.getClickCount()>1) return;
				if (SwingUtilities.isLeftMouseButton(e)) {
					setBusy(true);
					getForegroundComponent().removeMouseInputListener(this);
					if (found!=null) {
						found.outline=zoomGoOutline;
						found.fill=zoomGoFill;
						java.awt.Rectangle r=found.shape.getBounds();
						r.x-=2; r.y-=2; r.width+=4; r.height+=4;
						if (aux!=null)
							aux.repaint(transformRect(getTransform(),r));
						setRedrawEnabled(false);
						viewer.blockRedraw=true;
						viewer.goinActionPerformed=true;
						map.setActiveRegionView((IZoomRect) found.shape);
						viewer.blockRedraw=false;
						//Place zoom factor to 100%
						if (viewer.zoom!=null)
							viewer.zoom.setValue(100);
						else
							setZoom(1);
						layers.viewZoomChanged(true);
						setRedrawEnabled(true);
						found=null;
					}
					setBusy(false);
					getForegroundComponent().addMouseInputListener(this);
					if (!viewer.goin.isEnabled())
						setCursor(getCustomCursor("default"));
				}
			}
			public void mouseReleased(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				setCursor(getCustomCursor("zoomcursor"));
			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				AuxiliaryShape previousFound=found;
				found=null;
				if (zoomRects==null) return;
				Point2D.Double p=(Point2D.Double) getInverseTransform().transform(new Point2D.Double(e.getX(),e.getY()),null);
				//If more than one rectangles overlap, the one that its center
				//is nearest to the mouse is selected.
				double distance=Double.MAX_VALUE;
				double thisdist;
				for (int i=0;i<zoomRects.length;i++)
					if (zoomRects[i].shape.contains(p)) {
						Rectangle2D bd=zoomRects[i].shape.getBounds2D();
						thisdist=Math.pow(bd.getX()+bd.getWidth()/2-p.getX(),2)+Math.pow(bd.getY()+bd.getHeight()/2-p.getY(),2);
						if (thisdist<distance) {
							found=zoomRects[i];
							distance=thisdist;
						}
					}
				if (previousFound!=found) {
					//Turn off the highlight
					if (previousFound!=found) {
						if (previousFound!=null) {
							previousFound.outline=zoomOutline;
							previousFound.fill=zoomFill;
							java.awt.Rectangle r=previousFound.shape.getBounds();
							r.x-=2; r.y-=2; r.width+=4; r.height+=4;
							if (aux!=null)
								aux.repaint(transformRect(getTransform(),r));
						}
					}

					if (found!=null) {
						viewer.getStatusBar().setMessage(((IZoomRect) found.shape).getName(),0);
						setCursor(getCustomCursor("zoomincursor"));
						//Bring the rectangle to front
						getAuxiliaryPane().removeShape(found);
						getAuxiliaryPane().addShape(found);
						//Highlight the rectangle
						found.outline=zoomHighOutline;
						found.fill=zoomHighFill;
						java.awt.Rectangle r=found.shape.getBounds();
						r.x-=2; r.y-=2; r.width+=4; r.height+=4;
						aux.repaint(transformRect(getTransform(),r));
					} else {
						viewer.getStatusBar().setMessage(MapViewer.toolbarTipBundle.getString("goInhelp"),0);
						setCursor(getCustomCursor("zoomcursor"));
					}
				}
			}
			public void mouseExited(MouseEvent e) {
				//Turn off the highlight
				if (found!=null) {
					found.outline=zoomOutline;
					found.fill=zoomFill;
					java.awt.Rectangle r=found.shape.getBounds();
					r.x-=2; r.y-=2; r.width+=4; r.height+=4;
					if (aux!=null)
						aux.repaint(transformRect(getTransform(),r));
					found=null;
				}
			}
		};
		return lsnrGoIn;
	}
	/**
	 * Mouse Listener for go-outside tool.
	 */
	protected MouseInputListener getGoOutMouseListener() {
		if (lsnrGoOut==null)
		lsnrGoOut=new MouseInputAdapter() {
			public void mousePressed(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				if (map==null || (map!=null && map.getOuterRegion(map.getActiveRegionView())==null))
					return;
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==1) {
					setBusy(true);
					Point2D.Double center=new Point2D.Double(layers.currentView.x+layers.currentView.width/2,layers.currentView.y+layers.currentView.height/2);
					getInversePositionTransform().transform(center,center);
					setRedrawEnabled(false);
					viewer.blockRedraw=true;
					map.setActiveRegionView(map.getOuterRegion(map.getActiveRegionView()));
					viewer.blockRedraw=false;
					//Place zoom factor to 100%
					if (viewer.zoom!=null)
						viewer.zoom.setValue(100);
					else
						setZoom(1);
					//layers.viewZoomChanged(true);
					//Try to center the view as near to the previous center as possible.
					Point2D.Double newCenter=new Point2D.Double(layers.currentView.x+layers.currentView.width/2,layers.currentView.y+layers.currentView.height/2);
					getPositionTransform().transform(center,center);
					addOffsetX((int) (newCenter.x-center.x));
					addOffsetY((int) (newCenter.y-center.y));
					layers.viewSizeChanged(false);
					needsRedrawing=true;

					setRedrawEnabled(true);
					setBusy(false);
					if (!viewer.goout.isEnabled())
						setCursor(getCustomCursor("default"));
				}
			}
			public void mouseReleased(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				setCursor(getCustomCursor("zoomoutcursor"));

			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				setCursor(getCustomCursor("zoomoutcursor"));
			}
		};
		return lsnrGoOut;
	}
	/**
	 * Mouse Listener for meter tool.
	 */
	protected MouseInputListener getMeterMouseListener() {
		if (lsnrMeter==null)
		lsnrMeter=new MouseInputAdapter() {
			private double pressX,pressY;
			private Point2D.Double pp;
			private StringBuffer string=new StringBuffer();
			protected double lastDistance=0;
			private double dis;
			private java.awt.Point pt=new java.awt.Point();
			private TimedPopupWindow frame=new TimedPopupWindow(MapPane.this);
			public void mousePressed(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					pp=null;
					setCursor(getCustomCursor("default"));
					return;
				}*/
				//Position the display window
				pt.setLocation(e.getX(),e.getY()+5);
				SwingUtilities.convertPointToScreen(pt,MapPane.this);
				Dimension ss=Toolkit.getDefaultToolkit().getScreenSize();
				if (ss.width<pt.x+250)
					pt.x-=pt.x+250-ss.width;
				if (ss.height<pt.y+20)
					pt.y-=pt.y+20-ss.height;
				frame.setLocation(pt.x,pt.y);
				dis=0;
				setCursor(getCustomCursor("crosshair"));
				pp=new Point2D.Double(e.getX(),e.getY());
				getInverseTransform().transform(pp,pp);
				pressX=pp.x;
				pressY=pp.y;
				getInversePositionTransform().transform(pp,pp);
				getAuxiliaryPane().removeAllShapes();
			}
			public void mouseReleased(MouseEvent e) {
				if (meterLine1!=null)
					getAuxiliaryPane().removeShape(meterLine1);
				if (meterLine2!=null)
					getAuxiliaryPane().removeShape(meterLine2);
				java.awt.Rectangle re=meterLine1.shape.getBounds();
				if (re!=null) {
					transformRect(getTransform(),re);
					getAuxiliaryPane().repaint(re.x-3,re.y-3,re.width+7,re.height+7);
				}
				meterLine1=meterLine2=null;
				lastDistance=dis;
				getAuxiliaryPane().removeAllShapes();
			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				dis=0;
				setCursor(getCustomCursor("crosshair"));
				if (getAuxiliaryPane().countShapes()>0) {
					ArrayList s=(ArrayList)getAuxiliaryPane().getShapes().clone();
					getAuxiliaryPane().removeAllShapes();
					for (int i=0;i<s.size();i++) {
						Object obj=s.get(i);
						if (obj instanceof AuxiliaryShape)
							repaintAux(((IVectorGeographicObject)((AuxiliaryShape)obj).shape).getBounds2D());
					}
				}
				ObjectBaseArray o=scanAroundForAllInLayer(e.getX(),e.getY());
				produceMultiLineToolTip(o);
			}
			public void mouseDragged(MouseEvent e) {
				//The dragging started from an invalid mouse press.
				if (pp==null)
					return;
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				//Left mouse button
				if (SwingUtilities.isLeftMouseButton(e)) {
					setCursor(getCustomCursor("crosshair"));
					if (getAuxiliaryPane().countShapes()>0) {
						ArrayList s=(ArrayList)getAuxiliaryPane().getShapes().clone();
						getAuxiliaryPane().removeAllShapes();
						for (int i=0;i<s.size();i++) {
							Object obj=s.get(i);
							if (obj instanceof AuxiliaryShape)
								repaintAux(((Shape)((AuxiliaryShape)obj).shape).getBounds2D());
						}
					}
					ObjectBaseArray o=scanAroundForAllInLayer(e.getX(),e.getY());
					produceMultiLineToolTip(o);
					if (meterLine1==null) {
						meterLine1=new AuxiliaryShape(new Line2D.Double(),new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {3,3},0),new Color(255,255,255,160),null,false);
						meterLine2=new AuxiliaryShape(new Line2D.Double(),new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {3,3},3),new Color(32,32,32,228),null,false);
					}
					p2d.setLocation(e.getX(),e.getY());
					getInverseTransform().transform(p2d,p2d);

					//Repaint old territory
					java.awt.Rectangle re=meterLine1.shape.getBounds();
					transformRect(getTransform(),re);
					getAuxiliaryPane().repaint(re.x-3,re.y-3,re.width+7,re.height+7);

					((Line2D.Double) meterLine1.shape).x1=((Line2D.Double) meterLine2.shape).x1=pressX;
					((Line2D.Double) meterLine1.shape).y1=((Line2D.Double) meterLine2.shape).y1=pressY;
					((Line2D.Double) meterLine1.shape).x2=((Line2D.Double) meterLine2.shape).x2=p2d.getX();
					((Line2D.Double) meterLine1.shape).y2=((Line2D.Double) meterLine2.shape).y2=p2d.getY();
					getAuxiliaryPane().addShape(meterLine1);
					getAuxiliaryPane().addShape(meterLine2);
					//Print the distance
					getInversePositionTransform().transform(p2d,p2d);
					dis=map.getActiveRegionView().measureDistance(pp.x,pp.y,p2d.x,p2d.y);
					//Control pressed, measure sequence of draggings
					if (e.isControlDown())
						dis+=lastDistance;
					if (dis<1000) {
						NumberFormat nf1=NumberFormat.getInstance();
						nf1.setMaximumFractionDigits(1);
						string.delete(0,string.length());
						string.append(MapViewer.messagesBundle.getString("distance"));
						string.append(' ');
						string.append(nf1.format(dis));
						string.append(' ');
						string.append(MapViewer.messagesBundle.getString("m"));
						string.append(".");
						if (viewer.getStatusBar()!=null)
							viewer.getStatusBar().setMessage(string.toString(),0);
					} else {
						double d=dis/1000;
						double km=Math.floor(d);
						double m=(d-km)*1000;
						NumberFormat nf0=NumberFormat.getInstance();
						nf0.setMaximumFractionDigits(0);
						string.delete(0,string.length());
						string.append(MapViewer.messagesBundle.getString("distance"));
						string.append(' ');
						string.append(nf0.format(d));
						string.append(' ');
						string.append(MapViewer.messagesBundle.getString("km"));
						string.append(' ');
						string.append(MapViewer.messagesBundle.getString("and"));
						string.append(' ');
						string.append(nf0.format(m));
						string.append(' ');
						string.append(MapViewer.messagesBundle.getString("m"));
						string.append(".");
						if (viewer.getStatusBar()!=null)
							viewer.getStatusBar().setMessage(string.toString(),0);
					}
					re=meterLine1.shape.getBounds();
					transformRect(getTransform(),re);
					getAuxiliaryPane().repaint(re.x-3,re.y-3,re.width+7,re.height+7);

					if (viewer.isMeterPopupEnabled()) {
						frame.setText(string.toString());
						frame.show();
					}
				}
			}
		};
		return lsnrMeter;
	}
	/**
	 * Mouse Listener for activate tool.
	 */
	protected MouseInputListener getIdentifyMouseListener() {
		if (lsnrIdentify==null)
		lsnrIdentify=new MouseInputAdapter() {
			private GeographicObject toBeActive;
			private int init,resh;
			public void mouseEntered(MouseEvent e) {
				setCursor(getCustomCursor("identifycursor"));
				setToolTipText(null);
			}
			public void mouseExited(MouseEvent e) {
				if (toBeActive!=null) {
					getAuxiliaryPane().removeAllShapes();
					getAuxiliaryPane().repaint();
				}
				setCursor(getCustomCursor("default"));
				setToolTipText(null);
			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				toBeActive=null;
				ILayerView activeLayer=null;
				int pr=getAuxiliaryPane().countShapes();
				if (pr>0) {
					ArrayList s=(ArrayList)getAuxiliaryPane().getShapes().clone();
					getAuxiliaryPane().removeAllShapes();
					for (int i=0;i<s.size();i++) {
						Object obj=s.get(i);
						if (obj instanceof AuxiliaryShape)
							repaintAux(((IVectorGeographicObject)((AuxiliaryShape)obj).shape).getBounds2D());
					}
				}
				ObjectBaseArray o=scanAround(e.getX(),e.getY());
				if (o.size()==0) {
					toBeActive=null;
					activeLayer=null;
				} else {
					toBeActive=(GeographicObject) o.get(0);
					activeLayer=(ILayerView) o.get(1);
				}
				if (toBeActive!=null) {
					getAuxiliaryPane().repaint();
					setCursor(getCustomCursor("identifyfoundcursor"));
					String tt=activeLayer.getName();
					//Set the tooltip text to the label of the object
					if (tt!=null) {
						setToolTipText(tt);
					} else {
						setToolTipText(null);
					}
				} else {
					setCursor(getCustomCursor("identifycursor"));
					if (pr!=0)
						getAuxiliaryPane().repaint();
					//Reset the tooltip text
					setToolTipText(null);
				}
			}
		};
		return lsnrIdentify;
	}
	/**
	 * Mouse Listener for navigate tool.
	 */
	protected MouseInputListener getNavigateMouseListener() {
		if (lsnrNavigate==null)
		lsnrNavigate=new MouseInputAdapter() {
			public void mousePressed(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==1) {
					getMotionPane().moveActiveTo(e.getX(),e.getY());
				}
			}
			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				setCursor(getCustomCursor("navigatecursor"));
			}
		};
		return lsnrNavigate;
	}
	/**
	 * Mouse Listener to display the coordinates on top-left.
	 * Exists together with the other listeners.
	 */
	protected MouseInputListener getCoordinatesMouseListener() {
		if (lsnrCoords==null)
		lsnrCoords=new MouseInputAdapter() {
			private NumberFormat nf=NumberFormat.getInstance(Locale.getDefault());
			public void mouseMoved(MouseEvent e) {
				if (map==null)
					return;
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					getAuxiliaryPane().setCoords("");
				} else {*/
					try {
//System.out.println("event location : "+e.getX()+","+e.getY());
						coordsCache1[0]=e.getX();
						coordsCache1[1]=e.getY();
						getInverseTransform().transform(coordsCache1,0,coordsCache2,0,1);
						getInversePositionTransform().transform(coordsCache2,0,coordsCache1,0,1);
						getAuxiliaryPane().setCoords("("+nf.format(coordsCache1[0])+" , "+nf.format(coordsCache1[1])+")");
					} catch(NullPointerException ex) {//Transformations may be null
						getAuxiliaryPane().setCoords("");
					}
				//}
			}
			public void mouseDragged(MouseEvent e) {
				if (map==null)
					return;
				/*if (!layers.insideVisibleArea(e.getX(),e.getY()))
					aux.setCoords("");
				else*/ {
					coordsCache1[0]=e.getX();
					coordsCache1[1]=e.getY();
					getInverseTransform().transform(coordsCache1,0,coordsCache2,0,1);
					getInversePositionTransform().transform(coordsCache2,0,coordsCache1,0,1);
					getAuxiliaryPane().setCoords("("+nf.format(coordsCache1[0])+" , "+nf.format(coordsCache1[1])+")");
				}
			}
			public void mouseExited(MouseEvent e) {
				getAuxiliaryPane().setCoords("");
			}
		};
		return lsnrCoords;
	}

	/**
	 * MouseListener for tool that zooms in a given rectangle.
	 */


	protected MouseInputListener getZoomInToRectMouseListener() {
		if (lsnrZoomRect==null)
		lsnrZoomRect=new MouseInputAdapter() {
			private int pressX,pressY;
			private boolean cancel,validStart=false;
			private final Color flclr=new Color(255,255,128,64);
			private BasicStroke strk=new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {1,1},0);
			private double[] reusable=new double[2];
			private AuxiliaryShape zoomRect;

			public void mouseMoved(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}*/
				if (e.isControlDown())
					setCursor(getCustomCursor("zoomoutrectcursor"));
				else
					setCursor(getCustomCursor("zoomrectcursor"));
			}
			public void mousePressed(MouseEvent e) {
				//A mouse event must be inside the clip area.
				/*if (!layers.insideVisibleArea(e.getX(),e.getY()))
					return;*/
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==1) {
					p2d.setLocation(e.getX(),e.getY());
					getInverseTransform().transform(p2d,p2d);
					pressX=(int)Math.round(p2d.x);
					pressY=(int)Math.round(p2d.y);
					//getAuxiliaryPane().removeAllShapes();
					getAuxiliaryPane().repaint();
					cancel=false;
					validStart=true;
				}
			}

			private IRegionView getInnerRegionView(IRegionView region,Rectangle2D rect,IRegionView found) {
				IRegionView[] r=region.getChildRegionViews();
				for (int i=0;i<r.length;i++) {
					if (r[i].getBoundingRect().contains(rect)) {
						if (found==null || (found!=null && r[i].getDepthInTree()>found.getDepthInTree()))
							found=r[i];
					}
					found=getInnerRegionView(r[i],rect,found);
				}
				return found;
			}

			public void mouseReleased(MouseEvent e) {
				getAuxiliaryPane().removeAllShapes();
				if (!validStart || SwingUtilities.isRightMouseButton(e)) {
					zoomRect=null;
					cancel=true;
				} else {
					if (!cancel) {
						if (zoomRect!=null) {
							//Look for an inner region for better zoom quality
							Rectangle2D.Double d=new Rectangle2D.Double(((Rectangle2D.Double)zoomRect.shape).x,((Rectangle2D.Double)zoomRect.shape).y,((Rectangle2D.Double)zoomRect.shape).width,((Rectangle2D.Double)zoomRect.shape).height);
							IRegionView zz=getInnerRegionView(map.getActiveRegionView(),transformRect(getInversePositionTransform(),d),null);
							if (zz!=null) {
								viewer.blockRedraw=true;
								map.setActiveRegionView(zz);
								viewer.blockRedraw=false;
								clearTransformations();
								d.setRect(d.x,d.y,d.width,d.height);
								transformRect(getPositionTransform(),d);
								zoomRect.shape=d;
							}

							//Whole area selected
							Rectangle2D.Double rc=(Rectangle2D.Double) zoomRect.shape;
							offsetX+=((int) Math.round(layers.currentView.x+layers.currentView.width/2-rc.x-rc.width/2));
							offsetY+=((int) Math.round(layers.currentView.y+layers.currentView.height/2-rc.y-rc.height/2));
							clearTransformations();
							if (viewer.zoom!=null) {
								//The change listener in the zoom tool does the change
								if (e.isControlDown())
								//Zoom out
									viewer.zoom.setValue((int) Math.round(100*zoom*Math.min(rc.width/layers.currentView.width,rc.height/layers.currentView.height)));
								else
								//Zoom in
									viewer.zoom.setValue((int) Math.round(100*zoom*Math.min(layers.currentView.width/rc.width,layers.currentView.height/rc.height)));
							} else {
								//There is no zoom tool to pass the zoom from the change listener
								if (e.isControlDown())
								//Zoom out
									setZoom(zoom*Math.min(rc.width/layers.currentView.width,rc.height/layers.currentView.height));
								else
								//Zoom in
									setZoom(zoom*Math.min(layers.currentView.width/rc.width,layers.currentView.height/rc.height));
							}
							layers.viewZoomChanged(true);
							setRedrawEnabled(true);
						} else {
							//Just clicked
							if (e.isControlDown()) {
							//Zoom out
							} else {
							//Zoom in
							}
						}
					}
					zoomRect=null;
				}
				validStart=false;
			}
			public void mouseDragged(MouseEvent e) {
				//A mouse event must be inside the clip area.
				if (!validStart) {// || !layers.insideVisibleArea(e.getX(),e.getY())) {
					setCursor(getCustomCursor("default"));
					return;
				}
				if (e.isControlDown())
					setCursor(getCustomCursor("zoomoutrectcursor"));
				else
					setCursor(getCustomCursor("zoomrectcursor"));
				//Left mouse button
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (zoomRect==null) {
						zoomRect=new AuxiliaryShape(new java.awt.geom.Rectangle2D.Double(),strk,null,flclr,false);
						getAuxiliaryPane().addShape(zoomRect);
					}
					p2d.setLocation(e.getX(),e.getY());
					getInverseTransform().transform(p2d,p2d);
					double ox,oy,ow,oh;
					ox=((Rectangle2D.Double) zoomRect.shape).x;
					oy=((Rectangle2D.Double) zoomRect.shape).y;
					ow=((Rectangle2D.Double) zoomRect.shape).width;
					oh=((Rectangle2D.Double) zoomRect.shape).height;
					((Rectangle2D.Double) zoomRect.shape).x=Math.min(pressX,p2d.x);
					((Rectangle2D.Double) zoomRect.shape).y=Math.min(pressY,p2d.y);
					((Rectangle2D.Double) zoomRect.shape).width=Math.abs(p2d.x-pressX);
					((Rectangle2D.Double) zoomRect.shape).height=Math.abs(p2d.y-pressY);

					//Calculate the minimum repaint rectangle.
					double minX=Math.min(ox,((Rectangle2D.Double) zoomRect.shape).x);
					double minY=Math.min(oy,((Rectangle2D.Double) zoomRect.shape).y);
					double maxX=Math.max(ox+ow,((Rectangle2D.Double) zoomRect.shape).x+((Rectangle2D.Double) zoomRect.shape).width);
					double maxY=Math.max(oy+oh,((Rectangle2D.Double) zoomRect.shape).y+((Rectangle2D.Double) zoomRect.shape).height);
					//Rectangle r=(Rectangle) (new Rectangle(minX,minY,maxX-minX,maxY-minY)).createTransformedShape(getTransform());
					//getAuxiliaryPane().repaint((int) r.x-2,(int) r.y-2,(int) r.width+4,(int) r.height+4);
					reusable[0]=minX; reusable[1]=minY;
					getTransform().transform(reusable,0,reusable,0,1);
					double tlX=reusable[0];
					double tlY=reusable[1];
					reusable[0]=maxX; reusable[1]=minY;
					getTransform().transform(reusable,0,reusable,0,1);
					double trX=reusable[0];
					double trY=reusable[1];
					reusable[0]=maxX; reusable[1]=maxY;
					getTransform().transform(reusable,0,reusable,0,1);
					double brX=reusable[0];
					double brY=reusable[1];
					reusable[0]=minX; reusable[1]=maxY;
					getTransform().transform(reusable,0,reusable,0,1);
					double blX=reusable[0];
					double blY=reusable[1];
					minX=Math.min(Math.min(Math.min(tlX,trX),blX),brX)-5;
					minY=Math.min(Math.min(Math.min(tlY,trY),blY),brY)-5;
					maxX=Math.max(Math.max(Math.max(tlX,trX),blX),brX)+10;
					maxY=Math.max(Math.max(Math.max(tlY,trY),blY),brY)+10;


					getAuxiliaryPane().repaint((int) minX,(int) minY,(int) (maxX-minX),(int) (maxY-minY));
				}
			}
		};
		return lsnrZoomRect;
	}

	//Constants
	private static final double ZOOM_STEP=0.01d;
	/**
	 * Defines the pixel tolerance for activation and other actions.
	 */
	protected static final int PIXEL_TOLERANCE=3;
	//Listener management variables
	ArrayList ml;
	//UI variables
	private int width,height; //Hold the width and height of MapPane. Needed to calculate the center of view on resizing.
	private Cursor cacheCursor;
	private boolean busy;
	/**
	 * The associated viewer.
	 */
	protected MapViewer viewer;

	//The offset of the center of view from the center of the map
	int offsetX,offsetY;
	//Layer variables
	/**
	 * Draws the vector and raster layers.
	 */
	protected LayerPane layers;
	/**
	 * Draws auxiliary information (highlights, coordinates etc).
	 */
	protected AuxiliaryPane aux;
	/**
	 * Draws the labels of the geographic objects.
	 */
	protected LabelPane labels;
	/**
	 * Draws agents and miscellaneous agent information (paths etc).
	 */
	protected MotionPane motion;
	private AuxiliaryShape[] zoomRects;
	private AuxiliaryShape meterLine1,meterLine2;
	//Cursors
	static HashMap cursors;
	/**
	 * Visual control.
	 */
	private boolean antialiasing,quality;
	private RenderingHints renderingHints;
	//Map variables
	protected IMapView map;
	double rotation;
	private double zoom;
	private AffineTransform transform,invTransform,posTransform,invPosTransform;
	/**
	 * A reusable array, used in to pass return values to scan around calls.
	 */
	private ObjectBaseArray scanArray;
	//Colors for zoom rects
	Color zoomOutline,zoomFill,zoomHighOutline,zoomHighFill,zoomGoOutline,zoomGoFill,COLOROUTT,COLORFILT,COLOROUTF,COLORFILF;
	//Reusable objects
	Point2D.Double offX,offY,ptr;
	private double[] coordsCache1,coordsCache2;
	private MouseInputListener coordsListener;
	private Legend legend;
	/**
	 * oldWidth and oldHeight exist to prevent the ancestor listener being called more than once
	 * when one of the ancestors change.
	 */
	private int oldWidth=0,oldHeight=0;
	/**
	 * Flags the validity of the coordinates the map has.
	 */
	private boolean validCoordinates;
	private static double TO_RAD=Math.PI/180d;
	/**
	 * This variable disables redrawing in the RedrawThread, when false. It is used
	 * to enable batch redrawing of the map in synergy with variable 'needsRedrawing'.
	 */
	boolean redrawEnabled=true;
	/**
	 * When redrawing is disabled and redraw requests arrive, this variable is set to
	 * true, to mark that the map needs redrawing.
	 */
	boolean needsRedrawing=false;
	/**
	 * When redrawing is disabled and redraw requests arrive, this variable is set to
	 * true, to mark that the labels need redrawing.
	 */
	boolean labelsNeedRedrawing=false;
	/**
	 * A timer to delay sending continuous resize events to layers which produce a great amount of work
	 * trying to sent as less as possible.
	 */
	private javax.swing.Timer delayResizeEvent;
	/**
	 * An AuxiliaryShape used in selection. Avoids creating new objects (strokes, colors etc).
	 * To make the new selection shape the selshape.shape object must be changed.
	 */
	private AuxiliaryShape selshape;
	private Point2D.Double p2d=new Point2D.Double();
	private Insets ins;
	private static final Integer LAYERS_LAYER=new Integer(10);
	private static final Integer LABELS_LAYER=new Integer(20);
	private static final Integer AUXILIARY_LAYER=new Integer(30);
	private static final Integer MOTION_LAYER=new Integer(40);
	private static final Integer LEGEND_LAYER=new Integer(50);
	static RenderingHints clearedRenderingHints=new RenderingHints(null);
	//Save time from creating the listeners, especially when changing the region
	//and attachListenersOfSelectedTools reattaches the listeners.
	private MouseInputListener lsnrActivate,lsnrSelectRect,lsnrSelectCircle,lsnrBrowse,lsnrPan,lsnrInsObj,lsnrRotate,lsnrGoIn,lsnrGoOut,lsnrMeter,lsnrIdentify,lsnrNavigate,lsnrCoords,lsnrZoomRect;
	private Dimension screenSizeMM,screenSizePXL;
	//Variable initialization
	{
		rotation=0d;
		zoom=1d;
		busy=false;
		antialiasing=true;
		quality=false;
		scanArray=new ObjectBaseArray();
		validCoordinates=false;
		offX=new Point2D.Double();
		offY=new Point2D.Double();
		ptr=new Point2D.Double();
		selshape=new AuxiliaryShape(null,new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {1,1},0),new Color(255,255,255,255),new Color(0,0,0,0),true);

		COLOROUTT=new Color(64,128,64);
		COLORFILT=new Color(255,255,196,75);

		COLOROUTF=new Color(0,0,0,0);
		COLORFILF=new Color(0,0,0,0);
		zoomOutline=COLOROUTT;
		zoomFill=COLORFILT;
		zoomHighOutline=new Color(255,255,255);
		zoomHighFill=new Color(255,255,196,75);
		zoomGoOutline=new Color(0,235,255);
		zoomGoFill=new Color(255,255,255,64);
	}
}
