package gr.cti.eslate.base.container;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 * @version
 */

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;

public interface DesktopItem {
    public void setDesktopItemLocation(Point p);
    public void setDesktopItemLocation(int x, int y);
    public Point getDesktopItemLocation();
    public Point getDesktopItemLocation(Point p);
    public Point getDesktopItemRestoreLocation();
    public Dimension getDesktopItemRestoreSize();
    public Rectangle getDesktopItemBounds();
    public Rectangle getDesktopItemBounds(Rectangle bounds);
    public void setDesktopItemResizable(boolean resizable);
    public boolean isDesktopItemResizable();
    public void setDesktopItemBounds(Rectangle bounds);
    public void setDesktopItemBounds(int x, int y, int width, int height);
    public void revalidate();
    public void setIcon(boolean icon) throws PropertyVetoException;
    public boolean isIcon();
    public boolean isIconifiable();
    public void setIconifiable(boolean iconifiable);
    public void setMaximum(boolean maximum) throws PropertyVetoException;
    public boolean isMaximum();
    public void setMaximizable(boolean maximizable);
    public boolean isMaximizable();
    public void setClosable(boolean closable);
    public boolean isClosable();
    public void setClosed(boolean closed) throws PropertyVetoException;
    public void setActive(boolean active) throws PropertyVetoException;
    public boolean isActive();
    public boolean displaysESlateMenuBar();
    public boolean usesGlassPane();
    public int getLayer();
    public void setLayer(int layer);
    public Dimension getDesktopItemSize();
    public Dimension getDesktopItemSize(Dimension d);
    public void setDesktopItemSize(Dimension size);
    public void setDesktopItemSize(int width, int height);
//    public Class getCustomizerClass();
    public void setVisible(boolean visible);
    public boolean isVisible();
    public void setTitlePanelVisible(boolean visible);
    public boolean isTitlePanelVisible();
}