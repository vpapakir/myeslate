package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import gr.cti.eslate.utils.*;
import gr.cti.eslate.eslateButton.*;
import gr.cti.eslate.eslateCheckBox.*;
import gr.cti.eslate.eslateComboBox.*;
import gr.cti.eslate.eslateRadioButton.*;
import gr.cti.eslate.eslateSlider.*;
import gr.cti.eslate.spinButton.*;
import gr.cti.eslate.eslateToggleButton.*;
import gr.cti.eslate.eslateTextField.*;

/**
 * This class implements the dialog that allows the user to select the class
 * of the component that they want to add to the E-Slate toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 */
class ClassDialog extends JDialog
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
   * The combo box presented in the dialog.
   */
  private JComboBox cb = null;
  /**
   * The class selected by the user.
   */
  Class<?> selectedClass = null;
  /**
   * The name of the class selected by the user.
   */
  String selectedClassName = null;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources = VisualGroupEditor.resources;

  /**
   * Build the dialog
   * @param     owner           The owner of the dialog.
   */
  ClassDialog(Component owner)
  {
    super(
      (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, owner)),
      resources.getString("classDialogTitle"),
      true
    );

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));
    topPanel.add(new JLabel(resources.getString("className")));
    topPanel.add(Box.createHorizontalStrut(4));
    ClassLabel[] labels = new ClassLabel[] {
      new ClassLabel(ESlateButton.class, resources.getString("button")),
      new ClassLabel(ESlateCheckBox.class, resources.getString("checkBox")),
      new ClassLabel(ESlateComboBox.class, resources.getString("comboBox")),
      new ClassLabel(
        ESlateRadioButton.class, resources.getString("radioButton")
      ),
      new ClassLabel(ESlateSlider.class, resources.getString("slider")),
      new ClassLabel(
        SpinButton.class, resources.getString("spinButton")
      ),
      new ClassLabel(
        ESlateToggleButton.class, resources.getString("toggleButton")
      ),
      new ClassLabel(ESlateTextField.class, resources.getString("textField")),
    };
    Arrays.sort(labels);
    cb = new ClassComboBox(labels);
    cb.setSelectedItem(cb.getItemAt(0));
    topPanel.add(cb);

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
    cb = null;
  }

  /**
   * Fills in the selected class and selecetd class name.
   */
  private void getSelection()
  {
    Object o = cb.getSelectedItem();
    if (o instanceof ClassLabel) {
      selectedClass = ((ClassLabel)o).cls;
      selectedClassName = ((ClassLabel)o).getText();
    }else{
      String name = o.toString();
      int n = cb.getItemCount();
      // Try matching the text with the text of one of the labels.
      for (int i=0; i<n; i++) {
        ClassLabel lab = (ClassLabel)(cb.getItemAt(i));
        String labText = lab.getText();
        if (labText.equals(name)) {
          selectedClass = lab.cls;
          selectedClassName = labText;
          return;
        }
      }
      // If matching fails, check if this is a valid class name.
      try {
        selectedClass = Class.forName(name);
        selectedClassName = ClassNameString.getClassNameStringWOPackage(name);
      } catch (ClassNotFoundException cnfe) {
        selectedClass = null;
        selectedClassName = name;
      }
    }
  }

}
