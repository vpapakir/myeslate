package gr.cti.eslate.scene3d.viewer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.media.opengl.*;

import gr.cti.eslate.scene3d.viewer.actions.ActionQueue;
import gr.cti.eslate.math.linalg.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Viewer3D extends JPanel{

    ImageObserver screenshotObserver;

    CameraDirector director;

    // The list of all Object3Ds registered for rendering.
    ArrayList<Object3D> objectsToView = new ArrayList<Object3D>();

    private int windowWidth = 800;
    private int windowHeight = 600;

    OnscreenGLEventListener onScrGLLlistener;

    // Time
    Time time;
    // Actions
    ActionQueue actions;

    // Main Timer
    public Timer wallClock;

    // Can not use fullscreen mode yet due to incompatibility with OpenGL
    private static boolean TRY_FULLSCREEN = false;
//    static JPanel panel;
    static JFrame frame;
    GLCanvas canvas;
    GraphicsDevice dev;
    DisplayMode mode;
    

    
    
    /**
     * Constructor for the viewer
     */

    public Viewer3D(){
        director = new CameraDirector();
        try{
            initialize();
        } catch (Exception exc){
            exc.printStackTrace();
        }
        canvas.repaint();
        canvas.setFocusable(true);
        canvas.requestFocus();
    }
    
    /**
     * Returns the camera director.
     * @return The camera director.
     */

    public CameraDirector getDirector(){
        return director;
    }

    public void paint(Graphics g){

        canvas.repaint();
    }

    public Camera getActiveCamera(){
        return director.getActiveCamera();
    }
    
    /**
     * Adds a new Object3D in the viewer3d space.
     * @param object An Objec3D to be added
     */

    public void addObject3D(Object3D object){
        objectsToView.add(object);
        ((Object3D) object).viewers.add(this);
        if (canvas != null)
            canvas.repaint();
        actions.clear();
        if (wallClock!= null)
            wallClock.stop();
    }
    
    /**
     * Removes a new Object3D from the viewer3d space.
     * @param object The Object3D to be removed.
     */

    public void removeObject3D(final Object3D object){
        object.viewers.remove(this);   
        objectsToView.remove(object); 
        canvas.repaint();
        actions.clear();
        if (wallClock!= null)
            wallClock.stop();
    }
    
    /**
     * Removes all Object3Ds from the viewer3d space.
     */
    
    public void removeAllObjects3D(){
        for (int i=objectsToView.size()-1;i>=0;i--){
            ((Object3D) objectsToView.get(i)).viewers.remove(this);
            ((Object3D) objectsToView.get(i)).dispose();
            
            objectsToView.remove(i);
        }
        actions.clear();
        if (wallClock!= null)
            wallClock.stop();
    }

    public void setBackground(Color c){
        if (onScrGLLlistener!=null)
            onScrGLLlistener.setBackgroundColor(c);
        super.setBackground(c);
    }

    public void setVisible(boolean visible){
        if (visible)
            repaint();
        super.setVisible(visible);

    }
    

    public static void main(String[] args){

        frame = new JFrame("Viewer3D!");
        frame.setResizable(true);
        frame.setSize(800, 600);
        frame.getContentPane().setLayout(new BorderLayout());
        Viewer3D p = new Viewer3D();

        frame.getContentPane().add(p, BorderLayout.CENTER);
        frame.setVisible(true);

    }
    /**/

    public Image getScreenShot(ImageObserver obs){
        screenshotObserver = obs;
        onScrGLLlistener.screenShotMode = true;
        canvas.repaint();
        return onScrGLLlistener.screenShot;
    }
    
    private void initialize() throws IOException{
        Camera camera = newCamera("Main",0);
        director.addCamera(camera);
        director.setActiveCamera(0);
        
        dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        mode = null;
        setLayout(new BorderLayout());
        
        // Initialize GLCanvas, the hardware accelerated canvas.
        GLCapabilities caps = new GLCapabilities();
        caps.setHardwareAccelerated(true); //We want hardware acceleration
        caps.setDoubleBuffered(true);
        // NOTE: When using GLJPanel, double buffering in GLCapabilities must be set false. There are (11/1/05) many
        // suggestions that soon jogl (or JSR231 in Java) will be providing hardware accelerated GLJPanels using pbuffers
        // That would really be a breakthrough, since it will allow use of glasspanes, swing components and layout.

        //caps.setDoubleBuffered(false);
        canvas = new GLCanvas();//GLDrawableFactory.getFactory().createGLCanvas(caps);
        canvas.setBounds(0, 0, getWidth(), getHeight());
        //canvas.setRenderingThread(Thread.currentThread());
        //canvas.setNoAutoRedrawMode(false);
        canvas.setAutoSwapBufferMode(true);
        
        /*JPanel p  = new JPanel();
        p.setBackground(Color.YELLOW);
        add(p, BorderLayout.CENTER);
        */
        add(canvas,BorderLayout.CENTER);
        
        //setBorder(BorderFactory.createLineBorder(Color.red,10));

        onScrGLLlistener = new OnscreenGLEventListener(this);
        canvas.addGLEventListener(onScrGLLlistener);
        
        /*
        // Initialize offscreen panel, a non-hardware accelerated canvas for
        // offscreen rendering.
        GLCapabilities cCaps = new GLCapabilities();
        cCaps.setHardwareAccelerated(false);
        cCaps.setDoubleBuffered(false);
        offscreenPanel = new OffscreenPanel(cCaps, null,windowWidth, windowHeight);
        offscreenPanel.setBounds(0,0,getWidth(), getHeight());
        offScrGLListener = new OffscreenGLEventListener(onScrGLLlistener); 
        
        offscreenPanel.addGLEventListener(offScrGLListener);
        offscreenPanel.display();
        
        BufferedImage im = offscreenPanel.getSnapshot();
        ImageIO.write(im, "png", new File("image.png"));
        //add(offscreenPanel, BorderLayout.CENTER);
        JFrame f = new JFrame("Switch");
        JButton b = new JButton("Switch!");
        f.add(b);
        f.setSize(100,100);
        f.setVisible(true);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setRenderingMode(!onScreenRenderingMode);
            }
        });
        */
        
        if (TRY_FULLSCREEN && dev.isFullScreenSupported()){
            // Choose screen resolution (or at least try!)
            //mode = ScreenResSelector.showSelectionDialog();
            //GraphicsDevice dev = getDefaultScreenDevice();
            mode = dev.getDisplayMode();
            windowWidth = mode.getWidth();
            windowHeight = mode.getHeight();
            dev.setFullScreenWindow(frame);

        } else{
            dev.setFullScreenWindow(null);
        }
        //...if not then just center to screen
        //if (!fullScreen) {
        //  center(frame, Toolkit.getDefaultToolkit().getScreenSize());
        //}
    }
    
    public void update(Graphics g){
        //System.out.println("CALLED UPDATE");
    }
    
    private Camera newCamera(String name, int mode){
        Camera camera = new Camera(this, name);
        camera.setAspectRatio(1.33333);
        camera.setFovY(MathUtils.dec2rad(45.0f));
        camera.setMode(mode);
        return camera;
    }
    
    private boolean onScreenRenderingMode = true;

    void setRenderingMode(boolean onscreen){
    }

}
