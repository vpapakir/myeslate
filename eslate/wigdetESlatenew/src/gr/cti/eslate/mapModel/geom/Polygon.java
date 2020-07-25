package gr.cti.eslate.mapModel.geom;

import gr.cti.eslate.protocol.IPolygon;
import gr.cti.eslate.protocol.IVectorGeographicObject;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import sun.awt.geom.Crossings;

/**
 * A Polygon object represents an area (a closed path) or a group of areas
 * (a group of closed paths) in the plane.
 */
public abstract class Polygon extends VectorGeographicObject implements IPolygon {
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	//TODO: This class must be thread safe as Polygon!!!!!!!!!!!!!!!!!!!!!
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	/**
	 * No arg-constructor for externalization.
	 */
	public Polygon() {
		id=0;
		selected=false;
	}

	public boolean equals(Object o) {
		return o==this;
	}
	/**
	 * Calculates the minimum distance between the given point and the geographic object.
	 */
	public double calculateDistance(Point2D p) {
		if (contains(p))
			return 0;
		else
			return Math.sqrt(Math.pow(p.getX()-(getBoundingMinX()+(getBoundingMaxX()-getBoundingMinX())/2),2)+Math.pow(p.getY()-(getBoundingMinY()+(getBoundingMaxY()-getBoundingMinY())/2),2));
	}

