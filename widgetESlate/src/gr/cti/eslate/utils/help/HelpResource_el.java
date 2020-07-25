package gr.cti.eslate.utils.help;

import java.util.*;

/**
 * Greek language resources for the help set constructor.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class HelpResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"hsTitle", "Σελίδες βοήθειας για το μικρόκοσμο"},
    {"contents", "Περιεχόμενα"},
    {"index", "Ευρετήριο"},
    {"search", "Αναζήτηση"},
    {"selectDir", "Επιλέξτε τον κατάλογο με τα αρχεία τής βοήθειας"},
    {"adjustContents", "Ρυθμίστε τον πίνακα περιεχομένων"},
    {"ok", "Εντάξει"},
    {"cancel", "’κυρο"},
    {"up","Μετακίνηση προς τα πάνω"},
    {"down", "Μετακίνηση προς τα κάτω"},
    {"delete", "Αφαίρεση"},
    {"enterName1", "Δώστε το όνομα τής κλάσης για την οποία θα παραχθεί βοήθεια"},
    {"enterName2", "Δώστε όνομα κλάσης"},
    {"confirmOverwriteAll", "Τα αρχεία περιγραφής τής βοήθειας υπάρχουν ήδη.\nΘέλετε να τα ξαναφτιάξετε;"},
    {"confirmOverwriteSome", "Μερικά από τα αρχεία περιγραφής τής βοήθειας υπάρχουν ήδη.\nΘέλετε να τα ξαναφτιάξετε;"},
    {"confirm", "Επιβεβαίωση"},
    {"warning", "Προσοχή"},
    {"noHTML", "Αυτός ο κατάλογος δεν περιέχει κανένα αρχείο HTML"},
    {"noHTMLleft", "Πρέπει να συμπεριλάβετε τουλάχιστον ένα αρχείο HTML στη βοήθεια"},
  };
}
