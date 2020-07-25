package gr.cti.eslate.scene3d.viewer.event;

//~--- non-JDK imports --------------------------------------------------------
import gr.cti.eslate.math.linalg.*;

//~--- classes ----------------------------------------------------------------

/**
 * Created by A.Mantes at 25 ײוג 2004, 7:17:55 לל
 */
public class CameraEvent extends java.util.EventObject {
    private static final long serialVersionUID      = 2206575332733919275L;
    public static final int   POSITION_CHANGED      = 0;
    public static final int   ORIENTATION_CHANGED   = 1;
    public static final int   CAMERA_STOPPED_MOVING = 3;
    public static final int   CAMERA_STARTED_MOVING = 2;

    //~--- fields -------------------------------------------------------------

    int   eventID;
    Vec3d position, oldPosition, orientation, oldOrientation;

    //~--- constructors -------------------------------------------------------

    public CameraEvent(Object source, int id, Vec3d position,
                       Vec3d orientation) {
        super(source);
        this.eventID     = id;
        this.position    = position;
        this.orientation = orientation;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Vec3d getOrientation() {
        return orientation;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getPitchAngle() {
        return orientation.x();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Vec3d getPosition() {
        return position;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getRollAngle() {
        return orientation.z();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getX() {
        return position.x();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getY() {
        return position.y();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getYawAngle() {
        return orientation.y();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getZ() {
        return position.z();
    }
}
