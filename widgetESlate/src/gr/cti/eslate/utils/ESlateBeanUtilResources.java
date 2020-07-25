package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

/**
 * English language resources for ESlateBeanInfo and custom property editors.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class ESlateBeanUtilResources extends ListResourceBundle
{
  public Object [][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Resources for ESlateBeanInfo
    //
    {"propertyChange", "Property change"},
    {"mouseEntered", "Mouse entered"},
    {"mouseExited", "Mouse exited"},
    {"mouseMoved", "Mouse moved"},
    {"vetoableChange", "Vetoable change"},
    {"componentHidden", "Component hidden"},
    {"componentShown", "Component shown"},
    //
    // Resources for TaggedStringPropertyEditor
    //
    {"illegalValue", "Illegal value"}
  };
}

