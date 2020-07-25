package gr.cti.eslate.base.container;

/**
 * User: Yiorgos Tsironis
 */
/* MicroworldCurrentView is a subclass of MicroworldView. The main reason for differentiating
 * the microworld's currentView from the rest of the views of the microworld, is the fact
 * that while the views normally are just recorded and do never change, the 'currentView'
 * changes, whenever a view is activated. Actually view activation means that the
 * properties of the activated view are assigned to the properties of the 'currentView'.
 * So there exists a mutability difference, which is expected to cause more coding
 * differences in the future.
 */
public final class MicroworldCurrentView extends MicroworldView {
    static final long serialVersionUID = 12;
    ESlateContainer container = null;

    public MicroworldCurrentView() {
    }

    public MicroworldCurrentView(ESlateContainer container, MicroworldView view) {
        super(container, view);
        this.container = container;
    }

    // The caching mechanism does not apply to the currentView of the microworld
    public void setIconsCached(boolean cached) {
        return;
    }

    public boolean isIconsCached() {
        return true;
    }
}
