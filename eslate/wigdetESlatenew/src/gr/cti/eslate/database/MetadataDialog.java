package gr.cti.eslate.database;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Insets;
import gr.cti.eslate.database.engine.*;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;


class MetadataDialog extends JDialog {
    Table table;
    DBase db;
//1    Font dialogFont;
    FontMetrics fm;
    JTextArea metadataArea;
    int aboutWhat;
    JButton ok, cancel;
    transient ResourceBundle infoBundle;

    protected MetadataDialog(Frame frame, Database dbComponent, DBTable dbTable, int aboutWhat, boolean editAllowed) {
        super(frame, true);

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);


        if (dbComponent != null)
            this.db = dbComponent.db;
        if (dbTable != null)
            this.table = dbTable.table;
        this.aboutWhat = aboutWhat;

        infoBundle = Database.infoBundle;
        setTitle(infoBundle.getString("Description"));

        JLabel lb = new JLabel();
        if (aboutWhat == 0 && db != null)
            if (db.getTitle() == null)
                lb.setText(infoBundle.getString("MetadataDialogMsg1") + "\"\"");
            else
                lb.setText(infoBundle.getString("MetadataDialogMsg1") + "\"" + db.getTitle() + "\"");
        else if (aboutWhat == 1 && table != null)
            if (table.getTitle() == null)
                lb.setText(infoBundle.getString("MetadataDialogMsg2") + "\"\"");
            else
                lb.setText(infoBundle.getString("MetadataDialogMsg2") + "\"" + table.getTitle() + "\"");
        lb.setAlignmentY(CENTER_ALIGNMENT);

        JPanel labelPanel = new JPanel(true);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.add(Box.createGlue());
        labelPanel.add(lb);
        labelPanel.add(Box.createGlue());
        fm = lb.getFontMetrics(lb.getFont());
        int dialogWidth = fm.stringWidth(lb.getText()) + 10;
        if (dialogWidth < 250)
            dialogWidth = 250;
        Dimension d = new Dimension(dialogWidth, 15);
        labelPanel.setMaximumSize(d);
        labelPanel.setMinimumSize(d);
        labelPanel.setPreferredSize(d);

        metadataArea = new JTextArea();
        metadataArea.setEnabled(editAllowed);

        JScrollPane scrollPane = new JScrollPane(metadataArea);

        JPanel metadataPanel = new JPanel(true);
        metadataPanel.setLayout(new BoxLayout(metadataPanel, BoxLayout.Y_AXIS));
        metadataPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        metadataPanel.add(labelPanel);
        metadataPanel.add(Box.createVerticalStrut(2));
        metadataPanel.add(scrollPane);

        Insets zeroInsets = new Insets(0, 0, 0, 0);
        Color color128 = new Color(0, 0, 128);

        d = new Dimension(110, 30);
        ok = new JButton(infoBundle.getString("OK"));
        ok.setMaximumSize(d);
        ok.setPreferredSize(d);
        ok.setMinimumSize(d);
        ok.setEnabled(true);
        ok.setForeground(color128);
        ok.setMargin(zeroInsets);

        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(d);
        cancel.setPreferredSize(d);
        cancel.setMinimumSize(d);
        cancel.setEnabled(true);
        cancel.setForeground(color128);
        cancel.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(ok);
        buttonPanel.add(Box.createHorizontalStrut(6));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue());

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0,5));

        mainPanel.add(metadataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(new MatteBorder(7,3,5, 3, UIManager.getColor("control")));

        getContentPane().add(mainPanel);

        //Initialization
        if (aboutWhat == 0 && db != null)
            metadataArea.setText(db.getMetadata());
        else if (aboutWhat == 1 && table != null)
            metadataArea.setText(table.getMetadata());

        // Actions
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (MetadataDialog.this.aboutWhat == 0 && MetadataDialog.this.db != null)
                    MetadataDialog.this.db.setMetadata(metadataArea.getText());
                else if (MetadataDialog.this.aboutWhat == 1 && MetadataDialog.this.table != null)
                    MetadataDialog.this.table.setMetadata(metadataArea.getText());
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        /* Display the "MetadataDialog".
         */
        int height = 190;
        pack();
        setSize(dialogWidth, height);

        Rectangle bounds = null;
        java.awt.Point location = null;
System.out.println("getLocationOnScreen() 5");
        if (dbComponent != null) {
            bounds = dbComponent.getBounds();
            location = dbComponent.getLocationOnScreen();
        }else{
            bounds = dbTable.getBounds();
            location = dbTable.getLocationOnScreen();
        }
//        System.out.println("dbBounds: " + dbBounds + " location: " + dbComponent.getLocationOnScreen());
        int x = location.x + bounds.width/2 - getSize().width/2;
        int y = location.y + bounds.height/2-getSize().height/2;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (x+getSize().width > screenSize.width)
            x = screenSize.width - getSize().width;
        if (y+getSize().height > screenSize.height)
            y = screenSize.height - getSize().height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        setLocation(x, y);
//        metadataArea.requestFocus();
        setVisible(true);
    }
}

