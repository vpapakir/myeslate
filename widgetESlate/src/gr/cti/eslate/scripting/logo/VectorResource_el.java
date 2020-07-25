package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the vector component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.VectorComponentPrimitives
 */
public class VectorResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SETVECTOR", "������������"},
    {"VECTOR", "��������"},
    {"SETVECTORNORTH", "������������������"},
    {"VECTORNORTH", "��������������"},
    {"SETVECTOREAST", "���������������������"},
    {"VECTOREAST", "�����������������"},
    {"SETVECTORPOLAR", "������������������"},
    {"VECTORPOLAR", "��������������"},
    {"SETVECTORLENGTH", "��������������������"},
    {"VECTORLENGTH", "����������������"},
    {"SETVECTORANGLE", "��������������������"},
    {"VECTORANGLE", "����������������"},
    {"SETVECTORSCALE", "����������������������"},
    {"VECTORSCALE", "������������������"},
    {"SETVECTORPRECISION", "�����������������������"},
    {"VECTORPRECISION", "�������������������"},
    {"badCoords", "��������, ����� ��� ��������� ��� ���������� ������������ ��� �����������"},
    {"badX", "���������� ��������� ������������"},
    {"badY", "���������� ���������� ������������"},
    {"whichVector", "��������, ������������ ��� ��������"},
    {"badL", "���������� �����"},
    {"badA", "���������� �����"}
  };
}
