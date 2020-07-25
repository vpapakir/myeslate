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

public class ShowComponentBarAction extends AbstractAction {
    ESlateContainer container = null;

    public ShowComponentBarAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
                                        java.awt.Event.ALT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
    	((ESlateComposer) container).toggleComponentPanel();
    }

    public boolean isEnabled() {
        return (container.currentView != null && container.currentView.plugConnectionChangeAllowed);
    }
}
