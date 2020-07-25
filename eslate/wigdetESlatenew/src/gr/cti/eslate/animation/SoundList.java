package gr.cti.eslate.animation;

import java.applet.AudioClip;
import javax.swing.*;
import java.net.URL;

/**
 * Loads and holds a bunch of audio files.
 * @author	Augustine Grillakis
 * @version	1.0.0, 27-Jun-2002
 * @see     gr.cti.eslate.animation.Animation
 */
class SoundList extends java.util.Hashtable {
    JApplet applet;

    public SoundList() {
        super();
    }

    public void startLoading(URL fileURL, String fileName) {
        new SoundLoader(this, fileURL, fileName);
    }

    public AudioClip getClip(String fileName) {
        return (AudioClip)get(fileName);
    }

    public void putClip(AudioClip clip, String fileName) {
        put(fileName, clip);
    }
}