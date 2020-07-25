package gr.cti.eslate.base.container.event;

import gr.cti.eslate.base.container.Microworld;

import java.util.EventObject;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class MicroworldEvent extends EventObject {

    public MicroworldEvent(Microworld microworld) {
        super(microworld);
    }

    public Microworld getMicroworld() {
        return (Microworld) getSource();
    }
}
