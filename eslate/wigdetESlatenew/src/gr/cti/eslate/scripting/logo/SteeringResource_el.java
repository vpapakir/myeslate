package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the steering control component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.SteeringPrimitives
 */
public class SteeringResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SETSTEERINGDIRECTION", "ΘΕΣΕΚΑΤΕΥΘΥΝΣΗΠΙΛΟΤΗΡΙΟΥ"},
    {"STEERINGDIRECTION", "ΚΑΤΕΥΘΥΝΣΗΠΙΛΟΤΗΡΙΟΥ"},
    {"STEERINGGO", "ΕΚΚΙΝΗΣΗΠΙΛΟΤΗΡΙΟΥ"},
    {"N", "Β"},
    {"NE", "ΒΑ"},
    {"E", "Α"},
    {"SE", "ΝΑ"},
    {"S", "Ν"},
    {"SW", "ΝΔ"},
    {"W", "Δ"},
    {"NW", "ΒΔ"},
    {"badDir", "Κακή διεύθυνση. Παρακαλώ δώστε μία από τις:"},
    {"whichSteering", "Παρακαλώ ορίστε ένα πιλοτήριο"}
  };
}
