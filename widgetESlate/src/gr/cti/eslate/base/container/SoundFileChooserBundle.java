package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class SoundFileChooserBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"WAV",               "WAV sound files"},
        {"MIDI",              "MIDI sound files"},
        {"AU",                "AU sound files"},
        {"RMF",               "RMF sound files"},
        {"AIFF",              "AIFF sound files"},
        {"All",               "All sound files"},
        {"AllFiles",          "All files"},
        {"Load Sound",        "Load sound"},
    };
}