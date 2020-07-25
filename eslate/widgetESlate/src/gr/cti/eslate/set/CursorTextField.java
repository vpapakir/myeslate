package gr.cti.eslate.set;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This class implements a text field where the user can position a cursor and
 * navigate with the arrow keys, as in an editable text field, but cannot
 * modify its contents.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 */
public class CursorTextField extends JTextField
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a field.
   */
  public CursorTextField()
  {
    super();
    setEditable(true);
    setBackground(UIManager.getColor("textInactiveText"));
    // Install a new keymap with all the keystrokes that can alter a text
    // field's text disabled.
    Keymap oldKeymap = getKeymap();
    Keymap newKeymap = addKeymap("noedit", oldKeymap);
    Action nullAction = new AbstractAction()
    {
      /**
       * Serialization version.
       */
      final static long serialVersionUID = 1L;
      
      public void actionPerformed(ActionEvent e)
      {
      }
    };
    newKeymap.setDefaultAction(nullAction);
    newKeymap.addActionForKeyStroke(KeyStroke.getKeyStroke('\b'), nullAction);
    newKeymap.addActionForKeyStroke(
      KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), nullAction
    );
    newKeymap.addActionForKeyStroke(
      KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), nullAction
    );
    newKeymap.addActionForKeyStroke(
      KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK), nullAction
    );
    newKeymap.addActionForKeyStroke(
      KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK), nullAction
    );
    newKeymap.addActionForKeyStroke(
      KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_MASK),
      nullAction
    );
    newKeymap.addActionForKeyStroke(
      KeyStroke.getKeyStroke(KeyEvent.VK_PASTE, 0), nullAction
    );
    setKeymap(newKeymap);
  }
}
