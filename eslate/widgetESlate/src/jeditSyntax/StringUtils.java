package jeditSyntax;

/**
 * String utilities.
 */
public class StringUtils
{
  private StringUtils()
  {
  }

  /**
   * Convert a character to upper case, taking Greek stress marks into
   * concideration, by removing them, if found.
   * @param     c       The character to convert.
   * @return    The upper case version of the character, with any Greek stress
   *            marks removed.
   */
  public static char toUpperCase(char c)
  {
    switch (c) {
      case 'α':
        return 'Α';
      case 'β':
        return 'Β';
      case 'γ':
        return 'Γ';
      case 'δ':
        return 'Δ';
      case 'ε':
        return 'Ε';
      case 'ζ':
        return 'Ζ';
      case 'η':
        return 'Η';
      case 'θ':
        return 'Θ';
      case 'ι':
        return 'Ι';
      case 'κ':
        return 'Κ';
      case 'λ':
        return 'Λ';
      case 'μ':
        return 'Μ';
      case 'ν':
        return 'Ν';
      case 'ξ':
        return 'Ξ';
      case 'ο':
        return 'Ο';
      case 'π':
        return 'Π';
      case 'ρ':
        return 'Ρ';
      case 'σ':
        return 'Σ';
      case 'τ':
        return 'Τ';
      case 'υ':
        return 'Υ';
      case 'φ':
        return 'Φ';
      default:
        return Character.toUpperCase(c);
    }
  }

  /**
   * Convert a string to upper case, taking Greek stress marks into
   * concideration, by removing them, if found.
   * @param     s       The string to convert.
   * @return    The upper case version of the string, with any Greek stress
   *            marks removed.
   */
  public static String toUpperCase(String s)
  {
    int n = s.length();
    StringBuilder sb = new StringBuilder();
    for (int i=0; i<n; i++) {
      sb.append(toUpperCase(s.charAt(i)));
    }
    return sb.toString();
  }
}
