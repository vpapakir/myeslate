package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.BaseSegment;
import gr.cti.eslate.animation.Milestone;
import gr.cti.eslate.animation.Segment;
import gr.cti.eslate.animation.SoundSegment;
import gr.cti.eslate.protocol.AnimatedPropertyDescriptor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * This class handles events for mouseListener and mouseMotionListener.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see	gr.cti.eslate.animationEditor.AnimationEditor
public class MouseInputHandler extends MouseInputAdapter {
  AnimationEditor animationEditor;
// Base change
  BaseActorViewer workingActorViewer;
  BaseSegmentViewer workingSegmentViewer;
  BaseMilestoneViewer workingMilestoneViewer;

  boolean segmentProcess;
  boolean milestoneProcess;
  boolean actorProcess;
  int whereInSegment = 0;
  int mouseOldX;
  int segmentOldX;
  int segmentOldWidth;
  int mousePressedX;
  int segmentCreation = 0;
  boolean zeroFlagLeft;
  boolean zeroFlagRight;
  int actorViewerZeroMaxWidth = 0;
//  int actorViewerZeroWidth;
  int zeroOffsetX;
  boolean releaseFlag;
  boolean createSegment;
  boolean negativeFlag;
  Cursor moveCursor;
  Cursor arrowCursor;
  MilestoneViewer tempMilestoneViewer;
  boolean createSoundSegment = false;

  /**
   * Create a MouseInputHandler.
   * @param animationEditor  The animation viewer of the mouseInputHandler.
   */
  public MouseInputHandler(AnimationEditor animationEditor) {
    this.animationEditor = animationEditor;
    moveCursor = Toolkit.getDefaultToolkit().createCustomCursor(
      new ImageIcon(getClass().getResource("images/h_move.gif")).getImage(),
      new Point(16,6),
      "moveCursor");
    arrowCursor = Toolkit.getDefaultToolkit().createCustomCursor(
      new ImageIcon(getClass().getResource("images/h_arrow.gif")).getImage(),
      new Point(12,2),
      "arrowCursor");
  }

