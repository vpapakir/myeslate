package gr.cti.eslate.mapModel;

import gr.cti.eslate.mapModel.geom.VectorGeographicObject;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.DblBaseArray;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class encapsulates a GeneralPath object and provides a similar interface as GeneralPath does.
 * This is done because GeneralPath is declared final
 */
public class polyLine extends VectorGeographicObject implements GeographicObject,java.awt.Shape,Externalizable {
	/**
	 * No arg-constructor for externalization.
	 */
	public polyLine() {
		path=new GeneralPath(GeneralPath.WIND_NON_ZERO);
		id=0;
		selected=false;
		boundingMin=new Point2D.Double();
		boundingMax=new Point2D.Double();
	}

	public polyLine(int id,int[] parts,double[][] points,double boundingMinX,double boundingMinY,double boundingMaxX,double boundingMaxY) {
		path=new GeneralPath(GeneralPath.WIND_NON_ZERO);
		this.id=id;
		boundingMin=new Point2D.Double(boundingMinX,boundingMinY);
		boundingMax=new Point2D.Double(boundingMaxX,boundingMaxY);
		selected=false;
		noParts=parts.length;
		noPoints=points.length;
		constructPath(parts,points);
	}

	private polyLine(int id,GeneralPath path) {
		this.id=id;
		this.path=path;
		selected=false;
		boundingMin=new Point2D.Double();
		boundingMax=new Point2D.Double();
	}

	public int getNumberOfSegments() {
		return noPoints;
	}

	public int getNumberOfParts() {
		return noParts;
	}
	/**
	 * Returns the tilt of the given segment in degrees anti-clockwise.
	 */
	public double getTiltOfSegment(int i) {
		double tilt=0;
		int segNo=0;
		PathIterator it=path.getPathIterator(new AffineTransform());
		//Skips segments
		while (!it.isDone() && segNo<i) {
			it.next();
			segNo++;
		}
		//Find the tilt.
		double[] d=new double[6];
		it.currentSegment(d);
		double x0=d[0];
		double y0=d[1];
		it.next();
		it.currentSegment(d);
		double x1=d[0];
		double y1=d[1];
		double tol=1E-6;
		//The segment is not a function. It is x=ct;
		if (Math.abs(x1-x0)<tol) {
			if (y1>y0)
				return 180;
			else
				return 0;
		} else if (Math.abs(y1-y0)<tol) {
			if (x1>x0)
				return 270;
			else
				return 90;
		} else {
			double a=Math.atan((y1-y0)/(x1-x0));
			if (x1>x0)
				return a*180/Math.PI-90;
			else
				return a*180/Math.PI-90-180;
		}
	}


	/**
	 * Calculates the minimum distance between the given point and the geographic object.
	 */
	public double calculateDistance(Point2D p) {
		return calculateDistanceAndSnap(p)[0];
	}
	/**
	 * Calculates the minimum distance between the given point and the geographic object as well
	 * as the snapping point.
	 * @return  The returned double[] contains 0: the distance, 1: the snap point x, 2 the snap point y,
	 * 3 the segment number.
	 */
	public double[] calculateDistanceAndSnap(Point2D p) {
		double x0,y0,x1,y1;
		double[] d=new double[6];
		double[] t; //Temporary snap values
		double maxDist;
		//0: distance
		//1: snapX
		//2: snapY
		//3: segment number
		double[] res;
		int segNo;

		res=new double[4];

		maxDist=Double.MAX_VALUE;

		PathIterator it=path.getPathIterator(new AffineTransform());
		it.currentSegment(d);
		it.next();
		x1=d[0]; y1=d[1];
		segNo=-1;
		while (!it.isDone()) {
			it.currentSegment(d);
			it.next();
			segNo++;
			//First we check if the p point is inside the geometric space defined by two
			//perpendicular to the segment straight lines that cross the two points of the segment.
			//If yes, we find the distance (the length of the perpendicular segment from the point
			//to the segment) and the snapping to the segment.
			//If no, the distance is calculated as the minimum distance of the point and the segment edges.
			x0=x1; y0=y1;
			x1=d[0]; y1=d[1];
			t=dist(p,x0,y0,x1,y1);
			if (t[0]<maxDist) {
				res[0]=t[0];
				res[1]=t[1];
				res[2]=t[2];
				res[3]=segNo;
				maxDist=t[0];
			}
		}

		return res;
	}
	/**
	 * Returns the distance as well as the snapping points from point p to the
	 * segment (x0,y0)-(x1,y1).
	 */
	private double[] dist(Point2D p,double x0,double y0,double x1,double y1) {
		double tol=1E-6;
		double t,tx,ty;
		t=tx=ty=Double.MAX_VALUE;
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
		if (t==Double.MAX_VALUE) {
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

		return new double[]{t,tx,ty};
	}
	/**
	 * Returns the point of the line that is m meters from the given point, ascending or descending.
	 * If null, such a point doesn't exist in the line.
	 */
	public double[] findPointFromDistance(int startSegment,double sx,double sy,boolean ascending,double m,double unitsPerMeter) {
		double[] xy=null;
		double distance=0;
		double[] d=new double[6];
		//Convert the distance to units because the distances are
		//here calculated in units. This avoids many multiplications and divisions.
		m*=unitsPerMeter;
		if (ascending) {
			PathIterator it=path.getPathIterator(new AffineTransform());
			//Skip the preceding points
			int count=0;
			while (count<=startSegment && !it.isDone()) {
				it.next();
				count++;
			}
			//Start calculating distance from the starting point. The array contains all
			//the points from the starting to the first.
			//The last one is the end of the line and the maximum distance.
			double px=sx,py=sy;
			while (distance<m && !it.isDone()) {
				double seg;
				it.currentSegment(d);
				sx=d[0];
				sy=d[1];
				it.next();
				count++;
				seg=Math.sqrt(Math.pow(sx-px,2)+Math.pow(sy-py,2));
				if (distance+seg>m) {
					xy=getPoint(px,py,sx,sy,m-distance);
					xy[2]=count;
					distance=m;
				} else
					distance+=seg;
				px=sx;
				py=sy;
			}
		} else {
		//Descending
			DblBaseArray ax=new DblBaseArray();
			DblBaseArray ay=new DblBaseArray();
			//Skip the preceding points
			PathIterator it=path.getPathIterator(new AffineTransform());
			int count=0;
			//Find all the points until the starting one
			while (count<=startSegment && !it.isDone()) {
				it.currentSegment(d);
				it.next();
				count++;
				ax.pushFront(d[0]);
				ay.pushFront(d[1]);
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
					xy=getPoint(px,py,cx,cy,m-distance);
					xy[2]=startSegment-i;
					distance=m;
				} else
					distance+=seg;
				px=cx;
				py=cy;
			}
		}
		return xy;
	}

