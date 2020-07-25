package gr.cti.eslate.base;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class implements the pane that includes the desktop pane that contains
 * the internal frames, and the menu bar of this frame.
 * associated with E-Slate components in the plug view window.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 6-Jul-2007
 */
public class PlugViewDesktop extends JPanel implements ActionListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The component's menu bar.
   */
  private JMenuBar menuBar;

  /**
   * The content of this pane.
   */
  private PlugViewDesktopPane content = null;

  /**
   * The scroll pane containing the content.
   */
  private JScrollPane scrollPane;

  /**
   * Resources.
   */
  private ResourceBundle resources;

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

  /**
   * The "Resize frames automatically" menu item.
   */
  JCheckBoxMenuItem resizeFramesItem;

  /**
   * The "Show existing connections" menu item.
   */
  JCheckBoxMenuItem showExistItem;

  /**
   * The "Show possible connections" menu item.
   */
  JCheckBoxMenuItem showNewItem;

  /**
   * The "Open nodes automatically" menu item.
   */
  JCheckBoxMenuItem autoOpenItem;

  /**
   * The "Open nodes automatically after delay" menu item.
   */
  JCheckBoxMenuItem delayedAutoOpenItem;

  /**
   * The "Open compatible nodes automatically" menu item.
   */
  JCheckBoxMenuItem autoOpenCompatibleItem;

  /**
   * Construct a new pane with an empty desktop.
   * @param     microworld      The microworld whose plug view this component
   * provides.
   */
  PlugViewDesktop(ESlateMicroworld microworld)
  {
    super();
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.base.ESlateResource",
      ESlateMicroworld.getCurrentLocale()
    );
    setLayout(new BorderLayout());
    menuBar = new JMenuBar();

    JMenu prefsMenu = new JMenu(resources.getString("prefs"));
      resizeFramesItem =
        new JCheckBoxMenuItem(resources.getString("resizeFrames"));
      resizeFramesItem.setActionCommand(RESIZE_FRAMES);
      resizeFramesItem.addActionListener(this);
      prefsMenu.add(resizeFramesItem);

      showExistItem = new JCheckBoxMenuItem(resources.getString("showExist"));
      showExistItem.setActionCommand(SHOW_EXIST);
      showExistItem.addActionListener(this);
      prefsMenu.add(showExistItem);

      showNewItem = new JCheckBoxMenuItem(resources.getString("showNew"));
      showNewItem.setActionCommand(SHOW_NEW);
      showNewItem.addActionListener(this);
      prefsMenu.add(showNewItem);

      autoOpenItem = new JCheckBoxMenuItem(resources.getString("autoOpen"));
      autoOpenItem.setActionCommand(AUTO_OPEN);
      autoOpenItem.addActionListener(this);
      prefsMenu.add(autoOpenItem);

      delayedAutoOpenItem =
        new JCheckBoxMenuItem(resources.getString("delayedAutoOpen"));
      delayedAutoOpenItem.setActionCommand(DELAYED_AUTO_OPEN);
      delayedAutoOpenItem.addActionListener(this);
      prefsMenu.add(delayedAutoOpenItem);

      autoOpenCompatibleItem =
        new JCheckBoxMenuItem(resources.getString("autoOpenCompatible"));
      autoOpenCompatibleItem.setActionCommand(AUTO_OPEN_COMPATIBLE);
      autoOpenCompatibleItem.addActionListener(this);
      prefsMenu.add(autoOpenCompatibleItem);

    menuBar.add(prefsMenu);

    JMenu editMenu = new JMenu(resources.getString("edit"));
      editMenu.add(microworld.undoAction);
      editMenu.add(microworld.redoAction);
    
    menuBar.add(editMenu);

    JMenu toolsMenu = new JMenu(resources.getString("tools"));
      JMenuItem orderItem = new JMenuItem(resources.getString("order"));
      orderItem.setActionCommand(ORDER);
      orderItem.addActionListener(this);
      toolsMenu.add(orderItem);

    menuBar.add(toolsMenu);

    add(menuBar, BorderLayout.NORTH);
    scrollPane = new JScrollPane();
    scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    add(scrollPane, BorderLayout.CENTER);
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e)
      {
        content.adjustSize();
        content.revalidate();
      }
    });
  }

  /**
   * Set the content pane of this panel.
   * @param     content The content to set.
   */
  public void setContentPane(PlugViewDesktopPane content)
  {
    this.content = content;
    resizeFramesItem.setSelected(content.isResizeFrames());
    showExistItem.setSelected(content.isHighlightingExistingConnections());
    showNewItem.setSelected(content.isHighlightingPossibleConnections());
    autoOpenItem.setSelected(content.isAutoOpen());
    autoOpenCompatibleItem.setSelected(content.isAutoOpenCompatible());
    delayedAutoOpenItem.setSelected(content.isDelayedAutoOpen());
    autoOpenCompatibleItem.setSelected(content.isAutoOpenCompatible());
    scrollPane.setViewportView(content);
    scrollPane.setBackground(content.getBackground());
  }

  /**
   * Returns the content pane of this pane.
   * @return    The displayed desktop pane.
   */
  public PlugViewDesktopPane getContentPane()
  {
    return content;
  }

  /**
   * Returns the component's menu bar.
   * @return    The requested menu bar.
   */
  public JMenuBar getMenuBar()
  {
    return menuBar;
  }

  /**
   * Returns the scroll pane of this pane.
   * @return    The requested scroll pane.
   */
  public JScrollPane getScrollPane()
  {
    return scrollPane;
  }

  /**
   * Returns the displayed size of the content.
   */
  public Dimension getDisplayedSize()
  {
    Dimension size = scrollPane.getSize();
    Insets insets = scrollPane.getInsets();
    int width = size.width - insets.left - insets.right;
    int height =size.height - insets.top - insets.bottom;
    return new Dimension(width, height);
  }

  /**
   * Free resources. After invoking this method, the class is unusable.
   */
  public void dispose()
  {
    remove(menuBar);
    menuBar = null;
    resizeFramesItem = null;
    showExistItem = null;
    showNewItem = null;
    autoOpenItem = null;
    delayedAutoOpenItem = null;
    autoOpenCompatibleItem = null;
    remove(scrollPane);
    scrollPane = null;
    content.dispose();
    content = null;
  }

  /**
   * Parse menu actions.
   * @param     e       The action event to parse.
   */
  public void actionPerformed(ActionEvent e)
  {
    String cmd = e.getActionCommand();
    if (cmd.equals(RESIZE_FRAMES)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setResizeFrames(selected);
      content.autoAdjustAll();
      return;
    }
    if (cmd.equals(SHOW_EXIST)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setHighlightingExistingConnections(selected);
      return;
    }
    if (cmd.equals(SHOW_NEW)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setHighlightingPossibleConnections(selected);
      return;
    }
    if (cmd.equals(AUTO_OPEN)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setAutoOpen(selected);
      return;
    }
    if (cmd.equals(DELAYED_AUTO_OPEN)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setDelayedAutoOpen(selected);
      return;
    }
    if (cmd.equals(AUTO_OPEN_COMPATIBLE)) {
      boolean selected = ((JCheckBoxMenuItem)(e.getSource())).isSelected();
      setAutoOpenCompatible(selected);
      return;
    }
    if (cmd.equals(ORDER)) {
      content.order();
      return;
    }
  }

  /**
   * Checks whether frames are resized automatically.
   * @return    True if yes, false if no.
   */
  boolean isResizeFrames()
  {
    return resizeFramesItem.isSelected();
  }

  /**
   * Specifies whether frames are resized automatically.
   * @param     state   True if yes, false if no.
   */
  void setResizeFrames(boolean state)
  {
    resizeFramesItem.setSelected(state);
    content.setResizeFrames(state);
  }

  /**
   * Checks whether existing connections will be highlighted during selection.
   * @return    True if yes, false if no.
   */
  boolean isHighlightingExistingConnections()
  {
    return showExistItem.isSelected();
  }

  /**
   * Specifies whether existing connections will be highlighted during
   * selection.
   * @param     state   True if yes, false if no.
   */
  void setHighlightingExistingConnections(boolean state)
  {
    showExistItem.setSelected(state);
    content.setHighlightingExistingConnections(state);
  }

  /**
   * Checks whether possible connections will be highlighted during selection.
   * @return    True if yes, false if no.
   */
  boolean isHighlightingPossibleConnections()
  {
    return showNewItem.isSelected();
  }

  /**
   * Specifies whether possible connections will be highlighted during
   * selection.
   * @param     state   True if yes, false if no.
   */
  void setHighlightingPossibleConnections(boolean state)
  {
    showNewItem.setSelected(state);
    content.setHighlightingPossibleConnections(state);
  }

  /**
   * Checks whether tree nodes are opened automatically when a plug is
   * dragged over them.
   * @return    True if yes, false if no.
   */
  boolean isAutoOpen()
  {
    return autoOpenItem.isSelected();
  }

  /**
   * Specifies whether tree nodes are opened automatically when a plug is
   * dragged over them.
   * @param     state   True if yes, false if no.
   */
  void setAutoOpen(boolean state)
  {
    autoOpenItem.setSelected(state);
    content.setAutoOpen(state);
    if (state) {
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
    return delayedAutoOpenItem.isSelected();
  }

  /**
   * Specifies whether tree nodes are opened automatically, after a delay,
   * when a plug is dragged over them.
   * @param     state   True if yes, false if no.
   */
  void setDelayedAutoOpen(boolean state)
  {
    delayedAutoOpenItem.setSelected(state);
    content.setDelayedAutoOpen(state);
    if (state) {
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
    return autoOpenCompatibleItem.isSelected();
  }

  /**
   * Specifies whether compatible tree nodes are opened automatically during
   * connection/disconnection.
   * @param     state   True if yes, false if no.
   */
  void setAutoOpenCompatible(boolean state)
  {
    autoOpenCompatibleItem.setSelected(state);
    content.setAutoOpenCompatible(state);
  }

}
