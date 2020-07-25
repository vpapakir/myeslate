package gr.cti.eslate.navigator;

import java.util.ListResourceBundle;

/**
 * Greek localized resources for Navigator component.
 *
 * @author      George Birtbilis
 * @author      Kriton Kyrimis
 * @version     3.0.5, 23-Jan-2008
 */
public class MessagesBundle_el_GR extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final String[] info = {
    "����� ��� ������������� ������ (http://E-Slate.cti.gr)",
    "��������: �. ����������, �. �������",
    "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.",
    "�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.",
    "�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.",
    " ",
    "����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������",
    "���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������",
    "��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."
  };

  static final Object[][] contents =
  {
    {"info", info},
    //
    {"Navigator", "�������"},
    {"title", "������ �������"},
    {"version", "������"},
    {"homeURL", "http://E-Slate.cti.gr/"}, //16Jun2000
    //
    {"URL", "URL"},
    {"URL as string", "URL �� �������������"}, //14Mar2000
    //
    {"Go!", "�����!"},

    {"Navigation", "��������"},
    {"Back", "����"},
    {"Forward", "�������"},
    {"Home", "����"},

    {"View", "�������"},
    {"Stop", "�����"},
    {"Refresh", "��������"}, //24Jun2000
    {"Address bar", "������ ����������"},
    {"Toolbar", "������ ���������"},
    {"Status bar", "������ ����������"},
    //
    {"warning1", "*** �������: � ������� "},
    {"warning2", " ��� �������������."},
    {"warning3", "*** ���' ����� �� ������������� � ������� "},
    {"warning4", " ."},
    //
    {"LoadTimer",             "�������� �������"},
    {"SaveTimer",             "���������� �������"},
  };
}
