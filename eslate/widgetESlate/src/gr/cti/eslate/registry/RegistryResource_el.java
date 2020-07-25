package gr.cti.eslate.registry;

import java.util.*;

/**
 * Greek language localized strings for the registry component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 * @see         gr.cti.eslate.registry.Registry
 */
public class RegistryResource_el extends ListResourceBundle
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
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright � 2001-2006 ���������� ����������� �����������"},
    {"version", "������"},
    //
    // Other text
    {"exportRegistry", "������� �������"},
    //
    // BeanInfo resources
    {"valueChanged", "������ �����"},
    {"commentChanged", "������ �������"},
    {"persistenceChanged", "������ �����������������"},
    {"variableAdded", "�������� ����������"},
    {"variableRemoved", "�������� ����������"},
    {"registryCleared", "���������� �������"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� �������"},
    {"LoadTimer",             "�������� �������"},
    {"SaveTimer",             "���������� �������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� �������"},
  };
}
