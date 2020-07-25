package gr.cti.eslate.set;

/**
 * This class encapsulates data associated with an element of a set.
 * @author      Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 * @see         gr.cti.eslate.set.SetPanel
 */
class SetElement
{
  /**
   * X coordinate of the element.
   */
  int x;
  /**
   * Y coordinate of the element.
   */
  int y;
  /**
   * Original x coordinate of the element.
   */
  int oldX;
  /**
   * Original y coordinate of the element.
   */
  int oldY;
  /**
   * X coordinate where the element should move.
   */
  int newX;
  /**
   * Y coordinate where the element should move.
   */
  int newY;
  /**
   * Indicates whether this element is part of a table's selected set.
   */
  boolean selected = false;

  /**
   * Creates an element.
   * @param     x       X coordinate of the element.
   * @param     y       X coordinate of the element.
   */
  SetElement(int x, int y)
  {
    this.x = x;
    this.y = y;
    this.newX = x;
    this.newY = y;
  }
}
