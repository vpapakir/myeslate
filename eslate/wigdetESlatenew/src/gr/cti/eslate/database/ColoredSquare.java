package gr.cti.eslate.database;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;


class ColoredSquare implements Icon {
  	Color color;

    private static ColoredSquare whiteColoredSquare = null;
    private static ColoredSquare darkGrayColoredSquare = null;
    private static ColoredSquare grayColoredSquare = null;
    private static ColoredSquare lightGrayColoredSquare = null;
    private static ColoredSquare color128ColoredSquare = null;
    private static ColoredSquare blueColoredSquare = null;
    private static ColoredSquare cyanColoredSquare = null;
    private static ColoredSquare greenColoredSquare = null;
    private static ColoredSquare magentaColoredSquare = null;
    private static ColoredSquare orangeColoredSquare = null;
    private static ColoredSquare pinkColoredSquare = null;
    private static ColoredSquare redColoredSquare = null;
    private static ColoredSquare yellowColoredSquare = null;
    private static ColoredSquare blackColoredSquare = null;

    public ColoredSquare(Color c) {
        this.color = c;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color oldColor = g.getColor();
   	    g.setColor(color);
        g.fill3DRect(x,y+2,getIconWidth(), getIconHeight()-4, true);
        g.setColor(oldColor);
    }

    public int getIconWidth() { return 40; }
   	public int getIconHeight() { return 16; }

   	public boolean equals(Object x) {
   	    if (!(this.getClass().isInstance(x)))
   	        return false;
   	    if (color.equals(((ColoredSquare) x).color))
   	        return true;
   	    return false;
   	}


    public Color getColor() {
        return color;
    }

    public static ColoredSquare getWhiteColoredSquare() {
        if (whiteColoredSquare == null)
            whiteColoredSquare = new ColoredSquare(Color.white);
        return whiteColoredSquare;
    }

    public static ColoredSquare getDarkGrayColoredSquare() {
        if (darkGrayColoredSquare == null)
            darkGrayColoredSquare = new ColoredSquare(Color.darkGray);
        return darkGrayColoredSquare;
    }

    public static ColoredSquare getGrayColoredSquare() {
        if (grayColoredSquare == null)
            grayColoredSquare = new ColoredSquare(Color.gray);
        return grayColoredSquare;
    }

    public static ColoredSquare getLightGrayColoredSquare() {
        if (lightGrayColoredSquare == null)
            lightGrayColoredSquare = new ColoredSquare(Color.lightGray);
        return lightGrayColoredSquare;
    }

    public static ColoredSquare getColor128ColoredSquare() {
        if (color128ColoredSquare == null)
            color128ColoredSquare = new ColoredSquare(new Color(0,0,128));
        return color128ColoredSquare;
    }

    public static ColoredSquare getBlueColoredSquare() {
        if (blueColoredSquare == null)
            blueColoredSquare = new ColoredSquare(Color.blue);
        return blueColoredSquare;
    }

    public static ColoredSquare getCyanColoredSquare() {
        if (cyanColoredSquare == null)
            cyanColoredSquare = new ColoredSquare(Color.cyan);
        return cyanColoredSquare;
    }

    public static ColoredSquare getGreenColoredSquare() {
        if (greenColoredSquare == null)
            greenColoredSquare = new ColoredSquare(Color.green);
        return greenColoredSquare;
    }

    public static ColoredSquare getMagentaColoredSquare() {
        if (magentaColoredSquare == null)
            magentaColoredSquare = new ColoredSquare(Color.magenta);
        return magentaColoredSquare;
    }

    public static ColoredSquare getOrangeColoredSquare() {
        if (orangeColoredSquare == null)
            orangeColoredSquare = new ColoredSquare(Color.orange);
        return orangeColoredSquare;
    }

    public static ColoredSquare getPinkColoredSquare() {
        if (pinkColoredSquare == null)
            pinkColoredSquare = new ColoredSquare(Color.pink);
        return pinkColoredSquare;
    }

    public static ColoredSquare getRedColoredSquare() {
        if (redColoredSquare == null)
            redColoredSquare = new ColoredSquare(Color.red);
        return redColoredSquare;
    }

    public static ColoredSquare getYellowColoredSquare() {
        if (yellowColoredSquare == null)
            yellowColoredSquare = new ColoredSquare(Color.yellow);
        return yellowColoredSquare;
    }

    public static ColoredSquare getBlackColoredSquare() {
        if (blackColoredSquare == null)
            blackColoredSquare = new ColoredSquare(Color.black);
        return blackColoredSquare;
    }


}
