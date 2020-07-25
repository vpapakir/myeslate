package gr.cti.eslate.base.container.event;

import java.util.EventListener;


/**
 * The listener interface for receiving events about the open ESlate microworld. The
 * events in the listener interface which have to do with components are not delivered
 * while a microworld is being loaded or closed.
 * <p>
 */
public interface MicroworldListener extends EventListener {
    /**
     * Invoked every time a new empty microworld is created. This event precedes the
     * microworldLoaded() event.
     */
    public void microworldCreated(MicroworldEvent e);

    /**
     * Invoked every time a microworld is loaded indise an ESlateContainer.
     */
    public void microworldLoaded(MicroworldEvent e);

    /**
     * Invoked when a microworld is closed.
     */
    public void microworldClosed(MicroworldEvent e);

    /**
     * Invoked just before the microworld starts closing.
     */
    public void microworldClosing(MicroworldEvent e);

    /**
     * Invoked when a component of the microworld becomes active. Only one active
     * component is permitted inside a microworld.
     */
    public void componentActivated(MicroworldComponentEvent e);

    /**
     * Invoked when a component of the microworld becomes inactive.
     */
    public void componentDeactivated(MicroworldComponentEvent e);

    /**
     * Invoked when a component of the microworld becomes iconified, i.e. invisible.
     */
    public void componentIconified(MicroworldComponentEvent e);

    /**
     * Invoked when a component of the microworld is restored. This means that
     * either the component was iconified and it was made visible(but not maximized),
     * or the component was maximized and then it was restored. The restoration cause
     * is conveyed by a flag on the <i>MicroworldComponentEvent</i>.
     */
    public void componentRestored(MicroworldComponentEvent e);

    /**
     * Invoked when a component of the microworld becomes maximized, i.e. occupies the
     * whole screen of the microworld. This can only happen to components which have
     * user interface.
     */
    public void componentMaximized(MicroworldComponentEvent e);

    /**
     * Invoked when a component of the microworld has been closed. The component
     * does not exist when this event is fired.
     */
    public void componentClosed(MicroworldComponentEvent e);

    /**
     * Invoked when a component of the microworld is about to be destroyed. The component
     * does exist when this event is fired.
     */
    public void componentClosing(MicroworldComponentEvent e);

    /**
     * Invoked when a registered view is applied to the microworld.
     */
    public void activeViewChanged(MicroworldViewEvent e);
}
