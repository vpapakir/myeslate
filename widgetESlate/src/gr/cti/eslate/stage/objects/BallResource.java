package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the ball object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class BallResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of ball"},
    {"setAngle", "Angle"},
    {"setAngleTip", "Enter angle of ball"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of ball"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of ball"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of ball"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of ball"},
    {"setObjectHeight", "Object height"},
    {"setObjectHeightTip", "Enter object height for ball"},
    {"setObjectWidth", "Object width"},
    {"setObjectWidthTip", "Enter object width for ball"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the ball is prepared for copying"},
    {"setRadius", "Radius"},
    {"setRadiusTip", "Enter radius of ball"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
