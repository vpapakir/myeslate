package gr.cti.eslate.base.container.event;

import gr.cti.eslate.base.container.ESlateComponent;
import gr.cti.eslate.base.container.Microworld;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class MicroworldComponentEvent extends MicroworldEvent {
    private ESlateComponent component;
    private boolean wasIcon = false;

    public MicroworldComponentEvent(Microworld microworld, ESlateComponent component) {
        super(microworld);
        this.component = component;
    }

    public MicroworldComponentEvent(Microworld microworld, ESlateComponent component, boolean wasIcon) {
        super(microworld);
        this.component = component;
        this.wasIcon = wasIcon;
    }

    public ESlateComponent getComponent() {
        return component;
    }

    public boolean wasIcon() {
        return wasIcon;
    }

}