	private double[] getPoint(double px,double py,double sx,double sy,double unitsLeft) {
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
		return new double[]{x,y,0};
	}
	/**
	 * Sets the object id which is its
	 * reference to the associated database table.
	 */
	public void setID(int i) {
		if (cacheIntShape!=null)
			((polyLine) cacheIntShape).id=i;
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

	public Rectangle getBounds() {
		return path.getBounds();
	}

	public Rectangle2D getBounds2D() {
		return path.getBounds2D();
	}

	public boolean contains(double x, double y) {
		return path.contains(x,y);
	}

	public boolean contains(Point2D p) {
		return path.contains(p);
	}

	public boolean intersects(double x, double y, double w, double h) {
		return path.intersects(x,y,w,h);
	}

	public boolean intersects(Rectangle2D r) {
		return path.intersects(r);
	}

	public boolean contains(double x, double y, double w, double h) {
		return path.contains(x,y,w,h);
	}

	public boolean contains(Rectangle2D r) {
		return path.contains(r);
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return path.getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return path.getPathIterator(at,flatness);
	}

	public void moveTo(float x,float y) {
		cacheIntTrans=null;
		cacheIntShape=null;
		path.moveTo(x,y);
	}

	public void lineTo(float x,float y) {
		cacheIntTrans=null;
		cacheIntShape=null;
		path.lineTo(x,y);
	}

	public void closePath() {
		cacheIntTrans=null;
		cacheIntShape=null;
		path.closePath();
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
			return cacheIntShape;
		}
		//Otherwise calculate the shape and put it in the cache.
		else {
			GeneralPath newPath=new GeneralPath(GeneralPath.WIND_NON_ZERO,noPoints);
			PathIterator it=path.getPathIterator(at);
			polyLine p;
			synchronized (reusableF) {
				//Coordinates of the last point added
				int ax=0,ay=0;
				//Coordinates of the pending point
				int px=0,py=0;
				//Coordinates of the current point
				int cx=0,cy=0;
				//Diagonal line equation parameters
				double a,b;
				//Distance from the diagonal line
				double d;
				//Initialize the new path. Set the starting location.
				if (!it.isDone()) {
					it.currentSegment(reusableF);
					it.next();
					newPath.moveTo((int) reusableF[0],(int) reusableF[1]);
					ax=(int) reusableF[0]; ay=(int) reusableF[1];
				}
				//Initialize the first pending point. A point is put to the path only if its next one will
				//produce a different line.
				if (!it.isDone()) {
					it.currentSegment(reusableF);
					it.next();
					px=(int) reusableF[0];
					py=(int) reusableF[1];
				}
				//Do the loop that goes through all the points.
				while (!it.isDone()) {
					int type=it.currentSegment(reusableF);
					it.next();
					cx=(int) reusableF[0]; cy=(int) reusableF[1];
					//The moveTo points are immediately passed to the path. Write the pending point and then
					//move to the new location
					if (type==PathIterator.SEG_MOVETO) {
						if (ax!=px || ay!=py)
							newPath.lineTo(px,py);
						ax=px; ay=py;
						newPath.moveTo(cx,cy);
						px=cx;
						py=cy;
						continue;
					}
					//If the coordinates are the same it is completely useless
					if ((cx==ax && cy==ay) || (cx==px && cy==py))
						continue;
					//If the new point is in the same vertical line
					else if (px==ax && cx==ax) {
						if (Math.abs(ay-cy)>Math.abs(ay-py))
							py=cy;
					}
					//If the new point is in the same horizontal line
					else if (py==ay && cy==ay) {
						if (Math.abs(ax-cx)>Math.abs(ax-px))
							px=cx;
					}
					//If the new point is in the same diagonal line
					else if (((a=((py-ay)*1d)/((px-ax)*1d))*cx+(b=ay-a*ax))==cy) {
						//If the current point produces a greater segment, make the current pending
						if (Math.sqrt(Math.pow(cx-ax,2)+Math.pow(cy-ay,2))>Math.sqrt(Math.pow(px-ax,2)+Math.pow(py-ay,2))) {
							px=cx; py=cy;
						}
					}
					//Put the pending point and make the current one pending
					else {
						newPath.lineTo(px,py);
						ax=px; ay=py;
						px=cx; py=cy;
					}
				}
				//A pending point remained
				if (px!=ax || py!=ay)
					newPath.lineTo(px,py);
				if (noPoints>2 && (px!=cx || py!=cy))
					newPath.lineTo(cx,cy);

				p=new polyLine(id,newPath);
				p.selected=selected;
				reusable[0]=boundingMin.x; reusable[1]=boundingMin.y;
				at.transform(reusable,0,reusable,0,1);
				p.setBoundingMin(reusable[0],reusable[1]);
				reusable[0]=boundingMax.x; reusable[1]=boundingMax.y;
				at.transform(reusable,0,reusable,0,1);
				p.setBoundingMax(reusable[0],reusable[1]);
				//Put in the cache
				cacheIntTrans=at;
				cacheIntShape=p;
			}
			return p;
		}
	}

