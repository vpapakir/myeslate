package gr.cti.eslate.eslateComboBox;


import java.util.ListResourceBundle;


public class ComboBoxBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"List Element", "List Element"},

            {"Define Elements", "Define Elements.."},
            {"DialogTitle", "Define ComboBox list elements"},
            {"OK", "OK"},
            {"Cancel", "Cancel"},
            {"Input", "Element modification"},
            {"New element", "New element"},
            {"UpButtonTip", "Advance selected element"},
            {"DownButtonTip", "Denote selected elements"},
            {"AddButtonTip", "Insert new element"},
            {"DeleteButtonTip", "Remove selected elements"},

            {"componentName", "Combo Βox component"},
            {"name", "ESlateComboBox"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th.Mantes"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"ESlateComboBox", "ComboBox"},

            {"Model", "Model"},
            {"setModelTip", "Sets ComboBox's model"},

            {"Editable", "Editable"},
            {"setEditableTip", "Sets editabiliy"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if button is enabled"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets combobox's background"},

            {"Foreground", "Text color"},
            {"setForegroundTip", "Sets combobox's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets combobox's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets combobox's Font"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets combobox's tooltiptext"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if combobox is opaque"},

            {"Name", "Name"},
            {"setNameTip", "Combobox's name "},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving combobox should use a buffer to paint"},

            {"PopupVisible", "Visible popup"},
            {"setPopupVisibleTip", "Sets popup visibility"},

            {"MaximumRowCount", "Maximum row count"},
            {"setMaximumRowCountTip", "Sets maximum row count"},

            {"MaximumSize", "Μaximum size"},
            {"setMaximumSizeTip", "Sets maximum size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Sets minimum size "},

            {"SelectedIndex", "Selected index"},
            {"setSelectedIndexTip", "Sets the selected index "},

            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"ItemStateChanged", "Item state changed"},
            {"setItemStateChangedTip", "The components state was changed"},


            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},

            {"SelectedItemChanged", "Selected item changed"},
            {"SelectedItemChangedTip", "Selected item has changed"},

            {"ActionPerformed", "Action performed"},
            {"setActionPerformedTip", "An action happened"},

            {"ConstructorTimer", "ESlateComboBox constructor"},
            {"LoadTimer", "ESlateComboBox load"},
            {"SaveTimer", "ESlateComboBox save"},

            {"InitESlateAspectTimer", "ESlateComboBox E-Slate aspect creation"},
        };
}
