package gr.cti.eslate.logo;

import java.util.ListResourceBundle;

/**
 * Logo component.
 *
 * @version     2.0.6, 23-Jan-2008
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K. Kyrimis
 */
public class MessagesBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"����� ��� ������������� ������ (http://E-Slate.cti.gr)",
                                "����������� ����: �. �������, �. �������",
                                "��������: �. ����������, �. ��������, �. ������, �. �������",
                                "��������� ���� TurtleTracks",
                                "(http://www.ugcs.caltech.edu/~dazuma/turtle/index.html)",
                                "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.",
                                "�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.",
                                "�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.",
                                " ",
                                "����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������",
                                "���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������",
                                "��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."
                               };

    static final Object[][] contents={
     {"info", info},
     //
     {"title",    "������ Logo, ������ "},
     {"logopin",  "����������� �������"},
     {"commandpin", "�������/����������"},
     {"logocallpin", "��������� ��� �������"},
     //{"��������� ��� �������", "��������� ��� �������"},
     //Dialog Titles//
     {"Open text file","������� ������� ��������"},
     {"Save text file","���������� ������� ��������"},
     {"Load machine state","������� ���������� �������"},
     {"Save machine state","���������� ���������� �������"},
     //MenuBar//
     {"File","������"},
     {"Edit","�����������"},
     {"LOGO Machine","������ LOGO"},
     //File menu//
     {"New",   "��o"},
     {"Open Text",  "������� ��������"},
     {"Save Text",  "���������� ��������"},
     {"Print", "��������"},
     //Edit menu//
     {"Cut",        "�������"},
     {"Copy",       "���������"},
     {"Paste",      "����������"},
     {"Clear",      "����������"},
     {"Select All", "������� ����"},
     {"Read","��������"},
     //Search menu//
     {"Search",     "���������"},
     {"Find",       "������"},
     {"Find Next",  "������ ��������"},
     {"Replace",    "�������������"},
     {"Go To Line", "������ �������"},
     //Machine menu//
     {"Load State", "������� ����������"},
     {"Save State", "���������� ����������"},
     {"Reset Environment", "���������������� �������������"},
     {"Try to unfreeze","���������� �����������"},
     {"Run","��������"},
     {"Pause","�����"},
     {"Stop","���������"},
     //
     {"ConsoleInputing", "��������: ������ ��� ������ �������/���������..."},
     {"ConsoleExecuting", "������������: ������� �������..."},
     {"machinePin", "������ LOGO"},
     //Editor//
     {"Program", "���������"},
     {"Output", "������"},
     {"ConstructorTimer",      "��������� Logo"},
     {"LoadTimer",             "�������� Logo"},
     {"SaveTimer",             "���������� Logo"},
     {"InitESlateAspectTimer", "��������� ��������� ������� Logo"},
     //
     {"Font", "�������������"},
     {"Font size", "������� ��������������"},
     {"Bold", "������"},
     {"Italic", "������"},
     {"B", "�"},
     {"I", "�"},

        };
}
