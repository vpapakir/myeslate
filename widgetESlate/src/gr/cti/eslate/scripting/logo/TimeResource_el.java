package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the travel-for-a-given-time control
 * component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.TimePrimitives
 */
public class TimeResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"GOTIME", "������������"},
    {"TIME", "������"},
    {"SETTIME", "���������"},
    {"TIMEUNIT", "������������"},
    {"SETTIMEUNIT", "����������������"},
    {"TIMEUNITS", "�������������"},
    {"STOPATLANDMARKS", "���������������"},
    {"SETSTOPATLANDMARKS", "�������������������"},
    {"badTime", "����� ������"},
    {"whichTime", "�������� ������ ��� ����������� �������� ��� �������� �����"}
  };
}
