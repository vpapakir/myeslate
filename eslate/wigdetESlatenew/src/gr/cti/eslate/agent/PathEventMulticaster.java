package gr.cti.eslate.agent;

import java.awt.AWTEventMulticaster;
import java.util.EventListener;

public class PathEventMulticaster extends AWTEventMulticaster implements PathListener {

    protected PathEventMulticaster(EventListener a, EventListener b) {
        super(a, b);
    }

    public static PathListener add(PathListener a, PathListener b) {
        return (PathListener) addInternal(a, b);
    }

    public static PathListener remove(PathListener l,PathListener oldl) {
        return (PathListener) removeInternal(l,oldl);
    }
    /**
     * Invoked when a segment property has been changed.
     */
    public void segmentPropertiesChanged(PathEvent e) {
        if (a != null) ((PathListener) a).segmentPropertiesChanged(e);
        if (b != null) ((PathListener) b).segmentPropertiesChanged(e);
    }

    protected static EventListener addInternal(EventListener a,EventListener b) {
        if (a == null) return b;
        if (b == null) return a;
        return new PathEventMulticaster(a, b);
    }

    protected EventListener remove(EventListener oldl) {
        if (oldl == a) return b;
        if (oldl == b) return a;
        EventListener a2 = removeInternal(a, oldl);
        EventListener b2 = removeInternal(b, oldl);
        if (a2 == a && b2 == b) return this;
        return addInternal(a2, b2);
    }
}
