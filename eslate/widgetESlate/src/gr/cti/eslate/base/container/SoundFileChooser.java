package gr.cti.eslate.base.container;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class SoundFileChooser extends JFileChooser {
    ResourceBundle bundle;

    public SoundFileChooser() {
        super();
        bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.SoundFileChooserBundle", Locale.getDefault());

        AllSoundsFileFilter defaultFilter = new AllSoundsFileFilter();
        addChoosableFileFilter(defaultFilter);
        addChoosableFileFilter(new WavSoundFileFilter());
        addChoosableFileFilter(new MidiSoundFileFilter());
        addChoosableFileFilter(new AUSoundFileFilter());
        addChoosableFileFilter(new RMFSoundFileFilter());
        addChoosableFileFilter(new AIFFSoundFileFilter());
        addChoosableFileFilter(new AllFilesFilter());
        setDialogTitle(bundle.getString("Load Sound"));
        setMultiSelectionEnabled(false);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(defaultFilter);
    }

    class WavSoundFileFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            String fileName = f.getName().toLowerCase();
//            if (!f.isDirectory())
//                System.out.println("file: " + fileName + ", f.getName().endsWith(\".wav\"): " + fileName.endsWith(".wav"));
            if (f.isDirectory() || fileName.endsWith(".wav"))
                return true;
            return false;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("WAV");
        }
    }

    class MidiSoundFileFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            String fileName = f.getName().toLowerCase();
            if (f.isDirectory() || fileName.endsWith(".mid"))
                return true;
            return false;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("MIDI");
        }
    }

    class AUSoundFileFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            String fileName = f.getName().toLowerCase();
            if (f.isDirectory() || fileName.endsWith(".au"))
                return true;
            return false;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("AU");
        }
    }

    class RMFSoundFileFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            String fileName = f.getName().toLowerCase();
            if (f.isDirectory() || fileName.endsWith(".rmf"))
                return true;
            return false;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("RMF");
        }
    }

    class AIFFSoundFileFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            String fileName = f.getName().toLowerCase();
            if (f.isDirectory() || fileName.endsWith(".aiff"))
                return true;
            return false;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("AIFF");
        }
    }

    class AllSoundsFileFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            String fileName = f.getName().toLowerCase();
            if (f.isDirectory() ||
            fileName.endsWith(".rmf") ||
            fileName.endsWith(".aiff") ||
            fileName.endsWith(".wav") ||
            fileName.endsWith(".mid") ||
            fileName.endsWith(".au"))
                return true;
            return false;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("All");
        }
    }

    class AllFilesFilter extends FileFilter {
        public boolean accept(java.io.File f) {
            return true;
        }

        public String getDescription() {
            return SoundFileChooser.this.bundle.getString("AllFiles");
        }
    }

}