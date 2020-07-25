package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

/**
 * English language resources for ESlateBeanResource.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 * @author      George Tsironis
 */
public class ESlateBeanBundle_el extends ListResourceBundle
{
  public Object [][] getContents() {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Events
    //
    {"actionPerformed", "�������� ���������"},
    {"componentHidden", "�������� �������"},
    {"componentMoved", "���������� �������"},
    {"componentResized", "������ �������� �������"},
    {"componentShown", "�������� �������"},
    {"mouseClicked", "���� ��������"},
    {"mouseDragged", "������� ��������"},
    {"mouseEntered", "������� ��������"},
    {"mouseExited", "������ ��������"},
    {"mouseMoved", "���������� ��������"},
    {"mousePressed", "������ ��������"},
    {"mouseReleased", "����� ��������"},
    {"propertyChange", "������ ���������"},
    {"vetoableChange", "��������� ������ ���������"},
    //
    // Properties
    //
    {"opaque", "���������"},
    {"visible", "�����"},
    {"border", "����������"}
  };
}
