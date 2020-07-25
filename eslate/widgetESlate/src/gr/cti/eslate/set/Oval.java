package gr.cti.eslate.set;

import java.awt.*;
import java.awt.geom.*;

import gr.cti.eslate.database.engine.*;

/**
 * This class implements oval shapes (circles and ellipses).
 * @author      Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 * @see         gr.cti.eslate.set.Set
 */
class Oval extends Ellipse2D.Double
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;

  /**
   * X coordinate of the oval's upper left corner.
   */
  int x;
  /**
   * Y coordinate of the oval's upper left corner.
   */
  int y;
  /**
   * X coordinate of the oval's center.
   */
  int xCenter;
  /**
   * Y coordinate of the oval's center.
   */
  int yCenter;
  /**
   * Width of the oval.
   */
  int width;
  /**
   * Height of the oval.
   */
  int height;
  /**
   * Indicates whether the oval is being used to represent a set in a Venn
   * diagram.
   */
  boolean inUse = false;
  /**
   * Indicates whether the oval has been selected by the user.
   */
  boolean selected = false;
  /**
   * Description of the query represented by the oval.
   */
  String query = null;
  /**
   * Point for invoking the contains method repetedly without allocating a new
   * point each time.
   */
  Point2D.Double tmpPt = new Point2D.Double();
  /**
   * The text returned by getQuery when the query text is null.
   */
  private String nullQuery;
  /**
   * The table, queries on which this oval displayes.
   */
  private Table table;

  /**
   * Create an oval shape.
   * @param     table   The table, queries on which this oval displayes.
   */
  Oval(Table table)
  {
    super();
    this.table = table;
  }

  /**
   * Sets the oval's parameters.
   * @param     x       X coordinate of the oval's upper left corner.
   * @param     y       Y coordinate of the oval's upper left corner.
   * @param     width   Width of the oval.
   * @param     height  Y coordinate of the oval's center.
   */
  void setParameters(int x, int y, int width, int height)
  {
    super.x = (double)x;
    super.y = (double)y;
    super.width = (double)width;
    super.height = (double)height;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    xCenter = x + (width / 2);
    yCenter = y + (height / 2);
  }

  /**
   * Checks whether a given point is inside the oval.
   * @param     x       X coordinate of the point to check.
   * @param     y       Y coordinate of the point to check.
   * @return    True if yes, false if no.
   */
  boolean inside(int x, int y)
  {
    tmpPt.x = (double)x;
    tmpPt.y = (double)y;
    return contains(tmpPt);
  }

  /**
   * Checks whether a given point is "quite" inside the oval, i.e., "not very
   * close" to its circumference.
   * @param     x       X coordinate of the point to check.
   * @param     y       Y coordinate of the point to check.
   * @return    True if yes, false if no.
   */
  boolean quiteInside(int x, int y)
  {
    int xx = x - xCenter;
    double x2 = (double)(xx * xx);
    int yy = y - yCenter;
    double y2 = (double)(yy * yy);
    double a2 = (double)(width * width);
    double b2 = (double)(height * height);
    return ((x2 / a2) + (y2 / b2)) <= 0.2025d;
  }

  /**
   * Checks whether a given point is "quite" outside the oval, i.e., outside
   * the oval and "not very close" to its circumference.
   * @param     x       X coordinate of the point to check.
   * @param     y       Y coordinate of the point to check.
   * @return    True if yes, false if no.
   */
  boolean quiteOutside(int x, int y)
  {
    int xx = x - xCenter;
    double x2 = (double)(xx * xx);
    int yy = y - yCenter;
    double y2 = (double)(yy * yy);
    double a2 = (double)(width * width);
    double b2 = (double)(height * height);
    return ((x2 / a2) + (y2 / b2)) > 0.3025d;
  }

  /**
   * Draws the oval.
   * @param     g       The graphics context in which to draw the oval.
   */
  void draw(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    g2.draw(this);

    Color c = g2.getColor();
    float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    Color hi;
    if (hsb[2] > 0.5) {
      hi = Color.black;
    }else{
      hi = Color.white;
    }
    g2.setColor(hi);
    Ellipse2D.Double el = new Ellipse2D.Double(x+1, y+1, width-2, height-2);
    g2.draw(el);
    g2.setColor(c);
  }

  /**
   * Draws the oval filled.
   * @param     g       The graphics context in which to draw the oval.
   */
  void drawFilled(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    g2.fill(this);
  }

  /**
   * Returns the text of the associated query.
   * @return    The requested text. If this text is null, then a query
   *            guaranteed to produce no elements (field1 != [field1]) is
   *            returned. If the associated table has no field, the text set
   *            using the setNullQuery method is returned instead.
   */
  String getQuery()
  {
    if (query == null) {
      try {
        AbstractTableField f = table.getTableField(0);
        return f.getName() + " != [" + f.getName() + "]";
      } catch (InvalidFieldIndexException e) {
        return nullQuery;
      }
    }else{
      return query;
    }
  }

  /**
   * Sets the text returned by getQuery when the query text is null and no
   * query guaranteed to produce no elements can be constructed.
   * @param     text    The text to set.
   */
  void setNullQuery(String text)
  {
    nullQuery = text;
  }

  /**
   * Returns the square of the distance between a given point and the oval's
   * center.
   * @param     x       The x coordinate of the point.
   * @param     y       The y coordinate of the point.
   */
  long distance2(int x, int y)
  {
    long xdiff = xCenter - x;
    long ydiff = yCenter - y;
    return (xdiff * xdiff) + (ydiff * ydiff);
  }
}
