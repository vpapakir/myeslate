package gr.cti.eslate.base;

import gr.cti.eslate.utils.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * This class implements the internal frame associated with an E-Slate
 * component in the plug view window.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class PlugViewFrame extends JInternalFrame
  implements ESlateListener, MouseMotionListener, MouseListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * A reference to the E-Slate handle of the associated component.
   */
  ESlateHandle myHandle;

  /**
   * A reference to the PlugTree displayed in the internal frame.
   */
  private PlugTree tree;

  /**
   * Used to make sure that the component event handler is not called multiple
   * times concurrently.
   */
  private Boolean servingComponentEvent = Boolean.FALSE;

  /**
   * The default scaling factor for determining the location of the
   * internal frames with the plug views of components, based on the
   * components' actual location.
   */
  private final static int intScale = 2;

  /**
   * The maximum number of internal frames that can appear in one row or
   * column in the plug view, when tiling the internal frames rather than
   * determining their location based on the actual location
   * of the associated components.
   */
  private final static int maxIntFrames = 10;

  /**
   * The horizontal coordinate of the next internal frame to be created
   * in the plug tree view.
   */
  private int intX = 0;

  /**
   * The vertical coordinate of the next internal frame to be created
   * in the plug tree view.
   */
  private int intY = 0;

  /**
   * The increment for changing the horizontal and vertical coordinates of
   * successive internal frames in the plug tree view.
   */
  private final static int intIncr = 20;

  /**
   * The current "column" of internal frames.
   */
  private int intCol = 0;

  /**
   * The border used when the frame is highlighted.
   */
  private static Border highlightBorder = BorderFactory.createBevelBorder(
    BevelBorder.RAISED, new Color(250,250,98), new Color(250, 212, 5)
  );

  /**
   * The border used when the frame is not highlighted.
   */
  private static Border normalBorder = BorderFactory.createBevelBorder(
    BevelBorder.RAISED
  );

  /**
   * When dragging a plug to connect it to another plug in the plug view window,
   * this variable contains a reference to the plug being dragged.
   */
  private Plug dragPlug = null;

  /**
   * Indicates whether we are currently dragging a plug to connect it to
   * another plug in the plug view window.
   */
  private boolean dragging = false;

  /**
   * Indicates whether we are currently moving a plug view frame by dragging
   * the root node.
   */
  private boolean moving = false;

  /**
   * The "move" cursor.
   */
  private static Cursor moveCursor = null;

  /**
   * The "hand" cursor.
   */
  private static Cursor handCursor = null;

  /**
   * The default cursor.
   */
  private static Cursor defaultCursor = null;

  /**
   * The "forbidden" cursor.
   */
  private static Cursor forbiddenCursor = null;

  /**
   * The "connected plugs" cursor.
   */
  private static Cursor connectCursor = null;

  /**
   * The "disconnected plugs" cursor.
   */
  private static Cursor disconnectCursor = null;

  /**
   * The "scisors" cursor.
   */
  private static Cursor scisorsCursor = null;

  /**
   * The plug view desktop being used for drag & drop operations.
   */
  private PlugViewDesktopPane pvd = null;

  /**
   * The point where the user clicked on the root node of a plug view window,
   * relative to the top left corner of this window.
   */
  private Point clickPt = null;

  /**
   * The X coordinate of the frame, where the user clicked.
   */
  int clickX;

  /**
   * The Y coordinate of the frame, where the user clicked.
   */
  int clickY;

  /**
   * The button that the user moves when dragging a plug to connect it to
   * another plug in the plug view window.
   */
  private JButton dragButton = null;

  /**
   * A temporary point used during move operations.
   */
  private Point pt = new Point();

  /**
   * When locating the plug corresponding to the location under the mouse in
   * the plug editor, the getPlugAt mnethod puts the PlugTree to which the
   * plug is displayed in this variable.
   */
  private PlugTree targetTree;

  /**
   * Indicates whether the right mouse button hase been pressed.
   */
  private boolean rightButtonPressed = false;

  /**
   * The plug over which the disconnection popup menu has appeared.
   */
  private Plug disconnectPlug = null;

  /**
   * The tolerance for recognizing plug drags as such. Smaller drags are
   * considered to have been caused by shaking hands, and are ignored.
   */
  private final static int CLICKTHRESHOLD = 4;

  /**
   * A timer used for delaying the automatic opening of nodes.
   */
  java.util.Timer timer = new java.util.Timer();
  /**
   * The task scheduled for the automatic opening of nodes.
   */
  private AutoOpenTask autoOpenTask;
  /**
   * The last node to be scheduled for automatic opening after a delay.
   */
  private PlugTreeNode lastNodeToOpen = null;


  /**
   * Adjusts the size of the frame after a node was expanded.
   * @param     path    The path of the expanded node.
   */
  private void adjustAfterExpansion(TreePath path)
  {
    PlugViewDesktopPane pvdp =
      (PlugViewDesktopPane)(PlugViewFrame.this.getParent());
    // Expanding a node adjusts the frame to its optimal size if the
    // "automatically adjust frames" option has been selected.
    if (pvdp.isResizeFrames()) {
      autoAdjust();
    }else{
      // If the "automatically adjust frames" option has not been selected,
      // then expanding the root node restores the frames' size, thus
      // "deiconifying" the frame.
      if (path.getParentPath() == null) {
        Rectangle r = myHandle.oldBounds;
        if (r != null && (r.width > 0) && (r.height > 0)) {
          setBounds(getX(), getY(), r.width, r.height);
        }
      }
    }
  }

  /**
   * Adjusts the size of the frame after a node was collapsed.
   * @param     path    The path of the collapsed node.
   */
  private void adjustAfterCollapse(TreePath path)
  {
    if (path.getParentPath() == null) {
      // Collapsing the root node "iconifies" the frame.
      Rectangle bounds = getBounds(myHandle.oldBounds);
      JScrollPane jsp = (JScrollPane)(getContentPane());
      Dimension ss = jsp.getSize();
      int width = getPreferredSize().width;
      int height = getSize().height - ss.height + tree.getRowHeight() +
                   (2 * PlugTreeCellRenderer.BORDER_THICKNESS);
      myHandle.oldBounds = bounds;
      setBounds(bounds.x, bounds.y, width, height);
    }else{
      // Collapsing other nodes adjusts the frame to its optimal size
      // if the "automatically adjust frames" option has been selected.
      PlugViewDesktopPane pvdp =
        (PlugViewDesktopPane)(PlugViewFrame.this.getParent());
      if (pvdp.isResizeFrames()) {
        autoAdjust();
      }
    }
  }

  /**
   * Creates a plug view frame for a given E-Slate component.
   * @param     handle          The E-Slate handle of the component.
   */
  PlugViewFrame(ESlateHandle handle)
  {
    super(handle.getComponentName());
    tree = new PlugTree(handle.plugs)
    {
      /**
       * Serialization version.
       */
      final static long serialVersionUID = 1L;
      
      public void updateUI()
      {
        super.updateUI();
        try {
          PlugViewFrame.this.adjustUI();
          TreeCellRenderer r = tree.getCellRenderer();
          if (r instanceof PlugTreeCellRenderer) {
            ((PlugTreeCellRenderer)r).updateColorsAndStuff();
          }
        } catch (NullPointerException npe) {
        }
      }
    };
    DefaultTreeModel tm = (DefaultTreeModel)(tree.getModel());
    tree.setModel(
      new PlugTreeModel((TreeNode)(tm.getRoot()), tm.asksAllowsChildren())
    );
    setUI(new ESlateBasicInternalFrameUI(this));
    setNormalBorder();
    setResizable(true);
    setClosable(false);
    setMaximizable(true);
    setIconifiable(true);
    setContentPane(new JScrollPane(tree));
    myHandle = handle;
    myHandle.addESlateListener(this);
    addComponentListener(new ComponentAdapter() {
      public void componentMoved(ComponentEvent e)
      {
        synchronized (servingComponentEvent) {
          if (servingComponentEvent.booleanValue()) {
            return;
          }else{
            servingComponentEvent = Boolean.TRUE;
          }
        }
        if (myHandle.plugViewFrameBounds != null) {
          myHandle.plugViewFrameBounds.x = getX();
          myHandle.plugViewFrameBounds.y = getY();
        }
        updateScrollPane(e);
        servingComponentEvent = Boolean.FALSE;
      }
      public void componentResized(ComponentEvent e)
      {
        synchronized (servingComponentEvent) {
          if (servingComponentEvent.booleanValue()) {
            return;
          }else{
            servingComponentEvent = Boolean.TRUE;
          }
        }
        if (myHandle.plugViewFrameBounds != null) {
          myHandle.plugViewFrameBounds.width = getWidth();
          myHandle.plugViewFrameBounds.height = getHeight();
        }
        updateScrollPane(e);
        servingComponentEvent = Boolean.FALSE;
      }
      private void updateScrollPane(ComponentEvent e)
      {
        PlugViewFrame fr = (PlugViewFrame) e.getSource();
        PlugViewDesktopPane ptv = (PlugViewDesktopPane)(fr.getParent());
        JScrollPane scrollPane = (JScrollPane)(ptv.getParent().getParent());
        int ptvWidth = ptv.getWidth();
        int ptvHeight = ptv.getHeight();
        int compoX = fr.getX();
        int compoY = fr.getY();
        int compoWidth = fr.getWidth();
        int compoHeight = fr.getHeight();
        if (compoX < 0 || compoY < 0) {
          int xDiff = compoX;
          int yDiff = compoY;
          if (xDiff > 0) {
            xDiff = 0;
          }else{
            xDiff = -xDiff;
          }
          if (yDiff > 0) {
            yDiff = 0;
          }else{
            yDiff = -yDiff;
          }
          fr.reshape(fr.getX() + xDiff, fr.getY() + yDiff,
                     fr.getWidth(), fr.getHeight());
          Dimension newSize =
            new Dimension(ptvWidth + xDiff, ptvHeight + yDiff);
          ptv.setPreferredSize(newSize);
          ptv.setMinimumSize(newSize);
          ptv.setMaximumSize(newSize);
        }
        if ((compoX+compoWidth) > ptvWidth ||
            (compoY + compoHeight) > ptvHeight) {
          int xDiff = -(ptvWidth-(compoX+compoWidth));
          int yDiff = -(ptvHeight-(compoY+compoHeight));
          if (yDiff < 0) {
            yDiff = 0;
          }
          if (xDiff < 0) {
            xDiff = 0;
          }
          Dimension newSize =
            new Dimension(ptvWidth + xDiff, ptvHeight + yDiff);
          ptv.setPreferredSize(newSize);
          ptv.setMinimumSize(newSize);
          ptv.setMaximumSize(newSize);

          // This line of code causes both the scrollbars to appear, even if
          // if only one is needed.
          scrollPane.getViewport().setViewPosition(
            new Point(
              scrollPane.getViewport().getViewPosition().x + xDiff,
              scrollPane.getViewport().getViewPosition().y + yDiff
            )
          );
        }
        scrollPane.revalidate();
      }
    });
    addInternalFrameListener(new InternalFrameAdapter() {
      /**
       * Remove any references to the associated component when the frame is
       * closed.
       */
      @Override
      public void internalFrameClosed(InternalFrameEvent e)
      {
        if (myHandle != null) {
          myHandle.removeESlateListener(PlugViewFrame.this);
          myHandle.removeTree(PlugViewFrame.this.tree);
          myHandle = null;
        }
      }
      @Override
      public void internalFrameIconified(InternalFrameEvent e)
      {
        myHandle.plugViewFrameIconified = true;
        JInternalFrame.JDesktopIcon dIcon = getDesktopIcon();
        if (myHandle.iconifiedPlugViewFrameBounds != null) {
          dIcon.setBounds(myHandle.iconifiedPlugViewFrameBounds);
        }else{
          dIcon.addComponentListener(new ComponentListener() {
            public void componentMoved(ComponentEvent e)
            {
              if (myHandle.iconifiedPlugViewFrameBounds != null) {
                Component c = (Component)e.getSource();
                myHandle.iconifiedPlugViewFrameBounds.x = c.getX();
                myHandle.iconifiedPlugViewFrameBounds.y = c.getY();
              }
            }
            public void componentResized(ComponentEvent e)
            {
              if (myHandle.iconifiedPlugViewFrameBounds != null) {
                Component c = (Component)e.getSource();
                myHandle.iconifiedPlugViewFrameBounds.width = c.getWidth();
                myHandle.iconifiedPlugViewFrameBounds.height = c.getHeight();
              }
            }
            public void componentHidden(ComponentEvent e)
            {
            }
            public void componentShown(ComponentEvent e)
            {
            }
          });
          myHandle.iconifiedPlugViewFrameBounds =
            dIcon.getBounds(myHandle.iconifiedPlugViewFrameBounds);
        }
      }
      @Override
      public void internalFrameDeiconified(InternalFrameEvent e)
      {
        myHandle.plugViewFrameIconified = false;
      }
      @Override
      public void internalFrameActivated(InternalFrameEvent e)
      {
        ESlateMicroworld mw = myHandle.getESlateMicroworld();
        if (mw != null) {
          mw.setSelectedComponent(myHandle);
        }
      }
    });
    tree.addMouseListener(this);
    tree.addMouseMotionListener(this);

    if (moveCursor == null) {
      Toolkit tk = getToolkit();
      Point hotspot = new Point(5, 0);

      moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
      defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

      Image im = tk.createImage(getClass().getResource("forbidden.gif"));
      try {
        forbiddenCursor = tk.createCustomCursor(im, hotspot, "forbidden");
      } catch (IndexOutOfBoundsException iobe) {
      }

      im = tk.createImage(getClass().getResource("connect.gif"));
      try {
        connectCursor = tk.createCustomCursor(im, hotspot, "connect");
      } catch (IndexOutOfBoundsException iobe) {
      }

      im = tk.createImage(getClass().getResource("disconnect.gif"));
      try {
        disconnectCursor = tk.createCustomCursor(im, hotspot, "disconnect");
      } catch (IndexOutOfBoundsException iobe) {
      }

      im = tk.createImage(getClass().getResource("scisors.gif"));
      try {
        scisorsCursor = tk.createCustomCursor(im, hotspot, "scisors");
      } catch (IndexOutOfBoundsException iobe) {
      }

      // We don't use the default "hand" cursor, as we want the hand to be the
      // same as that in the above cursors."
      im = tk.createImage(getClass().getResource("hand.gif"));
      try {
        handCursor = tk.createCustomCursor(im, hotspot, "disconnect");
      } catch (IndexOutOfBoundsException iobe) {
      }
    }

    tree.addTreeExpansionListener(new TreeExpansionListener()
    {
      public void treeExpanded(TreeExpansionEvent event)
      {
        TreePath path = event.getPath();
        adjustAfterExpansion(path);
        if (path.getParentPath() == null) {
          myHandle.isCollapsed = false;
        }
      }
      public void treeCollapsed(TreeExpansionEvent event)
      {
        TreePath path = event.getPath();
        adjustAfterCollapse(event.getPath());
        if (path.getParentPath() == null) {
          myHandle.isCollapsed = true;
        }
      }
    });

    // Enable tool tips for the tree, without this tool tips will not
    // be picked up.
    ToolTipManager.sharedInstance().registerComponent(tree);
    handle.addTree(tree);
    adjustUI();
    tree.getSelectionModel().setSelectionMode(
      TreeSelectionModel.SINGLE_TREE_SELECTION);
    PlugTreeCellRenderer renderer = new PlugTreeCellRenderer(handle.getImage());
    tree.setCellRenderer(renderer);

    // PlugTreeCellEditor uses the tree's cell renderer, so setCellEditor
    // should be invoked after having set the cell renderer.
    tree.setCellEditor(new PlugTreeCellEditor(tree));

    // Do we want to allow the user to change plug names? If we do, the code is
    // in place, so all we need do is change the editable status to true.
    tree.setEditable(false);

    // If the frame had been "iconified" in the previous run of the
    // microworld, then iconify it again.
    if (handle.isCollapsed) {
      Rectangle oldBounds = myHandle.oldBounds;
      tree.collapseRow(0);
      myHandle.oldBounds = oldBounds;
    }

    if (handle.plugViewFrameBounds != null) {
      // Use last known bounds of the E-Slate plug view frame
      setBounds(handle.plugViewFrameBounds);
    }else{
      try {
        // If the E-Slate component is an AWT component that is visible,
        // position its plug view window according to the component's location
        // on screen.
        Component c = (Component)(handle.getComponent());
        // The E-Slate container displays the content pane of JApplets,
        // instead of the JApplets themselves. Thus, if a JApplet is running
        // in the container, getting its location, rather than that of its
        // content pane will fail.
        if (c instanceof JApplet) {
          c = ((JApplet)c).getContentPane();
        }
        // If the component is running inside the E-Slate container, then its
        // location will be 0,0, as it is running inside an internal frame.
        // In this case, to get the component's position inside the container,
        // we must use the position of the internal frame rather than that of
        // the component itself. The number of getParent()s was found by trial
        // and error. Reading the manual would probably have yielded the same
        // results!
        Point loc = null;
        try {
          Component c2 = c.getParent().getParent().getParent().getParent();
          if (c2 instanceof JInternalFrame) {
            loc = c2.getLocation();
          }
        } catch (Exception e2) {
        }
        if (loc == null) {
          loc = c.getLocationOnScreen();
        }
        adjustBounds(loc.x / intScale, loc.y / intScale);
      } catch (Exception e) {
        // Otherwise, position the plug view windows in a cascade.
        adjustBounds(intX, intY);
        intX += intIncr;
        intY += intIncr;
        if (intY == (maxIntFrames * intIncr)) {
          intY = 0;
          intCol++;
          if (intCol == maxIntFrames){
            intCol = 0;
          }
          intX = (intCol * intIncr);
        }
      }
      handle.plugViewFrameBounds = getBounds(handle.plugViewFrameBounds);
    }
  }

  /**
   * Sets appropriate values for various UI-related parameters that depend on
   * the look and feel.
   */
  private void adjustUI()
  {
    if (!(getUI() instanceof ESlateBasicInternalFrameUI)) {
      setUI(new ESlateBasicInternalFrameUI(this));
    }
    // Tree row height is too small for the icons we are going to use.
    // Increase it to accommodate them. (We assume that all icons we'll
    // be using have the same height, which is currently true.)
    tree.setRowHeight(
      4 +
      Math.max(
        tree.getRowHeight(),
        ESlateRootIcon.HEIGHT
      )
    );
    tree.setBackground(UIManager.getColor("Panel.background"));
    hideTitlePanel();
  }

  /**
   * Adjusts the bounds of the frame so that it does not contain any scroll
   * bars and is entirely within the visible region of the plug view window.
   */
  void autoAdjust()
  {
    if (isVisible()) {
      PlugViewDesktop pvd = myHandle.getESlateMicroworld().getPlugViewDeskTop();
      if (pvd.isResizeFrames()) {
        adjustBounds(getX(), getY());
/*
        // This code will resize and reposition the frame so that it is
        // entirely within the visible region of the plug view window.
        // This causes frames to "jump" in strange and confusing ways, so we
        // don't use it.
        JViewport jv = pvd.getScrollPane().getViewport();
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        Point org = jv.getViewPosition();
        int jx = org.x;
        int jy = org.y;
        Dimension jvSize = jv.getExtentSize();
        int jw = jvSize.width;
        int jh = jvSize.height;
        if ((x+width) > (jx+jw)) {
          x = (jx + jw - width);
        }
        if ((y+height) > (jy+jh)) {
          y = (jy + jh - height);
        }
        setBounds(x, y, width, height);
        pvd.getContentPane().adjustSize();
*/
      }
    }
  }

  /**
   * Hide the title panel.
   */
  private void hideTitlePanel()
  {
    ESlateBasicInternalFrameUI ui = (ESlateBasicInternalFrameUI)getUI();
    JComponent northPane = ui.getNorthPane();
    JComponent southPane = ui.getSouthPane();
    JComponent eastPane = ui.getEastPane();
    JComponent westPane = ui.getWestPane();
    Dimension zero = new Dimension(0, 0);
    if (northPane != null) {
      northPane.setMaximumSize(zero);
      northPane.setMinimumSize(zero);
      northPane.setPreferredSize(zero);
    }
    if (southPane != null) {
      southPane.setMaximumSize(zero);
      southPane.setMinimumSize(zero);
      southPane.setPreferredSize(zero);
    }
    if (eastPane != null) {
      eastPane.setMaximumSize(zero);
      eastPane.setMinimumSize(zero);
      eastPane.setPreferredSize(zero);
    }
    if (westPane != null) {
      westPane.setMaximumSize(zero);
      westPane.setMinimumSize(zero);
      westPane.setPreferredSize(zero);
    }
  }

  /**
   * Set the normal (not highlighted) border.
   */
  void setNormalBorder()
  {
    setBorder(normalBorder);
  }

  /**
   * Set the highlighted border.
   */
  void setHighlightBorder()
  {
    setBorder(highlightBorder);
  }

  /**
   * Selects and deselects a Plug view frame.
   * @param     selected        Set to true to select the frame and to false
   *                            to deselect it.
   */
  public void setSelected(boolean selected) throws PropertyVetoException
  {
    super.setSelected(selected);
    if (selected) {
      getGlassPane().setVisible(false);
      moveToFront();
    }else{
      getGlassPane().setVisible(true);
    }
  }

  /**
   * Returns a reference to the PlugTree displayed in the internal frame.
   * @return    The requested reference.
   */
  PlugTree getTree()
  {
    return tree;
  }

  /**
   * Updates the frame's title bar when the associated component is renamed.
   * @param     e       The event signaling the component name change.
   */
  public void componentNameChanged(ComponentNameChangedEvent e)
  {
    setTitle(e.getNewName());
    repaint();
  }

  /**
   * Deselects the selected plug if it belongs to a component whose E-Slate
   * handle is about to be disposed.
   * @param     e       The event signaling the disposal of the component's
   *                    handle.
   */
  public void disposingHandle(HandleDisposalEvent e)
  {
    Component c = this;
    while ( !(c instanceof PlugViewDesktopPane)) {
      c = c.getParent();
    }
    PlugViewDesktopPane pvd = (PlugViewDesktopPane)c;
    Plug selectedPlug = pvd.selectedPlug;
    if ((selectedPlug) != null && myHandle.equals(selectedPlug.getHandle())) {
      pvd.selectedPlug = null;
      markCompatiblePlugs(selectedPlug, false, false);
    }
  }

  /**
   * Required by ESlateListener interface; not used.
   */
  public void handleDisposed(HandleDisposalEvent e)
  {
  }

  /**
   * Required by ESlateListener interface; not used.
   */
  public void parentChanged(ParentChangedEvent e)
  {
  }

  /**
   * Sets the cursor in a component and all its parents, until a plug view
   * desktop is reached.
   * @param     c       The component from which to start setting the cursor.
   * @param     cursor  The cursor to use.
   */
  private static void setCursorAllTheWayUp(Component c, Cursor cursor)
  {
    c.setCursor(cursor);
    while (!(c instanceof PlugViewDesktopPane)) {
      c = c.getParent();
      c.setCursor(cursor);
    }
  }

  public void mousePressed(MouseEvent e)
  {
    clickX = e.getX();
    clickY = e.getY();
    Component c = this;
    while ( !(c instanceof PlugViewDesktopPane)) {
      c = c.getParent();
    }
    pvd = (PlugViewDesktopPane)c;

    // Check for popup menu mouse button. Since isPopupTrigger does not
    // appear to be working under Windows, we also check explicitly for
    // the right button.
    rightButtonPressed = 
      e.isPopupTrigger() || (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0;
    int selRow = tree.getRowForLocation(e.getX(), e.getY());
    if (selRow != -1) {
      PlugTreeNode node =
        (PlugTreeNode)(tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent());
      if (node != null) {
        Plug p = node.getPlug();
        if (rightButtonPressed) {
          if (p != null) {
            disconnectPopup(p, e.getX(), e.getY());
          }
        }else{
          if (p != null) {
            dragPlug = p;
            pvd.selectedPlug = dragPlug;
            pvd.selectedFrame = PlugViewFrame.this;
            // Deselect any plug that was selected for connection from the plug
            // menu or by double clicking on it, and select the clicked plug.
            if (ESlateMicroworld.firstPlug != null) {
              ESlateMicroworld.firstPlug.setSelected(false);
            }
            // Select the plug on which the mouse was pressed.
            if (dragPlug != null) {
              updatePlugViews(dragPlug);
            }
          }else{
            ESlateHandle h = node.getHandle();
            if (h == null || h.isTopLevelComponent()) {
              setupMove(e);
            }
          }
        } 
      }
    }else{
      if (!rightButtonPressed) {
        setupMove(e);
      }
    }
  }

  /**
   * Initialize things so that we can move the plug view window around.
   */
  private void setupMove(MouseEvent e)
  {
    moving = true;
    setCursorAllTheWayUp(tree, moveCursor);
    clickPt =
      SwingUtilities.convertPoint(tree, new Point(e.getX(), e.getY()), this);
  }

  public void mouseReleased(MouseEvent e)
  {
    if (dragging) {
      //ESlateMicroworld mw = myHandle.getESlateMicroworld();
      ESlateMicroworld.firstPlug = null;
      // Removing the button from the desktop pane in swing 1.1 does not
      // remove it from the display, so hide it before removing it.
      if (dragButton != null) {
        dragButton.setVisible(false);
        pvd.remove(dragButton);
        dragButton = null;
      }
      Plug targetPlug = getPlugAt(e);
      if (targetPlug != null) {
        if (targetPlug.getCompatibilityFlag() == Plug.CAN_CONNECT) {
          setCursorAllTheWayUp(targetTree, connectCursor);
        }else{
          setCursorAllTheWayUp(targetTree, disconnectCursor);
        }
        ESlateMicroworld.connectComponent(dragPlug, targetTree);
        ESlateMicroworld.connectComponent(targetPlug, targetTree);
      }
      dragging = false;
      // Set cursor to normal cursor.
      setCursorAllTheWayUp(targetTree, defaultCursor);
      // Cancel the auto-open task.
      cancelAutoOpen();
    }
    if (dragPlug != null) {
      updatePlugViews(dragPlug);
      dragPlug = null;
    }
    if (moving) {
      setCursorAllTheWayUp(tree, defaultCursor);
      moving = false;
      clickPt = null;
    }
    if (!rightButtonPressed) {
      pvd = null;
    }
  }

  public void mouseDragged(MouseEvent e)
  {
    Component src = (Component)(e.getSource());
    // Ignore small drags on a plug.
    if (!moving && (dragPlug != null) &&
        (Math.abs(clickX-e.getX()) < CLICKTHRESHOLD) &&
        (Math.abs(clickY-e.getY()) < CLICKTHRESHOLD)) {
      return;
    }
    if (dragPlug != null) {
      dragging = true;
      // Translate the location of the event from the coordinates
      // of the PlugTree to those of the desktop pane in which it appears.
      MouseEvent e2 = SwingUtilities.convertMouseEvent(src, e, pvd);
      int x = e2.getX();
      int y = e2.getY();

      PlugButton pb = new PlugButton(dragPlug.getIcon());
      int iconWidth = pb.getIconWidth();
      int iconHeight = pb.getIconHeight();
      if (dragButton == null) {
        // Set cursor to "hand" cursor.
        setCursorAllTheWayUp(pvd, handCursor);

        // Make sure that all possible connections and disconnections are
        // highlighted.
        markCompatiblePlugs(dragPlug, true, true);

        dragButton = new JButton(pb);
        dragButton.setBounds(x - iconWidth / 2, y - iconHeight / 2,
                             iconWidth, iconHeight);
        pvd.add(dragButton, JLayeredPane.DRAG_LAYER);
      }else{
        dragButton.setLocation(x - iconWidth / 2, y - iconHeight / 2);
      }
      pvd.remove(dragButton);
      PlugTreeNode node = getNodeAt(e);
      Plug p;
      ESlateHandle h;
      if (node != null) {
        p = node.getPlug();
        h = node.getHandle();
      }else{
        p = null;
        h = null;
      }
      pvd.add(dragButton);
      if (p == null) {
        setCursorAllTheWayUp(dragButton, handCursor);
        if (h != null) {
          if (pvd.isAutoOpen()) {
            makeChildNodesVisible(node);
          }else{
            if (pvd.isDelayedAutoOpen()) {
              makeChildNodesVisibleAfterDelay(node);
            }
          }
        }
      }else{
        switch (p.getCompatibilityFlag()) {
          case Plug.CANT_CONNECT_OR_DISCONNECT:
            setCursorAllTheWayUp(dragButton, forbiddenCursor);
            break;
          case Plug.CAN_CONNECT:
            setCursorAllTheWayUp(dragButton, disconnectCursor);
            break;
          case Plug.CAN_DISCONNECT:
            setCursorAllTheWayUp(dragButton, scisorsCursor);
            break;
        }
        if (pvd.isAutoOpen()) {
          makeChildNodesVisible(node);
        }else{
          if (pvd.isDelayedAutoOpen()) {
            makeChildNodesVisibleAfterDelay(node);
          }
        }
      }
    }
    if (moving) {
      pt.x = e.getX();
      pt.y = e.getY();
      Point pt2 = SwingUtilities.convertPoint(src, pt, pvd);
      int newX = pt2.x-clickPt.x;
      int newY = pt2.y-clickPt.y;
      setLocation(newX, newY);
      adjustLocations(newX, newY);
      pvd.adjustSize();
    }
  }

  /**
   * Makes the child nodes of a given node visible.
   * @param     node    The node.
   */
  @SuppressWarnings("unchecked")
  private void makeChildNodesVisible(PlugTreeNode node)
  {
    ESlateHandle h = node.getHandle();
    if (h != null) {
      Enumeration childNodes = h.plugs.children();
      while (childNodes.hasMoreElements()) {
        PlugTreeNode n = (PlugTreeNode)(childNodes.nextElement());
        h.makeVisible(n);
      }
    }else{
      Enumeration childNodes = node.getPlug().getPlugTreeNode().children();
      while (childNodes.hasMoreElements()) {
        PlugTreeNode n = (PlugTreeNode)(childNodes.nextElement());
        n.getPlug().makeVisible();
      }
    }
  }

  /**
   * Makes the child nodes of a given node visible after a delay of one
   * second.
   * @param     node    The node.
   */
  private void makeChildNodesVisibleAfterDelay(PlugTreeNode node)
  {
    if (node == null) {
      cancelAutoOpen();
    }else{
      if (!node.equals(lastNodeToOpen)) {
        cancelAutoOpen();
        if (!tree.isExpanded(node)) {
          lastNodeToOpen = node;
          autoOpenTask = new AutoOpenTask(node);
          timer.schedule(autoOpenTask, 1000L);
        }
      }
    }
  }

  /**
   * Cancels the task that was scheduled to automatically open a node after a
   * delay.
   */
  private void cancelAutoOpen()
  {
    if (autoOpenTask != null) {
      autoOpenTask.cancel();
      autoOpenTask = null;
      lastNodeToOpen = null;
    }
  }

  public void mouseMoved(MouseEvent e)
  {
    Component c = tree;
    while (! (c instanceof PlugViewDesktopPane) ) {
      c = c.getParent();
    }
    MouseEvent e2 = SwingUtilities.convertMouseEvent(tree, e, c);
    c = c.getComponentAt(e2.getX(), e2.getY());
    if (!moving && !dragging) {
      if (e.getY() < tree.getRowHeight()) {
        setCursorAllTheWayUp(tree, defaultCursor);
      }else{
        Plug p = getPlugAt(e);
        if (p != null) {
          setCursorAllTheWayUp(targetTree, handCursor);
        }else{
          setCursorAllTheWayUp(targetTree, defaultCursor);
        }
      }
    }
  }

  public void mouseExited(MouseEvent e)
  {
    setCursorAllTheWayUp(this, defaultCursor);
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  /**
   * Pops up a popup menu with various disconnection options for a fgiven plug.
   * @param     p       The plug whose disconnection options will appear.
   * @param     x       The x coordinate where the popup menu will appear.
   * @param     y       The y coordinate where the popup menu will appear.
   */
  private void disconnectPopup(Plug p, int x, int y)
  {
    ResourceBundle resources = ESlateMicroworld.resources;
    ESlateJPopupMenu popup = new ESlateJPopupMenu();
    popup.setLightWeightPopupEnabled(false);
    ESlateJMenu disconnectMenu =
      new ESlateJMenu(resources.getString("disconnect"));
    disconnectMenu.getPopupMenu().setLightWeightPopupEnabled(false);
    JMenuItem allItem =
      disconnectMenu.add(resources.getString("disconnectAllplugs"));
    allItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        disconnectAllPlugs();
      }
    });
    allItem.addMouseListener(new MouseAdapter(){
      public void mouseEntered(MouseEvent e)
      {
        // Highlight all possible disconnections.
        markCompatiblePlugs(disconnectPlug, true, false);
      }
    });
    Plug[] prov = p.getProviders();
    Plug[] dep = p.getDependents();
    int nProv = prov.length;
    int nDep = dep.length;
    int n = nProv + nDep;
    disconnectPlug = p;
    if (n > 0) {
      disconnectMenu.addSeparator();
      Plug[] plugs = new Plug[n];
      System.arraycopy(prov, 0, plugs, 0, nProv);
      System.arraycopy(dep, 0, plugs, nProv, nDep);
      for (int i=0; i<n; i++) {
        JMenuItem item = disconnectMenu.add(new ESlateMenuItem(plugs[i],
          resources.getString("fromPlug") + " " + plugs[i].getName() +
          " " + resources.getString("ofComponent") + " " +
          plugs[i].getHandle().getComponentName())
        );
        item.setActionCommand(plugs[i].getName());
        item.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e)
          {
            disconnectPlug((ESlateMenuItem)(e.getSource()));
          }
        });
        item.addMouseListener(new MouseAdapter(){
          public void mouseEntered(MouseEvent e)
          {
            highlightPopupPlug(((ESlateMenuItem)e.getSource()).getPlug());
          }
        });
      }
    }else{
      allItem.setEnabled(false);
    }
    popup.add(disconnectMenu);
    popup.show(this, x, y);
  }

  /**
   * Highlight the plug corresponding to the currently highlighted entry in the
   * plug disconnection popup menu.
   */
  private void highlightPopupPlug(Plug p)
  {
    // Do not highlight any possible connections or disconnections.
    markCompatiblePlugs(disconnectPlug, false, false);
    // Highlight only the specific plug.
    p.setCompatibilityFlag(disconnectPlug, true, false);
  }

  /**
   * Process the "disconnect from all plugs" entry in the plug disconnection
   * popup menu.
   */
  private void disconnectAllPlugs()
  {
    ResourceBundle resources = ESlateMicroworld.resources;
    int ok = ESlateOptionPane.showConfirmDialog(
      this,
      resources.getString("confirmDisconnect1") + " " +
        disconnectPlug.getName() + " " +
        resources.getString("confirmDisconnect2"),
      resources.getString("confirm"),
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE
    );
    if (ok == JOptionPane.OK_OPTION) {
      disconnectPlug.disconnect();
    }
    updatePlugViewsAfterDisconnection();
    disconnectPlug = null;
  }

  /**
   * Process a "disconnect from a specific plug" entry in the plug
   * disconnection popup menu.
   * @param     item    The menu item selected from the plug disconnection
   *                    popup menu.
   */
  private void disconnectPlug(ESlateMenuItem item)
  {
    ResourceBundle resources = ESlateMicroworld.resources;
    Plug p = item.getPlug();
    int ok = ESlateOptionPane.showConfirmDialog(
      this,
      resources.getString("confirmDisconnect1") + " " +
        disconnectPlug.getName() + " " +
        resources.getString("confirmDisconnect3") + " " +
        p.getName() + 
        resources.getString("confirmDisconnect4") + " " +
        p.getHandle().getComponentName() +
        resources.getString("confirmDisconnect5"),
      resources.getString("confirm"),
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE
    );
    if (ok == JOptionPane.OK_OPTION) {
      try {
        disconnectPlug.disconnectPlug(p);
      } catch (PlugNotConnectedException pnce) {
      }
    }
    updatePlugViewsAfterDisconnection();
    disconnectPlug = null;
  }

  /**
   * Makes sure that all plugs are highlighted properly after making a
   * disconnection from the plug disconnection popup menu.
   */
  private void updatePlugViewsAfterDisconnection()
  {
    pvd.selectedPlug = disconnectPlug;
    updatePlugViews(disconnectPlug);
    rightButtonPressed = false;
    pvd = null;
  }

  /**
   * Makes sure that all plugs are highlighted properly.
   * @param     plug    The Plug relative to which highlighting is made.
   */
  void updatePlugViews(Plug plug)
  {
    Component c = this;
    while ( !(c instanceof PlugViewDesktopPane)) {
      c = c.getParent();
    }
    PlugViewDesktopPane pvd = (PlugViewDesktopPane)c;
    boolean markExisting = pvd.isHighlightingExistingConnections();
    boolean markPossible = pvd.isHighlightingPossibleConnections();
    markCompatiblePlugs(plug, markExisting, markPossible);
  }

  /**
   * Get the plug at the point referenced by a mouse event.
   * @param     e       The mouse event.
   * @return    The requested plug. If there is no plug at the
   *            coordinates of the mouse event, this method returns
   *            null. As a side-effect, a reference to the PlugTree in which
   *            the plug is displayed is placed in the targetTree variable.
   */
  private Plug getPlugAt(MouseEvent e)
  {
    PlugTreeNode node = getNodeAt(e);
    if (node != null) {
      return node.getPlug();
    }else{
      return null;
    }
  }

