package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the travel-for-a-given-time control
 * component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.TimePrimitives
 */
public class TimeResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"GOTIME", "GOTIME"},
    {"TIME", "TIME"},
    {"SETTIME", "SETTIME"},
    {"TIMEUNIT", "TIMEUNIT"},
    {"SETTIMEUNIT", "SETTIMEUNIT"},
    {"TIMEUNITS", "TIMEUNITS"},
    {"STOPATLANDMARKS", "STOPATLANDMARKS"},
    {"SETSTOPATLANDMARKS", "SETSTOPATLANDMARKS"},
    {"badTime", "Bad time"},
    {"whichTime", "Please specify a travel-for-a-given-time control component"}
  };
}
