package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.geom.Heading;

/**
 * Reports aspects of a motion on an agent host.
 *
 * @author  Giorgos Vasiliou
 * @version 1.0, 26 ��� 2002
 * @since   2.3.1
 */
public class MotionReport {
    /**
     * The total distance travelled.
     */
    public double distance;
    /**
     * The new longitude.
     */
    public double longt;
    /**
     * The new latitude.
     */
    public double lat;
    /**
     * The Heading object to help calculations the next time.
     */
    public Heading heading;
}
