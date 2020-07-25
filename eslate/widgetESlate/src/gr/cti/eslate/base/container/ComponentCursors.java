package gr.cti.eslate.base.container;

import java.awt.Component;
import java.awt.Cursor;
import java.util.HashMap;


public class ComponentCursors extends HashMap {

    public void storeCursors(ESlateContainer container) {
        clear();
        MicroworldComponents mwdComponents = container.getMicroworldComponentIndex();
        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            if (comp.isVisualBean()) {
//System.out.println("Store component: " + comp.getName() + ", cursor: " + ((Component) comp.getHandle().getComponent()).getCursor());
                put(comp.getName(), ((Component) comp.getHandle().getComponent()).getCursor());
			}
        }
    }

    public void restoreCursors(ESlateContainer container, Cursor defaultCursor) {
        MicroworldComponents mwdComponents = container.getMicroworldComponentIndex();
        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            if (comp.isVisualBean()) {
                Cursor c = (Cursor) get(comp.getName());
                if (c != null) {
                    ((Component) comp.getHandle().getComponent()).setCursor(c);
					comp.frame.setDefaultCursor(c);
                }else
                    ((Component) comp.getHandle().getComponent()).setCursor(defaultCursor);
//System.out.println("Component: " + comp.getName() + ", cursor: " + c);
            }
        }
    }
}