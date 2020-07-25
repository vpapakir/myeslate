package org.cbook.cbookif.rm;

import java.io.IOException;
/**
 * General exception for the resource manager.
 * @author Wim van Velthoven
 *
 */
@SuppressWarnings("serial")
public class ResourceException extends IOException {

	public ResourceException() {
	}

	public ResourceException(String message) {
		super(message);
	}

	public ResourceException(Throwable t) {
		this(t.getMessage(), t);
	}

	public ResourceException(String message, Throwable t) {
		super(message);
		initCause(t);
	}

}
