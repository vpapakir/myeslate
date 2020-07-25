package gr.cti.eslate.agent;

import java.awt.AWTEvent;

/**
 * The event that informs for path changes.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 1-Jun-2000
 */
public class PathEvent extends AWTEvent {
    public static final int PATH_FIRST=AWTEvent.RESERVED_ID_MAX+1;
    public static final int PATH_PROPERTIES_CHANGED=PATH_FIRST;
    public static final int PATH_LAST=PATH_FIRST;

    private int segNo;

    public PathEvent(Object source,int id,int segmentNo) {
        super(source,id);
        segNo=segmentNo;
    }

    public int getSegmentNumber() {
        return segNo;
    }
}
