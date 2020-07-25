package gr.cti.eslate.scene3d.viewer.actions;


/** An action that runs for a specified duration specified in
 seconds. */

public abstract class DurationAction extends BaseAction {
    private float duration;

    /** Duration is initialized to 0.0 seconds */
    public DurationAction() {}

    /** Duration is initialized to specified duration in seconds */
    public DurationAction(float secs) {
        duration = secs;
    }

    /** Returns the duration of this action */
    public float duration() {
        return duration;
    }

    /** Sets duration, specified in seconds. */
    public void setDuration(float secs) {
        duration = secs;
    }

    /** Clients should not need to override this. */
    public void update(float time) {
        float diff = time - startTime();

        if (diff >= duration) {
            float extra = diff - duration;
            float timeStopped = time - extra;

            update(timeStopped, duration, 1.0f);
            stop(timeStopped);
        } else {
            update(time, diff, diff / duration);
        }
    }

    /** Override this to receive notification of all update events.
     Arguments are the absolute wall-clock time in seconds, the time
     since the startTime, and the "alpha" value of this action, where
     0.0 corresponds to the start of the action and 1.0 the end. */
    public abstract void update(float wallClockTime,
        float elapsedTime,
        float alpha);
}
