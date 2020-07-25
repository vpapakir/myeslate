package gr.cti.eslate.mapViewer;

import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IVectorLayerView;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 * A pane where the GeographicObject labels are drawn.
 * It is passive, no action can be made on it.
 * @author Giorgos Vasiliou
 * @version 1.0, 10-Sep-2002
 */

class LabelPane extends JPanel implements TransparentMouseInput {
	/**
	 * Constructs a LabelPane.
	 */
	protected LabelPane(MapPane mapPane) {
		super();
		this.mapPane=mapPane;
		setOpaque(false);
		p2d=new Point2D.Double();
		fm=getFontMetrics(getFont());
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
	public java.util.List removeAllListeners() {
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
	 * Clears the image and releases all resources.
	 */
	protected void clear() {
		if (image!=null) {
			if (image.getGraphics()!=null)
				image.getGraphics().dispose();
			image.flush();
			image=null;
			repaint();
		}
	}
	/**
	 * Redraws all the labels. It calculates the Rectangles by itself.
	 */
	protected void redrawImage() {
		Rectangle offscreenView=mapPane.layers.offscreenView;
		redrawImage(offscreenView,mapPane.layers.offscreenViewToReal());
	}
	/**
	 * Redraws all the labels.
	 * @param   pixel  The pixel coordinates of the area that should be redrawn.
	 * @param   coords  The real coordinates of the area that should be redrawn.
	 */
	protected void redrawImage(Rectangle offscreenView,Rectangle2D.Double coords) {
		//When there are no layers clean memory and return
		if (mapPane.map.getActiveRegionView().getLayerViews().length==0) {
			if (image!=null) {
				//Clean previous image
				image.getGraphics().dispose();
				image.flush();
				image=null;
			}
		} else {
			PerformanceManager pm=PerformanceManager.getPerformanceManager();
			pm.init(mapPane.viewer.fullLabelRedraw);
			//Keep the same image and avoid destroying and recreating when image is 25% or less larger
			if (image==null || (image.getWidth()<offscreenView.width || image.getHeight()<offscreenView.height) || (0.75f*image.getWidth()>offscreenView.width || 0.75f*image.getHeight()>offscreenView.height)) {
				//We cannot reuse the current buffer image. A new one has to be created.
				//First clean the old one.
				if (image!=null) {
					image.getGraphics().dispose();
					image.flush();
				}
				image=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(offscreenView.width,offscreenView.height,Transparency.TRANSLUCENT);
				((Graphics2D)image.getGraphics()).setRenderingHints(mapPane.getRenderingHints());
			} else {
				//Clean previous image
				Graphics g=image.getGraphics();
				((Graphics2D) g).setBackground(new Color(0,0,0,0));
				g.setClip(null);
				g.clearRect(0,0,image.getWidth(),image.getHeight());
				//If the buffer image is larger than needed, we keep it and clip its rectangle
				g.clipRect(0,0,offscreenView.width,offscreenView.height);
			}
			drawPatch(image,coords,null);
			setOffset(mapPane.layers.shownOffscreenView.x,mapPane.layers.shownOffscreenView.y);
			pm.stop(mapPane.viewer.fullLabelRedraw);
			pm.displayTime(mapPane.viewer.fullLabelRedraw,mapPane.viewer.getESlateHandle(),"","ms");
		}
	}
	/**
	 * Redraws all the labels in the given rectangle.
	 * @param   bufferImage The image on which the labels are drawn.
	 * @param   coords  The real coordinates of the area that should be redrawn.
	 * @param   pixel   The pixel coordinates of the area that should be redrawn.
	 */
	private void drawPatch(BufferedImage bufferImage,Rectangle2D.Double coords,Rectangle pixel) {
		Graphics2D g2=(Graphics2D) bufferImage.getGraphics();
		AffineTransform postrans=new AffineTransform(mapPane.getPositionTransform());
		//Translate the context to the patch position
		Rectangle2D r=mapPane.map.getActiveRegionView().getBoundingRect();
		if (pixel!=null) {
			Rectangle o=mapPane.layers.offscreenView;
			g2.clipRect(-o.x+pixel.x,-o.y+pixel.y,pixel.width,pixel.height);
			Rectangle2D c=new Rectangle2D.Double(o.x,o.y,o.width,o.height);
			mapPane.transformRect(mapPane.getInversePositionTransform(),c);
			postrans.translate(-c.getX()+r.getX(),-c.getY()-c.getHeight()+r.getY()+r.getHeight());
		} else
			postrans.translate(-coords.getX()+r.getX(),-coords.getY()-coords.getHeight()+r.getY()+r.getHeight());
		//All the objects that reside into the y-axis stripe defined by coords
		//must be checked, because we don't know how many of them have their
		//label intersect with the LabelPane.
		coords.width+=(coords.x-mapPane.map.getActiveRegionView().getBoundingRect().getX()); coords.x=mapPane.map.getActiveRegionView().getBoundingRect().getX();
		ILayerView[] layers=mapPane.map.getActiveRegionView().getLayerViews();
		for (int i=0;i<layers.length;i++) {
			if (layers[i].isVisible() && layers[i].mayHaveLabels()) {
				g2.setFont(getFont());
				if (layers[i] instanceof IVectorLayerView) {
					ArrayList obj=layers[i].getGeographicObjects(coords,true);
					String label=null;
					int labelX=0,labelY=0;
					if (layers[i] instanceof IPointLayerView) { //Point layers
						for (int j=obj.size()-1;j>-1;j--) {
							gr.cti.eslate.mapModel.geom.Point p=(gr.cti.eslate.mapModel.geom.Point)obj.get(j);
							if ((label=layers[i].getLabel((GeographicObject)obj.get(j)))!=null) {
								try {
									//********########
									//When changing this, change drawObjects-highlight also
									p2d.setLocation(p.getX(),p.getY());
									postrans.transform(p2d,p2d);
									labelX=(int) (p2d.getX()+4);
									labelY=(int) (p2d.getY());
									//Paint the label
									g2.setPaint(DEFAULT_LABEL_OUTLINE);
									g2.fillRect(labelX,labelY-fm.getHeight()/2,fm.stringWidth(label),fm.getHeight());
									g2.setPaint(DEFAULT_LABEL_FILL);
									g2.drawString(label,labelX,labelY+fm.getHeight()/2-fm.getDescent());
								} catch(Throwable t) {/*Bad path exceptions may occur on very small scale maps where objects are drawn far away from the image*/}
							}
						}
					} else { //PolyLine and Polygon layers
						for (int j=obj.size()-1;j>-1;j--) {
							IVectorGeographicObject p=(IVectorGeographicObject)obj.get(j);
							//Paint the label
							if ((label=layers[i].getLabel(p))!=null) {
								try {
									int sw=fm.stringWidth(label);
									p2d.setLocation(p.getBoundingMinX()+(p.getBoundingMaxX()-p.getBoundingMinX())/2d,p.getBoundingMinY()+(p.getBoundingMaxY()-p.getBoundingMinY())/2d);
									postrans.transform(p2d,p2d);
									labelX=(int) (p2d.getX());
									labelY=(int) (p2d.getY());
									//Paint the label
									g2.setPaint(DEFAULT_LABEL_OUTLINE);
									g2.fillRect(labelX-sw/2,labelY-fm.getHeight()/2,sw,fm.getHeight());
									g2.setPaint(DEFAULT_LABEL_FILL);
									g2.drawString(label,labelX-sw/2,labelY+fm.getHeight()/2-fm.getDescent());
								} catch(Throwable t) {/*Bad path exceptions may occur on very small scale maps where objects are drawn far away from the image*/}
							}
						}
					}
				}
			}
		}
		g2.setClip(null);
	}
	/**
	 * Draws the given patches, reusing a part of the existing image if possible.
	 * @param topPatchReal      The top patch real coordinates rectangle. If null, no top patch is drawn.
	 * @param bottomPatchReal   The bottom patch real coordinates rectangle. If null, no bottom patch is drawn.
	 * @param leftPatchReal     The left patch real coordinates rectangle. If null, no left patch is drawn.
	 * @param rightPatchReal    The right patch real coordinates rectangle. If null, no right patch is drawn.
	 */
	protected void drawPatches(Rectangle offscreenView,Rectangle2D.Double topPatchReal,Rectangle topPatchPixel,Rectangle2D.Double bottomPatchReal,Rectangle bottomPatchPixel,Rectangle2D.Double leftPatchReal,Rectangle leftPatchPixel,Rectangle2D.Double rightPatchReal,Rectangle rightPatchPixel) {
		if (mapPane.map.getActiveRegionView().getLayerViews().length==0)
			return;
		PerformanceManager pm=PerformanceManager.getPerformanceManager();
		pm.init(mapPane.viewer.partialLabelRedraw);
		BufferedImage newImage=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(offscreenView.width,offscreenView.height,Transparency.TRANSLUCENT);
		Rectangle valid=new Rectangle(mapPane.layers.offscreenView);
		if (topPatchReal!=null && topPatchPixel!=null) {
			drawPatch(newImage,topPatchReal,topPatchPixel);
			valid.height-=topPatchPixel.height; valid.y=topPatchPixel.y+topPatchPixel.height;
		}
		if (bottomPatchReal!=null && bottomPatchPixel!=null) {
			drawPatch(newImage,bottomPatchReal,bottomPatchPixel);
			valid.height-=bottomPatchPixel.height;
		}
		if (leftPatchReal!=null && leftPatchPixel!=null) {
			drawPatch(newImage,leftPatchReal,leftPatchPixel);
			valid.width-=leftPatchPixel.width; valid.x=leftPatchPixel.x+leftPatchPixel.width;
		}
		if (rightPatchReal!=null && rightPatchPixel!=null) {
			drawPatch(newImage,rightPatchReal,rightPatchPixel);
			valid.width-=rightPatchPixel.width;
		}
		//Paint the old image on the new
		Graphics2D g2=(Graphics2D) newImage.getGraphics();
		Rectangle o=mapPane.layers.offscreenView;
		g2.clipRect(-o.x+valid.x,-o.y+valid.y,valid.width,valid.height);
		g2.drawImage(image,offsetX-o.x,offsetY-o.y,null);
		image.getGraphics().dispose();
		image.flush();
		image=newImage;
		pm.stop(mapPane.viewer.partialLabelRedraw);
		pm.displayTime(mapPane.viewer.partialLabelRedraw,mapPane.viewer.getESlateHandle(),"","ms");
	}
	/**
	 * Sets the offset where the image should be painted.
	 */
	protected void setOffset(int x,int y) {
		offsetX=x;
		offsetY=y;
	}
	/**
	 * Paints the component.
	 */
	public void paintComponent(Graphics g) {
		if (image!=null && !mapPane.layers.isZooming) {
			Graphics2D g2=(Graphics2D) g;
			AffineTransform at=new AffineTransform(mapPane.getTransform());
			at.translate(offsetX,offsetY);
			g2.drawImage(image,at,null);
		}
	}
	/**
	 * Buffering image that has the labels painted.
	 */
	private BufferedImage image;
	/**
	 * The hosting component.
	 */
	private MapPane mapPane;
	/**
	 * The offset from 0 in x-axis where the image should be painted in component space.
	 */
	private int offsetX;
	/**
	 * The offset from 0 in y-axis where the image should be painted in component space.
	 */
	private int offsetY;
	/**
	 * Spare object.
	 */
	private Point2D.Double p2d;
	/**
	 * Default color.
	 */
	private final Color DEFAULT_LABEL_OUTLINE=new Color(0,0,0,128);
	/**
	 * Default color.
	 */
	private final Color DEFAULT_LABEL_FILL=new Color(216,216,216);
	/**
	 * FontMetrics of the default font.
	 */
	private FontMetrics fm;
	/**
	 * MouseInput Listener list.
	 */
	private ArrayList ml;
}