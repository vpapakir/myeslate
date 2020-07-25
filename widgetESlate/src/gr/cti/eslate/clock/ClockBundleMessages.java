package gr.cti.eslate.clock;


import java.util.ListResourceBundle;


public class ClockBundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        System.out.println("Got in here");
        return contents;
    }

    static final Object[][] contents = {
            {"Time", "Time"},
            {"Date", "Date"},
            {"Color", "Color"},
            {"PM", "P.M."},
            {"AM", "A.M."},
            {"tick", "Tick"},
            {"hours", "Hours"},
            {"mins", "Minutes"},
            {"secs", "Seconds"},
            // component info
            {"componentName", "Clock component"},
            {"name", "Clock"},
            {"E-Slate", "E-Slate"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th.Mantes, K.Kyrimis"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"Clock", "Clock"},

            {"DateFormat", "Date format"},
            {"setDateFormatTip", "Sets the pattern of the date"},

            {"SystemDate", "System date"},
            {"setSystemDateTip", "Sets the clock's date to be the system date"},

            {"DateVisible", "Date visible"},
            {"setDateVisibleTip", "Sets whether the date is visible in analog appearance"},

            {"ShowHourNumbers", "Show hour numbers"},
            {"setShowHourNumbersTip", "Determines whether hour numbers or hour marks are shown"},

            {"ShowMinNumbers", "Show minute numbers"},
            {"setShowMinNumbersTip", "Determines whether minute numbers or minute marks are shown"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets component's background"},

            {"Foreground", "Text color"},
            {"setForegroundTip", "Sets button's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets component's Border"},

            {"DigitalReedingsFont", "Digital clock font"},
            {"setDigitalReedingsFontTip", "Sets the font of the digital face of the clock"},

            {"LogoTextFont", "Analog clock text font"},
            {"setLogoTextFontTip", "Sets the font of the analog face of the clock"},

            {"Appearance", "Appearance (analog/digital)"},
            {"setAppearanceTip", "Sets clocks's appearance"},

            {"SystemTimeCounting", "Display system time"},
            {"setSystemTimeCountingTip", "Enables system time display feature"},

            {"BackgroundImageIcon", "Background image"},
            {"setBackgroundImageIconTip", "Sets background image"},

            {"Text", "Text"},
            {"setTextTip", "Sets analog clocks's logo text"},

            {"HourHandColor", "Color of hours hand"},
            {"setHourHandColorTip", "Sets the color of the hours hand"},

            {"MinuteHandColor", "Color of minutes hand"},
            {"setMinuteHandColorTip", "Sets the color of the minutes hand"},

            {"SweepHandColor", "Color of seconds hand"},
            {"setSweepHandColorTip", "Sets the color of the seconds hand"},

            {"FaceColor", "Color of clock's disk"},
            {"setFaceColorTip", "Sets the color of the analog clock's disk"},

            {"CaseColor", "Color of clock's ring margin"},
            {"setCaseColorTip", "Sets the color of the analog clock's ring margin"},

            {"TextColor", "Color of clock's text"},
            {"setTextColorTip", "Sets the color of the clock's text"},

            {"ContentAreaFilled", "Content area filled"},
            {"setContentAreaFilledTip", "Defines if inner area is color-filled"},

            {"Selected", "Selected"},
            {"setSelectedTip", "Sets button to be selected"},

            {"RolloverEnabled", "Rollover enabled"},
            {"setRolloverEnabledTip", "Button's responce when mouse is passing over "},

            {"VerticalAlignment", "Vertical alignment"},
            {"setVerticalAlignmentTip", "Defines Vertical alignment"},

            {"VerticalTextPosition", "Vertical text position"},
            {"setVerticalTextPositionTip", "Defines Vertical text position"},

            {"HorizontalTextPosition", "Horizontal text position"},
            {"setHorizontalTextPositionTip", "Defines Horizontal text position"},

            {"HorizontalAlignment", "Horizontal alignment"},
            {"setHorizontalAlignmentTip", "Defines Horizontal alignment"},

            {"FocusPainted", "Focus painted"},
            {"setFocusPaintedTip", "Defines if button is focus painted or not"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if clock is opaque"},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving button should use a buffer to paint"},

            {"Layout", "Layout"},
            {"setLayoutTip", "Defines space layout"},

            {"Margin", "Margin"},
            {"setMarginTip", "Defines margin"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if button is enabled"},

            //action Event
            {"ActionPerformed", "Action performed"},
            {"setActionPerformedTip", "An action happened"},

            {"componentHidden", "Component hidden"},
            {"setcomponentHiddenTip", "Makes component invisible"},

            {"componentShown", "Component shown"},
            {"setcomponentShownTip", "Makes component visible"},

            {"mouseEntered", "Mouse entered"},
            {"setmouseEnteredTip", "Mouse entered component's occupied space"},

            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},


            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},

            {"MouseClicked", "Mouse Clicked"},
            {"setMouseClickedTip", "Mouse was clicked inside component's area"},

            {"mouseExited", "Mouse exited"},
            {"setmouseExitedTip", "Mouse left component's occupied space"},

            {"mouseMoved", "Mouse moved"},
            {"setmouseMovedTip", "Mouse has moved"},

            {"propertyChange", "Property change"},
            {"setpropertyChangeTip", "A component's property has changed"},

            {"vetoableChange", "Vetoable change"},
            {"setvetoableChangeTip", "A change that is approved/disapproved"},

            {"Time", "Time"},
            {"setTimeTip", "Sets clock time"},
            {"setDateTip", "Sets clock date"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},


            {"Month0", "January"},
            {"Month1", "February"},
            {"Month2", "March"},
            {"Month3", "April"},
            {"Month4", "May"},
            {"Month5", "June"},
            {"Month6", "July"},
            {"Month7", "August"},
            {"Month8", "September"},
            {"Month9", "October"},
            {"Month10", "November"},
            {"Month11", "December"},
            {"left", "LEFT"},
            {"center", "CENTER"},
            {"right", "RIGHT"},
            {"leading", "LEADING"},
            {"trailing", "TRAILING"},

            {"top", "TOP"},
            {"bottom", "BOTTOM"},

            {"ConstructorTimer", "Clock constructor"},
            {"LoadTimer", "Clock load"},
            {"SaveTimer", "Clock save"},

            {"InitESlateAspectTimer", "Clock E-Slate aspect creation"},

        };

}
