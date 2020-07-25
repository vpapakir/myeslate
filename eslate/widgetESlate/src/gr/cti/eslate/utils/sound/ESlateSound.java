package gr.cti.eslate.utils.sound;

import javax.sound.sampled.Clip;
import javax.sound.midi.Sequencer;
import java.util.ArrayList;


/** This class encapsulates either a javax.sound.sampled.Clip or a javax.sound.midi.Sequencer,
 *  which are the basic sound classes of the Java Sound API. It also provides sound finish
 *  tracking, through the ESlateSoundListener class.
 *
 */
public class ESlateSound {
    public static final int CLIP_TYPE = 1;
    public static final int SEQ_TYPE = 2;

    Clip clip;
    Sequencer sequencer;
    private int type = CLIP_TYPE;
    ArrayList<ESlateSoundListener> soundListeners =
      new ArrayList<ESlateSoundListener>();

    public ESlateSound(Object sound) {
        if (sound instanceof Clip)
            clip = (Clip) sound;
        else{
            sequencer = (Sequencer) sound;
            type = SEQ_TYPE;
        }
    }

    /** Returns the type of the Java sound. One of CLIP_TYPE, SEQ_TYPE.
     */
    public int getType() {
        return type;
    }

    /** Returns the actual sound object, which may be either a Clip or a Sequencer.
     */
    public Object getSound() {
        if (type == CLIP_TYPE)
            return clip;
        return sequencer;
    }

    public synchronized void addSoundListener(ESlateSoundListener l) {
        if (soundListeners.indexOf(l) == -1)
            soundListeners.add(l);
    }


    public void removeSoundListener(ESlateSoundListener l) {
        int index = soundListeners.indexOf(l);
        if (index != -1)
            soundListeners.remove(index);
    }

    @SuppressWarnings(value={"unchecked"})
    protected void fireSoundStopped() {
        if (soundListeners.size() == 0) return;
        ESlateSoundEvent e = new ESlateSoundEvent(this);
        ArrayList<ESlateSoundListener> sls;
        synchronized(this) {
            sls = (ArrayList<ESlateSoundListener>)soundListeners.clone();
        }
        for (int i=0; i<sls.size(); i++)
            sls.get(i).soundStopped(e);
    }


    /** Notification method that get's called whenever the encapluslated sound play
     *  is stopped (interrupted or finished). This method may be overidden to provide
     *  custom behaviour.
     */
    public void soundStopped() {
    }
}
