package gr.cti.eslate.agent;

import java.util.EventListener;

public interface PathListener extends EventListener {
    /**
     * Invoked when a segment property has been changed.
     */
    public abstract void segmentPropertiesChanged(PathEvent e);
}
