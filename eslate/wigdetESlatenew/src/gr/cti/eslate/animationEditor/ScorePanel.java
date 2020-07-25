package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.Milestone;
import gr.cti.eslate.animation.SoundSegment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * This class implements the score panel.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
public class ScorePanel extends JPanel {
  AnimationEditor animationEditor;

  /**
   * Create a score panel.
   * @param aniEditor  The animation viewer of the score panel.
   */
  public ScorePanel(AnimationEditor aniEditor) {
    this.animationEditor = aniEditor;
    addMouseMotionListener(new MouseMotionAdapter(){
      public void mouseMoved(MouseEvent e) {
        int virtualMouseX = e.getX()-animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
        int virtualMouseY = e.getY()-animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
        String toolTipText = null;
        BaseActorViewer tempActorViewer;
        BaseSegmentViewer tempSegmentViewer;
        for (int i=0;i<animationEditor.actorViewers.size();i++) {
          tempActorViewer = (BaseActorViewer)animationEditor.actorViewers.get(i);
          if (i==0) {
            if (tempActorViewer.actorShape.contains(virtualMouseX,virtualMouseY)) {
              toolTipText = "<html><font face=Helvetica size=2>";
              int cursorFlag = 0;
              for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
                tempSegmentViewer = (BaseSegmentViewer)tempActorViewer.segmentViewers.get(j);
                if (tempSegmentViewer.segmentShape.contains(virtualMouseX,virtualMouseY)) {
                  cursorFlag = 1;
                  if (((SoundSegment)tempSegmentViewer.segment).getSoundFile() != null)
                    toolTipText += ((SoundSegment)tempSegmentViewer.segment).getSoundFile().getName();
                  else {
                    toolTipText = null;
                    break;
                  }
                    toolTipText += "</font>";
                  break;
                }
              }
              if (cursorFlag==0) {
                for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
                  if (j>0)
                    toolTipText += "<P>";
                  if (((SoundSegment)((BaseSegmentViewer)tempActorViewer.segmentViewers.get(j)).segment).getSoundFile() != null)
                    toolTipText += ((SoundSegment)((BaseSegmentViewer)tempActorViewer.segmentViewers.get(j)).segment).getSoundFile().getName();
                  else
                    toolTipText += "<P>";
                  if (j==((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.size()-1)
                    toolTipText += "</font>";
                }
              }
              if (tempActorViewer.segmentViewers.size()==0)
                toolTipText = null;
              break;
            }
          }
          else {
            Actor tempActor = (Actor)tempActorViewer.actor;
            if (tempActorViewer.actorShape.contains(virtualMouseX,virtualMouseY)) {
              int cursorFlag=0;
              for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
                tempSegmentViewer = (BaseSegmentViewer)tempActorViewer.segmentViewers.get(j);
                if (tempSegmentViewer.segmentShape.contains(virtualMouseX,virtualMouseY)) {
                  for (int k=0;k<tempSegmentViewer.milestoneViewers.size();k++) {
                    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
                    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
                    int milestoneTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(((MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k)).milestone.getWhen()));
                    if (((MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k)).milestoneShape.contains(virtualMouseX,virtualMouseY)) {
                      cursorFlag = 1;
                      for (int l=0;l<tempActor.getAniVarCount();l++) {
                        if (l==0)
                          toolTipText = "<html><font face=Helvetica size=2>";
                        else if (l>0)
                          toolTipText += "<P>";
                        toolTipText += "<B>" + tempActor.getActorInterface().getAnimatedPropertyStructure().getPropertyName(
                         tempActor.getAniPropertyIDs().get(l));
                        toolTipText += "</B> = ";
// Base change
                        toolTipText += ((Milestone)((MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k)).milestone).getAniVarValues().get(l);
                        if (l==tempActor.getAniVarCount()-1)
                          toolTipText += "</font>";
                      }
                      break;
                    }
                  }
                  break;
                }
              }
              if (cursorFlag==0) {
                toolTipText =
                  "<html><font face=Helvetica size=2>"
                  +"<B>"
                  +tempActor.getActorInterface().getActorName()
                  +" "
                  +tempActor.getAnimationSession().getPlugID()
                  +"</B>";
                for (int j=0;j<tempActor.getAniVarCount();j++) {
                  toolTipText += "<P>";
                  toolTipText += tempActor.getActorInterface().getAnimatedPropertyStructure().getPropertyName(
                   tempActor.getAniPropertyIDs().get(j));
                  if (j==tempActor.getAniVarCount()-1)
                    toolTipText += "</font>";
                }
              }
              break;
            }
          }
        }
        setToolTipText(toolTipText);
      }
    });
  }

  /**
   * Custom paintComponent.
   * @param g The graphics class.
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    super.paintComponent(g2);

    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;

    // Draw buffered score image.
    if (animationEditor.scoreImage != null)
      g2.drawImage(animationEditor.scoreImage, offsetX, offsetY, this);

    //  Place frame cursor on main panel.
    if (animationEditor.animation != null) {
      int cursorTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(animationEditor.animation.getCursorTime()));
      if (cursorTime % animationEditor.columnView.increment != 0)
        cursorTime -= cursorTime % animationEditor.columnView.increment;
      animationEditor.timeCursorMainPanel.setBounds(
        cursorTime+animationEditor.columnView.increment/2,
        offsetY,
        1,
        animationEditor.getSize().height-animationEditor.scrollPaneY-animationEditor.columnView.SIZE);
    }
    
    g2.setColor(Color.black);
    g2.drawLine(0, 0, 0, getSize().height);
  }

  /**
   * Draw whole buffered score image, extending 3 widths & 3 heights of the
   * view window.
   */
  public void drawBufferedScoreImage() {
    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    int offsetWidth = offsetX + animationEditor.scoreScrollPane.getViewport().getViewRect().width;
    int offsetHeight = offsetY + animationEditor.scoreScrollPane.getViewport().getViewRect().height;

    int allWidth = animationEditor.getZoomed(animationEditor.actorViewerMaxWidth);
    int allHeight = animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE);
    int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
    int height = animationEditor.getSize().height-animationEditor.scrollPaneY-animationEditor.columnView.SIZE;
    int startActor=0;
    int endActor=0;
    int startTime=0;
    int endTime=0;
    int startActorTime=0;
    int endActorTime=0;

    // Compute start & end actor for drawing the actor viewers, segment
    // viewers & milestone viewers shapes.
    // 3 times the view height is bigger than all actor's height.
    if (3*height >= allHeight) {
      startActor = 0;
      endActor = animationEditor.actorViewers.size();
    }
    else {
      // Upper virtual height is smaller than view height.
      if (offsetY <= height) {
        startActor = 0;
        endActor = 3*height/(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE);
//System.out.println("Smaller Top: "+startActor+","+endActor);
      }
      // Bottom virtual height is smaller than view height.
      else if (allHeight-offsetHeight <= height) {
        startActor = animationEditor.actorViewers.size() - 3*height/(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE);
        endActor = animationEditor.actorViewers.size();
//System.out.println("Smaller Bottom: "+startActor+","+endActor);
      }
      // Upper & bottom virtual heights are bigger than view height.
      else {
        startActor = (offsetY-height)/(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE);
        endActor = startActor + 3*height/(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE);
//System.out.println("Larger Top-Bottom: "+startActor+","+endActor);
      }
    }

    // Compute start & end time for drawing the segment viewers & milestone
    // viewers shapes.
    // 3 times the view width is bigger than actors's width.
    if (3*width >= allWidth) {
      startTime = 0;
      endTime = animationEditor.actorViewerMaxWidth;
    }
    else {
      // Left virtual width is smaller than view width.
      if (offsetX <= width) {
        startTime = 0;
        endTime = animationEditor.getReal(3*width);
//System.out.println("Smaller Left: "+startTime+","+endTime);
      }
      // Right virtual width is smaller than view width.
      else if (allWidth-offsetWidth <= width) {
        startTime = animationEditor.actorViewerMaxWidth - animationEditor.getReal(3*width);
        endTime = animationEditor.actorViewerMaxWidth;
//System.out.println("Smaller Right: "+startTime+","+endTime);
      }
      // Left & right virtual widths are bigger than view width.
      else {
        startTime = animationEditor.getReal(offsetX-width);
        endTime = startTime + animationEditor.getReal(3*width);
//System.out.println("Larger Left-Right: "+startTime+","+endTime);
      }
    }
    paintScoreImage(startActor, endActor-1, startTime, endTime);
  }

  /**
   * Paint buffered score image within limits.
   * @param startActor  The start actor to start paint buffered score image from.
   * @param endActor    The end actor to stop paint buffered score image at.
   * @param startTime   The start time to start paint buffered score image from.
   * @param endTime     The end time to stop paint buffered score image at.
   */
  public void paintScoreImage(int startActor, int endActor, int startTime, int endTime) {
//  Thread.currentThread().dumpStack();
    startTime = animationEditor.columnView.getFrame(startTime);
    endTime = animationEditor.columnView.getFrame(endTime);

    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    int offsetWidth = offsetX + animationEditor.scoreScrollPane.getViewport().getViewRect().width;

    int allWidth = animationEditor.getZoomed(animationEditor.actorViewerMaxWidth);
    int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
    int startActorTime=0;
    int endActorTime=0;

    // Compute start & end actor time for drawing the actor viewers shapes.
    // 3 times the view width is bigger than actor viewers's maximum width.
    if (3*width >= allWidth) {
      startActorTime = 0;
      if (animationEditor.actorViewerMaxWidth < animationEditor.getReal(width+offsetX))
        endActorTime = animationEditor.getReal(width+offsetX);
      else
        endActorTime = animationEditor.actorViewerMaxWidth;
    }
    else {
      // Left virtual width is smaller than view width.
      if (offsetX <= width) {
        startActorTime = 0;
        endActorTime = animationEditor.getReal(3*width);
      }
      // Right virtual width is smaller than view width.
      else if (allWidth-offsetWidth <= width) {
        startActorTime = animationEditor.actorViewerMaxWidth - animationEditor.getReal(3*width);
        endActorTime = animationEditor.actorViewerMaxWidth;
      }
      // Left & right virtual widths are bigger than view width.
      else {
        startActorTime = animationEditor.getReal(offsetX-width);
        endActorTime = startActorTime + animationEditor.getReal(3*width);
      }
    }

    Graphics2D gi2 = animationEditor.scoreImage.createGraphics();
//    gi2.setColor(gi2.getBackground());
//    gi2.fillRect(0,0,animationEditor.scoreImage.getWidth(),animationEditor.scoreImage.getHeight());

    BaseActorViewer tempActorViewer;
    BaseSegmentViewer tempSegmentViewer;
    BaseMilestoneViewer tempMilestoneViewer;
    for (int i=startActor;i<endActor+1;i++) {
/*      if (i==0)
        tempActorViewer = (SoundActorViewer)animationEditor.actorViewers.get(i);
      else
        tempActorViewer = (ActorViewer)animationEditor.actorViewers.get(i);*/
      tempActorViewer = (BaseActorViewer)animationEditor.actorViewers.get(i);
      // Draw actor viewers shapes.
      tempActorViewer.actorShape.setRect(
        animationEditor.getZoomed(startActorTime)-offsetX,
        i*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)-offsetY,
        animationEditor.getZoomed(endActorTime-startActorTime),
        animationEditor.ACTOR_VIEWER_HEIGHT);
      gi2.setPaint(Color.white);
      gi2.fill(tempActorViewer.actorShape);
      gi2.setPaint(Color.black);
      gi2.draw(tempActorViewer.actorShape);

      // Draw the frame grid.
      gi2.setPaint(Color.lightGray);
      for (int k=0;k<tempActorViewer.actorViewerZoomedWidth;k+=animationEditor.columnView.increment) {
        gi2.drawLine(k-offsetX,
          i*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)-offsetY,
          k-offsetX,
          i*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)-offsetY+animationEditor.ACTOR_VIEWER_HEIGHT);
      }

      // Draw segment viewers shapes.
      for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
