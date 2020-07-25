package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the toolbar component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.ToolBarPrimitives
 */
public class ToolBarResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"TOOLBAR.SHOWTOOL", "TOOLBAR.SHOWTOOL"},
    {"TOOLBAR.HIDETOOL", "TOOLBAR.HIDETOOL"},
    {"TOOLBAR.SHOWGROUP", "TOOLBAR.SHOWGROUP"},
    {"TOOLBAR.HIDEGROUP", "TOOLBAR.HIDEGROUP"},
  };
}
