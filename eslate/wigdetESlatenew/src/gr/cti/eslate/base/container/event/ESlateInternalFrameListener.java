package gr.cti.eslate.base.container.event;

import java.util.EventListener;

/**
 * The listener interface for receiving internal frame events.
 * This class is functionally equivalent to the WindowListener class
 * in the AWT.
 * <p>
 */
public interface ESlateInternalFrameListener extends EventListener {
    /**
     * Invoked when a internal frame has been opened.
     */
    public void internalFrameOpened(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    public void internalFrameClosing(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame has been closed.
     */
    public void internalFrameClosed(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame is iconified.
     */
    public void internalFrameIconified(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame is restored. This happens either
     * when the frame was iconified and becomes visible(but not maximized),
     * or when the frame was maximized and it gets restored.
     */
    public void internalFrameRestored(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame is maximized.
     */
    public void internalFrameMaximized(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame is activated.
     */
    public void internalFrameActivated(ESlateInternalFrameEvent e);

    /**
     * Invoked when an internal frame is de-activated.
     */
    public void internalFrameDeactivated(ESlateInternalFrameEvent e);
}