/*        if (i==0)
          tempSegmentViewer = (SoundSegmentViewer)tempActorViewer.segmentViewers.get(j);
        else
          tempSegmentViewer = (SegmentViewer)tempActorViewer.segmentViewers.get(j);*/
        tempSegmentViewer = (BaseSegmentViewer)tempActorViewer.segmentViewers.get(j);
        if ((tempSegmentViewer.segment.getEnd() >= startTime)
          && (tempSegmentViewer.segment.getStart() <= endTime)) {
            tempSegmentViewer.segmentShape.setRect(
              animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(tempSegmentViewer.segment.getStart()))-offsetX,
              i*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)+animationEditor.SEGMENT_Y-offsetY,
              animationEditor.getZoomed(animationEditor.columnView.getFrameEndTime(tempSegmentViewer.segment.getEnd())-animationEditor.columnView.getFrameStartTime(tempSegmentViewer.segment.getStart())+1),
              animationEditor.SEGMENT_HEIGHT);
            gi2.setPaint(Color.cyan);
            gi2.fill(tempSegmentViewer.segmentShape);
            gi2.setPaint(Color.blue);
            gi2.draw(tempSegmentViewer.segmentShape);

            // Draw milestone viewers shapes.
            if (i>0)
            for (int k=0;k<tempSegmentViewer.milestoneViewers.size();k++) {
/*              if (i==0)
                tempMilestoneViewer = (SoundMilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
              else
                tempMilestoneViewer = (MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);*/
              tempMilestoneViewer = (BaseMilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
              int milestoneTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(tempMilestoneViewer.milestone.getWhen()));
              gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              gi2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
              tempMilestoneViewer.milestoneShape.setFrame(
                milestoneTime-offsetX+1,
                i*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)+animationEditor.SEGMENT_Y-offsetY+(animationEditor.SEGMENT_HEIGHT-(animationEditor.columnView.increment-2))/2,
                animationEditor.columnView.increment-1,
                animationEditor.columnView.increment-1);
              gi2.setPaint(Color.black);
              gi2.draw(tempMilestoneViewer.milestoneShape);
              gi2.setPaint(Color.white);
              gi2.fill(tempMilestoneViewer.milestoneShape);
