package gr.cti.eslate.protocol;

import java.awt.geom.Point2D;

/**
 * Tags an object as a PolyLine Geographic Object.
 * @author Giorgos Vasiliou
 * @version 1.0, 30-Oct-2002
 */
public interface IPolyLine extends GeographicObject,java.awt.Shape {
	/**
	 * Calculates the minimum distance from the given point to the
	 * line. The distance is calculated finding the minimum distance
	 * of the point and all the segments of the line.
	 */
	public double calculateDistance(java.awt.geom.Point2D p);
	/**
	 * Calculates varius geometric aspects from the given point to the
	 * nearest segment of the line. The distance is calculated finding
	 * the minimum distance of the point and all the segments of the line.
	 * @return  An array the positions of which contain: [0]=distance, [1]=X-coordinate of the nearest point to the line, [2]=Y-coordinate of the nearest point to the line, [3]: The segment number of the nearest segment.
	 */
	public LineAspects calculateDistanceAndSnap(java.awt.geom.Point2D p);
    /**
     * Gets the i'th point in row.
     */
	public Point2D getPoint(int i);
	/**
	 * Gets the total number of points.
	 */
	public int getNumberOfPoints();
    /**
     * Checks if the polyline has the given point coordinates in one of its points.
     * The checking is performed using <code>EQUALITY_TOLERANCE</code>.
     * @param   point   The point to check if exists in the line.
     * @return  The index of the point in the line or -1 if it does not exist.
     */
    public int hasPoint(Point2D point);
    /**
     * Returns the point of the line that is m meters from the given point, ascending or descending.
     * If null, such a point doesn't exist in the line.
     */
    public double[] findPointFromDistance(int startSegment,double sx,double sy,boolean ascending,double m,double unitsPerMeter);
    /**
     * Returns the point of the line that is m meters from the given point, heading to the given direction.
     * If null, such a point doesn't exist in the line.
     */
    public double[] findPointFromDistance(int startSegment,double sx,double sy,double heading,double m,double unitsPerMeter);

	public class LineAspects {
        public double distance;
		public double snapX;
		public double snapY;
		public int segment;
        public LineAspects() {}
        public LineAspects(LineAspects la) {
            distance=la.distance;
            snapX=la.snapX;
            snapY=la.snapY;
            segment=la.segment;
        }
	}

    public static final double EQUALITY_TOLERANCE=1E-6;
}