package gr.cti.structfile;

import java.util.*;

/**
 * Greek language localized strings for structured file utilities.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class UtilResources_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Resources for List
    {"listUsage", "Χρήση: List αρχείο"},

    // Resources for Defragment
    {"defragUsage", "Χρήση: Defragment αρχείο προσωρινό_αρχείο"},
    {"deleteFailed1", "Η διαγραφή του αρχείου "},
    {"deleteFailed2", " απέτυχε"},
    {"renameFailed1", "Η μετονομασία του αρχείου "},
    {"renameFailed2", " σε "},
    {"renameFailed3", " απέτυχε "},

    // Resources for FileTool
    {"fileToolName", "Διαχείριση δομημένου αρχείου"},
    {"file", "Αρχείο"},
    {"new", "Νέο"},
    {"open", "’νοιγμα"},
    {"close", "Κλείσιμο"},
    {"newFolder", "Νέος κατάλογος"},
    {"delete", "Διαγραφή"},
    {"addFiles", "Προσθήκη αρχείων"},
    {"addFolder", "Προσθήκη καταλόγου"},
    {"optimize", "Βελτιστοποίηση αρχείου"},
    {"exit", "Έξοδος"},
    {"help", "Βοήθεια"},
    {"about", "Πληροφορίες..."},
    {"createFile", "Δημιουργία δομημένου αρχείου"},
    {"create", "Δημιουργία"},
    {"openFile", "’νοιγμα δομημένου αρχείου"},
    {"ok", "Εντάξει"},
    {"error", "Σφάλμα"},
    {"yes", "Ναι"},
    {"no", "Όχι"},
    {"confirm", "Επιβεβαίωση"},
    {"confirmDelete1", "Είστε βέβαιοι ότι θέλετε να σβήσετε το αρχείο"},
    {"confirmDelete2", ";"},
    {"confirmDeleteAll1", "Είστε βέβαιοι ότι θέλεε να σβήσετε τον κατάλογο"},
    {"confirmDeleteAll2", "και όλα του τα περιεχόμενα;"},
    {"confirmDeleteContents", "Είστε βέβαιοι ότι θέλετε να σβήσετε όλα τα περιεχόμενα αυτού του αρχείου?"},
    {"notDir1", "Η καταχώριση"},
    {"notDir2", "δεν είναι κατάλογος"},
    {"newFolderName", "Νέος Κατάλογος"},
    {"noEntry", "Δεν έχετε επιλέξει καμμία καταχώριση"},
    {"add", "Προσθήκη"},
    {"notExist1", "Το αρχείο"},
    {"notExist2", "δεν υπάρχει"},
    {"notExist3", "Ο κατάλογος"},
    {"notExist4", "δεν υπάρχει"},
    {"confirmOverWrite1", "Είστε βέβαιοι ότι θέλετε να αντικαταστήσετε τα περιεχόμενα του αρχείου"},
    {"confirmOverWrite2", ";"},
    {"confirmOverWrite3", "Είστε βέβαιοι ότι θέλετε να αντικαταστήσετε τον κατάλογο"},
    {"confirmOverWrite4", "με το αρχείο"},
    {"confirmOverWrite5", ";"},
    {"confirmOverWrite6", "Είστε βέβαιοι ότι θέλετε να αντικαταστήσετε το αρχείο"},
    {"confirmOverWrite7", "με τον κατάλογο"},
    {"confirmOverWrite8", ";"},
    {"fileToolAbout0", "Περί του FileTool"},
    {"fileToolAbout1", "Εργαλείο διαχειρίσεως δομημένων αρχείων, έκδοση"},
    {"fileToolAbout2", "Σχεδιασμός & ανάπτυξη: Κ. Κυρίμης"},
    {"fileToolAbout3", "© 1999-2002 Ινστιτούτο Τεχνολογίας Υπολογιστών"},
    {"formatVersion1", "Το αρχείο είναι σε μορφή έκδοσης "},
    {"formatVersion2", ""},
  };
}
