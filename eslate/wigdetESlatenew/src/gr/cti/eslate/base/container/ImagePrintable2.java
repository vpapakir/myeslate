package gr.cti.eslate.base.container;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.JPanel;


public class ImagePrintable2 extends JPanel implements Printable {
//    BufferedImage[] images;
    BufferedImage image;
    public int maxNumPage = 1;

    public ImagePrintable2(BufferedImage img) {
        super();
        this.image = img;
    }

    public int print(Graphics pg, PageFormat pf, int pageIndex) {
        System.out.println("pageIndex: " + pageIndex) ;
        if (pageIndex >= maxNumPage || image == null)
            return NO_SUCH_PAGE;

        pg.translate((int) pf.getImageableX(), (int) pf.getImageableY());
        int wPage = (int) pf.getImageableWidth();
        int hPage = (int) pf.getImageableHeight();

        int w = image.getWidth(this);
        int h = image.getHeight(this);
        System.out.println("w: " + w + ", h: " + h);
        if (w == 0 || h == 0)
            return NO_SUCH_PAGE;
        int nCol = Math.max((int) Math.ceil((double) w/wPage), 1);
        int nRow = Math.max((int) Math.ceil((double)h/hPage), 1);
        maxNumPage = nCol*nRow;
        System.out.println("maxNumPage: " + maxNumPage);

        int iCol = pageIndex % nCol;
        int iRow = pageIndex / nCol;
        int x = iCol*wPage;
        int y = iRow*hPage;
        int wImage = Math.min(wPage, w-x);
        int hImage = Math.min(hPage, h-y);
        System.out.println("Source: " + x + ", " + y + ", " + (x+wImage) + ", " + (y+hImage));
        pg.drawImage(image, 0, 0, wImage, hImage, x, y, x+wImage, y+hImage, this);
        System.gc();

        return PAGE_EXISTS;
    }


/*    public int print(Graphics g, PageFormat pf, int pageIndex) {
        System.out.println("pageIndex: " + pageIndex) ;
        if (pageIndex < 0 || images == null || pageIndex >= images.length) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        Image img = images[pageIndex];
        g2d.drawImage(img, 0, 0, null);
        return PAGE_EXISTS;
    }
*/
}
