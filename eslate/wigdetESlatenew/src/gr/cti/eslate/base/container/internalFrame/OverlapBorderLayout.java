package gr.cti.eslate.base.container.internalFrame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * A border layout manager which can be configured to change the default BorderLayout
 * layout of the panels.
 * <p>
 * The <code>setNorthOverWest(), setEastOverNorth(), setSouthOverEast() and setWestOverSouth()
 * </code> methods are those that do the configuration. To understand their use, bare in
 * mind that the default BorderLayout has <code>setNorthOverWest(true), setEastOverNorth(false),
 * setSouthOverEast(true) and setWestOverSouth(false)</code>.
 * @author  Giorgos Vasiliou
 * @version 1.0
 */

public class OverlapBorderLayout implements LayoutManager2, java.io.Serializable {
    private boolean now,eon,soe,wos;
     /**
     * Constructs a border layout with the horizontal gaps
     * between components.
     * The horizontal gap is specified by <code>hgap</code>.
     *
     * @see #getHgap
     * @see #setHgap
     *
     * @serial
     */
	int hgap;

	/**
     * Constructs a border layout with the vertical gaps
     * between components.
     * The vertical gap is specified by <code>vgap</code>.
     *
     * @see #getVgap
     * @see #setVgap
	 * @serial
     */
	int vgap;

    /**
     * Constant to specify components location to be the
     *      north portion of the border layout.
     * @serial
     * @see #getChild
     * @see #addLayoutComponent
     * @see #removeLayoutComponent
     */
	Component north;
     /**
     * Constant to specify components location to be the
     *      west portion of the border layout.
     * @serial
     * @see #getChild
     * @see #addLayoutComponent
     * @see #removeLayoutComponent
     */
	Component west;
    /**
     * Constant to specify components location to be the
     *      east portion of the border layout.
     * @serial
     * @see #getChild
     * @see #addLayoutComponent
     * @see #removeLayoutComponent
     */
	Component east;
    /**
     * Constant to specify components location to be the
     *      south portion of the border layout.
     * @serial
     * @see #getChild
     * @see #addLayoutComponent
     * @see #removeLayoutComponent
     */
    Component south;
    /**
     * Constant to specify components location to be the
     *      center portion of the border layout.
     * @serial
     * @see #getChild
     * @see #addLayoutComponent
     * @see #removeLayoutComponent
     */
	Component center;

    /**
     *
     * A relative positioning constant, that can be used instead of
     * north, south, east, west or center.
     * mixing the two types of constants can lead to unpredicable results.  If
     * you use both types, the relative constants will take precedence.
     * For example, if you add components using both the <code>NORTH</code>
     * and <code>BEFORE_FIRST_LINE</code> constants in a container whose
     * orientation is <code>LEFT_TO_RIGHT</code>, only the
     * <code>BEFORE_FIRST_LINE</code> will be layed out.
     * This will be the same for lastLine, firstItem, lastItem.
     * @serial
     */
    Component firstLine;
     /**
     * A relative positioning constant, that can be used instead of
     * north, south, east, west or center.
     * Please read Description for firstLine.
     * @serial
     */
	Component lastLine;
     /**
     * A relative positioning constant, that can be used instead of
     * north, south, east, west or center.
     * Please read Description for firstLine.
     * @serial
     */
	Component firstItem;
    /**
     * A relative positioning constant, that can be used instead of
     * north, south, east, west or center.
     * Please read Description for firstLine.
     * @serial
     */
	Component lastItem;

    /**
     * The north layout constraint (top of container).
     */
    public static final String NORTH  = "North";

    /**
     * The south layout constraint (bottom of container).
     */
    public static final String SOUTH  = "South";

    /**
     * The east layout constraint (right side of container).
     */
    public static final String EAST   = "East";

    /**
     * The west layout constraint (left side of container).
     */
    public static final String WEST   = "West";

    /**
     * The center layout constraint (middle of container).
     */
    public static final String CENTER = "Center";

    /**
     * The component comes before the first line of the layout's content.
     * For Western, top-to-bottom, left-to-right orientations, this is
     * equivalent to NORTH.
     *
     * @see java.awt.Component#getComponentOrientation
     * @since 1.2
     */
    public static final String BEFORE_FIRST_LINE = "First";

