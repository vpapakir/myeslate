package gr.cti.eslate.chronometer;

import java.util.*;

/**
 * English language localized strings for the chronometer component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.chronometer.Chronometer
 */
public class ChronometerResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Chronometer component"},
    {"name", "Chronometer"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "version"},
    // help file
    {"helpfile", "help/chronometer.html"},
    // plug names
    {"tick", "Tick"},
    {"hours", "Time in hours"},
    {"mins", "Time in minutes"},
    {"secs", "Time in seconds"},
    {"millis", "Time in milliseconds"},
    //
    // Other text
    //
    {"stopString1", "No component connected to chronometer."},
    {"stopString2", "Chronometer stopped."},
    {"warning", "Warning"},
    {"start", "Start chronometer"},
    {"stop", "Stop chronometer"},
    {"reset", "Reset display"},
    //
    // BeanInfo resources
    //
    {"setMilliseconds", "Elapsed time in milliseconds"},
    {"setMillisecondsTip", "Enter value of chronometer display in milliseconds"},
    {"chronometerStarted", "Chronometer start"},
    {"chronometerStopped", "Chronometer stop"},
    {"valueChanged", "Chronometer display change"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Chronometer constructor"},
    {"LoadTimer",             "Chronometer load"},
    {"SaveTimer",             "Chronometer save"},
    {"InitESlateAspectTimer", "Chronometer E-Slate aspect creation"},
  };
}