//              gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        }
      }
    }
  }

  /**
   * Paint temporary a segment before create it.
   * @param actor The actor to paint into.
   * @param time  The time to paint.
   */
  public void paintTempSegment(int actor, int time) {
    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    Rectangle2D.Double tempSegmentShape = new Rectangle2D.Double();
//    Rectangle2D.Double tempMilestoneShape = new Rectangle2D.Double();
    Ellipse2D.Double tempMilestoneShape = new Ellipse2D.Double();
    Graphics2D gi2 = animationEditor.scoreImage.createGraphics();
    tempSegmentShape.setRect(
      time-offsetX,
      actor*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)+animationEditor.SEGMENT_Y-offsetY,
      animationEditor.columnView.increment,
      animationEditor.SEGMENT_HEIGHT);
    gi2.setPaint(Color.cyan);
    gi2.fill(tempSegmentShape);
    gi2.setPaint(Color.blue);
    gi2.draw(tempSegmentShape);

    if (actor>0) {
      gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      gi2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
      tempMilestoneShape.setFrame(
        time - offsetX+1,
        actor*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)+animationEditor.SEGMENT_Y-offsetY+(animationEditor.SEGMENT_HEIGHT-(animationEditor.columnView.increment-2))/2,
        animationEditor.columnView.increment-1,
        animationEditor.columnView.increment-1);
      gi2.setPaint(Color.black);
      gi2.draw(tempMilestoneShape);
      gi2.setPaint(Color.white);
      gi2.fill(tempMilestoneShape);
//      gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
  }

  /**
   * Paint temporary a milestone for milestone edit mode.
   * @param milestoneViewer The milestone to paint.
   */
  public void paintTempMilestone(MilestoneViewer milestoneViewer) {
    int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    Ellipse2D.Double tempMilestoneShape = new Ellipse2D.Double();
    Graphics2D gi2 = animationEditor.scoreImage.createGraphics();
    gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	gi2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

    tempMilestoneShape.setFrame(
      animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(milestoneViewer.milestone.getWhen())) - offsetX+1,
      milestoneViewer.segmentViewer.actorViewer.animationEditor.actorViewers.indexOf(milestoneViewer.segmentViewer.actorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)+animationEditor.SEGMENT_Y-offsetY+(animationEditor.SEGMENT_HEIGHT-(animationEditor.columnView.increment-2))/2,
      animationEditor.columnView.increment-1,
      animationEditor.columnView.increment-1);
    gi2.setPaint(Color.black);
    gi2.draw(tempMilestoneShape);
    gi2.setPaint(Color.red);
    gi2.fill(tempMilestoneShape);
//    gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
  }
}
