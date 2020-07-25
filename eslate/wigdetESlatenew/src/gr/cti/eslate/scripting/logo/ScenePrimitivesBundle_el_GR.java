/*
26Nov1999 - renamed all PHYSICS.* from SCENE.* and жусийг.* to сйгмийо.*
29Nov1999 - renamed all STAGE.* to SCENE.*
24Feb2000 - renamed сйгмийо.йахаяисесйгмг to сйгмийо.йахаяисе
          - added SCENE.CLONEOBJECT's LOCALIZATION
*/

package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;

public class ScenePrimitivesBundle_el_GR extends ListResourceBundle {
 public Object [][] getContents() {
  return contents;
 }

 static final Object[][] contents={

  {"SCENE.MAKEOBJECT", "сйгмийо.жтианеамтийеилемо"},
  {"SCENE.REMOVEOBJECT", "сйгмийо.ажаияесеамтийеилемо"},
  {"SCENE.CLONEOBJECT", "сйгмийо.йкымопоигсеамтийеилемо"}, //24Feb2000

  {"SCENE.BRINGTOFRONT", "сйгмийо.жеяестопяосйгмио"}, //31Mar2000
  {"SCENE.SENDTOBACK", "сйгмийо.стеикестоупобахяо"}, //31Mar2000

  {"SCENE.CLEAR", "сйгмийо.йахаяисе"}, //24Feb2000: Greek localized string renamed to "сйгмийо.йахаяисе" from "сйгмийо.йахаяисесйгмг"
  {"SCENE.ENABLEREFRESH", "сйгмийо.епитяеьгамамеысгс"},
  {"SCENE.DISABLEREFRESH", "сйгмийо.апотяеьгамамеысгс"}, //13Oct1999: had "ENABLE" instead of "DISABLE"
  {"SCENE.TRANSLATEVIEW", "сйгмийо.йукисгоьгс"},
  {"SCENE.PHOTO", "сйгмийо.жытоцяажгсг"}, //10May2000
  {"SCENE.GRIDSIZE", "сйгмийо.лецехоспкецлатос"},
  {"SCENE.SETGRIDSIZE", "сйгмийо.хеселецехоспкецлатос"},
  {"SCENE.GRIDVISIBLE", "сйгмийо.пкецлаояато"},
  {"SCENE.SHOWGRID", "сйгмийо.елжамисгпкецлатос"},
  {"SCENE.HIDEGRID", "сйгмийо.апойяуьгпкецлатос"},
  {"SCENE.SETAXESOVERSHAPES", "сйгмийо.хесеаномеспамыапосвглата"},
  {"SCENE.SETAXESUNDERSHAPES", "сйгмийо.хесеаномесйатыапосвглата"},
  {"SCENE.AXESOVERSHAPES", "сйгмийо.аномеспамыапосвглата"},
 };

}
