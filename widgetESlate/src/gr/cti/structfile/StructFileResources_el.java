package gr.cti.structfile;

import java.util.*;

/**
 * Greek language localized strings for class StructFile.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class StructFileResources_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"writeProtected1", "�� ������ "},
    {"writeProtected2", " ����� ������������� ��� �������"},
    {"couldNotDelete", "��� ���� ������� �� ��������� � ����� ������ ��� "},
    {"cannotOpen1", "�� ������ "},
    {"cannotOpen2", " ��� ������ �� ��������"},
    {"badMode", "� ������ ���������� ������ �� ����� OLD � NEW"},
    {"notOpen", "�� ������ ����� �������"},
    {"badEntry", "� ���������� ��� ������ �� ����� null � ����"},
    {"nullEntry", "� ���������� ��� ������ �� ����� null"},
    {"nullFiles", "�� ������ ��� ������ �� ����� null"},
    {"equalFiles", "�� ������ ������ �� ����� �����������"},
    {"noOpenDir", "��� ����������� �� ������� ���������"},
    {"notFound1", "� ���������� "},
    {"notFound2", " ��� �������"},
    {"fileOrDir",
      "� ���������� ������ �� ����� ����� Entry.FILE � Entry.DIRECTORY"},
    {"anotherEntry1", "������� ���� ���������� �� ����� "},
    {"anotherEntry2", ""},
    {"isCurrentDir1", "� ���������� "},
    {"isCurrentDir2", " ����� � ������ ��������� ��� ��� ������ �� ���������"},
    {"notDir1", "� ���������� "},
    {"notDir2", " ��� ����� ���������"},
    {"badPath",
      "� �������� ������ �� ����� ��� �������� ��� ��������� �������"},
    {"rootDir", "� ������ ��������� ����� � ����"},
    {"alreadyOpen", "�� �������� ������ ����� ��� �������"},
    {"nullDestination", "�� �������� ������ ���������� ����� null"},
    {"cantCdDest1", "��� ����� ������ � �������� ���� �������� "},
    {"cantCdDest2", " ��� ������ ���������� "},
    {"cantCdSrc1", "��� ����� ������ � �������� ���� �������� "},
    {"cantCdSrc2", " ��� ������ ������ "},
    {"nullBuffer", "�� buffer ����� null"},
  };
}
