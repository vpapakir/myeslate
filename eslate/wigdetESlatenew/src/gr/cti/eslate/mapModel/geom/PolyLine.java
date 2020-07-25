package gr.cti.eslate.mapModel.geom;

import gr.cti.eslate.protocol.IPolyLine;
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
 * A PolyLine object represents a segmented line or a group of
 * segmented lines in the plane.
 */
public abstract class PolyLine extends VectorGeographicObject implements IPolyLine, Externalizable {
	/**
	 * No arg-constructor for externalization.
	 */
	public PolyLine() {
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
		return calculateDistanceAndSnap(p).distance;
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
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return getPathIterator(at);
	}

	public boolean contains(double x,double y) {
		PathIterator pi=getPathIterator(null);
		int cross=MathUtils.pointCrossingsForPath(pi,x,y);
		unlockIterator(pi);
		return ((cross & 1)!=0);
	}

	public boolean contains(Point2D p) {
		return contains(p.getX(),p.getY());
	}

	public boolean intersects(double x,double y,double w,double h) {
		PathIterator pi=getPathIterator(null);
		Crossings c=Crossings.findCrossings(pi,x,y,x+w,y+h);
		unlockIterator(pi);
		return (c==null || !c.isEmpty());
	}

	public boolean intersects(Rectangle2D r) {
		return intersects(r.getX(),r.getY(),r.getWidth(),r.getHeight());
	}

	public boolean contains(double x,double y,double w,double h) {
		PathIterator pi=getPathIterator(null);
		Crossings c=Crossings.findCrossings(pi,x,y,x+w,y+h);
		unlockIterator(pi);
		return (c!=null && c.covers(y,y+h));
	}

	public boolean contains(Rectangle2D r) {
		return contains(r.getX(),r.getY(),r.getWidth(),r.getHeight());
	}

	void lockIterator(PathIterator iterator) {
		synchronized (iterator) {
			((PolyLineIterator)iterator).lock=true;
		}
	}
	/**
	 * This method must be called after finishing with getPathIterator by
	 * whichever class it uses it. For now, if isDone() is called, unlockIterator() is
	 * automatically called before isDone() returns true.
	 */
	public void unlockIterator(PathIterator iterator) {
		synchronized (iterator) {
			((PolyLineIterator)iterator).lock=false;
		}
	}

	/**
	 * Calculates varius geometric aspects from the given point to the
	 * nearest segment of the line. The distance is calculated finding
	 * the minimum distance of the point and all the segments of the line.
     * <strong>WARNING: Do not use the LineAspects return object directly if you need to store it!
     * Make a copy instead, as only one instance is being used for performance reasons.</strong>
	 * @return  The aspects.
	 */
	public LineAspects calculateDistanceAndSnap(Point2D p) {
		double x0,y0,x1,y1;
		double maxDist;
		int segNo;

		maxDist=java.lang.Double.MAX_VALUE;

		PathIterator it=getPathIterator(null);
		LineAspects res=new LineAspects();
		synchronized (synchSnap) {
			it.currentSegment(synchSnap);
			it.next();
			x1=synchSnap[0]; y1=synchSnap[1];
			segNo=-1;
			while (!it.isDone()) {
				it.currentSegment(synchSnap);
				it.next();
				segNo++;
				//First we check if the p point is inside the geometric space defined by two
				//perpendicular to the segment straight lines that cross the two points of the segment.
				//If yes, we find the distance (the length of the perpendicular segment from the point
				//to the segment) and the snapping to the segment.
				//If no, the distance is calculated as the minimum distance of the point and the segment edges.
				x0=x1; y0=y1;
				x1=synchSnap[0]; y1=synchSnap[1];
				synchronized (synchDist) {
					double[] t=dist(p,x0,y0,x1,y1);
					if (t[0]<maxDist) {
						res.distance=t[0];
						res.snapX=t[1];
						res.snapY=t[2];
						res.segment=segNo;
						maxDist=t[0];
					}
				}
			}
		}
		unlockIterator(it);

		return res;
	}
	/**
	 * Returns the distance as well as the snapping points from point p to the
	 * segment (x0,y0)-(x1,y1).
	 */
	private double[] dist(Point2D p,double x0,double y0,double x1,double y1) {
		double tol=1E-6;
		double t,tx,ty;
		t=tx=ty=java.lang.Double.MAX_VALUE;
		//The segment is not a function. It is x=ct;
		if (Math.abs(x1-x0)<tol) {
			//The point is inside the geometric space
			if (p.getY()>=Math.min(y0,y1) && p.getY()<=Math.max(y0,y1)) {
				t=Math.abs(p.getX()-x0);
				tx=x0;
				ty=p.getY();
			}
		} else {
		//The segment is a function y=ax+b.
			//With a=0
			if (Math.abs(y1-y0)<tol) {
				//The point is inside the geometric space
				if (p.getX()>=Math.min(x0,x1) && p.getX()<=Math.max(x0,x1)) {
					t=Math.abs(p.getY()-y0);
					tx=p.getX();
					ty=y0;
				}
			} else {
			//With a!=0
				double a,b0,b1;
				a=Math.tan(Math.atan((y1-y0)/(x1-x0))+Math.PI/2);
				b0=y0-a*x0;
				b1=y1-a*x1;

				double dx0=(p.getY()-b0)/a;
				double dx1=(p.getY()-b1)/a;
				//The point is inside the geometric space
				if ((p.getX()>=dx0 && p.getX()<=dx1) || (p.getX()>=dx1 && p.getX()<=dx0)) {
					//The snap point is the section of the segment and the perpendicular
					//straight line crossing p.
					double as=(y1-y0)/(x1-x0);
					double bs=y0-as*x0;
					double ap=a;
					double bp=p.getY()-ap*p.getX();
					tx=(bs-bp)/(ap-as);
					ty=ap*tx+bp;
					t=Math.sqrt((p.getX()-tx)*(p.getX()-tx)+(p.getY()-ty)*(p.getY()-ty));
				}
			}
		}

		//The point is outside the space
		if (t==java.lang.Double.MAX_VALUE) {
			double d0=p.distance(x0,y0);
			double d1=p.distance(x1,y1);
			t=Math.min(d0,d1);
			if (d0<d1) {
				tx=x0;
				ty=y0;
			} else {
				tx=x1;
				ty=y1;
			}
		}

		synchDist[0]=t;
        synchDist[1]=tx;
        synchDist[2]=ty;
        return synchDist;
	}

