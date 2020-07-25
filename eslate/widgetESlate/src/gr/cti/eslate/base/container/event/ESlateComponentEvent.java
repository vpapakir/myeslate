package gr.cti.eslate.base.container.event;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author George Tsironis
 * @version
 */

import gr.cti.eslate.base.container.ESlateComponent;

import java.util.EventObject;


public class ESlateComponentEvent extends EventObject {
    public static final int COMPONENT_ACTIVATED = 0;
    public static final int COMPONENT_DEACTIVATED = 1;
    public static final int COMPONENT_ICONIFIED = 2;
    public static final int COMPONENT_RESTORED = 3; //DEICONIFIED = 3;
    public static final int COMPONENT_CLOSED = 4;
    public static final int COMPONENT_CLOSING = 5;
    public static final int COMPONENT_MAXIMIZED = 6;
    int id = COMPONENT_ACTIVATED;
    private String componentName = null;

    public ESlateComponentEvent(ESlateComponent source, int id, String componentName) {
        super(source);
        this.id = id;
        this.componentName = componentName;
    }

    public int getID() {
        return id;
    }

    public String getComponentName() {
        return componentName;
    }
}
