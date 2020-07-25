package gr.cti.eslate.distance;

import java.util.*;

/**
 * Greek language localized strings for the distance control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2007
 * @see         gr.cti.eslate.distance.Distance
 */
public class DistanceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "������ ����������� ���������"},
    {"name", "����������� ���������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    // help file
    {"helpfile", "help/distance_el.html"},
    // plug names
    {"distance", "��������"},
    //
    // Other text
    //
    {"go", "��������"},
    {"m", "�����"},
    {"km", "����������"},
    {"ft", "�����"},
    {"miles", "�����"},
    {"stop", "����� ��� �������"},
    {"badUnit", "� ������ ���� ��� �������������"},
    //
    // BeanInfo resources
    //
    {"setDistance", "�������� ��� �� ��������"},
    {"setDistanceTip", "����� ��� �������� ��� ������ �� ��������"},
    {"setStopAtLandmarks", "����� ��� �������"},
    {"setStopAtLandmarksTip", "������ �� �� ������ �� ������� ��� �������"},
    {"setUnit", "������ ��������"},
    {"setUnitTip", "������ �� ��� ����� �������� � ��������"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� ������������ ���������"},
    {"LoadTimer",             "�������� ������������ ���������"},
    {"SaveTimer",             "���������� ������������ ���������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� ������������ ���������"},
  };
}
