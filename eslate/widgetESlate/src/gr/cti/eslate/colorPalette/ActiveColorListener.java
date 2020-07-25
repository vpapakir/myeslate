package gr.cti.eslate.colorPalette;

import java.util.EventListener;

public interface ActiveColorListener extends EventListener {
	public abstract void activeColorChanged(ActiveColorEvent e);
}
