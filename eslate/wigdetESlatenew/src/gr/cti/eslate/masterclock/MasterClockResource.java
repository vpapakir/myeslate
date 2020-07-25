package gr.cti.eslate.masterclock;

import java.util.*;

/**
 * English language localized strings for the master clock component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.masterclock.MasterClock
 */
public class MasterClockResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Master Clock component"},
    {"name", "Master Clock"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "version"},
    // help file
    {"helpfile", "help/masterclock.html"},
    // plug names
    {"tick", "Tick"},
    //
    // Other text
    //
    {"start", "Start master clock"},
    {"stop", "Stop master clock"},
    {"set", "Set GMT"},
    {"select", "Select time scale"},
    {"timeScale", "Time scale"},
    {"h", "H"},
    {"m", "M"},
    {"s", "S"},
    {"noComponents1" , "No components connected to master clock."},
    {"noComponents2", "Master clock stopped."},
    {"warning", "Warning"},
    //
    // BeanInfo resources
    //
    {"setMinimumTimeScale", "Minimum time scale"},
    {"setMinimumTimeScaleTip", "Enter the minimum time scale"},
    {"setMaximumTimeScale", "Maximum time scale"},
    {"setMaximumTimeScaleTip", "Enter the maximum time scale"},
    {"setTimeScale", "Time scale"},
    {"setTimeScaleTip", "Enter the time scale"},
    {"startStop", "Store start/stop state"},
    {"startStopTip", "Specify if you want the start/stop state to be stored when saving"},
    {"setSleepInterval", "Milliseconds between ticks"},
    {"setSleepIntervalTip", "Specify the number of milliseconds to sleep after sending each tick"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Master clock constructor"},
    {"LoadTimer",             "Master clock load"},
    {"SaveTimer",             "Master clock save"},
    {"InitESlateAspectTimer", "Master clock E-Slate aspect creation"},
  };
}
