package org.cbook.cbookif.rm;
/**
 * When a Resource/ResourceContainer is read-only, 
 * it will throw a ReadOnlyException.
 * Methods that will throw this exception are: create, createContainer, remove, setName.
 * @author Wim van Velthoven
 *
 */
@SuppressWarnings("serial")
public class ReadOnlyException extends ResourceException {

	public ReadOnlyException() {
	}

	public ReadOnlyException(String arg0) {
		super(arg0);
	}

	public ReadOnlyException(Throwable arg0) {
		super(arg0);
	}

	public ReadOnlyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
