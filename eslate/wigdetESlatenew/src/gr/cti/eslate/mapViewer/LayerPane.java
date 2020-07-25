package gr.cti.eslate.mapViewer;

import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.mapModel.geom.Polygon;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapBackground;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IPolyLineLayerView;
import gr.cti.eslate.protocol.IPolygonLayerView;
import gr.cti.eslate.protocol.IRasterLayerView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IVectorLayerView;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 * In this layer the map layers are drawn. There exists the concept of Z-Ordering.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
class LayerPane extends JPanel implements TransparentMouseInput {

	LayerPane(MapPane mpPane) {
		this.mapPane=mpPane;
		setOpaque(false);
		setLayout(null);
	}

	/**
	 * Adds a MouseInputListener. This is different from adding a MouseListener and a MouseMotionListener seperately.
	 */
	public void addMouseInputListener(MouseInputListener l) {
		if (l==null) return;
		if (ml==null)
			ml=new ArrayList();
		ml.add(l);
		addMouseListener(l);
		addMouseMotionListener(l);
	}
	/**
	 * Removes a MouseInputListener.
	 */
	public void removeMouseInputListener(MouseInputListener l) {
		if (ml==null)
			return;
		ml.remove(l);
		removeMouseListener(l);
		removeMouseMotionListener(l);
	}

