package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the spring object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class SpringResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of spring"},
    {"setAngle", "Angle"},
    {"setAngleTip", "Enter angle of spring"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of spring"},
    {"setDisplacement", "Displacement"},
    {"setDisplacementTip", "Enter displacement of spring"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of spring"},
    {"setLength", "Length"},
    {"setLengthTip", "Enter length of spring"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of spring"},
    {"setMaximumLength", "Maximum length"},
    {"setMaximumLengthTip", "Enter maximum length of spring"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of spring"},
    {"setNaturalLength", "Natural length"},
    {"setNaturalLengthTip", "Enter natural length of spring"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the spring is prepared for copying"},
    {"setSpringConstant", "SpringConstant"},
    {"setSpringConstantTip", "Enter value of spring constant"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
