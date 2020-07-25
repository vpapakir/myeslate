package gr.cti.eslate.base;

import java.beans.beancontext.*;
import java.util.*;

/**
 * This class provides a context encapsulating components that are children of
 * another component. It extends BeanContextServicesSupport, so that all of
 * the bean context containment and services functionality is available,
 * should it be required.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21-Sep-2007
 */
public class ESlateBeanContext extends BeanContextServicesSupport
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The E-Slate handle of the component associated with this context.
   */
  private ESlateHandle handle;
  /**
   * The ESlateBeanContext of the parent component.
   */
  private ESlateBeanContext parentContext = null;

  /**
   * Create an ESlateBeanContext.
   * @param     handle  The E-Slate handle of the component associated with
   *                    this context.
   */
  ESlateBeanContext(ESlateHandle handle)
  {
    super();
    this.handle = handle;
  }

  /**
   * Returns the E-Slate handle of the component associated with this context.
   * @return    The requested handle.
   */
  public ESlateHandle getESlateHandle()
  {
    return handle;
  }

  /**
   * Returns the ESlateBeanContext associated with the parent component.
   * @return    The requested context.
   */
  ESlateBeanContext getParentContext()
  {
    return parentContext;
  }

  /**
   * Add a component to this component's list of child components.
   * @param     child   The ESlateBeanContext of the component to add to the
   *                    list.
   */
  public boolean add(Object child)
  {
    if (!(child instanceof ESlateBeanContext)) {
      return false;
    }
    boolean status = super.add(child);
    if (status) {
      ((ESlateBeanContext)child).parentContext = this;
    }
    return status;
  }

  public boolean remove(Object child)
  {
    if (!(child instanceof ESlateBeanContext)) {
      return false;
    }
    boolean status = super.remove(child);
    if (status) {
      ((ESlateBeanContext)child).parentContext = null;
    }
    return status;
  }

  /**
   * Add a collection of components to the list of child components.
   * @param     c       A collection containing the ESlateBeanContexts
   *                    associated with the components to add to the list.
   * @return    True if at least one of the components was actually added
   *            to the list, false otherwise (i.e., if all the components were
   *            already in the list).
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>addAll</code> method is not supported. (Not
   *                    thrown.)
   * @exception ClassCastException      Thrown if an element of the specified
   *                    collection is not an E-Slate handle.
   * @exception IllegalArgumentException        Thrown if some aspect of an
   *                    element of the specified collection prevents it from
   *                    being added to the list of child components. (Not
   *                    currently thrown.)
   */
  @SuppressWarnings("unchecked")
  public boolean addAll(Collection c)
     throws java.lang.UnsupportedOperationException, ClassCastException,
            IllegalArgumentException
   {
     Iterator it = c.iterator();
     boolean status = false;
     while (it.hasNext()) {
       ESlateBeanContext bc = (ESlateBeanContext)(it.next());
       status |= add(bc);
     }
     return status;
   }

  /**
   * Remove all child components.
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>clear</code> method is not supported. (Not
   *                    thrown.)
   */
  public void clear() throws java.lang.UnsupportedOperationException
  {
    Object[] c = toArray();
    for (int i=0; i<c.length; i++) {
      remove(c);
    }
  }

  /**
   * Remove from the list of child components all the components contained
   * in a specified set of components.
   * @param     c       A collection containing the ESlateBeanContexts of the
   *                    components to remove.
   * @return    True if at least one of the components was actually removed
   *            from the list, false otherwise (i.e., if none of the components
   *            were contained in the list).
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>removeAll</code> method is not supported. (Not
   *                    thrown.)
   */
  @SuppressWarnings("unchecked")
  public boolean removeAll(Collection c)
    throws java.lang.UnsupportedOperationException
  {
    boolean status = false;
    Iterator it = c.iterator();
    while (it.hasNext()) {
      status |= remove(it.next());
    }
    return status;
  }


  /**
   * Remove from the list of child components all components except those
   * contained in a specified set of components.
   * @param     c       A collection containing the ESlateBeanContexts of the
   *                    components not to remove.
   * @return    True if at least one of the components was actually removed
   *            from the list, false otherwise (i.e., if the specified
   *            collection contained the ESlateBeanContexts of all the child
   *            components).
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>retainAll</code> method is not supported. (Not
   *                    thrown.)
   */
  @SuppressWarnings("unchecked")
  public boolean retainAll(Collection c)
    throws java.lang.UnsupportedOperationException
  {
    Iterator it = iterator();
    boolean status = false;
    while (it.hasNext()) {
      ESlateBeanContext bc = (ESlateBeanContext)(it.next());
      if (!(c.contains(bc))) {
        status |= remove(bc);
      }
    }
    return status;
  }

  /**
   * Free resources. After invoking this method, the object is unusable.
   */
  public void dispose()
  {
    clear();
    handle = null;
    parentContext = null;
    super.releaseBeanContextResources();
  }
}
