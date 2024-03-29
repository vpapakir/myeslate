package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the chronometer component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 * @see         gr.cti.eslate.scripting.logo.ChronometerPrimitives
 */
public class ChronometerResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"STARTCHRONOMETER", "ΞΕΚΙΝΑΧΡΟΝΟΜΕΤΡΟ"},
    {"STOPCHRONOMETER", "ΣΤΑΜΑΤΑΧΡΟΝΟΜΕΤΡΟ"},
    {"RESETCHRONOMETER", "ΜΗΔΕΝΙΣΕΧΡΟΝΟΜΕΤΡΟ"},
    {"CHRONOMETERTIME", "ΧΡΟΝΟΣΧΡΟΝΟΜΕΤΡΟΥ"},
    {"SETCHRONOMETERTIME", "ΘΕΣΕΧΡΟΝΟΧΡΟΝΟΜΕΤΡΟΥ"},
    {"CHRONOMETERMILLISECONDS", "ΧΙΛΙΟΣΤΟΔΕΥΤΕΡΟΛΕΠΤΑΧΡΟΝΟΜΕΤΡΟΥ"},
    {"SETCHRONOMETERMILLISECONDS", "ΘΕΣΕΧΙΛΙΟΣΤΟΔΕΥΤΕΡΟΛΕΠΤΑΧΡΟΝΟΜΕΤΡΟΥ"},
    {"CHRONOMETERSECONDS", "ΔΕΥΤΕΡΟΛΕΠΤΑΧΡΟΝΟΜΕΤΡΟΥ"},
    {"SETCHRONOMETERSECONDS", "ΘΕΣΕΔΕΥΤΕΡΟΛΕΠΤΑΧΡΟΝΟΜΕΤΡΟΥ"},
    {"CHRONOMETERMINUTES", "ΛΕΠΤΑΧΡΟΝΟΜΕΤΡΟΥ"},
    {"SETCHRONOMETERMINUTES", "ΘΕΣΕΛΕΠΤΑΧΡΟΝΟΜΕΤΡΟΥ"},
    {"CHRONOMETERHOURS", "ΩΡΕΣΧΡΟΝΟΜΕΤΡΟΥ"},
    {"SETCHRONOMETERHOURS", "ΘΕΣΕΩΡΕΣΧΡΟΝΟΜΕΤΡΟΥ"},
    {"CHRONOMETERRUNNING", "ΧΡΟΝΟΜΕΤΡΟΤΡΕΧΕΙ"},
    {"badTime", "Κακός χρόνος"},
    {"whichChronometer", "Παρακαλώ ορίστε ένα χρονόμετρο"}
  };
}
