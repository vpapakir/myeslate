package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------
import gr.cti.eslate.math.linalg.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

//~--- classes ----------------------------------------------------------------

/**
 * Created by IntelliJ IDEA.
 * User: mantesat
 * Date: 3 Ιαν 2005
 * Time: 1:36:36 μμ
 * To change this template use Options | File Templates.
 */
public class MouseInputController extends MouseInputAdapter {
    boolean      isFlying    = false;
    private Vec3d mouseDragPoint;
    Vec3f intersectionPoint;
    boolean      ctrlPressed = false;
    int          oldX        = 0,
                 oldY        = 0, mouseX, mouseY;
    StringBuffer buf         = new StringBuffer();
    Camera       camera;

    // Timer speedIncrementor, speedDecrementor, clickTimer;
    MouseEvent     clickSaveEvent;
    boolean        leftButtonPressed, rightButtonPressed;
    final Viewer3D viewer;
    
    private Vec2i screenCoords = new Vec2i(0,0);
    private Plane plane = new Plane();

    //~--- constructors -------------------------------------------------------

//  private EventTimeStampBindComparator bindComparator = new EventTimeStampBindComparator();
//  
//  /** Its not wise to process every single event that comes from outside, too much render 
//  load. Instead, an event queue is maintained for each event type, and during periodic 
//  intervals, the last event in queues is handled, and all other events -of the same type- 
//  are disposed. Note that when flushing last-arrived events for every type, the event that
//  came first is flushed first! */
//  
//  // FIXME/NOTE: Maybe the same policy should be followed for the entire event handling, and 
//  // for the viewer3D?
//  
//  private ArrayList mouseMovedEventQueue = new ArrayList(),       // The event queue for mouse moves
//                    mouseDraggedEventQueue = new ArrayList(),     // The event queue for mouse draggs
//                    mousePressedEventQueue = new ArrayList(),     // The event queue for mouse presses
//                    mouseReleasedEventQueue = new ArrayList(),    // The event queue for mouse releases
//                    mouseClickedEventQueue = new ArrayList(),     // The event queue for mouse clicks
//                    mouseEnteredEventQueue = new ArrayList(),     // The event queue for mouse enters
//                    mouseExitedEventQueue = new ArrayList();      // The event queue for mouse exits
//
////  Timer eventTimer;
//  
//  /** A small class, used to bind an event with a timestamp */
//  
//  private class EventTimeStampBind {
//      MouseEvent event;
//      long nanosecTimeStamp;
//      
//      EventTimeStampBind(MouseEvent e, long nanosecTimeStamp){
//          this.event = e;
//          this.nanosecTimeStamp = nanosecTimeStamp;       
//      }
//  }
//  
//  /** and its comparator */
//   
//  private class EventTimeStampBindComparator implements Comparator{
//      
//      public int compare(Object o1, Object o2){
//          EventTimeStampBind b1 = (EventTimeStampBind) o1;
//          EventTimeStampBind b2 = (EventTimeStampBind) o2;
//          
//          if (b1.nanosecTimeStamp<b2.nanosecTimeStamp){
//              return -1;
//          }else if(b1.nanosecTimeStamp>b2.nanosecTimeStamp){
//              return 1;
//          }else{
//              return 0;
//          }
//      }
//  }
    public MouseInputController(Viewer3D viewer) {
        this.viewer = viewer;
        camera      = viewer.getActiveCamera();

//      eventTimer = new Timer(30,new ActionListener(){
//          MouseEvent event;
//          EventTimeStampBind bind;
//          ArrayList bindsToFire = new ArrayList();
//          
//          public void actionPerformed(ActionEvent e){
//              bind = flushEvent(mouseClickedEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);
//              bind = flushEvent(mouseDraggedEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);
//              bind = flushEvent(mouseMovedEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);
//              bind = flushEvent(mousePressedEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);
//              bind = flushEvent(mouseReleasedEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);
//              bind = flushEvent(mouseEnteredEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);               
//              bind = flushEvent(mouseExitedEventQueue);
//              if (bind != null)
//                  bindsToFire.add(bind);
//              
//              // Now to fire events, using fifo order
//              Collections.sort(bindsToFire, bindComparator);
//              for (int i=0;i<bindsToFire.size();i++){
//                  processEvent(((EventTimeStampBind) bindsToFire.get(i)).event);
//              }
//              bindsToFire.clear();
//              
//              
//      }
//      });
//      eventTimer.start();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {

        // mouseClickedEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseDragged(MouseEvent e) {

        // mouseDraggedEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {

        // mouseEnteredEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {

        // mouseExitedEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseMoved(MouseEvent e) {

        // mouseMovedEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {

        // mousePressedEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {

        // mouseReleasedEventQueue.add(0,new EventTimeStampBind(e,System.nanoTime()));
        processEvent(e);
    }

