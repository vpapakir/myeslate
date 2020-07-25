package gr.cti.eslate.slider;

import java.util.ListResourceBundle;

/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: Ch. Kynigos",
                                "Development: G. Birbilis, A. Oikonomou, N.  Drossos, K. Kyrimis",
                                "Contribution: M. Koutlis",
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
     {"Slider", "Variation Tool"}, //28-8-1998
     {"title",   "Variation Tool component, version "},
     {"turtlePage", "LOGO calls notification"},
     //{"��������� ��� LOGO �������", "LOGO calls notification"},
     {"logopin", "LOGO commands"},
     //{"������� LOGO", "LOGO commands"},
     {"Vector","Vector"},
     //{"��������","Vector"}, //29-9-1998
     //
     {"Procedure","Procedure"},
     {"Track","Track"},
     {"Grid","Grid"},
     {"From","From"},
     {"To","To"},
     //{"���","From"},
     //{"To","�����"},
     //{"�����","To"},
     {"Step","Step"},
     {"Var","Var"},
     {"2D","2D"} //6Jul1999
     //
//     {"Yes","Yes"},
//     {"No","No"}
	};
}
