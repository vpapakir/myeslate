package gr.cti.eslate.eslateToolBar;

import java.io.*;

import gr.cti.eslate.utils.*;

/**
 * This class describes the location of a tool in the toolbar.
 *
 * @version     2.0.0, 22-May-2006
 * @author      Kriton Kyrimis
 */
public class ToolLocation implements Externalizable
{
  /**
   * Index of visual group to which the tool belongs.
   */
  public int visualGroup;
  /**
   * Index of tool within the visual group to which it belongs.
   */
  public int toolIndex;

  // StorageStructure keys.
  private final static String VISUAL_GROUP = "1";
  private final static String TOOL_INDEX = "2";

  /**
   * Save format version.
   */
  private final static int saveVersion = 1;

  static final long serialVersionUID = 1L;

  /**
   * Create a <code>ToolLocation</code> instance.
   * @param     visualGroup     Index of visual group to which the tool
   *                            belongs.
   * @param     toolIndex       Index of tool within the visual group to
   *                            which it belongs.
   */
  public ToolLocation(int visualGroup, int toolIndex)
  {
    this.visualGroup = visualGroup;
    this.toolIndex = toolIndex;
  }

  /**
   * Create a <code>ToolLocation</code> instance. No-argument constructor,
   * required by readExternal.
   */
  public ToolLocation()
  {
  }

  /**
   * String representation of the tool location.
   * @return    The string representation of the tool location.
   */
  public String toString()
  {
    return "Visual group " + visualGroup + ", tool index " + toolIndex;
  }

  /**
   * Save the tool location.
   * @param     oo      The stream to which the location will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 2);

    map.put(VISUAL_GROUP, visualGroup);
    map.put(TOOL_INDEX, toolIndex);

    oo.writeObject(map);
  }

  /**
   * Load the tool location.
   * @param     oi      The stream from which the location will be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    visualGroup = map.get(VISUAL_GROUP, -1);
    toolIndex = map.get(TOOL_INDEX, -1);
  }
}
