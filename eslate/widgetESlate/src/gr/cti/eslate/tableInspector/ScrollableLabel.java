package gr.cti.eslate.tableInspector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ScrollableLabel extends JComponent implements SwingConstants {
    private JLabel label;
    private Arrow east,west;
    private int xPos;
    private int halignment;

    public ScrollableLabel() {
        super();
        setLayout(null);
        setOpaque(true);

        xPos=0;
        halignment=LEFT;

        east=new Arrow(EAST);
        east.setVisible(false);
        west=new Arrow(WEST);
        west.setVisible(false);

        add(east);
        add(west);

        label=new JLabel() {
            public Color getForeground() {
                try {
                    return ScrollableLabel.this.getForeground();
                } catch(NullPointerException ex) {/*On instantiation*/}
                return null;
            }
        };
        add(label);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Insets in=getInsets();
                east.setLocation(getWidth()-east.getWidth()-in.right,(getHeight()-east.getHeight())/2);
                west.setLocation(in.left,(getHeight()-east.getHeight())/2);
                checkFit();
                label.setLocation(label.getX(),(getHeight()-label.getPreferredSize().height)/2);
            }
        });

        addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            public void ancestorResized(HierarchyEvent e) {
                Insets in=getInsets();
                east.setLocation(getWidth()-east.getWidth()-in.right,(getHeight()-east.getHeight())/2);
                west.setLocation(in.left,(getHeight()-east.getHeight())/2);
                checkFit();
                label.setLocation(label.getX(),(getHeight()-label.getPreferredSize().height)/2);
            }
        });
    }

    public void setText(String t) {
        label.setText(t);
        revalidate();
        checkFit();
    }

    public Dimension getPreferredSize() {
        Dimension d=super.getPreferredSize();
        if (label.getPreferredSize().height>d.height)
            d.height=label.getPreferredSize().height;
        return d;
    }

    public void setHorizontalAlignment(int al) {
        label.setHorizontalAlignment(al);
        halignment=al;
        checkFit();
    }

    public int getHorizontalAlignment() {
        return label.getHorizontalAlignment();
    }

    public void setVerticalAlignment(int al) {
        label.setVerticalAlignment(al);
    }

    public void setIcon(Icon ic) {
        label.setIcon(ic);
        revalidate();
        checkFit();
    }

    public String getText() {
        return label.getText();
    }

    public void setFont(Font f) {
        if (label!=null)
            label.setFont(f);
        checkFit();
    }

    private void checkFit() {
        //This happens upon initialization
        if (east==null || west==null)
            return;
        if (label.getPreferredSize().width>getAvailableTextWidth()) {
            if (halignment==LEFT) {
                east.setVisible(true);
                west.setVisible(false);
                xPos=0;
            } else {
                east.setVisible(false);
                west.setVisible(true);
                xPos=getAvailableTextWidth()-label.getPreferredSize().width;
            }
        } else {
            if (halignment==LEFT)
                xPos=0;
            else
                xPos=getAvailableTextWidth()-label.getPreferredSize().width;
            east.setVisible(false);
            west.setVisible(false);
        }
        revalidate();
    }

    private int getAvailableTextWidth() {
        Insets in=getInsets();
        return getWidth()-in.left-in.right;
    }

    public void revalidate() {
        Dimension s=label.getPreferredSize();
        label.setBounds(getInsets().left+xPos,(getHeight()-s.height)/2,s.width,s.height);
        super.revalidate();
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        label.setEnabled(b);
    }

    public void paintComponent(Graphics g) {
        //Fill the background
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0,0,getWidth(),getHeight());
        }
        //Paint the border
        if (getBorder()!=null)
            getBorder().paintBorder(this,g,0,0,getWidth(),getHeight());
        //Paint the content
        Insets in=getInsets();
        g.clipRect(in.left,in.top,getWidth()-in.left-in.right,getHeight()-in.top-in.bottom);
        super.paintComponent(g);
    }

    private class Arrow extends JComponent {
        private int direction;
        private GeneralPath arrow;
        private boolean stopThread;
        boolean armed;

        Arrow(int dir) {
            this.direction=dir;
            setSize(7,9);
            armed=false;

            arrow=new GeneralPath();
            if (direction==EAST) {
                arrow.moveTo(0,0);
                arrow.lineTo(0,8);
                arrow.lineTo(6,4);
                arrow.lineTo(0,0);
            } else if (direction==WEST) {
                arrow.moveTo(6,0);
                arrow.lineTo(6,8);
                arrow.lineTo(0,4);
                arrow.lineTo(6,0);
            }

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    Thread thread=null;
                    if (direction==EAST)
                        thread=new EastThread();
                    else
                        thread=new WestThread();
                    armed=true;
                    repaint();
                    stopThread=false;
                    thread.start();
                }
                public void mouseExited(MouseEvent e) {
                    stopThread=true;
                    armed=false;
                    repaint();
                }
                public void mousePressed(MouseEvent e) {
                    stopThread=true;
                    if (direction==EAST) {
                        east.setVisible(false);
                        xPos=getAvailableTextWidth()-label.getPreferredSize().width;
                    } else {
                        west.setVisible(false);
                        xPos=0;
                    }
                    ScrollableLabel.this.revalidate();
                }
            });
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            if (armed) {
                g2.setColor(Color.black);
                g2.fill(arrow);
                g2.setColor(Color.lightGray);
                g2.draw(arrow);
            } else {
                g2.setColor(Color.white);
                g2.fill(arrow);
                g2.setColor(Color.black);
                g2.draw(arrow);
            }
        }

        private class EastThread extends Thread {
            public void run() {
                int limit;
                limit=-(label.getPreferredSize().width-getAvailableTextWidth());

                while (!stopThread) {
                    if (xPos<=limit) {
                        stopThread=true;
                        armed=false;
                        east.setVisible(false);
                        ScrollableLabel.this.repaint();
                        continue;
                    }
                    xPos--;
                    west.setVisible(true);
                    ScrollableLabel.this.revalidate();
                    try {
                        sleep(5);
                    } catch(InterruptedException ex) {}
                }
            }
        }

        private class WestThread extends Thread {
            public void run() {
                int limit;
                limit=0;

                while (!stopThread) {
                    if (xPos>=limit) {
                        stopThread=true;
                        armed=false;
                        west.setVisible(false);
                        ScrollableLabel.this.repaint();
                        continue;
                    }
                    xPos++;
                    east.setVisible(true);
                    ScrollableLabel.this.revalidate();
                    try {
                        sleep(5);
                    } catch(InterruptedException ex) {}
                }
            }
        }
    }
}