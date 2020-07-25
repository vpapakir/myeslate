package gr.cti.eslate.scene3d.viewer.actions;


import gr.cti.eslate.scene3d.viewer.Viewer3D;

import java.util.*;


/** Provides a concrete framework dealing with sequenced actions.
 Acts as a first-in-first-out queue. The currently active action is
 at the front of the queue. When that action transitions to the
 inactive state it is removed from the queue. */

public class ActionQueue {
    Viewer3D viewer;

    public ActionQueue(Viewer3D viewer){
        super();

        this.viewer = viewer;
    }

    private LinkedList/*<Action>*/ actions = new LinkedList();

    /** Adds an action to the end of the list. */
    public void add(Action action) {
        if (action instanceof CameraAction)
            actions.clear();
        actions.add(action);
        
        viewer.wallClock.start();

    }

    /** Indicates whether this ActionQueue is empty or not. */
    public boolean empty() {
        return (actions.size() == 0);
    }

    public void clear(){
        actions.clear();
    }

    /** Updates the currently-active action if any. Takes wall-clock
     time in seconds. */
    public void update(float time) {
        float nextStartTime = 0;
        boolean useNextStartTime = false;
        boolean done = (actions.size() == 0);

        do {
            done = true;
            Action head = (Action) actions.getFirst();
            //System.out.println("ACTION : "+head+", isACTIVE : "+head.isActive());
            if (head.state()==Action.STOPPED){
                useNextStartTime = true;
                actions.removeFirst();
                done = (actions.size() == 0);
                continue;
            }
            if (head.isActive()) {
                head.update(time);
            } else {
                if (useNextStartTime) {
                    head.start(nextStartTime);
                }else {
                    head.start(time);
                }
                head.update(time);
            }
            if (!head.isActive()) {
                nextStartTime = head.stopTime();

                useNextStartTime = true;
                actions.removeFirst();
                done = (actions.size() == 0);
            }
        }while (!done);
    }
}
