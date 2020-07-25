package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.io.*;

import gr.cti.eslate.utils.*;

/**
 * This class implements a visual group of components in the ESlate Toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class VisualGroup implements Externalizable
{
  /**
   * The name of the visual group.
   */
  String name;
  /**
   * The components in the visual group.
   */
  ComponentBaseArray components = new ComponentBaseArray();
  /**
   * The separation between components in the visual group.
   */
  Dimension separation = new Dimension(0, 0);
  /**
   * Specifies whether the components in the group should be drawn on the
   * toolbar.
   */
  boolean visible = true;

  // StorageStructure keys.
  private final static String NAME = "1";
  private final static String X_SEP = "2";
  private final static String Y_SEP = "3";
  private final static String VISIBLE = "4";

  /**
   * Save format version.
   */
  private final static int saveVersion = 1;
  /**
   * Serialization format version.
   */
  static final long serialVersionUID = 1L;

  /**
   * Create a visual group.
   * @param     name    The name of the visual group.
   */
  VisualGroup(String name)
  {
    super();
    this.name = name;
  }

  /**
   * Create a visual group. No-argument constructor, required by readExternal.
   */
  public VisualGroup()
  {
    super();
  }

  /**
   * Set the name of the visual group.
   * @param     name    The name of the visual group.
   */
  void setName(String name)
  {
    this.name = name;
  }

  /**
   * Returns the name of the visual group.
   * @return    The name of the visual group.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the number of components in the visual group.
   * @return    The number of components in the visual group.
   */
  public int getComponentCount()
  {
    return components.size();
  }

  /**
   * Returns the components in the visual group.
   * @return    An array containing the components in the visual group.
   */
  public Component[] getComponents()
  {
    return components.toArray();
  }

  /**
   * Returns a component that is part of the visual group.
   * @param     i       The position of the component in the group.
   * @return    The <code>i</code>-th component in the group, or
   *            <code>null</code>, if there is no such component.
   */
  public Component getComponent(int i)
  {
    if (i < components.size()) {
      return components.get(i);
    }else{
      return null;
    }
  }

  /**
   * Returns the position of a component in the visual group.
   * @param     c       The component whose position is requested.
   * @return    The position of the given component, or -1
   *            if the component is not contained in the group.
   */
  public int getComponentIndex(Component c)
  {
    return components.indexOf(c);
  }

  /**
   * Returns a string representation of the visial group.
   * @return    A string representation of the visial group.
   */
  public String toString()
  {
    return "Visual group " + name;
  }

  /**
   * Sets the value of the separation between components in the visual group.
   * @param     sep     The value of the separation between components in the
   *                    visual group. Use <code>null</code> to specify that a
   *                    default value should be used.
   */
  void setSeparation(Dimension sep)
  {
    separation = sep;
  }

  /**
   * Returns the value of the separation between components in the visual
   * group.
   * @return    The value of the separation between components in the visual
   *            group. A <code>null</code> return value signifies that a
   *            default value is being used.
   */
  public Dimension getSeparation()
  {
    return separation;
  }

  /**
   * Adds a component to the visual group.
   * @param     c       The component to add.
   */
  void add(Component c)
  {
    components.add(c);
  }

  /**
   * Adds a component to the visual group.
   * @param     i       The position in which the component will be added.
   * @param     c       The component to add.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                    an illegal value.
   */
  void add(int i, Component c)
  {
    components.add(i, c);
  }

  /**
   * Removes a component from the visual group.
   * @param     i       The index of the component to remove.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                    an illegal value.
   */
  void remove(int i)
  {
    components.remove(i);
  }

  /**
   * Removes a component from the visual group.
   * @param     c       The component tro remove.
   */
  void remove(Component c)
  {
    components.removeElements(c);
  }

  /**
   * Removes all the components from the visual group.
   */
  void removeAll()
  {
    components.clear();
  }

  /**
   * Checks whether the visual group contains a given component.
   * @param     c       The component to check.
   * @return    True if the visual group contains <code>c</code>, false
   *            otherwise.
   */
  public boolean contains(Component c)
  {
    return components.contains(c);
  }

  /**
   * Specifies whether the components in the group should be drawn on the
   * toolbar.
   * @param     visible True if yes, false otherwise.
   */
  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  /**
   * Checks whether the components in the group will be drawn on the tolbar.
   * @return    True if yes, false otherwise.
   */
  public boolean isVisible()
  {
    return visible;
  }

  /**
   * Save the visual group's state. The state does not include the components
   * in the group.
   * @param     oo      The stream to which the state will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 4);

    map.put(NAME, name);
    if (separation != null) {
      map.put(X_SEP, separation.width);
      map.put(Y_SEP, separation.height);
    }
    map.put(VISIBLE, visible);

    oo.writeObject(map);
  }

  /**
   * Load the visual group's state. The state does not include the components
   * in the group.
   * @param     oi      The stream from which the state will be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    name = map.get(NAME, "");
    if (map.get(X_SEP) != null) {
      separation = new Dimension(map.get(X_SEP, 0), map.get(Y_SEP, 0));
    }
    visible = map.get(VISIBLE, true);
  }
}
