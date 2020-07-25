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

public class OpenConsolesAction extends AbstractAction {
    ESlateContainer container = null;

    public OpenConsolesAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J,
                                        java.awt.Event.ALT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (!isEnabled()) return;

        if (container.consoles == null) return;
        container.consoles.showConsole(container);
        container.consoles.bringToFront();
    }

    public boolean isEnabled() {
        if (container.javaConsoleEnabled) {
            if (container.microworld != null)
                return container.microworld.consolesAllowed;
            else
                return true;
        }else
            return false;
    }
}
