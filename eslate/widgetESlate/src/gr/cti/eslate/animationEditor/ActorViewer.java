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
 * This class implements a view and controller for the actor model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-Mar-2002
 * @see		gr.cti.eslate.animationEditor.AnimationEditor
 */
public class ActorViewer extends BaseActorViewer {

  /**
   * Create an actor viewer component.
   * @param	aniEditor       The animation viewer to create the actor viewer in.
   * @param	newActor        The actor to create the actor viewer of.
   * @param	newActorShape   The actor shape to link the actor viewer with.
   */
  public ActorViewer(AnimationEditor aniEditor, BaseActor newActor, Rectangle2D.Double newActorShape) {
    this.animationEditor = aniEditor;
    this.actor = newActor;
    this.actorShape = newActorShape;

    // Actor listener and its methods (segmentAdded,segmentRemoved).
    actorListener = new ActorListener() {
      public void segmentAdded(ActorEvent e) {
        if (animationEditor.processModelEvents == false)
          animationEditor.processModelEvents = true;
        else {
          int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
          Rectangle r = new Rectangle();
          int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
          int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
          Rectangle2D.Double segmentShape = new Rectangle2D.Double();
          SegmentViewer segmentViewer = new SegmentViewer(ActorViewer.this, e.getSegment(), segmentShape);
          segmentViewers.add(segmentViewer);
//          Rectangle2D.Double milestoneShapeStart = new Rectangle2D.Double();
//          Rectangle2D.Double milestoneShapeEnd = new Rectangle2D.Double();
          Ellipse2D.Double milestoneShapeStart = new Ellipse2D.Double();
          Ellipse2D.Double milestoneShapeEnd = new Ellipse2D.Double();
          MilestoneViewer milestoneViewerStart = new MilestoneViewer(segmentViewer, e.getSegment().getStartMilestone(), milestoneShapeStart);
          MilestoneViewer milestoneViewerEnd = new MilestoneViewer(segmentViewer, e.getSegment().getEndMilestone(), milestoneShapeEnd);
          segmentViewer.addMilestoneViewer(milestoneViewerStart);
          segmentViewer.addMilestoneViewer(milestoneViewerEnd);

          if (e.getSegment().getEnd() > animationEditor.actorViewerMaxWidth)
            animationEditor.actorViewerMaxWidth = e.getSegment().getEnd();
          if (animationEditor.actorViewerMaxWidth != actorViewerRealWidth) {
            actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
          }
          if (actorViewerRealWidth < animationEditor.getReal(width))
            actorViewerRealWidth = animationEditor.getReal(width);
          actorViewerZoomedWidth = animationEditor.getZoomed(actorViewerRealWidth);
          for (int k=0;k<animationEditor.actorViewers.size();k++) {
            ((ActorViewer)animationEditor.actorViewers.get(k)).actorViewerRealWidth = actorViewerRealWidth;
            ((ActorViewer)animationEditor.actorViewers.get(k)).actorViewerZoomedWidth = actorViewerZoomedWidth;
          }
          animationEditor.mainPanelZoomedWidth = actorViewerZoomedWidth;
          animationEditor.mainPanelRealWidth = actorViewerRealWidth;
          animationEditor.mainPanel.setPreferredSize(new Dimension(
            animationEditor.mainPanelZoomedWidth,
            animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
          animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
          animationEditor.columnView.revalidate();
          animationEditor.mainPanel.revalidate();
          r.setRect(actorShape.getWidth()+offsetX, actorShape.getY()+offsetY, 1, 1);
          animationEditor.mainPanel.scrollRectToVisible(r);

          animationEditor.mainPanel.paintScoreImage(
            animationEditor.actorViewers.indexOf(ActorViewer.this),
            animationEditor.actorViewers.indexOf(ActorViewer.this),
            0,
            actorViewerRealWidth);
          animationEditor.mainPanel.repaint(
            0,
            animationEditor.actorViewers.indexOf(ActorViewer.this)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
            actorViewerZoomedWidth,
            animationEditor.ACTOR_VIEWER_HEIGHT);
        }
      }
      public void segmentRemoved(ActorEvent e) {
        if (animationEditor.processModelEvents == false)
          animationEditor.processModelEvents = true;
        else {
          int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
          Rectangle r = new Rectangle();
          int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
          int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
          for (int i=0;i<segmentViewers.size();i++) {
            if (((SegmentViewer)segmentViewers.get(i)).segment == e.getSegment()) {
              removeSegmentViewer((SegmentViewer)segmentViewers.get(i));
              break;
            }
          }

          animationEditor.computeActorViewerMaxWidth();
          if (animationEditor.actorViewerMaxWidth != actorViewerRealWidth) {
            actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
          }
          if (actorViewerRealWidth < animationEditor.getReal(width))
            actorViewerRealWidth = animationEditor.getReal(width);
          actorViewerZoomedWidth = animationEditor.getZoomed(actorViewerRealWidth);
          for (int k=0;k<animationEditor.actorViewers.size();k++) {
            ((ActorViewer)animationEditor.actorViewers.get(k)).actorViewerRealWidth = actorViewerRealWidth;
            ((ActorViewer)animationEditor.actorViewers.get(k)).actorViewerZoomedWidth = actorViewerZoomedWidth;
          }
          animationEditor.mainPanelZoomedWidth = actorViewerZoomedWidth;
          animationEditor.mainPanelRealWidth = actorViewerRealWidth;
          animationEditor.mainPanel.setPreferredSize(new Dimension(
            animationEditor.mainPanelZoomedWidth,
            animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
          animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
          animationEditor.columnView.revalidate();
          animationEditor.mainPanel.revalidate();
          r.setRect(actorShape.getWidth()+offsetX, actorShape.getY()+offsetY, 1, 1);
          animationEditor.mainPanel.scrollRectToVisible(r);

          animationEditor.mainPanel.paintScoreImage(
            animationEditor.actorViewers.indexOf(ActorViewer.this),
            animationEditor.actorViewers.indexOf(ActorViewer.this),
            0,
            actorViewerRealWidth);
          animationEditor.mainPanel.repaint(
            0,
            animationEditor.actorViewers.indexOf(ActorViewer.this)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
            actorViewerZoomedWidth,
            animationEditor.ACTOR_VIEWER_HEIGHT);
        }
      }
    };
    actor.addActorListener(actorListener);
  }

}