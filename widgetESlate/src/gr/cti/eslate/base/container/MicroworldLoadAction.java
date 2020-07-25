package gr.cti.eslate.base.container;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

class MicroworldLoadAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldLoadAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
System.out.println("ACTION");    	
//        if (container.microworld != null)
//            container.containerUtils.forceMenuClose(new JMenu[] {microworldMenu, microworldLoad});
        String fileName = container.getSystemFile(false, container.containerBundle.getString("ContainerMsg8"), null, container.getMwdFileExtensions());
        container.loadLocalMicroworld(fileName, true, true);
    }

}
