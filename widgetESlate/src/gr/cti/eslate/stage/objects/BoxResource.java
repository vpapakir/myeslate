package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the box object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class BoxResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of box"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of box"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of box"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of box"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of box"},
    {"setObjectHeight", "Object height"},
    {"setObjectHeightTip", "Enter object height for box"},
    {"setObjectWidth", "Object width"},
    {"setObjectWidthTip", "Enter object width for box"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the box is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
