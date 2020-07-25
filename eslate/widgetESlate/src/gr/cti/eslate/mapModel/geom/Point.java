package gr.cti.eslate.mapModel.geom;

import gr.cti.eslate.protocol.IPoint;
import gr.cti.eslate.protocol.IVectorGeographicObject;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * A Point geographic object represents a point coordinate in the plane.
 * @author  Giorgos Vasiliou
 * @version 1.0, 15-May-2002
 */
public abstract class Point extends VectorGeographicObject implements IPoint {

	public Point() {
		id=0;
		selected=false;
	}
	public boolean equals(Object o) {
		return o==this;
	}

	/**
	 * Gets the X coordinate of the Point.
	 */
	public abstract double getX();
	/**
	 * Gets the Y coordinate of the Point.
	 */
	public abstract double getY();
	/**
	 * Sets the coordinates of the Point.
	 */
	public abstract void setXY(double x,double y);
	/**
	 * Calculates the minimum distance between the given Point and the geographic object.
	 */
	public double calculateDistance(Point2D p) {
		return Math.sqrt(Math.pow((p.getX()-getX()),2)+Math.pow(p.getY()-getY(),2));
	}
	/**
	 * Sets the object bounding rectangle top-left point.
	 */
	public void setBoundingMin(double x,double y) {
	}
	/**
	 * Sets the object bounding rectangle top-left point.
	 */
	public void setBoundingMax(double x,double y) {
	}
	/**
	 * Gets the object bounding rectangle top-left point.
	 */
	public double getBoundingMinX() {
		return getX();
	}
	/**
	 * Gets the object bounding rectangle top-left point.
	 */
	public double getBoundingMinY() {
		return getY();
	}
	/**
	 * Gets the object bounding rectangle bottom-right point.
	 */
	public double getBoundingMaxX() {
		return getX();
	}
	/**
	 * Gets the object bounding rectangle bottom-right point.
	 */
	public double getBoundingMaxY() {
		return getY();
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
		if (r.contains(getX(),getY()))
			return true;
		else
			return false;
	}

	public boolean intersects(double x, double y, double w, double h) {
		if (getX()<x || getX()>(x+w) || getY()<y || getY()>(y+h))
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
		return new java.awt.geom.Ellipse2D.Double(getX()-3,getY()-3,7,7).getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new java.awt.geom.FlatteningPathIterator(getPathIterator(at), flatness);
	}



	/**
	 * Single precision Point object.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 15-May-2002
	 */
	public static class Float extends Point implements Externalizable {
		public Float() {
			super();
		}

		public Float(double x, double y, int id) {
			super();
			this.x=(float) x;
			this.y=(float) y;
			this.id=id;
		}

		public Float(double x, double y) {
			super();
			this.x=(float) x;
			this.y=(float) y;
		}

		public double getX() {
			return (double) x;
		}

		public double getY() {
			return (double) y;
		}

		public void setXY(double x,double y) {
			this.x=(float) x;
			this.y=(float) y;
		}
		/**
		 * Transforms the object.
		 */
		public IVectorGeographicObject createTransformedShape(java.awt.geom.AffineTransform at) {
			Point p;
			synchronized (reusable) {
				reusable[0]=x; reusable[1]=y;
				at.transform(reusable,0,reusable,0,1);
				p=new Point.Float(reusable[0],reusable[1],id);
				p.selected=selected;
			}
			return p;
		}
		/**
		 * Externalization input.
		 * @param in The input stream.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
			in.readByte(); //Save Version. Ignore for now.
			id=in.readInt();
			x=in.readFloat();
			y=in.readFloat();
			selected=in.readBoolean();
//			in.close();
		}
		/**
		 * Externalization output.
		 * @param out The ouput stream.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeByte(1); //Save Version
			out.writeInt(id);
			out.writeFloat(x);
			out.writeFloat(y);
			out.writeBoolean(selected);
//Structfile crashes			out.close();
		}

		private float x,y;
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID=7199459739102047367L;
	}



	/**
	 * Double precision Point object.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 15-May-2002
	 */
	public static class Double extends Point implements Externalizable {
		public Double() {
			super();
		}

		public Double(double x, double y, int id) {
			super();
			this.x=x;
			this.y=y;
			this.id=id;
		}

		public Double(double x, double y) {
			super();
			this.x=x;
			this.y=y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public void setXY(double x,double y) {
			this.x=x;
			this.y=y;
		}
		/**
		 * Transforms the object.
		 */
		public IVectorGeographicObject createTransformedShape(java.awt.geom.AffineTransform at) {
			Point p;
			synchronized (reusable) {
				reusable[0]=x; reusable[1]=y;
				at.transform(reusable,0,reusable,0,1);
				p=new Point.Double(reusable[0],reusable[1],id);
				p.selected=selected;
			}
			return p;
		}
		/**
		 * Externalization input.
		 * @param in The input stream.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
			in.readByte(); //Save Version. Ignore for now.
			id=in.readInt();
			x=in.readDouble();
			y=in.readDouble();
			selected=in.readBoolean();
//			in.close();
		}
		/**
		 * Externalization output.
		 * @param out The ouput stream.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeByte(1); //Save Version
			out.writeInt(id);
			out.writeDouble(x);
			out.writeDouble(y);
			out.writeBoolean(selected);
//Structfile crashes			out.close();
		}

		private double x,y;
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID=-2009884209918732424L;
	}




	private static double[] reusable=new double[2];
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID=-2009884209918732424L;
}