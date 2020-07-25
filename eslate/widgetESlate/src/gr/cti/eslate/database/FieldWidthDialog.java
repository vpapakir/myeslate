package gr.cti.eslate.database;

import gr.cti.eslate.utils.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;
import java.util.Locale;


class FieldWidthDialog extends JDialog {
    int dialogWidth = 250;
    int dialogHeight = 156;

//    boolean alreadyLostFocus = false;
//    boolean entryError = false;

    JButton cancel;
    static int minWidth, maxWidth;
    JTextField minWidthField, maxWidthField;
//    Font dialogFont;
//    FontMetrics fm;
    static int clickedButton = 0;
    transient ResourceBundle infoBundle; // = ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());

    protected FieldWidthDialog(Frame frame, int minWidth, int maxWidth, Database dbComponent) {
        super(frame, true);
        final Frame dialogFrame = frame;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        infoBundle = dbComponent.infoBundle;
//        if (infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR"))
//            dialogFont = new Font("Helvetica", Font.PLAIN, 12);
//        else
//            dialogFont = new Font("Dialog", Font.PLAIN, 12);

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

//        fm = getToolkit().getFontMetrics(dialogFont);

        setTitle(infoBundle.getString("FieldWidthDialogMsg1"));

        Color titleBorderColor = new Color(119, 40, 104);

        //getContentPane().setBackground(Color.lightGray);
        BoxLayout bl0 = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl0);

        JPanel widthPanel = new JPanel(true);
        BoxLayout bl1 = new BoxLayout(widthPanel, BoxLayout.Y_AXIS);
        widthPanel.setLayout(bl1);

        minWidthField = new JTextField();
        if (minWidth == -1)
            minWidthField.setText("");
        else
            minWidthField.setText(new Integer(minWidth).toString());
//        minWidthField.setFont(dialogFont);
        minWidthField.setPreferredSize(new Dimension(50, 20));
        minWidthField.setMaximumSize(new Dimension(50, 20));
        minWidthField.setMinimumSize(new Dimension(50, 20));
        minWidthField.setHorizontalAlignment(JTextField.RIGHT);
        JLabel l1 = new JLabel(infoBundle.getString("FieldWidthDialogMsg2"));
//        l1.setFont(dialogFont);
        l1.setAlignmentY(CENTER_ALIGNMENT);
        int l1Width = l1.getFontMetrics(l1.getFont()).stringWidth(l1.getText());

        maxWidthField = new JTextField();
        if (maxWidth == -1)
            maxWidthField.setText("");
        else
            maxWidthField.setText(new Integer(maxWidth).toString());
        maxWidthField.setPreferredSize(new Dimension(50, 20));
        maxWidthField.setMaximumSize(new Dimension(50, 20));
        maxWidthField.setMinimumSize(new Dimension(50, 20));
//        maxWidthField.setFont(dialogFont);
        maxWidthField.setHorizontalAlignment(JTextField.RIGHT);
        JLabel l2 = new JLabel(infoBundle.getString("FieldWidthDialogMsg3"));
//        l2.setFont(dialogFont);
        l2.setAlignmentY(CENTER_ALIGNMENT);
        int l2Width = l2.getFontMetrics(l2.getFont()).stringWidth(l2.getText());

        JPanel minWidthPanel = new JPanel(true);
        BoxLayout bl2 = new BoxLayout(minWidthPanel, BoxLayout.X_AXIS);
        minWidthPanel.setLayout(bl2);

        minWidthPanel.add(Box.createHorizontalStrut(112 - l1Width));
        minWidthPanel.add(l1);
        minWidthPanel.add(Box.createHorizontalStrut(40));
        minWidthPanel.add(minWidthField);
        minWidthPanel.setAlignmentY(CENTER_ALIGNMENT);
        minWidthPanel.setPreferredSize(new Dimension(dialogWidth - 20, 30));
        minWidthPanel.setMaximumSize(new Dimension(dialogWidth - 20, 30));

        JPanel maxWidthPanel = new JPanel(true);
        BoxLayout bl3 = new BoxLayout(maxWidthPanel, BoxLayout.X_AXIS);
        maxWidthPanel.setLayout(bl3);

        maxWidthPanel.add(Box.createHorizontalStrut(112 - l2Width));
        maxWidthPanel.add(l2);
        maxWidthPanel.add(Box.createHorizontalStrut(40));
        maxWidthPanel.add(maxWidthField);
        maxWidthPanel.setAlignmentY(CENTER_ALIGNMENT);
        maxWidthPanel.setPreferredSize(new Dimension(dialogWidth - 20, 30));
        maxWidthPanel.setMaximumSize(new Dimension(dialogWidth - 20, 30));

        widthPanel.add(minWidthPanel);
        widthPanel.add(Box.createVerticalStrut(10));
        widthPanel.add(maxWidthPanel);
        TitledBorder tb = new TitledBorder(infoBundle.getString("FieldWidthDialogMsg4"));
        widthPanel.setBorder(tb);
        tb.setTitleColor(titleBorderColor);

