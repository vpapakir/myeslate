package gr.cti.eslate.utils.sound;

import java.util.ListResourceBundle;


public class SoundUtilsBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"File",              "File"},
        {"ErrorMsg1",         "Invalid midi file"},
        {"ErrorMsg2",         "Unupported sound file format"},
        {"ErrorMsg3",         "Unable to load sound from file"},
        {"ErrorMsg4",         "not found"},
        {"ErrorMsg5",         "Unable to find sound file"},
        {"ErrorMsg6",         "in microworld file"},
        {"ErrorMsg7",         "Error while opening stream to sound file"},
        {"ErrorMsg8",         "Unable to open microworld file"},
    };
}
