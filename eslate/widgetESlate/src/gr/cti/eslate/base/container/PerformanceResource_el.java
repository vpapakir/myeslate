package gr.cti.eslate.base.container;

import java.util.*;

/**
 * Greek language localized strings for the performance manager.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PerformanceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"idInUse1", "� ������� "},
    {"idInUse2", " ����� ��� �� �����"},
    {"cycle", "� �������� ����� ��� ������ �� ������������ �����"},
    {"nullName", "�� ����� ��� ������ ����� null"},
    {"nullPath", "� �������� ��� ������ ����� null"},
    {"nullGroup", "�� PerformanceTimerGroup ��� ������ ����� null"},
    {"nullMW", "� ����������� ��� ������ ����� null"},
    {"badPolicy", "������� �������"},
    {"realTime1", "����������� ������ ������������ "},
    {"realTime2", " ������� "},
    {"realTime3", ""},
    {"accTime1", "�������������� ������ ������������ "},
    {"accTime2", " ������� "},
    {"accTime3", ""},
    {"time1", "������ ������������ "},
    {"time2", " ������� "},
    {"time3", ""},
    {"accTime1", "����������� ������ ������������ "},
    {"accTime2", " ������� "},
    {"accTime3", ""},
    {"constructor", "�������������"},
    {"loadState", "�������� ����������"},
    {"saveState", "���������� ����������"},
    {"initESlate", "������������ ��������� �������"},
    {"exists", "��� ������� ��� ����� ��' ���� ��� ������ ���� ��� �� ������������ ���������"},
    {"constrTime1", "������ ���������� "},
    {"eSlateTime1", "������ ���������� ��������� ������� "},
    {"object", " ������������"},
    {"objects", " ������������"},
    {"constrTime2", " ��� ������ "},
    {"constrTime3", ": "},
    {"constrTime4", " ms, ����� ������: "},
    {"constrTime5", " ms"},
    {"badGroupID", "����� ������� ������"},
  };
}