        widthPanel.setPreferredSize(new Dimension(dialogWidth - 10, 100));
        widthPanel.setMaximumSize(new Dimension(dialogWidth - 10, 100));

        JPanel buttonPanel = new JPanel(true);

        JButton ok = new JButton(infoBundle.getString("OK"));
        ok.setMaximumSize(new Dimension(100, 30));
        ok.setPreferredSize(new Dimension(100, 30));
        ok.setMinimumSize(new Dimension(100, 30));
//        ok.setFont(dialogFont);
        ok.setEnabled(true);
        ok.setForeground(new Color(0,0,128));

        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(new Dimension(100, 30));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.setMinimumSize(new Dimension(100, 30));
//        cancel.setFont(dialogFont);
        cancel.setEnabled(true);
        cancel.setForeground(new Color(0,0,128));

        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(ok);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);

        BoxLayout bl6 = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(bl6);
        buttonPanel.setMaximumSize(new Dimension(dialogWidth-10, 35));
        buttonPanel.setPreferredSize(new Dimension(dialogWidth-10, 35));
        buttonPanel.setMinimumSize(new Dimension(dialogWidth-10, 35));

//        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(widthPanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(10));

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean unspecifiedMinWidth = false, unspecifiedMaxWidth = false;
                int w1 = 0, w2 = 0;
                String value = minWidthField.getText();
                if (!value.equals("")) {
                    try{
                        w1 = new Integer(value).intValue();
                    }catch (NumberFormatException e1) {
                        ESlateOptionPane.showMessageDialog(dialogFrame, infoBundle.getString("FieldWidthDialogMsg5") + value + infoBundle.getString("FieldWidthDialogMsg6"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        minWidthField.setText((FieldWidthDialog.minWidth == -1)? "" : new Integer(FieldWidthDialog.minWidth).toString());
                        minWidthField.selectAll();
                        return;
                    }
                    if (w1 < 30) {
                        ESlateOptionPane.showMessageDialog(dialogFrame, infoBundle.getString("FieldWidthDialogMsg7"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        minWidthField.setText((FieldWidthDialog.minWidth == -1)? "" : new Integer(FieldWidthDialog.minWidth).toString());
                        minWidthField.selectAll();
                        return;
                    }
                }else
                    unspecifiedMinWidth = true;

                value = maxWidthField.getText();
                if (!value.equals("")) {
                    try{
                        w2 = new Integer(value).intValue();
                    }catch (NumberFormatException e1) {
                        ESlateOptionPane.showMessageDialog(dialogFrame, infoBundle.getString("FieldWidthDialogMsg5") + value + infoBundle.getString("FieldWidthDialogMsg8"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        maxWidthField.setText((FieldWidthDialog.maxWidth == -1)? "" : new Integer(FieldWidthDialog.maxWidth).toString());
                        maxWidthField.selectAll();
                        return;
                    }
                    if (w2 > 2000) {
                        ESlateOptionPane.showMessageDialog(dialogFrame, infoBundle.getString("FieldWidthDialogMsg9"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        maxWidthField.setText((FieldWidthDialog.maxWidth == -1)? "" : new Integer(FieldWidthDialog.maxWidth).toString());
                        maxWidthField.selectAll();
                        return;
                    }
                }else
                    unspecifiedMaxWidth = true;

                if (!unspecifiedMinWidth && !unspecifiedMaxWidth) {
                    if (w1 > w2) {
                        ESlateOptionPane.showMessageDialog(dialogFrame, infoBundle.getString("FieldWidthDialogMsg10"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        minWidthField.setText((FieldWidthDialog.minWidth == -1)? "" : new Integer(FieldWidthDialog.minWidth).toString());
                        maxWidthField.setText((FieldWidthDialog.maxWidth == -1) ? "" : new Integer(FieldWidthDialog.maxWidth).toString());
                        minWidthField.selectAll();
                        maxWidthField.selectAll();
                        return;
                    }
                }

//                System.out.println("w1: " + w1 + "  w2: " + w2);
                if (unspecifiedMinWidth)
                    FieldWidthDialog.minWidth = -1;
                else
                    FieldWidthDialog.minWidth = w1;
                if (unspecifiedMaxWidth)
                    FieldWidthDialog.maxWidth = -1;
                else
                    FieldWidthDialog.maxWidth = w2;
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
                clickedButton = 0;
	            dispose();
	        }
	    });

        /* Dispaying the "FieldWidthDialog".
         */
        setResizable(false);
        pack();
        setSize(dialogWidth, dialogHeight);
//    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//	    setLocation(screenSize.width/2 - dialogWidth/2, screenSize.height/2 - dialogHeight/2);
        Rectangle dbBounds = dbComponent.getBounds();
        System.out.println("getLocationOnScreen() 2");
        java.awt.Point dbLocation = dbComponent.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + dbComponent.getLocationOnScreen());
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

}