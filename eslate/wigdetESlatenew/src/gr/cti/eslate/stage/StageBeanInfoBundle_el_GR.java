package gr.cti.eslate.stage;

import java.util.ListResourceBundle;

/**
 * Greek language resources for the stage component bean info
 *
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 * @version     2.0.9, 21-Nov-2006
 */
public class StageBeanInfoBundle_el_GR extends ListResourceBundle {
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

        {"keyPressed", "������ ��������"},
        {"keyReleased", "�������� ��������"},
        {"keyTyped", "�������� ��������"},

        {"menuBarVisible",        "������ ����� �����"},
        {"menuBarVisibleTip",     "������� ��� ���������� ��� ������� �����"},
        {"toolBarVisible",        "������ ��������� �����"},
        {"toolBarVisibleTip",     "������� ��� ���������� ��� ������� ���������"},
        {"border",                "����������"},
        {"borderTip",             "������� ��� ���������� ��� �������������"},

        {"objectMovementEnabled", "����������� ������ ���� ���������"},
        {"objectMovementEnabledTip", "��������� ��� ����������� �� ��������� ���� ���������"},

        {"controlPointMovementEnabled", "������ ������ ������ ���� ���������"},
        {"controlPointMovementEnabledTip", "��������� ��� ������ ������� �� ��������� ���� ���������"},

        {"viewMovementEnabled", "��� ������ ���� ���������"},
        {"viewMovementEnabledTip", "��������� ��� ���������� ��� ���� ��� ������ ���� ���������"},

        {"objectsAdjustable", "����������� ������������ ���� ���������"},
        {"objectsAdjustableTip", "��������� �� ���������� ������������ ���� ���������"},

        {"coordinatesVisible", "������������� ������"},
        {"coordinatesVisibleTip", "������� ��� ���������� ��� �������������"},

        {"gridVisible", "������ �����"},
        {"gridVisibleTip", "������� ��� ���������� ��� ���������"},

        {"gridSize", "������� ���������"},
        {"gridSizeTip", "������� ��� �������� ��� ���������"},

        {"axisVisible", "������ ������"},
        {"axisVisibleTip", "������� ��� ���������� ��� ������"},

        {"controlPointsVisible", "������ ������� �����"}, //12May2000
        {"controlPointsVisibleTip", "������� ��� ���������� ��� ������� �������"}, //12May2000

        {"marksOverShapes", "������ & ������ ���� ��� �������"},
        {"marksOverShapesTip", "��������� �� ������ ��� ������ �� ���������� ���� ��� �������"},

        {"image","������"} //12May2000

    };
}

