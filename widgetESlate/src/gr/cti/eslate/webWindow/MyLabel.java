package gr.cti.eslate.webWindow;


import java.awt.*;


public class MyLabel extends Component {

    public MyLabel() {
        m_font = new Font("Arial", 0, 12);
        m_txt = "";
        padding = 2;
        m_textColor = Color.black;
    }

    public MyLabel(String txt) {
        m_font = new Font("Arial", 0, 12);
        m_txt = "";
        padding = 2;
        m_textColor = Color.black;
        m_txt = txt;
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(m_font);
        int h = fm.getHeight() + 2 * padding;
        int w = 2 * padding;

        if (m_txt != null)
            w += fm.stringWidth(m_txt);
        return new Dimension(w, h);
    }

    public void paint(Graphics g) {
        Dimension d = getSize();

        if (m_offscreen == null || d.width != m_offscreensize.width || d.height != m_offscreensize.height) {
            m_offscreen = createImage(d.width, d.height);
            m_offscreensize = d;
            m_offgraphics = m_offscreen.getGraphics();
            m_offgraphics.setFont(m_font);
        }
        //m_offgraphics.setColor(SystemColor.control);
        //m_offgraphics.fillRect(0, 0, d.width, d.height);
        m_offgraphics.setColor(Color.black);
        FontMetrics fm = getFontMetrics(m_font);

        if (m_txt != null) {
            m_offgraphics.setColor(m_textColor);
            m_offgraphics.drawString(m_txt, padding, padding + fm.getAscent());
        }
        g.drawImage(m_offscreen, 0, 0, null);
    }

    public void setText(String t) {
        m_txt = t;
        repaint();
    }

    public void update(Graphics g) {
        paint(g);
    }

    Font m_font;
    String m_txt;
    int padding;
    Dimension m_offscreensize;
    Graphics m_offgraphics;
    Image m_offscreen;
    Color m_textColor;
}
