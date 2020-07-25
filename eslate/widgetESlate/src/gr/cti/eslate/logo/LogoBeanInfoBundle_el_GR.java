package gr.cti.eslate.logo;

import java.util.ListResourceBundle;

/**
 * Greek language resources for Logo component's bean info.
 * @version     2.0.0, 24-May-2006
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K.Kyrimis
 */
public class LogoBeanInfoBundle_el_GR extends ListResourceBundle {
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
        {"statusBarVisible",      "������ ���������� �����"},

        {"menuBarVisibleTip",     "������� ��� ���������� ��� ������� �����"},
        {"toolBarVisibleTip",     "������� ��� ���������� ��� ������� ���������"},
        {"statusBarVisibleTip",   "������� ��� ���������� ��� ������� ����������"},
        {"border",                "����������"},
        {"borderTip",             "������� ��� ���������� ��� �������������"},


        {"hasFontSelector", "�������� ��������� �������������� ��� ������ ���������"},
        {"execQueueMaxSize", "������� ������� ����� ������� (-1=�����������)"},
        {"hasFontSelectorTip", "�������� ��������� �������������� ��� ������ ���������"},
        {"execQueueMaxSizeTip", "������� ������� ����� ������� (-1=�����������)"},

        {"lineNumbersVisibleInProgramArea", "�������� ������� ������� ���� ������� ������������"},
        {"lineNumbersVisibleInProgramAreaTip", "�������� ������� ������� ���� ������� ������������"},

        {"lineNumbersVisibleInOutputArea", "�������� ������� ������� ���� ������� ������"},
        {"lineNumbersVisibleInOutputAreaTip", "�������� ������� ������� ���� ������� ������"}
    };
}

