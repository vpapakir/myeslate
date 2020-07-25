package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.event.MwdChangedEvent;

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

class MicroworldCloseAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldCloseAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (container.microworld == null) return;
//System.out.println("Microworld close acttion");
//        container.containerUtils.forceMenuClose(new JMenu[] {microworldMenu});
//                container.containerUtils.forceMenuClose();
        container.closeMicroworld(true);
        container.containerUtils.fireMwdChanged(new MwdChangedEvent(this));
    }

    public boolean isEnabled() {
        return (container.microworld != null);
    }

}
