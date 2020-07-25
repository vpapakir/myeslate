/*
 * Created on 6 ���� 2006
 *
 */
package gr.cti.eslate.scene3d;

import java.util.ListResourceBundle;

public class BundleMessages extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
        {"HorizontalAngle", "Horizontal angle"},
        {"VerticalAngle",   "Vertical angle"},
        {"XPos", "Position X"},
        {"YPos", "Position Y"},
        {"ZPos", "Position Z"},
        
        {"XDim", "Dimension X"},
        {"YDim", "Dimension Y"},
        {"ZDim", "Dimension Z"},
        { "XScale", "Scale �" },
        { "YScale", "Scale �" },
        { "ZScale", "Scale �" },
        {"Viewer3D", "Viewer3D"},
        {"PlugsUsed"," PlugsUsed"},
        { "PitchAngle", "Pitch angle" },
        { "YawAngle", "Yaw angle" },
        { "RollAngle", "Roll angle" },
        
        {"Title", "Title"},
        {"NumberValue", "Numeric value"},
        {"Pressed", "Boolean"},
        // component info
        {"componentName", "Text field component"},
        {"name", "Viewer3D"},
        {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
        {"development", "Development : Th. Mantes"},
        //{"funding", "� Computer Technology Institute"},
        {"copyright", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
        {"version", "version"},
        //Bean Info
        {"ProviderCase", "This plug has provider/dependent plugs connected to it. Do you wish to proceed ?"},
        {"DependentCase", "This plug has dependent plugs connected to it. Do you wish to proceed ?"},
        {"PlugDisconnection", "Plug Disconnection"},
        {"CartesianHelperVisible", "Cartesian Helper visible"},
        {"CartesianHelperVisibleTip", "Shows/hides the reference cartesian helper renderings"},
        {"CameraPosition", "Camera position"},
        {"CameraOrientation", "Camera orientation"},
        {"CameraPositionTip", "Sets the camera's position"},
        {"CameraOrientationTip", "Sets the camera's orientation"},
        {"BoundingBoxDimensions", "BoundingBoxDimensions"},
        {"BoundingBoxDimensionsTip", "Sets the bounding box dimensions"},
        {"PositionChanged", "Camera position changed"},
        {"OrientationChanged", "Camera orientation changed"},
        
        };

}

