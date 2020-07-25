package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the master clock component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 30-May-2006
 * @see         gr.cti.eslate.scripting.logo.MasterClockPrimitives
 */
public class MasterClockResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"STARTTICK", "нейимавяомистг"},
    {"STOPTICK", "сталатавяомистг"},
    {"MASTERCLOCKMINIMUMSCALE", "екавистгйкилайавяомистг"},
    {"SETMASTERCLOCKMINIMUMSCALE", "хесеекавистгйкилайавяомистг"},
    {"MASTERCLOCKMAXIMUMSCALE", "лецистгйкилайавяомистг"},
    {"SETMASTERCLOCKMAXIMUMSCALE", "хеселецистгйкилайавяомистг"},
    {"MASTERCLOCKSCALE", "йкилайавяомистг"},
    {"SETMASTERCLOCKSCALE", "хесейкилайавяомистг"},
    {"MASTERCLOCKRUNNING", "вяомистгстяевеи"},
    {"badMinScale", "йАЙч ЕКэВИСТГ ЙКъЛАЙА"},
    {"badMaxScale", "йАЙч ЛщЦИСТГ ЙКъЛАЙА"},
    {"badScale", "йАЙч ЙКъЛАЙА"},
    {"whichMasterClock", "пАЯАЙАКЧ ОЯъСТЕ щМА ВЯОМИСТч"}
  };
}
