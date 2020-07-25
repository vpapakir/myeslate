package gr.cti.eslate.scene3d.viewer.event;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashSet;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

/**
 * Created by A.Mantes at 26 ÷ев 2004, 11:21:16 рм
 */
public class CameraEventMulticaster extends HashSet implements CameraListener {
    private static final long serialVersionUID =
        new String("CameraEventMulticaster").hashCode();

    //~--- constructors -------------------------------------------------------

    public CameraEventMulticaster() {
        super();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param e
     */
    public void orientationChanged(CameraEvent e) {
        Iterator       it = iterator();
        CameraListener listener;

        while (it.hasNext()) {
            listener = (CameraListener) it.next();
            listener.orientationChanged(e);
        }
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void positionChanged(CameraEvent e) {
        Iterator it = iterator();

        while (it.hasNext()) {
            ((CameraListener) it.next()).positionChanged(e);
        }
    }
}
