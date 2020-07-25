package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.Milestone;
import gr.cti.eslate.animation.Segment;
import gr.cti.eslate.animation.SoundSegment;
import gr.cti.eslate.base.IProtocolPlug;
import gr.cti.eslate.base.PlugNotConnectedException;
import gr.cti.eslate.base.ProtocolPlug;
import gr.cti.typeArray.IntBaseArray;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * This class implements the popup listener.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see         gr.cti.eslate.animationEditor.AnimationEditor
public class PopupListener extends MouseAdapter {
  AnimationEditor animationEditor;
  BaseActorViewer workingActorViewer;
  BaseSegmentViewer workingSegmentViewer;
  BaseMilestoneViewer workingMilestoneViewer;
  JPopupMenu popup = new JPopupMenu();
  JMenuItem menuItemAddMilestone;
  JMenuItem menuItemRemoveMilestone;
  JMenuItem menuItemRemoveSegment;
  JMenuItem menuItemRemoveActor;
  JMenuItem menuItemShiftRight;
  JMenuItem menuItemShiftLeft;
  JMenuItem menuItemAddSoundFile;
  JMenuItem menuItemEditSoundFile;
  JMenuItem menuItemRemoveSoundFile;
  boolean showFlag;
  boolean segmentProcess;
  boolean milestoneProcess;
  int mousePressedX;

  /**
   * Create a popup listener.
   * @param aniEditor  The animation viewer of the popup listener.
   */
  public PopupListener(AnimationEditor aniEditor) {
    this.animationEditor = aniEditor;

    menuItemAddMilestone = new JMenuItem(animationEditor.resources.getString("addMilestone"));
    menuItemRemoveMilestone = new JMenuItem(animationEditor.resources.getString("removeMilestone"));
    menuItemRemoveSegment = new JMenuItem(animationEditor.resources.getString("removeSegment"));
    menuItemRemoveActor = new JMenuItem(animationEditor.resources.getString("removeActor"));
    menuItemShiftRight = new JMenuItem(animationEditor.resources.getString("shiftRight"));
    menuItemShiftLeft = new JMenuItem(animationEditor.resources.getString("shiftLeft"));
    menuItemAddSoundFile = new JMenuItem(animationEditor.resources.getString("addSoundFile"));
    menuItemEditSoundFile = new JMenuItem(animationEditor.resources.getString("editSoundFile"));
    menuItemRemoveSoundFile = new JMenuItem(animationEditor.resources.getString("removeSoundFile"));

    menuItemRemoveActor.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        // Remove actor plug from animation
        IProtocolPlug [] protocolPlugs = ((ProtocolPlug)animationEditor.animation.getESlateHandle().getPlug(animationEditor.animation.getResources().getString("actorPlug"))).getProtocolPlugs();
        int i;
        for (i=0;i<protocolPlugs.length;i++) {
// Base change
          if (((Actor)workingActorViewer.actor).getAnimationSession() == animationEditor.animation.getAnimationSessions().get((ProtocolPlug)protocolPlugs[i])) {
            try {
              ((ProtocolPlug)animationEditor.animation.getESlateHandle().getPlug(animationEditor.animation.getResources().getString("actorPlug"))).disconnectPlug((ProtocolPlug)protocolPlugs[i]);
            } catch (PlugNotConnectedException ex) {
            }
            break;
          }
        }
/*        animationEditor.removeActorViewer(workingActorViewer);
        animationEditor.processModelEvents = false;
        animationEditor.animation.removeActor(workingActorViewer.actor);

        animationEditor.lblActorPanel.paintActors(animationEditor.actorViewers.size());
        animationEditor.lblActorPanel.repaint();

        Graphics2D gi2 = animationEditor.scoreImage.createGraphics();
        gi2.setColor(gi2.getBackground());
        gi2.fillRect(0,
          animationEditor.actorViewers.size()*(animationEditor.actorViewerHeight+animationEditor.actorSpace),
          animationEditor.scoreImage.getWidth(),
          animationEditor.actorViewerHeight+animationEditor.actorSpace);
        animationEditor.mainPanel.drawBufferedScoreImage();
        animationEditor.mainPanel.repaint();
        animationEditor.mainPanel.setPreferredSize(new Dimension(
          animationEditor.mainPanelZoomedWidth,
          animationEditor.actorViewers.size()*(animationEditor.actorViewerHeight+animationEditor.actorSpace)));
        animationEditor.lblActorPanel.setPreferredSize(new Dimension(
          animationEditor.lblActorWidth,
          animationEditor.actorViewers.size()*(animationEditor.actorViewerHeight+animationEditor.actorSpace)));
        animationEditor.mainPanel.revalidate();
        animationEditor.lblActorPanel.revalidate();*/
      }
    });

    menuItemRemoveSegment.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
        Rectangle r = new Rectangle();
        int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
        int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;

        workingActorViewer.removeSegmentViewer(workingSegmentViewer);
        workingActorViewer.animationEditor.processModelEvents = false;
        workingActorViewer.actor.removeSegment(workingSegmentViewer.segment);
        animationEditor.mainPanel.paintScoreImage(
          animationEditor.actorViewers.indexOf(workingActorViewer),
          animationEditor.actorViewers.indexOf(workingActorViewer),
          0,
          workingActorViewer.actorViewerRealWidth);
        animationEditor.mainPanel.repaint(0,
              animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
              workingActorViewer.actorViewerZoomedWidth,
              animationEditor.ACTOR_VIEWER_HEIGHT);

        animationEditor.computeActorViewerMaxWidth();
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
        animationEditor.mainPanel.setPreferredSize(new Dimension(
          animationEditor.mainPanelZoomedWidth,
          animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
        animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
        animationEditor.columnView.revalidate();
        animationEditor.mainPanel.revalidate();
        r.setRect(workingActorViewer.actorShape.getWidth()+offsetX, workingActorViewer.actorShape.getY()+offsetY, 1, 1);
        animationEditor.mainPanel.scrollRectToVisible(r);
      }
    });

    menuItemAddMilestone.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        int when = animationEditor.columnView.getFrame(animationEditor.getReal(mousePressedX));
        Milestone milestone = new Milestone(workingSegmentViewer.segment, when);
        animationEditor.processModelEvents = false;
        workingSegmentViewer.segment.addMilestone(milestone);

        // Compute milestone's variables.
