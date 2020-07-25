package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.utils.CustomBorder;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
//import gr.cti.eslate.utils.ESlateFieldMap;.ESlaFieldMap;

/**
 * This is a normal ESlatePanelComponent with two properties added:
 * <code>customSize</code> and <code>useCustomSize</code>. See the corresponding
 * method descriptions for further details.
 * <p>
 * This pane is mainly used in ESlateInternalFrames, every pane of which
hA * (North, South, East, West) is a SkinPane.
 * <p>
 * @author  Giorgos Vasiliou
 * @version 2.0, 23-Apr-2001
 */
public class SkinPane extends gr.cti.eslate.panel.PanelComponent implements CustomBorder {
    static final int FORMAT_VERSION = 2;

    public SkinPane() {
        super();
        setContentPaneLayout(new BorderLayout());
        setBorder(null);
//        getESlateHandle().getMenuPanel().setVisible(false);
        setPreferredSize(new Dimension(10,10));
    }
    /**
     * The known method extended so that if the pane contains no components
     * its preferred size is not 0 but 10.
     */
    public Dimension getPreferredSize() {
        Dimension toReturn;
        if (useCustomSize)
            return customSize;
        if (getContentPaneLayout()!=null)
            toReturn=getContentPaneLayout().preferredLayoutSize(this);
        else
            toReturn=super.getPreferredSize();
        if (toReturn.width==0 && getContentPane().getComponents().length==0)
            toReturn.width=10;
        if (toReturn.height==0 && getContentPane().getComponents().length==0)
            toReturn.height=10;
        return toReturn;
    };
    /**
     * This sets the size (width or height) that will be used by the SkinPane
     * if <code>useCustomSize</code> property is <code>true</code>.
     */
    public void setCustomSize(int sz) {
        customSize.width=sz;
        customSize.height=sz;
        invalidate();
        if (getParent()!=null) {
            getParent().invalidate();
            getParent().validate();
        }
        repaint();
    }
    /**
     * This is the size (width or height) that will be used by the SkinPane
     * if <code>useCustomSize</code> property is <code>true</code>.
     */
    public int getCustomSize() {
        return customSize.width;
    }
    /**
     * If this property is set to <code>true</code>, the SkinPane has the
     * width or height of the <code>customSize</code> property.
     * <p>
     * If this property is set to <code>false</code>, the SkinPane has the
     * preferred width or height of its contents.
     */
    public void setUseCustomSize(boolean b) {
        useCustomSize=b;
        invalidate();
        if (getParent()!=null) {
            getParent().invalidate();
            getParent().validate();
        }
        repaint();
    }
    /**
     * If this property is <code>true</code>, the SkinPane has the
     * width or height of the <code>customSize</code> property.
     * <p>
     * If this property is  <code>false</code>, the SkinPane has the
     * preferred width or height of its contents.
     */
    public boolean getUseCustomSize() {
        return useCustomSize;
    }
    /**
     * Externalization input.
     */
    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
//long start = System.currentTimeMillis();
        super.readExternal(in);
//System.out.println("SkinPane readExternal(): " + (System.currentTimeMillis()-start));
        StorageStructure fm=(StorageStructure) in.readObject();
        try {
            setCustomSize(fm.getInteger("customSize"));
        } catch(Exception ex) {}; //ESlateFieldMap.KeyDoesntExistException
        try {
            setUseCustomSize(fm.getBoolean("useCustomSize"));
        } catch(Exception ex) {}; //ESlateFieldMap.KeyDoesntExistException
    }
    /**
     * Externalization output.
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        ESlateFieldMap2 fm=new ESlateFieldMap2(FORMAT_VERSION); //"1.0");
        fm.put("customSize",getCustomSize());
        fm.put("useCustomSize",getUseCustomSize());
        out.writeObject(fm);
    }

    /** Returns if the SkinPane has a custom south border. The SkinPane by default has no
     *  custom border. However if it contains components, it delegates this call to the
     *  first component in it. So the SkinPane supports custom borders according to the
     *  first component in it.
     */
    public boolean hasCustomBottomBorder() {
        if (getContentPane().getComponentCount() > 0) {
            Component comp = getContentPane().getComponents()[0];
            if (CustomBorder.class.isAssignableFrom(comp.getClass()))
                return ((CustomBorder) comp).hasCustomBottomBorder();
        }
        return false;
    }

    /** Returns if the SkinPane has a custom north border. The SkinPane by default has no
     *  custom border. However if it contains components, it delegates this call to the
     *  first component in it. So the SkinPane supports custom borders according to the
     *  first component in it.
     */
    public boolean hasCustomTopBorder() {
        if (getContentPane().getComponentCount() > 0) {
            Component comp = getContentPane().getComponents()[0];
            if (CustomBorder.class.isAssignableFrom(comp.getClass()))
                return ((CustomBorder) comp).hasCustomTopBorder();
        }
        return false;
    }

    /** Returns if the SkinPane has a custom west border. The SkinPane by default has no
     *  custom border. However if it contains components, it delegates this call to the
     *  first component in it. So the SkinPane supports custom borders according to the
     *  first component in it.
     */
    public boolean hasCustomWestBorder() {
        if (getContentPane().getComponentCount() > 0) {
            Component comp = getContentPane().getComponents()[0];
            if (CustomBorder.class.isAssignableFrom(comp.getClass())) {
                return ((CustomBorder) comp).hasCustomWestBorder();
            }
        }
        return false;
    }

    /** Returns if the SkinPane has a custom east border. The SkinPane by default has no
     *  custom border. However if it contains components, it delegates this call to the
     *  first component in it. So the SkinPane supports custom borders according to the
     *  first component in it.
     */
    public boolean hasCustomEastBorder() {
        if (getContentPane().getComponentCount() > 0) {
            Component comp = getContentPane().getComponents()[0];
            if (CustomBorder.class.isAssignableFrom(comp.getClass()))
                return ((CustomBorder) comp).hasCustomEastBorder();
        }
        return false;
    }

    /** Returns if the SkinPane realtive location of a point inside a SkinPane. The SkinPane
     *  by default has no custom border. However if it contains components, it delegates
     *  this call to the first component in it. So the SkinPane supports custom borders
     *  according to the first component in it.
     */
    public int getPointRelativeLocation(Point p) {
        if (getContentPane().getComponentCount() > 0) {
            Component comp = getContentPane().getComponents()[0];
            if (CustomBorder.class.isAssignableFrom(comp.getClass()))
                return ((CustomBorder) comp).getPointRelativeLocation(p);
        }
        return CustomBorder.INSIDE;
    }

    private Dimension customSize;
    private boolean useCustomSize;

    {
        customSize=new Dimension(10,10);
        useCustomSize=false;
    }
}