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
 * This class implements a view and controller for the sound segment model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 27-Jun-2002
 * @see		gr.cti.eslate.animationEditor.SoundActorViewer
 */
public class SoundSegmentViewer extends BaseSegmentViewer {

  /**
   * Create a segment viewer component.
   * @param	newActorViewer   The actor viewer to create the segment viewer in.
   * @param	newSegment       The segment to create the segment viewer of.
   * @param	newSegmentShape  The segment shape to link the segment viewer with.
   */
  public SoundSegmentViewer(BaseActorViewer newActorViewer, BaseSegment newSegment, Rectangle2D.Double newSegmentShape) {
    this.actorViewer = newActorViewer;
    this.segment = newSegment;
    this.segmentShape = newSegmentShape;

    // Segment listener and its methods (milestoneAdded,milestoneRemoved).
    segmentListener = new SegmentListener() {
      public void milestoneAdded(SegmentEvent e) {
        if (actorViewer.animationEditor.processModelEvents == false)
          actorViewer.animationEditor.processModelEvents = true;
        else {
//          Rectangle2D.Double milestoneShape = new Rectangle2D.Double();
          Ellipse2D.Double milestoneShape = new Ellipse2D.Double();
          MilestoneViewer milestoneViewer = new MilestoneViewer(SoundSegmentViewer.this, e.getMilestone(), milestoneShape);
          addMilestoneViewer(milestoneViewer);

          actorViewer.animationEditor.mainPanel.paintScoreImage(
            actorViewer.animationEditor.actorViewers.indexOf(actorViewer),
            actorViewer.animationEditor.actorViewers.indexOf(actorViewer),
            0,
            actorViewer.actorViewerRealWidth);
          actorViewer.animationEditor.mainPanel.repaint(
            0,
            actorViewer.animationEditor.actorViewers.indexOf(actorViewer)*(actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+actorViewer.animationEditor.ACTOR_SPACE),
            actorViewer.actorViewerZoomedWidth,
            actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT);
        }
      }
      public void milestoneRemoved(SegmentEvent e) {
        if (actorViewer.animationEditor.processModelEvents == false)
          actorViewer.animationEditor.processModelEvents = true;
        else {
          for (int i=0;i<milestoneViewers.size();i++) {
            if (((MilestoneViewer)milestoneViewers.get(i)).milestone == e.getMilestone()) {
              removeMilestoneViewer((MilestoneViewer)milestoneViewers.get(i));
              break;
            }
          }
          actorViewer.animationEditor.mainPanel.paintScoreImage(
            actorViewer.animationEditor.actorViewers.indexOf(actorViewer),
            actorViewer.animationEditor.actorViewers.indexOf(actorViewer),
            0,
            actorViewer.actorViewerRealWidth);
          actorViewer.animationEditor.mainPanel.repaint(
            0,
            actorViewer.animationEditor.actorViewers.indexOf(actorViewer)*(actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+actorViewer.animationEditor.ACTOR_SPACE),
            actorViewer.actorViewerZoomedWidth,
            actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT);
        }
      }
    };
    segment.addSegmentListener(segmentListener);
  }

}
