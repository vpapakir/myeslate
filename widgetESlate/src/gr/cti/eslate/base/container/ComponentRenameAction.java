package gr.cti.eslate.base.container;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class ComponentRenameAction extends AbstractAction {
    ESlateContainer container = null;

    public ComponentRenameAction(ESlateContainer container, String title) {
          super(title);
          this.container = container;
    }

    public void actionPerformed(ActionEvent e) {
        container.renameActiveComponent();
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.mwdComponents.activeComponent != null && container.microworld.componentNameChangeAllowed);
    }
}
