package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;

import javax.swing.Icon;

/**
 * Icon shared object.
 *
 * @author      Nicolas Drossos
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class IconSO extends SharedObject {
    private Icon icon = null;

    public IconSO(gr.cti.eslate.base.ESlateHandle app) {
        super(app);
    }

    @SuppressWarnings(value={"deprecation"})
    public void setIconSO(Icon icon) {
        if (areDifferent(this.icon, icon)) {
            this.icon=icon;
            // Create an event
            SharedObjectEvent soe =
              new SharedObjectEvent(getHandle().getComponent(), this);
            fireSharedObjectChanged(soe);       // Notify the listeners
        }
    }

    public Icon getIconSO() {
        return icon;
    }

}
