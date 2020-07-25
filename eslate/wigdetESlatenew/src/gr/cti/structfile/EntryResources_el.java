package gr.cti.structfile;

import java.util.*;

/**
 * Greek language localized strings for class Entry.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class EntryResources_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"badName", "�� ����� ��� ������������ ������ �� ��� ����� ����"},
    {"badLocation", "� ��������� ������ �� ����� ���������� � ��� ��� 0"},
    {"badSize", "�� ������� ������ �� ����� ���������� � ��� ��� 0"},
    {"badType", "� ����� ��� ������������ ������ �� ����� FILE � DIRECTORY"},
    {"root", "<����>"},
    {"file", "������"},
    {"directory", "���������"},
    {"entry", "����������"},
    {"size", "�������"},
    {"type", "�����"},
    {"location", "���������"},
    {"parent", "������"},
    {"notDir1", "� ���������� "},
    {"notDir2", " ��� ����� ���������"},
  };
}
