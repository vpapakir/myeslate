package gr.cti.eslate.navigator;

import gr.cti.eslate.utils.*;

/**
 * Property editor for Navigator's "Navigator class" property.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
public class NavigatorClassPropertyEditor extends TaggedStringPropertyEditor
{
  private static String ww = "WebWindow";
  private static String ie = "Internet Explorer";
  private static String swing = "JTextPane";

  public NavigatorClassPropertyEditor()
  {
    super("navigatorClass", getClasses(), getNames());
  }

  private static String[] getClasses()
  {
    String[] classes = new String[3];
    classes[0] = Navigator.WWNavigator;
    classes[1] = Navigator.SWTNavigator;
    classes[2] = Navigator.SwingNavigator;
    return classes;
  }

  private static String[] getNames()
  {
    String[] names = new String[3];
    names[0] = ww;
    names[1] = ie;
    names[2] = swing;
    return names;
  }

}
