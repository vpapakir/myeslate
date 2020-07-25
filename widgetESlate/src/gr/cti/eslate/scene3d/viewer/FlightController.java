
/*
 * Created on 18 Ìבס 2006
 *
 */
package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------

import gr.cti.eslate.math.linalg.*;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

//~--- classes ----------------------------------------------------------------

/**
 * A simple flight controller for a camera
 * @author mantesat
 *
 */
public class FlightController {
    double poslim = Math.PI / 2 - 0.005,
           neglim = -Math.PI / 2 + 0.005;

    /**
     * The Camera using this controller.
     */
    private Camera camera;
    private double posIncrement, yawAngleIncrement, pitchAngleIncrement;
    private double speedMultiplier;

    //~--- constructors -------------------------------------------------------

    public FlightController(Camera camera) {
        this.camera = camera;
        init();
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void handleEvent(InputEvent e) {
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;

            switch(me.getID()){
                case MouseEvent.MOUSE_RELEASED:
                    if (SwingUtilities.isLeftMouseButton(me)){
                        speedMultiplier /= 2d;
                    }
                    arrangeFlightParameters(me);
                    break;
                case MouseEvent.MOUSE_PRESSED:
                    if (SwingUtilities.isLeftMouseButton(me)){
                        speedMultiplier *= 2;
                    }
                    arrangeFlightParameters(me);               
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    
                    arrangeFlightParameters(me);
                    break;
                case MouseEvent.MOUSE_MOVED:
                    arrangeFlightParameters(me);
                    break;
            }
        }

        if (e instanceof MouseWheelEvent) {
            MouseWheelEvent me = (MouseWheelEvent) e;
            if (me.getID()==MouseEvent.MOUSE_WHEEL){
                boolean b= speedMultiplier>0;
                speedMultiplier-=2*me.getWheelRotation();
                if (speedMultiplier>400)
                	speedMultiplier=400;
                else if (speedMultiplier<-400)
                	speedMultiplier=-400;
                if (Math.abs(speedMultiplier)<1){
                    if (b) {
                        speedMultiplier = -1;
                    } else {
                        speedMultiplier = 1;
                    }
                }
            }
        }
    }

    /**
     * Method description
     *
     */
    public void init() {
        speedMultiplier   = 6;
        posIncrement      = 1;
        yawAngleIncrement = pitchAngleIncrement = 0f;
    }
    
    private void arrangeFlightParameters(MouseEvent e){
        int centerX = camera.getViewWidth()/ 2;
        int centerY = camera.getViewHeight() / 2;

        float turnRatio = Math.max(camera.getViewWidth(), camera.getViewHeight())*90.0f;
        this.yawAngleIncrement = (centerX - e.getX()) / turnRatio;
        System.out.println("yawAngleIncr: "+this.yawAngleIncrement);
        this.pitchAngleIncrement = (centerY - e.getY()) / turnRatio;        
        //System.out.println("arranging parameters!: "+yawAngleIncrement+", "+pitchAngleIncrement);

    }
    
    public void calculateNextStep(float stepSpeedMultiplier){
        double angleMultiplier;
        if (speedMultiplier>0)
        	angleMultiplier=1+(speedMultiplier-1)/30;
        else
        	angleMultiplier=-(1+(-speedMultiplier-1)/30);
        float pitchAngle =camera.getPitchAngle();
        Vec3d cameraPosition = camera.position();
        if (pitchAngle+pitchAngleIncrement*angleMultiplier<poslim && pitchAngle+pitchAngleIncrement*angleMultiplier> neglim)
            pitchAngle+=pitchAngleIncrement*angleMultiplier;
  
        Mat4d modelViewMatrix = new Mat4d();
        Mat4d projectionMatrix = new Mat4d();
        camera.getModelViewMatrix(new Vec3d(0,0,0),modelViewMatrix);
        camera.getProjectionMatrix(projectionMatrix);
//        double heightMultiplier = 1-Math.exp(-cameraPosition.y()/2e3);
        double heightMultiplier = Math.abs(1.4*cameraPosition.y()/1E3);
        double increment = posIncrement*speedMultiplier*heightMultiplier*stepSpeedMultiplier;
//        System.out.println("Height: "+cameraPosition.y()+", multiplier: "+heightMultiplier+"increment: "+increment);
        /** Z must be a value between 0 and 1, and it seems to must be negative, 
        for the point on the ray to be inside the frustum (but why?)**/
      
        Vec3d ray = ProjectionTransform.screenToSpaceCoordinates(new Vec2i(camera.getViewWidth()/2, camera.getViewHeight()/2),
                                                   modelViewMatrix,
                                                   projectionMatrix,
                                                   new Dimension(camera.getViewWidth(), camera.getViewHeight()),
                                                   -0.75); //0.75 is a "good" z value, can be different
        
        double distance = MathUtils.distance(cameraPosition,ray);
        double ratio = increment/distance;
        Vec3d coords = cameraPosition.addScaled(ratio,ray.minus(cameraPosition));
        
        double yawAngle = camera.getYawAngle()+yawAngleIncrement*angleMultiplier;
        camera.setupCamera(coords,new Vec3d(pitchAngle, 
                                            yawAngle, 
                                            camera.getRollAngle()));

    }

}
