package gr.cti.eslate.webWindow;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gr.cti.eslate.utils.*;


public class ImageButton extends NoBorderButton
    implements MouseListener {

    /*    public ImageButton()
     {
     m_font = new Font("Arial", 0, 12);
     m_image = null;
     m_bPressed = false;
     m_ae = new ActionEvent(this, 0, "");
     m_al = null;
     bHighlight = false;
     bCanHighlight = true;
     addMouseListener(this);
     padding = 5;
     }

     public ImageButton(Image image)
     {
     this(image, null);
     padding = 0;
     }

     public ImageButton(Image image, String txt)
     {
     this();
     m_text = txt;
     m_image = image;
     padding = 5;
     }

     public void addActionListener(ActionListener al)
     {
     m_al = al;
     }

     public Dimension getMinimumSize()
     {
     return getPreferredSize();
     }

     public Dimension getPreferredSize()
     {
     Dimension d = new Dimension(0, 0);
     if(m_image != null)
     {
     d.width = Math.max(0, m_image.getWidth(null)) ;
     d.height = Math.max(0, m_image.getHeight(null)) + 2 * padding;
     }
     if(m_text != null)
     {
     FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(m_font);
     d.height += fm.getHeight();
     d.width = Math.max(d.width, fm.stringWidth(m_text) + 2 * padding);
     }
     return d;
     }

     public void mouseClicked(MouseEvent e)
     {
     m_bPressed = false;
     repaint();
     }

     public void mouseEntered(MouseEvent e)
     {
     bHighlight = true;
     repaint();
     }

     public void mouseExited(MouseEvent e)
     {
     bHighlight = false;
     if(m_bPressed)
     m_bPressed = false;
     m_bPressed = false;
     repaint();
     }

     public void mousePressed(MouseEvent e)
     {
     m_bPressed = true;
     repaint();
     }

     public void mouseReleased(MouseEvent e)
     {
     boolean bPressed = m_bPressed;
     m_bPressed = false;
     repaint();
     if(m_al != null && bPressed)
     m_al.actionPerformed(m_ae);
     }

     public void paint(Graphics g)
     {
     update(g);
     }

     public void removeActionListener(ActionListener al)
     {
     if(m_al == al)
     m_al = null;
     }

     public void setImage(Image img)
     {
     m_image = img;
     }

     public void update(Graphics g)
     {
     Dimension dim = getSize();
     //g.setColor(SystemColor.control);
     //g.fillRect(0, 0, dim.width, dim.height);
     if(m_image != null)
     {
     int width = m_image.getWidth(this);
     int height = m_image.getHeight(this);
     int xOffset = (dim.width - 2 * padding - width) / 2;
     g.drawImage(m_image, padding + xOffset, padding, this);
     }
     g.setFont(m_font);
     g.setColor(Color.blue);
     FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(g.getFont());
     int yPos = padding + fm.getHeight();
     if(m_image != null)
     yPos += m_image.getHeight(this);
     if(m_text != null)
     {
     int xOffset = (dim.width - 2 * padding - fm.stringWidth(m_text)) / 2;
     g.drawString(m_text, padding + xOffset, yPos);
     }
     if(bCanHighlight && bHighlight)
     {
     if(m_bPressed)
     g.setColor(darkColor);
     else
     g.setColor(brightColor);
     g.drawLine(0, 0, dim.width - 1, 0);
     g.drawLine(0, 0, 0, dim.height - 1);
     if(m_bPressed)
     g.setColor(brightColor);
     else
     g.setColor(darkColor);
     g.drawLine(dim.width - 1, 0, dim.width - 1, dim.height - 1);
     g.drawLine(0, dim.height - 1, dim.width - 1, dim.height - 1);
     }
     }

     static Color brightColor;
     static Color darkColor;
     Font m_font;
     String m_text;
     Image m_image;
     boolean m_bPressed;
     ActionEvent m_ae;
     ActionListener m_al;
     int padding;
     boolean bHighlight;
     boolean bCanHighlight;

     static
     {
     brightColor = Color.decode("#dcdcdc");
     darkColor = brightColor.darker().darker();
     }
     */

    static Color brightColor;
    static Color darkColor;
    Font m_font;
    String m_text;
    ImageIcon m_icon;
    boolean m_bPressed;
    ActionEvent m_ae;
    ActionListener m_al;
    int padding;
    boolean bHighlight;
    boolean bCanHighlight;

    static {
        brightColor = Color.decode("#dcdcdc");
        darkColor = brightColor.darker().darker();
    }
    public ImageButton() {
        setMargin(new Insets(1, 1, 1, 1));
        setFont(new Font("Arial", 0, 12));
        m_font = new Font("Arial", 0, 12);
        m_icon = null;
        m_bPressed = false;
        m_ae = new ActionEvent(this, 0, "");
        m_al = null;
        bHighlight = false;
        bCanHighlight = true;
        addMouseListener(this);
        padding = 5;
    }

    public ImageButton(ImageIcon icon) {
        this(icon, null);
        padding = 0;
    }

    public ImageButton(ImageIcon icon, String txt) {
        this();
        m_text = txt;
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setText(txt);
        setIcon(icon);
        m_icon = icon;
        padding = 5;
    }

    public Dimension getPreferredSize() {
        Dimension d = new Dimension(0, 0);

        if (m_icon != null) {
            d.width = Math.max(0, m_icon.getImage().getWidth(null)) + 4 * padding;
            d.height = Math.max(0, m_icon.getImage().getHeight(null)) + 2 * padding;
        }
        if (m_text != null) {
            FontMetrics fm = getFontMetrics(m_font);

            d.height += fm.getHeight();
            d.width = fm.stringWidth(m_text) + 2 * padding;
        }
        return d;
    }

    public void addActionListener(ActionListener al) {
        m_al = al;
    }

    public void mouseClicked(MouseEvent e) {
        m_bPressed = false;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
        bHighlight = true;
        repaint();
    }

    public void mouseExited(MouseEvent e) {
        bHighlight = false;
        if (m_bPressed)
            m_bPressed = false;
        m_bPressed = false;
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        m_bPressed = true;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        boolean bPressed = m_bPressed;

        m_bPressed = false;
        repaint();
        if (m_al != null && bPressed)
            m_al.actionPerformed(m_ae);
    }

    public void removeActionListener(ActionListener al) {
        if (m_al == al)
            m_al = null;
    }

    public void setImageIcon(ImageIcon icon) {
        m_icon = icon;
    }

}
