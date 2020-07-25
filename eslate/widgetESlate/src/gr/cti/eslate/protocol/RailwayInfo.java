package gr.cti.eslate.protocol;

public interface RailwayInfo extends MotionInfo {
    public GeographicObject getRailwayLine(double x,double y);
    /**
     * @return All the railroad that are adjacent to the given point. Used to decide on crossings.
     */
    public GeographicObject[] getAdjacentRailways(double x,double y);
}

