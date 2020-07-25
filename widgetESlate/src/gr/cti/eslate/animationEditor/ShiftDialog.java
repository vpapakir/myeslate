package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.BaseActor;
import gr.cti.eslate.animation.BaseSegment;
import gr.cti.eslate.animation.SoundActor;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.MathEF;

/**
 * This class implements a dialog for insert/delete frames.
 * @author	Augustine Grillakis
 * @version	1.0.0, 20-Jun-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
class ShiftDialog extends JDialog {
  private static final int HGAP = 15;
  private static final int VGAP = 10;

  BaseActorViewer actorViewer;
  AnimationEditor animationEditor;
  int when;
  boolean insert;

  JButton okButton;
  JButton cancelButton;
  JTextField textField;

// Base change
  BaseActor tempActor;

  /**
   * Create a shift dialog.
   * @param frame              Parent frame.
   * @param newActorViewer     The actor viewer in which to insert/delete frames.
   * @param aniEditor          The animation editor of the actor to edit.
   * @param newWhen            Where to insert/delete frames.
   * @param newInsert          True for insert frames, false for delete.
   */
  public ShiftDialog(Frame frame, BaseActorViewer newActorViewer, AnimationEditor aniEditor, int newWhen, boolean newInsert) {
    super(frame, true);
    this.actorViewer = newActorViewer;
    this.animationEditor = aniEditor;
    this.when = newWhen;
    this.insert = newInsert;

// Base change
    if (SoundActorViewer.class.isAssignableFrom(actorViewer.getClass()))
      tempActor = (SoundActor)actorViewer.actor;
    else
      tempActor = (Actor)actorViewer.actor;
    setSize(new Dimension(240,120));
    if (insert)
      if (SoundActorViewer.class.isAssignableFrom(actorViewer.getClass()))
        setTitle(animationEditor.resources.getString("insertFrames")
        +animationEditor.resources.getString("soundActor"));
      else
        setTitle(animationEditor.resources.getString("insertFrames")
          +((Actor)tempActor).getActorInterface().getActorName()
          +" "
          +((Actor)tempActor).getAnimationSession().getPlugID());
    else
      if (SoundActorViewer.class.isAssignableFrom(actorViewer.getClass()))
        setTitle(animationEditor.resources.getString("deleteFrames")
        +animationEditor.resources.getString("soundActor"));
      else
        setTitle(animationEditor.resources.getString("deleteFrames")
          +((Actor)tempActor).getActorInterface().getActorName()
          +" "
          +((Actor)tempActor).getAnimationSession().getPlugID());
    setResizable(false);
//    setDefaultCloseOperation(DISPOSE_ON_CLOSE);


    // OK handler.
    getRootPane().registerKeyboardAction(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                             okButton.doClick();
                                           }
                                        },
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0),
      JComponent.WHEN_IN_FOCUSED_WINDOW);
    // Esc handler.
    getRootPane().registerKeyboardAction(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                             cancelButton.doClick();
                                           }
                                        },
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
      JComponent.WHEN_IN_FOCUSED_WINDOW);

    getContentPane().setLayout(new ExplicitLayout());

    JLabel label = new JLabel(animationEditor.resources.getString("framesNumber"));
    getContentPane().add(label, new ExplicitConstraints(label,
                          ContainerEF.left(getContentPane()).add(HGAP),
                          ContainerEF.top(getContentPane()).add(VGAP),
                          null, null, 0.0, 0.0, true, true));

    textField = new JTextField();
    getContentPane().add(textField, new ExplicitConstraints(textField,
                          ComponentEF.right(label).add(HGAP*4),
                          ComponentEF.top(label),
                          MathEF.constant(50), null,
                          0.0, 0.0, true, true));

    okButton = new JButton(animationEditor.resources.getString("okButton"));
    cancelButton = new JButton(animationEditor.resources.getString("cancelButton"));

    getContentPane().add(okButton, new ExplicitConstraints(okButton,
                          MathEF.subtract(ContainerEF.xFraction(getContentPane(), 0.5),MathEF.add(MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),20)),
                          ComponentEF.bottom(label).add(VGAP*2),
                          MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                          null, 0.0, 0.0, true, true));
    getContentPane().add(cancelButton, new ExplicitConstraints(cancelButton,
                          MathEF.add(ContainerEF.xFraction(getContentPane(), 0.5),20),
                          ComponentEF.top(okButton),
                          MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                          null, 0.0, 0.0, true, true));

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        if (Integer.decode(textField.getText()).intValue()>0) {
          boolean shiftFlag = false;
          for (int j=0;j<actorViewer.segmentViewers.size();j++) {
            if (when < ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.getStart()) {
              ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segmentZeroStart = ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.getStart();
              ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segmentZeroEnd = ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.getEnd();
              for (int k=0;k<((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.size();k++) {
                ((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime =
                  ((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getWhen();
              }
              shiftFlag = true;
            }
          }
          if (shiftFlag) {
            if (insert) {
// Base change
              BaseSegment iteratorSegment = actorViewer.actor.getStartSegment();
              while (iteratorSegment.getNext() != null)
                iteratorSegment = iteratorSegment.getNext();
              if (animationEditor.columnView.getFrameEndTime(iteratorSegment.getEnd()+Integer.decode(textField.getText()).intValue()) > animationEditor.actorViewerMaxWidth)
                animationEditor.actorViewerMaxWidth = animationEditor.columnView.getFrameEndTime(iteratorSegment.getEnd() + Integer.decode(textField.getText()).intValue());
              for (int j=0;j<actorViewer.segmentViewers.size();j++) {
                if (when < ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.getStart()) {
                  animationEditor.processModelEvents = false;
                  ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.setStart(((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).getSegmentZeroStart() + Integer.decode(textField.getText()).intValue());
                  animationEditor.processModelEvents = false;
                  ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.setEnd(((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).getSegmentZeroEnd() + Integer.decode(textField.getText()).intValue());
                  for (int k=0;k<((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.size();k++) {
                    if ((((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                      && (((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                        animationEditor.processModelEvents = false;
                        ((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                          ((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime
                          + Integer.decode(textField.getText()).intValue());
                    }
                  }
                }
              }
            }
            else {
// Base change
              BaseSegment iteratorSegment = actorViewer.actor.getStartSegment();
              while (iteratorSegment != null) {
                if (when < iteratorSegment.getStart())
                  break;
                iteratorSegment = iteratorSegment.getNext();
              }
              if ((iteratorSegment.getStart()-when) >= Integer.decode(textField.getText()).intValue()) {
                for (int j=0;j<actorViewer.segmentViewers.size();j++) {
                  if (when < ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.getStart()) {
                    animationEditor.processModelEvents = false;
                    ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.setStart(((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).getSegmentZeroStart() - Integer.decode(textField.getText()).intValue());
                    animationEditor.processModelEvents = false;
                    ((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).segment.setEnd(((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).getSegmentZeroEnd() - Integer.decode(textField.getText()).intValue());
                    for (int k=0;k<((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.size();k++) {
                      if ((((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getPrevious()!=null)
                        && (((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.getNext()!=null)) {
                        animationEditor.processModelEvents = false;
                        ((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).milestone.setWhen(
                          ((BaseMilestoneViewer)((BaseSegmentViewer)actorViewer.segmentViewers.get(j)).milestoneViewers.get(k)).zeroTime
                          - Integer.decode(textField.getText()).intValue());
                      }
                    }
                  }
                }
                animationEditor.computeActorViewerMaxWidth();
              }
            }
            int width = animationEditor.getSize().width-animationEditor.scrollPaneX-animationEditor.lblActorWidth-animationEditor.viewportGap;
            if (animationEditor.actorViewerMaxWidth != actorViewer.actorViewerRealWidth)
              actorViewer.actorViewerRealWidth = animationEditor.actorViewerMaxWidth;
            if (actorViewer.actorViewerRealWidth < animationEditor.getReal(width))
              actorViewer.actorViewerRealWidth = animationEditor.getReal(width);
            actorViewer.actorViewerZoomedWidth = animationEditor.getZoomed(actorViewer.actorViewerRealWidth);
            for (int i=0;i<animationEditor.actorViewers.size();i++) {
              ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerRealWidth = actorViewer.actorViewerRealWidth;
              ((BaseActorViewer)animationEditor.actorViewers.get(i)).actorViewerZoomedWidth = actorViewer.actorViewerZoomedWidth;
            }
            animationEditor.mainPanelZoomedWidth = actorViewer.actorViewerZoomedWidth;
            animationEditor.mainPanelRealWidth = actorViewer.actorViewerRealWidth;
            animationEditor.mainPanel.setPreferredSize(new Dimension(
              animationEditor.mainPanelZoomedWidth,
              animationEditor.actorViewers.size()*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE)));
            animationEditor.columnView.setPreferredWidth(animationEditor.mainPanelZoomedWidth);
            animationEditor.columnView.revalidate();
            animationEditor.mainPanel.revalidate();
            animationEditor.mainPanel.paintScoreImage(
              animationEditor.actorViewers.indexOf(actorViewer),
              animationEditor.actorViewers.indexOf(actorViewer),
              0,
              actorViewer.actorViewerRealWidth);
            animationEditor.mainPanel.repaint(
              0,
              animationEditor.actorViewers.indexOf(actorViewer)*(animationEditor.ACTOR_VIEWER_HEIGHT+animationEditor.ACTOR_SPACE),
              actorViewer.actorViewerZoomedWidth,
              animationEditor.ACTOR_VIEWER_HEIGHT);
          }
        }
        dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        dispose();
      }
    });

    // Add the window listener.
    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            dispose();
        }
    });
  }
}
