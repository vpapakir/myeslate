package gr.cti.eslate.set;

import java.util.*;

/**
 * Greek language localized strings for the set component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.set.Set
 */
public class SetResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "������ ������"},
    {"name", "������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "����������� ����: ������ Tabletop (Broderbund)"},
    {"credits3", "��������: �. �������"},
    {"credits4", "�������: �. ��������, �. �������, �. �������"},
    {"credits5", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    // help file
    {"helpfile", "help/set_el.html"},
    // plug names
    {"database", "���� ���������"},
    // Other text
    //
    {"count", "�������"},
    {"percentTotal", "% ��� �� ������"},
    {"total", "������"},
    {"mean", "����� ����"},
    {"median", "�������"},
    {"smallest", "����������"},
    {"largest", "�����������"},
    {"percent", "��� ���� �����"},
    {"and", "���"},
    {"or", "�"},
    {"not", "���"},
    {"all", "���"},
    {"queryA", "������� �"},
    {"queryB", "������� �"},
    {"queryC", "������� �"},
    {"select", "������� ���������"},
    {"selectSet", "������� ����������"},
    {"selectOval", "������� ���������"},
    {"delete", "�������� ��������"},
    {"new", "��� �������"},
    {"copy", "���������"},
    {"project", "������� ����������� ������"},
    {"projField", "����� ��������"},
    {"calculate", "�����������"},
    {"calcOp", "������������ ����������"},
    {"calcKey", "����� �����������"},
    {"true", "������"},
    {"false", "������"},
    {"??", ";;"},
    {"badField1", "�� �����"},
    {"badField2", "��� �������"},
    {"badOp1", "� ����������"},
    {"badOp2", "��� �������������"},
    {"badKey", "���������� ����� �����������"},
    {"badTable1", "� �������"},
    {"badTable2", "��� �������"},
    {"badVersion1", "� ������������ ������ "},
    {"badVersion2", " ����� ��������. ���������� � ������ "},
    {"badVersion3", "."},
    {"wrongTableNumber", "� ������� ��� ������� ��� ���� ����� ������������\n��� ����� ��� ������ ��� �����������."},
    {"error", "������"},
    {"nullQuery", "������"},
    {"none", "--������--"},
    //
    // BeanInfo resources
    //
    {"setCalcKey", "����� �����������"},
    {"setCalcKeyTip", "����� ���� ��� ����� �� ��������� �� �����������"},
    {"setCalcOp", "������������ ����������"},
    {"setCalcOpTip", "������������ ���������� ��� �� ����������"},
    {"setProjectionField", "����� ��������"},
    {"setProjectionFieldTip", "����� ��� ������ ��� �� ��������� ��� ������"},
    {"setSelectedTable", "����������� �������"},
    {"setSelectedTableTip", "�������� ��� ������ ��� �� ������������"},
    {"setShowLabels", "�������� ���������"},
    {"setShowLabelsTip", "�������� ��� ��������� ��� ������������ �� ���� ������"},
    {"uniformProjection", "������ ����� ��������"},
    {"uniformProjectionTip", "������ �� ������ �� ���� ����� �������� ��� ��� �� ���������"},
    {"background", "����� ���������"},
    {"backgroundTip", "������ �� ����� ��� ��������� "},
    {"selectionColor", "����� ����������� ����������"},
    {"selectionColorTip", "������ �� ����� ��� ����������� ����������"},
    {"toolBarVisible", "������ ��������� �����"},
    {"toolBarVisibleTip", "������ �� �� ����������� � ������ ��������� ��� �������"},
    {"backgroundImage", "������ ���������"},
    {"backgroundImageTip", "������ ��� ������ ��� �� ������������ ��� ��������"},
    {"selectionImage", "������ ��������"},
    {"selectionImageTip", "������ ��� ������ �� ��� ����� �� ������������ �� ���������� ���������"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� �������"},
    {"LoadTimer",             "�������� �������"},
    {"SaveTimer",             "���������� �������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� �������"},
  };
}
