package gr.cti.eslate.panel;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * This class implements the content pane of the panel component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class ContentPane extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;

  boolean initted = false;
  private Icon bgImage = null;
  private boolean transparent = false;
  private int bgStyle = PanelComponent.BG_CENTERED;
  private boolean gridVisible = false;
  private int gridStep = 20;
  private Color gridColor = Color.black;
  boolean ignoreContainerEvents = false;
  HashMap<Component, Object> constraints = new HashMap<Component, Object>();

  /**
   * Construct the content pane.
   */
  ContentPane()
  {
    super();
    setOpaque(false);
  }

  /**
   * Sets a new layout manager.
   * @param     mgr     The new layout manager.
   */
  public void setLayout(LayoutManager mgr)
  {
    if (!initted) {
      super.setLayout(mgr);
    }else{
      // Ensure that all the components are visible, as the previous layout
      // manager (e.g., CardLayout) may have rendered them invisible for its
      // own evil purposes.
      PanelComponent parent = getHostingPanelComponent();
      if (parent != null) {
        Component[] compos = parent.getHostedComponents();
        int nComponents = compos.length;
        for (int i=0; i<nComponents; i++) {
          compos[i].setVisible(true);
        }
      }

      // Set the new layout manager.
      if (mgr instanceof BorderLayout) {
      try {
        // If we had previously used a border layout manager, remove
        // everything and put them back using the old constraints, which
        // are still available.
        Component[] comp = getComponents();
        int n = comp.length;
        ignoreContainerEvents = true;
        super.removeAll();
        super.setLayout(mgr);
        for (int i=0; i<n; i++) {
          Component c = comp[i];
          Object constr = constraints.get(c);
          if (constr != null) {
            super.add(c, constr);
          }else{
            super.add(c);
          }
        }
        ignoreContainerEvents = false;
      } catch (Exception e) {
        e.printStackTrace();
      }
      }else{
        super.setLayout(mgr);
      }

      // Make sure that the component has been laid out correctly using the
      // new layout manager.
      revalidate();
    }
  }
  
  /**
   * Returns the instance of PanelComponent hosting this component.
   * @return    The requested component. If no PanelComponent is hosting this
   *            component, this method returns null.
   */
  public PanelComponent getHostingPanelComponent()
  {
    Component parent = this;
    while ((parent != null) && !(parent instanceof PanelComponent)) {
      parent = parent.getParent();
    }
    return (PanelComponent)parent;
  }

  /**
   * Frees any resources held by the panel.
   */
  void dispose()
  {
    bgImage = null;
    constraints.clear();
    constraints = null;
  }

  /**
   * Sets the background image.
   * @param     image   The image to set. If image is null, the panel will
   *                    have no background image.
   */
  public void setBackgroundImage(Icon image)
  {
    bgImage = image;
  }

  /**
   * Returns the background image.
   * @return    The requested image or null if the panel has no background
   * image.
   */
  public Icon getBackgroundImage()
  {
    return bgImage;
  }

  /**
   * Sets the style of the background image.
   * @param     style   One of PanelComponent.BG_NONE,
   *                    PanelComponent.BG_CENTERED,
   *                    PanelComponent.BG_STRETCHED, or
   *                    PanelComponent.BG_TILED.
   *                    Other values are treated as equivalent to
   *                    PanelComponent.BG_NONE.
   */
  public void setBackgroundImageStyle(int style)
  {
    switch (style) {
      case PanelComponent.BG_NONE:
      case PanelComponent.BG_CENTERED:
      case PanelComponent.BG_STRETCHED:
      case PanelComponent.BG_TILED:
        bgStyle = style;
        break;
      default:
        bgStyle = PanelComponent.BG_NONE;
    }
  }

  /**
   * Returns the style of the background image.
   * @return    One of PanelComponent.BG_NONE, PanelComponent.BG_CENTERED,
   *            PanelComponent.BG_STRETCHED, or PanelComponent.BG_TILED.
   */
  public int getBackgroundImageStyle()
  {
    return bgStyle;
  }

  /**
   * Specifies whether the pane is transparent
   * @param     status  True if yes, false if no.
   */
  public void setTransparent(boolean status)
  {
    transparent = status;
  }

  /**
   * Checks whether the panel is transparent
   * @return    True if yes, false if no.
   */
  public boolean isTransparent()
  {
    return transparent;
  }

  /**
   * Specifies whether the alignment grid is visible.
   * @param     status  True if yes, false if no.
   */
  public void setGridVisible(boolean status)
  {
    gridVisible = status;
  }

  /**
   * Checks whether the alignment grid is visible.
   * @return    True if yes, false if no.
   */
  public boolean isGridVisible()
  {
    return gridVisible;
  }

  /**
   * Sets the step of the alignment grid.
   * @param     step    The step of the alignment grid. If the step is less
   *                    than or equal to 0, the step is set to 1.
   */
  public void setGridStep(int step)
  {
    if (step <= 0) {
      step = 1;
    }
    gridStep = step;
  }

  /**
   * Returns the step of the alignment grid.
   * @return    The requested value.
   */
  public int getGridStep()
  {
    return gridStep;
  }

  /**
   * Sets the color of the alignment grid.
   * @param     color   The color of the alignment grid.
   */
  public void setGridColor(Color color)
  {
    gridColor = color;
  }

  /**
   * Returns the color of the alignment grid.
   * @return    The requested color.
   */
  public Color getGridColor()
  {
    return gridColor;
  }

  /**
   * Paints the component.
   * @param     g       The graphics context in which to paint the component.
   */
  public void paint(Graphics g)
  {
    int w = getWidth();
    int h = getHeight();
    if ((bgImage != null) /*&& !transparent*/) {
      paintBackgroundImage(g);
    }
    if ((bgImage == null) && !transparent) {
      g.setColor(getBackground());
      g.fillRect(0, 0, w, h);
    }
    if (gridVisible) {
      paintGrid(g);
    }
    super.paint(g);
  }

  /**
   * Paints the background image.
   * @param     g       Graphics context in which to paint the background
   *                    image.
   */
  private void paintBackgroundImage(Graphics g)
  {
    int imw = bgImage.getIconWidth();
    int imh = bgImage.getIconHeight();
    int w = getWidth();
    int h = getHeight();
    switch (bgStyle) {
      case PanelComponent.BG_TILED:
        for (int y=0; y<h; y+=imh) {
          for (int x=0; x<w; x+=imw) {
            bgImage.paintIcon(this, g, x, y);
          }
        }
        break;
      case PanelComponent.BG_CENTERED:
        bgImage.paintIcon(this, g, (w - imw) / 2, (h - imh) / 2);
        break;
      case PanelComponent.BG_STRETCHED:
        Image originalImage;
        if (bgImage instanceof ImageIcon) {
          originalImage = ((ImageIcon)bgImage).getImage();
        }else{
          if (bgImage instanceof NewRestorableImageIcon) {
            originalImage = ((NewRestorableImageIcon)bgImage).getImage();
          }else{
            originalImage = null;
          }
        }
        if (originalImage != null) {
          Image scaledImage =
            originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
          g.drawImage(scaledImage, 0, 0, null);
        }else{
          System.out.println("Can't stretch image");
          bgImage.paintIcon(this, g, 0, 0);
        }
        break;
      case PanelComponent.BG_NONE:
      default:
        bgImage.paintIcon(this, g, 0, 0);
    }
  }

  /**
   * Paints the alignment grid.
   * @param     g       Graphics context in which to paint the alignment grid.
   */
  private void paintGrid(Graphics g)
  {
    int w = getWidth();
    int h = getHeight();
    g.setColor(gridColor);
    for (int y=0; y<h; y+=gridStep) {
      for (int x=0; x<w; x+=gridStep) {
        g.fillRect(x, y, 2, 2);
      }
    }
  }

//  public Component add(Component comp)
//  {
//    Component c = super.add(comp);
//    return c;
//  }

  public void add(Component comp, Object constraints)
  {
    super.add(comp, constraints);
    this.constraints.put(comp, constraints);
  }

  /**
   * @deprecated        It is strongly advised to use the 1.1 method,
   * add(Component, Object), in place of this method.
   */
  public Component add(String name, Component comp)
  {
    add(comp, (Object)name);
    return comp;
  }

//  public Component add(Component comp, int index)
//  {
//    Component c = super.add(comp, index);
//    return c;
//  }

  public void add(Component comp, Object constraints, int index)
  {
    super.add(comp, constraints, index);
    this.constraints.put(comp, constraints);
  }

  public void remove(Component comp)
  {
    super.remove(comp);
    constraints.remove(comp);
  }

  public void remove(int index)
  {
    Component comp = getComponent(index);
    super.remove(index);
    if (comp != null) {
      constraints.remove(comp);
    }
  }

  public void removeAll()
  {
    super.removeAll();
    constraints.clear();
  }

}
