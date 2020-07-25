package gr.cti.eslate.base.container.event;

import gr.cti.eslate.base.container.Microworld;
import gr.cti.eslate.base.container.MicroworldView;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class MicroworldViewEvent extends MicroworldEvent {
    MicroworldView view = null;

    public MicroworldViewEvent(Microworld microworld, MicroworldView view) {
        super(microworld);
        this.view = view;
    }

    public MicroworldView getView() {
        return view;
    }
}
