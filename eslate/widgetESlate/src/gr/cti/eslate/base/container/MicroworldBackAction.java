package gr.cti.eslate.base.container;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class MicroworldBackAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldBackAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
    }

    public void actionPerformed(ActionEvent e) {
//        container.containerUtils.forceMenuClose(new JMenu[] {microworldMenu, microworldNavigationMenu});
//                container.containerUtils.forceMenuClose();
        if (container.mwdHistory.canGoBack((container.microworld != null)))
            container.back();
    }

    public boolean isEnabled() {
        return (container.mwdHistory.canGoBack((container.microworld != null)));
    }

}