/*        IntBaseArray aniValues = new IntBaseArray();
        for (int j=0;j<workingSegmentViewer.segment.getActor().getAniVarCount();j++) {
          double step = ((double)(milestone.getNext().getAniVarValues().get(j)-milestone.getPrevious().getAniVarValues().get(j)))/((double)(milestone.getNext().getWhen()-milestone.getPrevious().getWhen()));
          int newAniValue = (int)(milestone.getPrevious().getAniVarValues().get(j)+step*((double)(when-milestone.getPrevious().getWhen())));
          aniValues.add(newAniValue);
        }
        milestone.setAniVarValues(aniValues);*/

        // Get actor's current values.
        IntBaseArray aniValues = new IntBaseArray();
// Base change
        Actor tempActor = (Actor)workingSegmentViewer.segment.getActor();
        for (int j=0;j<tempActor.getAniVarCount();j++) {
          int newAniValue = tempActor.getActorInterface().getVarValues().get(
            tempActor.getActorInterface().getAnimatedPropertyStructure().indexOfAnimatedPropertyDescriptor(
            tempActor.getAniPropertyIDs().get(j)));
          aniValues.add(newAniValue);
        }
        milestone.setAniVarValues(aniValues);

//        Rectangle2D.Double milestoneShape = new Rectangle2D.Double();
        Ellipse2D.Double milestoneShape = new Ellipse2D.Double();
        MilestoneViewer milestoneViewer = new MilestoneViewer(workingSegmentViewer, milestone, milestoneShape);
        workingSegmentViewer.addMilestoneViewer(milestoneViewer);
        animationEditor.mainPanel.paintScoreImage(
          animationEditor.actorViewers.indexOf(workingActorViewer),
          animationEditor.actorViewers.indexOf(workingActorViewer),
          0,
          workingActorViewer.actorViewerRealWidth);
        animationEditor.mainPanel.repaint(0,
              animationEditor.actorViewers.indexOf(workingActorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
              workingActorViewer.actorViewerZoomedWidth,
              animationEditor.ACTOR_VIEWER_HEIGHT);

        // Recompute the segment's current milestone.
// Base change
        ((Segment)milestone.getSegment()).currentMilestone = null;
      }
    });

    menuItemRemoveMilestone.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        // Recompute the segment's current milestone.
