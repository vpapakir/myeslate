package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class ComponentActivateOnClickAction extends AbstractAction {
    ESlateContainer container = null;

    public ComponentActivateOnClickAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
    }

    public void actionPerformed(ActionEvent e) {
        if (container.microworld == null || container.mwdComponents.activeComponent == null)
            return;
        ESlateInternalFrame fr = container.mwdComponents.activeComponent.frame;
        if (fr == null) return;
        fr.setComponentActivatedOnMouseClick(!fr.isComponentActivatedOnMouseClick());
    }

    public boolean isEnabled() {
        if (container.microworld == null)
            return false;
        ESlateComponent activeComponent = container.mwdComponents.activeComponent;
        if (activeComponent == null || activeComponent.frame == null || activeComponent.frame.isMaximum())
            return false;

        return container.currentView.componentActivationMethodChangeAllowed;
    }

}
