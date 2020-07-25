package gr.cti.eslate.scene3d.viewer.actions;

import java.awt.Dimension;

import gr.cti.eslate.scene3d.viewer.Camera;
import gr.cti.eslate.scene3d.viewer.FlightController;
import gr.cti.eslate.math.linalg.*;
import gr.cti.eslate.math.linalg.filter.*;

/**
 * Created by A.Mantes at 27 ײוג 2004, 1:40:27 לל
 */
public class FlyAction extends BaseAction{

    private Camera camera;
    private int    type;
    Vec3d cameraPosition;
    double poslim = Math.PI/2-0.005, neglim = -Math.PI/2+0.005;
    private double speedMultiplier;
    private FlightController flightController;

// this is an interpolator camera action used to interpolate through positions to achieve
// moving effect

    public FlyAction (Camera camera) {
        this.camera = camera;
        flightController = camera.flightController;
    }

    public void start(float time){
        super.start(time);
    }

    public void stop(float time){
        state = STOPPED;
        camera.update();
        //System.out.println("HERE2");
    }

    public void update(float time) {
        flightController.calculateNextStep(1);
    }
}
