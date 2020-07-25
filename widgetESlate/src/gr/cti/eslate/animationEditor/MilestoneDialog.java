package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.Milestone;

import java.awt.Component;
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
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;

/**
 * This class implements a dialog for edit milestone's properties.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
class MilestoneDialog extends JDialog {
  private static final int HGAP = 20;
  private static final int VGAP = 10;
  JButton okButton;
  JButton cancelButton;

  Milestone milestone;
  AnimationEditor animationEditor;
  JTextField [] textFieldGroup;

// Base change
  Actor tempActor;

  /**
   * Create a milestone dialog.
   * @param frame            Parent frame.
   * @param newMilestone     The milestone to edit.
   * @param aniEditor        The animation viewer of the milestone to edit.
   */
  public MilestoneDialog(Frame frame, Milestone newMilestone, AnimationEditor aniEditor) {
    super(frame, true);
    this.milestone = newMilestone;
    this.animationEditor = aniEditor;

// Base change
    tempActor = (Actor)milestone.getSegment().getActor();
    setSize(new Dimension(
      250,
      70+17*tempActor.getAniVarCount()+VGAP*(tempActor.getAniVarCount()+2)));
    setTitle(animationEditor.resources.getString("editMilestone")
      +tempActor.getActorInterface().getActorName()
      +" "
      +tempActor.getAnimationSession().getPlugID());
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

    Component [] labelGroup = new Component [tempActor.getAniVarCount()];
    for (int i=0;i<tempActor.getAniVarCount();i++) {
      JLabel label = new JLabel(
        tempActor.getActorInterface().getAnimatedPropertyStructure().getPropertyName(tempActor.getAniPropertyIDs().get(i))
        );
      labelGroup[i] = label;
      getContentPane().add(label, new ExplicitConstraints(label,
                            ContainerEF.left(getContentPane()).add(HGAP),
                            (i==0?ContainerEF.top(getContentPane()).add(VGAP):ComponentEF.bottom(labelGroup[i-1]).add(VGAP)),
                            null, null, 0.0, 0.0, true, true));
    }

    Expression fieldX = GroupEF.right(labelGroup).add(2*HGAP);
    Expression availableWidth = ContainerEF.right(getContentPane()).subtract(fieldX).subtract(HGAP);

    textFieldGroup = new JTextField [tempActor.getAniVarCount()];
    for (int i=0;i<tempActor.getAniVarCount();i++) {
      JTextField textField = new JTextField(Integer.toString(milestone.getAniVarValues().get(i)));
      textFieldGroup[i] = textField;
      getContentPane().add(textField, new ExplicitConstraints(textField,
                            fieldX, ComponentEF.top(labelGroup[i]),
                            MathEF.constant(100), null,
                            0.0, 0.0, true, true));
    }

    okButton = new JButton(animationEditor.resources.getString("okButton"));
    cancelButton = new JButton(animationEditor.resources.getString("cancelButton"));

    getContentPane().add(okButton, new ExplicitConstraints(okButton,
                          MathEF.subtract(ContainerEF.xFraction(getContentPane(), 0.5),MathEF.add(MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),20)),
                          ComponentEF.bottom(labelGroup[tempActor.getAniVarCount()-1]).add(VGAP*2),
                          MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                          null, 0.0, 0.0, true, true));
    getContentPane().add(cancelButton, new ExplicitConstraints(cancelButton,
                          MathEF.add(ContainerEF.xFraction(getContentPane(), 0.5),20),
                          ComponentEF.bottom(labelGroup[tempActor.getAniVarCount()-1]).add(VGAP*2),
                          MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                          null, 0.0, 0.0, true, true));

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        for (int i=0;i<tempActor.getAniVarCount();i++) {
          milestone.getAniVarValues().set(i,Integer.decode(textFieldGroup[i].getText()).intValue());
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
