package gr.cti.eslate.eslateToolBar;

import java.io.*;

import gr.cti.eslate.utils.*;

/**
 * Information associated with tools in the toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 */
class ToolInfo implements Externalizable
{
  /**
   * Specifies whether the tool is visible.
   */
  boolean visible;
  /**
   * Text associated with the tool.
   */
  String associatedText;

  // StorageStructure keys.
  private final static String VISIBLE = "1";
  private final static String ASSOCIATED_TEXT = "2";

  /**
   * Save format version.
   */
  private final static int saveVersion = 1;

  static final long serialVersionUID = 1L;

  /**
   * Construct the information.
   */
  public ToolInfo()
  {
    visible = true;
    associatedText = null;
  }

  /**
   * Specify whether the tool is visible.
   * @param     visible True if the tool is visible, false otherwise.
   */
  void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  /**
   * Checks whether the tool is visible.
   * @return    True if the tool is visible, false otherwise.
   */
  boolean isVisible()
  {
    return visible;
  }

  /**
   * Associates a piece of text with the tool.
   * @param     text    The text to associate with the tool.
   */
  void setAssociatedText(String text)
  {
    associatedText = text;
  }

  /**
   * Returns the text associated with the tool.
   * @return    The text associated with the tool.
   */
  String getAssociatedText()
  {
    return associatedText;
  }

  /**
   * Saves the information.
   * @param     oo      The stream to which the information will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 2);

    map.put(VISIBLE, visible);
    map.put(ASSOCIATED_TEXT, associatedText);

    oo.writeObject(map);
  }

  /**
   * Loads the information.
   * @param     oi      The stream from which the information will be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    visible = map.get(VISIBLE, true);
    associatedText = map.get(ASSOCIATED_TEXT, associatedText);
  }
}
