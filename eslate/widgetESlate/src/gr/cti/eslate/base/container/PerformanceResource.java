package gr.cti.eslate.base.container;

import java.util.*;

/**
 * English language localized strings for the performance manager.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PerformanceResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"idInUse1", "ID "},
    {"idInUse2", " is already in use"},
    {"cycle", "Adding this group would create a cycle"},
    {"nullName", "Provided name is null"},
    {"nullPath", "Provided path is null"},
    {"nullGroup", "Provided PerformanceTimerGroup is null"},
    {"nullMW", "Provided microworld is null"},
    {"badPolicy", "Unknown policy"},
    {"realTime1", "Timer "},
    {"realTime2", " of component "},
    {"realTime3", " real time"},
    {"accTime1", "Timer "},
    {"accTime2", " of component "},
    {"accTime2", " accumulated time"},
    {"time1", "Timer "},
    {"time2", " of component "},
    {"time3", " time"},
    {"accTime1", "Timer "},
    {"accTime2", " of component "},
    {"accTime3", " accumulative time"},
    {"constructor", "Constructor"},
    {"loadState", "Load state"},
    {"saveState", "Save state"},
    {"initESlate", "Initialize E-Slate aspect"},
    {"exists", "There is already a group for this component under the specified category"},
    {"constrTime1", "Construction time for "},
    {"eSlateTime1", "E-Slate aspect initialization time for "},
    {"object", " object"},
    {"objects", " objects"},
    {"constrTime2", " of class "},
    {"constrTime3", ": "},
    {"constrTime4", " ms, average time: "},
    {"constrTime5", " ms"},
    {"badGroupID", "Illegal group ID"},
  };
}
