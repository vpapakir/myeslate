package gr.cti.eslate.base.container;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public final class ListenerSecurityProxy {
    ESlateContainer container;
    private String id = null;

    ListenerSecurityProxy(ESlateContainer container, String id) {
        this.id = id;
        this.container = container;
    }

    public final void grantAccess() {
        container.grantAccess(id);
    }

    public final void revokeAccess() {
        container.revokeAccess();
    }

    final String getID() {
        return id;
    }
}
