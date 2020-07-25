package gr.cti.eslate.animation;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class implements a sound segment of the sound actor.
 * @author	Augustine Grillakis
 * @version	1.0.0, 27-Jun-2002
 * @see     gr.cti.eslate.animation.SoundActor
 */
public class SoundSegment extends BaseSegment implements Externalizable {
  final static String SOUNDFILE = "soundFile";

  File soundFile;

  /**
   * Create a segment.
   */
  public SoundSegment() {
  }

  /**
   * Create a segment.
   * @param	actor    The segment's actor.
   */
  public SoundSegment(BaseActor actor) {
    this.actor = actor;

    startMilestone = new Milestone(this);
    endMilestone = new Milestone(this);
    startMilestone.when = 0;
    endMilestone.when = 0;
    startMilestone.previous = null;
    startMilestone.next = endMilestone;
    endMilestone.previous = startMilestone;
    endMilestone.next = null;
  }

  /**
   * Create a segment.
   * @param	actor   The actor of the segment.
   * @param	start   The start time of the segment.
   * @param	end     The end of the segment.
   */
  public SoundSegment (BaseActor actor, int start, int end) {
    this(actor);
    startMilestone.when = start;
    endMilestone.when = end;
  }

  /**
   * Get sound file.
   * @return  The sound file.
   */
  public File getSoundFile() {
    return soundFile;
  }

  /**
   * Set the sound file.
   * @param soundFile The sound file.
   */
  public void setSoundFile(File soundFile) {
    this.soundFile = soundFile;

    if (soundFile != null) {
      String fileName;
      URL fileURL = null;

      fileName = soundFile.getName();
      try {
          fileURL = soundFile.toURL();
      } catch (MalformedURLException e){
          System.err.println(e.getMessage());
      }
      actor.animation.soundList.startLoading(fileURL, fileName);
    }
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws    IOException
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    // Construct an arraylist of segment's milestones beginning from 1st
    // mlestone in order to save it.
    ArrayList milestones = new ArrayList();
    BaseMilestone iterator = startMilestone;
    while (iterator != null) {
      milestones.add(iterator);
      iterator = iterator.next;
    }

    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion);

    map.put(SOUNDFILE, soundFile);
    map.put(MILESTONES, milestones);

    oo.writeObject(map);
  }

  /**
   * Load the component's state.
   * @param	oi	The stream from where the state should be loaded.
   * @throws    IOException
   * @throws    ClassNotFoundException
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    ArrayList milestones = null;
    BaseMilestone previousMilestone = null;
    BaseMilestone actualMilestone = null;

    StorageStructure map = (StorageStructure)(oi.readObject());

    soundFile = (File)map.get(SOUNDFILE);
    milestones = (ArrayList)map.get(MILESTONES);

    // Construct the custom linked list of segment's milestones from the
    // loaded arraylist of milestones.
    for (int i=0;i<milestones.size();i++) {
      if (i==0) {
        startMilestone = (BaseMilestone)milestones.get(0);
        startMilestone.segment = this;
        previousMilestone = startMilestone;
        actualMilestone = startMilestone;
      }
      else {
        actualMilestone = (BaseMilestone)milestones.get(i);
        actualMilestone.segment = this;
        previousMilestone.next = actualMilestone;
        actualMilestone.previous = previousMilestone;
        previousMilestone = (BaseMilestone)milestones.get(i);
      }
      if (actualMilestone == (BaseMilestone)milestones.get(milestones.size()-1))
        endMilestone = actualMilestone;
    }
  }
}
