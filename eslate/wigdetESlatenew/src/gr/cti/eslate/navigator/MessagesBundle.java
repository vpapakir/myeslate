package gr.cti.eslate.navigator;

import java.util.ListResourceBundle;

/**
 * English localized resources for Navigator component.
 *
 * @author      George Birtbilis
 * @author      Kriton Kyrimis
 * @version     3.0.5, 23-Jan-2008
 */
public class MessagesBundle extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final String[] info = {
    "Part of the E-Slate environment (http://E-Slate.cti.gr)",
    "Development: G. Birbilis, K. Kyrimis)",
    "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.",
    "�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.",
    "�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.",
    " ",
    "����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������",
    "���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������",
    "��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."
  };

  static final Object[][] contents = {
    {"info", info},
    //
    {"title", "Navigator component"},
    {"version", "version"},
    {"homeURL", "http://E-Slate.cti.gr/"},
    //
    {"URL", "URL"},
    {"URL as string", "URL as string"},
    //
    {"warning1", "*** WARNING: Navigator "},
    {"warning2", " is not supported."},
    {"warning3", "*** Using "},
    {"warning4", " instead."},
    //
    {"LoadTimer",             "Navigator load"},
    {"SaveTimer",             "Navigator save"},
  };
}
