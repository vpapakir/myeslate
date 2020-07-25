package gr.cti.eslate.base;

import java.util.*;

/**
 * English language localized strings for the <code>CurrencyManager</code>
 * class.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class CurrencyResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"notEuro", "Can't change Euro exchange rate!"},
  };
}
