package gr.cti.eslate.set;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.database.engine.*;
import gr.cti.typeArray.*;

/**
 * This class implements a panel for drawing a Venn diagram.
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see gr.cti.eslate.set.Set
 */
public class SetPanel extends JPanel
  implements MouseMotionListener, MouseListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The width of the panel.
   */
  private int width;
  /**
   * The height of the panel.
   */
  private int height;
  /**
   * The width of the panel when the data were placed.
   */
  int placeWidth;
  /**
   * The height of the panel when the data were placed.
   */
  int placeHeight;
  /**
   * The color used during the fill operation.
   */
  private Color fillColor;
  /**
   * A string describing what the user selected.  When repainting the
   * component, this string is checked. If it is not null, then the selected
   * area is highlighted.
   */
  String selected = null;
  /**
   * Color used for drawing the Venn diagram.
   */
  private Color fgColor;
  /**
   * Color used for drawing selected items.
   */
  private Color selColor;
  /**
   * Color used for drawing the item corresponding to the active record.
   */
  private Color activeColor;
  /**
   * Color used for the background of the panel.
   */
  Color bgColor;
  /**
   * The three ovals that the pane will use for the Venn diagram.
   */
  Oval oval[];
  /**
   * Table of ovals in use in LRU order.
   */
  int[] ovalsInUse = {-1, -1, -1};
  /**
   * Number of ovals in use.
   */
  int nOvals = 0;
  /**
   * Text describing the user selection.
   */
  String selText = " ";
  /**
   * The set component to which the set panel belongs.
   */
  Set set;
  /**
   * The database table displayed by the panel.
   */
  Table table;
  /**
   * Information about the elements of the table displayed by the panel.
   */
  ArrayList<SetElement> elements = new ArrayList<SetElement>();
  /**
   * X coordinates of elemnts in the array, specified externally;
   */
  ArrayList<Integer> xCoords = null;
  /**
   * Y coordinates of elemnts in the array, specified externally;
   */
  ArrayList<Integer> yCoords = null;
  /**
   * Array containing the indices of the elements of the table displayed by
   * the panel that were selected after a query.
   */
  private IntBaseArrayDesc selectedElements = null;
  /**
   * A random number generator for creating coordinates.
   */
  private static Random random = new Random();
  /**
   * Indicates whether initial placement of the data failed, because
   * the panel was not being displayed.
   */
  boolean delayedPlacement = false;
  /**
   * The currently selected projection field for each possible subset.
   */
  private int projField[] = new int[8];
  /**
   * The currently selected calculation operation.
   */
  int calcOp = 0;
  /**
   * The currently selected calculation key.
   */
  int calcKey = 0;
  /**
   * The offset between the mark denoting a set element and its description
   */
  private final static int OFFSET = 6;
  /**
   * Indicates that no oval is currently active.
   */
  static int NO_OVAL = -1;
  /**
   * Indicates whether the selected table field is projected on the set panel.
   */
  static boolean project = false;
  /**
   * Element being moved by the user.
   */
  private SetElement elementToMove = null;
  /**
   * Index of element being moved by the user.
   */
  private int elementToMoveIndex = -1;
  /**
   * Localized resources.
   */
  private ResourceBundle resources;
  /**
   * Tolerance in pixels for shakiness of the hands.
   */
  private final int CLICK_THRESHOLD = 2;
  /**
   * Tolerance in pixels for selecting set elements.
   */
  private final int SELECT_THRESHOLD = 3;
  /**
   * An invalid coordinate that will never be received in a mouse event
   * and is larger than the shakiness threshold.
   */
  private final int INVALID = -100;
  /**
   * X coordinate of the point where the mouse was pressed.
   */
  private int x = INVALID;
  /**
   * Y coordinate of the point where the mouse was pressed.
   */
  private int y = INVALID;
  /**
   * Graphics context of current set panel.
   */
  Graphics g;
  /**
   * Font metrics of currrent set panel's font.
   */
  FontMetrics fm;
  /**
   * Vertical offet for printing the text description of each element.
   */
  int yOffset;
  /**
   * String representation of boolean "true".
   */
  String trueString;
  /**
   * String representation of boolean "false".
   */
  String falseString;
  /**
   * Indicates that text should be drawn in a bold font. Used to make the
   * "??" result stand out.
   */
  private boolean doBold = false;
  /**
   * Indicates whether the subset to select belongs in the first (top) oval.
   */
  private boolean selectA;
  /**
   * Indicates whether the subset to select belongs in the second (left) oval.
   */
  private boolean selectB;
  /**
   * Indicates whether the subset to select belongs in the third (right) oval.
   */
  private boolean selectC;
  /**
   * The labels describing the queries that generated the displayed sets.
   */
  SetLabel label[] = new SetLabel[3];
  /**
   * Ellipse for painting the outside of each element, without having to
   * allocate a new object each time.
   */
  private Ellipse2D.Double el1 = new Ellipse2D.Double(0, 0, 7, 7);
  /**
   * Ellipse for painting the inside of each element, without having to
   * allocate a new object each time.
   */
  private Ellipse2D.Double el2 = new Ellipse2D.Double(0, 0, 5, 5);
  /**
   * The currently active oval.
   */
  int activeOval = NO_OVAL;
  /**
   * The area belonging exclusively to oval A.
   */
  private final static int A = 1;
  /**
   * The area belonging exclusively to oval B.
   */
  private final static int B = 2;
  /**
   * The area belonging exclusively to oval C.
   */
  private final static int C = 4;
  /**
   * The area belonging exclusively to ovals A and B.
   */
  private final static int AB = A | B;
  /**
   * The area belonging exclusively to ovals A and C.
   */
  private final static int AC = A | C;
  /**
   * The area belonging exclusively to ovals B and C.
   */
  private final static int BC = B | C;
  /**
   * The area belonging exclusively to ovals A, B and C.
   */
  private final static int ABC = A | B | C;

  /**
   * Create a set panel.
   * @param     table           The table displayed in the panel.
   * @param     set             The set component to which the panel belongs.
   * @param     bgColor         The background color of the panel.
   * @param     fillColor       The color used for the selected subset.
   */
  SetPanel(Table table, Set set, Color bgColor, Color fillColor)
  {
    super();

    resources = set.resources;

    setLayout(null);

    this.bgColor = bgColor;
    this.fillColor = fillColor;

    oval = new Oval[3];
    for (int i=0; i<3; i++) {
      oval[i] = new Oval(table);
      oval[i].setNullQuery(resources.getString("nullQuery"));
    }
    for (int i=0; i<8; i++) {
      projField[i] = 0;
    }
    this.table = table;
    this.set = set;
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  /**
   * Prepare to move an element to a new position, after the user has pressed
   * the mouse over it.
   * @param     x       X coordinate of the point where the user has pressed
   *                    the mouse.
   * @param     y       Y coordinate of the point where the user has pressed
   *                    the mouse.
   */
  public void prepareToMoveElement(int x, int y)
  {
    int size = elements.size();
    int minDist = Integer.MAX_VALUE;
    int closestElement = -1;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      int dist = (e.x - x)*(e.x -x) + (e.y - y)*(e.y - y);
      if (minDist > dist) {
        minDist = dist;
        closestElement = i;
      }
    }
    // Allow for a fuzziness of +/- SELECT_THRESHOLD pixels in either direction.
    if (minDist <= 2*SELECT_THRESHOLD*SELECT_THRESHOLD) {
      elementToMove = elements.get(closestElement);
      elementToMoveIndex = closestElement;
      elementToMove.oldX = elementToMove.x;
      elementToMove.oldY = elementToMove.y;
    }else{
      elementToMove = null;
    }
  }

  /**
   * Paint the panel.
   * @param     g       The graphics context to use for painting.
   */
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Dimension mySize = getSize();
    width = mySize.width;
    height = mySize.height;
    resizeOvals(width, height);
    g.setColor(bgColor);
    SetDisplayPanel sdp = (SetDisplayPanel)getParent();
    sdp.recolor(bgColor);
    for (int i=0; i<3; i++) {
      label[i].recolor(bgColor);
    }
    g.fillRect(0, 0, width, height);
    fgColor = Color.black;
    selColor = Color.blue;
    activeColor = Color.yellow;
    set.statusBar.setText(selText);
    if (selected != null) {
      g.setColor(fillColor);
      // If we have to fill a simple shape, do it using AWT functions instead
      // of using the slow fill operation.
      //
      // Just the first oval: draw it filled with the selection color.
      if (selected.equals("1A")) {
        oval[0].drawFilled(g);
      }else{
        // Just the second oval: draw it filled with the selection color.
        if (selected.equals("1B")) {
          oval[1].drawFilled(g);
        }else{
          // Just the second oval: draw it filled with the selection color.
          if (selected.equals("1C")) {
            oval[2].drawFilled(g);
          }else{
            // Outside the displayed ovals: draw the background in the
            // selection color and then the ovals filled with the background
            // color.
            if (selectedOutside()) {
              sdp.recolor(fillColor);
              for (int i=0; i<3; i++) {
                label[i].recolor(fillColor);
              }
              g.fillRect(0, 0, width, height);
              g.setColor(bgColor);
              if (oval[0].inUse) {
                oval[0].drawFilled(g);
              }
              if (oval[1].inUse) {
                oval[1].drawFilled(g);
              }
              if (oval[2].inUse) {
                oval[2].drawFilled(g);
              }
            }else{
              Area a = null;
              for (int i=0; i<3; i++) {
                if (oval[i].inUse && oval[i].selected) {
                  if (a == null) {
                    a = new Area(oval[i]);
                  }else{
                    a.intersect(new Area(oval[i]));
                  }
                }
              }
              if (a != null) {
                for (int i=0; i<3; i++) {
                  if (oval[i].inUse && !oval[i].selected) {
                    a.subtract(new Area(oval[i]));
                  }
                }
              }
              if (a != null) {
                ((Graphics2D)g).fill(a);
              }
            }
          }
        }
      }
    }else{
      sdp.recolor(bgColor);
      for (int i=0; i<3; i++) {
        label[i].recolor(bgColor);
      }
    }
    for (int i=0; i<3; i++) {
      if (oval[i].inUse) {
        if (activeOval == i) {
          g.setColor(activeColor);
        }else{
          g.setColor(fgColor);
        }
        oval[i].draw(g);
      }
    }

    // Paint the data.
    g.setColor(fgColor);
    if (delayedPlacement) {
      if (xCoords == null || yCoords == null ||
          xCoords.size() == 0 || yCoords.size() == 0) {
        placeData();
      }else{
        placeData(xCoords, yCoords);
      }
    }
    int size = elements.size();
    removeAll();
    fm = g.getFontMetrics();
    yOffset = fm.getAscent() / 2 - 1;
    if (set.calculate) {
      if (oval[0].inUse && oval[1].inUse && oval[2].inUse) {
        calcABC(g);
      }else{
        if (oval[0].inUse && oval[1].inUse) {
          calcAB(g);
        }else{
          if (oval[0].inUse && oval[2].inUse) {
            calcAC(g);
          }else{
            if (oval[1].inUse && oval[2].inUse) {
              calcBC(g);
            }else{
              if (oval[0].inUse) {
                calcA(g);
              }else{
                if (oval[1].inUse) {
                  calcB(g);
                }else{
                  if (oval[2].inUse) {
                    calcC(g);
                  }else{
                    calcNone(g);
                  }
                }
              }
            }
          }
        }
      }
    }else{
      trueString = resources.getString("true");
      falseString = resources.getString("false");
      if (width != placeWidth || height != placeHeight) {
        scalePositions();
      }
      int activeRecord = table.getActiveRecord();
      for (int i=0; i<size; i++) {
        SetElement e = elements.get(i);
        Color outerColor, innerColor;
        if (i == activeRecord) {
          outerColor = activeColor;
        }else{
          if (e.selected) {
            outerColor = selColor;
          }else{
            outerColor = fgColor;
          }
        }
        if (e.selected) {
          innerColor = selColor;
        }else{
          innerColor = fgColor;
        }
        paintElement(
          g, i, yOffset, trueString, falseString, innerColor, outerColor
        );
      }
    }
  }

  /**
   * Paints an element of the set displayed by this panel.
   * @param     g               Graphics context where the element should be
   *                            painted.
   * @param     index           The index number of the element to be painted.
   * @param     yOffset         Vertical offet for printing the text
   *                            description of each element.
   * @param     trueString      String representation of boolean "true".
   * @param     falseString     String representation of boolean "false".
   * @param     innerColor      The color to use to paint the element.
   * @param     outerColor      The color to use to paint the outline of the
   *                            element.
   */
  public void paintElement(Graphics g, int index, int yOffset,
                           String trueString, String falseString,
                           Color innerColor, Color outerColor)
  {
    Graphics2D g2 = (Graphics2D)g;
    SetElement e = elements.get(index);
    g2.setColor(outerColor);
    el1.x = e.x - 3;
    el1.y = e.y - 3;
    g2.fill(el1);
    g2.setColor(innerColor);
    el2.x = e.x - 2;
    el2.y = e.y - 2;
    g2.fill(el2);
    if (project) {
      int pf = getProjField(e.x, e.y);
      if (pf >= 0) {
        Object o = table.riskyGetCell(getProjField(e.x, e.y), index);
        if (o != null) {
          if (o instanceof CImageIcon) {
            Icon ic = ((CImageIcon)o).getIcon();
            ic.paintIcon(this, g, e.x + OFFSET, e.y - ic.getIconHeight() / 2);
          }else{
            if (o instanceof Number) {
              g.drawString(set.nf.format(o), e.x + OFFSET, e.y + yOffset);
            }else{
              if (o instanceof Boolean) {
                g.drawString(
                  ((Boolean)o).booleanValue() ? trueString : falseString,
                  e.x + OFFSET, e.y + yOffset
                );
              }else{
                g.drawString(o.toString(), e.x + OFFSET, e.y + yOffset);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Scales the positions of the various set elements, so that they appear
   * properly after the panel is resided.
   */
  private void scalePositions()
  {
    double xScale = (double)width / (double)placeWidth;
    double yScale = (double)height / (double)placeHeight;
    int size = elements.size();
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      e.x =  (int)((double)e.x * xScale + 0.5);
      e.y =  (int)((double)e.y * yScale + 0.5);
    }
    placeWidth = width;
    placeHeight = height;
  }

  /**
   * Processes mouse clicks.
   * @param     x       The x coordinate of the point where the mouse was
   *                    clicked.
   * @param     y       The y coordinate of the point where the mouse was
   *                    clicked.
   */
  private void processClick(int x, int y)
  {
    if (set.deleteQuery) {
      if (set.delButton != null) {
        set.delButton.setSelected(false);
      }
      if (set.lastButton != null) {
        set.lastButton.setSelected(true);
      }
      removeOvals(x, y);
      set.deleteQuery = false;
    }else{
      if (set.selectOval) {
        selectOval(x, y);
      }else{
        select(x, y);
      }
    }
  }

  /**
   * Toggles the activation of an oval at a given set of coordinates.
   * @param     x       The x coordinate.
   * @param     y       The y coordinate.
   */
  void selectOval(int x, int y)
  {
    int nearest = NO_OVAL;
    long minDist = Long.MAX_VALUE;
    for (int i=0; i<3; i++) {
      if (oval[i].inUse && oval[i].inside(x, y)) {
        long dist = oval[i].distance2(x, y);
        if (dist < minDist) {
          minDist = dist;
          nearest = i;
        }
      }
    }
    selectOval(nearest);
  }

  /**
   * Toggles the activation of a given oval.
   * @param     index   The oval's index.
   */
  void selectOval(int index)
  {
    if (activeOval == index) {
      activeOval = NO_OVAL;
    }else{
      activeOval = index;
    }
    repaint();
  }

  /**
   * Toggle selection of the subset at a given set of coordinate.
   * @param     x       X coordinate of the subset.
   * @param     y       Y coordinate of the subset.
   */
  public void select(int x, int y)
  {
    setSelectedOvals(x, y);
    selectOvals();
  }

  /**
   * Determines to which ovals a selected subset will belong, based on whether
   * a given point is contained in each of the three ovals.
   * @param     x       X coordinate of the point.
   * @param     y       Y coordinate of the point.
   */
  private void setSelectedOvals(int x, int y)
  {
    selectA = oval[0].inside(x, y);
    selectB = oval[1].inside(x, y);
    selectC = oval[2].inside(x, y);
  }

  /**
   * Toggle selection of a subset.
   * @param     a       Subset is in the first (top) ellipse.
   * @param     b       Subset is in the second (left) ellipse.
   * @param     c       Subset is in the third (right) ellipse.
   */
  public void select(boolean a, boolean b, boolean c)
  {
    selectA = a;
    selectB = b;
    selectC = c;
    selectOvals();
  }

  /**
   * Toggle selection of a subset.
   */
  private void selectOvals()
  {
    if (oval[0].inUse && oval[1].inUse && oval[2].inUse) {
      checkABC();
    }else{
      if (oval[0].inUse && oval[1].inUse) {
        checkAB();
      }else{
        if (oval[0].inUse && oval[2].inUse) {
          checkAC();
        }else{
          if (oval[1].inUse && oval[2].inUse) {
            checkBC();
          }else{
            if (oval[0].inUse) {
              checkA();
            }else{
              if (oval[1].inUse) {
                checkB();
              }else{
                if (oval[2].inUse) {
                  checkC();
                }else{
                  checkNone();
                }
              }
            }
          }
        }
      }
    }
    updateParentProjField();
  }

  /**
   * Sets the value of the projection field of the parent Set class to the
   * projection field of the currently selected subset. If there is no
   * currently selected subset, the projection field of the parent is
   * deactivated.
   */
  void updateParentProjField()
  {
    Component parent = this;
    while (!(parent instanceof Set)) {
      parent = parent.getParent();
    }
    int value;
    if (selected != null) {
      value = 
        projField[(selectA ? 1 : 0) | (selectB ? 2 : 0) | (selectC ? 4 : 0)];
    }else{
      value = -1;
    }
    ((Set)parent).changeProjField(value);
  }

  /**
   * Checks in which area the user clicked when all three ovals are being
   * displayed.
   */
  private void checkABC()
  {
    if (selectA && selectB && selectC) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[1].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[2].getQuery() + ")";
      setSelected("3ABC");
      selectOvals(true, true, true);
      return;
    }
    if (selectA && selectB) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[1].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[2].getQuery() + ")";
      setSelected("3AB");
      selectOvals(true, true, false);
      return;
    }
    if (selectA && selectC) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[2].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[1].getQuery() + ")";
      setSelected("3AC");
      selectOvals(true, false, true);
      return;
    }
    if (selectB && selectC) {
      selText =
        "(" + oval[1].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[2].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[0].getQuery() + ")";
      setSelected("3BC");
      selectOvals(false, true, true);
      return;
    }
    if (selectA) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " ((" + oval[1].getQuery() + ") " +
        resources.getString("or") +
        " (" + oval[2].getQuery() + "))";
      setSelected("3A");
      selectOvals(true, false, false);
      return;
    }
    if (selectB) {
      selText =
        "(" + oval[1].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " ((" + oval[0].getQuery() + ") " +
        resources.getString("or") +
        " (" + oval[2].getQuery() + "))";
      setSelected("3B");
      selectOvals(false, true, false);
      return;
    }
    if (selectC) {
      selText =
        "(" + oval[2].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " ((" + oval[0].getQuery() + ") " +
        resources.getString("or") +
        " (" + oval[1].getQuery() + "))";
      setSelected("3C");
      selectOvals(false, false, true);
      return;
    }
    selText =
      resources.getString("not") +
      " ((" + oval[0].getQuery() + ") " +
      resources.getString("or") +
      " (" + oval[1].getQuery() + ") " +
      resources.getString("or") +
      " (" + oval[2].getQuery() + "))";
    setSelected("3");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when the first and second ovals are
   * being displayed.
   */
  private void checkAB()
  {
    if (selectA && selectB) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[1].getQuery() + ")";
      setSelected("2AB");
      selectOvals(true, true, false);
      return;
    }
    if (selectA) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[1].getQuery() + ")";
      setSelected("2A-C");
      selectOvals(true, false, false);
      return;
    }
    if (selectB) {
      selText =
        "(" + oval[1].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[0].getQuery() + ")";
      setSelected("2B-C");
      selectOvals(false, true, false);
      return;
    }
    selText =
      resources.getString("not") +
      " ((" + oval[0].getQuery() + ") " +
      resources.getString("or") +
      " (" + oval[1].getQuery() + "))";
    setSelected("2-C");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when the first and third ovals are
   * being displayed.
   */
  private void checkAC()
  {
    if (selectA && selectC) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[2].getQuery() + ")";
      setSelected("2AC");
      selectOvals(true, false, true);
      return;
    }
    if (selectA) {
      selText =
        "(" + oval[0].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[2].getQuery() + ")";
      setSelected("2A-B");
      selectOvals(true, false, false);
      return;
    }
    if (selectC) {
      selText =
        "(" + oval[2].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[0].getQuery() + ")";
      setSelected("2C-B");
      selectOvals(false, false, true);
      return;
    }
    selText =
      resources.getString("not") +
      " ((" + oval[0].getQuery() + ") " +
      resources.getString("or") +
      " (" + oval[2].getQuery() + "))";
    setSelected("2-B");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when the second and third ovals are
   * being displayed.
   */
  private void checkBC()
  {
    if (selectB && selectC) {
      selText =
        "(" + oval[1].getQuery() + ") " +
        resources.getString("and") +
        " (" + oval[2].getQuery() + ")";
      setSelected("2BC");
      selectOvals(false, true, true);
      return;
    }
    if (selectB) {
      selText =
        "(" + oval[1].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[2].getQuery() + ")";
      setSelected("2B-A");
      selectOvals(false, true, false);
      return;
    }
    if (selectC) {
      selText =
        "(" + oval[2].getQuery() + ") " +
        resources.getString("and") + " " + resources.getString("not") +
        " (" + oval[1].getQuery() + ")";
      setSelected("2C-A");
      selectOvals(false, false, true);
      return;
    }
    selText = resources.getString("not") +
      " ((" + oval[1].getQuery() + ") " +
      resources.getString("or") +
      " (" + oval[2].getQuery() + "))";
    setSelected("2-A");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when only the first oval is
   * being displayed.
   */
  private void checkA()
  {
    if (selectA) {
      selText = oval[0].getQuery();
      setSelected("1A");
      selectOvals(true, false, false);
      return;
    }
    selText = resources.getString("not") + " (" + oval[0].getQuery() + ")";
    setSelected("1-BC");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when only the second oval is
   * being displayed.
   */
  private void checkB()
  {
    if (selectB) {
      selText = oval[1].getQuery();
      setSelected("1B");
      selectOvals(false, true, false);
      return;
    }
    selText = resources.getString("not") + " (" + oval[1].getQuery() + ")";
    setSelected("1-AC");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when only the third oval is
   * being displayed.
   */
  private void checkC()
  {
    if (selectC) {
      selText = oval[2].getQuery();
      setSelected("1C");
      selectOvals(false, false, true);
      return;
    }
    selText = resources.getString("not") + " (" + oval[2].getQuery() + ")";
    setSelected("1-AB");
    selectOvals(false, false, false);
  }

  /**
   * Checks in which area the user clicked when no oval is being displayed.
   * (Easy implementation!!!)
   */
  private void checkNone()
  {
    //selText = resources.getString("all");
    try {
      AbstractTableField f = table.getTableField(0);
      selText = f.getName() + " = [" + f.getName() + "]";
    }catch (InvalidFieldIndexException e) {
      selText = resources.getString("all");
    }
    setSelected("0");
    selectOvals(false, false, false);
  }

  /**
   * Displays calculated data when all three ovals are being
   * displayed.
   * @param     g       The graphics contest on which to display the data.
   */
  private void calcABC(Graphics g)
  {
    int size = elements.size();
    int[] inA = new int[size];
    int[] inB = new int[size];
    int[] inC = new int[size];
    int[] inAB = new int[size];
    int[] inAC = new int[size];
    int[] inBC = new int[size];
    int[] inABC = new int[size];
    int[] outside = new int[size];
    int sizeInA = 0;  
    int sizeInB = 0;
    int sizeInC = 0;
    int sizeInAB = 0;
    int sizeInAC = 0;
    int sizeInBC = 0;
    int sizeInABC = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      boolean insideA = oval[0].inside(e.x, e.y);
      boolean insideB = oval[1].inside(e.x, e.y);
      boolean insideC = oval[2].inside(e.x, e.y);
      if (insideA && insideB && insideC) {
        inABC[sizeInABC++] = i;
      }else{
        if (insideA && insideB) {
          inAB[sizeInAB++] = i;
        }else{
          if (insideA && insideC) {
            inAC[sizeInAC++] = i;
          }else{
            if (insideB && insideC) {
              inBC[sizeInBC++] = i;
            }else{
              if (insideA) {
                inA[sizeInA++] = i;
              }else{
                if (insideB) {
                  inB[sizeInB++] = i;
                }else{
                  if (insideC) {
                    inC[sizeInC++] = i;
                  }else{
                    outside[sizeOutside++] = i;
                  }
                }
              }
            }
          }
        }
      }
    }
    String result;
    result = getCalculationResult(inA, sizeInA);
    centerText(g, result, oval[0].xCenter, (oval[0].y+oval[1].y)/2);
    result = getCalculationResult(inB, sizeInB);
    centerText(g, result, oval[0].x, oval[1].yCenter);
    result = getCalculationResult(inC, sizeInC);
    centerText(g, result, oval[0].x+oval[0].width, oval[2].yCenter);
    result = getCalculationResult(inAB, sizeInAB);
    centerText(g, result, 
      (oval[0].x+oval[2].x)/2,
      (oval[1].yCenter+oval[1].y)/2);
    result = getCalculationResult(inAC, sizeInAC);
    centerText(g, result, 
      (oval[1].x+oval[1].width+oval[0].x+oval[0].width)/2,
      (oval[2].yCenter+oval[2].y)/2);
    result = getCalculationResult(inBC, sizeInBC);
    centerText(g, result,
      (oval[1].x+oval[1].width+oval[2].x)/2,
      (oval[2].y+oval[2].height+oval[0].y+oval[0].height)/2);
    result = getCalculationResult(inABC, sizeInABC);
    centerText(g, result, width/2, height/2);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when the first and second ovals are
   * being displayed.
   * @param     g       The graphics contest on which to display the data.
   */
  private void calcAB(Graphics g)
  {
    int size = elements.size();
    int[] inA = new int[size];
    int[] inB = new int[size];
    int[] inAB = new int[size];
    int[] outside = new int[size];
    int sizeInA = 0;
    int sizeInB = 0;
    int sizeInAB = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      boolean insideA = oval[0].inside(e.x, e.y);
      boolean insideB = oval[1].inside(e.x, e.y);
      if (insideA && insideB) {
        inAB[sizeInAB++] = i;
      }else{
        if (insideA) {
          inA[sizeInA++] = i;
        }else{
          if (insideB) {
            inB[sizeInB++] = i;
          }else{
            outside[sizeOutside++] = i;
          }
        }
      }
    }
    String result;
    result = getCalculationResult(inA, sizeInA);
    centerText(g, result, oval[1].x+oval[1].width, oval[1].y);
    result = getCalculationResult(inB, sizeInB);
    centerText(g, result, oval[0].x, oval[0].y+oval[0].height);
    result = getCalculationResult(inAB, sizeInAB);
    centerText(g, result,
      (oval[0].x+oval[1].x+oval[1].width)/2, 
      (oval[1].y+oval[0].y+oval[0].height)/2);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when the first and third ovals are
   * being displayed.
   * @param     g       The graphics contest on which to display the data.
   */
  private void calcAC(Graphics g)
  {
    int size = elements.size();
    int[] inA = new int[size];
    int[] inC = new int[size];
    int[] inAC = new int[size];
    int[] outside = new int[size];
    int sizeInA = 0;
    int sizeInC = 0;
    int sizeInAC = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      boolean insideA = oval[0].inside(e.x, e.y);
      boolean insideC = oval[2].inside(e.x, e.y);
      if (insideA && insideC) {
        inAC[sizeInAC++] = i;
      }else{
        if (insideA) {
          inA[sizeInA++] = i;
        }else{
          if (insideC) {
            inC[sizeInC++] = i;
          }else{
            outside[sizeOutside++] = i;
          }
        }
      }
    }
    String result;
    result = getCalculationResult(inA, sizeInA);
    centerText(g, result, oval[2].x, oval[2].y);
    result = getCalculationResult(inC, sizeInC);
    centerText(g, result, oval[0].x+oval[0].width, oval[0].y+oval[0].height);
    result = getCalculationResult(inAC, sizeInAC);
    centerText(g, result,
      (oval[0].x+oval[0].width+oval[2].x)/2,
      (oval[0].y+oval[0].height+oval[2].y)/2);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when the second and third ovals are
   * being displayed.
   * @param     g       The graphics contest on which to display the data.
   */
  private void calcBC(Graphics g)
  {
    int size = elements.size();
    int[] inB = new int[size];
    int[] inC = new int[size];
    int[] inBC = new int[size];
    int[] outside = new int[size];
    int sizeInB = 0;
    int sizeInC = 0;
    int sizeInBC = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      boolean insideB = oval[1].inside(e.x, e.y);
      boolean insideC = oval[2].inside(e.x, e.y);
      if (insideB && insideC) {
        inBC[sizeInBC++] = i;
      }else{
        if (insideB) {
          inB[sizeInB++] = i;
        }else{
          if (insideC) {
            inC[sizeInC++] = i;
          }else{
            outside[sizeOutside++] = i;
          }
        }
      }
    }
    String result;
    result = getCalculationResult(inB, sizeInB);
    centerText(g, result, (oval[1].x+oval[2].x)/2, oval[1].yCenter);
    result = getCalculationResult(inC, sizeInC);
    centerText(g, result,
      (oval[1].x+oval[1].width+oval[2].x+oval[2].width)/2,
      oval[2].yCenter);
    result = getCalculationResult(inBC, sizeInBC);
    centerText(g, result,
      (oval[1].x+oval[2].x+oval[2].width)/2, oval[1].yCenter);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when only the first oval is
   * @param     g       The graphics contest on which to display the data.
   * being displayed.
   */
  private void calcA(Graphics g)
  {
    int size = elements.size();
    int[] inA = new int[size];
    int[] outside = new int[size];
    int sizeInA = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      if (oval[0].inside(e.x, e.y)) {
        inA[sizeInA++] = i;
      }else{
        outside[sizeOutside++] = i;
      }
    }
    String result;
    result = getCalculationResult(inA, sizeInA);
    centerText(g, result, oval[0].xCenter, oval[0].yCenter);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when only the second oval is
   * being displayed.
   * @param     g       The graphics contest on which to display the data.
   */
  private void calcB(Graphics g)
  {
    int size = elements.size();
    int[] inB = new int[size];
    int[] outside = new int[size];
    int sizeInB = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      if (oval[1].inside(e.x, e.y)) {
        inB[sizeInB++] = i;
      }else{
        outside[sizeOutside++] = i;
      }
    }
    String result;
    result = getCalculationResult(inB, sizeInB);
    centerText(g, result, oval[1].xCenter, oval[1].yCenter);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when only the third oval is
   * being displayed.
   * @param     g       The graphics contest on which to display the data.
   */
  private void calcC(Graphics g)
  {
    int size = elements.size();
    int[] inC = new int[size];
    int[] outside = new int[size];
    int sizeInC = 0;
    int sizeOutside = 0;
    for (int i=0; i<size; i++) {
      SetElement e = elements.get(i);
      if (oval[2].inside(e.x, e.y)) {
        inC[sizeInC++] = i;
      }else{
        outside[sizeOutside++] = i;
      }
    }
    String result;
    result = getCalculationResult(inC, sizeInC);
    centerText(g, result, oval[2].xCenter, oval[2].yCenter);
    result = getCalculationResult(outside, sizeOutside);
    textAtCorner(g, result);
  }

  /**
   * Displays calculated data when no oval is being displayed.
   * @param     g       The graphics contest on which to display the data.
   * @
   */
  private void calcNone(Graphics g)
  {
    int size = elements.size();
    int[] data = new int[size];
    for (int i=0; i<size; i++) {
      data[i] = i;
    }
    String result;
    result = getCalculationResult(data, size);
    centerText(g, result, width/2, height/2);
  }

  /**
   * Returns the calculation operation selected by the user.
   * @return    The requested operation.
   */
  private int getOp()
  {
    return set.vCalcOp.getSelectedIndex();
  }

  /**
   * Returns the field that the user has selected to use for calculations.
   * @return    The index of the selected field. If no field can be selected,
   *            because the table has no appropriate fields for the selected
   *            calculation operation, this method returns null.
   */
  private int getKey()
  {
    String keyName = (String)(set.vCalcKey.getSelectedItem());
    int key;
    try {
      if (keyName != null) {
        key = table.getFieldIndex(keyName);
      }else{
        key = -1;
      }
    } catch (Exception e) {
      key = -1;
    }
    return key;
  }

  /**
   * Returns the result of a calculation operation.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @result    A string representing the result of the calculation.
   *            If the operation cannot be carried out, this method returns a
   *            localized "??".
   */
  private String getCalculationResult(int[] data, int size)
  {
    int op = getOp();
    int key = getKey();
    String result;

    if (op < Set.TOTAL || key != -1) {
      result = calculate(data, size, op, key);
    }else{
      doBold = true;
      result = resources.getString("??");
    }

    return result;
  }

  /**
   * Displays a string centered at a given pair of coordinates.
   * @param     g       The graphics context where the string should be drawn.
   * @param     text    The string to display.
   * @param     x       X coordinate of the center of the string.
   * @param     y       Y coordinate of the center of the string.
   */
  private void centerText(Graphics g, String text, int x, int y)
  {
    Font oldFont = null;
    if (doBold) {
      oldFont = g.getFont();
      g.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()));
    }
    FontMetrics fm = g.getFontMetrics();
    byte[] bytes = text.getBytes();
    int xOffset = fm.bytesWidth(bytes, 0, bytes.length) / 2;
    int yOffset = fm.getAscent() / 2 - 1;
    g.drawString(text, x-xOffset, y+yOffset);
    if (doBold) {
      g.setFont(oldFont);
      doBold = false;
    }
  }

  /**
   * Displays a string at the upper left corner of the component.
   * @param     g       The graphics context where the string should be drawn.
   * @param     text    The string to display.
   */
  private void textAtCorner(Graphics g, String text)
  {
    Font oldFont = null;
    if (doBold) {
      oldFont = g.getFont();
      g.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()));
    }
    FontMetrics fm = g.getFontMetrics();
    int xPos = OFFSET;
    int yPos = OFFSET + fm.getMaxAscent();
    g.drawString(text, xPos, yPos);
    if (doBold) {
      g.setFont(oldFont);
      doBold = false;
    }
  }


  /**
   * Performs a calculation operation on a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     op      The calculation operation to perform.
   * @param     key     The field on which to perform the calculation.
   * @result    A string representing the result of the calculation.
   */
  private String calculate(int[] data, int size, int op, int key)
  {
    double result;

    try {
      switch (op) {
        case Set.COUNT:
          result = (double)count(data, size);
          break;
        case Set.PERCENT_TOTAL:
          result = percentTotal(data, size);
          break;
        case Set.TOTAL:
          result = total(data, size, key);
          break;
        case Set.MEAN:
          result = mean(data, size, key);
          break;
        case Set.MEDIAN:
          result = median(data, size, key);
          break;
        case Set.SMALLEST:
          result = smallest(data, size, key);
          break;
        case Set.LARGEST:
          result = largest(data, size, key);
          break;
        case Set.PERCENT:
          result = percent(data, size, key);
          break;
        default:
          doBold = true;
          return resources.getString("??");
      }
      return set.nf.format(result);
    } catch (BadCalculationException e) {
      doBold = true;
      return resources.getString("??");
    }
  }

  /**
   * Counts the number of data in a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @return    The requested number.
   */
  private int count(int[] data, int size)
  {
    return size;
  }

  /**
   * Calculates the percentage of the total number of records in the table
   * that the number of data in a set of data represents.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @return    The requested percentage.
   */
  private double percentTotal(int[] data, int size)
  {
    return 100.0d * (double)size / (double)(table.getRecordCount());
  }

  /**
   * Calculates the total of a given table field from a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     key     The field on which to perform the calculation.
   * @return    The requested total.
   */
  private double total(int[] data, int size, int key)
  {
    double total = 0;
    for (int i=0; i<size; i++) {
      Number d = (Number)(table.riskyGetCell(key, data[i]));
      if (d != null) {
        total += d.doubleValue();
      }
    }
    return total;
  }

  /**
   * Calculates the mean of a given table field from a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     key     The field on which to perform the calculation.
   * @return    The requested mean.
   */
  private double mean(int[] data, int size, int key)
  {
    if (size == 0) {
      return 0;
    }else{
      return total(data, size, key) / (double)size;
    }
  }

  /**
   * Calculates the median of a given table field from a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     key     The field on which to perform the calculation.
   * @exception BadCalculationException Thrown if the calculation cannot be
   *                    performed.
   * @return    The requested median.
   */
  private double median(int[] data, int size, int key)
    throws BadCalculationException
  {
    if (size == 0) {
      throw new BadCalculationException();
    }
    double[] value = new double[size];
    for (int i=0; i<size; i++) {
      Number d = (Number)(table.riskyGetCell(key, data[i]));
      if (d != null) {
        value[i] = d.doubleValue();
      }else{
        value[i] = 0.0d;
      }
    }
    // Do a stupid n^2 sort, as the component will probably become unusable
    // because of screen clutter long before we need to resort to an n*log(n)
    // algorithm.
    for (int i=0; i<size-1; i++) {
      for (int j=i+1; j<size; j++) {
        if (value[i] > value[j]) {
          double tmp = value[i];
          value[i] = value[j];
          value[j] = tmp;
        }
      }
    }
    int index;
    if (size % 2 == 0) {
      index = size / 2 - 1;
    }else{
      index = size / 2;
    }
    return value[index];
  }

  /**
   * Calculates the minimum of a given table field from a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     key     The field on which to perform the calculation.
   * @exception BadCalculationException Thrown if the calculation cannot be
   *                    performed.
   * @return    The requested minimum.
   */
  private double smallest(int[] data, int size, int key)
    throws BadCalculationException
  {
    if (size == 0) {
      throw new BadCalculationException();
    }
    double smallest = Integer.MAX_VALUE;
    for (int i=0; i<size; i++) {
      Number d = (Number)(table.riskyGetCell(key, data[i]));
      double dd;
      if (d != null) {
        dd = d.doubleValue();
      }else{
        dd = 0.0d;
      }
      if (smallest > dd) {
        smallest = dd;
      }
    }
    return smallest;
  }

  /**
   * Calculates the maximum of a given table field from a set of data.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     key     The field on which to perform the calculation.
   * @exception BadCalculationException Thrown if the calculation cannot be
   *                    performed.
   * @return    The requested maximum.
   */
  private double largest(int[] data, int size, int key)
    throws BadCalculationException
  {
    if (size == 0) {
      throw new BadCalculationException();
    }
    double largest = Integer.MIN_VALUE;
    for (int i=0; i<size; i++) {
      Number d = (Number)(table.riskyGetCell(key, data[i]));
      double dd;
      if (d != null) {
        dd = d.doubleValue();
      }else{
        dd = 0.0d;
      }
      if (largest < dd) {
        largest = dd;
      }
    }
    return largest;
  }

  /**
   * Calculates the percentage of records from a given set of data, for which
   * a given table field of boolean type is true.
   * @param     data    An array containing the record indices of the data.
   * @param     size    The number of data in the array. (Some of the elements
   *                    in the array may be unused.)
   * @param     key     The field on which to perform the calculation.
   * @exception BadCalculationException Thrown if the calculation cannot be
   *                    performed.
   * @return    The requested percentage.
   */
  private double percent(int[] data, int size, int key)
    throws BadCalculationException
  {
    if (size == 0) {
      throw new BadCalculationException();
    }
    int totalTrue = 0;
    for (int i=0; i<size; i++) {
      Boolean b = (Boolean)(table.riskyGetCell(key, data[i]));
      if (b != null && b.booleanValue()) {
        totalTrue++;
      }
    }
    return 100.0d * (double)totalTrue / (double)size;
  }
  
  /**
   * Sets the string describing what the user selected to a given value and
   * repaints the panel.
   * If this value is equal to the previous value of the string, then
   * the string is set to null. When repainting the component, this string is
   * checked. If it is not null, then the selected area is highlighted.
   * @param     what            A string identifying the selected area.
   * @param     skipSelection   Specifies whether selecting the elements in
   *                            the selected area should be skipped.
   */
  void setSelected(String what, boolean skipSelection)
  {
    if (selected == null || !selected.equals(what)) {
      selected = what;
      if (what == null) {
        selText = " ";
        selectOvals(false, false, false, skipSelection);
      }
    }else{
      selected = null;
      selText = " ";
      selectOvals(false, false, false, skipSelection);
    }
    repaint();
  }

  /**
   * Sets the string describing what the user selected to a given value and
   * repaints the panel.
   * If this value is equal to the previous value of the string, then
   * the string is set to null. When repainting the component, this string is
   * checked. If it is not null, then the selected area is highlighted.
   * After invoking this method, the elements in the selected area are also
   * selected.
   * @param     what    A string identifying the selected area.
   */
  void setSelected(String what)
  {
    setSelected(what, false);
  }

  /**
   * Sets the "selected" flag of the three ovals and optionally selects the
   * elements that are in the intersection of the selected ovals.
   * @param     sel1            Indicates whether the first oval is selectd.
   * @param     sel2            Indicates whether the second oval is selectd.
   * @param     sel3            Indicates whether the third oval is selectd.
   * @param     skipSelection   Specifies whether the selection of the elements
   *                            that are in the intersection of the selected
   *                            ovals should be skipped.
   */
  private void selectOvals(boolean sel1, boolean sel2, boolean sel3,
                           boolean skipSelection)
  {
    oval[0].selected = sel1;
    oval[1].selected = sel2;
    oval[2].selected = sel3;
    if (!skipSelection) {
      selectSelectedElements();
    }
  }

  /**
   * Sets the "selected" flag of the three ovals and selects the
   * elements that are in the intersection of the selected ovals.
   * @param     sel1    Indicates whether the first oval is selectd.
   * @param     sel2    Indicates whether the second oval is selectd.
   * @param     sel3    Indicates whether the third oval is selectd.
   */
  private void selectOvals(boolean sel1, boolean sel2, boolean sel3)
  {
    selectOvals(sel1, sel2, sel3, false);
  }

  /**
   * Sets the "selected" flag of the three ovals, based on the selection
   * string. Used during restoration, to preserve compatibility with previous
   * versions that did not store the "selected" flag.
   */
  void selectOvalsFromSelection()
  {
    if (selected == null)
      selectOvals(false, false, false, true);
    else if (selected.equals("3ABC"))
      selectOvals(true, true, true, true);
    else if (selected.equals("3AB"))
      selectOvals(true, true, false, true);
    else if (selected.equals("3AC"))
      selectOvals(true, false, true, true);
    else if (selected.equals("3BC"))
      selectOvals(false, true, true, true);
    else if (selected.equals("3A"))
      selectOvals(true, false, false, true);
    else if (selected.equals("3B"))
      selectOvals(false, true, false, true);
    else if (selected.equals("3C"))
      selectOvals(false, false, true, true);
    else if (selected.equals("3"))
      selectOvals(false, false, false, true);
    else if (selected.equals("2AB"))
      selectOvals(true, true, false, true);
    else if (selected.equals("2A-C"))
      selectOvals(true, false, false, true);
    else if (selected.equals("2B-C"))
      selectOvals(false, true, false, true);
    else if (selected.equals("2-C"))
      selectOvals(false, false, false, true);
    else if (selected.equals("2AC"))
      selectOvals(true, false, true, true);
    else if (selected.equals("2A-B"))
      selectOvals(true, false, false, true);
    else if (selected.equals("2C-B"))
      selectOvals(false, false, true, true);
    else if (selected.equals("2-B"))
      selectOvals(false, false, false, true);
    else if (selected.equals("2BC"))
      selectOvals(false, true, true, true);
    else if (selected.equals("2B-A"))
      selectOvals(false, true, false, true);
    else if (selected.equals("2C-A"))
      selectOvals(false, false, true, true);
    else if (selected.equals("2-A"))
      selectOvals(false, false, false, true);
    else if (selected.equals("1A"))
      selectOvals(true, false, false, true);
    else if (selected.equals("1-BC"))
      selectOvals(false, false, false, true);
    else if (selected.equals("1B"))
      selectOvals(false, true, false, true);
    else if (selected.equals("1-AC"))
      selectOvals(false, false, false, true);
    else if (selected.equals("1C"))
      selectOvals(false, false, true, true);
    else if (selected.equals("1-AB"))
      selectOvals(false, false, false, true);
    else if (selected.equals("0"))
      selectOvals(false, false, false, true);
    selectA = oval[0].selected;
    selectB = oval[1].selected;
    selectC = oval[2].selected;
  }

  /**
   * Return the names of the fields of the displayed table.
   * @return    The requested names.
   */
  String[] getFieldNames()
  {
    int size = table.getFieldCount();
    String[] names = new String[size];
    TableFieldBaseArray fields = table.getFields();

    for (int i=0; i<size; i++) {
      names[i] = fields.get(i).getName();
    }
    return names;
  }

  /**
   * Return the names of the fields of the displayed table that have a given
   * type.
   * @param     type    The returned fields will have a type that is a
   *                    subclass of this type.
   * @return    The requested names.
   */
  String[] getFieldNames(Class<?> type)
  {
    int nFields = 0;
    int size = table.getFieldCount();
    TableFieldBaseArray fields = table.getFields();
    for (int i=0; i<size; i++) {
      Class<?> c = fields.get(i).getDataType();
      if (type.isAssignableFrom(c)) {
        nFields++;
      }
    }
    String[] names = new String[nFields];
    nFields = 0;
    for (int i=0; i<size; i++) {
      Class<?> c = fields.get(i).getDataType();
      if (type.isAssignableFrom(c)) {
        names[nFields++] = fields.get(i).getName();
      }
    }
    return names;
  }

  /**
   * Place the data in random positions.
   */
  public void placeData()
  {
    int nElements = table.getRecordCount();
    elements = new ArrayList<SetElement>(nElements);
    Dimension size = getSize();
    int w = size.width;
    int h = size.height;
    if (w > 0 && h > 0) {
      placeWidth = w;
      placeHeight = h;
      selectedElements = table.getSelectedSubset();
      for (int i=0; i<nElements; i++) {
        Point pt = pointOutside();
        SetElement e = new SetElement(pt.x, pt.y);
        if (selectedElements.contains(i)) {
          e.selected = true;
        }else{
          e.selected = false;
        }
        elements.add(e);
      }
      selectedElements = null;
      delayedPlacement = false;
      repaint();
    }else{
      delayedPlacement = true;
    }
  }

  /**
   * Place the data in specified positions.
   * @param     x       X positions of the data.
   * @param     y       Y positions of the data.
   */
  void placeData(ArrayList<Integer> x, ArrayList<Integer> y)
  {
    int nElements = table.getRecordCount();
    if (x.size() != nElements || y.size() != nElements) {
      System.out.println(
        "SetPanel.placeData: x.size="+x.size()+" y.size="+y.size()+
        " nElements="+nElements
      );
      return;
    }
    elements = new ArrayList<SetElement>(nElements);
    Dimension size = getSize();
    int w = size.width;
    int h = size.height;
    if (w > 0 && h > 0) {
      selectedElements = table.getSelectedSubset();
      for (int i=0; i<nElements; i++) {
        int xx = x.get(i);
        int yy = y.get(i);
        SetElement e = new SetElement(xx, yy);
        if (selectedElements.contains(i)) {
          e.selected = true;
        }else{
          e.selected = false;
        }
        elements.add(e);
      }
      selectedElements = null;
      delayedPlacement = false;
      xCoords = null;
      yCoords = null;
      repaint();
    }else{
      for (int i=0; i<nElements; i++) {
        int xx = x.get(i);
        int yy = y.get(i);
        SetElement e = new SetElement(xx, yy);
        elements.add(e);
      }
      xCoords = x;
      yCoords = y;
      delayedPlacement = true;
    }
  }

  /**
   * Place the data relative to a given oval.
   * @param     index   The index of the oval to use.
   * @param     inSelSet        If true, the data from the selected set are
   *                            placed inside the given oval. If false, no
   *                            data are placed inside the given oval.
   */
  private void placeData(int index, boolean inSelSet)
  {
    int nElements = table.getRecordCount();
    boolean[] inOval = new boolean[3];
    int nMoved = 0;;
    for (int i=0; i<nElements; i++) {
      SetElement e = elements.get(i);
      inOval[0] = (index != 0) && oval[0].inUse && oval[0].inside(e.x, e.y);
      inOval[1] = (index != 1) && oval[1].inUse && oval[1].inside(e.x, e.y);
      inOval[2] = (index != 2) && oval[2].inUse && oval[2].inside(e.x, e.y);
      if (inSelSet) {
        if (selectedElements.contains(i)) {
          inOval[index] = true;
        }
      }else{
        inOval[index] = false;
      }
      Point pt = pointInside(inOval, e.x, e.y);
      if (pt.x != e.x && pt.y != e.y) {
        nMoved++;
      }
      e.oldX = e.x;
      e.oldY = e.y;
      e.newX = pt.x;
      e.newY = pt.y;
    }
    selectedElements = null;
    setSelected(null, true);
    if (!set.calculate) {
      if (nMoved > 0) {
        animate();
      }
    }else{
      for (int i=0; i<nElements; i++) {
        SetElement e = elements.get(i);
        e.x = e.newX;
        e.y = e.newY;
      }
    }
  }

  /**
   * Repositions an element so that it is inside any of the ovals for which
   * the element satisfies the corresponding queries.
   * @param     index   The index of the element to reposition.
   */
  void repositionElement(int index)
  {
    boolean[] inOval = new boolean[3];
    for (int i=0; i<3; i++) {
      if (oval[i].inUse && (oval[i].query != null)) {
        try {
          LogicalExpression expr = new LogicalExpression(
            table, oval[i].query, LogicalExpression.NEW_SELECTION, false
          );
          IntBaseArrayDesc result = expr.getQueryResults();
          if (result.contains(index)) {
            inOval[i] = true;
          }else{
            inOval[i] = false;
          }
        } catch (InvalidLogicalExpressionException e) {
        }
      }
    }
    SetElement e = elements.get(index);
    Point pt = pointInside(inOval, e.x, e.y);
    e.x = pt.x;
    e.y = pt.y;
    repaint();
  }

  /**
   * Reposition data whose location has been made invalid after deleting an
   * oval.
   */
  private void placeInvalidData()
  {
    int nElements = table.getRecordCount();
    int nMoved = 0;
    for (int i=0; i<nElements; i++) {
      SetElement e = elements.get(i);
      if (e.newX == -1) {
        nMoved++;
        Point pt = pointOutside();
        e.oldX = e.x;
        e.oldY = e.y;
        e.newX = pt.x;
        e.newY = pt.y;
      }else{
        e.oldX = e.x;
        e.oldY = e.y;
        e.newX = e.x;
        e.newY = e.y;
      }
    }
    if (!set.calculate) {
      if (nMoved > 0) {
        animate();
      }
    }else{
      for (int i=0; i<nElements; i++) {
        SetElement e = elements.get(i);
        e.x = e.newX;
        e.y = e.newY;
      }
    }
  }

  /**
   * Deselect data in a set of specified ovals.
   * @param     inOval  An array containing three booleans, specifying
   *                    whether data in the corresponding oval should be
   *                    deselected.
   */
  private void deselectData(boolean[] inOval)
  {
    int nElements = table.getRecordCount();
    IntBaseArray a = new IntBaseArray();
    for (int i=0; i<nElements; i++) {
      SetElement e = elements.get(i);
      for (int j=0; j<3; j++) {
        if (inOval[j] && oval[j].inside(e.x, e.y)) {
          a.add(i);
          break;
        }
      }
    }
    table.removeFromSelectedSubset(a.toArray());
  }

  /**
   * Select data whose location is inside a selected area.
   */
  private void selectSelectedElements()
  {
    boolean selectOutside;
    if (selected != null && selectedOutside()) {
      selectOutside = true;
    }else{
      selectOutside = false;
    }
      
    IntBaseArray a = new IntBaseArray();
    if (selected != null) {
      int nElements = table.getRecordCount();
      for (int i=0; i<nElements; i++) {
        SetElement e = elements.get(i);
        boolean useIt = true;
        if (selectOutside) {
          for (int j=0; j<3; j++) {
            if (oval[j].inUse && oval[j].inside(e.x, e.y)) {
              useIt = false;
              break;
            }
          }
        }else{
          int nInside = 0;
          for (int j=0; j<3; j++) {
            if (oval[j].inUse) {
              if (oval[j].selected) {
                nInside++;
                useIt &= oval[j].inside(e.x, e.y);
              }else{
                useIt &= !oval[j].inside(e.x, e.y);
              }
            }
          }
          if (nInside == 0) {
            useIt = false;
          }
        }
        if (useIt) {
          a.add(i);
        }
      }
    }
    table.setSelectedSubset(a.toArray());
  }

  /**
   * Invalidate the location of all elements contained in a given oval
   * but not contained in any other oval.
   * @param     n       The number of the oval.
   */
  private void invalidateLocations(int n)
  {
    int nElements = table.getRecordCount();
    for (int i=0; i<nElements; i++) {
      SetElement e = elements.get(i);
      boolean valid = false;
      for (int j=0; j<3; j++) {
        if ((j != n) && oval[j].inUse && oval[j].inside(e.x, e.y)) {
          valid = true;
        }
      }
      if (!(oval[n].inside(e.x, e.y))) {
        valid = true;
      }
      if (!valid) {
        e.newX = -1;
      }
    }
  }

  /**
   * Moves the elements on the display to their new positions.
   */
  private void animate()
  {
    int nSteps = 20;
    int nElements = table.getRecordCount();
    boolean iconified = set.isIconified();
    Graphics g = getGraphics();
    try {
      for (int i=1; i<=nSteps; i++) {
        for (int j=0; j<nElements; j++) {
          SetElement e = elements.get(j);
          e.x = e.oldX + (e.newX-e.oldX)*i/nSteps;
          e.y = e.oldY + (e.newY-e.oldY)*i/nSteps;
        }
        if (g != null && !iconified) {
          update(g);
          try {
            Thread.sleep(50);
          } catch (Exception e) {
          }
        }
      }
      if (xCoords != null || yCoords != null) {
        xCoords.clear();
        yCoords.clear();
        for (int i=0; i<nElements; i++) {
          SetElement el = elements.get(i);
          xCoords.add(el.x);
          yCoords.add(el.y);
        }
      }
    } finally {
      if (g != null) {
        g.dispose();
      }
    }
  }

  /**
   * Removes an element from the displayed data.
   * @param     index   The index of the element to remove.
   */
  void removeElement(int index)
  {
    elements.remove(index);
    repaint();
  }

  /**
   * Adds an element in the displayed data.
   * @param     index   The index of the element to add.
   */
  void addElement(int index)
  {
    selectedElements = table.getSelectedSubset();
    Point pt = pointOutside();
    SetElement e = new SetElement(pt.x, pt.y);
    if (selectedElements.contains(index)) {
      e.selected = true;
    }else{
      e.selected = false;
    }
    elements.add(index, e);
    selectedElements = null;
    repaint();
  }

  /**
   * Returns a point outside any of the ovals in use.
   * @return    The requested point.
   */
  private Point pointOutside()
  {
    Dimension size = getSize();
    int w = size.width;
    int h = size.height;
    if (w <= 0 || h <= 0) {
      w = placeWidth;
      h = placeHeight;
      resizeOvals(w, h);
    }
    int x;
    int y;
    boolean ok = false;
    do {
      int xLimit = w * 9 / 10;
      int yLimit = h * 9 /10;
      int xOffset = w * 5 / 100;
      int yOffset = h * 5 /100;
      x = Math.abs(random.nextInt())%xLimit + xOffset;
      y = Math.abs(random.nextInt())%yLimit + yOffset;
      if (oval[0].inUse && !oval[0].quiteOutside(x, y)) {
        continue;
      }
      if (oval[1].inUse && !oval[1].quiteOutside(x, y)) {
        continue;
      }
      if (oval[2].inUse && !oval[2].quiteOutside(x, y)) {
        continue;
      }
      ok = true;
    } while (!ok);
    return new Point(x, y);
  }

  /**
   * Returns a point inside a specified set of ovals.
   * @param     inOval  An array of three boolean flags, indicating
   *                    whether the point should be inside one of the
   *                    corresponding three ovals.
   * @param     x       Initial x coordinate for the point to return.
   * @param     y       Initial y coordinate for the point to return.
   */
  private Point pointInside(boolean[] inOval, int x, int y)
  {
    Dimension size = getSize();
    int w = size.width;
    int h = size.height;
    if (w <= 0 || h <= 0) {
      w = placeWidth;
      h = placeHeight;
      resizeOvals(w, h);
    }
    boolean ok = false;
    do {
      if ((inOval[0] && !oval[0].quiteInside(x, y)) || 
          (!inOval[0] && oval[0].inUse && !oval[0].quiteOutside(x, y)) ||
          (inOval[1] && !oval[1].quiteInside(x, y)) ||
          (!inOval[1] && oval[1].inUse && !oval[1].quiteOutside(x, y)) ||
          (inOval[2] && !oval[2].quiteInside(x, y)) ||
          (!inOval[2] && oval[2].inUse && !oval[2].quiteOutside(x, y))) {
        // Bad point; try again.
        x = Math.abs(random.nextInt()) % w;
        y = Math.abs(random.nextInt()) % h;
      }else{
        ok = true;
      }
    } while (!ok);
    return new Point(x, y);
  }

  /**
   * Resizes the three ovals.
   * @param     width   The width of the component.
   * @param     height  The height of the component.
   */
  private void resizeOvals(int width, int height)
  {
    oval[0].setParameters(width/6, 0, width*4/6, height*7/10);
    oval[1].setParameters(0, height*3/10, width*4/6, height*7/10);
    oval[2].setParameters(width*2/6, height*3/10, width*4/6, height*7/10);
  }

  /**
   * Checks whether the currently selected area is outside any of the
   * currently used ovals.
   * @return    True if yes, false if no.
   */
  private boolean selectedOutside()
  {
    return selected.equals("3") || selected.equals("2-C") ||
           selected.equals("2-B") || selected.equals("2-A") ||
           selected.equals("1-BC") || selected.equals("1-AC") ||
           selected.equals("1-AB") || selected.equals("0");
  }

  /**
   * Returns the index of an oval to use.
   * @return    The index of the first oval that is currently not in use.
   *            If all ovals are in use, the least recently used oval is
   *            returned.
   */
  private int reserveOval()
  {
    int index = -1;
    for (int i=0; i<3; i++) {
      if (!oval[i].inUse) {
        index = i;
        break;
      }
    }
    if (index != -1) {
      ovalsInUse[nOvals] = index;
      nOvals++;
    }else{
      index = ovalsInUse[0];
      ovalsInUse[0] = ovalsInUse[1];
      ovalsInUse[1] = ovalsInUse[2];
      ovalsInUse[2] = index;
    }
    oval[index].inUse = true;
    return index;
  }

  /**
   * Removes the ovals containing a given point.
   * @param     x       X coordinate of the point.
   * @param     y       Y coordinate of the point.
   */
  void removeOvals(int x, int y)
  {
    int nOvals = 0;
    boolean deletedOvals[] = new boolean[3];
    for (int i=0; i<3; i++) {
      if (oval[i].inUse && oval[i].inside(x, y)) {
        invalidateLocations(i);
        freeOval(i);
        nOvals++;
        deletedOvals[i] = true;
      }else{
        deletedOvals[i] = false;
      }
    }
    deselectData(deletedOvals);
    placeInvalidData();
    setSelected(null, true);
  }

  /**
   * Removes an oval.
   * @param     n       The number of the oval: 0 is the first (top) oval,
   *                    1 is the second (left), and 2 is the third (right).
   */
  void removeOval(int n)
  {
    if ((n >= 0) && (n <= 2)) {
      boolean deletedOvals[] = new boolean[3];
      for (int i=0; i<3; i++) {
        deletedOvals[i] = false;
      }
      if (oval[n].inUse) {
        invalidateLocations(n);
        freeOval(n);
        deletedOvals[n] = true;
        deselectData(deletedOvals);
        placeInvalidData();
      }
      setSelected(null, true);
      if (activeOval == n) {
        activeOval = -1;
      }
    }
  }

  /**
   * Frees an oval.
   * @param     index   The index of the oval to free.
   */
  private void freeOval(int index)
  {
    for (int i=0; i<3; i++) {
      if (ovalsInUse[i] == index) {
        oval[index].inUse = false;
        for (int j=i; j<2; j++) {
          ovalsInUse[j] = ovalsInUse[j+1];
        }
        ovalsInUse[2] = -1;
        nOvals--;
        break;
      }
    }
    if (index == activeOval) {
      activeOval = NO_OVAL;
    }
    adjustProjFieldsAfterRemoval(index);
    label[index].hideLabel();
  }

  /**
   * Adjusts the projection fields for each possible subset after the removal
   * of an oval.
   * @param     index   The index of the oval that was removed.
   */
  private void adjustProjFieldsAfterRemoval(int index)
  {
    // This ingenious algorithm works by assigning a diffrent bit to each set,
    // figuring out the bit patterns of the projection field indices that
    // change for a particular example, and applying them to all cases.
    // What it's supposed to do is make each area inherit the projection field
    // of the area with which it is merged. It might even work...
    int thisOval, otherOval1, otherOval2;
    boolean other1InUse, other2InUse;
    switch (index) {
      case 0:
        thisOval = 1;
        otherOval1 = 2;
        otherOval2 = 4;
        other1InUse = oval[1].inUse;
        other2InUse = oval[2].inUse;
        break;
      case 1:
        thisOval = 2;
        otherOval1 = 1;
        otherOval2 = 4;
        other1InUse = oval[0].inUse;
        other2InUse = oval[2].inUse;
        break;
      case 2:
      default:
        thisOval = 4;
        otherOval1 = 1;
        otherOval2 = 2;
        other1InUse = oval[0].inUse;
        other2InUse = oval[1].inUse;
        break;
    }
    projField[thisOval] = projField[0];
    if (other1InUse) {
      projField[thisOval | otherOval1] = projField[otherOval1];
    }else{
      projField[thisOval | otherOval1] = projField[0];
    }
    if (other2InUse) {
      projField[thisOval | otherOval2] = projField[otherOval2];
    }else{
      projField[thisOval | otherOval2] = projField[0];
    }
    if (other1InUse && other2InUse) {
      projField[7] = projField[7 & ~thisOval];
    }else{
      if (other1InUse) {
        projField[7] = projField[otherOval1];
      }else{
        if (other2InUse) {
          projField[7] = projField[otherOval2];
        }else{
          projField[7] = projField[0];
        }
      }
    }
  }

  /**
   * Processes a query
   * @param     query   The text of the query to process.
   */
  void processQuery(String query)
  {
    if (activeOval >= 0) {
      oval[activeOval].query = query;
      selectedElements = table.getSelectedSubset();
      placeData(activeOval, true);
      label[activeOval].setText(query);
      label[activeOval].showLabel();
      updateParentProjField();
    }
  }

  /**
   * Adds a new query to the display.
   */
  void addQuery()
  {
    activeOval = reserveOval();
    oval[activeOval].query = null;
    placeData(activeOval, false);
    label[activeOval].setText("");
    label[activeOval].showLabel();
    updateParentProjField();
  }

  /**
   * Marks the records in the table's selected set as selected.
   */
  void markSelectedElements()
  {
    if (!delayedPlacement) {
      selectedElements = table.getSelectedSubset();
      int nElements = table.getRecordCount();
      for (int i=0; i<nElements; i++) {
        SetElement e = elements.get(i);
        if (selectedElements.contains(i)) {
          e.selected = true;
        }else{
          e.selected = false;
        }
      }
      selectedElements = null;
      repaint();
    }
  }

// Methods for MouseListener interface.

  /**
   * Invoked when a mouse button has been pressed on a component.
   * @param     e       Event received.
   */
  public void mousePressed(MouseEvent e)
  {
    if (set.deleteQuery || set.selecting || set.selectOval) {
      // Record the coordinates of the point where the mouse was clicked,
      // for processing when the mouse is released.
      x = e.getX();
      y = e.getY();
    }else{
      if (!set.calculate) {
        // Move the element under the pointer until the mouse is released.
        trueString = resources.getString("true");
        falseString = resources.getString("false");
        prepareToMoveElement(e.getX(), e.getY());
        // Adjust selection/ativation status of the corresponding record.
        if (elementToMove != null) {
          adjustSelectionActivation(e, elementToMoveIndex);
        }
      }
    }
  }

  /**
   * Invoked when a mouse button has been released on the component.
   * @param     e       Event received.
   */
  public void mouseReleased(MouseEvent e)
  {
    if (elementToMove != null) {
/*
      // On double clicks, make selected element the active record of the
      // table.
      if (e.getClickCount() == 2) {
        //table.setSelectedSubset(elementToMoveIndex);
        table.setActiveRecord(elementToMoveIndex);
      }
*/
      // Finish dragging a set element.
      int x = e.getX();
      int y = e.getY();
      int oldX = elementToMove.oldX;
      int oldY = elementToMove.oldY;
      // Make sure that the new point is inside any ovals in which the old
      // point was, and only those.
      boolean badPoint = false;
      for (int i=0; i<3; i++) {
        if (oval[i].inUse &&
            (oval[i].inside(x, y) != oval[i].inside(oldX, oldY))) {
          badPoint = true;
          break;
        }
      }
      // Make sure the point is inside the visible area, otherwise we
      // shall lose the corresponding element!
      if (x < 0 || x >= width || y < 0 || y >= height) {
        badPoint = true;
      }
      if (badPoint) {
        elementToMove.x = oldX;
        elementToMove.y = oldY;
      }else{
        elementToMove.x = x;
        elementToMove.y = y;
      }
      elementToMove = null;
      repaint();
    }else{
      // Process mouse click.
      // If the point where the mouse was released was not greater than
      // the shakiness threshold, process the event as a mouse click at the
      // point where the mouse was pressed.
      int newX = e.getX();
      int newY = e.getY();
      if ((Math.abs(x - newX) <= CLICK_THRESHOLD) &&
          (Math.abs(y - newY) <= CLICK_THRESHOLD)) {
        processClick(x, y);
        x = INVALID;
        y = INVALID;
      }
    }
  }

  /**
   * Invoked when the mouse has been clicked on the component.
   * @param     e       Event received.
   */
  public void mouseClicked(MouseEvent e)
  {
  }

  /**
   * Invoked when the mouse enters the component.
   * @param     e       Event received.
   */
  public void mouseEntered(MouseEvent e)
  {
  }

  /**
   * Invoked when the mouse exits the component.
   * @param     e       Event received.
   */
  public void mouseExited(MouseEvent e)
  {
  }

// Methods for MouseMotionListener interface.

  /**
   * Invoked when a mouse button is pressed on the component and then dragged.
   * @param     e       Event received.
   */
  public void mouseDragged(MouseEvent e)
  {
    if (elementToMove != null) {
      elementToMove.x = e.getX();
      elementToMove.y = e.getY();
      repaint();
    }
  }

  /**
   * Invoked when the mouse button has been moved on a component (with no
   * buttons no down).
   * @param     e       Event received.
   */
  public void mouseMoved(MouseEvent e)
  {
  }

  /**
   * Adjusts the selection and activation status of the record corresponding
   * to the element on which the user has clicked. Clicking with the left
   * mouse button makes the record active and toggles its selection status
   * of the record. Clicking with the right mouse button activates the record;
   * if the record was already the active record, it is deactivated, instead.
   * @param     e       The mouse event generated when the user clicked on a
   *                    set element.
   * @param     index   The index of the corresponding record.
   */
  private void adjustSelectionActivation(MouseEvent e, int index)
  {
    SetElement se = elements.get(index);
    if (e.isPopupTrigger() ||
        (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
      // Right button pressed
      int activeRecord = table.getActiveRecord();
      if (index == activeRecord) {
        table.setActiveRecord(-1);
      }else{
        table.setActiveRecord(index);
      }
    }else{
      if (se.selected) {
        table.removeFromSelectedSubset(index);
      }else{
        table.addToSelectedSubset(index);
      }
      table.setActiveRecord(index);
    }
  }

  /**
   * Sets the projection field for the currently selected subset.
   * @param     value   The new value of the projection field.
   */
  void setProjField(int value)
  {
    boolean noA = !oval[0].inUse;
    boolean noB = !oval[1].inUse;
    boolean noC = !oval[2].inUse;

    int index = selectedAreaIndex();

    // Depending on which of the 8 possible sub-areas of the diagram the user
    // clicked, and on which ellipses are currently being used, the projection
    // field of more than one sub-area may change. Exhaustively test for each
    // case.
    switch (index) {
      case 0:
        projField[0] = value;
        if (noA) {
          projField[A] = value;
        }
        if (noB) {
          projField[B] = value;
        }
        if (noC) {
          projField[C] = value;
        }
        if (noA && noB) {
          projField[AB] = value;
        }
        if (noA && noC) {
          projField[AC] = value;
        }
        if (noB && noC) {
          projField[BC] = value;
        }
        if (noA && noB && noC) {
          projField[ABC] = value;
        }
        break;
      case A:
        if (noA) {
          projField[0] = value;
        }
        projField[A] = value;
        if (noA && noB) {
          projField[B] = value;
        }
        if (noA && noC) {
          projField[C] = value;
        }
        if (noB) {
          projField[AB] = value;
        }
        if (noC) {
          projField[AC] = value;
        }
        if (noA && noB && noC) {
          projField[BC] = value;
        }
        if (noB && noC) {
          projField[ABC] = value;
        }
        break;
      case B:
        if (noB) {
          projField[0] = value;
        }
        projField[B] = value;
        if (noA && noB) {
          projField[A] = value;
        }
        if (noB && noC) {
          projField[C] = value;
        }
        if (noA) {
          projField[AB] = value;
        }
        if (noC) {
          projField[BC] = value;
        }
        if (noA && noB && noC) {
          projField[AC] = value;
        }
        if (noA && noC) {
          projField[ABC] = value;
        }
        break;
      case C:
        if (noC) {
          projField[0] = value;
        }
        projField[C] = value;
        if (noA && noC) {
          projField[A] = value;
        }
        if (noB && noC) {
          projField[B] = value;
        }
        if (noA) {
          projField[AC] = value;
        }
        if (noB) {
          projField[BC] = value;
        }
        if (noA && noB && noC) {
          projField[AB] = value;
        }
        if (noA && noB) {
          projField[ABC] = value;
        }
        break;
      case AB:
        if (noA && noB) {
          projField[0] = value;
        }
        if (noB) {
          projField[A] = value;
        }
        if (noA) {
          projField[B] = value;
        }
        if (noA && noB && noC) {
          projField[C] = value;
        }
        projField[AB] = value;
        if (noB && noC) {
          projField[AC] = value;
        }
        if (noA && noC) {
          projField[BC] = value;
        }
        if (noC) {
          projField[ABC] = value;
        }
        break;
      case AC:
        if (noA && noC) {
          projField[0] = value;
        }
        if (noC) {
          projField[A] = value;
        }
        if (noA) {
          projField[C] = value;
        }
        if (noA && noB && noC) {
          projField[B] = value;
        }
        projField[AC] = value;
        if (noA && noB) {
          projField[BC] = value;
        }
        if (noB && noC) {
          projField[AB] = value;
        }
        if (noB) {
          projField[ABC] = value;
        }
        break;
      case BC:
        if (noB && noC) {
          projField[0] = value;
        }
        if (noB) {
          projField[C] = value;
        }
        if (noC) {
          projField[B] = value;
        }
        if (noA && noB && noC) {
          projField[A] = value;
        }
        projField[BC] = value;
        if (noA && noB) {
          projField[AC] = value;
        }
        if (noA && noC) {
          projField[AB] = value;
        }
        if (noA) {
          projField[ABC] = value;
        }
        break;
      case ABC:
        if (noA && noB && noC) {
          projField[0] = value;
        }
        if (noB && noC) {
          projField[A] = value;
        }
        if (noA && noC) {
          projField[B] = value;
        }
        if (noA && noB) {
          projField[C] = value;
        }
        if (noC) {
          projField[AB] = value;
        }
        if (noA) {
          projField[BC] = value;
        }
        if (noB) {
          projField[AC] = value;
        }
        projField[ABC] = value;
        break;
      default:
        break;
    }

  }

  /**
   * Sets the projection field for a particular subset.
   * @param     subset  The index of the subset.
   * @param     value   The new value of the projection field.
   */
  void setProjField(int subset, int value)
  {
    projField[subset] = value;
  }

  /**
   * Returns the value of the projection field for the currently selected
   * subset.
   * @return    The requested value. If no subset is currently selected, this
   *            method returns -1;
   */
  int getProjField()
  {
    int index = selectedAreaIndex();
    if (index >= 0) {
      return projField[index];
    }else{
      return -1;
    }
  }

  /**
   * Returns a referance to the array containg the projection fields for all
   * possible subsets.
   * @return    The requested array.
   */
  int[] getProjFields()
  {
    return projField;
  }

  /**
   * Returns the index of the currently selected subset.
   * @return    The requested index. If there is no selected subset, this
   *            method returns -1.
   */
  private int selectedAreaIndex()
  {
    if (selected == null) {
      return -1;
    }else{
      int index = 0;
      if (selectA) {
        index |= 1;
      }
      if (selectB) {
        index |= 2;
      }
      if (selectC) {
        index |= 4;
      }
      return index;
    }
  }

  /**
   * Returns the value of the projection field for the subset containing a
   * given set of coordinates.
   * @param     x       The x coordinate of the subset.
   * @param     y       The y coordinate of the subset.
   */
  int getProjField(int x, int y)
  {
    int index = 0;
    if (oval[0].inside(x, y)) {
      index |= 1;
    }
    if (oval[1].inside(x, y)) {
      index |= 2;
    }
    if (oval[2].inside(x, y)) {
      index |= 4;
    }
    return projField[index];
  }

  /**
   * Sets the color used for the background of the panel.
   * @param     color   The color to use.
   */
  void setBackgroundColor(Color color)
  {
    bgColor = color;
    SetDisplayPanel sdp = (SetDisplayPanel)getParent();
    sdp.repaint();
  }

  /**
   * Returns the color used for the background of the panel.
   * @return    The requested color.
   */
  Color getBackgroundColor()
  {
    return bgColor;
  }

  /**
   * Sets the color used for the selected subset.
   * @param     color   The color to use.
   */
  void setSelectionColor(Color color)
  {
    fillColor = color;
    repaint();
  }

  /**
   * Returns the color used for the selected subset.
   * @return    The requested color.
   */
  Color getSelectionColor()
  {
    return fillColor;
  }

  /**
   * String representation of the panel.
   * @return    The title of the database table displayed by the panel.
   */
  public String toString()
  {
    return table.getTitle();
  }
}
