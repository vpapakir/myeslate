package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.utils.TimeCount;

/**
 * This class provides static methods that parse a time value from a Logo
 * object.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 * @see         gr.cti.eslate.scripting.logo.ChronometerPrimitives
 */
public class ChronometerTimeParse
{
  /**
   * Private empty constructor. There is no need to instanciate members of
   * this class.
   */
  private ChronometerTimeParse()
  {
  }

  /**
   * Parses a time value from a logo object.
   * @param     obj     The object from which to parse the time value.
   *                    This can be either a string of the form HH:MM:SS.mmm
   *                    or a list of the form [HH MM SS mmm]. HH is mandatory,
   *                    remaining fields are optional.
   * @param     return  The parsed time or null if the time cannot be parsed.
   */
  static TimeCount timeParse(LogoObject obj)
  {
    TimeCount tc;
    if (obj instanceof LogoWord) {
      tc = timeParse(((LogoWord)obj).toString());
    }else{
      if (obj instanceof LogoList) {
        tc = timeParse((LogoList)obj);
      }else{
        tc = null;
      }
    }
    if (tc != null) {
      if (tc.hour < 0 || tc.hour >= 100) {
        tc = null;
      }else{
        if (tc.min < 0 || tc.min >= 60) {
          tc = null;
        }else{
          if (tc.sec < 0 || tc.sec >= 60) {
            tc = null;
          }else{
            if (tc.usec < 0 || tc.usec >= 1000000) {
              tc = null;
            }
          }
        }
      }
    }
    return tc;
  }

  /**
   * Parses a time value from a string.
   * @param     s       The string from which to parse the time value.
   *                    This has the form HH:MM:SS.mmm. HH is mandatory,
   *                    remaining fields are optional
   * @param     return  The parsed time or null if the time cannot be parsed.
   */
  private static TimeCount timeParse(String s)
  {
    String hms[] = new String[4];
    hms[0] = "00";
    hms[1] = "00";
    hms[2] = "00";
    hms[3] = "000";
    StringTokenizer t = new StringTokenizer(s, ":");
    int nFields = t.countTokens();
    if (nFields < 1 || nFields > 3) {
      return null;
    }
    for (int i=0; i<nFields; i++) {
      hms[i] = t.nextToken();
    }
    if (nFields == 3) {
      t = new StringTokenizer(hms[2], ".");
      nFields = t.countTokens();
      if (nFields < 1 || nFields > 2) {
        return null;
      }
      hms[2] = t.nextToken();
      if (nFields == 2) {
        hms[3] = t.nextToken();
      }
    }
    TimeCount tc = new TimeCount(0,0,0);
    try {
      tc.hour = Integer.valueOf(hms[0]).intValue();
      tc.min = Integer.valueOf(hms[1]).intValue();
      tc.sec = Integer.valueOf(hms[2]).intValue();
      int length = hms[3].length();
      switch (length) {
        case 0:
          hms[3] = "000";
          break;
        case 1:
          hms[3] = hms[3] + "00";
          break;
        case 2:
          hms[3] = hms[3] + "0";
          break;
        case 3:
          break;
        default:
          return null;
      }
      tc.usec = Integer.valueOf(hms[3]).intValue() * 1000;
    } catch (Exception e) {
      return null;
    }
    return tc;
  }

  /**
   * Parses a time value from a Logo list.
   * @param     l       The list from which to parse the time value.
   *                    This has the form [HH MM SS mmm]. HH is mandatory,
   *                    remaining fields are optional.
   * @param     return  The parsed time or null if the time cannot be parsed.
   */
  private static TimeCount timeParse(LogoList l)
  {
    int length = l.length();
    if (length < 1 || length > 4) {
      return null;
    }
    TimeCount tc = new TimeCount(0, 0, 0);
    try {
      if (length > 0) {
        tc.hour = l.pickInPlace(0).toInteger();
      }
      if (length > 1) {
        tc.min = l.pickInPlace(1).toInteger();
      }
      if (length > 2) {
        tc.sec = l.pickInPlace(2).toInteger();
      }
      if (length > 3) {
        tc.usec = l.pickInPlace(3).toInteger() * 1000;
      }
    } catch (Exception e) {
      return null;
    }
    return tc;
  }
}
