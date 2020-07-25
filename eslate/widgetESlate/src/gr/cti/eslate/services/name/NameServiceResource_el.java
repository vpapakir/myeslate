package gr.cti.eslate.services.name;

import java.util.*;

/**
 * Greek language localized strings for the E-Slate name service.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameServiceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"nameInUse1", "�� ����� "},
    {"nameInUse2", " ����� ��� ������������ �� ��� �����������"},
    {"nameNotUsed1", "�� ����� "},
    {"nameNotUsed2", " ��� ����� ������������ �� ������ �����������"},
    {"noNullName", "�� ����� ��� ������ �� ����� null"},
    {"noNullObject", "�� ����������� ��� ������ �� ����� null"},
    {"noPath1", "� �������� "},
    {"noPath2", " ��� �������"},
    {"noDir1", "� �������� "},
    {"noDir2", " ��� ���������� �� �������� ���������"}
  };
}
