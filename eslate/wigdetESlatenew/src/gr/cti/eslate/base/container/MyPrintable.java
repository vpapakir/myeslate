package gr.cti.eslate.base.container;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.JComponent;


public class MyPrintable implements Printable {
    JComponent jcomp;

    public MyPrintable(JComponent c) {
        jcomp = c;
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
          System.out.println("pageIndex: " + pageIndex) ;
          if (pageIndex != 0) return NO_SUCH_PAGE;
          Graphics2D g2d = (Graphics2D) g;
          g2d.translate(pf.getImageableX(), pf.getImageableY());
          Rectangle2D outline = new Rectangle2D.Double(
              pf.getImageableX(), pf.getImageableY(),
              pf.getImageableWidth(), pf.getImageableHeight());
//          g2d.draw(outline);

          boolean wasBuffered = disableDoubleBuffering(jcomp);
          jcomp.print(g2d);
          restoreDoubleBuffering(jcomp, wasBuffered);
          return PAGE_EXISTS;
    }

    private boolean disableDoubleBuffering(JComponent c) {
        boolean wasBuffered = c.isDoubleBuffered();
        c.setDoubleBuffered(false);
        return wasBuffered;
    }

    private void restoreDoubleBuffering(JComponent c, boolean b) {
        c.setDoubleBuffered(b);
    }
}