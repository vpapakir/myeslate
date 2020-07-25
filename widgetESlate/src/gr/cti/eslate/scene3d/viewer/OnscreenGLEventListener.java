package gr.cti.eslate.scene3d.viewer;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import gr.cti.eslate.scene3d.viewer.actions.ActionQueue;
import gr.cti.eslate.math.linalg.*;

import javax.swing.*;

import com.sun.opengl.util.GLUT;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import java.awt.event.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by A.Mantes at 5 Íןו 2003, 6:35:31 לל
 */
public class OnscreenGLEventListener implements GLEventListener {

    private Camera camera;

    private GLHandle glHandle;
    
    private GL gl;
    private GLUT glut                      = new GLUT();
    private GLU  glu                       = new GLU();
    MouseInputController  mouseController;
    private float[]     zbuffer      = new float[1];
    private FloatBuffer zBufferBuff  = FloatBuffer.wrap(zbuffer);
    private int  flyIndicatorDisplayListID = -1, pinpointDisplayListID = -1;

    Viewer3D viewer3D;

    MouseInputController mouseMotionController;

    KeyController keyController;

    MouseWheelController mouseWheelController;

    // int mouseX = 1, mouseY = 1;

    // Window height is needed (along with aspect ratio) to compute
    // pixels per unit in global coordinates

    private int windowWidth = 800;

    private int windowHeight = 600;

    // Wireframe mode
    boolean wireframeView = false;

    // Sky&Sun enabled
    private boolean skyEnabled = false;

    float lastMouseZBuffer = 0.0f;
    Vec3d lastPointIn3DSpace = null;
    float centerZBuffer = 0.0f;

    private Vec3f horizonColor = new Vec3f(168/255f, 207/255.f, 247/255.f);

    private float fps;

    private float ppf; // Polys (really triangles) per frame

    boolean shouldDisplay = false;

    // Output
    boolean showHUD = false;

    private HeadUpDisplay hud;

    Image screenShot;

    boolean screenShotMode = false;

    float[] pixels;

    private int textureID;

    // Icons for cursors...

    ImageIcon handIcon = new ImageIcon(OnscreenGLEventListener.class
            .getResource("icons/cursors/crs_hand_32.gif"));

    ImageIcon grabIcon = new ImageIcon(OnscreenGLEventListener.class
            .getResource("icons/cursors/crs_grab_32.gif"));

    ImageIcon panIcon = new ImageIcon(OnscreenGLEventListener.class
            .getResource("icons/cursors/crs_pan_32.gif"));

    ImageIcon rotateIcon = new ImageIcon(OnscreenGLEventListener.class
            .getResource("icons/cursors/crs_rotate_32.gif"));

    // ImageIcon grabIcon = new
    // ImageIcon(OpenGLListener.class.getResource("icons/cursors/crs_grab_32.gif"));
    // Cursors

