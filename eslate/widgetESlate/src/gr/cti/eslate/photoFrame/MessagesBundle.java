package gr.cti.eslate.photoFrame;


import java.util.ListResourceBundle;


public class MessagesBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"componame", "Picture Frame"},
            {"pin", "Picture filename"},
            {"imagepin", "Picture"},
            {"compo", "Photo Frame component, version"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
            {"development", "Development: G. Vasiliou"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"ConstructorTimer", "PhotoFrame constructor"},
            {"LoadTimer", "PhotoFrame load"},
            {"SaveTimer", "PhotoFrame save"},
            {"InitESlateAspectTimer", "PhotoFrame E-Slate aspect creation"},
            
            {"actorName", "Picture Frame"},
            {"x", "X"},
            {"y", "Y"},
            {"width", "Width"},
            {"height", "Height"},
            {"animationPlug", "Animation"},
        };
}
