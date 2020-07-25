package gr.cti.eslate.base.container;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class ComponentCutAction extends AbstractAction {
    ESlateContainer container = null;

    public ComponentCutAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(AbstractAction.ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        container.cutComponent(container.getActiveComponent());
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.mwdComponents.activeComponent != null && container.microworld.componentRemovalAllowed);
    }

}
