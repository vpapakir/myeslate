package gr.cti.structfile;

import java.util.*;

/**
 * Greek language localized strings for structured file utilities.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class UtilResources_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Resources for List
    {"listUsage", "�����: List ������"},

    // Resources for Defragment
    {"defragUsage", "�����: Defragment ������ ���������_������"},
    {"deleteFailed1", "� �������� ��� ������� "},
    {"deleteFailed2", " �������"},
    {"renameFailed1", "� ����������� ��� ������� "},
    {"renameFailed2", " �� "},
    {"renameFailed3", " ������� "},

    // Resources for FileTool
    {"fileToolName", "���������� ��������� �������"},
    {"file", "������"},
    {"new", "���"},
    {"open", "�������"},
    {"close", "��������"},
    {"newFolder", "���� ���������"},
    {"delete", "��������"},
    {"addFiles", "�������� �������"},
    {"addFolder", "�������� ���������"},
    {"optimize", "�������������� �������"},
    {"exit", "������"},
    {"help", "�������"},
    {"about", "�����������..."},
    {"createFile", "���������� ��������� �������"},
    {"create", "����������"},
    {"openFile", "������� ��������� �������"},
    {"ok", "�������"},
    {"error", "������"},
    {"yes", "���"},
    {"no", "���"},
    {"confirm", "�����������"},
    {"confirmDelete1", "����� ������� ��� ������ �� ������� �� ������"},
    {"confirmDelete2", ";"},
    {"confirmDeleteAll1", "����� ������� ��� ����� �� ������� ��� ��������"},
    {"confirmDeleteAll2", "��� ��� ��� �� �����������;"},
    {"confirmDeleteContents", "����� ������� ��� ������ �� ������� ��� �� ����������� ����� ��� �������?"},
    {"notDir1", "� ����������"},
    {"notDir2", "��� ����� ���������"},
    {"newFolderName", "���� ���������"},
    {"noEntry", "��� ����� �������� ������ ����������"},
    {"add", "��������"},
    {"notExist1", "�� ������"},
    {"notExist2", "��� �������"},
    {"notExist3", "� ���������"},
    {"notExist4", "��� �������"},
    {"confirmOverWrite1", "����� ������� ��� ������ �� ��������������� �� ����������� ��� �������"},
    {"confirmOverWrite2", ";"},
    {"confirmOverWrite3", "����� ������� ��� ������ �� ��������������� ��� ��������"},
    {"confirmOverWrite4", "�� �� ������"},
    {"confirmOverWrite5", ";"},
    {"confirmOverWrite6", "����� ������� ��� ������ �� ��������������� �� ������"},
    {"confirmOverWrite7", "�� ��� ��������"},
    {"confirmOverWrite8", ";"},
    {"fileToolAbout0", "���� ��� FileTool"},
    {"fileToolAbout1", "�������� ������������ ��������� �������, ������"},
    {"fileToolAbout2", "���������� & ��������: �. �������"},
    {"fileToolAbout3", "� 1999-2002 ���������� ����������� �����������"},
    {"formatVersion1", "�� ������ ����� �� ����� ������� "},
    {"formatVersion2", ""},
  };
}
