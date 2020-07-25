package gr.cti.eslate.palette;

import java.util.ListResourceBundle;

/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class MessagesBundle_el_GR extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }
    
    static final String[] info={"����� ��� ������������� ������ (http://E-Slate.cti.gr)",
                                "��������: �. ����������, �. �������",
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
     {"name", "�������"}, //29Jun2000: fixed the "key" - it must have had some strange character encoding
     {"title", "������ �������, ������ "},
     {"pin",   "�������"},
     //
     {"color",     "�����: "},
     {"fgrColor",  "��������"},
     {"fillColor", "������"},
     {"bkgrColor", "�������"}
 	};
}
