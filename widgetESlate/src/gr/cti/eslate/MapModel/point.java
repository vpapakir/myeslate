package gr.cti.eslate.mapModel;

import gr.cti.eslate.mapModel.geom.VectorGeographicObject;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class point extends VectorGeographicObject implements GeographicObject,Shape,java.io.Externalizable {

	public point() {
		x=y=id=0;
		visible=selected=true;
		boundingMin=new Point2D.Double();
		boundingMax=new Point2D.Double();
	}

	public point(double x1, double y1, int i) {
		x=x1;
		y=y1;
		id=i;
		visible=true;
		selected=false;
		boundingMin=new Point2D.Double(x,y);
		boundingMax=new Point2D.Double(x,y);
	}

	public point(double x1, double y1) {
		x=x1;
		y=y1;
		id=0;
		visible=true;
		selected=false;
		boundingMin=new Point2D.Double(x,y);
		boundingMax=new Point2D.Double(x,y);
	}
	/**
	 * Gets the object bounding rectangle bottom-right point.
	 */
	public double getBoundingMinX() {
		return boundingMin.x;
	}
	/**
	 * Gets the object bounding rectangle bottom-right point.
	 */
	public double getBoundingMinY() {
		return boundingMin.y;
	}
	/**
	 * Gets the object bounding rectangle bottom-right point.
	 */
	public double getBoundingMaxX() {
		return boundingMax.x;
	}
	/**
	 * Gets the object bounding rectangle bottom-right point.
	 */
	public double getBoundingMaxY() {
		return boundingMax.y;
	}
	/**
	 * Calculates the minimum distance between the given point and the geographic object.
	 */
	public double calculateDistance(Point2D p) {
		return Math.sqrt((p.getX()-x)*(p.getX()-x)+(p.getY()-y)*(p.getY()-y));
	}
	/**
	 * Externalization input.
	 * @param in The output stream.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		id=ht.get("id",0);
		x=ht.get("x",0d);
		y=ht.get("y",0d);
		visible=ht.get("visible",true);
		selected=ht.get("selected",false);
		boundingMin=new Point2D.Double(x,y);
		boundingMax=new Point2D.Double(x,y);
	}
	/**
	 * Externalization output.
	 * @param out The input stream.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("id",id);
		ht.put("x",x);
		ht.put("y",y);
		ht.put("visible",visible);
		ht.put("selected",selected);
		out.writeObject(ht);
		//out.flush();
	}
	/**
	 * Sets the object id which is its
	 * reference to the associated database table.
	 */
	public void setID(int i) {
		if (cacheIntShape!=null)
			((point) cacheIntShape).id=i;
		id=i;
	}
	/**
	 * Sets the object bounding rectangle top-left point.
	 */
	public void setBoundingMin(double x,double y) {
		cacheIntTrans=null;
		cacheIntShape=null;
		boundingMin.x=x;
		boundingMin.y=y;
	}
	/**
	 * Sets the object bounding rectangle top-left point.
	 */
	public void setBoundingMax(double x,double y) {
		cacheIntTrans=null;
		cacheIntShape=null;
		boundingMax.x=x;
		boundingMax.y=y;
	}
	/**
	 * Sets the X coordinate of the point.
	 */
	public void setX(double x) {
		cacheIntTrans=null;
		cacheIntShape=null;
		this.x=x;
	}
	/**
	 * Sets the Y coordinate of the point.
	 */
	public void setY(double y) {
		cacheIntTrans=null;
		cacheIntShape=null;
		this.y=y;
	}
	/**
	 * Gets the X coordinate of the point.
	 */
	public double getX() {
		return x;
	}
	/**
	 * GSets the Y coordinate of the point.
	 */
	public double getY() {
		return y;
	}
	/**
	 * Sets the coordinates of the point.
	 */
	public void setXY(double x,double y) {
		cacheIntTrans=null;
		cacheIntShape=null;
		this.x=x;
		this.y=y;
	}
	/**
	 * Transforms the object into an object with integer coordinates. Furthermore, useless points
	 * of the shape are cut off, i.e. points that would not change the resulting shape.
	 * This representation is very useful and accelerates significantly drawing in the screen.
	 */
	public IVectorGeographicObject createIntegerTransformedShape(AffineTransform at) {
		//If this transformation is the previous one used, return the cached shape
		if (at.equals(cacheIntTrans)) {
			cacheIntShape.selected=selected;
			cacheIntShape.visible=visible;
			return cacheIntShape;
		}
		//Otherwise calculate the shape and put it in the cache.
		else {
			point p;
			synchronized (reusable) {
				reusable[0]=x; reusable[1]=y;
				at.transform(reusable,0,reusable,0,1);
				p=new point((int) reusable[0],(int) reusable[1],id);
				p.selected=selected;
				p.visible=visible;
				cacheIntTrans=at;
				cacheIntShape=p;
			}
			return p;
		}
	}
	/**
	 * Transforms the object.
	 */
	public IVectorGeographicObject createTransformedShape(java.awt.geom.AffineTransform at) {
		point p;
		synchronized (reusable) {
			reusable[0]=x; reusable[1]=y;
			at.transform(reusable,0,reusable,0,1);
			p=new point(reusable[0],reusable[1],id);
			p.selected=selected;
			p.visible=visible;
		}
		return p;
	}

	public java.awt.Rectangle getBounds() {
		return null;
	}

	public Rectangle2D getBounds2D() {
		return null;
	}

	public boolean contains(double x, double y) {
		return false;
	}

	public boolean contains(Point2D p) {
		return false;
	}

	public boolean intersects(Rectangle2D r) {
		if (r.contains(x,y))
			return true;
		else
			return false;
	}

	public boolean intersects(double x, double y, double w, double h) {
		if (this.x<x || this.x>(x+w) || this.y<y || this.y>(y+h))
			return false;
		return true;
	}

	public boolean contains(double x, double y, double w, double h) {
		return false;
	}

	public boolean contains(Rectangle2D r) {
		return false;
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return new java.awt.geom.Ellipse2D.Double(x-3,y-3,7,7).getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new java.awt.geom.FlatteningPathIterator(getPathIterator(at), flatness);
	}

	/**
	 * The serial version UID.
	 */
	static final long serialVersionUID=3000L;
	double x,y;
	private boolean visible;
	private Point2D.Double boundingMin,boundingMax;
	/**
	 * Caches the last transformation used to produce a transformed shape. If the same transformed
	 * is needed, it is returned by the cache.
	 */
	private AffineTransform cacheIntTrans;
	/**
	 * Caches the last transformed shape transformed by cacheIntTrans.
	 */
	private point cacheIntShape;
	private static double[] reusable=new double[2];
}