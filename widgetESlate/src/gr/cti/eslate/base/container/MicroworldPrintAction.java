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

public class MicroworldPrintAction extends AbstractAction {
    ESlateContainer container = null;

    public MicroworldPrintAction(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        putValue(ACCELERATOR_KEY,
                 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
                                        java.awt.Event.CTRL_MASK,
                                        false)
        );
    }

    public void actionPerformed(ActionEvent e) {
//        JMenu[] menus = new JMenu[] {microworldMenu, printMenu};
//        container.containerUtils.forceMenuClose(menus);
        // At the moment the DesktopPane cannot be printed, because its print()/paint()
        // method throws an Exception. For the moment we print the view port.
        if (container.microworld == null) return;
        container.printMicroworld(ESlateContainer.PRINTER);
//                oldStylePrint();
//                pageLayoutPDF();

//                classicImagePrinterJob();

//                classicPrinterJob();

//                printStylePrinterJob();
    }

    public boolean isEnabled() {
        return (container.microworld != null && container.currentView.mwdPrintAllowed);
    }
}
