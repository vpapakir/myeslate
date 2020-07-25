package gr.cti.eslate.animation;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Loads and an audio file.
 * @author	Augustine Grillakis
 * @version	1.0.0, 27-Jun-2002
 * @see     gr.cti.eslate.animation.SoundList
 */
class SoundLoader /*extends Thread*/ {
    SoundList soundList;
    URL fileURL;
    String fileName;

    public SoundLoader(SoundList soundList,
                       URL fileURL, String fileName) {
        this.soundList = soundList;
        this.fileURL = fileURL;
        this.fileName = fileName;
//        setPriority(MIN_PRIORITY);
//        start();
        run();
    }

    public void run() {
        AudioClip audioClip = Applet.newAudioClip(fileURL);
        soundList.putClip(audioClip, fileName);
    }
}