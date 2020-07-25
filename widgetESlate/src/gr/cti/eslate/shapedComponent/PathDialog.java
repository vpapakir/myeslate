package gr.cti.eslate.shapedComponent;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import gr.cti.eslate.eslateToolBar.*;
import gr.cti.typeArray.*;

/**
 * This class implements the path editing dialog of the clip shape property
 * editor.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.1, 2-Jun-2006
 */
class PathDialog extends JDialog
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The button that adds points to the path.
   */
  private JToggleButton addButton;
  /**
   * The button that removes points from the path.
   */
  private JToggleButton delButton;
  /**
   * The button that moves points in the path.
   */
  private JToggleButton moveButton;
  /**
   * The button that selects points in the path.
   */
  private JToggleButton selButton;
  /**
   * The component where the panel preview is drawn.
   */
  private JLabel preview;
  /**
   * The result returned by the dialog.
   */
  private int result = CANCEL;
  /**
   * Ellipse for painting each point, without having to
   * allocate a new object each time.
   */
  private Ellipse2D.Float el1 = new Ellipse2D.Float(0, 0, 7, 7);
  /**
   * Ellipse for painting the inside of each point, without having to
   * allocate a new object each time.
   */
  private Ellipse2D.Float el2 = new Ellipse2D.Float(0, 0, 5, 5);
  /**
   * The "OK" button of the dialog.
   */
  private JButton okButton;
  /**
   * The component whose path is being edited.
   */
  private ShapedComponent component;
  /**
   * The X coordinates of the points in the path.
   */
  private IntBaseArray xx;
  /**
   * The Y coordinates of the points in the path.
   */
  private IntBaseArray yy;
  /**
   * The index of the currently selected point;
   */
  private int selected = -1;
  /**
   * The index of the point that is currently being moved.
   */
  private int moving = -1;
  /**
   * The dialog is adding points to the path.
   */
  private final static int ADD = 0;
  /**
   * The dialog is removing points from the path.
   */
  private final static int DELETE = 1;
  /**
   * The dialog is moving points in the path.
   */
  private final static int MOVE = 2;
  /**
   * The dialog is selecting points in the path.
   */
  private final static int SELECT = 3;
  /**
   * The current mode of the dialog.
   */
  private int mode = ADD;

  /**
   * Indicates that the user has pressed the "OK" button.
   */
  final static int OK = 0;
  /**
   * Indicates that the user has canceled the dialog.
   */
  final static int CANCEL = 1;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.eslate.shapedComponent.ShapedComponentResource",
      Locale.getDefault()
    );

  /**
   * Build the dialog.
   * @param     owner   The owner of the dialog.
   * @param     panel   The component whose path is being edited.
   */
  PathDialog(Component owner, ShapedComponent component)
  {
    super(
      (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, owner)),
      resources.getString("pathDialogTitle"),
      true
    );

    this.component = component;

    JPanel topPanel = new JPanel(new BorderLayout(0, 0));
    topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));

    xx = new IntBaseArray();
    yy = new IntBaseArray();
    Polygon p = (Polygon)(component.getClipShape());
    if (p == null) {
      p = new Polygon();
    }
    int n = p.npoints;
    int[] x = p.xpoints;
    int[] y = p.ypoints;
    for (int i=0; i<n; i++) {
      xx.add(x[i]);
      yy.add(y[i]);
    }
    selected = n - 1;

    Component comp = (Component)component;
    int w = comp.getWidth();
    int h = comp.getHeight();
    BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics g = im.createGraphics();
