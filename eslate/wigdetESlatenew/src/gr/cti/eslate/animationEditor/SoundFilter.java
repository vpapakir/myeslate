package gr.cti.eslate.animationEditor;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * This class implements filter for choosing sound files.
 * @author	Augustine Grillakis
 * @version	1.0.0, 28-Jun-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
public class SoundFilter extends FileFilter {
  public final static String wav = "wav";
  public final static String midi = "mid";
//  public final static String mp3 = "mp3";

  AnimationEditor animationEditor;

  public SoundFilter(AnimationEditor animationEditor) {
    super();
    this.animationEditor = animationEditor;
  }

  // Accept all directories and all sound files.
  public boolean accept(File f) {
    if (f.isDirectory()) {
        return true;
    }

    String extension = getExtension(f);
    if (extension != null) {
      if (extension.equals(wav)
        || extension.equals(midi)
        /*|| extension.equals(mp3)*/) {
              return true;
      } else {
          return false;
      }
    }

    return false;
  }

  // The description of this filter
  public String getDescription() {
      return animationEditor.resources.getString("soundFiles");
  }

  /*
   * Get the extension of a file.
   */
  public static String getExtension(File f) {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 &&  i < s.length() - 1) {
          ext = s.substring(i+1).toLowerCase();
      }
      return ext;
  }
}