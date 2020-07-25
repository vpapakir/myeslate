package org.cbook.cbookif.rm;

/**
 * The resource manager. It manages three containers. A global for read-only global resources.
 * One for the widget designer to use and one for the student.
 * It can be accessed via the getProperty method of a CBookContext
 * @see org.cbook.cbookif.CBookContext#getProperty(String)
 * @see org.cbook.cbookif.Constants#RESOURCE_MANAGER
 * @author Wim van Velthoven
 *
 */
public interface ResourceManager {

	/**
	 * The images container.
	 */
	String IMAGES = "images";
	
	/**
	 * The global container. Read-only.
	 * @return a container of resources
	 */
	ResourceContainer getWidgetContainer();

	/**
	 * The local container. Read-write for designer, read-only for student
	 * @return a container of resources
	 */
	ResourceContainer getInstanceContainer();

	/**
	 * The local per student container. 
	 * Read-write for student, non existent for designer
	 * @return a container of resources
	 */
	ResourceContainer getStudentContainer();

}