//  /**
//   * Get the E-Slate handle of the component at the point referenced by a mouse
//   * event.
//   * @param   e       The mouse event.
//   * @return  The requested handle. If there is no component at the
//   *          coordinates of the mouse event, this method returns
//   *          null. As a side-effect, a reference to the PlugTree in which
//   *          the component is displayed is placed in the targetTree
//   *          variable.
//   */
//  private ESlateHandle getHandleAt(MouseEvent e)
//  {
//    PlugTreeNode node = getNodeAt(e);
//    return node.getHandle();
//  }

  /**
   * Get the PlugTreeNode at the point referenced by a mouse event.
   * @param     e       The mouse event.
   * @return    The requested node. If there is no plug or component
   *            at the coordinates of the mouse event, this method returns
   *            null. As a side-effect, a reference to the PlugTree in which
   *            the component is displayed is placed in the targetTree
   *            variable.
   */
  private PlugTreeNode getNodeAt(MouseEvent e)
  {
    Component src = (Component)(e.getSource());
    Component c = src;
    while (! (c instanceof PlugViewDesktopPane) ) {
      c = c.getParent();
    }
    // Translate the location of the event from the coordinates
    // of the PlugTree to those of the desktop pane in which it appears.
    MouseEvent e2 = SwingUtilities.convertMouseEvent(src, e, c);
    int x = e2.getX();
    int y = e2.getY();

    //Find component over which mouse was released.
    if (c != null) {
      c = c.getComponentAt(x,y);
    } else {
      c = null;
    }
    // Find the PlugTree (if any) over which the mouse was released, by
    // descending the hierarchy of components, and translating from the
    // coordinates of the desktop pane to those of the successive
    // components. The number of iterations was found by trial and error.
    Component nextc = c;
    if (c != null && c instanceof PlugViewFrame) {
      for (int i=0; i<4; i++) {
        c = nextc;
        if (c != null) {
          Point loc = c.getLocation();
          x -= loc.x;
          y -= loc.y;
          if (c instanceof Container) {
            nextc = c.getComponentAt(x,y);
            // Root panes do not contain any components--their content
            // panes do--but they do have a location relative to their
            // container.
            if (nextc instanceof JRootPane) {
              loc = nextc.getLocation();
              x -= loc.x;
              y -= loc.y;
              nextc = ((JRootPane)nextc).getContentPane();
            }
          }
        }
      }
    }else{
      c = null;
    }
    // Identify object over which the mouse was released.
    PlugTreeNode node = null;
    if (c != null && c instanceof PlugTree) {
      targetTree = (PlugTree)c;
      int selRow = targetTree.getRowForLocation(x, y);
      if (selRow != -1) {
        node = (PlugTreeNode)
          (targetTree.getPathForLocation(x, y).getLastPathComponent());
      }
    }
    return node;
  }

  /**
   * Marks all plugs in the microworld that can be connected to or disconnected
   * from a given plug.
   * @param     plug            The plug.
   * @param     markExisting    Indicates whether plugs that can be
   *                            disconnected from the given plug will be
   *                            marked.
   * @param     markPossible    Indicates whether plugs that can be connected
   *                            to the given plug will be marked.
   */
  void markCompatiblePlugs(Plug plug, boolean markExisting, boolean markPossible)
  {
    ESlateHandle[] h =
      myHandle.getESlateMicroworld().getAllHandlesInHierarchy();
    for (int i=0; i<h.length; i++) {
      Plug[] p = h[i].getPlugs();
      for (int j=0; j<p.length; j++) {
        markPlug(plug, p[j], markExisting, markPossible);
      }
    }
    plug.setSelected(true);
    for (int i=0; i<h.length; i++) {
      h[i].repaintTrees();
    }
    plug.makeVisible();
    PlugViewDesktop pvd = myHandle.getESlateMicroworld().getPlugViewDeskTop();
    if (pvd.isAutoOpenCompatible()) {
      for (int i=0; i<h.length; i++) {
        Plug[] p = h[i].getPlugs();
        for (int j=0; j<p.length; j++) {
          makeMarkedPlugsVisible(p[j]);
        }
      }
    }
  }

  /*
   * Recursively marks a plug and its sub-plugs if they can be connected to or
   * disconnected from a given plug.
   * @param     plug1   The plug against which compatibility tests will be
   *                    made.
   * @param     plug2   The plug to test recursively for compatibility.
   * @param     markExisting    Indicates whether plugs that can be
   *                            disconnected from the given plug will be
   *                            marked.
   * @param     markPossible    Indicates whether plugs that can be connected
   *                            to the given plug will be marked.
   */
  private static void markPlug(Plug plug1, Plug plug2, boolean markExisting,
                              boolean markPossible)
  {
    plug2.setCompatibilityFlag(plug1, markExisting, markPossible);
    plug2.setSelected(false);
    Plug[] p = plug2.getChildPlugs();
    for (int i=0; i<p.length; i++) {
      markPlug(plug1, p[i], markExisting, markPossible);
    }
  }

  /**
   * Recursively make visible in the plug view window those plugs that have
   * been marked as compatible for connection to or disconnection from
   * another plug.
   * @param     plug    The plug from which to start.
   */
  
  private void makeMarkedPlugsVisible(Plug plug)
  {
    if (plug.getCompatibilityFlag() != Plug.CANT_CONNECT_OR_DISCONNECT) {
      plug.makeVisible();
    }
    Plug[] p = plug.getChildPlugs();
    for (int i=0; i<p.length; i++) {
      makeMarkedPlugsVisible(p[i]);
    }
  }

  /**
   * Adjusts the bounds of a plug view frame to the most appropriate size.
   * @param     x       The x position of the plug view frame.
   * @param     y       The y position of the plug view frame.
   */
  void adjustBounds(int x, int y)
  {
    int w, h;
    Dimension td = tree.getPreferredSize();
/*
    // This code computes the tree's preferred size when automatic frame
    // resize is selected, and  a maximum of 140 pixels wide / 7 rows high
    // when it is not. Using the preferred size seems to be better.
    int defaultWidth = 140;
    w = defaultWidth;
    PlugViewDesktop pvd = myHandle.getESlateMicroworld().getPlugViewDeskTop();
    if (pvd.isResizeFrames()) {
      w = td.width;
      h = td.height;
//      // This code makes the frame smaller than necessary, making plugs
//      // seem to disappear--don't use!
//      JScrollPane sp = pvd.getScrollPane();
//      Dimension maxSize = sp.getViewport().getExtentSize();
//      if (w > maxSize.width) {
//        w = maxSize.width;
//      }
//      if (h > maxSize.height) {
//        h = maxSize.height;
//      }
    }else{
      int nPlugs = myHandle.getPlugs().length;
      if (nPlugs > 7) {
        nPlugs = 7;
      }
      h = (nPlugs + 1) * tree.getRowHeight();
      if (td.width > defaultWidth) {
        // Account for the height of the scroll bar.
        Component c = tree;
        while (!(c instanceof JScrollPane)) {
          c = c.getParent();
        }
        JScrollPane sp = (JScrollPane)c;
        h += sp.getHorizontalScrollBar().getPreferredSize().height;
      }
    }
*/
    w = td.width;
    h = td.height;

    // Our tree cell renderer is so convoluted and self-referencing that
    // it gets hopelessly confused when asked to provide the tree's preferred
    // size at this point. Until we can figure out what's going wrong, we
    // work around the confusion by uninstalling and reinstalling it.
    TreeCellRenderer r = tree.getCellRenderer();
    tree.setCellRenderer(null); // Get out of the car...
    tree.setCellRenderer(r);    // ...and get back in. The car will now start.

    tree.setPreferredSize(new Dimension(w, h));
    td = tree.getPreferredSize();
    Insets fi = getInsets();
    Insets vi = getContentPane().getInsets();
    w = td.width + fi.left + fi.right + vi.left + vi.right;
    h = td.height + fi.top + fi.bottom + vi.top + vi.bottom;
    setBounds(x, y, w, h);
    tree.setPreferredSize(null);
  }

  /**
   * Adjusts the locations of the plug view frames of other components
   * when the component is moved beyond the left or top edge of the desktop,
   * so that the desktop can be automaticly expanded to the left or the top
   * respectively, as this is not handled automaticly by swing.
   */
  private void adjustLocations(int x, int y)
  {
    if (x < 0 || y <0) {
      int xdiff, ydiff;
      if (x < 0) {
        xdiff = x;
      }else{
        xdiff = 0;
      }
      if (y < 0) {
        ydiff = y;
      }else{
        ydiff = 0;
      }
      JInternalFrame[] frames = pvd.getAllFrames();
      for (int i=0; i<frames.length; i++) {
        if (!(frames[i].equals(this))) {
          Point p = frames[i].getLocation();
          frames[i].setLocation(p.x - xdiff, p.y - ydiff);
        }
      }
    }
  }

  /**
   * This class implements the task that will automatically open a node after
   * some delay.
   */
  class AutoOpenTask extends TimerTask
  {
    /**
     * The node to be opened.
     */
    private PlugTreeNode node;

    /**
     * Indicates whether the task is being invoked from a timer (0) or from
     * the AWT dispatcher (1).
     */
    private int invocation;

    /**
     * Create the task.
     * @param   node    The node to be autoopened.
     */
    AutoOpenTask(PlugTreeNode node)
    {
      this.node = node;
      invocation = 0;
    }

    /**
     * Body of the task.
     */
    public void run()
    {
      if (invocation == 0) {
        invocation = 1;
        try {
          SwingUtilities.invokeAndWait(this);
        } catch (Exception e) {
        }
      }else{
        makeChildNodesVisible(node);
        autoOpenTask = null;
      }
    }
  }
}
