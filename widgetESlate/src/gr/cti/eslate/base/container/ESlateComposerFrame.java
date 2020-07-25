package gr.cti.eslate.base.container;

/**
 * User: Yiorgos Tsironis
 */
public class ESlateComposerFrame extends ESlateContainerFrame {
    public ESlateComposerFrame(String[] args, String testMicroworldFileName, int times) {
        super(args, testMicroworldFileName, times);

        getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ((ESlateComposer) container).setMenuBarVisible(!((ESlateComposer) container).isMenuBarVisible());
//                System.out.println("ALT + Z pressed ");
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.ALT_MASK, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected void initializeContainer() {
        container = new ESlateComposer();
        container.initialize();
    }
}
