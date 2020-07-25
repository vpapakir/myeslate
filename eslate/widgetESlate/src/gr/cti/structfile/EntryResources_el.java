package gr.cti.structfile;

import java.util.*;

/**
 * Greek language localized strings for class Entry.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class EntryResources_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"badName", "Το όνομα της καταχωρίσεως πρέπει να μην είναι κενό"},
    {"badLocation", "Η τοποθεσία πρέπει να είναι μεγαλύτερη ή ίση του 0"},
    {"badSize", "Το μέγεθος πρέπει να είναι μεγαλύτερο ή ίσο του 0"},
    {"badType", "Ο τύπος της καταχωρίσεως πρέπει να είναι FILE ή DIRECTORY"},
    {"root", "<ρίζα>"},
    {"file", "ΑΡΧΕΙΟ"},
    {"directory", "ΚΑΤΑΛΟΓΟΣ"},
    {"entry", "Καταχώριση"},
    {"size", "μέγεθος"},
    {"type", "τύπος"},
    {"location", "τοποθεσία"},
    {"parent", "γονεύς"},
    {"notDir1", "Η καταχώριση "},
    {"notDir2", " δεν είναι κατάλογος"},
  };
}
