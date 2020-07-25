package gr.cti.eslate.base;

import gr.cti.eslate.utils.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.beans.beancontext.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * This class implements the desktop pane that contains the internal frames
 * associated with E-Slate components in the plug view window.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class PlugViewDesktopPane
  extends JDesktopPane
  implements ComponentAddedListener, ComponentRemovedListener, ActionListener,
             BeanContextMembershipListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The Plug that the user has selected.
   */
  Plug selectedPlug = null;
  /**
   * The internal frame in which the plug that the user has selected is being
   * displayed.
   */
  PlugViewFrame selectedFrame = null;
  /**
   * The microworld which this desktop pane displays.
   */
  ESlateMicroworld microworld;
  /**
   * Action command for "Resize frames automatically" menu item.
   */
  private final static String RESIZE_FRAMES = "resizeFrames";
  /**
   * Action command for "Show existing connections" menu item.
   */
  private final static String SHOW_EXIST = "showExist";
  /**
   * Action command for "Show possible connections" menu item.
   */
  private final static String SHOW_NEW = "showNew";
  /**
   * Action command for "Open nodes automatically" menu item.
   */
  private final static String AUTO_OPEN = "autoOpen";
  /**
   * Action command for "Open nodes automatically after delay" menu item.
   */
  private final static String DELAYED_AUTO_OPEN = "delayedAutoOpen";
  /**
   * Action command for "Open compatible nodes automatically" menu item.
   */
  private final static String AUTO_OPEN_COMPATIBLE = "autoOpenCompatible";
  /**
   * Action command for "Order" item.
   */
  private final static String ORDER = "order";

  private JLabel empty;
  private int nComponents = 0;
  private ESlateJPopupMenu popup;
  private Dimension initialSize;
  private boolean resizeFrames = true;
  private boolean showExisting = true;
  private boolean showNew = true;
  private boolean autoOpen = true;
  private boolean delayedAutoOpen = false;
  private boolean autoOpenCompatible = false;
  private JCheckBoxMenuItem resizeFramesItem;
  private JCheckBoxMenuItem showExistItem;
  private JCheckBoxMenuItem showNewItem;
  private JCheckBoxMenuItem autoOpenItem;
  private JCheckBoxMenuItem delayedAutoOpenItem;
  private JCheckBoxMenuItem autoOpenCompatibleItem;

  /**
   * Construct a plug view desktop pane.
   * @param     microworld      The microworld that this pane will display.
   */
  PlugViewDesktopPane(ESlateMicroworld microworld)
  {
    this(microworld, new Dimension(640, 400));
  }

  /**
   * Construct a plug view desktop pane.
   * @param     microworld      The microworld that this pane will display.
   * @param     initialSize     The initial size of the desktop pane,
   *                            provided as a hint.
   */
  public PlugViewDesktopPane(ESlateMicroworld microworld, Dimension initialSize)
  {
    super();
    this.initialSize = initialSize;
    setDesktopManager(new PlugViewDesktopManager());
    setBackground(UIManager.getColor("controlShadow"));
    setOpaque(false);
    this.microworld = microworld;
    empty = new JLabel(ESlateMicroworld.resources.getString("noActive"));
    Font f = new Font(empty.getFont().getName(), Font.ITALIC+Font.BOLD, 12);
    empty.setFont(f);
    Dimension d = empty.getPreferredSize();
    empty.setBounds(100, 100, d.width, d.height);
    add(empty, PALETTE_LAYER);
    popup = new ESlateJPopupMenu();
    popup.setLightWeightPopupEnabled(false);

    ESlateJMenu prefsMenu =
      new ESlateJMenu(ESlateMicroworld.resources.getString("prefs"));
      prefsMenu.getPopupMenu().setLightWeightPopupEnabled(false);

      resizeFramesItem =
         new JCheckBoxMenuItem(ESlateMicroworld.resources.getString("resizeFrames"));
      resizeFramesItem.setActionCommand(RESIZE_FRAMES);
      resizeFramesItem.addActionListener(this);
      resizeFramesItem.setSelected(resizeFrames);
      prefsMenu.add(resizeFramesItem);

      showExistItem =
         new JCheckBoxMenuItem(ESlateMicroworld.resources.getString("showExist"));
      showExistItem.setActionCommand(SHOW_EXIST);
      showExistItem.addActionListener(this);
      showExistItem.setSelected(showExisting);
      prefsMenu.add(showExistItem);

      showNewItem =
        new JCheckBoxMenuItem(ESlateMicroworld.resources.getString("showNew"));
      showNewItem.setActionCommand(SHOW_NEW);
      showNewItem.addActionListener(this);
      showNewItem.setSelected(showNew);
      prefsMenu.add(showNewItem);

      autoOpenItem =
        new JCheckBoxMenuItem(ESlateMicroworld.resources.getString("autoOpen"));
      autoOpenItem.setActionCommand(AUTO_OPEN);
      autoOpenItem.addActionListener(this);
      autoOpenItem.setSelected(autoOpen);
      prefsMenu.add(autoOpenItem);

      delayedAutoOpenItem = new JCheckBoxMenuItem(
        ESlateMicroworld.resources.getString("delayedAutoOpen")
      );
      delayedAutoOpenItem.setActionCommand(DELAYED_AUTO_OPEN);
      delayedAutoOpenItem.addActionListener(this);
      delayedAutoOpenItem.setSelected(delayedAutoOpen);
      prefsMenu.add(delayedAutoOpenItem);

      autoOpenCompatibleItem =
        new JCheckBoxMenuItem(
          ESlateMicroworld.resources.getString("autoOpenCompatible")
        );
      autoOpenCompatibleItem.setActionCommand(AUTO_OPEN_COMPATIBLE);
      autoOpenCompatibleItem.addActionListener(this);
      autoOpenCompatibleItem.setSelected(autoOpenCompatible);
      prefsMenu.add(autoOpenCompatibleItem);

    popup.add(prefsMenu);

    ESlateJMenu toolsMenu =
      new ESlateJMenu(ESlateMicroworld.resources.getString("tools"));
      toolsMenu.getPopupMenu().setLightWeightPopupEnabled(false);

      JMenuItem orderItem =
        toolsMenu.add(ESlateMicroworld.resources.getString("order"));
      orderItem.setActionCommand(ORDER);
      orderItem.addActionListener(this);

    popup.add(toolsMenu);

    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e)
      {
        // Check for popup menu mouse button. Since isPopupTrigger does not
        // appear to be working under Windows, we also check explicitly for
        // the right button.
        if (e.isPopupTrigger() ||
            (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
          popup.show(PlugViewDesktopPane.this, e.getX(), e.getY());
        }
      }
    });
  }

  /**
   * Overrides the corresponding method in java.awt.Container, to keep track
   * of the total number of E-Slate components added to the desktop, to
   * place internal frames in a tiled fashion, and to ensure that the contents
   * of the internal frames are displayed correctly.
   * @param     c       The component to add to the desktop.
   * @param     layer   The desktop layer to which to add the component.
   */
  public void add(Component c, Object layer)
  {
    int actualX = 0;
    int actualY = 0;

    PlugViewFrame f = null;
    if (c instanceof PlugViewFrame) {
      f = (PlugViewFrame)c;
    }

    if (f != null) {
      f.myHandle.addMembershipListener(this);
    }

    if (f != null &&
        !(f.myHandle.usePlugViewFrameBounds)) {
      Rectangle bounds = f.getBounds();
      Rectangle testBounds = new Rectangle(bounds);
      Dimension size = getSize();
      int width = size.width;
      if (width == 0) {
        width = initialSize.width;
      }
      int height = size.height;
      if (height == 0) {
        height = initialSize.height;
      }
      int dist2 = Integer.MAX_VALUE;
      JInternalFrame[] myFrames = getAllFrames();
      int nFrames = myFrames.length;
      Rectangle[] frameBounds = new Rectangle[nFrames];
      for (int i=0; i<nFrames; i++) {
        frameBounds[i] = myFrames[i].getBounds();
      }
      for (int y=0; y<=height; y++) {
        for (int x=0; x<=width; x++) {
          testBounds.x = x;
          testBounds.y = y;
          boolean foundIntersection = false;
          for (int boundNum=0; boundNum<nFrames; boundNum++) {
            if (testBounds.intersects(frameBounds[boundNum])) {
              foundIntersection = true;
              x = frameBounds[boundNum].x + frameBounds[boundNum].width - 1;
            }
          }
          if (foundIntersection) {
            continue;
          }
          int xdist = bounds.x - x;
          int ydist = bounds.y - y;
          int newdist2 = (xdist * xdist) + (ydist * ydist);
          if (newdist2 < dist2) {
            dist2 = newdist2;
            actualX = x;
            actualY = y;
          }
        }
      }
    }
    super.add(c, layer);
    if (f != null) {
      nComponents++;
      if (nComponents == 1) {
        setBackground(new Color(0xff, 0xff, 0xe4));
        remove(empty);
        repaint();
      }
      if (!(f.myHandle.usePlugViewFrameBounds)) {
        // Position frame to its new location.
        f.setLocation(actualX, actualY);
        Rectangle r = f.myHandle.plugViewFrameBounds;
        r.x = actualX;
        r.y = actualY;
      }
      PlugTree tree = f.getTree();
      // The nodes of trees that start in a collapsed state do not have the
      // correct width.  Expanding and collapsing the root fixes the problem.
      // Yes, this is a kludge.
      PlugTreeNode root =
        (PlugTreeNode)(tree.getPathForRow(0).getLastPathComponent());
      toggleExpand(tree, root);
      // Adjust desktop size.
      adjustSize();
    }
  }

  @SuppressWarnings("unchecked")
  public void childrenAdded(BeanContextMembershipEvent bcme)
  {
    JInternalFrame[] jif = getAllFrames();
    Iterator it = bcme.iterator();
    while (it.hasNext()) {
      Object ob = it.next();
      if (ob instanceof ESlateBeanContext) {
        ESlateHandle h = ((ESlateBeanContext)ob).getESlateHandle();
        for (int i=0; i<jif.length; i++) {
          PlugViewFrame f = (PlugViewFrame)(jif[i]);
          if (f.myHandle.equals(h)) {
            removeFrame(f);
            break;
          }
        }
      }
    }
  }

  public void childrenRemoved(BeanContextMembershipEvent bcme)
  {
  }

  /**
   * Expands and collapses all collapsed nodes in a tree, starting from a
   * given node.
   * @param     tree    The tree.
   * @param     root    The starting node.
   */
  @SuppressWarnings("unchecked")
  private void toggleExpand(PlugTree tree, PlugTreeNode root)
  {
    if (root.isLeaf()) {
      return;
    }
    TreePath path = tree.getPath(root);
    if (tree.isExpanded(root)) {
      Enumeration children = root.children();
      while (children.hasMoreElements()) {
        PlugTreeNode node = (PlugTreeNode)(children.nextElement());
        toggleExpand(tree, node);
      }
    }else{
      if (!root.isLeaf()) {
        tree.expandPath(path);
        tree.collapsePath(path);
      }
    }
  }

  /**
   * Overrides the corresponding method in javax.swing.JComponent, to update
   * the location of the label shown when there is no active component in the
   * microworld.
   */
  @SuppressWarnings(value={"deprecation"})
  public void reshape(int x, int y, int w, int h)
  {
    super.reshape(x, y, w, h);
    Dimension es = empty.getPreferredSize();
    empty.setLocation((w-es.width)/2, (h-es.height)/2);
    adjustSize();
  }

  /**
   * Overrides the corresponding method in java.awt.JComponent, to update
   * the location of the label shown when there is no active component in the
   * microworld.
   */
  public void setBounds(int x, int y, int w, int h) 
  {
    reshape(x, y, w, h);
  }

  /**
   * Adjust the size of the component to be the larger of the maximum
   * available size and of the minimum size required for all the internal
   * frames to fit.
   */
  void adjustSize()
  {
    Component parent = this;
    while (parent != null && !(parent instanceof PlugViewDesktop)) {
      parent = parent.getParent();
    }
    if (parent != null) {
      Dimension displayedSize = ((PlugViewDesktop)parent).getDisplayedSize();
      Rectangle myBounds = new Rectangle(new Point(0, 0), displayedSize);
      JInternalFrame[] f = getAllFrames();
      for (int i=0; i<f.length; i++) {
        myBounds = myBounds.union(f[i].getBounds());
      }
      // Adjust desktop size.
      Dimension newSize = new Dimension(myBounds.width, myBounds.height);
      setPreferredSize(newSize);
      setMinimumSize(newSize);
      setMaximumSize(newSize);
    }
  }

  /**
   * Free resources held by the desktop pane and its internal frames.
   */
  public void dispose()
  {
    JInternalFrame[] f = getAllFrames();
    for (int i=0; i<f.length; i++) {
      if (f[i] instanceof PlugViewFrame) {
        ((PlugViewFrame)(f[i])).myHandle.removeMembershipListener(this);
      }
      // Closing a PlugViewFrame will make it remove its listeners and
      // release any references to E-Slate.
      try {
        f[i].setClosed(true);
      } catch (java.beans.PropertyVetoException e){
      }
      f[i].dispose();
    }
    microworld.removeComponentAddedListener(this);
    microworld.removeComponentRemovedListener(this);
    microworld = null;
    popup = null;
    initialSize = null;
    selectedPlug = null;
    selectedFrame = null;
  }

  /**
   * Handler for component added events. Whenever a component is added to the
   * microworld that this desktop pane displays, the handler adds a
   * corresponding internal frame.
   * @param     e       The event to handle.
   */
  public void componentAdded(ComponentAddedEvent e)
  {
    ESlateHandle handle = (ESlateHandle)(e.getSource());
    ESlateHandle containerHandle = microworld.getMicroworldEnvironmentHandle();
    if (!handle.equals(containerHandle)) {
      PlugViewFrame f = new PlugViewFrame(handle);
      add(f, JLayeredPane.PALETTE_LAYER);
      f.setVisible(true);
      try {
        f.setSelected(true);
      } catch (PropertyVetoException pve) {
      }
    }
  }

  /**
   * Handler for component removed events. Whenever a component is removed
   * from the microworld that this desktop pane displays, the handler removes
   * the corresponding internal frame.
   * @param     e       The event to handle.
   */
  public void componentRemoved(ComponentRemovedEvent e)
  {
    JInternalFrame[] f = getAllFrames();
    String name = ((ESlateHandle)(e.getSource())).getComponentName();
    for (int i=0; i<f.length; i++) {
      if (name.equals(f[i].getTitle())) {
        removeFrame(f[i]);
        break;
      }
    }
  }

  /**
   * Remove an internal frame.
   * @param     f       The frame to remove.
   */
  private void removeFrame(JInternalFrame f)
  {
    if (f instanceof PlugViewFrame) {
      ((PlugViewFrame)f).myHandle.removeMembershipListener(this);
    }
    try {
      f.setClosed(true);
    } catch (java.beans.PropertyVetoException e){
    }
    f.dispose();
    nComponents--;
    if (nComponents == 0) {
      Dimension ds = getSize();
      Dimension es = empty.getPreferredSize();
      empty.setLocation((ds.width-es.width)/2, (ds.height-es.height)/2);
      setBackground(UIManager.getColor("controlShadow"));
      add(empty, PALETTE_LAYER);
    }
  }

  /**
   * Parse action events from the desktop's pop-up menu.
   * @param     e       The event to parse.
   */
  public void actionPerformed(ActionEvent e)
  {
    String cmd = e.getActionCommand();
    if (cmd.equals(RESIZE_FRAMES)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setResizeFrames(selected);
      Component parent = this;
      while (parent != null && !(parent instanceof PlugViewDesktop)) {
        parent = parent.getParent();
      }
      ((PlugViewDesktop)parent).resizeFramesItem.setSelected(selected);
      autoAdjustAll();
      return;
    }
    if (cmd.equals(SHOW_EXIST)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setHighlightingExistingConnections(selected);
      Component parent = this;
      while (parent != null && !(parent instanceof PlugViewDesktop)) {
        parent = parent.getParent();
      }
      ((PlugViewDesktop)parent).showExistItem.setSelected(selected);
      return;
    }
    if (cmd.equals(SHOW_NEW)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setHighlightingPossibleConnections(selected);
      Component parent = this;
      while (parent != null && !(parent instanceof PlugViewDesktop)) {
        parent = parent.getParent();
      }
      ((PlugViewDesktop)parent).showNewItem.setSelected(selected);
      return;
    }
    if (cmd.equals(AUTO_OPEN)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setAutoOpen(selected);
      Component parent = this;
      while (parent != null && !(parent instanceof PlugViewDesktop)) {
        parent = parent.getParent();
      }
      ((PlugViewDesktop)parent).autoOpenItem.setSelected(selected);
      if (selected) {
        ((PlugViewDesktop)parent).delayedAutoOpenItem.setSelected(false);
      }
      return;
    }
    if (cmd.equals(DELAYED_AUTO_OPEN)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setDelayedAutoOpen(selected);
      Component parent = this;
      while (parent != null && !(parent instanceof PlugViewDesktop)) {
        parent = parent.getParent();
      }
      ((PlugViewDesktop)parent).delayedAutoOpenItem.setSelected(selected);
      if (selected) {
        ((PlugViewDesktop)parent).autoOpenItem.setSelected(false);
      }
      return;
    }
    if (cmd.equals(AUTO_OPEN_COMPATIBLE)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setAutoOpenCompatible(selected);
      Component parent = this;
      while (parent != null && !(parent instanceof PlugViewDesktop)) {
        parent = parent.getParent();
      }
      ((PlugViewDesktop)parent).autoOpenCompatibleItem.setSelected(selected);
      return;
    }
    if (cmd.equals(ORDER)) {
      order();
      return;
    }
  }

  /**
   * Order the managed internal frames. Ordering is done left to right, top to
   * bottom. The desktop is expanded as necessary, if the size does not fit.
   */
  void order()
  {
    if (resizeFrames) {
      autoAdjustAll();
    }
    JInternalFrame[] myFrames = getAllFrames();
    int nFrames = myFrames.length;
    if (nFrames == 0 ) {
      return;
    }
    Dimension mySize;
    Component c = this;
    while (c != null && !(c instanceof JViewport)) {
      c = c.getParent();
    }
    if (c != null) {
      mySize = c.getSize();
    }else{
      mySize = c.getSize();
    }
    Arrays.sort(myFrames, new InternalFrameComparator());
    Rectangle[] frameBounds = new Rectangle[nFrames];
    Rectangle desktopRectangle = new Rectangle(new Point(0, 0), mySize);
    Rectangle destinationRectangle = new Rectangle(desktopRectangle);
    boolean addToRight = true;
    boolean found = false;

    do {
      for (int i=0; i<nFrames; i++) {
        found = false;
        int width = destinationRectangle.width;
        int height = destinationRectangle.height;
        Rectangle bounds = myFrames[i].getBounds();
        for (int y=0; y<height && !found; y++) {
          for (int x=0; x<width && !found; x++) {
            bounds.x = x;
            bounds.y = y;
            boolean foundIntersection = false;
            for (int boundNum=0; boundNum<i; boundNum++) {
              if (bounds.intersects(frameBounds[boundNum])) {
                foundIntersection = true;
                x = frameBounds[boundNum].x + frameBounds[boundNum].width - 1;
              }
            }
            if (foundIntersection) {
              continue;
            }
            if ((x + bounds.width) > width) {
              x = width;
              continue;
            }
            found = true;
            frameBounds[i] = bounds;
          }
        }
        if (!found) {
          if (addToRight) {
            destinationRectangle.width += bounds.width;
          }else{
            destinationRectangle.height += bounds.height;
          }
          addToRight = !addToRight;
          break;
        }
      }
    } while (!found);

    // Position frames to their new location.
    for (int i=0; i<nFrames; i++) {
      myFrames[i].setLocation(frameBounds[i].x, frameBounds[i].y);
    }

    // Adjust dekstop size.
    adjustSize();
  }

  /**
   * Adjusts the bounds of the frame so that it does not contain any scroll
   * bars and is entirely within the visible region of the plug view window.
   */
  void autoAdjustAll()
  {
    JInternalFrame[] myFrames = getAllFrames();
    int nFrames = myFrames.length;
    for (int i=0; i<nFrames; i++) {
      ((PlugViewFrame)(myFrames[i])).autoAdjust();
    }
  }

  /**
   * Checks whether frames are resized automatically.
   * @return    True if yes, false if no.
   */
  boolean isResizeFrames()
  {
    return resizeFrames;
  }

  /**
   * Specifies whether frames are resized automatically.
   * @param     state   True if yes, false if no.
   */
  void setResizeFrames(boolean state)
  {
    resizeFrames = state;
    resizeFramesItem.setSelected(state);
  }

  /**
   * Checks whether existing connections will be highlighted during selection.
   * @return    True if yes, false if no.
   */
  boolean isHighlightingExistingConnections()
  {
    return showExisting;
  }

  /**
   * Specifies whether existing connections will be highlighted during
   * selection.
   * @param     state   True if yes, false if no.
   */
  void setHighlightingExistingConnections(boolean state)
  {
    showExisting = state;
    showExistItem.setSelected(state);
    try {
      selectedFrame.markCompatiblePlugs(selectedPlug, showExisting, showNew);
    } catch (Exception e) {
      // Just in case...
    }
  }

  /**
   * Checks whether tree nodes are opened automatically when a plug is dragged
   * over them.
   * @return    True if yes, false if no.
   */
  boolean isAutoOpen()
  {
    return autoOpen;
  }

  /**
   * Specifies whether tree nodes are opened automatically when a plug is
   * dragged over them.
   * @param     state   True if yes, false if no.
   */
  void setAutoOpen(boolean state)
  {
    autoOpen = state;
    autoOpenItem.setSelected(state);
    if (state) {
      delayedAutoOpen = false;
      delayedAutoOpenItem.setSelected(false);
    }
  }

  /**
   * Checks whether tree nodes are opened automatically, after a delay,
   * when a plug is dragged over them.
   * @return    True if yes, false if no.
   */
  boolean isDelayedAutoOpen()
  {
    return delayedAutoOpen;
  }

  /**
   * Specifies whether tree nodes are opened automatically, after a delay,
   * when a plug is dragged over them.
   * @param     state   True if yes, false if no.
   */
  void setDelayedAutoOpen(boolean state)
  {
    delayedAutoOpen = state;
    delayedAutoOpenItem.setSelected(state);
    if (state) {
      autoOpen = false;
      autoOpenItem.setSelected(false);
    }
  }

  /**
   * Checks whether compatible tree nodes are opened automatically during
   * connection/disconnection.
   * @return    True if yes, false if no.
   */
  boolean isAutoOpenCompatible()
  {
    return autoOpenCompatible;
  }

  /**
   * Specifies whether compatible tree nodes are opened automatically during
   * connection/disconnection.
   * @param     state   True if yes, false if no.
   */
  void setAutoOpenCompatible(boolean state)
  {
    autoOpenCompatible = state;
    autoOpenCompatibleItem.setSelected(state);
  }

  /**
   * Checks whether possible connections will be highlighted during selection.
   * @return    True if yes, false if no.
   */
  boolean isHighlightingPossibleConnections()
  {
    return showNew;
  }

  /**
   * Specifies whether possible connections will be highlighted during
   * selection.
   * @param     state   True if yes, false if no.
   */
  void setHighlightingPossibleConnections(boolean state)
  {
    showNew = state;
    showNewItem.setSelected(state);
    try {
      selectedFrame.markCompatiblePlugs(selectedPlug, showExisting, showNew);
    } catch (Exception e) {
      // Just in case...
    }
  }

  /**
   * Update the plug views containing two plugs that have just been connected
   * or disconnected.
   */
  void updatePlugViews()
  {
    if (selectedPlug != null) {
      ESlateHandle h = selectedPlug.getHandle();
      if (h != null) {
        h = h.getRootComponentHandle();
      }
      if (h != null) {
        JInternalFrame[] myFrames = getAllFrames();
        int nFrames = myFrames.length;
        for (int i=0; i<nFrames; i++) {
          PlugViewFrame f = (PlugViewFrame)(myFrames[i]);
          if (h.equals(f.myHandle)) {
            f.updatePlugViews(selectedPlug);
            break;
          }
        }
      }
    }
  }

  /**
   * Update the component's look and feel.
   */
  public void updateUI()
  {
    super.updateUI();
    if (nComponents <= 0) {
      setBackground(UIManager.getColor("controlShadow"));
    }
  }

  /**
   * This class implements a comparator for the internal frames of the desktop
   * pane.
   */
  private class InternalFrameComparator implements Comparator<JInternalFrame>
  {
    /**
     * Compare two internal frames.
     * @param   f1      The first internal frame to compare.
     * @param   f2      The second internal frame to compare.
     * @return  0 if the titles of the two frames are equal, a negative number
     *          if the title of the first frame is lexicographically smaller
     *          than the title of the second frame, and a positive number if
     *          the title of the first frame is lexicographically greater than
     *          the title of the second frame.
     */
    public int compare(JInternalFrame f1, JInternalFrame f2)
    {
      return ESlateStrings.compareIgnoreCase(
        f1.getTitle(), f2.getTitle(), ESlateMicroworld.getCurrentLocale()
      );
    }

//    /**
//     * Checks whether two internal frames are "equal".
//     * @param   o1      The first internal frame.
//     * @param   o2      The second internal frame.
//     * @return  True if the titles of the two internal frames are equal, false
//     *          otherwise.
//     */
//    public boolean equals(Object o1, Object o2)
//    {
//      return compare(o1, o2) == 0;
//    }
  }
}
