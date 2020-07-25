package gr.cti.eslate.palette;

import java.util.ListResourceBundle;

/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class MessagesBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Development: G. Birbilis, K. Kyrimis",
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
     {"name", "Palette"},
     {"title", "Palette component, version "},
     {"pin",   "Colors"},
     //
     {"color",     "Color: "},
     {"fgrColor",  "drawing"},
     {"fillColor", "filling"},
     {"bkgrColor", "wiping"}
	};
}