    /**
     * Single mouse click handling method
     * @param e The Mouse event
     */
    private void mouseSingleClicked(MouseEvent e) {

        // viewer.actions.clear();
        // viewer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        int factor = 1;

        if (ctrlPressed) {
            factor = 2;
        }

        if ((e.getButton() == MouseEvent.BUTTON1) &&!camera.isOrbiting()) {
            if (viewer.onScrGLLlistener.keyController.altPressed) {
                if (!camera.isFlying()) {
                    camera.startFlying();

                    return;
                }

                camera.stopFlying();

                return;
            }

            if (camera.isFlying()) {
                return;
            }

            mouseX = e.getX();
            mouseY = e.getY();

            if ((viewer.onScrGLLlistener.lastMouseZBuffer == 1.0f)
                    || (viewer.onScrGLLlistener.lastMouseZBuffer == 0.0f)) {
                return;
            }

            Mat4d modelViewMatrix  = new Mat4d();
            Mat4d projectionMatrix = new Mat4d();

            camera.getModelViewMatrix(new Vec3d(0, 0, 0), modelViewMatrix);
            camera.getProjectionMatrix(projectionMatrix);

            Vec3d objCoords =
                ProjectionTransform
                    .screenToSpaceCoordinates(new Vec2i(mouseX,
                        camera.getViewHeight() - mouseY), modelViewMatrix,
                            projectionMatrix,
                            new Dimension(camera.getViewWidth(),
                                          camera.getViewHeight()), viewer
                                              .onScrGLLlistener
                                              .lastMouseZBuffer);

            // System.out.println("coords3:"+objCoords[0]+", "+objCoords[1]+", "+objCoords[2]);
            // System.out.println("Called retreatFrom...");
            camera.approachTo(objCoords.get(0), objCoords.get(2),
                              objCoords.get(1));
        } else if ((e.getButton() == MouseEvent.BUTTON3)
                   &&!camera.isOrbiting() &&!camera.isFlying()) {

            // retreat! (move backwards)
            mouseX = e.getX();
            mouseY = e.getY();

            if ((viewer.onScrGLLlistener.lastMouseZBuffer == 1.0f)
                    || (viewer.onScrGLLlistener.lastMouseZBuffer == 0.0f)) {
                return;
            }

            Mat4d modelViewMatrix  = new Mat4d();
            Mat4d projectionMatrix = new Mat4d();

            camera.getModelViewMatrix(new Vec3d(0, 0, 0), modelViewMatrix);
            camera.getProjectionMatrix(projectionMatrix);

            Vec3d objCoords =
                ProjectionTransform
                    .screenToSpaceCoordinates(new Vec2i(mouseX,
                        camera.getViewHeight() - mouseY), modelViewMatrix,
                            projectionMatrix,
                            new Dimension(camera.getViewWidth(),
                                          camera.getViewHeight()), viewer
                                              .onScrGLLlistener
                                              .lastMouseZBuffer);

            // System.out.println("coords3:"+objCoords[0]+", "+objCoords[1]+", "+objCoords[2]);
            // System.out.println("Called retreatFrom...");
            camera.retreatFrom(objCoords.get(0), objCoords.get(2),
                               objCoords.get(1));
        }
    }

    private boolean notifyObjects3DForEvent(InputEvent e) {
        Object3D obj3d;

        for (int i = 0; i < viewer.objectsToView.size(); i++) {
            obj3d = viewer.objectsToView.get(i);

            if (obj3d instanceof EventResponsive) {
                boolean b = ((EventResponsive) obj3d).handleEvent(e);

                if (b) {
                    return true;
                }
            }
        }

        return false;
    }

