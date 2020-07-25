package gr.cti.eslate.agent;

import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.utils.ESlateHashtable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.JDialog;

/**
 * A path segment is a segment of an agent path.
 * It has information about how it should be painted. To save memory,
 * disk usage and persistence time the paint information may be null,
 * indicating that this segment should be painted with the default parameters
 * or using the previous segment parameters.
 * <p>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 26-Apr-2000
 * @see		gr.cti.eslate.agent.Path
 */

public class PathSegment implements Externalizable, Shape {
	public PathSegment() {
		strokeAs=STROKE_AS_STRAIGHT_LINE;
		paintAs=PAINT_AS_SOLID_COLOR;
		width=2;
		lastx=lasty=Double.MAX_VALUE;
		solidColor=new Color(255,0,0,180);
		gradientStart=new Color(255,0,0,180);
		gradientEnd=new Color(255,255,255,180);
		shape=new GeneralPath();
		createStroke(false);
	}

	public PathSegment(int width) {
		this();
		this.width=width;
		createStroke(false);
	}

	public PathSegment(int width,Color solidColor) {
		this();
		this.width=width;
		this.solidColor=solidColor;
		strokeAs=STROKE_AS_STRAIGHT_LINE;
		paintAs=PAINT_AS_SOLID_COLOR;
		createStroke(false);
	}

	public PathSegment(int width,Color solidColor,int strokeType) {
		this();
		if (strokeType!=STROKE_AS_STRAIGHT_LINE && strokeType!=STROKE_AS_DOTTED_LINE && strokeType!=STROKE_AS_POINTS)
			throw new IllegalArgumentException("Not a valid stroke mode for the path segment.");
		this.width=width;
		this.solidColor=solidColor;
		strokeAs=strokeType;
		paintAs=PAINT_AS_SOLID_COLOR;
		createStroke(false);
	}

	public PathSegment(int width,Color gradientStart,Color gradientEnd) {
		this();
		this.width=width;
		this.gradientStart=gradientStart;
		this.gradientEnd=gradientEnd;
		strokeAs=STROKE_AS_STRAIGHT_LINE;
		paintAs=PAINT_AS_GRADIENT_COLOR;
		createStroke(false);
	}