	/**
	 * Returns the point of the line that is m meters from the given point, heading to the given direction.
	 * If null, such a point doesn't exist in the line.
	 */
	public double[] findPointFromDistance(int startSegment,double sx,double sy,double heading,double m,double unitsPerMeter) {
		//Find whether the line should be travelled ascending or descending, according to the given heading.
		if (startSegment==0)
        	return findPointFromDistance(startSegment,sx,sy,true,m,unitsPerMeter);
		else if (startSegment==getNumberOfPoints()/2-1) {
        	return findPointFromDistance(startSegment,sx,sy,false,m,unitsPerMeter);
		} else {
			Point2D p0=getPoint(startSegment);
			Point2D p1=getPoint(startSegment+1);
			double diff1=Math.abs(findHeading(p0,p1)-heading);
			p1=getPoint(startSegment-1);
			double diff2=Math.abs(findHeading(p0,p1)-heading);
			if (diff1<diff2)
				return findPointFromDistance(startSegment,sx,sy,true,m,unitsPerMeter);
			else
				return findPointFromDistance(startSegment,sx,sy,false,m,unitsPerMeter);
		}
	}

	private double findHeading(Point2D p0,Point2D p1) {
		double x=p1.getX()-p0.getX();
		double y=p1.getY()-p0.getY();
		double currentHeading=0;
		if (x!=0)
			currentHeading=Math.atan(y/x)*180/Math.PI;
		if (x==0) {
			if (y<0)
				currentHeading=180;
			else
				currentHeading=0;
		} else if (x>0)
			currentHeading=270+currentHeading;
		else if (x<0)
			currentHeading=90+currentHeading;
		return tiltAngle(currentHeading);
	}

	/**
	 * Converts an angle in degrees to agent-tilt angle in [0,360).
	 */
	private double tiltAngle(double angle) {
	    angle=angle-90;
	    return normAngle(angle);
	}

	private boolean valid(double angle) {
	    if (angle>=0 && angle<360)
	        return true;
	    return false;
	}

	/**
	 * Normalizes the given angle (in degrees) in the interval [0,360).
	 */
	private double normAngle(double angle) {
	    if (!valid(angle)) {
	        if (angle<0)
	            for (;!valid(angle);)
	                angle+=360;
	        else
	            for (;!valid(angle);)
	                angle-=360;
	    }
	    return angle;
	}


