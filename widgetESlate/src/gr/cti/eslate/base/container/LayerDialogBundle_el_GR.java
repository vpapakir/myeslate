package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LayerDialogBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",     "Ορισμός επιπέδων μικρόκοσμου"},
        {"OK",              "Εντάξει"},
        {"Cancel",          "Άκυρο"},
        {"NewLayer",        "Νέο επίπεδο"},
        {"UpButtonTip",     "Προώθηση επιλεγμένου επιπέδου"},
        {"DownButtonTip",   "Υποβιβασμός επιλεγμένου επιπέδου"},
        {"AddButtonTip",    "Εισαγωγή νέου επιπέδου"},
        {"DeleteButtonTip", "Διαγραφή επιλεγμένων επιπέδων"},
        {"Error",           "Λάθος"},
        {"Warning",         "Προειδοποίηση"},
        {"SameLayerNamesError","Κάποια επίπεδα έχουν κοινά ονόματα. Τα επίπεδα ενός μικρόκοσμου πρέπει να έχουν μοναδικά ονόματα."},
        {"LayersMoved0",    "Το διαγραφέν επίπεδο \""},
        {"LayersMoved1",    "\" περιέχει την ψηφίδα \""},
        {"LayersMoved2",    "\". Η ψηφίδα θα μεταφερθεί στο \"προκαθορισμένο\" επίπεδο."},
		{"NewLayerMsg",       "Εισάγετε το όνομα του νέου επιπέδου"},
		{"NewLayerTitle",     "Νέο επίπεδο"},
		{"NameExists",        "Δεν ορίστηκε το νέο επίπεδο. Υπάρχει ήδη επίπεδο με το ίδιο όνομα."},
		{"RenameLayerMsg",    "Εισάγετε το νέο όνομα του επιπέδου"},
		{"RenameLayerTitle",  "Αλλαγή ονόματος επιπέδου"},
    };
}
