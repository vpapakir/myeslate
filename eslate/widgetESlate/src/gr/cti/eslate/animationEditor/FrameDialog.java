package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.FrameLabel;

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
 * This class implements a dialog for frame labeling.
 * @author	Augustine Grillakis
 * @version	1.0.0, 21-Jun-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
class FrameDialog extends JDialog {
  private static final int HGAP = 15;
  private static final int VGAP = 10;

  AnimationEditor animationEditor;
  int when;
  boolean addEditFlag;

  JButton okButton;
  JButton cancelButton;
  JTextField textField;

  /**
   * Create a frame dialog.
   * @param frame           Parent frame.
   * @param aniEditor       The animation editor.
   * @param newWhen         The frame to label.
   * @param newAddEditFlag  True for add label, false for edit label.
   */
  public FrameDialog(Frame frame, AnimationEditor aniEditor, int newWhen, boolean newAddEditFlag) {
    super(frame, true);
    this.animationEditor = aniEditor;
    this.when = newWhen;
    this.addEditFlag = newAddEditFlag;

    setSize(new Dimension(260,120));
    if (addEditFlag)
      setTitle(animationEditor.resources.getString("addFrameLabel")
      +when);
    else
      setTitle(animationEditor.resources.getString("editFrameLabel")
      +when);
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

    JLabel label = new JLabel(animationEditor.resources.getString("frameLabel"));
    getContentPane().add(label, new ExplicitConstraints(label,
                          ContainerEF.left(getContentPane()).add(HGAP),
                          ContainerEF.top(getContentPane()).add(VGAP),
                          null, null, 0.0, 0.0, true, true));

    textField = new JTextField();
    getContentPane().add(textField, new ExplicitConstraints(textField,
                          ComponentEF.right(label).add(HGAP),
                          ComponentEF.top(label),
                          MathEF.constant(90), null,
                          0.0, 0.0, true, true));
    if (!addEditFlag) {
      for (int i=0;i<animationEditor.animation.getFrameLabels().size();i++) {
        if (((FrameLabel)animationEditor.animation.getFrameLabels().get(i)).getFrame() == when) {
          textField.setText(((FrameLabel)animationEditor.animation.getFrameLabels().get(i)).getLabel());
          break;
        }
      }
    }

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
        int position = animationEditor.animation.getFrameLabels().size();
        for (int i=0;i<animationEditor.animation.getFrameLabels().size();i++) {
          if (when <= ((FrameLabel)animationEditor.animation.getFrameLabels().get(i)).getFrame())
            position = i;
        }
        if (addEditFlag)
          animationEditor.animation.getFrameLabels().add(
            position,
            new FrameLabel(when, textField.getText()));
        else
          animationEditor.animation.getFrameLabels().set(
            position,
            new FrameLabel(when, textField.getText()));
        animationEditor.columnView.repaint();
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
