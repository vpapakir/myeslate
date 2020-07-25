//This �ranslati�n was adapted from a technical report about
//the translation of the terappin Logo

//5-2-1999: moved many primitives to Turtle Component's primitives' localization bundle
//20Sep1999: moved TELL/ASK/EACH to ComponentPrimitives localization bundle
//28Jun2000: added again "TO" and "END" and also added "TOMACRO" 

package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;

public class PrimitivesBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     
{"ABS","��"},
{"AGET","�����"},
{"ARRAY","�������"},
{"ARRAY?","�������?"},
{"ARRAYDIMS","�����������"},
{"ASET","���"},
{"BITXOR","���"},

{"BL","��"},
{"BUTFIRST","�����������"},
{"BUTLAST","���������������"},
{"BUTTON?","������?"},

{"CHAR","����������"},

{"FILLARRAY","�������"},
{"LISTARRAY","�������������"},
{"AND","���"},
{"ARCTAN","�����"},

{"BEFORE?","����?"},
{"BF","��"},
{"BITAND","����"},
{"BITOR","��"},
{"CLEARKEYS","������������"},
{"CLEARTEXT","������������"},
{"CONTINUE","��������"},
{"COUNT","�������"},
{"COPYDEF","��������"},
{"COS","�����"},
{"DATE","�����"},
{"DEBUG","��������"},
{"DEFINE","�����"},

{"DIVIDE","���"},
{"DOT?","������?"},
//11-9-1998:wrote my own "�����" primitive implementation// {"EDIT","�����"},
{"EDALL","�����"},
{"EDNAME","�������"},
{"EDPROPS","����������"},

{"EJECT","�������"},
{"EMPTY?","�����"},

{"END","�����"}, //28Jun2000: removed from hard-coded

{"EQUAL?","���?"},
{"ERALL","������������"},
{"ERASE","�����"},
{"ER","��"},
{"ERASEFILE","�����������"},
{"ERNAME","����������"},

{"ERP","���"}, //28-8-1998
{"ERASEPROCEDURE","���������������"}, //28-8-1998

{"ERPROPS","����������"},
{"ERROR","�����"},
{"EXP","���"},

{"FIRST","�����"},
{"FORWARD","�������"},
{"FPRINT","��������"},
{"FPUT","���������"},
{"GO","�������"},
{"GOODBYE","�������������"},
{"GPROP","������"},

{"INRECT?","�������?"},
{"IF","��"},
{"INTEGER","��������"},
{"ITEM","�����������"},
{"KEYSDOWN","�������"},
{"LAST","���������"},

{"LIST","�����"},
{"LIST?","�����?"},
{"LOWERCASE","����"},
{"LOAD","�������"},

{"LOADPICT","�������������"}, //Could be implemented by the Canvas or TV component
{"LOADTEXT","��������������"}, //Could be implemented by the LOGO component

{"LOCAL","������"},
{"LPUT","�������������"},
{"MAKE", "������"}, //31May2000: new greek name so that it doesn't conflict with SET or DO primitives' greek names
{"MEMBER?","������?"},
{"MINUS","�����"},
{"MOUSE","�������"},
{"NAMES","�������"},
{"ONLINE","���������"},
{"NODEBUG","�����������"},
{"NOT","���"},
{"NUMBER?","�������?"},
{"OP","��"},
{"OR","�"},
{"OUTPUT","������"},
{"PARSE","�����������"},
{"PAUSE","�����"}, //Could be implemented by the LOGO component

{"PI","��"},
{"PLIST","���������"},
{"PLUS","���"},

{"POALL","�������������"},
{"PONAME","��������"},
{"POPROPS","�����������"},
{"PPPATT�RN","�����������"},
{"POWER","������"},
{"PR","���"},
{"PREFIX","�������"},
{"PRINTOUT","��������"},
{"PROCEDURE?","����������?"},
{"PROCEDURES","�����������?"},
{"PRINT","������"},
{"PUTPROP","���������"},

{"PX","��"},
{"QUOTIENT","������"},
{"RANDOM","������"},
{"RC?","�����������������?"},
{"RC","����"},
{"READCHARACTER","����������������"},
{"READLIST","������������"},
{"RL","��"},
{"READSTRING","����������������"},
{"REMAINDER","��������"},
{"REMPROP","�������"},
{"RENAMEFILE","����������������"},
{"REPEAT","���������"},
{"ROUND","���������������"},

{"RUN","�����"}, //Could be implemented by the LOGO component
{"SAVE","����������"},
{"SAVEPICT","�����������������"},
{"SAVETEXT","������������������"},

{"SE","��"},
{"SENTENCE","�������"},

{"SETPREFIX","�����������"},
{"SETRANDOM","����������"},

{"SETSHAPE","���������"},
{"SETTFONT","�����������������"},
{"SETTSTYLE","��������"},
{"SETTWINDOW","��������������������"},

{"SHAPE","�����"},

{"SIN","��"},
{"SQRT","����"},

{"STAMP","��������"}, //should move to Canvas component's primitive bundle

{"STRING","������������"},
{"STRING?","������������?"},

{"STOP","���������"}, //Could be implemented by the LOGO component

{"TAN","��"},

{"TEXT","�������"},

{"THING","��������"},
{"THING?","��������?"},
{"TIMES","���"},
{"TIME","���"},

{"TO","���"}, //28Jun2000: removed from hard-coded
{"TOMACRO","��������"}, //28Jun2000: removed from hard-coded

{"TSTYLE","������������"},
{"TFONT","���������������"},

{"TWINDOW","�������������"},
{"UPPERCASE","��������"},
{"WAIT","��������"},
{"WHO","�����"},
{"WRAP","����������"},
{"WORD","����"},
{"WORD?","����?"},

{"REPCOUNT","�����������"}, //Birb

{"PRINTOUTPROCEDURE","����������������"}, //28-8-1998
{"POP","���"} //28-8-1998

   };

}
