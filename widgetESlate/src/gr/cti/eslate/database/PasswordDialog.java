package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.Insets;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.Table;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.event.*;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Enumeration;


class PasswordDialog extends JDialog {
    PasswordDialog thisDialog;

    JButton cancel;

    static int clickedButton = 0;
    transient ResourceBundle infoBundle;
    Database database;
    protected String givenPassword =  null;

    protected PasswordDialog(Frame frame, Database db, String passwordLabel, String title, String password) {
        super(frame, true);
        thisDialog = this;
        this.database = db;
        infoBundle = database.infoBundle;
        givenPassword = null;

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);



        Color color128 = new Color(0, 0, 128);
        Color titleBorderColor = new Color(119, 40, 104);

        Font dialogFont;
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            dialogFont = new Font("Dialog", Font.PLAIN, 12);
//        else
//            dialogFont = new Font("Helvetica", Font.PLAIN, 12);

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel passPanel = new JPanel(true);
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
        JLabel lb1 = new JLabel(infoBundle.getString(passwordLabel));
        lb1.setAlignmentX(CENTER_ALIGNMENT);
        final JPasswordField passFld = new JPasswordField();
        passFld.setEchoChar('*');
        passFld.setFont(new Font("Courier", Font.PLAIN, 12));
        Dimension d1 = new Dimension(110, 20);
        passFld.setMaximumSize(d1);
        passFld.setMinimumSize(d1);
        passFld.setPreferredSize(d1);
        passFld.setAlignmentX(CENTER_ALIGNMENT);
        passFld.setText(password);

        passPanel.add(Box.createGlue());
        passPanel.add(lb1);
        passPanel.add(Box.createHorizontalStrut(8));
        passPanel.add(passFld);
        passPanel.add(Box.createGlue());
        passPanel.setBorder(new EmptyBorder(5,5,5,5));

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        Dimension d = new Dimension(100, 28);
        final JButton OK = new JButton(infoBundle.getString("OK"));
        OK.setMaximumSize(d);
        OK.setMinimumSize(d);
        OK.setPreferredSize(d);
        OK.setForeground(new Color(0,0,128));
        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(d);
        cancel.setMinimumSize(d);
        cancel.setPreferredSize(d);
        cancel.setForeground(new Color(0,0,128));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(OK);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue());
        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        mainPanel.add(passPanel);
        mainPanel.add(buttonPanel);


        passFld.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    OK.doClick();
            }
        });
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                givenPassword = passFld.getText();
                clickedButton = 1;
                dispose();
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickedButton = 0;
                dispose();
            }
        });

        /* Add the window listener.
         */
    	addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
//                thisDialog.removeWindowListener(l);
                clickedButton = 0;
	            dispose();
	        }
	    });

        getContentPane().add(mainPanel);
        setTitle(infoBundle.getString(title));
        setResizable(false);

//    	Rectangle dbBounds = database.getBounds();
        pack();
//	    setLocation(dbBounds.width/2 - getSize().width/2, dbBounds.height/2 - getSize().height/2);
        passFld.requestFocus();
        Rectangle dbBounds = database.getBounds();
        System.out.println("getLocationOnScreen() 9");
        java.awt.Point dbLocation = database.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
        int x = dbLocation.x + dbBounds.width/2 - getSize().width/2;
        int y = dbLocation.y + dbBounds.height/2-getSize().height/2;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (x+getSize().width > screenSize.width)
            x = screenSize.width - getSize().width;
        if (y+getSize().height > screenSize.height)
            y = screenSize.height - getSize().height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        setLocation(x, y);
        setVisible(true);
    }

    public String getPassword() {
        return givenPassword;
    }
}