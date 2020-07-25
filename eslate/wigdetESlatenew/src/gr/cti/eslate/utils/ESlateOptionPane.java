package gr.cti.eslate.utils;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import gr.cti.eslate.base.*;

/**
 * This class provides localized versions of some of the methods in class
 * JOptionPane.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateOptionPane
{
  private static ResourceBundle resources = null;

  /**
   * Brings up a dialog that displays a message using a default icon
   * determined by the messageType parameter.
   * @param     parentComponent Determines the Frame in which the dialog is
   *                            displayed. If null, or if the parentComponent
   *                            has no Frame, a default Frame is used.
   * @param     message         The Object to display.
   * @param     title           The title string for the dialog.
   * @param     messageType     The type of message to be displayed:
   *                            JOptionPane.ERROR_MESSAGE,
   *                            JOptionPane.INFORMATION_MESSAGE,
   *                            JOptionPane.WARNING_MESSAGE,
   *                            JOptionPane.QUESTION_MESSAGE, or
   *                            JOptionPane.PLAIN_MESSAGE.
   */
  public static void showMessageDialog(Component parentComponent,
                                       Object message, String title,
                                       int messageType)
  {
    showMessageDialog(parentComponent, message, title, messageType, null);
  }

  /**
   * Brings up a dialog that displays a message using a default icon
   * determined by the messageType parameter.
   * @param     parentComponent Determines the Frame in which the dialog is
   *                            displayed. If null, or if the parentComponent
   *                            has no Frame, a default Frame is used.
   * @param     message         The Object to display.
   * @param     title           The title string for the dialog.
   * @param     messageType     The type of message to be displayed:
   *                            JOptionPane.ERROR_MESSAGE,
   *                            JOptionPane.INFORMATION_MESSAGE,
   *                            JOptionPane.WARNING_MESSAGE,
   *                            JOptionPane.QUESTION_MESSAGE, or
   *                            JOptionPane.PLAIN_MESSAGE.
   * @param     icon            An icon to display in the dialog.
   */
  public static void showMessageDialog(Component parentComponent,
                                       Object message, String title,
                                       int messageType, Icon icon)
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.ESlateOptionPaneResource",
        ESlateMicroworld.getCurrentLocale());
    }
    Object[] okLabel = {resources.getString("ok")};
    JOptionPane pane =
      new JOptionPane(message, messageType, JOptionPane.OK_OPTION, icon,
                      okLabel, okLabel[0]);
    JDialog dialog = pane.createDialog(parentComponent, title);
    dialog.setVisible(true);
  }

  /**
   * Brings up a modal dialog where the number of choices is determined by the
   * optionType parameter, where the messageType parameter determines the icon
   * to display. The messageType parameter is primarily used to supply a
   * default icon from the look and feel. 
   * @param     parentComponent Determines the Frame in which the dialog is
   *                            displayed. If null, or if the parentComponent
   *                            has no Frame, a default Frame is used.
   * @param     message         The Object to display.
   * @param     title           The title string for the dialog.
   * @param     optionType      An int designating the options available on
   *                            the dialog: JOptionPane.YES_NO_OPTION,
   *                            JOptionPane.OK_CANCEL_OPTION, or
   *                            JOptionPane.YES_NO_CANCEL_OPTION.
   * @param     messageType     The type of message to be displayed:
   *                            JOptionPane.ERROR_MESSAGE,
   *                            JOptionPane.INFORMATION_MESSAGE,
   *                            JOptionPane.WARNING_MESSAGE,
   *                            JOptionPane.QUESTION_MESSAGE, or
   *                            JOptionPane.PLAIN_MESSAGE.
   * @return    An int indicating the option selected by the user.
   */
  public static int showConfirmDialog(Component parentComponent,
                                      Object message, String title,
                                      int optionType, int messageType)
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.ESlateOptionPaneResource",
        ESlateMicroworld.getCurrentLocale());
    }
    Object[] yesNoLabels = {
      resources.getString("yes"),
      resources.getString("no")
    };
    Object[] okCancelLabels = {
      resources.getString("ok"),
      resources.getString("cancel")
    };
    Object[] yesNoCancelLabels = {
      resources.getString("yes"),
      resources.getString("no"),
      resources.getString("cancel")
    };
    Object[] defaultLabels = {
      resources.getString("ok")
    };
    Object[] labels;
    switch (optionType) {
      case JOptionPane.YES_NO_OPTION:
        labels = yesNoLabels;
        break;
      case JOptionPane.OK_CANCEL_OPTION:
        labels = okCancelLabels;
        break;
      case JOptionPane.YES_NO_CANCEL_OPTION:
        labels = yesNoCancelLabels;
        break;
      default:
        labels = defaultLabels;
        break;
    }
    JOptionPane pane =
      new JOptionPane(message, messageType, optionType, null,
                      labels, labels[0]);
    JDialog dialog = pane.createDialog(parentComponent, title);
    dialog.setVisible(true);
    Object selectedValue = pane.getValue();
    if (selectedValue == null) {
      return JOptionPane.CLOSED_OPTION;
    }
    for (int counter = 0, maxCounter = labels.length;
        counter < maxCounter; counter++) {
      if (labels[counter].equals(selectedValue)) {
        int result;
        if (optionType == JOptionPane.OK_CANCEL_OPTION && counter == 1) {
          result = JOptionPane.CANCEL_OPTION;
        }else{
          result = counter;
        }
        return result;
      }
    }
    return JOptionPane.CLOSED_OPTION;
  }

  /**
   * Shows a question-message dialog requesting input from the user. The 
   * dialog uses the default frame, which usually means it is centered on the
   * screen. 
   * @param     message The Object to display.
   */
  public static String showInputDialog(Object message)
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.ESlateOptionPaneResource",
        ESlateMicroworld.getCurrentLocale());
    }
    return showInputDialog(null, message);
  }

  /**
   * Shows a question-message dialog requesting input from the user parented
   * to <CODE>parentComponent</CODE>. The dialog is displayed in the
   * Component's frame, and is usually positioned below the Component. 
   * @param     parentComponent The parent Component for the dialog.
   * @param     message         The Object to display.
   */
  public static String showInputDialog(Component parentComponent,
                                      Object message)
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.ESlateOptionPaneResource",
        ESlateMicroworld.getCurrentLocale());
    }
    return showInputDialog(parentComponent, message,
                           resources.getString("input"),
                           JOptionPane.QUESTION_MESSAGE);
  }

  /**
   * Shows a dialog requesting input from the user parented to
   * <CODE>parentComponent</CODE> with the dialog having the title
   * <CODE>title</CODE> and message type <CODE>messageType</CODE>.
   * @param     parentComponent The parent Component for the dialog.
   * @param     message         The Object to display.
   * @param     title           The String to display in the dialog title bar.
   * @param     messageType     The type of message that is to be displayed:
   *                            JOptionPane.ERROR_MESSAGE,
   *                            JOptionPane.INFORMATION_MESSAGE,
   *                            JOptionPane.WARNING_MESSAGE,
   *                            JOptionPane.QUESTION_MESSAGE, or
   *                            JOptionPane.PLAIN_MESSAGE.
   */
  public static String showInputDialog(Component parentComponent,
                                       Object message, String title,
                                       int messageType)
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.ESlateOptionPaneResource",
        ESlateMicroworld.getCurrentLocale());
    }
    return (String)showInputDialog(parentComponent, message, title,
                                   messageType, null, null, null);
  }

  /**
   * Prompts the user for input in a blocking dialog where the initial
   * selection, possible selections, and all other options can be specified.
   * The user will able to choose from <CODE>selectionValues</CODE>, where
   * null implies the user can input whatever they wish, usually by means of
   * a JTextField. <CODE>initialSelectionValue</CODE> is the initial value to
   * prompt the user with. It is up to the UI to decide how best to represent
   * the <CODE>selectionValues</CODE>, but usually a JComboBox, JList, or
   * JTextField will be used.
   * @param     parentComponent The parent Component for the dialog.
   * @param     message         The Object to display.
   * @param     title           The String to display in the dialog title bar.
   * @param     messageType     The type of message to be displayed:
   *                            JOptionPane.ERROR_MESSAGE,
   *                            JOptionPane.INFORMATION_MESSAGE,
   *                            JOptionPane.WARNING_MESSAGE,
   *                            JOptionPane.QUESTION_MESSAGE, or
   *                            JOptionPane.PLAIN_MESSAGE.
   * @param     icon            The Icon image to display.
   * @param     selectionValues An array of Objects that gives the possible
   *                            selections.
   * @param     initialSelectionValue   The value used to initialize the input
   *                                    field.
   * @return    User's input or null, meaning the user canceled the input.
   */
  public static Object showInputDialog(Component parentComponent,
                                       Object message, String title,
                                       int messageType, Icon icon,
                                       Object[] selectionValues,
                                       Object initialSelectionValue)
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.utils.ESlateOptionPaneResource",
        ESlateMicroworld.getCurrentLocale());
    }
    String ok = resources.getString("ok");
    String cancel = resources.getString("cancel");
    Object[] okCancelLabels = {ok, cancel};
    JOptionPane pane =
      new JOptionPane(message, messageType, JOptionPane.OK_CANCEL_OPTION, icon,
                      okCancelLabels, okCancelLabels[0]);
    pane.setWantsInput(true);
    pane.setSelectionValues(selectionValues);
    pane.setInitialSelectionValue(initialSelectionValue);
    JDialog dialog = pane.createDialog(parentComponent, title);
    pane.selectInitialValue();
    dialog.setVisible(true);
    Object value = pane.getValue();
    if (value == null || value == JOptionPane.UNINITIALIZED_VALUE) {
      return null;
    }else{
      if (value.equals(cancel)) {
        return null;
      }else{
        return pane.getInputValue();
      }
    }
  }

  /**
   * Brings up a dialog that displays a message using a default icon
   * determined by the messageType parameter. By pressing an "expand"
   * button on the dialog, an additional, expanded message will appear, which
   * can be copied to the clipboard by pressing the "copy" button that appears
   * when the expanded message is visible.
   * @param parentComponent     Determines the Frame in which the dialog is
   *                            displayed. If null, or if the parentComponent
   *                            has no Frame, a default Frame is used.
   * @param     message         The Object to display. To display the message
   *                            in multiple lines, separate each line with a
   *                            newline character (\n).
   * @param     expMessage      The expanded message to display. To display
   *                            the expanded message in multiple lines,
   *                            separate each line with a newline character
   *                            (\n).
   * @param     title           The title string for the dialog.
   * @param     messageType     The type of message to be displayed:
   *                            JOptionPane.ERROR_MESSAGE,
   *                            JOptionPane.INFORMATION_MESSAGE,
   *                            JOptionPane.WARNING_MESSAGE,
   *                            JOptionPane.QUESTION_MESSAGE, or
   *                            JOptionPane.PLAIN_MESSAGE.
   */
  public static void showDetailMessageDialog(
    Component parentComponent, String message, String expMessage,
    String title, int messageType)
  {
    JPanel mainPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel detailPanel = new JPanel();
    JTextField expField = new JTextField(expMessage);
    expField.setEditable(false);
    detailPanel.setLayout(new BorderLayout());
    detailPanel.add(expField, "Center");
    DetailButton copyButton = new DetailButton(
      "copy1.gif", "copy2.gif",
      null, null,
      Box.createHorizontalStrut(2), expField
    );
    copyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        JTextField expField = (JTextField)((DetailButton)e.getSource()).comp2;
        expField.selectAll();
        expField.copy();
        expField.select(0, 0);
      }
    });
    DetailButton expandButton = new DetailButton(
      "down1.gif", "down2.gif",
      mainPanel, detailPanel,
      copyButton, null
    );
    expandButton.addActionListener(new ActionListener() {
      private boolean showDetails;

      public void actionPerformed(ActionEvent e)
      {
        DetailButton expandButton = (DetailButton)e.getSource();
        JPanel mainPanel = expandButton.mainPanel;
        JPanel detailPanel = expandButton.detailPanel;
        if(!showDetails) {
          showDetails = true;
          mainPanel.add(detailPanel);
          expandButton.setIcon(new
          ImageIcon(getClass().getResource("up1.gif")));
          expandButton.setPressedIcon(new
          ImageIcon(getClass().getResource("up2.gif")));
          JPanel buttonPanel = (JPanel)((JButton)e.getSource()).getParent();
          buttonPanel.add(((DetailButton)expandButton.comp1).comp1);
          buttonPanel.add(expandButton.comp1);
        }else{
          showDetails = false;
          mainPanel.remove(detailPanel);
          expandButton.setIcon(
            new ImageIcon(getClass().getResource("down1.gif"))
          );
          expandButton.setPressedIcon(
            new ImageIcon(getClass().getResource("down2.gif"))
          );
          JPanel buttonPanel = (JPanel)((JButton)e.getSource()).getParent();
          buttonPanel.remove(expandButton.comp1);
          buttonPanel.remove(((DetailButton)expandButton.comp1).comp1);
        }
        Window window =
        SwingUtilities.windowForComponent((Component)e.getSource());
        window.setSize(window.getPreferredSize());
      }
    });
    StringTokenizer st = new StringTokenizer(message, "\n");
    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    while (st.hasMoreTokens()) {
      messagePanel.add(new JLabel(st.nextToken()));
    }
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(messagePanel);
    buttonPanel.add(Box.createHorizontalStrut(16));
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(expandButton);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(buttonPanel);
    ESlateOptionPane.showMessageDialog(
      parentComponent, mainPanel, title, messageType
    );
  }
}
