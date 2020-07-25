package gr.cti.eslate.eslateTextField;


import java.util.ListResourceBundle;


public class BundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"Title", "Title"},
            {"NumberValue", "Numeric value"},
            {"Pressed", "Boolean"},
            // component info
            {"componentName", "Text field component"},
            {"name", "ESlateTextField"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"ProviderCase", "This plug has provider/dependent plugs connected to it. Do you wish to proceed ?"},
            {"DependentCase", "This plug has dependent plugs connected to it. Do you wish to proceed ?"},
            {"PlugDisconnection", "Plug Disconnection"},
            {"ESlateTextField", "Text Field"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets field's background"},

            {"Foreground", "Τext color"},
            {"setForegroundTip", "Sets field's text color"},

            {"CaretColor", "Caret color"},
            {"setCaretColorTip", "Sets field's caret color"},

            {"DisabledTextColor", "DisabledTextColor"},
            {"setDisabledTextColorTip", "Sets disabled text color"},

            {"SelectedTextColor", "SelectedTextColor"},
            {"setSelectedTextColorTip", "Sets selected text color"},

            {"SelectionColor", "SelectionColor"},
            {"setSelectionColorTip", "Sets color of selection"},

            {"Border", "Border"},
            {"setBorderTip", "Sets field's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets text font"},

            {"Text", "Text"},
            {"setTextTip", "Sets text"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets  tool tip text"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if field is opaque"},

            {"Editable", "Editable"},
            {"setEditableTip", "Defines if filed is editable"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if field is enabled"},

            {"CaretPosition", "Caret position"},
            {"setCaretPositionTip", "Sets field's caret position"},

            {"SelectionEnd", "Selection end"},
            {"setSelectionEndTip", "Sets selection end point"},

            {"SelectionStart", "Selection start"},
            {"setSelectionStartTip", "Sets selection start point"},

            {"Columns", "Columns"},
            {"setColumnsTip", "Sets text columns"},

            {"HorizontalAlignment", "Horizontal alignment"},
            {"setHorizontalAlignmentTip", "Sets horizontal alignment"},

            {"Name", "Name"},
            {"setNameTip", "Field's name "},

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

            {"ScrollOffset", "Scroll offset"},
            {"setScrollOffsetTip", "Defines the scroll offset"},

            {"NumberMode", "Number mode"},
            {"setNumberModeTip", "Defines whether Number mode is on or off"},

            {"FireOnEnterPress", "Distribute text on ENTER press"},
            {"setFireOnEnterPressTip", "Defines whether text is given to other components on ENTER key press, or continiously"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

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


            {"left", "LEFT"},
            {"center", "CENTER"},
            {"right", "RIGHT"},
            {"leading", "LEADING"},
            {"trailing", "TRAILING"},
            {"ConstructorTimer", "ESlateTextField constructor"},
            {"LoadTimer", "ESlateTextField load"},
            {"SaveTimer", "ESlateTextField save"},

            {"InitESlateAspectTimer", "ESlateTextField E-Slate aspect creation"},

        };

}
