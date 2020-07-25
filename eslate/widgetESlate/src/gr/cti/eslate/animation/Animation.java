package gr.cti.eslate.animation;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.FemaleSingleIFMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleOutputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.ProtocolPlug;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.protocol.ActorInterface;
import gr.cti.eslate.protocol.AnimatedPropertyDescriptor;
import gr.cti.eslate.protocol.AnimationSession;
import gr.cti.eslate.scripting.AsAnimation;
import gr.cti.eslate.sharedObject.AnimationSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ObjectBaseArray;

import java.applet.AudioClip;
import java.awt.Color;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class implements a animation model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 7-Mar-2002
 */
public class Animation implements ESlatePart, Externalizable, AsAnimation {
  private ESlateHandle handle = null;
  private AnimationSO animationSO;
  private final static String ACTORS = "actors";
  private final static String FRAMELABELS = "frameLabels";
  private final static String LOOP = "loop";
  private final static String version = "2.0.0";
  private final static int storageVersion = 1;
  private FemaleSingleIFMultipleConnectionProtocolPlug plug;

  private ArrayList animationModelListeners = new ArrayList();
  private ArrayList milestoneProcessListeners = new ArrayList();
  private ArrayList segmentProcessListeners = new ArrayList();
  private ArrayList animationViewListeners = new ArrayList();

  int start;
  int duration;
  ArrayList actors = new ArrayList();
  int cursorTime=1;
//  int delay = 1;
  int maxTime = 0;

  PlayThread pt;
  boolean loopedPlayback = true;
  int fps=30;
  long startTime;
//  Milestone editMilestone = null;

  HashMap animationSessions = new HashMap();
  ArrayList aniSessions = new ArrayList();
  ObjectBaseArray frameLabels = new ObjectBaseArray();

  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.animation.AnimationResource", Locale.getDefault()
  );

  int startFrame = -1;
  int stopFrame = -1;

  SoundActor soundActor;
  SoundList soundList;
  AudioClip audioClip = null;

  /**
   * Returns Copyright information.
   * @return	The Copyright information.
   */
  private ESlateInfo getInfo()
  {
    String[] info = {
      resources.getString("credits1"),
      resources.getString("credits2"),
      resources.getString("credits3"),
    };
    return new ESlateInfo(
      resources.getString("componentName") + ", " +
	      resources.getString("version") + " " + version,
        info);
  }

  /**
   * Create a animation component.
   */
  public Animation() {
    soundActor = new SoundActor(this);
    actors.add(soundActor);
    soundList = new SoundList();
  }

  /**
   * Sets the edit milestone.
   * @param	editMilestone   The edit milestone.
   */
/*  public void setEditMilestone(Milestone milestone) {
    this.editMilestone = editMilestone;
  }*/

  /**
   * Gets the edit milestone.
   * @return  The edit milestone.
   */
