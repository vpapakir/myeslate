package gr.cti.eslate.animation;


/**
 * This class implements the play thread.
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-May-2002
 * @see		gr.cti.eslate.animation.Animation
 */
class PlayThread extends Thread {
  Animation animation;
  boolean stopFlag = false;

  public PlayThread(Animation animation) {
    this.animation = animation;
  }

  public void stopThread(){
//    animation.firePlayThreadListeners(animation.STOPPLAYTHREAD);
    animation.firePlayThreadStoppedAnimationViewListeners();
    stopFlag = true;
  }

  public void run() {
    // Stop all previous running play threads.
/*    for (int i=0;i<animation.playThreads.size()-1;i++){
        ((PlayThread)animation.playThreads.get(i)).stopThread();
        ((PlayThread)animation.playThreads.get(i)).interrupt();
        animation.playThreads.remove(i);
    }*/

    if (!stopFlag) {
      if (animation.cursorTime >= animation.getMaxFrameTime())
        animation.cursorTime = 1;
      int frameTimer = animation.cursorTime;
      while ((stopFlag==false) && (frameTimer < animation.getMaxFrameTime()+1)) {
        animation.startTime = System.currentTimeMillis();
        // Set the position of frame cursor.
        animation.setCursorTime(frameTimer);

        long dif = new Double((Math.pow(animation.fps,-1))*1000 - (System.currentTimeMillis()-animation.startTime)).longValue();
        if (dif > 0) {
          try {
            sleep(dif);
          } catch(InterruptedException e) {
          }
        }

        frameTimer++;

        if ((animation.stopFrame > 0) && (animation.stopFrame != animation.getMaxFrameTime()+1) && (frameTimer == animation.stopFrame+1)) {
          if (animation.loopedPlayback == true)
            frameTimer = animation.startFrame;
          else {
            animation.startFrame = -1;
            animation.stopFrame = -1;
            stopThread();
          }
        }

        if (frameTimer == animation.getMaxFrameTime()+1) {
          if (animation.loopedPlayback == true) {
            if (animation.startFrame >=0)
              frameTimer = animation.startFrame;
            else
              frameTimer = 1;
          }
          else {
            if (animation.startFrame >=0)
              animation.startFrame = -1;
            if (animation.stopFrame >0)
              animation.stopFrame = -1;
            stopThread();
          }
        }
/*        try {
          sleep(animation.delay);
        } catch(InterruptedException e) {
//          System.err.println("Interrupted");
//          break;
        }*/
      }
      if (stopFlag)
        animation.pt = null;
    }
    else
      animation.pt = null;
  }
}
