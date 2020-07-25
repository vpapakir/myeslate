package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the travel-for-a-given-time control
 * component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.TimePrimitives
 */
public class TimeResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"GOTIME", "ΠΡΟΧΩΡΑΧΡΟΝΟ"},
    {"TIME", "ΧΡΟΝΟΣ"},
    {"SETTIME", "ΘΕΣΕΧΡΟΝΟ"},
    {"TIMEUNIT", "ΜΟΝΑΔΑΧΡΟΝΟΥ"},
    {"SETTIMEUNIT", "ΘΕΣΕΜΟΝΑΔΑΧΡΟΝΟΥ"},
    {"TIMEUNITS", "ΜΟΝΑΔΕΣΧΡΟΝΟΥ"},
    {"STOPATLANDMARKS", "ΣΤΑΣΗΣΤΑΟΡΟΣΗΜΑ"},
    {"SETSTOPATLANDMARKS", "ΘΕΣΕΣΤΑΣΗΣΤΑΟΡΟΣΗΜΑ"},
    {"badTime", "Κακός χρόνος"},
    {"whichTime", "Παρακαλώ ορίστε ένα χειριστήριο ταξιδιού για δεδομένο χρόνο"}
  };
}
