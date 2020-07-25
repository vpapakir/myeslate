package gr.cti.eslate.propertyEditors;

import java.util.*;

/**
 * English language localized strings for the dimension property editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class DimensionEditorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"horizontal", "Width"},
    {"vertical", "Height"},
    {"default", "Default"},
  };
}