	/**
	 * Returns the point of the line that is m meters from the given point, ascending or descending.
	 * If null, such a point doesn't exist in the line.
	 */
	public double[] findPointFromDistance(int startSegment,double sx,double sy,boolean ascending,double m,double unitsPerMeter) {
		double distance=0;
		//Convert the distance to units because the distances are
		//here calculated in units. This avoids many multiplications and divisions.
		m*=unitsPerMeter;
		//These arrays cannot be reusable because they would not be thread safe
		double[] res=new double[4];
System.out.println("asc "+ascending+" ss "+startSegment+" np "+getNumberOfPoints());
		if (ascending) {
			//Skip the preceding points
			double px=sx,py=sy;
			for (int i=startSegment+1;i<getNumberOfPoints() && distance<m;i++) {
System.out.println("\tdist "+distance+" "+i);				
				double seg;
				Point2D p2d=getPoint(i);
				sx=p2d.getX();
				sy=p2d.getY();
				seg=Math.sqrt(Math.pow(sx-px,2)+Math.pow(sy-py,2));
				if (distance+seg>m) {
                    Point2D.Double p=getPoint(px,py,sx,sy,m-distance);
                    res[0]=p.x;
                    res[1]=p.y;
					distance=m;
				} else {
                    res[0]=sx;
                    res[1]=sy;
					distance+=seg;
                }
                res[2]=i-1;
                px=sx;
				py=sy;
			}
		} else {
		//Descending
			double px=sx,py=sy;
			for (int i=startSegment;i>-1 && distance<m;i--) {
				double seg;
				Point2D p2d=getPoint(i);
				sx=p2d.getX();
				sy=p2d.getY();
				seg=Math.sqrt(Math.pow(sx-px,2)+Math.pow(sy-py,2));
				if (distance+seg>m) {
					Point2D.Double p=getPoint(px,py,sx,sy,m-distance);
                    res[0]=p.x;
                    res[1]=p.y;
                    distance=m;
				} else {
                    res[0]=sx;
                    res[1]=sy;
					distance+=seg;
                }
                res[2]=i;
				px=sx;
				py=sy;
			}
		}
        //The distance travelled.
        res[3]=distance/unitsPerMeter;
		return res;

/*
		double distance=0;
		//Convert the distance to units because the distances are
		//here calculated in units. This avoids many multiplications and divisions.
		m*=unitsPerMeter;
		//These arrays cannot be reusable because they would not be thread safe
		double[] res=new double[4];
		double[] snap=new double[3];
		PathIterator it=getPathIterator(null);
		if (ascending) {
			//Skip the preceding points
			int count=startSegment;
			while (startSegment>=0 && !it.isDone()) {
				it.next();
				startSegment--;
			}
			//Start calculating distance from the starting point. The array contains all
			//the points from the starting to the first.
			//The last one is the end of the line and the maximum distance.
			double px=sx,py=sy;
			while (distance<m && !it.isDone()) {
				double seg;
				it.currentSegment(snap);
				sx=snap[0];
				sy=snap[1];
				it.next();
				seg=Math.sqrt(Math.pow(sx-px,2)+Math.pow(sy-py,2));
				if (distance+seg>m) {
                    Point2D.Double p=getPoint(px,py,sx,sy,m-distance);
                    res[0]=p.x;
                    res[1]=p.y;
					distance=m;
				} else {
                    res[0]=sx;
                    res[1]=sy;
					distance+=seg;
                }
                res[2]=count;
                count++;
                px=sx;
				py=sy;
			}
		} else {
		//Descending
			DblBaseArray ax=new DblBaseArray();
			DblBaseArray ay=new DblBaseArray();
			//Skip the preceding points
			int count=0;
			//Find all the points until the starting one
			while (count<=startSegment && !it.isDone()) {
				it.currentSegment(snap);
				it.next();
				count++;
				ax.pushFront(snap[0]);
				ay.pushFront(snap[1]);
			}
			//Start calculating distance from the starting point. The array was constructed
			//vice-versa but contains all the points from the starting to the first.
			//The last one is the begining of the line and the maximum distance.
			double px=sx,py=sy;
			for (int i=0;i<ax.size() && distance<m;i++) {
				double seg;
				double cx=ax.get(i);
				double cy=ay.get(i);
				seg=Math.sqrt(Math.pow(cx-px,2)+Math.pow(cy-py,2));
				if (distance+seg>m) {
					Point2D.Double p=getPoint(px,py,cx,cy,m-distance);
                    res[0]=p.x;
                    res[1]=p.y;
                    distance=m;
				} else {
                    res[0]=cx;
                    res[1]=cy;
					distance+=seg;
                }
                res[2]=startSegment-i; //The numbering of the segments is always from the start
				px=cx;
				py=cy;
			}
		}
        //The distance travelled.
        res[3]=distance/unitsPerMeter;
		unlockIterator(it);
		return res;
*/
	}

