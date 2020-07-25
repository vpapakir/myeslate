package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * English language localized strings for the polyline object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class PolyLineResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Altitude"},
    {"setAltitudeTip", "Enter altitude of polyline"},
    {"setColor", "Color"},
    {"setColorTip", "Enter color of polyline"},
    {"setImage", "Image"},
    {"setImageTip", "Enter image of polyline"},
    {"setMass", "Mass"},
    {"setMassTip", "Enter mass of polyline"},
    {"setName", "Name"},
    {"setNameTip", "Enter name of polyline"},
    {"setPreparedForCopy", "Prepared for copy"},
    {"setPreparedForCopyTip", "Specify if the polyline is prepared for copying"},
    {"actorNameChanged", "Actor name changed"},
    {"propertyChange", "Property change"},
    {"vetoableChange", "Vetoable change"},
  };
}
