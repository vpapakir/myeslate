package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the vector component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.VectorComponentPrimitives
 */
public class VectorResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SETVECTOR", "хеседиамусла"},
    {"VECTOR", "диамусла"},
    {"SETVECTORNORTH", "хеседиамуслабояеиа"},
    {"VECTORNORTH", "диамуслабояеиа"},
    {"SETVECTOREAST", "хеседиамуслааматокийа"},
    {"VECTOREAST", "диамуслааматокийа"},
    {"SETVECTORPOLAR", "хеседиамуслапокийа"},
    {"VECTORPOLAR", "диамуслапокийа"},
    {"SETVECTORLENGTH", "хеселгйосдиамуслатос"},
    {"VECTORLENGTH", "лгйосдиамуслатос"},
    {"SETVECTORANGLE", "хесецымиадиамуслатос"},
    {"VECTORANGLE", "цымиадиамуслатос"},
    {"SETVECTORSCALE", "хесейкилайадиамуслатос"},
    {"VECTORSCALE", "йкилайадиамуслатос"},
    {"SETVECTORPRECISION", "хесеайяибеиадиамуслатос"},
    {"VECTORPRECISION", "айяибеиадиамуслатос"},
    {"badCoords", "пАЯАЙАКЧ, ДЧСТЕ ТГМ ОЯИФЭМТИА ЙАИ ЙАТАЙЭЯУЖГ СУМТЕТАЦЛщМГ ТОУ ДИАМЩСЛАТОР"},
    {"badX", "аЙАТэККГКГ ОЯИФЭМТИА СУМТЕТАЦЛщМГ"},
    {"badY", "аЙАТэККГКГ ЙАТАЙЭЯУЖГ СУМТЕТАЦЛщМГ"},
    {"whichVector", "пАЯАЙАКЧ, ПЯОСДИОЯъСТЕ щМА ДИэМУСЛА"},
    {"badL", "аЙАТэККГКО ЛчЙОР"},
    {"badA", "аЙАТэККГКГ ЦЫМъА"}
  };
}
