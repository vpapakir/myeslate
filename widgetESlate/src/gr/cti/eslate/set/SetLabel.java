package gr.cti.eslate.set;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements the label attached to an ellipse, showing the text of
 * the query that generated the corresponding set. In addition to the displyed
 * text, it provides a button for deleting the ellipse.
 *
 * @author	Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 */
class SetLabel extends CardPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The panel shown when the quety text is not displayed.
   */
  JPanel bg;
  /**
   * The text field where the query text is displayed.
   */
  private CursorTextField tf;
  /**
   * The button for deleting the ellipse.
   */
  private JButton button;

  /**
   * Display the button and query text.
   */
  final static String LABEL = "label";
  /**
   * Hide the button and query text.
   */
  final static String NOLABEL = "nolabel";

  /**
   * Create a label.
   * @param	c	The color of the label when the text and button are
   *			hidden.
   */
  SetLabel(Color c)
  {
    super();
    bg = new JPanel();
    bg.setBackground(c);
    bg.setOpaque(true);
    add(bg, NOLABEL);
    JPanel fg = new JPanel();
    tf = new CursorTextField();
    tf.setBackground(Color.lightGray);
    //Font f = tf.getFont();
    //tf.setFont(new Font(f.getFamily(), f.getStyle(), f.getSize()-1));
    fg.setLayout(new BoxLayout(fg, BoxLayout.X_AXIS));
    fg.add(tf);
    int buttonSize = tf.getPreferredSize().height;
    button = new JButton(new ImageIcon(getClass().getResource("images/x.gif")));
    button.setAlignmentY(JButton.CENTER_ALIGNMENT);
    button.setAlignmentX(JButton.RIGHT_ALIGNMENT);
    button.setFocusPainted(false);
    Dimension buttonDims = new Dimension(buttonSize, buttonSize);
    button.setPreferredSize(buttonDims);
    button.setMaximumSize(buttonDims);
    button.setMinimumSize(buttonDims);
    fg.add(button);
    add(fg, LABEL);
  }

  /**
   * Set the text in the label.
   * @param	text	The text to set.
   */
  public void setText(String text)
  {
    tf.setText(text);
  }

  /**
   * Make the button and query text visible.
   */
  public void showLabel()
  {
    showCard(LABEL);
  }

  /**
   * Hide the button and query text.
   */
  public void addActionListener(ActionListener l)
  {
    button.addActionListener(l);
  }

  /**
   * Add a listener to the button.
   * @param	l	The listener to add.
   */
  public void removeActionListener(ActionListener l)
  {
    button.removeActionListener(l);
  }

  /**
   * Remove a listener from the button.
   * @param	l	The listener to remove.
   */
  public void hideLabel()
  {
    showCard(NOLABEL);
  }

  /**
   * Change the background color of the label.
   * @param	color	The new background color.
   */
  public void recolor(Color color)
  {
    bg.setBackground(color);
    bg.repaint();
  }
}
