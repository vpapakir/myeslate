package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the distance control component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.DistancePrimitives
 */
public class DistanceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"GODISTANCE", "���������������"},
    {"DISTANCE", "��������"},
    {"SETDISTANCE", "������������"},
    {"DISTANCEUNIT", "���������������"},
    {"SETDISTANCEUNIT", "�������������������"},
    {"DISTANCEUNITS", "����������������"},
    {"STOPATLANDMARKS", "���������������"},
    {"SETSTOPATLANDMARKS", "�������������������"},
    {"badDistance", "���� ��������"},
    {"whichDistance", "�������� ������ ��� ����������� ����������"}
  };
}