    private void processEvent(MouseEvent e) {
        switch (e.getID()) {
        case MouseEvent.MOUSE_MOVED :
            processMouseMoveEvent(e);

            break;

        case MouseEvent.MOUSE_DRAGGED :
            processMouseDragEvent(e);

            break;

        case MouseEvent.MOUSE_PRESSED :
            processMousePressedEvent(e);

            break;

        case MouseEvent.MOUSE_RELEASED :
            processMouseReleasedEvent(e);

            break;

        case MouseEvent.MOUSE_CLICKED :
            processMouseClickedEvent(e);

            break;

        case MouseEvent.MOUSE_ENTERED :
            processMouseEnteredEvent(e);

            break;

        case MouseEvent.MOUSE_EXITED :
            processMouseExitedEvent(e);

            break;
        }
    }

    private void processMouseClickedEvent(MouseEvent e) {
        if (notifyObjects3DForEvent(e)) {
            return;
        }
        mouseSingleClicked(e);
    }

    private void processMouseDragEvent(MouseEvent e) {
        if ((oldX == e.getX()) && (oldY == e.getY())) {
            return;
        }
        mouseX = e.getX();
        mouseY = e.getY();

        Object3D obj3d;

//      if (camera.isMoving()){
//          return;
//      }
        if (notifyObjects3DForEvent(e)) {
            return;
        }

        if (!camera.isOrbiting() &&!camera.isFlying()) {
            if (SwingUtilities.isRightMouseButton(e) || (SwingUtilities.isLeftMouseButton(e) && e.isControlDown())) {
                viewer.setCursor(viewer.onScrGLLlistener.rotateCursor);

                int factor = 1;

                // if (ctrlPressed)
                // factor = 2;

                /**
                 * CAUTION : Below code snippet, is used for avoiding gimball locks. However, a normal behaviour
                 * would be for the user to be able to " go inverted", which is not allowed now. Needs more thought
                 */

                /**  */
                float  pitchAngle = camera.getPitchAngle(),
                       yawAngle   = camera.getYawAngle(), mod;
                double mod1       = factor*2*(float) Math.PI*45/360*(mouseY - oldY)/camera.getViewHeight();

                pitchAngle += mod1;

                double mod2 = factor*2*(float) Math.PI*60/360*(mouseX - oldX)/camera.getViewWidth();

                yawAngle = camera.getYawAngle() + (float) mod2;

                if (Math.abs(pitchAngle) >= (Math.PI / 2 - 0.001)) {
                    pitchAngle = (float) ((pitchAngle > 0)
                                          ? 1
                                          : -1 * (Math.PI / 2 - 0.001));
                }
                
                camera.setOrientation(pitchAngle, yawAngle, 0);
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                Rectangle r = new Rectangle(0,0,camera.getViewWidth(), camera.getViewHeight());
                if (r.contains(e.getPoint())){
                    if (mouseDragPoint==null)
                        mouseDragPoint = viewer.onScrGLLlistener.lastPointIn3DSpace;  
                    if (mouseDragPoint!=null){
                        Vec3d pos = camera.position();
                        // Compute ray in 3D
                        Vec3d rayDirection = new Vec3d();
                        Vec3d up = camera.getUpVector();
                        
                        Mat4d modelView = new Mat4d();
                        Mat4d projection = new Mat4d();
                        
                        camera.getModelViewMatrix(new Vec3d(0, 0, 0), modelView);
                        camera.getProjectionMatrix(projection);
                        screenCoords.set(mouseX,camera.getViewHeight()-mouseY);
                        Vec3d objCoords = ProjectionTransform.screenToSpaceCoordinates(screenCoords, 
                                                          modelView, 
                                                          projection, 
                                                          camera.getViewWidth(), 
                                                          camera.getViewHeight(),
                                                          -0.75);
                        
                        
                        rayDirection = objCoords.minus(camera.position());
                        plane.setNormal(up.toFloat());
                        plane.setPoint(mouseDragPoint.toFloat());
                        IntersectionPoint ipoint = new IntersectionPoint();
                        plane.intersectRay(pos.toFloat(),rayDirection.toFloat(),ipoint);
                        intersectionPoint = ipoint.getIntersectionPoint();
                        
                        // try to cut off reverse ray-plane intersections. If angle between camera
                        // forward vector and the camera-intersection point vector is greater than 90
                        // deg, then, the intersection point is on the reverse side of the ray.
                        Vec3f iVec = intersectionPoint.minus(camera.position().toFloat());
                        iVec.normalize();
                        Vec3f fwdVector = camera.getForwardVector().toFloat();
                        fwdVector.normalize();
                        double angle = Math.acos(fwdVector.dot(iVec));
                        if (angle<Math.PI/2){              
                            intersectionPoint.sub(mouseDragPoint.toFloat());
                            camera.setPosition(new Vec3d(pos.x()-intersectionPoint.x(),
                                                         pos.y()-intersectionPoint.y(),
                                                         pos.z()-intersectionPoint.z()));
                        }
                    }
                }
                
                //camera.move((float) xShift,0.0f);
                viewer.setCursor(viewer.onScrGLLlistener.panCursor);
            }
        }

        if (camera.isFlying()) {
            camera.flightController.handleEvent(e);
        }

        oldX = mouseX;
        oldY = mouseY;
    }

