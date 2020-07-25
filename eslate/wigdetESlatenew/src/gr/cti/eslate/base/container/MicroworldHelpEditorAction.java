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

class MicroworldHelpEditorAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldHelpEditorAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H,
                                        java.awt.Event.ALT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (container.microworld == null) return;
        container.createMicroworldHelp();
    }

}
