package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class SoundEventPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Play",              "Play"},
        {"Stop",              "Stop"},
        {"Next",              "Next event"},
        {"Previous",          "Previous event"},
        {"Load",              "Attach sound to event"},
        {"Delete",            "Detach event sound"},
        {"FailureMsg",        "Unable to attach sound"},
        {"FailureMsg1",       "to event"},
        {"FailureMsg2",       "Unable to create listener of class"},
        {"FailureMsg3",       "Unable to remove the already attached listener of class"},
        {"FailureMsg4",       "Unable add listener of class"},
    };
}
