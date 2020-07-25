package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class PerformanceManagerDialog extends JDialog {
    JButton closeButton;
    ResourceBundle bundle;

    public PerformanceManagerDialog(ESlateContainer container, Frame topLevelFrame) {
        super(topLevelFrame, true);
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.PerformanceManagerDialogBundle", Locale.getDefault());
        setTitle(bundle.getString("Title"));


        JPanel performanceMgrPanel = PerformanceManager.getPerformanceManager().getGUI();

        closeButton = new JButton(bundle.getString("Close"));

        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        closeButton.setMargin(zeroInsets);
        closeButton.setForeground(color128);

        Dimension buttonDim2 = new Dimension(90, 25);
        closeButton.setMaximumSize(buttonDim2);
        closeButton.setMinimumSize(buttonDim2);
        closeButton.setPreferredSize(buttonDim2);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.setBorder(new EmptyBorder(10, 0, 6, 0));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(performanceMgrPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }
}
