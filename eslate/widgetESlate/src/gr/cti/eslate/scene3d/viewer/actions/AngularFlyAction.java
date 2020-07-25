package gr.cti.eslate.scene3d.viewer.actions;

import gr.cti.eslate.math.linalg.*;
import gr.cti.eslate.math.linalg.filter.*;
import gr.cti.eslate.scene3d.viewer.Camera;


/**
 * Created by A.Mantes at 25 Απρ 2004, 6:47:59 μμ
 */
public class AngularFlyAction extends DurationAction{

 public static final int SPHERICAL = 0;
    public static final int LINEAR = 1;

    private Camera camera;
    private int    type;
    private AlphaFilter filter;
    private SphericalCoordInterpolator interp;
    private Vec3fInterpolator      linPosInterp;
    private Vec3fInterpolator      linLookAtInterp;

// this is an interpolator camera action used to interpolate through positions to achieve
// moving effect

    /** Type is either SPHERICAL or LINEAR */
    public AngularFlyAction (int type,  AlphaFilter filter,
                                    Camera camera,
                                    Vec3f pos1, Vec3f orientation1,
                                    Vec3f pos2, Vec3f orientation2,
                                    float duration) {

        super(duration);
        //System.out.println("Duration for this action will be : "+duration);
        this.camera = camera;
        this.type = type;
        this.filter = filter;

        float tmpAngle;
        //pitch data


        float pitchAngle1 = (float) MathUtils.dec2rad(orientation1.x()%360);
        float pitchAngle2 = (float) MathUtils.dec2rad(orientation2.x()%360);
        double pitchDiff = MathUtils.angleDiff(pitchAngle1,pitchAngle2);
        if (pitchAngle1>pitchAngle2)
            pitchAngle2+=pitchDiff;
        else
            pitchAngle2-=pitchDiff;

        float yawAngle1 = (float) MathUtils.dec2rad(orientation1.y()%360);
        float yawAngle2 = (float) MathUtils.dec2rad(orientation2.y()%360);
        double yawDiff = MathUtils.angleDiff(yawAngle1,yawAngle2);
        if (yawAngle1>yawAngle2)
            yawAngle2+=yawDiff;
        else
            yawAngle2-=yawDiff;

        float rollAngle1 = (float) MathUtils.dec2rad(orientation1.z()%360);
        float rollAngle2 = (float) MathUtils.dec2rad(orientation2.z()%360);
        double rollDiff = MathUtils.angleDiff(rollAngle1,rollAngle2);
        if (rollAngle1>rollAngle2)
            rollAngle2+=rollDiff;
        else
            rollAngle2-=rollDiff;

        if (pos1 != null && pos2 !=null){
            linPosInterp = new Vec3fInterpolator();
            linPosInterp.setup(pos1, pos2);
        }
        if (orientation1 != null && orientation2 !=null){
            linLookAtInterp = new Vec3fInterpolator();
            linLookAtInterp.setup(orientation1, orientation2);
        }
    }

    public void start(float time){
        camera.isMoving = true;
        //System.out.println("Starting at time : "+time);
        super.start(time);
    }

    public void stop(float time){
        camera.isMoving = false;
        super.stop(time);
    }

    public void update(float wallClockTime,
        float elapsedTime,
        float alpha) {
        alpha = filter.filter(alpha);

        if (linPosInterp != null){
            linPosInterp.interpolate(alpha);
            camera.setPosition(linPosInterp.value().toDouble());
        }
        if (linLookAtInterp != null){
            linLookAtInterp.interpolate(alpha);
            camera.setOrientation(linLookAtInterp.value().x(), linLookAtInterp.value().y(), linLookAtInterp.value().z());
        }

    }
}
