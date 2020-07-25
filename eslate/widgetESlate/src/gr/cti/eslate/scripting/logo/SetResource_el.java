package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the set component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 * @see         gr.cti.eslate.scripting.logo.SetPrimitives
 */
public class SetResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SELECTSUBSET", "ΕΠΙΛΟΓΗΥΠΟΣΥΝΟΛΟΥ"},
    {"CLEARSELECTEDSUBSET", "ΑΚΥΡΩΣΗΕΠΙΛΕΓΜΕΝΟΥΥΠΟΣΥΝΟΛΟΥ"},
    {"QUERYINSET", "ΕΡΩΤΗΣΗΣΥΝΟΛΟΥ"},
    {"DELETEELLIPSE", "ΔΙΑΓΡΑΦΗΕΛΛΕΙΨΗΣ"},
    {"SETTABLEINSET", "ΘΕΣΕΠΙΝΑΚΑΣΥΜΟΛΟΥ"},
    {"TABLEINSET", "ΠΙΝΑΚΑΣΣΥΝΟΛΟΥ"},
    {"TABLESINSET", "ΠΙΝΑΚΕΣΣΥΝΟΛΟΥ"},
    {"SETPROJECTIONFIELD", "ΘΕΣΕΠΕΔΙΟΠΡΟΒΟΛΗΣ"},
    {"PROJECTIONFIELD", "ΠΕΔΙΟΠΡΟΒΟΛΗΣ"},
    {"PROJECTIONFIELDS", "ΠΕΔΙΑΠΡΟΒΟΛΗΣ"},
    {"SETCALCULATIONTYPE", "ΘΕΣΕΤΥΠΟΥΠΟΛΟΓΙΣΜΟΥ"},
    {"CALCULATIONTYPE", "ΤΥΠΟΣΥΠΟΛΟΓΙΣΜΟΥ"},
    {"CALCULATIONTYPES", "ΤΥΠΟΙΥΠΟΛΟΓΙΣΜΟΥ"},
    {"SETCALCULATIONFIELD", "ΘΕΣΕΠΕΔΙΟΥΠΟΛΟΓΙΣΜΟΥ"},
    {"CALCULATIONFIELD", "ΠΕΔΙΟΥΠΟΛΟΓΙΣΜΟΥ"},
    {"CALCULATIONFIELDS", "ΠΕΔΙΑΥΠΟΛΟΓΙΣΜΟΥ"},
    {"PROJECTFIELD", "ΠΡΟΒΑΛΕΠΕΔΙΟ"},
    {"PROJECTINGFIELDS", "ΠΡΟΒΑΛΛΟΝΤΑΙΠΕΔΙΑ"},
    {"CALCULATEINSET", "ΥΠΟΛΟΓΙΣΕΣΤΟΣΥΝΟΛΟ"},
    {"CALCULATINGINSET", "ΥΠΟΛΟΓΙΣΜΟΙΣΤΟΣΥΝΟΛΟ"},
    {"SHOWQUERIESINSET", "ΕΜΦΑΝΙΣΗΕΡΩΤΗΣΕΩΝΣΤΟΣΥΝΟΛΟ"},
    {"SHOWINGQUERIESINSET", "ΕΜΦΑΝΙΖΟΝΤΑΙΕΡΩΤΗΣΕΙΣΣΤΟΣΥΝΟΛΟ"},
    {"NEWELLIPSE", "ΝΕΑΕΛΛΕΙΨΗ"},
    {"ACTIVATEELLIPSE", "ΕΝΕΡΓΟΠΟΙΗΣΗΕΛΛΕΙΨΗΣ"},
    {"DEACTIVATEELLIPSE", "ΑΠΕΝΕΡΓΟΠΟΙΗΣΗΕΛΛΕΙΨΗΣ"},
    {"badX", "Ακατάλληλη οριζόντια συντεταγμένη"},
    {"badY", "Ακατάλληλη κατακόρυφη συντεταγμένη"},
    {"badSelection", "Παρακαλώ δώστε ένα ζεύγος συντεταγμένων ή τρεις λογικές τιμές"},
    {"badActivation", "Παρακαλώ δώστε ένα ζεύγος συντεταγμένων ή έναν αριθμό μεταξύ 0 και 2"},
    {"badBoolean", "Παρακαλώ δώστε τρεις λογικές τιμές"},
    {"badEllipse", "Παρακαλώ δώστε έναν αριθμό μεταξύ 0 και 2"},
    {"whichSet", "Παρακαλώ ορίστε ένα σύνολο"},
    {"noTable", "Η ψηφίδα δεν απεικονίζει κανένα πίνακα"},
    {"noProjField", "Δεν υπάρχει πεδίο που να μπορεί να προβληθεί στην ψηφίδα"},
    {"noCalcOp", "Δεν είναι επιλεγμένη καμμία λειτουργία υπολογισμού"},
    {"noCalcField", "Δεν είναι διαλεγμένο κανένα πεδίο υπολογισμού"}
  };
}
