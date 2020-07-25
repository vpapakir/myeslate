package gr.cti.eslate.masterclock;

import java.util.*;

/**
 * Greek language localized strings for the master clock component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.masterclock.MasterClock
 */
public class MasterClockResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "������ ���������"},
    {"name", "���������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    // help file
    {"helpfile", "help/masterclock_el.html"},
    // plug names
    {"tick", "���������"},
    //
    // Other text
    //
    {"start", "�������� ��������"},
    {"stop", "��������� ��������"},
    {"set", "������� ���� ����������"},
    {"select", "������� �������� ������"},
    {"timeScale", "������� ������"},
    {"h", "�"},
    {"m", "�"},
    {"s", "�"},
    {"noComponents1", "����� ������ ��� ����� ����������� �� �� ��������."},
    {"noComponents2", "� ��������� ���������."},
    {"warning", "�������"},
    //
    // BeanInfo resources
    //
    {"setMinimumTimeScale", "�������� ������� ������"},
    {"setMinimumTimeScaleTip", "����� ��� �������� ������������ ������� ������"},
    {"setMaximumTimeScale", "������� ������� ������"},
    {"setMaximumTimeScaleTip", "����� �� ������� ������������ ������� ������"},
    {"setTimeScale", "������� ������"},
    {"setTimeScaleTip", "����� ��� ���� ��� �������� ������"},
    {"startStop", "���������� ���������� ���������/������������"},
    {"startStopTip", "������ �� �� ������������ � ��������� ���������/������������"},
    {"setSleepInterval", "�������� ������������� ������ ���"},
    {"setSleepIntervalTip", "������ �� ����� �������� ���� ��� �������� ���� ���, �� �������� �������������"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� ��������"},
    {"LoadTimer",             "�������� ��������"},
    {"SaveTimer",             "���������� ��������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� ��������"},
  };
}
