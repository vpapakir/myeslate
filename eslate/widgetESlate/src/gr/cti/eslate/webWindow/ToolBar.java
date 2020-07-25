package gr.cti.eslate.webWindow;


import java.awt.*;
import javax.swing.*;


public class ToolBar extends JPanel {

    public ToolBar() {
        m_buttonPanel = new JPanel();

        m_buttonPanel.setLayout(new BoxLayout(m_buttonPanel, 0));//(FlowLayout.LEADING,0,0));
        //m_buttonPanel.setBackground(SystemColor.control);
        //add(m_buttonPanel);
        //setLayout(new FlowLayout(FlowLayout.LEFT))
        setLayout(new GridBagLayout());
        Helper.addComponent(this, m_buttonPanel, 17, 0, 1, 1, 0, 0, new Insets(2, 5, 2, 2), 0, 0, 1.0D, 0.0D);
    }

    public void addButton(ImageButton btn, int gridx) {

        //Helper.addComponent(this, btn, 17, 0,1,1, 0,0, new Insets(0, 0, 0, 0), 0, 0, 0.0D, 1.0D);
        m_buttonPanel.add(btn);
    }

    /*    public void paint(Graphics g)
     {
     Dimension d = getSize();
     g.setColor(SystemColor.control);
     g.fillRect(0, 0, d.width, d.height);
     super.paint(g);
     }
     */
    JPanel m_buttonPanel;
}
