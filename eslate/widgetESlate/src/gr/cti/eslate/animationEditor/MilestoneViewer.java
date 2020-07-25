package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.*;
import java.awt.geom.*;
import java.awt.*;

/**
 * This class implements a view and controller for the milestone model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 * @see		gr.cti.eslate.animationEditor.SegmentViewer
 */
public class MilestoneViewer extends BaseMilestoneViewer{
  boolean delete=false;

  /**
   * Create a milestone viewer component.
   * @param	newSegmentViewer   The segment viewer to create the milestone viewer in.
   * @param	newMilestone       The milestone to create the milestone viewer of.
   * @param	newMilestoneShape  The milestone shape to link the milestone viewer with.
   */
  public MilestoneViewer(BaseSegmentViewer newSegmentViewer, BaseMilestone newMilestone, Ellipse2D.Double newMilestoneShape) {
    this.segmentViewer = newSegmentViewer;
    this.milestone = newMilestone;
    this.milestoneShape = newMilestoneShape;

    // Milestone listener and its method (whenChanged).
    milestoneListener = new MilestoneListener() {
      public void whenChanged(MilestoneEvent e) {
        if (segmentViewer.actorViewer.animationEditor.processModelEvents == false)
          segmentViewer.actorViewer.animationEditor.processModelEvents = true;
        else {
          Rectangle r = new Rectangle();
          int offsetY = segmentViewer.actorViewer.animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
          int width = segmentViewer.actorViewer.animationEditor.getSize().width-segmentViewer.actorViewer.animationEditor.scrollPaneX-segmentViewer.actorViewer.animationEditor.lblActorWidth-segmentViewer.actorViewer.animationEditor.viewportGap;
          segmentViewer.actorViewer.animationEditor.computeActorViewerMaxWidth();
          if (segmentViewer.actorViewer.animationEditor.columnView.getFrameEndTime(e.getWhen()) > segmentViewer.actorViewer.animationEditor.actorViewerMaxWidth)
            segmentViewer.actorViewer.animationEditor.actorViewerMaxWidth = segmentViewer.actorViewer.animationEditor.columnView.getFrameEndTime(e.getWhen());
          int widthSet;
          if (segmentViewer.actorViewer.animationEditor.actorViewerMaxWidth < segmentViewer.actorViewer.animationEditor.getReal(width))
            widthSet = segmentViewer.actorViewer.animationEditor.getReal(width);
          else {
            widthSet = segmentViewer.actorViewer.animationEditor.actorViewerMaxWidth;
            for (int k=0;k<segmentViewer.actorViewer.animationEditor.actorViewers.size();k++) {
              ((BaseActorViewer)segmentViewer.actorViewer.animationEditor.actorViewers.get(k)).actorViewerRealWidth = widthSet;
              ((BaseActorViewer)segmentViewer.actorViewer.animationEditor.actorViewers.get(k)).actorViewerZoomedWidth = segmentViewer.actorViewer.animationEditor.getZoomed(widthSet);
            }
            segmentViewer.actorViewer.animationEditor.mainPanelZoomedWidth = segmentViewer.actorViewer.animationEditor.getZoomed(widthSet);
            segmentViewer.actorViewer.animationEditor.mainPanelRealWidth = widthSet;
            segmentViewer.actorViewer.animationEditor.mainPanel.setPreferredSize(new Dimension(segmentViewer.actorViewer.animationEditor.mainPanelZoomedWidth, segmentViewer.actorViewer.animationEditor.actorViewers.size()*(segmentViewer.actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+segmentViewer.actorViewer.animationEditor.ACTOR_SPACE)));
            segmentViewer.actorViewer.animationEditor.columnView.setPreferredWidth(segmentViewer.actorViewer.animationEditor.mainPanelZoomedWidth);
            segmentViewer.actorViewer.animationEditor.columnView.revalidate();
            segmentViewer.actorViewer.animationEditor.mainPanel.revalidate();
          }
          segmentViewer.actorViewer.animationEditor.mainPanel.paintScoreImage(
            segmentViewer.actorViewer.animationEditor.actorViewers.indexOf(segmentViewer.actorViewer),
            segmentViewer.actorViewer.animationEditor.actorViewers.indexOf(segmentViewer.actorViewer),
            0,
            segmentViewer.actorViewer.actorViewerRealWidth);
          segmentViewer.actorViewer.animationEditor.mainPanel.repaint(
            0,
            segmentViewer.actorViewer.animationEditor.actorViewers.indexOf(segmentViewer.actorViewer)*(segmentViewer.actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+segmentViewer.actorViewer.animationEditor.ACTOR_SPACE),
            segmentViewer.actorViewer.actorViewerZoomedWidth,
            segmentViewer.actorViewer.animationEditor.ACTOR_VIEWER_HEIGHT);
          r.setRect(segmentViewer.actorViewer.animationEditor.getZoomed(segmentViewer.actorViewer.animationEditor.columnView.getFrameStartTime(milestone.getWhen())),segmentViewer.actorViewer.actorShape.getY()+offsetY,segmentViewer.actorViewer.animationEditor.columnView.increment,1);
          segmentViewer.actorViewer.animationEditor.mainPanel.scrollRectToVisible(r);
        }
      }
    };
    milestone.addMilestoneListener(milestoneListener);
  }
}
