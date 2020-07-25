package gr.cti.eslate.mapModel;

import gr.cti.eslate.mapModel.geom.VectorGeographicObject;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

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
public class polygon extends VectorGeographicObject implements GeographicObject,java.awt.Shape,Externalizable {
	/**
	 * No arg-constructor for externalization.
	 */
	public polygon() {
		path=new GeneralPath();
		id=0;
		selected=false;
		boundingMin=new Point2D.Double();
		boundingMax=new Point2D.Double();
	}

	public polygon(int id,int[] parts,double[][] points,double boundingMinX,double boundingMinY,double boundingMaxX,double boundingMaxY) {
		path=new GeneralPath();
		this.id=id;
		boundingMin=new Point2D.Double(boundingMinX,boundingMinY);
		boundingMax=new Point2D.Double(boundingMaxX,boundingMaxY);
		selected=false;
		noParts=parts.length;
		noPoints=points.length;
		constructPath(parts,points);
	}
	/**
	 * Constructor,
	 */
	public polygon(int id,GeneralPath path) {
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
	 * Calculates the minimum distance between the given point and the geographic object.
	 */
	public double calculateDistance(Point2D p) {
		if (path.contains(p))
			return 0;
		else
			return Math.sqrt(Math.pow(p.getX()-(getBounds().x+getBounds().width/2),2)+Math.pow(p.getY()-(getBounds().y+getBounds().height/2),2));
	}
	/**
	 * Sets the object id which is its
	 * reference to the associated database table.
	 */
	public void setID(int i) {
		if (cacheIntShape!=null)
			((polygon) cacheIntShape).id=i;
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
	public synchronized IVectorGeographicObject createIntegerTransformedShape(AffineTransform at) {
		//If this transformation is the previous one used, return the cached shape
		if (at.equals(cacheIntTrans)) {
			cacheIntShape.selected=selected;
			return cacheIntShape;
		}
/*        //If the zoom factor and rotation have not changed, we can simply translate the cached shape
		//to make pan operations faster.
		else if (cacheIntTrans!=null && at.getScaleX()==cacheIntTrans.getScaleX() && at.getScaleY()==cacheIntTrans.getScaleY()) {
System.out.println("Transformed cached shape");
			cacheIntShape.selected=selected;
			cacheIntShape.visible=visible;
			cacheIntTrans=at;
			AffineTransform.getRotateInstance(
			cacheIntShape.transform(;
			return cacheIntShape;
		}*/
		//Otherwise calculate the shape and put it in the cache.
		else {
			GeneralPath newPath=new GeneralPath(GeneralPath.WIND_NON_ZERO,noPoints);
			PathIterator it=path.getPathIterator(at);
			polygon p;
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

				p=new polygon(id,newPath);
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

	public synchronized IVectorGeographicObject createTransformedShape(AffineTransform at) {
		polygon p=new polygon(id,new GeneralPath(path.createTransformedShape(at)));
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
			throw new IOException("Polygon data not correctly saved.");
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
	private polygon cacheIntShape;
	private boolean visible;

	private static double[] reusable=new double[2];
	private static float[] reusableF=new float[2];
}