    private void processMouseEnteredEvent(MouseEvent e) {
        if (notifyObjects3DForEvent(e)) {
            return;
        }
    }

    private void processMouseExitedEvent(MouseEvent e) {
        if (notifyObjects3DForEvent(e)) {
            return;
        }
    }

    private void processMouseMoveEvent(MouseEvent e) {
        if ((oldX == e.getX()) && (oldY == e.getY())) {
            return;
        }

        mouseX = e.getX();
        mouseY = e.getY();

        if (notifyObjects3DForEvent(e)) {
            return;
        }

        if (camera.isOrbiting()) {
            return;
        }

        if (camera.isFlying()) {
            camera.flightController.handleEvent(e);
        }

        viewer.setCursor(viewer.onScrGLLlistener.handCursor);
        mouseX = e.getX();
        mouseY = e.getY();
        oldX   = mouseX;
        oldY   = mouseY;
    }

    private void processMousePressedEvent(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        oldX   = mouseX;
        oldY   = mouseY;
        viewer.canvas.repaint();
        if (e.getButton() == MouseEvent.BUTTON2) {
            // Middle button (wheel) click
            if (camera.isFlying()) {
                camera.stopFlying();
            } else {
                camera.startFlying();
            }

            // Below code was (sucessfully) handling orbiting, which won't be used with Cruiser....

            /*
             * if (camera.isLookingAround()){
             *     camera.stopLookingAround();
             *     return;
             * }
             * if (camera.isOrbiting()){
             *     camera.stopOrbiting();
             *     return;
             * }
             * if (ctrlPressed){
             *     if (!camera.isLookingAround()){
             *         camera.startLookingAround();
             *     }
             * } else{
             *     if (!camera.isOrbiting()){
             * /                             if not orbiting already, orbit around!
             *         mouseX = e.getX();
             *         mouseY = e.getY();
             *         if (viewer.onScrGLLlistener.lastMouseZBuffer == 1.0f) return;
             *         float[] objCoords = viewer.onScrGLLlistener.screenToSpaceCoords(viewer.getSize().width/2, viewer.getSize().height/2);
             *         //System.out.println("coords2:"+objCoords[0]+", "+objCoords[1]+", "+objCoords[2]);
             *         camera.startOrbiting(new Vec3d(objCoords[0], objCoords[1], objCoords[2]));
             *     }
             * }
             */
            return;
        }

        if (notifyObjects3DForEvent(e)) {
            return;
        }

        if (camera.isFlying()) {
            camera.flightController.handleEvent(e);
        }

        if (camera.isOrbiting()) {
            return;
        }

        if ((oldX == e.getX()) && (oldY == e.getY())) {
            return;
        }

        viewer.setCursor(viewer.onScrGLLlistener.grabCursor);


//      viewer.renderingWorkerThread.setDirty();
        viewer.canvas.repaint();
    }

    private void processMouseReleasedEvent(MouseEvent e) {
        mouseDragPoint = null;
        if (notifyObjects3DForEvent(e)) {
            return;
        }

        if (camera.isFlying()) {
            camera.flightController.handleEvent(e);
        }

        mouseX = e.getX();
        mouseY = e.getY();
        oldX   = mouseX;
        oldY   = mouseY;

//      viewer.renderingWorkerThread.setDirty();
        viewer.canvas.repaint();
        viewer.setCursor(viewer.onScrGLLlistener.handCursor);

        // viewer.canvas.repaint();
    }

//  private EventTimeStampBind flushEvent(ArrayList eventQueue){
//      EventTimeStampBind bind = null;
//      synchronized (eventQueue){
//          if (!eventQueue.isEmpty()){           
//              bind = (EventTimeStampBind) eventQueue.get(0);
//              eventQueue.clear();
//          }
//      } 
//      return bind;
//  }
}
