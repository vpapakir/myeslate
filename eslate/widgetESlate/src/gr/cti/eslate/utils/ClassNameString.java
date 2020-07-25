package gr.cti.eslate.utils;

import java.lang.String;

/**
 * This class provides static methods for retrieving the class name or
 * package name from a fully qualified class name (e.g., "java.lang.String").
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ClassNameString
{
  /**
   * Private constructor--this class only provides static methods.
   */
  private ClassNameString()
  {
  }

  /**
   * Retrieves the class name from a fully qualified class name.
   * E.g., for "java.lang.String" it will return "String".
   * @param     origString      A fully qualified class name.
   * @return    The class name.
   */
  public static String getClassNameStringWOPackage(String origString)
  {
    int i = origString.lastIndexOf('.');
    if (i >= 0) {
      return origString.substring(i+1);
    }else{
      return origString;
    }
  }

  /**
   * Retrieves the package name from a fully qualified class name.
   * E.g., for "java.lang.String" it will return "java.lang".
   * @param     origString      A fully qualified class name.
   * @return    The package name.
   */
  public static String getPackage(String origString)
  {
    int i = origString.lastIndexOf('.');
    if (i >= 0) {
      return origString.substring(0, i);
    }else{
      return null;
    }
  }
}