/*  public Milestone getEditMilestone() {
    return editMilestone;
  }*/

  /**
   * Check if play thread is active.
   * @return Return true if the play thread is running.
   */
  public boolean isPlayThreadActive() {
    if (pt != null)
      return true;
    else
      return false;
  }

  /**
   * Set looped playback.
   * @param loopedPlayback  True for looped playback.
   */
  public void setLoopedPlayback(boolean loopedPlayback) {
    this.loopedPlayback = loopedPlayback;
//    fireLoopListeners(loopedPlayback);
    fireLoopChangedAnimationViewListeners(loopedPlayback);
  }

  /**
   * Get looped playback.
   * @return True if looped playback.
   */
  public boolean isLoopedPlayback() {
    return loopedPlayback;
  }

  /**
   * Set maximum fps.
   * @param fps Maximum fps.
   */
  public void setFps(int fps) {
    this.fps = fps;
    fireFpsChangedAnimationViewListeners(fps);
  }

  /**
   * Get maximum fps.
   * @return Maximum fps.
   */
  public int getFps() {
    return fps;
  }

  /**
   * Get frame labels.
   * @return Frame labels.
   */
  public ObjectBaseArray getFrameLabels() {
    return frameLabels;
  }

  /**
   * Checks if a frame has label.
   * @param frame The frame to check.
   * @return  True if the frame has label.
   */
  public boolean isFrameLabel (int frame) {
    boolean flag = false;
    for (int i=0;i<frameLabels.size();i++) {
      if (((FrameLabel)frameLabels.get(i)).frame == frame) {
        flag = true;
        break;
      }
    }
    return flag;
  }

  /**
   * Get index of frameLabel.
   * @param frame The frame to get index of.
   * @return  The index.
   */
  public int indexOfFrame(int frame) {
    int index = -1;
    for (int i=0;i<frameLabels.size();i++) {
      if (((FrameLabel)frameLabels.get(i)).frame == frame) {
        index = i;
        break;
      }
    }
    return index;
  }

  /**
   * Get frame from label.
   * @param label The label to get frame of.
   * @return  The frame.
   */
  public int getFrameFromLabel(String label) {
    int frame = -1;
    for (int i=0;i<frameLabels.size();i++) {
      if (((FrameLabel)frameLabels.get(i)).label.compareTo(label) == 0) {
        frame = ((FrameLabel)frameLabels.get(i)).frame;
        break;
      }
    }
    return frame;
  }

  /**
   * Get resource bundle.
   * @return The resource bundle.
   */
  public ResourceBundle getResources() {
    return resources;
  }

  /**
   * Get animation sessions.
   * @return The animation sessions.
   */
  public HashMap getAnimationSessions() {
    return animationSessions;
  }

  /**
   * Add a listener for animation model events.
   * @param	listener	The listener to add.
   */
  public void addAnimationModelListener(AnimationModelListener listener)
  {
    synchronized (animationModelListeners) {
      if (!animationModelListeners.contains(listener)) {
        animationModelListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for animation model events.
   * @param	listener	The listener to remove.
   */
  public void removeAnimationModelListener(AnimationModelListener listener)
  {
    synchronized (animationModelListeners) {
      int ind = animationModelListeners.indexOf(listener);
      if (ind >= 0) {
        animationModelListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for animation events.
   * @param	actor     The actor that added.
   */
  private void fireActorAddedAnimationModelListeners(BaseActor actor)
  {
    ArrayList listeners;
    synchronized(animationModelListeners) {
      listeners = (ArrayList)(animationModelListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationModelListener l = (AnimationModelListener)(listeners.get(i));
      AnimationEvent e = new AnimationEvent(this, actor);
      l.actorAdded(e);
    }
  }

  /**
   * Fires all listeners registered for animation events.
   * @param	actor     The actor that added/removed.
   */
  private void fireActorRemovedAnimationModelListeners(BaseActor actor)
  {
    ArrayList listeners;
    synchronized(animationModelListeners) {
      listeners = (ArrayList)(animationModelListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationModelListener l = (AnimationModelListener)(listeners.get(i));
      AnimationEvent e = new AnimationEvent(this, actor);
      l.actorRemoved(e);
    }
  }

  /**
   * Fires all listeners registered for cursor events.
   * @param	time     The time of the cursor that changed.
   */
  private void fireTimeChangedAnimationModelListeners(int time)
  {
    ArrayList listeners;
    synchronized(animationModelListeners) {
      listeners = (ArrayList)(animationModelListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationModelListener l = (AnimationModelListener)(listeners.get(i));
      CursorEvent e = new CursorEvent(this, time);
        l.timeChanged(e);
    }
  }

  /**
   * Add a listener for finding milestone events.
   * @param	listener	The listener to add.
   */
  public void addMilestoneProcessListener(MilestoneProcessListener listener)
  {
    synchronized (milestoneProcessListeners) {
      if (!milestoneProcessListeners.contains(listener)) {
        milestoneProcessListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from finding milestone events.
   * @param	listener	The listener to remove.
   */
  public void removeMilestoneProcessListener(MilestoneProcessListener listener)
  {
    synchronized (milestoneProcessListeners) {
      int ind = milestoneProcessListeners.indexOf(listener);
      if (ind >= 0) {
        milestoneProcessListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for finding milestone events.
   * @param	milestone     The milestone that found.
   */
  private void fireMilestoneProcessListeners(Milestone milestone)
  {
    ArrayList listeners;
    synchronized(milestoneProcessListeners) {
      listeners = (ArrayList)(milestoneProcessListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      MilestoneProcessListener l = (MilestoneProcessListener)(listeners.get(i));
      MilestoneProcessEvent e = new MilestoneProcessEvent(this, milestone);
      l.milestoneFound(e);
    }
  }

  /**
   * Add a listener for entering/exiting segment events.
   * @param	listener	The listener to add.
   */
  public void addSegmentProcessListener(SegmentProcessListener listener)
  {
    synchronized (segmentProcessListeners) {
      if (!segmentProcessListeners.contains(listener)) {
        segmentProcessListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from entering/exiting segment events.
   * @param	listener	The listener to remove.
   */
  public void removeSegmentProcessListener(SegmentProcessListener listener)
  {
    synchronized (segmentProcessListeners) {
      int ind = segmentProcessListeners.indexOf(listener);
      if (ind >= 0) {
        segmentProcessListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for entering segment events.
   * @param     segment   The segment to enter.
   */
  private void fireSegmentEnteredListeners(BaseSegment segment)
  {
    ArrayList listeners;
    synchronized(segmentProcessListeners) {
      listeners = (ArrayList)(segmentProcessListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      SegmentProcessListener l = (SegmentProcessListener)(listeners.get(i));
      SegmentProcessEvent e = new SegmentProcessEvent(this, segment);
      l.segmentEntered(e);
    }
  }

  /**
   * Fires all listeners registered for exiting segment events.
   * @param     segment   The segment to exit.
   */
  private void fireSegmentExitedListeners(BaseSegment segment)
  {
    ArrayList listeners;
    synchronized(segmentProcessListeners) {
      listeners = (ArrayList)(segmentProcessListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      SegmentProcessListener l = (SegmentProcessListener)(listeners.get(i));
      SegmentProcessEvent e = new SegmentProcessEvent(this, segment);
      l.segmentExited(e);
    }
  }

  /**
   * Add a listener for play thread's or looped playback's state changes events.
   * @param	listener	The listener to add.
   */
  public void addAnimationViewListener(AnimationViewListener listener)
  {
    synchronized (animationViewListeners) {
      if (!animationViewListeners.contains(listener)) {
        animationViewListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from play thread's or looped playback's state changes events.
   * @param	listener	The listener to remove.
   */
  public void removeAnimationViewListener(AnimationViewListener listener)
  {
    synchronized (animationViewListeners) {
      int ind = animationViewListeners.indexOf(listener);
      if (ind >= 0) {
        animationViewListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for play thread's start events.
   */
  void firePlayThreadStartedAnimationViewListeners()
  {
    ArrayList listeners;
    synchronized(animationViewListeners) {
      listeners = (ArrayList)(animationViewListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationViewListener l = (AnimationViewListener)(listeners.get(i));
      PlayThreadEvent e = new PlayThreadEvent(this);
      l.playThreadStarted(e);
    }
  }

  /**
   * Fires all listeners registered for play thread's stop events.
   */
  void firePlayThreadStoppedAnimationViewListeners()
  {
    ArrayList listeners;
    synchronized(animationViewListeners) {
      listeners = (ArrayList)(animationViewListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationViewListener l = (AnimationViewListener)(listeners.get(i));
      PlayThreadEvent e = new PlayThreadEvent(this);
      l.playThreadStopped(e);
    }
  }

  /**
   * Fires all listeners registered for looped playback's state changes events.
   * @param looped The state of looped playback.
   */
  void fireLoopChangedAnimationViewListeners(boolean looped)
  {
    ArrayList listeners;
    synchronized(animationViewListeners) {
      listeners = (ArrayList)(animationViewListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationViewListener l = (AnimationViewListener)(listeners.get(i));
      LoopEvent e = new LoopEvent(this, looped);
      l.loopChanged(e);
    }
  }

  /**
   * Fires all listeners registered for maximum fps change events.
   * @param fps The maximum fps.
   */
  void fireFpsChangedAnimationViewListeners(int fps)
  {
    ArrayList listeners;
    synchronized(animationViewListeners) {
      listeners = (ArrayList)(animationViewListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      AnimationViewListener l = (AnimationViewListener)(listeners.get(i));
      FpsEvent e = new FpsEvent(this, fps);
      l.fpsChanged(e);
    }
  }

  /**
   * Returns the component's E-Slate handle.
   * @return	The requested handle. If the component's constructor has not
   *		been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      initESlate();
    }
    return handle;
  }

  /**
   * Initializes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    handle = ESlate.registerPart(this);
    handle.setInfo(getInfo());
	try {
		handle.setUniqueComponentName(resources.getString("name"));
	} catch (RenamingForbiddenException e) {
		e.printStackTrace();
	}
    // Construct a MultipleOutputPlug in order to export the animation
    // to viewers.
    animationSO = new AnimationSO(handle, this);
    try {
      Plug plug = new MultipleOutputPlug (
        handle, resources, "animationPlug", new Color(0,255,100),
        AnimationSO.class, animationSO);
      handle.addPlug(plug);
    } catch (InvalidPlugParametersException e) {
    } catch (PlugExistsException e) {
    }
    // Construct a FemaleSingleIFMultipleConnectionProtocolPlug in order to
    // insert the exported variables from a component.
    try {
      plug = new FemaleSingleIFMultipleConnectionProtocolPlug(
        handle, resources, "actorPlug", new Color(0,100,255),
        ActorInterface.class);
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e) {
          boolean newActorFlag = true;
          for (int i=1;i<actors.size();i++) {
            Actor tempActor = (Actor)actors.get(i);
            if (tempActor.actorInterface == null) {
              newActorFlag = false;
              ProtocolPlug p = (ProtocolPlug)e.getPlug();
              ActorInterface ai = (ActorInterface)(p.getProtocolImplementor());
              tempActor.actorInterface = ai;
              tempActor.varCount = tempActor.actorInterface.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().size();
              tempActor.animationSession = new AnimationSession(ai.getAnimatedPropertyStructure(), ai);
              tempActor.animationSession.setPlugID(tempActor.getActorInterface().getPlugCount());
              tempActor.animationSession.setAnimationStatus(tempActor.animationStatus);
              animationSessions.put(p, tempActor.animationSession);
              aniSessions.add(tempActor.animationSession);
              tempActor.aniVarCount = 0;
              for (int j=0;j<tempActor.varCount;j++) {
                if (tempActor.animationSession.isAnimated(((AnimatedPropertyDescriptor)tempActor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID())) {
                  tempActor.aniPropertyIDs.add(((AnimatedPropertyDescriptor)tempActor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID());
                  tempActor.aniVarCount++;
                  tempActor.getActorInterface().getAnimatedPropertyStructure().setAnimated(((AnimatedPropertyDescriptor)tempActor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID(),true);
                }
              }
              break;
            }
          }
          if (newActorFlag == true) {
            ProtocolPlug p = (ProtocolPlug)e.getPlug();
            ActorInterface ai = (ActorInterface)(p.getProtocolImplementor());
            Actor actor = new Actor(Animation.this, ai);
            actor.varCount = actor.actorInterface.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().size();
            actor.aniVarCount = 0;
            AnimationSession aniSession = new AnimationSession(actor.actorInterface.getAnimatedPropertyStructure(), actor.actorInterface);
            actor.animationSession = aniSession;
            actor.animationSession.setPlugID(actor.getActorInterface().getPlugCount());
            animationSessions.put(p, actor.animationSession);
            aniSessions.add(actor.animationSession);
            for (int j=0;j<actor.varCount;j++) {
              if (!actor.getActorInterface().getAnimatedPropertyStructure().isAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID())) {
                actor.animationSession.setAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID(), true);
                actor.aniPropertyIDs.add(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID());
                actor.getActorInterface().getAnimatedPropertyStructure().setAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID(),true);
                actor.aniVarCount++;
              }
              else
                actor.animationSession.setAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(j)).getPropertyID(), false);
            }
            addActor(actor);
          }
        }
      });
      plug.addDisconnectionListener(new DisconnectionListener() {
        public void handleDisconnectionEvent(DisconnectionEvent e) {
          ProtocolPlug p = (ProtocolPlug)e.getPlug();
//          ActorInterface ai = (ActorInterface)(p.getProtocolImplementor());
          for (int i=1;i<actors.size();i++) {
            Actor tempActor = (Actor)actors.get(i);
//            if (tempActor.actorInterface == ai) {
            if (tempActor.animationSession == animationSessions.get(p)) {
              animationSessions.remove(p);
              removeActor(tempActor);
              break;
            }
          }
        }
      });
    } catch (NoClassDefFoundError e) {
    } catch (InvalidPlugParametersException e) {
    } catch (PlugExistsException e) {
    }
    handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.AnimationPrimitives");
  }

  /**
   * Add an actor.
   * @param	actor   The actor to add.
   */
  public void addActor(BaseActor actor) {
    synchronized (actors) {
      if (!actors.contains(actor)) {
        actors.add(actor);
        fireActorAddedAnimationModelListeners(actor);
      }
    }
  }

  /**
   * Remove an actor.
   * @param	actor   The actor to remove.
   */
  public void removeActor(BaseActor actor) {
    synchronized (actors) {
      int ind = actors.indexOf(actor);
      if (ind >= 0) {
        actors.remove(actor);
        fireActorRemovedAnimationModelListeners(actor);
      }
    }
  }

  /**
   * Returns the animation's actors.
   * @return	The actors.
   */
  public ArrayList getActors() {
    return actors;
  }

  /**
   * Returns the animation's actors with specific actor interface.
   * @param   ai  Actor interface.
   * @return	The actors.
   */
  public ArrayList getActors(ActorInterface ai) {
    ArrayList specificActors = new ArrayList();
    for (int i=0;i<actors.size();i++) {
//      Actor tempActor = (Actor)actors.get(i);
      if (((Actor)actors.get(i)).actorInterface == ai)
        specificActors.add((Actor)actors.get(i));
    }
    return specificActors;
  }

  /**
   * Returns the animation's animation sessions.
   * @return	The animation sessions.
   */
  public ArrayList getAniSessions() {
    return aniSessions;
  }

  /**
   * Returns the animation's animation sessions with specific actor interface.
   * @param   ai  Actor interface.
   * @return	The animation sessions.
   */
  public ArrayList getAniSessions(ActorInterface ai) {
    ArrayList specificAniSessions = new ArrayList();
    for (int i=0;i<aniSessions.size();i++) {
      if (((AnimationSession)aniSessions.get(i)).getActorInterface() == ai)
        specificAniSessions.add((AnimationSession)aniSessions.get(i));
    }
    return specificAniSessions;
  }

  /**
   * Sets the animation's cursor time.
   * @param	newCursorTime   The cursor time to set.
   */
  public void setCursorTime(int newCursorTime) {
    this.cursorTime = newCursorTime;
    fireTimeChangedAnimationModelListeners(newCursorTime);

    // Find the segment and milestone where the frame cursor is for each
    // actor and process the corresponding milestone values.
    processSegment(newCursorTime);
  }

  /**
   * Find the segment where frame cursor is.
   * @param	newCursorTime   The cursor time to set.
   */
  private void processSegment(int newCursorTime) {
    for (int i=0;i<actors.size();i++) {
      BaseActor tempActor = (BaseActor)actors.get(i);
     if (tempActor.getSegmentCount() != 0) {
      // If current segment is null search to find the segment.
      if (tempActor.currentSegment == null) {
// Base change
        BaseSegment iteratorSegment = tempActor.startSegment;
        boolean foundSegmentFlag = false;
        while (iteratorSegment != null) {
          if (iteratorSegment.isTimeInSegment(newCursorTime)) {
            tempActor.currentSegment = iteratorSegment;
            foundSegmentFlag = true;
            if (!tempActor.onStageFlag) {
              tempActor.onStageFlag = true;
              tempActor.offStageFlag = false;
              if (i>0)
                ((Actor)tempActor).actorInterface.onStage(((Actor)tempActor).animationSession);
            }
            break;
          }
          iteratorSegment = iteratorSegment.next;
        }
/*        if ((foundSegmentFlag == false) && (!tempActor.offStageFlag)) {
          tempActor.offStageFlag = true;
          tempActor.onStageFlag = false;
          if (i==0) {
// *** Sound Actor Off Stage ***
          }
          else
            ((Actor)tempActor).actorInterface.offStage(((Actor)tempActor).animationSession);
        }*/
      }
      if (tempActor.currentSegment != null) {
        // If frame cursor isn't in current segment.
        if (!tempActor.currentSegment.isTimeInSegment(newCursorTime)) {
          boolean nextFlag = false;
          boolean previousFlag = false;
          boolean betweenFlag = false;
          //Check if frame cursor is in next segment.
          if (tempActor.currentSegment.next != null) {
            if (tempActor.currentSegment.next.isTimeInSegment(newCursorTime)) {
              tempActor.currentSegment = tempActor.currentSegment.next;
              nextFlag = true;
              if (!tempActor.onStageFlag) {
                tempActor.onStageFlag = true;
                tempActor.offStageFlag = false;
                if (i>0)
                  ((Actor)tempActor).actorInterface.onStage(((Actor)tempActor).animationSession);
              }
// Base change
              if (i>0)
                ((Segment)tempActor.currentSegment).currentMilestone = null;
              processMilestone(newCursorTime, i);
            }
          }
          //Check if frame cursor is in previous segment.
          if (nextFlag == false) {
            if (tempActor.currentSegment.previous != null) {
              if (tempActor.currentSegment.previous.isTimeInSegment(newCursorTime)) {
                tempActor.currentSegment = tempActor.currentSegment.previous;
                previousFlag = true;
                if (!tempActor.onStageFlag) {
                  tempActor.onStageFlag = true;
                  tempActor.offStageFlag = false;
                  if (i>0)
                    ((Actor)tempActor).actorInterface.onStage(((Actor)tempActor).animationSession);
                }
// Base change
                if (i>0)
                  ((Segment)tempActor.currentSegment).currentMilestone = null;
                processMilestone(newCursorTime, i);
              }
            }
          }
          if ((nextFlag == false) && (previousFlag == false)) {
            //Check if frame cursor is between current segment and next segment.
            if (tempActor.currentSegment.next != null) {
              if ((newCursorTime>tempActor.currentSegment.getEnd()) && (newCursorTime<tempActor.currentSegment.next.getStart())) {
                betweenFlag = true;
                if (!tempActor.offStageFlag) {
                  tempActor.offStageFlag = true;
                  tempActor.onStageFlag = false;
                  if (i==0) {
// *** Sound Actor Off Stage ***
                  }
                  else
                    ((Actor)tempActor).actorInterface.offStage(((Actor)tempActor).animationSession);
                }
              }
            }
            //Check if frame cursor is between current segment and previous segment.
            if (betweenFlag == false) {
              if (tempActor.currentSegment.previous != null) {
                if ((newCursorTime<tempActor.currentSegment.getStart()) && (newCursorTime>tempActor.currentSegment.previous.getEnd())) {
                  betweenFlag = true;
                  if (!tempActor.offStageFlag) {
                    tempActor.offStageFlag = true;
                    tempActor.onStageFlag = false;
                    if (i==0) {
// *** Sound Actor Off Stage ***
                    }
                    else
                      ((Actor)tempActor).actorInterface.offStage(((Actor)tempActor).animationSession);
                  }
                }
              }
            }
          }
          // Else search to find the segment where the frame cursor is.
          if ((nextFlag == false) && (previousFlag == false) && (betweenFlag == false)) {
            boolean foundSegmentFlag = false;
// Base change
            BaseSegment iteratorSegment = tempActor.startSegment;
            while (iteratorSegment != null) {
              if (iteratorSegment.isTimeInSegment(newCursorTime)) {
                foundSegmentFlag = true;
                if (!tempActor.onStageFlag) {
                  tempActor.onStageFlag = true;
                  tempActor.offStageFlag = false;
                  if (i>0)
                    ((Actor)tempActor).actorInterface.onStage(((Actor)tempActor).animationSession);
                }
                tempActor.currentSegment = iteratorSegment;
// Base change
                if (i>0)
                  ((Segment)tempActor.currentSegment).currentMilestone = null;
                processMilestone(newCursorTime, i);
                break;
              }
              iteratorSegment = iteratorSegment.next;
            }
            // If frame cursor isn's in any segment put actor off stage.
            if ((foundSegmentFlag == false) && (!tempActor.offStageFlag)) {
              tempActor.offStageFlag = true;
              tempActor.onStageFlag = false;
              if (i==0) {
// *** Sound Actor Off Stage ***
              }
              else
                ((Actor)tempActor).actorInterface.offStage(((Actor)tempActor).animationSession);
            }
          }
        }
        else {
          // Find the milestone where frame cursor is and process the
          // corresponding milestone values.
          if (!tempActor.onStageFlag) {
            tempActor.onStageFlag = true;
            tempActor.offStageFlag = false;
            if (i>0)
              ((Actor)tempActor).actorInterface.onStage(((Actor)tempActor).animationSession);
          }
          processMilestone(newCursorTime, i);
        }
      }
     }
     else
      tempActor.currentSegment = null;
    }
  }

  /**
   * Find the milestone where frame cursor is and process the
   * corresponding milestone values.
   * @param	newCursorTime   The cursor time to set.
   * @param	actorIndex      The actor of the milestone.
   */
  private void processMilestone(int newCursorTime, int actorIndex) {
    if (actorIndex == 0) {
      if (pt != null) {
        if (((SoundSegment)((BaseActor)actors.get(actorIndex)).currentSegment).soundFile != null) {
          if (((BaseSegment)((BaseActor)actors.get(actorIndex)).currentSegment).startMilestone.when == newCursorTime)
            soundPlay(((SoundSegment)((BaseActor)actors.get(actorIndex)).currentSegment).soundFile);
          else if (((BaseSegment)((BaseActor)actors.get(actorIndex)).currentSegment).endMilestone.when == newCursorTime)
            soundStop(((SoundSegment)((BaseActor)actors.get(actorIndex)).currentSegment).soundFile);
        }
      }
      return;
    }
// Base change
    Segment tempSegment = (Segment)((Actor)actors.get(actorIndex)).currentSegment;
    // If current milestone is null search to find the milestone.
    if (tempSegment.currentMilestone == null) {
      boolean foundMilestoneFlag = false;
// Base change
      BaseMilestone iteratorMilestone = tempSegment.startMilestone;
      while (iteratorMilestone != null) {
        if (iteratorMilestone.when == newCursorTime) {
          foundMilestoneFlag = true;
          tempSegment.betweenMilestones = false;
          tempSegment.currentMilestone = (Milestone)iteratorMilestone;
          break;
        }
      // If frame cursor isn't at any milestone find previous milestone.
        else if (newCursorTime > iteratorMilestone.when)
          tempSegment.currentMilestone = (Milestone)iteratorMilestone;
        iteratorMilestone = iteratorMilestone.next;
      }
      // Process previous milestone variables at cursor time.
      if (foundMilestoneFlag == false) {
        tempSegment.betweenMilestones = true;
        IntBaseArray values = new IntBaseArray(((Actor)actors.get(actorIndex)).aniVarCount);
        for (int j=0;j<((Actor)actors.get(actorIndex)).aniVarCount;j++) {
          double step = ((double)(((Milestone)tempSegment.currentMilestone.next).getAniVarValues().get(j)-tempSegment.currentMilestone.getAniVarValues().get(j)))
            /((double)(tempSegment.currentMilestone.next.when-tempSegment.currentMilestone.when));
          int newValue = (int)(tempSegment.currentMilestone.getAniVarValues().get(j)
            +step*((double)(newCursorTime-tempSegment.currentMilestone.when)));
          tempSegment.steps.setSize(((Actor)actors.get(actorIndex)).aniVarCount);
          tempSegment.steps.set(j,step);
          values.add(newValue);
        }
        ((Actor)actors.get(actorIndex)).getActorInterface().setVarValues(values, ((Actor)actors.get(actorIndex)).animationSession);
//        ((Actor)actors.get(actorIndex)).setVarValues(values);
      }
    }
    if (tempSegment.currentMilestone != null) {
      // If frame cursor isn't at current milestone.
      if (tempSegment.currentMilestone.when != newCursorTime) {
        boolean nextFlag = false;
        boolean previousFlag = false;
        boolean betweenFlag = false;
        //Check if frame cursor is at next milestone.
        if (tempSegment.currentMilestone.next != null) {
          if (tempSegment.currentMilestone.next.when == newCursorTime) {
// Base change
            tempSegment.currentMilestone = (Milestone)tempSegment.currentMilestone.next;
            nextFlag = true;
            tempSegment.betweenMilestones = false;
            fireMilestoneProcessListeners(tempSegment.currentMilestone);
            // Fire event for entering a segment.
            if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.startMilestone)
              fireSegmentEnteredListeners(((Actor)actors.get(actorIndex)).currentSegment);
            // Fire event for exiting a segment.
            if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.endMilestone)
              fireSegmentExitedListeners(((Actor)actors.get(actorIndex)).currentSegment);
            ((Actor)actors.get(actorIndex)).actorInterface.setVarValues(tempSegment.currentMilestone.getAniVarValues(), ((Actor)actors.get(actorIndex)).animationSession);
//            ((Actor)actors.get(actorIndex)).setVarValues((IntBaseArray)tempSegment.currentMilestone.getAniVarValues());
          }
        }
        //Check if frame cursor is at previous milestone.
        if (nextFlag == false) {
          if (tempSegment.currentMilestone.previous != null) {
            if (tempSegment.currentMilestone.previous.when == newCursorTime) {
// Base change
              tempSegment.currentMilestone = (Milestone)tempSegment.currentMilestone.previous;
              previousFlag = true;
              tempSegment.betweenMilestones = false;
              fireMilestoneProcessListeners(tempSegment.currentMilestone);
              // Fire event for entering a segment.
              if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.startMilestone)
                fireSegmentEnteredListeners(((Actor)actors.get(actorIndex)).currentSegment);
              // Fire event for exiting a segment.
              if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.endMilestone)
                fireSegmentExitedListeners(((Actor)actors.get(actorIndex)).currentSegment);
              ((Actor)actors.get(actorIndex)).actorInterface.setVarValues(tempSegment.currentMilestone.getAniVarValues(), ((Actor)actors.get(actorIndex)).animationSession);
//              ((Actor)actors.get(actorIndex)).setVarValues((IntBaseArray)tempSegment.currentMilestone.getAniVarValues());
            }
          }
        }

        //Check if frame cursor is between current milestone and next milestone.
        if ((nextFlag == false) && (previousFlag == false)) {
          IntBaseArray values = new IntBaseArray(((Actor)actors.get(actorIndex)).aniVarCount);
          if (tempSegment.currentMilestone.next != null) {
            if ((newCursorTime>tempSegment.currentMilestone.when) && (newCursorTime<tempSegment.currentMilestone.next.when)) {
              betweenFlag = true;
              // If this is the 1st time the frame cursor moves between the
              // milestones, set the step parameter of the actors.
              if (tempSegment.betweenMilestones == false) {
                tempSegment.betweenMilestones = true;
                for (int j=0;j<((Actor)actors.get(actorIndex)).aniVarCount;j++) {
// Base change
                  double step = ((double)(((Milestone)tempSegment.currentMilestone.next).getAniVarValues().get(j)-tempSegment.currentMilestone.getAniVarValues().get(j)))
                    /((double)(tempSegment.currentMilestone.next.when-tempSegment.currentMilestone.when));
                  int newValue = (int)(tempSegment.currentMilestone.getAniVarValues().get(j)
                    +step
                    *((double)(newCursorTime-tempSegment.currentMilestone.when)));
                  tempSegment.steps.setSize(((Actor)actors.get(actorIndex)).aniVarCount);
                  tempSegment.steps.set(j,step);
                  values.add(newValue);
                }
                ((Actor)actors.get(actorIndex)).getActorInterface().setVarValues(values, ((Actor)actors.get(actorIndex)).animationSession);
//                ((Actor)actors.get(actorIndex)).setVarValues(values);
              }
              // Else process milestones variables at cursor frame.
              else {
                for (int j=0;j<((Actor)actors.get(actorIndex)).aniVarCount;j++) {
                  int newValue = (int)(tempSegment.currentMilestone.getAniVarValues().get(j)
                    +tempSegment.steps.get(j)
                    *((double)(newCursorTime-tempSegment.currentMilestone.when)));
                  values.add(newValue);
                }
                ((Actor)actors.get(actorIndex)).getActorInterface().setVarValues(values, ((Actor)actors.get(actorIndex)).animationSession);
//                ((Actor)actors.get(actorIndex)).setVarValues(values);
              }
            }
          }
          //Check if frame cursor is between current milestone and previous milestone.
          if (betweenFlag == false) {
            if (tempSegment.currentMilestone.previous != null) {
              if ((newCursorTime<tempSegment.currentMilestone.when) && (newCursorTime>tempSegment.currentMilestone.previous.when)) {
                betweenFlag = true;
                // Set the step parameter of the actors.
// Base change
                tempSegment.currentMilestone = (Milestone)tempSegment.currentMilestone.previous;
                tempSegment.betweenMilestones = true;
                for (int j=0;j<((Actor)actors.get(actorIndex)).aniVarCount;j++) {
// Base change
                  double step = ((double)(((Milestone)tempSegment.currentMilestone.next).getAniVarValues().get(j)-tempSegment.currentMilestone.getAniVarValues().get(j)))
                    /((double)(tempSegment.currentMilestone.next.when-tempSegment.currentMilestone.when));
                  int newValue = (int)(tempSegment.currentMilestone.getAniVarValues().get(j)
                    +step
                    *((double)(newCursorTime-tempSegment.currentMilestone.when)));
                  tempSegment.steps.setSize(((Actor)actors.get(actorIndex)).aniVarCount);
                  tempSegment.steps.set(j,step);
                  values.add(newValue);
                }
                ((Actor)actors.get(actorIndex)).getActorInterface().setVarValues(values, ((Actor)actors.get(actorIndex)).animationSession);
//                ((Actor)actors.get(actorIndex)).setVarValues(values);
              }
            }
          }
        }

        // Else search to find the milestone where the frame cursor at.
        if ((nextFlag == false) && (previousFlag == false) && (betweenFlag == false)) {
          boolean foundMilestoneFlag = false;
// Base change
          BaseMilestone iteratorMilestone = ((Actor)actors.get(actorIndex)).currentSegment.startMilestone;
          while (iteratorMilestone != null) {
            if (iteratorMilestone.when == newCursorTime) {
              foundMilestoneFlag = true;
              tempSegment.betweenMilestones = false;
// Base change
              tempSegment.currentMilestone = (Milestone)iteratorMilestone;
              fireMilestoneProcessListeners(tempSegment.currentMilestone);
              // Fire event for entering a segment.
              if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.startMilestone)
                fireSegmentEnteredListeners(((Actor)actors.get(actorIndex)).currentSegment);
              // Fire event for exiting a segment.
              if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.endMilestone)
                fireSegmentExitedListeners(((Actor)actors.get(actorIndex)).currentSegment);
              ((Actor)actors.get(actorIndex)).actorInterface.setVarValues(tempSegment.currentMilestone.getAniVarValues(), ((Actor)actors.get(actorIndex)).animationSession);
//              ((Actor)actors.get(actorIndex)).setVarValues((IntBaseArray)tempSegment.currentMilestone.getAniVarValues());
              break;
            }
            // If frame cursor isn's at any milestone find previous milestone.
            else if (newCursorTime > iteratorMilestone.when)
// Base change
              tempSegment.currentMilestone = (Milestone)iteratorMilestone;
            iteratorMilestone = iteratorMilestone.next;
          }
          // Process previous milestone variables at cursor time.
          if (foundMilestoneFlag == false) {
            tempSegment.betweenMilestones = true;
            IntBaseArray values = new IntBaseArray(((Actor)actors.get(actorIndex)).aniVarCount);
            for (int j=0;j<((Actor)actors.get(actorIndex)).aniVarCount;j++) {
// Base change
              double step = ((double)(((Milestone)tempSegment.currentMilestone.next).getAniVarValues().get(j)-tempSegment.currentMilestone.getAniVarValues().get(j)))
                /((double)(tempSegment.currentMilestone.next.when-tempSegment.currentMilestone.when));
              int newValue = (int)(tempSegment.currentMilestone.getAniVarValues().get(j)
                +step*((double)(newCursorTime-tempSegment.currentMilestone.when)));
              tempSegment.steps.setSize(((Actor)actors.get(actorIndex)).aniVarCount);
              tempSegment.steps.set(j,step);
              values.add(newValue);
            }
            ((Actor)actors.get(actorIndex)).getActorInterface().setVarValues(values, ((Actor)actors.get(actorIndex)).animationSession);
//            ((Actor)actors.get(actorIndex)).setVarValues(values);
          }
        }
      }
      // Process milestone variables.
      else {
        fireMilestoneProcessListeners(tempSegment.currentMilestone);
        // Fire event for entering a segment.
        if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.startMilestone)
          fireSegmentEnteredListeners(((Actor)actors.get(actorIndex)).currentSegment);
        // Fire event for exiting a segment.
        if (tempSegment.currentMilestone == ((Actor)actors.get(actorIndex)).currentSegment.endMilestone)
          fireSegmentExitedListeners(((Actor)actors.get(actorIndex)).currentSegment);
        ((Actor)actors.get(actorIndex)).actorInterface.setVarValues(tempSegment.currentMilestone.getAniVarValues(), ((Actor)actors.get(actorIndex)).animationSession);
//        ((Actor)actors.get(actorIndex)).setVarValues(tempSegment.currentMilestone.getAniVarValues());
      }
    }
  }

  /**
   * Returns the animation's cursor time.
   * @return	The cursor time.
   */
  public int getCursorTime() {
    return cursorTime;
  }

  /**
   * Sets the animation's delay time.
   * @param	delay   The delay time to set.
   */
/*  public void setDelay(int delay) {
    if (delay<0) {
      delay=0;
    }
    this.delay = delay;
  }*/

  /**
   * Returns the animation's delay time.
   * @return	The delay time.
   */
/*  public int getDelay() {
    return delay;
  }*/

  /**
   * Start play thread.
   */
  public void play() {
    if ((pt == null) && (getMaxFrameTime()>0) /*&& (cursorTime<getMaxFrameTime())*/) {
      pt = new PlayThread(this);
      firePlayThreadStartedAnimationViewListeners();
      pt.start();
    }
  }
  /**
   * Stop play thread.
   */
  public void stop() {
    if (pt != null) {
      pt.stopThread();
      if (audioClip != null)
        audioClip.stop();
    }
  }

  /**
   * Returns the animation's rightmost segment's end.
   * @return	The rightmost segment's end.
   */
  public int getMaxFrameTime() {
    int maxFrameTime = 0;
    for (int i=0;i<actors.size();i++) {
      if (((BaseActor)actors.get(i)).startSegment != null ) {
// Base change
        BaseSegment iteratorSegment = ((BaseActor)actors.get(i)).startSegment;
        while (iteratorSegment.next != null) {
          iteratorSegment = iteratorSegment.next;
        }
        if (maxFrameTime < iteratorSegment.getEnd())
          maxFrameTime = iteratorSegment.getEnd();
      }
    }
    return maxFrameTime;
  }

  /**
   * Start playing from a frame.
   * @param startFrame Start frame.
   */
  public void playFromFrame(int startFrame) {
    if (startFrame <= getMaxFrameTime()) {
      this.startFrame = startFrame;
      setCursorTime(startFrame);
      play();
    }
  }

  /**
   * Playing from a frame to another.
   * @param startFrame Start frame.
   * @param endFrame   End frame.
   */
  public void playFromFrameToFrame(int startFrame, int endFrame) {
    if (endFrame > startFrame) {
      if (startFrame <= getMaxFrameTime()) {
        this.startFrame = startFrame;
        this.stopFrame = endFrame;
        setCursorTime(startFrame);
        play();
      }
    }
  }

  /**
   * Start playing from a label.
   * @param startLabel Start label.
   */
  public void playFromLabel(String startLabel) {
    int startFrame = getFrameFromLabel(startLabel);
    if (startFrame >= 0)
      playFromFrame(startFrame);
  }

  /**
   * Playing from a label to another.
   * @param startLabel Start label.
   * @param endLabel   End label.
   */
  public void playFromLabelToLabel(String startLabel, String endLabel) {
    int startFrame = getFrameFromLabel(startLabel);
    int endFrame = getFrameFromLabel(endLabel);
    if ((startFrame >= 0) && (endFrame >= 0))
      playFromFrameToFrame(startFrame, endFrame);
  }

  /**
   * Going to a frame.
   * @param frame The frame to go.
   */
  public void goToFrame(int frame) {
    setCursorTime(frame);
  }

  /**
   * Start playing sound file.
   * @param soundFile The sound file.
   */
  public void soundPlay(File soundFile) {
    //Try to get the AudioClip.
    audioClip = soundList.getClip(soundFile.getName());
    if (audioClip != null)
      audioClip.play();     //Play it once.
  }

  /**
   * Sto playing the sound file.
   * @param soundFile The sound file.
   */
  public void soundStop(File soundFile) {
    if (audioClip != null)
      audioClip.stop();
    audioClip = null;
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws    IOException
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion);

    map.put(ACTORS, actors);
    map.put(FRAMELABELS, frameLabels);
    map.put(LOOP, isLoopedPlayback());
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
//    readExternalFlag = true;
    StorageStructure map = (StorageStructure)(oi.readObject());

    actors = (ArrayList)map.get(ACTORS);
    for (int i=0;i<actors.size();i++) {
      if (i==0)
        ((SoundActor)actors.get(i)).animation = this;
      else
        ((Actor)actors.get(i)).animation = this;
    }
    frameLabels = (ObjectBaseArray)((ObjectBaseArray)map.get(FRAMELABELS)).clone();
//    Thread.currentThread().dumpStack();

    SoundSegment iterator = (SoundSegment)((BaseActor)actors.get(0)).startSegment;
    while (iterator != null) {
      if (iterator.soundFile != null) {
        String fileName;
        URL fileURL = null;

        fileName = iterator.soundFile.getName();
        try {
            fileURL = iterator.soundFile.toURL();
        } catch (MalformedURLException e){
            System.err.println(e.getMessage());
        }
        soundList.startLoading(fileURL, fileName);
      }
      iterator = (SoundSegment)iterator.next;
    }
    
    setLoopedPlayback(map.get(LOOP, true));
  }
}
