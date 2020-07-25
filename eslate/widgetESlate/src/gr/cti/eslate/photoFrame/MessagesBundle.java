package gr.cti.eslate.photoFrame;


import java.util.ListResourceBundle;


public class MessagesBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"componame", "Picture Frame"},
            {"pin", "Picture filename"},
            {"imagepin", "Picture"},
            {"compo", "Photo Frame component, version"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
            {"development", "Development: G. Vasiliou"},
            {"copyright", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
            {"ConstructorTimer", "PhotoFrame constructor"},
            {"LoadTimer", "PhotoFrame load"},
            {"SaveTimer", "PhotoFrame save"},
            {"InitESlateAspectTimer", "PhotoFrame E-Slate aspect creation"},
            
            {"actorName", "Picture Frame"},
            {"x", "X"},
            {"y", "Y"},
            {"width", "Width"},
            {"height", "Height"},
            {"animationPlug", "Animation"},
        };
}
