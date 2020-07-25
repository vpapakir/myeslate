package gr.cti.eslate.utils;

/*
 * @(#)EtchedBorder.java        1.14 00/02/02
 *
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of Sun Microsystems, Inc.
 * Use is subject to license terms.
 *
 */

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;
import javax.swing.border.*;

/**
 * A class which implements a simple etched border which can
 * either be etched-in or etched-out.  If no highlight/shadow
 * colors are initialized when the border is created, then
 * these colors will be dynamically derived from the background
 * color of the component argument passed into the paintBorder()
 * method.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @version 1.14 02/02/00
 * @author David Kloba
 * @author Amy Fowler
 */
public class ConfigurableEtchedBorder extends AbstractBorder
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /** Raised etched type. */
    public static final int RAISED  = 0;
    /** Lowered etched type. */
    public static final int LOWERED = 1;

    protected int etchType;
    protected Color highlight;
    protected Color shadow;
    protected boolean drawNorthBorderSide = true;
    protected boolean drawSouthBorderSide = true;
    protected boolean drawWestBorderSide = true;
    protected boolean drawEastBorderSide = true;

    /**
     * Creates a lowered etched border whose colors will be derived
     * from the background color of the component passed into
     * the paintBorder method.
     */
    public ConfigurableEtchedBorder()    {
        this(LOWERED);
    }

    /**
     * Creates a lowered etched border whose colors will be derived
     * from the background color of the component passed into
     * the paintBorder method.
     * @param drawNorthSide if the north side of the border will be painted
     * @param drawSouthSide if the south side of the border will be painted
     * @param drawWestSide if the west side of the border will be painted
     * @param drawEastSide if the east side of the border will be painted
     */
    public ConfigurableEtchedBorder(boolean drawNorthSide,
                                    boolean drawSouthSide,
                                    boolean drawWestSide,
                                    boolean drawEastSide)    {
        this(LOWERED, drawNorthSide, drawSouthSide, drawWestSide, drawEastSide);
    }

    /**
     * Creates an etched border with the specified etch-type
     * whose colors will be derived
     * from the background color of the component passed into
     * the paintBorder method.
     * @param etchType the type of etch to be drawn by the border
     */
    public ConfigurableEtchedBorder(int etchType)    {
        this(etchType, null, null);
    }

    /**
     * Creates an etched border with the specified etch-type
     * whose colors will be derived
     * from the background color of the component passed into
     * the paintBorder method.
     * @param etchType the type of etch to be drawn by the border
     * @param drawNorthSide if the north side of the border will be painted
     * @param drawSouthSide if the south side of the border will be painted
     * @param drawWestSide if the west side of the border will be painted
     * @param drawEastSide if the east side of the border will be painted
     */
    public ConfigurableEtchedBorder(int etchType,
                                    boolean drawNorthSide,
                                    boolean drawSouthSide,
                                    boolean drawWestSide,
                                    boolean drawEastSide)    {
        this(etchType, null, null, drawNorthSide, drawSouthSide, drawWestSide, drawEastSide);
    }

    /**
     * Creates a lowered etched border with the specified highlight and
     * shadow colors.
     * @param highlight the color to use for the etched highlight
     * @param shadow the color to use for the etched shadow
     */
    public ConfigurableEtchedBorder(Color highlight, Color shadow)    {
        this(LOWERED, highlight, shadow);
    }

    /**
     * Creates an etched border with the specified etch-type,
     * highlight and shadow colors.
     * @param etchType the type of etch to be drawn by the border
     * @param highlight the color to use for the etched highlight
     * @param shadow the color to use for the etched shadow
     */
    public ConfigurableEtchedBorder(int etchType, Color highlight, Color shadow)    {
        this.etchType = etchType;
        this.highlight = highlight;
        this.shadow = shadow;
    }

    /**
     * Creates an etched border with the specified etch-type,
     * highlight and shadow colors.
     * @param etchType the type of etch to be drawn by the border
     * @param highlight the color to use for the etched highlight
     * @param shadow the color to use for the etched shadow
     * @param drawNorthSide if the north side of the border will be painted
     * @param drawSouthSide if the south side of the border will be painted
     * @param drawWestSide if the west side of the border will be painted
     * @param drawEastSide if the east side of the border will be painted
     */
    public ConfigurableEtchedBorder(int etchType, Color highlight, Color shadow,
                        boolean drawNorthSide,
                        boolean drawSouthSide,
                        boolean drawWestSide,
                        boolean drawEastSide)    {
        this.etchType = etchType;
        this.highlight = highlight;
        this.shadow = shadow;
        this.drawNorthBorderSide = drawNorthSide;
        this.drawSouthBorderSide = drawSouthSide;
        this.drawWestBorderSide = drawWestSide;
        this.drawEastBorderSide = drawEastSide;
    }

    /**
     * Paints the border for the specified component with the
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int w = width;
        int h = height;

        g.translate(x, y);

        g.setColor(etchType == LOWERED? getShadowColor(c) : getHighlightColor(c));
//      g.drawRect(0, 0, w-2, h-2);
        if (drawNorthBorderSide) {
            int wt = w-2;
            if (!drawEastBorderSide)
                wt++;
            g.drawLine(0,0,wt,0);
        }
        if (drawSouthBorderSide) {
            int wt = w-2;
            if (!drawEastBorderSide)
                wt++;
            g.drawLine(0,h-2,wt,h-2);
        }
        if (drawEastBorderSide) {
            int ht = h-2;
            if (!drawNorthBorderSide)
                ht++;
            if (!drawSouthBorderSide)
                ht++;
            g.drawLine(w-2,0,w-2,ht);
        }
        if (drawWestBorderSide) {
            int ht = h-1; //h-2;
            if (!drawSouthBorderSide)
                ht++;
//            if (!drawNorthBorderSide)
//                ht++;
            g.drawLine(0,0,0,ht);
        }

        g.setColor(etchType == LOWERED? getHighlightColor(c) : getShadowColor(c));
        if (drawWestBorderSide) {
            int yorigin = h-3;
            int yorigin2 = 1;

            if (!drawSouthBorderSide)
                yorigin++;
            if (!drawNorthBorderSide) {
                yorigin++;
                yorigin2 = 0;
            }
            g.drawLine(1, yorigin, 1, yorigin2);
        }
        if (drawNorthBorderSide) {
            int wt = w-3;
            int xorigin = 1;
            if (!drawEastBorderSide)
                wt++;
            if (!drawWestBorderSide)
                xorigin--;
            g.drawLine(xorigin, 1, wt, 1);
        }
        if (drawSouthBorderSide)
            g.drawLine(0, h-1, w-1, h-1);
        if (drawEastBorderSide) {
            int yorigin = h-1;
            if (!drawSouthBorderSide)
                yorigin++;
            g.drawLine(w-1, yorigin, w-1, 0);
        }

        g.translate(-x, -y);
    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c)       {
        Insets insets = new Insets(0, 0, 0, 0);
        if (drawNorthBorderSide) insets.top = 2;
        if (drawSouthBorderSide) insets.bottom = 2;
        if (drawWestBorderSide) insets.left = 2;
        if (drawEastBorderSide) insets.right = 2;
        return insets;
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        if (drawNorthBorderSide) insets.top = 2; else insets.top = 0;
        if (drawSouthBorderSide) insets.bottom = 2; else insets.bottom = 0;
        if (drawWestBorderSide) insets.left = 2; else insets.left = 0;
        if (drawEastBorderSide) insets.right = 2; else insets.right = 0;
        return insets;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() { return true; }

    /**
     * Returns which etch-type is set on the etched border.
     */
    public int getEtchType() {
        return etchType;
    }

    /**
     * Returns the highlight color of the etched border
     * when rendered on the specified component.  If no highlight
     * color was specified at instantiation, the highlight color
     * is derived from the specified component's background color.
     * @param c the component for which the highlight may be derived
     */
    public Color getHighlightColor(Component c)   {
        return highlight != null? highlight :
                                       c.getBackground().brighter();
    }

    /**
     * Returns the highlight color of the etched border.
     * Will return null if no highlight color was specified
     * at instantiation.
     */
    public Color getHighlightColor()   {
        return highlight;
    }

    /**
     * Returns the shadow color of the etched border
     * when rendered on the specified component.  If no shadow
     * color was specified at instantiation, the shadow color
     * is derived from the specified component's background color.
     * @param c the component for which the shadow may be derived
     */
    public Color getShadowColor(Component c)   {
        return shadow != null? shadow : c.getBackground().darker();
    }

    /**
     * Returns the shadow color of the etched border.
     * Will return null if no shadow color was specified
     * at instantiation.
     */
    public Color getShadowColor()   {
        return shadow;
    }

}