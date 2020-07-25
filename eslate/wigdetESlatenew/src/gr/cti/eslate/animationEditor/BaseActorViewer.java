package gr.cti.eslate.animationEditor;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import gr.cti.eslate.animation.*;
import com.zookitec.layout.*;
import java.awt.geom.*;

/**
 * This class implements a view and controller for the base actor model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-Mar-2002
 * @see		gr.cti.eslate.animationEditor.AnimationEditor
 */
public abstract class BaseActorViewer {
  AnimationEditor animationEditor;
  BaseActor actor;
  Rectangle2D.Double actorShape;
  ArrayList segmentViewers = new ArrayList();
  ActorListener actorListener;

  int actorViewerRealWidth;
  int actorViewerZoomedWidth;

  /**
   * Sets actor's real width.
   * @param   newActorViewerRealWidth   Actor viewer's real width.
   */
  public void setActorViewerRealWidth(int newActorViewerRealWidth) {
    this.actorViewerRealWidth = newActorViewerRealWidth;
  }

  /**
   * Gets actor viewer's real width.
   * @return  The actor viewer's real width.
   */
  public int getActorViewerRealWidth() {
    return actorViewerRealWidth;
  }

  /**
   * Sets actor's zoomed width.
   * @param   newActorViewerZoomedWidth   Actor viewer's zoomed width.
   */
  public void setActorViewerZoomedWidth(int newActorViewerZoomedWidth) {
    this.actorViewerZoomedWidth = newActorViewerZoomedWidth;
  }

  /**
   * Gets actor viewer's zoomed width.
   * @return  The actor viewer's zoomed width.
   */
  public int getActorViewerZoomedWidth() {
    return actorViewerZoomedWidth;
  }

  /**
   * Add a segment viewer.
   * @param	segmentViewer   The segment viewer to add.
   */
  public void addSegmentViewer(BaseSegmentViewer segmentViewer) {
    synchronized (segmentViewers) {
      if (!segmentViewers.contains(segmentViewer)) {
        if (segmentViewers.size() == 0)
          segmentViewers.add(segmentViewer);
        else {
          if (segmentViewer.segment.getStart()<((BaseSegmentViewer)segmentViewers.get(0)).segment.getStart())
            segmentViewers.add(0,segmentViewer);
          else {
            for (int i=0;i<segmentViewers.size();i++) {
              if (segmentViewer.segment.getStart()>((BaseSegmentViewer)segmentViewers.get(i)).segment.getStart()) {
                if (segmentViewers.size()>=i+2) {
                  if (segmentViewer.segment.getStart()<((BaseSegmentViewer)segmentViewers.get(i+1)).segment.getStart()) {
                    segmentViewers.add(i+1,segmentViewer);
                    break;
                  }
                }
                else {
                  segmentViewers.add(i+1,segmentViewer);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * Remove a segment viewer.
   * @param	segmentViewer   The segment viewer to remove.
   */
  public void removeSegmentViewer(BaseSegmentViewer segmentViewer) {
    // Remove segment's listeners from animation
    for (int k=0;k<segmentViewer.milestoneViewers.size();k++) {
      ((BaseMilestoneViewer)segmentViewer.milestoneViewers.get(k)).milestone.removeMilestoneListener(((BaseMilestoneViewer)segmentViewer.milestoneViewers.get(k)).milestoneListener);
    }
    segmentViewer.segment.removeSegmentListener(segmentViewer.segmentListener);
    synchronized (segmentViewers) {
      int ind = segmentViewers.indexOf(segmentViewer);
      if (ind >= 0) {
        segmentViewers.remove(segmentViewer);
      }
    }
  }
}