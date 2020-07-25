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

class ComponentCopyAction extends AbstractAction {
    ESlateContainer container = null;

    public ComponentCopyAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(AbstractAction.ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        container.copyComponent(container.getActiveComponent());
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.mwdComponents.activeComponent != null);
    }

}