	public IVectorGeographicObject createTransformedShape(AffineTransform at) {
		polyLine p=new polyLine(id,new GeneralPath(path.createTransformedShape(at)));
		p.selected=selected;
		reusable[0]=boundingMin.x; reusable[1]=boundingMin.y;
		at.transform(reusable,0,reusable,0,1);
		p.setBoundingMin(reusable[0],reusable[1]);
		reusable[0]=boundingMax.x; reusable[1]=boundingMax.y;
		at.transform(reusable,0,reusable,0,1);
		p.setBoundingMax(reusable[0],reusable[1]);
		return p;
	}

	private void constructPath(int[] parts,double[][] points) {
		int pp=0;
		for (int i=0;i<points.length;i++) {
			if (i==parts[pp]) {
				path.moveTo((float) points[i][0],(float) points[i][1]);
				//No Array index out of bounds will occur.
				if ((++pp)==parts.length) pp=0;
			} else
				path.lineTo((float) points[i][0],(float) points[i][1]);
		}
	}

	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		id=ht.get("id",0);
		noParts=ht.get("noParts",0);
		noPoints=ht.get("noPoints",0);
		point p2d=(point) ht.get("boundmin",(point) null);
		boundingMin=new Point2D.Double(p2d.getX(),p2d.getY());
		p2d=(point) ht.get("boundmax",(point) null);
		boundingMax=new Point2D.Double(p2d.getX(),p2d.getY());
		selected=ht.get("selected",false);
		double[][] points=(double[][]) ht.get("points",(double[][]) null);
		int[] parts=(int[]) ht.get("parts",(int[]) null);
		if (points==null || parts==null)
			throw new IOException("Polygon data not corrrectly saved.");
		constructPath(parts,points);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		int[] parts=new int[noParts];
		double[][] points=new double[noPoints][2];
		PathIterator it=path.getPathIterator(new AffineTransform());
		double[] d=new double[6];
		int type;
		int prts=0,pnts=0;
		while (!it.isDone()) {
			type=it.currentSegment(d);
			points[pnts][0]=d[0];
			points[pnts++][1]=d[1];
			if (type==PathIterator.SEG_MOVETO)
				parts[prts++]=pnts-1;
			it.next();
		}
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("id",id);
		ht.put("noParts",noParts);
		ht.put("noPoints",noPoints);
		ht.put("boundmin",new point(boundingMin.getX(),boundingMin.getY()));
		ht.put("boundmax",new point(boundingMax.getX(),boundingMax.getY()));
		ht.put("selected",selected);
		ht.put("points",points);
		ht.put("parts",parts);
		out.writeObject(ht);
	}

	/**
	 * Bounding coords.
	 */
	private Point2D.Double boundingMin,boundingMax;

	/**
	 * The serial version UID.
	 */
	static final long serialVersionUID=3000L;
	private int noParts,noPoints;
	private GeneralPath path;
	/**
	 * Caches the last transformation used to produce a transformed shape. If the same transformed
	 * is needed, it is returned by the cache.
	 */
	private AffineTransform cacheIntTrans;
	/**
	 * Caches the last transformed shape transformed by cacheIntTrans.
	 */
	private polyLine cacheIntShape;
	private boolean visible;
	private static double[] reusable=new double[2];
	private static float[] reusableF=new float[2];
}
