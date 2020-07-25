package gr.cti.eslate.utils;

import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

import javax.swing.*;

/**
 * Pops up a window containing a message and a button that makes the window go
 * away when pressed (a poor man's dialog box). When the button is hit, the
 * window is removed and disposed.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class MessageFrame extends AuxFrame
                         implements ActionListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private MessageFrame(String title, String message, String buttonText) {

    super(title, new Dimension (100, 100));

    //Font messageFont = new Font("Helvetica", Font.PLAIN, 12);
    Font messageFont = getFont();
    FontMetrics fm = getFontMetrics(messageFont);

    int hspace = 20;
    int vspace = 40;
    int vdist = 20;
    int width =
      Math.max(fm.stringWidth(message),
               fm.stringWidth(buttonText)) + 2 * hspace;
    int height = fm.getHeight() + 2 * vspace + vdist;
    Dimension size = new Dimension(width, height);
    setSize(size);

    JTextField messageField = new JTextField(message);
    messageField.setEditable(false);
    messageField.setMaximumSize(size);

    JButton button = new JButton(buttonText);
    button.setActionCommand("ButtonPressed");
    button.addActionListener(this);
    button.setFocusPainted(false);

    Box col = Box.createVerticalBox();
    col.add(messageField);
    col.add(Box.createVerticalStrut(vdist));
    col.add(button);

    JPanel panel = new JPanel(true);
    //panel.setFont(messageFont);
    panel.setSize(size);
    panel.setMinimumSize(size);
    panel.setMaximumSize(size);
    panel.add(col);

    add("Center", panel);
    pack();
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();

    if (cmd.equals("ButtonPressed")){
      setVisible(false);
      dispose();
    }
  }

 /**
  * Creates and displays a dialog box with a given title, message, and
  * button text.
  * @param      title           The title of the dialog box.
  * @param      message         The message displayed in the dialog box.
  * @param      buttonText      The text on the button.
  */
 public static void show(String title, String message, String buttonText) {
   new MessageFrame(title, message, buttonText);
 }

 /**
  * Creates and displays an error dialog box, with a given message.
  * The title of the dialog box is "Error", and the button text is "OK".
  * @param      message         The message displayed in the dialog box.
  */
 public static void error(String message) {
   show("Error", message, "OK");
 }

 /**
  * Creates and displays a warning dialog box, with a given message.
  * The title of the dialog box is "Warning", and the button text is "OK".
  * @param      message         The message displayed in the dialog box.
  */
 public static void warning(String message) {
   show("Warning", message, "OK");
 }

}
