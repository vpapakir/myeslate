package gr.cti.eslate.planar;

import java.util.ListResourceBundle;

/**
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Kriton Kyrimis"
 * @version     2.0.1, 23-Jan-2008
 */
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: Ch. Kynigos",
                                "Development: G. Birbilis, A. Oikonomou, K.  Kyrimis",
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
     {"title",   "2D Variation Tool component, version "},

     {"Planar", "2D Variation Tool"},
     {"Vector","Vector"},
     {"From","From"},
     {"To","To"},
     //
     {"File","File"},
     {"Edit","Edit"}, //26-11-1998
//     {"Tool","��������"},
     {"View","View"},
//     {"Settings","���������"},
     //////
     {"New",   "New"},
     {"Open",  "Open"},
//     {"Close", "��������"},
//     {"Save",  "����������"},
     {"Print", "Print"},
     //////
     {"Cut",   "Cut"},
     {"Copy",  "Copy"},
     {"Paste", "Paste"},
     {"Clear", "Clear"},
     {"Select All", "Select All"},
     //
     {"Axis","Axis"},
     {"Grid","Grid"},
     {"Control Points","Control Points"},
     {"Mapping","Mapping"},
     {"Cartesian","Cartesian"},
     {"Polar","Polar"},
     {"Dots","Dots"},
     {"Lines","Lines"}
	};
}
