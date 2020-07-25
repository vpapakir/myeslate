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

public class ExitFullScreenModeAction extends AbstractAction {
    ESlateContainer container = null;

    public ExitFullScreenModeAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(AbstractAction.ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5,
                                        0,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (!isEnabled()) return;
//        container.setFullScreen(container.parentFrame, false);
    }

    public boolean isEnabled() {
        return container.isFullScreen();
    }
}
