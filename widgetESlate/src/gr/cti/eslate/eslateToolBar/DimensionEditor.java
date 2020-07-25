package gr.cti.eslate.eslateToolBar;

import gr.cti.eslate.spinButton.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import com.zookitec.layout.*;

/**
 * Custom editor for "dimension" properties, where <code>null</code> is an
 * accepted value.
 * The editor takes the form of two spin buttons (width and height), and a
 * check box. When the check box is checked, the value of the dimension is
 * <code>null</code>, otherwise it is the value specified by the spin boxes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 31-May-2006
 */
public class DimensionEditor extends PropertyEditorSupport
{
  /**
   * Used to implement <code>addPropertyChangeListener()</code> and
   * <code>removePropertyChangeListener()</code>.
   */
  private PropertyChangeSupport pcs;
  /**
   * The separation being edited.
   */
  private Dimension separation;
  /**
   * The editor panel.
   */
  private JPanel editor;
  /**
   * The spin button for the separation width.
   */
  private SpinButton spin1;
  /**
   * The spin button for the separation height.
   */
  private SpinButton spin2;
  /**
   * The text for the separation width.
   */
  private JLabel label1;
  /**
   * The text for the separation height
   */
  private JLabel label2;
  /**
   * The default separation check box.
   */
  private JCheckBox cb;
  /**
   * Localized resources.
   */
  protected ResourceBundle resources;
  /**
   * The name of the property being edited.
   */
  protected String propertyName = "Separation";

