package gr.cti.eslate.eslateToolBar;

import java.io.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * This class implements the button groups used by the E-Slate toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class ESlateButtonGroup extends ButtonGroup
  implements Serializable, Externalizable
{
  /**
   * The name of the button group.
   */
  String name;

  // StorageStructure keys.
  private final static String NAME = "1";

  /**
   * Save format version.
   */
  private final static int saveVersion = 1;
  /**
   * Serialization format version.
   */
  static final long serialVersionUID = 1L;

  /**
   * Create an ESlateButtonGroup instance.
   * @param     name    The name of the button group.
   */
  ESlateButtonGroup(String name)
  {
    super();
    this.name = name;
  }

  /**
   * Create an ESlateButtonGroup instance. No-argument constructor, required
   * by readExternal.
   */
  public ESlateButtonGroup()
  {
    super();
  }

  /**
   * Set the name of the button group.
   * @param     name    The name of the button group.
   */
  void setName(String name)
  {
    this.name = name;
  }

  /**
   * Returns the name of the button group.
   * @return    The name of the button group.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the buttons in the buton group.
   * @return    An array containing the buttons in the button group.
   */
  public AbstractButton[] getButtons()
  {
    int n = buttons.size();
    AbstractButton[] ab = new AbstractButton[n];
    for (int i=0; i<n; i++) {
      ab[i] = (AbstractButton)(buttons.elementAt(i));
    }
    return ab;
  }

  /**
   * Returns a button that is part of the button group.
   * @param     i       The position of the button in the group.
   * @return    The <code>i</code>-th button in the group, or
   *            <code>null</code>, if there is no such button.
   */
  public AbstractButton getButton(int i)
  {
    if (i < buttons.size()) {
      return (AbstractButton)(buttons.elementAt(i));
    }else{
      return null;
    }
  }

  /**
   * Returns the position of a button in the button group.
   * @param     button  The button whose position is requested.
   * @return    The position of the given button , or -1
   *            if the button is not contained in the group.
   */
  public int getButtonIndex(AbstractButton button)
  {
    return buttons.indexOf(button);
  }

  /**
   * Checks whether the button group contains a given button.
   * @param     button  The button to check.
   * @return    True if the button group contains <code>button</code>, false
   *            otherwise.
   */
  public boolean contains(AbstractButton button)
  {
    return buttons.contains(button);
  }

  /**
   * Save the button group's state. The state does not include the buttons in
   * the group.
   * @param     oo      The stream to which the state will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 1);

    map.put(NAME, name);

    oo.writeObject(map);
  }

  /**
   * Load the button group's state. The state does not include the buttons in
   * the group.
   * @param     oi      The stream from which the state will be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    name = map.get(NAME, "");
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }
}
