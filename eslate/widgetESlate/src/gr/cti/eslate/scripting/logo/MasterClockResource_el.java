package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the master clock component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 30-May-2006
 * @see         gr.cti.eslate.scripting.logo.MasterClockPrimitives
 */
public class MasterClockResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"STARTTICK", "ΞΕΚΙΝΑΧΡΟΝΙΣΤΗ"},
    {"STOPTICK", "ΣΤΑΜΑΤΑΧΡΟΝΙΣΤΗ"},
    {"MASTERCLOCKMINIMUMSCALE", "ΕΛΑΧΙΣΤΗΚΛΙΜΑΚΑΧΡΟΝΙΣΤΗ"},
    {"SETMASTERCLOCKMINIMUMSCALE", "ΘΕΣΕΕΛΑΧΙΣΤΗΚΛΙΜΑΚΑΧΡΟΝΙΣΤΗ"},
    {"MASTERCLOCKMAXIMUMSCALE", "ΜΕΓΙΣΤΗΚΛΙΜΑΚΑΧΡΟΝΙΣΤΗ"},
    {"SETMASTERCLOCKMAXIMUMSCALE", "ΘΕΣΕΜΕΓΙΣΤΗΚΛΙΜΑΚΑΧΡΟΝΙΣΤΗ"},
    {"MASTERCLOCKSCALE", "ΚΛΙΜΑΚΑΧΡΟΝΙΣΤΗ"},
    {"SETMASTERCLOCKSCALE", "ΘΕΣΕΚΛΙΜΑΚΑΧΡΟΝΙΣΤΗ"},
    {"MASTERCLOCKRUNNING", "ΧΡΟΝΙΣΤΗΣΤΡΕΧΕΙ"},
    {"badMinScale", "Κακή ελάχιστη κλίμακα"},
    {"badMaxScale", "Κακή μέγιστη κλίμακα"},
    {"badScale", "Κακή κλίμακα"},
    {"whichMasterClock", "Παρακαλώ ορίστε ένα χρονιστή"}
  };
}