    /**
     * The component comes after the last line of the layout's content.
     * For Western, top-to-bottom, left-to-right orientations, this is
     * equivalent to SOUTH.
     *
     * @see java.awt.Component#getComponentOrientation
     * @since 1.2
     */
    public static final String AFTER_LAST_LINE = "Last";

    /**
     * The component goes at the beginning of the line direction for the
     * layout. For Western, top-to-bottom, left-to-right orientations,
     * this is equivalent to WEST.
     *
     * @see java.awt.Component#getComponentOrientation
     * @since 1.2
     */
    public static final String BEFORE_LINE_BEGINS = "Before";

    /**
     * The component goes at the end of the line direction for the
     * layout. For Western, top-to-bottom, left-to-right orientations,
     * this is equivalent to EAST.
     *
     * @see java.awt.Component#getComponentOrientation
     * @since 1.2
     */
    public static final String AFTER_LINE_ENDS = "After";

    /*
     * JDK 1.1 serialVersionUID
     */
     private static final long serialVersionUID = -19991113L;

    /**
     * Constructs a new border layout with
     * no gaps between components. The default layout of panes is used.
     */
    public OverlapBorderLayout() {
	this(0, 0);
    }

    /**
     * Constructs a new border layout with
     * no gaps between components. The XoY arguments show their layout, e.g. <code>now</code>
     * is North Over West.
     */
    public OverlapBorderLayout(boolean now,boolean eon,boolean soe,boolean wos) {
	this(0, 0);
        this.now=now;
        this.eon=eon;
        this.soe=soe;
        this.wos=wos;
    }

    /**
     * Constructs a border layout with the specified gaps
     * between components. The default layout of panes is used.
     * The horizontal gap is specified by <code>hgap</code>
     * and the vertical gap is specified by <code>vgap</code>.
     * @param   hgap   the horizontal gap.
     * @param   vgap   the vertical gap.
     */
    public OverlapBorderLayout(int hgap, int vgap) {
	this.hgap = hgap;
	this.vgap = vgap;
        now=soe=true;
        wos=eon=false;
    }

    /**
     * Returns the horizontal gap between components.
     * @since   JDK1.1
     */
    public int getHgap() {
	return hgap;
    }

    /**
     * Sets the horizontal gap between components.
     * @param hgap the horizontal gap between components
     * @since   JDK1.1
     */
    public void setHgap(int hgap) {
	this.hgap = hgap;
    }

    /**
     * Returns the vertical gap between components.
     * @since   JDK1.1
     */
    public int getVgap() {
	return vgap;
    }

    /**
     * Sets the vertical gap between components.
     * @param vgap the vertical gap between components
     * @since   JDK1.1
     */
    public void setVgap(int vgap) {
	this.vgap = vgap;
    }

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.  For border layouts, the constraint must be
     * one of the following constants:  <code>NORTH</code>,
     * <code>SOUTH</code>, <code>EAST</code>,
     * <code>WEST</code>, or <code>CENTER</code>.
     * <p>
     * Most applications do not call this method directly. This method
     * is called when a component is added to a container using the
     * <code>Container.add</code> method with the same argument types.
     * @param   comp         the component to be added.
     * @param   constraints  an object that specifies how and where
     *                       the component is added to the layout.
     * @see     java.awt.Container#add(java.awt.Component, java.lang.Object)
     * @exception   IllegalArgumentException  if the constraint object is not
     *                 a string, or if it not one of the five specified
	 *              constants.
     * @since   JDK1.1
     */
    public void addLayoutComponent(Component comp, Object constraints) {
      synchronized (comp.getTreeLock()) {
	if ((constraints == null) || (constraints instanceof String)) {
	    addLayoutComponent((String)constraints, comp);
	} else {
	    throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
	}
      }
    }

