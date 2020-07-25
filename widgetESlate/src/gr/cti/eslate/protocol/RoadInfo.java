package gr.cti.eslate.protocol;

public interface RoadInfo extends MotionInfo {
    public GeographicObject getRoadLine(double x,double y);
    /**
     * @return All the roads that are adjacent to the given point. Used to decide on crossroads.
     */
    public GeographicObject[] getAdjacentRoads(double x,double y);
}

