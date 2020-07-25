package gr.cti.eslate.registry.event;

import java.util.*;

import gr.cti.eslate.registry.*;

/**
 * Event sent when the value or associated comment of a registry variable is
 * modified.
 *
 * @version     2.0.0, 25-May-2006
 * @author      Kriton Kyrimis.
 */
public class RegistryEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The name of the variable.
   */
  private String name;
  /**
   * The new value of the variable.
   */
  private Object value;
  /**
   * The new comment associated with the variable.
   */
  private String comment;
  /**
   * Specifies whether the variable is persistent.
   */
  private boolean persistent;

  /**
   * Constructs a <code>RegistryEvent</code> signaling a change in the value
   * of a variable.
   * @param     registry        The registry to which the variable belongs.
   * @param     name            The name of the variable whose value was
   *                            modified.
   * @param     value           The new value of the variable.
   * @param     persistent      Specifiles if the variable is persistent.
   * @param     comment         The comment associated with the variable.
   */
  public RegistryEvent(Registry registry, String name, Object value,
                       boolean persistent, String comment)
  {
    super(registry);
    this.name = name;
    this.value = value;
    this.persistent = persistent;
    this.comment = comment;
  }

  /**
   * Returns the new value of the variable.
   * @return    The new value of the variable.
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * Returns the new comment associated with the variable.
   * @return    The new comment associated with the variable.
   */
  public String getComment()
  {
    return comment;
  }

  /**
   * Returns the name of the variable.
   * @return    The name of the variable.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Checks whether the variable is persistent.
   * @return    True if yes, false if no.
   */
  public boolean isPersistent()
  {
    return persistent;
  }

}
