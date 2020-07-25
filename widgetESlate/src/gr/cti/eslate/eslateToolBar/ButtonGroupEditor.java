package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

import gr.cti.eslate.utils.*;

/**
 * Custom editor for the "button group layout" property.
 * The editor takes the form of a button which, when pressed pops up a dialog
 * that allows the user to modify the layout of the tool bar's button groups.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class ButtonGroupEditor extends PropertyEditorSupport
{
  /**
   * Used to implement <code>addPropertyChangeListener()</code> and
   * <code>removePropertyChangeListener()</code>.
   */
  private PropertyChangeSupport pcs;
  /**
   * Localized resources.
   */
  static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.eslateToolBar.ButtonGroupEditorResource",
    Locale.getDefault()
  );
  /**
   * The name of the property being edited.
   */
  protected String propertyName = "ButtonGroupLayout";
  /**
   * The visual group layout that is being edited.
   */
  private ButtonGroupLayout bgLayout;
  /**
   * The button that is returned as the custom editor.
   */
  private NoBorderButton editButton;

  /**
   * Create a ButtonGroupEditor instance.
   */
  public ButtonGroupEditor()
  {
    super();

    pcs=new PropertyChangeSupport(this);

    editButton = new NoBorderButton(resources.getString("bgEditText"));
    editButton.setMargin(new Insets(0, 0, 0, 0));
    Dimension buttonSize = new Dimension(80, 22);
    editButton.setPreferredSize(buttonSize);
    editButton.setMaximumSize(buttonSize);
    editButton.setMinimumSize(buttonSize);
    editButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        // bgLayout contains the tools that were present in the toolbar when
        // the component properties were first displayed in the component
        // editor, so it may be out of date.
        // Get an updated button group layout from the toolbar, so that it
        // contains any new tools that were added to the toolbar.
        ButtonGroupLayout oldLayout = bgLayout.toolBar.getButtonGroupLayout();
        ButtonGroupLayout newLayout;
        try {
          newLayout = (ButtonGroupLayout)(oldLayout.clone());
        } catch(CloneNotSupportedException cnse) {
          newLayout = null;
        }
        ButtonGroupDialog dialog = new ButtonGroupDialog(editButton, newLayout);
        int result = dialog.showDialog();
        if (result != ButtonGroupDialog.CANCEL) {
          bgLayout.toolBar.repaint();
          setValue(newLayout);
          pcs.firePropertyChange(propertyName, oldLayout, bgLayout);
        }else{
          setValue(oldLayout);
        }
      }
    });
  }

  /**
   * Set or change the visual group layout.
   * @param     value   The new visual group layout.
   */
  public void setValue(Object value)
  {
    bgLayout = (ButtonGroupLayout)value;
  }

  /**
   * Return the current value of the visual group layout.
   * @return    The requested value.
   */
  public Object getValue()
  {
    return bgLayout;
  }

  /**
   * Returns the java.awt.Component that will allow a human to directly edit
   * the current property value.
   * @return    The requested component.
   */
  public Component getCustomEditor()
  {
    return editButton;
  }

  /**
   * Determines whether the propertyEditor can provide a custom editor.
   * @return    True.
   */
  public boolean supportsCustomEditor()
  {
    return true;
  }

  /**
   * Register a listener for the PropertyChange event. The class will fire a
   * PropertyChange value whenever the value is updated.
   * @param     l       An object to be invoked when a PropertyChange event is
   *                    fired.
   */
  public synchronized void addPropertyChangeListener(PropertyChangeListener l)
  {
    pcs.addPropertyChangeListener(l);
  }

  /**
   * Remove a listener for the PropertyChange event.
   * @param     l       The PropertyChange listener to be removed.
   */
  public synchronized void removePropertyChangeListener(
    PropertyChangeListener l)
  {
    pcs.removePropertyChangeListener(l);
  }
}
