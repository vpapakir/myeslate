package gr.cti.eslate.vector;

import java.util.*;

/**
 * Greek language localized strings for the vector component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.2, 23-Jan-2008
 * @see         gr.cti.eslate.vector.VectorComponent
 */
public class VectorResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "������ ��������"},
    {"name", "��������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    // help file
    {"helpfile", "help/vector_el.html"},
    // plug names
    {"vector", "��������"},
    //
    // Other text
    //
    {"x", "�"},
    {"y", "�"},
    {"length", "�����"},
    {"scale", "�������"},
    {"angle", "�����"},
    {"needPositive", "� ������� ������ �� ����� ���� ������� ����������� �������"},
    {"error", "������"},
    {"nameUsed", "�� ����� ���� ���� �������� �� ���� ��������� ��� �����������"},
    //
    // BeanInfo resources
    //
    {"setEast", "��������� ���������"},
    {"setEastTip", "��������� ��������� ��� �����������"},
    {"setNorth", "���������� ���������"},
    {"setNorthTip", "���������� ��������� ��� �����������"},
    {"setLength", "�����"},
    {"setLengthTip", "����� ��� �����������"},
    {"setAngle", "�����"},
    {"setAngleTip", "����� ��� �����������"},
    {"setScale", "�������"},
    {"setScaleTip", "������� ��� �����������"},
    {"setPrecision", "��������"},
    {"setPrecisionTip", "������� ������ ��� ������������ ���� ��� �����������"},
    {"setEditable", "������������ �������"},
    {"setEditableTip", "������ �� � ������� ������ �� ������� ��� ���� ��� �����������"},
    {"graphVisible", "������� ������������"},
    {"graphVisibleTip", "������ �� �� ����������� � ������� ������������ ��� �����������"},
    {"componentsVisible", "���������� ������������"},
    {"componentsVisibleTip", "������ �� �� ����������� � ���������� ������������ ��� �����������"},
    {"eastName", "����� ���������� ����������"},
    {"eastNameTip", "����� ��� ���������� ���������� ��� �����������"},
    {"northName", "����� ����������� ����������"},
    {"northNameTip", "����� ��� ����������� ���������� ��� �����������"},
    {"angleName", "����� ������"},
    {"angleNameTip", "����� ��� ������ ��� �����������"},
    {"lengthName", "����� ������"},
    {"lengthNameTip", "����� ��� ������ ��� �����������"},
    {"valueChanged", "������ ����� �����������"},
    {"maxLength", "������� �����"},
    {"maxLengthTip", "������� ����� ��� ����������� (0=����� ����������)"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� �����������"},
    {"LoadTimer",             "�������� �����������"},
    {"SaveTimer",             "���������� �����������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� �����������"},
  };
}
