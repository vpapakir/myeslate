package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

/**
 * English language resources for ESlateBeanResource.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 * @author      George Tsironis
 */
public class ESlateBeanBundle_el extends ListResourceBundle
{
  public Object [][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Events
    //
    {"actionPerformed", "Εκτέλεση ενέργειας"},
    {"componentHidden", "Απόκρυψη ψηφίδας"},
    {"componentMoved", "Μετακίνηση ψηφίδας"},
    {"componentResized", "Αλλαγή μεγέθους ψηφίδας"},
    {"componentShown", "Εμφάνιση ψηφίδας"},
    {"mouseClicked", "Κλικ ποντικού"},
    {"mouseDragged", "Σύρσιμο ποντικού"},
    {"mouseEntered", "Είσοδος ποντικού"},
    {"mouseExited", "Έξοδος ποντικού"},
    {"mouseMoved", "μετακίνηση ποντικού"},
    {"mousePressed", "Πάτημα ποντικού"},
    {"mouseReleased", "¶φημα ποντικού"},
    {"propertyChange", "Αλλαγή ιδιότητας"},
    {"vetoableChange", "Ακυρώσιμη αλλαγή ιδιότητας"},
    //
    // Properties
    //
    {"opaque", "Αδιαφανής"},
    {"visible", "Ορατή"},
    {"border", "Περίγραμμα"}
  };
}