	public Rectangle getBounds() {
		return new Rectangle((int) getBoundingMinX(),(int) getBoundingMinY(),(int) (getBoundingMaxX()-getBoundingMinX()),(int) (getBoundingMaxY()-getBoundingMinY()));
	}

	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(getBoundingMinX(),getBoundingMinY(),getBoundingMaxX()-getBoundingMinX(),getBoundingMaxY()-getBoundingMinY());
	}
	/**
	 * The PathIterator can cut down points to speed up drawing. This value adjusts
	 * the quality and preciseness of the shape in relavance to drawing speed.
	 * The greater the value, the more points are being cut down to speed up drawing, the less accurate is the shape.
	 * The smaller the value, the lesser points are being cut down to speed up drawing, the more accurate is the shape.
	 * If 0, no point is cut down.
	 */
	public static void setErrorTolerance(float error) {
		errorTolerance=error;
	}
	/**
	 * All Polygons share a static iterator to improve performance.
	 * If the Iterator is not run until the end (until isDone() returns true)
	 * the user of the Iterator MUST call unlockIterator(), otherwise a deadlock
	 * will occur.
	 * @param at    The transformation.
	 * @return  A PathIterator.
	 */
	public PathIterator getPathIterator(AffineTransform at,double flatness) {
		return getPathIterator(at);
	}
	public boolean contains(double x,double y) {
		int cross=MathUtils.pointCrossingsForPath(getPathIterator(null),x,y);
		unlockIterator();
		return ((cross & 1)!=0);
	}

	public boolean contains(Point2D p) {
		return contains(p.getX(),p.getY());
	}

	public boolean intersects(double x,double y,double w,double h) {
		Crossings c=Crossings.findCrossings(getPathIterator(null),x,y,x+w,y+h);
		unlockIterator();
		return (c==null || !c.isEmpty());
	}

	public boolean intersects(Rectangle2D r) {
		return intersects(r.getX(),r.getY(),r.getWidth(),r.getHeight());
	}

	public boolean contains(double x,double y,double w,double h) {
		Crossings c=Crossings.findCrossings(getPathIterator(null),x,y,x+w,y+h);
		unlockIterator();
		return (c!=null && c.covers(y,y+h));
	}

	public boolean contains(Rectangle2D r) {
		return contains(r.getX(),r.getY(),r.getWidth(),r.getHeight());
	}

	/**
	 * This method must be called after finishing with getPathIterator by
	 * whichever class it uses it. For now, if isDone() is called, unlockIterator() is
	 * automatically called before isDone() returns true.
	 */
	static public void unlockIterator() {
		iterator.lock=false;
	}


	/**
	 * Single precision Polygon object.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 15-May-2002
	 */
	public static class Float extends Polygon implements Externalizable {
		public Float() {
			super();
		}

		public Float(int id,int[] parts,float[] points,float boundingMinX,float boundingMinY,float boundingMaxX,float boundingMaxY) {
			this.id=id;
			selected=false;
			this.boundingMinX=boundingMinX;
			this.boundingMinY=boundingMinY;
			this.boundingMaxX=boundingMaxX;
			this.boundingMaxY=boundingMaxY;
			this.parts=parts;
			this.points=points;
		}

		public void setBoundingMin(double x,double y) {
			boundingMinX=(float) x;
			boundingMinY=(float) y;
		}

		public void setBoundingMax(double x,double y) {
			boundingMaxX=(float) x;
			boundingMaxY=(float) y;
		}

		public double getBoundingMinX() {
			return boundingMinX;
		}

		public double getBoundingMinY() {
			return boundingMinY;
		}

		public double getBoundingMaxX() {
			return boundingMaxX;
		}

		public double getBoundingMaxY() {
			return boundingMaxY;
		}

		/**
		 * All Polygons share a static iterator to improve performance.
		 * If the Iterator is not run until the end (until isDone() returns true)
		 * the user of the Iterator MUST call unlockIterator(), otherwise a deadlock
		 * will occur.
		 */
		public PathIterator getPathIterator(AffineTransform at) {
			//Produces a new iterator when the static is found in use.
			//It is necessary in thread environments
			boolean produceNew=false;
			if (iterator!=null && iterator.lock)
				produceNew=true;
			if (at!=null) {
				if (fWTPolygonIterator==null || produceNew)
					fWTPolygonIterator=new PolygonIterator.FloatWithTransform();
				iterator=fWTPolygonIterator;
				fWTPolygonIterator.points=points;
			} else {
				if (fNTPolygonIterator==null || produceNew)
					fNTPolygonIterator=new PolygonIterator.FloatNoTransform();
				iterator=fNTPolygonIterator;
				fNTPolygonIterator.points=points;
			}
			iterator.lock=true;
			iterator.idx=0;
			iterator.prtidx=0;
			iterator.endidx=points.length-2;
			iterator.partStop=2*parts[0];
			iterator.prePartStop=-1;
			iterator.at=at;
			iterator.parts=parts;
			iterator.errorTolerance=errorTolerance;
			if (at!=null) {
				fWTPolygonIterator.initializeIterator();
			}
			return iterator;
		}

		public synchronized IVectorGeographicObject createTransformedShape(AffineTransform at) {
			float[] newpoints=new float[points.length];
			System.arraycopy(points,0,newpoints,0,points.length);
			int[] newparts=new int[parts.length];
			System.arraycopy(parts,0,newparts,0,parts.length);
			at.transform(newpoints,0,newpoints,0,newpoints.length);

			PolyLine.Float p=new PolyLine.Float(id,newparts,newpoints,0,0,0,0);
			p.selected=selected;
			reusable[0]=(double) boundingMinX; reusable[1]=(double) boundingMinY;
			at.transform(reusable,0,reusable,0,1);
			p.setBoundingMin(reusable[0],reusable[1]);
			reusable[0]=(double) boundingMaxX; reusable[1]=(double) boundingMaxY;
			at.transform(reusable,0,reusable,0,1);
			p.setBoundingMax(reusable[0],reusable[1]);
			return p;
		}
		/**
		 * Externalization input.
		 * @param in The input stream.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
			//Read version
			byte version=in.readByte();
			id=in.readInt();
			//Read bounds
			boundingMinX=in.readFloat();
			boundingMinY=in.readFloat();
			boundingMaxX=in.readFloat();
			boundingMaxY=in.readFloat();
			//Read parts
			int noParts=in.readInt();
			parts=new int[noParts];
			if (version==1)
				for (int i=0;i<noParts;i++)
					parts[i]=in.readInt()*2;
			else
				for (int i=0;i<noParts;i++)
					parts[i]=in.readInt();
			//Read points
			int noPoints=in.readInt();
			points=new float[noPoints];
			for (int i=0;i<noPoints;i++)
				points[i]=in.readFloat();
			//Read misc
			selected=in.readBoolean();

//			in.close();
		}
		/**
		 * Externalization output.
		 * @param out The output stream.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			//Save version
			//1:Initial
			//2:Parts are doubled to show actual positions in points array
			out.writeByte(2);
			out.writeInt(id);
			//Save bounds
			out.writeFloat(boundingMinX);
			out.writeFloat(boundingMinY);
			out.writeFloat(boundingMaxX);
			out.writeFloat(boundingMaxY);
			//Save parts
			out.writeInt(parts.length);
			for (int i=0;i<parts.length;i++)
				out.writeInt(parts[i]);
			//Save points
			out.writeInt(points.length);
			for (int i=0;i<points.length;i++)
				out.writeFloat(points[i]);
			//Save misc
			out.writeBoolean(selected);
//Structfile crashes			out.close();
		}


		public float[] points;
		private float boundingMinX,boundingMinY,boundingMaxX,boundingMaxY;
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID=2522705097747390738L;
	}



	/**
	 * Double precision Polygon object.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 17-May-2002
	 */
	public static class Double extends Polygon implements Externalizable {
		public Double() {
			super();
		}

		public Double(int id,int[] parts,double[] points,double boundingMinX,double boundingMinY,double boundingMaxX,double boundingMaxY) {
			this.id=id;
			selected=false;
			this.boundingMinX=boundingMinX;
			this.boundingMinY=boundingMinY;
			this.boundingMaxX=boundingMaxX;
			this.boundingMaxY=boundingMaxY;
			this.parts=parts;
			this.points=points;
		}

		public void setBoundingMin(double x,double y) {
			boundingMinX=x;
			boundingMinY=y;
		}

		public void setBoundingMax(double x,double y) {
			boundingMaxX=x;
			boundingMaxY=y;
		}

		public double getBoundingMinX() {
			return boundingMinX;
		}

		public double getBoundingMinY() {
			return boundingMinY;
		}

		public double getBoundingMaxX() {
			return boundingMaxX;
		}

		public double getBoundingMaxY() {
			return boundingMaxY;
		}
		/**
		 * All Polygons share a static iterator to improve performance.
		 * If the Iterator is not run until the end (until isDone() returns true)
		 * the user of the Iterator MUST call unlockIterator(), otherwise a deadlock
		 * will occur.
		 */
		public PathIterator getPathIterator(AffineTransform at) {
			//Produces a new iterator when the static is found in use.
			//It is necessary in thread environments
			boolean produceNew=false;
			if (iterator!=null && iterator.lock)
				produceNew=true;
			if (at!=null) {
				if (dWTPolygonIterator==null || produceNew)
					dWTPolygonIterator=new PolygonIterator.DoubleWithTransform();
				iterator=dWTPolygonIterator;
				dWTPolygonIterator.points=points;
			} else {
				if (dNTPolygonIterator==null || produceNew)
					dNTPolygonIterator=new PolygonIterator.DoubleNoTransform();
				iterator=dNTPolygonIterator;
				dNTPolygonIterator.points=points;
			}
			iterator.lock=true;
			iterator.idx=0;
			iterator.prtidx=0;
			iterator.endidx=points.length-2;
			iterator.partStop=2*parts[0];
			iterator.prePartStop=-1;
			iterator.at=at;
			iterator.parts=parts;
			iterator.errorTolerance=errorTolerance;
			if (at!=null) {
				dWTPolygonIterator.initializeIterator();
			}
			return iterator;
		}

		public synchronized IVectorGeographicObject createTransformedShape(AffineTransform at) {
			double[] newpoints=new double[points.length];
			System.arraycopy(points,0,newpoints,0,points.length);
			int[] newparts=new int[parts.length];
			System.arraycopy(parts,0,newparts,0,parts.length);
			at.transform(newpoints,0,newpoints,0,newpoints.length);

			PolyLine.Double p=new PolyLine.Double(id,newparts,newpoints,0,0,0,0);
			p.selected=selected;
			reusable[0]=(double) boundingMinX; reusable[1]=(double) boundingMinY;
			at.transform(reusable,0,reusable,0,1);
			p.setBoundingMin(reusable[0],reusable[1]);
			reusable[0]=(double) boundingMaxX; reusable[1]=(double) boundingMaxY;
			at.transform(reusable,0,reusable,0,1);
			p.setBoundingMax(reusable[0],reusable[1]);
			return p;
		}
		/**
		 * Externalization input.
		 * @param in The input stream.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
			//Read version
			byte version=in.readByte();
			id=in.readInt();
			//Read bounds
			boundingMinX=in.readDouble();
			boundingMinY=in.readDouble();
			boundingMaxX=in.readDouble();
			boundingMaxY=in.readDouble();
			//Read parts
			int noParts=in.readInt();
			parts=new int[noParts];
			if (version==1)
				for (int i=0;i<noParts;i++)
					parts[i]=in.readInt()*2;
			else
				for (int i=0;i<noParts;i++)
					parts[i]=in.readInt();
			//Read points
			int noPoints=in.readInt();
			points=new double[noPoints];
			for (int i=0;i<noPoints;i++)
				points[i]=in.readDouble();
			//Read misc
			selected=in.readBoolean();

//			in.close();
		}
		/**
		 * Externalization output.
		 * @param out The output stream.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			//Save version
			//1:Initial
			//2:Parts are doubled to show actual positions in points array
			out.writeByte(2);
			out.writeInt(id);
			//Save bounds
			out.writeDouble(boundingMinX);
			out.writeDouble(boundingMinY);
			out.writeDouble(boundingMaxX);
			out.writeDouble(boundingMaxY);
			//Save parts
			out.writeInt(parts.length);
			for (int i=0;i<parts.length;i++)
				out.writeInt(parts[i]);
			//Save points
			out.writeInt(points.length);
			for (int i=0;i<points.length;i++)
				out.writeDouble(points[i]);
			//Save misc
			out.writeBoolean(selected);

//			out.close();
		}


		public double[] points;
		private double boundingMinX,boundingMinY,boundingMaxX,boundingMaxY;
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID=1555702763314628358L;
	}


	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID=1555702763314628358L;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolygonIterator.FloatNoTransform fNTPolygonIterator;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolygonIterator.FloatWithTransform fWTPolygonIterator;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolygonIterator.DoubleNoTransform dNTPolygonIterator;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolygonIterator.DoubleWithTransform dWTPolygonIterator;
	private static PolygonIterator iterator;

	public int parts[];

	private static double[] reusable=new double[2];
	private static float errorTolerance=0.01f;
}
