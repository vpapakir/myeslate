package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.Segment;
import gr.cti.eslate.animation.SoundSegment;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class implements the actor panel.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
public class ActorPanel extends JPanel {
  AnimationEditor animationEditor;

  /**
   * Create an actor panel.
   * @param aniEditor The animation viewer of the actor panel.
   */
  public ActorPanel(AnimationEditor aniEditor) {
    this.animationEditor = aniEditor;
    addMouseMotionListener(new MouseMotionAdapter(){
      public void mouseMoved(MouseEvent e) {
        String toolTipText = null;
        for (int i=0;i<animationEditor.actorViewers.size();i++) {
          if (i==0) {
            BaseActorViewer tempActorViewer = (BaseActorViewer)animationEditor.actorViewers.get(i);
            if((e.getY()>(i*(AnimationEditor.ACTOR_VIEWER_HEIGHT+AnimationEditor.ACTOR_SPACE)))
              && (e.getY()<((i+1)*(AnimationEditor.ACTOR_VIEWER_HEIGHT+AnimationEditor.ACTOR_SPACE)))) {
                toolTipText = "<html><font face=Helvetica size=2>";
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
                if (tempActorViewer.segmentViewers.size()==0)
                  toolTipText = null;
            }
          }
          else {
            Actor tempActor = (Actor)((BaseActorViewer)animationEditor.actorViewers.get(i)).actor;
            if((e.getY()>(i*(AnimationEditor.ACTOR_VIEWER_HEIGHT+AnimationEditor.ACTOR_SPACE)))
              && (e.getY()<((i+1)*(AnimationEditor.ACTOR_VIEWER_HEIGHT+AnimationEditor.ACTOR_SPACE)))) {
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
                break;
            }
          }
        }
        setToolTipText(toolTipText);
      }
    });
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        // Check if play thread is active.
        if (!animationEditor.animation.isPlayThreadActive())
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
          if (e.getClickCount() == 2) {
            if (animationEditor.editMilestoneViewer != null)
              animationEditor.exitMilestoneEditMode();
            for (int i=1;i<animationEditor.actorViewers.size();i++) {
              if((e.getY()>(i*(AnimationEditor.ACTOR_VIEWER_HEIGHT+AnimationEditor.ACTOR_SPACE)))
                && (e.getY()<((i+1)*(AnimationEditor.ACTOR_VIEWER_HEIGHT+AnimationEditor.ACTOR_SPACE)))) {
                  VariablesDialog variablesDialog = new VariablesDialog((Frame)SwingUtilities.getAncestorOfClass(Frame.class,animationEditor),(Actor)animationEditor.animation.getActors().get(i), animationEditor);
                  variablesDialog.setLocationRelativeTo(animationEditor);
                  variablesDialog.setVisible(true);
                  // Recompute the segment's variables change step.
                  Segment tempSegment = (Segment)((Actor)animationEditor.animation.getActors().get(i)).getSegment(animationEditor.animation.getCursorTime());
                  if (tempSegment != null)
// Base change
                    tempSegment.betweenMilestones = false;
                  break;
              }
            }
          }
        }
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

    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    // Draw buffered actor label image.
    if (animationEditor.actorImage != null)
      g2.drawImage(animationEditor.actorImage, 0, offsetY, this);
  }

  /**
   * Draw fixed actors labels.
   */
  public void paintFixedActors () {
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    Graphics2D gi2 = animationEditor.actorImage.createGraphics();
//    gi2.setColor(gi2.getBackground());
//    gi2.fillRect(0,0,animationEditor.actorImage.getWidth(),animationEditor.actorImage.getHeight());
//    gi2.setColor(Color.green);
    gi2.setColor(Color.black);
    gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	gi2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    gi2.drawString(animationEditor.resources.getString("soundActor"),
      0,
      (animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)-offsetY-5);
//    gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
  }

  /**
   * Draw actor labels.
   */
  public void paintActors () {
    int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    Graphics2D gi2 = animationEditor.actorImage.createGraphics();
//    gi2.setColor(gi2.getBackground());
//    gi2.fillRect(0,0,animationEditor.actorImage.getWidth(),animationEditor.actorImage.getHeight());
//    gi2.setColor(Color.yellow);
    gi2.setColor(Color.black);
    removeAll();
    if (animationEditor.animation != null)
      paintFixedActors();
    for (int i=1;i<animationEditor.actorViewers.size();i++) {
      gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      gi2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
      gi2.drawString(((Actor)animationEditor.animation.getActors().get(i)).getActorInterface().getActorName(),
        0,
        (i+1)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)-offsetY-5);
//      gi2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
  }
}
