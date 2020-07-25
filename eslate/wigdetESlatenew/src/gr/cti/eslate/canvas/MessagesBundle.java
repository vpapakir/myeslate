package gr.cti.eslate.canvas;

import java.util.ListResourceBundle;

/**
 * @version     2.0.9, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Development: G. Birbilis, N. Drossos, K. Kyrimis",
                                "Contribution: M. Koutlis, Ch. Kynigos, A. Oikonomou",
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
     {"name", "Canvas"},
     {"title",    "Canvas Component, version "},
     {"colorpin", "Colors"},
     {"logopin",  "Turtle Commands"},
     {"imagepin", "Image"},
     {"turtlepin", "Turtles"},
     {"sliderpin", "Slider"},
     //{"����������", "Slider"},
     {"logocallinput", "Logo procedure execution notification"},
     //{"���������� ��� �������� �������� LOGO", "Logo procedure execution notification"},
     //
     {"ColorChooser","Color Chooser"},
     {"TurtleLineChooser","Turtle Line Chooser"},
     {"CanonicalPolygon","Canonical Polygon"},
     //
     {"Page","Pages stack"},
     //
     {"New","New Painting page"},
     {"Open","Load image"},
     {"Close","Close page"},
     {"Save","Save image"},
     //
     {"newpage","Painting"}, //24-12-1998: new pages are now called Painting1, Painting2, Painting3, etc.
     {"newturtlepage","Turtles"},
     {"warning","Warning!!!"},
     {"lastchangesnotsaved","Your last changes have not been saved yet"},
     {"savefile","Save the file"},
     {"questionmark","?"},
     //
	};

}
