
/*
 * Created on 16 Δεκ 2005
 *
 */
package gr.cti.eslate.scene3d.viewer;

//~--- JDK imports ------------------------------------------------------------

import com.sun.opengl.util.GLUT;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

//~--- classes ----------------------------------------------------------------

/** A handle, encapsulating a Camera object, GL, GLU, and GLUT contexts, and providing some
 *  additional functionality, like allocation pbuffers.
 */
public class GLHandle {
    private Camera camera;
    GLAutoDrawable drawable;
    private GL     gl;
    private GLU    glu;
    private GLUT   glut;

    //~--- constructors -------------------------------------------------------

    GLHandle() {}

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param width
     * @param height
     *
     * @return
     */
    public GLPbuffer createOffscreenCanvas(int width, int height) {
        GLCapabilities caps = new GLCapabilities();

        caps.setHardwareAccelerated(true);    // We want hardware acceleration
        caps.setDoubleBuffered(false);

        // caps.setOffscreenRenderToTexture(true);
        // caps.setOffscreenRenderToTextureRectangle( true );
        return GLDrawableFactory.getFactory().createGLPbuffer(caps, null,
                width, height, drawable.getContext());
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public GL getGL() {
        return gl;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public GLU getGLU() {
        return glu;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public GLUT getGLUT() {
        return glut;
    }

    //~--- set methods --------------------------------------------------------

    void setParameters(GLAutoDrawable drawable, Camera camera, GL gl, GLU glu,
                       GLUT glut) {
        this.drawable = drawable;
        this.camera   = camera;
        this.gl       = gl;
        this.glu      = glu;
        this.glut     = glut;
    }
}
