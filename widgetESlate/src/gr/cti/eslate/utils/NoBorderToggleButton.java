package gr.cti.eslate.utils;

import javax.swing.JToggleButton;
import javax.swing.Icon;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class NoBorderToggleButton extends JToggleButton {

static final long serialVersionUID=-3948506449862488023L;

    final static ItemListener itemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            NoBorderToggleButton b = (NoBorderToggleButton) e.getSource();
//            System.out.println("ItemListener setBorderPainted("+isSelected()+")");
            b.setBorderPainted(b.isSelected());
            b.repaint();
        }
    };
    final static MouseAdapter borderAdapter = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            NoBorderToggleButton b = (NoBorderToggleButton) e.getSource();
            if (b.isEnabled() && !b.isSelected()) {
                b.setBorderPainted(true);
                b.repaint();
            }
        }
        public void mouseExited(MouseEvent e) {
            NoBorderToggleButton b = (NoBorderToggleButton) e.getSource();
            if (b.isEnabled() && !b.isSelected()) {
                b.setBorderPainted(false);
                b.repaint();
            }
        }
    };

    public NoBorderToggleButton() {
        this(null, null);
    }

    public NoBorderToggleButton(Icon icon) {
        this(null, icon);
    }

    public NoBorderToggleButton(String text) {
        this(text, null);
    }

    public NoBorderToggleButton(String text, Icon icon) {
        super(text, icon);
            setAlignmentY(CENTER_ALIGNMENT);
              setRequestFocusEnabled(false);
              setBorderPainted(false);
        addMouseListener(borderAdapter);
        addItemListener(itemListener);
    }
}
