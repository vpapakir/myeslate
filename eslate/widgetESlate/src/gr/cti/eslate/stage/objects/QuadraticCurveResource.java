package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the quadratic curve object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class QuadraticCurveResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of quadratic curve"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of quadratic curve"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of quadratic curve"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of quadratic curve"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of quadratic curve"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the quadratic curve is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
