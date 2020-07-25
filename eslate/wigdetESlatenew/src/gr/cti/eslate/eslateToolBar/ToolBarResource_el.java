package gr.cti.eslate.eslateToolBar;

import java.util.*;

/**
 * Greek language localized strings for the toolbar component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.eslateToolBar.ESlateToolBar
 */
public class ToolBarResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // component info
    //
    {"componentName", "������ ������ ���������"},
    {"name", "������ ���������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    //
    // Other text
    //
    {"group", "�����"},
    {"nullName", "�� ����� ��� ������ �� ����� null"},
    {"nullGroup", "� ����� ��� ������ �� ����� null"},
    {"nullComponent", "�� �������� ��� ������ �� ����� null"},
    {"groupExists", "������� ��� ��� ����� �� ���� �� �����"},
    {"foreignGroup", "� ����� ���� ��� ������ �' ���� �� ����� ���������"},
    {"foreignTool", "�� �������� ���� ��� ������ �' ���� �� ����� ���������"},
    {"badIndex", "�� �������� ���� ���������"},
    {"removeFromParent", "�������� ������ ���������"},
    {"confirm", "�����������"},
    {"confirmRemove", "� ������ ��������� ���� ����������.\n����� ������� ��� ������ �� ��� ����������;"},
    {"notice", "����������"},
    {"noTopLevel", "�������� �������������� �� ������ ��� �� ���������� ������� ��� ����"},
    {"noAddSeparator", "�� �������������� ��� ESlateToolbar.addSeparator. ���' �����, �������������� ������� ������."},
    {"nullTool", "�� �������� ��� ������ �� ����� null"},
    //
    // BeanInfo resources
    //
    {"orientation", "���������������"},
    {"orientationTip", "������ ��� �������������� ��� ������ ���������"},
    {"horizontal", "���������"},
    {"vertical", "����������"},
    {"dynBorders", "�������� ������������ ������������"},
    {"dynBordersTip", "������ �� �� ������������ ��� ��������� �� ������������ ��������"},
    {"border", "����������"},
    {"borderTip", "������ �� ���������� ��� ������ ���������"},
    {"separation", "�������� ������ ������� ������"},
    {"separationTip", "������ �� �������� ������� �� ���������� ������� ������"},
    {"leadingSeparation", "������ ��������"},
    {"leadingSeparationTip", "������ �� �������� ������� ��� ����� �������� ��� ��� ���� ��� ������"},
    {"mouseClickedOnTool", "������/����������� �������� �� ��������"},
    {"mousePressedOnTool", "������ �������� �� ��������"},
    {"mouseReleasedOnTool", "����������� �������� �������� �� ��������"},
    {"mouseEnteredOnTool", "������� �������� �� ��������"},
    {"mouseExitedOnTool", "������ ������� ��� ��������"},
    {"mouseDraggedOnTool", "������� �������� �� ��������"},
    {"mouseMovedOnTool", "���������� �������� �� ��������"},
    {"actionPerformedOnTool", "�������������� ��������� �� ��������"},
    {"vgLayout", "������� ������� ������"},
    {"vgLayoutTip", "������������ �� ������� ��� �� ������� ��� ������"},
    {"bgLayout", "������ ��������"},
    {"bgLayoutTip", "������������ ��� ������ ��������� ����������� �������� ��� ������"},
    {"centered", "�����������"},
    {"centeredTip", "������ �� �� ����������� ��� ������ �� ����� �������������"},
    {"borderPainted", "���������� �������������"},
    {"borderPaintedTip", "������ �� �� ����������� �� ���������� ��� ������"},
    {"floatable", "������"},
    {"floatableTip", "������ �� � ������ ������ �� ������ �� ���� ����"},
    {"preferredSize", "����������� �������"},
    {"preferredSizeTip", "������ �� ����������� ������� ��� ������"},
    {"minimumSize", "�������� �������"},
    {"minimumSizeTip", "������ �� �������� ������� ��� ������"},
    {"maximumSize", "������� �������"},
    {"maximumSizeTip", "������ �� ������� ������� ��� ������"},
    {"modified", "�������������"},
    {"modifiedTip", "������ �� � ������ ���� ������������ ��� �� ������� ��� ���������"},
    {"menuEnabled", "����� ������ ������"},
    {"menuEnabledTip", "������ �� �� ����������� ����� �� ���� ���� �� �������������� ����"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "��������� ������ ���������"},
    {"LoadTimer",             "�������� ������ ���������"},
    {"SaveTimer",             "���������� ������ ���������"},
    {"InitESlateAspectTimer", "��������� ��������� ������� ������ ���������"},
  };
}
