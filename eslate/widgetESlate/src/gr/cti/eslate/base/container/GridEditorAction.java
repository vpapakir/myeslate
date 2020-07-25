package gr.cti.eslate.base.container;

import java.awt.Frame;
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

public class GridEditorAction extends AbstractAction {
    ESlateComposer composer = null;

    public GridEditorAction(ESlateComposer composer, String title) {
        super(title);
        this.composer = composer;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G,
                                        java.awt.Event.ALT_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (composer.microworld == null || !composer.microworld.gridMgmtAllowed)
            return;
        DesktopPane dp = (DesktopPane) composer.lc;
        Frame topLevelFrame = null;
        if (composer.parentComponent != null && Frame.class.isAssignableFrom(composer.parentComponent.getClass()))
            topLevelFrame = (Frame) composer.parentComponent;
        new GridDialog(topLevelFrame, dp.gridVisible, dp.gridStep, dp.gridColor, dp.snapToGrid, composer, composer);
    }

    public boolean isEnabled() {
        return (composer.microworld != null && composer.microworld.gridMgmtAllowed);
    }

}
