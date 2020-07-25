package gr.cti.eslate.scene3d.viewer.actions;


public interface Action {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;
    public static final int STOPPED = 3;

    /** Retrieves the state of this action; either ACTIVE or
     INACTIVE. */
    public int state();

    /** Indicates whether state is ACTIVE. */
    public boolean isActive();

    /** This is called by the ActionList before the first call to
     update() when this action is updated for the first time. The
     time argument is in seconds since the start of the system. This
     routine should change the state of the action to ACTIVE. */
    public void start(float time);

    /** This is called each tick the action declares itself to be
     active. The time argument is in seconds since the start of the
     system. */
    public void update(float time);

    /** This is called by the ActionList when an action transitions from
     the ACTIVE to INACTIVE state to discover at what absolute time
     the Action was shut off. This allows the ActionList to take any
     remaining "quantum" and give it to the subsequent action. */
    public float stopTime();
}
