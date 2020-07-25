package gr.cti.eslate.database;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.IntArray;

import gr.cti.eslate.utils.ESlateOptionPane;


class TableAutoCreateDialog extends JDialog {
    int dialogWidth = 500;
    int dialogHeight = 200;
    Color titleBorderColor = new Color(119, 40, 104);
    JTextField colField, rowField;
    JButton ok;
    DBTable dbTable;
    boolean findWhatFieldEmpty = true;
    static int clickedButton = 0;
    static int rowCount = 20;
    static int colCount = 2;
    Database dbComponent;
    JButton cancel;

    protected TableAutoCreateDialog(Frame frame, Database database) {
        super(frame, true);
        this.dbComponent = database;

//        getContentPane().setBackground(Color.lightGray);

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	this.getRootPane().WHEN_IN_FOCUSED_WINDOW);

        Font dialogFont;
//        if (database.infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR"))
//            dialogFont = new Font("Helvetica", Font.PLAIN, 12);
//        else
//            dialogFont = new Font("Dialog", Font.PLAIN, 12);

        JPanel rowColPanel = new JPanel(true);
        rowColPanel.setLayout(new BoxLayout(rowColPanel, BoxLayout.Y_AXIS));

        JLabel rowLabel = new JLabel(database.infoBundle.getString("Number of records"));
        JLabel colLabel = new JLabel(database.infoBundle.getString("Number of fields"));
        rowField = new JTextField("20");
        colField = new JTextField("2");

        rowField.setHorizontalAlignment(SwingConstants.RIGHT);
        colField.setHorizontalAlignment(SwingConstants.RIGHT);

        Dimension d = new Dimension(30, 20);
        rowField.setMaximumSize(d);
        rowField.setMinimumSize(d);
        rowField.setPreferredSize(d);
        colField.setMaximumSize(d);
        colField.setMinimumSize(d);
        colField.setPreferredSize(d);

        JPanel rowPanel = new JPanel(true);
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.add(rowLabel);
        rowPanel.add(Box.createHorizontalStrut(10));
        rowPanel.add(rowField);

        JPanel colPanel = new JPanel(true);
        colPanel.setLayout(new BoxLayout(colPanel, BoxLayout.X_AXIS));
        colPanel.add(colLabel);
        colPanel.add(Box.createGlue());
        colPanel.add(colField);

        rowColPanel.add(rowPanel);
        rowColPanel.add(colPanel);
        rowColPanel.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(0, 5, 0, 5)));


        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    rowCount = new Integer(rowField.getText()).intValue();
                    if (rowCount < 0) {
                        ESlateOptionPane.showMessageDialog(dbComponent, dbComponent.infoBundle.getString("DatabaseMsg93"), dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        rowField.requestFocus();
                        rowField.selectAll();
                        return;
                    }
                }catch (NumberFormatException exc) {
                    ESlateOptionPane.showMessageDialog(dbComponent, dbComponent.infoBundle.getString("DatabaseMsg91") + "\"" + rowField.getText() + "\"" + dbComponent.infoBundle.getString("DatabaseMsg95"), dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
                    rowField.requestFocus();
                    rowField.selectAll();
                    return;
                }
                try{
                    colCount = new Integer(colField.getText()).intValue();
                    if (colCount < 0) {
                        ESlateOptionPane.showMessageDialog(dbComponent, dbComponent.infoBundle.getString("DatabaseMsg94"), dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        colField.requestFocus();
                        colField.selectAll();
                        return;
                    }

                    clickedButton = 1;
	                dispose();
                }catch (NumberFormatException exc) {
                    colField.requestFocus();
                    colField.selectAll();
                    ESlateOptionPane.showMessageDialog(dbComponent, dbComponent.infoBundle.getString("DatabaseMsg91") + "\"" + colField.getText() + "\"" + dbComponent.infoBundle.getString("DatabaseMsg95"), dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        rowField.addActionListener(al);
        colField.addActionListener(al);

        KeyListener kl = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source.getText() != null && source.getText().trim().length() != 0)
                    ok.setEnabled(true);
                else
                    ok.setEnabled(false);

            }
            public void keyPressed(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (source.getText() != null && source.getText().trim().length() != 0)
                        ok.setEnabled(true);
                    else
                        ok.setEnabled(false);
                }
            }
        };
        rowField.addKeyListener(kl);
        colField.addKeyListener(kl);


        ok = new JButton(database.infoBundle.getString("OK"));
        ok.setMaximumSize(new Dimension(100, 30));
        ok.setPreferredSize(new Dimension(100, 30));
        ok.setMinimumSize(new Dimension(100, 30));
//        ok.setFont(dialogFont);
        ok.setEnabled(true);
        ok.setForeground(new Color(0,0,128));

        cancel = new JButton(database.infoBundle.getString("Cancel"));
        cancel.setMaximumSize(new Dimension(100, 30));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.setMinimumSize(new Dimension(100, 30));
//        cancel.setFont(dialogFont);
        cancel.setEnabled(true);
        cancel.setForeground(new Color(0,0,128));

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createGlue()); //createHorizontalStrut(15));
        buttonPanel.add(ok);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue()); //createHorizontalStrut(15));

/*        buttonPanel.setMaximumSize(new Dimension(120, 35));
        buttonPanel.setPreferredSize(new Dimension(120, 35));
        buttonPanel.setMinimumSize(new Dimension(120, 35));
*/
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        mainPanel.add(rowColPanel);
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(buttonPanel);
        mainPanel.setBorder(new MatteBorder(10, 10, 10, 10, UIManager.getColor("control")));

        getContentPane().add(mainPanel);

        ok.addActionListener(al);

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


        setTitle("Auto-create Table");
        setResizable(false);
        setSize(dialogWidth, dialogHeight);
        pack();
        java.awt.Rectangle dbBounds = database.getBounds();
        System.out.println("getLocationOnScreen() 11");
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
}