  /**
   * Checks where the mouse moves in order to set the right cursor icon.
   * @param e
   */
  public void mouseMoved(MouseEvent e) {
    animationEditor.mouseMarkXStart = e.getX();
    animationEditor.mouseMarkXEnd = -1;
    if (animationEditor.mouseMarkXStart % animationEditor.columnView.increment != 0)
      animationEditor.mouseMarkXStart -= animationEditor.mouseMarkXStart % animationEditor.columnView.increment;
    animationEditor.columnView.repaint();

    int virtualMouseX = e.getX()-animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int virtualMouseY = e.getY()-animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    animationEditor.mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    BaseActorViewer tempActorViewer;
    BaseSegmentViewer tempSegmentViewer;
    BaseMilestoneViewer tempMilestoneViewer;
    for (int i=0;i<animationEditor.actorViewers.size();i++) {
      if (i==0)
        tempActorViewer = (SoundActorViewer)animationEditor.actorViewers.get(i);
      else
        tempActorViewer = (ActorViewer)animationEditor.actorViewers.get(i);
      if (tempActorViewer.actorShape.contains(virtualMouseX,virtualMouseY)) {
        for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
          if (i==0)
            tempSegmentViewer = (SoundSegmentViewer)tempActorViewer.segmentViewers.get(j);
          else
            tempSegmentViewer = (SegmentViewer)tempActorViewer.segmentViewers.get(j);
          if (tempSegmentViewer.segmentShape.contains(virtualMouseX,virtualMouseY)) {
            double left = tempSegmentViewer.segmentShape.getX();
            double right = tempSegmentViewer.segmentShape.getX()+
              tempSegmentViewer.segmentShape.width;
            int cursorFlag=0;
            for (int k=0;k<tempSegmentViewer.milestoneViewers.size();k++) {
              if (i==0)
                tempMilestoneViewer = (SoundMilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
              else
                tempMilestoneViewer = (MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);

              int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
              int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
              int milestoneTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(tempMilestoneViewer.milestone.getWhen()));
              if (tempMilestoneViewer.milestoneShape.contains(virtualMouseX,virtualMouseY)) {
            	  animationEditor.mainPanel.setCursor(arrowCursor);
                cursorFlag=1;
                break;
              }
            }
            if (cursorFlag==0) {
              if ((virtualMouseX >= left) && (virtualMouseX <= left+2))
                animationEditor.mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
              else if ((virtualMouseX > left+2) && (virtualMouseX < right-2))
                animationEditor.mainPanel.setCursor(moveCursor);
              else if ((virtualMouseX >= right-2) && (virtualMouseX <= right))
                animationEditor.mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            }
            break;
          }
        }
        break;
      }
    }
  }

  /**
   * Checks where the mouse is pressed in the main panel of the animation viewer.
   * @param e
   */
  public void mousePressed(MouseEvent e) {
    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    int virtualMouseX = e.getX() - offsetX;
    int virtualMouseY = e.getY() - offsetY;
    int realMouseX = animationEditor.getReal(e.getX());
    int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
    Rectangle r = new Rectangle();
    // Check if play thread is active.
    if (animationEditor.animation == null)
    	return;
    if (!animationEditor.animation.isPlayThreadActive())
    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
      tempMilestoneViewer = null;
      if (animationEditor.editMilestoneViewer != null) {
        tempMilestoneViewer = animationEditor.editMilestoneViewer;
        animationEditor.exitMilestoneEditMode();
      }
      workingActorViewer = null;
      workingSegmentViewer = null;
      workingMilestoneViewer = null;

      BaseActorViewer tempActorViewer;
      BaseSegmentViewer tempSegmentViewer;
      BaseMilestoneViewer tempMilestoneViewer;
      for (int i=0;i<animationEditor.actorViewers.size();i++) {
        if (i==0)
          tempActorViewer = (SoundActorViewer)animationEditor.actorViewers.get(i);
        else
          tempActorViewer = (ActorViewer)animationEditor.actorViewers.get(i);
        if (tempActorViewer.actorShape.contains(virtualMouseX,virtualMouseY)) {
          if (i==0)
            workingActorViewer = (SoundActorViewer)animationEditor.actorViewers.get(i);
          else
            workingActorViewer = (ActorViewer)animationEditor.actorViewers.get(i);
          segmentProcess = false;
          milestoneProcess = false;
          for (int j=0;j<workingActorViewer.segmentViewers.size();j++) {
            if (i==0)
              tempSegmentViewer = (SoundSegmentViewer)tempActorViewer.segmentViewers.get(j);
            else
              tempSegmentViewer = (SegmentViewer)tempActorViewer.segmentViewers.get(j);
            if (tempSegmentViewer.segmentShape.contains(virtualMouseX,virtualMouseY)) {
              if (i==0)
                workingSegmentViewer = (SoundSegmentViewer)workingActorViewer.segmentViewers.get(j);
              else
                workingSegmentViewer = (SegmentViewer)workingActorViewer.segmentViewers.get(j);
              for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                if (i==0)
                  tempMilestoneViewer = (SoundMilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
                else
                  tempMilestoneViewer = (MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);

                int milestoneTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(tempMilestoneViewer.milestone.getWhen()));
                // Mouse is pressed on a milestone.
                if (tempMilestoneViewer.milestoneShape.contains(virtualMouseX,virtualMouseY)) {
                  if (i==0)
                    workingMilestoneViewer = (SoundMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k);
                  else
                    workingMilestoneViewer = (MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k);
                  milestoneProcess = true;
                  break;
                }
              }
              // Mouse is pressed on a segment.
              if (milestoneProcess == false) {
                segmentProcess = true;
                int left = animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart());
                int right = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd());
                double segmentLeft = tempSegmentViewer.segmentShape.getX();
                double segmentRight = tempSegmentViewer.segmentShape.getX()+
                  tempSegmentViewer.segmentShape.width;
                whereInSegment = 2;
                segmentOldX = left;
                mouseOldX = e.getX();
                segmentOldWidth = right-left;
                if ((virtualMouseX >= segmentLeft) && (virtualMouseX <= segmentLeft+2))
                  whereInSegment = 1;
                else if ((virtualMouseX >= segmentRight-2) && (virtualMouseX <= segmentRight))
                  whereInSegment = 3;
                for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                  if ((((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                    && (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                      ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime
                        = ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen();
                  }
                }
                releaseFlag = false;
              }
              break;
            }
          }
          // Mouse is pressed on an actor.
          if ((segmentProcess == false) && (milestoneProcess == false)) {
            mousePressedX = e.getX();
            actorProcess = true;
            // Check if mouse is pressed in an actor but above or beneath a segment
            for (i=0;i<workingActorViewer.segmentViewers.size();i++) {
              if (((BaseSegmentViewer)workingActorViewer.segmentViewers.get(i)).segment.isTimeInSegment(animationEditor.columnView.getFrame(animationEditor.getReal(mousePressedX))))
                  actorProcess = false;
            }
            if (actorProcess == true) {
              segmentCreation = 0;
              if (mousePressedX % animationEditor.columnView.increment != 0)
                mousePressedX -= mousePressedX % animationEditor.columnView.increment;
              animationEditor.mainPanel.paintTempSegment(
                animationEditor.actorViewers.indexOf(workingActorViewer),
                mousePressedX);
              animationEditor.mainPanel.repaint(0,
                animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                workingActorViewer.actorViewerZoomedWidth,
                animationEditor.ACTOR_VIEWER_HEIGHT);
              createSegment = true;
            }
            break;
          }
        }
      }
      zeroFlagLeft = false;
      zeroFlagRight = false;
      negativeFlag = false;
    }
  }

  /**
   * Checks for mouse release.
   * @param e
   */
  public void mouseReleased(MouseEvent e) {
    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
      // Checks for false segment creation.
      if (workingActorViewer != null) {
        int mouseReleasedX = e.getX();
        if (mouseReleasedX % animationEditor.columnView.increment != 0)
          mouseReleasedX -= mouseReleasedX % animationEditor.columnView.increment;
        if (mouseReleasedX == mousePressedX) {
          animationEditor.mainPanel.paintScoreImage(
            animationEditor.actorViewers.indexOf(workingActorViewer),
            animationEditor.actorViewers.indexOf(workingActorViewer),
            0,
            workingActorViewer.actorViewerRealWidth);
          animationEditor.mainPanel.repaint(
            0,
            animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
            workingActorViewer.actorViewerZoomedWidth,
            animationEditor.ACTOR_VIEWER_HEIGHT);
        }
        else if (SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass())) {
          if (createSoundSegment) {
            createSoundSegment = false;
            animationEditor.chooseSoundFile((SoundSegment)workingSegmentViewer.segment);
          }
        }
      }
      // Checks for remove milestones in case of segment shrinking.
      if (releaseFlag) {
        for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
          if (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).delete == true) {
            workingSegmentViewer.actorViewer.animationEditor.processModelEvents = false;
            workingSegmentViewer.segment.removeMilestone(((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone);
            workingSegmentViewer.removeMilestoneViewer((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k));
          }
        }
        animationEditor.mainPanel.paintScoreImage(
          animationEditor.actorViewers.indexOf(workingActorViewer),
          animationEditor.actorViewers.indexOf(workingActorViewer),
          0,
          workingActorViewer.actorViewerRealWidth);
        animationEditor.mainPanel.repaint(0,
          animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
          workingActorViewer.actorViewerZoomedWidth,
          animationEditor.ACTOR_VIEWER_HEIGHT);
      }
    }
  }

  /**
   * Checks where the mouse is dragged.
   * @param e
   */
  public void mouseDragged(MouseEvent e){
    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    int virtualMouseX = e.getX() - offsetX;
    int leftFrontier;
    int rightFrontier;
    Rectangle r = new Rectangle();
    int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
    // Check if play thread is active.
    if (!animationEditor.animation.isPlayThreadActive())
    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
      if (workingActorViewer != null) {
        // Create a segment
        if ((segmentProcess == false) && (milestoneProcess == false) && (actorProcess == true)) {
          animationEditor.processModelEvents = false;
          int mouseX = e.getX();
          if (mouseX % animationEditor.columnView.increment != 0)
            mouseX -= mouseX % animationEditor.columnView.increment;
          animationEditor.mouseMarkXStart = mouseX;
          animationEditor.mouseMarkXEnd = -1;
          animationEditor.columnView.repaint();

          if (createSegment) {
            if (mouseX != mousePressedX) {
              boolean inSegment = false;
              for (int i=0;i<workingActorViewer.segmentViewers.size();i++) {
                if (((BaseSegmentViewer)workingActorViewer.segmentViewers.get(i)).segment.isTimeInSegment(animationEditor.columnView.getFrame(animationEditor.getReal(e.getX()))))
                  inSegment = true;
              }
              if (inSegment == false) {
                createSegment = false;
                int startTime = 0;
                int endTime = 0;
                if (mouseX > mousePressedX) {
                  startTime = animationEditor.columnView.getFrame(animationEditor.getReal(mousePressedX));
                  endTime = animationEditor.columnView.getFrame(animationEditor.getReal(mouseX));
                }
                else if (mouseX < mousePressedX) {
                  startTime = animationEditor.columnView.getFrame(animationEditor.getReal(mouseX));
                  endTime = animationEditor.columnView.getFrame(animationEditor.getReal(mousePressedX));
                }

                BaseSegment segment;
                if (SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass())) {
                  segment = new SoundSegment(
                    workingActorViewer.actor,
                    startTime,
                    endTime);
                  createSoundSegment = true;
                }
                else {
                  segment = new Segment(
                    workingActorViewer.actor,
                    startTime,
                    endTime);
                }

                // Get actor's current values.
                if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass())) {
// Base change
                  Actor tempActor = (Actor)workingActorViewer.actor;
                  for (int i=0;i<tempActor.getVarCount();i++) {
                    if (tempActor.getAnimationSession().isAnimated(((AnimatedPropertyDescriptor)tempActor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID())) {
                      ((Milestone)segment.getStartMilestone()).getAniVarValues().add(tempActor.getActorInterface().getVarValues().get(i));
                      ((Milestone)segment.getEndMilestone()).getAniVarValues().add(tempActor.getActorInterface().getVarValues().get(i));
                    }
                  }
                }

                Rectangle2D.Double segmentShape = new Rectangle2D.Double();
                BaseSegmentViewer segmentViewer;
                if (SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                  segmentViewer = new SoundSegmentViewer(workingActorViewer, segment, segmentShape);
                else
                  segmentViewer = new SegmentViewer(workingActorViewer, segment, segmentShape);
                workingActorViewer.addSegmentViewer(segmentViewer);
                workingSegmentViewer = segmentViewer;
                animationEditor.processModelEvents = false;
                workingActorViewer.actor.addSegment(workingSegmentViewer.segment);
                Ellipse2D.Double milestoneShapeStart = new Ellipse2D.Double();
                Ellipse2D.Double milestoneShapeEnd = new Ellipse2D.Double();
                BaseMilestoneViewer milestoneViewerStart;
                BaseMilestoneViewer milestoneViewerEnd;
                if (SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass())) {
                  milestoneViewerStart = new SoundMilestoneViewer(segmentViewer, segment.getStartMilestone(), milestoneShapeStart);
                  milestoneViewerEnd = new SoundMilestoneViewer(segmentViewer, segment.getEndMilestone(), milestoneShapeEnd);
                }
                else {
                  milestoneViewerStart = new MilestoneViewer(segmentViewer, segment.getStartMilestone(), milestoneShapeStart);
                  milestoneViewerEnd = new MilestoneViewer(segmentViewer, segment.getEndMilestone(), milestoneShapeEnd);
                }
                segmentViewer.addMilestoneViewer(milestoneViewerStart);
                segmentViewer.addMilestoneViewer(milestoneViewerEnd);
                if (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd()) > animationEditor.actorViewerMaxWidth)
                  animationEditor.actorViewerMaxWidth = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd());
                if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth) {
                  workingActorViewer.actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
                }
                if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                  workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);
                for (int k=0;k<animationEditor.actorViewers.size();k++) {
                  ((BaseActorViewer)animationEditor.actorViewers.get(k)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                  ((BaseActorViewer)animationEditor.actorViewers.get(k)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                }
                animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                animationEditor.mainPanel.setPreferredSize(new Dimension(animationEditor.mainPanelZoomedWidth, animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
                animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                animationEditor.columnView.revalidate();
                animationEditor.mainPanel.revalidate();
                r.setRect(e.getX(), workingActorViewer.actorShape.getY()+offsetY, 1, 1);
                animationEditor.mainPanel.scrollRectToVisible(r);
                animationEditor.mainPanel.paintScoreImage(
                  animationEditor.actorViewers.indexOf(workingActorViewer),
                  animationEditor.actorViewers.indexOf(workingActorViewer),
                  0,
                  workingActorViewer.actorViewerRealWidth);
                animationEditor.mainPanel.repaint(0,
                  animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                  workingActorViewer.actorViewerZoomedWidth,
                  animationEditor.ACTOR_VIEWER_HEIGHT);
              }
              else
                createSegment = false;
            }
          }
          // Create segment moving the mouse to the right.
          if (mouseX > mousePressedX) {
            // Check if moving to the left, pass the minimum width and continue
            // moving to the left.
            if ((segmentCreation == 0) || (segmentCreation == 2)) {
              segmentCreation = 2;
              // Check if there is another segment to the right.
              if (workingSegmentViewer.segment.getNext() != null) {
                rightFrontier = animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getNext().getStart())-1;
                if (animationEditor.getReal(mouseX) <= rightFrontier) {
                  if (animationEditor.getReal(mouseX) > animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())+2*animationEditor.getReal(animationEditor.columnView.increment)-1)
                    workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(animationEditor.getReal(mouseX)));
                  else
                    workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())+2*animationEditor.getReal(animationEditor.columnView.increment)-1));
                }
                else {
                  workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(rightFrontier));
                  animationEditor.mouseMarkXStart = animationEditor.getZoomed(rightFrontier+1)-animationEditor.columnView.increment;
                  animationEditor.mouseMarkXEnd = -1;
                  animationEditor.columnView.repaint();
                }
                  animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                    animationEditor.actorViewers.indexOf(workingActorViewer),
                    0,
                    workingActorViewer.actorViewerRealWidth);
                  animationEditor.mainPanel.repaint(0,
                    animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                    workingActorViewer.actorViewerZoomedWidth,
                    animationEditor.ACTOR_VIEWER_HEIGHT);
              }
              // There isn't another segment to the right.
              else {
                if (!zeroFlagRight) {
                  zeroFlagRight = true;
                  zeroOffsetX = offsetX;
                  actorViewerZeroMaxWidth = animationEditor.actorViewerMaxWidth;
                }
                mouseX -= (offsetX-zeroOffsetX);
                if (mouseX % animationEditor.columnView.increment != 0)
                  mouseX -= mouseX % animationEditor.columnView.increment;
                if (animationEditor.getReal(mouseX) > animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())+2*animationEditor.getReal(animationEditor.columnView.increment)-1)
                  workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(animationEditor.getReal(mouseX)));
                else
                  workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())+2*animationEditor.getReal(animationEditor.columnView.increment)-1));
                animationEditor.mouseMarkXStart = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getEnd()));
                animationEditor.mouseMarkXEnd = -1;
                animationEditor.columnView.repaint();

                if (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd()) > actorViewerZeroMaxWidth)
                  animationEditor.actorViewerMaxWidth = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd());
                else
                  animationEditor.actorViewerMaxWidth = actorViewerZeroMaxWidth;
                if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth)
                  workingActorViewer.actorViewerRealWidth =animationEditor.actorViewerMaxWidth;
                if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                  workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);
                for (int i=0;i<animationEditor.actorViewers.size();i++) {
                  ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                  ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                }

                animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                animationEditor.mainPanel.setPreferredSize(new Dimension(
                  animationEditor.mainPanelZoomedWidth,
                  workingActorViewer.animationEditor.actorViewers.size()*(workingActorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+workingActorViewer.animationEditor.ACTOR_SPACE)));
                animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                animationEditor.mainPanel.revalidate();
                animationEditor.columnView.revalidate();
                r.setRect(animationEditor.getZoomed(animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())),workingActorViewer.actorShape.getY()+offsetY,1,1);
                animationEditor.mainPanel.scrollRectToVisible(r);

                animationEditor.mainPanel.paintScoreImage(
                  animationEditor.actorViewers.indexOf(workingActorViewer),
                  animationEditor.actorViewers.indexOf(workingActorViewer),
                  0,
                  workingActorViewer.actorViewerRealWidth);
                animationEditor.mainPanel.repaint(
                  0,
                  animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                  workingActorViewer.actorViewerZoomedWidth,
                  animationEditor.ACTOR_VIEWER_HEIGHT);
              }
            }
            else {
              workingSegmentViewer.segment.setStart(workingSegmentViewer.segment.getEnd()-1);
              animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
              animationEditor.actorViewers.indexOf(workingActorViewer),
              0,
              workingActorViewer.actorViewerRealWidth);
              animationEditor.mainPanel.repaint(0,
                animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                workingActorViewer.actorViewerZoomedWidth,
                animationEditor.ACTOR_VIEWER_HEIGHT);
            }
          }
          // Create segment moving the mouse to the left.
          else if (mouseX < mousePressedX) {
            // Check if moving to the right, pass the minimum width and continue
            // moving to the right.
            if ((segmentCreation==0) || (segmentCreation==1)) {
              segmentCreation = 1;
              // Check if there is another segment to the left.
              if (workingSegmentViewer.segment.getPrevious() != null) {
                leftFrontier = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getPrevious().getEnd())+1;
                if (animationEditor.getReal(mouseX) >= leftFrontier) {
                  if (animationEditor.getReal(mouseX) < animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-2*animationEditor.getReal(animationEditor.columnView.increment))
                    workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(animationEditor.getReal(mouseX)));
                  else
                    workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-2*animationEditor.getReal(animationEditor.columnView.increment)+1));
                }
                else {
                  workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(leftFrontier));
                  animationEditor.mouseMarkXStart = animationEditor.getZoomed(leftFrontier);
                  animationEditor.mouseMarkXEnd = -1;
                  animationEditor.columnView.repaint();
                }
                if (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd()) > animationEditor.actorViewerMaxWidth)
                  animationEditor.actorViewerMaxWidth = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd());
                  animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                  animationEditor.actorViewers.indexOf(workingActorViewer),
                  0,
                  workingActorViewer.actorViewerRealWidth);
                  animationEditor.mainPanel.repaint(0,
                    animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                    workingActorViewer.actorViewerZoomedWidth,
                    animationEditor.ACTOR_VIEWER_HEIGHT);
              }
              // There isn't another segment to the left.
              else {
                if (animationEditor.getReal(mouseX) < animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-2*animationEditor.getReal(animationEditor.columnView.increment))
                  workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(animationEditor.getReal(mouseX)));
                else
                  workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-2*animationEditor.getReal(animationEditor.columnView.increment)+1));
                if (!zeroFlagLeft) {
                  zeroFlagLeft = true;
//                  actorViewerZeroWidth = workingActorViewer.actorViewerZoomedWidth;
                  actorViewerZeroMaxWidth =animationEditor.actorViewerMaxWidth;
                  for (int i=0;i<animationEditor.actorViewers.size();i++) {
                    for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                      ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentZeroStart
                        = ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.getStart();
                      ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentZeroEnd
                        = ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.getEnd();
                      for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                        ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime =
                          ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getWhen();
                      }
                    }
                  }
                }
                // Mouse gets out of main panel to the left.
                if (e.getX() < 0) {
                  negativeFlag = true;
                  animationEditor.actorViewerMaxWidth = actorViewerZeroMaxWidth + animationEditor.getReal(Math.abs(mouseX));
                  for (int i=0;i<animationEditor.actorViewers.size();i++) {
                    for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                      animationEditor.processModelEvents = false;
                      ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setStart(
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroStart() + animationEditor.columnView.getFrame(animationEditor.getReal(Math.abs(mouseX)))-1);
                      animationEditor.processModelEvents = false;
                      ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setEnd(
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroEnd() + animationEditor.columnView.getFrame(animationEditor.getReal(Math.abs(mouseX)))-1);
                      for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                        if ((((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                          && (((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                            animationEditor.processModelEvents = false;
                            ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                              ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime
                              +animationEditor.columnView.getFrame(animationEditor.getReal(Math.abs(mouseX)))-1);
                        }
                      }
                    }
                  }
                  animationEditor.processModelEvents = false;
                  workingSegmentViewer.segment.setStart(1);
                  animationEditor.mouseMarkXStart = 0;
                  animationEditor.mouseMarkXEnd = -1;
                  animationEditor.columnView.repaint();

                  if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth)
                    workingActorViewer.actorViewerRealWidth =animationEditor.actorViewerMaxWidth;
                  if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                    workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                  workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);

                  for (int i=0;i<animationEditor.actorViewers.size();i++) {
                    ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                    ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                  }
                  animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                  animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                  animationEditor.mainPanel.setPreferredSize(new Dimension(
                    animationEditor.mainPanelZoomedWidth,
                    workingActorViewer.animationEditor.actorViewers.size()*(workingActorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+workingActorViewer.animationEditor.ACTOR_SPACE)));
                  animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                  animationEditor.columnView.revalidate();
                  animationEditor.mainPanel.revalidate();
                  animationEditor.mainPanel.drawBufferedScoreImage();
                  animationEditor.mainPanel.repaint();
                }
                else {
                  if (negativeFlag == true) {
                    for (int i=0;i<animationEditor.actorViewers.size();i++) {
                      for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                        animationEditor.processModelEvents = false;
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setEnd(
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroEnd());
                        if (workingActorViewer != (BaseActorViewer)animationEditor.actorViewers.get(i)) {
                          animationEditor.processModelEvents = false;
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setStart(
                            ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroStart());
                          for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                            if ((((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                              && (((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                                animationEditor.processModelEvents = false;
                                ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                                  ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime);
                            }
                          }
                        }
                      }
                      animationEditor.mainPanel.drawBufferedScoreImage();
                      animationEditor.mainPanel.repaint();
                    }
                  }
                  else {
                    animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                      animationEditor.actorViewers.indexOf(workingActorViewer),
                      0,
                      workingActorViewer.actorViewerRealWidth);
                    animationEditor.mainPanel.repaint(0,
                      animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                      workingActorViewer.actorViewerZoomedWidth,
                      animationEditor.ACTOR_VIEWER_HEIGHT);
                  }
                }
                r.setRect(mouseX,workingActorViewer.actorShape.getY()+offsetY,1,1);
                animationEditor.mainPanel.scrollRectToVisible(r);
              }
            }
            else {
              workingSegmentViewer.segment.setEnd(workingSegmentViewer.segment.getStart()+1);
              animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
              animationEditor.actorViewers.indexOf(workingActorViewer),
              0,
              workingActorViewer.actorViewerRealWidth);
              animationEditor.mainPanel.repaint(0,
                animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                workingActorViewer.actorViewerZoomedWidth,
                animationEditor.ACTOR_VIEWER_HEIGHT);
            }
          }
        }
        // Moving a milestone
        else if ((segmentProcess == false) && (milestoneProcess == true)) {
          animationEditor.processModelEvents = false;
          int realMouseX = animationEditor.getReal(e.getX());
          if (realMouseX % animationEditor.getReal(animationEditor.columnView.increment) != 0)
            realMouseX -= realMouseX % animationEditor.getReal(animationEditor.columnView.increment);
          if ((workingMilestoneViewer.milestone.getPrevious() != null) && (workingMilestoneViewer.milestone.getNext() != null))
          if ((animationEditor.columnView.getFrame(realMouseX) > workingMilestoneViewer.milestone.getPrevious().getWhen())
            && (animationEditor.columnView.getFrame(realMouseX) < workingMilestoneViewer.milestone.getNext().getWhen()))
            {
              // Recompute the segment's current milestone.
// Base change
              if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                ((Segment)workingMilestoneViewer.milestone.getSegment()).currentMilestone = null;

              workingMilestoneViewer.milestone.setWhen(animationEditor.columnView.getFrame(realMouseX));
              animationEditor.mouseMarkXStart = animationEditor.getZoomed(realMouseX);
              animationEditor.mouseMarkXEnd = -1;
              animationEditor.columnView.repaint();
              animationEditor.mainPanel.paintScoreImage(
                animationEditor.actorViewers.indexOf(workingActorViewer),
                animationEditor.actorViewers.indexOf(workingActorViewer),
                0,
                workingActorViewer.actorViewerRealWidth);
              animationEditor.mainPanel.repaint(
                0,
                animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                workingActorViewer.actorViewerZoomedWidth,
                animationEditor.ACTOR_VIEWER_HEIGHT);
            }
        }
        // Process a segment.
        else if ((segmentProcess == true) && (milestoneProcess == false)) {
          int realMouseX = animationEditor.getReal(e.getX());
          switch (whereInSegment) {
            // Resize a segment to left.
            case 1:
              // Recompute the segment's current milestone.
// Base change
              if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                ((Segment)workingSegmentViewer.segment).currentMilestone = null;

              animationEditor.processModelEvents = false;
              if (realMouseX % animationEditor.getReal(animationEditor.columnView.increment) != 0)
                realMouseX -= realMouseX % animationEditor.getReal(animationEditor.columnView.increment);
              animationEditor.mouseMarkXStart = animationEditor.getZoomed(realMouseX);
              animationEditor.mouseMarkXEnd = -1;
              animationEditor.columnView.repaint();
              // There is another segment to the left.
              if (workingSegmentViewer.segment.getPrevious() != null) {
                  leftFrontier = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getPrevious().getEnd())+1;
                  if (realMouseX >= leftFrontier) {
                    if (realMouseX < animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-2*animationEditor.getReal(animationEditor.columnView.increment))
                      workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(realMouseX));
                    else
                      workingSegmentViewer.segment.setStart(workingSegmentViewer.segment.getEnd()-1);
                  }
                  else {
                    workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(leftFrontier));
                    animationEditor.mouseMarkXStart = animationEditor.getZoomed(leftFrontier);
                    animationEditor.mouseMarkXEnd = -1;
                    animationEditor.columnView.repaint();
                  }
                  // Expand or shrink milestones.
                  if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                  for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                    if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null) && (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                      int milestoneTime =
                        (animationEditor.columnView.getFrameStartTime(((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime)
                        -segmentOldX)
                        * (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart()))
                        /segmentOldWidth
                        + animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart());
                      milestoneTime = animationEditor.columnView.getFrame(milestoneTime);
                      animationEditor.processModelEvents = false;
                      ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(milestoneTime);
                      if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious().getWhen())
                        || (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext().getWhen())) {
                          ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).delete = true;
                          releaseFlag = true;
                      }
                    }
                  }
                  animationEditor.mainPanel.paintScoreImage(
                    animationEditor.actorViewers.indexOf(workingActorViewer),
                    animationEditor.actorViewers.indexOf(workingActorViewer),
                    0,
                    workingActorViewer.actorViewerRealWidth);
                  animationEditor.mainPanel.repaint(0,
                    animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                    workingActorViewer.actorViewerZoomedWidth,
                    animationEditor.ACTOR_VIEWER_HEIGHT);
                }
                // There isn't another segment to the left.
                else {
                  if (realMouseX < animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-2*animationEditor.getReal(animationEditor.columnView.increment))
                    workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(realMouseX));
                  else
                    workingSegmentViewer.segment.setStart(workingSegmentViewer.segment.getEnd()-1);
                  if (!zeroFlagLeft) {
                    zeroFlagLeft = true;
//                    actorViewerZeroWidth = workingActorViewer.actorViewerZoomedWidth;
                    actorViewerZeroMaxWidth =animationEditor.actorViewerMaxWidth;
                    for (int i=0;i<animationEditor.actorViewers.size();i++) {
                      for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentZeroStart
                          = ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.getStart();
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentZeroEnd
                          = ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.getEnd();
                        for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                          ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime =
                            ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getWhen();
                        }
                      }
                    }
                  }
                  // Mouse gets out of screen to the left.
                  if (e.getX() < 0) {
                    negativeFlag = true;
                    animationEditor.actorViewerMaxWidth = actorViewerZeroMaxWidth + animationEditor.getReal(Math.abs(
                    e.getX()%animationEditor.columnView.increment!=0
                    ? e.getX()-e.getX()%animationEditor.columnView.increment
                    : e.getX()));
                    for (int i=0;i<animationEditor.actorViewers.size();i++) {
                      for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                        animationEditor.processModelEvents = false;
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setStart(
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroStart() + animationEditor.columnView.getFrame(animationEditor.getReal(Math.abs(e.getX())))-1);
                        animationEditor.processModelEvents = false;
                        ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setEnd(
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroEnd() + animationEditor.columnView.getFrame(animationEditor.getReal(Math.abs(e.getX())))-1);
                        for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                          if ((((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                            && (((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                              animationEditor.processModelEvents = false;
                              ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                                ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime
                                +animationEditor.columnView.getFrame(animationEditor.getReal(Math.abs(e.getX())))-1);
                          }
                        }
                      }
                    }
                    animationEditor.processModelEvents = false;
                    workingSegmentViewer.segment.setStart(1);
                    animationEditor.mouseMarkXStart = 0;
                    animationEditor.mouseMarkXEnd = -1;
                    animationEditor.columnView.repaint();

                    // Expand or shrink milestones.
                    if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                    for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                      if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null) && (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                        int milestoneTime =
                          (animationEditor.columnView.getFrameStartTime(((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime)
                          -segmentOldX)
                          * (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart()))
                          /segmentOldWidth
                          + animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart());
                        milestoneTime = animationEditor.columnView.getFrame(milestoneTime);
                        animationEditor.processModelEvents = false;
                        ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(milestoneTime);
                        if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious().getWhen())
                          || (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext().getWhen())) {
                            ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).delete = true;
                            releaseFlag = true;
                        }
                      }
                    }

                    if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth)
                      workingActorViewer.actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
                    if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                      workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                    workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);
                    for (int i=0;i<animationEditor.actorViewers.size();i++) {
                      ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                      ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                    }
                    animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                    animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                    animationEditor.mainPanel.setPreferredSize(new Dimension(
                      animationEditor.mainPanelZoomedWidth,
                      workingActorViewer.animationEditor.actorViewers.size()*(workingActorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+workingActorViewer.animationEditor.ACTOR_SPACE)));
                    animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                    animationEditor.columnView.revalidate();
                    animationEditor.mainPanel.revalidate();
                    animationEditor.mainPanel.drawBufferedScoreImage();
                    animationEditor.mainPanel.repaint();
                  }
                  else {
                    // Expand or shrink milestones.
                    if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                    for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                      if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                        && (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                          int milestoneTime =
                            (animationEditor.columnView.getFrameStartTime(((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime)
                            -segmentOldX)
                            * (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart()))
                            /segmentOldWidth
                            + animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart());
                          milestoneTime = animationEditor.columnView.getFrame(milestoneTime);
                          animationEditor.processModelEvents = false;
                          ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(milestoneTime);
                          if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious().getWhen())
                            || (((MilestoneViewer)workingSegmentViewer .milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext().getWhen())) {
                              ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).delete = true;
                              releaseFlag = true;
                          }
                      }
                    }
                    if (negativeFlag == true) {
                     for (int i=0;i<animationEditor.actorViewers.size();i++) {
                        for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                          animationEditor.processModelEvents = false;
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setEnd(
                            ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroEnd());
                          if (workingActorViewer != (BaseActorViewer)animationEditor.actorViewers.get(i)) {
                            animationEditor.processModelEvents = false;
                            ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setStart(
                              ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroStart());
                            for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                              if ((((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                                && (((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                                  animationEditor.processModelEvents = false;
                                  ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                                    ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime);
                              }
                            }
                          }
                        }
                      }
                      animationEditor.mainPanel.drawBufferedScoreImage();
                      animationEditor.mainPanel.repaint();
                    }
                    else {
                      animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                        animationEditor.actorViewers.indexOf(workingActorViewer),
                        0,
                        workingActorViewer.actorViewerRealWidth);
                      animationEditor.mainPanel.repaint(0,
                        animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                        workingActorViewer.actorViewerZoomedWidth,
                        animationEditor.ACTOR_VIEWER_HEIGHT);
                    }
                  }
                  r.setRect(animationEditor.getZoomed(realMouseX),workingActorViewer.actorShape.getY()+offsetY,1,1);
                  animationEditor.mainPanel.scrollRectToVisible(r);
                }
              break;
            // Move a segment.
            case 2:
              // Recompute the segment's current milestone.
