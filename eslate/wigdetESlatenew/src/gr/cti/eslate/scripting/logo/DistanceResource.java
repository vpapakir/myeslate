package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the distance control component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.DistancePrimitives
 */
public class DistanceResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"GODISTANCE", "GODISTANCE"},
    {"DISTANCE", "DISTANCE"},
    {"SETDISTANCE", "SETDISTANCE"},
    {"DISTANCEUNIT", "DISTANCEUNIT"},
    {"SETDISTANCEUNIT", "SETDISTANCEUNIT"},
    {"DISTANCEUNITS", "DISTANCEUNITS"},
    {"STOPATLANDMARKS", "STOPATLANDMARKS"},
    {"SETSTOPATLANDMARKS", "SETSTOPATLANDMARKS"},
    {"badDistance", "Bad distance"},
    {"whichDistance", "Please specify a distance control component"}
  };
}
