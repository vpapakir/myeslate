package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the chronometer component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 * @see         gr.cti.eslate.scripting.logo.ChronometerPrimitives
 */
public class ChronometerResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"STARTCHRONOMETER", "STARTCHRONOMETER"},
    {"STOPCHRONOMETER", "STOPCHRONOMETER"},
    {"RESETCHRONOMETER", "RESETCHRONOMETER"},
    {"CHRONOMETERTIME", "CHRONOMETERTIME"},
    {"SETCHRONOMETERTIME", "SETCHRONOMETERTIME"},
    {"CHRONOMETERMILLISECONDS", "CHRONOMETERMILLISECONDS"},
    {"SETCHRONOMETERMILLISECONDS", "SETCHRONOMETERMILLISECONDS"},
    {"CHRONOMETERSECONDS", "CHRONOMETERSECONDS"},
    {"SETCHRONOMETERSECONDS", "SETCHRONOMETERSECONDS"},
    {"CHRONOMETERMINUTES", "CHRONOMETERMINUTES"},
    {"SETCHRONOMETERMINUTES", "SETCHRONOMETERMINUTES"},
    {"CHRONOMETERHOURS", "CHRONOMETERHOURS"},
    {"SETCHRONOMETERHOURS", "SETCHRONOMETERHOURS"},
    {"CHRONOMETERRUNNING", "CHRONOMETERRUNNING"},
    {"badTime", "Bad time"},
    {"whichChronometer", "Please specify a chronometer component"}
  };
}
