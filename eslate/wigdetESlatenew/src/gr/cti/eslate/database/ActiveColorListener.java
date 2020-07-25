package gr.cti.eslate.database;

import java.util.EventListener;


public interface ActiveColorListener extends EventListener {
    public abstract void activeColorChanged(ActiveColorEvent e);
}
