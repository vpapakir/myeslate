package gr.cti.structfile;

import java.util.*;

/**
 * Greek language localized strings for class StructFile.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class StructFileResources_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"writeProtected1", "Το αρχείο "},
    {"writeProtected2", " είναι προστατευμένο από γράψιμο"},
    {"couldNotDelete", "Δεν ήταν δυνατόν να διαγραφεί η παλιά έκδοση του "},
    {"cannotOpen1", "Το αρχείο "},
    {"cannotOpen2", " δεν μπορεί να ανοιχθεί"},
    {"badMode", "Ο τρόπος ανοίγματος πρέπει να είναι OLD ή NEW"},
    {"notOpen", "Το αρχείο είναι κλειστό"},
    {"badEntry", "Η καταχώριση δεν πρέπει να είναι null ή κενή"},
    {"nullEntry", "Η καταχώριση δεν πρέπει να είναι null"},
    {"nullFiles", "Τα αρχεία δεν πρέπει να είναι null"},
    {"equalFiles", "Τα αρχεία πρέπει να είναι διαφορετικά"},
    {"noOpenDir", "Δεν επιτρέπεται το άνοιγμα καταλόγων"},
    {"notFound1", "Η καταχώριση "},
    {"notFound2", " δεν βρέθηκε"},
    {"fileOrDir",
      "Η καταχώριση πρέπει να είναι τύπου Entry.FILE ή Entry.DIRECTORY"},
    {"anotherEntry1", "Υπάρχει άλλη καταχώριση με όνομα "},
    {"anotherEntry2", ""},
    {"isCurrentDir1", "Η καταχώριση "},
    {"isCurrentDir2", " είναι ο τρέχων κατάλογος και δεν μπορεί να διαγραφεί"},
    {"notDir1", "Η καταχώριση "},
    {"notDir2", " δεν είναι κατάλογος"},
    {"badPath",
      "Η διαδρομή πρέπει να είναι ένα διάνυσμα από διαδοχικά ονόματα"},
    {"rootDir", "Ο τρέχων κατάλογος είναι η ρίζα"},
    {"alreadyOpen", "Το δομημένο αρχείο είναι ήδη ανοικτό"},
    {"nullDestination", "Το δομημένο αρχείο προορισμού είναι null"},
    {"cantCdDest1", "Δεν είναι δυνατή η μετάβαση στον κατάλογο "},
    {"cantCdDest2", " στο αρχείο προορισμού "},
    {"cantCdSrc1", "Δεν είναι δυνατή η μετάβαση στον κατάλογο "},
    {"cantCdSrc2", " στο αρχικό αρχείο "},
    {"nullBuffer", "Το buffer είναι null"},
  };
}
