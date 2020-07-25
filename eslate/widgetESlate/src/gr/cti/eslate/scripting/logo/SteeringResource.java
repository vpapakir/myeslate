package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the steering control component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.SteeringPrimitives
 */
public class SteeringResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SETSTEERINGDIRECTION", "SETSTEERINGDIRECTION"},
    {"STEERINGDIRECTION", "STEERINGDIRECTION"},
    {"STEERINGGO", "STEERINGGO"},
    {"N", "N"},
    {"NE", "NE"},
    {"E", "E"},
    {"SE", "SE"},
    {"S", "S"},
    {"SW", "SW"},
    {"W", "W"},
    {"NW", "NW"},
    {"badDir", "Bad Direction. Please specify one of"},
    {"whichSteering", "Please specify a steering control component"}
  };
}
