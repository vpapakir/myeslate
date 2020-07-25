package gr.cti.eslate.webWindow;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LocationPanel extends JPanel {
    private JLabel openedFileType = new JLabel();

    private static ResourceBundle bundleMessages;

    public LocationPanel(ImageButton img) {

        //setBackground(SystemColor.control);
        //openedFileType.setBackground(SystemColor.control);
        openedFileType.setText("");
        //setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        setLayout(new GridBagLayout());
        JLabel label = new JLabel();

        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.webWindow.BrowserBundle", Locale.getDefault());
        label.setText(bundleMessages.getString("Location:"));
        label.setText(bundleMessages.getString("Location:"));
        textField = new JTextField("");
        //add(label);add(openedFileType);add(textField);
        JPanel lPanel = new JPanel();

        lPanel.setLayout(new GridBagLayout());
        Helper.addComponent(this, label, 17, 0, 1, 1, 0, 0, new Insets(0, 5, 0, 0), 0, 0, 0.0D, 1.0D);
        Helper.addComponent(this, openedFileType, 17, 0, 1, 1, 1, 0, new Insets(0, 5, 0, 5), 0, 0, 0.0D, 1.0D);
        Helper.addComponent(this, textField, 17, 1, 1, 1, 2, 0, new Insets(0, 0, 0, 5), 0, 0, 1.0D, 1.0D);
        //add(lPanel);
    }

    /*    public void paint(Graphics g)
     {
     Dimension d = getSize();
     g.setColor(SystemColor.control);
     g.fillRect(0, 0, d.width, d.height);
     super.paint(g);
     }
     */
    public void setLocation(String txt) {
        //System.out.println("setlocation in location panel called");
        textField.setText(txt);
    }

    JTextField textField;

    protected void adjustFileTypeSettings(String kind) {
        if (kind.equals("mwd")) {
            openedFileType.setForeground(Color.yellow);
            openedFileType.setFont(new Font("Arial", 1, 12));
            openedFileType.setText("MWD ");
        } else if (kind.equals("html")) {
            openedFileType.setForeground(Color.blue);
            openedFileType.setFont(new Font("Arial", 1, 12));
            openedFileType.setText("HTML ");
        } else if (kind.equals("txt")) {
            openedFileType.setForeground(Color.green);
            openedFileType.setFont(new Font("Arial", 1, 12));
            openedFileType.setText("TXT ");
        } else {
            openedFileType.setForeground(Color.red);
            openedFileType.setFont(new Font("Arial", 1, 12));
            openedFileType.setText(" ? ");
        }
    }
}