    /**
     * @deprecated  replaced by <code>addLayoutComponent(Component, Object)</code>.
     */
    public void addLayoutComponent(String name, Component comp) {
      synchronized (comp.getTreeLock()) {
	/* Special case:  treat null the same as "Center". */
	if (name == null) {
	    name = "Center";
	}

	/* Assign the component to one of the known regions of the layout.
	 */
	if ("Center".equals(name)) {
	    center = comp;
	} else if ("North".equals(name)) {
	    north = comp;
	} else if ("South".equals(name)) {
	    south = comp;
	} else if ("East".equals(name)) {
	    east = comp;
	} else if ("West".equals(name)) {
	    west = comp;
	} else if (BEFORE_FIRST_LINE.equals(name)) {
	    firstLine = comp;
	} else if (AFTER_LAST_LINE.equals(name)) {
	    lastLine = comp;
	} else if (BEFORE_LINE_BEGINS.equals(name)) {
	    firstItem = comp;
	} else if (AFTER_LINE_ENDS.equals(name)) {
	    lastItem = comp;
	} else {
	    throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
	}
      }
    }

    /**
     * Removes the specified component from this border layout. This
     * method is called when a container calls its <code>remove</code> or
     * <code>removeAll</code> methods. Most applications do not call this
     * method directly.
     * @param   comp   the component to be removed.
     * @see     java.awt.Container#remove(java.awt.Component)
     * @see     java.awt.Container#removeAll()
     */
    public void removeLayoutComponent(Component comp) {
      synchronized (comp.getTreeLock()) {
	if (comp == center) {
	    center = null;
	} else if (comp == north) {
	    north = null;
	} else if (comp == south) {
	    south = null;
	} else if (comp == east) {
	    east = null;
	} else if (comp == west) {
	    west = null;
	}
	if (comp == firstLine) {
	    firstLine = null;
	} else if (comp == lastLine) {
	    lastLine = null;
	} else if (comp == firstItem) {
	    firstItem = null;
	} else if (comp == lastItem) {
	    lastItem = null;
	}
      }
    }

    /**
     * Determines the minimum size of the <code>target</code> container
     * using this layout manager.
     * <p>
     * This method is called when a container calls its
     * <code>getMinimumSize</code> method. Most applications do not call
     * this method directly.
     * @param   target   the container in which to do the layout.
     * @return  the minimum dimensions needed to lay out the subcomponents
     *          of the specified container.
     * @see     java.awt.Container
     * @see     java.awt.BorderLayout#preferredLayoutSize
     * @see     java.awt.Container#getMinimumSize()
     */
    public Dimension minimumLayoutSize(Container target) {
      synchronized (target.getTreeLock()) {
	Dimension dim = new Dimension(0, 0);

        boolean ltr = target.getComponentOrientation().isLeftToRight();
        Component c = null;

	if ((c=getChild(EAST,ltr)) != null) {
	    Dimension d = c.getMinimumSize();
	    dim.width += d.width + hgap;
	    dim.height = Math.max(d.height, dim.height);
	}
	if ((c=getChild(WEST,ltr)) != null) {
	    Dimension d = c.getMinimumSize();
	    dim.width += d.width + hgap;
	    dim.height = Math.max(d.height, dim.height);
	}
	if ((c=getChild(CENTER,ltr)) != null) {
	    Dimension d = c.getMinimumSize();
	    dim.width += d.width;
	    dim.height = Math.max(d.height, dim.height);
	}
	if ((c=getChild(NORTH,ltr)) != null) {
	    Dimension d = c.getMinimumSize();
	    dim.width = Math.max(d.width, dim.width);
	    dim.height += d.height + vgap;
	}
	if ((c=getChild(SOUTH,ltr)) != null) {
	    Dimension d = c.getMinimumSize();
	    dim.width = Math.max(d.width, dim.width);
	    dim.height += d.height + vgap;
	}

	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right;
	dim.height += insets.top + insets.bottom;

	return dim;
      }
    }

