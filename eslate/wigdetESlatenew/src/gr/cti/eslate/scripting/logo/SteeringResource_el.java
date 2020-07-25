package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the steering control component
 * primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.SteeringPrimitives
 */
public class SteeringResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SETSTEERINGDIRECTION", "������������������������"},
    {"STEERINGDIRECTION", "��������������������"},
    {"STEERINGGO", "������������������"},
    {"N", "�"},
    {"NE", "��"},
    {"E", "�"},
    {"SE", "��"},
    {"S", "�"},
    {"SW", "��"},
    {"W", "�"},
    {"NW", "��"},
    {"badDir", "���� ���������. �������� ����� ��� ��� ���:"},
    {"whichSteering", "�������� ������ ��� ���������"}
  };
}
