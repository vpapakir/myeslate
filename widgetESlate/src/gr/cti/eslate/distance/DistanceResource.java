package gr.cti.eslate.distance;

import java.util.*;

/**
 * English language localized strings for the distance control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2007
 * @see         gr.cti.eslate.distance.Distance
 */
public class DistanceResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Distance Control component"},
    {"name", "Distance Control"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "version"},
    // help file
    {"helpfile", "help/distance.html"},
    // plug names
    {"distance", "Distance"},
    //
    // Other text
    //
    {"go", "Go"},
    {"m", "m"},
    {"km", "Km"},
    {"ft", "ft"},
    {"miles", "miles"},
    {"stop", "Stop at landmarks"},
    {"badUnit", "Unsupported unit"},
    //
    // BeanInfo resources
    //
    {"setDistance", "Distance to travel"},
    {"setDistanceTip", "Enter the distance to travel"},
    {"setStopAtLandmarks", "Stop at landmarks"},
    {"setStopAtLandmarksTip", "Specify if the travel should stop at landmarks"},
    {"setUnit", "Unit of measurement"},
    {"setUnitTip", "Unit in which the distance is measured"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Distance control constructor"},
    {"LoadTimer",             "Distance control load"},
    {"SaveTimer",             "Distance control save"},
    {"InitESlateAspectTimer", "Distance control E-Slate aspect creation"},
  };
}
