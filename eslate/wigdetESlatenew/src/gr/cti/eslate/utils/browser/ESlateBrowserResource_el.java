package gr.cti.eslate.utils.browser;

import java.util.*;

/**
 * Greek language localized strings for ESlateBrowser and its subclasses.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.12, 31-Oct-2006
 */
public class ESlateBrowserResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Location
    {"changedLocation", "Αλλαγή τόπου"},
    {"changingLocation", "Επεικείμενη αλλαγή τόπου"},
    // Title
    {"changedTitle", "Αλλαγή τίτλου"},
    // Status text
    {"changedStatusText", "Αλλαγή κειμένου κατάστασης"},
    // WebBrowserEvents
    {"documentCompleted", "Ολοκλήρωση εγγράφου"},
    {"downloadCompleted", "Ολοκλήρωση λήψης"},
    {"downloadError", "Σφάλμα λήψης"},
    {"downloadProgress", "Πρόοδος λήψης"},
    {"downloadStarted", "Εκκίνηση λήψης"},
    {"statusTextChange", "Αλλαγή κειμένου κατάστασης"},
    {"titleChange", "Αλλαγή τίτλου"},
    // HistoryChanged
    {"historyChanged", "Μεταβολή ιστορικού"},
    // LinkFollowed
    {"linkFollowed", "Ακολούθηση συνδέσμου"}
  };
}
