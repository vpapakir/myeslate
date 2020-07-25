package gr.cti.eslate.eslateToolBar;

/**
 * Button group information required by the button group editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class ButtonGroupEditorGroupInfo
{
  /**
   * The name of the button group.
   */
  String name;
  /**
   * Information about the tools in the visual group.
   */
  ButtonGroupEditorToolInfoBaseArray tools;

  /**
   * Construct a ButtonGroupEditorGroupInfo instance.
   * @param     name    The name of the tool.
   * @param     tools   Information about the tools in the button group.
   */
  ButtonGroupEditorGroupInfo(
    String name, ButtonGroupEditorToolInfoBaseArray tools)
  {
    this.name = name;
    this.tools = tools;
  }
}
