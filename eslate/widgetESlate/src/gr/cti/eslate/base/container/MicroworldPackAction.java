package gr.cti.eslate.base.container;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class MicroworldPackAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldPackAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
    }

/* This class makes possible the creation of only 1 instance of 1 listener class for the 10
 * menu items that have to do with microworld packaging.
 */
/*class PackMicroworldActionListener implements ActionListener {
    ESlateContainer container;

    public PackMicroworldActionListener(ESlateContainer container) {
        if (container == null)
            throw new NullPointerException();
        this.container = container;
    }
*/
    public void actionPerformed(ActionEvent e) {
        if (container.microworld == null) return;
        String actionText = ((JMenuItem) e.getSource()).getText();
//        System.out.println("PackMicroworldActionListener actionText: " + actionText);
        if (actionText.equals(container.containerBundle.getString("MicroworldPackDown")))
            container.packMicroworld(ESlateContainer.DOWN_SIDE);
        else if (actionText.equals(container.containerBundle.getString("MicroworldPackUp")))
            container.packMicroworld(ESlateContainer.UP_SIDE);
        else if (actionText.equals(container.containerBundle.getString("MicroworldPackLeft")))
            container.packMicroworld(ESlateContainer.LEFT_SIDE);
        else if (actionText.equals(container.containerBundle.getString("MicroworldPackRight")))
            container.packMicroworld(ESlateContainer.RIGHT_SIDE);
        else if (actionText.equals(container.containerBundle.getString("MicroworldPackAll")))
            container.packMicroworld(ESlateContainer.ALL_SIDES);
    }

    public boolean isEnabled() {
        return (container.microworld != null);
    }
}

