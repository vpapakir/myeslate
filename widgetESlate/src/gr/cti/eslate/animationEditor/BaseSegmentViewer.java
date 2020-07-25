package gr.cti.eslate.animationEditor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import gr.cti.eslate.animation.*;
import java.awt.geom.*;
import java.util.*;

/**
 * This class implements a view and controller for the base segment model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 26-Jun-2002
 * @see		gr.cti.eslate.animationEditor.BaseActorViewer
 */
public abstract class BaseSegmentViewer{
  BaseActorViewer actorViewer;
  BaseSegment segment;
  Rectangle2D.Double segmentShape;
  int segmentZeroStart;
  int segmentZeroEnd;
  ArrayList milestoneViewers = new ArrayList();
  SegmentListener segmentListener;

  /**
   * Sets the segment viewer's zero start time.
   * @param	newSegmentZeroStart   Segment viewer's zerostart time.
   */
  public void setSegmentZeroStart(int newSegmentZeroStart) {
    this.segmentZeroStart = newSegmentZeroStart;
  }

  /**
   * Gets the segment viewer's zerostart time.
   * @return	The zero start time.
   */
  public int getSegmentZeroStart() {
    return segmentZeroStart;
  }

  /**
   * Sets the segment viewer's zero end time.
   * @param	newSegmentZeroEnd   Segment viewer's zero end time.
   */
  public void setSegmentZeroEnd(int newSegmentZeroEnd) {
    this.segmentZeroEnd = newSegmentZeroEnd;
  }

  /**
   * Gets the segment viewer's zero end time.
   * @return	The zero end time.
   */
  public int getSegmentZeroEnd() {
    return segmentZeroEnd;
  }

   /**
   * Add a milestone viewer.
   * @param	milestoneViewer   The milestone viewer to add.
   */
  public void addMilestoneViewer(BaseMilestoneViewer milestoneViewer) {
    synchronized (milestoneViewers) {
      if (!milestoneViewers.contains(milestoneViewer)) {
        if (milestoneViewers.size() >=2) {
          for (int i=0;i<milestoneViewers.size();i++) {
            if ((milestoneViewer.milestone.getWhen()>((BaseMilestoneViewer)milestoneViewers.get(i)).milestone.getWhen())
              && (milestoneViewer.milestone.getWhen()<((BaseMilestoneViewer)milestoneViewers.get(i+1)).milestone.getWhen())) {
                milestoneViewers.add(i+1,milestoneViewer);
                break;
            }
          }
        } else
          milestoneViewers.add(milestoneViewer);
      }
    }
  }

  /**
   * Remove a milestone viewer.
   * @param	milestoneViewer   The milestone viewer to remove.
   */
  public void removeMilestoneViewer(BaseMilestoneViewer milestoneViewer) {
    // Remove milestone's listeners from animation
    milestoneViewer.milestone.removeMilestoneListener(milestoneViewer.milestoneListener);
    synchronized (milestoneViewers) {
      int ind = milestoneViewers.indexOf(milestoneViewer);
      if (ind >= 0) {
        milestoneViewers.remove(milestoneViewer);
      }
    }
  }

}
