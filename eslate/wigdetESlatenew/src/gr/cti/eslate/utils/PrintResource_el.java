package gr.cti.eslate.utils;

import java.util.*;

/**
 * Greek language resources for the Print class.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class PrintResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"PrintErrorMsg1", "��� ������������� � �������� ������� �' ���� ��� ���������"},
    {"PrintErrorMsg2", "� �������� ����� �������. �� ���������� ������ "},
    {"PrintErrorMsg3", " ������ ��� ��� �������� bin ��� ������������ ��� �������."}
  };
}
