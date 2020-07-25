package gr.cti.eslate.eslateTextArea;


import java.util.ListResourceBundle;


public class BundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"Title", "Title"},
            {"Text File", "Text file path"},
            {"Pressed", "Boolean"},
            // component info
            {"componentName", "Text component"},
            {"name", "ESlateTextArea"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},

            {"fileDoesNotExist", "File {0} does not exist"},


            //Bean Info
            {"ESlateTextArea", "Text area"},

            {"GIF Files", "GIF Files"},
            {"JPEG Files", "JPEG Files"},
            {"PNG Files", "PNG Files"},

            {"if needed", "IF NEEDED"},
            {"never", "NEVER"},
            {"always", "ALWAYS"},

            {"File", "File"},
            {"setFileTip", "Sets the file"},

            {"BackgroundImageFile", "Background image file"},
            {"setBackgroundImageFileTip", "Sets area's background image"},

            {"TextFile", "Text file"},
            {"setTextFileTip", "Sets area's text file resource"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets area's background"},

            {"Foreground", "Τext color"},
            {"setForegroundTip", "Sets area's text color"},

            {"CaretColor", "Caret color"},
            {"setCaretColorTip", "Sets area's caret color"},

            {"DisabledTextColor", "DisabledTextColor"},
            {"setDisabledTextColorTip", "Sets disabled text color"},

            {"SelectedTextColor", "SelectedTextColor"},
            {"setSelectedTextColorTip", "Sets selected text color"},

            {"SelectionColor", "SelectionColor"},
            {"setSelectionColorTip", "Sets color of selection"},

            {"Border", "Border"},
            {"setBorderTip", "Sets area's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets text font"},

            {"Text", "Text"},
            {"setTextTip", "Sets text"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets  tooltiptext"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if area is opaque"},

            {"Editable", "Editable"},
            {"setEditableTip", "Defines if area is editable"},

            {"LineWrap", "LineWrap"},
            {"setLineWrapTip", "Defines if linewrap is enabled"},

            {"WrapStyleWord", "WrapStyleWord"},
            {"setWrapStyleWordTip", "Defines the style of wrapping used if the text area is wrapping lines"},

            {"CaretPosition", "Caret position"},
            {"setCaretPositionTip", "Sets area's caret position"},

            {"TabSize", "Tab size"},
            {"setTabSizeTip", "Sets tab size"},

            {"SelectionEnd", "Selection end"},
            {"setSelectionEndTip", "Sets selection end point"},

            {"SelectionStart", "Selection start"},
            {"setSelectionStartTip", "Sets selection start point"},

            {"Columns", "Columns"},
            {"setColumnsTip", "Sets text columns"},

            {"Rows", "Rows"},
            {"setRowsTip", "Sets text rows"},

            {"Name", "Name"},
            {"setNameTip", "area's name "},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving button should use a buffer to paint"},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"MaximumSize", "Maximum size"},
            {"setMaximumSizeTip", "Defines maximum size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Defines minimum size"},

            {"Margin", "Margin"},
            {"setMarginTip", "Defines margin"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"FireOnEnterPress", "Distribute text on ENTER press"},
            {"setFireOnEnterPressTip", "Defines whether text is given to other components on ENTER key press, or continiously"},

            {"HorizontalScrollBarPolicy", "Horizontal scrollbar policy"},
            {"setHorizontalScrollBarPolicyTip", "Sets the horizontal scrollbar policy"},

            {"VerticalScrollBarPolicy", "Vertical scrollbar policy"},
            {"setVerticalScrollBarPolicyTip", "Sets the vertical scrollbar policy"},

            //action Event
            {"ActionPerformed", "Action performed"},
            {"setActionPerformedTip", "An action happened"},

            {"CaretUpdate", "Caret updated"},
            {"setCaretUpdateTip", "Caret was updated"},


            {"KeyPressed", "Key pressed"},
            {"setKeyPressedTip", "A key was pressed "},

            {"KeyReleased", "Key released"},
            {"setKeyReleasedTip", "A pressed key was released "},

            {"KeyTyped", "Key typed"},
            {"setKeyTypedTip", "A key was typed "},

            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},


            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},

            {"ConstructorTimer", "ESlateTextArea constructor"},
            {"LoadTimer", "ESlateTextArea load"},
            {"SaveTimer", "ESlateTextArea save"},

            {"InitESlateAspectTimer", "ESlateTextArea E-Slate aspect creation"},


        };

}
