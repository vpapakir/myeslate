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

class ComponentEditorAction extends AbstractAction {
    ESlateComposer container = null;

    public ComponentEditorAction(ESlateComposer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                                        java.awt.Event.ALT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        container.displayComponentEditor();
    }

    public boolean isEnabled() {
        if (container.microworld == null)
            return false;
        return (container.microworld.componentPropertyMgmtAllowed ||
                container.microworld.componentEventMgmtAllowed ||
                container.microworld.componentSoundMgmtAllowed);
    }
}
