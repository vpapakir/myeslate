package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the master clock component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 30-May-2006
 * @see         gr.cti.eslate.scripting.logo.MasterClockPrimitives
 */
public class MasterClockResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"STARTTICK", "STARTTICK"},
    {"STOPTICK", "STOPTICK"},
    {"MASTERCLOCKMINIMUMSCALE", "MASTERCLOCKMINIMUMSCALE"},
    {"SETMASTERCLOCKMINIMUMSCALE", "SETMASTERCLOCKMINIMUMSCALE"},
    {"MASTERCLOCKMAXIMUMSCALE", "MASTERCLOCKMAXIMUMSCALE"},
    {"SETMASTERCLOCKMAXIMUMSCALE", "SETMASTERCLOCKMAXIMUMSCALE"},
    {"MASTERCLOCKSCALE", "MASTERCLOCKSCALE"},
    {"SETMASTERCLOCKSCALE", "SETMASTERCLOCKSCALE"},
    {"MASTERCLOCKRUNNING", "MASTERCLOCKRUNNING"},
    {"badMinScale", "Bad minimum scale"},
    {"badMaxScale", "Bad maximum scale"},
    {"badScale", "Bad scale"},
    {"whichMasterClock", "Please specify a master clock component"}
  };
}
