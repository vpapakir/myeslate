package gr.cti.eslate.steering;

import java.util.*;

/**
 * English language localized strings for the steering control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.steering.Steering
 */
public class SteeringResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Steering Control component"},
    {"name", "Steering Control"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "version"},
    // help file
    {"helpfile", "help/steering.html"},
    // plug names
    {"direction", "Direction"},
    //
    // Other text
    //

    //
    // BeanInfo resources
    //
    {"setDirection", "Direction"},
    {"setDirectionTip", "Enter the direction of the steering control"},
    {"N", "North"},
    {"NE", "Northeast"},
    {"E", "East"},
    {"SE", "Southeast"},
    {"S","South"},
    {"SW", "Southwest"},
    {"W", "West"},
    {"NW", "Northwest"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Steering control constructor"},
    {"LoadTimer",             "Steering control load"},
    {"SaveTimer",             "Steering control save"},
    {"InitESlateAspectTimer", "Steering control E-Slate aspect creation"},
  };
}
