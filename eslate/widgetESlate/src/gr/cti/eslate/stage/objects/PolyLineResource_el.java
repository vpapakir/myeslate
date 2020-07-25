package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * Greek language localized strings for the polyline object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class PolyLineResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Ύψος"},
    {"setAltitudeTip", "Δώστε το ύψος τής πολυγωνικής γραμμής"},
    {"setColor", "Χρώμα"},
    {"setColorTip", "Δώστε το χρώμα τής πολυγωνικής γραμμής"},
    {"setImage", "Εικόνα"},
    {"setImageTip", "Δώστε την εικόνα τής πολυγωνικής γραμμής"},
    {"setMass", "Μάζα"},
    {"setMassTip", "Δώστε τη μάζα τής πολυγωνικής γραμμής"},
    {"setName", "Όνομα"},
    {"setNameTip", "Δώστε το όνομα τής πολυγωνικής γραμμής"},
    {"setPreparedForCopy", "Έτοιμο για αντιγραφή"},
    {"setPreparedForCopyTip", "Ορίστε αν η πολυγωνική γραμμή είναι έτοιμη για αντιγραφή"},
    {"actorNameChanged", "Αλλαγή ονόματος ηθοποιού"},
    {"propertyChange", "Αλλαγή ιδιότητας"},
    {"vetoableChange", "Ακυρώσιμη αλλαγή ιδιότητας"},
  };
}
