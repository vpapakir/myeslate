package gr.cti.eslate.base;

import java.util.*;

/**
 * Greek language localized strings for the E-Slate base classes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.23, 23-Jan-2008
 */
public class ESlateResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Resources for ESlateMicroworld
    //
    {"componentName", "�����������"},
    {"credits1", "����� ��� ������������� ������ (http://E-Slate.cti.gr)"},
    {"credits2", "��������: �. �������"},
    {"credits3", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
    {"version", "������"},
    {"notSelf", "�� ��������� ��� ������� �� ��������� �� ��� ����� ����"},
    {"singleConnected1", "� ��������� \""},
    {"singleConnected2", "\" ����� ��� ������������.\n��� ������������ �������� ��� ��� ��������� �� ����� ��� ����� ��� ��������."},
    {"noProtocolDefined", "��� ���� ������� ���������� ������� ����� ��� ����������"},
    {"incompatible1", "�� ��������� ����� ������ ��� ��� ���������� ��� ��� ������� �� ���������."},
    {"incompatible2", "�������� ������ ���� ���������� �� �� ���� ����� ��� ����������� ������/������."},
    {"noShobjEither", "������ ��� ��� ��� ������� ��� ������� �������������� �����������"},
    {"cantOpen1", "�� ������ "},
    {"cantOpen2", " ��� ������ �� ��������"},
    {"cantCreate1", "�� ������ "},
    {"cantCreate2", " ��� ������ �� ������������"},
    {"loadPage", "�������� �������"},
    {"savePage", "���������� �������"},
    {"saveFailed", "� ���������� �������"},
    {"notArchive", "�� ������ ���� ��� �������� ������������ ������"},
    {"badVersion", "�������� ������ �������"},
    {"required1", "--���������� � ������"},
    {"required2", ""},
    {"loadFailed", "� �������� �������"},
    {"notThis", "�� ������ ���� ��� �������� �� ������� ����������� ����� ��� �������"},
    {"component", "� ������"},
    {"dontHavePlug", "��� ���� �������� �� ��������� �����"},
    {"sltDesc", "������������� �������"},
    {"open", "��������"},
    {"save", "����������"},
    {"plugView", "���������� ���������"},
    {"loadingPage", "�������� �������"},
    {"loadingPleaseWait", "�������� �������--�������� ����������"},
    {"savingPage", "���������� �������"},
    {"savingPleaseWait", "���������� �������--�������� ����������"},
    {"loading", "�������� �������..."},
    {"saving", "���������� �������..."},
    {"pleaseWait", "�������� ����������"},
    {"nullOldName", "�� ����� ����� ����� null"},
    {"nullNewName", "�� ��� ����� ����� null"},
    {"noSuchComponent", "��� ������� ������ �� �����"},
    {"microworldNameUsed", "�� ����� ���� ��������������� ��� ���� ����������"},
    {"microworld", "�����������"},
    {"defaultMicroworldName", "-=+*�����������*+=-"},
    {"folderNotExist1", "� ��������� "},
    {"folderNotExist2", " ��� �������"},
    {"folderNotDir1", "�� ������ "},
    {"folderNotDir2", " ��� ����� ���������"},
    {"cantDeleteInfo", "��� ������� �� ���������� �� ����������� ��� ��� ������� ��� �� ������ ��� �����������"},
    {"notOpenLoad", "��� ����� ������� �� ������ ��� ����������� ��� ��������"},
    {"notOpenSave", "��� ����� ������� �� ������ ��� ����������� ��� �������"},
    {"notExtSerializable", "� ������ ��� ����� ���� Externalizable ���� Serializable"},
    {"connectSound", "connect.wav"},
    {"disconnectSound", "disconnect.au"},
    {"cantconnectSound", "cantconnect.wav"},
    {"failedToLoad", "��� ������� ������ � ������������ ��� ����������� ��� �������� �������:"},
    {"failedToSave", "��� ������� ������ � ���������� ��� ����������� ��� �������� �������:"},
    {"failedToConnect", "���� ��� ������������ ��� ������� �������������� �� �������� ����������:"},
    {"restoringState", "�������� ����������� �����������"},
    {"restoringComponentState", "�������� ����������� �������"},
    {"restoringConnections", "������������ ���������"},
    {"savingState", "���������� ����������� �����������"},
    {"savingComponentState", "���������� ����������� �������"},
    {"copyComponentData", "��������� ��������� �������"},
    {"deleteRedundantComponentData", "�������� �������� ��������� �������"},
    {"noTarget", "��� ���� ������� � ������ ������"},
    {"noTarget2", "� ������� ��������� ��� ������ �� ����� ������"},
    {"noOrig", "��� ���� ������� � ������ ������"},
    {"noOrigPlug", "��� ���� ������� � ������� ���������"},
//    {"noOrigPlug2", "� ������ ������ ��� ���� ����� ��������"},
    {"notInMW", "��� ������� �' ���� �� ����������"},
    {"noHandle", "���� ��� ���� ��� ���������� ��� ������ �� ������ ������"},
    {"differentMicroworlds", "�� ��������� ������� �� ������������� ������������ ��� ��� ������� �� ���������"},
    {"noKey", "��� ������ ������"},
    {"badKey", "�� ������ ��� ������ ����� �����"},
    {"fromESlateHandle", "���� � ������� ������ �� ������ ���� ��� ��� ����� ESlateHandle"},
    {"selectDirToEditHelp", "������ ��� �������� ���� �� �������� �� ������ ��������"},
    {"editAndHitOK1", "������������ �� ������ ���� �������� "},
    {"editAndHitOK2", " ��� ������� \"�������\""},
    {"editHelp", "������������ �� ������ ��������"},
    {"removeHelpDir1", "�� ��������� � ��������� "},
    {"removeHelpDir2", ";"},
    {"removeHelp", "�������� ���������� �������"},
    //
    // Resources for ESlateHandle
    //
    {"error", "������"},
    {"showPlugs", "�������� ���������"},
    {"help", "�������"},
    {"about", "���� ����� ��� �������"},
    {"nullPlug", "� ��������� ����� null"},
    {"aTopPlugNamed", "���� ��������� ������ �������� �� �����"},
    {"aTopPlugInternallyNamed", "���� ��������� ������ �������� �� ��������� �����"},
    {"alreadyAttached", "����� ��� �������������� �' ����� ��� ������"},
    {"doesntHavePlug", "���� � ������ ��� ���� �������� �� �����"},
    {"nameUsed1", "�� ����� "},
    {"nameUsed2", " ��������������� ��� ���� ������"},
    {"noHelp", "��� ������� ��������� ������� ��' ����� ��� ������"},
    {"noHelpSystem", "�� ������� �������� ��� ����� ���������"},
    {"noInfo", "��� �����o�� ����������� ��' ����� ��� ������"},
    {"helpFor", "������� ��� ��� ������"},
    {"nullMicroworld", "� ����������� ��� ������ �� ����� null"},
    {"renamingForbidden", "� ������ �������� ��� ������� ��� ����������� ���� �� ������"},
    {"renamingForbiddenHere", "� ������ ��� �������� ����� ��� ������� ��� ����������� ���� �� ������"},
    {"renamingMWForbidden", "� ������ ��� �������� ��� ����������� ��� ����������� ���� �� ������"},
    {"nullName", "�� ����� ��� ������� ��� ������ �� ����� ���� � null"},
    {"cantContain1", "�� �������� ����� \""},
    {"cantContain2", "\"-�� ������� ��� ����������� �� ��������� ��� ��������� \""},
    {"cantContain3", "\""},
    {"nullHandle", "�� handle ��� ������ �� ����� ����"},
    {"reparentQuery1", "� ������"},
    {"reparentQuery2", "���������� ��� �� ����� ���."},
    {"reparentQuery3", "������ �� ��� ������������ � �� �� ���������� �� ��� ��� ��� ��������� �������;"},
    {"reparentOK", "��������"},
    {"reparentCancel", "����������"},
    {"query", "�������"},
    {"noMicroworldFile", "��� ����� ������� ������ ������ �����������"},
    {"needMWToUnpack", "� ������ ���� ���������� �� ����� ���� ��� ������ ��� ������ ��� �����������, ���� ��� ����� ������� ������ ������ �����������"},
    {"compoNullName", "[������ �� ���� �����]"},
    //
    // Resources for Plug
    //
    {"noListener", "� shared object listener ����� null"},
    {"noSharedObject", "��� ���� ������� ����� ��������������� ������������"},
    {"class", "� �����"},
    {"notShobjSubClass", "��� ����� �������� ��� ������� SharedObject"},
    {"nullProtocol", "�� ���������� ��� ������ ����� ����"},
    {"notInterface", "��� ����� Java interface"},
    {"noComponent", "������ ������ ��� ����� ����������� �' ����� ��� �������� ���� ��� ���������� �����������"},
    {"manyComponents", "������������ ��� ��� ������� ����� ������������ �' ����� ��� �������� ���� ��� ���������� �����������"},
    {"aPlugNamed", "���� ��������� �� �����"},
    {"aPlugInternallyNamed", "���� ��������� �� ��������� �����"},
    {"alreadyAttachedToPlug", "����� ��� �������������� �' ����� ��� ��������"},
    {"noSubPlug", "����� � ��������� ��� ���� ����������� �� �����"},
    {"plug", "� ���������"},
    {"notConnected", "��� ����� ������������ �'����� ��� ��������"}, 
    {"notConnectedToInput", "��� ����� ������������ ��� ����� ������� ����� ��� ���������"},
    {"notConnectedToOutput", "��� ����� ������������ ��� ����� ������ ����� ��� ���������"},
    {"cantConnect1", "� ��������� "},
    {"cantConnect2", " ��� ������ �� �������� ��� �������� "},
    {"onlyIO", "����� �� ����� ������� � ������ ���� ��������� ������ �� �����������"},
    {"badRole", "����������� ������������� �����"},
    {"noImplementor", "� ��������� ��� ���� ��������� �����������"},
    {"nullInterface", "�� interface ��� ������ ����� ����"},
    {"notImplements", "� ���������� ����������� ��� �������� �� interface"},
    {"nullKey", "�� resource bundle key ��� ������ �� ����� null"},
    {"badShObj", "�� �������������� ����������� ������ �� ������ �����"},
    //
    // Resources for MasterControl
    //
    {"MCcomponentName", "������ ��������� �������"},
    {"MCname", "���������_�������"},
    {"part", "����� ��� ���������� \"������\" (http://E-Slate.cti.gr/)"},
    {"design", "���������� & ��������: �. ������� (1998-2000)"},
    {"funding", "�������������: ���� \"IMEL\" (EC DGXXII, Socrates 25136-CP-1-96-1-GR-ODL)"},
    {"copyright", "� ���������� ����������� �����������"},
    {"version", "������"},
    {"MChelpfile", "help/mastercontrol_el.html"},
    //
    // Resources for Console
    //
    {"CcomponentName", "������ �������"},
    {"Cname", "�������"},
    {"Chelpfile", "help/console_el.html"},
    {"saveText", "���������� ��������"},
    {"copyText", "��������� ��������"},
    {"clearText", "�������� ��������"},
    {"logDesc", "�������� ��������"},
    //
    // Resources for EORReachedException
    //
    {"eorText", "� ������ ���������� �� ������� ����������� �������� ��' ��� ����� ���������"},
    //
    // Resources for PlugViewDesktopPane
    //
    {"noActive", "����� ������ ������"},
    {"order", "�����������"},
    //
    // Resources for PlugViewDesktop
    //
    {"prefs", "�����������"},
    {"resizeFrames", "�������� ������� �������� ��������"},
    {"showExist", "������� ���������� ���������"},
    {"showNew", "������� ������� ���������"},
    {"autoOpen", "�������� ������� ������"},
    {"delayedAutoOpen", "�������� ������� ������ �� �����������"},
    {"autoOpenCompatible", "�������� ������� �������� ������"},
    {"tools", "��������"},
    {"edit", "�������"},
    {"undoConnect", "������� �������� ��������� "},
    {"redoConnect", "��������� �������� ��������� "},
    {"undoDisconnect", "������� ����������� ��������� "},
    {"redoDisconnect", "��������� ����������� ��������� "},
    {"fromPlug", " ��� �� �������� "},
    {"toPlug", " ��� �������� "},
    {"undo", "�������"},
    {"redo", "���������"},
    //
    // Resources for PlugViewFrame
    //
    {"disconnect", "����������"},
    {"disconnectAllplugs", "��� ���� ��� �������"},
    {"fromPlug", "���������"},
    {"ofComponent", "��� �������"},
    {"confirm", "�����������"},
    {"confirmDisconnect1", "����� ������� ��� ������ �� ������������ �� ��������"},
    {"confirmDisconnect2", "��� ���� ��� �������;"},
    {"confirmDisconnect3", "��� �� ��������"},
    {"confirmDisconnect4", "��� �������"},
    {"confirmDisconnect5", ";"}
  };
}
