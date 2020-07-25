package gr.cti.eslate.scene3d.viewer.event;

//~--- JDK imports ------------------------------------------------------------

import java.util.EventListener;

//~--- interfaces -------------------------------------------------------------

/**
 * Created by A.Mantes at 25 ��� 2004, 7:17:31 ��
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
