package gr.cti.eslate.utils;

import java.util.*;

/**
 * Greek language localized strings for ESlateOptionPane dialog boxes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateOptionPaneResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"ok", "Εντάξει"},
    {"cancel", "’κυρο"},
    {"yes", "Ναι"},
    {"no", "Όχι"},
    {"input", "Εισαγωγή"}
  };
}
