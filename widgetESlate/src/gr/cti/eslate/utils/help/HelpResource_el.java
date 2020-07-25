package gr.cti.eslate.utils.help;

import java.util.*;

/**
 * Greek language resources for the help set constructor.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class HelpResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"hsTitle", "������� �������� ��� �� ����������"},
    {"contents", "�����������"},
    {"index", "���������"},
    {"search", "���������"},
    {"selectDir", "�������� ��� �������� �� �� ������ ��� ��������"},
    {"adjustContents", "�������� ��� ������ ������������"},
    {"ok", "�������"},
    {"cancel", "�����"},
    {"up","���������� ���� �� ����"},
    {"down", "���������� ���� �� ����"},
    {"delete", "��������"},
    {"enterName1", "����� �� ����� ��� ������ ��� ��� ����� �� �������� �������"},
    {"enterName2", "����� ����� ������"},
    {"confirmOverwriteAll", "�� ������ ���������� ��� �������� �������� ���.\n������ �� �� ������������;"},
    {"confirmOverwriteSome", "������ ��� �� ������ ���������� ��� �������� �������� ���.\n������ �� �� ������������;"},
    {"confirm", "�����������"},
    {"warning", "�������"},
    {"noHTML", "����� � ��������� ��� �������� ������ ������ HTML"},
    {"noHTMLleft", "������ �� ������������� ����������� ��� ������ HTML ��� �������"},
  };
}
