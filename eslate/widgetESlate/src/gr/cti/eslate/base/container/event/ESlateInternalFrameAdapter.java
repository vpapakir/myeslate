package gr.cti.eslate.base.container.event;


/**
 * The listener interface for receiving internal frame events.
 * This class is functionally equivalent to the WindowListener class
 * in the AWT.
 * <p>
 */
public class ESlateInternalFrameAdapter implements ESlateInternalFrameListener {
    /**
     * Invoked when a internal frame has been opened.
     */
    public void internalFrameOpened(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    public void internalFrameClosing(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame has been closed.
     */
    public void internalFrameClosed(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is iconified.
     */
    public void internalFrameIconified(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is restored.
     */
    public void internalFrameRestored(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is maximized.
     */
    public void internalFrameMaximized(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is activated.
     */
    public void internalFrameActivated(ESlateInternalFrameEvent e) {}

    /**
     * Invoked when an internal frame is de-activated.
     */
    public void internalFrameDeactivated(ESlateInternalFrameEvent e) {}
}