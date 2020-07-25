package gr.cti.eslate.base.help;

import java.util.ListResourceBundle;

/**
 * Greek language error message resources for the E-Slate help system.
 *
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ErrorMessageBundle_el_GR extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"fileNotFound", "Δεν βρέθηκε το αρχείο"},
    {"folderNotFound", "Δεν βρέθηκε ο κατάλογος"},
    {"comp", "για την ψηφίδα με helpset file"},
    {"attentionFile", "Πρόσεξτε μήπως το αρχείο που δηλώνεται στο αντίστοιχο helpset file είναι διαφορετικό από αυτό που χρησιμοποιείτε"},
    {"attentionFolder", "Πρόσεξτε μήπως ο κατάλογος που δηλώνεται στο αντίστοιχο helpset file είναι διαφορετικός από αυτόν που χρησιμοποιείτε ή δεν φτιάχθηκε ορθά από τον jhindexer"},
    {"none", "\nΔεν βρέθηκε κανένα από τα αναμενόμενα αρχεία\n"},
    {"or", "ή"},
    {"dir", "στον κατάλογο help της ψηφίδας "},
    {"orDir", "ή στον κατάλογο"},
    {"inClassPath", "στο class path"},
    {"notCorrespond", "δεν αντιστοιχεί στο αρχείο "},
    {"mapref",  "που περιγράφεται στο πεδιο mapref του "},
    {"file", "το αρχείο "},
    {"either1", "\nΕίτε δεν βρέθηκε το αρχείο "},
    {"either2", ""},
    {"or2", "είτε"},
    {"search", "για τη λειτουργία <αναζήτηση> της ψηφίδας ή δεν φτιάχθηκε ορθά με τον jhindexer"},
    {"wrong", "Υπάρχει λάθος στη βοήθεια της ψηφίδας"},
    {"attention", "Προσοχή"},
    {"OK", "Εντάξει"},
    {"continue", "H βοήθεια θα συνεχιστεί για τις υπόλοιπες ψηφίδες"},
    {"impossible", "Η βοήθεια δεν μπορεί να φορτωθεί"},
    {"toc", "Ο κατάλογος περιεχομένων είναι άδειος"},
    {"rightInfo", "ή αν περιέχει σωστά δημιουργημένες πληροφορίες"},
    {"linkNotFound", "το σχετικό έγγραφο δεν βρέθηκε"},
    {"noHelpSupported", "Δεν υπάρχει διαθέσιμη βοήθεια"},
    {"noStartingPage", "H αρχική σελίδα δεν έχει δηλωθεί σωστά"},
    {"noStartingPageDetails", "To συμβολικό όνομα που έχει δηλωθεί στο πεδίο <HOMEID>...</HOMEID> του "},
    {"noStartingPageDetails2", "δεν αντιστοιχεί σε μια υπαρκτή πληροφορία"}
  };
}
