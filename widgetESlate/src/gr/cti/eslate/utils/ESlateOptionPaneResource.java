package gr.cti.eslate.utils;

import java.util.*;

/**
 * English language localized strings for ESlateOptionPane dialog boxes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateOptionPaneResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"ok", "OK"},
    {"cancel", "Cancel"},
    {"yes", "Yes"},
    {"no", "No"},
    {"input", "Input"}
  };
}
