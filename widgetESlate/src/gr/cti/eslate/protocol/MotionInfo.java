package gr.cti.eslate.protocol;

public interface MotionInfo {
	/**
	 * @ return Given an x,y location this function returns the location of the nearest entry point, if any. <em>Null</em> if no entry points are defined.
	 */
	public java.awt.geom.Point2D.Double getEntryPointLocation(double x,double y);
	/**
	 * @ return Given an x,y location this function returns a non-null value if it is a valid position.
	 * This way the implementor of the interface can define special geographic areas such as roads
	 * and sea, and answer e.g. if a given point is in or out of this area. The returned value
	 * is an object on which the motion will take place or a <code>true</code> Boolean object
	 * if the motion is permited everywhere.
	 */
	public boolean isValidLocation(double x,double y,double tolerance);
	/**
	 * Given a start and end x,y location this function returns a non-null value
	 * with information about the trip (validity, snappings etc).
	 * This way the implementor of the interface can define special geographic areas such as roads
	 * and sea, and answer e.g. if a given point is in or out of this area. The returned value
	 * is an object on which the motion will take place.
	 */
	public MotionFeatures isValidTrip(double startLongt,double startLat,double endLongt,double endLat,double tolerance);
	/**
	 * The ID of the motion layer. May be one the predefined values or a custom one.
	 */
	public void setID(String id);
	/**
	 * The ID of the motion layer. May be one the predefined values or a custom one.
	 */
	public String getID();

	public static final String MOTION_ROAD_KEY="$$$__typeroad";
	public static final String MOTION_RAILWAY_KEY="$$$__typerailway";
	public static final String MOTION_SEA_KEY="$$$__typesea";
	public static final String MOTION_AIR_KEY="$$$__typeair";
}
