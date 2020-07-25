package gr.cti.eslate.stage.objects;

import java.util.*;

/**
 * Greek language localized strings for the box object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class BoxResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"setAltitude", "Ύψος"},
    {"setAltitudeTip", "Δώστε το ύψος τού κουτιού"},
    {"setColor", "Χρώμα"},
    {"setColorTip", "Δώστε το χρώμα τού κουτιού"},
    {"setImage", "Εικόνα"},
    {"setImageTip", "Δώστε την εικόνα τού κουτιού"},
    {"setMass", "Μάζα"},
    {"setMassTip", "Δώστε τη μάζα τού κουτιού"},
    {"setName", "Όνομα"},
    {"setNameTip", "Δώστε το όνομα τού κουτιού"},
    {"setObjectHeight", "Ύψος αντικειμένου"},
    {"setObjectHeightTip", "Δώστε το ύψος αντικειμένου για το κουτί"},
    {"setObjectWidth", "Πλάτος αντικειμένου"},
    {"setObjectWidthTip", "Δώστε το πλάτος αντικειμένου για το κουτί"},
    {"setPreparedForCopy", "Έτοιμο για αντιγραφή"},
    {"setPreparedForCopyTip", "Ορίστε αν το κουτί είναι έτοιμο για αντιγραφή"},
    {"actorNameChanged", "Αλλαγή ονόματος ηθοποιού"},
    {"propertyChange", "Αλλαγή ιδιότητας"},
    {"vetoableChange", "Ακυρώσιμη αλλαγή ιδιότητας"},
  };
}
