package gr.cti.eslate.base.container.event;

import java.util.EventListener;


public interface ESlateContainerListener extends EventListener {
    public abstract void mwdChanged(MwdChangedEvent e);
    public abstract void mwdHistoryChanged(MwdHistoryChangedEvent e);
    public abstract void activeComponentChanged(ActiveComponentChangedEvent e);
}
