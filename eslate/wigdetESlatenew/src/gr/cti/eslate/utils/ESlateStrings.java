package gr.cti.eslate.utils;

import java.util.*;

/**
 * This class provides various string handling methods.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateStrings
{
  private final static int NORMAL_CHAR = 0;
  private final static int PERCENT = 1;
  private final static int FIRST_DIGIT = 2;
  /**
   * Lower case letters in the Greek locale.
   */
  private static String greekLower =
    "abcdefghijklmnopqrstuvwxyzáÜâãäåÝæçÞèéßúÀêëìíîïüðñóòôõýûàö÷øùþ¢¸¹º¼¾¿";
  /**
   * Equivalent upper case Greek letters in the Greek locale.
   */
  private static String greekUpper =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZÁÁÂÃÄÅÅÆÇÇÈÉÉÚÚÊËÌÍÎÏÏÐÑÓÓÔÕÕÛÛÖ×ØÙÙÁÅÇÉÏÕÙ";

  /**
   * Private empty constructor. There is no need to instanciate members of
   * this class.
   */
  private ESlateStrings()
  {
  }
  
  /**
   * Tests whether a given character is a hexadecimal digit.
   * @param     c       The character to test.
   * @return    True, if the character is a hexadecimal digit, false otherwise.
   */
  private static boolean isHexDigit(char c)
  {
    if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') ||
        (c >= 'A' && c <= 'F')) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Returns the value of a hexadecimal digit.
   * @param     c       The digit whose value os to be rturned.
   * @return    The requested value.
   */
  private static int hexValue(char c)
  {
    if (c >= '0' && c <= '9') {
      return (c - '0');
    }else{
      if (c >= 'a' && c <= 'f') {
        return 10 + (c - 'a');
      }else{
        return 10 + (c - 'A');
      }
    }
  }

  /**
   * Returns a string, derived from a given string, where all sequences of the
   * form %XX, where XX is a pair of hexadecimal digits, have been converted to
   * the corresponding character.
   * @param     s       The stringto convert.
   * @return    The converted string.
   */
  public static String eliminatePercent(String s)
  {
    if (s == null) {
      return null;
    }
    char firstDigit = 0, secondDigit = 0;
    byte[] in = s.getBytes();
    StringBuffer out = new StringBuffer(s.length());
    int state = NORMAL_CHAR;
    for (int i=0; i<in.length; i++) {
      char nextChar = (char)(in[i]);
      switch (state) {
        case NORMAL_CHAR:
          if (nextChar == '%') {
            state = PERCENT;
          }else{
            out.append(nextChar);
          }
          break;
        case PERCENT:
          if (isHexDigit(nextChar)) {
            firstDigit = nextChar;
            state = FIRST_DIGIT;
          }else{
            out.append('%');
            if (nextChar == '%') {
              state = PERCENT;
            }else{
              out.append(nextChar);
              state = NORMAL_CHAR;
            }
          }
          break;
        case FIRST_DIGIT:
          if (isHexDigit(nextChar)) {
            secondDigit = nextChar;
            out.append(
             (char)(16 * hexValue(firstDigit) + hexValue(secondDigit)));
            state = NORMAL_CHAR;
          }else{
            out.append('%');
            out.append(firstDigit);
            if (nextChar == '%') {
              state = PERCENT;
            }else{
              out.append(nextChar);
              state = NORMAL_CHAR;
            }
          }
          break;
      }
    }
    // Deal with left-over characters.
    switch (state) {
      case NORMAL_CHAR:
        break;
      case PERCENT:
        out.append('%');
        break;
      case FIRST_DIGIT:
        out.append('%');
        out.append(firstDigit);
        break;
    }
    return out.toString();
  }

  /**
   * Converts a string into upper case, using the rules of the given locale,
   * applying correctly the rules for the greek locale (capital letters are
   * not accented in capitalized strings, terminal sigma is capitalized
   * correctly).
   * @param     s       The string to capitalize.
   * @param     locale  The locale whose rules to use for capitalization.
   * @return    The capitalized string.
   */
  public static String upperCase(String s, Locale locale)
  {
    if (locale.getLanguage().equals("el") ||
        locale.getLanguage().equals("el_GR")) {
      char[] sc = s.toCharArray();
      for (int i=0; i<sc.length; i++){
        int p = greekLower.indexOf(sc[i]);
        if (p != -1) {
          sc[i] = greekUpper.charAt(p);
        }
      }
      // Try to uppercase using Java's routine in case we missed something
      // (for example english chars).
      return new String(sc).toUpperCase(locale);
    }else{
      return s.toUpperCase(locale);
    }
  }

  /**
   * Determines whether two strings are equal, ignoring case and considering
   * the rules of a given locale, applying correctly the rules for the greek
   * locale (capital letters are not accented in capitalized strings, terminal
   * sigma is capitalized correctly).
   * @param     s1      The first string to compare.
   * @param     s2      The second string to compare.
   * @param     l       The locale whose rules should be applied.
   * @return    True if the two strings are equal, false otherwise.
   */
  public static boolean areEqualIgnoreCase(String s1, String s2, Locale l)
  {
    return upperCase(s1, l).equals(upperCase(s2, l));
  }

  /**
   * Compares two strings, ignoring case and considering the rules of a given
   * locale, applying correctly the rules for the greek locale (capital
   * letters are not accented in capitalized strings, terminal sigma is
   * capitalized correctly).
   * @param     s1      The first string to compare.
   * @param     s2      The second string to compare.
   * @param     l       The locale whose rules should be applied.
   * @return    0 if s1 is equal to s2, a value less than 0 if s1 is
   *            lexicographically less than s2, and a value greater than 0 if
   *            s1 is lexicographically greater than s2,
   */
  public static int compareIgnoreCase(String s1, String s2, Locale l)
  {
    return upperCase(s1, l).compareTo(upperCase(s2, l));
  }
}
