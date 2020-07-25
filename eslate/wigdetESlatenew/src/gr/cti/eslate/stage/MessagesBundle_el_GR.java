package gr.cti.eslate.stage;

import java.util.ListResourceBundle;

/**
 * @version     2.0.12, 23-Jan-2008
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MessagesBundle_el_GR extends ListResourceBundle
{
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"needsJava2","���� � ������ ���������� �������� ������ Java ������� �� �� Java2"},
     {"info", new String[]
               {"����� ��� ������������� ������ (http://E-Slate.cti.gr)",
                "����������� ����: �. �������, �. ����������",
                "��������: �. ����������, ����� ������, ������ �������",
                "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.",
                "�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.",
                "�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.",
                " ",
                "����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������",
                "���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������",
                "��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."
                }
     },
     //
     {"Stage", "�����"}, //� �����;
     {"title",  "������ �����, ������ "},
     {"Vector","��������"},

// Localization of Menus //////////////////////////////////////////////////////

     {"File","������"},
     {"New",   "���"},
     {"Load...",  "�������..."},
//     {"Close", "��������"},
     {"Save...",  "����������..."},
     {"Print...", "��������..."},
     {"Photo...","�����������..."}, //9May2000

     ////

     {"Edit","�����������"}, //26-11-1998
     {"New object", "��� �����������"}, //29Nov1999
     {"Cut",   "�������"},
     {"Copy",  "���������"},
     {"Paste", "����������"},
     {"Clear", "����������"},
     {"Select All", "������� ����"},
     {"Area Selection","������� ��������"}, //26Apr2000
     {"Select","�������"}, //19May2000
     {"Deselect","����������"}, //19May2000
     {"Clone","�����������"}, //13Apr2000

     ////

     {"View","�������"},
     {"Axis","������"},
     {"Grid","������"},
     {"Coordinates","�������������"},
     {"Control Points","������ �������"},
     {"Mapping","����������"},
     {"Cartesian","����������"},
     {"Polar","������"},
     {"Dots","�������"},
     {"Lines","�������"},
     {"Zoom In","���������"},
     {"Zoom Out", "���������"},

     ////

     {"Insert","��������"},
     {"Scene", "�������"}, //the document object's name (used in scripting)
     {"ControlPoint", "�������������"}, //used in scripting
     {"RoundBox","��������������"}, //used in scripting
     {"SquareBox","��������������"}, //used in scripting
     {"pLine", "����������������"}, //used in scripting
     {"quadCurve", "��������������������"}, //used in scripting
     {"cubicCurve", "������������������"}, //used in scripting

     {"Box","�����"}, //used in scripting too
     {"Ball","�����"}, //used in scripting too
     {"Spring","��������"}, //used in scripting too
     {"Slope","���������"}, //used in scripting too

     {"Rope","������"}, //to be used in scripting too...
     {"Round Box","������������� �����"},
     {"Square Box","��������� �����"},
     {"PolyLine","���������� ������"},
     //{"Quadratic Curve","������� �� 1 ������ �������"},
     //{"Cubic Curve","������� �� 2 ������ �������"},
     {"Quadratic Curve","������������� �������"},
     {"Cubic Curve","����������� �������"},

     //

//     {"Tool","��������"},
//     {"Settings","���������"},

////////////////////////////////////////////////////////////////////////

     {"Error", "������"},
     {"BindPointPoint", "������� ������-������"},
     {"BindPointShape", "������� ������-�����"},
     {"Split", "�����������"},
     {"Need2Points", "����������� ����������� ��� ���������� ������ ��� �������"},
     {"Need1ShapeNPoints", "���������� ��� ���������� ����� ��� ����������� ��� ������ ��� �������"},
     {"badConstraint", "��� ������� �� ������������ & ������������� � ����������� �����"},

     {"badGridSize", "�� ������� ��� ��������� ������ �� ����� ������"},

// Localization of Customizers //////////////////////////////////////////////////////
     {"Customization","���������"},
     {"OK","�������"},
     {"Cancel","�������"},

     {"Object","�����������"},
     {"Scene Object","������� �����������"},
     {"Name","�����"},
     {"Location","����"},
     {"Color","�����"},
     {"Image","������"},

     {"Change","������"},

     {"Physics Object","������ �����������"},
     {"Mass","����"},
     {"Velocity","��������"},
     {"Acceleration","����������"},
     {"Applied Force","��������� ������"},
     {"Kinetic Energy","�������� ��������"},
     {"Altitude","��������"},

     {"Spring Constant", "������� ���������"},
     {"Length","�����"},
     {"Natural Length", "������ �����"},
     {"Displacement", "�����������"},

     {"Angle","�����"},

     {"Radius","������"}, //17May2000
     {"Diameter","���������"}, //17May2000

     {"Width","������"},
     {"Height","����"},

     //

     {"Bring To Front", "���� ��� ���������"},
     {"Send To Back", "������ ��� ��������"},
     {"Properties...","���������..."},

     {"Error!", "������!"}, //10May2000
     {"Failed to print", "� �������� �������"}, //10May2000

     {"loadSceneMsg", "������� ��������"}, //16May2000 //7Jun2000: ���� ���� "��������" ���� "������"
     {"saveSceneMsg", "���������� ��������"}, //16May2000 //7Jun2000: ���� ���� "��������" ���� "������"

     //

     {"LoadTimer",             "�������� ������"},
     {"SaveTimer",             "���������� ������"},

        };
}
