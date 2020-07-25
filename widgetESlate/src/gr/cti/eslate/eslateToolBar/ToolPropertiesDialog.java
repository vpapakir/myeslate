package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * This class implements the dialog that allows the user to change the
 * properties of a tool.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class ToolPropertiesDialog extends JDialog
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Indicates that the user has pressed the "OK" button.
   */
  final static int OK = 0;
  /**
   * Indicates that the user has canceled the dialog.
   */
  final static int CANCEL = 1;
  /**
   * The result returned by the dialog.
   */
  private int result = CANCEL;
  /**
   * The "OK" button of the dialog.
   */
  private JButton okButton;
  /**
   * The information describing the tool.
   */
  private VisualGroupEditorToolInfo info;
  /**
   * The dialog's check box.
   */
  private JCheckBox cBox;
  /**
   * The dialog's text field.
   */
  private JTextField textField;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources = VisualGroupEditor.resources;

  /**
   * Build the dialog
   * @param     owner   The owner of the dialog.
   * @param     info    The information decsribing the tool.
   */
  ToolPropertiesDialog(Component owner, VisualGroupEditorToolInfo info)
  {
    super(
      (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, owner)),
      resources.getString("toolPropDialogTitle"),
      true
    );

    this.info = info;

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));

    JPanel cBoxPanel = new JPanel();
    cBoxPanel.setLayout(new BoxLayout(cBoxPanel, BoxLayout.X_AXIS));
    cBox = new JCheckBox(resources.getString("toolVisible"), info.visible);
    cBoxPanel.add(cBox);
    cBoxPanel.add(Box.createHorizontalGlue());

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
    textPanel.add(Box.createHorizontalGlue());
    textPanel.add(new JLabel(resources.getString("text")));
    textPanel.add(Box.createHorizontalStrut(4));
    textField = new JTextField(info.text);
    Dimension prefSize = textField.getPreferredSize();
    Dimension maxSize = textField.getMaximumSize();
    int minWidth = 200;
    textField.setMaximumSize(new Dimension(maxSize.width, prefSize.height));
    textField.setPreferredSize(new Dimension(
      Math.max(prefSize.width, minWidth), prefSize.height
    ));
    textPanel.add(textField);
    textPanel.add(Box.createHorizontalGlue());

    topPanel.add(cBoxPanel);
    topPanel.add(Box.createVerticalStrut(4));
    topPanel.add(textPanel);

    Dimension buttonSize = new Dimension(90, 25);
    //Color buttonTextColor = new Color(0, 0, 128);
    Insets buttonInsets = new Insets(0, 0, 0, 0);
    okButton = new JButton(resources.getString("ok"));
    okButton.setMargin(buttonInsets);
    okButton.setPreferredSize(buttonSize);
    okButton.setMaximumSize(buttonSize);
    okButton.setMinimumSize(buttonSize);
    //okButton.setForeground(buttonTextColor);
    okButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        getSelection();
        result = OK;
        setVisible(false);
      }
    });
    JButton cancelButton = new JButton(resources.getString("cancel"));
    cancelButton.setMargin(buttonInsets);
    cancelButton.setPreferredSize(buttonSize);
    cancelButton.setMaximumSize(buttonSize);
    cancelButton.setMinimumSize(buttonSize);
    //cancelButton.setForeground(buttonTextColor);
    cancelButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        result = CANCEL;
        setVisible(false);
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createGlue());
    buttonPanel.add(okButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(cancelButton);
    buttonPanel.add(Box.createGlue());

    JPanel mainPanel = new JPanel(true);
    mainPanel.setLayout(new BoxLayout(mainPanel, 1));
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(topPanel);
    mainPanel.add(buttonPanel);

    getContentPane().add(mainPanel);
    pack();
    //setSize(new Dimension(320, 452));
    center(owner);
    addWindowListener(new WindowAdapter() {
      boolean gotFocus = false;

      public void windowActivated(WindowEvent we)
      {
        if (!gotFocus) {
          // Set the focus on the "OK" button...
          okButton.requestFocus();
          // ... and ensure that the button will be activated when
          // hitting <ENTER>.
          JRootPane root = SwingUtilities.getRootPane(okButton);
          if (root != null) {
            root.setDefaultButton(okButton);
          }
          gotFocus = true;
        }else{
        }
      }
    });
  }

  /**
   * Centers the dialog relative to its owner.
   * @param     owner   The owner of the dialog. If it is <null>code</code>,
   *                    the dialog will be placed in the center of the screen.
   */
  private void center(Component owner)
  {
    Dimension s = getSize();
    int width = s.width;
    int height = s.height;
    int x, y;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    if (owner == null || !owner.isVisible()) {
      x = (screenWidth - width) / 2;
      y = (screenHeight - height) / 2;
    }else{
      Dimension ownerSize = owner.getSize();
      int ownerWidth = ownerSize.width;
      int ownerHeight = ownerSize.height;
      Point ownerLocation = owner.getLocationOnScreen();
      x = ownerLocation.x + (ownerWidth - width) / 2;
      y = ownerLocation.y + (ownerHeight - height) / 2;
      if ((x + width) > screenWidth) {
        x = screenWidth - width;
      }
      if ((y + height) > screenHeight) {
        y = screenHeight - height;
      }
      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }
    }
    setLocation(x, y);
  }

  /**
   * Displays the dialog.
   * @return    If the user presses the "OK" button, <code>OK</code> is
   *            returned, otherwise <code>CANCEL</code> is returned.
   */
  int showDialog()
  {
    setVisible(true);
    dispose();
    return result;
  }

  /**
   * Dispose the dialog.
   */
  public void dispose()
  {
    super.dispose();
    cBox = null;
    textField = null;
  }

  /**
   * Fills in the selected class and selected class name.
   */
  private void getSelection()
  {
    info.visible = cBox.isSelected();
    String text = textField.getText();
    if ((text != null) && text.equals("")) {
      text = null;
    }
    info.text = text;
  }

}
