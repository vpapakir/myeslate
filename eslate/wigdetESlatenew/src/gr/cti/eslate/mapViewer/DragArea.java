package gr.cti.eslate.mapViewer;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

class DragArea extends JPanel {
    private ActionListener listener;
    static Cursor normCursor,presCursor;
    private static ImageIcon updown;

    DragArea() {
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        normCursor=MapViewer.createCustomCursor("images/pancursorhand.gif",new Point(7,7),new Point(15,9),"pancursorhand");
        presCursor=MapViewer.createCustomCursor("images/grabhand.gif",new Point(7,7),new Point(15,9),"grabhand");

        setCursor(normCursor);
        if (updown==null)
            updown=Helpers.loadImageIcon("images/updown.gif");

        MouseInputListener mil=new MouseInputAdapter() {
            int prevpos;
            private Cursor prevCursor;
            public void mouseDragged(MouseEvent e) {
                setCursor(presCursor);
                Container element=getParent();
                if (element==null)
                    return;
                Container parent=element.getParent();
                if (parent==null)
                    return;
                Point p=SwingUtilities.convertPoint(DragArea.this,e.getX(),e.getY(),parent);
                Component sc;
                if (parent.contains(p) && (sc=parent.getComponentAt(p))!=null) {
                    if (sc!=element && (sc.getClass().equals(element.getClass()))) {
                        Component[] c=parent.getComponents();
                        //Find the indices of the swapping components,
                        //to know if the component is ascending or descending
                        int ei=0,sci=0;
                        for (int i=0;i<c.length;i++) {
                            if (element==c[i])
                                ei=i;
                            if (sc==c[i])
                                sci=i;
                        }
                        parent.removeAll();
                        for (int i=0;i<c.length;i++) {
                            //Skip the parent of this drag area
                            if (c[i]==element)
                                continue;
                            //Descending
                            if (ei>sci && c[i]==sc)
                                parent.add(element);
                            parent.add(c[i]);
                            //Ascending
                            if (ei<sci && c[i]==sc)
                                parent.add(element);
                        }
                        parent.invalidate();
                        parent.validate();
                        parent.repaint();
                    }
                } else
                    Toolkit.getDefaultToolkit().beep();
            }

            public void mousePressed(MouseEvent e) {
                setCursor(presCursor);
                prevpos=Integer.MAX_VALUE;
                Container element=getParent();
                if (element==null)
                    return;
                Container parent=element.getParent();
                if (parent==null)
                    return;
                prevCursor=element.getCursor();
                element.setCursor(presCursor);
                ((CategoryPane) parent).isDragging=true;
                Component[] c=parent.getComponents();
                for (int i=0;i<c.length;i++)
                    if (c[i]==element) {
                        prevpos=i;
                        break;
                    }
            }

            public void mouseReleased(MouseEvent e) {
                setCursor(normCursor);
                Container element=getParent();
                if (element==null)
                    return;
                Container parent=element.getParent();
                if (parent==null)
                    return;
                element.setCursor(prevCursor);
                Component[] c=parent.getComponents();
                for (int i=0;i<c.length;i++)
                    if (c[i]==element) {
                        if (prevpos!=i) {
                            StringBuffer s=new StringBuffer();
                            for (int j=0;j<c.length;j++) {
                                if (j==prevpos)
                                    continue;
                                else if (j==i && prevpos<i) {
                                    s.append(j);
                                    s.append(" ");
                                    s.append(prevpos);
                                    s.append(" ");
                                } else if (j==i && prevpos>i) {
                                    s.append(prevpos);
                                    s.append(" ");
                                    s.append(j);
                                    s.append(" ");
                                } else {
                                    s.append(j);
                                    s.append(" ");
                                }
                            }
                            //The action command of event is a string defining the positions of the layers
                            listener.actionPerformed(new ActionEvent(element,ActionEvent.ACTION_PERFORMED,new String(s)));
                        }
                        break;
                    }
                ((CategoryPane) parent).isDragging=false;
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                setCursor(normCursor);
            }
        };

        addMouseListener(mil);
        //The mouse listener that makes the container holding this area to move
        addMouseMotionListener(mil);
    }

    public void addActionListener(ActionListener l) {
        listener=l;
    }

    public void removeActionListener(ActionListener l) {
        listener=null;
    }

    private void jbInit() throws Exception {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updown.paintIcon(this,g,1,getHeight()-updown.getIconHeight()-2);
    }
}