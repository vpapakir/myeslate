package gr.cti.eslate.mapViewer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * Layout manager that fits all its contents to full width, retaining their preferred size.
 * Furthermore, it doesn't show containers that have no contents.
 */
class FullWidthLayout implements LayoutManager {

    FullWidthLayout() {
    }
    /**
     * Not used by this class.
     *
     * @param name the name of the component
     * @param comp the component
     */
    public void addLayoutComponent(String name, Component comp) {
    }
    /**
     * Not used by this class.
     *
     * @param name the name of the component
     * @param comp the component
     */
    public void removeLayoutComponent(Component comp) {
    }
    /**
     * Returns the preferred dimensions for this layout, given the components
     * in the specified target container.
     */
    public Dimension preferredLayoutSize(Container parent) {
        int nChildren = parent.getComponentCount();
        int y=0;
        //Count the height
        for (int i = 0; i<nChildren; i++) {
            Component c=parent.getComponent(i);
            if (!(c instanceof Container  && ((Container) c).getComponentCount()==0))
                y+=c.getPreferredSize().height;
        }

        return new Dimension(0,y);
    }
    /**
     * Returns the minimum dimensions needed to lay out the components
     * contained in the specified target container.
     */
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }
    /**
     * Called by the AWT when the specified container
     * needs to be laid out.
     */
    public void layoutContainer(Container parent) {
        int nChildren = parent.getComponentCount();
        int y=0;
        // flush changes to the container
        for (int i = 0; i < nChildren; i++) {
            Component c = parent.getComponent(i);
            if (c instanceof Container && ((Container) c).getComponentCount()==0)
                c.setBounds(0,0,0,0);
            else {
                c.setBounds(0,y,parent.getWidth(),c.getPreferredSize().height);
                y+=c.getPreferredSize().height;
            }
        }
    }
}