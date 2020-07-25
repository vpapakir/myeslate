/*
26Nov1999 - renamed all PHYSICS.* from SCENE.* and ������.* to �������.*
29Nov1999 - renamed all STAGE.* to SCENE.*
24Feb2000 - renamed �������.������������� to �������.��������
          - added SCENE.CLONEOBJECT's LOCALIZATION
*/

package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;

public class ScenePrimitivesBundle_el_GR extends ListResourceBundle {
 public Object [][] getContents() {
  return contents;
 }

 static final Object[][] contents={

  {"SCENE.MAKEOBJECT", "�������.�����������������"},
  {"SCENE.REMOVEOBJECT", "�������.�������������������"},
  {"SCENE.CLONEOBJECT", "�������.����������������������"}, //24Feb2000

  {"SCENE.BRINGTOFRONT", "�������.����������������"}, //31Mar2000
  {"SCENE.SENDTOBACK", "�������.�����������������"}, //31Mar2000

  {"SCENE.CLEAR", "�������.��������"}, //24Feb2000: Greek localized string renamed to "�������.��������" from "�������.�������������"
  {"SCENE.ENABLEREFRESH", "�������.�����������������"},
  {"SCENE.DISABLEREFRESH", "�������.�����������������"}, //13Oct1999: had "ENABLE" instead of "DISABLE"
  {"SCENE.TRANSLATEVIEW", "�������.����������"},
  {"SCENE.PHOTO", "�������.�����������"}, //10May2000
  {"SCENE.GRIDSIZE", "�������.����������������"},
  {"SCENE.SETGRIDSIZE", "�������.��������������������"},
  {"SCENE.GRIDVISIBLE", "�������.�����������"},
  {"SCENE.SHOWGRID", "�������.�����������������"},
  {"SCENE.HIDEGRID", "�������.�����������������"},
  {"SCENE.SETAXESOVERSHAPES", "�������.������������������������"},
  {"SCENE.SETAXESUNDERSHAPES", "�������.������������������������"},
  {"SCENE.AXESOVERSHAPES", "�������.��������������������"},
 };

}
