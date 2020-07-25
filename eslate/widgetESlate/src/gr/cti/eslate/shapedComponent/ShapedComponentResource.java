package gr.cti.eslate.shapedComponent;

import java.util.*;

/**
 * English language localized strings for shaped components.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 24-May-2006
 */
public class ShapedComponentResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"pathDialogTitle", "Edit panel shape"},
    {"add", "Add point"},
    {"del", "Delete point"},
    {"move", "Move point"},
    {"select", "Select point"},
    {"ok", "OK"},
    {"cancel", "Cancel"},
    {"rectangle", "Rectangle"},
    {"ellipse", "Ellipse"},
    {"polygon", "Polygon"},
    {"freehand", "Freehand"},
    {"edit", "Edit"},
    {"notPolygon", "This shape is not a polygon"},
  };
}
