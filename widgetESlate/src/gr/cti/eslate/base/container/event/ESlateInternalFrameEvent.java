package gr.cti.eslate.base.container.event;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author George Tsironis
 * @version 1.0
 */

public class ESlateInternalFrameEvent extends java.awt.AWTEvent {

    /**
     * The first number in the range of ids used for window events.
     */
    public static final int INTERNAL_FRAME_FIRST        = 25549;

    /**
     * The last number in the range of ids used for window events.
     */
    public static final int INTERNAL_FRAME_LAST         = 25555;

    /**
     * The window opened event.  This event is delivered only
     * the first time a window is made visible.
     */
    public static final int INTERNAL_FRAME_OPENED	= INTERNAL_FRAME_FIRST;

    /**
     * The "window is closing" event. This event is delivered when
     * the user selects "Quit" from the window's system menu.  If
     * the program does not explicitly hide or destroy the window as
     * while processing this event, the window close operation will be
     * canceled.
     */
    public static final int INTERNAL_FRAME_CLOSING	= 1 + INTERNAL_FRAME_FIRST;

    /**
     * The window closed event. This event is delivered after
     * the window has been closed as the result of a call to hide or
     * destroy.
     */
    public static final int INTERNAL_FRAME_CLOSED	= 2 + INTERNAL_FRAME_FIRST;

    /**
     * The window iconified event. This event indicates that the window
     * was shrunk down to a small icon.
     */
    public static final int INTERNAL_FRAME_ICONIFIED	= 3 + INTERNAL_FRAME_FIRST;

    /**
     * The window restored event type. This event indicates that the
     * window has been restored to its normal size.
     */
    public static final int INTERNAL_FRAME_RESTORED  = 4 + INTERNAL_FRAME_FIRST;

    /**
     * The window activated event type. This event indicates that keystrokes
     * and mouse clicks are directed towards this window.
     */
    public static final int INTERNAL_FRAME_ACTIVATED    = 5 + INTERNAL_FRAME_FIRST;

    /**
     * The window deactivated event type. This event indicates that keystrokes
     * and mouse clicks are no longer directed to the window.
     */
    public static final int INTERNAL_FRAME_DEACTIVATED	= 6 + INTERNAL_FRAME_FIRST;

    /**
     * The window maximized event. This event indicates that the window
     * was maximized to occupy the whole screen.
     */
    public static final int INTERNAL_FRAME_MAXIMIZED  = 7 + INTERNAL_FRAME_FIRST;

    /**
     * Constructs a InternalFrameEvent object.
     * @param source the JInternalFrame object that originated the event
     * @param id     an integer indicating the type of event
     */
    public ESlateInternalFrameEvent(ESlateInternalFrame source, int id) {
        super(source, id);
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
          case INTERNAL_FRAME_OPENED:
              typeStr = "INTERNAL_FRAME_OPENED";
              break;
          case INTERNAL_FRAME_CLOSING:
              typeStr = "INTERNAL_FRAME_CLOSING";
              break;
          case INTERNAL_FRAME_CLOSED:
              typeStr = "INTERNAL_FRAME_CLOSED";
              break;
          case INTERNAL_FRAME_ICONIFIED:
              typeStr = "INTERNAL_FRAME_ICONIFIED";
              break;
          case INTERNAL_FRAME_RESTORED: //DEICONIFIED:
              typeStr = "INTERNAL_FRAME_RESTORED"; //DEICONIFIED";
              break;
          case INTERNAL_FRAME_MAXIMIZED: //DEICONIFIED:
              typeStr = "INTERNAL_FRAME_MAXIMIZED"; //DEICONIFIED";
              break;
          case INTERNAL_FRAME_ACTIVATED:
              typeStr = "INTERNAL_FRAME_ACTIVATED";
              break;
          case INTERNAL_FRAME_DEACTIVATED:
              typeStr = "INTERNAL_FRAME_DEACTIVATED";
              break;
          default:
              typeStr = "unknown type";
        }
        return typeStr;
    }


    /**
     * Returns the originator of the event.
     *
     * @return the JInternalFrame object that originated the event
     * @since 1.3
     */

    public ESlateInternalFrame getInternalFrame () {
      return (source instanceof ESlateInternalFrame)? (ESlateInternalFrame)source : null;
    }
}
