package gr.cti.eslate.base.container;

import java.util.*;

/**
 * Greek language localized strings for the performance manager.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PerformanceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"idInUse1", "Ο αριθμός "},
    {"idInUse2", " είναι ήδη σε χρήση"},
    {"cycle", "Η προσθήκη αυτής τής ομάδας θα δημιουργούσε κύκλο"},
    {"nullName", "Το όνομα που δόθηκε είναι null"},
    {"nullPath", "Η διαδρομή που δόθηκε είναι null"},
    {"nullGroup", "Το PerformanceTimerGroup που δόθηκε είναι null"},
    {"nullMW", "Ο μικρόκοσμος που δόθηκε είναι null"},
    {"badPolicy", "¶γνωστη τακτική"},
    {"realTime1", "Πραγματικός χρόνος χρονομετρητή "},
    {"realTime2", " ψηφίδας "},
    {"realTime3", ""},
    {"accTime1", "Συγκεντρωτικός χρόνος χρονομετρητή "},
    {"accTime2", " ψηφίδας "},
    {"accTime3", ""},
    {"time1", "Χρόνος χρονομετρητή "},
    {"time2", " ψηφίδας "},
    {"time3", ""},
    {"accTime1", "Αθροιστικός χρόνος χρονομετρητή "},
    {"accTime2", " ψηφίδας "},
    {"accTime3", ""},
    {"constructor", "Κατασκευαστής"},
    {"loadState", "Ανάγνωση κατάστασης"},
    {"saveState", "Αποθήκευση κατάστασης"},
    {"initESlate", "Αρχικοποίηση αβακιακής πλευράς"},
    {"exists", "Ήδη υπάρχει μία ομάδα γι' αυτή την ψηφίδα κάτω από τη συγκεκριμένη κατηγορία"},
    {"constrTime1", "Χρόνος κατασκευής "},
    {"eSlateTime1", "Χρόνος κατασκευής αβακιακής πλευράς "},
    {"object", " αντικειμένου"},
    {"objects", " αντικειμένων"},
    {"constrTime2", " τής κλάσης "},
    {"constrTime3", ": "},
    {"constrTime4", " ms, μέσος χρόνος: "},
    {"constrTime5", " ms"},
    {"badGroupID", "Λάθος αριθμός ομάδας"},
  };
}