	private Point2D.Double getPoint(double px,double py,double sx,double sy,double unitsLeft) {
		double tol=1E-6;
		double x,y;
		if (Math.abs(sx-px)<tol) {
			x=sx;
			if (py<=sy)
				y=py+unitsLeft;
			else
				y=py-unitsLeft;
		} else if (Math.abs(sy-py)<tol) {
			y=sy;
			if (px<=sx)
				x=px+unitsLeft;
			else
				x=px-unitsLeft;
		} else {
			double seg=Math.sqrt(Math.pow(sx-px,2)+Math.pow(sy-py,2));
			x=(sx-px)*unitsLeft/seg+px;
			y=(sy-py)*unitsLeft/seg+py;
		}
		return new Point2D.Double(x,y);
	}



	/**
	 * Single precision PolyLine object.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 15-May-2002
	 */
	public static class Float extends PolyLine implements Externalizable {
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
			synchronized (iterator) {
				PolyLineIterator thisIt;
				//Produces a new iterator when the static is found in use.
				//It is necessary in multi-threaded environments
				if (at!=null) {
					if (fWTPolyLineIterator==null || (fWTPolyLineIterator!=null && fWTPolyLineIterator.lock))
						fWTPolyLineIterator=new PolyLineIterator.FloatWithTransform();
					iterator=thisIt=fWTPolyLineIterator;
					fWTPolyLineIterator.points=points;
				} else {
					if (fNTPolyLineIterator==null || (fNTPolyLineIterator!=null && fNTPolyLineIterator.lock))
						fNTPolyLineIterator=new PolyLineIterator.FloatNoTransform();
					iterator=thisIt=fNTPolyLineIterator;
					fNTPolyLineIterator.points=points;
				}
				thisIt.lock=true;
				thisIt.idx=0;
				thisIt.prtidx=0;
				thisIt.endidx=points.length-2;
				thisIt.partStop=2*parts[0];
				thisIt.prePartStop=-1;
				thisIt.at=at;
				thisIt.parts=parts;
				thisIt.errorTolerance=errorTolerance;
				if (at!=null) {
					thisIt.initializeIterator();
				}
				return thisIt;
			}
		}

		public synchronized IVectorGeographicObject createTransformedShape(AffineTransform at) {
			float[] newpoints=new float[points.length];
			System.arraycopy(points,0,newpoints,0,points.length);
			int[] newparts=new int[parts.length];
			System.arraycopy(parts,0,newparts,0,parts.length);
			at.transform(newpoints,0,newpoints,0,newpoints.length);

			PolyLine.Float p=new PolyLine.Float(id,newparts,newpoints,0f,0f,0f,0f);
			p.selected=selected;
			synchronized (reusable) {
				reusable[0]=(double) boundingMinX; reusable[1]=(double) boundingMinY;
				at.transform(reusable,0,reusable,0,1);
				p.setBoundingMin(reusable[0],reusable[1]);
				reusable[0]=(double) boundingMaxX; reusable[1]=(double) boundingMaxY;
				at.transform(reusable,0,reusable,0,1);
				p.setBoundingMax(reusable[0],reusable[1]);
			}
			return p;
		}

		/**
		 * Gets the i'th point in row.
		 */
		public Point2D getPoint(int i) {
			return new Point2D.Float(points[2*i],points[2*i+1]);
		}

        /**
         * Checks if the polyline has the given point coordinates in one of its points.
         * The checking is performed using <code>EQUALITY_TOLERANCE</code>.
         * @param   point   The point to check if exists in the line.
         * @return  The index of the point in the line or -1 if it does not exist.
         */
        public int hasPoint(Point2D point) {
            double x=point.getX();
            double y=point.getY();
            for (int i=points.length-2;i>-1;i=i-2) {
                if (Math.abs(points[i]-x)<IPolyLine.EQUALITY_TOLERANCE && Math.abs(points[i+1]-y)<IPolyLine.EQUALITY_TOLERANCE)
                    return i/2;
            }
            return -1;
        }

		/**
		 * Gets the total number of points.
		 */
		public int getNumberOfPoints() {
			return points.length/2;
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
		}

