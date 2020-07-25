package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the slope object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class SlopeResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of slope"},
    {"setAngle", "Angle"},
    {"setAngle", "Enter angle of slope"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of slope"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of slope"},
    {"setLength", "Length"},
    {"setLengthTip", "Enter length of slope"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of slope"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of slope"},
    {"setObjectHeight", "Object height"},
    {"setObjectHeightTip", "Enter object height for slope"},
    {"setObjectWidth", "Object width"},
    {"setObjectWidthTip", "Enter object width for slope"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the slope is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
