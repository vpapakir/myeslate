package gr.cti.eslate.webWindow;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
;


public class MessageBox extends JDialog
    implements ActionListener {

    public MessageBox(JFrame parent, String szTitle, String szMsg) {
        super(parent, true);
        lblMessage = new JLabel();
        butOK = new JButton();
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().setFont(new Font("SansSerif", 0, 12));
        setResizable(false);
        lblMessage.setText(szMsg);
        //lblMessage.setAlignment(1);
        addComponent(this, lblMessage, 11, 2, 1, 1, 0, 0, new Insets(5, 10, 5, 10), 0, 0, 1.0D, 0.0D);
        butOK.setText("  OK  ");
        addComponent(this, butOK, 10, 0, 1, 1, 0, 1, new Insets(5, 0, 10, 0), 10, 0, 0.5D, 0.0D);
        butOK.addActionListener(this);
        setTitle(szTitle);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == butOK)
            setVisible(false);
    }

    protected void addComponent(Container con, Component comp, int anchor, int fill, int gridheight, int gridwidth, int gridx,
        int gridy, Insets insets, int ipadx, int ipady, double weightx, double weighty) {
        ((JDialog) con).getContentPane().setLayout(new GridBagLayout());
        GridBagLayout gridbag = (GridBagLayout) ((JDialog) con).getLayout();
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
        ((JDialog) con).getContentPane().add(comp);
    }

    public void setMessageText(String szMsg) {
        lblMessage.setText(szMsg);
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

    public void setVisible(boolean b) {
        if (b) {
            int width = getFontMetrics(getFont()).stringWidth(lblMessage.getText());

            setSize(Math.max(200, width + 40), 110);
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();

            setLocation(bounds.x + (bounds.width - abounds.width) / 2, bounds.y + (bounds.height - abounds.height) / 2);
        }
        super.setVisible(b);
    }

    protected JLabel lblMessage;
    protected JButton butOK;

}
