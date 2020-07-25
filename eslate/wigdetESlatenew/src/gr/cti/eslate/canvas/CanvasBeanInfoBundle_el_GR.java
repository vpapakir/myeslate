package gr.cti.eslate.canvas;

import java.util.ListResourceBundle;


/**
 * @version     2.0.7, 14-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class CanvasBeanInfoBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"mouseEntered",    "������� ��������"},
        {"mouseExited",     "������ ��������"},
        {"mouseMoved",      "���������� ��������"},

        {"propertyChange",  "������ ���������"},
        {"vetoableChange",  "��������� ������ ���������"},

        {"componentHidden", "�������� �������"},
        {"componentShown",  "�������� �������"},

        {"menuBarVisible",        "������ ����� �����"},
        {"toolBarVisible",        "������ ��������� �����"},

        {"menuBarVisibleTip",     "������� ��� ���������� ��� ������� �����"},
        {"toolBarVisibleTip",     "������� ��� ���������� ��� ������� ���������"},
        {"border",                "����������"},
        {"borderTip",             "������� ��� ���������� ��� �������������"},

        {"fixedTurtlePageSizes", "������� ������ ������� �������"},
        {"fixedTurtlePageSizesTip", "������ �� �� ������ ��� ������� ������� �� ����� �������"},

        {"foregroundColor", "����� ��������"},
        {"backgroundColorTip", "������� �������� �����"},
        {"foregroundColorTip", "������� �������� ��������"},
        {"backgroundColor", "����� �����"}

    };
}

