package gr.cti.eslate.animationEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * This class implements the time cursor.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
public class TimeCursor extends JLabel {
  AnimationEditor animationEditor;
  int position;

  /**
   * Create a time cursor.
   * @param   animationEditor  The animation viewer of the time cursor.
   * @param   position        The positon of the time cursor.
   */
  public TimeCursor(AnimationEditor animationEditor, int position) {
    this.animationEditor = animationEditor;
    setOpaque(false);
    this.position = position;
  }

  /**
   * Custom paintComponent.
   * @param g The graphics class.
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    super.paintComponent(g2);
    if (position == 1) {
      g2.setPaint(Color.red);
      g2.drawRect(0,0,animationEditor.columnView.increment-2,animationEditor.columnView.SIZE-1);
      g2.setPaint(new Color(255,0,0,100));
      g2.fillRect(1,1,animationEditor.columnView.increment-3,animationEditor.columnView.SIZE-2);
    }
    else {
      g2.setPaint(Color.red);
      g2.drawLine(0,0,0,animationEditor.getSize().height-animationEditor.scrollPaneY-animationEditor.columnView.SIZE);
    }
  }
}