// Base change
        ((Segment)workingMilestoneViewer.milestone.getSegment()).currentMilestone = null;

        workingSegmentViewer.removeMilestoneViewer(workingMilestoneViewer);
        workingSegmentViewer.actorViewer.animationEditor.processModelEvents = false;
        workingSegmentViewer.segment.removeMilestone(workingMilestoneViewer.milestone);
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
    });

    menuItemShiftRight.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        ShiftDialog shiftDialog = new ShiftDialog(
          (Frame)SwingUtilities.getAncestorOfClass(Frame.class,animationEditor),
          workingActorViewer,
          animationEditor,
          animationEditor.columnView.getFrame(animationEditor.getReal(mousePressedX)),
          true);
        shiftDialog.setLocationRelativeTo(animationEditor);
        shiftDialog.show();
      }
    });

    menuItemShiftLeft.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        ShiftDialog shiftDialog = new ShiftDialog(
          (Frame)SwingUtilities.getAncestorOfClass(Frame.class,animationEditor),
          workingActorViewer,
          animationEditor,
          animationEditor.columnView.getFrame(animationEditor.getReal(mousePressedX)),
          false);
        shiftDialog.setLocationRelativeTo(animationEditor);
        shiftDialog.show();
      }
    });

    menuItemAddSoundFile.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        animationEditor.chooseSoundFile((SoundSegment)workingSegmentViewer.segment);
      }
    });
    menuItemEditSoundFile.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        animationEditor.chooseSoundFile((SoundSegment)workingSegmentViewer.segment);
      }
    });
    menuItemRemoveSoundFile.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        ((SoundSegment)workingSegmentViewer.segment).setSoundFile(null);
      }
    });
  }

  public void mousePressed(MouseEvent e) {
    mousePressedX = e.getX();
    if (mousePressedX % animationEditor.columnView.increment != 0)
      mousePressedX -= mousePressedX % animationEditor.columnView.increment;
    showFlag = false;
    int virtualMouseX = e.getX()-animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
    int virtualMouseY = e.getY()-animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
    // Check if play thread is active.
    if (!animationEditor.animation.isPlayThreadActive())
    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
      if (animationEditor.editMilestoneViewer != null)
        animationEditor.exitMilestoneEditMode();
      workingActorViewer = null;
      workingSegmentViewer = null;
      workingMilestoneViewer = null;
      for (int i=0;i<animationEditor.actorViewers.size();i++) {
        if (((BaseActorViewer)animationEditor.actorViewers.get(i)).actorShape.contains(virtualMouseX,virtualMouseY)) {
          workingActorViewer = (BaseActorViewer)animationEditor.actorViewers.get(i);
          segmentProcess = false;
          milestoneProcess = false;
          for (int j=0;j<workingActorViewer.segmentViewers.size();j++) {
            if (((BaseSegmentViewer)workingActorViewer.segmentViewers.get(j)).segmentShape.contains(virtualMouseX,virtualMouseY)) {
              workingSegmentViewer = (BaseSegmentViewer)workingActorViewer.segmentViewers.get(j);
              for (int k=0;k<workingSegmentViewer.milestoneViewers.size();k++) {

                int offsetX = animationEditor.scoreScrollPane.getViewport().getViewPosition().x;
                int offsetY = animationEditor.scoreScrollPane.getViewport().getViewPosition().y;
                int milestoneTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(
                  ((BaseMilestoneViewer)((BaseSegmentViewer)((BaseActorViewer)animationEditor.actorViewers.get(i)).segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getWhen()));
                if (((BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k)).milestoneShape.contains(virtualMouseX,virtualMouseY)) {
                  workingMilestoneViewer = (BaseMilestoneViewer)workingSegmentViewer.milestoneViewers.get(k);
                  milestoneProcess = true;
                  showFlag = true;
                  popup.removeAll();
                  if ((workingMilestoneViewer.milestone.getPrevious() != null) && (workingMilestoneViewer.milestone.getNext() != null)) {
                    popup.add(menuItemRemoveMilestone);
                    popup.add(menuItemRemoveSegment);
                    popup.add(menuItemRemoveActor);
                  }
                  else {
                    popup.add(menuItemRemoveSegment);
                    popup.add(menuItemRemoveActor);
                  }
                  break;
                }
              }
              if (milestoneProcess == false) {
                segmentProcess = true;
                showFlag = true;
                popup.removeAll();
                popup.add(menuItemRemoveSegment);
                if (SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass())) {
                  if (((SoundSegment)workingSegmentViewer.segment).getSoundFile() != null) {
                    popup.add(menuItemEditSoundFile);
                    popup.add(menuItemRemoveSoundFile);
                  }
                  else
                    popup.add(menuItemAddSoundFile);
                }
                if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass())) {
                  popup.add(menuItemAddMilestone);
                  popup.add(menuItemRemoveActor);
                }
              }
              break;
            }
          }
          if ((segmentProcess == false) && (milestoneProcess == false)) {
            showFlag = true;
            popup.removeAll();
            popup.add(menuItemShiftRight);
            popup.add(menuItemShiftLeft);
            if (!SoundActorViewer.class.isAssignableFrom(workingActorViewer.getClass()))
              popup.add(menuItemRemoveActor);
          }
          break;
        }
      }
    }
    maybeShowPopup(e);
  }

  public void mouseReleased(MouseEvent e) {
    if (showFlag == true)
      maybeShowPopup(e);
  }

  private void maybeShowPopup(MouseEvent e) {
    if (e.isPopupTrigger())
      popup.show(e.getComponent(), e.getX(), e.getY());
  }
}
