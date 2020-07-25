/* A JPopup menu that does not extend beyond the end of the screen.
 */

package gr.cti.eslate.base.container;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPopupMenu;


public class ESlatePopupMenu extends JPopupMenu {
    Point screenLoc = null;
    Point originLoc = null;

    public void show(Component invoker, int x, int y) {
        pack();
        originLoc = new Point(x, y);

        Dimension popupSize = getPreferredSize();
//        System.out.println("popupSize: " + popupSize);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Container topMostCntainer = getTopLevelAncestor();
        Point p = new Point(x, y);
        javax.swing.SwingUtilities.convertPointToScreen(p, invoker);

//        System.out.println("Previous --> x: " + x + ", y: " + y);
//        System.out.println("Point p: " + p);
//        System.out.println("screensize: " + screensize);
        if (p.x + popupSize.width >  screensize.width) {
            x = x - popupSize.width;
            p.x = p.x - popupSize.width;
        }

        if (p.y + popupSize.height >  screensize.height) {
            y = y - popupSize.height;
            p.y = p.y - popupSize.height;
        }

//        System.out.println("After --> x: " + x + ", y: " + y);
        screenLoc = p;
        super.show(invoker, x, y);
    }
}
