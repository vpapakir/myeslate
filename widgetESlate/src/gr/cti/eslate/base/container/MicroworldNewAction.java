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

class MicroworldNewAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldNewAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
//                container.containerUtils.forceMenuClose();
//        container.containerUtils.forceMenuClose(new JMenu[] {microworldMenu});
        container.createNewMicroworld();
        if (container.microworld != null)
            container.setContainerTitle(container.microworld.eslateMwd.getName());
    }
}
