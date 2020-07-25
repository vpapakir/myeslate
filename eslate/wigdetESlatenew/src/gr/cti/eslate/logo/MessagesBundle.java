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
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: M. Koutlis, Ch Kynigos",
                                "Development: G. Birbilis, G. Tsironis, N. Drossos, K. Kyrimis",
                                "Based on TurtleTracks",
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
     {"title",    "Logo component, version "},
     {"logopin", "Primitives"},
     {"commandpin", "Commands/Slider"},
     {"logocallpin", "Calls Notification"},
     //{"��������� ��� �������", "Calls Notification"},
     //
     {"ConsoleInputing", "idle: ready for commands/data input..."},
     {"ConsoleExecuting", "busy: executing program..."},
     {"machinePin", "LOGO machine"},
     {"ConstructorTimer",      "Logo constructor"},
     {"LoadTimer",             "Logo load"},
     {"SaveTimer",             "Logo save"},
     {"InitESlateAspectTimer", "Logo E-Slate aspect creation"},

    };
}
