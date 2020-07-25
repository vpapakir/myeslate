package gr.cti.eslate.registry;

import java.util.*;

/**
 * Greek language localized strings for the registry component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 * @see         gr.cti.eslate.registry.Registry
 */
public class RegistryResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Μητρώο"},
    {"name", "Μητρώο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright © 2001-2006 Ινστιτούτο Τεχνολογίας Υπολογιστών"},
    {"version", "έκδοση"},
    //
    // Other text
    {"exportRegistry", "Εξαγωγή μητρώου"},
    //
    // BeanInfo resources
    {"valueChanged", "Αλλαγή τιμής"},
    {"commentChanged", "Αλλαγή σχολίου"},
    {"persistenceChanged", "Αλλαγή αποθηκευσιμότητας"},
    {"variableAdded", "Προσθήκη μεταβλητής"},
    {"variableRemoved", "Διαγραφή μεταβλητής"},
    {"registryCleared", "Καθαρισμός μητρώου"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή μητρώου"},
    {"LoadTimer",             "Ανάκτηση μητρώου"},
    {"SaveTimer",             "Αποθήκευση μητρώου"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς μητρώου"},
  };
}
