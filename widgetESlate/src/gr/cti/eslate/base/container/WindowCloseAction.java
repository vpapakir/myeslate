package gr.cti.eslate.base.container;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

public class WindowCloseAction extends AbstractAction {
	Window window = null;

    public WindowCloseAction(Window window, String title) {
		super(title);
		this.window = window;
		putValue(AbstractAction.ACCELERATOR_KEY,
				 KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
										0,
										false)
		);
    }
    public void actionPerformed(ActionEvent e) {
System.out.println("WindowCloseAction");
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
}
