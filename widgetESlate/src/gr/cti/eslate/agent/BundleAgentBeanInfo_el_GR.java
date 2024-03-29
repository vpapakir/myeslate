package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Agent BeanInfo bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class BundleAgentBeanInfo_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"componentFace",   "Όψη"},
        {"componentFaceTip","Αυτή είναι η εικόνα της οντότητας. Φαίνεται μόνο πάνω στην ψηφίδα της οντότητας."},
        {"phaseImages",     "Αναπαράσταση"},
        {"phaseImagesTip",  "Με το πλήκτρο αυτό αλλάζετε την αναπαράσταση της οντότητας"},
        {"phases",          "Φάσεις"},
        {"phasebase",       "Βασική εικόνα:"},
        {"faceauto",        "Οι άλλες εικόνες παράγονται αυτόματα"},
        {"facemanual",      "Οι άλλες εικόνες ορίζονται από το χρήστη"},
        {"definephases",    "Προσαρμογή αναπαράστασης της οντότητας (3 βήματα)"},
        {"previous",        "< Προηγούμενο"},
        {"next",            "Επόμενο >"},
        {"cancel",          "Άκυρο"},
        {"finish",          "Τέλος"},
        {"step1desc",       "BHMA 1:\n- Επιλέξτε με ποιο τρόπο θέλετε να καθορίσετε την αναπαράσταση της οντότητας (η αναπαράσταση της οντότητας είναι ο τρόπος με τον οποίο απεικονίζεται στις ψηφίδες που τη φιλοξενούν):"},
        {"step1choice1",    "\"Θα δώσω μία εικόνα και οι εικόνες των άλλων φάσεων θα παραχθούν αυτόματα.\""},
        {"step1choice2",    "\"Θα δώσω εγώ τις εικόνες όλων των φάσεων που με ενδιαφέρουν.\""},
        {"step2desc",       "ΒΗΜΑ 2:\n- Δώστε τον αριθμό των διαφορετικών φάσεων. Όσες φάσεις δοθούν, τόσες θα είναι οι διαφορετικές εικόνες της οντότητας, μία για κάθε εύρος γωνιών περιστροφής.\n\nΠαράδειγμα:\nΑν δοθούν 10 φάσεις, η οντότητα θα έχει διαφορετική εικόνα κάθε 36 μοίρες. Θα έχει, δηλαδή, μία εικόνα όταν η κατεύθυνσή της είναι από -18 έως 18 μοίρες, άλλη από 18 έως 54 κ.ο.κ."},
        {"step3desc",       "ΒΗΜΑ 3:\n- Τελειώνοντας, καθορίστε τη βασική εικόνα. Πατώντας το πλήκτρο \"Ορισμός\", μπορείτε να φτιάξετε μία καινούργια ή να χρησιμοποιήσετε μία έτοιμη εικόνα."},
        {"step3desc2",      "Η εικόνα αυτή θα χρησιμοποιηθεί για να παραχθούν αυτόματα όλες οι φάσεις της οντότητας."},
        {"step3define",     "Ορισμός"},
        {"step3phase",      "Φάση"},
        {"step3icon",       "Εικόνα"},
        {"step32desc",      "ΒΗΜΑ 3:\n- Τελειώνοντας, πρέπει να καθορίσετε τις εικόνες των φάσεων. Στον κύκλο κάθε περιοχή χρώματος δηλώνει ένα εύρος γωνιών. Όταν η οντότητα είναι στραμμένη προς αυτές τις γωνίες χρησιμοποιεί την ίδια εικόνα. Στον πίνακα αριστερά ορίζετε την εικόνα για κάθε εύρος γωνιών (φάση)."},
        {"always",          "ΠΑΝΤΑ"},
        {"never",           "ΠΟΤΕ"},
        {"onmouse",         "ΟΤΑΝ ΔΕΙΧΝΕΙ ΤΟ ΠΟΝΤΙΚΙ"},
        {"travelseverywhere","ΟΠΟΥΔΗΠΟΤΕ"},
        {"travelsonroads",  "ΔΡΟΜΟΥΣ"},
        {"travelsonrailways","ΣΙΔΗΡΟΔΡΟΜΟΥΣ"},
        {"travelsonsea",    "ΘΑΛΑΣΣΑ"},
        {"travelsonair",    "ΑΕΡΟΔΙΑΔΡΟΜΟΥΣ"},
        {"travelsoncustom", "ΑΛΛΟ ΕΠΙΠΕΔΟ (Όρισε)"},
        {"travellingOnMotionLayerID","Ταξιδεύει σε"},
        {"travellingOnMotionLayerIDTip","Επιλέξτε που θα ταξιδεύει η οντότητα"},
        {"embarksOn",       "Επιβιβάζεται σε"},
        {"embarksOnTip",    "Επιλέξτε σε ποιους τύπους οντοτήτων επιβιβάζεται η οντότητα (CTRL για πολλαπλούς)"},
        {"man",             "ΑΝΘΡΩΠΟΣ"},
        {"auto",            "ΑΥΤΟΚΙΝΗΤΟ"},
        {"train",           "ΤΡΑΙΝΟ"},
        {"ship",            "ΠΛΟΙΟ"},
        {"plane",           "ΑΕΡΟΠΛΑΝΟ"},
        {"all",             "ΟΛΕΣ ΤΙΣ ΟΝΤΟΤΗΤΕΣ"},
        {"other",           "ΑΛΛΟ (Όρισε)"},
        {"type",            "Είναι"},
        {"typeTip",         "Καθορίζει τι αντιπροσωπεύει η οντότητα"},
        {"velocity",        "Ταχύτητα κίνησης"},
        {"velocityTip",     "Είναι η ταχύτητα που θα αναπτύξει η οντότητα όταν κινηθεί σε km/h"},
        {"minVelocity",     "Ταχύτητα ελάχιστη"},
        {"minVelocityTip",  "Είναι η ελάχιστη ταχύτητα της οντότητας σε km/h"},
        {"maxVelocity",     "Ταχύτητα μέγιστη"},
        {"maxVelocityTip",  "Είναι η μέγιστη ταχύτητα της οντότητας σε km/h"},
        {"border",          "Περίγραμμα"},
        {"borderTip",       "Ελέγχει το περίγραμμα της ψηφίδας"},
        {"statusbarVisible","Ράβδος κατάστασης ορατή"},
        {"statusbarVisibleTip","Ελέγχει αν θα εμφανίζεται η ράβδος κατάστασης"},
        {"locationChanged", "Αλλαγή θέσης"},
        {"agentStopped",    "Στάση οντότητας"},
        {"geographicObjectTouched","Άγγιξε γεωγραφικό αντικείμενο"},
        {"motionObjectChanged","Αλλαγή αντικειμένου κίνησης"},
        {"agentMeeting",    "Συνάντησε άλλη οντότητα"},
        {"initLoc",         " Αρχική θέση"},
        {"initLocTip",      "Θέτει την αρχική θέση. Ενεργή μόνο όταν η οντότητα δεν έχει τοποθετηθεί."},
        {"initDefined",     "Ήδη ορισμένη"},
        {"initUndefined",   "Ορισμός"},
        {"initLongt",       "Δώσε αρχικό γεωγραφικό πλάτος (λ) ή καρτεσιανό x:"},
        {"initLat",         "Δώσε αρχικό γεωγραφικό μήκος (φ) ή καρτεσιανό y:"},
        {"invalidInit",     "Άκυρη αρχική θέση!"},
        {"alwaysVisible",   "Πάντοτε ορατή"},
        {"alwaysVisibleTip","Δηλώνει ότι η οντότητα επιθυμεί να φαίνεται σε οποιαδήποτε θέση κι αν βρίσκεται στις ψηφίδες που τη φιλοξενούν"},
        {"unitTolerance",   "\"Ενεργή\" περιοχή γύρω από το κέντρο"},
        {"unitToleranceTip","Η \"ενεργή\" περιοχή γύρω από το κέντρο σε μονάδες χάρτη"},
        {"stopAtCrossings", "Στάση στις διασταυρώσεις"},
        {"stopAtCrossingsTip","Στάση στις διακλαδώσεις η προσπάθεια εύρεσης άλλου δρόμου"},
    };
}
