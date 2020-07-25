package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the E-Slate handle primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.scripting.logo.ESlatePrimitives
 */
public class ESlateResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SHOWHELP", "����������������"},
    {"SHOWINFO", "�������������������"},
    {"SHOWPLUGVIEW","�������������������"},
    {"PLUGS", "���������"},
    {"CONNECT", "�������"},
    {"DISCONNECT", "����������"},
    {"DISCONNECTPLUG", "������������������"},
    {"CONNECTED", "���������"},
    {"LISTCONNECTIONS", "������������������"},
    {"SETRENAMINGALLOWEDFROMBAR", "�����������������������������������"},
    {"RENAMINGALLOWEDFROMBAR", "�������������������������������"},
    {"SETNATIVEPROGRAMFOLDERS", "���������������������������������"},
    {"NATIVEPROGRAMFOLDERS", "����������������������������"},
    {"NATIVEPROGRAM", "������������������"},
    {"noComponent", "��� ������� ������ �� �����"},
    {"noPlug", "��� ������� �������� ��� ��� ������"},
    {"component", "� ������"},
    {"dontHavePlug", "��� ���� ������ ��������"},
    {"incompatible", "�� ��������� ����� ���������"},
    {"alreadyConnected", "�� ��������� ����� ��� ������������"},
    {"notConnected", "�� ��������� ��� ����� ������������"},
    {"progNotFound1", "�� ��������� "},
    {"progNotFound2", " ��� �������"}
  };
}
