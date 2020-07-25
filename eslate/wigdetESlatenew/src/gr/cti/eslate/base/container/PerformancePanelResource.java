package gr.cti.eslate.base.container;

import java.util.*;

/**
 * English language localized strings for the performance manager panel.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PerformancePanelResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"dontActivateTip", "Activation/Deactivation of specific nodes"},
    {"immediateTip", "Activation/Deactivation of nodes and their immediate children"},
    {"activateAllTip", "Activation/Deactivation of entire sub-trees"},
    {"activationPolicy", "Activation/Deactivation"},
    {"dontActivate", "Specific nodes"},
    {"immediate", "Nodes and immediate children"},
    {"activateAll", "Entire sub-trees"},
    {"dontActivate1", "Activate node"},
    {"immediate1", "Activate node and immediate children"},
    {"activateAll1", "Activate entire sub-tree"},
    {"dontActivate0", "Deactivate node"},
    {"immediate0", "Deactivate node and immediate children"},
    {"activateAll0", "Deactivate entire sub-tree"},
    {"globalGroups", "Performance Timer groups"},
  };
}