/*
    if (component instanceof PanelComponent) {
      PanelComponent panel = (PanelComponent)component;
      boolean transp = panel.isTransparent();
      // We get weird results if the panel is transparent, so we disable
      // transparency before painting the panel into the image.
      if (transp) {
        panel.setTransparent(false);
      }
      panel.simplePrint(g);
      if (transp) {
        panel.setTransparent(true);
      }
    }else{
      comp.print(g);
    }
*/
    component.simplePrint(g);
    g.dispose();
    preview = new JLabel(new ImageIcon(im))
    {
      /**
       * Serialization version.
       */
      final static long serialVersionUID = 1L;
      
      public void paint(Graphics g)
      {
        super.paint(g);
        paintPath((Graphics2D)g);
      }
    };
    preview.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        processClick(e);
      }
      public void mousePressed(MouseEvent e)
      {
        processPress(e);
      }
      public void mouseReleased(MouseEvent e)
      {
        processRelease(e);
      }
    });
    preview.addMouseMotionListener(new MouseMotionAdapter()
    {
      public void mouseDragged(MouseEvent e)
      {
        processDrag(e);
      }
    });
    preview.setBorder(null);

    JScrollPane scrollPane = new JScrollPane(preview);
    scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
    //Dimension scrollPaneSize = new Dimension(300, 300);
    //scrollPane.setPreferredSize(scrollPaneSize);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    Dimension buttonSize = new Dimension(27, 22);
    addButton = new JToggleButton(
      new ImageIcon(PathDialog.class.getResource("images/add.gif"))
    );
    addButton.setEnabled(true);
    addButton.setSelected(true);
    addButton.setToolTipText(resources.getString("add"));
    addButton.setMargin(new Insets(1, 1, 1, 0));
    addButton.setMaximumSize(buttonSize);
    addButton.setPreferredSize(buttonSize);
    addButton.setMinimumSize(buttonSize);
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processAdd(e);
      }
    });
    delButton = new JToggleButton(
      new ImageIcon(PathDialog.class.getResource("images/del.gif"))
    );
    delButton.setEnabled(true);
    delButton.setSelected(false);
    delButton.setToolTipText(resources.getString("del"));
    delButton.setMargin(new Insets(1, 1, 1, 0));
    delButton.setMaximumSize(buttonSize);
    delButton.setPreferredSize(buttonSize);
    delButton.setMinimumSize(buttonSize);
    delButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processDel(e);
      }
    });
    moveButton = new JToggleButton(
      new ImageIcon(PathDialog.class.getResource("images/move.gif"))
    );
    moveButton.setEnabled(true);
    moveButton.setSelected(false);
    moveButton.setToolTipText(resources.getString("move"));
    moveButton.setMargin(new Insets(1, 1, 1, 0));
    moveButton.setText(null);
    moveButton.setMaximumSize(buttonSize);
    moveButton.setPreferredSize(buttonSize);
    moveButton.setMinimumSize(buttonSize);
    moveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processMove(e);
      }
    });
    selButton = new JToggleButton(
      new ImageIcon(PathDialog.class.getResource("images/sel.gif"))
    );
    selButton.setEnabled(true);
    selButton.setSelected(false);
    selButton.setToolTipText(resources.getString("select"));
    selButton.setMargin(new Insets(1, 1, 1, 0));
    selButton.setMaximumSize(buttonSize);
    selButton.setPreferredSize(buttonSize);
    selButton.setMinimumSize(buttonSize);
    selButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        processSelect(e);
      }
    });

    ESlateToolBar toolPanel = new ESlateToolBar(ESlateToolBar.VERTICAL);
    toolPanel.setFloatable(false);
    toolPanel.setCentered(true);
    toolPanel.setBorderPainted(false);
    //toolPanel.setDynamicBorders(false);
    VisualGroup vg = toolPanel.addVisualGroup();
    toolPanel.setSeparation(vg, null);  // Use default separation.
    toolPanel.add(addButton);
    toolPanel.add(delButton);
    toolPanel.add(moveButton);
    toolPanel.add(selButton);
    ESlateButtonGroup bg = toolPanel.addButtonGroup();
    bg.add(addButton);
    bg.add(delButton);
    bg.add(moveButton);
    bg.add(selButton);

    topPanel.add(scrollPane, BorderLayout.CENTER);
    topPanel.add(toolPanel, BorderLayout.EAST);

    buttonSize = new Dimension(90, 25);
    //Color buttonTextColor = new Color(0, 0, 128);
    Insets buttonInsets = new Insets(0, 0, 0, 0);
    okButton = new JButton(resources.getString("ok"));
    okButton.setMargin(buttonInsets);
    okButton.setPreferredSize(buttonSize);
    okButton.setMaximumSize(buttonSize);
    okButton.setMinimumSize(buttonSize);
    //okButton.setForeground(buttonTextColor);
    okButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        result = OK;
        setVisible(false);
      }
    });
    JButton cancelButton = new JButton(resources.getString("cancel"));
    cancelButton.setMargin(buttonInsets);
    cancelButton.setPreferredSize(buttonSize);
    cancelButton.setMaximumSize(buttonSize);
    cancelButton.setMinimumSize(buttonSize);
    //cancelButton.setForeground(buttonTextColor);
    cancelButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        result = CANCEL;
        setVisible(false);
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createGlue());
    buttonPanel.add(okButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(cancelButton);
    buttonPanel.add(Box.createGlue());

    JPanel mainPanel = new JPanel(true);
    mainPanel.setLayout(new BoxLayout(mainPanel, 1));
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(topPanel);
    mainPanel.add(buttonPanel);

    getContentPane().add(mainPanel);
    pack();
    //setSize(new Dimension(320, 452));
    center(owner);
    addWindowListener(new WindowAdapter() {
      boolean gotFocus = false;

      public void windowActivated(WindowEvent we)
      {
        if (!gotFocus) {
          // Set the focus on the "OK" button...
          okButton.requestFocus();
          // ... and ensure that the button will be activated when
          // hitting <ENTER>.
          JRootPane root = SwingUtilities.getRootPane(okButton);
          if (root != null) {
            root.setDefaultButton(okButton);
          }
          gotFocus = true;
        }else{
        }
      }
    });
  }

  /**
   * Processes clicks on the "add" buton.
   * @param     e       The event sent when the "add" button is clicked.
   */
  private void processAdd(ActionEvent e)
  {
    mode = ADD;
    moving = -1;
  }

  /**
   * Processes clicks on the "delete" button.
   * @param     e       The event sent when the "delete" button is clicked.
   */
  private void processDel(ActionEvent e)
  {
    mode = DELETE;
    moving = -1;
  }

  /**
   * Processes clicks on the "move" button.
   * @param     e       The event sent when the "move" button is clicked.
   */
  private void processMove(ActionEvent e)
  {
    mode = MOVE;
    moving = -1;
  }

  /**
   * Processes clicks on the "select" button.
   * @param     e       The event sent when the "select" button is clicked.
   */
  private void processSelect(ActionEvent e)
  {
    mode = SELECT;
    moving = -1;
  }

  /**
   * Centers the dialog relative to its owner.
   * @param     owner   The owner of the dialog. If it is <null>code</code>,
   *                    the dialog will be placed in the center of the screen.
   */
  private void center(Component owner)
  {
    Dimension s = getSize();
    int width = s.width;
    int height = s.height;
    int x, y;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    if (owner == null || !owner.isVisible()) {
      x = (screenWidth - width) / 2;
      y = (screenHeight - height) / 2;
    }else{
      Dimension ownerSize = owner.getSize();
      int ownerWidth = ownerSize.width;
      int ownerHeight = ownerSize.height;
      Point ownerLocation = owner.getLocationOnScreen();
      x = ownerLocation.x + (ownerWidth - width) / 2;
      y = ownerLocation.y + (ownerHeight - height) / 2;
      if ((x + width) > screenWidth) {
        x = screenWidth - width;
      }
      if ((y + height) > screenHeight) {
        y = screenHeight - height;
      }
      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }
    }
    setLocation(x, y);
  }

  /**
   * Displays the dialog.
   * @return    If the user presses the "OK" button, <code>OK</code> is
   *            returned, <code>CANCEL</code> if the dialog is cancelled, and
   *            <code>RESET_TO_DEFAULT</code> if the "reset to default" button
   *            is pressed.
   */
  int showDialog()
  {
    setVisible(true);
    dispose();
    return result;
  }

  /**
   * Disposes the Dialog and then causes show() to return if it is currently
   * blocked.
   */
  public void dispose()
  {
    super.dispose();
    addButton = null;
    delButton = null;
    moveButton = null;
    selButton = null;
    okButton = null;
    component = null;
    preview = null;
  }

  /**
   * Paint the path.
   * @param     g       The graphics context in which to paint the path.
   */
  public void paintPath(Graphics2D g)
  {
    int n = xx.size();
    g.setColor(component.getOutlineColor());
    for (int i=1; i<n; i++) {
      int x0 = xx.get(i-1);
      int y0 = yy.get(i-1);
      int x1 = xx.get(i);
      int y1 = yy.get(i);
      g.drawLine(x0, y0, x1, y1);
    }
    if (n > 1) {
      int x0 = xx.get(n-1);
      int y0 = yy.get(n-1);
      int x1 = xx.get(0);
      int y1 = yy.get(0);
      g.drawLine(x0, y0, x1, y1);
    }
    for (int i=0; i<n; i++) {
      el1.x = xx.get(i) - 3;
      el1.y = yy.get(i) - 3;
      g.fill(el1);
    }
    if (selected >= 0) {
      el2.x = xx.get(selected) - 2;
      el2.y = yy.get(selected) - 2;
      g.setColor(Color.black);
      g.setXORMode(Color.white);
      g.fill(el2);
    }
  }

  /**
   * Process mouse clicks on the preview of the component.
   * @param     e       The event generated by a mouse click.
   */
  private void processClick(MouseEvent e)
  {
    if (mode == MOVE) {
      return;
    }
    int x = e.getX();
    int y = e.getY();
    int ind;
    switch (mode) {
      case ADD:
        selected++;
        xx.add(selected, x);
        yy.add(selected, y);
        repaint();
        break;
      case DELETE:
        ind = getPoint(x, y);
        if (ind >= 0) {
          xx.remove(ind);
          yy.remove(ind);
          if (selected > ind) {
            selected--;
          }else{
            if (selected == ind) {
              if (selected == xx.size()) {
                selected--;
              }
            }
          }
          repaint();
        }
        break;
      case SELECT:
        ind = getPoint(x, y);
        if (ind >= 0) {
          selected = ind;
          repaint();
        }
        break;
    }
  }

  /**
   * Process mouse presses on the preview of the component.
   * @param     e       The event generated by a mouse press.
   */
  private void processPress(MouseEvent e)
  {
   if (mode != MOVE) {
     return;
   }
   int x = e.getX();
   int y = e.getY();
   int ind = getPoint(x, y);
   moving = ind;
  }

  /**
   * Process mouse releases on the preview of the component.
   * @param     e       The event generated by a mouse release.
   */
  private void processRelease(MouseEvent e)
  {
    if (mode != MOVE) {
      return;
    }
    if (moving >= 0) {
      xx.set(moving, e.getX());
      yy.set(moving, e.getY());
    }
    moving = -1;
    repaint();
  }

  /**
   * Process mouse drags on the preview of the pane.
   * @param     e       The event generated by a mouse drag.
   */
  private void processDrag(MouseEvent e)
  {
    if (mode != MOVE) {
      return;
    }
    if (moving >= 0) {
      xx.set(moving, e.getX());
      yy.set(moving, e.getY());
    }
    repaint();
  }

  /**
   * Returns the point closest to a pair of coordinates.
   * @param     x       The X coordinate.
   * @param     y       The Y coordinate.
   * @return    The index of the point. If there is no point near the given
   *            coordinates, -1 is returned.
   */
  private int getPoint(int x, int y)
  {
    int closest = -1;
    int minDist = Integer.MAX_VALUE;
    int n = xx.size();
    for (int i=0; i<n; i++) {
      int x0 = xx.get(i);
      int y0 = yy.get(i);
      int xd = x - x0;
      int yd = y - y0;
      int d = (xd * xd) + (yd * yd);
      if (d < minDist) {
        minDist = d;
        closest = i;
      }
    }
    if (minDist < 9) {
      return closest;
    }else{
      return -1;
    }
  }

  /**
   * Returns the X coordinates of the points in the path.
   * @return    An array containing the X coordinates of the points in the
   *            path.
   */
  int[] getXCoords()
  {
    return xx.toArray();
  }

  /**
   * Returns the Y coordinates of the points in the path.
   * @return    An array containing the X coordinates of the points in the
   *            path.
   */
  int[] getYCoords()
  {
    return yy.toArray();
  }

}
