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

class MicroworldSaveAsAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldSaveAsAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                                        java.awt.Event.CTRL_MASK | java.awt.Event.SHIFT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
System.out.println("ACTIONSAVE");
//        container.containerUtils.forceMenuClose(new JMenu[] {microworldMenu, microworldSaveAs});
//                container.containerUtils.forceMenuClose();
        if (container.microworld == null || !container.microworld.isMwdStorageAllowed()) return;
        container.saveAsLocalMicroworld(true);
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.microworld.isMwdStorageAllowed());
    }

}
