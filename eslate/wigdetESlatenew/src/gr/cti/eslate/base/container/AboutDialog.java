package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;


public class AboutDialog extends JDialog {
    JLabel lb;

    public AboutDialog(java.awt.Frame parentFrame, ESlateContainer container) {
        super(parentFrame, true);

        String title = container.containerBundle.getString("About");
        setTitle(title);

        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            lb = new JLabel(new ImageIcon(this.getClass().getResource("images/about_el.png")));
        else
            lb = new JLabel(new ImageIcon(this.getClass().getResource("images/about.png")));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(lb, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ((ImageIcon) lb.getIcon()).getImage().flush();
            }
        });

        pack();
        setResizable(false);
        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ((ImageIcon) lb.getIcon()).getImage().flush();
                dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        container.containerUtils.showDialog(this, container, true);
    }
}



