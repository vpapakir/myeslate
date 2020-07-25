package gr.cti.eslate.utils;

import javax.swing.JButton;
import javax.swing.Icon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class NoBorderButton extends JButton {

    static final long serialVersionUID = -7683207718936436283L;

    final static MouseAdapter borderAdapter = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            JButton b = (JButton) e.getSource();
            if (b.isEnabled()) {
                b.setBorderPainted(true);
                b.repaint();
            }
        }
        public void mouseExited(MouseEvent e) {
            JButton b = (JButton) e.getSource();
            b.setBorderPainted(false);
            b.repaint();
        }
    };


    public NoBorderButton() {
        this(null, null);
    }

    public NoBorderButton(Icon icon) {
        this(null, icon);
    }

    public NoBorderButton(String text) {
        this(text, null);
    }

    public NoBorderButton(String text, Icon icon) {
        super(text, icon);
        setAlignmentY(CENTER_ALIGNMENT);
        setRequestFocusEnabled(false);
        setBorderPainted(false);
        addMouseListener(borderAdapter);
    }
}
