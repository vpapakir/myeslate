package gr.cti.eslate.scene3d.viewer.event;

//~--- JDK imports ------------------------------------------------------------

import java.util.EventListener;

//~--- interfaces -------------------------------------------------------------

/**
 * Created by A.Mantes at 25 ײוג 2004, 7:17:31 לל
 */
public interface CameraListener extends EventListener {

    /**
     * Method description
     *
     *
     * @param e
     */
    public void orientationChanged(CameraEvent e);

    /**
     * Method description
     *
     *
     * @param e
     */
    public void positionChanged(CameraEvent e);
}
