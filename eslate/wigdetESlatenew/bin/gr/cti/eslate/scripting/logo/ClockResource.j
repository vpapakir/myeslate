package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the clock component
 * primitive group.
 *
 * @author	Kriton Kyrimis
 * @version	1.5.17, 20-Oct-1999
 * @see		gr.cti.eslate.scripting.logo.ClockPrimitives
 */
public class ClockResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"CLOCK.CLOCKTIME", "CLOCK.CLOCKTIME"},
    {"CLOCK.SETCLOCKTIME", "CLOCK.SETCLOCKTIME"},
    {"CLOCK.CLOCKDATE", "CLOCK.CLOCKDATE"},
    {"CLOCK.SETCLOCKDATE", "CLOCK.SETCLOCKDATE"},
    {"CLOCK.OPAQUE", "CLOCK.OPAQUE"},
    {"CLOCK.NOTOPAQUE", "CLOCK.NOTOPAQUE"},
    {"CLOCK.ISOPAQUE", "CLOCK.ISOPAQUE"},
    {"CLOCK.ANALOGAPPEARANCE", "CLOCK.ANALOGAPPEARANCE"},
    {"CLOCK.SETANALOGAPPEARANCE", "CLOCK.SETANALOGAPPEARANCE"},
    {"CLOCK.SETDIGITALAPPEARANCE", "CLOCK.SETDIGITALAPPEARANCE"},

    {"CLOCK.SETHOURS", "CLOCK.SETHOURS"},
    {"CLOCK.SETMINUTES", "CLOCK.SETMINUTES"},
    {"CLOCK.SETSECONDS", "CLOCK.SETSECONDS"},
    {"CLOCK.HOURS", "CLOCK.HOURS"},
    {"CLOCK.MINUTES", "CLOCK.MINUTES"},
    {"CLOCK.SECONDS", "CLOCK.SECONDS"},
    {"badTime", "Bad time"},
    {"badTime", "Bad date"},
    {"whichClock", "Please specify a clock component"}
  };
}
