package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

/**
 * English language resources for ESlateBeanInfo and custom property editors.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class ESlateBeanUtilResources_el extends ListResourceBundle
{
  public Object [][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Resources for ESlateBeanInfo
    //
    {"propertyChange", "Αλλαγή ιδιότητας"},
    {"mouseEntered", "Είσοδος ποντικού"},
    {"mouseExited", "Έξοδος ποντικού"},
    {"mouseMoved", "Μετακίνηση ποντικού"},
    {"vetoableChange", "Ακυρώσιμη αλλαγή ιδιότητας"},
    {"componentHidden", "Απόκρυψη ψηφίδας"},
    {"componentShown", "Εμφάνιση ψηφίδας"},
    //
    // Resources for TaggedStringPropertyEditor
    //
    {"illegalValue", "Μη επιτρεπτή τιμή"}
  };
}
