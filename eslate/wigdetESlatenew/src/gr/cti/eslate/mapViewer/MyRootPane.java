package gr.cti.eslate.mapViewer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JRootPane;

class MyRootPane extends JRootPane {
    private MapPane mapPane;

    MyRootPane(MapPane mapPane) {
        super();
        this.mapPane=mapPane;
        setOpaque(false);
        setLayout(new MyRootLayout());
    }
    /**
     * Known method.
     */
    public void setFont(Font f) {
        super.setFont(f);
        for (int i=0;i<getComponentCount();i++)
            ((Component) getComponents()[i]).setFont(f);
        for (int i=0;i<getContentPane().getComponentCount();i++)
            ((Component) getContentPane().getComponents()[i]).setFont(f);
    }
    /**
     * A custom layout manager that is responsible for the layout of a rootPane.
     * Code taken from Sun but modified to make the glassPane smaller.
     */
    class MyRootLayout implements LayoutManager2, Serializable
    {
        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's preferred size
         */ 
        public Dimension preferredLayoutSize(Container parent) {
            Dimension rd, mbd;
            Insets i = getInsets();

            if(contentPane != null) {
                rd = contentPane.getPreferredSize();
            } else {
                rd = parent.getSize();
            }
            if(menuBar != null) {
                mbd = menuBar.getPreferredSize();
            } else {
                mbd = new Dimension(0, 0);
            }
            return new Dimension(Math.max(rd.width, mbd.width) + i.left + i.right,
                                        rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's minimum size
         */ 
        public Dimension minimumLayoutSize(Container parent) {
            Dimension rd, mbd;
            Insets i = getInsets();
            if(contentPane != null) {
                rd = contentPane.getMinimumSize();
            } else {
                rd = parent.getSize();
            }
            if(menuBar != null) {
                mbd = menuBar.getMinimumSize();
            } else {
                mbd = new Dimension(0, 0);
            }
            return new Dimension(Math.max(rd.width, mbd.width) + i.left + i.right, 
                        rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's maximum size
         */ 
        public Dimension maximumLayoutSize(Container target) {
            Dimension rd, mbd;
            Insets i = getInsets();
            if(menuBar != null) {
                mbd = menuBar.getMaximumSize();
            } else {
                mbd = new Dimension(0, 0);
            }
            if(contentPane != null) {
                rd = contentPane.getMaximumSize();
            } else {
                // This is silly, but should stop an overflow error
                rd = new Dimension(Integer.MAX_VALUE,
                        Integer.MAX_VALUE - i.top - i.bottom - mbd.height - 1);
            }
            return new Dimension(Math.min(rd.width, mbd.width) + i.left + i.right,
                                         rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param the Container for which this layout manager is being used
         */
        public void layoutContainer(Container parent) {
            Rectangle b = parent.getBounds();
            Insets i = getInsets();
            int contentY = 0;
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;

            if(layeredPane != null) {
                layeredPane.setBounds(i.left, i.top, w, h);
            }
            /*****
             ** This is my addition!
             **/
            if(glassPane != null) {
                glassPane.setBounds(i.left, i.top+h-glassPane.getPreferredSize().height, glassPane.getPreferredSize().width, glassPane.getPreferredSize().height);
            }
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our chilren.
            if(menuBar != null) {
                Dimension mbd = menuBar.getPreferredSize();
                menuBar.setBounds(0, 0, w, mbd.height);
                contentY += mbd.height;
            }
            if(contentPane != null) {
                contentPane.setBounds(0, contentY, w, h - contentY);
            }
        }

        public void addLayoutComponent(String name, Component comp) {}
        public void removeLayoutComponent(Component comp) {}
        public void addLayoutComponent(Component comp, Object constraints) {}
        public float getLayoutAlignmentX(Container target) { return 0.0f; }
        public float getLayoutAlignmentY(Container target) { return 0.0f; }
        public void invalidateLayout(Container target) {}
    }
}

