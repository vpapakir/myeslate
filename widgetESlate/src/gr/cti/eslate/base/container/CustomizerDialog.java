package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


class CustomizerDialog extends JDialog {
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    Locale locale;
    boolean localeIsGreek = false;
    ResourceBundle customizerDialogBundle;
    JButton okButton;

    public CustomizerDialog(java.awt.Frame parentFrame, String title, Component propertyPanel) {
        super(parentFrame, true);
        if (propertyPanel == null) throw new NullPointerException();
//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());

        locale = Locale.getDefault();
        customizerDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.CustomizerDialogBundle", locale);
        if (customizerDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.CustomizerDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(title);

        okButton = new JButton(customizerDialogBundle.getString("OK"));
//        if (localeIsGreek)
//            okButton.setFont(greekUIFont);
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.setBorder(new EmptyBorder(2, 0, 4, 0));

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 5));
        mainPanel.add(propertyPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = okButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = okButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(okButton);
    }
}

