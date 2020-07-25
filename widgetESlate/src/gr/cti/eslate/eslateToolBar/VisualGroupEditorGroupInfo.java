package gr.cti.eslate.eslateToolBar;

/**
 * Visual group information required by the visual group editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class VisualGroupEditorGroupInfo
{
  /**
   * The name of the visual group.
   */
  String name;
  /**
   * Visual group visibility flag.
   */
  boolean visible;
  /**
   * Information about the tools in the visual group.
   */
  VisualGroupEditorToolInfoBaseArray tools;

  /**
   * Construct a VisualGroupEditorGroupInfo instance.
   * @param     name    The name of the tool.
   * @param     visible Visual group visibility flag.
   * @param     tools   Information about the tools in the visual group.
   */
  VisualGroupEditorGroupInfo(
    String name, boolean visible, VisualGroupEditorToolInfoBaseArray tools)
  {
    this.name = name;
    this.visible = visible;
    this.tools = tools;
  }
}
