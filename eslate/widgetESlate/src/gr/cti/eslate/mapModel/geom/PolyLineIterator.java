package gr.cti.eslate.mapModel.geom;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * A PathIterator used by Polygon objects.
 * @author Giorgos Vasiliou
 * @version 2.0, 09-Oct-2002
 */
public abstract class PolyLineIterator implements PathIterator {
	/**
	 * Initializes the iterator.
	 */
	public abstract void initializeIterator();
	/**
	 * Single precision PathIterator when no transformation is provided.
	 * @author Giorgos Vasiliou
	 * @version 1.0
	 */
	public static class FloatNoTransform extends PolyLineIterator {
		public float[] points;
		public void initializeIterator() {}
		public int currentSegment(double[] coords) {
			coords[0]=points[idx];
			coords[1]=points[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}

		public int currentSegment(float[] coords) {
			coords[0]=points[idx];
			coords[1]=points[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}
	}


	/**
	 * Single precision PathIterator when a transformation is provided.
	 * @author Giorgos Vasiliou
	 * @version 1.0
	 */
	public static class FloatWithTransform extends PolyLineIterator {
		public void initializeIterator() {
			if (transpoints.length<points.length)
				transpoints=new float[points.length];
			at.transform(points,0,transpoints,0,points.length/2);
			lastX=lastY=Float.MAX_VALUE;
		}
		public int currentSegment(double[] coords) {
			startidx=idx; farx=fary=0;
			for (int i=startidx;Math.abs(lastX-transpoints[i])<errorTolerance && i<endidx && i!=partStop /*&& i!=prePartStop*/;i+=2)
				farx++;
			for (int i=startidx+1;Math.abs(lastY-transpoints[i])<errorTolerance && i<endidx && i!=partStop+1 /*&& i!=prePartStop+1*/;i+=2)
				fary++;
			if (farx!=0 || fary!=0)
				idx+=2*Math.min(farx,fary);
			coords[0]=lastX=transpoints[idx];
			coords[1]=lastY=transpoints[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}


		public int currentSegment(float[] coords) {
			startidx=idx; farx=fary=0;
			for (int i=startidx;Math.abs(lastX-transpoints[i])<errorTolerance && i<endidx && i!=partStop /*&& i!=prePartStop*/;i+=2)
				farx++;
			for (int i=startidx+1;Math.abs(lastY-transpoints[i])<errorTolerance && i<endidx && i!=partStop+1 /*&& i!=prePartStop+1*/;i+=2)
				fary++;
			if (farx!=0 || fary!=0)
				idx+=2*Math.min(farx,fary);
			coords[0]=lastX=transpoints[idx];
			coords[1]=lastY=transpoints[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}
		/**
		 * The points of the shape transformed.
		 */
		public float[] transpoints=new float[10000];
		/**
		 * The points of the shape.
		 */
		public float[] points;
		private int startidx;
		private int farx,fary;
		private float lastX,lastY;
	}


	/**
	 * Double precision PathIterator when no transformation is provided.
	 * @author Giorgos Vasiliou
	 * @version 1.0
	 */
	public static class DoubleNoTransform extends PolyLineIterator {
		public double[] points;
		public void initializeIterator() {}
		public int currentSegment(double[] coords) {
			coords[0]=points[idx];
			coords[1]=points[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}

		public int currentSegment(float[] coords) {
			coords[0]=(float) points[idx];
			coords[1]=(float) points[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}
	}


	/**
	 * Double precision PathIterator when a transformation is provided.
	 * @author Giorgos Vasiliou
	 * @version 1.0
	 */
	public static class DoubleWithTransform extends PolyLineIterator {
		public void initializeIterator() {
			if (transpoints.length<points.length)
				transpoints=new double[points.length];
			at.transform(points,0,transpoints,0,points.length/2);
			lastX=lastY=Float.MAX_VALUE;
		}
		public int currentSegment(double[] coords) {
			startidx=idx; farx=fary=0;
			for (int i=startidx;Math.abs(lastX-transpoints[i])<errorTolerance && i<endidx && i!=partStop /*&& i!=prePartStop*/;i+=2)
				farx++;
			for (int i=startidx+1;Math.abs(lastY-transpoints[i])<errorTolerance && i<endidx && i!=partStop+1 /*&& i!=prePartStop+1*/;i+=2)
				fary++;
			if (farx!=0 || fary!=0)
				idx+=2*Math.min(farx,fary);
			coords[0]=lastX=transpoints[idx];
			coords[1]=lastY=transpoints[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}

		public int currentSegment(float[] coords) {
			startidx=idx; farx=fary=0;
			for (int i=startidx;Math.abs(lastX-transpoints[i])<errorTolerance && i<endidx && i!=partStop /*&& i!=prePartStop*/;i+=2)
				farx++;
			for (int i=startidx+1;Math.abs(lastY-transpoints[i])<errorTolerance && i<endidx && i!=partStop+1 /*&& i!=prePartStop+1*/;i+=2)
				fary++;
			if (farx!=0 || fary!=0)
				idx+=2*Math.min(farx,fary);
			coords[0]=(float) transpoints[idx];
			coords[1]=(float) transpoints[idx+1];
			lastX=transpoints[idx]; lastY=transpoints[idx+1];
			//Decide the line type with minimal implementation cost
			if (idx==partStop) {
				doPartStuff();
				return PathIterator.SEG_MOVETO;
			}
			return PathIterator.SEG_LINETO;
		}
		/**
		 * The points of the shape transformed.
		 */
		public double[] transpoints=new double[10000];
		/**
		 * The points of the shape.
		 */
		public double[] points;
		private int startidx;
		private int farx,fary;
		private double lastX,lastY;
	}

	public int getWindingRule() {
		return PathIterator.WIND_EVEN_ODD;
	}

	public boolean isDone() {
		if (idx>endidx)
			lock=false;
		return (idx>endidx);
	}

	public void next() {
		idx+=2;
	}

	void doPartStuff() {
		prtidx++;
		if (prtidx>=parts.length) {
			partStop=endidx+1;
			prePartStop=-1;
		} else {
			partStop=2*parts[prtidx];
			prePartStop=partStop-2;
		}
	}

	/**
	 * Usage-lock flag.
	 */
	public boolean lock;
	/**
	 * The transformation
	 */
	public AffineTransform at;
	/**
	 * The current index.
	 */
	public int idx;
	/**
	 * The last valid index.
	 */
	public int endidx;
	/**
	 * The index of a new part.
	 */
	public int partStop;
	/**
	 * The index before a new part.
	 */
	public int prePartStop;
	/**
	 * The parts.
	 */
	public int[] parts;
	/**
	 * The index within the parts array.
	 */
	public int prtidx;
	/**
	 * The greater the value, the more points are being cut down to speed up drawing, the less accurate is the shape.
	 * The smaller the value, the lesser points are being cut down to speed up drawing, the more accurate is the shape.
	 * If 0, no point is cut down.
	 */
	public float errorTolerance;
}