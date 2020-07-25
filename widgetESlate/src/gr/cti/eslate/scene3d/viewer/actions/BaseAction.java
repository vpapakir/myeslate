package gr.cti.eslate.scene3d.viewer.actions;


/** A simple implementation of the Action interface. */

public abstract class BaseAction implements Action {
    protected int   state = INACTIVE;
    private float startTime;
    private float stopTime;

    public int state() {
        return state;
    }

    public boolean isActive() {
        return (state == ACTIVE);
    }

    /** Transitions the action to the ACTIVE state and records the
     starting time of the action. */
    public void start(float time) {
        state = ACTIVE;
        startTime = time;
    }

    /** Clients override this. When the action is complete the client
     should call stop(), passing in the absolute time at which the
     action shut down. (Note that that time may be less than the time
     passed in to the call to update(), if the action shut down
     sometime between the last update and this one.) */
    public abstract void update(float time);

    /** Should be called by clients when the action is to be shut off.
     Transitions the state to INACTIVE and records the time as the
     stop time. */
    protected void stop(float time) {
        state = INACTIVE;
        stopTime = time;
    }

    public float startTime() {
        return startTime;
    }

    public float stopTime() {
        return stopTime;
    }
}
