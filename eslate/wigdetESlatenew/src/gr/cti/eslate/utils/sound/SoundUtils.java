package gr.cti.eslate.utils.sound;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Vector;
import gr.cti.structfile.*;

public class SoundUtils {

    static ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.utils.sound.SoundUtilsBundle", Locale.getDefault());

        /** Plays the sound.
         */
        public static ESlateSound playSound(final ESlateSound eslateSound) {
                if (eslateSound!=null) {
                        if (eslateSound.getType()==ESlateSound.CLIP_TYPE) {
                                Clip clip=(Clip) eslateSound.getSound();
                clip.start();
                                clip.addLineListener(new LineListener() {
                                        public void update(LineEvent event) {
                                                if (event.getType()==LineEvent.Type.STOP) {
                                                        Clip clip=(Clip)event.getSource();
                                                        clip.close();
                                                        clip.removeLineListener(this);
                                                        eslateSound.fireSoundStopped();
                                                        eslateSound.soundStopped();
                                                }
                                        }
                                });
                        } else {
                                Sequencer sequencer=(Sequencer) eslateSound.getSound();
                                sequencer.start();
                                sequencer.addMetaEventListener(new CloseMetaEventListener(sequencer, eslateSound));
                        }
                }
                return eslateSound;
        }
        /** Loads the sound from the specified file path and plays it.
         */
        public static ESlateSound playSound(String sound) {
                ESlateSound eslateSound=getSound(sound);
                playSound(eslateSound);
                return eslateSound;
        }

        /** Loads the sound from the specified file path.
         */
        public static ESlateSound getSound(String sound) {
        if (sound == null || sound.trim().length() == 0)
            return null;

        Object audio = null;
        if (sound.startsWith("http")) {
            try {
                URL url = new URL(sound);
                audio = AudioSystem.getAudioInputStream(url);
            }catch(Exception ex) {
                try {
                    URL url = new URL(sound);
                    audio = MidiSystem.getSequence(url);
                }catch (InvalidMidiDataException imde) {
                    throw new RuntimeException(bundle.getString("ErrorMsg1") + ' ' + sound);
                }catch (Exception ex2) {
                    throw new RuntimeException(bundle.getString("ErrorMsg2") + ". " + bundle.getString("File") + ": " + sound);
                }
            }
        }else{
            File soundFile = new File(sound);
            if (!soundFile.exists())
                throw new RuntimeException(bundle.getString("File") + ' ' + sound + ' ' + bundle.getString("ErrorMsg4") + '.');
            try {
                audio = AudioSystem.getAudioInputStream(soundFile);
            }catch(Exception ex) {
                try {
                    FileInputStream is = new FileInputStream(soundFile);
                    audio = new BufferedInputStream(is,1024);
                }catch (Exception imde) {
                    throw new RuntimeException(bundle.getString("ErrorMsg2") + ". " + bundle.getString("File") + ": " + sound);
                }
            }
        }

        return internalGetSound(audio, sound);
    }

    /** Loads the sound from the specified path in a structured storage file and plays it.
     */
    @SuppressWarnings("unchecked")
    public static ESlateSound playSound(StructFile file, String structFileName, Vector absolutePath) {
            ESlateSound eslateSound=getSound(file,structFileName,absolutePath);
            playSound(eslateSound);
            return eslateSound;
    }

    /** Loads the sound from the specified path in a structured storage file.
     */
    @SuppressWarnings("unchecked")
    public static ESlateSound getSound(StructFile file, String structFileName, Vector absolutePath) {
//System.out.println("absolutePath: " + absolutePath);
        if (absolutePath == null || file == null || absolutePath.size() == 0)
            return null;

        Object audio = null;
        if (!file.isOpen()) {
            try{
                file.open(structFileName, StructFile.OLD);
            }catch (Throwable thr) {
                throw new RuntimeException(bundle.getString("ErrorMsg8") + ' ' + structFileName);
            }
        }
        Entry soundEntry = null;
        try{
            soundEntry = file.findEntry(absolutePath, false);
        }catch (Exception exc) {
            throw new RuntimeException(bundle.getString("ErrorMsg5") + ' ' + absolutePath + ' ' + bundle.getString("ErrorMsg6"));
        }

        BufferedInputStream soundStream = null;
        try{
            soundStream = new BufferedInputStream(new StructInputStream(file, soundEntry));
        }catch (Throwable thr) {
            throw new RuntimeException(bundle.getString("ErrorMsg7") + ' ' + absolutePath + ' ' + bundle.getString("ErrorMsg6"));
        }

        try{
            audio = AudioSystem.getAudioInputStream(soundStream);
        }catch(Exception ex) {
            try {
                audio = MidiSystem.getSequence(soundStream);
            }catch (InvalidMidiDataException imde) {
                throw new RuntimeException(bundle.getString("ErrorMsg1") + ' ' + absolutePath);
            }catch (Exception ex2) {
                throw new RuntimeException(bundle.getString("ErrorMsg2") + ". " + bundle.getString("File") + ": " + absolutePath);
            }
        }

        return internalGetSound(audio, absolutePath);
    }

