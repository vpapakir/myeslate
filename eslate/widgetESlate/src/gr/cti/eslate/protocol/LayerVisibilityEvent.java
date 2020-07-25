package gr.cti.eslate.protocol;


public class LayerVisibilityEvent extends java.util.EventObject {
	private boolean changing;

	public LayerVisibilityEvent(Object source, boolean isChanging) {
		super(source);
		this.changing = isChanging;
	}

	public boolean isChanging() {
		return changing;
	}
}
