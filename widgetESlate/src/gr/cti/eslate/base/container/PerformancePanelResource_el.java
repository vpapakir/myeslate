package gr.cti.eslate.base.container;

import java.util.*;

/**
 * Greek language localized strings for the performance manager panel.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PerformancePanelResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"dontActivateTip", "Ενεργοποίηση/Απενεργοποίηση συγκεκριμένων κόμβων"},
    {"immediateTip", "Ενεργοποίηση/Απενεργοποίηση συγκεκριμένων κόμβων και των αμέσων απογόνων τους"},
    {"activateAllTip", "Ενεργοποίηση/Απενεργοποίηση ολοκλήρων υποδένδρων"},
    {"activationPolicy", "Ενεργοποίηση/Απενεργοποίηση"},
    {"dontActivate", "Συγκεκριμένων κόμβων"},
    {"immediate", "Κόμβων και αμέσων απογόνων"},
    {"activateAll", "Ολόκληρων υποδένδρων"},
    {"dontActivate1", "Ενεργοποίηση κόμβου"},
    {"immediate1", "Ενεργοποίηση κόμβου και αμέσων απογόνων"},
    {"activateAll1", "Ενεργοποίηση ολoκλήρου υποδένδρου"},
    {"dontActivate0", "Απενεργοποίηση κόμβου"},
    {"immediate0", "Απενεργοποίηση κόμβου και αμέσων απογόνων"},
    {"activateAll0", "Απενεργοποίηση ολοκλήρου υποδένδρου"},
    {"globalGroups", "Ομάδες μετρητή απόδοσης"},
  };
}
