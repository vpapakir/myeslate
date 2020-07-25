package gr.cti.eslate.services.name;

import java.util.*;

/**
 * English language localized strings for the E-Slate name service.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameServiceResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"nameInUse1", "Name "},
    {"nameInUse2", " is already associated with an object"},
    {"nameNotUsed1", "Name "},
    {"nameNotUsed2", " is not associated with an object"},
    {"noNullName", "Name must not be null"},
    {"noNullObject", "Object must not be null"},
    {"noPath1", "Path "},
    {"noPath2", " does not exist"},
    {"notDir1", "Path "},
    {"notDir2", " is not a name service context"}
  };
}