    Cursor handCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            handIcon.getImage(), new Point(16, 4), "HAND");
    Cursor grabCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            grabIcon.getImage(), new Point(16, 4), "GRAB");
    Cursor panCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            grabIcon.getImage(), new Point(16, 4), "PAN");
    Cursor rotateCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            rotateIcon.getImage(), new Point(16, 8), "ROTATE");

    ImageIcon flyIcon = new ImageIcon(OnscreenGLEventListener.class
            .getResource("icons/fly.png"));

    private boolean initialized = false;

    // A timer, which causes screen refresh every one second. Used to avoid
    // CPU load when using Animator instead of Canvas.
    private Timer screenRefreshTimer;

    private Mat4d camMat = new Mat4d();

    private double[] camMatDat = new double[16];
    
    // GC call intervals in seconds.
    private int gcCallInterval = 3;

    /** Sets the background color */
    void setBackgroundColor(Color c) {
        if (c!=null)
            horizonColor.set(c.getRed()/255.f, c.getGreen()/255.f,
                    c.getBlue()/255.f);
    }

    
    public OnscreenGLEventListener(Viewer3D viewer3D) {
        this.viewer3D = viewer3D;
        // Time and actions
        viewer3D.time = new Time();
        viewer3D.actions = new ActionQueue(viewer3D);
        screenRefreshTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OnscreenGLEventListener.this.viewer3D.repaint();
            }
        });
        screenRefreshTimer.setRepeats(true);
        screenRefreshTimer.start();

    }

    public void init(final GLAutoDrawable drawable) {
        initialized = true;
        // FIXME: hack
        // ((Component) drawable).requestFocus();

        GL gl = drawable.getGL();
        // drawable.setGL(new DebugGL(gl));
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        gl.glScalef(1.0f, - 1.0f, 1.0f);

        gl.glEnable(GL.GL_DEPTH_TEST);

        // Set up perspective camera
        camera = viewer3D.director.getActiveCamera();

        hud = new HeadUpDisplay();
        drawable.removeKeyListener(keyController);
        drawable.removeMouseMotionListener(mouseController);
        drawable.removeMouseListener(mouseController);
        drawable.removeMouseWheelListener(mouseWheelController);
        drawable.addKeyListener(keyController = new KeyController(viewer3D));
        drawable.addMouseWheelListener(mouseWheelController = new MouseWheelController(viewer3D));
        mouseController = new MouseInputController(viewer3D);
        drawable.addMouseMotionListener(mouseController);
        drawable.addMouseListener(mouseController);

        viewer3D.wallClock = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (camera==null)
                    return;
                if (camera.isOrbiting()) {
                    camera.orbit();
                } else if (camera.isLookingAround()) {
                    camera.lookAround();
                }
                // System.out.println("CLOCK TICKING"+
                // System.currentTimeMillis());
                viewer3D.canvas.repaint();
            }
        });

        // Temporarily initialize texture for flyMode
        int[] texs = new int[1];
        gl.glGenTextures(1, texs,0);
        textureID = texs[0];

        int width = 64;
        int height = 64;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                0.85f));
        g2.drawImage(flyIcon.getImage(), 0, 0, null);

        int[] pixels = ((DataBufferInt) image.getData().getDataBuffer())
                .getData();

        // System.out.println("TEXTURE ID for this : "+textureID);
        gl.glShadeModel(GL.GL_FLAT);
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
                GL.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
                GL.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
                GL.GL_NEAREST);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
                GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, 64, 64, 0, GL.GL_BGRA,
                GL.GL_UNSIGNED_INT_8_8_8_8_REV, IntBuffer.wrap(pixels));
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
        g2.dispose();
        image.flush();
        image = null;

    }

    private boolean disposeFlag = false;

    void dispose() {
        disposeFlag = true;
        viewer3D.canvas.repaint();
    }

    private void dispose(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glDeleteTextures(1, new int[] { textureID },0);
    }

    public void display(final GLAutoDrawable drawable) {
        if (!initialized) {
            return;
        }

        gl  = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();

        // drawable.setGL(new DebugGL(gl)); // enable this to get debug results
        if (disposeFlag) {
            dispose(drawable);
            disposeFlag = false;

            return;
        }

        glHandle = new GLHandle();
        camera = viewer3D.director.getActiveCamera();
        glHandle.setParameters(viewer3D.canvas, camera, gl, glu, glut);
        viewer3D.time.update();
        camera.getModelViewMatrix(Vec3d.ORIGIN, camMat);
        camMat.getColumnMajorData(camMatDat);
        gl.glLoadMatrixd(camMatDat, 0);
        camera.viewInfo.calculateViewplanes(gl);
        
        if (! viewer3D.actions.empty()) {
            if (! viewer3D.wallClock.isRunning())
                viewer3D.wallClock.start();
            viewer3D.actions.update(viewer3D.time.time());
            // System.out.println("actions not empty!");
        } else if (! camera.isOrbiting()&&! camera.isLookingAround()) {
            if (viewer3D.wallClock.isRunning())
                viewer3D.wallClock.stop();
        }
        gl.glClearColor(horizonColor.x(), horizonColor.y(), horizonColor.z(),
                        0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (wireframeView) {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        } else {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
        }

        // Render Objects
        int objects3DNum = viewer3D.objectsToView.size();
        
        // Experimntal pinpoint pos rendering point

        /*
        gl.glPointSize(10);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(0,1,0);
        if (camera!= null && camera.pinpointPosition!= null){
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3d(camera.pinpointPosition.x(),camera.pinpointPosition.y(), camera.pinpointPosition.z());
            gl.glEnd();
        }
        */

        
        for (int i = 0; i < objects3DNum; i++) {
            viewer3D.objectsToView.get(i).render(glHandle);
        }

        if (screenShotMode) {
            pixels = new float[3 * windowWidth * windowHeight];
            gl.glReadPixels(0, 0, windowWidth, windowHeight, GL.GL_RGB,
                            GL.GL_FLOAT, FloatBuffer.wrap(pixels));

            BufferedImage vBufferedImage = new BufferedImage(windowWidth,
                                               windowHeight,
                                               BufferedImage.TYPE_INT_RGB);

            // Copy pixel data into buffered image
            for (int vRow = 0; vRow < windowHeight; vRow++) {
                for (int vColumn = 0; vColumn < windowWidth; vColumn++) {

                    // Get separate color components
                    int vIndex = ((vRow * windowWidth) + vColumn) * 3;
                    int r      = (int) (pixels[vIndex] * 255);
                    int g      = (int) (pixels[vIndex + 1] * 255);
                    int b      = (int) (pixels[vIndex + 2] * 255);

                    /*
                     * Set rgb color by shifting components into corresponding
                     * integer bits.
                     */
                    int vRgb = (r << 16) + (g << 8) + b;

                    // Set buffer image pixel -- flip y coordinate
                    vBufferedImage.setRGB(vColumn, windowHeight - vRow - 1,
                                          vRgb);
                }
            }

            ImageIcon icon = new ImageIcon(vBufferedImage);

            screenShot     = icon.getImage();
            screenShotMode = false;
            viewer3D.screenshotObserver.imageUpdate(screenShot,
                    ImageObserver.ALLBITS, 0, 0, icon.getIconWidth(),
                    icon.getIconHeight());
        }
        
//        if (camera.pinpointTimer.isRunning()){
//            Mat4d modelviewMatrix = new Mat4d(), projectionMatrix = new Mat4d();
//            camera.getModelViewMatrix(Vec3d.ORIGIN,modelviewMatrix);
//            camera.getProjectionMatrix(projectionMatrix);
//            Vec2i screenPos = ProjectionTransform.spaceToScreenCoordinates(camera.pinpointPosition,
//                                                         modelviewMatrix, 
//                                                         projectionMatrix, 
//                                                         new Dimension(camera.getViewWidth(), camera.getViewHeight()));
//            
//
//            gl.glPushMatrix();
//            gl.glMatrixMode(GL.GL_PROJECTION);
//            gl.glLoadIdentity();
//            glu.gluOrtho2D(0.0, viewer3D.getSize().width, 0.0,
//                           viewer3D.getSize().height);
//            
//            gl.glMatrixMode(GL.GL_MODELVIEW);
//            gl.glLoadIdentity();
//            double scaleFactor = (camera.pinpointTimer.ticks%15)/15.0f;
//            gl.glTranslated(screenPos.x(), screenPos.y(), 0);
//            gl.glScaled(scaleFactor,scaleFactor,1);
//            gl.glCallList(pinpointDisplayListID);
//            gl.glPopMatrix();
//        }

        // Draw fly indicator
        if (camera.isFlying()) {
            gl.glPushMatrix();
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluOrtho2D(0.0, viewer3D.getSize().width, 0.0,
                           viewer3D.getSize().height);
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslated(viewer3D.getSize().width/2,viewer3D.getSize().height/2,0);
            gl.glCallList(flyIndicatorDisplayListID);
            gl.glPopMatrix();
        }

        // Display HUD
        if (showHUD) {
            hud.draw(glHandle, camera, fps);
        }
        
        centerZBuffer = getZBuffer(gl,windowWidth/2,windowHeight/2); 
        lastMouseZBuffer = getZBuffer(gl,mouseController.mouseX,mouseController.mouseY);
        if (lastMouseZBuffer!=0 && lastMouseZBuffer!=1){
            // calculate point coords in 3d space
            Mat4d modelViewMatrix  = new Mat4d();
            Mat4d projectionMatrix = new Mat4d();
    
            camera.getModelViewMatrix(new Vec3d(0, 0, 0), modelViewMatrix);
            camera.getProjectionMatrix(projectionMatrix);
            lastPointIn3DSpace  = ProjectionTransform.screenToSpaceCoordinates(
                        new Vec2i(mouseController.mouseX,windowHeight - mouseController.mouseY),
                        modelViewMatrix,
                        projectionMatrix,
                        new Dimension(camera.getViewWidth(),camera.getViewHeight()),
                        lastMouseZBuffer);
        }else{
            lastPointIn3DSpace=null;
        }
    
        camera.updateOpenGL(gl, glu, true);
        gl.glFlush();
    }

    void getScreenShot(GL gl) {
        float[] pixels = new float[3*windowWidth*windowHeight];
        gl.glReadPixels(0, 0, windowWidth, windowHeight, GL.GL_RGB,
                GL.GL_FLOAT, FloatBuffer.wrap(pixels));
    }

    // Note : for a strange reason, this method fails if called outside a
    // display()
    // method context, that's why the "lastMouseZBuffer" variable is used
    // for....
    // maybe it should be checked in the future...

    // ... was solved the same day! When updating a camera, alway viewport
    // should be
    // set first!(but glReadPixels still fails...)

    float getZBuffer(GL gl, int x, int y) {
        gl.glViewport(0, 0, windowWidth, windowHeight);
        gl.glReadPixels(x, windowHeight-y, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, zBufferBuff);
        return zbuffer[0];
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        if (width*height==0)
            return;
        GL gl = drawable.getGL();
        if (height<=0) // avoid a divide by zero error!
            height = 2;
        windowWidth = width;
        windowHeight = height;
        hud.setSize(width, height);
        float ar = (float) width/height;
        float fov;
        fov = (float) (Math.PI/4*((float) 4/3)/ar);
        camera.setFovY(fov);
        camera.setAspectRatio(ar);
        gl.glViewport(0, 0, width, height);

    }

    private void setActiveCamera(int index) {
        viewer3D.director.setActiveCamera(index);
        viewer3D.canvas.repaint();
    }

    // Unused routines
    public void preDisplay(GLDrawable drawable) {
        // System.out.println("PREDISPLAY");
    }

    public void postDisplay(GLDrawable drawable) {
        // System.out.println("postDisplay");
    }

    public void cleanup(GLDrawable drawable) {
        // System.out.println("CLEANUP");
    }

    public void displayChanged(GLAutoDrawable gl, boolean b1, boolean b2) {
        // System.out.println("displayChanged");
    }

    // /////////////////////////////////////UNUSED
    // ROUTINES/////////////////////////////////

    /*
     * float[] spaceToScreenCoords(Vec3f screenCoords) {
     * //System.out.println("spaceToScreenCoords() called"); double[]
     * modelviewMatrix = new double[16], projectionMatrix = new double[16];
     * int[] viewport = new int[]{0, 0, windowWidth, windowHeight}; Camera
     * camera = viewer3D.director.getActiveCamera();
     * 
     * projectionMatrix = camera.projdata; gl.glViewport(0, 0, windowWidth,
     * windowHeight); camera.updateOpenGL(gl, glu, true); //double[]
     * modelviewMatrix = new double[16]; Mat4d matrix = new Mat4d();
     * camera.getModelViewMatrix(new Vec3d(0.0, 0.0, 0.0), matrix);
     * matrix.getColumnMajorData(modelviewMatrix); double[] objx = new
     * double[1]; double[] objy = new double[1]; double[] objz = new double[1];
     * 
     * //float zBuffer=lastMouseZBuffer; glu.gluProject((double)
     * screenCoords.x(), (double) screenCoords.y(), (double) screenCoords.z(),
     * modelviewMatrix, projectionMatrix, viewport, objx, objy, objz);
     * 
     * 
     * return new float[]{(float) objx[0], (float) objy[0], (float) objz[0]}; }
     * 
     * float[] screenToSpaceCoords(int mouseX, int mouseY) { double[]
     * modelviewMatrix = new double[16], projectionMatrix = new double[16];
     * int[] viewport = new int[]{0, 0, windowWidth, windowHeight}; Camera
     * camera = viewer3D.director.getActiveCamera();
     * 
     * projectionMatrix = camera.projdata; //gl.glViewport(0, 0, windowWidth,
     * windowHeight); //camera.updateOpenGL(gl, glu, true); //double[]
     * modelviewMatrix = new double[16]; Mat4d matrix = new Mat4d();
     * camera.getModelViewMatrix(new Vec3d(0.0, 0.0, 0.0), matrix);
     * matrix.getColumnMajorData(modelviewMatrix);
     * 
     * double[] objx = new double[1]; double[] objy = new double[1]; double[]
     * objz = new double[1]; float zBuffer =
     * lastMouseZBuffer;//getZBuffer(mouseX, windowHeight-mouseY); if (zBuffer >
     * 0) glu.gluUnProject((double) mouseX, (double) (windowHeight - mouseY),
     * (double) zBuffer, modelviewMatrix, projectionMatrix, viewport, objx,
     * objy, objz); return new float[]{(float) objx[0], (float) objy[0], (float)
     * objz[0]}; }
     */

}
