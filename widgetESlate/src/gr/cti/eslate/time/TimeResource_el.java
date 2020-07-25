package gr.cti.eslate.time;

import java.util.*;

/**
 * Greek language localized strings for the travel-for-a-given-time
 * control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.time.Time
 */
public class TimeResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "������ ����������� �������� ��� �������� �����"},
    {"name", "����������� �������� ��� �������� �����"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    // help file
    {"helpfile", "help/time_el.html"},
    // plug names
    {"time", "������"},
    //
    // Other text
    //
    {"go", "��������"},
    {"hours", "����"},
    {"minutes", "�����"},
    {"seconds", "������������"},
    {"stop", "����� ��� �������"},
    {"badUnit", "� ������ ���� ��� �������������"},
    //
    // BeanInfo resources
    //
    {"setTime", "������ ��������"},
    {"setTimeTip", "����� �� ����� ��� ������ �� ��������� �� ������"},
    {"setStopAtLandmarks", "����� ��� �������"},
    {"setStopAtLandmarksTip", "������ �� �� ������ �� ������� ��� �������"},
    {"setUnit", "������ ���������"},
    {"setUnitTip", "������ �� ��� ����� �������� � ��������"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� ������������ �������� ��� �������� �����"},
    {"LoadTimer",             "�������� ������������ �������� ��� �������� �����"},
    {"SaveTimer",             "���������� ������������ �������� ��� �������� �����"},
    {"InitESlateAspectTimer", "��������� ��������� ������� ������������ �������� ��� �������� �����"},
  };
}
