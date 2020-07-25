package gr.cti.eslate.base.container.event;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.container.ESlateContainer;

import java.util.EventObject;
                     

public class ActiveComponentChangedEvent extends EventObject {
    private ESlateHandle activeComponentHandle;

    public ActiveComponentChangedEvent(Object source) {
        super(source);
        ESlateContainer container = (ESlateContainer) source;
        activeComponentHandle = container.getActiveComponentHandle();
    }

    public ESlateHandle getActiveComponentHandle() {
        return activeComponentHandle;
    }
}