	public PathSegment(int width,Color gradientStart,Color gradientEnd,int strokeType) {
		this();
		if (strokeType!=STROKE_AS_STRAIGHT_LINE && strokeType!=STROKE_AS_DOTTED_LINE && strokeType!=STROKE_AS_POINTS)
			throw new IllegalArgumentException("Not a valid stroke mode for the path segment.");
		this.width=width;
		this.gradientStart=gradientStart;
		this.gradientEnd=gradientEnd;
		strokeAs=strokeType;
		paintAs=PAINT_AS_GRADIENT_COLOR;
		createStroke(false);
	}
	/**
	 * Needs the agent that has the segment for synchorization purposes.
	 * @return  A frame with properties for the segment.
	 */
	public JDialog getPropertiesDialog(IAgent a) {
		return new PathSegmentProperties(this,a);
	}
	/**
	 * Informs the path that there has been a change in the properties of the segment.
	 */
	private void firePathSegmentPropertiesChanged() {
		if (path!=null)
			path.segmentPropertiesChanged(this);
	}
	/**
	 * Sets the path the segment belongs to.
	 */
	void setPath(Path path) {
		this.path=path;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Sets the name of the segment.
	 */
	public void setName(String name) {
		nm=name;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the name of the segment.
	 */
	public String getName() {
		return nm;
	}
	/**
	 * Sets the width.
	 */
	public void setWidth(int i) {
		width=i;
		createStroke(true);
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the width.
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Sets the solid color.
	 */
	public void setSolidColor(Color color) {
		solidColor=color;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the solid color.
	 */
	public Color getSolidColor() {
		return solidColor;
	}
	/**
	 * Sets the gradient start color.
	 */
	public void setGradientStart(Color color) {
		gradientStart=color;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the gradient start color.
	 */
	public Color getGradientStart() {
		return gradientStart;
	}
	/**
	 * Sets the gradient end color.
	 */
	public void setGradientEnd(Color color) {
		gradientEnd=color;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the gradient end color.
	 */
	public Color getGradientEnd() {
		return gradientEnd;
	}
	/**
	 * Sets both the gradient colors.
	 */
	public void setGradientColors(Color start,Color end) {
		gradientStart=start;
		gradientEnd=end;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Sets the stroke mode.
	 * @param i One of STROKE_AS_STRAIGHT_LINE, STROKE_AS_DOTTED_LINE.
	 */
	public void setStrokeAs(int i) {
		if (i!=STROKE_AS_STRAIGHT_LINE && i!=STROKE_AS_DOTTED_LINE && i!=STROKE_AS_POINTS)
			throw new IllegalArgumentException("Not a valid stroke mode for the path segment.");
		strokeAs=i;
		createStroke(true);
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the stroke mode.
	 * One of STROKE_AS_STRAIGHT_LINE, STROKE_AS_DOTTED_LINE, STROKE_AS_POINTS.
	 */
	public int getStrokeAs() {
		 return strokeAs;
	}
	/**
	 * Sets the paint mode.
	 * @param i One of PAINT_AS_SOLID_COLOR, PAINT_AS_GRADIENT_COLOR.
	 */
	public void setPaintAs(int i) {
		if (i!=PAINT_AS_SOLID_COLOR && i!=PAINT_AS_GRADIENT_COLOR)
			throw new IllegalArgumentException("Not a valid paint mode for the path segment.");
		paintAs=i;
		firePathSegmentPropertiesChanged();
	}
	/**
	 * Gets the paint mode.
	 * One of PAINT_AS_SOLID_COLOR, PAINT_AS_GRADIENT_COLOR.
	 */
	public int getPaintAs() {
		 return paintAs;
	}
	/**
	 * The stroke used to paint the segment.
	 */
	public Stroke getStroke() {
		return stroke;
	}
	/**
	 * The PaintContext used to paint the segment.
	 */
	public Paint getPaint() {
		if (paintAs==PAINT_AS_SOLID_COLOR)
			paint=solidColor;
		else if (paintAs==PAINT_AS_GRADIENT_COLOR) {
			paint=new GradientPaint(0,0,gradientStart,100,100,gradientEnd,true);
		}
		return paint;
	}

	private void createStroke(boolean fireEvent) {
		if (strokeAs==STROKE_AS_DOTTED_LINE)
			stroke=new BasicStroke((float) width,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[]{width,3*width},0);
		else
			stroke=new BasicStroke(width);
	}

	/**
	 * Adds a point to the segment. If there is no space, the space is incremented by POINT_ARRAY_GROWTH.
	 * The growth accurs in a thread to avoid blocking the system.
	 */
	public void addPoint(double x,double y) {
		//Eliminate useless points
		if (x==lastx && y==lasty)
			return;
		if (hasPoints())
			shape.lineTo((float) x,(float) y);
		else
			shape.moveTo((float) x,(float) y);
		lastx=x; lasty=y;
	}
	/**
	 * @return  <em>True</em>, if the segment has at least one point.
	 */
	public boolean hasPoints() {
		return (shape.getCurrentPoint()!=null);
	}
	/**
	 * Shape interface method.
	 */
	public Rectangle getBounds() {
		return shape.getBounds();
	}
	/**
	 * Shape interface method.
	 */
	public Rectangle2D getBounds2D() {
		return shape.getBounds2D();
	}
	/**
	 * Shape interface method.
	 */
	public boolean contains(double x, double y) {
		return shape.contains(x,y);
	}
	/**
	 * Shape interface method.
	 */
	public boolean contains(Point2D p) {
		return shape.contains(p);
	}
	/**
	 * Shape interface method.
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return shape.intersects(x,y,w,h);
	}
	/**
	 * Shape interface method.
	 */
	public boolean intersects(Rectangle2D r) {
		return shape.intersects(r);
	}
	/**
	 * Shape interface method.
	 */
	public boolean contains(double x, double y, double w, double h) {
		return shape.contains(x,y,w,h);
	}
	/**
	 * Shape interface method.
	 */
	public boolean contains(Rectangle2D r) {
		return shape.contains(r);
	}
	/**
	 * Shape interface method.
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return shape.getPathIterator(at);
	}
	/**
	 * Shape interface method.
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return shape.getPathIterator(at,flatness);
	}

	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		ESlateHashtable ht=(ESlateHashtable) in.readObject();
		width=ht.get("width",width);
		strokeAs=ht.get("strokeAs",strokeAs);
		paintAs=ht.get("paintAs",paintAs);
		solidColor=ht.get("solidcolor",solidColor);
		gradientStart=ht.get("gradientstart",gradientStart);
		gradientEnd=ht.get("gradientend",gradientEnd);
		int[] parts=(int[]) ht.get("parts",(int[]) null);
		double[][] points=(double[][]) ht.get("points",(double[][]) null);
		//Reconstruct the shape
		int pp=0;
		shape=new GeneralPath();
		for (int i=0;i<points.length;i++) {
			if (i==parts[pp]) {
				((GeneralPath) shape).moveTo((float) points[i][0],(float) points[i][1]);
				//No Array index out of bounds will occur.
				if ((++pp)==parts.length) pp=0;
			} else
				((GeneralPath) shape).lineTo((float) points[i][0],(float) points[i][1]);
		}
		createStroke(false);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateHashtable ht=new ESlateHashtable();
		ht.put("width",width);
		ht.put("strokeAs",strokeAs);
		ht.put("paintAs",paintAs);
		ht.put("solidcolor",solidColor);
		ht.put("gradientstart",gradientStart);
		ht.put("gradientend",gradientEnd);
		//**Save the shape**
		int[] parts;
		double[][] points;
		int prts=0;
		int pnts=0;
		double[] d=new double[6];
		//Find how many points and parts there are
		PathIterator it=shape.getPathIterator(new AffineTransform());
		while (!it.isDone()) {
			pnts++;
			if (it.currentSegment(d)==PathIterator.SEG_MOVETO)
				prts++;
			it.next();
		}
		parts=new int[prts];
		points=new double[pnts][2];
		int type; pnts=0; prts=0;
		it=shape.getPathIterator(new AffineTransform());
		while (!it.isDone()) {
			type=it.currentSegment(d);
			points[pnts][0]=d[0];
			points[pnts++][1]=d[1];
			if (type==PathIterator.SEG_MOVETO)
				parts[prts++]=pnts-1;
			it.next();
		}
		//**Shape defined**
		ht.put("parts",parts);
		ht.put("points",points);
		out.writeObject(ht);
	}

	//Variable declaration
	private Path path;
	private String nm;
	private Paint paint;
	private Stroke stroke;
	private int width;
	private int strokeAs;
	private int paintAs;
	private Color solidColor,gradientStart,gradientEnd;
	private GeneralPath shape;
	double lastx,lasty;
	public static final int STROKE_AS_STRAIGHT_LINE=0;
	public static final int STROKE_AS_DOTTED_LINE=1;
	public static final int STROKE_AS_POINTS=2;
	public static final int PAINT_AS_SOLID_COLOR=0;
	public static final int PAINT_AS_GRADIENT_COLOR=1;
	/**
	 * The serial version UID for persistence.
	 */
	private final static long serialVersionUID=1000L;
}