	/**
	 * Removes all Mouse and MouseMotion Listeners.
	 * @return A list containing the listeners removed.
	 */
	public List removeAllListeners() {
		ArrayList copy=new ArrayList();
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
	/**
	 * Processes mouse events occurring on this component by dispatching
	 * them to any registered MouseListener objects and to the hosting MapPane object.
	 /
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
		mapPane.dispatchEvent(e);
	}
	/**
	 * Processes mouse motion events occurring on this component by dispatching
	 * them to any registered MouseListener objects and to the hosting MapPane object.
	 /
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
		mapPane.dispatchEvent(e);
	}*/
	/**
	 * Sets the background image.
	 */
	protected void setBackground(IMapBackground background) {
		//Clear the memory from the previous background
		if (this.background!=null) {
			this.background.clearImageData();
		}
		this.background=background;
		if (background==null) {
			if (mapPane.viewer.miniaturePane!=null)
				mapPane.viewer.miniaturePane.clearImage();
			if (mapPane.map!=null && mapPane.map.getActiveRegionView().getBoundingRect()!=null) {
				//View size changed is called in this
				findOriginalSizeOnNullBackground();
			}
		} else {
			int ow=originalWidth; int oh=originalHeight;
			if (mapPane.viewer.miniaturePane!=null)
				mapPane.viewer.miniaturePane.imageChanged(mapPane.map);
			PerformanceManager pm=PerformanceManager.getPerformanceManager();
			pm.init(mapPane.viewer.getBackgroundFromModel);
			//Invoke the loading of the image, if it is not loaded.
			background.getImage();
			pm.stop(mapPane.viewer.getBackgroundFromModel);
			pm.displayTime(mapPane.viewer.getBackgroundFromModel,mapPane.viewer.getESlateHandle(),"","ms");
			originalWidth=background.getIconWidth();
			originalHeight=background.getIconHeight();
			//if the new image has different dimensions the layer image painted
			//is invalid. Clearing the currentImage will make viewSizeChanged
			//to repaint the entire image.
			if (ow!=originalWidth && oh!=originalHeight) {
				mapPane.clearTransformations();
				if (currentImage!=null) {
					currentImage.getGraphics().dispose();
					currentImage.flush();
					currentImage=null;
				}
			}
		}

		if (mapPane.viewer.miniaturePane!=null)
			mapPane.viewer.miniaturePane.repaint();

		//Do not clear image data as they will be needed by the first
		//request. They will be cleared afterwards.
		//if (background!=null)
			//background.clearImageData();
	}

	protected Icon getCurrentBackground() {
		return background;
	}

	/**
	 * Sets the layers and draws them.
	 */
	protected void setLayers(ILayerView[] lv) {
		if (notShownYet) {
			pendingLayers=lv;
			return;
		}
		if (lv==null || lv.length == 0) {
			//Clean images
			if (currentImage!=null) {
				currentImage.getGraphics().dispose();
				currentImage.flush();
			}
			currentImage=null;
			mayHaveLabels=false;
			if (mapPane.labels!=null)
				mapPane.labels.clear();
		} else {
			mapPane.needsRedrawing=true;
			mayHaveLabels=mayHaveLabels();
			if (mayHaveLabels && mapPane.labels==null)
				mapPane.createLabelPane();
		}
	}
	/**
	 * Finds out if the layers shown may have labels to draw.
	 */
	protected boolean mayHaveLabels() {
		mayHaveLabels=false;
		ILayerView[] lv=mapPane.map.getActiveRegionView().getLayerViews();
		for (int i=0;i<lv.length && !mayHaveLabels;i++)
			mayHaveLabels=lv[i].mayHaveLabels();
		return mayHaveLabels;
	}
	/**
	 * Clears all resources on closing.
	 */
	protected void clearAll() {
		if (currentImage!=null) {
			currentImage.getGraphics().dispose();
			currentImage.flush();
		}
		currentImage=null;
		if (mapPane.viewer.miniaturePane!=null)
			mapPane.viewer.miniaturePane.clearImage();
	}
	/**
	 * Converts the offscreenView to real coordinates.
	 */
	protected Rectangle2D.Double offscreenViewToReal() {
		//Find the real coordinates of the offscreen view
		reused[0]=offscreenView.x;
		reused[1]=offscreenView.y+offscreenView.height;
		mapPane.getInversePositionTransform().transform(reused,0,reused,0,1);
		double x=reused[0];
		double y=reused[1];
		reused[0]=offscreenView.x+offscreenView.width;
		reused[1]=offscreenView.y;
		mapPane.getInversePositionTransform().transform(reused,0,reused,0,1);
		return new Rectangle2D.Double(x,y,reused[0]-x,reused[1]-y);
	}
	/**
	 * Redraws the layers image, calculating the real coordinates
	 * of the offscreen view.
	 */
	protected void redrawImage() {
		if (!mapPane.isRedrawEnabled()) {
			mapPane.needsRedrawing=true;
			return;
		}
		redrawImage(offscreenViewToReal());
	}
	/**
	 * Redraws the layers image without calculating the real coordinates
	 * of the offscreen view. The coordinates are given in redrawRect.
	 */
	protected void redrawImage(Rectangle2D redrawRect) {
		if (!mapPane.isRedrawEnabled()) {
			mapPane.needsRedrawing=true;
			return;
		}
		mapPane.needsRedrawing=false;

		mapPane.setBusy(true);

//@@@@@@@@@@@@@@REMOVE#@@@@@@@@@@@@@@
//for (int i=0;i<mapPane.map.getActiveRegionView().getLayerViews().length;i++)
//mapPane.map.getActiveRegionView().getLayerViews()[i].getGeographicObjects();
		PerformanceManager pm=PerformanceManager.getPerformanceManager();
		pm.init(mapPane.viewer.fullMapRedraw);
		//When there are no layers clean memory and return
		if (mapPane.map.getActiveRegionView().getLayerViews().length==0) {
			if (currentImage!=null) {
				//Clean previous image
				currentImage.getGraphics().dispose();
				currentImage.flush();
				currentImage=null;
			}
		} else {
			//Keep the same image and avoid destroying and recreating when image is 25% or less larger
			if (currentImage==null || (currentImage.getWidth()<offscreenView.width || currentImage.getHeight()<offscreenView.height) || (0.75f*currentImage.getWidth()>offscreenView.width || 0.75f*currentImage.getHeight()>offscreenView.height)) {
				//We cannot reuse the current buffer image. A new one has to be created.
				//First clean the old one.
				if (currentImage!=null) {
					currentImage.getGraphics().dispose();
					currentImage.flush();
				}
				currentImage=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(offscreenView.width,offscreenView.height,Transparency.TRANSLUCENT);
				((Graphics2D)currentImage.getGraphics()).setRenderingHints(mapPane.getRenderingHints());
			} else {
				//Clean previous image
				Graphics2D g2=(Graphics2D) currentImage.getGraphics();
				Composite old=g2.getComposite();
				g2.setComposite(AlphaComposite.Clear);
				g2.fillRect(0,0,currentImage.getWidth(),currentImage.getHeight());
//				g2.clearRect(0,0,currentImage.getWidth(),currentImage.getHeight());
				//If the buffer image is larger than needed, we keep it and clip its rectangle
				g2.clipRect(0,0,offscreenView.width,offscreenView.height);
				g2.setComposite(old);
			}
			drawPatch(currentImage,redrawRect);
		}

		pm.stop(mapPane.viewer.fullMapRedraw);
		pm.displayTime(mapPane.viewer.fullMapRedraw,mapPane.viewer.getESlateHandle(),"","ms");
		mapPane.setBusy(false);
	}
	/**
	 * Redraws the layers image. If either of rect1,rect2 is not null a portion
	 * is redrawn which is patched to the existing image.
	 * @param   real1   The first rectangle to redraw in real coordinates.
	 * @param   real2   The second rectangle to redraw in real coordinates. Two rectangles are used in selecting, for example.
	 */
	protected void redrawImage(Rectangle2D real1,Rectangle2D real2) {
		if (!mapPane.isRedrawEnabled()) {
			mapPane.needsRedrawing=true;
			return;
		}
		mapPane.setBusy(true);

		PerformanceManager pm=PerformanceManager.getPerformanceManager();
		pm.init(mapPane.viewer.partialMapRedraw);
		//When there are no layers clean memory and return
		if (mapPane.map.getActiveRegionView().getLayerViews().length==0) {
			if (currentImage!=null) {
				//Clean previous image
				currentImage.getGraphics().dispose();
				currentImage.flush();
				currentImage=null;
			}
		} else {
			if (real1!=null) {
				//Enlarge the rectangle by 5 pixels in all dimensions to easily eliminate the need
				//to find the icon size of point objects
				int amount=5;
				real1.setRect(real1.getX()-amount/mapPane.getPositionTransform().getScaleX(),
				              real1.getY()-amount/(-mapPane.getPositionTransform().getScaleY()),
				              real1.getWidth()+2*amount/mapPane.getPositionTransform().getScaleX(),
				              real1.getHeight()+2*amount/(-mapPane.getPositionTransform().getScaleY())
				              );
				BufferedImage patch=drawPatch(real1);
				if (patch!=null) {
					//Transform the rectangle to place the patch
					tl.setLocation(real1.getX(),real1.getY());
					mapPane.getPositionTransform().transform(tl,tl);
					br.setLocation(real1.getX()+real1.getWidth(),real1.getY()+real1.getHeight());
					mapPane.getPositionTransform().transform(br,br);
					//Clear and place the patch
					Graphics2D g2=(Graphics2D) currentImage.getGraphics();
					//g2.setBackground(clearColor);
					//g2.clearRect((int)tl.getX()-shownOffscreenView.x,(int)br.getY()-shownOffscreenView.y,(int)(br.getX()-tl.getX()),(int)(tl.getY()-br.getY()));
					Composite old=g2.getComposite();
					g2.setComposite(AlphaComposite.Clear);
					g2.fillRect((int) tl.getX()-shownOffscreenView.x,(int) br.getY()-shownOffscreenView.y,(int) (br.getX()-tl.getX()),(int) (tl.getY()-br.getY()));
					g2.setComposite(old);

					g2.drawImage(patch,(int)tl.getX()-shownOffscreenView.x,(int)br.getY()-shownOffscreenView.y,LayerPane.this);
					patch.flush();
					patch=null;
					//This actually transforms the drawing point to the component space
					p2d.setLocation(0,0);
					mapPane.getTransform().transform(p2d,p2d);
					repaint((int)(tl.getX()+p2d.x),(int)(br.getY()+p2d.y),currentImage.getWidth(),currentImage.getHeight());
				}
			}
			if (real2!=null) {
				//Enlarge the rectangle by 5 pixels in all dimensions to easily eliminate the need
				//to find the icon size of point objects
				int amount=5;
				real2.setRect(real2.getX()-amount/mapPane.getPositionTransform().getScaleX(),
				              real2.getY()-amount/(-mapPane.getPositionTransform().getScaleY()),
				              real2.getWidth()+2*amount/mapPane.getPositionTransform().getScaleX(),
				              real2.getHeight()+2*amount/(-mapPane.getPositionTransform().getScaleY())
				              );
				BufferedImage patch=drawPatch(real2);
				if (patch!=null) {
					//Transform the rectangle to place the patch
					tl.setLocation(real2.getX(),real2.getY());
					mapPane.getPositionTransform().transform(tl,tl);
					br.setLocation(real2.getX()+real2.getWidth(),real2.getY()+real2.getHeight());
					mapPane.getPositionTransform().transform(br,br);
					//Clear and place the patch
					Graphics2D g2=(Graphics2D) currentImage.getGraphics();
					//g2.setBackground(clearColor);
					//g2.clearRect((int)tl.getX()-shownOffscreenView.x,(int)br.getY()-shownOffscreenView.y,(int)(br.getX()-tl.getX()),(int)(tl.getY()-br.getY()));
					Composite old=g2.getComposite();
					g2.setComposite(AlphaComposite.Clear);
					g2.fillRect((int) tl.getX()-shownOffscreenView.x,(int) br.getY()-shownOffscreenView.y,(int) (br.getX()-tl.getX()),(int) (tl.getY()-br.getY()));
					g2.setComposite(old);

					g2.drawImage(patch,(int)tl.getX()-shownOffscreenView.x,(int)br.getY()-shownOffscreenView.y,LayerPane.this);
					patch.flush();
					patch=null;
					//This actually transforms the drawing point to the component space
					p2d.setLocation(0,0);
					mapPane.getTransform().transform(p2d,p2d);
					repaint((int)(tl.getX()+p2d.x),(int)(br.getY()+p2d.y),currentImage.getWidth(),currentImage.getHeight());
				}
			}
		}
		pm.stop(mapPane.viewer.partialMapRedraw);
		pm.displayTime(mapPane.viewer.partialMapRedraw,mapPane.viewer.getESlateHandle(),"","ms");
		mapPane.setBusy(false);
	}
	/**
	 * Draws an image of a specific area. This area can be patched to the map image.
	 * The method creates a new image and returns it drawn.
	 * @param   dim The bounding rectangle of the area in map real coordinates.
	 */
	private BufferedImage drawPatch(Rectangle2D dim) {
		//Don't draw an image larger than the offscreen view. It can possibly produce
		//an out of memory error, especially in large zoom factors.
		Rectangle2D rd=new Rectangle2D.Double(dim.getX(),dim.getY(),dim.getWidth(),dim.getHeight());
		mapPane.transformRect(mapPane.getPositionTransform(),rd);
		//Find the intersection of the view rectangle and the selection rectangle
		if (rd.getWidth()>offscreenView.width || rd.getHeight()>offscreenView.height) {
			Area ar=(new Area(new Rectangle2D.Double(rd.getX(),rd.getY(),rd.getWidth(),rd.getHeight())));
			ar.intersect(new Area(offscreenView));
			rd=ar.getBounds2D();
			//Change the real coordinates rectangle also
			dim.setRect(rd);//.getX(),rd.getY()+rd.getHeight(),rd.getWidth(),-rd.getHeight());
			mapPane.transformRect(mapPane.getInversePositionTransform(),dim);
		}
		if (Math.round(rd.getWidth())<=0 || Math.round(rd.getHeight())<=0)
			return null;
		BufferedImage bufferImage=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage((int)Math.round(rd.getWidth()),(int)Math.round(rd.getHeight()),Transparency.TRANSLUCENT);
		((Graphics2D)bufferImage.getGraphics()).setRenderingHints(mapPane.getRenderingHints());
		drawPatch(bufferImage,dim);
		return bufferImage;
	}
	/**
	 * Draws a specific area on the given image. This area can be patched to the map image.
	 * @param   dim The bounding rectangle of the area in map real coordinates.
	 */
	private void drawPatch(BufferedImage bufferImage,Rectangle2D dim) {
		Graphics2D g2=(Graphics2D) bufferImage.getGraphics();

		g2.setTransform(mapPane.getPositionTransform());
		//Translate the context to the patch position
		Rectangle2D r=mapPane.map.getActiveRegionView().getBoundingRect();
		g2.translate(-dim.getX()+r.getX(),-dim.getY()-dim.getHeight()+r.getY()+r.getHeight());
		ILayerView[] layers=mapPane.map.getActiveRegionView().getLayerViews();
		PerformanceManager pm=PerformanceManager.getPerformanceManager();

		for (int i=0;i<layers.length;i++) {
			if (layers[i].isVisible()) {
				if (layers[i] instanceof IVectorLayerView) {
					pm.init(mapPane.viewer.getLayersFromModel);
					ArrayList objs=layers[i].getGeographicObjects(dim,true);
					pm.stop(mapPane.viewer.getLayersFromModel);
					pm.displayTime(mapPane.viewer.getLayersFromModel,mapPane.viewer.getESlateHandle(),layers[i].getName(),"ms");
					drawObjects(((IVectorLayerView) layers[i]),objs,g2,false);
				} else { //It is a raster layer
					IRasterLayerView rlv=(IRasterLayerView) layers[i];
					//Find the top-left point in pixels
					reused[0]=rlv.getBoundingRect().x;
					reused[1]=rlv.getBoundingRect().y+rlv.getBoundingRect().height;
					mapPane.getPositionTransform().transform(reused,0,reused,0,1);
					double rx1=reused[0]; double ry1=reused[1];
					//Find the bottom-right point in pixels
					reused[0]=rlv.getBoundingRect().x+rlv.getBoundingRect().width;
					reused[1]=rlv.getBoundingRect().y;
					mapPane.getPositionTransform().transform(reused,0,reused,0,1);
					double rx2=reused[0]; double ry2=reused[1];
					//The scaling factors
					double sx=(rx2-rx1)/rlv.getRaster().getWidth();
					double sy=(ry2-ry1)/rlv.getRaster().getHeight();
					//Paint the raster
					AffineTransform at=new AffineTransform();
					at.translate(rx1,ry1);
					at.scale(sx,sy);
					g2.drawImage(rlv.getRaster(),at,null);
				}
			}
		}
	}
	/**
	 * When the layerPane shows no background, this procedure calculates the size it should have.
	 */
	void findOriginalSizeOnNullBackground() {
		int ow=originalWidth; int oh=originalHeight;
		Rectangle2D.Double r=(Rectangle2D.Double) mapPane.map.getActiveRegionView().getBoundingRect();
		if (getWidth()/r.getWidth()>=getHeight()/r.getHeight()) {
			originalHeight=getHeight();
			originalWidth=(int) Math.round(getHeight()*r.getWidth()/r.getHeight());
		} else {
			originalHeight=(int) Math.round(getWidth()*r.getHeight()/r.getWidth());
			originalWidth=getWidth();
		}
		mapPane.clearTransformations();
		//if the new image has different dimensions the layer image painted
		//is invalid. Clearing the currentImage will make viewSizeChanged
		//to repaint the entire image.
		if (ow!=originalWidth && oh!=originalHeight && currentImage!=null) {
			currentImage.getGraphics().dispose();
			currentImage.flush();
			currentImage=null;
		}
		viewSizeChanged(true);
	}
	/**
	 * Called by the parent component to inform for a view size change, e.g. when the resizing parent.
	 */
	protected void viewSizeChanged(boolean startPainting) {
		redefineView();
		if (!mapPane.isRedrawEnabled()) {
			mapPane.needsRedrawing=true;
			return;
		}
		PerformanceManager pm=PerformanceManager.getPerformanceManager();
		pm.init(mapPane.viewer.partialMapRedraw);
		//Make the current image the "previous" cache image, so the current image can be
		// drawn until the new one is ready.
		Rectangle2D.Double topreal,bottomreal,leftreal,rightreal;
		Rectangle toppixel,bottompixel,leftpixel,rightpixel;
		topreal=bottomreal=leftreal=rightreal=null;
		toppixel=bottompixel=leftpixel=rightpixel=null;
		if (startPainting) {
			if (/*!shownOffscreenView.contains(offscreenView) &&*/ offscreenView.width>0 && offscreenView.height>0) {
				if (currentImage==null) {
					redrawImage();
				} else {
					int upOffsetForLnR=0;
					int bottomOffsetForLnR=0;
					mapPane.setBusy(true);
					redefineView();

					//Create a new image
					BufferedImage newImage=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(offscreenView.width,offscreenView.height,Transparency.TRANSLUCENT);
					((Graphics2D)newImage.getGraphics()).setRenderingHints(mapPane.getRenderingHints());
					//Paint the old one on it
					newImage.getGraphics().drawImage(currentImage,shownOffscreenView.x-offscreenView.x,shownOffscreenView.y-offscreenView.y,this);
					//Paint the 4 possible patches
					Rectangle2D.Double real=new Rectangle2D.Double();
					Rectangle2D.Double pixel=new Rectangle2D.Double();
					//Top
					pixel.setRect(offscreenView.x,offscreenView.y,offscreenView.width,shownOffscreenView.y-offscreenView.y);
					if (((int)pixel.width)>0 && ((int)pixel.height)>0) {
						//Find the rectangle to repaint in real coordinates
						p2d.setLocation(pixel.x,pixel.y+pixel.height);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						double x=p2d.x,y=p2d.y;
						p2d.setLocation(pixel.x+pixel.width,pixel.y);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						real.setRect(x,y,p2d.x-x,p2d.y-y);
						//Paint the patch
						BufferedImage patch=drawPatch(real);
						if (patch!=null) {
						//Place the patch
							Graphics2D g2=(Graphics2D) newImage.getGraphics();
							g2.drawImage(patch,0,0,LayerPane.this);
							upOffsetForLnR=patch.getHeight();
							patch.getGraphics().dispose();
							patch.flush();
							patch=null;
						}
						if (mayHaveLabels) {
							topreal=new Rectangle2D.Double(real.getX(),real.getY(),real.getWidth(),real.getHeight());
							toppixel=new Rectangle((int) Math.round(pixel.getX()),(int) Math.round(pixel.getY()),(int) Math.round(pixel.getWidth()),(int) Math.round(pixel.getHeight()));
						}
					}
					//Bottom
					pixel.setRect(offscreenView.x,shownOffscreenView.y+shownOffscreenView.height,offscreenView.width,offscreenView.y+offscreenView.height-shownOffscreenView.y-shownOffscreenView.height);
					if (((int)pixel.width)>0 && ((int)pixel.height)>0) {
						//Find the rectangle to repaint in real coordinates
						p2d.setLocation(pixel.x,pixel.y+pixel.height);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						double x=p2d.x,y=p2d.y;
						p2d.setLocation(pixel.x+pixel.width,pixel.y);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						real.setRect(x,y,p2d.x-x,p2d.y-y);
						//Paint the patch
						BufferedImage patch=drawPatch(real);
						if (patch!=null) {
							//Place the patch
							Graphics2D g2=(Graphics2D) newImage.getGraphics();
							g2.drawImage(patch,0,newImage.getHeight()-patch.getHeight(),LayerPane.this);
							bottomOffsetForLnR=patch.getHeight();
							patch.getGraphics().dispose();
							patch.flush();
							patch=null;
						}
						if (mayHaveLabels) {
							bottomreal=new Rectangle2D.Double(real.getX(),real.getY(),real.getWidth(),real.getHeight());
							bottompixel=new Rectangle((int) Math.round(pixel.getX()),(int) Math.round(pixel.getY()),(int) Math.round(pixel.getWidth()),(int) Math.round(pixel.getHeight()));
						}
					}
					//Left
					pixel.setRect(offscreenView.x,offscreenView.y+upOffsetForLnR,shownOffscreenView.x-offscreenView.x,newImage.getHeight()-upOffsetForLnR-bottomOffsetForLnR);
					if (((int)pixel.width)>0 && ((int)pixel.height)>0) {
						//Find the rectangle to repaint in real coordinates
						p2d.setLocation(pixel.x,pixel.y+pixel.height);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						double x=p2d.x,y=p2d.y;
						p2d.setLocation(pixel.x+pixel.width,pixel.y);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						real.setRect(x,y,p2d.x-x,p2d.y-y);
						//Paint the patch
						BufferedImage patch=drawPatch(real);
						if (patch!=null) {
							//Place the patch
							Graphics2D g2=(Graphics2D) newImage.getGraphics();
							g2.drawImage(patch,0,upOffsetForLnR,LayerPane.this);
							patch.getGraphics().dispose();
							patch.flush();
							patch=null;
						}
						if (mayHaveLabels) {
							leftreal=new Rectangle2D.Double(real.getX(),real.getY(),real.getWidth(),real.getHeight());
							leftpixel=new Rectangle((int) Math.round(pixel.getX()),(int) Math.round(pixel.getY()),(int) Math.round(pixel.getWidth()),(int) Math.round(pixel.getHeight()));
						}
					}
					//Right
					pixel.setRect(shownOffscreenView.x+shownOffscreenView.width,offscreenView.y+upOffsetForLnR,offscreenView.x+offscreenView.width-shownOffscreenView.x-shownOffscreenView.width,newImage.getHeight()-upOffsetForLnR-bottomOffsetForLnR);
					if (((int)pixel.width)>0 && ((int)pixel.height)>0) {
						//Find the rectangle to repaint in real coordinates
						p2d.setLocation(pixel.x,pixel.y+pixel.height);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						double x=p2d.x,y=p2d.y;
						p2d.setLocation(pixel.x+pixel.width,pixel.y);
						mapPane.getInversePositionTransform().transform(p2d,p2d);
						real.setRect(x,y,p2d.x-x,p2d.y-y);
						//Paint the patch
						BufferedImage patch=drawPatch(real);
						if (patch!=null) {
							//Place the patch
							Graphics2D g2=(Graphics2D) newImage.getGraphics();
							g2.drawImage(patch,newImage.getWidth()-patch.getWidth(),upOffsetForLnR,LayerPane.this);
							patch.getGraphics().dispose();
							patch.flush();
							patch=null;
						}
						if (mayHaveLabels) {
							rightreal=new Rectangle2D.Double(real.getX(),real.getY(),real.getWidth(),real.getHeight());
							rightpixel=new Rectangle((int) Math.round(pixel.getX()),(int) Math.round(pixel.getY()),(int) Math.round(pixel.getWidth()),(int) Math.round(pixel.getHeight()));
						}
					}
					pm.stop(mapPane.viewer.partialMapRedraw);
					pm.displayTime(mapPane.viewer.partialMapRedraw,mapPane.viewer.getESlateHandle(),"","ms");
					//Restore the image
					BufferedImage tmp;
					tmp=currentImage;
					currentImage=newImage;
					tmp.flush();
					tmp=null;
				}
			}
			//Fix the shownOffscreenView
			shownOffscreenView.setRect(offscreenView.x,offscreenView.y,offscreenView.width,offscreenView.height);
			if (mayHaveLabels) {
				mapPane.getLabelPane().drawPatches(offscreenView,topreal,toppixel,bottomreal,bottompixel,leftreal,leftpixel,rightreal,rightpixel);
				mapPane.getLabelPane().setOffset(shownOffscreenView.x,shownOffscreenView.y);
			}
			repaint();
			mapPane.setBusy(false);
		}
	}
	/**
	 * Clears the isZooming flag which prevents the viewer to draw the
	 * image it has for the layers.
	 */
	protected void clearZooming() {
		isZooming=false;
	}
	/**
	 * Fixes the shownOffscreenView.
	 */
	void updateShownOffscreenView() {
		shownOffscreenView.setRect(offscreenView.x,offscreenView.y,offscreenView.width,offscreenView.height);
	}
	/**
	 * Called by the parent component to inform for a change in the zoom level,
	 * e.g. when one of the zoom tools has been used
	 */
	protected void viewZoomChanged(boolean startPainting) {
		isZooming=true;
		redefineView();
		if (startPainting) {
			Rectangle2D.Double r=offscreenViewToReal();
			redrawImage(r);
			updateShownOffscreenView();
			if (mayHaveLabels) {
				if (mapPane.isRedrawEnabled())
					mapPane.getLabelPane().redrawImage(offscreenView,r);
				else
					mapPane.labelsNeedRedrawing=true;
			}
			isZooming=false;
			repaint();
		}
		if (mapPane.map!=null)
			mapPane.viewer.updateScale();
	}
	/**
	 * Called by the parent component to inform for a rotation in the view.
	 */
	protected void viewRotationChanged() {//boolean startPainting) {
		/*rotdim2.x=originalWidth;
		rotdim2.y=originalHeight;
		differenceWhenRotated(mapPane.rotation,rotdim2);
		rotatedWidth=(int) Math.round(originalWidth+rotdim2.x);
		rotatedHeight=(int) Math.round(originalHeight+rotdim2.y);*/
	}
	/**
	 * Draws a set of geographic objects on a specific Graphics context.
	 */
	public void drawObjects(IVectorLayerView layer,ArrayList geoObjects,Graphics2D g2,boolean highlight) {
		if ((geoObjects==null) || (geoObjects.size()==0) || (g2==null)) return;
		//Initialize colors
		Color normalOutline=null,normalFill=null;
		Color selectedOutline=null,selectedFill=null;
		Color highlightedOutline=null,highlightedFill=null;
		//Initialize paint properties
		//Find which colors are actually transparent to prevent drawing them
		boolean pnf,pno,psf,pso,phf,pho;
		pnf=pno=psf=pso=phf=pho=false;
		if (highlight) {
			highlightedOutline=layer.getHighlightedOutlineColor();
			highlightedFill=layer.getHighlightedFillColor();
			normalOutline=layer.getNormalOutlineColor();
			phf=highlightedFill.getAlpha()>5;
			pho=highlightedOutline.getAlpha()>5;
		} else {
			normalOutline=layer.getNormalOutlineColor();
			normalFill=layer.getNormalFillColor();
			selectedOutline=layer.getSelectedOutlineColor();
			selectedFill=layer.getSelectedFillColor();
			pnf=normalFill.getAlpha()>5;
			pno=normalOutline.getAlpha()>5;
			psf=selectedFill.getAlpha()>5;
			pso=selectedOutline.getAlpha()>5;
		}
		String label; FontMetrics fm=null;
		//(#$#$$$$$$ Change when layers get their own font
		if (mapPane.labels!=null) {
			fm=getFontMetrics(mapPane.labels.getFont());
			if (highlight)
				g2.setPaint(layer.getHighlightedOutlineColor());
		}
		//Start drawing
		if (layer instanceof IPointLayerView) {
			Icon ic;
			int radius=((IPointLayerView) layer).getCircleRadius();
			int diameter=2*radius;
			//Prepare cache images to draw more quickly the circles
			ImageIcon normal=null;
			ImageIcon selected=null;
			ImageIcon highlighted=null;
			//Disable rendering hints. They are added to the small icons.
			g2.setRenderingHints(mapPane.clearedRenderingHints);
			if (layer.getPaintMode()==IPointLayerView.PAINT_AS_CIRCLE || layer.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
				Graphics2D g2i;
				//Highlighted icon
				if (highlight) {
					BufferedImage hbi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(diameter+2,diameter+2,Transparency.TRANSLUCENT);
					g2i=(Graphics2D) hbi.getGraphics();
					g2i.setRenderingHints(mapPane.getRenderingHints());
					//g2i.setRenderingHints(g2.getRenderingHints());
					if ((((IPointLayerView) layer).isCircleFilled()) && (highlightedFill!=null)) {
						g2i.setPaint(highlightedFill);
						g2i.fillOval(1,1,diameter,diameter);
					}
					g2i.setPaint(highlightedOutline);
					g2i.drawOval(1,1,diameter,diameter);
					highlighted=new ImageIcon(hbi);
				} else {
					//Normal icon
					BufferedImage nbi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(diameter+2,diameter+2,Transparency.TRANSLUCENT);
					g2i=(Graphics2D) nbi.getGraphics();
					g2i.setRenderingHints(mapPane.getRenderingHints());
					if ((((IPointLayerView) layer).isCircleFilled()) && (normalFill!=null)) {
						g2i.setPaint(normalFill);
						g2i.fillOval(1,1,diameter,diameter);
					}
					g2i.setPaint(normalOutline);
					g2i.drawOval(1,1,diameter,diameter);
					normal=new ImageIcon(nbi);
					//Selected icon
					BufferedImage sbi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(diameter+2,diameter+2,Transparency.TRANSLUCENT);
					g2i=(Graphics2D) sbi.getGraphics();
					g2i.setRenderingHints(mapPane.getRenderingHints());
					if ((((IPointLayerView) layer).isCircleFilled()) && (selectedFill!=null)) {
						g2i.setPaint(selectedFill);
						g2i.fillOval(1,1,diameter,diameter);
					}
					g2i.setPaint(selectedOutline);
					g2i.drawOval(1,1,diameter,diameter);
					selected=new ImageIcon(sbi);
				}
			} else if (layer.getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS) {
				Graphics2D g2i;
				//Highlighted icon
				if (highlight) {
					Icon ich=((IPointLayerView) layer).getHighlightedIcon((Point) geoObjects.get(0));
					if (ich==null)
						highlighted=new ImageIcon();
					else {
						BufferedImage hbi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ich.getIconWidth(),ich.getIconHeight(),Transparency.TRANSLUCENT);
						g2i=(Graphics2D) hbi.getGraphics();
						g2i.setRenderingHints(mapPane.getRenderingHints());
						ich.paintIcon(this,g2i,0,0);
						highlighted=new ImageIcon(hbi);
					}
					if (ich!=null && ich instanceof ImageIcon && ((ImageIcon) ich).getImage()!=null)
						((ImageIcon) ich).getImage().flush();
					ich=null;
				} else {
					Icon icn=((IPointLayerView) layer).getNormalIcon((Point) geoObjects.get(0));
					Icon ics=((IPointLayerView) layer).getSelectedIcon((Point) geoObjects.get(0));
					//Normal icon
					if (icn==null)
						normal=new ImageIcon();
					else {
						BufferedImage nbi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(icn.getIconWidth(),icn.getIconHeight(),Transparency.TRANSLUCENT);
						g2i=(Graphics2D) nbi.getGraphics();
						g2i.setRenderingHints(mapPane.getRenderingHints());
						icn.paintIcon(this,g2i,0,0);
						normal=new ImageIcon(nbi);
					}
					//Selected icon
					if (ics==null)
						selected=new ImageIcon();
					else {
						BufferedImage sbi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ics.getIconWidth(),ics.getIconHeight(),Transparency.TRANSLUCENT);
						g2i=(Graphics2D) sbi.getGraphics();
						g2i.setRenderingHints(mapPane.getRenderingHints());
						ics.paintIcon(this,g2i,0,0);
						selected=new ImageIcon(sbi);
					}
					if (icn!=null && icn instanceof ImageIcon && ((ImageIcon) icn).getImage()!=null)
						((ImageIcon) icn).getImage().flush();
					if (ics!=null && ics instanceof ImageIcon && ((ImageIcon) ics).getImage()!=null)
						((ImageIcon) ics).getImage().flush();
					icn=ics=null;
				}
			}

			//Extract the transformation from the graphics context. Each point location is
			//tranformed according to the extracted transformation and the graphics context
			//paints the icon in normal scale and not in zoom, as it would happen if the
			//graphics kept the transformation.
			AffineTransform trans=g2.getTransform();
			g2.setTransform(new AffineTransform());
			Rectangle2D rd=new Rectangle2D.Double(offscreenView.x,offscreenView.y,offscreenView.width,offscreenView.height);
			mapPane.transformRect(mapPane.getInversePositionTransform(),rd);
			if (layer.getPaintMode()==IPointLayerView.PAINT_AS_CIRCLE) {
				for (int i=0;i<geoObjects.size();i++) {
					Point po=(Point) geoObjects.get(i);
					p2d.setLocation(po.getX(),po.getY());
					trans.transform(p2d,p2d);
					//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
					if (!rd.contains(po.getX(),po.getY()))
						continue;
					if (highlight) {
					   highlighted.paintIcon(this,g2,(int) (p2d.getX()-radius),(int) (p2d.getY()-radius));
					   //Highlight the label
					   if ((label=layer.getLabel(po))!=null)
						   g2.drawRect((int)p2d.getX()-1+4,(int)(p2d.getY())-fm.getHeight()/2,fm.stringWidth(label)+2,fm.getHeight());
					} else if (po.isSelected())
					   selected.paintIcon(this,g2,(int) (p2d.getX()-radius),(int) (p2d.getY()-radius));
					else
					   normal.paintIcon(this,g2,(int)(p2d.getX()-radius),(int)(p2d.getY()-radius));
				}
			} else if (layer.getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS) {
				int rxn,ryn,rxs,rys,rxh,ryh;
				rxn=ryn=rxs=rys=rxh=ryh=0;
				if (highlight) {
					rxh=(int) (highlighted.getIconWidth()/2d);
					ryh=(int) (highlighted.getIconHeight()/2d);
				} else {
					rxn=(int) (normal.getIconWidth()/2d);
					ryn=(int) (normal.getIconHeight()/2d);
					rxs=(int) (selected.getIconWidth()/2d);
					rys=(int) (selected.getIconHeight()/2d);
				}
				for (int i=0;i<geoObjects.size();i++) {
					Point po=(Point) geoObjects.get(i);
					p2d.setLocation(po.getX(),po.getY());
					trans.transform(p2d,p2d);
					//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
					if (!rd.contains(po.getX(),po.getY()))
						continue;
					if (highlight) {
//long s1=System.currentTimeMillis();
						highlighted.paintIcon(this,g2,(int) (p2d.getX()-rxh),(int) (p2d.getY()-ryh));
//totalT+=System.currentTimeMillis()-s1;
					   //Highlight the label
					   if ((label=layer.getLabel(po))!=null)
						   g2.drawRect((int)p2d.getX()-1+4,(int)(p2d.getY())-fm.getHeight()/2,fm.stringWidth(label)+2,fm.getHeight());
					} else if (po.isSelected()) {
//long s1=System.currentTimeMillis();
						selected.paintIcon(this,g2,(int) (p2d.getX()-rxs),(int) (p2d.getY()-rys));
//totalT+=System.currentTimeMillis()-s1;
					} else {
//long s1=System.currentTimeMillis();
						normal.paintIcon(this,g2,(int)(p2d.getX()-rxn),(int)(p2d.getY()-ryn));
//totalT+=System.currentTimeMillis()-s1;
					}
				}
			} else if (layer.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
				for (int i=0;i<geoObjects.size();i++) {
					Point po=(Point) geoObjects.get(i);
					p2d.setLocation(po.getX(),po.getY());
					trans.transform(p2d,p2d);
					//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
					if (!rd.contains(po.getX(),po.getY()))
						continue;
					if (highlight) {
						ic=((IPointLayerView) layer).getHighlightedIcon(po);
						if (ic==null)
							ic=highlighted;
					   //Highlight the label
					   if ((label=layer.getLabel(po))!=null)
						   g2.drawRect((int)p2d.getX()-1+4,(int)(p2d.getY())-fm.getHeight()/2,fm.stringWidth(label)+2,fm.getHeight());
					} else if (po.isSelected()) {
						ic=((IPointLayerView) layer).getSelectedIcon(po);
						if (ic==null)
							ic=selected;
					} else {
						ic=((IPointLayerView) layer).getNormalIcon(po);
						if (ic==null)
							ic=normal;
					}
					ic.paintIcon(this,g2,(int) (p2d.getX()-ic.getIconWidth()/2d),(int) (p2d.getY()-ic.getIconHeight()/2d));
				}
			}
			//Restore memory from temporarily used icons
			if (normal!=null && normal.getImage()!=null)
				normal.getImage().flush();
			if (selected!=null && selected.getImage()!=null)
				selected.getImage().flush();
			if (highlighted!=null && highlighted.getImage()!=null)
				highlighted.getImage().flush();
			//Restore the transformation in the graphics context
			g2.transform(trans);
			//Restore the rendering hints.
			g2.setRenderingHints(mapPane.getRenderingHints());
		} else if (layer instanceof IPolyLineLayerView) {
			float errT=((IPolyLineLayerView)layer).getErrorTolerance();
			if (errT<0)
				PolyLine.setErrorTolerance(mapPane.viewer.getErrorTolerance());
			else
				PolyLine.setErrorTolerance(errT);
			PolyLine p;
			Stroke stroke;
			float linewidth=(float) (((IPolyLineLayerView)layer).getLineWidth()/Math.min(Math.abs(g2.getTransform().getScaleX()),Math.abs(g2.getTransform().getScaleY())));
			g2.setRenderingHints(mapPane.getRenderingHints());
			switch (layer.getPaintMode()) {
				case IPolyLineLayerView.PAINT_AS_DASHED_LINE:
					stroke=new BasicStroke(linewidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[] {4*linewidth,2*linewidth},0);
					break;
				case IPolyLineLayerView.PAINT_AS_DOTTED_LINE:
					stroke=new BasicStroke(linewidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[] {linewidth/3f,3f*linewidth},0);
					break;
				default:
					stroke=new BasicStroke(linewidth);
			}
			if (stroke!=null)
				g2.setStroke(stroke);
			Rectangle2D rd=new Rectangle2D.Double(offscreenView.x,offscreenView.y,offscreenView.width,offscreenView.height);
			mapPane.transformRect(mapPane.getInversePositionTransform(),rd);
			if (pno || pso || pho) {
				//Paint the lines
				for (int i=0;i<geoObjects.size();i++) {
					p=(PolyLine) geoObjects.get(i);
					//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
					if (!rd.intersects(p.getBoundingMinX(),p.getBoundingMinY(),Math.max(1E-6,p.getBoundingMaxX()-p.getBoundingMinX()),Math.max(1E-6,p.getBoundingMaxY()-p.getBoundingMinY())))
						continue;
					if (highlight) {
						g2.setPaint(highlightedOutline);
//long s=System.currentTimeMillis();
						g2.draw(p);
//totalT+=System.currentTimeMillis()-s;
					   //Highlight the label
					   if ((label=layer.getLabel(p))!=null) {
							p2d.setLocation(p.getBoundingMinX()+(p.getBoundingMaxX()-p.getBoundingMinX())/2d,p.getBoundingMinY()+(p.getBoundingMaxY()-p.getBoundingMinY())/2d);
							g2.getTransform().transform(p2d,p2d);
							int sw=fm.stringWidth(label);
							//Paint the label
							Rectangle r=new Rectangle((int)p2d.x-sw/2,(int)p2d.y-fm.getHeight()/2,sw,fm.getHeight());
							mapPane.transformRect(g2.getTransform(),r);
							g2.drawRect(r.x,r.y,r.width,r.height);
					   }
					} else if (p.isSelected()) {
						g2.setPaint(selectedOutline);
//long s=System.currentTimeMillis();
						g2.draw(p);
//totalT+=System.currentTimeMillis()-s;
					} else {
						g2.setPaint(normalOutline);
//long s=System.currentTimeMillis();
						g2.draw(p);
//totalT+=System.currentTimeMillis()-s;
					}
//Point2D.Double p1=(Point2D.Double) p.getPoint(0);
//Ellipse2D.Double e=new Ellipse2D.Double(p1.x-0.1,p1.y-0.1,0.2,0.2);
//g2.fill(e);
				}
			}
		} else if (layer instanceof IPolygonLayerView) {
			float errT=((IPolygonLayerView)layer).getErrorTolerance();
			if (errT<0)
				Polygon.setErrorTolerance(mapPane.viewer.getErrorTolerance());
			else
				Polygon.setErrorTolerance(errT);
			Polygon p;
			Rectangle2D rd=new Rectangle2D.Double(offscreenView.x,offscreenView.y,offscreenView.width,offscreenView.height);
			mapPane.transformRect(mapPane.getInversePositionTransform(),rd);
			//Paint the fill of the polygons
			if (((IPolygonLayerView) layer).isPolygonFilled() && (pnf || psf || phf)/* || (!((IPolygonLayerView) layer).isPolygonFilled() && !pnf && !psf && !phf)*/) {
				//Care for transparent layers
				if (pnf || psf || phf) {
					//Not a transparent layer
					for (int i=0;i<geoObjects.size();i++) {
						p=(Polygon) geoObjects.get(i);
						//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
						if (!rd.intersects(p.getBoundingMinX(),p.getBoundingMinY(),p.getBoundingMaxX()-p.getBoundingMinX(),p.getBoundingMaxY()-p.getBoundingMinY()))
							continue;
						if (highlight) {
							if (phf) {
								g2.setPaint(highlightedFill);
//long s=System.currentTimeMillis();
								g2.fill(p);
//totalF+=System.currentTimeMillis()-s;
								Polygon.unlockIterator();
							}
						} else if (p.isSelected()) {
							if (psf) {
								g2.setPaint(selectedFill);
//long s=System.currentTimeMillis();
								g2.fill(p);
//totalF+=System.currentTimeMillis()-s;
								Polygon.unlockIterator();
							}
						} else {
							if (pnf) {
								g2.setPaint(normalFill);
//long s=System.currentTimeMillis();
								g2.fill(p);
//totalF+=System.currentTimeMillis()-s;
								Polygon.unlockIterator();
							}
						}
					}
				}
			}
			//Paint the outline of polygons
			if (pno || pso || pho) {
				if (((IPolygonLayerView)layer).getLineWidth()==1 && layer.getPaintMode()==IPolygonLayerView.PAINT_AS_STRAIGHT_LINE && (layer.getAntialiasState().equals(RenderingHints.VALUE_ANTIALIAS_OFF) || (layer.getAntialiasState().equals(RenderingHints.VALUE_ANTIALIAS_DEFAULT) && !mapPane.getAntialiasing()))) {
					if (mapPane.getAntialiasing())
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
					//In this case the drawing can speed up significantly using drawline
					AffineTransform at=g2.getTransform();
					g2.setTransform(new AffineTransform());
					g2.setStroke(new BasicStroke(1));
					for (int i=0;i<geoObjects.size();i++) {
						p=(Polygon) geoObjects.get(i);
						//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
						if (!rd.intersects(p.getBoundingMinX(),p.getBoundingMinY(),p.getBoundingMaxX()-p.getBoundingMinX(),p.getBoundingMaxY()-p.getBoundingMinY()))
							continue;
//long s=System.currentTimeMillis();
						PathIterator it=p.getPathIterator(at);
						float[] seg=new float[2]; float x1,x2,y1,y2; int type;
						it.currentSegment(seg); it.next();
						x1=seg[0]; y1=seg[1];
						if (highlight) {
							if (!pho)
								continue;
							g2.setPaint(highlightedOutline);
						} else if (p.isSelected()) {
							if (!pso)
								continue;
							g2.setPaint(selectedOutline);
						} else {
							if (!pno)
								continue;
							g2.setPaint(normalOutline);
						}
						while (!it.isDone()) {
							type=it.currentSegment(seg);
							x2=seg[0]; y2=seg[1];
							if (type!=PathIterator.SEG_MOVETO)
								g2.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
							x1=x2; y1=y2;
							it.next();
						}
//totalT+=System.currentTimeMillis()-s;
					}
					//Restore transformation
					g2.setTransform(at);
					if (mapPane.getAntialiasing())
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				} else {
					//Normal paint mode using Graphics2D.draw(Shape)
					Stroke stroke;
					float linewidth=(float) (((IPolygonLayerView)layer).getLineWidth()/Math.min(Math.abs(g2.getTransform().getScaleX()),Math.abs(g2.getTransform().getScaleY())));
					g2.setRenderingHints(mapPane.getRenderingHints());
					switch (layer.getPaintMode()) {
						case IPolygonLayerView.PAINT_AS_DASHED_LINE:
							stroke=new BasicStroke(linewidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[] {4*((IPolygonLayerView)layer).getLineWidth(),2*((IPolygonLayerView)layer).getLineWidth()},0);
							break;
						case IPolygonLayerView.PAINT_AS_DOTTED_LINE:
							stroke=new BasicStroke(linewidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[] {((IPolygonLayerView)layer).getLineWidth()/3f,3*((IPolygonLayerView)layer).getLineWidth()},0);
							break;
						default:
							stroke=new BasicStroke(linewidth);
					}
					if (stroke!=null)
						g2.setStroke(stroke);
					for (int i=0;i<geoObjects.size();i++) {
						p=(Polygon) geoObjects.get(i);
						//Cancel drawing if it will happen far from the view, which may cause a bad path exception.
						if (!rd.intersects(p.getBoundingMinX(),p.getBoundingMinY(),p.getBoundingMaxX()-p.getBoundingMinX(),p.getBoundingMaxY()-p.getBoundingMinY()))
							continue;
						if (highlight) {
							if (pho) {
								g2.setPaint(highlightedOutline);
//long s=System.currentTimeMillis();
								g2.draw(p);
//totalT+=System.currentTimeMillis()-s;
							   //Highlight the label
							   if ((label=layer.getLabel(p))!=null) {
									p2d.setLocation(p.getBoundingMinX()+(p.getBoundingMaxX()-p.getBoundingMinX())/2d,p.getBoundingMinY()+(p.getBoundingMaxY()-p.getBoundingMinY())/2d);
									g2.getTransform().transform(p2d,p2d);
									int sw=fm.stringWidth(label);
									//Paint the label
									Rectangle r=new Rectangle((int)p2d.x-sw/2,(int)p2d.y-fm.getHeight()/2,sw,fm.getHeight());
									mapPane.transformRect(g2.getTransform(),r);
									g2.drawRect(r.x,r.y,r.width,r.height);
							   }
							}
						} else if (p.isSelected()) {
							if (pso) {
								g2.setPaint(selectedOutline);
//long s=System.currentTimeMillis();
								g2.draw(p);
//totalT+=System.currentTimeMillis()-s;
							}
						} else {
							if (pno) {
								g2.setPaint(normalOutline);
//long s=System.currentTimeMillis();
								g2.draw(p);
//totalT+=System.currentTimeMillis()-s;
							}
						}
						Polygon.unlockIterator();
					}
				}
			}
		}
	}

	/**
	 * This is the width in pixels of the view when viewed in 100% view.
	 */
	int getOriginalWidth() {
		return originalWidth;
	}
	/**
	 * This is the height in pixels of the view when viewed in 100% view.
	 */
	int getOriginalHeight() {
		return originalHeight;
	}
	/**
	 * @return <em>True</em> when the map fits in the panel vertically.
	 */
	protected boolean fitsVertically() {
		return (currentView.y==0 && currentView.height<=getHeight());
	}
	/**
	 * @return <em>True</em> when the map fits in the panel horizontally.
	 */
	protected boolean fitsHorizontally() {
		return (currentView.x==0 && currentView.width<=getWidth());
	}
	/**
	 * Called by the map pane to indicate that it has been resized, so the view must
	 * be updated.
	 */
	void redefineView() {
		maxViewWidth=(int) Math.round(originalWidth*mapPane.getZoom());
		maxViewHeight=(int) Math.round(originalHeight*mapPane.getZoom());

		mapPane.clearTransformations();

		if ((getHeight()>0 && maxViewHeight>0) && (maxViewWidth<getWidth() || maxViewHeight<getHeight())) {
			if ((((double)maxViewWidth)/maxViewHeight)>=((double)getWidth())/getHeight()) {
				if (maxViewHeight<getHeight() && originalHeight!=0)
					//Set the zoom and let the zoom proc decide whether it will accept the value
					//The zoom is set through the slider because its changelistener will call
					//setZoom again and produce a stack overflow.
					if (mapPane.viewer.zoom!=null)
						mapPane.viewer.zoom.sldZoom.setValue(1+(int) Math.ceil(((double)getHeight())/originalHeight*100));
			} else {
				if (maxViewWidth<getWidth() && originalWidth!=0)
					//Set the zoom and let the zoom proc decide whether it will accept the value
					//The zoom is set through the slider because its changelistener will call
					//setZoom again and produce a stack overflow.
					if (mapPane.viewer.zoom!=null)
						mapPane.viewer.zoom.sldZoom.setValue(1+(int) Math.ceil(((double)getWidth())/originalWidth*100));
			}
			//Recalculate the maximum in case it has changed
			maxViewWidth=(int) Math.round(originalWidth*mapPane.getZoom());
			maxViewHeight=(int) Math.round(originalHeight*mapPane.getZoom());
		}
		//Keep the current view rectangle in pixels.
		//The view is the visible area of the map pane. It is never bigger than the original
		//size itself. We keep track of it to understand when there is a change in the viewable area
		//(rezize or zoom etc) and rebuild the layers image.

		AffineTransform transform=mapPane.getTransform();
		if (transform.getTranslateX()>0)
			tl.x=(int) transform.getTranslateX();
		else
			tl.x=0;

		if (transform.getTranslateY()>0)
			tl.y=(int) transform.getTranslateY();
		else
			tl.y=0;

		if (tl.x+maxViewWidth>getWidth())
			br.x=tl.x+getWidth();
		else
			br.x=tl.x+maxViewWidth;

		if (tl.y+maxViewHeight>getHeight())
			br.y=tl.y+getHeight();
		else
			br.y=tl.y+maxViewHeight;

		mapPane.getInverseTransform().transform(tl,tl);
		mapPane.getInverseTransform().transform(br,br);


		currentView=new Rectangle((int) Math.round(tl.x),(int) Math.round(tl.y),(int) Math.round(br.x-tl.x),(int) Math.round(br.y-tl.y));

		//Math rounding
		if (currentView.x<0) {currentView.width-=currentView.x; currentView.x=0;}
		if (currentView.y<0) {currentView.height-=currentView.y; currentView.y=0;}

		//What is the new buffer view?
		if (currentView.width==maxViewWidth) { //The whole region fits in the area X-axis
			offscreenView.x=0;
			offscreenView.width=maxViewWidth;
		} else { //The whole region does not fit in the area X-axis
			offscreenView.x=(int) Math.round(currentView.x+Math.max(mapPane.getTransform().getTranslateX(),-BUFFER_EXT*getWidth()));
			offscreenView.width=(int) Math.round(Math.min(maxViewWidth,currentView.x+currentView.width+BUFFER_EXT*getWidth())-offscreenView.x);
		}
		if (currentView.height==maxViewHeight) { //The whole region fits in the area Y-axis
			offscreenView.y=0;
			offscreenView.height=maxViewHeight;
		} else { //The whole region does not fit in the area Y-axis
			offscreenView.y=currentView.y+(int) Math.round(Math.max(mapPane.getTransform().getTranslateY(),-BUFFER_EXT*getHeight()));
			offscreenView.height=(int) Math.round(Math.min(maxViewHeight,currentView.y+currentView.height+BUFFER_EXT*getHeight())-offscreenView.y);
		}
		if (notShownYet && offscreenView.width>0 && offscreenView.height>0) {
			notShownYet=false;
			if (pendingLayers!=null) {
				setLayers(pendingLayers);
				pendingLayers=null;
			}
		}

	}
	/**
	 * Helper method which calculates the new size (width-height given as x-y in the point)
	 * when original rectangle with the given size is rotated by <code>rotation</code> radians.
	 */
	void differenceWhenRotated(double rotation,Point2D.Double original) {
		double w,h,tLx,tLy,bRx,bRy,tRx,tRy,bLx,bLy; double cos,sin;
		w=original.x;
		h=original.y;
		//The rotated image needs a greater rectangle. Calculate it.
		cos=Math.cos(rotation);
		sin=Math.sin(rotation);
		tLx=(w/2*(1-cos)+h/2*sin);
		tLy=(h/2*(1-cos)-w/2*sin);
		bRx=(cos*w-sin*h+w/2*(1-cos)+h/2*sin);
		bRy=(sin*w+cos*h+h/2*(1-cos)-w/2*sin);
		tRx=(cos*w+w/2*(1-cos)+h/2*sin);
		tRy=(sin*w+h/2*(1-cos)-w/2*sin);
		bLx=(-sin*h+w/2*(1-cos)+h/2*sin);
		bLy=(cos*h+h/2*(1-cos)-w/2*sin);
		//The bounding rectangle of the rotated view has the following increase in width and height
		original.x=Math.max(Math.max(Math.max(tLx,tRx),bLx),bRx)-Math.min(Math.min(Math.min(tLx,tRx),bLx),bRx)-original.x;
		original.y=Math.max(Math.max(Math.max(tLy,tRy),bLy),bRy)-Math.min(Math.min(Math.min(tLy,tRy),bLy),bRy)-original.y;
	}
	/**
	 * Checks if the given point (in pixel coordinates) is inside the visible area of the map.
	 */
	protected boolean insideVisibleArea(int x,int y) {
		tl.x=x; tl.y=y;
		mapPane.getInverseTransform().transform(tl,tl);
		return (background==null) || (currentView.contains(tl));
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			getComponents()[i].setFont(f);
	}

	public void paintComponent(Graphics g) {
		if (mapPane.map==null) {
			super.paintComponent(g);
			return;
		}

		if (shownOffscreenView.x!=lastPaintOffscreenX || shownOffscreenView.y!=lastPaintOffscreenY) {
			//A "cracking" effect will be produced if the images are not painted entirely when there is a change in the
			//transformations because a small portion of the image will be painted first and then the rest. Make
			//the clip rectangle all the area.
//            g.setClip(getVisibleRect());
		}
		lastPaintOffscreenX=shownOffscreenView.x;
		lastPaintOffscreenY=shownOffscreenView.y;
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHints(mapPane.getRenderingHints());
		//If the current view is smaller than the component view, clipping must occur
		/*if (background!=null && (currentView.width!=getWidth() || currentView.height!=getHeight())) {
			if (currentView.width!=getWidth()) {
				clipRect.x=(int) Math.round(mapPane.getTransform().getTranslateX());
				clipRect.width=currentView.width;
			} else {
				clipRect.x=0;
				clipRect.width=getWidth();
			}
			if (currentView.height!=getHeight()) {
				clipRect.y=(int) Math.round(mapPane.getTransform().getTranslateY());
				clipRect.height=currentView.height;
			} else {
				clipRect.y=0;
				clipRect.height=getHeight();
			}

			g2.setClip(clipRect);
		}*/
		if (background==null || mapPane.getZoom()>4999) {
			g2.setPaint(mapPane.viewer.getMapBackground());
			Rectangle r=getVisibleRect();
			g2.fillRect(r.x,r.y,r.width,r.height);
		}
		//g2.transform(mapPane.getTransform());
//System.out.println("Clip "+g2.getClip());
		if (background!=null && mapPane.getZoom()<5000) {
			//g2.rotate(mapPane.rotation,maxViewWidth/2d,maxViewHeight/2d);
			if (mapPane.getAntialiasing())
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			AffineTransform at=new AffineTransform(mapPane.getTransform());
			if (mapPane.getZoom()!=1)
				at.scale(mapPane.getZoom(),mapPane.getZoom());
			g2.drawImage(background.getImage(),at,null);
		}
		java.awt.geom.Ellipse2D.Double ellipse;
		//Map is null after disconnection
		if (mapPane.map!=null) {
			if (currentImage!=null && !isZooming) {
				if (mapPane.getAntialiasing())
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
				//Scaling and painting an image is a very expensive operation. Extract the portion
				//of the image that covers the clip shape and paint this only.
				AffineTransform at=new AffineTransform(mapPane.getTransform());
				at.translate(shownOffscreenView.x,shownOffscreenView.y);
				Rectangle clip=g.getClip().getBounds();
				if (clip!=null) {
					//Calculate which portion of the image to paint
					try {
						mapPane.transformRect(at.createInverse(),clip);
					} catch(NoninvertibleTransformException e) {
						e.printStackTrace();
					}
					try {
						BufferedImage patch=(currentImage.getSubimage(clip.x,clip.y,clip.width,clip.height));
						at.translate(clip.x,clip.y);
						g2.drawImage(patch,at,null);
					} catch(Exception ex) {
						//Can happen if the clip is outside the area of the image
						g2.drawImage(currentImage,at,null);
					}
				} else
					g2.drawImage(currentImage,at,null);
				if (mapPane.getAntialiasing())
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			}
			double zz=Math.min(Math.abs(mapPane.getPositionTransform().getScaleX()),Math.abs(mapPane.getPositionTransform().getScaleY()));
			BasicStroke one=new BasicStroke((float)(1/zz));
			BasicStroke three=new BasicStroke((float)(3/zz));
			ILayerView[] layers=mapPane.map.getActiveRegionView().getLayerViews();
			g2.setRenderingHints(mapPane.getRenderingHints());
			g2.transform(mapPane.getTransform());
			for (int i=0;i<layers.length;i++) {
				if (layers[i] instanceof IVectorLayerView && layers[i].isVisible() && layers[i].getActiveGeographicObject()!=null) {
					IVectorLayerView vlv=(IVectorLayerView) layers[i];
					if (vlv instanceof IPointLayerView) {
						Point p=(Point) vlv.getActiveGeographicObject();
						p2d.setLocation(p.getX(),p.getY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						if (!offscreenView.contains(p2d))
							continue;
						ellipse=new java.awt.geom.Ellipse2D.Double(p2d.getX()-4,p2d.getY()-4,activeCircle,activeCircle);
						g2.setStroke(new BasicStroke(3));
						g2.setPaint(vlv.getHighlightedFillColor());//new Color(vlv.getHighlightedFillColor().getRed(),vlv.getHighlightedFillColor().getGreen(),vlv.getHighlightedFillColor().getBlue(),176));
						g2.draw(ellipse);
						g2.setStroke(new BasicStroke(1));
						g2.setPaint(vlv.getHighlightedOutlineColor());//new Color(vlv.getHighlightedOutlineColor().getRed(),vlv.getHighlightedOutlineColor().getGreen(),vlv.getHighlightedOutlineColor().getBlue(),192));
						g2.draw(ellipse);
					} else if (vlv instanceof IPolyLineLayerView) {
						IVectorGeographicObject p=(IVectorGeographicObject) layers[i].getActiveGeographicObject();
						g2.setPaint(vlv.getHighlightedOutlineColor());
						g2.transform(mapPane.getPositionTransform());
						//Check if the shape will be drawn inside the area. Otherwise a bad path may be thrown.
						p2d.setLocation(p.getBoundingMaxX(),p.getBoundingMaxY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						if (p2d.x<offscreenView.x || p2d.y<offscreenView.y) {
							g2.transform(mapPane.getInversePositionTransform());
							continue;
						}
						p2d.setLocation(p.getBoundingMinX(),p.getBoundingMinY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						if (p2d.x>offscreenView.x+offscreenView.width || p2d.y>offscreenView.y+offscreenView.height) {
							g2.transform(mapPane.getInversePositionTransform());
							continue;
						}
						//Create the stroke
						g2.setStroke(three);
						g2.setPaint(vlv.getHighlightedFillColor());//new Color(highlightedFill.getRed(),highlightedFill.getGreen(),highlightedFill.getBlue(),176));
						g2.draw(p);
						g2.setStroke(one);
						g2.setPaint(vlv.getHighlightedOutlineColor());//new Color(highlightedOutline.getRed(),highlightedOutline.getGreen(),highlightedOutline.getBlue(),192));
						g2.draw(p);
						g2.transform(mapPane.getInversePositionTransform());
					} else if (vlv instanceof IPolygonLayerView) {
						IVectorGeographicObject p=(IVectorGeographicObject) layers[i].getActiveGeographicObject();
						g2.transform(mapPane.getPositionTransform());
						//Check if the shape will be drawn inside the area. Otherwise a bad path may be thrown.
						p2d.setLocation(p.getBoundingMaxX(),p.getBoundingMaxY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						if (p2d.x<offscreenView.x || p2d.y<offscreenView.y) {
							g2.transform(mapPane.getInversePositionTransform());
							continue;
						}
						p2d.setLocation(p.getBoundingMinX(),p.getBoundingMinY());
						mapPane.getPositionTransform().transform(p2d,p2d);
						if (p2d.x>offscreenView.x+offscreenView.width || p2d.y>offscreenView.y+offscreenView.height) {
							g2.transform(mapPane.getInversePositionTransform());
							continue;
						}
						g2.setStroke(three);
						g2.setPaint(vlv.getHighlightedFillColor());
						g2.draw(p);
						g2.setStroke(one);
						g2.setPaint(vlv.getHighlightedOutlineColor());
						g2.draw(p);
						g2.transform(mapPane.getInversePositionTransform());
					}
				}
			}
			//Draw grid
			Rectangle2D.Double rc=(Rectangle2D.Double) mapPane.map.getActiveRegionView().getBoundingRect();
			if (mapPane.viewer.isGridVisible() && rc!=null) {
				g2.setRenderingHints(mapPane.clearedRenderingHints);
				Line2D.Double ln=new Line2D.Double();
				g2.transform(mapPane.getPositionTransform());
				g2.setColor(mapPane.viewer.getGridColor());
				float min=(float) Math.min(mapPane.getPositionTransform().getScaleX(),mapPane.getPositionTransform().getScaleY());
				if (min<0)
					g2.setStroke(new BasicStroke(1f/Math.abs(min)));
				else
					g2.setStroke(new BasicStroke(1f/min));

				min=(float) Math.min(rc.x,rc.y);
				double max=Math.max(rc.x+rc.width,rc.y+rc.height);
				for (double i=min;i<max;i=i+mapPane.viewer.getGridStep()) {
					ln.setLine(i,rc.y,i,rc.y+rc.height);
					g2.draw(ln);
					ln.setLine(rc.x,i,rc.x+rc.width,i);
					g2.draw(ln);
				}
			}
		}

		g2.transform(mapPane.getInverseTransform());
		super.paintComponent(g2);
	}

	protected final int NORMAL=0;
	protected final int SELECTED=1;
	protected final int HIGHLIGHTED=2;

	final int activeCircle=9;
	//Listener management variables
	ArrayList ml;

	//Variable declaration
	/**
	 * The parent map pane.
	 */
	private MapPane mapPane;
	/**
	 * The background image.
	 */
	IMapBackground background;
	/**
	 * The background image width and height.
	 */
	private int originalWidth,originalHeight;
	/**
	 * The background image width and height transformed by the view
	 */
	private int maxViewWidth,maxViewHeight;
	/**
	 * The image of the region viewed.
	 */
	BufferedImage currentImage;
	/**
	 * The current view in component coordinates (pixels).
	 */
	Rectangle currentView;
	/**
	 * The current offscreen buffer view in component coordinates (pixels).
	 */
	Rectangle offscreenView;
	/**
	 * The offscreen buffer shown view in component coordinates (pixels).
	 */
	Rectangle shownOffscreenView;
	//Reusable points
	private Point2D.Double tl;
	private Point2D.Double br;
	private Point2D.Double p2d;
	/**
	 * The viewable area is extended by this percent in all directions to build the offscreen buffer.
	 */
	private double BUFFER_EXT=0.1d; //10%
	private boolean notShownYet;
	/**
	 * Oh, dear! This damn Java and the invalid state of components before they are rendered for
	 * the first time! I keep this, and when I realize that at last I am valid, I add the layers.
	 */
	ILayerView[] pendingLayers;
	//Cache reusable objects.
	private double[] reused;
	/**
	 * Prevents painting the invalid layers image while zooming
	 */
	boolean isZooming;
	/**
	 * Tells if the currently showing layers may possibly have labels
	 */
	private boolean mayHaveLabels;
	/**
	 * Used to see if there is any change in the transformation since the last repaint.
	 * If there is a change the whole pictures are painted to ensure that the painting is solid and not partial.
	 */
	private int lastPaintOffscreenX;
	/**
	 * Used to see if there is any change in the transformation since the last repaint.
	 * If there is a change the whole pictures are painted to ensure that the painting is solid and not partial.
	 */
	private int lastPaintOffscreenY;
	//Variable initialization
	{
		notShownYet=true;
		currentView=new Rectangle();
		offscreenView=new Rectangle();
		shownOffscreenView=new Rectangle();
		tl=new Point2D.Double();
		br=new Point2D.Double();
		p2d=new Point2D.Double();
		reused=new double[2];
		isZooming=false;
	}

}
