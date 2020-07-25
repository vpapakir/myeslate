package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IVectorGeographicObject;

import java.awt.geom.Point2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Deprecated class. java.awt.geom.Rectangle2D is used instead.
 */
public class Rectangle extends java.awt.geom.Rectangle2D.Double implements IVectorGeographicObject,Externalizable {
	public Rectangle() {
		x=y=width=height=0;
		visible=true;
		selected=false;
		id=-1;
		boundingMin=new Point2D.Double();
		boundingMax=new Point2D.Double();
	}
	/**
	 * Normal constructor.
	 */
	public Rectangle(double x,double y,double width,double height) {
		super();
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	/**
	 * Calculates the minimum distance between the given point and the geographic object.
	 */
	public double calculateDistance(java.awt.geom.Point2D p) {
		//TODO: Implement this method
		return 0;
	}
	/**
	 * Sets the visibility of the object.
	 * @param value The new visibility status.
	 */
	public void setVisible(boolean value) {
		visible=value;
	}
	/**
	 * Gets the visibility property.
	 * @return The visibility property.
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * Sets the selected state of the object.
	 * @param value The new selected status.
	 */
	public void setSelected(boolean value) {
		selected=value;
	}
	/**
	 * Gets the selected property.
	 * @return The selected property.
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * Sets the object id which is its
	 * reference to the associated database table.
	 */
	public void setID(int i) {
		id=i;
	}
	/**
	 * Gets the object id which is its
	 * reference to the associated database table.
	 */
	public int getID() {
		return id;
	}
	/**
	 * Sets the object bounding rectangle top-left point.
	 */
	public void setBoundingMin(double x,double y) {
		boundingMin.x=x;
		boundingMin.y=y;
	}
	/**
	 * Sets the object bounding rectangle top-left point.
	 */
	public void setBoundingMax(double x,double y) {
		boundingMax.x=x;
		boundingMax.y=y;
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
	 * Transforms the object into an object with integer coordinates. Furthermore, useless points
	 * of the shape are cut off, i.e. points that would not change the resulting shape.
	 * This representation is very useful and accelerates significantly drawing in the screen.
	 */
	public IVectorGeographicObject createIntegerTransformedShape(java.awt.geom.AffineTransform at) {
		double[] a=new double[]{x,y,x+width,y+height};
		double[] b=new double[4];
		at.transform(a,0,b,0,2);
		return new Rectangle((int) Math.min(b[0],b[2]),(int) Math.min(b[1],b[3]),(int) Math.abs(b[2]-b[0]),(int) Math.abs(b[3]-b[1]));
	}

	public IVectorGeographicObject createTransformedShape(java.awt.geom.AffineTransform at) {
		double[] a=new double[]{x,y,x+width,y+height};
		double[] b=new double[4];
		at.transform(a,0,b,0,2);
		return new Rectangle(Math.min(b[0],b[2]),Math.min(b[1],b[3]),Math.abs(b[2]-b[0]),Math.abs(b[3]-b[1]));
	}
	/**
	 * Externalization input.
	 * @param in The output stream.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		x=((java.lang.Double) in.readObject()).doubleValue();
		y=((java.lang.Double) in.readObject()).doubleValue();
		width=((java.lang.Double) in.readObject()).doubleValue();
		height=((java.lang.Double) in.readObject()).doubleValue();
	}
	/**
	 * Externalization output.
	 * @param out The input stream.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(new java.lang.Double(x));
		out.writeObject(new java.lang.Double(y));
		out.writeObject(new java.lang.Double(width));
		out.writeObject(new java.lang.Double(height));
	}

	private int id;
	private Point2D.Double boundingMin,boundingMax;
	private boolean visible,selected;
	/**
	 * The serial version UID.
	 */
	static final long serialVersionUID=3000L;
}