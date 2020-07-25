package gr.cti.eslate.animationEditor;

import javax.swing.*;
import java.awt.*;

import gr.cti.eslate.animation.*;

/**
 * This class implements the horizontal rule of time.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.AnimationEditor
public class Rule extends JComponent {
  public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;
  public static final int SIZE = 25;  //35
  AnimationEditor animationEditor;

  public int orientation;
  public boolean isMetric;
  /*private*/ int increment;
  private int units;

  public Rule(int o, boolean m, AnimationEditor animationEditor) {
      this.animationEditor = animationEditor;
      orientation = o;
      isMetric = m;
      setIncrementAndUnits(100);
  }

  public void setIsMetric(boolean isMetric) {
      this.isMetric = isMetric;
      setIncrementAndUnits(100);
      repaint();
  }

  public /*private*/ void setIncrementAndUnits(int zoom) {
      if (isMetric) {
//              units = (int)((double)INCH / (double)2.54); // dots per centimeter
          units = animationEditor.getZoomed(40);
          increment = units/10;
      } else {
          units = INCH;
          increment = units / 2;
      }
  }

  public boolean isMetric() {
      return this.isMetric;
  }

  public int getIncrement() {
      return increment;
  }

  public void setPreferredHeight(int ph) {
      setPreferredSize(new Dimension(SIZE, ph));
  }

  public void setPreferredWidth(int pw) {
      setPreferredSize(new Dimension(pw, SIZE));
  }

  public void paintComponent(Graphics g) {
      Rectangle drawHere = g.getClipBounds();

      // Fill clipping area with color.
      g.setColor(Color.yellow);
      g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

      // Do the ruler labels in a small font that's black.
      g.setFont(new Font("SansSerif", Font.PLAIN, 10));
      g.setColor(Color.black);

      // Some vars we need.
      int end = 0;
      int start = 0;
      int tickLength = 0;
      String text = null;

      // Use clipping bounds to calculate first tick and last tick location.
      if (orientation == HORIZONTAL) {
          start = (drawHere.x / increment) * increment;
          end = (((drawHere.x + drawHere.width) / increment) + 1)
                * increment;
      } else {
          start = (drawHere.y / increment) * increment;
          end = (((drawHere.y + drawHere.height) / increment) + 1)
                * increment;
      }

      // Make a special case of 0 to display the number
      // within the rule and draw a units label.
      if (start == 0) {
//              text = Integer.toString(0) + (isMetric ? " cm" : " in");
          text = Integer.toString(1);
          tickLength = 10;
          if (orientation == HORIZONTAL) {
              g.drawLine(0, SIZE-1, 0, SIZE-tickLength-1);
//              g.drawString(text, 2, 21);
              g.drawString(text, increment/2-2, SIZE-14/*21*/);
          } else {
              g.drawLine(SIZE-1, 0, SIZE-tickLength-1, 0);
              g.drawString(text, 9, 10);
          }
          text = null;
          start = increment;
      }

      // ticks and labels
      for (int i = start; i < end; i += increment) {
          if (i % units == 0)  {
              tickLength = 10;
              text = Integer.toString(i/units*10);
          } else if (i % units == 9*increment)  {
              tickLength = 10;
              text = null;
          } else {
              tickLength = 7;
              text = null;
          }

          if (tickLength != 0) {
              if (orientation == HORIZONTAL) {
                  g.drawLine(i, SIZE-1, i, SIZE-tickLength-1);
                  if (text != null)
//                      g.drawString(text, i-3, 21);
                      g.drawString(text, i-3-increment, SIZE-14/*21*/);
              } else {
                  g.drawLine(SIZE-1, i, SIZE-tickLength-1, i);
                  if (text != null)
                      g.drawString(text, 9, i+3);
              }
          }
      }

    // Paint frames that have labels.
    if (animationEditor.animation != null) {
      for (int i=0;i<animationEditor.animation.getFrameLabels().size();i++) {
        g.setColor(Color.magenta);
        g.fillRect(
          animationEditor.getZoomed(getFrameStartTime(((FrameLabel)animationEditor.animation.getFrameLabels().get(i)).getFrame()))+1,
          SIZE-7,
          increment-1,
          6);
      }
    }

    // Place frame cursor on rule.
    if (animationEditor.animation != null) {
      int cursorTime = animationEditor.getZoomed(animationEditor.columnView.getFrameStartTime(animationEditor.animation.getCursorTime()));
      if (cursorTime % increment != 0)
        cursorTime -= cursorTime % increment;
      animationEditor.timeCursorColumnView.setBounds(
        cursorTime+1,
        0,
        increment-1,
        SIZE);
    }

    // Place mouse marks on rule.
    if (animationEditor.mouseMarkXStart >= 0) {
      g.setColor(Color.blue);
      g.drawRect(animationEditor.mouseMarkXStart,SIZE-3,increment,1);
    }
    if (animationEditor.mouseMarkXEnd >= 0) {
      g.setColor(Color.blue);
      g.drawRect(animationEditor.mouseMarkXEnd,SIZE-3,increment,1);
    }
  }

  /**
   * Gets the frame of time.
   * @param   time  The time to get frame of.
   * @return  The frame of time.
   */
  public int getFrame(int time) {
    return (time/animationEditor.getReal(increment)+1);
  }

  /**
   * Gets the start time of frame.
   * @param   frame   The frame to get the start time of.
   * @return  The start time of frame.
   */
  public int getFrameStartTime(int frame) {
    return ((frame-1)*animationEditor.getReal(increment));
  }
  /**
   * Gets the end time of frame.
   * @param   frame   The frame to get the end time of.
   * @return  The end time of frame.
   */
  public int getFrameEndTime(int frame) {
    return (frame*animationEditor.getReal(increment)-1);
  }
}