    private static ESlateSound internalGetSound(Object audio, Object soundPath) {
        Clip clip = null;
        Sequencer sequencer = null;

        if (audio instanceof AudioInputStream) {
            AudioInputStream audio1 = (AudioInputStream) audio;
            AudioFormat format = audio1.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW)
            || (format.getEncoding() == AudioFormat.Encoding.ALAW))  {
                AudioFormat tmp = new AudioFormat(
                                      AudioFormat.Encoding.PCM_SIGNED,
                                      format.getSampleRate(),
                                      format.getSampleSizeInBits()*2,
                                      format.getChannels(),
                                      format.getFrameSize()*2,
                                      format.getFrameRate(),
                                      true);
                audio1 = AudioSystem.getAudioInputStream(tmp,audio1);
                format = tmp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format,((int) audio1.getFrameLength()*format.getFrameSize()));
            try {
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(audio1);
            }catch (Exception ex3){
                throw new RuntimeException(bundle.getString("ErrorMsg3") + ' ' + soundPath);
            }
        }else if (audio instanceof Sequence || audio instanceof BufferedInputStream) {
            try {
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
                if (audio instanceof Sequence) {
                    sequencer.setSequence((Sequence) audio);
                }else{
                    sequencer.setSequence((BufferedInputStream) audio);
                }
            }catch (Exception ex3) {
                throw new RuntimeException(bundle.getString("ErrorMsg3") + ' ' + soundPath);
            }
        }

        if (clip != null) {
          final ESlateSound eslateSound = new ESlateSound(clip);
//          clip.start();
//          clip.addLineListener(new LineListener() {
//              public void update(LineEvent event) {
//                  if (event.getType() == LineEvent.Type.STOP) {
//                      Clip clip = (Clip) event.getSource();
//                      clip.close();
//                      clip.removeLineListener(this);
//                      eslateSound.fireSoundStopped();
//                      eslateSound.soundStopped();
//System.out.println("Clip stopped");
//                  }
//              }
//          });
          return eslateSound;
//System.out.println("clip: " + clip);
        }else if (sequencer != null) {
          final ESlateSound eslateSound = new ESlateSound(sequencer);
//          sequencer.start();
//          sequencer.addMetaEventListener(new CloseMetaEventListener(sequencer, eslateSound));
          return eslateSound;
//System.out.println("sequencer: " + sequencer);
        }
        return null;
    }

    public static void stopSound(ESlateSound sound) {
        if (sound == null) return;
        if (sound.getType() == ESlateSound.CLIP_TYPE) {
            Clip clip = (Clip) sound.getSound();
            if (clip.isRunning())
                clip.stop();
            if (clip.isOpen())
                clip.close();
        }else{ //else if (Sequencer.class.isAssignableFrom(sound.getClass())) {
            Sequencer seq = (Sequencer) sound.getSound();
            if (seq.isRunning())
                seq.stop();
            if (seq.isOpen())
                seq.close();
        }
    }

    static class CloseMetaEventListener implements MetaEventListener {
        Sequencer sequencer = null;
        ESlateSound sound = null;

        public CloseMetaEventListener(Sequencer sequencer, ESlateSound sound) {
            this.sequencer = sequencer;
            this.sound = sound;
        }
        public void meta(MetaMessage message) {
//System.out.println("message.getType(): " + message.getType());
            if (message.getType() == 47) {  // 47 is end of track
                sequencer.close();
                sequencer.removeMetaEventListener(this);
                sound.fireSoundStopped();
                sound.soundStopped();
//System.out.println("Sequencer stopped");
            }
        }
    }
}

