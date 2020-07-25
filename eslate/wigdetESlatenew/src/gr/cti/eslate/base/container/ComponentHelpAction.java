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

class ComponentHelpAction extends AbstractAction {
    ESlateContainer container = null;

    public ComponentHelpAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
    }

    public void actionPerformed(ActionEvent e) {
        container.showActiveComponentHelp();
    }

}
