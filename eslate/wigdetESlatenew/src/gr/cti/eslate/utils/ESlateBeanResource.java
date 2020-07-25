package gr.cti.eslate.utils;

import java.util.*;

import gr.cti.eslate.base.*;

/**
 * This class provides access to localized versions of the names of various
 * "standard" events and properties, for use with BeanInfo classes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */

public class ESlateBeanResource
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.utils.ESlateBeanBundle",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Returns the localized version of a "standard" event or property.
   * @param     name    The name of the event or property.
   * Supported names are:
   * <TABLE>
   * <TR><TD><B>Name</B></TD><TD><B>Description</B></TD></TR>
   * <TR><TD>border</TD><TD>Border</TD></TR>
   * <TR><TD>componentHidden</TD><TD>Component hidden</TD></TR>
   * <TR><TD>componentShown</TD><TD>Component shown</TD></TR>
   * <TR><TD>mouseEntered</TD><TD>Mouse entered</TD></TR>
   * <TR><TD>mouseExited</TD><TD>Mouse exited</TD></TR>
   * <TR><TD>mouseMoved</TD><TD>Mouse moved</TD></TR>
   * <TR><TD>propertyChange</TD><TD>Property change</TD></TR>
   * <TR><TD>vetoableChange</TD><TD>Vetoable change</TD></TR>
   * </TABLE>
   * @return    The localized name of the specified event or property.
   *            If the specified event of property is not supported,
   *            this method returns null.
   */
  public static String getString(String name)
  {
    try {
      return resources.getString(name);
    } catch (MissingResourceException mre) {
      return null;
    }
  }

  /**
   * Returns the English names of all supported events and properties.
   * @return    The requested names.
   */
  @SuppressWarnings("unchecked")
  public static Enumeration getNames()
  {
    return resources.getKeys();
  }
}
