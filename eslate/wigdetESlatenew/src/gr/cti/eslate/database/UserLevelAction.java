package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;
import java.awt.Rectangle;
import java.awt.Font;
import java.util.Locale;
import java.awt.Color;
import java.util.ResourceBundle;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.base.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;


public class UserLevelAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    static final int NOVICE_USER_MODE = 0;
    static final int ADVANCED_USER_MODE = 1;
    String errorStr;
    JButton cancel2;

    public UserLevelAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public UserLevelAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        //PasswordDialog pw = new PasswordDialog(new JFrame(), dBase.dbComponent, "Administrator Password", "DatabaseMsg131", null);
        //if (pw.clickedButton == 1) {
            //String pass = pw.getPassword();
            //if (pass != null && pass.equals(dBase.adminPass)) {
        final JDialog userLevelDialog = new JDialog(dBase.topLevelFrame, true);
        int width2 = 250;
        int height2 = 124;
        // ESCAPE HANDLER
        userLevelDialog.getRootPane().registerKeyboardAction(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              cancel2.doClick();
              javax.swing.ButtonModel bm = cancel2.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
            javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);


        JPanel mainPanel2 = new JPanel(true);
        mainPanel2.setLayout(new BoxLayout(mainPanel2, BoxLayout.Y_AXIS));

        JPanel userModePanel = new JPanel(true);
        userModePanel.setLayout(new BoxLayout(userModePanel, BoxLayout.Y_AXIS));
        Dimension d2 = new Dimension(width2, 82);
        userModePanel.setMaximumSize(d2);
        userModePanel.setMinimumSize(d2);
        userModePanel.setPreferredSize(d2);
        userModePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        TitledBorder tb = new TitledBorder(infoBundle.getString("User mode"));
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            tb.setTitleFont(new Font("Dialog", Font.PLAIN, 12));
//        else
//            tb.setTitleFont(new Font("Helvetica", Font.PLAIN, 12));

        tb.setTitleColor(new Color(119, 40, 104));
        userModePanel.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2), tb));

        final JRadioButton rb1 = new JRadioButton(infoBundle.getString("Novice"));
        final JRadioButton rb2 = new JRadioButton(infoBundle.getString("Advanced"));
        ButtonGroup bgrp = new ButtonGroup();
        bgrp.add(rb1);
        bgrp.add(rb2);
        if (dBase.userMode == NOVICE_USER_MODE)
            rb1.setSelected(true);
        else
            rb1.setSelected(false);
        if (dBase.userMode == ADVANCED_USER_MODE)
            rb2.setSelected(true);
        else
            rb2.setSelected(false);

        d2 = new Dimension(width2, 21);
        JPanel rb1Panel = new JPanel(true);
        rb1Panel.setLayout(new BoxLayout(rb1Panel, BoxLayout.X_AXIS));
        rb1Panel.add(Box.createHorizontalStrut(70));
        rb1Panel.add(rb1);
        rb1.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        rb1Panel.setMaximumSize(d2);
        rb1Panel.setMinimumSize(d2);
        rb1Panel.setPreferredSize(d2);
        rb1Panel.add(Box.createGlue());

        JPanel rb2Panel = new JPanel(true);
        rb2Panel.setLayout(new BoxLayout(rb2Panel, BoxLayout.X_AXIS));
        rb2Panel.add(Box.createHorizontalStrut(70));
        rb2Panel.add(rb2);
        rb2.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        rb2Panel.add(Box.createGlue());
        rb2Panel.setMaximumSize(d2);
        rb2Panel.setMinimumSize(d2);
        rb2Panel.setPreferredSize(d2);

        userModePanel.add(rb1Panel);
        userModePanel.add(rb2Panel);

        JPanel buttonPanel2 = new JPanel(true);
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.X_AXIS));

        d2 = new Dimension(100, 28);
        final JButton OK2 = new JButton(infoBundle.getString("OK"));
        OK2.setMaximumSize(d2);
        OK2.setMinimumSize(d2);
        OK2.setPreferredSize(d2);
        OK2.setForeground(new Color(0,0,128));
        cancel2 = new JButton(infoBundle.getString("Cancel"));
        cancel2.setMaximumSize(d2);
        cancel2.setMinimumSize(d2);
        cancel2.setPreferredSize(d2);
        cancel2.setForeground(new Color(0,0,128));

        buttonPanel2.add(Box.createGlue());
        buttonPanel2.add(OK2);
        buttonPanel2.add(Box.createHorizontalStrut(10));
        buttonPanel2.add(cancel2);
        buttonPanel2.add(Box.createGlue());
        buttonPanel2.setBorder(new EmptyBorder(5,5,5,5));

        mainPanel2.add(userModePanel);
        mainPanel2.add(buttonPanel2);

        d2 = new Dimension(width2, height2);
        mainPanel2.setMaximumSize(d2);
        mainPanel2.setMinimumSize(d2);
        mainPanel2.setPreferredSize(d2);

        userLevelDialog.getContentPane().add(mainPanel2);
        userLevelDialog.setTitle(infoBundle.getString("DatabaseMsg133"));
        userLevelDialog.setResizable(false);

        final Database dbComponentCopy = dBase.dbComponent;

        OK2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rb1.isSelected())
                    dbComponentCopy.setUserMode(NOVICE_USER_MODE);
                else
                    dbComponentCopy.setUserMode(ADVANCED_USER_MODE);
                userLevelDialog.dispose();
            }
        });
        cancel2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userLevelDialog.dispose();
            }
        });

        userLevelDialog.pack();
        Rectangle dbBounds = dBase.dbComponent.getBounds();
        System.out.println("getLocationOnScreen() 15");
        java.awt.Point dbLocation = dBase.dbComponent.getLocationOnScreen();
        int x = dbLocation.x + dbBounds.width/2 - userLevelDialog.getSize().width/2;
        int y = dbLocation.y + dbBounds.height/2-userLevelDialog.getSize().height/2;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (x+userLevelDialog.getSize().width > screenSize.width)
            x = screenSize.width - userLevelDialog.getSize().width;
        if (y+userLevelDialog.getSize().height > screenSize.height)
            y = screenSize.height - userLevelDialog.getSize().height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        userLevelDialog.setLocation(x, y);
        userLevelDialog.setVisible(true);

    //}else
     //   ESlateOptionPane.showMessageDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg132"), errorStr, JOptionPane.ERROR_MESSAGE);
  }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBUserLevel.setEnabled(b);
    }
}
