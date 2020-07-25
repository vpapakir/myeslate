package gr.cti.eslate.eslateToolBar;

import java.util.*;

/**
 * English language localized strings for the separation property editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.eslateToolBar.SeparationEditor
 */
public class SeparationEditorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"horizontal", "Horizontal"},
    {"vertical", "Vertical"},
    {"default", "Default"},
  };
}
