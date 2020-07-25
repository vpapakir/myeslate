package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the chronometer component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 * @see         gr.cti.eslate.scripting.logo.ChronometerPrimitives
 */
public class ChronometerResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"STARTCHRONOMETER", "����������������"},
    {"STOPCHRONOMETER", "�����������������"},
    {"RESETCHRONOMETER", "������������������"},
    {"CHRONOMETERTIME", "�����������������"},
    {"SETCHRONOMETERTIME", "��������������������"},
    {"CHRONOMETERMILLISECONDS", "�������������������������������"},
    {"SETCHRONOMETERMILLISECONDS", "�����������������������������������"},
    {"CHRONOMETERSECONDS", "�����������������������"},
    {"SETCHRONOMETERSECONDS", "���������������������������"},
    {"CHRONOMETERMINUTES", "����������������"},
    {"SETCHRONOMETERMINUTES", "��������������������"},
    {"CHRONOMETERHOURS", "���������������"},
    {"SETCHRONOMETERHOURS", "�������������������"},
    {"CHRONOMETERRUNNING", "����������������"},
    {"badTime", "����� ������"},
    {"whichChronometer", "�������� ������ ��� ����������"}
  };
}
