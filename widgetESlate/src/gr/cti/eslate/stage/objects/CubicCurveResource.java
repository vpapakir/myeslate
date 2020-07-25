package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the cubic curve object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class CubicCurveResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of cubic curve"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of cubic curve"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of cubic curve"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of cubic curve"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of cubic curve"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the cubic curve is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
