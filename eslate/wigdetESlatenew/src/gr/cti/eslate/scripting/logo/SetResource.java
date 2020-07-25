package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the set component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 * @see         gr.cti.eslate.scripting.logo.SetPrimitives
 */
public class SetResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SELECTSUBSET", "SELECTSUBSET"},
    {"CLEARSELECTEDSUBSET", "CLEARSELECTEDSUBSET"},
    {"QUERYINSET", "QUERYINSET"},
    {"DELETEELLIPSE", "DELETEELLIPSE"},
    {"SETTABLEINSET", "SETTABLEINSET"},
    {"TABLEINSET", "TABLEINSET"},
    {"TABLESINSET", "TABLESINSET"},
    {"SETPROJECTIONFIELD", "SETPROJECTIONFIELD"},
    {"PROJECTIONFIELD", "PROJECTIONFIELD"},
    {"PROJECTIONFIELDS", "PROJECTIONFIELDS"},
    {"SETCALCULATIONTYPE", "SETCALCULATIONTYPE"},
    {"CALCULATIONTYPE", "CALCULATIONTYPE"},
    {"CALCULATIONTYPES", "CALCULATIONTYPES"},
    {"SETCALCULATIONFIELD", "SETCALCULATIONFIELD"},
    {"CALCULATIONFIELD", "CALCULATIONFIELD"},
    {"CALCULATIONFIELDS", "CALCULATIONFIELDS"},
    {"PROJECTFIELD", "PROJECTFIELD"},
    {"PROJECTINGFIELDS", "PROJECTINGFIELDS"},
    {"CALCULATEINSET", "CALCULATEINSET"},
    {"CALCULATINGINSET", "CALCULATINGINSET"},
    {"NEWELLIPSE", "NEWELLIPSE"},
    {"ACTIVATEELLIPSE", "ACTIVATEELLIPSE"},
    {"DEACTIVATEELLIPSE", "DEACTIVATEELLIPSE"},
    {"badX", "Illegal horizontal coordinate"},
    {"badY", "Illegal vertical coordinate"},
    {"badSelection", "Please provide a pair of coordinates or three boolean values"},
    {"badActivation", "Please provide a pair of coordinates or a number between 0 and 2"},
    {"badBoolean", "Please provide three boolean values"},
    {"badEllipse", "Please provide a number between 0 and 2"},
    {"whichSet", "Please specify a set component"},
    {"noTable", "There is no table displayed in the component"},
    {"noProjField", "There is no field that can be projected onto the component"},
    {"noCalcOp", "There is no selected calculation operation"},
    {"noCalcField", "There is no selected calculation field"}
  };
}
