package gr.cti.eslate.utils;

import java.awt.Point;
/**
 * Custom border for E-Slate components.
 *
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 */


public interface CustomBorder {
    /** Constant which indicates the south border area of the component */
    public int SOUTH = 1;
    /** Constant which indicates the south east border area of the component */
    public int SOUTH_EAST = 2;
    /** Constant which indicates the south west border area of the component */
    public int SOUTH_WEST = 3;
    /** Constant which indicates the west border area of the component */
    public int WEST = 4;
    /** Constant which indicates the east border area of the component */
    public int EAST = 5;
    /** Constant which indicates the north border area of the component */
    public int NORTH = 6;
    /** Constant which indicates the north east border area of the component */
    public int NORTH_EAST = 7;
    /** Constant which indicates the north west border area of the component */
    public int NORTH_WEST = 8;
    /** Constant which indicates the inside(main) area of the component */
    public int INSIDE = 0;
    /** Returns if the components has a non-linear bottom border. If the component
     *  wants to specify a custom area as its south border area, this method should
     *  return true.
     */
    public boolean hasCustomBottomBorder();
    /** Returns if the components has a non-linear top border. If the component
     *  wants to specify a custom area as its north border area, this method should
     *  return true.
     */
    public boolean hasCustomTopBorder();
    /** Returns if the components has a non-linear west border. If the component
     *  wants to specify a custom area as its west border area, this method should
     *  return true.
     */
    public boolean hasCustomWestBorder();
    /** Returns if the components has a non-linear east border. If the component
     *  wants to specify a custom area as its east border area, this method should
     *  return true.
     */
    public boolean hasCustomEastBorder();
    /** Returns the area of the component (border area or interior) which contains the
     *  given point.
     */
    public int getPointRelativeLocation(Point p);
}

