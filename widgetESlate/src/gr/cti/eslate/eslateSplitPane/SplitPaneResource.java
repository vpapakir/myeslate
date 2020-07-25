package gr.cti.eslate.eslateSplitPane;

import java.util.*;

/**
 * English language localized strings for the split pane component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.eslateSplitPane.ESlateSplitPane
 */
public class SplitPaneResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    //
    {"componentName", "Split Pane component"},
    {"name", "Split Pane"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "version"},
    //
    // plug names
    //
    //
    // Other text
    //
    //
    // BeanInfo resources
    //
    {"preferredSize", "Preferred Size"},
    {"preferredSizeTip", "Specify the preferred size of the split pane"},
    {"minimumSize", "Minimum Size"},
    {"minimumSizeTip", "Specify the minimum size of the split pane"},
    {"maximumSize", "Maximum Size"},
    {"maximumSizeTip", "Specify the maximum size of the split pane"},
    {"firstComponent", "Class of first component"},
    {"firstComponentTip", "Specify the name of the class of the first component"},
    {"secondComponent", "Class of second component"},
    {"secondComponentTip", "Specify the name of the class of the second component"},
    {"notCompo1", "Class "},
    {"notCompo2", " is not a subclass of Component"},
    {"error", "Error"},
    {"notFound1", "Class "},
    {"notFound2", " was not found"},
    {"continuousLayout", "Continuous layout"},
    {"continuousLayoutTip", "Specify if the contents will be redrawn as the divider is moved"},
    {"dividerLocation", "Divider location"},
    {"dividerLocationTip", "Specify the location of the divider (-1 for preferred)"},
    {"dividerSize", "Divider size"},
    {"dividerSizeTip", "Specify the size of the divider"},
    {"oneTouchExpandable", "One touch expandable"},
    {"oneTouchExpandableTip", "Specify if there will be widgets for quickly collapsing/expanding the divider"},
    {"orientation", "Orientation"},
    {"orientationTip", "Specify the orientation of the split pane"},
    {"horizontal", "Horizontal"},
    {"vertical", "Vertical"},
    {"resizeWeight", "Resize weight"},
    {"resizeWeightTip", "Specify the \"weight\" of the first component during size changes (0..1)"},
    //{"border", "Border"},
    //{"borderTip", "Border of the split pane"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Split pane constructor"},
    {"LoadTimer",             "Split pane load"},
    {"SaveTimer",             "Split pane save"},
    {"InitESlateAspectTimer", "Split pane E-Slate aspect creation"},
  };
}
