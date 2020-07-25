package gr.cti.eslate.base.help;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.utils.browser.*;

/**
 * This class implements the help viewer's "find" dialog.
 *
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
class FindDialog extends JDialog
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private ESlateBrowser localBrowser;
  private Container frame;
  private JPanel buttonPanel;
  private JButton cancelButton;
  private JButton findNextButton;
  private JPanel textPanel;
  private JLabel findLabel;

  protected JTextField textField;
  protected int i;
  protected ResourceBundle info =
    ResourceBundle.getBundle(
      "gr.cti.eslate.base.help.MessageBundle",
      ESlateMicroworld.getCurrentLocale()
    );

  public FindDialog(ESlateBrowser passedBrowser)
  {
    super(
      (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, passedBrowser))
    );
    localBrowser = passedBrowser;
    setAppearance();
    assignListeners();

    setTitle(info.getString("dialogTitle"));
    setLocationRelativeTo(localBrowser);
    setResizable(false);
    setSize(300, 100);
    setModal(true);
    setVisible(true);

    addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        dispose();
      }
    });
  }

  private void setAppearance()
  {
    buttonPanel = new JPanel();
    cancelButton = new JButton(info.getString("cancel"));
    cancelButton.setToolTipText(info.getString("cancelTip"));
    findNextButton = new JButton(info.getString("find"));
    findNextButton.setToolTipText(info.getString("findTip"));
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(findNextButton);
    buttonPanel.add(cancelButton);

    textPanel = new JPanel();
    textField = new JTextField();
    textField.setColumns(1);
    findLabel = new JLabel(info.getString("Search"));
    findLabel.setLabelFor(textField);
    textPanel.setLayout(new BorderLayout());
    textPanel.add("West", findLabel);
    textPanel.add("South", textField);

    frame = getContentPane();
    frame.setLayout(new GridLayout(2, 1));
    frame.add(textPanel);
    frame.add(buttonPanel);
    frame.setVisible(true);
  }

  private void assignListeners()
  {
    cancelButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event)
      {
        dispose();
      }
    });

    findNextButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event)
      {
        String wordToSearch = textField.getText();
        if (i != 0) {
          i = localBrowser.search(i, wordToSearch, false);
        }else{
          i = localBrowser.search(0, wordToSearch, false);
        }
      }
    });

    textField.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event)
      {
        String wordToSearch = textField.getText();
        i = localBrowser.search(0, wordToSearch, false);
      }
    });
  }
}
