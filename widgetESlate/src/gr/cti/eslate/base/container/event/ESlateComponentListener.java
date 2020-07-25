package gr.cti.eslate.base.container.event;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 * @version
 */


import java.util.EventListener;


public interface ESlateComponentListener extends EventListener {
    public abstract void componentActivated(ESlateComponentEvent e);
    public abstract void componentDeactivated(ESlateComponentEvent e);
    public abstract void componentIconified(ESlateComponentEvent e);
//    public abstract void componentDeiconified(ESlateComponentEvent e);
    public abstract void componentRestored(ESlateComponentEvent e);
    public abstract void componentMaximized(ESlateComponentEvent e);
    public abstract void componentClosed(ESlateComponentEvent e);
    public abstract void componentClosing(ESlateComponentEvent e);
}
