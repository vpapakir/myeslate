//Title:        Stage
//Version:      2Doc1999
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  base constraints' localization service

package gr.cti.eslate.stage.constraints.base;

import java.util.ListResourceBundle;

public class MessagesBundle_el_GR extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"Unknown","<�������� �����������>"},
     {"cantEnforce","��� ������� �� ������������� � �����������"},
     {"notAllowed","��� ����������� ���� ��� ����������� "}, //have a space at the end
     //
     {"UnknownPointPoint","<�������� ����������� ����� ������-������>"},
     {"need2points","���� ����������� ����� ������-������ ���������� ��� ������ �������"},
     //
     {"UnknownMasterSlave","<�������� ����������� ����� �������-�������>"},
     {"need2members","���� ����������� ����� �������-������� ���������� ��� ����"}
        };

}