  /**
   * Create a SeparationEditor instance.
   */
  public DimensionEditor()
  {
    super();

    initResources();

    pcs=new PropertyChangeSupport(this);

    editor = new JPanel();
    ExplicitLayout layout = new ExplicitLayout();
    editor.setLayout(layout);

    label1 = new JLabel(resources.getString("horizontal"));
    label2 = new JLabel(resources.getString("vertical"));

    spin1 = new SpinButton(0, 0, Integer.MAX_VALUE);
    Dimension d = spin1.getPreferredSize();
    spin1.setPreferredSize(new Dimension(100, d.height));
    spin1.addValueChangedListener(new ValueChangedListener(){
      public void valueChanged(ValueChangedEvent e)
      {
        int w = ((Number)(spin1.getValue())).intValue();
        int h = ((Number)(spin2.getValue())).intValue();
        Dimension oldSeparation = separation;
        Dimension newSeparation = new Dimension(w, h);
        pcs.firePropertyChange(propertyName, oldSeparation, newSeparation);
      }
    });

    spin2 = new SpinButton(0, 0, Integer.MAX_VALUE);
    d = spin2.getPreferredSize();
    spin2.setPreferredSize(new Dimension(100, d.height));
    spin2.addValueChangedListener(new ValueChangedListener() {
      public void valueChanged(ValueChangedEvent e)
      {
        int w = ((Number)(spin1.getValue())).intValue();
        int h = ((Number)(spin2.getValue())).intValue();
        Dimension oldSeparation = separation;
        Dimension newSeparation = new Dimension(w, h);
        pcs.firePropertyChange(propertyName, oldSeparation, newSeparation);
      }
    });

    cb = new JCheckBox(resources.getString("default"));
    cb.setFocusPainted(false);
    cb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        if (cb.isSelected()) {
          Dimension oldSeparation = separation;
          Dimension newSeparation = null;
          pcs.firePropertyChange(propertyName, oldSeparation, newSeparation);
          enableDimensions(false);
        }else{
          Dimension oldSeparation = null;
          int w = ((Number)(spin1.getValue())).intValue();
          int h = ((Number)(spin2.getValue())).intValue();
          Dimension newSeparation = new Dimension(w, h);
          pcs.firePropertyChange(propertyName, oldSeparation, newSeparation);
          enableDimensions(true);
        }
      }
    });

    //
    // Lay out the editor as follows:
    //
    // label1 spin1
    //              cb
    // label2 spin2
    //
    ExplicitConstraints ec;
    Component[] group;
    int spinWidth = 50;
    // If we don't this, then we'll get an exception when computing the size
    // of the components in the editor panel.
    d = editor.getPreferredSize();

    ec = new ExplicitConstraints(label1);
    ec.setWidthZeroIfInvisible(true);
    ec.setHeightZeroIfInvisible(false);
    ec.setOriginX(ExplicitConstraints.LEFT);
    ec.setOriginY(ExplicitConstraints.CENTER);
    ec.setX(ContainerEF.left(editor));
    ec.setY(ComponentEF.yFraction(spin1, 0.5));
    editor.add(label1, ec);

    ec = new ExplicitConstraints(spin1);
    ec.setWidthZeroIfInvisible(true);
    ec.setHeightZeroIfInvisible(false);
    ec.setWidth(MathEF.constant(spinWidth));
    group = new Component[]{label1, label2};
    ec.setX(MathEF.add(GroupEF.right(group), 4));
    ec.setY(ContainerEF.top(editor));
    editor.add(spin1, ec);

    ec = new ExplicitConstraints(label2);
    ec.setWidthZeroIfInvisible(true);
    ec.setHeightZeroIfInvisible(false);
    ec.setOriginX(ExplicitConstraints.LEFT);
    ec.setOriginY(ExplicitConstraints.CENTER);
    ec.setX(ContainerEF.left(editor));
    ec.setY(ComponentEF.yFraction(spin2, 0.5));
    editor.add(label2, ec);

    ec = new ExplicitConstraints(spin2);
    ec.setWidthZeroIfInvisible(true);
    ec.setHeightZeroIfInvisible(false);
    ec.setWidth(MathEF.constant(spinWidth));
    ec.setX(ComponentEF.left(spin1));
    ec.setY(MathEF.add(ComponentEF.bottom(spin1), 4));
    editor.add(spin2, ec);

    ec = new ExplicitConstraints(cb);
    group = new Component[]{spin1, spin2};
    ec.setOriginX(ExplicitConstraints.LEFT);
    ec.setOriginY(ExplicitConstraints.CENTER);
    ec.setX(MathEF.add(ComponentEF.right(spin2), 4));
    ec.setY(GroupEF.yFraction(group, 0.5));
    editor.add(cb, ec);

    // If we don't do this, the editor panel will have a preferred size of
    // (100, 100), which is too high.
    group = editor.getComponents();
    int w = (int)(GroupEF.width(group).getValue(layout));
    int h = (int)(GroupEF.height(group).getValue(layout));
    editor.setPreferredSize(new Dimension(w, h));
  }

  /**
   * Initialize resources.
   */
  protected void initResources()
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.eslateToolBar.DimensionEditorResource",
      Locale.getDefault()
    );
  }

  /**
   * Enables or disables the components that allow the user to enter explicit
   * values for the separation.
   * @param     enable  True to enable, false to disable.
   */
  private void enableDimensions(boolean enable)
  {
    label1.setEnabled(enable);
    label2.setEnabled(enable);
    spin1.setEnabled(enable);
    spin2.setEnabled(enable);
  }

  /**
   * Set or change the separation that is to be edited.
   * @param     value   The new value of the separation.
   */
  public void setValue(Object value)
  {
    if (value == null) {
      enableDimensions(false);
      separation = null;
      cb.setSelected(true);
    }else{
      if (value instanceof Dimension) {
        enableDimensions(true);
        cb.setSelected(false);
        Dimension newSep = (Dimension)value;
        spin1.setValue(new Integer(newSep.width));
        spin2.setValue(new Integer(newSep.height));
        separation = newSep;
      }
    }
  }

  /**
   * Return the current value of the property.
   * @return    The requested value.
   */
  public Object getValue()
  {
    return separation;
  }

  /**
   * Returns the java.awt.Component that will allow a human to directly edit
   * the current property value.
   * @return    The requested component.
   */
  public Component getCustomEditor()
  {
    return editor;
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
