package gr.cti.eslate.mapViewer;

/**
 * A specialization of MapViewerEvents that
 * @author  Giorgos Vasiliou
 * @version 1.0, 8-Feb-2001
 */

public class MapViewerMouseEvent extends MapViewerEvent {
    public static final int MAP_VIEWER_MOUSE_FIRST=10000;
    public static final int MAP_VIEWER_MOUSE_PRESSED=MAP_VIEWER_MOUSE_FIRST;
    public static final int MAP_VIEWER_MOUSE_RELEASED=MAP_VIEWER_MOUSE_FIRST+2;
    public static final int MAP_VIEWER_MOUSE_CLICKED=MAP_VIEWER_MOUSE_FIRST+3;
    public static final int MAP_VIEWER_MOUSE_DRAGGED=MAP_VIEWER_MOUSE_FIRST+4;
    public static final int MAP_VIEWER_MOUSE_MOVED=MAP_VIEWER_MOUSE_FIRST+5;
    public static final int MAP_VIEWER_MOUSE_ENTERED=MAP_VIEWER_MOUSE_FIRST+6;
    public static final int MAP_VIEWER_MOUSE_EXITED=MAP_VIEWER_MOUSE_FIRST+7;
    public static final int MAP_VIEWER_MOUSE_LAST=MAP_VIEWER_MOUSE_FIRST+7;

    private double x,y;
    private long when;
    private int modifiers, clickCount;
    private boolean popupTrigger;

    /**
     * The constructor. Except for the source and the id of the event, it needs the x and y coordinates
     * of the Map coordinate space where the mouse event took place.
     */
    public MapViewerMouseEvent(Object source, int id, long when, int modifiers, double x, double y, int clickCount, boolean popupTrigger) {
        super(source,id);
        this.when=when;
        this.modifiers=modifiers;
        this.x=x;
        this.y=y;
        this.clickCount=clickCount;
        this.popupTrigger=popupTrigger;
    }
    /**
     * Returns the horizontal x position of the event relative to the
     * Map coordinate space.
     *
     * @return x  an integer indicating horizontal position relative to
     *            the Map coordinate space.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the vertical y position of the event relative to the
     * Map coordinate space.
     *
     * @return y  an integer indicating vertical position relative to
     *            the Map coordinate space.
     */
    public double getY() {
        return y;
    }
    /**
     * Return the number of mouse clicks associated with this event.
     *
     * @return integer value for the number of clicks
     */
    public int getClickCount() {
        return clickCount;
    }
    /**
     * Returns whether or not this mouse event is the popup-menu
     * trigger event for the platform.
     *
     * @return boolean, true if this event is the popup-menu trigger
     *         for this platform
     */
    public boolean isPopupTrigger() {
        return popupTrigger;
    }
    /**
     * Returns the timestamp of when this event occurred.
     */
    public long getWhen() {
        return when;
    }
    /**
     * Returns the modifiers flag for this event.
     */
    public int getModifiers() {
        return modifiers;
    }
    /**
     * Returns a parameter string identifying this event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;
        switch(id) {
          case MAP_VIEWER_MOUSE_PRESSED:
              typeStr = "MAP_VIEWER_MOUSE_PRESSED";
              break;
          case MAP_VIEWER_MOUSE_RELEASED:
              typeStr = "MAP_VIEWER_MOUSE_RELEASED";
              break;
          case MAP_VIEWER_MOUSE_CLICKED:
              typeStr = "MAP_VIEWER_MOUSE_CLICKED";
              break;
          case MAP_VIEWER_MOUSE_ENTERED:
              typeStr = "MAP_VIEWER_MOUSE_ENTERED";
              break;
          case MAP_VIEWER_MOUSE_EXITED:
              typeStr = "MAP_VIEWER_MOUSE_EXITED";
              break;
          case MAP_VIEWER_MOUSE_MOVED:
              typeStr = "MAP_VIEWER_MOUSE_MOVED";
              break;
          case MAP_VIEWER_MOUSE_DRAGGED:
              typeStr = "MAP_VIEWER_MOUSE_DRAGGED";
              break;
          default:
              typeStr = "unknown type";
        }
        return typeStr + ",("+x+","+y+")"+ ",mods="+getModifiers()+
               ",clickCount="+clickCount;
    }
}