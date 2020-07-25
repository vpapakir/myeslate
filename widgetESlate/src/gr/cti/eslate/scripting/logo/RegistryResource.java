package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the registry component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 * @see         gr.cti.eslate.scripting.logo.RegistryPrimitives
 */
public class RegistryResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"REGISTRY.REGISTER", "REGISTRY.REGISTER"},
    {"REGISTRY.UNREGISTER", "REGISTRY.UNREGISTER"},
    {"REGISTRY.SETCOMMENT", "REGISTRY.SETCOMMENT"},
    {"REGISTRY.LOOKUP", "REGISTRY.LOOKUP"},
  };
}
