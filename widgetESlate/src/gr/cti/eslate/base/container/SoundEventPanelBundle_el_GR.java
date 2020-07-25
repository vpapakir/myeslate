package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class SoundEventPanelBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Play",              "Παίξιμο"},
        {"Stop",              "Σταμάτημα"},
        {"Next",              "Επόμενο συμβάν"},
        {"Previous",          "Προηγούμενο συμβάν"},
        {"Load",              "Τοποθέτηση ήχου σε συμβάν"},
        {"Delete",            "Διαγραφή ήχουν από συμβάν"},
        {"FailureMsg",        "Δεν στάθηκε δυνατή η τοποθέτηση του ήχου"},
        {"FailureMsg1",       "στο συμβάν"},
        {"FailureMsg2",       "Αδύνατη η δημιουργία listener κλάσης"},
        {"FailureMsg3",       "Δεν στάθηκε δυνατή η αφαίρεση του ήδη προσαρτημένου listener κλάσης"},
        {"FailureMsg4",       "Δεν στάθηκε δυνατή η προσθήκη listener κλάσης"},
   };
}
