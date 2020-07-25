package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the square box object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class SquareBoxResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of square box"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of square box"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of square box"},
    {"setLength", "Length"},
    {"setLengthTip", "Enter length of square box"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of square box"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of square box"},
    {"setObjectHeight", "Object height"},
    {"setObjectHeightTip", "Enter object height for square box"},
    {"setObjectWidth", "Object width"},
    {"setObjectWidthTip", "Enter object width for square box"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the square box is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
