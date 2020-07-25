package gr.cti.eslate.utils;

import java.util.*;

/**
 * English language resources for the Print class.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class PrintResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"PrintErrorMsg1", "Printing images is not supported on this platform"},
    {"PrintErrorMsg2", "Unable to print. The executable file "},
    {"PrintErrorMsg3", " is missing from the bin folder of the ESlate installation."}
  };
}
