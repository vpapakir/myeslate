package gr.cti.eslate.chronometer;

import java.util.*;

/**
 * Greek language localized strings for the chronometer component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.chronometer.Chronometer
 */
public class ChronometerResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "������ ����������"},
    {"name", "����������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    // help file
    {"helpfile", "help/chronometer_el.html"},
    // plug names
    {"tick", "���������"},
    {"hours", "������� �� ����"},
    {"mins", "������� �� �����"},
    {"secs", "������� �� ������������"},
    {"millis", "������� �� �������� �������������"},
    //
    // Other text
    //
    {"stopString1", "����� ������ ��� ����� ����������� ��� ����������."},
    {"stopString2", "�� ���������� ���������."},
    {"warning", "�������"},
    {"start", "�������� �����������"},
    {"stop", "��������� �����������"},
    {"reset", "���������� ���������"},
    //
    // BeanInfo resources
    //
    {"setMilliseconds", "������� �� �������� �������������"},
    {"setMillisecondsTip", "����� ��� ���� ��� ��������� ��� ����������� �� �������� �������������"},
    {"chronometerStarted", "�������� �����������"},
    {"chronometerStopped", "��������� �����������"},
    {"valueChanged", "������ �������� �����������"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� �����������"},
    {"LoadTimer",             "�������� �����������"},
    {"SaveTimer",             "���������� �����������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� �����������"},
  };
}
