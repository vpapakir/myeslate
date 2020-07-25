package gr.cti.eslate.shapedComponent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.imageEditor.*;
import gr.cti.eslate.utils.*;

/**
 * Custom editor for a shaped component's "clip shape type" property.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ShapeTypeEditor extends PropertyEditorSupport
{
  /**
   * Used to implement <code>addPropertyChangeListener()</code> and
   * <code>removePropertyChangeListener()</code>.
   */
  private PropertyChangeSupport pcs;
  /**
   * Localized resources.
   */
  private static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.eslate.shapedComponent.ShapedComponentResource",
      Locale.getDefault()
    );
  /**
   * The name of the property being edited.
   */
  protected String propertyName = "ClipShape";
  /**
   * The shape type that is being edited.
   */
  private ShapeType st;
  /**
   * The component that is returned as the custom editor.
   */
  private JPanel editor;
  /**
   * The "shape type" combo box.
   */
  private JComboBox cb;
  /*
   * The "edit shape" button.
   */
  private JButton editButton;
  /**
   * The gap between the "shape type" combo box and the "edit shape" button.
   */
  private final static int GAP = 4;

  /**
   * Create a ShapeTypeEditor instance.
   */
  public ShapeTypeEditor()
  {
    super();

    pcs = new PropertyChangeSupport(this);

    cb = new JComboBox(
      new String[]
      {
        resources.getString("rectangle"),
        resources.getString("ellipse"),
        resources.getString("polygon"),
        resources.getString("freehand"),
      }
    );
    cb.setEditable(false);
    cb.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        comboAction();
      }
    });

    editButton = new NoBorderButton(resources.getString("edit"));
    editButton.setMargin(new Insets(0, 0, 0, 0));
    //Dimension buttonSize = new Dimension(80, 22);
    //editButton.setPreferredSize(buttonSize);
    //editButton.setMaximumSize(buttonSize);
    //editButton.setMinimumSize(buttonSize);
    editButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        buttonAction();
      }
    });

    editor = new JPanel();
    editor.setLayout(new BoxLayout(editor, BoxLayout.X_AXIS));
    editor.add(cb);
    editor.add(Box.createHorizontalStrut(GAP));
    editor.add(editButton);
  }

  /**
   * Change the shape type as a result of a selection from the combo box.
   */
  private void comboAction()
  {
    int ind = cb.getSelectedIndex();
    if (ind != st.type) {
      ShapeType oldSt = st;
      st = new ShapeType(ind, st.component);
      switch (ind) {
        case ShapeType.RECTANGLE:
        case ShapeType.ELLIPSE:
          showEditButton(false);
          break;
        case ShapeType.POLYGON:
        case ShapeType.FREEHAND:
          showEditButton(true);
          break;
      }
      pcs.firePropertyChange(propertyName, oldSt, st);
    }
  }

  /**
   * Edit the shape.
   */
  private void buttonAction()
  {
    //ShapeType oldSt = st;

    st = new ShapeType(st.type, st.component);

    ShapedComponent scomponent = st.component;
    Component component = (Component)scomponent;
    if (st.type == ShapeType.POLYGON) {
      PathDialog pd = new PathDialog(editButton, scomponent);
      if (pd.showDialog() == PathDialog.OK) {
        int xx[] = pd.getXCoords();
        int yy[] = pd.getYCoords();
        Polygon p = new Polygon(xx, yy, xx.length);
        scomponent.setClipShape(p);
      }
    }else{
      ImageEditorDialog ied = new ImageEditorDialog(
        (Frame)SwingUtilities.getAncestorOfClass(Frame.class, component)
      );
      int w = component.getWidth();
      int h = component.getHeight();
      BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics g = im.createGraphics();
/*
      if (scomponent instanceof PanelComponent) {
        PanelComponent panel = (PanelComponent)scomponent;
        boolean transp = panel.isTransparent();
        boolean grid = panel.isGridVisible();
        // We get weird results if the panel is transparent, so we disable
        // transparency before painting the panel into the image.
        if (transp) {
          panel.setTransparent(false);
        }
        // The alignment grid interferes with the "magic wand" selection tool,
        // so we hide that, as well.
        if (grid) {
          panel.setGridVisible(false);
        }
        panel.simplePrint(g);
        if (transp) {
          panel.setTransparent(true);
        }
        if (grid) {
          panel.setGridVisible(true);
        }
      }else{
        component.print(g);
      }
*/
      scomponent.simplePrint(g);
      g.dispose();
      ied.setImage(im);
      ImageEditor ie = ied.getImageEditor();
      ie.setSelectedArea(scomponent.getClipShape());
      ie.zoomOperation("fix", 1);
      ied.setVisible(true);
      boolean returnCode = ied.getReturnCode();
      if (returnCode) {
        Shape sh = ie.getSelectedArea();
        scomponent.setClipShape(sh);
      }
      ied.dispose();
    }
  }

  /**
   * Set or change the shape type.
   * @param     value   The new shape type.
   */
  public void setValue(Object value)
  {
    ShapeType oldSt = st;
    st = (ShapeType)value;
    if ((st.type == ShapeType.POLYGON) ||
        (st.type == ShapeType.FREEHAND)) {
      showEditButton(true);
    }else{
      showEditButton(false);
    }
    cb.setSelectedIndex(st.type);
    pcs.firePropertyChange(propertyName, oldSt, st);
  }

  /**
   * Specifies whether the edit button will be shown.
   * @param     show    True if yes, false if no.
   */
  private void showEditButton(boolean show)
  {
    editor.removeAll();
    editor.add(cb);
    if (show) {
      editor.add(Box.createHorizontalStrut(GAP));
      editor.add(editButton);
    }
    editor.revalidate();
  }

  /**
   * Return the current value of the shape type;
   * @return    The requested value.
   */
  public Object getValue()
  {
    return st;
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
