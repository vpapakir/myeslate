package gr.cti.eslate.services.name;

import java.util.*;

/**
 * Greek language localized strings for the E-Slate name service.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameServiceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"nameInUse1", "Το όνομα "},
    {"nameInUse2", " είναι ήδη συσχετισμένο με ένα αντικείμενο"},
    {"nameNotUsed1", "Το όνομα "},
    {"nameNotUsed2", " δεν είναι συσχετισμένο με κάποιο αντικείμενο"},
    {"noNullName", "Το όνομα δεν πρέπει να είναι null"},
    {"noNullObject", "Το αντικείμενο δεν πρέπει να είναι null"},
    {"noPath1", "Η διαδρομή "},
    {"noPath2", " δεν υπάρχει"},
    {"noDir1", "Η διαδρομή "},
    {"noDir2", " δεν αναφέρεται σε υπηρεσία ονομασίας"}
  };
}
