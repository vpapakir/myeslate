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

class MicroworldSaveAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldSaveAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
//        container.containerUtils.forceMenuClose(new JMenu[] {microworldMenu});
//                container.containerUtils.forceMenuClose();
        if (container.microworld == null || !container.microworld.isMwdStorageAllowed()) return;
        container.saveMicroworld(true);
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.microworld.isMwdStorageAllowed());
    }

}
