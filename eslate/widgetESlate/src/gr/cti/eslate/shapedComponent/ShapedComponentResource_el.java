package gr.cti.eslate.shapedComponent;

import java.util.*;

/**
 * Greek language localized strings for shaped components.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 24-May-2006
 */
public class ShapedComponentResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"pathDialogTitle", "Διαμόρφωση σχήματος πλαισίου"},
    {"add", "Προσθήκη σημείου"},
    {"del", "Αφαίρεση σημείου"},
    {"move", "Μετακίνηση σημείου"},
    {"select", "Επιλογή σημείου"},
    {"ok", "Εντάξει"},
    {"cancel", "’κυρο"},
    {"rectangle", "Ορθογώνιο"},
    {"ellipse", "Έλλειψη"},
    {"polygon", "Πολύγωνο"},
    {"freehand", "Ελεύθερο σχέδιο"},
    {"edit", "Διαμόρφωση"},
    {"notPolygon", "Αυτό το σχήμα δεν είναι πολύγωνο"},
  };
}
