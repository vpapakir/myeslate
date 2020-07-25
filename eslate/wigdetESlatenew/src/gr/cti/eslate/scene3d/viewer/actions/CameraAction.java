package gr.cti.eslate.scene3d.viewer.actions;

import gr.cti.eslate.scene3d.viewer.Camera;
import gr.cti.eslate.math.linalg.*;
import gr.cti.eslate.math.linalg.filter.*;

public class CameraAction extends DurationAction {
    public static final int SPHERICAL = 0;
    public static final int LINEAR = 1;

    private Camera camera;
    private int    type;
    private AlphaFilter filter;
    private SphericalCoordInterpolator interp;
    private Vec3fInterpolator      linPosInterp;
    private Vec3fInterpolator      linLookAtInterp;
    
    private long timeStamp;

// this is an interpolator camera action used to interpolate through positions to achieve
// moving effect

    /** Type is either SPHERICAL or LINEAR */
    public CameraAction (int type,  AlphaFilter filter,
                                    Camera camera,
                                    Vec3f pos1, Vec3f lookAt1,
                                    Vec3f pos2, Vec3f lookAt2,
                                    float duration) {

        super(duration);        
        this.camera = camera;
        this.type = type;
        this.filter = filter;
        if (type == SPHERICAL) {
            interp = new SphericalCoordInterpolator();
            interp.setup(pos1, lookAt1, pos2, lookAt2);
        } else {
            if (pos1 != null && pos2 !=null){
                linPosInterp = new Vec3fInterpolator();
                linPosInterp.setup(pos1, pos2);
            }
            if (lookAt1 != null && lookAt2 !=null){
                linLookAtInterp = new Vec3fInterpolator();
                linLookAtInterp.setup(lookAt1, lookAt2);
            }
        }
    }

    public void start(float time){
        camera.isMoving = true;
        timeStamp = System.nanoTime();
        //System.out.println("*****************************START ACTION*******************************");
        super.start(time);
    }

    public void stop(float time){
        //System.out.println("HERE1");
        camera.isMoving = false;
        super.stop(time);
        camera.update();
        //System.out.println("*****************************STOP ACTION*******************************");
        //long millis = (System.nanoTime()-timeStamp)/1000000;
        //System.out.println("Time: "+millis);
        //System.out.println("Renderings allowed: "+millis/30);
    }

    public void update(float wallClockTime,
        float elapsedTime,
        float alpha) {
        alpha = filter.filter(alpha);
        if (type == SPHERICAL) {
            interp.interpolate(alpha);
            camera.setPosition(interp.position().toDouble());
            camera.lookAt(interp.lookAt().toDouble(), Vec3d.Y_AXIS);
        } else {
            if (linPosInterp != null){
                linPosInterp.interpolate(alpha);
                Vec3d pos = linPosInterp.value().toDouble();
                camera.setPosition(linPosInterp.value().toDouble());

            }
            if (linLookAtInterp != null){
                linLookAtInterp.interpolate(alpha);
                camera.lookAt(linLookAtInterp.value().toDouble(), Vec3d.Y_AXIS);
            }
        }
    }
}
