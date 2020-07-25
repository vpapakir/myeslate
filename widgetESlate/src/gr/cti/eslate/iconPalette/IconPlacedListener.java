package gr.cti.eslate.iconPalette;

import java.util.EventListener;

public interface IconPlacedListener extends EventListener {
	public abstract void iconPlaced(IconPlacedEvent e);
}
