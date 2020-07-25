package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the round box object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class RoundBoxResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of round box"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of round box"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of round box"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of round box"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of round box"},
    {"setObjectHeight", "Object height"},
    {"setObjectHeightTip", "Enter object height for round box"},
    {"setObjectWidth", "Object width"},
    {"setObjectWidthTip", "Enter object width for round box"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the round box is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
