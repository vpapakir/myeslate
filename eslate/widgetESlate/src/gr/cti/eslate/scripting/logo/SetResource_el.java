package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the set component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 * @see         gr.cti.eslate.scripting.logo.SetPrimitives
 */
public class SetResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SELECTSUBSET", "�����������������"},
    {"CLEARSELECTEDSUBSET", "����������������������������"},
    {"QUERYINSET", "��������������"},
    {"DELETEELLIPSE", "����������������"},
    {"SETTABLEINSET", "�����������������"},
    {"TABLEINSET", "��������������"},
    {"TABLESINSET", "��������������"},
    {"SETPROJECTIONFIELD", "�����������������"},
    {"PROJECTIONFIELD", "�������������"},
    {"PROJECTIONFIELDS", "�������������"},
    {"SETCALCULATIONTYPE", "�������������������"},
    {"CALCULATIONTYPE", "����������������"},
    {"CALCULATIONTYPES", "����������������"},
    {"SETCALCULATIONFIELD", "��������������������"},
    {"CALCULATIONFIELD", "����������������"},
    {"CALCULATIONFIELDS", "����������������"},
    {"PROJECTFIELD", "������������"},
    {"PROJECTINGFIELDS", "�����������������"},
    {"CALCULATEINSET", "������������������"},
    {"CALCULATINGINSET", "��������������������"},
    {"SHOWQUERIESINSET", "��������������������������"},
    {"SHOWINGQUERIESINSET", "������������������������������"},
    {"NEWELLIPSE", "����������"},
    {"ACTIVATEELLIPSE", "��������������������"},
    {"DEACTIVATEELLIPSE", "����������������������"},
    {"badX", "���������� ��������� ������������"},
    {"badY", "���������� ���������� ������������"},
    {"badSelection", "�������� ����� ��� ������ ������������� � ����� ������� �����"},
    {"badActivation", "�������� ����� ��� ������ ������������� � ���� ������ ������ 0 ��� 2"},
    {"badBoolean", "�������� ����� ����� ������� �����"},
    {"badEllipse", "�������� ����� ���� ������ ������ 0 ��� 2"},
    {"whichSet", "�������� ������ ��� ������"},
    {"noTable", "� ������ ��� ����������� ������ ������"},
    {"noProjField", "��� ������� ����� ��� �� ������ �� ��������� ���� ������"},
    {"noCalcOp", "��� ����� ���������� ������ ���������� �����������"},
    {"noCalcField", "��� ����� ���������� ������ ����� �����������"}
  };
}