// Base change
              if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                ((Segment)workingSegmentViewer.segment).currentMilestone = null;

              int xPos;
              xPos = segmentOldX + animationEditor.getReal(e.getX() - mouseOldX);
              if (xPos % animationEditor.getReal(animationEditor.columnView.increment) != 0)
                xPos -= xPos % animationEditor.getReal(animationEditor.columnView.increment);
              animationEditor.mouseMarkXStart = animationEditor.getZoomed(xPos);
              animationEditor.mouseMarkXEnd = animationEditor.getZoomed(xPos+segmentOldWidth+1)-animationEditor.columnView.increment;
              animationEditor.columnView.repaint();
              // Move a segment to the left.
              if (xPos < segmentOldX) {
                  // There is another segment to the left.
                  if (workingSegmentViewer.segment.getPrevious() != null) {
                    leftFrontier = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getPrevious().getEnd())+1;
                    if (xPos >= leftFrontier) {
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(xPos));
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(xPos + segmentOldWidth));
                    }
                    else {
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(leftFrontier));
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(leftFrontier+segmentOldWidth));
                      animationEditor.mouseMarkXStart = animationEditor.getZoomed(leftFrontier);
                      animationEditor.mouseMarkXEnd = animationEditor.getZoomed(leftFrontier+segmentOldWidth+1)-animationEditor.columnView.increment;
                      animationEditor.columnView.repaint();
                    }
                    for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                      if ((((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                        && (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                          animationEditor.processModelEvents = false;
                          ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(
                            ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime
                            + workingSegmentViewer.segment.getStart() - animationEditor.columnView.getFrame(segmentOldX));
                      }
                    }

                    animationEditor.computeActorViewerMaxWidth();
                    if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth) {
                      workingActorViewer.actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
                    }
                    if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                      workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                    workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);
                    for (int i=0;i<animationEditor.actorViewers.size();i++) {
                      ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                      ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                    }
                    animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                    animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                    animationEditor.mainPanel.setPreferredSize(new Dimension(animationEditor.mainPanelZoomedWidth, animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
                    animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                    animationEditor.columnView.revalidate();
                    animationEditor.mainPanel.revalidate();
//                    r.setRect(e.getX(), workingActorViewer.actorShape.getY()+offsetY, 1, 1);
                    r.setRect(animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())),workingActorViewer.actorShape.getY()+offsetY,1,1);
                    animationEditor.mainPanel.scrollRectToVisible(r);

                    animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                      animationEditor.actorViewers.indexOf(workingActorViewer),
                      0,
                      workingActorViewer.actorViewerRealWidth);
                    animationEditor.mainPanel.repaint(0,
                      animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                      workingActorViewer.actorViewerZoomedWidth,
                      animationEditor.ACTOR_VIEWER_HEIGHT);
                  }
                  // There isn't another segment to the left.
                  else {
                    animationEditor.processModelEvents = false;
                    workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(xPos));
                    animationEditor.processModelEvents = false;
                    workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(xPos + segmentOldWidth));
                    if (!zeroFlagLeft) {
                      zeroFlagLeft = true;
//                      actorViewerZeroWidth = workingActorViewer.actorViewerZoomedWidth;
                      actorViewerZeroMaxWidth =animationEditor.actorViewerMaxWidth;
                      for (int i=0;i<animationEditor.actorViewers.size();i++) {
                        for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentZeroStart
                            = ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.getStart();
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentZeroEnd
                            = ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.getEnd();
                          for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                            ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime =
                              ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getWhen();
                          }
                        }
                      }
                    }
                    // Left side of segment gets out of main panel to the left.
                    if (xPos-animationEditor.getReal(animationEditor.columnView.increment) < 0) {
                      negativeFlag = true;
                      animationEditor.actorViewerMaxWidth = actorViewerZeroMaxWidth + Math.abs(xPos);
                      for (int i=0;i<animationEditor.actorViewers.size();i++) {
                        for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                          animationEditor.processModelEvents = false;
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setStart(
                            ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroStart() + animationEditor.columnView.getFrame(Math.abs(xPos))-1);
                          animationEditor.processModelEvents = false;
                          ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setEnd(
                            ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroEnd() + animationEditor.columnView.getFrame(Math.abs(xPos))-1);
                          for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                            if ((((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                              && (((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                                animationEditor.processModelEvents = false;
                                ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                                  ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime
                                  +animationEditor.columnView.getFrame(Math.abs(xPos))-1);
                            }
                          }
                        }
                      }
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setStart(1);
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(segmentOldWidth));
                      animationEditor.mouseMarkXStart = animationEditor.getZoomed(0);
                      animationEditor.mouseMarkXEnd = animationEditor.getZoomed(segmentOldWidth+1)-animationEditor.columnView.increment;
                      animationEditor.columnView.repaint();

                      animationEditor.computeActorViewerMaxWidth();

                      if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth)
                        workingActorViewer.actorViewerRealWidth =animationEditor.actorViewerMaxWidth;
                      if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                        workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                      workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);
                      for (int i=0;i<animationEditor.actorViewers.size();i++) {
                        ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                        ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                      }
                      animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                      animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                      animationEditor.mainPanel.setPreferredSize(new Dimension(
                         animationEditor.mainPanelZoomedWidth,
                         workingActorViewer.animationEditor.actorViewers.size()*(workingActorViewer.animationEditor.ACTOR_VIEWER_HEIGHT+workingActorViewer.animationEditor.ACTOR_SPACE)));
                      animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                      animationEditor.columnView.revalidate();
                      animationEditor.mainPanel.revalidate();
                      for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                        if ((((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                          && (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                          animationEditor.processModelEvents = false;
                          ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(
                           ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime + workingSegmentViewer.segment.getStart() - segmentOldX);
                        }
                      }
                      animationEditor.mainPanel.drawBufferedScoreImage();
                      animationEditor.mainPanel.repaint();
                    }
                    else {
                      for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                        if ((((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                          && (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                          animationEditor.processModelEvents = false;
                          ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(
                           ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime
                           + workingSegmentViewer.segment.getStart() - animationEditor.columnView.getFrame(segmentOldX));
                        }
                      }

                      if (negativeFlag == true) {
                        for (int i=0;i<animationEditor.actorViewers.size();i++) {
                          for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
                            if (workingActorViewer != (BaseActorViewer)animationEditor.actorViewers.get(i)) {
                              animationEditor.processModelEvents = false;
                              ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setStart(
                                ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroStart());
                              animationEditor.processModelEvents = false;
                              ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment.setEnd(
                                ((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).getSegmentZeroEnd());
                              for (int k=0;k<((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                                if ((((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                                  && (((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                                    animationEditor.processModelEvents = false;
                                    ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                                      ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime);
                                }
                              }
                            }
                          }
                        }
                      }

                      animationEditor.computeActorViewerMaxWidth();
                      if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth) {
                        workingActorViewer.actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
                      }
                      if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                        workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                      workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);
                      for (int i=0;i<animationEditor.actorViewers.size();i++) {
                        ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                        ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                      }
                      animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                      animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                      animationEditor.mainPanel.setPreferredSize(new Dimension(animationEditor.mainPanelZoomedWidth, animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
                      animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                      animationEditor.columnView.revalidate();
                      animationEditor.mainPanel.revalidate();

                      if (negativeFlag == true) {
                        animationEditor.mainPanel.drawBufferedScoreImage();
                        animationEditor.mainPanel.repaint();
                      }
                      else {
                        animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                          animationEditor.actorViewers.indexOf(workingActorViewer),
                          0,
                          workingActorViewer.actorViewerRealWidth);
                        animationEditor.mainPanel.repaint(0,
                          animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                          workingActorViewer.actorViewerZoomedWidth,
                          animationEditor.ACTOR_VIEWER_HEIGHT);
                      }
                    }
//                    r.setRect(e.getX(),workingActorViewer.actorShape.getY()+offsetY,1,1);
                    r.setRect(animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())),workingActorViewer.actorShape.getY()+offsetY,1,1);
                    animationEditor.mainPanel.scrollRectToVisible(r);
                  }
                }
                // Move a segment to the right.
                else if (xPos > segmentOldX) {
                  // There is another segment to the right.
                  if (workingSegmentViewer.segment.getNext() != null) {
                    rightFrontier = animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getNext().getStart())-1;
                    if (xPos + segmentOldWidth <= rightFrontier) {
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(xPos));
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(xPos + segmentOldWidth));
                    }
                    else {
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(rightFrontier-segmentOldWidth));
                      animationEditor.processModelEvents = false;
                      workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(rightFrontier));
                      animationEditor.mouseMarkXStart = animationEditor.getZoomed(rightFrontier-segmentOldWidth);
                      animationEditor.mouseMarkXEnd = animationEditor.getZoomed(rightFrontier+1)-animationEditor.columnView.increment;
                      animationEditor.columnView.repaint();
                    }
                    for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                      if ((((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                        && (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                          animationEditor.processModelEvents = false;
                          ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(
                           ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime
                           + workingSegmentViewer.segment.getStart() - animationEditor.columnView.getFrame(segmentOldX));
                      }
                    }
                    animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                      animationEditor.actorViewers.indexOf(workingActorViewer),
                      0,
                      workingActorViewer.actorViewerRealWidth);
                    animationEditor.mainPanel.repaint(0,
                      animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                      workingActorViewer.actorViewerZoomedWidth,
                      animationEditor.ACTOR_VIEWER_HEIGHT);
                  }
                  // There isn't another segment to the right.
                  else {
                    if (!zeroFlagRight) {
                      zeroFlagRight = true;
                      zeroOffsetX = offsetX;
                      actorViewerZeroMaxWidth = animationEditor.actorViewerMaxWidth;
                    }
                    xPos -= animationEditor.getReal(offsetX-zeroOffsetX);
                    if (xPos % animationEditor.getReal(animationEditor.columnView.increment) != 0)
                      xPos -= xPos % animationEditor.getReal(animationEditor.columnView.increment);

                    animationEditor.processModelEvents = false;
                    workingSegmentViewer.segment.setStart(animationEditor.columnView.getFrame(xPos));
                    animationEditor.processModelEvents = false;
                    workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(xPos + segmentOldWidth));
                    for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                      if ((((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null)
                        && (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                          animationEditor.processModelEvents = false;
                          ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(
                           ((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime
                           + workingSegmentViewer.segment.getStart() - animationEditor.columnView.getFrame(segmentOldX));
                      }
                    }

                    if (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd()) > actorViewerZeroMaxWidth)
                     animationEditor.actorViewerMaxWidth = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd());
                    else
                     animationEditor.actorViewerMaxWidth = actorViewerZeroMaxWidth;
                    if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth)
                      workingActorViewer.actorViewerRealWidth =animationEditor.actorViewerMaxWidth;
                    if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                      workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                    workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);

                    for (int i=0;i<animationEditor.actorViewers.size();i++) {
                      ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                      ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                    }
                    animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                    animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                    animationEditor.mainPanel.setPreferredSize(new Dimension(animationEditor.mainPanelZoomedWidth, animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
                    animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                    animationEditor.columnView.revalidate();
                    animationEditor.mainPanel.revalidate();
                    r.setRect(animationEditor.getZoomed(animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())),workingActorViewer.actorShape.getY()+offsetY,1,1);
                    animationEditor.mainPanel.scrollRectToVisible(r);
                    animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                      animationEditor.actorViewers.indexOf(workingActorViewer),
                      0,
                      workingActorViewer.actorViewerRealWidth);
                    animationEditor.mainPanel.repaint(0,
                      animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                      workingActorViewer.actorViewerZoomedWidth,
                      animationEditor.ACTOR_VIEWER_HEIGHT);
                  }
                }
              break;
            // Resise a segment to the right.
            case 3:
              // Recompute the segment's current milestone.
// Base change
              if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                ((Segment)workingSegmentViewer.segment).currentMilestone = null;

              animationEditor.processModelEvents = false;
              if (realMouseX % animationEditor.getReal(animationEditor.columnView.increment) != 0)
                realMouseX -= realMouseX % animationEditor.getReal(animationEditor.columnView.increment);
              animationEditor.mouseMarkXStart = animationEditor.getZoomed(realMouseX);
              animationEditor.mouseMarkXEnd = -1;
              animationEditor.columnView.repaint();
              // There is another segment to the right.
              if (workingSegmentViewer.segment.getNext() != null) {
                  rightFrontier = animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getNext().getStart())-1;
                  if (realMouseX <= rightFrontier) {
                    if (realMouseX > animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())+2*animationEditor.getReal(animationEditor.columnView.increment)-1)
                      workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(realMouseX));
                    else
                      workingSegmentViewer.segment.setEnd(workingSegmentViewer.segment.getStart()+1);
                  }
                  else {
                    workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(rightFrontier));
                    animationEditor.mouseMarkXStart = animationEditor.getZoomed(rightFrontier+1)-animationEditor.columnView.increment;
                    animationEditor.mouseMarkXEnd = -1;
                    animationEditor.columnView.repaint();
                  }
                  // Expand or shrink milestones.
                  if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                  for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                    if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null) && (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                      int milestoneTime = (animationEditor.columnView.getFrameStartTime(((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime)
                        -segmentOldX)
                        * (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart()))
                        /segmentOldWidth
                        + animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart());
                      milestoneTime = animationEditor.columnView.getFrame(milestoneTime);
                      animationEditor.processModelEvents = false;
                      ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(milestoneTime);
                      if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious().getWhen())
                        || (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext().getWhen())) {
                          ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).delete = true;
                          releaseFlag = true;
                      }
                    }
                  }
                  animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                    animationEditor.actorViewers.indexOf(workingActorViewer),
                    0,
                    workingActorViewer.actorViewerRealWidth);
                  animationEditor.mainPanel.repaint(0,
                    animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                    workingActorViewer.actorViewerZoomedWidth,
                    animationEditor.ACTOR_VIEWER_HEIGHT);
                }
                // There isn't another segment to the right.
                else {
                  if (!zeroFlagRight) {
                    zeroFlagRight = true;
                    zeroOffsetX = offsetX;
                    actorViewerZeroMaxWidth = animationEditor.actorViewerMaxWidth;
                  }
                  realMouseX -= animationEditor.getReal(offsetX-zeroOffsetX);
                  if (realMouseX % animationEditor.getReal(animationEditor.columnView.increment) != 0)
                    realMouseX -= realMouseX % animationEditor.getReal(animationEditor.columnView.increment);

                  if (realMouseX > animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart())+2*animationEditor.getReal(animationEditor.columnView.increment)-1)
                    workingSegmentViewer.segment.setEnd(animationEditor.columnView.getFrame(realMouseX));
                  else
                    workingSegmentViewer.segment.setEnd(workingSegmentViewer.segment.getStart()+1);
                  animationEditor.mouseMarkXStart = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getEnd()));
                  animationEditor.mouseMarkXEnd = -1;
                  animationEditor.columnView.repaint();

                  // Expand or shrink milestones.
                  if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
                  for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {
                    if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious()!=null) && (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext()!=null)) {
                      int milestoneTime = (animationEditor.columnView.getFrameStartTime(((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).zeroTime)
                        -segmentOldX)
                        * (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())-animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart()))
                        /segmentOldWidth
                        + animationEditor.columnView.getFrameStartTime(workingSegmentViewer.segment.getStart());
                      milestoneTime = animationEditor.columnView.getFrame(milestoneTime);
                      animationEditor.processModelEvents = false;
                      ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.setWhen(milestoneTime);
                      if ((((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getPrevious().getWhen())
                        || (((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getWhen() == ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestone.getNext().getWhen())) {
                          ((MilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).delete = true;
                          releaseFlag = true;
                      }
                    }
                  }
                  animationEditor.computeActorViewerMaxWidth();
/*                  if (animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd()) > actorViewerZeroMaxWidth)
                    animationEditor.actorViewerMaxWidth = animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd());
                  else
                    animationEditor.actorViewerMaxWidth = actorViewerZeroMaxWidth;*/
                  if (animationEditor.actorViewerMaxWidth != workingActorViewer.actorViewerRealWidth)
                    workingActorViewer.actorViewerRealWidth =animationEditor.actorViewerMaxWidth;
                  if (workingActorViewer.actorViewerRealWidth < animationEditor.getReal(width))
                    workingActorViewer.actorViewerRealWidth = animationEditor.getReal(width);
                  workingActorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(workingActorViewer.actorViewerRealWidth);

                  for (int i=0;i<animationEditor.actorViewers.size();i++) {
                    ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = workingActorViewer.actorViewerRealWidth;
                    ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                  }
                  animationEditor.mainPanelZoomedWidth = workingActorViewer.actorViewerZoomedWidth;
                  animationEditor.mainPanelRealWidth = workingActorViewer.actorViewerRealWidth;
                  animationEditor.mainPanel.setPreferredSize(new Dimension(animationEditor.mainPanelZoomedWidth, animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
                  animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
                  animationEditor.columnView.revalidate();
                  animationEditor.mainPanel.revalidate();
                  r.setRect(animationEditor.getZoomed(animationEditor.columnView.getFrameEndTime(workingSegmentViewer.segment.getEnd())), workingActorViewer.actorShape.getY()+offsetY, 1, 1);
                  animationEditor.mainPanel.scrollRectToVisible(r);
                  animationEditor.mainPanel.paintScoreImage(animationEditor.actorViewers.indexOf(workingActorViewer),
                   animationEditor.actorViewers.indexOf(workingActorViewer),
                    0,
                    workingActorViewer.actorViewerRealWidth);
                  animationEditor.mainPanel.repaint(0,
                    animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                    workingActorViewer.actorViewerZoomedWidth,
                    animationEditor.ACTOR_VIEWER_HEIGHT);
                }
              break;
          }
        }
      }
    }
  }

  /**
   * Checks if the mouse is clicked.
   * @param e
   */
  public void mouseClicked(MouseEvent e) {
  // Check if play thread is active.
  if (!animationEditor.animation.isPlayThreadActive())
    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
      int virtualMouseX = e.getX()-animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
      int virtualMouseY = e.getY()-animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
      for (int i=1;i<animationEditor.actorViewers.size();i++) {
        if (((BaseActorViewer)animationEditor.actorViewers.get(i)).actorShape.contains(virtualMouseX,virtualMouseY)) {
          for (int j=0;j<((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size();j++) {
            if (((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segmentShape.contains(virtualMouseX,virtualMouseY)) {
              for (int k=0;k<((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.size();k++) {
                int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
                int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
                int milestoneTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(((MilestoneViewer)((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getWhen()));
                if (((MilestoneViewer)((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestoneShape.contains(virtualMouseX,virtualMouseY)) {
                  if (e.getClickCount() == 2) {
// Base change
                    try {
						MilestoneDialog milestoneDialog = new MilestoneDialog((Frame)SwingUtilities.getAncestorOfClass(Frame.class,animationEditor), (Milestone)workingMilestoneViewer.milestone, animationEditor);
						milestoneDialog.setLocationRelativeTo(animationEditor);
						milestoneDialog.show();
					} catch (RuntimeException e1) {}
                    // Recompute the segment's variables change step.
// Base change
                    ((Segment)((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).segment).betweenMilestones = false;
                  }
                  else if (e.getClickCount() == 1) {
/*                    tempMilestoneViewer = null;
                    if (animationEditor.editMilestoneViewer != null) {
                      tempMilestoneViewer = animationEditor.editMilestoneViewer;
                      animationEditor.exitMilestoneEditMode();
                    }*/
                    if ((tempMilestoneViewer == null)
                      || ((tempMilestoneViewer != null) && (tempMilestoneViewer != (MilestoneViewer)((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)))) {
                        animationEditor.editMilestoneViewer = (MilestoneViewer)((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k);
                        animationEditor.animation.setCursorTime(animationEditor.editMilestoneViewer.milestone.getWhen());
                        animationEditor.mainPanel.paintTempMilestone((MilestoneViewer)((SegmentViewer)((ActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k));
                        animationEditor.mainPanel.repaint(0,
                          animationEditor.actorViewers.indexOf((ActorViewer)animationEditor.actorViewers.get(i))*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
                          ((ActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth,
                          animationEditor.ACTOR_VIEWER_HEIGHT);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
