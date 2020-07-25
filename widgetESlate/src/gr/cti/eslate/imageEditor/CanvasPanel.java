package gr.cti.eslate.imageEditor;

import gr.cti.typeArray.IntBaseArray;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CanvasPanel extends JPanel {
	BufferedImage canvasImage, dragImage, backupPasteImage;

	double scalingFactor;

	Dimension canvasSize;

	AffineTransform worldToLocal, localToWorld;

	AffineTransform originalToDragImage, existingTransform;

	int xLoc=0, yLoc=0;

	int dragX, dragY;

	Point prevPoint;

	Point grabPoint;

	Color selectionColor, drawColor, backColor;

	Color whiteColor=Color.white;

	Shape selectedShape, initialShape, flatShape;

	boolean wand=false;

	Rectangle offLineRect, previousRect=new Rectangle(0,0,0,0);

	boolean offLine, offRect, offEllipse, offSelect;

	IntBaseArray xPoints, yPoints;

	BasicStroke normalStroke=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0);

	int strokeWidth;

	boolean existsSelection, isDragging, dragIcon, paste;

	// boolean firstDrag = true;
	ImageIcon draggedIcon;

	Color transparentColor=new Color(255,255,255,0);

	Image pattern=loadImageIcon("Images/transPattern.gif"," ").getImage();

	BufferedImage im=Utilities.makeBufferedImage(pattern);

	TexturePaint tp=new TexturePaint(im,new Rectangle2D.Double(0,0,10,10));

	boolean rectSelection=true;

	public void setTransform(AffineTransform f) {
		worldToLocal=f;
		try {
			localToWorld=f.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	public void destroyImages() {
		if (canvasImage != null)
			canvasImage.flush();
		if (dragImage != null)
			dragImage.flush();
		if (backupPasteImage != null)
			backupPasteImage.flush();
		if (im != null)
			im.flush();
		canvasImage=null;
		dragImage=null;
		backupPasteImage=null;
		im=null;
		draggedIcon=null;
	}

	public CanvasPanel(double scalingFactor) {
		this.scalingFactor=scalingFactor;
		setTransform(AffineTransform.getScaleInstance(scalingFactor,scalingFactor));
		xPoints=new IntBaseArray();
		yPoints=new IntBaseArray();
		strokeWidth=1;

	}

	public void setScalingFactor(double factor) {
		scalingFactor=factor;
		adjustCanvasSize();
		setTransform(AffineTransform.getScaleInstance(scalingFactor,scalingFactor));
	}

	public double getScalingFactor() {
		return scalingFactor;
	}

	public void setCanvasImage(BufferedImage image) {
		if (image == null)
			return;
		canvasImage=image;
		adjustCanvasSize();
		repaint();
	}

	public boolean intoImage(Point p) {
		int x=toOrigImCoords(p.x);
		int y=toOrigImCoords(p.y);
		if (x >= 0 && y >= 0 && x < canvasImage.getWidth() && y < canvasImage.getHeight())
			return true;
		return false;
	}

	public Point getRealImagePoint(Point p) {
		int x=toOrigImCoords(p.x);
		int y=toOrigImCoords(p.y);
		return new Point(x,y);
	}

	public Point getViewImagePoint(Point p) {
		int x=toTransImCoords(p.x);
		int y=toTransImCoords(p.y);
		return new Point(x,y);
	}

	public void adjustCanvasSize() {
		canvasSize=new Dimension(toTransImCoords(canvasImage.getWidth()),toTransImCoords(canvasImage.getHeight()));
		setPreferredSize(canvasSize);
		setMinimumSize(canvasSize);
		setMaximumSize(canvasSize);
	}

	public Dimension getCanvasSize() {
		return canvasSize;
	}

	private double srcPoints[]=new double[4];

	private double destPoints[]=new double[4];

	public void drawContPoints(int x,int y,Color color) {
		Graphics2D g=(Graphics2D) canvasImage.getGraphics();
		g.setColor(color);
		g.setStroke(normalStroke);
		if (prevPoint == null)
			prevPoint=new Point(x,y);

		int prevx=toOrigImCoords(prevPoint.x);
		int prevy=toOrigImCoords(prevPoint.y);
		int xi=toOrigImCoords(x);
		int yi=toOrigImCoords(y);

		if (color.getAlpha() == 0)
			g.setComposite(AlphaComposite.Clear);

		g.drawLine(prevx,prevy,xi,yi);

		srcPoints[0]=prevx;
		srcPoints[1]=prevy;
		srcPoints[2]=xi;
		srcPoints[3]=yi;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);
		repaint(findProperRect((int) destPoints[0],(int) destPoints[1],(int) destPoints[2],(int) destPoints[3]));
		prevPoint.x=x;
		prevPoint.y=y;
		g.dispose();
	}

	public void drawPoint(int x,int y,Color color) {
		Graphics2D g=(Graphics2D) canvasImage.getGraphics();
		g.setColor(color);
		g.setStroke(normalStroke);

		int xi=toOrigImCoords(x);
		int yi=toOrigImCoords(y);

		if (color.getAlpha() == 0)
			g.setComposite(AlphaComposite.Clear);

		g.fillRect(xi,yi,1,1);

		srcPoints[0]=xi;
		srcPoints[1]=yi;
		srcPoints[2]=xi + 1;
		srcPoints[3]=yi + 1;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);
		repaint((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2] - destPoints[0]),(int) (destPoints[3] - destPoints[1]));
		prevPoint=new Point(x,y);
		g.dispose();
	}

	public void drawLine(int x1,int y1,int x2,int y2,Color color,boolean offImage) {
		drawColor=color;
		x1=toOrigImCoords(x1);
		y1=toOrigImCoords(y1);
		x2=toOrigImCoords(x2);
		y2=toOrigImCoords(y2);
		srcPoints[0]=x1;
		srcPoints[1]=y1;
		srcPoints[2]=x2;
		srcPoints[3]=y2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);

		if (!offImage) {
			offLine=false;
			Graphics2D g=(Graphics2D) canvasImage.getGraphics();
			g.setColor(drawColor);
			g.setStroke(normalStroke);

			if (color.getAlpha() == 0)
				g.setComposite(AlphaComposite.Clear);

			g.drawLine(x1,y1,x2,y2);

			RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
			previousRect=findProperRect(r);
			repaint(previousRect);
			g.dispose();
		} else {
			repaint(previousRect);
			offLine=true;
			offLineRect=new Rectangle(x1,y1,x2,y2);
			RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
			previousRect=findProperRect(r);
			repaint(previousRect);
		}
	}

	private void drawOffLine(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setStroke(normalStroke);
		g2.setColor(drawColor);
		if (drawColor.getAlpha() == 0)
			g2.setComposite(AlphaComposite.Clear);
		g2.drawLine(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		g2.dispose();
	}

	public void drawEllipse(int x1,int y1,int x2,int y2,Color color,boolean offImage,boolean circle) {
		drawColor=color;
		x1=toOrigImCoords(x1);
		y1=toOrigImCoords(y1);
		x2=toOrigImCoords(x2);
		y2=toOrigImCoords(y2);
		srcPoints[0]=x1;
		srcPoints[1]=y1;
		srcPoints[2]=x2;
		srcPoints[3]=y2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);
		RectPoints outLine=findProperRectPoints(x1,y1,x2,y2);
		RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));

		if (!offImage) {
			offEllipse=false;
			Graphics2D g=(Graphics2D) canvasImage.getGraphics();
			g.setColor(drawColor);
			g.setStroke(normalStroke);
			if (circle) {
				offLineRect=calculateSquarePoints(outLine,x1 < x2,y1 < y2,true);
				previousRect=calculateSquarePoints(r,x1 < x2,y1 < y2,false);
			} else {
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
				previousRect=new Rectangle(r.x1 - strokeWidth * (int) scalingFactor,r.y1 - strokeWidth * (int) scalingFactor,r.x2 + 2 * strokeWidth * (int) scalingFactor,r.y2 + 2 * strokeWidth * (int) scalingFactor);
			}
			if (color.getAlpha() == 0)
				g.setComposite(AlphaComposite.Clear);
			g.drawOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			g.dispose();
			repaint(previousRect);
		} else {
			repaint(previousRect);
			offEllipse=true;
			if (circle) {
				offLineRect=calculateSquarePoints(outLine,x1 < x2,y1 < y2,true);
				previousRect=calculateSquarePoints(r,x1 < x2,y1 < y2,false);
			} else {
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
				previousRect=new Rectangle(r.x1 - strokeWidth * (int) scalingFactor,r.y1 - strokeWidth * (int) scalingFactor,r.x2 + 2 * strokeWidth * (int) scalingFactor,r.y2 + 2 * strokeWidth * (int) scalingFactor);
			}
			repaint(previousRect);
		}
	}

	private void drawOffEllipse(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(drawColor);
		g2.setStroke(normalStroke);
		if (drawColor.getAlpha() == 0)
			g2.setComposite(AlphaComposite.Clear);
		g2.drawOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		g2.dispose();
	}

	public void drawRectangle(int x1,int y1,int x2,int y2,Color color,boolean offImage,boolean square) {
		drawColor=color;
		x1=toOrigImCoords(x1);
		y1=toOrigImCoords(y1);
		x2=toOrigImCoords(x2);
		y2=toOrigImCoords(y2);
		srcPoints[0]=x1;
		srcPoints[1]=y1;
		srcPoints[2]=x2;
		srcPoints[3]=y2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);

		RectPoints outLine=findProperRectPoints(x1,y1,x2,y2);
		RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));

		if (!offImage) {
			offRect=false;
			Graphics2D g=(Graphics2D) canvasImage.getGraphics();
			g.setColor(drawColor);
			g.setStroke(normalStroke);
			if (square) {
				offLineRect=calculateSquarePoints(outLine,x1 < x2,y1 < y2,true);
				previousRect=calculateSquarePoints(r,x1 < x2,y1 < y2,false);
			} else {
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
				previousRect=new Rectangle(r.x1 - (int) scalingFactor,r.y1 - (int) scalingFactor,r.x2 + 2 * (int) scalingFactor,r.y2 + 2 * (int) scalingFactor);
			}

			if (color.getAlpha() == 0)
				g.setComposite(AlphaComposite.Clear);
			g.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			g.dispose();
			repaint(previousRect);
		} else {
			repaint(previousRect);
			offRect=true;
			if (square) {
				offLineRect=calculateSquarePoints(outLine,x1 < x2,y1 < y2,true);
				previousRect=calculateSquarePoints(r,x1 < x2,y1 < y2,false);
			} else {
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
				previousRect=new Rectangle(r.x1 - strokeWidth * (int) scalingFactor,r.y1 - strokeWidth * (int) scalingFactor,r.x2 + 2 * strokeWidth * (int) scalingFactor,r.y2 + 2 * strokeWidth * (int) scalingFactor);
			}
			repaint(previousRect);
		}
	}

	public void fillRectangle(int x1,int y1,int x2,int y2,Color foreColor,Color backColor,boolean offImage,boolean square,boolean outlineRect) {
		drawColor=foreColor;
		x1=toOrigImCoords(x1);
		y1=toOrigImCoords(y1);
		x2=toOrigImCoords(x2);
		y2=toOrigImCoords(y2);
		srcPoints[0]=x1;
		srcPoints[1]=y1;
		srcPoints[2]=x2;
		srcPoints[3]=y2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);

		RectPoints outLine=findProperRectPoints(x1,y1,x2,y2);
		RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));

		if (!offImage) {
			offRect=false;
			Graphics2D g=(Graphics2D) canvasImage.getGraphics();
			g.setColor(drawColor);
			g.setStroke(normalStroke);
			if (square) {
				offLineRect=calculateSquarePoints(outLine,x1 < x2,y1 < y2,true);
				previousRect=calculateSquarePoints(r,x1 < x2,y1 < y2,false);
			} else {
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
				previousRect=new Rectangle(r.x1 - (int) scalingFactor,r.y1 - (int) scalingFactor,r.x2 + 2 * (int) scalingFactor,r.y2 + 2 * (int) scalingFactor);
			}
			if (outlineRect) {
				g.setColor(backColor);
				g.fillRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
				g.setColor(drawColor);
				if (drawColor.getAlpha() == 0)
					g.setComposite(AlphaComposite.Clear);
				g.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			} else {
				if (drawColor.getAlpha() == 0)
					g.setComposite(AlphaComposite.Clear);
				g.fillRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			}
			g.dispose();
			repaint(previousRect);
		}
		/*
		 * else { repaint(previousRect); offRect = true; if (square){ offLineRect = calculateSquarePoints(outLine, x1<x2,
		 * y1<y2, true); previousRect = calculateSquarePoints(r, x1<x2, y1<y2, false); } else { offLineRect = new
		 * Rectangle(outLine.x1, outLine.y1, outLine.x2 - outLine.x1, outLine.y2 - outLine.y1); previousRect = new
		 * Rectangle(r.x1-(int)scalingFactor, r.y1-(int)scalingFactor, r.x2+2*(int)scalingFactor,
		 * r.y2+2*(int)scalingFactor); } repaint(previousRect); }
		 */
	}

	public void fillEllipse(int x1,int y1,int x2,int y2,Color foreColor,Color backColor,boolean offImage,boolean circle,boolean outlineEll) {
		drawColor=foreColor;
		x1=toOrigImCoords(x1);
		y1=toOrigImCoords(y1);
		x2=toOrigImCoords(x2);
		y2=toOrigImCoords(y2);
		srcPoints[0]=x1;
		srcPoints[1]=y1;
		srcPoints[2]=x2;
		srcPoints[3]=y2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);

		RectPoints outLine=findProperRectPoints(x1,y1,x2,y2);
		RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));

		if (!offImage) {
			offEllipse=false;
			Graphics2D g=(Graphics2D) canvasImage.getGraphics();
			g.setColor(drawColor);
			g.setStroke(normalStroke);
			if (circle) {
				offLineRect=calculateSquarePoints(outLine,x1 < x2,y1 < y2,true);
				previousRect=calculateSquarePoints(r,x1 < x2,y1 < y2,false);
			} else {
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
				previousRect=new Rectangle(r.x1 - (int) scalingFactor,r.y1 - (int) scalingFactor,r.x2 + 2 * (int) scalingFactor,r.y2 + 2 * (int) scalingFactor);
			}
			if (outlineEll) {
				g.setColor(backColor);
				g.fillOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
				g.setColor(drawColor);
				if (drawColor.getAlpha() == 0)
					g.setComposite(AlphaComposite.Clear);
				g.drawOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);

			} else {
				if (drawColor.getAlpha() == 0)
					g.setComposite(AlphaComposite.Clear);
				g.fillOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			}
			g.dispose();
			repaint(previousRect);
		}
		/*
		 * else { repaint(previousRect); offRect = true; if (square){ offLineRect = calculateSquarePoints(outLine, x1<x2,
		 * y1<y2, true); previousRect = calculateSquarePoints(r, x1<x2, y1<y2, false); } else { offLineRect = new
		 * Rectangle(outLine.x1, outLine.y1, outLine.x2 - outLine.x1, outLine.y2 - outLine.y1); previousRect = new
		 * Rectangle(r.x1-(int)scalingFactor, r.y1-(int)scalingFactor, r.x2+2*(int)scalingFactor,
		 * r.y2+2*(int)scalingFactor); } repaint(previousRect); }
		 */
	}

	public int getPixel(Point p) {
		int x=toOrigImCoords(p.x);
		int y=toOrigImCoords(p.y);
		return canvasImage.getRGB(x,y);
	}

	private Rectangle calculateSquarePoints(RectPoints rp,boolean x1Lx2,boolean y1Ly2,boolean realImage) {
		int maxDim=Math.max(rp.x2 - rp.x1,rp.y2 - rp.y1);
		int xi, yi;
		if (x1Lx2)
			xi=rp.x1;
		else
			xi=rp.x2 - maxDim;
		if (y1Ly2)
			yi=rp.y1;
		else
			yi=rp.y2 - maxDim;
		if (realImage)
			return new Rectangle(xi,yi,maxDim,maxDim);
		else
			return new Rectangle(xi - strokeWidth * (int) scalingFactor,yi - strokeWidth * (int) scalingFactor,maxDim + 2 * strokeWidth * (int) scalingFactor,maxDim + 2 * strokeWidth * (int) scalingFactor);
	}

	private void drawOffRect(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(drawColor);
		g2.setStroke(normalStroke);
		if (drawColor.getAlpha() == 0)
			g2.setComposite(AlphaComposite.Clear);
		g2.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		g2.dispose();
	}

	private void drawOffSelection(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(selectionColor);
		Stroke stroke=new BasicStroke(2f / (float) scalingFactor,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2f / (float) scalingFactor,2f / (float) scalingFactor},0);

		g2.setStroke(stroke);
		if (rectSelection)
			g2.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		else {
			g2.drawOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		}

		// g2.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		// ////////////
		g2.setColor(whiteColor);
		stroke=new BasicStroke(2f / (float) scalingFactor,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2f / (float) scalingFactor,2f / (float) scalingFactor},2f / (float) scalingFactor);
		g2.setStroke(stroke);
		if (rectSelection)
			g2.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		else
			g2.drawOval(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		// g2.drawRect(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
		g2.dispose();
	}

	public void drawPoly(int x,int y,Color foreColor,Color backColor,boolean firstPoint) {
		drawColor=foreColor;
		this.backColor=backColor;
		if (firstPoint) {
			drawPoint(x,y,drawColor);
			x=toOrigImCoords(x);
			y=toOrigImCoords(y);
			xPoints.add(x);
			yPoints.add(y);
		} else {
			offLine=false;
			Graphics2D g=(Graphics2D) canvasImage.getGraphics();
			g.setColor(drawColor);
			g.setStroke(normalStroke);
			x=toOrigImCoords(x);
			y=toOrigImCoords(y);
			xPoints.add(x);
			yPoints.add(y);

			RectPoints rp=new RectPoints(x,y,x + 1,y + 1);
			int currentSize=xPoints.size();
			if (currentSize - 2 >= 0)
				rp=findProperRectPoints(x,y,xPoints.get(currentSize - 2),yPoints.get(currentSize - 2));

			if (drawColor.getAlpha() == 0)
				g.setComposite(AlphaComposite.Clear);
			g.drawPolyline(xPoints.toArray(),yPoints.toArray(),currentSize);

			srcPoints[0]=rp.x1;
			srcPoints[1]=rp.y1;
			srcPoints[2]=rp.x2;
			srcPoints[3]=rp.y2;
			worldToLocal.transform(srcPoints,0,destPoints,0,2);
			rp=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
			previousRect=findProperRect(rp);
			repaint(previousRect);
			g.dispose();
		}
	}

	public void closePoly(int xi,int yi,int fillMode) { // 0: nofill, 1: outlinefill, 2: fill
		Graphics2D g=(Graphics2D) canvasImage.getGraphics();
		g.setColor(drawColor);
		g.setStroke(normalStroke);
		int x=xPoints.get(0);
		int y=yPoints.get(0);
		xPoints.add(x);
		yPoints.add(y);
		int currentSize=xPoints.size();
		Polygon p=new Polygon(xPoints.toArray(),yPoints.toArray(),currentSize);

		RectPoints rp=findProperRectPoints(x,y,xPoints.get(currentSize - 2),yPoints.get(currentSize - 2));
		if (fillMode == 1) {
			if (drawColor.getAlpha() == 0)
				g.setComposite(AlphaComposite.Clear);
			g.drawPolyline(xPoints.toArray(),yPoints.toArray(),currentSize);
			srcPoints[0]=rp.x1;
			srcPoints[1]=rp.y1;
			srcPoints[2]=rp.x2;
			srcPoints[3]=rp.y2;
			worldToLocal.transform(srcPoints,0,destPoints,0,2);
			rp=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
		} else {
			if (fillMode == 2) {
				g.setColor(backColor);
				g.fillPolygon(p);
				g.setColor(drawColor);
				if (drawColor.getAlpha() == 0)
					g.setComposite(AlphaComposite.Clear);
				g.drawPolyline(xPoints.toArray(),yPoints.toArray(),currentSize);
			} else {
				if (drawColor.getAlpha() == 0)
					g.setComposite(AlphaComposite.Clear);
				g.fillPolygon(xPoints.toArray(),yPoints.toArray(),currentSize);
			}
			Rectangle bounds=p.getBounds();
			srcPoints[0]=bounds.x;
			srcPoints[1]=bounds.y;
			srcPoints[2]=bounds.x + bounds.width;
			srcPoints[3]=bounds.y + bounds.height;
			worldToLocal.transform(srcPoints,0,destPoints,0,2);
			rp=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
		}
		previousRect=findProperRect(rp);
		repaint(previousRect);
		stopPoly(xi,yi);
		offLine=false;
		g.dispose();
	}

	public void stopPoly(int x,int y) {
		x=toOrigImCoords(x);
		y=toOrigImCoords(y);
		xPoints.add(x);
		yPoints.add(y);
		RectPoints rp=new RectPoints(x,y,x + 1,y + 1);
		int currentSize=xPoints.size();
		if (currentSize - 2 >= 0)
			rp=findProperRectPoints(x,y,xPoints.get(currentSize - 2),yPoints.get(currentSize - 2));

		srcPoints[0]=rp.x1;
		srcPoints[1]=rp.y1;
		srcPoints[2]=rp.x2;
		srcPoints[3]=rp.y2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);
		rp=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
		previousRect=findProperRect(rp);
		repaint(previousRect);

		xPoints.clear();
		yPoints.clear();
		offLine=false;
	}

	public void setStroke(Stroke st) {
		normalStroke=(BasicStroke) st;
		strokeWidth=(int) normalStroke.getLineWidth();
	}

	private void drawDragSelection(int x1,int y1,int x2,int y2,boolean symetricSelection) {
		int xi1=toOrigImCoords(x1);
		int yi1=toOrigImCoords(y1);
		int xi2=toOrigImCoords(x2);
		int yi2=toOrigImCoords(y2);
		srcPoints[0]=xi1;
		srcPoints[1]=yi1;
		srcPoints[2]=xi2;
		srcPoints[3]=yi2;
		worldToLocal.transform(srcPoints,0,destPoints,0,2);
		RectPoints outLine=findProperRectPoints(xi1,yi1,xi2,yi2);
		RectPoints r=findProperRectPoints((int) destPoints[0],(int) destPoints[1],(int) (destPoints[2]),(int) (destPoints[3]));
		repaint(previousRect);
		offSelect=true;

		if (symetricSelection) {
			offLineRect=calculateSquarePoints(outLine,xi1 < xi2,yi1 < yi2,true);
			previousRect=calculateSquarePoints(r,xi1 < xi2,yi1 < yi2,true);
			previousRect.x-=strokeWidth * (int) scalingFactor;
			previousRect.y-=strokeWidth * (int) scalingFactor;
			previousRect.width+=2 * strokeWidth * (int) scalingFactor;
			previousRect.height+=2 * strokeWidth * (int) scalingFactor;
		} else {
			offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);
			previousRect=new Rectangle(r.x1 - strokeWidth * (int) scalingFactor,r.y1 - strokeWidth * (int) scalingFactor,r.x2 + 2 * strokeWidth * (int) scalingFactor,r.y2 + 2 * strokeWidth * (int) scalingFactor);
		}

		// offLineRect = new Rectangle(outLine.x1, outLine.y1, outLine.x2 - outLine.x1, outLine.y2 - outLine.y1);
		// previousRect = new Rectangle(r.x1-strokeWidth*(int)scalingFactor, r.y1-strokeWidth*(int)scalingFactor,
		// r.x2+2*strokeWidth*(int)scalingFactor, r.y2+2*strokeWidth*(int)scalingFactor);
		repaint(previousRect);
	}

	public Point getProperPoint(Point p) {
		int x=toOrigImCoords(p.x);
		int y=toOrigImCoords(p.y);
		if (x > canvasImage.getWidth())
			x=canvasImage.getWidth();
		if (y > canvasImage.getHeight())
			y=canvasImage.getHeight();
		return new Point(toTransImCoords(x),toTransImCoords(y));
	}

	public void select(int x1,int y1,int x2,int y2,Color color,String str,boolean dragging,boolean rectSelection,boolean symetricSelection) {
		this.rectSelection=rectSelection;
		if (dragging) {
			selectionColor=color;
			drawDragSelection(x1,y1,x2,y2,symetricSelection);
		} else {
			existsSelection=true;
			offSelect=false;
			selectionColor=color;
			int xi1=toOrigImCoords(x1);
			int yi1=toOrigImCoords(y1);
			int xi2=toOrigImCoords(x2);
			int yi2=toOrigImCoords(y2);

			RectPoints outLine=findProperRectPoints(xi1,yi1,xi2,yi2);
			Shape toDrawOriginal;
			// Rectangle toDrawOriginal = new Rectangle(correctOriginalRect.x1, correctOriginalRect.y1,
			// correctOriginalRect.x2 - correctOriginalRect.x1, correctOriginalRect.y2 - correctOriginalRect.y1);

			if (symetricSelection)
				offLineRect=calculateSquarePoints(outLine,xi1 < xi2,yi1 < yi2,true);
			else
				offLineRect=new Rectangle(outLine.x1,outLine.y1,outLine.x2 - outLine.x1,outLine.y2 - outLine.y1);

			if (rectSelection)
				toDrawOriginal=new Rectangle(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			// toDrawOriginal = new Rectangle(outLine.x1, outLine.y1, outLine.x2 - outLine.x1, outLine.y2 - outLine.y1);
			else
				toDrawOriginal=new Ellipse2D.Double(offLineRect.x,offLineRect.y,offLineRect.width,offLineRect.height);
			// toDrawOriginal = new Ellipse2D.Double(correctOriginalRect.x1, correctOriginalRect.y1,
			// correctOriginalRect.x2 - correctOriginalRect.x1, correctOriginalRect.y2 - correctOriginalRect.y1);

			if (str.equals("add"))
				addToSelectedShapes(toDrawOriginal);
			else if (str.equals("remove"))
				removeFromSelectedShapes(toDrawOriginal);
			else
				setToSelectedShapes(toDrawOriginal);

			// for repainting
			/*
			 * srcPoints[0]=xi1; srcPoints[1]=yi1; srcPoints[2]=xi2; srcPoints[3]=yi2;
			 * worldToLocal.transform(srcPoints,0,destPoints,0,2);
			 */
			// RectPoints outLine = findProperRectPoints(xi1, yi1, xi2, yi2);
			// RectPoints r =
			// findProperRectPoints((int)destPoints[0],(int)destPoints[1],(int)(destPoints[2]),(int)(destPoints[3]));
			repaint(previousRect);

			// offLineRect = new Rectangle(outLine.x1, outLine.y1, outLine.x2 - outLine.x1, outLine.y2 - outLine.y1);
			Shape refreshShape=worldToLocal.createTransformedShape(selectedShape);
			previousRect=refreshShape.getBounds();
			// previousRect = new Rectangle(r.x1-strokeWidth*(int)scalingFactor, r.y1-strokeWidth*(int)scalingFactor,
			// r.x2+2*strokeWidth*(int)scalingFactor, r.y2+2*strokeWidth*(int)scalingFactor);
			repaint(previousRect);
		}
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setToSelectedShapes(Shape s) {
		selectedShape=s;
	}

	private void addToSelectedShapes(Shape s) {
		if (selectedShape == null)
			return;
		Area area1, area2;
		area1=new Area(selectedShape);
		area2=new Area(s);
		area1.add(area2);
		selectedShape=(Shape) area1;
	}

	private void removeFromSelectedShapes(Shape s) {
		if (selectedShape == null)
			return;
		Area area1, area2;
		area1=new Area(selectedShape);
		area2=new Area(s);
		area1.subtract(area2);
		selectedShape=(Shape) area1;
	}

	public void selectPixels(Color color) {
		int xSize=canvasImage.getWidth();
		int ySize=canvasImage.getHeight();
		boolean[][] hasBeenSelected=new boolean[xSize][ySize];
		for (int i=0;i < xSize;i++)
			for (int j=0;j < ySize;j++)
				if (canvasImage.getRGB(i,j) == color.getRGB())
					hasBeenSelected[i][j]=true;
		createWandShape(hasBeenSelected,selectionColor,"normal");
	}

	private void drawSelectedShapes(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(selectionColor);
		Stroke stroke=new BasicStroke(2f / (float) scalingFactor,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2f / (float) scalingFactor,2f / (float) scalingFactor},0);
		g2.setStroke(stroke);
		if (selectedShape != null) {
			g2.draw(selectedShape);
		}
		// ////////////
		g2.setColor(whiteColor);
		stroke=new BasicStroke(2f / (float) scalingFactor,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2f / (float) scalingFactor,2f / (float) scalingFactor},2f / (float) scalingFactor);

		g2.setStroke(stroke);
		if (selectedShape != null) {
			g2.draw(selectedShape);
		}

		g2.dispose();
	}

	public void fillSelectedShapes(Color color) {
		if (selectedShape == null)
			return;
		Graphics2D g2=(Graphics2D) canvasImage.getGraphics();
		g2.setColor(color);
		if (color.getAlpha() == 0)
			g2.setComposite(AlphaComposite.Clear);
		g2.fill(selectedShape);

		Shape refreshShape=worldToLocal.createTransformedShape(selectedShape);
		previousRect=refreshShape.getBounds();
		repaint(previousRect);
		g2.dispose();
	}

	public void flatFill(int x,int y,Color color) {
		magicWand(x,y,selectionColor,"flat");
		if (existsSelection) {
			/*
			 * if (!containsPoint(new Point(x,y))){ getToolkit().beep(); return; }
			 */
			Area flatArea=new Area(flatShape);
			Area selectedArea=new Area(selectedShape);

			boolean isContained=flatArea.getBounds().contains(selectedArea.getBounds());
			flatArea.intersect(selectedArea);

			flatShape=(Shape) flatArea;

			if (isContained)
				flatShape=scanShape(flatShape,x,y);
		}
		Graphics2D g2=(Graphics2D) canvasImage.getGraphics();
		g2.setColor(color);
		if (color.getAlpha() == 0)
			g2.setComposite(AlphaComposite.Clear);
		g2.fill(flatShape);
		Shape refreshShape=worldToLocal.createTransformedShape(flatShape);
		previousRect=refreshShape.getBounds();
		repaint(previousRect);
		g2.dispose();
	}

	private Shape scanShape(Shape s,int findX,int findY) {
		Area initialArea=new Area(s);
		GeneralPath subShape=new GeneralPath();
		PathIterator pi=s.getPathIterator(null);
		int segType=0;
		boolean shapeCreated=false;
		boolean forcedFinish=false;

		float seg[]=new float[6];
		while (!pi.isDone() && !forcedFinish) {
			segType=pi.currentSegment(seg);
			switch (segType) {
			case PathIterator.SEG_MOVETO:
				// System.out.println("currentPoint: "+subShape.getCurrentPoint());
				// System.out.println("moveTo: "+ seg[0]+", "+seg[1]);

				if (!shapeCreated) {
					// System.out.println("start a new shape");
					subShape.moveTo(seg[0],seg[1]);
				} else {
					// System.out.println("start a new reset shape");
					shapeCreated=false;
					subShape.reset();
					subShape.moveTo(seg[0],seg[1]);
				}
				break;
			case PathIterator.SEG_LINETO:
				// System.out.println("currentPoint: "+subShape.getCurrentPoint());
				// System.out.println("lineTo: "+ seg[0]+", "+seg[1]);
				subShape.lineTo(seg[0],seg[1]);
				break;
			case PathIterator.SEG_CLOSE:
				if (!shapeCreated) {
					// System.out.println("currentPoint: "+subShape.getCurrentPoint());
					shapeCreated=true;
					subShape.closePath();
					if (subShape.contains(new Point(toOrigImCoords(findX),toOrigImCoords(findY)))) {
						forcedFinish=true;
						// System.out.println("subShape found");
					} else {
						initialArea.subtract(new Area(subShape));
					}
				}
				break;
			case PathIterator.SEG_QUADTO:
				// System.out.println("quad: "+subShape.getCurrentPoint());
				subShape.quadTo(seg[0],seg[1],seg[2],seg[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				// System.out.println("cubic: "+subShape.getCurrentPoint());
				subShape.curveTo(seg[0],seg[1],seg[2],seg[3],seg[4],seg[5]);
				break;
			}
			pi.next();
		}
		// System.out.println("finished : "+subShape.getBounds());
		initialArea.intersect(new Area(subShape));
		return (Shape) initialArea;
	}

	public void invertSelection() {
		Rectangle imageRect=new Rectangle(0,0,canvasImage.getWidth(),canvasImage.getHeight());
		Area shapeArea=new Area(selectedShape);
		Area imageArea=new Area(imageRect);
		imageArea.subtract(shapeArea);
		selectedShape=imageArea;
		repaint();
	}

	public boolean containsPoint(Point p) {
		Point pOr=new Point(toOrigImCoords(p.x),toOrigImCoords(p.y));
		if (selectedShape != null) {
			Area selectedArea=new Area(selectedShape);
			return selectedArea.contains(pOr);
		}
		return false;
	}

	public boolean isAnythingSelected() {
		return existsSelection;
	}

	public void magicWand(int xPressed,int yPressed,Color color,String mode) {
		xPressed=toOrigImCoords(xPressed);
		yPressed=toOrigImCoords(yPressed);

		ArrayList<Point> centers=new ArrayList<Point>();
		int xSize=canvasImage.getWidth();
		int ySize=canvasImage.getHeight();
		boolean[][] hasBeenSelected=new boolean[xSize][ySize];

		centers.add(new Point(xPressed,yPressed));
		hasBeenSelected[xPressed][yPressed]=true;
		int index=0;

		while (index <= (centers.size() - 1)) {
			Point center=centers.get(index);
			int x=center.x;
			int y=center.y;

			if ((x > 0) && (!hasBeenSelected[x - 1][y])) {
				if (canvasImage.getRGB(x,y) == canvasImage.getRGB(x - 1,y)) {
					centers.add(new Point(x - 1,y));
					hasBeenSelected[x - 1][y]=true;
				}
			}

			if ((y > 0) && (!hasBeenSelected[x][y - 1])) {
				if (canvasImage.getRGB(x,y) == canvasImage.getRGB(x,y - 1)) {
					centers.add(new Point(x,y - 1));
					hasBeenSelected[x][y - 1]=true;
				}
			}

			if ((x < xSize - 1) && (!hasBeenSelected[x + 1][y])) {
				if (canvasImage.getRGB(x,y) == canvasImage.getRGB(x + 1,y)) {
					centers.add(new Point(x + 1,y));
					hasBeenSelected[x + 1][y]=true;
				}
			}

			if ((y < ySize - 1) && (!hasBeenSelected[x][y + 1])) {
				if (canvasImage.getRGB(x,y) == canvasImage.getRGB(x,y + 1)) {
					centers.add(new Point(x,y + 1));
					hasBeenSelected[x][y + 1]=true;
				}
			}
			index++;
		}
		createWandShape(hasBeenSelected,color,mode);
	}

	private void createWandShape(boolean[][] sel,Color color,String mode) {
		boolean previousSelection=existsSelection;
		selectionColor=color;
		existsSelection=true;
		Rectangle r=null;
		Area wandArea=new Area();
		boolean first=true, firstNotSelected=false;

		for (int i=0;i < sel.length;i++) {
			for (int j=0;j < sel[0].length;j++) {
				if (sel[i][j]) {
					if (first) {
						r=new Rectangle(new Point(i,j));
						first=false;
						firstNotSelected=true;
					}
					if (j == sel[0].length - 1) {
						r.width=1;
						r.height=sel[0].length - r.y;
						wandArea.add(new Area(r));
						firstNotSelected=false;
						first=true;
					}
				} else {
					if (firstNotSelected) {
						r.width=1;
						r.height=j - r.y;
						wandArea.add(new Area(r));
						firstNotSelected=false;
						first=true;
					}
				}
			}
		}

		if (mode.equals("add"))
			addToSelectedShapes((Shape) wandArea);
		else if (mode.equals("remove"))
			removeFromSelectedShapes((Shape) wandArea);
		else if (mode.equals("flat")) {
			if (!previousSelection)
				existsSelection=false;
			flatShape=(Shape) wandArea;
			return;
		} else
			selectedShape=(Shape) wandArea;

		repaint(previousRect);
		Shape refreshShape=worldToLocal.createTransformedShape(selectedShape);
		previousRect=refreshShape.getBounds();
		repaint(previousRect);
	}

	public Rectangle getSelectedRect() {
		return selectedShape.getBounds();
	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

	public void setSelectedShape(Shape s) {
		existsSelection=true;
		offSelect=false;
		selectionColor=new Color(10,10,100,120);
		selectedShape=s;
		repaint();
	}

	public BufferedImage getSelectedImage() {
		// construct the rectangular Image
		Rectangle bounds=selectedShape.getBounds();

		dragImage=new BufferedImage(bounds.width,bounds.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=dragImage.createGraphics();
		// set transparent background
		g2.setBackground(new Color(255,255,255,0));
		g2.clearRect(0,0,bounds.x,bounds.y);

		Area shapeArea=new Area(selectedShape);
		originalToDragImage=AffineTransform.getTranslateInstance(-bounds.x,-bounds.y);
		shapeArea=shapeArea.createTransformedArea(originalToDragImage);

		g2.clip((Shape) shapeArea);
		// g2.setComposite(AlphaComposite.Src);
		g2.drawImage(canvasImage,-bounds.x,-bounds.y,null);
		g2.dispose();
		return dragImage;
	}

	public void createAreaToDrag(Point p,Color color) {
		int x=toOrigImCoords(p.x);
		int y=toOrigImCoords(p.y);
		initialShape=selectedShape;
		backColor=color;
		Rectangle bounds=selectedShape.getBounds();

		grabPoint=new Point(x - bounds.x,y - bounds.y);
		getSelectedImage();
		/*
		 * ImageIcon previewPicture = new ImageIcon(dragImage); ImageDialog imageDialog = new
		 * ImageDialog(previewPicture);
		 * imageDialog.setSize(100+previewPicture.getIconWidth(),100+previewPicture.getIconHeight());
		 * imageDialog.show();
		 */
	}

	public void setPastedImage(BufferedImage img,int x,int y) {
		paste=true;
		x=toOrigImCoords(x);
		y=toOrigImCoords(y);
		Graphics2D g=(Graphics2D) canvasImage.getGraphics();
		g.setColor(drawColor);

		initialShape=selectedShape;
		double xOffset=x - initialShape.getBounds().x;
		double yOffset=y - initialShape.getBounds().y;
		AffineTransform moveTransform=AffineTransform.getTranslateInstance(xOffset,yOffset);
		selectedShape=moveTransform.createTransformedShape(initialShape);

		backupPasteImage=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=backupPasteImage.createGraphics();
		g2.drawImage(canvasImage,-x,-y,null);

		g.drawImage(img,x,y,null);
		repaint();
		g.dispose();
		g2.dispose();
	}

	public void stopDragging(Point stopPoint) {
		if (!isDragging)
			return;
		isDragging=false;
		Graphics2D g=(Graphics2D) canvasImage.createGraphics();
		g.setColor(drawColor);
		if (paste) {
			Rectangle bounds=initialShape.getBounds();
			g.drawImage(backupPasteImage,bounds.x,bounds.y,null);
		} else {
			// clear the original area that is left behind
			g.setColor(backColor);
			if (backColor.getAlpha() == 0)
				g.setComposite(AlphaComposite.Clear);
			g.fill(initialShape);
			g.setComposite(AlphaComposite.SrcOver);
		}
		paste=false;

		double xOffset=dragX - grabPoint.x - initialShape.getBounds().x;
		double yOffset=dragY - grabPoint.y - initialShape.getBounds().y;

		AffineTransform moveTransform=AffineTransform.getTranslateInstance(xOffset,yOffset);
		selectedShape=moveTransform.createTransformedShape(initialShape);

		int posX=initialShape.getBounds().x + (int) xOffset;
		int posY=initialShape.getBounds().y + (int) yOffset;

		Rectangle originalBounds=selectedShape.getBounds();
		int selectionXLimit=originalBounds.x + originalBounds.width;
		int selectionYLimit=originalBounds.y + originalBounds.height;

		if (selectionXLimit >= canvasImage.getWidth()) {
			int imageWidth=canvasImage.getWidth();
			removeFromSelectedShapes(new Rectangle(imageWidth,originalBounds.y,selectionXLimit - imageWidth,originalBounds.height));
		}

		if (selectionYLimit >= canvasImage.getHeight()) {
			int imageHeight=canvasImage.getHeight();
			removeFromSelectedShapes(new Rectangle(originalBounds.x,imageHeight,originalBounds.width,selectionYLimit - imageHeight));
		}

		if (!intoImage(stopPoint)) {
			Rectangle initialBounds=initialShape.getBounds();
			g.drawImage(dragImage,initialBounds.x,initialBounds.y,null);
			deselectAll();
		} else {
			g.clip(selectedShape);
			g.drawImage(dragImage,posX,posY,null);
		}
		repaint();
		g.dispose();
	}

	public BufferedImage createSubImage(Point pUpLeft,Point pDownRight) {
		int pX1=toOrigImCoords(pUpLeft.x);
		int pY1=toOrigImCoords(pUpLeft.y);
		int pX2=toOrigImCoords(pDownRight.x);
		int pY2=toOrigImCoords(pDownRight.y);
		RectPoints rp=findProperRectPoints(pX1,pY1,pX2,pY2);
		int width=rp.x2 - rp.x1;
		int height=rp.y2 - rp.y1;
		BufferedImage subImage=new BufferedImage(width + 1,height + 1,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=subImage.createGraphics();
		g2.drawImage(canvasImage,-rp.x1,-rp.y1,null);
		g2.dispose();
		return subImage;
	}

	public void restoreImage(BufferedImage im,Point pressPoint,Point restorePoint,double scaleF) {
		int pX1=pressPoint.x / (int) scaleF;
		int pY1=pressPoint.y / (int) scaleF;
		;
		int pX2=restorePoint.x / (int) scaleF;
		;
		int pY2=restorePoint.y / (int) scaleF;

		/*
		 * int pX1 = toOrigImCoords(pressPoint.x); int pY1 = toOrigImCoords(pressPoint.y); int pX2 =
		 * toOrigImCoords(restorePoint.x); int pY2 = toOrigImCoords(restorePoint.y);
		 */

		RectPoints rp=findProperRectPoints(pX1,pY1,pX2,pY2);
		Graphics2D g2=canvasImage.createGraphics();
		g2.drawImage(im,rp.x1,rp.y1,null);
		repaint();
		g2.dispose();
	}

	public void restoreWholeImage(BufferedImage im) {
		setCanvasImage(im);
	}

	public void setImageAsSelection(ImageIcon im,Point p,Color color,boolean drag) {
		selectionColor=color;
		existsSelection=true;
		offSelect=false;

		int x=toOrigImCoords(p.x);
		int y=toOrigImCoords(p.y);
		int width=im.getIconWidth();
		int height=im.getIconHeight();

		selectedShape=new Rectangle(x - width / 2,y - height / 2,width,height);
		Shape refreshShape=worldToLocal.createTransformedShape(selectedShape);
		Rectangle refBounds=refreshShape.getBounds();

		draggedIcon=im;
		if (drag)
			dragIcon=true;
		else {
			if (x > canvasImage.getWidth() || y > canvasImage.getHeight()) {
				dragIcon=false;
				deselectAll();
				return;
			}
			int imageWidth=canvasImage.getWidth();
			int imageHeight=canvasImage.getHeight();
			Rectangle bounds=selectedShape.getBounds();

			if (bounds.x + bounds.width > imageWidth)
				removeFromSelectedShapes(new Rectangle(imageWidth,bounds.y,bounds.x + bounds.width - imageWidth,imageHeight));
			if (bounds.y + bounds.height > imageHeight)
				removeFromSelectedShapes(new Rectangle(bounds.x,imageHeight,imageWidth,bounds.y + bounds.height - imageHeight));

			dragIcon=false;
			Graphics2D g2=canvasImage.createGraphics();
			g2.drawImage(draggedIcon.getImage(),bounds.x,bounds.y,null);
			g2.dispose();
		}
		repaint(previousRect);
		previousRect=new Rectangle(refBounds.x - (int) scalingFactor,refBounds.y - (int) scalingFactor,refBounds.width + 2 * (int) scalingFactor,refBounds.height + 2 * (int) scalingFactor);
		repaint(previousRect);
	}

	private void drawOffIcon(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		Rectangle bounds=selectedShape.getBounds();
		g2.drawImage(draggedIcon.getImage(),bounds.x,bounds.y,null);
		g2.dispose();
	}

	public void dragSelection(int x,int y) {
		isDragging=true;
		dragX=toOrigImCoords(x);
		dragY=toOrigImCoords(y);

		repaint(previousRect);
		Shape refreshShape=worldToLocal.createTransformedShape(selectedShape);
		Rectangle r=refreshShape.getBounds();
		previousRect=new Rectangle(r.x - 4,r.y - 4,r.width + 8,r.height + 8);
		repaint(previousRect);
	}

	private void drawSelectedImage(Graphics g) {
		if (dragImage != null) {
			Graphics2D g2=(Graphics2D) g;

			// clear the original area that is left behind
			Graphics2D gg=(Graphics2D) canvasImage.createGraphics();
			gg.setColor(drawColor);
			gg.setColor(backColor);
			if (backColor.getAlpha() == 0)
				gg.setComposite(AlphaComposite.Clear);
			gg.fill(initialShape);
			gg.setComposite(AlphaComposite.SrcOver);

			// g2.setColor(backColor);
			// g2.fill(initialShape);

			if (paste) {
				Rectangle bounds=initialShape.getBounds();
				g2.drawImage(backupPasteImage,bounds.x,bounds.y,null);
			}

			g2.drawImage(dragImage,dragX - grabPoint.x,dragY - grabPoint.y,null);
			g2.dispose();
		}
	}

	private RectPoints findProperRectPoints(int x1,int y1,int x2,int y2) {
		int fromX=Math.min(x1,x2);
		int toX=Math.max(x1,x2);
		int fromY=Math.min(y1,y2);
		int toY=Math.max(y1,y2);
		return new RectPoints(fromX,fromY,toX,toY);
	}

	private Rectangle findProperRect(int x1,int y1,int x2,int y2) {
		int fromX=Math.min(x1,x2);
		int toX=Math.max(x1,x2);
		int fromY=Math.min(y1,y2);
		int toY=Math.max(y1,y2);
		return new Rectangle(fromX - strokeWidth * (int) scalingFactor,fromY - strokeWidth * (int) scalingFactor,toX + 2 * strokeWidth * (int) scalingFactor,toY + 2 * strokeWidth * (int) scalingFactor);
	}

	private Rectangle findProperRect(RectPoints rp) {
		return new Rectangle(rp.x1 - strokeWidth * (int) scalingFactor,rp.y1 - strokeWidth * (int) scalingFactor,rp.x2 - rp.x1 + 2 * strokeWidth * (int) scalingFactor,rp.y2 - rp.y1 + 2 * strokeWidth * (int) scalingFactor);
	}

	public void setCanvasBackground(Color oldColor,Color newColor) {
		for (int i=0;i < canvasImage.getWidth();i++)
			for (int j=0;j < canvasImage.getHeight();j++)
				if (canvasImage.getRGB(i,j) == oldColor.getRGB())
					canvasImage.setRGB(i,j,newColor.getRGB());
		repaint();
	}

	public void deselectAll() {
		if (selectedShape == null) {
			existsSelection=false;
			offSelect=false;
			repaint();
			return;
		}
		if (existsSelection) {
			existsSelection=false;
			offSelect=false;

			Shape refreshShape=worldToLocal.createTransformedShape(selectedShape);
			Rectangle bounds=refreshShape.getBounds();
			selectedShape=null;
			repaint(bounds.x - 1,bounds.y - 1,bounds.width + 2,bounds.height + 2);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		g2.setPaint(tp);
		g2.fill(new Rectangle(0,0,toTransImCoords(canvasImage.getWidth()),toTransImCoords(canvasImage.getHeight())));

		existingTransform=g2.getTransform();
		existingTransform.concatenate(worldToLocal);
		g2.setTransform(existingTransform);
		g2.drawImage(canvasImage,0,0,null);

		if (offLine)
			drawOffLine(g2);
		else if (offRect)
			drawOffRect(g2);
		else if (offEllipse)
			drawOffEllipse(g2);
		else if (offSelect)
			drawOffSelection(g2);
		else if (dragIcon)
			drawOffIcon(g2);
		else if (isDragging) {
			drawSelectedImage(g2);
			double xOffset=dragX - grabPoint.x - selectedShape.getBounds().x;
			double yOffset=dragY - grabPoint.y - selectedShape.getBounds().y;
			AffineTransform moveTransform=AffineTransform.getTranslateInstance(xOffset,yOffset);
			selectedShape=moveTransform.createTransformedShape(selectedShape);
		}
		if (existsSelection)
			drawSelectedShapes(g2);
	}

	private int toOrigImCoords(int coord) {
		return coord / (int) scalingFactor;
	}

	private int toTransImCoords(int coord) {
		return coord * (int) scalingFactor;
	}

	public BufferedImage getImage() {
		return canvasImage;
	}

	// loads the indicated by the given string icon
	private ImageIcon loadImageIcon(String filename,String description) {
		try {
			URL u=this.getClass().getResource(filename);
			if (u != null)
				return new ImageIcon(u,description);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}// end of loadImageIcon

}

class RectPoints {
	int x1, x2, y1, y2;

	public RectPoints(int x1,int y1,int x2,int y2) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
}
