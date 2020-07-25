package cbookeditor.impl;

import java.io.Serializable;

public class Holder //implements Serializable 
{
	/*static*/ transient WidgetContainer object;

	public Holder() {}
	public Holder(WidgetContainer object) {
		super();
		this.object = object;
	}

	public WidgetContainer getObject() {
		return object;
	}
}