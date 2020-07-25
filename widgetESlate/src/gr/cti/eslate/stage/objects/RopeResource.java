package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the rope object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class RopeResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of rope"},
    {"setAngle", "Angle"},
    {"setAngleTip", "Enter angle of rope"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of rope"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of rope"},
    {"setLength", "Length"},
    {"setLengthTip", "Enter length of rope"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of rope"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of rope"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the rope is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