    /**
     * Determines the preferred size of the <code>target</code>
     * container using this layout manager, based on the components
     * in the container.
     * <p>
     * Most applications do not call this method directly. This method
     * is called when a container calls its <code>getPreferredSize</code>
     * method.
     * @param   target   the container in which to do the layout.
     * @return  the preferred dimensions to lay out the subcomponents
     *          of the specified container.
     * @see     java.awt.Container
     * @see     java.awt.BorderLayout#minimumLayoutSize
     * @see     java.awt.Container#getPreferredSize()
     */
    public Dimension preferredLayoutSize(Container target) {
      synchronized (target.getTreeLock()) {
System.out.println("21");
	Dimension dim = new Dimension(0, 0);

        boolean ltr = target.getComponentOrientation().isLeftToRight();
        Component c = null;
System.out.println("22");

	if ((c=getChild(EAST,ltr)) != null) {
	    Dimension d = c.getPreferredSize();
	    dim.width += d.width + hgap;
	    dim.height = Math.max(d.height, dim.height);
	}
System.out.println("23");
	if ((c=getChild(WEST,ltr)) != null) {
	    Dimension d = c.getPreferredSize();
	    dim.width += d.width + hgap;
	    dim.height = Math.max(d.height, dim.height);
	}
System.out.println("24");
	if ((c=getChild(CENTER,ltr)) != null) {
	    Dimension d = c.getPreferredSize();
	    dim.width += d.width;
	    dim.height = Math.max(d.height, dim.height);
	}
System.out.println("25");
	if ((c=getChild(NORTH,ltr)) != null) {
	    Dimension d = c.getPreferredSize();
	    dim.width = Math.max(d.width, dim.width);
	    dim.height += d.height + vgap;
	}
System.out.println("26");
	if ((c=getChild(SOUTH,ltr)) != null) {
	    Dimension d = c.getPreferredSize();
	    dim.width = Math.max(d.width, dim.width);
	    dim.height += d.height + vgap;
	}

	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right;
	dim.height += insets.top + insets.bottom;
System.out.println("27");

	return dim;
      }
    }

