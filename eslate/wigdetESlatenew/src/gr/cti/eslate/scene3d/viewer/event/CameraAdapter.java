package gr.cti.eslate.scene3d.viewer.event;

/**
 * Created by A.Mantes at 25 ײוג 2004, 7:18:10 לל
 */
public abstract class CameraAdapter implements CameraListener {

    /**
     * Method description
     *
     *
     * @param e
     */
    public void orientationChanged(CameraEvent e) {}

    /**
     * Method description
     *
     *
     * @param e
     */
    public void positionChanged(CameraEvent e) {}

/*
    public void cameraStartedMoving(CameraEvent e) {}

    public void cameraStoppedMoving(CameraEvent e) {}
*/
}
