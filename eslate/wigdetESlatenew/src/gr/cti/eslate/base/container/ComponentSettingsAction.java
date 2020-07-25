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

class ComponentSettingsAction extends AbstractAction {
	ESlateContainer container = null;

	public ComponentSettingsAction(ESlateContainer container, String title) {
		super(title);
		this.container = container;
		putValue(AbstractAction.ACCELERATOR_KEY,
				 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W,
										java.awt.Event.CTRL_MASK,
										false)
		);
	}

	public void actionPerformed(ActionEvent e) {
		if (container.microworld == null || container.mwdComponents.activeComponent == null) {
			return;
		}
		ESlateComponent activeComponent = container.mwdComponents.activeComponent;
//		container.containerUtils.forceMenuClose();
		new ComponentPropertiesDialog(container.parentFrame, container, activeComponent.frame);
	}

	public boolean isEnabled() {
		if (container.microworld == null)
			return false;
		ESlateComponent activeComponent = container.mwdComponents.activeComponent;
		if (activeComponent == null || activeComponent.frame == null || activeComponent.frame.isMaximum())
			return false;

		return true;
	}

}
