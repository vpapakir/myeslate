
/*
 * Created on 13 Ïêô 2005
 *
 */
package gr.cti.eslate.scene3d.viewer;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

public abstract class Object3D {
    ArrayList<Viewer3D> viewers = new ArrayList<Viewer3D>();

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     */
    public abstract void dispose();

    // public abstract void initialize(Camera camera, GL gl, GLU glu);
    // public abstract int render(Camera camera, GL gl, GLU glu);
    // New initialize and render methods, using GLHandle

    /**
     * Method description
     *
     *
     * @param handle
     */
    public abstract void initialize(GLHandle handle);

    /**
     * Method description
     *
     *
     * @param handle
     *
     * @return
     */
    public abstract int render(GLHandle handle);

    /**
     * Method description
     *
     */
    public void renderStateChanged() {
        for (int i = 0; i < viewers.size(); i++) {

//          viewers.get(i).renderingWorkerThread.setDirty();
            viewers.get(i).canvas.repaint();
        }
    }
}
