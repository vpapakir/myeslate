package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.NameUsedException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.container.event.ESlateComponentEvent;
import gr.cti.eslate.base.container.event.ESlateComponentListener;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.ArrayList;


public class ESlateComponent {
    protected ESlateHandle handle;
    DesktopItem desktopItem = null;
    protected ESlateInternalFrame frame = null;
    UILessComponentIcon icon = null;
    boolean visualBean = true;
    Object object = null;
    /* The class of the customizer of the 'object', if one exists. */
    Class customizerClass = null;
    boolean checkedForCustomizer = false;

    transient ArrayList eslateComponentListeners = new ArrayList();
    private int listenerCount = 0;
    ESlateContainer container;
//    private boolean active = false;

    public ESlateComponent(ESlateContainer container, ESlateHandle handle, boolean visual,
                           DesktopItem item, Object object) {
        this.container = container;
        this.handle = handle;
        this.object = object;
        this.desktopItem = item;
//System.out.println("Assigning the container of the ESlateComponent: " + this);
        visualBean = visual;
//        System.out.println("ESlateComponent constructor: " + this + ", visualBean: " + visualBean);
        if (ESlateInternalFrame.class.isAssignableFrom(item.getClass())) {
            frame = (ESlateInternalFrame) item;
//if            frame.eslateComponent = this;
        }else if (UILessComponentIcon.class.isAssignableFrom(item.getClass())) {
            icon = (UILessComponentIcon) item;
            icon.eslateComponent = this;
        }
    }

    public synchronized void addESlateComponentListener(ESlateComponentListener listener) {
        if (eslateComponentListeners.indexOf(listener) == -1) {
            eslateComponentListeners.add(listener);
            listenerCount++;
        }
    }


    public synchronized void removeESlateComponentListener(ESlateComponentListener listener) {
        if (eslateComponentListeners.indexOf(listener) != -1) {
            eslateComponentListeners.remove(listener);
            listenerCount--;
        }
    }

    protected void fireComponentActivated(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentActivated(e);
    }

    protected void fireComponentDeactivated(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentDeactivated(e);
    }

    protected void fireComponentIconified(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentIconified(e);
    }

    protected void fireComponentRestored(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentRestored(e);
    }

    protected void fireComponentMaximized(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentMaximized(e);
    }

    protected void fireComponentClosed(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentClosed(e);
    }

    protected void fireComponentClosing(ESlateComponentEvent e) {
        if (listenerCount == 0) return;
        ArrayList cl;
        synchronized(this) {cl = (ArrayList) eslateComponentListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateComponentListener) cl.get(i)).componentClosing(e);
    }

    public void setName(String newName) throws NameUsedException, RenamingForbiddenException {
        handle.setComponentName(newName);
    }

    public String getName() {
        return handle.getComponentName();
    }

    public DesktopItem getDesktopItem() {
        return desktopItem;
    }

    public ESlateInternalFrame getFrame() {
        return frame;
    }

    public Class getCustomizerClass() {
        return customizerClass;
    }

    public void setIcon(boolean icon) throws PropertyVetoException {
        desktopItem.setIcon(icon);
    }

    public boolean isIcon() {
        return desktopItem.isIcon();
    }

    public void setMaximum(boolean maximum) throws PropertyVetoException {
        desktopItem.setMaximum(maximum);
    }

    public boolean isMaximum() {
        return desktopItem.isMaximum();
    }

    public void setLocation(Point p) {
        desktopItem.setDesktopItemLocation(p);
    }

    public void setLocation(int x, int y) {
        desktopItem.setDesktopItemLocation(x, y);
    }

    public Point getDesktopItemLocation() {
        return desktopItem.getDesktopItemLocation();
    }

    public Point getDesktopItemLocation(Point p) {
        return desktopItem.getDesktopItemLocation(p);
    }

    public Rectangle getBounds() {
        return desktopItem.getDesktopItemBounds();
    }

    public Rectangle getBounds(Rectangle bounds) {
        return desktopItem.getDesktopItemBounds(bounds);
    }

    public void setResizable(boolean resizable) {
        desktopItem.setDesktopItemResizable(resizable);
    }

    public boolean isResizable() {
        return desktopItem.isDesktopItemResizable();
    }

    public void setBounds(Rectangle bounds) {
        desktopItem.setDesktopItemBounds(bounds);
    }

    public void setBounds(int x, int y, int width, int height) {
        desktopItem.setDesktopItemBounds(x, y, width, height);
    }

    public boolean isIconifiable() {
        return desktopItem.isIconifiable();
    }

    public void setIconifiable(boolean iconifiable) {
        desktopItem.setIconifiable(iconifiable);
    }

    public void setMaximizable(boolean maximizable) {
        desktopItem.setMaximizable(maximizable);
    }

    public boolean isMaximizable() {
        return desktopItem.isMaximizable();
    }

    public void setClosable(boolean closable) {
        desktopItem.setClosable(closable);
    }

    public boolean isClosable() {
        return desktopItem.isClosable();
    }

    public void setActive(boolean active) throws PropertyVetoException {
        desktopItem.setActive(active);
    }

    public boolean isActive() {
        return desktopItem.isActive();
    }

    public int getLayer() {
        return desktopItem.getLayer();
    }

    public void setLayer(int layer) {
        desktopItem.setLayer(layer);
    }

    public Dimension getSize() {
        return desktopItem.getDesktopItemSize();
    }

    public Dimension getSize(Dimension d) {
        return desktopItem.getDesktopItemSize(d);
    }

    public void setSize(Dimension size) {
        desktopItem.setDesktopItemSize(size);
    }

    public void setSize(int width, int height) {
        desktopItem.setDesktopItemSize(width, height);
    }

    public void setTitlePanelVisible(boolean visible) {
        desktopItem.setTitlePanelVisible(visible);
    }

    public boolean isTitlePanelVisible() {
        return desktopItem.isTitlePanelVisible();
    }

    public ESlateHandle getHandle() {
        return handle;
    }

    public boolean isVisualBean() {
        return visualBean;
    }

    public String toString() {
        return "ESlateComponent: " + handle.getComponentName() + ", " +
        desktopItem.getClass().getName().substring(desktopItem.getClass().getName().lastIndexOf('.')+1);
    }
}