    /**
     * Returns the maximum dimensions for this layout given the components
     * in the specified target container.
     * @param target the component which needs to be laid out
     * @see Container
     * @see #minimumLayoutSize
     * @see #preferredLayoutSize
     */
    public Dimension maximumLayoutSize(Container target) {
	return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    public float getLayoutAlignmentX(Container parent) {
	return 0.5f;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     */
    public float getLayoutAlignmentY(Container parent) {
	return 0.5f;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     */
    public void invalidateLayout(Container target) {
    }

    /**
     * Lays out the container argument using this border layout.
     * <p>
     * This method actually reshapes the components in the specified
     * container in order to satisfy the constraints of this
     * <code>BorderLayout</code> object. The <code>NORTH</code>
     * and <code>SOUTH</code> components, if any, are placed at
     * the top and bottom of the container, respectively. The
     * <code>WEST</code> and <code>EAST</code> components are
     * then placed on the left and right, respectively. Finally,
     * the <code>CENTER</code> object is placed in any remaining
     * space in the middle. The rule which component is over which
     * is defined by the <code>setNorthOverWest(), setEastOverNorth(), setSouthOverEast() and setWestOverSouth()
     * </code> methods .
     * <p>
     * Most applications do not call this method directly. This method
     * is called when a container calls its <code>doLayout</code> method.
     * @param   target   the container in which to do the layout.
     * @see     java.awt.Container
     * @see     java.awt.Container#doLayout()
     */
    public void layoutContainer(Container target) {
      synchronized (target.getTreeLock()) {
	Insets insets = target.getInsets();
	int top = insets.top;
	int bottom = target.getHeight() - insets.bottom;
	int left = insets.left;
	int right = target.getWidth() - insets.right;
        int nh=0,sh=0,et=0,wt=0;

        boolean ltr = target.getComponentOrientation().isLeftToRight();
        Component c = null;
        Component t = null;
        int tn = 0;

	if ((c=getChild(NORTH,ltr)) != null) {
	    c.setSize(right - left, c.getHeight());
	    Dimension d = c.getPreferredSize();
            int x,w;
            x=left;
            w=right-left;
            if (!now) {
                if ((t=getChild(WEST,ltr))!=null) {
                    tn=t.getPreferredSize().width;
                    x+=tn;
                    w-=tn;
                }
            }
            if (eon) {
                if ((t=getChild(EAST,ltr))!=null)
                    w-=t.getPreferredSize().width;
            }
	    c.setBounds(x, top, w, d.height);
            nh=d.height+vgap;
	}
	if ((c=getChild(SOUTH,ltr)) != null) {
	    c.setSize(right - left, c.getHeight());
	    Dimension d = c.getPreferredSize();
            int x,w;
            x=left;
            w=right-left;
            if (!soe) {
                if ((t=getChild(EAST,ltr))!=null) {
                    w-=t.getPreferredSize().width;
                }
            }
            if (wos) {
                if ((t=getChild(WEST,ltr))!=null) {
                    tn=t.getPreferredSize().width;
                    x+=tn;
                    w-=tn;
                }
            }
	    c.setBounds(x, bottom - d.height, w, d.height);
            sh=d.height+vgap;
	}
	if ((c=getChild(EAST,ltr)) != null) {
	    c.setSize(c.getWidth(), bottom - top);
	    Dimension d = c.getPreferredSize();
            int y,h;
            y=top;
            h=bottom - top;
            if (!eon) {
                if ((t=getChild(NORTH,ltr))!=null) {
                    tn=t.getPreferredSize().height;
                    y+=tn;
                    h-=tn;
                }
            }
            if (soe) {
                if ((t=getChild(SOUTH,ltr))!=null) {
                    h-=t.getPreferredSize().height;
                }
            }
	    c.setBounds(right - d.width, y, d.width, h);
            et=d.width+hgap;
	}
	if ((c=getChild(WEST,ltr)) != null) {
	    c.setSize(c.getWidth(), bottom - top);
	    Dimension d = c.getPreferredSize();
            int y,h;
            y=top;
            h=bottom - top;
            if (!wos) {
                if ((t=getChild(SOUTH,ltr))!=null)
                    h-=t.getPreferredSize().height;
            }
            if (now) {
                if ((t=getChild(NORTH,ltr))!=null) {
                    tn=t.getPreferredSize().height;
                    y+=tn;
                    h-=tn;
                }
            }
	    c.setBounds(left, y, d.width, h);
            wt=d.width+hgap;
	}
	if ((c=getChild(CENTER,ltr)) != null) {
	    c.setBounds(left + wt, top + nh, right - left - wt - et, bottom - top - nh - sh);
	}
      }
    }

    /**
     * Get the component that corresponds to the given constraint location
     *
     * @param   key     The desired absolute position,
     *                  either NORTH, SOUTH, EAST, or WEST.
     * @param   ltr     Is the component line direction left-to-right?
     */
    private Component getChild(String key, boolean ltr) {
        Component result = null;

        if (key == NORTH) {
            result = (firstLine != null) ? firstLine : north;
        }
        else if (key == SOUTH) {
            result = (lastLine != null) ? lastLine : south;
        }
        else if (key == WEST) {
            result = ltr ? firstItem : lastItem;
            if (result == null) {
                result = west;
            }
        }
        else if (key == EAST) {
            result = ltr ? lastItem : firstItem;
            if (result == null) {
                result = east;
            }
        }
        else if (key == CENTER) {
            result = center;
        }
        if (result != null && !result.isVisible()) {
            result = null;
        }
        return result;
    }

    /**
     * Returns a string representation of the state of this border layout.
     * @return    a string representation of this border layout.
     */
    public String toString() {
	return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
    }

    /**
     * Configures the layout so that the north pane is over the west pane.
     */
    public void setNorthOverWest(boolean b) {
        now=b;
    }
    /**
     * Returns a boolean value indicating if the north pane is laid out over the west pane.
     */
    public boolean isNorthOverWest() {
        return now;
    }
    /**
     * Configures the layout so that the east pane is over the north pane.
     */
    public void setEastOverNorth(boolean b) {
        eon=b;
    }
    /**
     * Returns a boolean value indicating if the east pane is laid out over the north pane.
     */
    public boolean isEastOverNorth() {
        return eon;
    }
    /**
     * Configures the layout so that the south pane is over the east pane.
     */
    public void setSouthOverEast(boolean b) {
        soe=b;
    }
    /**
     * Returns a boolean value indicating if the south pane is laid out over the east pane.
     */
    public boolean isSouthOverEast() {
        return soe;
    }
    /**
     * Configures the layout so that the west pane is over the south pane.
     */
    public void setWestOverSouth(boolean b) {
        wos=b;
    }
    /**
     * Returns a boolean value indicating if the west pane is laid out over the south pane.
     */
    public boolean isWestOverSouth() {
        return wos;
    }
}