		public float[] points;
		private float boundingMinX,boundingMinY,boundingMaxX,boundingMaxY;
		/**
		 * A reusable iterator object which shows the last iterator used
		 * in the Float Iterators class. It is initialized to avoid an
		 * initial NullPointerException in synchronization.
		 */
		private static PolyLineIterator iterator=new PolyLineIterator.FloatWithTransform();
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID=-6338936112330321265L;
	}



	/**
	 * Double precision PolyLine object.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 17-May-2002
	 */
	public static class Double extends PolyLine implements Externalizable {
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
			synchronized (iterator) {
				PolyLineIterator thisIt;
				//Produces a new iterator when the static is found in use.
				//It is necessary in multi-threaded environments
				if (at!=null) {
					if (dWTPolyLineIterator==null || (dWTPolyLineIterator!=null && dWTPolyLineIterator.lock))
						dWTPolyLineIterator=new PolyLineIterator.DoubleWithTransform();
					iterator=thisIt=dWTPolyLineIterator;
					dWTPolyLineIterator.points=points;
				} else {
					if (dNTPolyLineIterator==null || (dNTPolyLineIterator!=null && dNTPolyLineIterator.lock))
						dNTPolyLineIterator=new PolyLineIterator.DoubleNoTransform();
					iterator=thisIt=dNTPolyLineIterator;
					dNTPolyLineIterator.points=points;
				}
				thisIt.lock=true;
				thisIt.idx=0;
				thisIt.prtidx=0;
				thisIt.endidx=points.length-2;
				thisIt.partStop=2*parts[0];
				thisIt.prePartStop=-1;
				thisIt.at=at;
				thisIt.parts=parts;
				thisIt.errorTolerance=errorTolerance;
				if (at!=null) {
					thisIt.initializeIterator();
				}
				return thisIt;
			}
		}

		public synchronized IVectorGeographicObject createTransformedShape(AffineTransform at) {
			double[] newpoints=new double[points.length];
			System.arraycopy(points,0,newpoints,0,points.length);
			int[] newparts=new int[parts.length];
			System.arraycopy(parts,0,newparts,0,parts.length);
			at.transform(newpoints,0,newpoints,0,newpoints.length);

			PolyLine.Double p=new PolyLine.Double(id,newparts,newpoints,0d,0d,0d,0d);
			p.selected=selected;
			synchronized (reusable) {
				reusable[0]=boundingMinX; reusable[1]=boundingMinY;
				at.transform(reusable,0,reusable,0,1);
				p.setBoundingMin(reusable[0],reusable[1]);
				reusable[0]=boundingMaxX; reusable[1]=boundingMaxY;
				at.transform(reusable,0,reusable,0,1);
				p.setBoundingMax(reusable[0],reusable[1]);
			}
			return p;
		}

		/**
		 * Gets the i'th point in row.
		 */
		public Point2D getPoint(int i) {
			return new Point2D.Double(points[2*i],points[2*i+1]);
		}

        /**
         * Checks if the polyline has the given point coordinates in one of its points.
         * The checking is performed using <code>EQUALITY_TOLERANCE</code>.
         * @param   point   The point to check if exists in the line.
         * @return  The index of the point in the line or -1 if it does not exist.
         */
        public int hasPoint(Point2D point) {
            double x=point.getX();
            double y=point.getY();
            for (int i=points.length-2;i>-1;i=i-2) {
                if (Math.abs(points[i]-x)<IPolyLine.EQUALITY_TOLERANCE && Math.abs(points[i+1]-y)<IPolyLine.EQUALITY_TOLERANCE)
                    return i/2;
            }
            return -1;
        }

		/**
		 * Gets the total number of points.
		 */
		public int getNumberOfPoints() {
			return points.length/2;
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
		}

		public double[] points;
		private double boundingMinX,boundingMinY,boundingMaxX,boundingMaxY;
		/**
		 * A reusable iterator object which shows the last iterator used
		 * in the Double Iterators class. It is initialized to avoid an
		 * initial NullPointerException in synchronization.
		 */
		private static PolyLineIterator iterator=new PolyLineIterator.DoubleWithTransform();
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID=4939942649788527136L;
	}

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID=4939942649788527136L;
	/**
	 * An array holding the indices that define separate parts in the polyline.
	 */
	public int parts[];
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolyLineIterator.FloatNoTransform fNTPolyLineIterator;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolyLineIterator.FloatWithTransform fWTPolyLineIterator;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolyLineIterator.DoubleNoTransform dNTPolyLineIterator;
	/**
	 * A static reusable instance to avoid creating a new one each time
	 * the shape is about to be painted. A shared one is used to save memory.
	 * <code>unlockIterator()</code> must be called after using the shared Iterator.
	 */
	private static PolyLineIterator.DoubleWithTransform dWTPolyLineIterator;


    /**
     * Reusable array.
     */
	private static double[] synchSnap=new double[2];
	/**
	 * Any reference to this reusable object or the results of dist() must synchronized.
	 */
	private static double[] synchDist=new double[3];
	/**
	 * Reusable array.
	 */
	private static double[] reusable=new double[2];
	private static float errorTolerance=0.01f;
}
