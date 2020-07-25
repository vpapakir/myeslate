package gr.cti.eslate.scene3d.viewer.actions;

import gr.cti.eslate.scene3d.viewer.Camera;
import gr.cti.eslate.math.linalg.*;
import gr.cti.eslate.math.linalg.filter.*;

/**
 * Created by IntelliJ IDEA.
 * User: mantesat
 * Date: 11 …бн 2005
 * Time: 9:39:01 рм
 * To change this template use Options | File Templates.
 */
public class LeapAction extends DurationAction{

    private Camera camera;
    private int    type;
    private AlphaFilter filter;
    private SphericalCoordInterpolator interp;
    private Vec3fInterpolator      linXZPosInterp;
    private Vec3fInterpolator      linHeight1Interp, linHeight2Interp;
    private Vec3fInterpolator      linLookAtInterp;
    
    private Vec3fInterpolator      yawAngleInterp;

// this is an interpolator camera action used to interpolate through positions to achieve
// moving effect

    /** Type is either SPHERICAL or LINEAR */
    public LeapAction (int type,  AlphaFilter filter,
                                    Camera camera,
                                    Vec3f pos1, Vec3f lookAt1,
                                    Vec3f pos2, Vec3f lookAt2,
                                    float duration) {

        super(duration);
        //System.out.println("Duration for this action will be : "+duration);
        this.camera = camera;
        this.type = type;
        this.filter = filter;
        

        if (pos1 != null && pos2 !=null){
            linXZPosInterp = new Vec3fInterpolator();
            linXZPosInterp.setup(pos1, pos2);

            linHeight1Interp =  new Vec3fInterpolator();
            linHeight2Interp =  new Vec3fInterpolator();
            double dist = MathUtils.distance(new Vec3f(pos1.x(),0, pos1.z()),new Vec3f(pos2.x(),0, pos2.z()));
            // apply golden cut (or section) rule for height (as Dimitris proposed)
            double height = dist*1.618034+Math.max(pos1.y(), pos2.y());

            linHeight1Interp.setup(new Vec3f(0,pos1.y(),0), new Vec3f(0,(float) height,0));
            linHeight2Interp.setup(new Vec3f(0,(float) height,0),new Vec3f(0,pos2.y(),0));
        }
        if (lookAt1 != null && lookAt2 !=null){
            linLookAtInterp = new Vec3fInterpolator();
            linLookAtInterp.setup(lookAt1, lookAt2);
        }
        
        double startYawAngle = camera.getYawAngle();
        
        double tan_endYawAngle = (pos2.x()-lookAt2.x())/(pos2.z()-lookAt2.z());
        double endYawAngle = Math.atan(tan_endYawAngle);
        //System.out.println("StartYawAngle, EndYawAngle: "+startYawAngle+", "+endYawAngle);
        yawAngleInterp = new Vec3fInterpolator();
        yawAngleInterp.setup(new Vec3f((float) startYawAngle,0,0), new Vec3f((float) endYawAngle,0,0));
    }

    public void start(float time){
        camera.isMoving = true;
        //System.out.println("Starting at time : "+time);
        super.start(time);
    }

    public void stop(float time){
        camera.isMoving = false;
        super.stop(time);
        //System.out.println("HERE3");
        camera.update();
    }

    public void update(float wallClockTime,
        float elapsedTime,
        float alpha) {
        float beta = filter.filter(alpha);
        //System.out.println("WallClockTime : "+wallClockTime+" alpha : "+alpha);
        yawAngleInterp.interpolate(alpha);
        float a1,a2;

        if (linHeight1Interp != null && alpha<0.5){
            a1=filter.filter(alpha*2);
            linHeight1Interp.interpolate(a1);
        }
        if (linHeight2Interp != null && alpha>0.5){
            a2 = filter.filter(2*(alpha-0.5f));
            linHeight2Interp.interpolate(a2);
        }
        if (linXZPosInterp != null){
            linXZPosInterp.interpolate(beta);
            Vec3d posxz = linXZPosInterp.value().toDouble();
            double height = alpha<0.5?linHeight1Interp.value().y():linHeight2Interp.value().y();

            camera.setPosition(new Vec3d(posxz.x(),height,posxz.z()));
        }
        //println("YawAngle interp: "+yawAngleInterp.value().x());
        camera.setOrientation(camera.getPitchAngle(),yawAngleInterp.value().x(),camera.getRollAngle());

        if (linLookAtInterp != null){
            linLookAtInterp.interpolate(beta);
            camera.lookAt(linLookAtInterp.value().toDouble(), Vec3d.Y_AXIS);
        }

    }
}

