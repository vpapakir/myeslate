package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

import gr.cti.eslate.utils.*;

/**
 * Custom editor for the "visual group layout" property.
 * The editor takes the form of a button which, when pressed pops up a dialog
 * that allows the user to modify the layout of the tool bar's visual groups.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class VisualGroupEditor extends PropertyEditorSupport
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
    "gr.cti.eslate.eslateToolBar.VisualGroupEditorResource",
    Locale.getDefault()
  );
  /**
   * The name of the property being edited.
   */
  protected String propertyName = "VisualGroupLayout";
  /**
   * The visual group layout that is being edited.
   */
  private VisualGroupLayout vgLayout;
  /**
   * The button that is returned as the custom editor.
   */
  private NoBorderButton editButton;

  /**
   * Create a VisualGroupEditor instance.
   */
  public VisualGroupEditor()
  {
    super();

    pcs=new PropertyChangeSupport(this);

    editButton = new NoBorderButton(resources.getString("vgEditText"));
    editButton.setMargin(new Insets(0, 0, 0, 0));
    Dimension buttonSize = new Dimension(80, 22);
    editButton.setPreferredSize(buttonSize);
    editButton.setMaximumSize(buttonSize);
    editButton.setMinimumSize(buttonSize);
    editButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        VisualGroupLayout oldLayout;
        try {
          oldLayout = (VisualGroupLayout)(vgLayout.clone());
        } catch(CloneNotSupportedException cnse) {
          oldLayout = null;
        }
        VisualGroupDialog dialog = new VisualGroupDialog(editButton, vgLayout);
        int result = dialog.showDialog();
        switch (result) {
          case VisualGroupDialog.OK:
            vgLayout.toolBar.repaint();
            pcs.firePropertyChange(propertyName, oldLayout, vgLayout);
            break;
          case VisualGroupDialog.RESET_TO_DEFAULT:
            ESlateToolBar toolbar = vgLayout.toolBar;
            toolbar.repaint();
            pcs.firePropertyChange(
              propertyName, oldLayout, new VisualGroupLayout(toolbar)
            );
            break;
          case VisualGroupDialog.CANCEL:
          default:
            setValue(oldLayout);
            break;
          
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
    vgLayout = (VisualGroupLayout)value;
  }

  /**
   * Return the current value of the visual group layout.
   * @return    The requested value.
   */
  public Object getValue()
  {
    return vgLayout;
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
