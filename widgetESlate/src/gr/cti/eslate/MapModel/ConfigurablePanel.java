package gr.cti.eslate.mapModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ConfigurablePanel extends JPanel {
    private Color color;
    private boolean opaque, stretch, tile, borderPainted, contentsOpaque, contentsBorderPainted, contentsFocusPainted;
    private ImageIcon backImage, stretchImage, tileImage;
    private Border border;

    public ConfigurablePanel() {
        opaque=getOpaque();
        contentsOpaque=contentsBorderPainted=contentsFocusPainted=true;
        stretch=tile=false;
        border=getBorder();

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (backImage==null) return;
                //Recreate the Stretch and Tile Images
                if (stretchImage!=null) {
                    stretchImage.getImage().flush();
                    stretchImage=new ImageIcon(backImage.getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH));
                }
                if (tileImage!=null) {
                    tileImage.getImage().flush();
                    Image ti=ConfigurablePanel.this.createImage(getWidth(),getHeight());
                    Graphics ig=ti.getGraphics();
                    int w=getWidth(); int iw=backImage.getIconWidth();
                    int h=getHeight(); int ih=backImage.getIconHeight();
                    for (int j=0;j<h;j=j+ih)
                        for (int i=0;i<w;i=i+iw)
                            backImage.paintIcon(ConfigurablePanel.this,ig,i,j);
                    tileImage=new ImageIcon(ti);
                }
            }
        });
    }

    public void setOpaque(boolean op) {
        opaque=op;
        super.setOpaque(op);
        repaint();
    }

    public boolean getOpaque() {
        return super.isOpaque();
    }

    public void setBackgroundImage(ImageIcon ii) {
        backImage=ii;
        repaint();
    }

    public ImageIcon getBackgroundImage() {
        return backImage;
    }

    public void setBackgroundColor(Color c) {
        super.setBackground(c);
    }

    public Color getBackgroundColor() {
        return super.getBackground();
    }

    public void setStretchBackground(boolean s) {
        stretch=s;
        if (s==false) {
            if (stretchImage!=null)
                stretchImage.getImage().flush();
            stretchImage=null;
        }
        repaint();
    }

    public boolean getStretchBackground() {
        return stretch;
    }

    public void setTiledBackground(boolean t) {
        tile=t;
        if (t==false) {
            if (tileImage!=null)
                tileImage.getImage().flush();
            tileImage=null;
        }
        repaint();
    }

    public boolean getTiledBackground() {
        return tile;
    }

    public void setBorder(Border b) {
        border=b;
        super.setBorder(b);
    }

    public Border getBorder() {
        return border;
    }

    public void setBorderPainted(boolean b) {
        borderPainted=b;
        if (b)
            setBorder(border);
        else
            setBorder(null);
    }

    public boolean getBorderPainted() {
        return borderPainted;
    }

    public void setContentsOpaque(boolean op) {
        contentsOpaque=op;
        int cc=getComponentCount();
        Component c;
        for (int i=0;i<cc;i++) {
            c=getComponent(i);
            if (c instanceof AbstractButton) {
                ((AbstractButton) c).setOpaque(op);
                ((AbstractButton) c).repaint();
            }
        }
    }

    public boolean getContentsOpaque() {
        return contentsOpaque;
    }

    public void setContentsBorderPainted(boolean op) {
        contentsBorderPainted=op;
        int cc=getComponentCount();
        Component c;
        for (int i=0;i<cc;i++) {
            c=getComponent(i);
            if (c instanceof AbstractButton) {
                ((AbstractButton) c).setBorderPainted(op);
                ((AbstractButton) c).repaint();
            }
        }
    }

    public boolean getContentsBorderPainted() {
        return contentsBorderPainted;
    }

    public void setContentsFocusPainted(boolean op) {
        contentsFocusPainted=op;
        int cc=getComponentCount();
        Component c;
        for (int i=0;i<cc;i++) {
            c=getComponent(i);
            if (c instanceof AbstractButton) {
                ((AbstractButton) c).setFocusPainted(op);
                ((AbstractButton) c).repaint();
            }
        }
    }

    public boolean getContentsFocusPainted() {
        return contentsFocusPainted;
    }

    public Component add(Component c) {
        super.add(c);
        arrangeNew(c);
        return c;
    }

    public void add(Component c,Object con) {
        super.add(c,con);
        arrangeNew(c);
    }

    private void arrangeNew(Component c) {
        if ((c instanceof AbstractButton)) {
            ((JComponent)c).setOpaque(contentsOpaque);
            if (c instanceof AbstractButton) {
                ((AbstractButton)c).setBorderPainted(contentsBorderPainted);
                ((AbstractButton)c).setFocusPainted(contentsFocusPainted);
            }
        }
    }

    public void paint(Graphics g) {
        if (backImage!=null) {
            if (stretch) {
                if (stretchImage==null)
                    stretchImage=new ImageIcon(backImage.getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH));
                stretchImage.paintIcon(this,g,0,0);
            } else if (tile) {
                if (tileImage==null) {
                    Image ti=this.createImage(getWidth(),getHeight());
                    Graphics ig=ti.getGraphics();
                    int w=getWidth(); int iw=backImage.getIconWidth();
                    int h=getHeight(); int ih=backImage.getIconHeight();
                    for (int j=0;j<h;j=j+ih)
                        for (int i=0;i<w;i=i+iw)
                            backImage.paintIcon(this,ig,i,j);
                    tileImage=new ImageIcon(ti);
                }
                tileImage.paintIcon(this,g,0,0);
            } else {
                backImage.paintIcon(this,g,0,0);
            }
        }

        super.paint(g);
    }
}
