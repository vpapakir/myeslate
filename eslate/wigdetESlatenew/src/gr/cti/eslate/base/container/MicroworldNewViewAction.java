package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class MicroworldNewViewAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldNewViewAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (container.microworld == null || !container.microworld.viewCreationAllowed)
            return;
        MicroworldView view = container.createNewView(null);
        ESlateOptionPane.showMessageDialog(container.parentFrame, container.containerBundle.getString("ContainerMsg43") + view.viewName + container.containerBundle.getString("ContainerMsg44"), container.containerBundle.getString("ViewDefined"), JOptionPane.INFORMATION_MESSAGE);
//                System.out.println("mView.viewName: " + mView.viewName);
//                System.out.println("componentInfo: " + mView.componentInfo[0].componentName + ", iconifiableFrameState: " + mView.componentInfo[0].iconifiableFrameState);
    }

    public boolean isEnabled() {
//System.out.println("container.microworld != null: " + (container.microworld != null));
        return (container.microworld != null && container.microworld.viewCreationAllowed);
    }

}
