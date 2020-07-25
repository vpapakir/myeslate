package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

/**
 * English language resources for ESlateBeanResource.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 * @author      George Tsironis
 */
public class ESlateBeanBundle extends ListResourceBundle
{
  public Object [][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Events
    //
    {"actionPerformed", "Action Performed"},
    {"componentHidden", "Component hidden"},
    {"componentMoved", "Component moved"},
    {"componentResized", "Component resized"},
    {"componentShown", "Component shown"},
    {"mouseClicked", "Mouse clicked"},
    {"mouseDragged", "Mouse dragged"},
    {"mouseEntered", "Mouse entered"},
    {"mouseExited", "Mouse exited"},
    {"mouseMoved", "Mouse moved"},
    {"mousePressed", "Mouse pressed"},
    {"mouseReleased", "Mouse released"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
    //
    // Properties
    //
    {"opaque", "Opaque"},
    {"selected", "Selected"},
    {"visible", "Visible"},
    {"border", "Border"}
  };
}
