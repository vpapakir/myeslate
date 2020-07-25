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

class ComponentPasteAction extends AbstractAction {
    ESlateContainer container = null;

    public ComponentPasteAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(AbstractAction.ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        container.pasteComponent(true, null);
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.microworld.componentInstantiationAllowed && !container.lc.isModalFrameVisible());
    }

}
