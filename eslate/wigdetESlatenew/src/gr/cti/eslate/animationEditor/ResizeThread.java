package gr.cti.eslate.animationEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * This class implements the resize thread.
 * @author	Augustine Grillakis
 * @version	1.0.0, 19-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
public class ResizeThread extends Thread {
  AnimationEditor animationEditor;
  boolean stopFlag = false;

  public ResizeThread(AnimationEditor animationEditor) {
    this.animationEditor = animationEditor;
  }

  public void stopThread(){
    stopFlag = true;
  }

  public void run() {
    // Stop all previous running resize threads.
    for (int i=0;i<animationEditor.resizeThreads.size()-1;i++){
        ((ResizeThread)animationEditor.resizeThreads.get(i)).stopThread();
        animationEditor.resizeThreads.remove(i);
    }

    if (!stopFlag) {
      // Resize animation viewer.
      animationEditor.resizeAnimationEditor();
    }
  }
}
