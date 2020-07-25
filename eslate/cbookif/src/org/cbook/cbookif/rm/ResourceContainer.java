package org.cbook.cbookif.rm;

import java.io.InputStream;
import java.net.URL;
/**
 * Resource that represents a folder or directory.
 * @author Wim van Velthoven
 *
 */
public interface ResourceContainer extends Resource {
	/**
	 * Open a child of this container.
	 * @param name
	 * @return a resource
	 * @throws ResourceException
	 */
	Resource open(String name) throws ResourceException;
	/**
	 * Create a child. It's contents is taken from the inputstream.
	 * @param name it's name
	 * @param in the source
	 * @param mimetype the type
	 * @return a resource
	 * @throws ResourceException
	 */
	Resource create(String name, InputStream in, String mimetype) throws ResourceException;
	/**
	 * Create a child. It is a reference to an URL. The contents is not copied.
	 * @param name it's name
	 * @param url the URL
	 * @return a resource
	 * @throws ResourceException
	 */
	Resource create(String name, URL url) throws ResourceException;
	/**
	 * Create a child that is a copy of another resource.
	 * @param name it's name
	 * @param resource the source
	 * @return a resource
	 * @throws ResourceException
	 */
	Resource create(String name, Resource resource) throws ResourceException;
	/** 
	 * Create a new container.
	 * @param name it's name
	 * @return a resource container
	 * @throws ResourceException
	 */
	ResourceContainer createContainer(String name) throws ResourceException;
	/**
	 * Open a child container. 
	 * @param name
	 * @return a resource container
	 * @throws ResourceException
	 */
	ResourceContainer openContainer(String name) throws ResourceException;
	/**
	 * List the contents of this container.
	 * @return all children.
	 * @throws ResourceException
	 */
	Resource[] list() throws ResourceException;
	
}
