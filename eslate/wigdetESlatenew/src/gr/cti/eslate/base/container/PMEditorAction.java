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

public class PMEditorAction extends AbstractAction {
    ESlateComposer composer = null;

    public PMEditorAction(ESlateComposer composer, String title) {
        super(title);
        this.composer = composer;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M,
                                        java.awt.Event.ALT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        composer.showPerformanceManager();
    }

    public boolean isEnabled() {
        return PerformanceManager.getPerformanceManager().isEnabled();
    }

}
