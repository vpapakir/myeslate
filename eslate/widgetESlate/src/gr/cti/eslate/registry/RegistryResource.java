package gr.cti.eslate.registry;

import java.util.*;

/**
 * English language localized strings for the registry component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 * @see         gr.cti.eslate.registry.Registry
 */
public class RegistryResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Component info
    {"componentName", "Registry component"},
    {"name", "Registry"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright © 2001-2006 Computer Technology Institute"},
    {"version", "version"},
    //
    // Other text
    {"exportRegistry", "Export registry"},
    //
    // BeanInfo resources
    {"valueChanged", "Value changed"},
    {"commentChanged", "Comment changed"},
    {"persistenceChanged", "Persistence Changed"},
    {"variableAdded", "Variable added"},
    {"variableRemoved", "Variable removed"},
    {"registryCleared", "Registry cleared"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Registry constructor"},
    {"LoadTimer",             "Registry load"},
    {"SaveTimer",             "Registry save"},
    {"InitESlateAspectTimer", "Registry E-Slate aspect creation"},
  };
}
