package gr.cti.eslate.base.help;

import java.util.ListResourceBundle;

/**
 * Greek language error message resources for the E-Slate help system.
 *
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ErrorMessageBundle_el_GR extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"fileNotFound", "��� ������� �� ������"},
    {"folderNotFound", "��� ������� � ���������"},
    {"comp", "��� ��� ������ �� helpset file"},
    {"attentionFile", "�������� ����� �� ������ ��� ��������� ��� ���������� helpset file ����� ����������� ��� ���� ��� ��������������"},
    {"attentionFolder", "�������� ����� � ��������� ��� ��������� ��� ���������� helpset file ����� ������������ ��� ����� ��� �������������� � ��� ��������� ���� ��� ��� jhindexer"},
    {"none", "\n��� ������� ������ ��� �� ����������� ������\n"},
    {"or", "�"},
    {"dir", "���� �������� help ��� ������� "},
    {"orDir", "� ���� ��������"},
    {"inClassPath", "��� class path"},
    {"notCorrespond", "��� ����������� ��� ������ "},
    {"mapref",  "��� ������������ ��� ����� mapref ��� "},
    {"file", "�� ������ "},
    {"either1", "\n���� ��� ������� �� ������ "},
    {"either2", ""},
    {"or2", "����"},
    {"search", "��� �� ���������� <���������> ��� ������� � ��� ��������� ���� �� ��� jhindexer"},
    {"wrong", "������� ����� ��� ������� ��� �������"},
    {"attention", "�������"},
    {"OK", "�������"},
    {"continue", "H ������� �� ���������� ��� ��� ��������� �������"},
    {"impossible", "� ������� ��� ������ �� ��������"},
    {"toc", "� ��������� ������������ ����� ������"},
    {"rightInfo", "� �� �������� ����� �������������� �����������"},
    {"linkNotFound", "�� ������� ������� ��� �������"},
    {"noHelpSupported", "��� ������� ��������� �������"},
    {"noStartingPage", "H ������ ������ ��� ���� ������� �����"},
    {"noStartingPageDetails", "To ��������� ����� ��� ���� ������� ��� ����� <HOMEID>...</HOMEID> ��� "},
    {"noStartingPageDetails2", "��� ����������� �� ��� ������� ����������"}
  };
}
