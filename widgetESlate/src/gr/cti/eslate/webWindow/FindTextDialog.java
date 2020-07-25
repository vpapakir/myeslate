package gr.cti.eslate.webWindow;


import horst.webwindow.WebWindow;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FindTextDialog extends JDialog
    implements ActionListener {

    private static ResourceBundle bundleMessages;

    public FindTextDialog(JFrame parent, String title, WebWindow w) {
        super(parent, title);
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.webWindow.BrowserBundle", Locale.getDefault());
        getContentPane().add(createControls());
        setSize(300, 130);
        m_htmlWindow = w;
        m_frame = parent;
        center();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == m_cancel)
            dispose();
        else
        if (e.getSource() == m_ok) {
            String text = m_text.getText();

            if (text != null) {
                int pos = m_htmlWindow.find(text);

                if (pos == -1) {
                    JFrame frame = new JFrame();
                    //frame.setIconImage(logo.getImage());
                    JDialog dialog = new JDialog(frame) {
                            public void showDialog(Window dialog, Component centerAroundComp, boolean pack) {
                                if (pack)
                                    dialog.pack();
                                Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                                int x, y;

                                if (centerAroundComp == null || !centerAroundComp.isVisible()) {
                                    x = (screenSize.width / 2) - (dialog.getSize().width / 2);
                                    y = (screenSize.height / 2) - (dialog.getSize().height / 2);
                                } else {
                                    Rectangle compBounds = centerAroundComp.getBounds();
                                    java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();

                                    x = compLocation.x + compBounds.width / 2 - dialog.getSize().width / 2;
                                    y = compLocation.y + compBounds.height / 2 - dialog.getSize().height / 2;
                                    if (x + dialog.getSize().width > screenSize.width)
                                        x = screenSize.width - dialog.getSize().width;
                                    if (y + dialog.getSize().height > screenSize.height)
                                        y = screenSize.height - dialog.getSize().height;
                                    if (x < 0) x = 0;
                                    if (y < 0) y = 0;
                                }
                                dialog.setLocation(x, y);
                                dialog.setVisible(true);
                            };
                        };

                    JOptionPane.showConfirmDialog(dialog, bundleMessages.getString("TextNotFound"), bundleMessages.getString("NOResults"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }
            dispose();
        }
    }

    protected void addComponent(Container con, Component comp, int anchor, int fill, int gridheight, int gridwidth, int gridx,
        int gridy, Insets insets, int ipadx, int ipady, double weightx, double weighty) {
        GridBagLayout gridbag = (GridBagLayout) con.getLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = anchor;
        c.fill = fill;
        c.gridheight = gridheight;
        c.gridwidth = gridwidth;
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = insets;
        c.ipadx = ipadx;
        c.ipady = ipady;
        c.weightx = weightx;
        c.weighty = weighty;
        gridbag.setConstraints(comp, c);
        con.add(comp);
    }

    void center() {
        Dimension d1 = getSize();
        Dimension d2 = getToolkit().getScreenSize();
        int frameWidth = d1.width;
        int frameHeight = d1.height;
        int screenWidth = d2.width;
        int screenHeight = d2.height;
        int xPos = Math.max((screenWidth - frameWidth) / 2, 0);
        int yPos = Math.max((screenHeight - frameHeight) / 2, 0);

        setLocation(xPos, yPos);
    }

    JPanel createControls() {
        m_text = new JTextField(10);
        m_ok = new JButton(bundleMessages.getString("OK"));
        m_cancel = new JButton(bundleMessages.getString("Cancel"));
        m_ok.addActionListener(this);
        m_cancel.addActionListener(this);
        JPanel p = new JPanel();

        p.setLayout(new GridBagLayout());
        addComponent(p, new JLabel(bundleMessages.getString("Text")), 17, 0, 1, 1, 0, 0, new Insets(5, 5, 5, 5), 0, 0, 0.0D, 0.0D);
        addComponent(p, m_text, 17, 2, 1, 1, 1, 0, new Insets(5, 5, 5, 5), 0, 0, 1.0D, 0.0D);
        JPanel p2 = new JPanel();

        p2.add(m_ok);
        p2.add(m_cancel);
        addComponent(p, p2, 10, 0, 1, 2, 0, 3, new Insets(5, 5, 5, 5), 0, 0, 1.0D, 1.0D);
        return p;
    }

    public static void showDialog(Window dialog, Component centerAroundComp, boolean pack) {

        if (pack)

            dialog.pack();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;

        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width / 2) - (dialog.getSize().width / 2);
            y = (screenSize.height / 2) - (dialog.getSize().height / 2);
        } else {
            Rectangle compBounds = centerAroundComp.getBounds();
            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();

            x = compLocation.x + compBounds.width / 2 - dialog.getSize().width / 2;
            y = compLocation.y + compBounds.height / 2 - dialog.getSize().height / 2;
            if (x + dialog.getSize().width > screenSize.width)
                x = screenSize.width - dialog.getSize().width;
            if (y + dialog.getSize().height > screenSize.height)
                y = screenSize.height - dialog.getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        dialog.setLocation(x, y);
        dialog.setVisible(true);
    }

    public JPanel m_controls;
    public JTextField m_text;
    public JButton m_ok;
    public JButton m_cancel;
    WebWindow m_htmlWindow;
    JFrame m_frame;
}
