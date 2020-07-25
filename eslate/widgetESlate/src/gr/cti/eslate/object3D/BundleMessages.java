/*
 * Created on 6 ���� 2006
 *
 */
package gr.cti.eslate.object3D;

import java.util.ListResourceBundle;

public class BundleMessages extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
        
            {"PitchAngle", "Pitch angle"},
            {"YawAngle",   "Yaw angle"},
            {"RollAngle",   "Roll angle"},
            {"XPos", "Position X"},
            {"YPos",   "Position Y"},
            {"ZPos", "Position Z"},
            {"ScaleX", "Scale X"},
            {"ScaleY", "Scale Y"},
            {"ScaleZ", "Scale Z"},
            {"Object3D", "Object3D"},
            {"3DModelFilePath","Model file path"},
        
            {"Title", "Title"},
            // component info
            {"componentName", "Object3D component"},
            {"name", "Object3D"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            //{"funding", "� Computer Technology Institute"},
            {"copyright", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
            {"version", "version"},
            //Bean Info
            {"ProviderCase", "This plug has provider/dependent plugs connected to it. Do you wish to proceed ?"},
            {"DependentCase", "This plug has dependent plugs connected to it. Do you wish to proceed ?"},
            {"PlugDisconnection", "Plug Disconnection"},
            {"Object3D", "Object3D"},
            
            {"Visible", "Visible"},
            {"setVisibleTip", "Determines if this Object3D is visible inside a Viewer3D component"},
            
            {"Position", "Position"},
            {"Orientation", "Orientation"},
            {"Scale", "Scale"},
            {"PositionTip", "Sets the object's position"},
            {"OrientationTip", "Sets the object's orientation"},
            {"ScaleTip", "Sets the object's scale"},
            

        };

}

