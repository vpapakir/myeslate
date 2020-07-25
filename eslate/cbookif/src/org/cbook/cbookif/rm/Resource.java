package org.cbook.cbookif.rm;

import java.io.InputStream;
import java.net.URL;

/**
 * Interface to a resource. A resource can be accessed through an URL or an InputStream.
 * Some resources contain other resources.
 * @see ResourceContainer
 * @author Wim van Velthoven
 *
 */
public interface Resource {
	/**
	 * If this resource is a container
	 * @return boolean
	 */
	boolean isContainer();
	/** The parent of this resource. 
	 * @return parent container
	 */
	ResourceContainer getParent();
	/**
	 * The basename of the resource
	 * @return String
	 */
	String getName();
	/**
	 * Access of the resource by URL.
	 * @return an URL
	 * @throws ResourceException
	 */
	URL getURL() throws ResourceException;
	/**
	 * Access of the resource by inputstream.
	 * @return the stream of this resource
	 * @throws ResourceException
	 */
	InputStream getStream() throws ResourceException;
	/**
	 * Mimetype like image/jpeg of text/plain
	 * @return string
	 */
	String getMimeType();
	/**
	 * Remove this resource. If this method succeeds, the resource object becomes invalid.
	 * @throws ResourceException
	 */
	void remove() throws ResourceException;
	/**
	 * Rename the resource to a different name.
	 * @param name
	 * @throws ResourceException
	 */
	void setName(String name) throws ResourceException;
}
